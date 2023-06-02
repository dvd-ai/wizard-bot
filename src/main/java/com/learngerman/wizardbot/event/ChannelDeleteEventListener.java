package com.learngerman.wizardbot.event;

import com.learngerman.wizardbot.channel.ChannelService;
import discord4j.core.event.domain.channel.TextChannelDeleteEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ChannelDeleteEventListener implements EventListener<TextChannelDeleteEvent>{

    private final ChannelService channelService;

    public ChannelDeleteEventListener(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Override
    public Class<TextChannelDeleteEvent> getEventType() {
        return TextChannelDeleteEvent.class;
    }

    @Override
    public Mono<Void> execute(TextChannelDeleteEvent event) {
        long deletedChannelId = event.getChannel().getId().asLong();
        channelService.deleteChannel(deletedChannelId);
        return Mono.empty();
    }
}
