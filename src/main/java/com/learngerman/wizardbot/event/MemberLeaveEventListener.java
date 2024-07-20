package com.learngerman.wizardbot.event;

import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.event.domain.guild.MemberLeaveEvent;
import discord4j.core.object.entity.Member;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class MemberLeaveEventListener implements EventListener<MemberLeaveEvent> {

    private final StudentService studentService;

    public MemberLeaveEventListener(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Class<MemberLeaveEvent> getEventType() {
        return MemberLeaveEvent.class;
    }

    @Override
    public Mono<Void> execute(MemberLeaveEvent event) {
        Optional<Member> memberOpt = event.getMember();

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            if (!member.isBot()) {
                long studentId = member.getId().asLong();
                studentService.setStudentPresenceInGuild(studentId, false);
            }
        }

        return Mono.empty();

    }
}
