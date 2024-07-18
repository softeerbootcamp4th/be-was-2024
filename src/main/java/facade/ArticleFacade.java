package facade;

import common.FileUtils;
import common.RequestUtils;
import common.StringUtils;
import db.ArticleH2Database;
import model.Article;
import web.HttpRequest;

import java.util.Arrays;
import java.util.List;

public class ArticleFacade {

    /**
     * byte 단위로 body 파싱
     */
    public static void saveArticleData(HttpRequest request) {
        String userId = SessionFacade.getUserIdFromSession(request);
        // content(string), image(byte[])를 파싱
        String boundaryKey = "--"+ RequestUtils.getBoundaryKey(request.getContentType());
        byte[] delimiter = boundaryKey.getBytes();
        byte[] body = request.getBody();

        List<byte[]> parts = StringUtils.splitBytes(body, delimiter);
        byte[] image = parts.get(1);
        String content = new String(parts.get(2));
        List<byte[]> imageParts = StringUtils.splitBytes(image, "\r\n\r\n".getBytes());

        byte[] pureImage = imageParts.get(1);
        String imgExtension = FileUtils.getImageExtensionFromPath(new String(imageParts.get(0)));

        pureImage = Arrays.copyOf(pureImage, pureImage.length-2);

        String pureContent = content.split("\r\n\r\n")[1];
        pureContent = pureContent.substring(0, pureContent.length()-2);

        String fileName = FileUtils.saveFile(pureImage, imgExtension);
        ArticleH2Database.createArticle(new Article(null, userId, fileName, pureContent), SessionFacade.getUserIdFromSession(request));
    }
}
