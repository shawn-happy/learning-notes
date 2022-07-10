# Builder Design Pattern

## 为什么需要建造者模式

我们需要定义一个数据库连接池配置类，ConnectionPoolConfig

```java
public class ConnectionPoolConfig {

	/**
	 * jdbc connection url
	 */
	private String url;

	/**
	 * database username
	 */
	private String username;

	/**
	 * database password
	 */
	private String password;

	/**
	 * database drive class
	 */
	private String driveClassName;

	/**
	 * Maximum number of connections
	 */
	private int maxTotal;

	/**
	 * Maximum number of idle resources
	 */
	private int maxIdle;

	/**
	 * Minimum number of idle resources
	 */
	private int minIdle;

	/**
	 * Default Maximum number of connections
	 */
	private static final int DEFAULT_MAX_TOTAL = 8;

	/**
	 * Default Maximum number of idle resources
	 */
	private static final int DEFAULT_MAX_IDLE = 8;

	/**
	 * Default Minimum number of idle resources
	 */
	private static final int DEFAULT_MIN_IDLE = 0;

	public ConnectionPoolConfig(String url, String username, String password,
		String driveClassName) {
		this(url,username,password,password,DEFAULT_MAX_TOTAL,DEFAULT_MAX_IDLE,DEFAULT_MIN_IDLE);
	}

	public ConnectionPoolConfig(String url, String username, String password,
		String driveClassName, int maxTotal, int maxIdle, int minIdle) {

		isEmpty("url", url);
		this.url = url;

		isEmpty("username", username);
		this.username = username;

		this.password = password;

		isEmpty("driveClassName", driveClassName);
		this.driveClassName = driveClassName;

		if(maxTotal <= 0){
			throw new IllegalArgumentException("maxTotal should be positive!");
		}
		this.maxTotal = maxTotal;
		if(maxIdle < 0){
			throw new IllegalArgumentException("maxIdle should not be negative!");
		}
		this.maxIdle = maxIdle;
		if(minIdle < 0){
			throw new IllegalArgumentException("minIdle should not be negative!");
		}
		this.minIdle = minIdle;
	}

	private void isEmpty(String key,String value){
		if(null == value || value.length() == 0){
			throw new IllegalArgumentException(key + " should not be empty!");
		}
	}
    // 省略getter setter方法。。。
    
}
```

现在，ConnectionPoolConfig有7个可配置项，对应的构造参数，也是7个参数。参数个数还不是很多。但是随着可配置项逐渐增多，如果沿用现在的设计思路，那构造参数列表就会很长，也会有很多getter,setter方法。可读性和易用性就会变差，那在使用构造参数的时候，我们就很容易搞错参数的顺序，当然可以使用set()解决。代码如下：

```java
	public ConnectionPoolConfig(){
		
	}
	public void setUrl(String url) {
		isEmpty("url", url);
		this.url = url;
	}
	public void setUsername(String username) {
		isEmpty("username", username);
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setDriveClassName(String driveClassName) {
		isEmpty("driveClassName", driveClassName);
		this.driveClassName = driveClassName;
	}
	public void setMaxTotal(int maxTotal) {
		if(maxTotal <= 0){
			throw new IllegalArgumentException("maxTotal should be positive!");
		}
		this.maxTotal = maxTotal;
	}
	public void setMaxIdle(int maxIdle) {
		if(maxIdle < 0){
			throw new IllegalArgumentException("maxIdle should not be negative!");
		}
		this.maxIdle = maxIdle;
	}
	public void setMinIdle(int minIdle) {
		if(minIdle < 0){
			throw new IllegalArgumentException("minIdle should not be negative!");
		}
		this.minIdle = minIdle;
	}
```

另外还有三个问题：

1. url,username,password,driveClassName都是必填项，所以我们将它们放入构造器中，但是如果必填项很多，就会造成构造器参数列表过长，如果我们使用set，那又应该如何校验这些必填项是否已经有值了呢？
2. 如果配置项之间有一定的依赖关系，比如，我们设置了maxTotal,maxIdle,minIdle其中一个，就必须显式地设置另外两个。或者配置项之间有一定的约束关系。比如maxIdle和minIdle要小于等于MaxTotal。如果我们使用现在的设计思路，这一部分的逻辑也是无处安放的。
3. 如果我们希望这个config对象是不可变对象，那么我们就不能修改内部属性的值，就不能提供setter方法。

的确，我们可以使用构造器就行了，然后在我们的业务代码里，加上这些逻辑判断，比如必填项是否有值，配置项之间的依赖关系或者约束条件等。但是这样就达不到代码复用的效果了。

我们将使用Builder Design Pattern去解决上述问题。代码如下：

