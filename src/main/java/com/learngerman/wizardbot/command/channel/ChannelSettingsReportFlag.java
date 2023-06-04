package com.learngerman.wizardbot.command.channel;

import com.learngerman.wizardbot.channel.Channel;
import com.learngerman.wizardbot.channel.ChannelService;
import com.learngerman.wizardbot.command.NonexistentCommand;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.MessageEventUtils.extractChannelIdFromMention;
import static com.learngerman.wizardbot.command.MessageEventUtils.extractTrueFalseDefiner;
import static com.learngerman.wizardbot.command.channel.ChannelSettingsFlagName.SEND_REPORT_FLAG_NAME;


@Component
public class ChannelSettingsReportFlag implements ChannelSettingsFlag {

    private final NonexistentCommand nonexistentCommand;
    private final ChannelService channelService;
    private final ChannelValidator channelValidator;

    public ChannelSettingsReportFlag(NonexistentCommand nonexistentCommand, ChannelService channelService, ChannelValidator channelValidator) {
        this.nonexistentCommand = nonexistentCommand;
        this.channelService = channelService;
        this.channelValidator = channelValidator;
    }

    @Override
    public String getDescription() {
        return String.format("!**%s <#Erwähnung von einem Textkanal> <1 | 0>** - legt einen Kanal fürs Senden von Berichten fest (1 - Senden anfangen, 0 - Senden beenden).", SEND_REPORT_FLAG_NAME);
    }

    @Override
    public String getName() {
        return SEND_REPORT_FLAG_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> parameters) {
        if (parameters.isEmpty())
            return nonexistentCommand.process(message, null);

        channelValidator.validate(parameters);
        processChannels(parameters);
        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseMessage())
                )
                ;

    }

    public MessageCreateSpec constructResponseMessage() {
        return MessageCreateSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .color(Color.VIVID_VIOLET)
                        .title("Kanaleinstellungen -> Bericht")
                        .description("Änderungen wurden gespeichert!")
                        .build()
                ).build()
                ;
    }

    private void processChannels(List<String> parameters) {
        boolean definer = extractTrueFalseDefiner(parameters.get(parameters.size() - 1));

        for (int i = 0; i < parameters.size() - 1; i++) {
            String channelId = extractChannelIdFromMention(parameters.get(i));
            channelService.setChannelForReports(
                    new Channel(
                            Long.valueOf(channelId),
                            definer,
                            false
                    )
            );
        }
    }
}
