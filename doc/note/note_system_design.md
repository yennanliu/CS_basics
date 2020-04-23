# Note for System design 

## 1) Concept (before starting)

- Scalability (part 1)
	- Vertical scaling
	- Horizontal scaling
	- Caching
	- Load balancing
	- Database replication
	- Database partitioning

- Scalability (part 2)
	- Clones
	- Databases
	- Caches
	- Asynchronism

## 2) Performance vs scalability

## 3) Latency vs throughput

## 4) Availability vs consistency

## 5) Lambda VS Kappa
- Lambda 
	- Stream layer + Batch layer + Service layer
	- Query = λ (Complete data) = λ (live streaming data) * λ (Stored data)
	- Event Sourcing : save whole events 

- Kappa 
	- Query = K (New Data) = K (Live streaming data)
- https://towardsdatascience.com/a-brief-introduction-to-two-data-processing-architectures-lambda-and-kappa-for-big-data-4f35c28005bb
- https://blog.csdn.net/brucesea/article/details/45937875

