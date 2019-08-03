package io.flutter.plugins;

import io.flutter.plugin.common.PluginRegistry;
import fleamarket.taobao.com.xservicekit.XserviceKitPlugin;

/**
 * Generated file. Do not edit.
 */
public final class GeneratedPluginRegistrant {
  public static void registerWith(PluginRegistry registry) {
    if (alreadyRegisteredWith(registry)) {
      return;
    }
    XserviceKitPlugin.registerWith(registry.registrarFor("fleamarket.taobao.com.xservicekit.XserviceKitPlugin"));
  }

  private static boolean alreadyRegisteredWith(PluginRegistry registry) {
    final String key = GeneratedPluginRegistrant.class.getCanonicalName();
    if (registry.hasPlugin(key)) {
      return true;
    }
    registry.registrarFor(key);
    return false;
  }
}
