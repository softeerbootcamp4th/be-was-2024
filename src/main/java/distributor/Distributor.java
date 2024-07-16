package distributor;

import model.ViewData;
import webserver.Request;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Distributor {
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

    public abstract ViewData getViewData();
}
