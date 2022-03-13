# SPARK / HADOOP ECOSYSTEM  FAQ 

0. - Spark Tutorial 
	- https://data-flair.training/blogs/spark-tutorial/

   - Hadoop VS hive VS hbase VS pig VS RDBMS (in EMR system)
   	- https://aws.amazon.com/tw/emr/faqs/

1. Difference between Spark VS Hadoop?

- ***Hadoop*** is a big data framework that make large scale dataset operation via `Map-Reduce`, do storage via split the dataset (`HDFS`) into `data block` and save in each node (data node)
	- Data node : the components save split data block
	- Name node : the components manage data storage places
	- `Doesn't save data in memory` when do data op

- ***Spark*** is a big data framework that access large scale dataset like above, and doing processing like : ETL, stream, machine learning and so on...

	- `Does save data in memory` as `RDD` when do data op, so that's why spark is faster than hadoop (since it saves data in memory when do op) (only work when data is not really in `large` scale)

- In short, `Spark` can do more flexible data task via RDD and `DAG`( 
  Map-Reduce-Map-Reduce ...) ops and faster speed (data in memory), but `Spark` job also `heavy memory costing`. So if the data is really in a `large` scale, then Spark may not be a good choice, but would use `Hadoop` since it only do Map-Reduce, all the momory cost is only for key-value pairs saving theoretically. 

  - http://web.stanford.edu/class/cs246/slides/01-intro.pdf

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/spark_vs_hadoop.png" width="500" height="300">

1'. What's `Map-Reduce` programming model?

- It's a `Map -> Group by -> Reduce` process model for scalable data transformation
- Developers only have to think about writing the code for `Map` and `Reduce`
 part, the execution machines will take care for the `Group by` step
- Map : map input values into `key-value` pair
- Group by : dispense key-value to cluster workers
- Reduce : Get the computaion result of the key-value based on redue function logic 

- Pros : 
	- Easy model 
	- Not heavy memory cost  
	- Can be scalable

- Cons :
	- Hadoop code long 
	- take time to write, doesn't offer flexible high level APIs. 
	- have to keep grab-release data when do `sequence` map-reduce tasks 
	- doesn't offer RDD features : lazy execution, call back ...

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/map_reduce_overview.png" width="500" height="300">  

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/map_reduce_diagram.png" width="500" height="300">  

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/map_reduce_parallel.png" width="500" height="300">  

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/map_reduce_pattern.png" width="500" height="300">  

2. Things happen after `spark-submit`?
	- step 1) This program invokes the main() method that is specified in the spark-submit command, which launches the driver program.
	- step 2)  The driver program `converts the code` into `Directed Acyclic Graph(DAG)` which will `have all the RDDs and transformations` to be performed on them. ( During this phase driver program also does some optimizations and then it converts the DAG to a physical execution plan with set of stages.)
	- step 3) After this physical plan, driver creates small execution units called tasks. Then these tasks are sent to Spark Cluster.
	- step 4) The driver program then talks to the cluster manager and requests for the resources for execution
	- step 5) Then the cluster manger launches the executors on the worker nodes
	- step 6)  Executors will register themselves with driver program so the driver program will have the complete knowledge about the executors
	- step 7) Then driver program sends the tasks to the executors and starts the execution. Driver program always monitors these tasks that are running on the executors till the completion of job.
	- step 8) When the job is completed or called stop() method in case of any failures, the driver program terminates and frees the allocated resources

- http://www.bigdatainterview.com/explain-spark-architecture-or-what-happens-when-submit-a-spark-job/


2'. `spark-submit` cmd explain ?
```bash
# pattern
./bin/spark-submit \
  --master <master-url> \
  --deploy-mode <deploy-mode> \
  --conf <key<=<value> \
  --driver-memory <value>g \
  --executor-memory <value>g \
  --executor-cores <number of cores>  \
  --jars  <comma separated dependencies>
  --class <main-class> \
  <application-jar> \
  [application-arguments]
```

