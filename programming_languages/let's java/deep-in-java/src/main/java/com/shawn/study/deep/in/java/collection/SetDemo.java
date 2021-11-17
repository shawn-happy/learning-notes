package com.shawn.study.deep.in.java.collection;

import java.util.*;

/** Set Demo HashSet内部是HashMap TreeSet内部是TreeMap LinkedHashSet继承了HashSet */
public class SetDemo {

  public static void main(String[] args) {
    testDisallowDuplicateElement();
    testUnOrderElement();
    testOrderElement();
    testTreeSet();
    testLinkedHashSet();
    testUnmodifiableSet();
    testAddNullElement();
    testSortedSet();
  }

  private static void testSortedSet() {
    SortedSet<Person> personSet = new TreeSet<>();
    personSet.add(new Person(1, "Shawn", "Shang Hai", 26));
    personSet.add(new Person(2, "Jack", "Bei Jing", 28));
    personSet.add(new Person(3, "John", "Tian Jing", 24));
    personSet.add(new Person(4, "Jackson", "Nan Jing", 26));
    System.out.println(personSet.size()); // 3
    for (Person person : personSet) {
      System.out.println(person); // John, Shawn, Jack
    }
  }

  private static void testAddNullElement() {
    Set<String> set = new TreeSet<>();
    try {
      set.add(null);
    } catch (NullPointerException e) {
      System.out.println("TreeSet disallow add null element!");
    }
  }

  private static void testUnmodifiableSet() {
    Set<String> set = new HashSet<>();
    set.add("java");
    set.add("mysql");
    set.add("spring");
    Set<String> unmodifiableSet = Collections.unmodifiableSet(set);
    try {
      unmodifiableSet.add("kafka");
    } catch (Exception e) {
      System.out.println("unmodifiable set!");
    }

    Set<String> setOf = Set.of("java", "docker", "python");
    try {
      setOf.add("kafka");
    } catch (Exception e) {
      System.out.println("unmodifiable set!");
    }
  }

  private static void testLinkedHashSet() {
    Set<String> set = new LinkedHashSet<>();
    set.add("java");
    set.add("spring");
    set.add("mysql");
    set.add("kafka");
    set.forEach(System.out::println);
  }

  private static void testTreeSet() {
    Set<String> set = new TreeSet<>();
    set.add("java");
    set.add("spring");
    set.add("mysql");
    set.add("kafka");
    set.forEach(System.out::println);
  }

  /** 如果是数字，或者是字母的场景是有序的。 */
  private static void testOrderElement() {
    System.out.println("数字场景：");
    Set<String> set = new HashSet<>();
    set.add("1");
    set.add("2");
    set.add("3");
    set.add("4");
    set.forEach(System.out::println);
    System.out.println("字母场景：");
    set.clear();
    set.add("a");
    set.add("b");
    set.add("c");
    set.add("d");
    set.forEach(System.out::println);
  }

  private static void testUnOrderElement() {
    System.out.println("HashSet 无序场景：");
    Set<String> set = new HashSet<>();
    set.add("java");
    set.add("spring");
    set.add("spring ioc");
    set.add("spring aop");
    set.forEach(System.out::println);
  }

  private static void testDisallowDuplicateElement() {
    System.out.println("Set不允许添加重复元素的场景：");
    Set<String> set = new HashSet<>();
    set.add("hello");
    set.add("java");
    set.add("java");
    System.out.println(set.size());
    for (String s : set) {
      System.out.print(s + " ");
    }
    System.out.println();
  }
}

class Person implements Comparable<Person> {

  private int id;
  private String name;
  private String address;
  private int age;

  public Person(int id, String name, String address, int age) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.age = age;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "Person{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", address='"
        + address
        + '\''
        + ", age="
        + age
        + '}';
  }

  @Override
  public int compareTo(Person person) {
    if (person == null) {
      return -1;
    }
    return this.age - person.age;
  }
}
