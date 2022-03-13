# FLINK FAQ

1. What is Apache Flink?
-  Flink is built around a distributed streaming data-flow engine written in Java and Scala. Flink runs every dataflow programm in a data-parallel and pipelined fashion.

2. Explain Apache Flink Architecture?
- Apache Flink is based on the Kappa architecture. The Kappa architecture uses a single processor - stream, who accepts all information as a stream, and the streaming engine processes data in real-time. Batch data in kappa architecture is a form of streaming data.

3. Explain the Apache Flink Job Execution Architecture?

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/flink-job-exe-architecture.png" width="500" height="300">

- Program
	- It is a piece of code that is executed on the Flink Cluster.
- Client
	- client server that takes code for to-run program, creates a job dataflow graph, which is then passed to JM (job manager).
- Job manager (JM)
	- It is responsible for generating the execution graph after obtaining the Job Dataflow Graph from the Client. It assigns the job to TaskManagers in the cluster and monitors its execution.
- Task manager (TM)
	- It is in charge of executing all of the tasks assigned to it by JobManager. Both TaskManagers execute the tasks in their respective slots in the specified parallelism. It is in charge of informing JobManager about the status of the tasks.

4. What is Unbounded streams in Apache Flink?
- Any type of data is produced as a stream of events. Data can be processed as unbounded or bounded streams.
- Unbounded streams have a beginning but no end. They do not end and continue to provide data as it is produced.
-  Unbounded streams should be processed continuously, i.e., events should be handled as soon as they are consumed. Since the input is unbounded and will not be complete at any point in time, it is not possible to wait for all of the data to arrive.

5. What is Bounded streams in Apache Flink?
- Bounded streams have a beginning and an end point.
-  Bounded streams could be processed by consuming all data before doing any computations.
-  Ordered ingestion is not needed to process bounded streams since a bounded data set could always be sorted. Processing of bounded streams is also called as batch processing.

6. What is Dataset API in Apache Flink?
- The Apache Flink Dataset API is used to do `batch` operations on data over time. 
- This API is available in Java, Scala, and Python. 
- It may perform various transformations on datasets such as filtering, mapping, aggregating, joining, and grouping.

7. What is DataStream API in Apache Flink?
- The Apache Flink DataStream API is used to handle data in a continuous `stream`.
- On the stream data, you can perform operations such as filtering, routing, windowing, and aggregation.
- On this data stream, there are different sources such as message queues, files, and socket streams, and the resulting data can be written to different sinks such as command line terminals. 

8. What is Apache Flink Table API?
- Table API is a relational API with an expression language similar to SQL. 
- This API is capable of `batch` and `stream` processing.
- . It is compatible with the Java and Scala Dataset and Datastream APIs. 
- Tables can be generated from internal Datasets and Datastreams as well as from external data sources. You can use this relational API to perform operations such as join, select, aggregate, and filter. 

9. What is Apache Flink FlinkML?
- FlinkML is the Flink Machine Learning (ML) library.

10. What are the differences between Apache Hadoop, Apache Spark and Apache Flink?

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic//compare_hadoop_spark_flink.png" width="500" height="300">


## Ref
- https://www.techgeeknext.com/apache/apache-flink-interview-questions