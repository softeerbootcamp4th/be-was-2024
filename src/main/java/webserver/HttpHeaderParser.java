package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class HttpHeaderParser {

    public static Map<String, String> parseHeaders(String request) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(request));
        Map<String, String> headers = new HashMap<>();
        br.readLine();

        while(true){
            String inputLine = br.readLine();
            if(inputLine!=null){
                if(inputLine.isEmpty()) break;
            }else break;
            System.out.println(inputLine);
            headers.put(
                    parseHeaderName(inputLine),
                    parseHeaderValue(inputLine)
            );
        }

        return headers;
    }

    public static String parseHeaderName(String header){
        StringBuilder sb = new StringBuilder(header.split(" ")[0]);
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public static String parseHeaderValue(String header){
        return header.split(" ")[1];
    }

}
