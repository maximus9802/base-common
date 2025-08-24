/*
 * HealthCheckController.java
 */
package com.quyvx.campusio.controller;

import com.quyvx.campusio.util.Constants.MessageConstants;
import com.quyvx.campusio.util.LogUtils;
import com.quyvx.campusio.util.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
  @GetMapping("/heathz/check")
  public ResponseEntity<String> health() {
    LogUtils.info(MessageUtils.getMessage(MessageConstants.SUCCESS));
    return ResponseEntity.ok("Health check ok! ");
  }
  @GetMapping("/dapr/config")
  public ResponseEntity<String> dapr() {
    return ResponseEntity.ok("dapr config !");
  }
}
