package handler;

import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ViewHandler {
    public static String viewProcess(String path,Map<String,String> params) throws IOException {
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
}
