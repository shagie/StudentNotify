package net.shagie.studentnotify;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
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
import java.util.List;

public class Main {
    public static void main(String[] args) {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        builder.add(ResourceFactory.newClassPathResource("net/shagie/studentnotify/rules/studentRules.drl"), ResourceType.DRL);
        if (builder.hasErrors()) {
            throw new RuntimeException(builder.getErrors().toString());
        }

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());

        for(Student student : getAllStudents()) {
            List<String> notifications = new ArrayList<>();

            StatefulKnowledgeSession ksession = knowledgeBase.newStatefulKnowledgeSession();
            ksession.setGlobal("notifications", notifications);
//            ksession.addEventListener( new DebugAgendaEventListener() );
//            ksession.addEventListener( new DebugWorkingMemoryEventListener() );

            KnowledgeRuntimeLogger logger =
                    KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "log/notify_" + student.getId());

            ksession.insert(student);
            ksession.fireAllRules();

            for(String notification : notifications) {
                System.out.println(notification);
            }
            System.out.println("----------------");
            logger.close();
            ksession.dispose();
        }
    }

    private static List<Student> getAllStudents()  {
        File dir = new File("main/resources/students");
        URL path = ClassLoader.getSystemResource("students");
        try {
            dir = new File(path.toURI());
        } catch (URISyntaxException e) {
            System.err.println("Exception: " + e);
        }

        File[] files = dir.listFiles();
        if(files == null) {
            return new ArrayList<>();
        }

        List<Student> rv = new ArrayList<>(files.length);

        for (final File file : files) {
            try {
                YamlReader reader = new YamlReader(new FileReader(file));
                Student student = reader.read(Student.class);
                System.out.println(student.toString());
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
}
