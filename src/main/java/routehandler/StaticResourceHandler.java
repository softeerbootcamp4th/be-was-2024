package routehandler;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import http.enums.MIMEType;
import http.utils.MimeTypeUtil;
import utils.FileExtensionUtil;
import utils.FileReadUtil;

public class StaticResourceHandler implements IRouteHandler {

    private final String STATIC_URL;
    public StaticResourceHandler(String STATIC_URL) {
        this.STATIC_URL = STATIC_URL;
    }

    @Override
    public boolean canMatch(Object... args) {
        return true;
    }

    @Override
    public void handle(MyHttpRequest request, MyHttpResponse response) {
        String fileName = request.getUrl();
        String filePath = STATIC_URL + fileName;

        // 본문 표현
        byte[] data = FileReadUtil.read(filePath);
        try {
            String extension = FileExtensionUtil.getFileExtension(fileName);
            MIMEType mimeType = MimeTypeUtil.getMimeType(extension);
            String mimeTypeString = mimeType.getMimeType();

            // TODO 헤더 설정을 처리할 다른 방법이 없는지 고민 ex) 헤더 일괄 처리?
            // 컨텐츠 타입 설정
            response.getHeaders().putHeader("Content-Type", mimeTypeString);
            response.setBody(data);

            response.setStatusInfo(HttpStatusType.OK);
        } catch (Exception e) {
            response.setStatusInfo(HttpStatusType.INTERNAL_SERVER_ERROR);
        }
    }

}
