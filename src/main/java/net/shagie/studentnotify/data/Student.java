package net.shagie.studentnotify.data;

import java.util.List;

public class Student {
    private String studentId;
    private List<Semester> grades;
    private String name;

    public Student() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<Semester> getGrades() {
        return grades;
    }

    public void setGrades(List<Semester> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", grades=" + grades +
                ", name='" + name + '\'' +
                '}';
    }
}
