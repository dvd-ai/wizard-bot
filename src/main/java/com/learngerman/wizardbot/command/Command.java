package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateMono;

public interface Command {

    String getDescription();

    MessageCreateMono process(Message message, String goal);

}
