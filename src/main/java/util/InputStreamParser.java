package util;

import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InputStreamParser {


    /**
     * InputStream을 HttpRequest 객체로 변환하는 로직
     */
    public static HttpRequest convertInputStreamToHttpRequest(InputStream in) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = buffer.readLine();
        String httpMethod = line.split(" ")[0];
        String path = line.split(" ")[1];
        String protocolVersion = line.split(" ")[2];

        List<String> headers = new ArrayList<>();

        while (!line.isEmpty()) {
            line=buffer.readLine();
            headers.add(line);
        }

        return new HttpRequest(httpMethod, path, protocolVersion, headers);

    }


}
