package common;

import exception.SizeNotMatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HttpRequest;
import webserver.WebAdapter;

import java.io.IOException;
import java.io.InputStream;

public class RequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    /**
     * InputStream에서 Request를 String형태로 읽어온 후, HttpRequest를 만들어 반환한다.
     * body는 byte[]형태로 받아온다.
     */
    public static HttpRequest parseHttpRequest(InputStream in) throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
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
        for (String line : headerLines) {
            if (line.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
                break;
            }
        }

        // Content-Length가 0보다 크다면 body를 읽는다.
        if(contentLength>0) {
            body = new byte[contentLength];
            int readSize = in.read(body, 0, contentLength);
            if(readSize!=contentLength) {
                throw new SizeNotMatchException("Content-Length 크기와 읽은 body 사이즈가 다릅니다");
            }
            sb.append(new String(body));
            sb.append("\n");
        }

        logger.debug("{}", sb);

        return WebAdapter.parseRequest(sb.toString(), body);
    }
}

