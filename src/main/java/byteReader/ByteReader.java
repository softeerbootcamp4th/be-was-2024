package byteReader;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ByteReader {
    public byte[] readBytes(String url) throws IOException;
}
