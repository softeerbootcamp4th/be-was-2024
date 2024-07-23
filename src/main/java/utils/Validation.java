package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Validation {
  public static boolean anyNull(String... checkStr) {
    return Stream.of(checkStr).anyMatch(Validation::isEmpty);
  }

  private static boolean isEmpty(String str) {
    return str == null || str.isEmpty();
  }

  public static boolean isEmail(String email) {
    Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }
}
