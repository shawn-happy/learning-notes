package com.shawn.study.deep.in.spring.core.bean.dependency.lookup;

import com.shawn.study.deep.in.spring.core.bean.dependency.lookup.config.ChildConfig;
import com.shawn.study.deep.in.spring.core.bean.dependency.lookup.config.ParentConfig;
import com.shawn.study.deep.in.spring.core.bean.dependency.lookup.domain.Department;
import com.shawn.study.deep.in.spring.core.bean.dependency.lookup.domain.Employee;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** 层次性依赖查找 */
public class HierarchicalDependencyLookupDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext childBeanFactory = new AnnotationConfigApplicationContext();
    childBeanFactory.register(ChildConfig.class);
    childBeanFactory.refresh();

    BeanFactory parentBeanFactory = childBeanFactory.getParentBeanFactory();
    System.out.println("parent BeanFactor is " + parentBeanFactory);

    parentBeanFactory = createBeanFactory();
    ConfigurableListableBeanFactory beanFactory = childBeanFactory.getBeanFactory();
    beanFactory.setParentBeanFactory(parentBeanFactory);

    displayContainsLocalBean(beanFactory, "employee1");
    displayContainsLocalBean((HierarchicalBeanFactory) parentBeanFactory, "employee1");

    displayContainsLocalBean(beanFactory, "department1");
    displayContainsLocalBean((HierarchicalBeanFactory) parentBeanFactory, "department1");

    displayContainsBean(beanFactory, "employee1");
    displayContainsBean((HierarchicalBeanFactory) parentBeanFactory, "employee1");

    displayContainsBean(beanFactory, "department1");
    displayContainsBean((HierarchicalBeanFactory) parentBeanFactory, "department1");

    getBeanByBeanOfType(beanFactory);
    childBeanFactory.close();
  }

  private static void displayContainsBean(HierarchicalBeanFactory beanFactory, String beanName) {
    System.out.printf(
        "当前 BeanFactory[%s] 是否包含 Bean[name : %s] : %s\n",
        beanFactory, beanName, containsBean(beanFactory, beanName));
  }

  private static boolean containsBean(HierarchicalBeanFactory beanFactory, String beanName) {
    BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
    if (parentBeanFactory instanceof HierarchicalBeanFactory) {
      HierarchicalBeanFactory parentHierarchicalBeanFactory =
          (HierarchicalBeanFactory) parentBeanFactory;
      if (containsBean(parentHierarchicalBeanFactory, beanName)) {
        return true;
      }
    }
    return beanFactory.containsLocalBean(beanName);
  }

  private static void displayContainsLocalBean(
      HierarchicalBeanFactory beanFactory, String beanName) {
    System.out.printf(
        "当前 BeanFactory[%s] 是否包含 Local Bean[name : %s] : %s\n",
        beanFactory, beanName, beanFactory.containsLocalBean(beanName));
  }

  private static HierarchicalBeanFactory createBeanFactory() {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(ParentConfig.class);
    applicationContext.refresh();
    return applicationContext.getBeanFactory();
  }

  private static void getBeanByBeanOfType(ConfigurableListableBeanFactory beanFactory) {
    Employee employee = BeanFactoryUtils.beanOfType(beanFactory, Employee.class);
    System.out.println(employee);

    Map<String, Department> departmentMap =
        BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory, Department.class);
    System.out.println(departmentMap);

    String[] names =
        BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, Employee.class);
    System.out.println(
        "BeanFactoryUtils#beanNamesForTypeIncludingAncestors" + Arrays.toString(names));
  }
}
