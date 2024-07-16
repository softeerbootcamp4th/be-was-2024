package view;

import view.exception.TemplateException;
import view.exception.TemplateExceptionMessage;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateBlockBuilder {

    public static String build(Map<String, String> attributes, String body, ItemFinder finder) {
        if (!canBuild(attributes, finder)) return "";
        if(checkForEachCondition(attributes, finder)) {
            String forEachAttr = attributes.get(TemplateBlockAttribute.foreachAttr);
            return getForeachHydratedBody(body, forEachAttr, finder);
        }
        return getHydratedBody(body, finder);
    }

    /**
     * 필수 요소가 모두 채워진 body를 받는다. 아이템이 존재하지 않으면 예외가 발생할 수 있다.
     */
    public static String getHydratedBody(String body, ItemFinder finder) {
        StringBuilder builder = new StringBuilder();

        Pattern propPattern = Pattern.compile(TemplateRegex.property, Pattern.DOTALL);
        Matcher matcher = propPattern.matcher(body);

        int nextStart = 0;
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

    /**
     * 필수 요소가 모두 채워진 body를 받는다. 아이템이 존재하지 않으면 예외가 발생할 수 있다.
     */
    public static String getForeachHydratedBody(String body, String forEachAttr, ItemFinder finder) {
        StringBuilder builder = new StringBuilder();
        String[] itemAndArr =  forEachAttr.split(":");
        if(itemAndArr.length != 2) throw new TemplateException(TemplateExceptionMessage.TEMPLATE_ATTR_INVALID);

        // 템플릿 부모 - 자식 이름 얻기
        String forEachItemName = itemAndArr[0].strip();
        String forEachItemsName = itemAndArr[1].strip();

        Pattern propPattern = Pattern.compile(TemplateRegex.property, Pattern.DOTALL);

        // 리스트로 변경
        List<?> forEachList;
        try {
            Object forEachItem = finder.getItem(forEachItemsName);
            forEachList = (List<?>) forEachItem;
        } catch(Exception e) {
            throw new TemplateException(TemplateExceptionMessage.ITEM_NOT_FOUND);
        }

        for(Object eachItem : forEachList) {
            Matcher matcher = propPattern.matcher(body);
            int nextStart = 0;
            while(matcher.find()) {
                // nextStart ~ end 구간을 새로운 body에 붙인다.
                int end = matcher.start();
                builder.append(body, nextStart, end);
                nextStart = matcher.end();

                String itemName = matcher.group(1);

                String itemString = null;
                if(itemName.startsWith(forEachItemName) && itemName.contains(".")) {
                    // foreach 아이템인 경우 + 해당 아이템의 하위 객체를 사용하는 경우
                    String childItemName = itemName.substring(forEachItemName.length() + 1);
                    itemString = ItemFinder.getItemString(eachItem, childItemName);
                } else {
                    // 아닌 경우
                    itemString = finder.getItemString(itemName);
                }

                if(itemString != null) builder.append(itemString);
                // 이름을 얻는다
            }

            builder.append(body, nextStart, body.length());
        }

        return builder.toString();
    }

    public static boolean canBuild(Map<String, String> attributes, ItemFinder finder) {
        return checkIfCondition(attributes, finder)
                && checkIfNotCondition(attributes, finder);
    }

    /**
     * if 조건에 따라 템플릿을 "렌더링 하는가?"
     */
    public static boolean checkIfCondition(Map<String, String> attributes, ItemFinder finder) {
        String ifCondition = attributes.get(TemplateBlockAttribute.ifAttr);
        if (ifCondition == null) return true; // 조건 자체가 지정되어 있지 않으면 렌더링
        return finder.getItemString(ifCondition) != null; // 아이템이 있으면 렌더링
    }

    /**
     * if-not 조건에 따라 템플릿을 "렌더링 하는가?" = if-not이 true면 렌더링하지 않으므로 false
     */
    public static boolean checkIfNotCondition(Map<String, String> attributes, ItemFinder finder) {
        String ifNotCondition = attributes.get(TemplateBlockAttribute.ifNotAttr);
        if (ifNotCondition == null) return true; // 조건 자체가 지정되어 있지 않으면 반드시 렌더링
        return finder.getItemString(ifNotCondition) == null; // 아이템이 없어야 렌더링
    }

    /**
     *
     * @return
     */
    public static boolean checkForEachCondition(Map<String, String> attributes, ItemFinder finder) {
        String foreachAttr = attributes.get(TemplateBlockAttribute.foreachAttr);
        return foreachAttr != null;
    }
}
