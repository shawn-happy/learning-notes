package com.shawn.study.deep.in.java.jmx;

import java.lang.management.ManagementFactory;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;

public class UserMBeanDemo {

  public static void main(String[] args) throws Exception {
    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    ObjectName objectName = new ObjectName("com.shawn.study.deep.in.java.jmx:type=user");
    User user = new User();
    UserManager userMBean = createUserMBean(user);
    mBeanServer.registerMBean(userMBean, objectName);

    StandardMBean standardMBean = new StandardMBean(userMBean, UserManagerMBean.class);

    MBeanInfo mBeanInfo = standardMBean.getMBeanInfo();

    System.out.println(mBeanInfo);

    while (true) {
      Thread.sleep(2000);
      System.out.println(user);
    }
  }

  private static UserManager createUserMBean(User user) throws Exception {
    return new UserManager(user);
  }
}
