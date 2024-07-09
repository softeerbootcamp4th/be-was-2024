package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReadUtil {
    /**
     * 파일 경로에서 데이터를 읽어오는 메서드. 길이 0의 파일을 읽는 것과 파일을 읽지 않는 것은 다르다.
      */
    public static byte[] read(String filePath) throws IOException {
        File targetFile = new File(filePath);

        byte[] buffer;

        if(!targetFile.exists() || targetFile.isDirectory())
            throw new FileNotFoundException("파일을 찾을 수 없음");

        int bodyLength = (int)targetFile.length();
        buffer = new byte[bodyLength];

        try(FileInputStream fis = new FileInputStream(targetFile)) {
            fis.read(buffer, 0, buffer.length);
        } catch (IOException e) {
            throw e;
        }

        return buffer;
    }
}
