package com.learngerman.wizardbot.student;

import java.util.Comparator;

public class StudentComparator implements Comparator<Student> {

    @Override
    public int compare(Student o1, Student o2) {
        return Float.compare(o2.getGoldBalance(), o1.getGoldBalance());
    }
}
