package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {

  private static final Pattern template = Pattern.compile("<my-template\\s*?(mt::(.+?)=\"(.+?)\")?\\s*?>(.*?)</my-template\\s*>", Pattern.DOTALL);

  private static final  Pattern binder = Pattern.compile("\\{\\s*([\\w\\.?]+)\\s*\\}");
  private static final Logger logger = LoggerFactory.getLogger(Template.class);

  private static final String MT_IF = "if";
  private static final String MT_IF_NOT = "if-not";
  private static final String MT_EACH = "each";

  public static byte[] render(byte[] body, Map<String, Object> model) throws IllegalAccessException {
    String bodyStr = new String(body, StandardCharsets.UTF_8);
    Matcher matcher = template.matcher(bodyStr);
    StringBuilder sb = new StringBuilder();
    while(matcher.find()){
      String templateBody = doBinding(matcher.group(4), model);
      if(matcher.group(1) == null) {
        matcher.appendReplacement(sb, bodyStr);
      } else {
        String instruction = matcher.group(2);
        String modelName = matcher.group(3);
        Object target = model.get(modelName);

        if (instruction.equals(MT_IF)) {
          if(target != null) {
            matcher.appendReplacement(sb, templateBody);
          } else {
            matcher.appendReplacement(sb, "");
          }
        } else if(instruction.equals(MT_IF_NOT)) {
          if(target != null) {
            matcher.appendReplacement(sb, "");
          } else {
            matcher.appendReplacement(sb, templateBody);
          }
        } else if(instruction.equals(MT_EACH)){
          if(target instanceof Iterable<?>) {
            Iterable<?> io = (Iterable<?>) target;
            StringBuilder iterSb = new StringBuilder();
            AtomicInteger i = new AtomicInteger(1);
            io.forEach((value) -> {
              iterSb.append(doBindingForIterable(matcher.group(4), value, i.getAndIncrement()));
            });
            matcher.appendReplacement(sb, iterSb.toString());
          } else {
            matcher.appendReplacement(sb, templateBody);
          }
        } else {
          matcher.appendReplacement(sb, "");
        }
      }
    }
    matcher.appendTail(sb);

    return sb.toString().trim().getBytes();
  }

  public static String doBindingForIterable(String body, Object o, int idx) {
    StringBuilder sb = new StringBuilder();
    if(body.isEmpty()) return body;
    Matcher matcher = binder.matcher(body);
    if(o == null) {
      return "";
    }
    while(matcher.find()) {
      String _check = matcher.group(1);

      if(_check.isEmpty()) continue;
      String[] check = _check.split("\\.");

      if(check[0].equals("idx"))  {
        matcher.appendReplacement(sb, Integer.toString(idx));
        continue;
      }

      int i = 0;
      boolean flag = true;
      Object tmpObj = o;
      while(i <= check.length - 1 && flag) {
        Field[] fields = tmpObj.getClass().getDeclaredFields();
        flag = false;
        for(Field field: fields) {
          if(field.getName().equals(check[i])) {
            field.setAccessible(true);
            try {
              tmpObj = field.get(tmpObj);
            } catch (IllegalAccessException e) {
              flag = false;
            }
            flag = true;
          }
        }
        i++;
      }
      matcher.appendReplacement(sb, flag ? tmpObj.toString() : "");
    }
    matcher.appendTail(sb);

    return sb.toString();
  }

  public static String doBinding(String body, Map<String, Object> model) throws IllegalAccessException {
    StringBuilder sb = new StringBuilder();
    if(body.isEmpty()) return body;
    Matcher matcher = binder.matcher(body);
    while(matcher.find()) {
      String _check = matcher.group(1);
      if(_check.isEmpty()) continue;
      String[] check = _check.split("\\.");
      int i = 1;
      if(!model.containsKey(check[0])) {
        matcher.appendReplacement(sb, "");
        continue;
      }
      Object o = model.get(check[0]);
      boolean flag = true;
      while(i <= check.length - 1 && flag) {
        Field[] fields = o.getClass().getDeclaredFields();
        flag = false;
        for(Field field: fields) {
          if(field.getName().equals(check[i])) {
            field.setAccessible(true);
            o = field.get(o);
            flag = true;
          }
        }
        i++;
      }
      matcher.appendReplacement(sb, flag ? o.toString() : "");
    }
    matcher.appendTail(sb);

    return sb.toString();
  }
}
