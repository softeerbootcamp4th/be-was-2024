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
            HttpRequest request = createHttpRequest(in);
            HttpResponse response = createHttpResponse(out);

            handleRequest(request, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpRequest createHttpRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return new HttpRequest(reader);
    }

    private HttpResponse createHttpResponse(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        return new HttpResponse(dos);
    }

    private void handleRequest(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        String content = request.getContentType();
        logger.debug("HTTP Request Content:\n" + content);

        if ("/".equals(url)) {
            handleRootRequest(response);
        } else {
            handleFileRequest(url, content, response);
        }
    }

    private void handleRootRequest(HttpResponse response) {
        String responseBody = "Hello World!";
        response.sendResponse(200, "OK", "text/html;charset=utf-8", responseBody.getBytes());
    }

    private void handleFileRequest(String url, String contentType, HttpResponse response) {
        File file = new File("src/main/resources/static" + url);

        if (file.exists() && !file.isDirectory()) {
            byte[] body = readFileToByteArray(file);
            response.sendResponse(200, "OK", contentType, body);
        } else {
            send404Response(response);
        }
    }

    private void send404Response(HttpResponse response) {
        String body = "<html><body><h1>404 Not Found</h1></body></html>";
        response.sendResponse(404, "Not Found", "text/html;charset=utf-8", body.getBytes());
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
