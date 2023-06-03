package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.learngerman.wizardbot.Wizard.PREFIX;
import static com.learngerman.wizardbot.command.CommandName.HELP_COMMAND_NAME;
import static com.learngerman.wizardbot.error.ErrorDescription.WRONG_FLAG_ERROR;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUsualMessage;

@Component
public class HelpCommand implements Command {

    private final Map<String, Command> commands;
    private final NonexistentCommand nonexistentCommand;

    public HelpCommand(ApplicationContext applicationContext, NonexistentCommand nonexistentCommand) {
        commands = applicationContext.getBeansOfType(Command.class);
        this.nonexistentCommand = nonexistentCommand;
    }

    @Override
    public String getCommandDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("The list of all available commands:\n\n");

        for (Command command : commands.values()) {
            sb.append(command.getCommandDescription()).append("\n\n");
        }

        sb.append("Type **" + PREFIX + HELP_COMMAND_NAME + " [command]** to get info how to use a specific command from the list.\n");
        sb.append("Most of them use flags and parameters.");
        return sb.toString();
    }

    @Override
    public String getFlagsDescription() {
        return getCommandDescription();
    }

    @Override
    public String getName() {
        return HELP_COMMAND_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> flags) {
        if (flags.isEmpty())
            return processWithNoParametersOrFlags(message);

        for (Command command : commands.values()) {
            if (command.getName().equals(flags.get(0))) {
                String flagsDescription = command.getFlagsDescription();
                return sendCommandDescriptionMessage(message, flagsDescription, command.getName());
            }
        }
        return nonexistentCommand.process(message, WRONG_FLAG_ERROR);
    }

    private Mono<Object> sendCommandDescriptionMessage(Message message, String description, String commandName) {
        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(buildUsualMessage("Command '" + commandName + "'", description)));
    }

    private Mono<Object> processWithNoParametersOrFlags(Message message) {
        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(buildUsualMessage("Help", getCommandDescription())));
    }
}
