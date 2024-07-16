package route.routes.post;

import chain.record.FormItem;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import http.utils.FormDataUtil;
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

        logger.debug("title: {}", title);
        logger.debug("content: {}", content);
        logger.debug("images: {}", images);

        for(var image : images) {
            try(FileOutputStream fos = new FileOutputStream("/Users/admin/Desktop/" + image.filename())) {
                fos.write(image.data().getBytes(StandardCharsets.ISO_8859_1));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        res.setStatusInfo(HttpStatusType.CREATED);
        res.redirect("/");
    }
}
