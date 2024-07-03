package byteReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface StaticByteReader extends ByteReader{
   String FILE_BASE_URL ="./src/main/resources/static/";
}

class StaticFileByteReader implements StaticByteReader{
    @Override
    public byte[] readBytes(String url) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FILE_BASE_URL+url);
        return fileInputStream.readAllBytes();
    }
}
