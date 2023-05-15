package com.learngerman.wizardbot.command.currency;

import com.learngerman.wizardbot.command.Flag;
import com.learngerman.wizardbot.command.MemberInfo;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.PartialMember;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Permission;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.MessageEventUtils.extractDiscordIdFromMention;
import static com.learngerman.wizardbot.command.MessageEventUtils.getMemberInfo;
import static com.learngerman.wizardbot.util.NumberUtil.isPositiveRealNumber;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;


@Component
public class CurrencyConfiscateFlag implements Flag {
    private final StudentService studentService;

    public CurrencyConfiscateFlag(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public String getFlagDescription() {
        return null;
    }

    @Override
    public Mono<Object> process(Message message, List<String> parameters) {
        checkParameters(parameters);
        checkAuthorPermissions(message.getAuthorAsMember());
        return confiscate(message, parameters);
    }

    private void checkAuthorPermissions(Mono<Member> author) {
        final boolean[] verified = new boolean[1];

        author.flatMap(PartialMember::getBasePermissions).doOnNext(
                permissions -> verified[0] = permissions.contains(Permission.ADMINISTRATOR)
        ).subscribe();

        if (!verified[0])
            throw new RuntimeException("You doesn't have authorities to confiscate member's money");

    }

    private Mono<Object> confiscate(Message message, List<String> parameters) {
        Long discordUserId = Long.valueOf(extractDiscordIdFromMention(parameters.get(0)));
        float confiscateAmount = Float.parseFloat(parameters.get(1));

        studentService.decreaseStudentGoldCurrencyByDiscordId(confiscateAmount, discordUserId);

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseConfiscateMessage(confiscateAmount, getMemberInfo(message)))
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

    private void checkParameters(List<String> parameters) {
        checkArraySize(parameters);
        checkFormatOfParameters(parameters.get(0), parameters.get(1));
    }

    private void checkArraySize(List<String> parameters) {
        if (parameters.isEmpty())
            throw new RuntimeException("parameters are empty");

        if (parameters.size() < 2)
            throw new RuntimeException("not enough amount of given parameters");
    }

    private void checkFormatOfParameters(String discordUserId, String goldAmount) {
        try {
            Long.valueOf(extractDiscordIdFromMention(discordUserId));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e.getMessage());
        }

        if (!isPositiveRealNumber(goldAmount)) {
            throw new RuntimeException("wrong number format: '" + goldAmount + "'");
        }
    }


}
