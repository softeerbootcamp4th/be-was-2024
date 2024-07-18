package util;

import java.util.Optional;

/**
 * 문자열을 파싱하는 클래스
 */
public class StringParser {

    private StringParser() {
    }

    /**
     * 쿠키 문자열에서 세션 아이디를 추출하는 메서드
     * @param cookie
     * @return Optional<String>
     */
    public static Optional<String> parseSessionId(String cookie) {
        if (cookie == null || cookie.isBlank()) {
            return Optional.empty();
        }

        String[] cookies = cookie.split(ConstantUtil.SEMICOLON_WITH_SPACES); // ;로 시작되는 1개 이상의 공백문자 기준으로 split
        for (String c : cookies) {
            if (c.contains(ConstantUtil.SESSION_ID)) {
                int idx = c.indexOf(ConstantUtil.EQUAL);
                if(idx != -1){
                    return Optional.of(c.substring(idx + 1));
                }
            }
        }
        return Optional.empty();
    }
}
