package com.learngerman.wizardbot.command.channel;

import com.learngerman.wizardbot.command.Command;
import com.learngerman.wizardbot.command.NonexistentCommand;
import discord4j.core.object.entity.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.learngerman.wizardbot.command.CommandName.CHANNEL_SETTINGS_COMMAND_NAME;
import static com.learngerman.wizardbot.command.CommandName.COMMAND_NOTE;
import static com.learngerman.wizardbot.command.CommandUtils.getNextCommandPartsToParse;

@Component
public class ChannelSettingsCommand implements Command {
    private final NonexistentCommand nonexistentCommand;

    private final Map<String, ChannelSettingsFlag> channelSettingsFlags;

    public ChannelSettingsCommand(NonexistentCommand nonexistentCommand, ApplicationContext applicationContext) {

        this.nonexistentCommand = nonexistentCommand;
        this.channelSettingsFlags = applicationContext.getBeansOfType(ChannelSettingsFlag.class);
    }

    @Override
    public String getCommandDescription() {
        return "**" + CHANNEL_SETTINGS_COMMAND_NAME + "**"
                + " - tunes channel configuration for the currency system (only for admins).";

    }

    @Override
    public String getFlagsDescription() {
        StringBuilder sb = new StringBuilder();

        sb.append("The list of all available '**").append(CHANNEL_SETTINGS_COMMAND_NAME).append("'** flags:\n\n");

        for (ChannelSettingsFlag channelSettingsFlag : channelSettingsFlags.values()) {
            sb.append(channelSettingsFlag.getDescription()).append("\n\n");
        }

        sb.append(COMMAND_NOTE);
        return sb.toString();
    }

    @Override
    public String getName() {
        return CHANNEL_SETTINGS_COMMAND_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> flags) {
        if (flags.isEmpty())
            return nonexistentCommand.process(message, null);

        for (ChannelSettingsFlag channelSettingsFlag : channelSettingsFlags.values()) {
            if (channelSettingsFlag.getName().equals(flags.get(0))) {
                return channelSettingsFlag.process(message, getNextCommandPartsToParse(flags));
            }
        }
        return nonexistentCommand.process(message, null);
    }
}
