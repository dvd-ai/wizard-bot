package com.learngerman.wizardbot.command;

import com.learngerman.wizardbot.student.Student;
import com.learngerman.wizardbot.student.StudentService;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learngerman.wizardbot.currency.GoldCurrency.GOLD_START_CAPITAL;
import static com.learngerman.wizardbot.util.ResponseMessageBuilder.buildUsualMessage;

@Component
public class AddCommand implements Command {
    private final NonexistentCommand nonexistentCommand;
    private final StudentService studentService;

    public AddCommand(NonexistentCommand nonexistentCommand, StudentService studentService) {
        this.nonexistentCommand = nonexistentCommand;
        this.studentService = studentService;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Mono<Object> process(Message message, List<String> flags) {
        if (flags.isEmpty())
            return nonexistentCommand.process(message, null);

        return switch (flags.get(0)) {
            case "new" -> processNew(message);
            default -> nonexistentCommand.process(message, null);
        };

    }

    private Mono<Object> processNew(Message message) {
        addNewStudents(message);
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
