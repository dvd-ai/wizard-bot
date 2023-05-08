package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HelpCommand implements Command {

    @Override
    public String getDescription() {
        return
                """
                **I am a wizard that:**
               
                \t⭐ monitors member's activity and takes actions accordingly;
                \t⭐ makes possible to play games with me;
                \t⭐ makes possible to deal with your currency;
                
                """
        ;
    }


    public MessageCreateSpec getFormattedResponseMessage() {
        return MessageCreateSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .color(Color.VIVID_VIOLET)
                        .title("Help")
                        .description(getDescription())
                        .build()
                ).build();

    }

    @Override
    public Mono<Object> process(Message message, String goal) {

        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(getFormattedResponseMessage()));
    }
}
