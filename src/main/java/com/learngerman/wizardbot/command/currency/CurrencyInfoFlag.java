package com.learngerman.wizardbot.command.currency;

import com.learngerman.wizardbot.command.CurrencyFlag;
import com.learngerman.wizardbot.command.MemberInfo;
import com.learngerman.wizardbot.command.NonexistentCommand;
import com.learngerman.wizardbot.event.MessageListenerManager;
import com.learngerman.wizardbot.student.Student;
import com.learngerman.wizardbot.student.StudentComparator;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.learngerman.wizardbot.Wizard.PAGE_SIZE;
import static com.learngerman.wizardbot.command.MessageEventUtils.extractDiscordIdFromMention;
import static com.learngerman.wizardbot.command.MessageEventUtils.getMemberInfo;
import static com.learngerman.wizardbot.command.currency.CurrencyFlagName.INFO_FLAG_NAME;
import static com.learngerman.wizardbot.error.ErrorDescription.NO_PARAMETERS_ERROR;
import static com.learngerman.wizardbot.util.ButtonUtil.*;
import static com.learngerman.wizardbot.util.GuildUtil.getStudentsInfosMapMono;
import static com.learngerman.wizardbot.util.PageUtils.createEditedPagedMessage;
import static com.learngerman.wizardbot.util.PageUtils.createPagedMessage;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUserInfoMessage;

@Component
public class CurrencyInfoFlag implements CurrencyFlag {

    private static final String TITLE_ALL = "Studentenliste mit Geldinformation";
    private final CurrencyValidator currencyValidator;
    private final NonexistentCommand nonexistentCommand;
    private final StudentService studentService;

    private final MessageListenerManager listenerManager;

    public CurrencyInfoFlag(CurrencyValidator currencyValidator, NonexistentCommand nonexistentCommand, StudentService studentService, MessageListenerManager listenerManager) {
        this.currencyValidator = currencyValidator;
        this.nonexistentCommand = nonexistentCommand;
        this.studentService = studentService;
        this.listenerManager = listenerManager;
    }


    @Override
    public String getDescription() {
        return String.format("""
                **%s <alle>** - erhält eine „Rangliste“ und einen Währungsstatus aller Studenten.
                                
                **%s <@Erwähnung von einem Studenten>** - erhält einen Währungsstatus eines bestimmten Studenten.
                """, INFO_FLAG_NAME, INFO_FLAG_NAME);
    }

    @Override
    public String getName() {
        return INFO_FLAG_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> parameters) {
        if (parameters.isEmpty())
            return nonexistentCommand.process(message, NO_PARAMETERS_ERROR);

        return switch (parameters.get(0)) {
            case "alle" -> processAll(message);
            default -> processSpecifiedStudent(message, parameters);
        };
    }

    private Mono<Object> processAll(Message message) {
        List<Student> students = studentService.getStudents(0, PAGE_SIZE);
        Mono<Map<Student, MemberInfo>> mapMono = getStudentsInfosMapMono(students, message.getGuild());
        processStudentsInfo(mapMono, message.getChannelId(), message.getGuild(), message.getClient());
        return Mono.empty();
    }

    private void processStudentsInfo(Mono<Map<Student, MemberInfo>> studentMap, Snowflake channelId, Mono<Guild> guildMono, GatewayDiscordClient client) {
        final int[] totalPageAmount = new int[1];

        Mono<Message> messageMono = studentMap.flatMap(studentsMap -> {
            Integer studentsAmount = studentService.calculateStudents();
            totalPageAmount[0] = (int) Math.ceil((double) studentsAmount / PAGE_SIZE);
            String pageDescription = formatPageDescription(studentsMap, 0);

            return createPagedMessage(channelId, pageDescription, TITLE_ALL, 1, totalPageAmount[0], client);
        });

        messageMono.doOnNext(message -> {
            Disposable dis = handleButtonClicks(message, guildMono, totalPageAmount[0]).subscribe();
            listenerManager.disposeListener(message, dis, Duration.ofMinutes(30));
        }).subscribe();
    }

    private Flux<Void> handleButtonClicks(Message reportMessage, Mono<Guild> guildMono, int totalPageAmount) {
        return reportMessage.getClient().on(ButtonInteractionEvent.class)
                .filter(event -> isMessageTheSame(reportMessage, event))
                .flatMap(event -> processButtonInteractionEvent(event, guildMono, reportMessage, totalPageAmount));
    }

    private Mono<Void> processButtonInteractionEvent(ButtonInteractionEvent event, Mono<Guild> guildMono,
                                                     Message reportMessage, int totalPageAmount) {
        int pageToShow = getPageNumberToShowNext(event);
        List<Student> students = studentService.getStudents(pageToShow - 1, PAGE_SIZE);
        Mono<Map<Student, MemberInfo>> mapMono = getStudentsInfosMapMono(students, guildMono);

        return mapMono
                .flatMap(memberInfos -> {
                    String newReportContent = formatPageDescription(memberInfos, pageToShow - 1);
                    return createEditedPagedMessage(newReportContent, TITLE_ALL, reportMessage, pageToShow, totalPageAmount);
                })
                .then(acknowledgeAndDeleteReply(event));
    }

    private String formatPageDescription(Map<Student, MemberInfo> studentsMap, int ten) {
        if (studentsMap.isEmpty())
            return null;

        List<Student> studentKeys = getSortedStudents(studentsMap);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < studentsMap.size(); i++) {
            Student student = studentKeys.get(i);
            result = appendNumeration(result, ten, i);
            result = appendStudentInfo(result, studentsMap.get(student), student);

            LocalDate defrostDate = student.getBalanceDefrostDate();
            if (defrostDate != null) {
                result = appendDefrostDate(result, defrostDate);
            }
            result.append("\n");
        }

        return result.toString();
    }

    private StringBuilder appendNumeration(StringBuilder sb, int ten, int row) {
        return sb.append("**").append(ten * 10 + row + 1).append(". **");
    }

    private StringBuilder appendStudentInfo(StringBuilder sb, MemberInfo studentInfo, Student student) {
        return sb.append(studentInfo.getUsername()).append(" ")
                .append(String.format("**%.2f", student.getGoldBalance())).append("**  🪙");
    }

    private StringBuilder appendDefrostDate(StringBuilder sb, LocalDate defrostDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd. MMMM yyyy", Locale.GERMAN);
        return sb.append(" ❄️").append(defrostDate.format(formatter));
    }

    private List<Student> getSortedStudents(Map<Student, MemberInfo> studentMemberInfoMap) {
        return studentMemberInfoMap.keySet()
                .stream()
                .sorted(new StudentComparator())
                .toList();
    }


    private Mono<Object> processSpecifiedStudent(Message message, List<String> parameters) {
        currencyValidator.checkDiscordId(parameters.get(0));

        Long discordUserId = Long.valueOf(extractDiscordIdFromMention(parameters.get(0)));

        Student student = studentService.getStudent(discordUserId);

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseMemberInfoCurrency(student, getMemberInfo(message, discordUserId)))
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
                        + "\n" + String.format("**%.2f", student.getGoldBalance()) + "** 🪙\n"
                        + defrostDateStr,
                memberInfo.getAvatar()
        );
    }
}
