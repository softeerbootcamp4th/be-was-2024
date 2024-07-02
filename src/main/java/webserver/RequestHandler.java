package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import constant.FileExtensionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            StringBuilder headers = new StringBuilder();

            // url 추출
            String line = br.readLine();
            headers.append(line);
            String url = line.split(" ")[1];

            // 파일 확장자 추출
            String[] parts = url.split("\\.");
            String extensionType = parts[parts.length - 1];

            // request header 출력
            while(!(line = br.readLine()).isEmpty()) {
                    headers.append(line).append("\n");
            }
            logger.debug("Request Headers:\n {}", headers);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = readFile("src/main/resources/static"+url);

            if(body != null) {
                response200Header(dos, body.length, extensionType);
                responseBody(dos, body);
            }
            else{
                // url에 해당하는 파일이 없으면 404 error 응답
                response404Error(dos);
            }

        } catch (IOException | IllegalArgumentException e) {
            logger.error(e.getMessage());

            // FileExtensionType에서 관리하지 않는 타입일 경우 404 error 응답
            response404Error(connection);
        }
    }

    private byte[] readFile(String filePath) {
        File file = new File(filePath);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] temp = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(temp)) != -1) {
                buffer.write(temp, 0, bytesRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String extensionType){
        try {
            FileExtensionType fileExtensionType = FileExtensionType.valueOf(extensionType.toUpperCase());

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + fileExtensionType.getContentType() + ";"
                    + "charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Error(DataOutputStream dos) throws IOException {
        try {
            String errorMessage = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Type: text/html\r\n");
            dos.writeBytes("Content-Length: " + errorMessage.length() + "\r\n");
            dos.writeBytes("\r\n");
            dos.writeBytes(errorMessage);
            dos.flush();
        }
        catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    private void response404Error(Socket connection) {
        try(OutputStream out = connection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out)){
            response404Error(dos);
        }
        catch (IOException e){
            logger.error(e.getMessage());
        }
    }
}
