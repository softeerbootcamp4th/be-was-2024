package webserver.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.response.ResponseLibrary;
import webserver.http.enums.Extension;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * static resource 요청 처리하는 클래스
 */
public class ReadFileHandler implements FunctionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ReadFileHandler.class);
    private static final String root = "./src/main/resources/static";


    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new ReadFileHandler();

        return single_instance;
    }


    /**
     * request에 해당하는 file을 반환한다.
     * @param request 해당 요청에 대한 Httprequest class
     * @return 반환할 HttpResponse class
     */
    @Override
    public HttpResponse function(HttpRequest request) {
        try{
            Extension extension;
            String pathname = root + request.getUrl().getPath().replace("/resource","");
            String file = pathname.substring(pathname.lastIndexOf("/")); // 마지막 부분의 확장자 확인
            if(!file.contains(".")) { // 확장자가 있다면
                return ResponseLibrary.notFound;
            }else{
                int lastDotIndex = file.lastIndexOf('.');
                extension = Extension.valueOfExtension(file.substring(lastDotIndex + 1)); // 마지막 인덱스
            }

            byte[] body = Files.readAllBytes(new File(pathname).toPath());
            if(body.length >0 ){
                return new HttpResponse.ResponseBuilder(200)
                        .addheader("Content-Type", extension.getContentType())
                        .setBody(body)
                        .build();
            }
            else return ResponseLibrary.notFound;

        }catch(IOException e){
            logger.error(e.getMessage());
            return ResponseLibrary.notFound;
        }
    }
}
