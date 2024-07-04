package webserver.front.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.front.data.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestParser {

    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    public HttpRequest parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String methodLine = br.readLine();

        String[] tokens = methodLine.split(" ");
        String method = tokens[0];
        String url = tokens[1];


        logger.debug(methodLine);
        while(true){
            String line = br.readLine();
            if(line.isEmpty()) break;
            logger.debug(line);
//            if(line.startsWith("Accept: ")){
//                tokens = line.split(" ");
//            }
        }
        return new HttpRequest(method,url);
    }
}
