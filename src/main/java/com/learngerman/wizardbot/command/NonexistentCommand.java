package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildErrorMessage;

@Component

public class NonexistentCommand {
    public Mono<Object> process(Message message, String errorDescription) {
        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        buildErrorMessage("Error!", errorDescription))
                );
    }
}
