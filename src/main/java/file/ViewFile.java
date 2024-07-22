package file;

/**
 * 리소스 파일의 정보를 저장 (path: 리소스 경로, extension: 확장자)
 */
public class ViewFile {

    private final String path;
    private final String extension;

    public ViewFile(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    public String getPath() {
        return this.path;
    }

    public String getExtension() {
        return this.extension;
    }
}
