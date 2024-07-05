package webserver.request;

import java.io.IOException;

public class HttpRequest {

    private Path path;

    public HttpRequest(String request) throws IOException {
        String[] inputLines = request.split("\n");
        String[] startLine = inputLines[0].split(" ");
        this.path = new Path(startLine[1]);
    }

    public Path getPath(){
        return this.path;
    }

}
