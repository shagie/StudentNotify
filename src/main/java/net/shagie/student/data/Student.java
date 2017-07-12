package net.shagie.student.data;

import java.util.LinkedList;
import java.util.List;

public class Student {
    private String studentId;
    private Name name;
    private List<Semester> grades;
    /**
     * couch recordType field
     */
    private final String recordType = "student";

    public Student() {
        grades = new LinkedList<>();
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
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
        if (grades == null) {
            this.grades = new LinkedList<>();
        }
    }

    @SuppressWarnings("unused")
    public String getRecordType() {
        return recordType;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name=" + name +
                ", grades=" + grades +
                ", recordType='" + recordType + '\'' +
                '}';
    }
}
