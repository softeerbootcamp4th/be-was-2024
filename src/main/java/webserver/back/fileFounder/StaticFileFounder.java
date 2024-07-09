package webserver.back.fileFounder;

import webserver.back.byteReader.Body;
import webserver.back.byteReader.ResponseStaticFileBody;
import webserver.back.contentType.ContentTypeMaker;

import java.io.FileNotFoundException;

public class StaticFileFounder implements FileFounder{
    private final String FILE_BASE_URL ="./src/main/resources/static";

    @Override
    public Body findFile(String fileUrl) throws FileNotFoundException {
        String contentType = ContentTypeMaker.getContentType(fileUrl);
        return new ResponseStaticFileBody(FILE_BASE_URL + fileUrl,contentType);
    }


}
