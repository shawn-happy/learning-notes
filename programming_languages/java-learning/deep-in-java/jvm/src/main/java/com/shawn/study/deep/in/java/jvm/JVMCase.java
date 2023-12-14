package com.shawn.study.deep.in.java.jvm;

public class JVMCase {

  /** 常量 */
  public static final String MAN_GENDER_TYPE = "man";

  /** 静态变量 */
  public static String WOMAN_GENDER_TYPE = "woman";

  public static void main(String[] args) {
    Student student = new Student();
    student.setName("Shawn");
    student.setAge(26);
    student.setGender(MAN_GENDER_TYPE);

    JVMCase jvmCase = new JVMCase();
    print(student);
    jvmCase.say(student);
  }

  public static void print(Student stu) {
    System.out.println(
        "name: " + stu.getName() + "; gender: " + stu.getGender() + "; age: " + stu.getAge());
  }

  public void say(Student stu) {
    System.out.println(stu.getName() + " say Hello ");
  }
}

class Student {

  String name;
  String gender;
  int age;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
