package com.learngerman.wizardbot.util;

import com.learngerman.wizardbot.command.MemberInfo;
import com.learngerman.wizardbot.student.Student;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.rest.http.client.ClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;

public class GuildUtil {

    private GuildUtil() {
    }

    public static Mono<List<MemberInfo>> getStudentsInfosListMono(List<Long> studentIds, Mono<Guild> guildMono) {
        return Flux.fromIterable(studentIds)
                .map(Snowflake::of)
                .flatMap(snowflake -> guildMono.flatMap(guild -> guild.getMemberById(snowflake)))
                .onErrorResume(ClientException.isStatusCode(404), e -> Mono.empty())
                .map(member -> new MemberInfo(
                        member.getUsername(),
                        member.getAvatarUrl()
                )).collectList();
    }

    public static Mono<Map<Student, MemberInfo>> getStudentsInfosMapMono(List<Student> students, Mono<Guild> guildMono) {
        return Flux.fromIterable(students)
                .flatMap(student -> guildMono.flatMap(guild -> guild.getMemberById(Snowflake.of(student.getDiscordId()))
                        .onErrorResume(ClientException.isStatusCode(404), e -> Mono.empty())
                        .map(member -> Tuples.of(student, new MemberInfo(
                                member.getUsername(),
                                member.getAvatarUrl()
                        )))
                ))
                .collectMap(Tuple2::getT1, Tuple2::getT2);
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
