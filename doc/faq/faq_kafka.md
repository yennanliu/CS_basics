# FAQ : Kafka

1. explain kafka architecture ?
	- Feature
		- kafka is a `distributed` pub-sub (publish-subscribe) message system
		- developed by Linkedin via scala
		- can work with both online and offlie msg. Data saved on disk with replica -> prevent data lost
	- component
		- broker : kafka cluster has many nodes. The node can also called as `broker`
		- topic : every event on kafka belong to a class, which is "topic"
		- partition : each topic has multiple partition
		- segment : each partition has multiple segment.
			-> Each segment has 2 parts:
				- .index : index file. for finding offset in .log file
				- .log :  file save data
		- producer : msg producer, send msg to kafka broker
		- consumer : msg consumer, each consumer belogs to a specific `consumer group` (we can define consumer's group name)
	- Ref
		- https://www.gushiciku.cn/pl/g6Tu/zh-tw

1. How does kafka implement `exactly once` ?

2. Explain kafka's ACKS ?
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

3. Explain kafka basic data model ?

3. Explain how does kafka save data (low level, file system level) ?
	- File
		- .log : file save data
		- .idnex : file save .log files' index 

4. explain kafka master, slaves relation ? regarding data partition, ... ?

5. Steps when a consumer consumes a kafka topic ?

6. Explain kafka's (冪等性)
	- Ref
		- https://blog.csdn.net/zc19921215/article/details/108466393#:~:text=Kafka%E5%B9%82%E7%AD%89%E6%80%A7%EF%BC%9A,number%E8%BF%99%E4%B8%A4%E4%B8%AA%E6%A6%82%E5%BF%B5%E3%80%82
		- http://matt33.com/2018/10/24/kafka-idempotent/

6. Explain kafka's (事務性)

7. Explain ISR (In-sync replica) ?
	- Ref
		- http://hk.noobyard.com/article/p-azlfvsay-mq.html

## Ref
- https://blog.csdn.net/ajianyingxiaoqinghan/article/details/107171104
- https://so.csdn.net/so/search?spm=1001.2101.3001.4498&q=Kafka%E6%8A%80%E6%9C%AF%E7%9F%A5%E8%AF%86%E6%80%BB%E7%BB%93&t=&u=