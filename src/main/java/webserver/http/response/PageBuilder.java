package webserver.http.response;

import model.post.Post;
import model.post.PostDAO;
import model.user.User;
import model.user.UserDAO;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;
import webserver.util.HtmlFiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 각 동적 페이지에 대한 builder
 */
public class PageBuilder {
    /**
     * User list page를 생성한다.
     * <p>
     *     이 과정에서 DB의 user list를 요청한다.
     * </p>
     * @return 해당 page에 대한 byte array
     */
    public static byte[] buildUserList() throws IOException {
        UserDAO userDAO = new UserDAO();
        StringBuilder userlist = new StringBuilder();
        for( User user : userDAO.getUserList()){
            userlist.append("<li>User : ").append(user.getName());
            userlist.append("<ul>");
            userlist.append("<li>ID : ").append(user.getUserId()).append("</li>");
            userlist.append("<li>Password : ").append(user.getPassword()).append("</li>");
            userlist.append("<li>Email : ").append(user.getEmail()).append("</li>");
            userlist.append("</ul>");
            userlist.append("</li>\r\n");
        }

        String page = HtmlFiles.readHtmlString(HtmlFiles.USER_LIST);
        page = page.replace("{Userlist}", userlist );

        return page.getBytes("UTF-8");
    }

    /**
     * login 실패 화면을 띄운다
     * @param errorMessage 표현할 에러 메시지
     * @return 해당 page에 대한 byte array
     */
    public static byte[] buildRegistrationFailedPage(String errorMessage) throws IOException {
        String page = HtmlFiles.readHtmlString(HtmlFiles.REGISTER_FAILED);
        page = page.replace("{ErrorMessage}", errorMessage );
        return page.getBytes("UTF-8");
    }

    /**
     * 로그인된 main page
     * @param username 표시할 username
     * @param postid 표시할 post id
     * @return 해당 page에 대한 byte array
     * @see PageBuilder#buildMainBody(String)
     */
    public static byte[] buildLoggedinPage(String username, String postid) throws IOException {
        String page = HtmlFiles.readHtmlString(HtmlFiles.LOGIN_SUCCESS);
        page = page.replace("{USERNAME}", username );

        page = page.replace("{MAIN}", buildMainBody(postid) );

        return page.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 로그아웃된 main page
     * @param postid 표시할 post id
     * @return 해당 page에 대한 byte array
     * @see PageBuilder#buildMainBody(String)
     */
    public static byte[] buildLoggedoutPage(String postid) throws IOException {
        String page = HtmlFiles.readHtmlString(HtmlFiles.MAIN_PAGE);

        page = page.replace("{MAIN}", buildMainBody(postid) );

        return page.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 메인페이지의 main body에 대한 builder
     * <p>
     *     해당 posid를 확인하여 글을 표시한다.
     * <p>
     *     만약 글이 없다면 글이 존재하지 않는다는 글을 반환한다.
     * </p>
     * @param postid 표시할 post id
     * @return body에 대한 문자열
     * @see PageBuilder#buildPostBody(Post)
     * @see PageBuilder#buildNav(int, int)
     */
    public static String buildMainBody(String postid) throws IOException {
        PostDAO postDAO = new PostDAO();
        int id = isNumeric(postid) ? Integer.parseInt(postid) : postDAO.getLastIndex();
        Post post = postDAO.getPost(id);
        if(post == null){
            return "글이 존재하지 않습니다.";
        }
        String postbody = buildPostBody(post);

        int previd = postDAO.getPrevPostIndex(id);
        int nextid = postDAO.getNextPostIndex(id);
        String nav = buildNav(previd,nextid);
        return postbody + nav;
    }

    /**
     * 글에 대한 내용을 작성한다.
     * @param post 표시할 post
     * @return post에 대한 문자열
     * @see PageBuilder#buildMainBody(String)
     */
    public static String buildPostBody(Post post) throws IOException {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(post.getUserid());
        String postbody = HtmlFiles.readHtmlString(HtmlFiles.POST)
                .replace("{POST_ACCOUNT}", user.getName());
        if(post.getImgpath() !=null){
            String path = post.getImgpath().replace("./src/main/resources/static","/resource");
            postbody = postbody.replace("{IMG_FIELD}",
                    "<img class=\"post__img\" src = "+path +" />");
        }
        else postbody = postbody.replace("{IMG_FIELD}","");
        postbody = postbody.replace("{POST_TEXT}", post.getText());
        return postbody;
    }

    /**
     * 메인 페이지 하단의 글 navigator를 생성한다
     * <p>
     *     해당 글에 대한 다음글 및 이전글을 찾아서 버튼에 링크한다.
     * <p>
     *     만약 글이 없다면 해당 화살표를 지운다.
     * </p>
     * @param previd 이전 글의 id
     * @param nextid 다음 글의 id
     * @return 하단 navigator 부분의 html
     */
    public static String buildNav(int previd, int nextid) throws IOException {
        String nav = HtmlFiles.readHtmlString(HtmlFiles.NAV);
        if (previd ==0){
            // 정규 표현식 패턴 정의
            String regex = "<a\\s+class=\"nav__menu__item__btn\"\\s+href=\\s*\\{PREV_POST\\}\\s*>.*?<\\/a>";
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);

            // 정규 표현식을 사용하여 내용을 지우고 클릭 불가능하게 변경
            Matcher matcher = pattern.matcher(nav);
            nav = matcher.replaceAll("<a class=\"nav__menu__item__btn\" href=\"#\" style=\"pointer-events: none;\"></a>");
        } else  nav = nav.replace("{PREV_POST}", "/post/"+ previd);

        if(nextid == 0){
            // 정규 표현식 패턴 정의
            String regex = "<a\\s+class=\"nav__menu__item__btn\"\\s+href=\\s*\\{NEXT_POST\\}\\s*>.*?<\\/a>";
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);

            // 정규 표현식을 사용하여 내용을 지우고 클릭 불가능하게 변경
            Matcher matcher = pattern.matcher(nav);
            nav = matcher.replaceAll("<a class=\"nav__menu__item__btn\" href=\"#\" style=\"pointer-events: none;\"></a>");
        }else nav = nav = nav.replace("{NEXT_POST}", "/post/"+ nextid);

        return nav;
    }

    /**
     * 에러 코드에 대한 에러 페이지 생성기
     * @param errorcode error code enum
     * @return 해당 페이지에 대한 byte array
     */
    public static byte[] buildErrorPage(StatusCode errorcode) {
        String body = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>" + errorcode.getDescription() + "</title>" +
                "</head>" +
                "<body>" +
                "<h1 style=\"text-align: center\">"+ errorcode.getCode() + " " + errorcode.getDescription() + "</h1>" +
                "</body>" +
                "</html>";
       return body.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * write 페이지 builder
     * @param errorMessege 표시할 에러 메세지
     * @return html body byte
     */
    public static byte[] buildeWritePage(String errorMessege) throws IOException {
        String page = HtmlFiles.readHtmlString(HtmlFiles.WRITE);
        if(!errorMessege.isEmpty()){

        }else page = page.replace("{ERROR}", errorMessege);
        return page.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 해당 문자열이 숫자인지 알아보기 위한 method
     * @param str 확인할 문자열
     * @return 숫자인지 여부
     */
    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}