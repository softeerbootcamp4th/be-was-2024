package webserver.util;

public class FileData {
    String fileName;
    byte[] data;

    FileData(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }
}