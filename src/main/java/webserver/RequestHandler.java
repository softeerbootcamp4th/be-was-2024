package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequest;
import util.HttpResponse;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new HttpRequest(reader);
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = new HttpResponse(dos);

            String url = request.getUrl();
            logger.debug("HTTP Request Content:\n" + request.toString());

            if ("/".equals(url)) {
                String responseBody = "Hello World!";
                response.sendResponse(200, "OK", "text/html;charset=utf-8", responseBody.getBytes());
            } else {
                File file = new File("src/main/resources/static" + url);

                if (file.exists() && !file.isDirectory()) {
                    byte[] body = readFileToByteArray(file);
                    response.sendResponse(200, "OK", "text/html;charset=utf-8", body);
                } else {
                    String body = "<html><body><h1>404 Not Found</h1></body></html>";
                    response.sendResponse(404, "Not Found", "text/html;charset=utf-8", body.getBytes());
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] readFileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return data;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
