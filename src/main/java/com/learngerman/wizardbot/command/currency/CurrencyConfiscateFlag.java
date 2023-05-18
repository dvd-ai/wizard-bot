package com.learngerman.wizardbot.command.currency;

import com.learngerman.wizardbot.command.Flag;
import com.learngerman.wizardbot.command.MemberInfo;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.MessageEventUtils.*;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;


@Component
public class CurrencyConfiscateFlag implements Flag {
    private final StudentService studentService;
    private final CurrencyValidator currencyValidator;

    public CurrencyConfiscateFlag(StudentService studentService, CurrencyValidator currencyValidator) {
        this.studentService = studentService;
        this.currencyValidator = currencyValidator;
    }

    @Override
    public String getFlagDescription() {
        return null;
    }

    @Override
    public Mono<Object> process(Message message, List<String> parameters) {
        currencyValidator.checkUpdateParameters(parameters);
        checkMessageAuthorPermissions(message);
        return confiscate(message, parameters);
    }

    private Mono<Object> confiscate(Message message, List<String> parameters) {
        Long discordUserId = Long.valueOf(extractDiscordIdFromMention(parameters.get(0)));
        float confiscateAmount = Float.parseFloat(parameters.get(1));

        studentService.decreaseStudentGoldCurrencyByDiscordId(confiscateAmount, discordUserId);

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseConfiscateMessage(confiscateAmount, getMemberInfo(message, discordUserId)))
                )
                ;
    }

    private EmbedCreateSpec constructResponseConfiscateMessage(float confiscateAmount, MemberInfo memberInfo) {
        return buildUserInfoMessage(
                "Beschlagnahmung",
                "Das Geld von @" + memberInfo.getUsername() + "#" + memberInfo.getDiscriminator() +
                        " wurde erfolgreich beschlagnahmt (" + confiscateAmount + "🪙)!",
                memberInfo.getAvatar()
        );
    }

}
