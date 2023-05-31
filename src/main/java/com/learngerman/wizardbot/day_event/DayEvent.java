package com.learngerman.wizardbot.day_event;

import com.learngerman.wizardbot.currency.CurrencyReport;
import com.learngerman.wizardbot.student.StudentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.learngerman.wizardbot.currency.GoldCurrency.GOLD_DAY_TAX;


@Service
public class DayEvent {

    private final StudentService studentService;
    private final CurrencyReport currencyReport;

    public DayEvent(StudentService studentService, CurrencyReport currencyReport) {
        this.studentService = studentService;
        this.currencyReport = currencyReport;
    }

    @Scheduled(cron = "0 * * * * *", zone = "Europe/Berlin")
    public void process() {
        studentService.decreaseAllStudentsGoldCurrencyBy(GOLD_DAY_TAX);
        currencyReport.send();
        studentService.updateFreezeStatus();
    }
}
