package http;

import java.util.*;
import java.util.function.Function;

/**
 * http header 정보를 관리하는 클래스
 */
public class MyHttpHeaders {
    Map<String, String> headers;

    public MyHttpHeaders() {
        headers = new HashMap<>();
    }

    /**
     * 헤더 라인 목록을 파싱하여 저장한다
     * @param headerLines 헤더 라인 목록
     */
    public void putHeaders(List<String> headerLines) {
        for (String headerLine : headerLines) {
            putHeader(headerLine);
        }
    }

    /**
     * 헤더를 파싱하여 저장한다
     * @param headerLine 헤더 라인
     */
    public void putHeader(String headerLine) {
        // 속성 값 부분에도 : 기호 존재 가능 => 2개만 나누기
        String[] parts = headerLine.split(":", 2);
        // 속성 이름: 값 구조가 아닌 경우 예외 처리
        if (parts.length != 2) return; // TODO 적절한 예외처리 필요
        this.putHeader(parts[0].strip(), parts[1].strip());
    }

    /**
     * 헤더를 key-value 형식으로 저장한다.
     * @param name 헤더의 이름
     * @param value 헤더의 값
     */
    public void putHeader(String name, String value) {
        // 대소문자를 구분하지 않음.
        String lowerName = name.toLowerCase();
        headers.put(lowerName, value);
    }

    /**
     * 원하는 헤더를 문자열 타입 그대로 얻는다
     * @param name 대상 헤더의 이름
     * @return 헤더
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    /**
     * 입력된 헤더의 값을 지운다.
     * @param name 헤더의 이름
     */
    public void clearHeader(String name) {
        headers.remove(name);
    }


    /**
     * 원하는 헤더를 원하는 타입으로 변환해서 얻는다
     * @param name 대상 헤더의 이름
     * @param converter 헤더를 원하는 타입으로 반환하는 람다 함수
     * @return 원하는 타입으로 변환된 헤더
     * @param <T>
     */
    public<T> T getHeader(String name, Function<String,T> converter) {
        String value = headers.get(name);
        if(value == null) return null;
        return converter.apply(value);
    }

    /**
     * Content-Length 헤더 정보를 얻는다. 지정된 값이 없다면 0을 반환한다.
     * @return Content-Length의 값 ( int )
     */
    public int getContentLength() {
        Integer value = getHeader(HeaderConst.ContentLength, Integer::parseInt);
        if(value == null) return 0;
        return value;
    }

    /**
     * 헤더 목록(map)을 반환한다. 객체 외부에서 모든 헤더 목록을 참조하는데 사용된다.
     * 추후에 배열 기반으로 변경될 수 있다.
     * @return 수정 불가능한 헤더 목록(map)
     */
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }
}
