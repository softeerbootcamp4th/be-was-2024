package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ConstantUtil;
import util.HttpRequest;
import util.HttpResponse;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final FrontRequestProcess frontRequestProcess;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.frontRequestProcess = FrontRequestProcess.getInstance();
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            String requestLine = readLine(in); // Request Line (ex: "GET /index.html HTTP/1.1")
            HttpRequest httpRequest = HttpRequest.from(requestLine);
            int contentLength = 0;

            // Request Headers
            String line;
            while (!(line = readLine(in)).isEmpty()) {
                httpRequest.putHeaders(line);
                logger.info("header: {}", line);
            }
            if(httpRequest.getContentLength() != 0) {
                contentLength = httpRequest.getContentLength();
            }

            // Request Body
            byte[] body = in.readNBytes(contentLength);
            httpRequest.putBody(body);

            // Multipart Data Handling
            if(httpRequest.getContentType().contains(ConstantUtil.FORM_DATA)) {
                String boundary = httpRequest.getContentType().split("boundary=")[1];
                processMultipartData(httpRequest, boundary);
            }

            HttpResponse responseInfo = frontRequestProcess.handleRequest(httpRequest);
            frontRequestProcess.handleResponse(out, responseInfo);
        }  catch (IOException e) {
            logger.error("error: {}", e.getStackTrace());
        }
    }

    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int b;
        while ((b = in.read()) != -1) {
            if (b == '\r') {
                int next = in.read();
                if (next == '\n') {
                    break;
                } else {
                    sb.append((char) b);
                    sb.append((char) next);
                }
            } else {
                sb.append((char) b);
            }
        }
        return sb.toString();
    }

    private void processMultipartData(HttpRequest httpRequest, String boundary) throws IOException {
        String bodyString = new String(httpRequest.getBody(), StandardCharsets.ISO_8859_1);

        String[] parts = bodyString.split("--" + boundary);
        for(String part : parts) {
            if(part.isEmpty() || part.equals("--")) continue;
            String[] lines = part.split(ConstantUtil.CRLF);
            if(lines[0].equals("--")) break;

            String[] contentDisposition = lines[1].split(":\\s+");
            String[] name = contentDisposition[1].trim().split("name=\"");
            String key = name[1].substring(0, name[1].length()-1);

            if(contentDisposition[1].contains("filename")) {
                String[] filename = contentDisposition[1].split("filename=\"");
                String fileName = filename[1].substring(0, filename[1].length()-1);

                // 파일 데이터는 바이트 배열로 직접 추출
                byte[] fileData = part.substring(part.indexOf(ConstantUtil.CRLF + ConstantUtil.CRLF) + 4).getBytes(StandardCharsets.ISO_8859_1);
                httpRequest.putFileData(fileName, fileData);
            } else {
                String value = new String(lines[3].getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).trim();
                httpRequest.putFormData(key, value);
            }
        }
    }
}
