package net.shagie.student.data;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class Student {
    @SerializedName("_id")
    private String documentId;
    @SerializedName("_rev")
    private String revision;
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

    public String getDocumentId() {
        return documentId;
    }

    public String getRevision() {
        return revision;
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
