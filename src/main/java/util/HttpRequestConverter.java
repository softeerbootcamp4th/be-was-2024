package util;

import model.HttpRequest;
import model.enums.HttpMethod;

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
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";

    /**
     * InputStream을 HttpRequest 객체로 변환하는 로직
     */
    public HttpRequest with(InputStream in) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, CHARSET));

        /* httpRequest start-line*/
        String httpRequestLine = buffer.readLine();
        HttpMethod httpMethod = HttpMethod.valueOf(httpRequestLine.split(SPACE)[0]);
        String path = httpRequestLine.split(SPACE)[1];
        Map<String, String> queryParams = new HashMap<>();
        if (path.contains(QUESTION_MARK)) {
            String[] queryPairs = path.substring(path.indexOf(QUESTION_MARK) + QUESTION_MARK.length()).split(AMPERSAND);
            path = path.substring(0, path.indexOf(QUESTION_MARK)); //string 재할당이 무슨 문제가 있는가
            for (String queryPair : queryPairs) {
                String queryKey = queryPair.substring(0, queryPair.indexOf(EQUAL));
                String queryValue = queryPair.substring(queryPair.indexOf(EQUAL), queryPair.length());
                queryParams.put(queryKey, queryValue);

            }
        }
        String protocolVersion = httpRequestLine.split(SPACE)[2];

        /* httpRequest headers */
        Map<String, String> headers = new HashMap<>();
        httpRequestLine = buffer.readLine();
        while (!httpRequestLine.isEmpty()) {
            String s = httpRequestLine.replaceAll(SPACE, EMPTY_SPACE); /// 반홚
            String headerName = httpRequestLine.split(COLON)[0];
            String headerValue = httpRequestLine.split(COLON)[1]; //idx
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

        return HttpRequest.of(httpMethod, path, queryParams, protocolVersion, headers, bodys);

    }


}
