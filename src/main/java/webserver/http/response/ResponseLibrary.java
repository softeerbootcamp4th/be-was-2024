package webserver.http.response;

import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;

import static webserver.http.response.PageBuilder.buildErrorPage;

public class ResponseLibrary {
    public static HttpResponse notFound =
            new HttpResponse.ResponseBuilder(404)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(buildErrorPage(StatusCode.CODE404))
                    .build();


}
