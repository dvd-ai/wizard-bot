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

import static com.learngerman.wizardbot.command.MessageEventUtils.*;
import static com.learngerman.wizardbot.command.channel.ChannelSettingsFlagName.CURRENCY_IGNORANCE_FLAG_NAME;
import static com.learngerman.wizardbot.error.ErrorDescription.NO_PARAMETERS_ERROR;

@Component
public class ChannelSettingsCurrencyIgnoranceFlag implements ChannelSettingsFlag {

    private final NonexistentCommand nonexistentCommand;
    private final ChannelService channelService;
    private final ChannelValidator channelValidator;

    public ChannelSettingsCurrencyIgnoranceFlag(NonexistentCommand nonexistentCommand, ChannelService channelService, ChannelValidator channelValidator) {
        this.nonexistentCommand = nonexistentCommand;
        this.channelService = channelService;
        this.channelValidator = channelValidator;
    }

    @Override
    public String getDescription() {
        return String.format(
                "!**%s <#Erwähnung von einem Textkanal> <1 | 0>** - legt einen Kanal für die Ignoranz der Währungsakkumulation fest (1 – Ignoranz anfangen, 0 – Ignoranz beenden).%n",
                CURRENCY_IGNORANCE_FLAG_NAME
        );
    }

    @Override
    public String getName() {
        return CURRENCY_IGNORANCE_FLAG_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> parameters) {
        if (parameters.isEmpty())
            return nonexistentCommand.process(message, NO_PARAMETERS_ERROR);

        channelValidator.validate(parameters, message);
        checkMessageAuthorPermissions(message);

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
                        .title("Kanaleinstellungen -> Unwissenheit über Währungen")
                        .description("Änderungen wurden gespeichert!")
                        .build()
                ).build()
                ;
    }

    private void processChannels(List<String> parameters) {
        boolean definer = extractTrueFalseDefiner(parameters.get(parameters.size() - 1));

        for (int i = 0; i < parameters.size() - 1; i++) {
            String channelId = extractChannelIdFromMention(parameters.get(i));
            channelService.setChannelForCurrencyOperationsIgnorance(
                    new Channel(
                            Long.valueOf(channelId),
                            false,
                            definer
                    )
            );
        }
    }
}

