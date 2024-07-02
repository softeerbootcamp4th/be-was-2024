package http;

import java.util.*;

/**
 * http header 정보를 관리하는 클래스
 */
public class MyHttpHeaders {
    Map<String, String> headers;

    public MyHttpHeaders() {
        headers = new HashMap<>();
    }

    public void PutHeaders(List<String> headerLines) {
        for (String headerLine : headerLines) {
            PutHeader(headerLine);
        }
    }

    public void PutHeader(String headerLine) {
        // 속성 값 부분에도 : 기호 존재 가능 => 2개만 나누기
        String[] parts = headerLine.split(":", 2);
        // 속성 이름: 값 구조가 아닌 경우 예외 처리
        if (parts.length != 2) return; // TODO 적절한 예외처리 필요
        headers.put(parts[0].trim(), parts[1].trim());
    }

    public String GetHeader(String name) {
        return headers.get(name);
    }

    // 외부에서 변경 불가능한 상태로 맵 공개
    public Map<String, String> GetHeaders() {
        return Collections.unmodifiableMap(headers);
    }
}
