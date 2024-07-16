package webserver;

import common.FileUtils;
import common.WebUtils;
import file.ViewFile;
import web.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public class ViewResolver {

    public static ViewFile getProperFileFromRequest(HttpRequest request, OutputStream out) throws IOException {
        String filePath = request.getPath(), extension;

        // REST 요청일 경우에는 filePath를 구해야 함
        if(WebUtils.isRESTRequest(filePath)) {
            filePath = WebAdapter.resolveRequestUri(request, out);
        }
        extension = FileUtils.getExtensionFromPath(filePath);


        return new ViewFile(filePath, extension);

    }
}
