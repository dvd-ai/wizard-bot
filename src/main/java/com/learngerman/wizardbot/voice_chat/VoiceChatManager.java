package com.learngerman.wizardbot.voice_chat;

import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.learngerman.wizardbot.currency.GoldCurrency.GOLD_VOICE_REWARD;

@Component
public class VoiceChatManager {

    private final StudentService studentService;
    private final Map<Long, Instant> joinTimes = new HashMap<>();

    public VoiceChatManager(StudentService studentService) {
        this.studentService = studentService;
    }

    public void process(VoiceStateUpdateEvent voiceEvent ) {
        VoiceState currentVoiceState = voiceEvent.getCurrent();
        Long userId = currentVoiceState.getUserId().asLong();

        if (!isUserInVC(currentVoiceState) && joinTimes.containsKey(userId)) {
            grantStudentGoldCurrency(userId);
            joinTimes.remove(userId);

            // User switched to a new voice channel
        } else if (isUserInVC(currentVoiceState)) {
            if (joinTimes.containsKey(userId)) {
                grantStudentGoldCurrency(userId);
            }
            joinTimes.put(userId, Instant.now());
        }
    }

    private boolean isUserInVC(VoiceState currentVoiceState) {
        return currentVoiceState.getChannelId().isPresent();
    }

    private void grantStudentGoldCurrency(Long userId) {
        Duration durationInVoiceChannel = Duration.between(joinTimes.get(userId), Instant.now());
        studentService.increaseStudentGoldCurrencyBy(durationInVoiceChannel.getSeconds() * GOLD_VOICE_REWARD, userId);
    }
}
