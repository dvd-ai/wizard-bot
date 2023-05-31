package com.learngerman.wizardbot.util;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageEditMono;
import reactor.core.publisher.Mono;

import static com.learngerman.wizardbot.util.ButtonUtil.createAndFormateButtons;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUsersInfoMessage;

public class PageUtils {

    private PageUtils() {
    }

    public static int getCurrentPageNumberFromMessage(Message message) {
        return Integer.parseInt(message.getEmbeds()
                .stream()
                .filter(embed -> embed.getFooter().isPresent())
                .findFirst().get().getFooter().get().getText().replaceAll("[\\sA-Za-z]", "").substring(0, 1));
    }

    public static String formatPageNumber(int finalCurrentPage, int totalPageAmount) {
        return "Seite " + finalCurrentPage + "/" + totalPageAmount;
    }

    public static MessageEditMono createEditedPagedMessage(String newContent, String title,
                                                           Message messageToEdit, int pageToShow, int totalPageAmount) {
        return messageToEdit.edit()
                .withEmbeds(buildUsersInfoMessage(title, newContent, formatPageNumber(pageToShow, totalPageAmount)))
                .withComponents(createAndFormateButtons(pageToShow, totalPageAmount));
    }

    public static Mono<Message> createPagedMessage(Snowflake channelId, String content, String title,
                                             int pageToShow, int totalPageAmount, GatewayDiscordClient client) {
        return client.getChannelById(channelId).ofType(MessageChannel.class)
                .flatMap(textChannel ->
                        textChannel.createMessage(
                                buildUsersInfoMessage(
                                        title, content, formatPageNumber(pageToShow, totalPageAmount)
                                )
                        ).withComponents(createAndFormateButtons(pageToShow, totalPageAmount)));
    }


}
