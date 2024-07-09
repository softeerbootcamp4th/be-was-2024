package enums;

public enum HttpResult {
  SUCCESS(200),
  REDIRECT(300),
  CLIENT_ERROR(400),
  SERVER_ERROR(500);

  private int statusCode;

  HttpResult(int statusCode) {
    this.statusCode = statusCode;
  }
}