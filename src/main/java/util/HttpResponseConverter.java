package util;

import db.Database;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import model.enums.HttpStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponseConverter {

    // httpRequest를 httpresponse로 변경하는 로직
    public HttpResponse with(HttpRequest httpRequest) throws IOException {
        if (isHttpResponseDynamic(httpRequest.getPath())) {
            return createDynamicHttpResponse(httpRequest);
        } else {
            return createStaticHttpResponse(httpRequest);

        }
    }


    private boolean isHttpResponseDynamic(String path){
        return !path.contains(".");
    }

    private HttpResponse createDynamicHttpResponse(HttpRequest httpRequest) throws IOException {
        HttpPathMapper httpPathMapper = new HttpPathMapper();
        return httpPathMapper.map(httpRequest);

    }

    private HttpResponse createStaticHttpResponse(HttpRequest httpRequest) throws IOException {

        String contentType = ExtensionMapper.getContentTypeFromRequestPath(httpRequest.getPath());
        byte[] body = FileMapper.getByteConvertedFile(httpRequest.getPath());

        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type", contentType + ";charset=utf-8");
        headers.put("Content-Length", String.valueOf(body.length));
//        headers.add("\r\n"); //TODO : get,post방식에 따라 필요할수도 있고 필요없을수도 있음,
        return HttpResponse.of("HTTP/1.1", HttpStatus.OK, headers, body);
    }
}
