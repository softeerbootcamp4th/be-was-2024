package model;

import apiprocess.ApiProcess;
import enums.HttpMethod;

public class ApiInfo {
  private final String apiPath;
  private final HttpMethod httpMethod;
  private final ApiProcess apiProcess;

  public ApiInfo(String apiPath, HttpMethod httpMethod, ApiProcess apiProcess) {
    this.apiPath = apiPath;
    this.httpMethod = httpMethod;
    this.apiProcess = apiProcess;
  }

  public boolean isApiPathNotSame(String apiPath) {
    return !this.apiPath.equals(apiPath);
  }

  public boolean isMethodSame(HttpMethod httpMethod) {
    return this.httpMethod.equals(httpMethod);
  }

  public ApiProcess getApiProcess() {
    return apiProcess;
  }
}
