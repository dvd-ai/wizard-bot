package com.learngerman.wizardbot.command;

public final class CommandName {

    public static final String CURRENCY_COMMAND_NAME = "währung";
    public static final String HELP_COMMAND_NAME = "hilfe";
    public static final String CHANNEL_SETTINGS_COMMAND_NAME = "kanaleinstellungen";
    public static final String ADD_COMMAND_NAME = "hinzufügen";
    public static final String COMMAND_NOTE = """
            **Notiz**:
            **!** - Befehl, der nur von Admins verwendet werden kann;
            **<>** - erforderlicher Parameter;
            **|** - oder;
                                          """;

    private CommandName() {
    }
}
