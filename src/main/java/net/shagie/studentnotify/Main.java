package net.shagie.studentnotify;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import net.shagie.studentnotify.data.Notification;
import net.shagie.studentnotify.data.Student;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private final static String RULE_PACKAGE = "net/shagie/studentnotify/rules/";

    public static void main(String[] args) {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        for (String ruleFile : getAllRules()) {
            builder.add(ResourceFactory.newClassPathResource(RULE_PACKAGE + ruleFile), ResourceType.DRL);
        }

        builder.add(ResourceFactory.newClassPathResource("net/shagie/studentnotify/rules/lastSemester.drl"), ResourceType.DRL);
        if (builder.hasErrors()) {
            throw new RuntimeException(builder.getErrors().toString());
        }

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());

        for(Student student : getAllStudents()) {
            List<Notification> notifications = new LinkedList<>();

            StatefulKnowledgeSession ksession = knowledgeBase.newStatefulKnowledgeSession();
            ksession.setGlobal("notifications", notifications);
//          ksession.addEventListener( new DebugAgendaEventListener() );
//          ksession.addEventListener( new DebugWorkingMemoryEventListener() );

            KnowledgeRuntimeLogger logger =
                    KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "log/notify_" + student.getId());

            ksession.insert(student);
            System.out.println("Student: " + student.getId() + " " + student.getName());
            ksession.fireAllRules();

            for(Notification notification : notifications) {
                System.out.println(notification);
            }
            System.out.println("----------------");
            logger.close();
            ksession.dispose();
        }
    }

    private static List<Student> getAllStudents()  {
        File dir;
        File[] files = new File[0];
        URL path = ClassLoader.getSystemResource("students");
        try {
            dir = new File(path.toURI());
            files = dir.listFiles();
            if(files == null) {
                return new LinkedList<>();
            }
        } catch (URISyntaxException e) {
            System.err.println("Exception: " + e);
        }

        List<Student> rv = new ArrayList<>(files.length);

        for (final File file : files) {
            try {
                YamlReader reader = new YamlReader(new FileReader(file));
                Student student = reader.read(Student.class);
//              System.out.println(student.toString());
                rv.add(student);
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + file.getName());
            } catch (YamlException e) {
                System.err.println("Couldn't parse " + file.getName());
                System.err.println(e.getMessage());
            }
        }
        return rv;
    }

    private static List<String> getAllRules() {
        URL dir = ClassLoader.getSystemClassLoader().getResource(RULE_PACKAGE);
        List<String> rv = new LinkedList<>();
        if(dir == null) {
            return rv;
        }

        try {
            File fDir = new File(dir.toURI());
            File[] files = fDir.listFiles();
            if (files != null) {
                for(File file : files) {
                    if(file.getName().endsWith(".drl")) {
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
