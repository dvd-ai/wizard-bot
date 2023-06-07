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
import static com.learngerman.wizardbot.command.currency.CurrencyFlagName.UNFREEZE_FLAG_NAME;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;

@Component
public class CurrencyUnfreezeFlag implements CurrencyFlag {

    private final CurrencyValidator currencyValidator;
    private final StudentService studentService;

    public CurrencyUnfreezeFlag(CurrencyValidator currencyValidator, StudentService studentService) {
        this.currencyValidator = currencyValidator;
        this.studentService = studentService;
    }

    @Override
    public String getDescription() {
        return String.format("""
                !**%s <@Erwähnung von einem Studenten>** - taut einen Währungssaldo eines bestimmten Studenten auf.
                
                """, UNFREEZE_FLAG_NAME);
    }

    @Override
    public String getName() {
        return UNFREEZE_FLAG_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> parameters) {
        currencyValidator.checkUnfreezeParameters(parameters);
        checkMessageAuthorPermissions(message);
        return unfreeze(message, parameters);
    }

    private Mono<Object> unfreeze(Message message, List<String> parameters) {
        Long discordUserId = Long.valueOf(extractDiscordIdFromMention(parameters.get(0)));

        studentService.unfreezeStudentBalance(discordUserId);

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                                constructResponseUnfreezeMessage(getMemberInfo(message, discordUserId))
                        )
                )
                ;
    }

    private EmbedCreateSpec constructResponseUnfreezeMessage(MemberInfo memberInfo) {
        return buildUserInfoMessage(
                "Auftauen",
                "Das Geld von @" + memberInfo.getUsername() + "#" + memberInfo.getDiscriminator() +
                        " wurde erfolgreich aufgetaut!",
                memberInfo.getAvatar()
        );
    }
}
