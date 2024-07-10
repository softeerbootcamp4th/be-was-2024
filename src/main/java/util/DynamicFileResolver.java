package util;

import constant.FileExtensionType;
import constant.HttpStatus;
import dto.HttpResponse;
import exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class DynamicFileResolver {
    private static final Logger logger = LoggerFactory.getLogger(DynamicFileResolver.class);

    private final static String prefix = "src/main/resources/templates";
    private final static String suffix = ".html";

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CHARSET_UTF8 = "utf-8";
    private static final String ERROR_MESSAGE_404 =
            "<html>" +
                    "<head><title>404 Not Found</title></head>" +
                    "<body><h1>404 Not Found</h1></body>" +
                    "</html>";

    public static void setHttpResponse(HttpResponse httpResponse, String fileName) throws UnsupportedEncodingException {
        String filePath = prefix + fileName + suffix;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ResourceNotFoundException("Resource not found: " + filePath);
        }

        byte[] fileBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        }
        catch(IOException e){
            logger.error(e.getMessage());

            // 동적 파일 읽기 실패시, 404 에러 응답
            httpResponse.setHttpStatus(HttpStatus.NOT_FOUND);
            httpResponse.addHeader(CONTENT_TYPE, FileExtensionType.HTML.getContentType());
            httpResponse.addHeader(CONTENT_LENGTH, String.valueOf(ERROR_MESSAGE_404.length()));
            httpResponse.setBody(ERROR_MESSAGE_404.getBytes(CHARSET_UTF8));
        }

        // 동적 파일 응답 설정
        httpResponse.setHttpStatus(HttpStatus.OK);
        httpResponse.addHeader(CONTENT_TYPE, FileExtensionType.HTML.getContentType());
        httpResponse.addHeader(CONTENT_TYPE, CHARSET_UTF8);
        httpResponse.addHeader(CONTENT_LENGTH, String.valueOf(fileBytes.length));
        httpResponse.setBody(fileBytes);

    }
}
