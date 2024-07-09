package file;

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
