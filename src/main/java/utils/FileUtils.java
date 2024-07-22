package utils;

import type.MIME;

import java.io.*;
import java.util.UUID;

public class FileUtils {
    public static boolean isExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isFile(String filePath) {
        File file = new File(filePath);
        return file.isFile();
    }

    public static String findMIME(String fileName) {
        String[] list = fileName.split("\\.");
        return MIME.findByContentType(list.length > 1 ? list[list.length - 1] : "").getContentType();
    }

    public static byte[] readFileToBytes(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }

        return bytes;
    }

    public static String uploadFile(String path, String fileName, byte[] data) throws IOException {
        UUID uuid = UUID.randomUUID();
        // 파일 이름 중복 확인 필요
        String concatenatedFileName = uuid + "_" + fileName;
        File filePath = new File(path + "/" + concatenatedFileName);
        if (!filePath.isFile()) filePath.createNewFile();
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(data);
        fos.close();
        return concatenatedFileName;
    }
}
