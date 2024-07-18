package controller;

import handler.ArticleHandler;
import util.*;

/**
 * Default 페이지를 처리
 */
public class DefaultController extends AbstractController{

    /**
     * Default 페이지를 불러옴
     * 루트 페이지인 경우 DEFAULT_PAGE로 리다이렉트하며, 세션이 없는 경우 기본적인 화면 유지
     * @param request
     * @return HttpResponse
     */
    @Override
    public HttpResponse doGet(HttpRequest request) {
        String path = request.getRequestPath();
        if (path.equals(HttpRequestMapper.ROOT.getPath())) {
            return HttpResponse.sendRedirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
        }

        String body = readBytesFromFile(request.getRequestPath());
        if(request.getSession() == null){
            return HttpResponse.forward(path, request.getHttpVersion(), body);
        }

        String bodyWithData = body.replace(DynamicHtmlUtil.USER_NAME_TAG, DynamicHtmlUtil.generateUserIdHtml(request.getSession().getUserId())); // 사용자 ID 표시
        bodyWithData = bodyWithData.replace(DynamicHtmlUtil.LOGIN_BUTTON_TAG, DynamicHtmlUtil.LOGIN_BUTTON_INVISIBLE); // 로그인 버튼 비활성화
        bodyWithData = bodyWithData.replace(DynamicHtmlUtil.ARTICLES_TAG, DynamicHtmlUtil.generateArticlesHtml(ArticleHandler.getInstance().findAll())); // 게시글 목록 표시
        return HttpResponse.forward(path, request.getHttpVersion(), bodyWithData);
    }
}
