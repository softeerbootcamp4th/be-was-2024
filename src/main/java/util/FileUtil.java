package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 파일 관련 기능 처리하는 유틸 클래스
 */
public class FileUtil {
    private static final String IMAGE_DIR = "src/main/resources/static/post-images/";
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 파일의 모든 바이트를 읽어서 반환한다.
     * @param file
     * @return 파일의 모든 바이트
     * @throws IOException
     */
    public static byte[] readAllBytesFromFile(File file) throws IOException {
        byte[] bytes;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            long fileLength = file.length();
            if (fileLength > Integer.MAX_VALUE) {
                throw new IOException("File is too large to read into a byte array");
            }
            bytes = new byte[(int) fileLength];
            int read = bufferedInputStream.read(bytes);
            if (read < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
        }
        return bytes;
    }

    /**
     * 모든 바이트를 파일에 쓴다.
     * @param bytes
     * @param fileName
     * @throws IOException
     */
    public static void writeAllBytesToFile(byte[] bytes, String fileName) throws IOException {
        File directory = new File(IMAGE_DIR);
        if (!directory.exists()) {
            boolean dirsCreated = directory.mkdirs();
            if (!dirsCreated) {
                logger.error("디렉토리를 생성할 수 없습니다.");
                return;
            }
        }
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(IMAGE_DIR + fileName))) {
            bufferedOutputStream.write(bytes);
        }
    }
}
