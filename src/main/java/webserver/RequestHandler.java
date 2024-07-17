package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.path.PathMap;
import webserver.http.response.ResponseLibrary;
import webserver.util.StreamByteReader;


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
            BufferedInputStream bif = new BufferedInputStream(in);

            String startline = StreamByteReader.readLine(bif); // http request의 첫번째줄
            HttpRequest.ReqeustBuilder reqeustBuilder = new HttpRequest.ReqeustBuilder(startline);

            String headers = startline;
            int contentLength = 0;
            while(!headers.isEmpty()){ //header들을 한줄씩 순회하면서 request에 header를 하나씩 추가함
                headers = StreamByteReader.readLine(bif);
                logger.info(headers);
                String[] parsedline = headers.split(":");
                if((parsedline.length) ==2) {
                    reqeustBuilder.addHeader(parsedline[0].trim(), parsedline[1].trim());
                    if(parsedline[0].trim().equalsIgnoreCase("content-length")) contentLength = Integer.parseInt(parsedline[1].trim());
                }
            }
            if(contentLength != 0) {
                byte[] body = new byte[contentLength];
                logger.info("bytes_read : {}\n",bif.read(body, 0, contentLength));
                reqeustBuilder.setBody(body);
            }
            HttpRequest request = reqeustBuilder.build();

            logger.info(request.printRequest());

            FunctionHandler api = PathMap.getPathMethod(request); //해당 path에 대한 function을 request정보를 이용하여 받아온다
            HttpResponse response =
                    (api == null)
                            ? ResponseLibrary.notFound
                            : api.function(request); // 해당 function을 실행

            // response 출력
            dos.writeBytes(response.getHeader());
            dos.write(response.getBody());
            dos.flush();
        } catch (IOException | NumberFormatException | NullPointerException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
