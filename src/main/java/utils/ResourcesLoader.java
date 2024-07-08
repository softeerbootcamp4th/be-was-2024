package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourcesLoader {

  private static final Logger logger = LoggerFactory.getLogger(ResourcesLoader.class);

  public static byte[] getFile(String filePath) throws IOException {
    byte[] body;
    logger.debug("filePath = {}", filePath);
    try (
            InputStream inputStream = ResourcesLoader.class.getClassLoader().getResourceAsStream(filePath)
    ) {
      body = inputStream.readAllBytes();
    } catch (Exception e) {
      logger.debug("Exception = {}", e.getMessage());
      throw new IllegalArgumentException("존재하지 않는 파일입니다.");
    }
    return body;
  }
}
