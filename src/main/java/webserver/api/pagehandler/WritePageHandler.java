package webserver.api.pagehandler;

import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.response.HtmlFiles;
import webserver.http.response.PageBuilder;

import java.io.IOException;

public class WritePageHandler implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new WritePageHandler();

        return single_instance;
    }

    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", "text/html; charset=utf-8")
                .setBody(HtmlFiles.readHtmlByte(HtmlFiles.WRITE))
                .build();
    }
}
