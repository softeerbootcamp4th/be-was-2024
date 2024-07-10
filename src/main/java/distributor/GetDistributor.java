package distributor;

import webserver.Request;
import webserver.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class GetDistributor extends Distributor {
    Request request;

    protected GetDistributor(Request request) {
        this.request = request;
    }

    @Override
    public void process(DataOutputStream dos) throws IOException {
        if (request.isQueryString()) {
            processQuery(dos);
        } else {
            Response response = new Response.Builder()
                    .url(request.getPath())
                    .statusCode(200)
                    .dataOutputStream(dos)
                    .build();

            response.sendResponse();
        }
    }

    private void processQuery(DataOutputStream dos) throws IOException {
        String path = request.getPath();
        if (path.equals("/user/create")) {
            Response response = new Response.Builder()
                    .url("/not_found.html")
                    .dataOutputStream(dos)
                    .redirectCode(404)
                    .build();

            response.sendResponse();
        }
    }
}
