package utils;

import enums.HttpMethod;

import java.util.Objects;

public class ApiPair {

  private String path;
  private HttpMethod method;

  public ApiPair(String path, HttpMethod method) {
    this.path = path;
    this.method = method;
  }

  public String getPath() {
    return path;
  }

  public HttpMethod getMethod() {
    return method;
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj) return true;
    if(obj.getClass() != getClass() || obj == null) return false;
    ApiPair apiPair = (ApiPair) obj;
    return apiPair.getPath().equals(path) && apiPair.getMethod().equals(method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, method);
  }
}
