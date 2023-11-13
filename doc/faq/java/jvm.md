# JVM FAQ

### 1) JVN internal storage ?

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
	<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/jvm_storage_1.jpeg">
    <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/jvm_storage_2.jpeg">
    <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/jvm_storage_3.png">
- Ref
	- https://copyfuture.com/blogs-details/20210918043412580m
	- https://cloud.tencent.com/developer/article/1648836

### 2) Difference between JVM heap, stack ?

- JVM `stack`
	- storage stack frame, local var
	- smaller than heap in general
	- NOT shared by different threads. used by local thread only
- JVM `heap`
	- storage class
	- shared by all threads in JVM
- Ref
	- https://github.com/yennanliu/JavaHelloWorld

### 3) Explain JVM GC ? GC strategy ? algorithm ?

- <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic//gc1.png">

- 5W1H
	- Where ? 
		- JVM heap
	- Why ? 
		- prevent `memory leakage`. in order to use memory efficiently
	- What ? 
		- GC "recycle" object which NOT used anymore
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
			- implement above algorithm to young, old generation seperately
			- after Java 1.3
			- mechanisms:
				- step 1) new instances storaged in Young Generation, when it's full, will trigger minor GC
				- step 2) move survived instances to FromSpace (survivor 0).
				- step 3) when FromSpace full, trigger minor GC
				- step 4) move survived instances to ToSpace (survivor 1).
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
	- JVM GC is an automatic mechanism. we can also manually trigger it : `System.gc();` (but NOT recommended)
- Ref
	- https://www.jyt0532.com/2020/03/12/garbage-collector/
	- https://en.wikipedia.org/wiki/Garbage_collection_(computer_science)
	- https://www.twblogs.net/a/5e50bb8cbd9eee21167e504f
	- https://www.alexleo.click/java-%E5%96%9D%E6%9D%AF%E5%92%96%E5%95%A1%EF%BC%8C%E8%81%8A%E9%BB%9E-gc%EF%BC%88%E4%B8%80%EF%BC%89-%E5%9F%BA%E7%A4%8E%E6%A6%82%E5%BF%B5/
	- https://iter01.com/542065.html

### 3') Explain GC's ”stop-the-world” ? 

### 3'') Explain type of GC collector ?

- Serial collector
	- will cause "stop-the-world"
	- only ONE thread
- Parallel collector
	- will cause "stop-the-world"
	- can have MUTI thread
- Parallel Old collector
- Parnew collector
- CMS collector
- G1 collector
- Ref
	- https://cloud.tencent.com/developer/article/1648836

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
	- `BootstrapClassLoader`
		- implemented by c/c++. we CAN'T access them (but they do exist!).
		- load core java classes under `JAVA_HOME/jre/lib` (jre path) (defined by `sun.boot.class.path`)
		- run after JVM launch
	- ExtensionClassLoader
		- we can access them (but seldom do that)
		- load jar class under `JAVA_HOME/lib/ext` (defined by `java.ext.dirs`)
		- run after Bootstrap class loader
	- AppClassLoader
		- load classes in application, e.g. test class, 3rd party class..
		- load jar under `Classpath` (defined by `java.class.path`) or `-cp` or `-classpath`
		- run after ExtClassLoader
	- `User defined classLoader`
		-  TODO
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
- Important methods
	- loadClass() : load target class, will check if current ClassLoader or its parent already have it. If not, will call findClass()
	- findClass() : can load user-defined class
	- defineClass() : when findClass() get class, defineClass() will transfrom such class to .class instance
- Pics
	<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic//classloader1.png">
	<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic//classloader2.png">
- Ref
	- https://www.baeldung.com/java-classloaders
	- https://www.javatpoint.com/classloader-in-java
	- https://juejin.cn/post/6844904005580111879
	- https://blog.csdn.net/briblue/article/details/54973413
	- https://kknews.cc/zh-tw/code/8zvokbq.html
	- https://kknews.cc/tech/34pn9ba.html
	- https://openhome.cc/Gossip/JavaGossip-V2/IntroduceClassLoader.htm#:~:text=Bootstrap%20Loader%E6%98%AF%E7%94%B1C,lib%2Fext%20%E7%9B%AE%E9%8C%84%E4%B8%8B%E7%9A%84
	- https://www.youtube.com/watch?v=oHM_fVXnPTE&list=PLmOn9nNkQxJH0qBIrtV6otI0Ep4o2q67A&index=660

