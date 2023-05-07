package com.learngerman.wizardbot.command;

import com.learngerman.wizardbot.event.MessageValidator;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateMono;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MessageCommandManager {

    private final HelpCommand helpCommand;
    private final NonexistentCommand nonexistentCommand;
    private final MessageValidator messageValidator;

    public MessageCommandManager(HelpCommand helpCommand, NonexistentCommand nonexistentCommand, MessageValidator messageValidator) {
        this.helpCommand = helpCommand;
        this.nonexistentCommand = nonexistentCommand;
        this.messageValidator = messageValidator;
    }

    public Mono<Void> processCommand(Message eventMessage) {
        return Mono.just(eventMessage)
                .filter(messageValidator::validate)
                .flatMap(this::makeResponse)
                .then();
    }


    private MessageCreateMono makeResponse(Message message) {

        String polishedContent = polishContent(message.getContent());
        String goal = defineCommandGoal(polishedContent);

        switch (goal) {
            case "help":
                return helpCommand.process(message, goal);
            default:
                return nonexistentCommand.process(message, null);
        }
    }

    private String defineCommandGoal(String polishedContent) {
        return polishedContent.split(" ")[0];
    }

    private String polishContent(String content) {
        return content.toLowerCase().replaceFirst(">", "");
    }

}