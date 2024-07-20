package com.learngerman.wizardbot.util;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import static com.learngerman.wizardbot.util.PageUtils.getCurrentPageNumberFromMessage;

public class ButtonUtil {
    private ButtonUtil() {
    }

    public static ActionRow createAndFormateButtons(int pageToShow, int totalPageAmount) {
        Button nextButton = Button.success("next_page", "Weiter");
        Button previousButton = Button.primary("prev_page", "Zurück");

        if (pageToShow == totalPageAmount && pageToShow == 1) {
            previousButton = previousButton.disabled(true);
            nextButton = nextButton.disabled(true);
        } else if (pageToShow == 1) {
            previousButton = previousButton.disabled(true);
        } else if (pageToShow == totalPageAmount) {
            nextButton = nextButton.disabled(true);
        }

        return ActionRow.of(previousButton, nextButton);
    }

    public static boolean isMessageTheSame(Message reportMessage, ButtonInteractionEvent event) {
        return event.getMessageId().equals(reportMessage.getId());
    }

    public static int updatePageNumberAccordingToClickedButton(String buttonId, int pageNumber) {
        if ("prev_page".equals(buttonId) && pageNumber > 0) {
            pageNumber -= 1;
        } else if ("next_page".equals(buttonId)) {
            pageNumber += 1;
        }
        return pageNumber;
    }

    public static Mono<Void> acknowledgeAndDeleteReply(ButtonInteractionEvent event) {
        return event.deferReply().then(event.deleteReply());
    }

    public static int getPageNumberToShowNext(ButtonInteractionEvent event) {
        String buttonId = event.getCustomId();
        int pageNumber = getCurrentPageNumberFromMessage(event.getMessage().get());
        return updatePageNumberAccordingToClickedButton(buttonId, pageNumber);
    }
}
