package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestLogging {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static String printRequest(BufferedReader br) throws IOException {

        String line = br.readLine();

        if (line == null) return "/";
        String url = line.split(" ")[1];

        String request = "";
        while (!line.equals("")) {
            request += line + "\n";
            line = br.readLine();
        }

        logger.debug("\n\n***** REQUEST *****\n" + request);

        return url;
    }
}
