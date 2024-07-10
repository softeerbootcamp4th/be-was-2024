package routehandler.route;

import config.AppConfig;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import http.enums.MIMEType;
import http.utils.MimeTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import utils.FileExtensionUtil;
import utils.FileReadUtil;
import webserver.WebServer;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 정적 경로 데이터 요청을 처리하는 핸들러
 */
public class StaticResourceHandler implements IRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticResourceHandler.class);

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        String fileName = req.getUrl().getPathname();
//        String filePath = AppConfig.STATIC_RESOURCES_PATH + fileName;
        String filePath = fileName;

        // 헤더 작성
        try {
            String extension = FileExtensionUtil.getFileExtension(fileName);
            MIMEType mimeType = MimeTypeUtil.getMimeType(extension);
            String mimeTypeString = mimeType.getMimeType();

            res.getHeaders().putHeader("Content-Type", mimeTypeString);
        } catch (Exception e) {
            // 지원되지 않는 Mime 타입.
            logger.error(e.getMessage());
            res.setStatusInfo(HttpStatusType.UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        // 본문 표현
        try {
            // 본문을 정상적으로 작성
            byte[] data = FileReadUtil.read(AppConfig.STATIC_BASE_PATH + filePath);
            res.setBody(data);
            res.setStatusInfo(HttpStatusType.OK);
        } catch (FileNotFoundException e) {
            // 대응되는 파일이 존재하지 않는 경우 => 컨텐츠는 존재 X
            res.getHeaders().clearHeader("Content-Type");
            res.setStatusInfo(HttpStatusType.NOT_FOUND);
        } catch (IOException e) {
            // 파일이 존재하지만, 내부적 이유로 보여줄 수 없는 경우 => 컨텐츠는 존재 X
            res.getHeaders().clearHeader("Content-Type");
            res.setStatusInfo(HttpStatusType.INTERNAL_SERVER_ERROR);
        }
    }
}
