package com.learngerman.wizardbot.event;

import com.learngerman.wizardbot.command.MessageCommandManager;
import com.learngerman.wizardbot.simple_message.SimpleMessageManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageCreateListener implements EventListener<MessageCreateEvent> {

    private final MessageCommandManager messageCommandManager;
    private final SimpleMessageManager simpleMessageManager;
    private final MessageValidator messageValidator;

    public MessageCreateListener(MessageCommandManager messageCommandManager, SimpleMessageManager simpleMessageManager, MessageValidator messageValidator) {
        this.messageCommandManager = messageCommandManager;
        this.simpleMessageManager = simpleMessageManager;
        this.messageValidator = messageValidator;
    }


    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        Message message = event.getMessage();

        if (messageValidator.isCommand(message))
            return messageCommandManager.processCommand(message);
        else if (!messageValidator.isBot(message.getAuthor())) {
            simpleMessageManager.process(message);
        }
        return Mono.empty();
    }
}