# FLINK FAQ

### 1. What is Apache Flink?
-  Flink is built around a distributed streaming data-flow engine written in Java and Scala. Flink runs every dataflow programm in a data-parallel and pipelined fashion.

### 2. Explain Apache Flink Architecture?
- Apache Flink is based on the Kappa architecture. The Kappa architecture uses a single processor - stream, who accepts all information as a stream, and the streaming engine processes data in real-time. Batch data in kappa architecture is a form of streaming data.

- Ref
	- note below !!!
	- https://www.shuzhiduo.com/A/amd0NwnXJg/

### 2' Explain Apache Flink execution graph and its step  ?
- StreamGraph -> JobGraph -> ExecutionGraph -> physical graph

### 3. Explain the Apache Flink Job Execution Architecture?

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/flink-job-exe-architecture.png" width="500" height="300">

- Program
	- It is a piece of code that is executed on the Flink Cluster.
- Client
	- client server that takes code for to-run program, creates a job dataflow graph, which is then passed to JM (job manager).
- Job manager (JM)
	- It is responsible for generating the execution graph after obtaining the Job Dataflow Graph from the Client. It assigns the job to TaskManagers in the cluster and monitors its execution.
- Task manager (TM)
	- It is in charge of executing all of the tasks assigned to it by JobManager. Both TaskManagers execute the tasks in their respective slots in the specified parallelism. It is in charge of informing JobManager about the status of the tasks.

###  4. What is Unbounded streams in Apache Flink?
- Any type of data is produced as a stream of events. Data can be processed as unbounded or bounded streams.
- Unbounded streams have a beginning but no end. They do not end and continue to provide data as it is produced.
-  Unbounded streams should be processed continuously, i.e., events should be handled as soon as they are consumed. Since the input is unbounded and will not be complete at any point in time, it is not possible to wait for all of the data to arrive.

### 5. What is Bounded streams in Apache Flink?
- Bounded streams have a beginning and an end point.
-  Bounded streams could be processed by consuming all data before doing any computations.
-  Ordered ingestion is not needed to process bounded streams since a bounded data set could always be sorted. Processing of bounded streams is also called as batch processing.

### 6. What is Dataset API in Apache Flink?
- The Apache Flink Dataset API is used to do `batch` operations on data over time. 
- This API is available in Java, Scala, and Python. 
- It may perform various transformations on datasets such as filtering, mapping, aggregating, joining, and grouping.

### 7. What is DataStream API in Apache Flink?
- The Apache Flink DataStream API is used to handle data in a continuous `stream`.
- On the stream data, you can perform operations such as filtering, routing, windowing, and aggregation.
- On this data stream, there are different sources such as message queues, files, and socket streams, and the resulting data can be written to different sinks such as command line terminals. 

### 8. What is Apache Flink Table API?
- Table API is a relational API with an expression language similar to SQL. 
- This API is capable of `batch` and `stream` processing.
- . It is compatible with the Java and Scala Dataset and Datastream APIs. 
- Tables can be generated from internal Datasets and Datastreams as well as from external data sources. You can use this relational API to perform operations such as join, select, aggregate, and filter. 

### 9. What is Apache Flink FlinkML?
- FlinkML is the Flink Machine Learning (ML) library.

### 10. What are the differences between Apache Hadoop, Apache Spark and Apache Flink?

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic//compare_hadoop_spark_flink.png" width="500" height="300">

### 11. What are the key programming constructs of a Flink streaming application?
- A Flink streaming application consists of four key programming constructs.
	- 1) Stream execution environment - Every Flink streaming application requires an environment in which the streaming program is executed.
	- 2) Data sources Data sources are applications or data stores from which the Flink application will read the input data from.
	- 3) Data streams and transformation operations - Input data from data sources are read by the Flink application in the form of data streams.
	- 4) Data sinks - The transformed data from data streams are consumed by data sinks which output the data to external systems. 

### 12. explain `Complex Event Processing (CEP)` ?
- FlinkCEP is an API in Apache Flink, which analyses event patterns on continuous streaming data. These events are near real time, which have high throughput and low latency. This API is used mostly on Sensor data, which come in real-time and are very complex to process.
- CEP: It is used for complex event processing.
- https://www.tutorialspoint.com/apache_flink/apache_flink_libraries.htm

