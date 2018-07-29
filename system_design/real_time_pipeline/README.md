# Real-time pipeline for automative APP QA test 

- Client side event : Click Timeline/transaction/category/ Change category / Back
- Design a system collect as many as real-time scenarios 
- Form of the returned scenarios
- Ways to guarantee the squential integrity of data 
- Describe the DB/data structure/end points ..

# Pipeline infra 

```


                                                                        ----> Scenario Generator 
ios 	---->                                                           ----> S3 ----> EMR ----> BI Tool 
               Event log/ prod MySQL ----> Fronting Kafka  -> Router	----> Elasticsearch/ Datadog 	 
android ---->                                                           ----> Consumer Kafka/ Stream Consumer(Spark/APP ...)



```

# Description 

```
* 1) 
          
ios 	---->                                                         
               Event log/ prod MySQL ----> Fronting Kafka	 
android ----> 


```
* Clinets send event log (streaming) to Kafka 
* Clinets database (MySQL) send operation data (batch) to Kafka
* The reason choose MySQL here is MySQL a easy to use/scalable DB, quick to start, good for storing simple data ; if for complex/detailed data, should use PosgreSQL instead.


```
* 2) 
          
Fronting Kafka  -> Router

```
* Kafka is an open source, distributed messaging system that enables build real-time applications using streaming data. 
* Kafka buffers all data and serves them up to all following subscribers (Router in this pipeline)



```
* 3)    
        ----> Scenario Generator 
        ----> S3 ----> EMR ----> BI Tool
Router	----> Elasticsearch/ Datadog 	 
        ----> Consumer Kafka/ Stream Consumer(Spark/APP ...)

```

* Router manage data came from Kafka and forward to S3 or Elastic Search or Consumer Kafka
* S3 as datalake, storing all data from client side, including relative and non-relative data (schema-on-read)
* EMR (Amazon Elastic MapReduce) runs scale computation on S3 data for reporting/analytics tasks
* Elasticsearch for quick streaming data (event log for example) query. 
* Datadog is for streaming data monitoring.
* Consumer Kafka/ Stream Consumer : stream dataflow to other spark jobs and Stream app Consumer  





# Event data  

```

timestamp          uid         sid         event platform country 
20170101 07:00:00 u000000001  s000000001  click_timeline ios UK 
...


```








