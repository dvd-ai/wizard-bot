package com.learngerman.wizardbot.day_event;

import com.learngerman.wizardbot.student.StudentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.learngerman.wizardbot.currency.GoldCurrency.GOLD_DAY_TAX;

@Service
public class DayEvent {

    private final StudentService studentService;

    public DayEvent(StudentService studentService) {
        this.studentService = studentService;
    }

    @Scheduled(cron = "0 0 9 * * ?", zone="Europe/Berlin")
    public Mono<Void>process() {
        studentService.decreaseStudentsGoldCurrencyBy(GOLD_DAY_TAX);
        //try
        //send report about the students to set channel

        return Mono.empty();
    }
}
