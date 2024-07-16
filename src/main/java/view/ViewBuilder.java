package view;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewBuilder {

    // original view를 적절한 형태로 변환한다.
    public static String build(String template, Map<String, Object> items) {
        StringBuilder builder = new StringBuilder();
        ItemFinder finder = new ItemFinder(items);

        Pattern blockPattern = Pattern.compile(TemplateRegex.block, Pattern.DOTALL);
        Pattern attrPattern = Pattern.compile(TemplateRegex.attribute, Pattern.DOTALL);

        int nextStart = 0;

        Matcher blockMatcher = blockPattern.matcher(template);
        while(blockMatcher.find()) {
            int end = blockMatcher.start();
            builder.append(template, nextStart, end);
            nextStart = blockMatcher.end();
            System.out.println(blockMatcher.start() + blockMatcher.end());
            // block의 attributes / body부 캡쳐
            String attrString = blockMatcher.group(1);
            String body = blockMatcher.group(3);

            Matcher attrMatcher = attrPattern.matcher(attrString);
            // 속성 목록 파싱
            Map<String, String> attributes = new HashMap<>();
            while(attrMatcher.find()) {
                String attrName = attrMatcher.group(1);
                String attrValue = attrMatcher.group(2).substring(1, attrMatcher.group(2).length() - 1);
                attributes.put(attrName, attrValue);
            }

            // 바디 파싱
            String templateBlockBody = TemplateBlockBuilder.build(attributes, body, finder);
            builder.append(templateBlockBody);
        }

        builder.append(template, nextStart, template.length());
        return builder.toString();
    }
}