- https://sparkbyexamples.com/spark/spark-submit-command/

2'' `SparkSession` VS `SparkContext` ?
- SparkContext 
	- an entry point to Spark and defined in org.apache.spark package since 1.x and used to programmatically create Spark RDD, accumulators and broadcast variables on the cluster. Since Spark 2.0 most of the functionalities (methods) available in SparkContext are also available in SparkSession. Its object sc is default available in spark-shell and it can be programmatically created using SparkContext class.
- SparkContext
	- introduced in version 2.0 and and is an entry point to underlying Spark functionality in order to programmatically create Spark RDD, DataFrame and DataSet. It’s object spark is default available in spark-shell and it can be created programmatically using SparkSession builder pattern.
	- SparkSession can be used in replace with SQLContext and HiveContext.

- https://sparkbyexamples.com/spark/sparksession-vs-sparkcontext/


3. What's RDD, HDFS ?

- RDD 
	- Resilient -- if data is lost, data can be recreated
	- Distributed -- stored in nodes among the cluster
	- Dataset -- initial data comes from some distributed storage

- Resilient Distributed Datasets (RDD) is a simple and immutable distributed collection of objects. Each RDD is split into multiple partitions which may be computed on different nodes of the cluster. In Spark, every function is performed on RDDs only.

- Spark revolves around the concept of a resilient distributed dataset (RDD), which is a fault-tolerant collection of elements that can be operated on in parallel.

- Hadoop HDFS VS RDD 

	- In Hadoop, we store the data as blocks and store them in different data nodes. In Spark, instead of following the above approach, we make partitions of the RDDs and store in worker nodes (datanodes) which are computed in parallel across all the nodes.

	- In Hadoop, we need to replicate the data for fault recovery, but in case of Spark, replication is not required as this is performed by RDDs.

	- RDDs load the data for us and are resilient which means they can be recomputed.

	- RDDs perform two types of operations: Transformations which creates a new dataset from the previous RDD and actions which return a value to the driver program after performing the computation on the dataset.

	- RDDs keeps a track of transformations and checks them periodically. If a node fails, it can rebuild the lost RDD partition on the other nodes, in parallel.


3'. Explain data partition in Spark?

- Partitioning is nothing but dividing it into parts. If you talk about partitioning in distributed system, we can define it as the division of the large dataset and store them as multiple parts across the cluster.

- Spark works on data locality principle. Worker nodes takes the data for processing that are nearer to them. By doing partitioning network I/O will be reduced so that data can be processed a lot faster.

- In Spark, operations like co-group, groupBy, groupByKey and many more will need lots of I/O operations. In this scenario, if we apply partitioning, then we can reduce the number of I/O operations rapidly so that we can speed up the data processing.

- To divide the data into partitions first we need to store it. Spark stores its data in form of RDDs.

- https://acadgild.com/blog/partitioning-in-spark

4. Explain spark  `master node`,`worker node` ,`executor`, `receiver`,`driver`...?

	<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/spark_driver_workder_executor.png" width="500" height="300">

	- Driver 
		- The program that runs on the master node of the machine and declares transformations and actions on data RDDs. In simple terms, a driver in Spark creates `SparkContext`, connected to a given Spark Master. The driver also delivers the RDD graphs to Master, where the standalone cluster manager runs.
		- The Driver is one of the nodes in the Cluster.
		- The driver does not run computations (filter,map, reduce, etc).
		- It plays the role of a master node in the Spark cluster.
		- When you call collect() on an RDD or Dataset, the` whole data` is sent to the `Driver`. This is why you should be careful when calling collect().

	- Master
		- Master node is responsible for task scheduling and resource dispensation.

	- Worker  
		- `Worker node refers to any node that can run the application code in a cluster`. The driver program must listen for and accept incoming connections from its executors and must be network addressable from the worker nodes. 
		- Worker node is basically the `slave node`. Master node assigns work and worker node actually performs the assigned tasks. `Worker nodes process the data stored on the node and report the resources to the master`. Based on the resource availability, the master schedule tasks.

	- Executor 
		- When SparkContext connects to a cluster manager, it acquires an Executor on nodes in the cluster. Executors are Spark processes that run computations and store the data on the worker node. The final tasks by SparkContext are transferred to executors for their execution.
		- Executors are JVMs that run on Worker nodes. 
		- These are the JVMs that actually run Tasks on data Partitions.

	- Cluster 
		- A Cluster is a group of JVMs (nodes) connected by the network, each of which runs Spark, either in Driver or Worker roles.

