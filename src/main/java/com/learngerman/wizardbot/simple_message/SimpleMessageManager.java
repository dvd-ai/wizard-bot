package com.learngerman.wizardbot.simple_message;

import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;

import static com.learngerman.wizardbot.command.MessageEventUtils.getMessageAuthorDiscordId;

@Component
public class SimpleMessageManager {
    private final StudentService studentService;
    private final float MESSAGE_REWARD = 0.5f;

    public SimpleMessageManager(StudentService studentService) {
        this.studentService = studentService;
    }

    public void process(Message message) {
        studentService.increaseStudentGoldCurrencyBy(MESSAGE_REWARD, getMessageAuthorDiscordId(message));
    }
}
