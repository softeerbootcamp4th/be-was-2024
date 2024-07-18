package util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * 문자열을 파싱하는 클래스
 */
public class StringParser {

    private StringParser() {
    }

    /**
     * 요청의 헤더를 파싱하는 메서드
     * @param in
     * @return String
     */
    public static String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int b;
        while ((b = in.read()) != -1) {
            if (b == '\r') {
                int next = in.read();
                if (next == '\n') {
                    break;
                } else {
                    sb.append((char) b);
                    sb.append((char) next);
                }
            } else {
                sb.append((char) b);
            }
        }
        return sb.toString();
    }

    /**
     * 멀티파트 형식의 요청 시 이를 파싱하는 메서드
     * @param httpRequest
     * @param boundary
     * @return void
     */
    public static void processMultipartData(HttpRequest httpRequest, String boundary) {
        String bodyString = new String(httpRequest.getBody(), StandardCharsets.ISO_8859_1);

        String[] parts = bodyString.split(ConstantUtil.PREFIX_HYPHEN + boundary);
        for(String part : parts) {
            if(part.isEmpty() || part.equals(ConstantUtil.PREFIX_HYPHEN)) continue;
            String[] lines = part.split(ConstantUtil.CRLF);
            if(lines[0].equals(ConstantUtil.PREFIX_HYPHEN)) break;

            String[] contentDisposition = lines[1].split(ConstantUtil.COLON_WITH_SPACES);
            String[] name = contentDisposition[1].trim().split(ConstantUtil.NAME + "=\"");
            String key = name[1].substring(0, name[1].length()-1);

            if(contentDisposition[1].contains(ConstantUtil.FILE_NAME)) {
                String[] filename = contentDisposition[1].split(ConstantUtil.FILE_NAME + "=\"");
                String fileName = filename[1].substring(0, filename[1].length()-1);

                // 파일 데이터는 바이트 배열로 직접 추출
                byte[] fileData = part.substring(part.indexOf(ConstantUtil.CRLF + ConstantUtil.CRLF) + 4).getBytes(StandardCharsets.ISO_8859_1);
                httpRequest.putFileData(fileName, fileData);
            } else {
                String value = new String(lines[3].getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).trim();
                httpRequest.putFormData(key, value);
            }
        }
    }

    /**
     * 쿠키 문자열에서 세션 아이디를 추출하는 메서드
     * @param cookie
     * @return String
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
