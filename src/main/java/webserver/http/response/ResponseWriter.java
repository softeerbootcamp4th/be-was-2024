package webserver.http.response;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseWriter {

    private final OutputStream outputStream;

    public ResponseWriter(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void write(Response response) throws IOException {
        outputStream.write(response.toByte());
    }

}
