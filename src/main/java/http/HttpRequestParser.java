package http;

import exception.InvalidHttpRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestParser {

    public static HttpRequest parse(InputStream in) throws InvalidHttpRequestException, IOException {
        HttpRequest request = new HttpRequest();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String line = bufferedReader.readLine();
        if(line == null || line.isEmpty()){
            throw new InvalidHttpRequestException("Empty request line.");
        }

        String[] startLine = line.split(" ");
        if(startLine.length != 3){
            throw new InvalidHttpRequestException("Invalid HTTP request line:" + line);
        }

        request.setMethod(startLine[0].trim());
        request.setUrl(startLine[1].trim());
        request.setHttpVersion(startLine[2].trim());

        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) { //null check only는 broken pipe 에러를 발생시킨다..?
            int colonIndex = line.indexOf(':');
            if (colonIndex == -1) {
                throw new InvalidHttpRequestException("Invalid HTTP header: " + line);
            }
            String key = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();
            request.addHeader(key, value);
        }

        return request;
    }
}
