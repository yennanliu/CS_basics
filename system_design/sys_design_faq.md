## System Design FAQ

### 1) When to use NoSQL ? (vs SQL)
- system design book (GOTOP) P.5
- when NoSQL ?
	- applications need act in `low letency`
	- data is `NOT structure`. i.e. : no relational data
	- need to `serialize and deserialize` on `JSON, XML, YAML` data
	- need to storage large scale data
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

### 4)