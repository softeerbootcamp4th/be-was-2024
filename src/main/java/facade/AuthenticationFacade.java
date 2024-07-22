package facade;

import common.ResponseUtils;
import web.HttpRequest;
import web.ViewPath;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 인증 공통 로직을 관리하는 퍼사드
 */
public class AuthenticationFacade {

    /**
     * 인증이 필요한 api에 인증되지 않은 유저가 접근하려 하는지 체크
     * @param request HTTP 요청
     * @param out 출력 스트림
     */
    public static void redirectHomeIfNotAuthenticated(HttpRequest request, OutputStream out) throws IOException {
        if(!SessionFacade.isAuthenticatedRequest(request)) {
            ResponseUtils.redirectToView(ViewPath.DEFAULT).writeInBytes(out);
        }
    }

    /**
     * 인증된 유저가 인증 프로세스에 접근하려 하는지 체크
     * @param request HTTP 요청
     * @param out 출력 스트림
     */
    public static void redirectHomeIfAuthenticated(HttpRequest request, OutputStream out) throws IOException {
        if(SessionFacade.isAuthenticatedRequest(request)) {
            ResponseUtils.redirectToView(ViewPath.DEFAULT).writeInBytes(out);
        }
    }
}
