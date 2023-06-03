package com.learngerman.wizardbot.error;

import static com.learngerman.wizardbot.Wizard.PREFIX;
import static com.learngerman.wizardbot.command.CommandName.HELP_COMMAND_NAME;

public final class ErrorDescription {

    private ErrorDescription() {
    }
    public static final String WRONG_FLAG_ERROR = String.format(
            "The flag that you've provided doesn't exist! Use **%s%s [command]** to get info about its flags.",
    PREFIX, HELP_COMMAND_NAME) ;

    public static final String NO_FLAG_ERROR = String.format(
            "This command requires a flag. Use **%s%s [command]** to get info about its flags.",
            PREFIX, HELP_COMMAND_NAME) ;
    public static final String WRONG_COMMAND_ERROR = String.format(
            "The command that you've provided doesn't exist! Use **%s** to get the list of all available commands.",
            HELP_COMMAND_NAME
    );

    public static final String MISSED_STUDENT_SERVER_ERROR = "The student isn't present in the server!";
    public static final String STUDENT_NOT_EXIST_ERROR = "Such student doesn't exist!";
}
