package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateMono;

public class NonexistentCommand implements Command{


    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public MessageCreateMono process(Message message, String goal) {
        return null;
    }
}
