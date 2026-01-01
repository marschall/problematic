package com.github.marschall.problematic.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProblematicConfiguration {

  @Bean
  public ProblemService problemService() {
    return new ProblemService();
  }

  @Bean
  public CrashService crashService(ApplicationContext applicationContext) {
    return new CrashService(applicationContext);
  }

  @Bean
  public CacheHolder cacheHolder() {
    return new CacheHolder();
  }

}
