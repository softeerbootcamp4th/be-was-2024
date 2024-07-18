package util;

import db.SessionDatabase;
import db.UserDatabase;
import dto.HttpRequest;
import dto.HttpResponse;
import dto.enums.HttpStatus;
import model.Session;
import util.constant.StringConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static util.constant.StringConstants.*;

public class HttpResponseConverter {


    // httpRequest 를 httpResponse 로 변경하는 로직
    public HttpResponse with(HttpRequest httpRequest) throws IOException {
        String sessionId = httpRequest.getSessionOrNull().orElse(null);
        String userId=  null;
        if (sessionId != null) {
             userId = SessionDatabase.findSessionById(sessionId).map(Session::getUserId).orElse(null);
        }

        HttpPathMapper httpPathMapper = new HttpPathMapper();
        return httpPathMapper.map(httpRequest,userId);

//
//        if (isHttpResponseDynamic(httpRequest.getPath())) {
//            return createDynamicHttpResponse(httpRequest, userId);
//        } else {
//            return createStaticHttpResponse(httpRequest, userId);
//
//        }
    }


    private boolean isHttpResponseDynamic(String path) {
        return !path.contains(DOT);
    }

    private HttpResponse createDynamicHttpResponse(HttpRequest httpRequest, String userId) throws IOException {
        HttpPathMapper httpPathMapper = new HttpPathMapper();
        return httpPathMapper.map(httpRequest,userId);

    }

    private HttpResponse createStaticHttpResponse(HttpRequest httpRequest, String userId) throws IOException {

        String contentType = ExtensionMapper.getContentTypeFromRequestPath(httpRequest.getPath());
        byte[] body = FileMapper.getByteConvertedFile(httpRequest.getPath(),userId);

        Map<String, String> headers = new HashMap<>();
        headers.put(StringConstants.HEADER_CONTENT_TYPE, contentType + SEMICOLON + HEADER_CHARSET_UTF_8);
        headers.put(HEADER_CONTENT_LENGTH, String.valueOf(body.length));
        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.OK, headers, body);
    }
}