```java
package com.shawn.design.create.builder;

public class ConnectionPoolConfigBuilderDemo {

	/**
	 * jdbc connection url
	 */
	private String url;

	/**
	 * database username
	 */
	private String username;

	/**
	 * database password
	 */
	private String password;

	/**
	 * database drive class
	 */
	private String driveClassName;

	/**
	 * Maximum number of connections
	 */
	private int maxTotal;

	/**
	 * Maximum number of idle resources
	 */
	private int maxIdle;

	/**
	 * Minimum number of idle resources
	 */
	private int minIdle;

	public ConnectionPoolConfigBuilderDemo(Builder builder){
		this.url = builder.url;
		this.username = builder.username;
		this.password = builder.password;
		this.driveClassName = builder.driveClassName;
		this.maxTotal = builder.maxTotal;
		this.maxIdle = builder.maxIdle;
		this.minIdle = builder.minIdle;
	}

	private static void isEmpty(String key,String value){
		if(null == value || value.length() == 0){
			throw new IllegalArgumentException(key + " should not be empty!");
		}
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDriveClassName() {
		return driveClassName;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public static class Builder{

		/**
		 * Default Maximum number of connections
		 */
		private static final int DEFAULT_MAX_TOTAL = 8;

		/**
		 * Default Maximum number of idle resources
		 */
		private static final int DEFAULT_MAX_IDLE = 8;

		/**
		 * Default Minimum number of idle resources
		 */
		private static final int DEFAULT_MIN_IDLE = 0;

		/**
		 * jdbc connection url
		 */
		private String url;

		/**
		 * database username
		 */
		private String username;

		/**
		 * database password
		 */
		private String password;

		/**
		 * database drive class
		 */
		private String driveClassName;

		/**
		 * Maximum number of connections
		 */
		private int maxTotal;

		/**
		 * Maximum number of idle resources
		 */
		private int maxIdle;

		/**
		 * Minimum number of idle resources
		 */
		private int minIdle;

		public ConnectionPoolConfigBuilderDemo build(){
			isEmpty("url",url);
			isEmpty("username",username);
			isEmpty("password",password);
			isEmpty("driveClassName",driveClassName);

			if(maxTotal <= 0){
				throw new IllegalArgumentException("maxTotal should be positive!");
			}
			if(maxIdle < 0){
				throw new IllegalArgumentException("maxIdle should not be negative!");
			}
			if(minIdle < 0){
				throw new IllegalArgumentException("minIdle should not be negative!");
			}

			if(maxIdle > maxTotal){
				throw new IllegalArgumentException("maxTotal should more then or equals maxIdle");
			}

			if(minIdle > maxTotal || minIdle > maxIdle){
				throw new IllegalArgumentException("maxTotal and maxIdle should more then or equals minIdle");
			}
			return new ConnectionPoolConfigBuilderDemo(this);
		}

		public Builder setUrl(String url) {
			isEmpty("url", url);
			this.url = url;
			return this;
		}

		public Builder setUsername(String username) {
			isEmpty("username", username);
			this.username = username;
			return this;
		}

		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder setDriveClassName(String driveClassName) {
			isEmpty("driveClassName", driveClassName);
			this.driveClassName = driveClassName;
			return this;
		}

		public Builder setMaxTotal(int maxTotal) {
			if(maxTotal <= 0){
				throw new IllegalArgumentException("maxTotal should be positive!");
			}
			this.maxTotal = maxTotal;
			return this;
		}

		public Builder setMaxIdle(int maxIdle) {
			if(maxIdle < 0){
				throw new IllegalArgumentException("maxIdle should not be negative!");
			}
			this.maxIdle = maxIdle;
			return this;
		}

		public Builder setMinIdle(int minIdle) {
			if(minIdle < 0){
				throw new IllegalArgumentException("minIdle should not be negative!");
			}
			this.minIdle = minIdle;
			return this;
		}
	}

}

```

建造者模式，还能避免对象存在无效状态。比如长方形类

```java
Rectangle r = new Rectange(); // r is invalid
r.setWidth(2); // r is invalid
r.setHeight(3); // r is valid
```



## 工厂模式和建造者模式的区别

实际上，工厂模式是用来创建不同但是相关类型的对象（继承同一父类或者接口的一组子类），由给定的参数来决定创建哪种类型的对象。建造者模式是用来创建一种类型的复杂对象，通过设置不同的可选参数，“定制化”地创建不同的对象。网上有一个经典的例子很好地解释了两者的区别。顾客走进一家餐馆点餐，我们利用工厂模式，根据用户不同的选择，来制作不同的食物，比如披萨、汉堡、沙拉。对于披萨来说，用户又有各种配料可以定制，比如奶酪、西红柿、起司，我们通过建造者模式根据用户选择的不同配料来制作披萨。



## 总结

1. 必填属性有很多，把这些必填属性都放入构造器中，会出现构造器列表过长的问题，并且在使用的时候容易搞混参数的顺序。如果我们使用set()，那校验这些必填项是否已经填写的逻辑将无处安放。
2. 属性之间有依赖关系或者约束条件。
3. 希望创建不可变对象，也就是不能提供setter方法，不能修改内部的属性值。构造器配合set方法就不适用了。
4. 避免无效的状态。