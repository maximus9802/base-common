package com.quyvx.campusio.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ProblemDetail;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorResponse extends ProblemDetail {

  private Map<String, Object> errors;
}
