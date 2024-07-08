package distributor;

import processor.LogicProcessor;
import webserver.Request;
import webserver.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class GetDistributor extends Distributor {
    Request request;
    Response response;
    LogicProcessor logicProcessor = new LogicProcessor();

    protected GetDistributor(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void process(DataOutputStream dos) throws IOException {
        if (request.isQueryString()) {
            processQeury(dos);
        } else {
            response.response(request.getPath(), dos);
        }
    }

    private void processQeury(DataOutputStream dos) throws IOException {
        String path = request.getPath();
        if (path.equals("/user/create")) {
            logicProcessor.createUser(request);
            response.redirect("/index.html", dos, 302);
        }
    }
}
