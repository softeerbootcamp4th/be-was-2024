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



/*
* readfile function
* */
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


    @Override
    public HttpResponse function(HttpRequest request) {
        try{
            Extension extension;
            String pathname = root + request.getUrl().getPath().replace("/resource","");
            String file = pathname.substring(pathname.lastIndexOf("/")); // 마지막 부분의 확장자 확인
            if(!file.contains(".")) { // 확장자가 있다면
                return ResponseLibrary.notFound;
            }else{
                extension = Extension.valueOfExtension(file.split("\\.")[1]); // 마지막 인덱스
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
