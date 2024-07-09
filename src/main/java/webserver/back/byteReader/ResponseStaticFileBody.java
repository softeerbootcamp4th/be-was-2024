package webserver.back.byteReader;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ResponseStaticFileBody implements Body {
    private FileInputStream fileInputStream;
    private String contentType;
    public ResponseStaticFileBody(String fileUrl, String contentType) throws FileNotFoundException {
        this.fileInputStream = new FileInputStream(fileUrl);
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public byte[] makeBytes()  {
        try{
            return fileInputStream.readAllBytes();
        }
        catch(IOException e){
            return new byte[0];
        }
    }
}
