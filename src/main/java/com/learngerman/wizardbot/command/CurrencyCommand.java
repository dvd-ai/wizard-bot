package com.learngerman.wizardbot.command;

import com.learngerman.wizardbot.command.currency.CurrencyGrantFlag;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.CommandUtils.getNextCommandPartsToParse;
import static com.learngerman.wizardbot.command.MessageEventUtils.*;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;

@Component
public class CurrencyCommand implements Command {

    private final StudentService studentService;
    private final CurrencyGrantFlag grantFlag;
    private final NonexistentCommand nonexistentCommand;

    public CurrencyCommand(StudentService studentService, CurrencyGrantFlag grantFlag, NonexistentCommand nonexistentCommand) {
        this.studentService = studentService;
        this.grantFlag = grantFlag;
        this.nonexistentCommand = nonexistentCommand;
    }


    @Override
    public String getDescription() {
        return """
                **currency** - shows your current currency balance 🪙.
                 """;
    }

    @Override
    public Mono<Object> process(Message message, List<String> flags) {
        if (flags.isEmpty())
            return processWithNoParametersOrFlags(message);

        return switch (flags.get(0)) {
            case "grant" -> grantFlag.process(message, getNextCommandPartsToParse(flags));
            default -> nonexistentCommand.process(message, null);
        };

    }


    private Mono<Object> processWithNoParametersOrFlags(Message message) {
        Long authorDiscordId = getMessageAuthorDiscordId(message);
        float goldCurrency = studentService.getStudentGoldCurrency(authorDiscordId);


        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseMemberInfoCurrency(goldCurrency, getMemberInfo(message)))
        );
    }

    private EmbedCreateSpec constructResponseMemberInfoCurrency(float goldCurrency, MemberInfo memberInfo ) {
        return buildUserInfoMessage(
                "Währungssaldo" ,
                "@" +  memberInfo.getUsername() + "#" + memberInfo.getDiscriminator() + " - " + goldCurrency + "🪙",
                memberInfo.getAvatar()
        );
    }
}