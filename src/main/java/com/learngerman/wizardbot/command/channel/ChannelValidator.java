package com.learngerman.wizardbot.command.channel;

import org.springframework.stereotype.Component;

import java.util.List;

import static com.learngerman.wizardbot.command.MessageEventUtils.extractChannelIdFromMention;


@Component
public class ChannelValidator {

    public void validate(List<String> parameters) {
        checkTrueFalseDefiner(parameters.get(parameters.size() - 1));
        checkChannels(parameters);
    }

    private void checkTrueFalseDefiner(String lastParameter) {
        if (!lastParameter.equals("1") && !lastParameter.equals("0"))
            throw new RuntimeException("the last parameter of the command should be '1' or '0'");
    }

    private void checkChannels(List<String> parameters) {
        for (int i = 0; i < parameters.size() - 1; i++) {
            String channelId = extractChannelIdFromMention(parameters.get(i));
            checkChannelId(channelId);
        }
    }

    public void checkChannelId(String channelId) {
        try {
            Long.valueOf(channelId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("There is no such a channel " + channelId);
        }
    }
}
