package com.learngerman.wizardbot.event;

import com.learngerman.wizardbot.voice_chat.VoiceChatManager;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class VoiceStateUpdateEventListener implements EventListener<VoiceStateUpdateEvent> {
    private final VoiceChatManager voiceChatManager;

    public VoiceStateUpdateEventListener(VoiceChatManager voiceChatManager) {
        this.voiceChatManager = voiceChatManager;
    }

    @Override
    public Class<VoiceStateUpdateEvent> getEventType() {
        return VoiceStateUpdateEvent.class;
    }

    @Override
    public Mono<Void> execute(VoiceStateUpdateEvent event) {
        voiceChatManager.process(event);
        return Mono.empty();
    }
}
