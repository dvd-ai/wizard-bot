package com.learngerman.wizardbot.currency;

import com.learngerman.wizardbot.channel.Channel;
import com.learngerman.wizardbot.channel.ChannelService;
import com.learngerman.wizardbot.command.MemberInfo;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.learngerman.wizardbot.Wizard.PAGE_SIZE;
import static com.learngerman.wizardbot.util.ButtonUtil.*;
import static com.learngerman.wizardbot.util.GuildUtil.getGuildMono;
import static com.learngerman.wizardbot.util.GuildUtil.getStudentsInfosMono;
import static com.learngerman.wizardbot.util.PageUtils.createEditedPagedMessage;
import static com.learngerman.wizardbot.util.PageUtils.createPagedMessage;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUsualMessage;

@Component
public class CurrencyReport {

    private final ChannelService channelService;
    private final StudentService studentService;
    private final GatewayDiscordClient client;


    public CurrencyReport(ChannelService channelService, StudentService studentService, GatewayDiscordClient client) {
        this.channelService = channelService;
        this.studentService = studentService;
        this.client = client;
    }


    public void send() {
        List<Channel> channelsForReport = channelService.getChannelsForReport();
        if (channelsForReport.isEmpty())
            return;
        //i have to test scenario when someone leaves the guild (before the report)
        List<Long>studentIdsWithNoCurrency = studentService.getStudentIdsWithNoCurrency(0, PAGE_SIZE);
        sendReportsToChannels(channelsForReport, studentIdsWithNoCurrency);
    }


    private void sendReportsToChannels(List<Channel> channelsForReport, List<Long>studentIdsWithNoCurrency) {
        for (Channel channel: channelsForReport) {
            Snowflake channelId = Snowflake.of(channel.getChannelId());
            Mono<Guild> guildMono = getGuildMono(channelId, client);

            if (studentIdsWithNoCurrency.isEmpty())
                noPoorStudentsMessage(channelId);
            else {
                Mono<List<MemberInfo>> studentInfoMono = getStudentsInfosMono(studentIdsWithNoCurrency, guildMono);
                processStudentsInfo(studentInfoMono, channelId, guildMono);
            }
        }
    }

    private void processStudentsInfo(Mono<List<MemberInfo>> studentInfoMono, Snowflake channelId, Mono<Guild> guildMono) {
        final int[] totalPageAmount = new int[1];

        studentInfoMono.flatMap(studentInfoList -> {
            Integer studentAmountWithNoCurrency = studentService.calculateStudentsWithZeroOrLessCurrency();
            totalPageAmount[0] = (int) Math.ceil((double) studentAmountWithNoCurrency / PAGE_SIZE);
            String reportContent = formatReportDescription(studentInfoList, 0);

            return createPagedMessage(channelId, reportContent, getTitle(), 1, totalPageAmount[0],  client);
        }).subscribe(message -> handleButtonClicks(message, guildMono, totalPageAmount[0]).subscribe());
    }

    private Flux<Void> handleButtonClicks(Message reportMessage, Mono<Guild> guildMono, int totalPageAmount) {
        return reportMessage.getClient().on(ButtonInteractionEvent.class)
                .filter(event -> isMessageTheSame(reportMessage, event))
                .flatMap(event -> processButtonInteractionEvent(event, guildMono, reportMessage, totalPageAmount));
    }

    private Mono<Void> processButtonInteractionEvent(ButtonInteractionEvent event, Mono<Guild> guildMono,
                                                     Message reportMessage, int totalPageAmount) {
        int pageToShow = getPageNumberToShowNext(event);
        List<Long>studentIdsWithNoCurrency = studentService.getStudentIdsWithNoCurrency(pageToShow - 1, PAGE_SIZE);
        Mono<List<MemberInfo>> memberInfosMono = getStudentsInfosMono(studentIdsWithNoCurrency, guildMono);

        return memberInfosMono
                .flatMap(memberInfos -> {
                    String newReportContent = formatReportDescription(memberInfos, pageToShow - 1);
                    return createEditedPagedMessage(newReportContent, getTitle(), reportMessage, pageToShow, totalPageAmount);
                })
                .then(acknowledgeAndDeleteReply(event));
    }

    private String formatReportDescription(List<MemberInfo> studentInfoList, int ten) {
        if (studentInfoList.isEmpty())
            return null;

        StringBuilder reportDescription = new StringBuilder();

        for (int i = 0; i < studentInfoList.size(); i++) {
            reportDescription.append("**").append(ten * 10 + i + 1).append(". **")
                    .append(studentInfoList.get(i).getUsername()).append("#")
                    .append(studentInfoList.get(i).getDiscriminator())
                    .append("\n");

        }

        return reportDescription.toString();
    }

    private void noPoorStudentsMessage(Snowflake channelId) {
        client.getChannelById(channelId).ofType(MessageChannel.class)
                .flatMap(textChannel ->
                        textChannel.createMessage(
                                buildUsualMessage(getTitle(),
                                        "Alle Studenten haben 🪙 mehr als 0.")
                        )).subscribe();
    }

    private String getTitle() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd. MMMM yyyy", Locale.GERMAN);
        return "Tagesbericht " + LocalDate.now().format(formatter) + "\n Die Liste der Studenten, die weniger oder gleich 0 🪙 haben.";
    }
}

