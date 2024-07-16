package distributor;

import webserver.Request;
import webserver.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class Distributor {
    public static Distributor from(Request request) {
        String method = request.getHttpMethod();
        if (method.equals("GET")) {
            return new GetDistributor(request);
        } else if (method.equals("POST")) {
            return new PostDistributor(request);
        }
        return null;
    }

    public void process(DataOutputStream dos) throws IOException {
    }
}
