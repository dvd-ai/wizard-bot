package com.learngerman.wizardbot.command.currency;

import com.learngerman.wizardbot.command.CurrencyFlag;
import com.learngerman.wizardbot.command.MemberInfo;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.MessageEventUtils.*;
import static com.learngerman.wizardbot.command.currency.CurrencyFlagName.CONFISCATE_FLAG_NAME;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;


@Component
public class CurrencyConfiscateFlag implements CurrencyFlag {
    private final StudentService studentService;
    private final CurrencyValidator currencyValidator;

    public CurrencyConfiscateFlag(StudentService studentService, CurrencyValidator currencyValidator) {
        this.studentService = studentService;
        this.currencyValidator = currencyValidator;
    }

    @Override
    public String getDescription() {
        return String.format("!**%s <@Erwähnung von einem Studenten> <N>** - beschlagnahmt von einem bestimmten Studenten N \uD83E\uDE99.", CONFISCATE_FLAG_NAME);
    }

    @Override
    public String getName() {
        return CONFISCATE_FLAG_NAME;
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

        studentService.confiscateStudentGoldCurrencyByDiscordId(confiscateAmount, discordUserId);

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
