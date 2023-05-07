package com.learngerman.wizardbot.event;

import com.learngerman.wizardbot.command.MessageCommandManager;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageUpdateListener implements EventListener<MessageUpdateEvent> {

    private final MessageCommandManager messageCommandManager;

    public MessageUpdateListener(MessageCommandManager messageCommandManager) {
        this.messageCommandManager = messageCommandManager;
    }

    @Override
    public Class<MessageUpdateEvent> getEventType() {
        return MessageUpdateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageUpdateEvent event) {
        return Mono.just(event)
                .filter(MessageUpdateEvent::isContentChanged)
                .flatMap(MessageUpdateEvent::getMessage)
                .flatMap(messageCommandManager::processCommand);
    }
}