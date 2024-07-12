package distributor;

import webserver.Request;
import webserver.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Distributor {
    static Distributor from(Request request, Response response) {
        String method = request.getHttpMethod();
        if (method.equals("GET")) {
            return new GetDistributor(request, response);
        } else if (method.equals("POST")) {
            // return new PostDistributor(request, response);
        }
        return null;
    }

    void process(DataOutputStream dos) throws IOException;
}
