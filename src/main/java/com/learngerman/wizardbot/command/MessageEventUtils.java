package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;

public class MessageEventUtils {

    private MessageEventUtils() {
    }

    public static MemberInfo getMemberInfo(Message message) {
        MemberInfo memberInfo = new MemberInfo();

        message.getAuthor().get().asMember(message.getGuildId().get())
                .doOnNext(member -> {
                    memberInfo.setAvatar(member.getAvatarUrl());
                    memberInfo.setDiscriminator(member.getDiscriminator());
                    memberInfo.setUsername(member.getUsername());
                }).subscribe();

        return memberInfo;
    }

    public static Long getMessageAuthorDiscordId(Message message) {
        return message.getAuthor().get().getId().asLong();
    }
}
