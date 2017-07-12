package net.shagie.student.data;

public class Notification implements Comparable<Notification> {
    private int notificationType;
    private String studentId;
    private int semester;
    private String message;
    private final String recordType = "notification";

    public Notification() {
    }

    public Notification(int notificationType, String studentId, int semester, String message) {
        this.notificationType = notificationType;
        this.studentId = studentId;
        this.semester = semester;
        this.message = message;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @SuppressWarnings("unused")
    public String getRecordType() {
        return recordType;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationType=" + notificationType +
                ", studentId='" + studentId + '\'' +
                ", semester=" + semester +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int compareTo(Notification that) {
        if (this.studentId.compareTo(that.studentId) != 0) {
            return this.studentId.compareTo(that.studentId);
        }
        if (Integer.valueOf(semester).compareTo(that.semester) != 0) {
            return Integer.valueOf(semester).compareTo(that.semester);
        }
        return Integer.valueOf(this.notificationType).compareTo(that.notificationType);
    }
}
