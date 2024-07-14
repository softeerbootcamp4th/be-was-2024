package util;

import constant.FileExtensionType;
import constant.HttpStatus;
import constant.MyTagAttribute;
import constant.MyTagRegex;
import dto.HttpResponse;
import exception.DynamicFileBuildException;
import handler.HandlerManager;
import model.MyTagDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 동적 파일을 생성하는 클래스
public class DynamicFileBuilder {
    private static final Logger logger = LoggerFactory.getLogger(DynamicFileBuilder.class);

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CHARSET_UTF8 = "utf-8";
    private static final String ERROR_MESSAGE_404 =
            "<html>" +
                    "<head><title>404 Not Found</title></head>" +
                    "<body><h1>404 Not Found</h1></body>" +
                    "</html>";

    // 동적 파일을 응답하는 HttpResponse 설정
    public static void setHttpResponse(HttpResponse httpResponse, String filePath, Map<String, List<MyTagDomain>> model) throws UnsupportedEncodingException {
        StringBuilder content = new StringBuilder();

        if(filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }

        try (InputStream inputStream = HandlerManager.class.getClassLoader().getResourceAsStream(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            byte[] responseContent = buildDynamicFile(content.toString(), model);

            // 동적 파일 응답 설정
            httpResponse.setHttpStatus(HttpStatus.OK);
            httpResponse.addHeader(CONTENT_TYPE, FileExtensionType.HTML.getContentType());
            httpResponse.addHeader(CONTENT_TYPE, CHARSET_UTF8);
            httpResponse.addHeader(CONTENT_LENGTH, String.valueOf(responseContent.length));
            httpResponse.setBody(responseContent);

        }
        catch(IOException e){
            logger.error(e.getMessage());

            // 동적 파일 읽기 실패시, 404 에러 응답
            httpResponse.setHttpStatus(HttpStatus.NOT_FOUND);
            httpResponse.addHeader(CONTENT_TYPE, FileExtensionType.HTML.getContentType());
            httpResponse.addHeader(CONTENT_LENGTH, String.valueOf(ERROR_MESSAGE_404.length()));
            httpResponse.setBody(ERROR_MESSAGE_404.getBytes(CHARSET_UTF8));
        }

    }

    // 파일에 있는 custom tag 부분을 찾아서 동적으로 구성하는 메서드
    private static byte[] buildDynamicFile(String content, Map<String, List<MyTagDomain>> model){

        String captureAttributeRegex = MyTagRegex.CAPTURE_ATTRIBUTE.getRegex();
        Pattern captureAttributePattern = Pattern.compile(captureAttributeRegex);
        Matcher captureAttributeMatcher = captureAttributePattern.matcher(content);

        StringBuilder dynamicContent = new StringBuilder();

        // my-tag 및 속성 부분 capture
        while (captureAttributeMatcher.find()) {
            MyTagAttribute attribute = MyTagAttribute.of(captureAttributeMatcher.group(2)); // 속성 이름
            String attributeValue = captureAttributeMatcher.group(3); // 속성 값
            String middlePart = captureAttributeMatcher.group(4); // 태그 사이의 전체 내용

            // my-tag 조건이 유효하지 않으면 my-tag 코드 부분 삭제
            if(!isTagAttributeValid(attribute, attributeValue, model)) {
                captureAttributeMatcher.appendReplacement(dynamicContent, "");
                continue;
            }

            switch(attribute) {
                case IF:
                case IF_NOT:
                    middlePart = BindingMiddlePart(attributeValue, middlePart, model);
                    captureAttributeMatcher.appendReplacement(dynamicContent, middlePart);
                    break;
                case EACH:
                    middlePart = IterableBindingMiddlePart(attributeValue, middlePart, model);
                    captureAttributeMatcher.appendReplacement(dynamicContent, middlePart);
                    break;
            }
        }

        // 나머지 부분들을 builder에 삽입
        captureAttributeMatcher.appendTail(dynamicContent);

        return dynamicContent.toString().getBytes();
    }

    // {} 부분을 model안에 있는 객체의 필드값으로 바인딩
    private static String BindingMiddlePart(String attributeValue, String middlePart, Map<String, List<MyTagDomain>> model){
        String captureValue = MyTagRegex.CAPTURE_VALUE.getRegex();
        Pattern valuePattern = Pattern.compile(captureValue);
        Matcher valueMatcher = valuePattern.matcher(middlePart);

        // {} 부분이 있는 경우
        if(valueMatcher.find()){
            do {
                String bindField = valueMatcher.group(1); // {} 부분
                String className = valueMatcher.group(2); // 클래스 이름
                String indexField = valueMatcher.group(3); // [] 부분
                String fieldName = valueMatcher.group(4); // 필드 이름

                String idxRegex = MyTagRegex.CAPTURE_INDEX.getRegex();
                Pattern idxPattern = Pattern.compile(idxRegex);
                Matcher idxMatcher = idxPattern.matcher(indexField);

                // [인덱스] 부분이 있는 경우
                if(idxMatcher.find()){
                    int idx = Integer.parseInt(idxMatcher.group(1));
                    middlePart = middlePart.replace(bindField,
                                    getFieldValue(model.get(attributeValue).get(idx), className, fieldName).toString()); // {클래스명.변수명} 치환
                }
                else
                    throw new DynamicFileBuildException("index not found");

            }while(valueMatcher.find());
        }

        return middlePart;
    }

    // model안에 있는 객체의 개수만큼 {} 부분을 model안에 있는 객체의 필드값으로 바인딩하고 이어붙이기
    private static String IterableBindingMiddlePart(String attributeValue, String middlePart, Map<String, List<MyTagDomain>> model){
        String captureValue = MyTagRegex.CAPTURE_VALUE.getRegex();
        Pattern valuePattern = Pattern.compile(captureValue);

        StringBuilder iterableMiddlePart = new StringBuilder();

        List<MyTagDomain> objList = model.get(attributeValue);
        for(int i=0; i<objList.size(); i++){
            String copyMiddlePart = middlePart;
            Matcher valueMatcher = valuePattern.matcher(copyMiddlePart);

            while(valueMatcher.find()){
                String bindField = valueMatcher.group(1); // {} 부분
                String className = valueMatcher.group(2); // 클래스 이름
                String fieldName = valueMatcher.group(4); // 필드 이름

                if (fieldName.equals("#"))
                    copyMiddlePart = copyMiddlePart.replace(bindField, String.valueOf(i + 1));
                else copyMiddlePart = copyMiddlePart.replace(bindField,
                        getFieldValue(objList.get(i), className, fieldName));

            }
            iterableMiddlePart.append(copyMiddlePart).append("\n");
        }

        return iterableMiddlePart.toString();
    }


    // 리플렉션을 사용하여 객체의 private field에 접근하여 해당 값을 string으로 반환
    private static String getFieldValue(MyTagDomain obj, String className, String fieldName) {
        if (obj != null) {

            try {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj).toString();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new DynamicFileBuildException("can't get field " + fieldName + " from " + className + "in DynamicFileBuilder");
            }
        }
        throw new DynamicFileBuildException("can't get obj " + " from " + className + "in DynamicFileBuilder");
    }

    // my-tag의 적용 여부를 반환하는 메서드
    private static boolean isTagAttributeValid(MyTagAttribute attribute, String attributeValue,
                                               Map<String, List<MyTagDomain>> model){
        // 속성이 if-not이면 속성 값이 model의 key에 없어야 유효
        if(attribute == MyTagAttribute.IF_NOT)
            return !model.containsKey(attributeValue);
        // 그 외 속성은 속성 값이 model의 key에 있어야 유효
        else
            return model.containsKey(attributeValue);
    }
}
