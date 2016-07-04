package net.shagie.studentnotify.data;

public class Notification {
    private int id;
    private int semester;
    private String message;

    public Notification() {
    }

    public Notification(int id, int semester, String message) {
        this.id = id;
        this.semester = semester;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", semester=" + semester +
                ", message='" + message + '\'' +
                '}';
    }
}
