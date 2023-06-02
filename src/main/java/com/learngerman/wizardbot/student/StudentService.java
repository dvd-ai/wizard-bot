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

    public int[] grantAllStudentsGoldCurrency(float goldAmount) {
        return studentRepository.increaseStudentsGoldCurrencyBy(goldAmount);
    }

    public int[] confiscateAllStudentsGoldCurrency(float goldAmount) {
        return studentRepository.decreaseStudentsGoldCurrencyBy(goldAmount);
    }

    public void increaseStudentGoldCurrencyBy(float goldAmount, Long studentDiscordId) {
        if (getStudentDefrostDate(studentDiscordId) == null)
            studentRepository.increaseStudentGoldCurrencyByDiscordId(goldAmount, studentDiscordId);
    }

    public void decreaseStudentGoldCurrencyByDiscordId(float goldAmount, Long studentDiscordId) {
        if (getStudentDefrostDate(studentDiscordId) == null)
            studentRepository.decreaseStudentGoldCurrencyByDiscordId(goldAmount, studentDiscordId);
    }

    public void decreaseAllStudentsGoldCurrencyBy(float goldAmount) {
        studentRepository.decreaseUnfreezedStudentsGoldCurrencyBy(goldAmount);
    }
  
    public void confiscateStudentGoldCurrencyByDiscordId(float goldAmount, Long studentDiscordId) {
        studentRepository.decreaseStudentGoldCurrencyByDiscordId(goldAmount, studentDiscordId);
    }

    public void grantStudentGoldCurrencyByDiscordId(float goldAmount, Long studentDiscordId) {
        studentRepository.increaseStudentGoldCurrencyByDiscordId(goldAmount, studentDiscordId);
    }

    public void setStudentEngagement(boolean isEngaged, Long studentDiscordId) {
        studentRepository.setStudentEngagement(isEngaged, studentDiscordId);
    }

    public void freezeStudentBalanceTillDefrostDate(LocalDate defrostDate, Long studentDiscordId) {
        studentRepository.freezeStudentBalanceTillDefrostDate(defrostDate, studentDiscordId);
    }

    public void unfreezeStudentBalance(Long studentDiscordId) {
        studentRepository.unfreezeStudentBalance(studentDiscordId);
    }

    public float getStudentGoldCurrency(Long studentDiscordId) {
        return studentRepository.getStudentGoldCurrency(studentDiscordId);
    }

    public LocalDate getStudentDefrostDate(Long studentDiscordId) {
        return studentRepository.getStudentDefrostDate(studentDiscordId);
    }

    public void updateFreezeStatus() {
        studentRepository.updateFreezeStatus();
    }

    public List<Long> getStudentIdsWithNoCurrency(int page, int size) {
        return studentRepository.getStudentIdsWithNoCurrency(page, size);
    }

    public Integer calculateStudentsWithZeroOrLessCurrency() {
        return studentRepository.calculateStudentsWithZeroOrLessCurrency();
    }

    public Integer calculateStudents() {
        return studentRepository.calculateStudents();
    }

    public List<Student> getStudents(int page, int size) {
        return studentRepository.getStudents(page, size);
    }

    public Student getStudent(Long studentId) {
        return studentRepository.getStudent(studentId);
    }

    public void setStudentPresenceInGuild(Long studentId, boolean isPresent) {
        studentRepository.setStudentPresenceInGuild(studentId, isPresent);
    }
}
