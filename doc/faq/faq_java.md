# Java FAQ

### 0) Ref

- https://javaguide.cn/java/basis/java-basic-questions-01.html
- https://cloud.tencent.com/developer/article/2183300


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

### String、StringBuffer、StringBuilder ?

### Exception VS Error ?

- https://javaguide.cn/java/basis/java-basic-questions-03.html#%E5%BC%82%E5%B8%B8

### Java serialize, deserializes ?

- https://javaguide.cn/java/basis/serialization.html#%E6%80%BB%E7%BB%93

### Java @transient explain ?

- https://www.cnblogs.com/huzi007/p/6600559.htmls

### try-with-resource VS try-catch-final ?

- https://javaguide.cn/java/basis/syntactic-sugar.html#try-with-resource