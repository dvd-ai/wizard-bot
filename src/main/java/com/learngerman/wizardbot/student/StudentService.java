package com.learngerman.wizardbot.student;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class StudentService {

    private final float START_GOLD_CAPITAL = 25f;
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Long addStudent(Student student) {
        student.setGoldBalance(START_GOLD_CAPITAL);
        return studentRepository.addStudent(student);
    }

    public int[] addStudents(List<Student> students) {
        students.forEach(student -> student.setGoldBalance(START_GOLD_CAPITAL));
        return studentRepository.addStudents(students);
    }

    public int[] increaseStudentsGoldCurrencyBy(float goldAmount) {
        return studentRepository.increaseStudentsGoldCurrencyBy(goldAmount);
    }

    public int[] decreaseStudentsGoldCurrencyBy(float goldAmount) {
        return studentRepository.decreaseStudentsGoldCurrencyBy(goldAmount);
    }

    public void increaseStudentGoldCurrencyBy(float goldAmount, Long studentDiscordId) {
        studentRepository.increaseStudentGoldCurrencyByDiscordId(goldAmount, studentDiscordId);
    }

    public void decreaseStudentGoldCurrencyByDiscordId(float goldAmount, Long studentDiscordId) {
        studentRepository.decreaseStudentGoldCurrencyByDiscordId(goldAmount, studentDiscordId);
    }

    public void setStudentEngagement(boolean isEngaged, Long studentDiscordId) {
        studentRepository.setStudentEngagement(isEngaged, studentDiscordId);
    }

    public void freezeStudentBalanceTillDefrostDate(OffsetDateTime defrostDate, Long studentDiscordId) {
        studentRepository.freezeStudentBalanceTillDefrostDate(defrostDate, studentDiscordId);
    }

    public void unfreezeStudentBalanceTillDefrostDate(Long studentDiscordId) {
        studentRepository.unfreezeStudentBalanceTillDefrostDate(studentDiscordId);
    }
}
