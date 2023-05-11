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
                        .description(description)
                        .build()
                ).build()
                ;
    }

    public static EmbedCreateSpec buildUserInfoMessage(String title, String description, String memberAvatarLink) {

        return EmbedCreateSpec.builder()
                .color(Color.VIVID_VIOLET)
                .title(title)
                .thumbnail(memberAvatarLink)
                .description(description)
                .build();


//        return MessageCreateSpec.builder()
//                .addEmbed(EmbedCreateSpec.builder()
//                        .color(Color.VIVID_VIOLET)
//                        .title(title)
//                        .description( description)
//                        .author(EmbedCreateFields.Author.of("@" + title, null, avatarLink))
//                        .image(avatarLink)
//                        .build()
//                ).build()

    }
}
