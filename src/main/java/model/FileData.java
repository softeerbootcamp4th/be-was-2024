package model;

public class FileData {
    private final byte[] fileBinaryData;
    private final String fileName;

    private FileData(Builder builder) {
        this.fileBinaryData = builder.fileBinaryData;
        this.fileName = builder.fileName;
    }

    public static class Builder {
        private byte[] fileBinaryData;
        private String fileName;

        public Builder fileBinaryData(byte[] fileBinaryData) {
            this.fileBinaryData = fileBinaryData;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public FileData build() {
            return new FileData(this);
        }
    }

    public byte[] getFileBinaryData() {
        return this.fileBinaryData;
    }

    public String getFileName() {
        return this.fileName;
    }
}
