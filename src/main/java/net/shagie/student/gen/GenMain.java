package net.shagie.student.gen;

import net.shagie.student.couch.DataAccess;
import net.shagie.student.data.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GenMain {
    private final static Logger LOG = LoggerFactory.getLogger(GenMain.class);
    private final static Random rand = new Random(0);

    public static void main(String[] args) {
        List<String> lines = Collections.emptyList();
        URL nameResource = GenMain.class.getClassLoader().getResource("names.txt");

        try {
            if (nameResource == null) {
                LOG.error("get resource names.txt returned a null URL resource");
                System.exit(1);
            }
            File names = new File(nameResource.getFile());
            lines = FileUtils.readLines(names, "UTF-8");
        } catch (IOException e) {
            LOG.warn("Unable to read lines from names.txt", e);
        }

        int id = 1;
        final int idmax = (int) (Math.log10(lines.size()) + 1);
        if (LOG.isDebugEnabled()) {
            LOG.debug("size:   " + lines.size());
            LOG.debug("log10:  " + idmax);
        }

        List<Grade> grades = Collections.unmodifiableList(Arrays.asList(Grade.values()));
        final int gradeCount = grades.size();

        for (String line : lines) {
            String[] nameSplit = line.split("\\t");
            Student s = new Student();
            s.setName(new Name(nameSplit[0], nameSplit[1]));
            s.setStudentId(StringUtils.leftPad(Integer.toString(id), idmax, "0"));
            int enrolled = rand.nextInt(2); // semesters enrolled
            List<Semester> semesters = new ArrayList<>(enrolled);
            for (int i = 0; i < enrolled; i++) {
                Semester sem = new Semester();
                sem.setMath(grades.get(rand.nextInt(gradeCount)));
                sem.setReading(grades.get(rand.nextInt(gradeCount)));
                sem.setWriting(grades.get(rand.nextInt(gradeCount)));
                semesters.add(sem);
            }

            s.setGrades(semesters);

            System.out.println(s.toString());
            DataAccess.addStudent(s);

            if (enrolled > 0) {
                Notification note = new Notification();
                note.setNotificationType(NotifyIds.ENROLL);
                note.setSemester(0);
                note.setStudentId(s.getStudentId());
                note.setMessage("initial enrolment");
                DataAccess.addNotification(s, note);
            }
            id += 1;
        }

    }
}
