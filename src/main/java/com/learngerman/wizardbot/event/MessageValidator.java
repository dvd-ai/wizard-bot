package com.learngerman.wizardbot.event;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageValidator {

    public boolean isCommand(Message message) {
        return !isBot(message.getAuthor()) && startsWithBotPrefix(message.getContent());
    }

    private boolean startsWithBotPrefix(String content) {
        return content.startsWith(">");
    }

    public boolean isBot(Optional<User> author) {
        return author.get().isBot();
    }

}
