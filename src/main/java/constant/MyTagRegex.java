package constant;

public enum MyTagRegex {
    // my-tag의 속성 및 속성값을 capture하는 regex
    CAPTURE_ATTRIBUTE("<\\s*my-tag\\b(\\s+([\\w-]+)\\s*=\\s*\"([^\"]*)\")\\s*>([\\s\\S]*?)</\\s*my-tag\\s*>"),

    // my-tag 사이에 있는 {클래스.변수명} 형태의 부분을 capture하는 regex
    CAPTURE_VALUE("(\\{\\s*(\\w+)(\\S*)\\.(#?\\w*)\\s*\\})"),

    // {클래스[인덱스].변수명} 형태에서 인덱스 부분을 caputre하는 regex
    CAPTURE_INDEX("\\[(\\d*)\\]");

    final String regex;

    MyTagRegex(String regex){
        this.regex = regex;
    }

    public String getRegex(){
        return regex;
    }
}
