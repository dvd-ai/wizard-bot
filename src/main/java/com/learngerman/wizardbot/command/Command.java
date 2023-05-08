package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface Command {

    String getDescription();

    Mono<Object> process(Message message, String goal);

}
