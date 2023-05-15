package com.learngerman.wizardbot.event;

import com.learngerman.wizardbot.command.MessageCommandManager;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageUpdateListener implements EventListener<MessageUpdateEvent> {

    private final MessageCommandManager messageCommandManager;
    private final MessageValidator messageValidator;

    public MessageUpdateListener(MessageCommandManager messageCommandManager, MessageValidator messageValidator) {
        this.messageCommandManager = messageCommandManager;
        this.messageValidator = messageValidator;
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
                .filter(messageValidator::isCommand)
                .flatMap(messageCommandManager::processCommand)
                ;
    }
}