package webserver;

import common.FileUtils;
import common.ResponseUtils;
import common.WebUtils;
import file.ViewFile;
import web.DynamicHtmlGenerator;
import web.HttpRequest;
import web.HttpResponse;

import java.io.*;

public class ViewResolver {

    /**
     * request에서 ViewFile객체를 만든 후, 실제 응답을 날리는 readAndResponseFromPath 메서드를 호출한다.
     */
    public static void responseProperFileFromRequest(HttpRequest request, OutputStream out) throws IOException {

        ViewFile viewFile = FileUtils.makeFileFromRequest(request);
        readAndResponseFromPath(request, out, FileUtils.getStaticFilePath(viewFile.getPath()), WebUtils.getProperContentType(viewFile.getExtension()));
    }

    /**
     * 경로에서 적절한 뷰 파일을 찾아서 응답합니다.
     */
    public static void readAndResponseFromPath(HttpRequest request, OutputStream out, String filePath, String contentType) throws IOException{
        DataOutputStream dos = new DataOutputStream(out);

        if(filePath.equals(FileUtils.STATIC_DIR_PATH+"/index.html")) {
            DynamicHtmlGenerator.responseDynamicStringHtml(request, dos, contentType);
            return;
        }

        File file = new File(filePath);
        byte[] body = new byte[(int)file.length()];

        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
            HttpResponse response = ResponseUtils.responseSuccessWithFile(contentType, body);
            response.writeInBytes(dos);
        } catch (Exception e) {
            HttpResponse response = ResponseUtils.responseServerError();
            response.writeInBytes(dos);
            e.printStackTrace();
        }
    }
}
