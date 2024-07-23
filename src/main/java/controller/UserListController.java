package controller;

import handler.UserHandler;
import model.User;
import util.*;

import java.util.List;

/**
 * 유저 리스트 요청 처리
 */
public class UserListController extends AbstractController{

    /**
     * 유저 리스트 화면을 불러옴
     * @param request
     * @return HttpResponse
     */
    @Override
    public HttpResponse doGet(HttpRequest request) {
        if(request.getSession() == null){
            return HttpResponse.sendRedirect(HttpRequestMapper.LOGIN.getPath(), request.getHttpVersion());
        }

        String pathWithHtml = request.getRequestPath() + ConstantUtil.DOT_HTML;
        String body = IOUtil.readBytesFromFile(pathWithHtml);
        List<User> users = UserHandler.getInstance().findAll();
        String bodyWithUserList = body.replace(DynamicHtmlUtil.USER_LIST_TAG, DynamicHtmlUtil.generateUserListHtml(users));
        return HttpResponse.forward(pathWithHtml, request.getHttpVersion(), bodyWithUserList);
    }
}