### 13. What are the Apache Flink domain-specific libraries?
- FlinkML: It is used for machine learning.
- Table: It is used to perform the relational operation.
- Gelly: It is used to perform the Graph operation.
- CEP: It is used for complex event processing.
- https://www.cloudduggu.com/flink/interview-questions/

### 14. What are the different ways to use Apache Flink?
Apache Flink can be deployed and configured in the below ways.
	- Flink can be setup Locally (Single Machine)
	- It can be deployed on a VM machine.
	- We can use Flink as a Docker image.
	- We can configure and deploy Flink as a standalone cluster.
	- Flink can be deployed and configured on Hadoop YARN and another resource managing framework.
	- Flink can be deployed and configured on a Cloud system.

### 15. Explain how flink implement `exactly once` ? what's the logics, mechanisms.. ?
- `distributed snapshot`
- `2 phases commit`
	- TwoPhaseCommitSinkFunction
- Steps)
	- step 1) for each checkpoint, flink will start a "transaction", add all inform to the transaction
		- beginTransaction
			- before transaction, create a tmp file, write data into it first
	- step 2) when data sink to external system, not commit, but `pre-commit`
		- pre-commit
			- "flush" in-memory data into tmp file, then close file. repeat this step to next checkpoint
	- step 3) when flink receive checkpoint's confirmation, commit the transaction, data is then "really" written to external system
		- commit
			- move tmp file to actual dest path. May have some delay
	- NOTE : external system also needs to have "transaction" mechanism, so can have end-to-end "exactly once"
- https://segmentfault.com/a/1190000022891333
- https://flink.apache.org/features/2018/03/01/end-to-end-exactly-once-apache-flink.html
- https://eng.uber.com/real-time-exactly-once-ad-event-processing/
- https://zhuanlan.zhihu.com/p/266620519

### 16. Explain flink `savepoint` ?
- `savepoint` is a "global backup" of flink status of a timestamp moment
- For software upgrade, change conf. Can make flink restart from the last savepoint
- Triggered by users
- will keep existing until user delete
- CAN recognize `savepoint` as a `special snapshot` of `checkpoint` in a specific time
- Trigger way
	- `flink savepoint` command
	- `flink cancel -s` command, when cancel flink job
	- via REST API : `**/jobs/:jobid /savepoints**`
- Ref
	- https://zhuanlan.zhihu.com/p/79526638

### 17. Explain flink `checkpoint` ?
- checkpoint is a "false tolerance" mechanism
- make sure flink can auto-recover when error/exception during running
- managed/op by flink. Users only need to define parameter
- Auto op by flink
- default `concurrent = 1` -> there is ONLY ONE runs per flink app
- when a Flink DataStream runs
	- steps:
		- StreamGraph -> JobGraph -> ExecutionGraph -> physical DAG
			- when "ExecutionGraph" init, "CheckpointCoordinator" get init as well
			- for each flink task, it will init a "CheckpointCoordinator"
		- Flink will trigger "Barrier" periodically
		- (check below)
- low level mechanisms:
	- 1) when checkpoint/savepoint triggered, get HDFS path based on its type
	- 2) if savepoint:
			- HDFS path = savepoint root path + savepoint-jobid-first-6-digit + random_num
		 if checkpoint:
		 	- checkpoint root path + other + checkpoint_numbers (??) (to fix)
	- 3) there is `_metadata` file under savepoint (index for status doc)
	- 4) can use various "status backend" for checkpoint storage

- can disable checkpoint via `CheckpointConfig` setting
	- DELETE_ON_CANCELLATION : delete checkpoint when program canceled
	- RETAIN_ON_CANCELLATION : keep checkpoint when program canceled
- Ref
	- https://zhuanlan.zhihu.com/p/79526638

### 17' Explain flink `Barrier` ?
- A special event
- Will follow event from upstream operator to downstream operator
- ONLY when "final" operator (e.g. sink operator) receive Barrier, and confirm checkpoint is OK, then this "checkpoint" is completed

### 18. Explain flink `backpressure` ?
- Is a common concept in stream framework
- It happens when `"downstream" CAN'T catchup "upstream"'s processing speed`
	- -> So there's a mechanism `push back` to upstream and ask them `slow down` their process
- Can due to internet, disk I/O, freqent GC, data hotpoint... or data skew, code efficiency, TM memory, its GC
- Can also affect checkpoint
	- if data get delay -> checkpoint get delay (more longer)
	- if "exactly once" -> have to wait delay barrier -> more data will be cached -> checkpoint get bigger
	- above make cause checkpoint failed, or OOM
