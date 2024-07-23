package common;

import web.HttpResponse;
import web.ViewPath;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 다양한 문자열 혹은 바이트 처리를 위한 클래스
 */
public class StringUtils {

    public static String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * url-encoded 형태로 오는 body를 Map으로 파싱
     * TODO 현재 내용이 empty일 때 문제 발생
     */
    public static Map<String, String> parseBodyInForm(byte[] body, OutputStream out) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        String bodyStr = new String(body);
        String[] chunks = bodyStr.split("&");
        for (String chunk : chunks) {
            if(chunk.split("=").length<2) {
                HttpResponse response = ResponseUtils.redirectToView(ViewPath.LOGIN);
                response.writeInBytes(out);
            } else {
                String key = chunk.split("=")[0];
                String value = chunk.split("=")[1];
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * bytes 배열을 delimeterbytes 배열로 split하는 메서드
     */
    public static List<byte[]> splitBytes(byte[] bytes, byte[] delimiterBytes) {
        List<byte[]> resultList = new ArrayList<>();
        int index = indexOf(bytes, delimiterBytes, 0);
        int start = 0;

        while (index!=-1) {
            byte[] sliced = new byte[index-start];
            System.arraycopy(bytes, start, sliced, 0, index-start);
            resultList.add(sliced);

            start = index+delimiterBytes.length;
            index = indexOf(bytes, delimiterBytes, start);
        }

        // Add the remaining bytes after the last delimiter
        byte[] lastSlice = new byte[bytes.length-start];
        System.arraycopy(bytes, start, lastSlice, 0, bytes.length-start);
        resultList.add(lastSlice);

        return resultList;
    }

    /**
     * 순차 탐색을 통해서 일치하는 부분을 찾기
     */
    public static int indexOf(byte[] source, byte[] target, int fromIndex) {
        for (int i=fromIndex; i<=source.length-target.length; i++) {
            boolean found = true;
            for (int j=0; j<target.length; j++) {
                if(source[i+j]!=target[j]) {
                    found = false;
                    break;
                }
            }
            if(found) {
                return i;
            }
        }
        return -1;
    }
}
