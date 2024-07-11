package view;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateBlockBuilder {

    public static String build(Map<String, String> attributes, String body, ItemFinder finder) {
        return canBuild(attributes, finder) ? getHydratedBody(body, finder) : "";
    }

    /**
     * 필수 요소가 모두 채워진 body를 받는다. 아이템이 존재하지 않으면 예외가 발생할 수 있다.
     */
    public static String getHydratedBody(String body, ItemFinder finder) {
        StringBuilder builder = new StringBuilder();
        int nextStart = 0;

        Pattern propPattern = Pattern.compile(TemplateRegex.property, Pattern.DOTALL);
        Matcher matcher = propPattern.matcher(body);

        while(matcher.find()) {
            // nextStart ~ end 구간을 새로운 body에 붙인다.
            int end = matcher.start();
            builder.append(body, nextStart, end);
            nextStart = matcher.end();

            String itemName = matcher.group(1);
            String itemString = finder.getItemString(itemName);
            if(itemString != null) builder.append(itemString);
            // 이름을 얻는다
        }

        builder.append(body, nextStart, body.length());
        return builder.toString();
    }

    public static boolean canBuild(Map<String, String> attributes, ItemFinder finder) {
        return checkIfCondition(attributes, finder)
                && checkIfNotCondition(attributes, finder);
    }

    public static boolean checkIfCondition(Map<String, String> attributes, ItemFinder finder) {
        String ifCondition = attributes.get(TemplateBlockAttribute.ifAttr);
        if (ifCondition == null) return true;
        return finder.getItemString(ifCondition) != null;
    }

    public static boolean checkIfNotCondition(Map<String, String> attributes, ItemFinder finder) {
        String ifNotCondition = attributes.get(TemplateBlockAttribute.ifNotAttr);
        if (ifNotCondition == null) return true;
        return finder.getItemString(ifNotCondition) == null;
    }
}
