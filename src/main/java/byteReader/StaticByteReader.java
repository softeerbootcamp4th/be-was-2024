package byteReader;

import java.io.FileInputStream;

public interface StaticByteReader extends ByteReader{
    final String FILE_BASE_URL ="./src/main/resources/static";
}

class StaticFileByteReader implements StaticByteReader{
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
