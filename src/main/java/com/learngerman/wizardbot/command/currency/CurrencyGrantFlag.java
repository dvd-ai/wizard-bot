package com.learngerman.wizardbot.command.currency;

import com.learngerman.wizardbot.command.CurrencyFlag;
import com.learngerman.wizardbot.command.Flag;
import com.learngerman.wizardbot.command.MemberInfo;
import com.learngerman.wizardbot.command.NonexistentCommand;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.CommandUtils.getNextCommandPartsToParse;
import static com.learngerman.wizardbot.command.MessageEventUtils.*;
import static com.learngerman.wizardbot.command.currency.CurrencyFlagName.GRANT_FLAG_NAME;
import static com.learngerman.wizardbot.util.NumberUtil.isPositiveRealNumber;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUsualMessage;

@Component
public class CurrencyGrantFlag implements CurrencyFlag {

    private final StudentService studentService;
    private final NonexistentCommand nonexistentCommand;
    private final CurrencyValidator currencyValidator;

    public CurrencyGrantFlag(StudentService studentService, NonexistentCommand nonexistentCommand, CurrencyValidator currencyValidator) {
        this.studentService = studentService;
        this.nonexistentCommand = nonexistentCommand;
        this.currencyValidator = currencyValidator;
    }

    @Override
    public String getDescription() {
        return String.format("!**%s <all> <N>** - grants all students N \uD83E\uDE99.\n" +
                "!**%s <@studentMention> <N>** - grants a specific student N \uD83E\uDE99.", GRANT_FLAG_NAME, GRANT_FLAG_NAME);
    }

    @Override
    public String getName() {
        return GRANT_FLAG_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> parameters) {
        if (parameters.isEmpty())
            return nonexistentCommand.process(message, null);

        return switch (parameters.get(0)) {
            case "all" -> processAll(message, getNextCommandPartsToParse(parameters));//check role of the initiator
            default -> processSpecifiedStudent(message, parameters);
        };
    }

    private Mono<Object> processSpecifiedStudent(Message message, List<String> parameters) {
        currencyValidator.checkUpdateParameters(parameters);
        checkMessageAuthorPermissions(message);

        Long discordUserId = Long.valueOf(extractDiscordIdFromMention(parameters.get(0)));
        float grantAmount = Float.parseFloat(parameters.get(1));
        studentService.grantStudentGoldCurrencyByDiscordId(grantAmount, discordUserId);

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseStudentGrantMessage(grantAmount, getMemberInfo(message, discordUserId)))
                )
                ;
    }

    private EmbedCreateSpec constructResponseStudentGrantMessage(float grantAmount, MemberInfo memberInfo) {
        return buildUserInfoMessage(
                "Zuschuss!",
                "Das Geld von @" + memberInfo.getUsername() + "#" + memberInfo.getDiscriminator() +
                        " wurde erfolgreich erhöht (" + grantAmount + "🪙)!",
                memberInfo.getAvatar()
        );
    }

    private Mono<Object> processAll(Message message, List<String> parameters) {
        if (parameters.isEmpty() || !isPositiveRealNumber(parameters.get(0)))
            return nonexistentCommand.process(message, null);

        float grantAmount = Float.parseFloat(parameters.get(0));
        studentService.grantAllStudentsGoldCurrency(grantAmount);

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseGrantAllMessage(grantAmount))
                )
                ;

    }

    private MessageCreateSpec constructResponseGrantAllMessage(float grantAmount) {
        return buildUsualMessage(
                "Zuschuss!",
                "Alle Studenten haben " + grantAmount + "🪙 bekommen!"
        );
    }
}
