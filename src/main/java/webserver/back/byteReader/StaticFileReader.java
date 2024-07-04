package webserver.back.byteReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StaticFileReader implements ByteReader {
    private FileInputStream fileInputStream;
    private String contentType;
    public StaticFileReader(String fileUrl,String contentType) throws FileNotFoundException {
        this.fileInputStream = new FileInputStream(fileUrl);
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public byte[] readBytes()  {
        try{
            return fileInputStream.readAllBytes();
        }
        catch(IOException e){
            return new byte[0];
        }

    }
}
