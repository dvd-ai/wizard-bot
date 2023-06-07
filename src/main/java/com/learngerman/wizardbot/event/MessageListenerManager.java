package com.learngerman.wizardbot.event;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("prototype")
public class MessageListenerManager {

    private final Map<Snowflake, Disposable> messageListenerDisposables = new ConcurrentHashMap<>();

    public void disposeListener(Message message, Disposable listener, Duration duration) {
        messageListenerDisposables.put(message.getId(), listener);
        scheduleRemoval(message, duration);
    }

    public void removeListener(Message message) {
        Disposable disposable = messageListenerDisposables.remove(message.getId());
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void scheduleRemoval(Message message, Duration duration) {
        Mono.delay(duration)
                .flatMap(time -> deleteMessage(message))
                .subscribe(aVoid -> removeListener(message));
    }

    private Mono<Void> deleteMessage(Message message) {
        return message.delete();
    }
}
