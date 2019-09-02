## SPARK / HADOOP ECOSYSTEM  FAQ 

1. Difference between Spark VS hadoop?

2. Things happen after `spark-submit`?

3. What's RDD, HDFS ?

4. Explain spark `master`, `worker`, ,`executor`, `receiver`,`driver`...?

5. Explain how to deal with `Spark data skew` problems ? ***

6. How do if one of the spark node failed because of overloading ?

7. Why Spark is faster than MapReduce in general ?

8. Explain Spark running mode : `stand alone`, `Mesos`, `YARN` ?

9. Explain spark operator ?

10. Explain `cache` VS `persist` ?

11. Which one is better in RDD ? `reducebykey` VS `groupby` ? why?

12. What errors may happen when `spark streaming` ? how to fix them ? 

13. How to prevent spark out of memeory ?

14. How does saprk split `stage` ?

15. What're spark work, stage, task; and there relationship ?

16. How to set up spark master HA ?

17. How does `spark-submit` import external `jars`  

18. Explain spark Polyglot, Lazy Evaluation ?

19. Is there any benefit of learning MapReduce if Spark is better than MapReduce?
	- Yes, MapReduce is a paradigm used by many big data tools including Spark as well. It is extremely relevant to use MapReduce when the data grows bigger and bigger. Most tools like Pig and Hive convert their queries into MapReduce phases to optimize them better.

20. What is Executor Memory in a Spark application?

21. What operations does RDD support?

	- RDDs support two types of operations: transformations and actions. 

		- Transformations: Transformations create new RDD from existing RDD like map, reduceByKey and filter we just saw. Transformations are executed on demand. That means they are computed lazily.
		e.g. :`map`, `flatmap`, `reducebykey`, `filter`, `union`, `sample`

		- Actions: Actions return final results of RDD computations. Actions triggers execution using lineage graph to load the data into original RDD, carry out all intermediate transformations and return final results to Driver program or write it out to file system.

22. Flatmap VS map ? Reduce VS ReduceByKey ? 

23. 



## Ref 
- https://www.edureka.co/blog/interview-questions/top-apache-spark-interview-questions-2016/
- https://data-flair.training/blogs/spark-rdd-operations-transformations-actions/
- https://zhuanlan.zhihu.com/p/47499258


