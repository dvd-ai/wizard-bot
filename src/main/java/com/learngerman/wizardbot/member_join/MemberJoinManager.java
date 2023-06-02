package com.learngerman.wizardbot.member_join;

import com.learngerman.wizardbot.student.Student;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Member;
import org.springframework.stereotype.Component;

import static com.learngerman.wizardbot.currency.GoldCurrency.GOLD_START_CAPITAL;

@Component
public class MemberJoinManager {

    private final StudentService studentService;

    public MemberJoinManager(StudentService studentService) {
        this.studentService = studentService;
    }

    public void process(Member member) {
        long memberId = member.getId().asLong();
        studentService.addStudent(
                new Student(memberId, GOLD_START_CAPITAL,
                        false, null,
                        true)
        );
    }
}
