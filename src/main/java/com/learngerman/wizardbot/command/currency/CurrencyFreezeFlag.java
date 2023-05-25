package com.learngerman.wizardbot.command.currency;

import com.learngerman.wizardbot.command.Flag;
import com.learngerman.wizardbot.command.MemberInfo;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.learngerman.wizardbot.Wizard.REPORT_TIME;
import static com.learngerman.wizardbot.command.MessageEventUtils.*;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;

@Component
public class CurrencyFreezeFlag implements Flag {

    private final CurrencyValidator currencyValidator;
    private final StudentService studentService;

    public CurrencyFreezeFlag(CurrencyValidator currencyValidator, StudentService studentService) {
        this.currencyValidator = currencyValidator;
        this.studentService = studentService;
    }

    @Override
    public String getFlagDescription() {
        return null;
    }

    @Override
    public Mono<Object> process(Message message, List<String> parameters) {
        currencyValidator.checkFreezeParameters(parameters);
        checkMessageAuthorPermissions(message);
        return freeze(message, parameters);
    }

    private Mono<Object> freeze(Message message, List<String> parameters) {
        Long discordUserId = Long.valueOf(extractDiscordIdFromMention(parameters.get(0)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(parameters.get(1), formatter);

        studentService.freezeStudentBalanceTillDefrostDate(date, discordUserId);

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseFreezeMessage(studentService.getStudentDefrostDate(discordUserId),
                                getMemberInfo(message, discordUserId))
                        )
                )
                ;
    }

    private EmbedCreateSpec constructResponseFreezeMessage(LocalDate defrostDate, MemberInfo memberInfo) {
        return buildUserInfoMessage(
                "Einfrieren",
                "Das Geld von @" + memberInfo.getUsername() + "#" + memberInfo.getDiscriminator() +
                        " wurde erfolgreich bis " + getFormattedMessageDate(defrostDate) + " gefroren!",
                memberInfo.getAvatar()
        );
    }

    private String getFormattedMessageDate(LocalDate defrostDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd. MMMM yyyy", Locale.GERMAN);
        return "**" + defrostDate.format(formatter) + " " + REPORT_TIME + "**";
    }
}
