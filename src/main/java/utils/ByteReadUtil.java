package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteReadUtil {
    private static final Logger logger = LoggerFactory.getLogger(ByteReadUtil.class);
    public static byte[] readlineToByteArray(InputStream in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int data;
        try {
            while ((data = in.read()) != -1) {
                // 개행 문자('\n')를 만나면 현재까지 구성한 라인을 처리
                if (data == '\n') {
                    break;
                } else if (data == '\r') {
                    // '\r'은 무시 (Windows 스타일의 '\r\n' 처리)
                    continue;
                } else {
                    // 바이트를 문자열로 변환하여 라인에 추가
                    bos.write(data);
                }
            }
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
        }
        return bos.toByteArray();
    }

    public static String readLineToString(InputStream in) {
        return new String(readlineToByteArray(in));
    }
}