5. Explain how to deal with `Spark data skew` problems ? ***

6. How do if one of the spark node failed because of overloading ?

7. Why Spark is faster than MapReduce in general ?

8. Explain Spark running mode : `stand alone`, `Mesos`, `YARN` ?

	- Spark stand anlone 
		- When running on standalone cluster deployment, the cluster manager is a Spark master instance.

	- Spark Mesos
		- When using Mesos, the Mesos master replaces the Spark master as the cluster manager. Mesos determines what machines handle what tasks. Because it takes into account other frameworks when scheduling these many short-lived tasks, multiple frameworks can coexist on the same cluster without resorting to a static partitioning of resources.

	- Spark YARN 

9. Explain spark operator ?

10. Explain `cache` VS `persist` ?

11. Which one is better in RDD ? `reducebykey` VS `groupby` ? why?

12. What errors may happen when `spark streaming` ? how to fix them ? 

13. How to prevent spark `out of memeory` `(OOM)` problem ?

14. How does saprk split `stage` ?

15. What're spark work, stage, task; and there relationship ?

16. How to set up spark master HA ?

17. How does `spark-submit` import external `jars` 
- https://sparkbyexamples.com/spark/add-multiple-jars-to-spark-submit-classpath/ 

18. Explain spark Polyglot, Lazy Evaluation ?

- Polyglot 
- Lazy Evaluation
	- Each RDD have access to it's parent RDD
	- NULL is the value of parent for first RDD
	- Before computing it's value, it always computes it's parent
	- Spark will not execute the task untill there is a `Actions` operation 
	- there 2 types of RDD operation in spark : 
		- Transformations
			- map, groupby, union, filter, flatmap...
		- Actions
			- reduce, count, collect, show, saveAsTextFile...

18'. types of Transformations?
- Narrow transformation 
	- all the elements that are required to compute the records in `single partition` live in the single partition of parent RDD
	- `map`, `filter`
- Wide transformation 
	- In wide transformation, all the elements that are required to compute the records in the single partition may live in `many partitions` of parent RDD
	- `groupbyKey`, `reducebykey`

19. Is there any benefit of learning MapReduce if Spark is better than MapReduce?

	- Yes, MapReduce is a paradigm used by many big data tools including Spark as well. It is extremely relevant to use MapReduce when the data grows bigger and bigger. Most tools like Pig and Hive convert their queries into MapReduce phases to optimize them better.

20. What is Executor Memory in a Spark application?

21. What operations does RDD support?

	- RDDs support two types of operations: transformations and actions. 

		- Transformations: Transformations create new RDD from existing RDD like map, reduceByKey and filter we just saw. Transformations are executed on demand. That means they are computed lazily.
		e.g. :`map`, `flatmap`, `reducebykey`, `filter`, `union`, `sample`

		- Actions: Actions return final results of RDD computations. Actions triggers execution using lineage graph to load the data into original RDD, carry out all intermediate transformations and return final results to Driver program or write it out to file system.

22. Flatmap VS map ? Reduce VS ReduceByKey ? 

23. Illustrate some cons of using Spark.

24. What is a Sparse Vector?

