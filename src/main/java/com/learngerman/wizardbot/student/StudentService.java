package com.learngerman.wizardbot.student;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void addStudent(Student student) {
        studentRepository.addStudent(student);
    }

    public int[] addStudents(List<Student> students) {
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

    public void freezeStudentBalanceTillDefrostDate(LocalDate defrostDate, Long studentDiscordId) {
        studentRepository.freezeStudentBalanceTillDefrostDate(defrostDate, studentDiscordId);
    }

    public void unfreezeStudentBalanceTillDefrostDate(Long studentDiscordId) {
        studentRepository.unfreezeStudentBalanceTillDefrostDate(studentDiscordId);
    }

    public float getStudentGoldCurrency(Long studentDiscordId) {
        return studentRepository.getStudentGoldCurrency(studentDiscordId);
    }

    public LocalDate getStudentDefrostDate(Long studentDiscordId) {
        return studentRepository.getStudentDefrostTime(studentDiscordId);
    }
}
