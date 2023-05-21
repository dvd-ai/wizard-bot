package com.learngerman.wizardbot.command.channel;

import com.learngerman.wizardbot.command.Command;
import com.learngerman.wizardbot.command.NonexistentCommand;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.CommandUtils.getNextCommandPartsToParse;

@Component
public class ChannelSettingsCommand implements Command {

    private final ChannelSettingsReportFlag reportFlag;
    private final ChannelSettingsCurrencyIgnoranceFlag currencyIgnoranceFlag;
    private final NonexistentCommand nonexistentCommand;

    public ChannelSettingsCommand(ChannelSettingsReportFlag reportFlag,
                                  ChannelSettingsCurrencyIgnoranceFlag currencyIgnoranceFlag,
                                  NonexistentCommand nonexistentCommand) {
        this.reportFlag = reportFlag;
        this.currencyIgnoranceFlag = currencyIgnoranceFlag;
        this.nonexistentCommand = nonexistentCommand;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Mono<Object> process(Message message, List<String> flags) {
        if (flags.isEmpty())
            return nonexistentCommand.process(message, null);

        return switch (flags.get(0)) {
            case "send-report" -> reportFlag.process(message, getNextCommandPartsToParse(flags));
            case "ignore-currency" -> currencyIgnoranceFlag.process(message, getNextCommandPartsToParse(flags));
            default -> nonexistentCommand.process(message, null);
        };
    }
}
