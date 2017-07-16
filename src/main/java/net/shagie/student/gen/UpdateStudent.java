package net.shagie.student.gen;

import net.shagie.student.couch.DataAccess;
import net.shagie.student.data.Grade;
import net.shagie.student.data.Semester;
import net.shagie.student.data.Student;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class UpdateStudent {
    private final static Random rand = new Random(0);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = DataAccess.getStudentCount();
        final int idMax = (int) (Math.log10(count) + 1);
        List<Grade> grades = Collections.unmodifiableList(Arrays.asList(Grade.values()));
        final int gradeCount = grades.size();

        while (true) {
            System.out.print("Student to update: ");
            String input = scanner.nextLine();
            Student student = null;

            if (input.matches("\\d{4}")) {
                student = DataAccess.getStudent(input.trim());
                System.out.println(student);
            } else if (input.matches("^[qQ].*")) {
                System.exit(0);
            } else if (input.matches("^[rR].*$")) {
                int id = rand.nextInt(count + 1);   // [0 .. count)
                String sId = StringUtils.leftPad(Integer.toString(id), idMax, "0");
                System.out.println(sId);
                student = DataAccess.getStudent(sId);
                System.out.println(student);
            }

            if (student == null) {
                System.out.println("Null student");
                System.exit(1);
            }

            System.out.print("Grades: ");
            input = scanner.nextLine();
            input = input.trim();
            Semester sem = new Semester();
            if (input.equalsIgnoreCase("q")) {
                System.exit(0);
            } else if (input.equalsIgnoreCase("r")) {
                sem.setMath(grades.get(rand.nextInt(gradeCount)));
                sem.setReading(grades.get(rand.nextInt(gradeCount)));
                sem.setWriting(grades.get(rand.nextInt(gradeCount)));
            } else {
                String[] gradeArray = input.split("");
                sem.setReading(Grade.valueOf(gradeArray[0]));
                sem.setWriting(Grade.valueOf(gradeArray[1]));
                sem.setMath(Grade.valueOf(gradeArray[2]));
            }
            student.getGrades().add(sem);

            System.out.println(student);
            DataAccess.updateStudent(student);
        }
    }
}
