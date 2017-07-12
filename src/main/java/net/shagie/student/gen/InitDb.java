package net.shagie.student.gen;

import net.shagie.student.couch.DataAccess;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.*;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InitDb {
    private final static Logger LOG = LoggerFactory.getLogger(InitDb.class);
    final static private Properties serverProp;

    static {
        serverProp = new Properties();
        InputStream inStream = DataAccess.class.getClassLoader().getResourceAsStream("server.properties");
        try {
            serverProp.load(inStream);
        } catch (IOException ioex) {
            LOG.error("Got io exception loading server.properties from classpath", ioex);
        }
    }

    /**
     * Deletes the students database
     * Sends up design documents
     *
     * @param args no arguments
     */
    public static void main(String[] args) {
        // Delete database
        HttpUriRequest delete = new HttpDelete(serverProp.getProperty("server.url"));
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(delete)) {

            StatusLine status = response.getStatusLine();
            System.out.println("delete: " + status.getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();    // XXX handle exception
        }

        // new database
        HttpUriRequest newDb = new HttpPut(serverProp.getProperty("server.url"));
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(newDb)) {

            StatusLine status = response.getStatusLine();
            System.out.println("create: " + status.getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();    // XXX handle exception
        }

        // add design documents
        // note: working directory is project root
        File designDocs = new File("src/main/couchdb/design");
        File[] files = designDocs.listFiles();
        if (files == null) {
            LOG.error("list of design documents is null");
        } else {
            for (File file : files) {
                HttpPut designDoc = new HttpPut(serverProp.getProperty("server.url") + "_design/" + file.getName());

                designDoc.setHeader("Referer", serverProp.getProperty("server.url"));
                designDoc.setHeader("Content-Type", "application/json");
                HttpEntity body = new FileEntity(file);
                designDoc.setEntity(body);

                try (CloseableHttpClient client = HttpClients.createDefault();
                     CloseableHttpResponse response = client.execute(designDoc)) {

                    StatusLine status = response.getStatusLine();
                    System.out.println("design doc: " + file.getName() + ": " + status.getStatusCode());
                } catch (IOException e) {
                    e.printStackTrace();    // XXX handle exception
                }
            }
        }
    }
}
