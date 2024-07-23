package util;

import constant.ErrorMessage;
import constant.FileExtensionType;
import constant.HttpResponseAttribute;
import constant.HttpStatus;
import dto.HttpResponse;
import exception.*;

import java.io.IOException;

/**
 * 예외 응답을 생성하는 클래스
 */
public class ErrorResponseBuilder {

    /**
     * 예외 종류에 따라 예외 응답 데이터를 HttpResponse 객체에 저장한다.
     *
     * @param e : HttpRequest 처러 과정에서 발생한 예외
     * @param response : 응답 데이터를 저장하는 HttpResponse 객체
     */
    public static void buildErrorResponse(Exception e, HttpResponse response) {

        if (e instanceof IOException || e instanceof DatabaseException || e instanceof DynamicFileBuildException) {

            build500ErrorResponse(response);

        } else if (e instanceof IllegalArgumentException || e instanceof InvalidHttpRequestException
                || e instanceof ResourceNotFoundException) {

            build404ErrorResponse(response);

        } else if (e instanceof MethodNotAllowedException) {

            build405ErrorResponse(response);
        }
    }

    /**
     * HttpStatus에 따라 예외 응답 데이터를 HttpResponse 객체에 저장한다.
     *
     * @param status : 발생한 예외의 HttpStatus
     * @param response : 응답 데이터를 저장하는 HttpResponse 객체
     */
    public static void buildErrorResponse(HttpStatus status, HttpResponse response) {

        switch(status){
            case NOT_FOUND -> build404ErrorResponse(response);
            case METHOD_NOT_ALLOWED -> build405ErrorResponse(response);
            case INTERNAL_SERVER_ERROR -> build500ErrorResponse(response);
        }
    }

    private static void build500ErrorResponse(HttpResponse httpResponse){
        httpResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        httpResponse.addHeader(HttpResponseAttribute.CONTENT_TYPE.getValue(), FileExtensionType.HTML.getContentType());
        httpResponse.addHeader(HttpResponseAttribute.CONTENT_LENGTH.getValue(),
                String.valueOf(ErrorMessage.ERROR_MESSAGE_500.getErrorMessage().length()));
        httpResponse.setBody(ErrorMessage.ERROR_MESSAGE_500.getErrorMessage().getBytes());
    }

    private static void build404ErrorResponse(HttpResponse httpResponse){
        httpResponse.setHttpStatus(HttpStatus.NOT_FOUND);
        httpResponse.addHeader(HttpResponseAttribute.CONTENT_TYPE.getValue(), FileExtensionType.HTML.getContentType());
        httpResponse.addHeader(HttpResponseAttribute.CONTENT_LENGTH.getValue(),
                String.valueOf(ErrorMessage.ERROR_MESSAGE_404.getErrorMessage().length()));
        httpResponse.setBody(ErrorMessage.ERROR_MESSAGE_404.getErrorMessage().getBytes());
    }

    private static void build405ErrorResponse(HttpResponse httpResponse){
        httpResponse.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
        httpResponse.addHeader(HttpResponseAttribute.CONTENT_TYPE.getValue(), FileExtensionType.HTML.getContentType());
        httpResponse.addHeader(HttpResponseAttribute.CONTENT_LENGTH.getValue(),
                String.valueOf(ErrorMessage.ERROR_MESSAGE_405.getErrorMessage().length()));
        httpResponse.setBody(ErrorMessage.ERROR_MESSAGE_405.getErrorMessage().getBytes());
    }

}

