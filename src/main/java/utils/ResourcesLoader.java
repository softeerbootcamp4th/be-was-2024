package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourcesLoader {

  private static final Logger logger = LoggerFactory.getLogger(ResourcesLoader.class);

  public static byte[] getFile(String filePath) {
    byte[] body;
    if(filePath.startsWith("/")) {
      filePath = filePath.substring(1);
    }
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
