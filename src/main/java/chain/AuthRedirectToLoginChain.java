package chain;

import chain.core.MiddlewareChain;
import config.AppConfig;
import http.MyHttpRequest;
import http.MyHttpResponse;

/**
 * 유저가 로그인하지 않았을 때 로그인 페이지로 redirect하는 체인
 */
public class AuthRedirectToLoginChain extends MiddlewareChain {
    String[] authUrls;
    public AuthRedirectToLoginChain(String... urls) {
        authUrls = urls;
    }
    @Override
    public void act(MyHttpRequest req, MyHttpResponse res) {
        // 유저가 로그인 한 상태일 때
        if (req.getStoreData(AppConfig.USER) != null) { // 유저가 있으면 다음으로
            next(req,res);
            return;
        }

        // 로그인하지 않았을 때
        for(String authUrl : authUrls) {
            if (!req.getUrl().getPathname().startsWith(authUrl)) continue;

            res.redirect("/login");
            return;
        }
        next(req, res);
    }
}
