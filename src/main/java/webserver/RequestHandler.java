package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder reqHeader = new StringBuilder();

            String reqLine = reader.readLine(), line;
            reqHeader.append("  ").append(reqLine).append("\n");
            while ((line = reader.readLine()) != null && !line.equals("")) {
                reqHeader.append("  ").append(line).append("\n");
            }
            logger.debug("\n:: Request ::\n{}", reqHeader.toString());

            String path = reqLine.split(" ")[1];

            if (path.equals("/")) { path = "/index.html"; }

            byte[] body = readFileToBytes("./src/main/resources/static" + path);
//            byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static byte[] readFileToBytes(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }

        return bytes;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
}
