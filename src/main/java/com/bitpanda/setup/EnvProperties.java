package com.bitpanda.setup;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class EnvProperties {

   private static EnvProperties instance;
   private final Config config;

   private EnvProperties() {
      log.info("Reading config files.");
      final Config envConfig = ConfigFactory.parseResources("application.properties");
      this.config = ConfigFactory.load().withFallback(envConfig).resolve();
   }

   public static Config getConfig() {
      return getInstance().config;
   }

   public static String getProperty(String property) {
      return getConfig().getString(property);
   }

   private static EnvProperties getInstance() {
      if (Objects.isNull(instance)) {
         instance = new EnvProperties();
      }
      return instance;
   }
}
