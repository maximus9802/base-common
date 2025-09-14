package com.quyvx.campusio.util.exception;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@Builder
@ToString
public class BusinessException extends RuntimeException {

  private final HttpStatus status;
  private final String errorCode;
  private final String item;
  private final transient Object[] errorArgs;
}
