package route.routes.post;

import db.tables.ImageLinkTable;
import db.tables.PostTable;
import http.form.FormItem;
import config.AppConfig;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import http.form.FormDataUtil;
import model.ImageLink;
import model.Post;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import utils.FileWriteUtil;
import utils.NameUtil;

import java.util.ArrayList;
import java.util.List;

public class PostWriteHandler implements IRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostWriteHandler.class);

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        User user = (User) req.getStoreData(AppConfig.USER);
        if(user == null) throw new RuntimeException("로그인하지 않은 유저가 접근할 수 없음");

        FormItem title = (FormItem) FormDataUtil.getFormData(req, "title");
        FormItem content = (FormItem) FormDataUtil.getFormData(req, "content");
        List<FormItem> images = (List<FormItem>) FormDataUtil.getFormData(req, "image[]");

        List<String> fileNames = writeImageFiles(images);

        Post post = new Post(
                title.dataAsText(),
                content.dataAsText(),
                user.getUserId()
        );

        PostTable.insert(post);
        List<ImageLink> links = fileNames
                .stream()
                .map(filename -> new ImageLink(filename, post.getId()))
                .toList();
        ImageLinkTable.insertMany(links);

        res.redirect("/");
    }

    private List<String> writeImageFiles(List<FormItem> images) {
        List<String> filenames = new ArrayList<>();
        for(var image : images) {
            String filename = NameUtil.genRandomName();

            if(image.filename() != null) {
                String[] nameSplitByDot = image.filename().split("\\.");
                if(nameSplitByDot.length > 1) {
                    filename += "." + nameSplitByDot[nameSplitByDot.length - 1];
                }
            }
            FileWriteUtil.writeToLocal(filename,image.data());
            filenames.add(filename);
        }
        return filenames;
    }
}
