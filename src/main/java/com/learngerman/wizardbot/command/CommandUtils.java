package com.learngerman.wizardbot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandUtils {

    private CommandUtils() {
    }

    public static String polishCommand(String content) {
        return content.toLowerCase().replaceFirst(">", "");
    }

    public static List<String> getNextCommandPartsToParse(List<String> commandParts) {
        if (commandParts.isEmpty())
            return commandParts;

        List<String> flagsAndParameters = new ArrayList<>(commandParts);
        flagsAndParameters.remove(0);
        return flagsAndParameters;
    }

    public static List<String> extractCommandParts(String polishedContent) {
        return Arrays.stream(polishedContent.split("\\s+")).toList();
    }
}
