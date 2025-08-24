package com.quyvx.campusio.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class MessageUtils {

  private MessageUtils() {
  }

  private static final String BUNDLE_NAME = "message";
  public static final ConcurrentHashMap<Locale, ResourceBundle> BUNDLES = new ConcurrentHashMap<>();
  private static volatile Locale defaultLocale = Locale.ENGLISH;

  private static ResourceBundle getBundle(Locale locale) {
    return BUNDLES.computeIfAbsent(
        locale,
        loc -> ResourceBundle.getBundle(BUNDLE_NAME, loc, UTF8_CONTROL)
    );
  }

  public static String getMessage(String key, Object... params) {
    return getMessage(key, defaultLocale, params);
  }

  public static String getMessage(String key, Locale locale, Object... params) {
    Objects.requireNonNull(key, "key must not be null");

    if (locale == null) {
      locale = defaultLocale;
    }
    ResourceBundle bundle = getBundle(locale);
    String pattern;
    try {
      pattern = bundle.getString(key);
    } catch (MissingResourceException e) {
      return "!" + key + "!";
    }
    MessageFormat messageFormat = new MessageFormat(pattern, locale);
    return messageFormat.format(params == null ? new Objects[]{} : params);
  }

  public static void setDefaultLocale(Locale locale) {
    if (locale != null) {
      defaultLocale = locale;
    }
  }

  private static final ResourceBundle.Control UTF8_CONTROL = new ResourceBundle.Control() {
    @Override
    public ResourceBundle newBundle(
        String baseName, Locale locale, String format,
        ClassLoader loader, boolean reload)
        throws IllegalAccessException, InstantiationException, IOException {

      String bundleName = toBundleName(baseName, locale);
      String resourceName = toResourceName(bundleName, "properties");

      InputStream stream = reload
          ? reloadStream(loader, resourceName)
          : loader.getResourceAsStream(resourceName);

      if (stream == null) {
        return null;
      }
      try (InputStreamReader reader = new InputStreamReader(stream,
          java.nio.charset.StandardCharsets.UTF_8)) {
        return new PropertyResourceBundle(reader);
      }
    }

    private InputStream reloadStream(ClassLoader loader, String resourceName) throws IOException {
      java.net.URL url = loader.getResource(resourceName);
      if (url == null) {
        return null;
      }
      java.net.URLConnection connection = url.openConnection();
      if (connection == null) {
        return null;
      }
      connection.setUseCaches(false);
      return connection.getInputStream();
    }
  };
}
