package util;

import java.io.*;
import java.util.Properties;

/**
 * 파일 입출력 전용 유틸리티 클래스
 */
public class IOUtil {

    // to prevent instantiation
    private IOUtil() {
    }

    /**
     * 경로가 디렉토리인지 판정
     * @param isStatic
     * @param path
     * @return boolean
     */
    public static boolean isDirectory(boolean isStatic, String path) {
        try(InputStream input = IOUtil.class.getClassLoader().getResourceAsStream(ConstantUtil.PROPERTIES)){
            if(input == null){
                throw new FileNotFoundException();
            }
            Properties prop = new Properties();
            prop.load(input);

            String fullPath = (isStatic ? prop.getProperty(ConstantUtil.STATIC_DIR) : prop.getProperty(ConstantUtil.TEMPLATES_DIR)) + path;
            File file = new File(fullPath);
            return file.isDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 파일을 읽어 바이트 배열로 반환하며, 정적과 동적 파일을 구분하여 읽음
     * @param isStatic
     * @param path
     * @return byte[]
     */
    public static byte[] readBytesFromFile(boolean isStatic, String path) throws IOException {
        try (InputStream input = IOUtil.class.getClassLoader().getResourceAsStream(ConstantUtil.PROPERTIES)){
            if(input == null){
                throw new FileNotFoundException();
            }
            Properties prop = new Properties();
            prop.load(input);

            String fullPath = (isStatic ? prop.getProperty(ConstantUtil.STATIC_DIR) : prop.getProperty(ConstantUtil.TEMPLATES_DIR)) + path;
            File file = new File(fullPath);
            if(file.isDirectory()){
                file = new File(fullPath + HttpRequestMapper.DEFAULT_PAGE.getPath());
            }
            int lengthOfBodyContent = (int) file.length();
            byte[] body = new byte[lengthOfBodyContent];
            try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis)) {
                bis.read(body);
            }
            return body;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * 파일을 읽어 String으로 반환하는 메서드 (중복 제거용)
     * @param path
     * @return String
     */
    public static String readBytesFromFile(String path) {
        try {
            return new String(IOUtil.readBytesFromFile(false, path));
        } catch (IOException e) {
            return HttpRequestMapper.DEFAULT_PAGE.getPath();
        }
    }

    /**
     * 파일을 저장하고 그 경로를 반환
     * @param fileData
     * @param fileName
     * @return String
     */
    public static String saveFile(byte[] fileData, String fileName) {
        // uploads라는 디렉토리 생성
        File dir = new File(ConstantUtil.FILE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = ConstantUtil.FILE_DIR + fileName;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
