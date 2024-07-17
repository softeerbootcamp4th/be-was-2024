package webserver;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MultipartFormDataParser {

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
}
