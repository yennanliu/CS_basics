# FAQ : Kafka

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
3. Explain kafka basic data model ?

4. explain kafka master, slaves relation ? regarding data partition, ... ?

5. Steps when a consumer consumes a kafka topic ?

6. Explain kafka's (冪等寫入)
	- Ref
		- https://blog.csdn.net/zc19921215/article/details/108466393#:~:text=Kafka%E5%B9%82%E7%AD%89%E6%80%A7%EF%BC%9A,number%E8%BF%99%E4%B8%A4%E4%B8%AA%E6%A6%82%E5%BF%B5%E3%80%82
		- http://matt33.com/2018/10/24/kafka-idempotent/

## Ref
- https://blog.csdn.net/ajianyingxiaoqinghan/article/details/107171104
- https://so.csdn.net/so/search?spm=1001.2101.3001.4498&q=Kafka%E6%8A%80%E6%9C%AF%E7%9F%A5%E8%AF%86%E6%80%BB%E7%BB%93&t=&u=