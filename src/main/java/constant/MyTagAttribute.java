package constant;


/**
 * my-tag의 속성 Enum
 */
public enum MyTagAttribute {
    IF("if"), // 속성값이 model에 저장되어 있는 경우 유효
    IF_NOT("if-not"), // 속성값이 model에 저장되어 있지 않아야 유효
    EACH("each"); // 속성값이 model에 저장되어 있는 경우, 각 객체마다 바인딩된 코드를 생성

    final String attributeName;

    MyTagAttribute(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public static MyTagAttribute of(String attributeName) {
        for (MyTagAttribute myTagAttribute : MyTagAttribute.values()) {
            if (myTagAttribute.getAttributeName().equals(attributeName)) {
                return myTagAttribute;
            }
        }
        throw new IllegalArgumentException("MyTagAttribute not found: " + attributeName);
    }
}
