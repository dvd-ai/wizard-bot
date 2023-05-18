package com.learngerman.wizardbot.simple_message;

import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;

import static com.learngerman.wizardbot.command.MessageEventUtils.getMessageAuthorDiscordId;
import static com.learngerman.wizardbot.currency.GoldCurrency.GOLD_MESSAGE_REWARD;

@Component
public class SimpleMessageManager {
    private final StudentService studentService;

    public SimpleMessageManager(StudentService studentService) {
        this.studentService = studentService;
    }

    public void process(Message message) {
        try {
            studentService.increaseStudentGoldCurrencyBy(GOLD_MESSAGE_REWARD, getMessageAuthorDiscordId(message));
        } catch (RuntimeException e) {

        }
    }
}
