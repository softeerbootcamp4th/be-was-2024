package http.utils;

import java.util.ArrayList;
import java.util.List;

public class HttpParseUtil {
    public static String[] ParseRequestLine(String requestLine) {
        // method url version 정보가 포함되어야 한다. 각각은 공백을 기준으로 나눔.
        List<String> result = new ArrayList<>();
        String[] line_info = requestLine.split(" ");
        // 3개의 정보가 아닌 경우 => 제거
        if(line_info.length != 3) throw new RuntimeException("invalid http request line");

        return line_info;
    }
}
