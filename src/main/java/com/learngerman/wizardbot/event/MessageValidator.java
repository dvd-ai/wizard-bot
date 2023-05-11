package com.learngerman.wizardbot.event;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageValidator {

    public boolean validate(Message message) {
        return checkAuthor(message.getAuthor()) && startsWithBotPrefix(message.getContent());
    }

    private boolean startsWithBotPrefix(String content) {
        return content.startsWith(">");
    }

    private boolean checkAuthor(Optional<User> author) {
        return author.map(user -> !user.isBot())
                .orElse(false);
    }

}
