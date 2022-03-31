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

11. What are the key programming constructs of a Flink streaming application?
- A Flink streaming application consists of four key programming constructs.
	- 1) Stream execution environment - Every Flink streaming application requires an environment in which the streaming program is executed.
	- 2) Data sources Data sources are applications or data stores from which the Flink application will read the input data from.
	- 3) Data streams and transformation operations - Input data from data sources are read by the Flink application in the form of data streams.
	- 4) Data sinks - The transformed data from data streams are consumed by data sinks which output the data to external systems. 

12. explain `Complex Event Processing (CEP)` ?
- FlinkCEP is an API in Apache Flink, which analyses event patterns on continuous streaming data. These events are near real time, which have high throughput and low latency. This API is used mostly on Sensor data, which come in real-time and are very complex to process.
- CEP: It is used for complex event processing.
- https://www.tutorialspoint.com/apache_flink/apache_flink_libraries.htm

13. What are the Apache Flink domain-specific libraries?
- FlinkML: It is used for machine learning.
- Table: It is used to perform the relational operation.
- Gelly: It is used to perform the Graph operation.
- CEP: It is used for complex event processing.
- https://www.cloudduggu.com/flink/interview-questions/

14. What are the different ways to use Apache Flink?
Apache Flink can be deployed and configured in the below ways.
	- Flink can be setup Locally (Single Machine)
	- It can be deployed on a VM machine.
	- We can use Flink as a Docker image.
	- We can configure and deploy Flink as a standalone cluster.
	- Flink can be deployed and configured on Hadoop YARN and another resource managing framework.
	- Flink can be deployed and configured on a Cloud system.

15. Explain how flink implement `exactly once` ? what's the logics, mechanisms.. ?
- `distributed snapshot`
- `2 phases commit`
	- TwoPhaseCommitSinkFunction
- https://segmentfault.com/a/1190000022891333
- https://flink.apache.org/features/2018/03/01/end-to-end-exactly-once-apache-flink.html
- https://eng.uber.com/real-time-exactly-once-ad-event-processing/
- https://zhuanlan.zhihu.com/p/266620519

16. Explain flink savepoint mechanisms ?

17. Explain flink checkpoint mechanisms ?

18. Explain flink backpressure ?

19. Explain how flink submit job to yarn ?

20. Explain how flink parallelism ?

## Ref
- https://www.techgeeknext.com/apache/apache-flink-interview-questions
- https://www.interviewgrid.com/interview_questions/flink
- https://www.tutorialspoint.com/apache_flink/index.htm