package com.learngerman.wizardbot.command;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.PartialMember;
import discord4j.rest.http.client.ClientException;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;


public class MessageEventUtils {

    private MessageEventUtils() {
    }

    public static void checkMessageAuthorPermissions(Message message) {
        final boolean[] verified = new boolean[1];

        message.getAuthorAsMember().flatMap(PartialMember::getBasePermissions).doOnNext(
                permissions -> verified[0] = permissions.contains(Permission.ADMINISTRATOR)
        ).subscribe();

        if (!verified[0])
            throw new RuntimeException("You doesn't have authorities to use this command!");

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

    public static MemberInfo getMemberInfo(Message message, Long discordId) {
        MemberInfo memberInfo = new MemberInfo();
        Snowflake memberId = Snowflake.of(discordId);

        message.getGuild().flatMap(guild -> guild.getMemberById(memberId))
                .onErrorResume(ClientException.isStatusCode(404), e -> Mono.empty())
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

    public static String extractDiscordIdFromMention(String idWithMention) {
        return idWithMention.replaceAll("[<>@]", "");
    }

    public static String extractChannelIdFromMention(String channelIdWithMention) {
        return channelIdWithMention.replaceAll("[<>#]", "");
    }

    public static boolean extractTrueFalseDefiner(String s) {
        return switch (s) {
            case "1" -> true;
            default -> false;
        };
    }
}
