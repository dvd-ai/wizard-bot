package com.learngerman.wizardbot.command;

public final class CommandName {

    private CommandName() {
    }

    public static final String CURRENCY_COMMAND_NAME = "currency";
    public static final String HELP_COMMAND_NAME = "help";
    public static final String CHANNEL_SETTINGS_COMMAND_NAME = "channel-settings";
    public static final String ADD_COMMAND_NAME = "add-new";

    public static final String COMMAND_NOTE = """
                **Note**:
                **!** - command that can be used only by admins;
                **<>** - required parameter;
                **|** - or;
                                              """;
}
