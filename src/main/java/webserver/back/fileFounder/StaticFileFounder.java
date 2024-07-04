package webserver.back.fileFounder;

import webserver.back.byteReader.ByteReader;
import webserver.back.byteReader.StaticFileReader;
import webserver.back.returnType.ContentTypeMaker;

import java.io.FileNotFoundException;

public class StaticFileFounder implements FileFounder{
    private final String FILE_BASE_URL ="./src/main/resources/static/";

    @Override
    public ByteReader findFile(String fileUrl) throws FileNotFoundException {
        String contentType = ContentTypeMaker.getContentType(fileUrl);
        return new StaticFileReader(FILE_BASE_URL + fileUrl,contentType);
    }


}
