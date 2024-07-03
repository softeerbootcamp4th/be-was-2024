package webserver;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestHanlder {
    private String method;
    private String uri;
    private String body;
    private String protocol;
    private Map<String, String> headers = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);



    public HttpRequestHanlder(BufferedReader br) {
        try{
            String line = br.readLine();
            String[] startline = line.split(" ");
            logger.info(line);
            method =  startline[0];
            uri = startline[1];
            protocol = startline[2];

            logger.info("////// request header start //////");
            logger.info(line);
            while(!line.isEmpty()){
                line = br.readLine();
                logger.info(line);
                String[] parsedline = line.split(". ");
                if((parsedline.length) ==2) headers.put(parsedline[0], parsedline[1]);
            }
            logger.info("////// request header end //////");

        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}