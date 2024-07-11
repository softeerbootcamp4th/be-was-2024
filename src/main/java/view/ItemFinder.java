package view;

import view.exception.TemplateException;

import java.lang.reflect.Field;
import java.util.Map;

public class ItemFinder {
    private static final String ITEM_NOT_FOUND = "아이템을 찾을 수 없습니다";
    private static final String ITEM_NAME_INVALID = "아이템 이름이 지정되지 않았습니다";
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

        // 첫번째에는 map에서 찾기. 두번째부터는 리플렉션으로 값이 존재하는지 탐색
        String[] itemSegments = itemName.split("\\.", 2);
        if (itemSegments.length == 0) throw new TemplateException(ITEM_NAME_INVALID);

        // 아이템 획특
        Object item = items.get(itemSegments[0]);

        if(itemSegments.length == 2) item = getItem(item, itemSegments[1]);

        return item != null ? item.toString() : null;
    }

    public static Object getItem(Object sourceItem, String itemName) {
        Object item = sourceItem;
        String[] itemSegments = itemName.split("\\.");

        for (String itemSegment : itemSegments) {
            if (item == null) throw new TemplateException(ITEM_NOT_FOUND);
            Class<?> objClass = item.getClass();

            try {
                Field targetField = objClass.getDeclaredField(itemSegment);
                targetField.setAccessible(true);
                item = targetField.get(item);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new TemplateException(ITEM_NOT_FOUND, e);
            }
        }
        return item;
    }
}
