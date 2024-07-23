package util;

import dto.HttpRequest;
import dto.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static logic.Logics.*;
import static util.constant.StringConstants.*;

public class HttpPathMapper {
    private static final Logger logger = LoggerFactory.getLogger(HttpPathMapper.class);


    public HttpResponse map(HttpRequest httpRequest,String userId) throws IOException {
        logger.info("httpRequest.getPath() = " + httpRequest.getPath());

        return switch (httpRequest.getPath()) {
            case PATH_CREATE -> create(httpRequest);
            case PATH_LOGIN -> login(httpRequest);
            case PATH_LOGOUT -> logout(httpRequest);
            case USER_LIST -> getUserList(httpRequest,userId);
            default -> throw new RuntimeException("Invalid path");
        };

    }

}
