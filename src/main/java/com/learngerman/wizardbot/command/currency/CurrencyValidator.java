package com.learngerman.wizardbot.command.currency;

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
        checkArraySize(parameters);
        checkFormatOfParameters(parameters.get(0), parameters.get(1));
    }

    public void checkFreezeParameters(List<String> parameters) {
        checkArraySize(parameters);
        checkDiscordId(parameters.get(0));
        checkDate(parameters.get(1));
    }

    private void checkArraySize(List<String> parameters) {
        if (parameters.isEmpty())
            throw new RuntimeException("parameters are empty");

        if (parameters.size() < 2)
            throw new RuntimeException("not enough amount of given parameters");
    }

    private void checkFormatOfParameters(String discordUserId, String goldAmount) {
        checkDiscordId(discordUserId);
        checkCurrencyFormat(goldAmount);
    }

    public void checkDiscordId (String discordUserId) {
        try {
            Long.valueOf(extractDiscordIdFromMention(discordUserId));
        } catch (NumberFormatException e) {
            throw new RuntimeException("There is no such a user.");
        }
    }

    public void checkCurrencyFormat(String goldAmount) {
        if (!isPositiveRealNumber(goldAmount)) {
            throw new RuntimeException("wrong number format: '" + goldAmount + "'");
        }
    }

    public void checkDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate parsedDate;
        LocalDate now = LocalDate.now();

        try {
            parsedDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("the date '" + date + "' doesn't follow the pattern: dd.MM.yyyy");
        }

        if (parsedDate.isBefore(now) || parsedDate.isEqual(now))
            throw new RuntimeException("""
                    The date you provided is in the past. Don't forget to set date,
                    according to Berlin/Europe timezone"
                    """
            );
    }
}
