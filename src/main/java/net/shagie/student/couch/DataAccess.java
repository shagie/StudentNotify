package net.shagie.student.couch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.shagie.student.data.Notification;
import net.shagie.student.data.Student;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class DataAccess {
    private final static Logger LOG = LoggerFactory.getLogger(DataAccess.class);
    final static private Properties serverProp;
    final static private Gson gson;

    static {
        serverProp = new Properties();
        InputStream inStream = DataAccess.class.getClassLoader().getResourceAsStream("server.properties");
        try {
            serverProp.load(inStream);
        } catch (IOException ioex) {
            LOG.error("Got io exception loading server.properties from classpath", ioex);
        }
        gson = new Gson();
    }

    public static List<Student> getStudents(int count, int offset) {
        Type responseType = new TypeToken<ResponseWrapper<Student>>() {
        }.getType();
        List<Student> rv = Collections.emptyList();
        HttpGet get = new HttpGet(serverProp.getProperty("server.url")
                + "_design/views/_view/byId?limit=" + count + "&skip=" + offset);
        try (CloseableHttpClient client = HttpClients.createDefault();  // TODO extract
             CloseableHttpResponse response = client.execute(get)) {
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == 200) {
                ResponseWrapper<Student> results =
                        gson.fromJson(EntityUtils.toString(response.getEntity()), responseType);
                rv = results.getRows().stream()
                        .map(RowWrapper::getValue)
                        .collect(Collectors.toList());
            } else {
                System.out.println("got non-200 response: " + status.getStatusCode());
            }
        } catch (IOException e) {
            LOG.error("Got an IO Exception accessing couch server", e);
        }

        return rv;
    }

    public static List<Notification> getNotifications(Student student) {
        List<Notification> rv = Collections.emptyList();
        HttpGet get = new HttpGet(
                serverProp.getProperty("server.url")
                        + "_design/views/_view/notifications?key=%22" + student.getStudentId() + "%22");
        // XXX encode properly

        Type responseType = new TypeToken<ResponseWrapper<Notification>>() {
        }.getType(); // TODO extract
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(get)) {
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == 200) {
                ResponseWrapper<Notification> results =
                        gson.fromJson(EntityUtils.toString(response.getEntity()), responseType);
                rv = results.getRows().stream()
                        .map(RowWrapper::getValue)
                        .collect(Collectors.toList());
            } else {
                System.out.println("got non-200 response: " + status.getStatusCode());
            }
        } catch (IOException e) {
            LOG.error("Got an IO Exception accessing couch server", e);
        }

        return rv;
    }

    public static void addNotification(Student student, Notification notification) {
        HttpPost post = new HttpPost(serverProp.getProperty("server.url"));
        post.setHeader("Referer", serverProp.getProperty("server.url"));
        post.setHeader("Content-Type", "application/json");
        StringEntity body = null;
        try {
            body = new StringEntity(gson.toJson(notification));
            body.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Got an IO Exception accessing converting notification to json", e);
            LOG.error(student.toString());
        }
        post.setEntity(body);

        doPost(post);
    }

    private static void doPost(HttpPost post) {
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == 200) {
                System.out.println(status.getStatusCode());
            } else {
                System.out.println(status.getStatusCode());
            }
        } catch (IOException e) {
            LOG.error("Got an IO Exception accessing couch server", e);
        }
    }

    public static int getStudentCount() {
        int rv = -1;
        HttpGet get = new HttpGet(serverProp.getProperty("server.url") + "_design/views/_view/studentCount");
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(get)) {
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == 200) {
                Count results = gson.fromJson(EntityUtils.toString(response.getEntity()), Count.class);
                rv = results.getRows().get(0).getValue();
            } else {
                System.out.println("got non-200 response: " + status.getStatusCode());
            }
        } catch (IOException e) {
            LOG.error("Got an IO Exception accessing couch server", e);
        }
        return rv;
    }

    public static void addStudent(Student student) {
        HttpPost post = new HttpPost(serverProp.getProperty("server.url"));
        post.setHeader("Referer", serverProp.getProperty("server.url"));
        post.setHeader("Content-Type", "application/json");
        StringEntity body = null;
        try {
            body = new StringEntity(gson.toJson(student));
            body.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Got an IO Exception accessing converting student to json", e);
            LOG.error(student.toString());
        }
        post.setEntity(body);

        doPost(post);
    }

}
