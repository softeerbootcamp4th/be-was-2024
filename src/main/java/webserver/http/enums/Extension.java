package webserver.http.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 파일 확장자에 따른 content-type을 정리한 enum
 */
public  enum Extension {
    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css;charset=utf-8"),
    JS("js", "text/javascript;charset=utf-8"),
    ICO("ico","image/x-icon"),
    PNG("png", "image/png"),
    JPG("jpg","image/jpeg"),
    SVG("svg","image/svg+xml"),
    Default("", "text/pain");

    private final String extensionName;
    private final String contentType;

    Extension(String extensionName, String contentType) {
        this.extensionName = extensionName;
        this.contentType = contentType;
    }

    private String extension(){
        return extensionName;
    }

    private String contentType(){
        return contentType;
    }

    private static final Map<String, Extension> BY_EXTENSION =
            Stream.of(values()).collect(Collectors.toMap(Extension::extension, e -> e));

    /**
     * 해당 extention에 대한 content-type이 있는지 확인한다.
     * <p>
     *     존재하지 않는다면 Default 를 반환한다.
     * </p>
     * @param label 파일의 확장자명
     * @return 해당 확장자에 대한 enum
     * @see Extension#Default
     */
    public static Extension valueOfExtension(String label) {
        Extension extension = BY_EXTENSION.get(label);
        if (extension == null) { extension = Default; }
        return extension;
    }

    public String getContentType() {
        return contentType;
    }
}
