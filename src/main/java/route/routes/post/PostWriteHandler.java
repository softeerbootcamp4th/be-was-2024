package route.routes.post;

import http.form.FormItem;
import config.AppConfig;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import http.form.FormDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PostWriteHandler implements IRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostWriteHandler.class);

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        FormItem title = (FormItem) FormDataUtil.getFormData(req, "title");
        FormItem content = (FormItem) FormDataUtil.getFormData(req, "content");
        List<FormItem> images = (List<FormItem>) FormDataUtil.getFormData(req, "image[]");

        logger.debug("title: {}", title.dataAsText());
        logger.debug("content: {}", title.dataAsText());
        logger.debug("images: {}", images);

        for(var image : images) {
            try(FileOutputStream fos = new FileOutputStream(AppConfig.FILE_SRC + image.filename())) {
                fos.write(image.data());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        res.setStatusInfo(HttpStatusType.CREATED);
        res.redirect("/");
    }
}
