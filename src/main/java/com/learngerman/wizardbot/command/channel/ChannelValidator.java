package com.learngerman.wizardbot.command.channel;

import com.learngerman.wizardbot.error.exception.CommandLineException;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.rest.http.client.ClientException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.MessageEventUtils.extractChannelIdFromMention;
import static com.learngerman.wizardbot.error.ErrorDescription.*;


@Component
public class ChannelValidator {

    public void validate(List<String> parameters, Message message) {

        checkArraySize(parameters);
        checkTrueFalseDefiner(parameters.get(parameters.size() - 1));
        checkChannels(parameters, message);
    }

    private void checkArraySize(List<String> parameters) {
        if (parameters.isEmpty())
            throw new CommandLineException(NO_PARAMETERS_ERROR);

        if (parameters.size() < 2)
            throw new CommandLineException(NOT_ENOUGH_PARAMETER_AMOUNT_ERROR);
    }

    private void checkTrueFalseDefiner(String lastParameter) {
        if (!lastParameter.equals("1") && !lastParameter.equals("0"))
            throw new CommandLineException("Der letzte Parameter des Befehls sollte ‚1‘ oder ‚0‘ sein.");
    }

    private void checkChannels(List<String> parameters, Message message) {
        for (int i = 0; i < parameters.size() - 1; i++) {
            String channelId = extractChannelIdFromMention(parameters.get(i));
            checkChannelId(channelId);
            channelExistInGuild(message, channelId);
        }
    }

    public void checkChannelId(String channelId) {
        try {
            Long.valueOf(channelId);
        } catch (NumberFormatException e) {
            throw new CommandLineException("Falsches Format des Kanals " + channelId);
        }
    }

    private void channelExistInGuild(Message message, String channelId) {
        boolean channelExist;

        channelExist = Boolean.TRUE.equals(message.getGuild()
                .flatMap(guild -> guild.getChannelById(Snowflake.of(channelId)))
                .thenReturn(true)
                .onErrorResume(ClientException.isStatusCode(404), e -> Mono.just(false))
                .block());

        if (!channelExist)
            throw new CommandLineException(CHANNEL_NOT_EXIST_ERROR);
    }
}
