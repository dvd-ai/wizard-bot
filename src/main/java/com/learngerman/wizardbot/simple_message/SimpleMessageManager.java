package com.learngerman.wizardbot.simple_message;

import com.learngerman.wizardbot.channel.Channel;
import com.learngerman.wizardbot.channel.ChannelService;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;

import static com.learngerman.wizardbot.command.MessageEventUtils.getMessageAuthorDiscordId;
import static com.learngerman.wizardbot.currency.GoldCurrency.GOLD_MESSAGE_REWARD_PER_WORD;

@Component
public class SimpleMessageManager {
    private final StudentService studentService;
    private final ChannelService channelService;

    public SimpleMessageManager(StudentService studentService, ChannelService channelService) {
        this.studentService = studentService;
        this.channelService = channelService;
    }

    public void process(Message message) {
        try {
            if (isMessageFromIgnoredChannel(message))
                return;
            int wordAmount = calculateWords(message.getContent());
            studentService.increaseStudentGoldCurrencyBy(wordAmount * GOLD_MESSAGE_REWARD_PER_WORD, getMessageAuthorDiscordId(message));
        } catch (RuntimeException e) {

        }
    }

    private int calculateWords(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        String[] words = content.split("\\s+");
        return words.length;
    }

    private boolean isMessageFromIgnoredChannel(Message message) {
        Channel usedChannel = channelService.getChannelById(message.getChannelId().asLong());
        return usedChannel.isIgnoredForCurrencyOperations();
    }


}
