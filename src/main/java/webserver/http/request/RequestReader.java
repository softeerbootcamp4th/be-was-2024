package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestReader {
    public static final Logger logger = LoggerFactory.getLogger(RequestReader.class);

    public static String readRequest(InputStream in) throws IOException {

        final char[] endCharacters = {'\r', '\n', '\r', '\n'};
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        int endCharactersCount=0;

        while(endCharactersCount!=endCharacters.length){

            int read = br.read();
            if(endCharacters[endCharactersCount]==read) endCharactersCount++;
            else endCharactersCount = 0;
            sb.append((char)read);

        }

        String request = sb.toString();
        logger.debug(request);
        return request;
    }

}
