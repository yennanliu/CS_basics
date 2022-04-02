# JVM FAQ

## Basics
- https://dunwu.github.io/javacore/java-interview.html#%E5%9F%BA%E7%A1%80

### 1) JVN internal storage ?

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/jvm_storage_1.jpeg">
<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/jvm_storage_2.jpeg">

- JVM internal storage
	- part 1) Thread local
		- program counter :
			- (no OutOfMemoryError), every thread has its own counter
		- VM stack (`thread stack`)
			- serves for java method
			- will create a `stack frame` when every method run.
			- stack frame storges : `local var, op stack, Dynamic Linking, method returned val, Dispatch Exception ...`
			- each method from `called -> completed` mapping the process : `push-to-stack -> pop-from-stack` (no matter method runs success or not)
			- stack frame : storage intermedia/result information. 
		- native method stack
			- serves for native method
	- part 2) Thread shared
		- java heap
		- method area
	- part 3) Direct memory (not managed by JVM GC)
- Ref
	- https://copyfuture.com/blogs-details/20210918043412580m
	- https://cloud.tencent.com/developer/article/1648836

### 2) Difference between JVM heap, stack ?
- JVM heap
	- storage class
	- shared by all threads in JVM
- JVM stack
	- storage stack frame, local var
	- smaller than heap in most cases 
	- NOT shared by different threads. used by local thread only

### 2) JVM GC ? stragegy ? algorithm ?

### 3) how to get memory in java program, heap usage percentage (%) ?
- `java.lang.Runtime`    : get remaining memory, all memory, max heap memory
- `Runtime.freeMemory()` : get remain memory in binary
- `Runtime.totalMemory()`: get total memory in binary
- `Runtime.maxMemory() ` : get max memory in binary
 
### 3) Explain classLoader ?
- Implemented by `ClassLoader` class (and its sub class)
- Load `.class` files to JVM
- Tyeps
	- java class classLoader 
		- Bootstrap class loader
			- implemented by c/c++. we CAN'T access them (but they do exist!).
			- load essential/core java classes under `JAVA_HOME/jre/lib` (jre path) (defined by `sun.boot.class.path`)
			- run after JVM launch
		- ExtClassLoader
			- we can access them (but seldom do that)
			- load jar class under `JAVA_HOME/lib/ext` (defined by `java.ext.dirs`)
			- run after Bootstrap class loader
		- AppClassLoader
			- load classes in application, e.g. test class, 3rd party class..
			- load jar under `Classpath` (defined by `java.class.path`)
			- run after ExtClassLoader
		- User defined classLoader
- Steps:
	- Bootstrap class loader -> ExtClassLoader -> AppClassLoader (loader step)
- Steps (inside classLoader):
	- load -> connect -> class init
	- Explain:
		- load:
			- load `.class` file to memory (create a binary array read .class), and create the corresponding class instance
		- connect
			- validate, prepare, and extract/load are included. 
			- validate : check if loaded class will "harm" JVM
			- prepare : give default init val to static val.
			- extract : modify "sign reference" to "direct reference"
		- class init:
			- if there is parent class which is not init yet -> init this parent class first
			- init "init code" in class in order
- Ref
	- https://juejin.cn/post/6844904005580111879
	- https://blog.csdn.net/briblue/article/details/54973413
	- https://kknews.cc/zh-tw/code/8zvokbq.html
	- https://openhome.cc/Gossip/JavaGossip-V2/IntroduceClassLoader.htm#:~:text=Bootstrap%20Loader%E6%98%AF%E7%94%B1C,lib%2Fext%20%E7%9B%AE%E9%8C%84%E4%B8%8B%E7%9A%84

### 4) does `memory leakage` happen in java ? how ?
- Yes, it may happen in users self defined data structure
- example ?
- how ?
- Ref
	- https://cloud.tencent.com/developer/article/1648836

### 5) Difference between Serial and Parallel GC  ? 

### 2) Thread, progress, program ?
- https://oldmo860617.medium.com/%E9%80%B2%E7%A8%8B-%E7%B7%9A%E7%A8%8B-%E5%8D%94%E7%A8%8B-%E5%82%BB%E5%82%BB%E5%88%86%E5%BE%97%E6%B8%85%E6%A5%9A-a09b95bd68dd

## Ref
- https://dunwu.github.io/javacore/#%F0%9F%93%96-%E5%86%85%E5%AE%B9
- https://cloud.tencent.com/developer/article/1648836
- https://iter01.com/565257.html
- https://www.51cto.com/article/670305.html