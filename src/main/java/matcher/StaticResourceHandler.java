package matcher;

import java.io.File;

public class StaticResourceHandler {
    private final String STATIC_URL = "./src/main/resources/static";

    byte[] body = (new File("./src/main/resources/static" + uri).toPath());

//    public GetBody() {
//    }
}
