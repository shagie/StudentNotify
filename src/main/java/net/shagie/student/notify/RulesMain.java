package net.shagie.student.notify;

import net.shagie.student.couch.DataAccess;
import net.shagie.student.data.Notification;
import net.shagie.student.data.Student;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class RulesMain {
    private final static String RULE_PACKAGE = "net/shagie/student/notify/rules/";
    private final static int INCREMENT = 10;

    public static void main(String[] args) {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        List<String> rules = getAllRules();
        for (String ruleFile : rules) {
            builder.add(ResourceFactory.newClassPathResource(RULE_PACKAGE + ruleFile), ResourceType.DRL);
        }

        if (builder.hasErrors()) {
            throw new RuntimeException(builder.getErrors().toString());
        }

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());

        int count = DataAccess.getStudentCount();
        for (int i = 0; i < count; i += INCREMENT) {
            List<Student> students = DataAccess.getStudents(INCREMENT, i);
            for (Student student : students) {
                List<Notification> newEvents = new LinkedList<>();

                StatefulKnowledgeSession ksession = knowledgeBase.newStatefulKnowledgeSession();
//              ksession.addEventListener( new org.drools.event.rule.DebugWorkingMemoryEventListener() );
                ksession.setGlobal("newEvents", newEvents);

                ksession.insert(student);
                for (Notification pastNotification : DataAccess.getNotifications(student)) {
                    ksession.insert(pastNotification);
                }

//              KnowledgeRuntimeLogger logger =
//                      KnowledgeRuntimeLoggerFactory.newFileLogger(ksession,
//                              "log/notify_" + student.getStudentId());

                System.out.println("Student: " + student.getStudentId() + " " + student.getName());
                ksession.fireAllRules();

                for (Notification notification : newEvents) {
                    System.out.println(notification);
                }
                System.out.println("----------------");
//              logger.close();
                ksession.dispose();
            }
        }
    }


    private static List<String> getAllRules() {
        URL dir = ClassLoader.getSystemClassLoader().getResource(RULE_PACKAGE);
        List<String> rv = new LinkedList<>();
        if (dir == null) {
            return rv;
        }

        try {
            File fDir = new File(dir.toURI());
            File[] files = fDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".drl")) {
                        rv.add(file.getName());
                    }
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return rv;
    }
}
