/*
 * LogUtil.java
 */
package com.quyvx.campusio.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {

  private LogUtils() {}

  public static void info(String message) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    StackTraceElement caller = stackTrace[2];

    String className = caller.getClassName();
    String methodName = caller.getMethodName();

    log.info("-----{}.{}: {}", className, methodName, message);
  }

  public static void warning(String message) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    StackTraceElement caller = stackTrace[2];

    String className = caller.getClassName();
    String methodName = caller.getMethodName();

    log.warn("-----{}.{}: {}", className, methodName, message);
  }

  public static void error(String message) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    StackTraceElement caller = stackTrace[2];

    String className = caller.getClassName();
    String methodName = caller.getMethodName();

    log.error("-----{}.{}: {}", className, methodName, message);
  }

  public static void debug(String message) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    StackTraceElement caller = stackTrace[2];

    String className = caller.getClassName();
    String methodName = caller.getMethodName();

    log.debug("-----{}.{}: {}", className, methodName, message);
  }

  public static void trace(String message) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    StackTraceElement caller = stackTrace[2];

    String className = caller.getClassName();
    String methodName = caller.getMethodName();

    log.trace("-----{}.{}: {}", className, methodName, message);
  }

}
