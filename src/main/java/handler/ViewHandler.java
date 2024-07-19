package handler;

import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 간단한 템플릿 엔진
 */
public class ViewHandler {
    /**
     * param에 담겨있는 값을 html에 넣기
     * @param path
     * @param params
     * @return param값이 적용된 html String
     * @throws IOException
     */
    public static String viewParamProcess(String path, Map<String,String> params) throws IOException {
        Pattern pattern = Pattern.compile("\\$\\{(\\w+)}");
        byte[] bytes = FileUtil.readAllBytesFromFile(new File(path));
        String templateHtml = new String(bytes);
        Matcher matcher = pattern.matcher(templateHtml);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = params.getOrDefault(key, "");
            matcher.appendReplacement(buffer, value);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * html에 list를 넣어준다.
     * @param path
     * @param list
     * @return list가 들어간 html String
     * @throws IOException
     */
    public static String viewListProcess(String path, List<String> list) throws IOException {
        byte[] bytes = FileUtil.readAllBytesFromFile(new File(path));
        String templateHtml = new String(bytes);
        StringBuilder buffer = new StringBuilder();
        for (String x : list) {
            buffer.append("<p class=\"btn btn_contained btn_size_m\">");
            buffer.append(x);
            buffer.append("</p>\r\n");
        }
        return templateHtml.replace("$list", buffer.toString());
    }
}
