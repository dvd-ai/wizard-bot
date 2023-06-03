package com.learngerman.wizardbot.command;

import com.learngerman.wizardbot.student.Student;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.learngerman.wizardbot.command.CommandName.COMMAND_NOTE;
import static com.learngerman.wizardbot.command.CommandName.CURRENCY_COMMAND_NAME;
import static com.learngerman.wizardbot.command.CommandUtils.getNextCommandPartsToParse;
import static com.learngerman.wizardbot.command.MessageEventUtils.getMemberInfo;
import static com.learngerman.wizardbot.command.MessageEventUtils.getMessageAuthorDiscordId;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;

@Component
public class CurrencyCommand implements Command {

    private final StudentService studentService;
    private final NonexistentCommand nonexistentCommand;
    private final Map<String, CurrencyFlag> currencyFlags;

    public CurrencyCommand(StudentService studentService, NonexistentCommand nonexistentCommand,
                           ApplicationContext applicationContext) {
        this.studentService = studentService;
        this.nonexistentCommand = nonexistentCommand;
        this.currencyFlags = applicationContext.getBeansOfType(CurrencyFlag.class);
    }


    @Override
    public String getCommandDescription() {
        return "**" + CURRENCY_COMMAND_NAME + "**" +
                " - helps you to manipulate with the currency (for admins only); to provide members' balance of 🪙.";
    }

    @Override
    public String getFlagsDescription() {

        StringBuilder sb = new StringBuilder();

        sb.append("The list of all available **'").append(CURRENCY_COMMAND_NAME).append("'** flags:\n\n");

        for (CurrencyFlag currencyFlag : currencyFlags.values()) {
            sb.append(currencyFlag.getDescription()).append("\n\n");
        }

        sb.append(COMMAND_NOTE);
        return sb.toString();
    }

    @Override
    public String getName() {
        return CURRENCY_COMMAND_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> flags) {
        if (flags.isEmpty())
            return processWithNoParametersOrFlags(message);

        for (CurrencyFlag currencyFlag : currencyFlags.values()) {
            if (currencyFlag.getName().equals(flags.get(0))) {
                return currencyFlag.process(message, getNextCommandPartsToParse(flags));
            }
        }
        return nonexistentCommand.process(message, null);
    }


    private Mono<Object> processWithNoParametersOrFlags(Message message) {
        Long authorDiscordId = getMessageAuthorDiscordId(message);
        Student student = studentService.getStudent(authorDiscordId);


        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseMemberInfoCurrency(student, getMemberInfo(message)))
                );
    }

    private EmbedCreateSpec constructResponseMemberInfoCurrency(Student student, MemberInfo memberInfo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd. MMMM yyyy", Locale.GERMAN);
        LocalDate defrostDate = student.getBalanceDefrostDate();
        String defrostDateStr = "";

        if (defrostDate != null) {
            defrostDateStr = " ❄ " + defrostDate.format(formatter);
        }

        return buildUserInfoMessage(
                "Währungssaldo",
                "@" + memberInfo.getUsername()
                        + "#" + memberInfo.getDiscriminator()
                        + "\n" + String.format("%.2f", student.getGoldBalance()) + " 🪙\n"
                        + defrostDateStr,
                memberInfo.getAvatar()
        );
    }
}