- Can monitor via Flink UI (version > 1.13)
- Not really a problem at all cases, have a backpressure sometimes means system is using fully of its resources. But a serious backpressure can cause system delay

- Ref
	- https://zhuanlan.zhihu.com/p/264637970
	- https://www.modb.pro/db/128767
	- https://zhuanlan.zhihu.com/p/266638799

### 18'. Explain flink data exchange mechanisms ?
- exchange in same Task
- exchange in different Task, same TM
- exchange in different Task, different TM
- Ref
	- https://zhuanlan.zhihu.com/p/264637970

### 19. Explain how flink submit job to yarn ?
- Components
	- ResourceManager (RM)
	- NodeManager (NM)
	- AppMaster (`job manager runs in the same container`)
	- Container (task manager runs on it)
- Steps
	- step 1) flink client upload `flink jar, app jar, and conf` to HDFS
	- step 2) client submit job to Yarn ResourceManager, and register resources
	- step 3) ResourceManager diepense container and launch `AppMaster`, then AppMaster load jar files and config env, then launch `job manager`
	- step 4) once above steps success, AppMaster knows job manager's ip
	- step 5) Job Manager create a new flink conf for task manager, this conf also uploaded to HDFS. AppMaster offer flink web service endpoint
		- NOTE : endpoints offered by Yarn are ALL temporal ones, user can run multiple flink on Yarn
	- step 6) AppMaster ask ResourceManager for resources. NodeManager load flink jar and launch TaskManager
	- step 7) TaskManager successfully launched, send hear-beat to job manager, ready to execute missions (job manager's command)
- pros:
	- "use as needed" -> can raise memory usage pct in system
	- can run jobs basd on "priority" as priority setting in Yarn jobs
	- can automatically deal with `Failover on various roles`
		- JobManager, TaskManager error/exception... can automatically retry/rerun by Yarn
- Pic
	<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/flink_yarn_1.png">
- Ref
	- https://blog.csdn.net/penriver/article/details/120221565
	- https://blog.csdn.net/lb812913059/article/details/86601150
	- https://blog.51cto.com/u_15478540/4911346
	- https://cloud.tencent.com/developer/article/1708704

### 20. Explain how flink parallelism ?

### 21. Explain flink watermark ?
- `watermark` is a `timestamp` (event happened time, NOT processed time)
- `watermark` tells Flink `since when it DOES NOT need to wait for delay event`
- Stream frameworks use it for "evaluate if there is still a event not arrived"
- Types
	- Punctuated Watermark
		- make watermark when there is "special event"
		- not relative to window time, depends when "special event" is received
		- usually used for "real real-time" scenario
	- Periodic Watermark
		- periodically make watermark
		- time gap can be defined by users
- examples :
	- "out-of-order"
		- `watermark` timestamp > window endTime
		- there is data in window_start_time, window_end_time] 
	- "late element" event case
		- once after `watermark` -> trigger `window` -> do the op/calculation 
- Ref
	- https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/event-time/generating_watermarks/#:~:text=%E6%97%B6%E9%97%B4%E6%A6%82%E8%A7%88%E5%B0%8F%E8%8A%82%E3%80%82-,Watermark%20%E7%AD%96%E7%95%A5%E7%AE%80%E4%BB%8B,%E5%8E%BB%E8%AE%BF%E9%97%AE%2F%E6%8F%90%E5%8F%96%E6%97%B6%E9%97%B4%E6%88%B3%E3%80%82
	- https://www.cnblogs.com/rossiXYZ/p/12286407.html
	- http://chris-liu.cn/Flink-%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0-%E2%80%94%E2%80%94%E2%80%94-WaterMark-%E6%B0%B4%E5%8D%B0.html
	- https://www.gushiciku.cn/pl/pRWT/zh-tw

### 22. Explain flink EvenTime, IngestionTime, ProcessingTime ?
- `EvenTime`
	- "event happened time". The most accurate time when things happended
- `IngestionTime`
	- time when event ingested into Flink, the time source operator created. Is Flink job manager's system time in most cases
- `ProcessingTime`
	- Event processed time. Is timestamp when transformation happened. created in Flink task manager

## Ref
- https://www.techgeeknext.com/apache/apache-flink-interview-questions
- https://www.interviewgrid.com/interview_questions/flink
- https://www.tutorialspoint.com/apache_flink/index.htm