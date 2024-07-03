package webserver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Callable<Void> {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final PathParser fileParser = new PathParser();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public Void call() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Request request = new Request(br);
            DataOutputStream dos = new DataOutputStream(out);

            // 파일 반환
            byte[] body;
            try(
                    FileInputStream fis = new FileInputStream(new File("src/main/resources/static" + request.getPath()));
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ) {
                bis.transferTo(bos);
                body = bos.toByteArray();
            }

            String fileExt = fileParser.fileExtExtract(request.getPath());
            MimeTypeMapper mimeTypeMapper = new MimeTypeMapper();
            MimeType mimeType = mimeTypeMapper.getMimeType(fileExt);

            response200Header(dos, body.length, mimeType);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, MimeType mimeType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + "\r\n");
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
