package webserver.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


/**
 *  buffered input stream 에 대해서 문자열로 읽는 클래스
 *  buffered input stream 의 일부분이 문자열로 바뀌면 안되는 경우, 문자열로 읽을 부분을 이 클래스를 사용하면 된다.
 */
public class StreamByteReader {
    private static final byte[] NEWLINE = {'\r','\n'};

    /**
     * read line 기능
     *
     * @param bif 읽을 내용이 있는 buffered input stream
     * @return 읽은 문자열을 뒤에 crlf 를 제거하고 반환
     */
    public static String readLine(BufferedInputStream bif) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int previous = 0;
        int current;

        while ((current = bif.read()) != -1) {
            buffer.write(current);
            if (previous == NEWLINE[0] && current == NEWLINE[1]) {
                break;
            }
            previous = current;
        }


        if (buffer.size() == 0) {
            return "";
        }

        byte[] bytes = buffer.toByteArray();
        int length = bytes.length;
        //crlf 제거
        if (length >= 2 && bytes[length - 2] == '\r' && bytes[length - 1] == '\n') {
            length -= 2;
        }

        return URLDecoder.decode(new String(bytes, 0, length, StandardCharsets.UTF_8),StandardCharsets.UTF_8);
    }

}
