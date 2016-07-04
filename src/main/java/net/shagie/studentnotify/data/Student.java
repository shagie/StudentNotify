package net.shagie.studentnotify.data;

import java.util.List;

public class Student {
    private String id;
    private List<Semester> grades;
    private String name;
    private List<Notification> notifications;

    public Student() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
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
