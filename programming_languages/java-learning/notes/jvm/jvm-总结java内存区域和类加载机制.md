---
typora-root-url: image
---

```java
public class JVMCase {

   public static final String MAN_GENDER_TYPE = "man";

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

   public static void print(Student stu){
      System.out.println("name: " + stu.getName() + "; gender: " + stu.getGender() +
         "; age: " + stu.getAge());
   }

   public void say(Student stu){
      System.out.println(stu.getName() + " say Hello ");
   }
}

class Student{

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
```

1. jvm通过配置参数或者默认的参数配置，向操作系统申请内存空间，并且记录下内存的起始地址和终止地址。

2. jvm根据参数配置分配堆，栈，方法区的内存大小。

3. 类加载，class文件经过加载，验证，准备，解析，初始化等。其中准备阶段会为类的静态变量分配内存，并且初始化为系统的初始值。

   ![jvm-运行过程-链接阶段](/jvm-运行过程-链接阶段.png)

4. 初始化阶段。执行`<clinit>`方法。编译器会在`.java`文件被编译成`.class`文件时，收集所有类的初始化代码，包括静态变量赋值语句，静态代码块，静态方法，收集在一起执行`<clinit>()`方法。

   ![jvm-运行过程-初始化阶段](/jvm-运行过程-初始化阶段.png)

5. 使用阶段：启动main线程，执行main方法，开始执行第一行代码`Student student = new Student()`。此时堆中会创建一个`student`对象实例，对象引用`student`存放在栈中。

   ![jvm-运行过程-使用阶段-1](/jvm-运行过程-使用阶段-1.png)

6. 继续往下执行，创建`JVMCase`对象，调用`say()`实例方法，入栈，并通过栈中`student`引用调用堆中`Student`对象；之后，调用静态方法`print`，属于`JVMCase`类，入栈，并通过栈中`student`引用调用堆中`Student`对象；

   ![jvm-运行过程-使用阶段-2](/jvm-运行过程-使用阶段-2.png)

