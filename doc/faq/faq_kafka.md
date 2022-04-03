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
		- every event on kafka belong to a class, which is "topic"
		- each topic for different data source (feeds of messages)
		- can have unlimit topics
		- producer-subscriber use "topic" as basic unit. Can split further via topic partition
	- `Partition`
		- each topic has multiple partition
		- NOTE : same broker can have multiple partition
				-> broker count has NO relation to partition count
		- each topic has a partition id. Start from 0
		- NOTE!!! : 
			- data in each partition can be ordering. But CAN'T guarantee global data (all data in topic) is ordering
			- ordering : keep the same ordering in producer' data and data read by consumer
		- partition defines the MAX "con-current" consumer in the same consumer group
		<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/partition1.png">
		<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/partition2.png">
	- `Segment`
		- each partition has multiple segment.
			- Each segment has 2 parts:
				- .index : index file. for finding offset in .log file
				- .log :  file save data
	- `Producer`
		- msg producer, send msg to kafka broker
	- `Consumer`
		- msg consumer (client), read msg from kafka
		- consumer MUST belong to a consumer group
	- `Consumer group`
		- each consumer belogs to a specific `consumer group` (we can define consumer's group name)
		- SAME consumer in SAME consunmer group ONLY consume same msg ONCE
		- each consumer has a ID (group ID). All consumers can subscribe all partition under a topic
		- each partition CAN ONLY be consumed by A consumer under a consumer group
- Pics
	<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/kafka_architecture1.png">

- Ref
	- https://www.gushiciku.cn/pl/g6Tu/zh-tw

### 2) How does kafka implement `exactly once` ?

### 3) Explain kafka's ACKS ?
- `request.required.acks` : how to acknowledge when kafka writes producers' messgage to its (kafka) copy
- it's a tradeoff between efficiency (response speed) and reliability (fault tolerance) 
- cases
	- ack = 1 (default): 
		- when producer -> kafka broker. if kafka leader comfirms msg received successfully -> success.but will lost data if leader down before followers sync
	- ack = 0
		- whenever roducer -> kafka broker, mark it as success. Highest speed, but will lost data if broker down
	- ack = -1 (or all)
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

### 10) Explain ISR (In-sync replica) ?
- Ref
	- http://hk.noobyard.com/article/p-azlfvsay-mq.html

### 11) Describe kafka limitation ?
- auto scale : it's hard to scale down if scale out first (modify partition data in topics)
- keep publish event in `global ordering`

## Ref
- https://blog.csdn.net/ajianyingxiaoqinghan/article/details/107171104
- https://so.csdn.net/so/search?spm=1001.2101.3001.4498&q=Kafka%E6%8A%80%E6%9C%AF%E7%9F%A5%E8%AF%86%E6%80%BB%E7%BB%93&t=&u=