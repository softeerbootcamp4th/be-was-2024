package webserver.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class StreamByteReader {

    private static final byte[] NEWLINE = {'\r','\n'};
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

    public static byte[] readLinebyByte(BufferedInputStream bif) throws IOException {
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
            return null;
        }

        byte[] bytes = buffer.toByteArray();
        int length = bytes.length;
        //crlf 제거
        if (length >= 2 && bytes[length - 2] == '\r' && bytes[length - 1] == '\n') {
            byte[] result = new byte[bytes.length - 2];
            System.arraycopy(bytes, 0, result, 0, bytes.length - 2);
            return result;
        }

        return bytes;
    }
}
