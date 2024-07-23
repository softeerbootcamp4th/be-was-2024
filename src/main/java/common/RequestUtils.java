package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HeaderKey;
import web.HttpMethod;
import web.HttpRequest;
import web.MIME;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Request를 파싱하여 Request Line, Header, Body를 추출하는 클래스
 * Header와 Body는 \r\n\r\n으로 구분하며, 헤더의 Content-Length값에 따라서 Body를 읽는다.
 */
public class RequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    /**
     * InputStream에서 Request를 String형태로 읽어온 후, HttpRequest를 만들어 반환한다.
     * body는 byte[]형태로 받아온다.
     */
    public static HttpRequest parseHttpRequest(InputStream in) throws IOException {
        final int TMP_BUFFER_SIZE = 1024;

        int ch;
        StringBuffer sb = new StringBuffer();
        int contentLength = 0;
        byte[] body = null;

        // Request Line부터 Body 전까지 읽는다
        // Request Line과 Body 사이에는 '\r\n'이 두번 오게 된다.
        boolean lastWasCR = false;
        boolean lastWasLF = false;
        while((ch = in.read())!=-1) {
            sb.append((char) ch);

            if(ch=='\r') {
                lastWasCR = true;
            } else if (ch=='\n') {
                if(lastWasCR && lastWasLF) {
                    break;
                }
                lastWasLF = true;
            } else {
                lastWasCR = false;
                lastWasLF = false;
            }
        }

        // Content-Length 헤더 찾기
        String header = sb.toString();
        String[] headerLines = header.split("\r\n");
        for(String line: headerLines) {
            if(line.toLowerCase().startsWith(HeaderKey.CONTENT_LENGTH.getKey())) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
                break;
            }
        }
        logger.debug("{}", sb);

        StringBuffer sbb = new StringBuffer();
        // Content-Length가 0보다 크다면 body까지 읽어서 로그에 출력한다.
        if (contentLength > 0) {
            body = new byte[contentLength];
            int bytesRead = 0;
            int offset = 0;

            while (bytesRead < contentLength) {
                int bytesToRead = Math.min(TMP_BUFFER_SIZE, contentLength - bytesRead);
                int readSize = in.read(body, offset, bytesToRead);

                if (readSize == -1) {
                    throw new IOException("Unexpected end of input stream while reading body");
                }

                bytesRead += readSize;
                offset += readSize;
            }

            sbb.append(new String(body)).append("\n");
        }

//        logger.debug("{}", sbb);

        return parseRequest(sb.toString(), body);
    }

    /**
     * request로 들어온 HTTP 요청을 한줄씩 파싱하여 적절한 HttpRequest 객체를 생성
     * @param request Request Line과 Header
     * @param body request body (byte[])
     * @return HttpRequest
     */
    private static HttpRequest parseRequest(String request, byte[] body) {
        HttpMethod method;
        String path, contentType = MIME.UNKNOWN.getType();
        LinkedList<String> accept = new LinkedList<>();
        int contentLength = 0;
        Map<String, String> cookie = new HashMap<>();

        String[] requestLine = request.split("\n");

        // Request Line
        String[] line_1 = requestLine[0].split(" ");
        method = HttpMethod.valueOf(line_1[0]);
        path = line_1[1];

        // Header
        for(int i=1; i<requestLine.length; i++) {
            if(requestLine[i].split(":").length==1) continue;
            String[] line_N = requestLine[i].split(":");
            String key = line_N[0].trim();
            String value = line_N[1].trim();

            // Accept헤더 MimeType 설정
            if(key.equalsIgnoreCase(HeaderKey.ACCEPT.getKey())) {
                String[] acceptLine = value.split(";");
                String[] mimeType = acceptLine[0].split(",");
                accept.addAll(Arrays.asList(mimeType));
            } else if(key.equalsIgnoreCase(HeaderKey.CONTENT_LENGTH.getKey())) {
                contentLength = Integer.parseInt(value);
            } else if(key.equalsIgnoreCase(HeaderKey.CONTENT_TYPE.getKey())) {
                contentType = value;
            } else if(key.equalsIgnoreCase(HeaderKey.COOKIE.getKey())) {
                String[] cookies = value.split(";");
                for(String c: cookies) {
                    String cookieName = c.split("=")[0].trim();
                    String cookieId = c.split("=")[1].trim();
                    cookie.put(cookieName, cookieId);
                }
            }
        }

        return new HttpRequest.HttpRequestBuilder()
                .method(method)
                .path(path)
                .accept(accept)
                .contentLength(contentLength)
                .contentType(contentType)
                .cookie(cookie)
                .body(body)
                .build();
    }

    /**
     * Multipart 파일의 Body 구분자를 추출하기 위한 메서드
     * @param contentType Request 헤더의 Content-Type
     * @return Body Delimiter
     */
    public static String getBoundaryKey(String contentType) {
        return contentType.split("boundary=")[1];
    }
}

