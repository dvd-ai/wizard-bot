package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component

public class NonexistentCommand implements Command {


    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Mono<Object> process(Message message, List<String> commandParts) {

        return null;
    }
}
