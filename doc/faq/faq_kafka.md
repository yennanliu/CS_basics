# Kafka FAQ

### 1) Explain kafka architecture ?
- Feature
	- kafka is a sustainable `distributed` pub-sub (publish-subscribe) message system
	- developed by Linkedin via scala
	- can work with both online and offlie msg. Data saved on disk with replica -> prevent data lost
- Component
	- `Broker`
		- kafka cluster has many nodes
		- A broker is a node/server
	- `Topic`
		- every event on kafka belongs to a class, which is "topic"
		- each topic for different data source (feeds of messages)
		- can have unlimit topics
		- producer-subscriber use "topic" as basic unit. Can split further via topic partition
	- `Partition`
		- each topic has multiple partitions
		- NOTE : same broker can have multiple partition
				-> broker count has NO relation to partition count
		- each topic has a partition id. Start from 0
		- NOTE !!! : 
			- data in each partition can be ordering. But CAN'T guarantee global data (all data in topic) is ordering
			- ordering : keep the same ordering in producer's data and data read by consumer
		- partition defines the MAX "con-current" consumer in the same consumer group
		- So, we can raise consuming speed, if we have more partition
		<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/partition1.png">
		<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/partition2.png">
	-  Partition replicas
		- replication-factor
			- define how many replicas (on different brokers).
			- `# of replicas == # of broker` in general
			- Each `partition` has its own `leader replica` and `follower replica`
				- e.g. 1 leader, N followers -> N replicas
			- the follower replica which is in sync called "in-sync-replicas(ISR)"
			- NOTE !!! : `producer and consumer` BOTH `read and write` data from `leader replica`, NOT interact with follower replica
			- For data reliability when data I/O
			- if leader down, then will raise the other follower as new leader
		<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/partition_replicas.png">
	- `Segment`
		- each partition has `multiple` segments.
			- Each segment has 2 parts:
				- `.index` : index file (for .log). for finding offset in .log file
				- `.log` :  record actual event data
			- Name convention
				- consider "global partition". Start from 0
				- Next segment name if last partition's MAX offset (offset msg count)
				- offset is 64 bit Long, 20 digit nunber, filled with 0 if no digit
		- example:
			- ` 3,497` in `index` : means 3rd msg in .log file, and its offset = 497
			- `Message 368772` in `log` : means it's 368772 rd msg in global partition
			- .log and .index
			```bash
			# file structure
			/<topic_name>-<partition_name>
			/.../xxx.index
			/.../xxx.log

			# index file
			00000000000000000000.index   // <offset>.index
			# log content
			00000000000000000000.log     // <offset>.log
			```
			```bash
			# example
			-rw-r--r--. 1 root root 389k  1月  17  18:03   00000000000000000000.index
			-rw-r--r--. 1 root root 1.0G  1月  17  18:03   00000000000000000000.log
			-rw-r--r--. 1 root root  10M  1月  17  18:03   00000000000000077894.index
			-rw-r--r--. 1 root root 127M  1月  17  18:03   00000000000000077894.log
			```
		<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/index_log_file1.png">
	- `Producer`
		- msg producer, send msg to kafka broker
		- write data to `leader replica`
	- `Consumer`
		- msg consumer (client), read msg from kafka
		- consumer MUST belong to a consumer group
		- read data from `leader replica`
		- NOTE !!! : `# of consumer` should `<=` `# of partition in topic`
			- since same data should ONLY be consumed by one consumer under same consumer group at once
	- `Consumer group`
		- each consumer belogs to a specific `consumer group` (we can define consumer's group name)
		- SAME consumer in SAME consunmer group ONLY consume same msg ONCE
		- each consumer has a ID (group ID). All consumers can subscribe all partition under a topic
		- each partition CAN ONLY be consumed by A consumer under a consumer group
- Pic
	<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/kafka_architecture1.png">

- Ref
	- https://www.gushiciku.cn/pl/g6Tu/zh-tw

### 1') Kafka message structure ?
- As below, every msg sent from producer will be pre-processed via kafka, then saved as below structure (in kafka broker). Only last field is the actual data from broker
- Pic
	<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/kafka_msg.png">

### 1'') How kafka find .log file via index ?

### 2) How does kafka implement `exactly once` ?

### 3) How kafka avoid data missing ?
- Producer 
	- Can use `sync`, `async` mode send data to kafka
	- Mode
	- Sync mode
		- send a batch data to kafka, wait for kafka's response
			- producer wait 10 sec (?), if no ACK response, mark as failure
			- producer retry 3 times (?), if no ACK response, mark as failure
	- Async mode
		- send a batch data to kafka, ONLY offer a `callback()` method
		- save data in producer's buffer first, buffer size is about 20k
		- if meat threshold, then can send data (to kafka)
		- size of batch data is about 500
		- NOTE : if there is no ACK from kafka broker, but producer buffer is full, developer can decide mechanisms whether clean buffer or not (programmatically)
- Producer
	- Use `Partition replicas` avoid data missing
- Consumer
	- Each consumer record/maintain its own offset. can avoid data missing
	- We can save offset on client's file system, DB, Redis...

### 3') Explain kafka `ACK` ?
- `request.required.acks` : how to acknowledge when kafka writes producers' messgage to its (kafka) copy
- A tradeoff between efficiency (response speed) and reliability (fault tolerance) 
- cases
	- `ack = 1 (default)`
		- when producer -> kafka broker. if kafka leader comfirms msg received successfully -> Success, will lost data if leader down before followers sync
	- `ack = 0`
		- whenever producer -> kafka broker, mark it as success. Highest speed, will lost data if broker down
	- `ack = -1 (or all)`
		- when producer -> kafka broker, have to wait `leader and ALL followers' confirmation`. Slowest speed, but make sure NO data lost. 
- Ref
	- https://blog.51cto.com/u_15193673/2850009
	- https://blog.51cto.com/u_15278282/2932140
	- https://blog.csdn.net/lbh199466/article/details/89917693

### 4) Explain kafka basic data model ?

### 5) Explain how does kafka save data (low level, file system level) ?
- File
	- .log : file save data
	- .idnex : file save .log files' index 

### 6) Explain kafka master, slaves relation ? regarding data partition, ... ?

### 7) Steps when a consumer consumes a kafka topic ?

### 8) Explain kafka's (冪等性)
- Ref
	- https://blog.csdn.net/zc19921215/article/details/108466393#:~:text=Kafka%E5%B9%82%E7%AD%89%E6%80%A7%EF%BC%9A,number%E8%BF%99%E4%B8%A4%E4%B8%AA%E6%A6%82%E5%BF%B5%E3%80%82
	- http://matt33.com/2018/10/24/kafka-idempotent/

### 9) Explain kafka's (事務性)

### 10) Explain AR（Assigned Replicas), ISR (In-sync replica), and OSR（Out-of-Sync Replicas）?
- Ref
	- http://hk.noobyard.com/article/p-azlfvsay-mq.html
	- https://www.gushiciku.cn/pl/pTAJ/zh-tw

### 11) Describe kafka limitation ?
- auto scale : it's hard to scale down if scale out first (modify partition data in topics)
- keep published event in `ordering in global`

### 12) Explain why kafka can do high I/O ?
- ordering read/write
- zero copy
- split file
- batch transmit
- data compression
- Ref
	- https://iter01.com/639107.html

### 13) Explain kafka topic partition strategy ?
- Range strategy
- RoundRobin strategy
- Ref
	- https://iter01.com/639107.html

## Ref
- https://blog.csdn.net/ajianyingxiaoqinghan/article/details/107171104
- https://so.csdn.net/so/search?spm=1001.2101.3001.4498&q=Kafka%E6%8A%80%E6%9C%AF%E7%9F%A5%E8%AF%86%E6%80%BB%E7%BB%93&t=&u=