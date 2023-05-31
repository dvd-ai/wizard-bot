package com.learngerman.wizardbot.command;

import com.learngerman.wizardbot.command.channel.ChannelSettingsCommand;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.CommandUtils.*;

@Component
public class MessageCommandManager {

    private final HelpCommand helpCommand;
    private final CurrencyCommand currencyCommand;
    private final ChannelSettingsCommand channelSettingsCommand;
    private final AddCommand addCommand;
    private final NonexistentCommand nonexistentCommand;

    //error handler

    public MessageCommandManager(HelpCommand helpCommand, CurrencyCommand currencyCommand, ChannelSettingsCommand channelSettingsCommand, AddCommand addCommand, NonexistentCommand nonexistentCommand) {
        this.helpCommand = helpCommand;
        this.currencyCommand = currencyCommand;
        this.channelSettingsCommand = channelSettingsCommand;
        this.addCommand = addCommand;
        this.nonexistentCommand = nonexistentCommand;
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
            return switch (commandParts.get(0)) {
                case "help" -> helpCommand.process(message, getNextCommandPartsToParse(commandParts));
                case "currency" -> currencyCommand.process(message, getNextCommandPartsToParse(commandParts));
                case "add" -> addCommand.process(message, getNextCommandPartsToParse(commandParts));
                case "channel-settings" ->
                        channelSettingsCommand.process(message, getNextCommandPartsToParse(commandParts));
                default -> nonexistentCommand.process(message, null);
            };
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            //process error and return error message
            return Mono.empty();
        }

    }
}