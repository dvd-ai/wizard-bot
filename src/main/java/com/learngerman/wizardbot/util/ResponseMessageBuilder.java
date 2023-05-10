package com.learngerman.wizardbot.util;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;

public class ResponseMessageBuilder {

    private ResponseMessageBuilder() {
    }

    public static MessageCreateSpec buildUsualMessage(String title, String description) {
        return MessageCreateSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .color(Color.VIVID_VIOLET)
                        .title(title)
                        .description( description)
                        .build()
                ).build()
        ;
    }
}
