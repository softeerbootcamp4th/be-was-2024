package readType;

import java.io.FileInputStream;

public class StaticHtmlByteReader implements ByteReader {
    private final String FILE_BASE_URL ="./src/main/resources/static";
    @Override
    public byte[] readBytes(String url)  {
        try{
            FileInputStream fileInputStream = new FileInputStream(FILE_BASE_URL+url);
            return fileInputStream.readAllBytes();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new byte[0];
    }
}
