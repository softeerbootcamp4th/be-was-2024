package webserver.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.http.enums.Extension;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;



/*
* readfile function
* */
public class ReadFile implements ApiFunction {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String root = "./src/main/resources/static";

    @Override
    public HttpResponse funcion(HttpRequest request) {
        try{
            Extension extension;
            String pathname = root + request.getUri().getPath();
            String file = pathname.substring(pathname.lastIndexOf("/"));// 마지막 부분의 확장자 확인
            if(!file.contains(".")) { // 확장자가 있다면
                pathname = pathname + "/index.html";
                extension = Extension.HTML;
            }else{
                extension = Extension.valueOfExtension(file.split("\\.")[1]); // 마지막 인덱스
            }
            byte[] body = Files.readAllBytes(new File(pathname).toPath());

            if(body.length >0 ){
                return new HttpResponse.ResponseBuilder(200)
                        .addheader("Content-Type", extension.getContentType())
                        .addheader("Content-Length", String.valueOf(body.length))
                        .setBody(body)
                        .build();
            }
            else return new HttpResponse.ResponseBuilder(404).build();

        }catch(IOException e){
            logger.error(e.getMessage());
        }

        return null;
    }
}
