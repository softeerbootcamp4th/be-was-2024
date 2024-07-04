package byteReader;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ByteReader {
    public String getContentType();
    public byte[] readBytes();
}
