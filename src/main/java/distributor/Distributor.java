package distributor;

import webserver.Request;

import java.io.DataOutputStream;
import java.io.IOException;

public class Distributor {
    public static Distributor of(Request request, DataOutputStream dos) {
        String method = request.getHttpMethod();
        if (method.equals("GET")) {
            return new GetDistributor(request, dos);
        } else if (method.equals("POST")) {
            return new PostDistributor(request, dos);
        }
        return null;
    }

    public void process() throws IOException {
    }
}
