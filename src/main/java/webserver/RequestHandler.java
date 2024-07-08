package webserver;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.api.ApiFunction;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.PathMap;
import webserver.http.enums.Methods;


/*
* Request들에 대해서 hanldling하는 class
* socket을 통한 모든 IO는 이 class가 관장한다
* */
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
            DataOutputStream dos = new DataOutputStream(out);
            InputStreamReader dis = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(dis);

            String startline = br.readLine(); // http request의 첫번째줄
            HttpRequest req = new HttpRequest(startline);

            String headers = startline;
            logger.info("////// request header start //////");
            logger.info(startline);
            while(!headers.isEmpty()){ //header들을 한줄씩 순회하면서 request에 header를 하나씩 추가함
                headers = br.readLine();
                logger.info(headers);
                String[] parsedline = headers.split(":");
                if((parsedline.length) ==2) req.addHeader(parsedline[0].trim(),parsedline[1].trim());
            }
            String contentLength = req.getHeaders().get("Content-Length");
            if(contentLength != null) {
                int length = Integer.parseInt(contentLength.trim());
                char[] body = new char[length];
                br.read(body, 0,length);
                req.setBody(new String(body));
            }
            logger.info("////// request header end //////");

            ApiFunction api = PathMap.getPathMethod(req.getMethod(),req.getUri().getPath()); //해당 path에 대한 function을 request정보를 이용하여 받아온다
            HttpResponse response = (api == null) ? new HttpResponse(404) : api.funcion(req); // 해당 function을 실행

            // response 출력
            dos.writeBytes(response.getHeader());
            dos.write(response.getBody());
            dos.flush();
        } catch (IOException | NumberFormatException e) {
            logger.error(e.getMessage());
        }
    }
}
