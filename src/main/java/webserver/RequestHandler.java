package webserver;

import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InputStream, OutputStream을 생성하고 Dispatcher에게 작업을 위임하는 클래스
 */
public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    /**
     *  InputStream, OutputStream을 생성한다.
     *  생성된 두 stream을 BufferedInputStream, DataOutputStream으로 감싸고
     *  Dispatcher에게 인수로 넘겨주어 HttpRequest를 처리한다.
     */
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){
            BufferedInputStream bis = new BufferedInputStream(in);
            DataOutputStream dos = new DataOutputStream(out);

            Dispatcher dispatcher = Dispatcher.getInstance();
            dispatcher.dispatch(bis, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
