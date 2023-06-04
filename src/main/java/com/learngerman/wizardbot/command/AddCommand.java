package com.learngerman.wizardbot.command;

import com.learngerman.wizardbot.student.Student;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.command.CommandName.ADD_COMMAND_NAME;
import static com.learngerman.wizardbot.command.MessageEventUtils.checkMessageAuthorPermissions;
import static com.learngerman.wizardbot.currency.GoldCurrency.GOLD_START_CAPITAL;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUsualMessage;

@Component
public class AddCommand implements Command {
    private final StudentService studentService;

    public AddCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public String getCommandDescription() {
        return "**" + ADD_COMMAND_NAME + "**"
                + " - fügt alle vorhandenen Studenten zum „Währungsbanksystem“ hinzu (nur einmal | als der Bot nicht verfügbar war und nur für Administratoren).";
    }

    @Override
    public String getFlagsDescription() {
        return getCommandDescription();
    }

    @Override
    public String getName() {
        return ADD_COMMAND_NAME;
    }

    @Override
    public Mono<Object> process(Message message, List<String> flags) {
        return processNew(message);
    }

    private Mono<Object> processNew(Message message) {
        addNewStudents(message);
        checkMessageAuthorPermissions(message);
        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        constructResponseAddNewStudentsMessage())
                )
                ;
    }

    private MessageCreateSpec constructResponseAddNewStudentsMessage() {
        return buildUsualMessage(
                "Gemacht!",
                "Neue Studenten wurden hinzugefügt!"
        );
    }

    private void addNewStudents(Message message) {
        message.getGuild()
                .flatMapMany(Guild::getMembers)
                .filter(member -> !member.isBot())
                .doOnNext(member -> studentService.addStudent(
                                new Student(
                                        member.getId().asLong(), GOLD_START_CAPITAL,
                                        false, null,
                                        true)
                        )
                ).subscribe();
    }
}
