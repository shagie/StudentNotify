package net.shagie.studentnotify.data;

public class Semester {
    private String reading;
    private String writing;
    private String math;

    public Semester() {
    }

    public Semester(String reading, String writing, String math) {
        this.reading = reading;
        this.writing = writing;
        this.math = math;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public String getMath() {
        return math;
    }

    public void setMath(String math) {
        this.math = math;
    }

    @Override
    public String toString() {
        return "Semester{" +
                "reading=" + reading +
                ", writing=" + writing +
                ", math=" + math +
                '}';
    }
}
