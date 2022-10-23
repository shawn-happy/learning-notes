package com.shawn.study.shiro.config;

import com.shawn.study.shiro.realm.JDBCRealm;
import java.util.Collections;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

  @Bean
  public DefaultWebSecurityManager defaultWebSecurityManager(JDBCRealm jdbcRealm) {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealms(Collections.singleton(jdbcRealm));
    return securityManager;
  }

  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(
      DefaultWebSecurityManager defaultWebSecurityManager,
      ShiroFilterChainDefinition shiroFilterChainDefinition) {
    ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
    filterFactoryBean.setSecurityManager(defaultWebSecurityManager);
    filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
    filterFactoryBean.setLoginUrl("/user/public/toLoginPage");
    return filterFactoryBean;
  }

  @Bean
  public ShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

    // logged in users with the 'admin' role
    //    chainDefinition.addPathDefinition("/user/public/", "anon");
    chainDefinition.addPathDefinition("/user/private/**", "authc");

    return chainDefinition;
  }
}
