package controller;

import exception.ModelException;
import handler.ArticleHandler;
import handler.UserHandler;
import model.User;
import util.*;

import java.util.Map;

/**
 * 게시글 관련 요청 처리
 */
public class ArticleController extends AbstractController{

    /**
     * 게시글 작성 화면을 불러옴
     * @param request
     * @return
     */
    @Override
    public HttpResponse doGet(HttpRequest request) {
        if(request.getSession() == null){
            return HttpResponse.sendRedirect(HttpRequestMapper.LOGIN.getPath(), request.getHttpVersion());
        }

        return HttpResponse.forward(HttpRequestMapper.ARTICLE.getPath(), request.getHttpVersion());
    }

    /**
     * 게시글 요청을 처리
     * @param request
     * @return HttpResponse
     */
    @Override
    public HttpResponse doPost(HttpRequest request) {
        String userId = request.getSession().getUserId();
        User user = UserHandler.getInstance().findById(userId).orElseThrow(() -> new ModelException(ConstantUtil.USER_NOT_FOUND));
        Map<String, String> formData = request.getFormData();
        Map<String, byte[]> files = request.getFileData();

        for(Map.Entry<String, byte[]> entry : files.entrySet()){
            String fileName = entry.getKey();
            byte[] fileData = entry.getValue();
            String savedPath = IOUtil.saveFile(fileData, fileName);
            formData.put(ConstantUtil.IMAGE_PATH, savedPath);
        }

        formData.put(ConstantUtil.AUTHOR_NAME, user.getName()); // authorName 추가
        ArticleHandler.getInstance().create(formData);
        return HttpResponse.sendRedirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
    }
}
