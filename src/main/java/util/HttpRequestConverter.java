package util;

import model.HttpRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestConverter {
    private static final String CHARSET = "UTF-8";
    private static final String SPACE = " ";
    private static final String EMPTY_SPACE = "";
    private static final String COLON = ":";

    /**
     * InputStream을 HttpRequest 객체로 변환하는 로직
     */
    public HttpRequest with(InputStream in) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, CHARSET));
        /* httpRequest start-line*/
        String httpRequestLine = buffer.readLine();
        String httpMethod = httpRequestLine.split(SPACE)[0];
        String path = httpRequestLine.split(SPACE)[1];
        String protocolVersion = httpRequestLine.split(SPACE)[2];

        /* httpRequest headers */
        Map<String, String> headers = new HashMap<>();
        httpRequestLine = buffer.readLine();
        while (!httpRequestLine.isEmpty()) {
            httpRequestLine.replaceAll(SPACE, EMPTY_SPACE);
            String headerName = httpRequestLine.split(COLON)[0];
            String headerValue = httpRequestLine.split(COLON)[1];
            headers.put(headerName, headerValue);
            httpRequestLine = buffer.readLine();
        }

        /* httpRequest bodys */
        List<Character> bodys = new ArrayList<>();

        //TODO  : 지우고 구현하기, 테스트코드로 확인하기 뭘?
        while (buffer.ready()) {
            char readChar = (char) buffer.read();
            bodys.add(readChar);
        }

        return HttpRequest.of(httpMethod,path,protocolVersion,headers,bodys);

}


}
