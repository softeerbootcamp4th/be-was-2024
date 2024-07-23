package controller;

import util.HttpRequest;
import util.HttpResponse;

/**
 * 웹 서버로 들어온 요청을 처리하는 기본 컨트롤러 인터페이스
 */
public interface Controller {
    HttpResponse service(HttpRequest request);
}
