package util;

import data.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;
import java.util.*;

/**
 * 멀티파트 형식을 파싱해주는 클래스
 */
public class MultiPartParser {
    private static final Logger logger = LoggerFactory.getLogger(MultiPartParser.class);

    /**
     *
     * @param body
     * @param boundary
     * @return
     */
    public static List<MultipartFile> parse(byte[] body, byte[] boundary) {
        List<byte[]> parts = parseMultipart(body, boundary);
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (byte[] part : parts) {
            try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(part)){
                Map<String, String> header = new HashMap<>();
                String s;
                while (!(s = readLine(byteArrayInputStream)).isEmpty()){
                    toHeader(header,s);
                }
                byte[] bytes = byteArrayInputStream.readAllBytes();
                multipartFiles.add(new MultipartFile(header, bytes));
            } catch (IOException e) {
                logger.error("error in MultipartParser");
                throw new RuntimeException(e);
            }
        }
        return multipartFiles;
    }

    private static void toHeader(Map<String,String> header, String str){
        String[] split = str.split(":");
        header.put(split[0], split[1]);
    }

    /**
     * 멀티파트 request의 바디를 각 파트별로 나눠서 반환한다.
     * @param byteArray
     * @param boundary
     * @return 멀티파트의 파트 리스트
     */
    public static List<byte[]> parseMultipart(byte[] byteArray, byte[] boundary) {
        List<byte[]> parts = new ArrayList<>();
        int boundaryLength = boundary.length;
        int pos = 0;

        while (pos < byteArray.length) {
            int start = indexOf(byteArray, boundary, pos);
            if (start == -1) {
                break;
            }
            start += boundaryLength;

            if (byteArray[start] == '-' && byteArray[start + 1] == '-') {
                break;
            }
            start += 2;

            int end = indexOf(byteArray, boundary, start);
            if (end == -1) {
                break;
            }

            byte[] part = Arrays.copyOfRange(byteArray, start, end - 2); // -2 to remove the trailing \r\n
            parts.add(part);
            pos = end;
        }

        return parts;
    }

    private static int indexOf(byte[] array, byte[] target, int fromIndex) {
        outer:
        for (int i = fromIndex; i <= array.length - target.length; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    private static String readLine(InputStream is) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            if (ch == '\r'){
                is.read();
                break;
            }
            byteArrayOutputStream.write(ch);
        }
        return byteArrayOutputStream.toString();
    }
}
