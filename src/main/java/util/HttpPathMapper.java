package util;

import db.Database;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import model.enums.HttpMethod;
import model.enums.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static logic.Logics.*;
import static util.constant.StringConstants.*;

public class HttpPathMapper {
    private static final Logger logger = LoggerFactory.getLogger(HttpPathMapper.class);


    public HttpResponse map(HttpRequest httpRequest) throws IOException {
        logger.info("httpRequest.getPath() = " + httpRequest.getPath());

        return switch (httpRequest.getPath()) {
            case PATH_CREATE -> create(httpRequest);
            case PATH_LOGIN -> login(httpRequest);
            default -> throw new RuntimeException("Invalid path");
        };

    }

}
