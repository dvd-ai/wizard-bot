package com.learngerman.wizardbot.event;

import com.learngerman.wizardbot.command.MessageCommandManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageCreateListener implements EventListener<MessageCreateEvent> {

    private final MessageCommandManager messageCommandManager;

    public MessageCreateListener(MessageCommandManager messageCommandManager) {
        this.messageCommandManager = messageCommandManager;
    }


    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return messageCommandManager.processCommand(event.getMessage());
    }
}