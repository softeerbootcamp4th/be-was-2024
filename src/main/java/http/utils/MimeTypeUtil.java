package http.utils;

import http.enums.MIMEType;

public class MimeTypeUtil {
    public static MIMEType getMimeType(String mimeTypeName) {
        try {
            String name = mimeTypeName.toLowerCase();
            return MIMEType.valueOf(name);
        }catch(IllegalArgumentException e) {
            throw new RuntimeException("not support mimetype " + mimeTypeName);
        }
    }
}
