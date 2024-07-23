package util;

import dto.HttpRequest;
import dto.enums.HttpMethod;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.constant.StringConstants.*;

public class HttpRequestConverter {


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
            String httpRequestLineElemSpace = httpRequestLine.replaceAll(SPACE, EMPTY_SPACE); /// 반홚
            String headerName = httpRequestLineElemSpace.split(COLON)[0];
            String headerValue = httpRequestLineElemSpace.split(COLON)[1]; //idx
            headers.put(headerName, headerValue);
            httpRequestLine = buffer.readLine();
        }
        /* httpRequest bodys */
        List<Byte> body = new ArrayList<Byte>();

        //TODO  : 지우고 구현하기, 테스트코드로 확인하기 뭘?
        while (buffer.ready()) {
            byte readByte = (byte) buffer.read(); //TODO : int -> byte 변환 문제 없는가?
            body.add(readByte);
        }

        // List<Byte>를 byte[]로 변환
        byte[] bodyBytes = new byte[body.size()];
        for (int i = 0; i < body.size(); i++) {
            bodyBytes[i] = body.get(i);
        }


        return HttpRequest.of(httpMethod, path, queryParams, protocolVersion, headers, bodyBytes);


    }

    public static Map<String, String> bodyToMap(byte[] body) throws UnsupportedEncodingException {

        String bodyToString = URLDecoder.decode(new String(body, StandardCharsets.UTF_8), UTF_8);

        String[] bodyParsedPairList = bodyToString.split(AMPERSAND);

        Map<String, String> parsingBodyParams = new HashMap<>();
        for (String bodyParsedPair : bodyParsedPairList) {
            String queryKey = bodyParsedPair.substring(0, bodyParsedPair.indexOf(EQUAL));
            String queryValue = bodyParsedPair.substring(bodyParsedPair.indexOf(EQUAL) + 1);
            parsingBodyParams.put(queryKey, queryValue);
        }
        return parsingBodyParams;
    }

}