25. How can you minimize data transfers when working with Spark?

	- Minimizing data transfers and avoiding shuffling helps write spark programs that run in a fast and reliable manner. The various ways in which data transfers can be minimized when working with Apache Spark are:

		- Using Broadcast Variable- Broadcast variable enhances the efficiency of joins between small and large RDDs.

		- Using Accumulators – Accumulators help update the values of variables in parallel while executing.

	The most common way is to `avoid` operations `ByKey, repartition` or `any other operations` which trigger `shuffles`.

26. What are broadcast variables and accumulators?

27. How can you trigger automatic clean-ups in Spark to handle accumulated metadata?

	- You can trigger the clean-ups by setting the parameter `spark.cleaner.ttl` or by dividing the long running jobs into different batches and writing the intermediary results to the disk.

28. Explain Caching in Spark Streaming ? 

	- DStreams allow developers to cache/ persist the stream’s data in memory. This is useful if the data in the DStream will be computed multiple times. This can be done using the persist() method on a DStream. For input streams that receive data over the network (such as Kafka, Flume, Sockets, etc.), the default persistence level is set to replicate the data to two nodes for fault-tolerance.

29. What are the various levels of persistence in Apache Spark?

	- MEMORY_ONLY: Store RDD as deserialized Java objects in the JVM. If the RDD does not fit in memory, some partitions will not be cached and will be recomputed on the fly each time they’re needed. This is the default level.

	- MEMORY_AND_DISK: Store RDD as deserialized Java objects in the JVM. If the RDD does not fit in memory, store the partitions that don’t fit on disk, and read them from there when they’re needed.
	
	- MEMORY_ONLY_SER: Store RDD as serialized Java objects (one byte array per partition).

	- MEMORY_AND_DISK_SER: Similar to MEMORY_ONLY_SER, but spill partitions that don’t fit in memory to disk instead of recomputing them on the fly each time they’re needed.

	- DISK_ONLY: Store the RDD partitions only on disk. OFF_HEAP: Similar to MEMORY_ONLY_SER, but store the data in off-heap memory.

30. Explain `mapPartitions`, what's the difference between mapPartitions and map ?

31. Explain `broadcast join`, what's the difference between `broadcast join`and join ?

32. How to optimize spark with `partitioner` ?

33. Explain `combineByKey` VS. `groupByKey` ?

i.e. 
```scala
// reduceByKey
rdd.reduceByKey(_.sum)

// groupByKey
rdd.groupByKey().mapValue(_.sum)

```

34. Explain `application`, `job`, `stage` , `task`? 

- `Application` -> `job` -> `stage` -> `task`

- Application
	- Initialize a SparkConext will generate an Application

- Job 
	- A Job is a sequence of `Stages`, triggered by an `Action` such as .count(), foreachRdd(), collect(), read() or write().
	- An `Action` operaor will generate a job

- Stage 
	- A Stage is a sequence of `Tasks` that can all be run together, `without a shuffle`.
	- e.g. : using .read to read a file from disk, then runnning .map and .filter can all be done `without a shuffle`, so it can fit in a `single` stage.

- Task 
	- A Task is a single operation `(.map or .filter)` happening on a specific RDD partition.
	- Each Task is executed as a single thread in an Executor
	- If your dataset has 2 Partitions, an operation such as a filter() will trigger 2 Tasks, one for each Partition.
	- Stage is a TaskSet, split the stage result to different Executors is a task

- Each `stage` contains as many `tasks` as `partitions` of the `RDD`
	- i.e. partition  (part of RDD) -> task (part of stage)

- http://queirozf.com/entries/spark-concepts-overview-clusters-jobs-stages-tasks-etc#stage-vs-task

- https://medium.com/@thejasbabu/spark-under-the-hood-partition-d386aaaa26b7

- https://www.youtube.com/watch?v=pEWrWdt60nY&list=PLmOn9nNkQxJF-qlCCDx9WsdAe6x5hhH77&index=56

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/spark_stage_task.png" width="500" height="300"> 

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/spark_stage_task2.png" width="500" height="300"> 


