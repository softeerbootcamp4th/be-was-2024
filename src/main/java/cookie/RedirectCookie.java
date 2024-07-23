package cookie;

/**
 * 핸들러에서 작업 성공 시, redirect 해야 하는 url을 설정하는 쿠키
 */
public class RedirectCookie implements Cookie{
    private final String redirectUrl;
    private final String domain;
    private final String path;
    private String maxAge;
    private final boolean secure;
    private final boolean httpOnly;

    public RedirectCookie(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        domain = "localhost";
        path = "/";
        secure = false;
        httpOnly = false;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = String.valueOf(maxAge);
    }

    /**
     * 쿠키의 값과 속성을 string으로 반환하여 OutputStream에 바로 저장할 수 있도록 한다.
     *
     * @return : 쿠키의 값과 속성을 string으로 반환
     */
    public String getCookieString(){
        StringBuilder sb = new StringBuilder();

        sb.append(headerName + ": ")
                .append(CookieAttribute.REDIRECT.getAttributeName()).append("=").append(redirectUrl).append("; ")
                .append(CookieAttribute.PATH.getAttributeName()).append("=").append(path).append("; ")
                .append(CookieAttribute.DOMAIN.getAttributeName()).append("=").append(domain).append("; ");

        if(maxAge!=null)
            sb.append(CookieAttribute.MAX_AGE.getAttributeName()).append("=").append(maxAge).append("; ");

        if(secure)
            sb.append(CookieAttribute.SECURE.getAttributeName()).append("; ");

        if(httpOnly)
            sb.append(CookieAttribute.HTTPONLY.getAttributeName()).append("; ");

        sb.append(CRLF);

        return sb.toString();
    }
}
