# 模板模式的原理

`Template Method Design Pattern`

`Define the skeleton of an algorithm in an operation,deferring some steps to subclasses.Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.`

模板方法模式在一个方法中定义一个算法骨架，并将某些步骤推迟到子类中实现，模板方法模式可以让子类在不改变算法整体结构的情况下，重新定义算法中的某些步骤。

算法：指代广义上的业务逻辑，并不是特指数据结构与算法中的算法。

模板：算法骨架

模板方法：包含算法骨架的方法

# 模板模式的应用场景

1. 复用

   `java.io.InputStream#read(byte[], int, int)`

   ```java
   /**
   * @see        java.io.InputStream#read() 模板方法
   */
   public int read(byte b[], int off, int len) throws IOException {
           if (b == null) {
               throw new NullPointerException();
           } else if (off < 0 || len < 0 || len > b.length - off) {
               throw new IndexOutOfBoundsException();
           } else if (len == 0) {
               return 0;
           }
   
           int c = read();
           if (c == -1) {
               return -1;
           }
           b[off] = (byte)c;
   
           int i = 1;
           try {
               for (; i < len ; i++) {
                   c = read();
                   if (c == -1) {
                       break;
                   }
                   b[off + i] = (byte)c;
               }
           } catch (IOException ee) {
           }
           return i;
   }
   ```

   ```java
   // java.io.FileInputStream#read()
   public int read() throws IOException {
           return read0();
       }
   
   private native int read0() throws IOException;
   ```

   `java.util.AbstractList#addAll`

   ```java
   // * {@link #add(int, Object) add(int, E)} is overridden.   
   public boolean addAll(int index, Collection<? extends E> c) {
           rangeCheckForAdd(index);
           boolean modified = false;
           for (E e : c) {
               // template method
               add(index++, e);
               modified = true;
           }
           return modified;
   }
   ```

2. 扩展

   `javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)`

   ```java
       protected void service(HttpServletRequest req, HttpServletResponse resp)
           throws ServletException, IOException {
   
           String method = req.getMethod();
   		// doGet(),doPost()等方法是模板中可以有子类来定制的部分
           if (method.equals(METHOD_GET)) {
               long lastModified = getLastModified(req);
               if (lastModified == -1) {
                   // servlet doesn't support if-modified-since, no reason
                   // to go through further expensive logic
                   doGet(req, resp);
               } else {
                   long ifModifiedSince;
                   try {
                       ifModifiedSince = req.getDateHeader(HEADER_IFMODSINCE);
                   } catch (IllegalArgumentException iae) {
                       // Invalid date header - proceed as if none was set
                       ifModifiedSince = -1;
                   }
                   if (ifModifiedSince < (lastModified / 1000 * 1000)) {
                       // If the servlet mod time is later, call doGet()
                       // Round down to the nearest second for a proper compare
                       // A ifModifiedSince of -1 will always be less
                       maybeSetLastModified(resp, lastModified);
                       doGet(req, resp);
                   } else {
                       resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                   }
               }
   
           } else if (method.equals(METHOD_HEAD)) {
               long lastModified = getLastModified(req);
               maybeSetLastModified(resp, lastModified);
               doHead(req, resp);
           } else if (method.equals(METHOD_POST)) {
               doPost(req, resp);
           } else if (method.equals(METHOD_PUT)) {
               doPut(req, resp);
           } else if (method.equals(METHOD_DELETE)) {
               doDelete(req, resp);
           } else if (method.equals(METHOD_OPTIONS)) {
               doOptions(req,resp);
           } else if (method.equals(METHOD_TRACE)) {
               doTrace(req,resp);
           } else {
               String errMsg = lStrings.getString("http.method_not_implemented");
               Object[] errArgs = new Object[1];
               errArgs[0] = method;
               errMsg = MessageFormat.format(errMsg, errArgs);
   
               resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, errMsg);
           }
       }
   ```

   `test case`

# 回调

相对于普通的函数来说，回调是一种双向调用关系。A类事先注册某个函数F到B类，A类在调用B类的P函数的时候，B反过来在调用A类注册给它的F函数。

```java
public interface ICallback{
    void methodToCallback();
}

public class BClass{
    public void process(ICallback callback){
        callback.methodToCallback();
    }
}

public class AClass{
    public static void main(String[] args){
        BClass b = new BClass();
        b.process(new ICallback(){
            void methodToCallback(){
                System.out.print("callback method");
            }
        });
    }
}
```

# 回调应用场景：

1. Spring xxxTemplate
2. 注册事件监听器
3. shutdownHook()

# 模板模式vs callback

1. 模板模式基于继承，回调基于组合
2. java只支持单继承的语言，基于模板模式编写的子类，已经继承了一个父类，不再具有继承的能力。
3. 回调可以使用匿名类来创建回调对象，可以不用事先定义类；而模板模式针对不同的实现都要定义不同的子类。
4. 如果某个类中定义了多个模板方法，每个方法都有对应的抽象方法，那即便我们只用到其中的一个模板方法，子类也必须实现所有的抽象方法。而回调就更加灵活，我们只需要往用到的模板方法里注入回调对象即可。