35. Explain `shuffle`?

- A Shuffle refers to an operation where data is `re-partitioned` across a Cluster.
- `join` and `any operation that ends with ByKey` will trigger a Shuffle.
- It is a `costly` operation because a lot of data can be sent via the network.

36. Explain `partition`, and its relation to RDD?
- `partition -> RDD`
- A Partition is a logical chunk of your RDD/Dataset
- Data is split into Partitions so that each Executor can operate on a single part, enabling parallelization.
- It can be processed by a single Executor core.
- e.g.: If you have 4 data partitions and you have 4 executor cores, you can process everything in parallel, in a single pass.

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/rdd_partiiton.png" width="500" height="300"> 


37. Explain spark `cache`?
- cache internally uses persist API
- persist sets a specific storage level for a given RDD
- Spark context tracks persistent RDD
- When first evaluates, partition will be put into memory by block manager


38. Explain repartition?
	- repartition():
		- shuffles the data between the executors and divides the data into number of partitions. But this might be an `expensive` operation since it `shuffles` the data between executors and involves `network traffic`. `Ideal place to partition` is at the `data source`, while fetching the data. Things can speed up greatly when data is partitioned the right way but can dramatically slow down when done wrong, especially due the Shuffle operation.
		- Reshuffle the data in the RDD `randomly` to create either `more or fewer` partitions and balance it across them. This always `shuffles ALL DATA over the network`.
	- repartitionAndSortWithinPartitions
		- Repartition the RDD according to the `given partitioner` and, within each resulting partition, sort records by their keys. This is `more efficient` than calling `repartition` and then sorting within each partition because it can push the sorting down into the shuffle machinery.
	- https://spark.apache.org/docs/latest/rdd-programming-guide.html

39. RDD Wide/narrow dependency ?
	- Narrow dependencies
		- When each partition at the parent RDD is used by at most one partition of the child RDD, then we have a narrow dependency. Computations of transformations with this kind of dependency are rather fast as they do not require any data shuffling over the cluster network. 
	- Wide dependencies
		- When each partition of the parent RDD may be depended on by multiple child partitions (wide dependency), then the computation speed might be significantly affected as we might need to shuffle data around different nodes when creating new partitions.
	- Reference
		- [ref1](https://medium.com/@dvcanton/wide-and-narrow-dependencies-in-apache-spark-21acf2faf031)
		- [ref2](https://www.coursera.org/lecture/scala-spark-big-data/wide-vs-narrow-dependencies-shGAX)
		- [ref3](https://youtu.be/ha6vTXJ9BMQ?t=707)

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/package_nego.png" width="500" height="300"> 

40. Cache/Persist VS Checkpoint ?
	- Cache/Persist
		- Persisting or caching with StorageLevel.DISK_ONLY (in `RAM`) cause the generation of RDD to be computed and stored in a location such that subsequent use of that RDD will not go beyond that points in recomputing the linage.
		- After persist is called, Spark still remembers the lineage of the RDD even though it doesn't call it.
		- After the application terminates, the cache is cleared or file destroyed
	- Checkpoint
		- Checkpointing stores the rdd `physically to hdfs` (in `Disk`) and destroys the lineage that created it.
		- The checkpoint file won't be deleted even after the Spark application terminated.
		- Checkpoint files can be used in subsequent job run or driver program
		- Checkpointing an RDD causes double computation because the operation will first call a cache before doing the actual job of computing and writing to the checkpoint directory.
	- [ref](https://stackoverflow.com/questions/35127720/what-is-the-difference-between-spark-checkpoint-and-persist-to-a-disk)

## Ref 
- https://www.edureka.co/blog/interview-questions/top-apache-spark-interview-questions-2016/
- https://data-flair.training/blogs/spark-rdd-operations-transformations-actions/
- https://zhuanlan.zhihu.com/p/47499258
