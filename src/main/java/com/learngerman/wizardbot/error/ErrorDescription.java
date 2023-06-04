package com.learngerman.wizardbot.error;

import static com.learngerman.wizardbot.Wizard.PREFIX;
import static com.learngerman.wizardbot.command.CommandName.HELP_COMMAND_NAME;

public final class ErrorDescription {

    public static final String HELP_COMMAND_ADVICE = String.format(
            "Verwenden Sie **%s%s [Befehl]** um Informationen zu seinen Flaggen zu erhalten.",
            PREFIX, HELP_COMMAND_NAME
    );
    public static final String WRONG_FLAG_ERROR = "Die Flagge, die Sie angegeben haben, existiert nicht! " + HELP_COMMAND_ADVICE;

    public static final String NO_FLAG_ERROR = "Dieser Befehl erfordert eine Flagge." + HELP_COMMAND_ADVICE;
    public static final String WRONG_COMMAND_ERROR = String.format(
            "Der Befehl, den Sie angegeben haben, existiert nicht! Verwenden Sie **%s** um die Liste aller verfügbaren Befehle zu erhalten.",
            HELP_COMMAND_NAME
    );
    public static final String MISSED_STUDENT_SERVER_ERROR = "Der Student ist im Server nicht!";
    public static final String STUDENT_NOT_EXIST_ERROR = "Solcher Student existiert nicht!";

    public static final String NOT_ENOUGH_PARAMETER_AMOUNT_ERROR = "Sie haben nicht genügend Parameter angegeben! " + HELP_COMMAND_ADVICE;
    public static final String NO_PARAMETERS_ERROR = "Die Parameter dürfen nicht leer sein! " + HELP_COMMAND_ADVICE;
    public static final String USER_FORMAT_ERROR = "Falsches Format des Benutzers, den Sie angegeben haben. " + HELP_COMMAND_ADVICE;
    public static final String NUMBER_FORMAT_ERROR = "Falsches Zahlformat. " + HELP_COMMAND_ADVICE;
    public static final String DATE_FORMAT_ERROR = "Falsches Datumformat. " + HELP_COMMAND_ADVICE;
    public static final String DATE_PAST_ERROR = """
            Das Datum, das Sie angegeben haben, liegt in der Vergangenheit. Vergessen Sie nicht, das Datum
                    nach Zeitzone Berlin/Europa festzulegen!
            """;

    public static final String KANAL_NOT_EXIST_ERROR = "Dieser Kanal existiert nicht!";
    private ErrorDescription() {
    }
}
