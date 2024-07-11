package util;

import dto.HttpRequest;
import dto.HttpResponse;
import dto.enums.HttpStatus;
import util.constant.StringConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static util.constant.StringConstants.*;

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
        return !path.contains(DOT);
    }

    private HttpResponse createDynamicHttpResponse(HttpRequest httpRequest) throws IOException {
        HttpPathMapper httpPathMapper = new HttpPathMapper();
        return httpPathMapper.map(httpRequest);

    }

    private HttpResponse createStaticHttpResponse(HttpRequest httpRequest) throws IOException {

        String contentType = ExtensionMapper.getContentTypeFromRequestPath(httpRequest.getPath());
        byte[] body = FileMapper.getByteConvertedFile(httpRequest.getPath());

        Map<String,String> headers = new HashMap<>();
        headers.put(StringConstants.HEADER_CONTENT_TYPE, contentType + SEMICOLON + HEADER_CHARSET_UTF_8);
        headers.put(HEADER_CONTENT_LENGTH, String.valueOf(body.length));
//        headers.add("\r\n"); //TODO : get,post방식에 따라 필요할수도 있고 필요없을수도 있음,
        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.OK, headers, body);
    }
}
