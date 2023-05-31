package com.learngerman.wizardbot.util;

import com.learngerman.wizardbot.command.MemberInfo;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.rest.http.client.ClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class GuildUtil {

    private GuildUtil() {
    }

    public static Mono<List<MemberInfo>> getStudentsInfosMono(List<Long> studentIds, Mono<Guild>guildMono) {
        return Flux.fromIterable(studentIds)
                .map(Snowflake::of)
                .flatMap(snowflake -> guildMono.flatMap(guild -> guild.getMemberById(snowflake)))
                .onErrorResume(ClientException.isStatusCode(404), e -> Mono.empty())
                .map(member -> new MemberInfo(
                        member.getUsername(),
                        member.getDiscriminator(),
                        member.getAvatarUrl()
                )).collectList();
    }

    public static Mono<Guild> getGuildMono(Snowflake channelId, GatewayDiscordClient client) {
        return client.getChannelById(channelId)
                .flatMap(channel1 -> {
                    GuildChannel guildChannel = (GuildChannel) channel1;
                    return guildChannel.getGuild();
                })
                ;
    }


}
