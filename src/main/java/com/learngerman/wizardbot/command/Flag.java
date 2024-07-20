package com.learngerman.wizardbot.command;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import java.util.List;

public interface Flag {

    String getDescription();

    String getName();

    Mono<Object> process(Message message, List<String> parameters);

}
