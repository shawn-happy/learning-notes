package com.shawn.study.deep.in.spring.core.i18n;

import java.util.Locale;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Spring Boot 场景下自定义 {@link MessageSource} Bean * @see MessageSource
 *
 * @see MessageSourceAutoConfiguration
 * @see ReloadableResourceBundleMessageSource
 * @since
 */
@EnableAutoConfiguration
public class CustomizedMessageSourceBeanDemo {
  /**
   * 在 Spring Boot 场景中，Primary Configuration Sources(Classes) 高于 *AutoConfiguration
   *
   * @return
   */
  @Bean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("messages");
    return messageSource;
  }

  public static void main(String[] args) {

    ConfigurableApplicationContext applicationContext =
        // Primary Configuration Class
        new SpringApplicationBuilder(CustomizedMessageSourceBeanDemo.class)
            .web(WebApplicationType.NONE)
            .run(args);

    ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

    if (beanFactory.containsBean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)) {
      // 查找 MessageSource 的 BeanDefinition
      System.out.println(
          beanFactory.getBeanDefinition(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME));
      // 查找 MessageSource Bean
      MessageSource messageSource =
          applicationContext.getBean(
              AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME, MessageSource.class);
      System.out.println(messageSource);
      String message =
          messageSource.getMessage(
              "error.message", new Object[] {"Integer"}, Locale.SIMPLIFIED_CHINESE);
      System.out.println(message);
    }

    // 关闭应用上下文
    applicationContext.close();
  }
}
