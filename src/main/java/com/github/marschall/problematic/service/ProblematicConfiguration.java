package com.github.marschall.problematic.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProblematicConfiguration {

  @Bean
  public ProblemService problemService() {
    return new ProblemService();
  }

  @Bean
  public CrashService crashService() {
    return new CrashService();
  }

  @Bean
  @Conditional(CrashIsSolved.class)
  public CacheBean cacheBean() {
    return new CacheBean();
  }

}
