package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.learngerman.wizardbot.command.CommandUtils.*;

@Component
public class MessageCommandManager {
    private final NonexistentCommand nonexistentCommand;
    private final Map<String, Command> commands;

    //error handler

    public MessageCommandManager(NonexistentCommand nonexistentCommand, ApplicationContext applicationContext) {
        this.nonexistentCommand = nonexistentCommand;
        this.commands = applicationContext.getBeansOfType(Command.class);
    }

    public Mono<Void> processCommand(Message eventMessage) {
        return Mono.just(eventMessage)
                .flatMap(this::makeResponse)
                .onErrorComplete()
                .then();
    }


    private Mono<Object> makeResponse(Message message) {

        String polishedCommand = polishCommand(message.getContent());
        List<String> commandParts = extractCommandParts(polishedCommand);

        try {

            for (Command command : commands.values()) {
                if (command.getName().equals(commandParts.get(0))) {
                    return command.process(message, getNextCommandPartsToParse(commandParts));
                }
            }
            return nonexistentCommand.process(message, null);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            //process error and return error message
            return Mono.empty();
        }

    }
}