package net.shagie.studentnotify.data;

import java.util.List;
import java.util.Map;

public class Student {
    private String id;
    private List<Semester> grades;
    private String name;
    private Map<String, List<Integer>> notifications;

    public Student() {
    }

    public Student(String id, List<Semester> grades, String name, Map<String, List<Integer>> notifications) {
        this.id = id;
        this.grades = grades;
        this.name = name;
        this.notifications = notifications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<Integer>> getNotifications() {
        return notifications;
    }

    public void setNotifications(Map<String, List<Integer>> notifications) {
        this.notifications = notifications;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", grades=" + grades +
                ", name='" + name + '\'' +
                ", notifications=" + notifications +
                '}';
    }
}
