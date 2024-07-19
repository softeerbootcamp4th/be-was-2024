package view;

import view.exception.TemplateException;
import view.exception.TemplateExceptionMessage;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 객체가 가진 프로퍼티를 찾아주는 클래스
 */
public class ItemFinder {
       private final Map<String, Object> items;

    public ItemFinder(Map<String, Object> items) {
        this.items = items;
    }

    /**
     * 아이템 이름을 기반으로 맵 안에 있는 아이템을 반환한다.
     * @param itemName 찾고 있는 아이템의 이름
     * @return 찾고 있는 아이템
     */
    public String getItemString(String itemName) {
        Object item = getItem(itemName);

        return item != null ? item.toString() : null;
    }

    public Object getItem(String itemName) {
        return getItem(this.items, itemName);
    }

    /**
     * 객체로부터 이름에 지정된 값을 얻는 메서드.
     * @param sourceItem 대상 아이템을 탐색할 루트 아이템
     * @param itemName 찾고 싶은 아이템의 이름
     * @return 찾는 대상 아이템
     */
    public static Object getItem(Object sourceItem, String itemName) {
        Object item = sourceItem;
        String[] itemSegments = itemName.split("\\.");

        for (String itemSegment : itemSegments) {
            // 찾지 못했는데 null을 던지면, 대상 필드가 없는 것인지 / 아니면 대상 필드는 있는데 null 값인지 구분 어려움
            // 만약 나중에 이로 인한 불편함이 생기면 수정 예정
            if (item == null) throw new TemplateException(TemplateExceptionMessage.ITEM_NOT_FOUND);

            if (item instanceof Map<?,?>) item = getNextItemFromMap(item, itemSegment);
            else item = getNextItemFromObject(item, itemSegment);
        }
        return item;
    }

    public static String getItemString(Object sourceItem, String itemName) {
        Object item = getItem(sourceItem, itemName);

        return item != null ? item.toString() : null;
    }

    private static Object getNextItemFromMap(Object sourceItem, String itemSegment) {
        try {
            return ((Map<?,?>) sourceItem).get(itemSegment);
        } catch(Exception e) {
            throw new TemplateException(TemplateExceptionMessage.ITEM_NOT_FOUND, e);
        }
    }

    private static Object getNextItemFromObject(Object sourceItem, String itemSegment) {
        Class<?> objClass = sourceItem.getClass();

        try {
            Field targetField = objClass.getDeclaredField(itemSegment);
            targetField.setAccessible(true);
            return targetField.get(sourceItem);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new TemplateException(TemplateExceptionMessage.ITEM_NOT_FOUND, e);
        }
    }
}
