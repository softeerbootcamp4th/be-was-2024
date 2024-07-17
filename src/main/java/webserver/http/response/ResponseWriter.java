package webserver.http.response;

import util.Utils;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseWriter {

    private final OutputStream outputStream;

    public ResponseWriter(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void write(Response response) throws IOException {
        if(response.getStatus().equals(Status.NOT_FOUND)) {
            response = new Response.Builder(Status.SEE_OTHER)
                    .redirect("/error/404.html")
                    .build();
        }else if(response.getStatus().equals(Status.METHOD_NOT_ALLOWED)) {
            response = new Response.Builder(Status.SEE_OTHER)
                    .redirect("/error/405.html")
                    .build();
        }else if(response.getStatus().equals(Status.INTERNAL_SERVER_ERROR)) {
            String body = new String(Utils.getFile("/error/500.html"));
            body = body.replace("{MESSAGE}", response.getBody());
            response = new Response.Builder(Status.INTERNAL_SERVER_ERROR)
                    .body(body)
                    .build();
        }
        outputStream.write(response.toByte());
    }

}
