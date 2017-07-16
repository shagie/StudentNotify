package net.shagie.student.couch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.shagie.student.data.Notification;
import net.shagie.student.data.Student;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.*;
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
import java.net.URLEncoder;
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
        } catch (IOException ioException) {
            LOG.error("Got io exception loading server.properties from classpath", ioException);
        }
        gson = new Gson();
    }

    public static List<Student> getStudents(int count, int offset) {
        HttpGet get = new HttpGet(serverProp.getProperty("server.url")
                + "_design/views/_view/byId?limit=" + count + "&skip=" + offset);
        return getStudentList(get);
    }

    public static Student getStudent(String studentId) {
        String key = "";
        try {
            key = URLEncoder.encode("\"" + studentId + "\"", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Unknown encoding", e);
        }

        HttpGet get = new HttpGet(serverProp.getProperty("server.url")
                + "_design/views/_view/byId?key=" + key);
        List<Student> rv = getStudentList(get);
        if (rv.isEmpty()) {
            return null;
        } else {
            return rv.get(0);
        }
    }


    public static List<Notification> getNotifications(Student student) {
        List<Notification> rv = Collections.emptyList();
        String key = "";
        try {
            key = URLEncoder.encode("\"" + student.getStudentId() + "\"", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Unknown encoding", e);
        }
        HttpGet get = new HttpGet(
                serverProp.getProperty("server.url")
                        + "_design/views/_view/notifications?key=" + key);

        Type responseType = new TypeToken<ResponseWrapper<Notification>>() {
        }.getType(); // TODO extract
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(get)) {
            StatusLine status = response.getStatusLine();
            if (isOk(status.getStatusCode())) {
                ResponseWrapper<Notification> results =
                        gson.fromJson(EntityUtils.toString(response.getEntity()), responseType);
                rv = results.getRows().stream()
                        .map(RowWrapper::getValue)
                        .collect(Collectors.toList());
            } else {
                LOG.warn("getNotifications non-200 response: " + status.getStatusCode());
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

        doAddUpdate(post);
    }


    public static int getStudentCount() {
        int rv = -1;
        HttpGet get = new HttpGet(serverProp.getProperty("server.url") + "_design/views/_view/studentCount");
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(get)) {
            StatusLine status = response.getStatusLine();
            if (isOk(status.getStatusCode())) {
                Count results = gson.fromJson(EntityUtils.toString(response.getEntity()), Count.class);
                rv = results.getRows().get(0).getValue();
            } else {
                LOG.warn("getStudentCount non-200 response: " + status.getStatusCode());
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
        post.setEntity(studentToStringEntity(student));
        doAddUpdate(post);
    }

    public static void updateStudent(Student student) {
        HttpPut put = new HttpPut(serverProp.getProperty("server.url") + "/" + student.getDocumentId());
        put.setHeader("Referer", serverProp.getProperty("server.url"));
        put.setHeader("Content-Type", "application/json");
        put.setEntity(studentToStringEntity(student));
        doAddUpdate(put);
    }


    private static StringEntity studentToStringEntity(Student student) {
        StringEntity body = null;
        try {
            body = new StringEntity(gson.toJson(student));
            body.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Got an IO Exception accessing converting student to json", e);
            LOG.error(student.toString());
        }
        return body;
    }

    private static List<Student> getStudentList(HttpGet get) {
        List<Student> rv = Collections.emptyList();
        Type responseType = new TypeToken<ResponseWrapper<Student>>() {}.getType();
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(get)) {
            StatusLine status = response.getStatusLine();
            if (isOk(status.getStatusCode())) {
                ResponseWrapper<Student> results =
                        gson.fromJson(EntityUtils.toString(response.getEntity()), responseType);
                rv = results.getRows().stream()
                        .map(RowWrapper::getValue)
                        .collect(Collectors.toList());
            } else {
                LOG.warn("getStudentList NOT OK: " + status.getStatusCode());
            }
        } catch (IOException e) {
            LOG.error("Got an IO Exception accessing couch server", e);
        }
        return rv;
    }


    /**
     * Do a put (or post) request to update the database.
     * @param request PUT or POST request
     */
    private static void doAddUpdate(HttpEntityEnclosingRequestBase request) {
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request)) {
            StatusLine status = response.getStatusLine();
            if (isOk(status.getStatusCode())) {
                LOG.info("doAddUpdate OK: " + status.getStatusCode());
            } else {
                LOG.warn("doAddUpdate NOT OK: " + status.getStatusCode());
            }
        } catch (IOException e) {
            LOG.error("Got an IO Exception accessing couch server", e);
        }
    }

    private static boolean isOk(int status) {
        return status >= 200 && status <= 299;
    }
}
