package com.learngerman.wizardbot.command.currency;

import com.learngerman.wizardbot.error.exception.CommandLineException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.learngerman.wizardbot.command.MessageEventUtils.extractDiscordIdFromMention;
import static com.learngerman.wizardbot.util.NumberUtil.isPositiveRealNumber;

@Component
public class CurrencyValidator {

    public void checkUpdateParameters(List<String> parameters) {
        checkArraySize(parameters, 2);
        checkFormatOfParameters(parameters.get(0), parameters.get(1));
    }

    public void checkFreezeParameters(List<String> parameters) {
        checkArraySize(parameters, 2);
        checkDiscordId(parameters.get(0));
        checkDate(parameters.get(1));
    }

    public void checkUnfreezeParameters(List<String> parameters) {
        checkArraySize(parameters, 1);
        checkDiscordId(parameters.get(0));
    }

    private void checkArraySize(List<String> parameters, int lowestSizeBound) {
        if (parameters.isEmpty())
            throw new CommandLineException("parameters shouldn't be empty!");

        if (parameters.size() < lowestSizeBound)
            throw new CommandLineException("You've provided not enough parameters!");
    }

    private void checkFormatOfParameters(String discordUserId, String goldAmount) {
        checkDiscordId(discordUserId);
        checkCurrencyFormat(goldAmount);
    }

    public void checkDiscordId(String discordUserId) {
        try {
            Long.valueOf(extractDiscordIdFromMention(discordUserId));
        } catch (NumberFormatException e) {
            throw new CommandLineException("Wrong format of the user you've provided.");
        }
    }

    public void checkCurrencyFormat(String goldAmount) {
        if (!isPositiveRealNumber(goldAmount)) {
            throw new CommandLineException("Wrong number format: '" + goldAmount + "'");
        }
    }

    public void checkDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate parsedDate;
        LocalDate now = LocalDate.now();

        try {
            parsedDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new CommandLineException("The date '" + date + "' doesn't follow the pattern: **dd.MM.yyyy**!");
        }

        if (parsedDate.isBefore(now) || parsedDate.isEqual(now))
            throw new CommandLineException("""
                    The date you provided is in the past. Don't forget to set date,
                    according to Berlin/Europe timezone!"
                    """
            );
    }
}