### 6) Explain `memory leakage` ?

- http://cloudtu.github.io/blog/2011/12/java-memory-leak.html
- https://www.baeldung.com/java-memory-leaks
- https://stackify.com/memory-leaks-java/

### 7) does `memory leakage` happen in java ? how ?

- Yes, it may happen in users self defined data structure
- example ?
- how ?
- Ref
	- https://cloud.tencent.com/developer/article/1648836

### 8) Difference between Serial and Parallel GC  ?

- Ref
	- https://www.alexleo.click/java-%E5%96%9D%E6%9D%AF%E5%92%96%E5%95%A1%EF%BC%8C%E8%81%8A%E9%BB%9E-gc%EF%BC%88%E4%B8%80%EF%BC%89-%E5%9F%BA%E7%A4%8E%E6%A6%82%E5%BF%B5/

### 9) Thread, progress, program ?

- https://oldmo860617.medium.com/%E9%80%B2%E7%A8%8B-%E7%B7%9A%E7%A8%8B-%E5%8D%94%E7%A8%8B-%E5%82%BB%E5%82%BB%E5%88%86%E5%BE%97%E6%B8%85%E6%A5%9A-a09b95bd68dd

### 10) Explain JVM `reflection` ? `dynamic proxy` ? 

### 11) Explain JVM instance creation steps ?

- Ref
	- https://github.com/Homiss/Java-interview-questions/blob/master/JVM/JVM%E9%9D%A2%E8%AF%95%E9%A2%98.md

### 12) Explain JVM instance life cycle ?

### 13) Explain JVM instance structure ?

### 14) Common JVM command ?

- jps
	- JVM Process Status Tool, show all system's hotspot threads in JVM
- jstat
	- JVM statistics Monitoring. Monitor JVM running status, can show class loading, inner memory GC, JIT
- jmap
	- JVM Memory Map. For creating heap dump doc
- jhat
	- JVM Heap Analysis. Use with jmap. analyze jmap's dump output. there is a HTTP/HTML server in jhat, can view view browser
- jstack
	- Create current JVM thread shanshot
- jinfo
	- JVM Configuration Info. Check/modify JVM running parameters in real-time 

### 15) Common JVM tune command ?

- jconsole
	- Java Monitoring and Management Console
	- java default too, for memory, thread, GC monitoring
- jvisualvm
	- JDK default too, can record memory/thread/ snapshot, monitor GC
- MAT
	- Memory Analyzer Tool
	- analyze JVM heap usage, can find memory leakage, usage
- GChisto
	- analyze GC log tool

### 16) Common JVM tune parameter ?

- `Xms` : min of java heap
- `Xmx` : max of java heap
- `-XX:NewSize` : new generation size
- `XX:NewRatio` : new generation pct VS old generation pct
- `XX:SurvivorRatio` : Eden pct VS survivor pct

### 17) What's int length in 64 bit JVM ?

- Not relative to platform. `Int length is a fixed value. It's always 34 bit`

### 18) Difference between WeakReference and SoftReference  and PhantomReference ?

### 19) Explain ` -XX:+UseCompressedOops` ?

### 20) What's max heap storage in 32 bit JVM and 64 bit JVM ?

- theoretically
	- 32 bit : `2**32` max heap storage
	- 64 bit : `2**64` max heap storage

### 21) Difference between JRE, JDK and JIT ?

- JRE : java run-time
- JDK : java development kit : java dev tool. JRE is included in it
- JIT : java in time compilation

## Ref

- https://javaguide.cn/java/jvm/memory-area.html#%E5%AF%B9%E8%B1%A1%E7%9A%84%E5%88%9B%E5%BB%BA
- https://dunwu.github.io/javacore/#%F0%9F%93%96-%E5%86%85%E5%AE%B9
- https://www.jyt0532.com/2020/03/14/epilogue/
- https://cloud.tencent.com/developer/article/1648836
- https://iter01.com/565257.html
- https://www.51cto.com/article/670305.html