package regex;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    @Test
    // 현재 테스트는 단순히 regex를 이용하는 부분으로, assertion이 없습니다.
    void testCustomTagRegex() {
        // 입력 문자열
        String input = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <h1>main page</h1>\n" +
                "    <p>hello world this is hello</p>\n" +
                "    <div>\n" +
                "        <h2>user info</h2>\n" +
                "        <         my-template        >  test </my-template>\n" +
                "        <ul>\n" +
                "            <li>username: < my-template name=\"username\" password='test' user = 'tt' >{user.value}</my-template></li>\n" +
                "            <li>password: <    my-template name=\"password\" hello = 'world' > test123 </my-template></li>\n" +
                "            <li>name: <my-template name=\"hello\" /></li>\n" +
                "            <li>age: <my-template name=\"world\"   /></li>\n" +
                "        </ul>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        // 찾을 패턴 문자열
        // < + 0개 이상의 공백 문자 + my-template + [0개 이상의 공백문자 | 1개 이상의 공백문자 + 공백이 아닌 문자들 ] + 0개 이상의 공백문자 + />
        String regex = "<\\s*my-template(\\s*|(\\s+[^\\s/>]+)+)\\s*>(.+)</\\s*my-template\\s*>";
        // 속성 값들은 1번 그룹
        // body는 3번 그룹
        Pattern regexPattern = Pattern.compile(regex);

        // 속성에 대한 패턴 문자열
        String attrRegex = "(\\w+)\\s*=\\s*(\"[^\"]*\"|'[^']*')";
        Pattern attrRegexPattern = Pattern.compile(attrRegex);

        // Pattern 객체 생성
        // Matcher 객체 생성

        Matcher matcher = regexPattern.matcher(input);
        // 매칭되는 패턴을 찾고 시작/끝 인덱스를 출력
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            System.out.println("Found match from " + start + " to " + end);

            String attrString = matcher.group(1);
            String body = matcher.group(3);
            Matcher attrMatcher = attrRegexPattern.matcher(attrString);
            while (attrMatcher.find()) {
                String attrName = attrMatcher.group(1);
                String attrValue = attrMatcher.group(2).substring(1, attrMatcher.group(2).length() - 1);

                System.out.println("Found match from " + attrName + " to " + attrValue);
            }

            System.out.println("body = " + body);

            System.out.println("1 " +matcher.group(1));
            System.out.println("2 " +matcher.group(2));
            System.out.println("3 " +matcher.group(3));
        }
    }
}
