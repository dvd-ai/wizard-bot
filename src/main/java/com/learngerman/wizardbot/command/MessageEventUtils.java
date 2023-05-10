package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public class MessageEventUtils {

    private MessageEventUtils() {
    }

    public static Mono<Member> getGuildMember(Message message) {
        return message.getAuthor().get().asMember(message.getGuildId().get());
    }

    public static Long getMessageAuthorDiscordId(Message message) {
        return message.getAuthor().get().getId().asLong();
    }
}
