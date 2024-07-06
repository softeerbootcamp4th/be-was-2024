package webserver;

import common.FileUtils;
import common.WebUtils;
import file.ViewFile;

import java.io.IOException;
import java.io.OutputStream;

public class ViewResolver {

    public static ViewFile getProperFileFromPath(String path, OutputStream out) throws IOException {
        String filePath = path, extension;

        if(WebUtils.isRESTRequest(path)) {
            filePath = WebAdapter.resolveRequestUri(path, out);
        }
        extension = FileUtils.getExtensionFromPath(filePath);


        return new ViewFile(filePath, extension);

    }
}
