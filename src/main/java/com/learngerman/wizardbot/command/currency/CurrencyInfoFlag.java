package com.learngerman.wizardbot.command.currency;

import com.learngerman.wizardbot.command.Flag;
import com.learngerman.wizardbot.command.MemberInfo;
import com.learngerman.wizardbot.command.NonexistentCommand;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.MessageEventUtils.*;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;

@Component
public class CurrencyInfoFlag implements Flag {

    private final CurrencyValidator currencyValidator;
    private final NonexistentCommand nonexistentCommand;
    private final StudentService studentService;

    public CurrencyInfoFlag(CurrencyValidator currencyValidator, NonexistentCommand nonexistentCommand, StudentService studentService) {
        this.currencyValidator = currencyValidator;
        this.nonexistentCommand = nonexistentCommand;
        this.studentService = studentService;
    }


    @Override
    public String getFlagDescription() {
        return null;
    }

    @Override
    public Mono<Object> process(Message message, List<String> parameters) {
        if (parameters.isEmpty())
            return nonexistentCommand.process(message, null);

        return switch (parameters.get(0)) {
            default -> processSpecifiedStudent(message, parameters);
        };
    }

    private Mono<Object> processSpecifiedStudent(Message message, List<String> parameters) {
        currencyValidator.checkDiscordId(parameters.get(0));

        Long discordUserId = Long.valueOf(extractDiscordIdFromMention(parameters.get(0)));

        float goldCurrency = studentService.getStudentGoldCurrency(discordUserId);

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseMemberInfoCurrency(goldCurrency, getMemberInfo(message, discordUserId)))
                );
    }

    private EmbedCreateSpec constructResponseMemberInfoCurrency(float goldCurrency, MemberInfo memberInfo) {
        return buildUserInfoMessage(
                "Währungssaldo",
                "@" + memberInfo.getUsername() + "#" + memberInfo.getDiscriminator() + " - " + goldCurrency + "🪙",
                memberInfo.getAvatar()
        );
    }
}
