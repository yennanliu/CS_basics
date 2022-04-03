# JVM FAQ

### 1) JVN internal storage ?

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/jvm_storage_1.jpeg">
<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/jvm_storage_2.jpeg">

- JVM internal storage
	- `part 1) Thread local`
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
	- `part 2) Thread shared`
		- java heap
		- method area (Non-heap memory)
	- `part 3) Direct memory (not managed by JVM GC)`
- Ref
	- https://copyfuture.com/blogs-details/20210918043412580m
	- https://cloud.tencent.com/developer/article/1648836

### 2) Difference between JVM heap, stack ?
- JVM `heap`
	- storage class
	- shared by all threads in JVM
- JVM `stack`
	- storage stack frame, local var
	- smaller than heap in general
	- NOT shared by different threads. used by local thread only
- Ref
	- https://github.com/yennanliu/JavaHelloWorld

### 3) Explain JVM GC ? GC stragegy ? algorithm ?
- 5W1H
	- Where ? 
		- JVM heap
	- Why ? 
		- prevent `memory leakage`. in order to use memory efficiently
	- What ? 
		- GC "recycle" obj which are will not be used anymore 
	- When ?
		- Reference counting : remove when "reference count = 0". But NOT working when "cyclic reference"
		- Tracing : tranverse ("dependence tree") from GC root, if not in visited list, means not used, then remove them
		- Escape analysis
	- HOW ? (GC remove algorithm)
		- `Mark-Sweep`
			- mark the to-clean area
			- pros:
				- easy understand, implement
			- cons:
				- low efficiency
				- will cause "space fragments" -> hard to maintain the "continuous storage space"

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic//mark_sweep.png">
		- `Mark-Compact`
			- mark the to-clean area, merge/move them altogether, then clean
			- pros:
				- can keep "continuous storage space"
			- cons:
				- spend extra time/resource on "merge/move" op

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic//mark_compact.png">
		- `Mark-Copy`
			- split memory space to 50%, 50%. Only use 50% each time, move "to-clean instance" to the other 50% when clean
			- pros:
				- fast, easy to implement, not cause  "space fragments"
			- cons:
				- Only 50% of memory space can be used everytime
				- will cause more frequent GC

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic//mark_copy.png">
		- `Generation collection`
			- > Java 1.3
			- mechanisms:
				- step 1) new instances storaged in Young Generation, when it's full, will trigger minor GC
				- step 2) move survived instances to FromSpace.
				- step 3) when FromSpace full, trigger minor GC
				- step 4) move survived instances to ToSpace.
				- ....
				- step 5) move still-survived instances to Old Generation
			- Young Generation
				 - (Eden：FromSpace：ToSpace = 8:1:1 by default)
				 - "minor GC"
				 - Eden : storage "new" instances
				 - Survivor
				 	- FromSpace (Survivor0)
				 	- ToSpace (Survivor1) : 
				 		- continuous memory
			- Old Generation 
				- Old Space : storage "long life cycle" instances
				- "major GC"
			- permanent generation
				- AKA "method area". (after Java 8, this space moved to "MetaSpace", not use JVM memory anymore, but local memory)
				- storage class, string..
				- there is also GC here. "major GC"
- properties:
	- GC process is a `local priority`, `independent` thread
	- JVM GC is an automatic mechanism. we can also manually trigger it : `System.gc();`
- Ref
	- https://www.jyt0532.com/2020/03/12/garbage-collector/
	- https://en.wikipedia.org/wiki/Garbage_collection_(computer_science)
	- https://www.twblogs.net/a/5e50bb8cbd9eee21167e504f
	- https://www.alexleo.click/java-%E5%96%9D%E6%9D%AF%E5%92%96%E5%95%A1%EF%BC%8C%E8%81%8A%E9%BB%9E-gc%EF%BC%88%E4%B8%80%EF%BC%89-%E5%9F%BA%E7%A4%8E%E6%A6%82%E5%BF%B5/
	- https://iter01.com/542065.html

### 3') Explain GC's ”stop-the-world” ? 

### 4) how to get memory in java program, heap usage percentage (%) ?
- `java.lang.Runtime`    : get remaining memory, all memory, max heap memory
- `Runtime.freeMemory()` : get remain memory in binary
- `Runtime.totalMemory()`: get total memory in binary
- `Runtime.maxMemory() ` : get max memory in binary
 
### 5) Explain classLoader ?
- Implemented by `ClassLoader` class (and its sub class)
- Load `.class` files to JVM
- Steps:
	- Bootstrap class loader -> ExtClassLoader -> AppClassLoader (loader step)
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

### 6) Explain `memory leakage` ?

### 7) does `memory leakage` happen in java ? how ?
- Yes, it may happen in users self defined data structure
- example ?
- how ?
- Ref
	- https://cloud.tencent.com/developer/article/1648836

### 8) Difference between Serial and Parallel GC  ? 

### 9) Thread, progress, program ?
- https://oldmo860617.medium.com/%E9%80%B2%E7%A8%8B-%E7%B7%9A%E7%A8%8B-%E5%8D%94%E7%A8%8B-%E5%82%BB%E5%82%BB%E5%88%86%E5%BE%97%E6%B8%85%E6%A5%9A-a09b95bd68dd

### 10) Explain JVM `reflection` ? `dynamic proxy` ? 

## Ref
- https://dunwu.github.io/javacore/#%F0%9F%93%96-%E5%86%85%E5%AE%B9
- https://www.jyt0532.com/2020/03/14/epilogue/
- https://cloud.tencent.com/developer/article/1648836
- https://iter01.com/565257.html
- https://www.51cto.com/article/670305.html