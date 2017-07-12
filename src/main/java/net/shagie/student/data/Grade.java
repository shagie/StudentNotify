package net.shagie.student.data;

public enum Grade {
    A(4), B(3), C(2), D(1), F(0);

    private final int score;

    Grade(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
