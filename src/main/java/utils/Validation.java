package utils;

import java.util.Objects;
import java.util.stream.Stream;

public class Validation {
  public static boolean anyNull(String... checkStr) {
    return Stream.of(checkStr).anyMatch(Validation::isEmpty);
  }

  private static boolean isEmpty(String str) {
    return str == null || str.isEmpty();
  }
}
