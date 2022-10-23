package com.shawn.study.shiro.realm;

import com.shawn.study.shiro.entity.User;
import com.shawn.study.shiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

@Component
public class JDBCRealm extends AuthorizingRealm {

  private final UserService userService;

  public JDBCRealm(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    return null;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {
    UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
    User account = userService.getUserByUsername(token.getUsername());
    if (account != null) {
      return new SimpleAuthenticationInfo(account, account.getPassword(), getName());
    }
    return null;
  }
}
