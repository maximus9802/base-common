package com.quyvx.campusio.config.exception;

import com.quyvx.campusio.dto.response.ApiErrorResponse;
import com.quyvx.campusio.util.Constants.ErrorCode;
import com.quyvx.campusio.util.LogUtils;
import com.quyvx.campusio.util.MessageUtils;
import com.quyvx.campusio.util.exception.BusinessException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @Value("${develop-flag}")
  private Boolean developFlag;

  private static final String CODE = "code";
  private static final String MESSAGE = "message";
  private static final String ITEM = "item";
  private static final String DATE_TIME = "dateTime";
  private static final String DETAIL = "detail";
  private static final String ERROR_ID = "id";

  private final MessageSource messageSource;


  @ExceptionHandler({BusinessException.class})
  public ResponseEntity<Object> handleApplicationException(BusinessException ex) {
    String errorCode = ex.getErrorCode();
    String message = ex.getMessage();
    String item = ex.getItem();
    Object[] errorArgs = ex.getErrorArgs() == null ? null : ex.getErrorArgs();
    try {
      message = MessageUtils.getMessage(errorCode, errorArgs);
    } catch (NoSuchMessageException e) {
      LogUtils.warning(String.format("Cannot find message code [%s]", errorCode));
    }

    return new ResponseEntity<>(buildApiErrorResponse(ex, errorCode, message, item),
        new HttpHeaders(), ex.getStatus());
  }

  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
    String errorCode = ErrorCode.UNAUTHORIZED;
    String message = MessageUtils.getMessage(errorCode);

    ApiErrorResponse response = buildApiErrorResponse(ex, errorCode, message, null);
    return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
    String errorCode = ErrorCode.FORBIDDEN;
    String message = MessageUtils.getMessage(errorCode);

    ApiErrorResponse response = buildApiErrorResponse(ex, errorCode, message, null);
    return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  private ApiErrorResponse buildApiErrorResponse(Exception ex, String code, String message,
      String item) {
    Map<String, Object> body = new HashMap<>();
    body.put(ERROR_ID, UUID.randomUUID().toString());
    body.put(CODE, code);
    body.put(MESSAGE, message);
    body.put(ITEM, item);
    body.put(DATE_TIME, System.currentTimeMillis());

    if (developFlag != null && developFlag) {
      body.put(DETAIL, ex.getStackTrace());
    }

    return ApiErrorResponse.builder().errors(body).build();
  }
}
