package byteReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StaticFileReader implements ByteReader {
    private FileInputStream fileInputStream;
    public StaticFileReader(String fileUrl) throws FileNotFoundException {
        fileInputStream = new FileInputStream(fileUrl);
    }
    @Override
    public byte[] readBytes() throws IOException {
        return fileInputStream.readAllBytes();
    }
}
