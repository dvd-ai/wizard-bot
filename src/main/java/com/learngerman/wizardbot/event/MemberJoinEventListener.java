package com.learngerman.wizardbot.event;

import com.learngerman.wizardbot.member_join.MemberJoinManager;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MemberJoinEventListener implements EventListener<MemberJoinEvent>{
    private final MemberJoinManager memberJoinManager;

    public MemberJoinEventListener(MemberJoinManager memberJoinManager) {
        this.memberJoinManager = memberJoinManager;
    }

    @Override
    public Class<MemberJoinEvent> getEventType() {
        return MemberJoinEvent.class;
    }

    @Override
    public Mono<Void> execute(MemberJoinEvent event) {
        event.getMember().asFullMember()
                .filter(member -> !member.isBot())
                .doOnNext(memberJoinManager::process)
                .subscribe()
        ;
        return Mono.empty();
    }
}
