# Java FAQ

### 0) Ref

- https://javaguide.cn/java/basis/java-basic-questions-01.html
- https://cloud.tencent.com/developer/article/2183300


### Java memoery model

### a++ VS ++a ?

- a++ : assign a first, then do a += 1
- ++a : do a += 1 first, then assign value

```java
// java
b = a++;
b = ++a;
```

### Java basic data type (基本數據類型) VS reference data type (引用數據類型) **** ?

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/basic_ref_data_type.jpeg" width="500" height="300">

- basic data type : byte, short, int, long, float, dobule, char, boolean ...
- reference data type : Class, interface, Array

- Note :
	- basic data type :
		- "==" : compare attr (value)
		- equals : compare attr (value)

	- reference data type :
		- "==" : compare storage address
		- equals : 
			- NOT override : Still compare storage address (same as "==")
			- override : compare attr (value)
		- all class are child class of Object class

	- Note : some reference class' equals method already overriden (e.g. String), so its equals method already compare attr

	```java
	// java
	public boolean equals(Object obj) {
     return (this == obj);
	}
	```

	```java
	// java

	// new Class : use a new storage in heap (內存)
    String a = new String("123");
    String b = new String("123");

    System.out.println(a==b); // false ( "new" will use new address in internal storage)

	// without new : will fetch from constant pool
    String c = b;

    System.out.println(c==b); // true ( c will fetch from constant pool (常量池))
	```

- https://blog.csdn.net/weixin_45658089/article/details/120248191
- https://javaguide.cn/java/basis/java-basic-questions-02.html#object-%E7%B1%BB%E7%9A%84%E5%B8%B8%E8%A7%81%E6%96%B9%E6%B3%95%E6%9C%89%E5%93%AA%E4%BA%9B


### Java boxing (裝箱) VS unboxing (拆箱) ?

- boxing : basic data type -> reference type
- unboxing : reference type -> basic data type

```java
// java
Integer i = 1; // boxing
int n = i; // unboxing
```

### String constant pool ?

- Avoid repeatedly create same String, for better performance

```java
// java
// 在堆中创建字符串对象”ab“
// 将字符串对象”ab“的引用保存在字符串常量池中
String aa = "ab";
// 直接返回字符串常量池中字符串对象”ab“的引用
String bb = "ab";
System.out.println(aa==bb);// true
```

### 成員變量 (member variable) VS 局部變量 (local variable) ?

- member variable
	- belong to class

- local variable

```java
// java
public class VariableExample {

    // 成员变量
    private String name;
    private int age;

    // 方法中的局部变量
    public void method() {
        int num1 = 10; // 栈中分配的局部变量
        String str = "Hello, world!"; // 栈中分配的局部变量
        System.out.println(num1);
        System.out.println(str);
    }

    // 带参数的方法中的局部变量
    public void method2(int num2) {
        int sum = num2 + 10; // 栈中分配的局部变量
        System.out.println(sum);
    }

    // 构造方法中的局部变量
    public VariableExample(String name, int age) {
        this.name = name; // 对成员变量进行赋值
        this.age = age; // 对成员变量进行赋值
        int num3 = 20; // 栈中分配的局部变量
        String str2 = "Hello, " + this.name + "!"; // 栈中分配的局部变量
        System.out.println(num3);
        System.out.println(str2);
    }
}

```

### String VS StringBuffer VS StringBuilder ?

- String : 
	- unchangeable
	- thread safety, constant

- StringBuilder :
	- parent class : AbstractStringBuilder
	- thread unsafety
	- use case : single thread

- StringBuffer :
	- parent class : AbstractStringBuilder
	- thread safety (lock)
	- use case : multi thread

### Exception VS Error ?

- https://javaguide.cn/java/basis/java-basic-questions-03.html#%E5%BC%82%E5%B8%B8

### Java serialize, deserializes ?

- https://javaguide.cn/java/basis/serialization.html#%E6%80%BB%E7%BB%93

### Java @transient explain ?

- https://www.cnblogs.com/huzi007/p/6600559.htmls

### try-with-resource VS try-catch-final ?

- https://javaguide.cn/java/basis/syntactic-sugar.html#try-with-resource