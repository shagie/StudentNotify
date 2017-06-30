package net.shagie.student.data;

public class Semester {
    private Grade reading;
    private Grade writing;
    private Grade math;

    public Semester() {
    }

    public Semester(Grade reading, Grade writing, Grade math) {
        this.reading = reading;
        this.writing = writing;
        this.math = math;
    }

    public Grade getReading() {
        return reading;
    }

    public void setReading(Grade reading) {
        this.reading = reading;
    }

    public Grade getWriting() {
        return writing;
    }

    public void setWriting(Grade writing) {
        this.writing = writing;
    }

    public Grade getMath() {
        return math;
    }

    public void setMath(Grade math) {
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
