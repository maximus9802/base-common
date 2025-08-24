/*
 * UserController.java
 */
package com.quyvx.campusio.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @GetMapping("/get")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String getUser() {
    return "12321";
  }
}
