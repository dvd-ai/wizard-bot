package com.learngerman.wizardbot.command.currency;

import com.learngerman.wizardbot.command.Flag;
import com.learngerman.wizardbot.command.NonexistentCommand;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.CommandUtils.getNextCommandPartsToParse;
import static com.learngerman.wizardbot.util.NumberUtil.isPositiveRealNumber;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUsualMessage;

@Component
public class CurrencyGrantFlag implements Flag {

    private final StudentService studentService;
    private final NonexistentCommand nonexistentCommand;

    public CurrencyGrantFlag(StudentService studentService, NonexistentCommand nonexistentCommand) {
        this.studentService = studentService;
        this.nonexistentCommand = nonexistentCommand;
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
            case "all" -> processAll(message, getNextCommandPartsToParse(parameters));//check role of the initiator
            default -> nonexistentCommand.process(message, null);
        };
    }

    private Mono<Object> processAll(Message message, List<String> parameters) {
        if (parameters.isEmpty() || !isPositiveRealNumber(parameters.get(0)))
            return nonexistentCommand.process(message, null);

        float grantAmount = Float.parseFloat(parameters.get(0));
        studentService.increaseStudentsGoldCurrencyBy(grantAmount);

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseGrantMessage(grantAmount))
                )
                ;

    }

    private MessageCreateSpec constructResponseGrantMessage(float grantAmount) {
        return buildUsualMessage(
                "Zuschuss!",
                "Alle Studenten haben " + grantAmount + "🪙 bekommen!"
        );
    }
}
