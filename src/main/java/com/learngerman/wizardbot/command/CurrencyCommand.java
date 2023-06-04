package com.learngerman.wizardbot.command;

import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.learngerman.wizardbot.command.CommandName.COMMAND_NOTE;
import static com.learngerman.wizardbot.command.CommandName.CURRENCY_COMMAND_NAME;
import static com.learngerman.wizardbot.command.CommandUtils.getNextCommandPartsToParse;
import static com.learngerman.wizardbot.error.ErrorDescription.NO_FLAG_ERROR;
import static com.learngerman.wizardbot.error.ErrorDescription.WRONG_FLAG_ERROR;

@Component
public class CurrencyCommand implements Command {
    private final NonexistentCommand nonexistentCommand;
    private final Map<String, CurrencyFlag> currencyFlags;

    public CurrencyCommand(NonexistentCommand nonexistentCommand,
                           ApplicationContext applicationContext) {
        this.nonexistentCommand = nonexistentCommand;
        this.currencyFlags = applicationContext.getBeansOfType(CurrencyFlag.class);
    }


    @Override
    public String getCommandDescription() {
        return "**" + CURRENCY_COMMAND_NAME + "**" +
                " - hilft Ihnen, mit der Währung zu manipulieren (nur für Administratoren); um den Mitgliedern einen Saldo von \uD83E\uDE99 bereitzustellen.";
    }

    @Override
    public String getFlagsDescription() {

        StringBuilder sb = new StringBuilder();

        sb.append("Die Liste aller verfügbaren **'").append(CURRENCY_COMMAND_NAME).append("'** Flaggen:\n\n");

        for (CurrencyFlag currencyFlag : currencyFlags.values()) {
            sb.append(currencyFlag.getDescription()).append("\n\n");
        }

        sb.append(COMMAND_NOTE);
        return sb.toString();
    }

    @Override
    public String getName() {
        return CURRENCY_COMMAND_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> flags) {
        if (flags.isEmpty())
            return nonexistentCommand.process(message, NO_FLAG_ERROR);

        for (CurrencyFlag currencyFlag : currencyFlags.values()) {
            if (currencyFlag.getName().equals(flags.get(0))) {
                return currencyFlag.process(message, getNextCommandPartsToParse(flags));
            }
        }
        return nonexistentCommand.process(message, WRONG_FLAG_ERROR);
    }
}