package net.shagie.student.data;

public class Notification {
    private int notificationType;
    private String student;
    private int semester;
    private String message;

    public Notification() {
    }

    public Notification(int notificationType, String student, int semester, String message) {
        this.notificationType = notificationType;
        this.student = student;
        this.semester = semester;
        this.message = message;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationType=" + notificationType +
                ", student='" + student + '\'' +
                ", semester=" + semester +
                ", message='" + message + '\'' +
                '}';
    }
}
