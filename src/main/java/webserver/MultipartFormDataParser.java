package webserver;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 멀티파트 형식으로 들어온 바디의 파싱 담당하는 클래스
 */
public class MultipartFormDataParser {

    /**
     * 이미지 파싱을 담당하는 메서드
     * @param headers 요청 받은 헤더
     * @param body 멀티파트 형식의 바디
     * @return 파싱된 이미지 배열
     */
    public static byte[] imageParser(Map<String, String> headers, byte[] body){
        String boundary = headers.get("content-type").split("boundary=")[1]; // boundary정보 분리
        String byteStr = new String(body, StandardCharsets.ISO_8859_1); // body->String 변경
        String[] parsed = byteStr.split(boundary); // body를 boundary기준으로 분리
        String imageContentType = parsed[2].split("/")[1].split("\n")[0];
        String startIndex = parsed[2].split("/"+imageContentType)[1];
        startIndex.trim();
        int startIntIndex = startIndex.indexOf("\r\n\r\n") + 4;
        startIndex = startIndex.substring(startIntIndex, startIndex.length()-2);
        byte[] b = startIndex.getBytes(StandardCharsets.ISO_8859_1);
        return b;

    }

    /**
     * 컨텐트 파싱을 담당하는 메서드
     * @param headers 요청 받은 헤더
     * @param body 멀티파트 형식의 바디
     * @return 파싱된 컨텐트
     */
    public static String contentParser(Map<String, String> headers, byte[] body){
        String boundary = headers.get("content-type").split("boundary=")[1]; // boundary정보 분리
        String byteStr = new String(body, StandardCharsets.UTF_8); // body->String 변경
        String[] parsed = byteStr.split(boundary);
        String content = parsed[1].split("\r\n")[3];
        return content;
    }
}
