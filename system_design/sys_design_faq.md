## System Design FAQ

### 1) When to use NoSQL ? (vs SQL)
- system design book (GOTOP) P.5
- when NoSQL ?
	- applications need act in `low letency`
	- data is `NOT structure`. i.e. : no relational data
	- need to `serialize and deserialize` on `JSON, XML, YAML` data
	- need to storage large scale data
	- ref
		- https://docs.microsoft.com/zh-tw/dotnet/architecture/cloud-native/relational-vs-nosql-data
		- Cassandra
			- https://dotblogs.com.tw/chris0920/2010/09/21/17835
- when SQL ?

### 2) Vertical VS horizontal scale ?
- system design book (GOTOP) P.5
- Vertical scale
	- AKA `scale up`
	- add more resources (CPU, memory..) to `a single` machine
	- pros:
		- easy in op
		- no need to deal with distributed system
	- cons:
		- hardward limitation (can't add infinite resources to a machine)
		- no high availability : if machine down, all services are down
- Horizontal scale
	- pros
	- cons

### 3) What's is Load balancer (LB) ? how to use it in distributed system ?
- system design book (GOTOP) P.6

### 4) DB read-write strategy
- system design book (GOTOP) P.7
- Master-slave mode
- Master-master mode

### 5) What's CDN (content delivery network) ?
- system design book (GOTOP) P.11, p.14

### 6) What's cache ?
- system design book (GOTOP) P.11

### 7) What's `stateful / stateless` server ?
- system design book (GOTOP) P.18

### 8) What's cache ?
- system design book (GOTOP) P.11

### 9) What's DC (data center) ?
- system design book (GOTOP) P.21

### 10) What's message queue ?
- system design book (GOTOP) P.23

### 11) examples on metric, logging, automation ? what to track ? why ?
- system design book (GOTOP) P.25
- Metrics
	- server (machine) level metric : CPU, memory, disk IO..
	- integrated level metric : all DB layer, cache layer ...
	- business metric : DAU, retention, income (e.g. GMV) ...

### 12) DB scaling out strategy ?
- system design book (GOTOP) P.27
