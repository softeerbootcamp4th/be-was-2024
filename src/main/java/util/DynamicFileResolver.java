package util;

import exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DynamicFileResolver {
    private static final Logger logger = LoggerFactory.getLogger(DynamicFileResolver.class);

    private final static String prefix = "/src/main/resources/templates";
    private final static String suffix = ".html";

    public static byte[] readDynamicFile(String fileName) {
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
            return null;
        }

        return fileBytes;
    }
}
