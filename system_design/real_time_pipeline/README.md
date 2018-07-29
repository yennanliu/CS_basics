# Real-time pipeline for automative APP QA test 

- Client side event : Click Timeline/transaction/category/ Change category / Back
- Design a system collect as many as real-time scenarios 
- Form of the returned scenarios
- Ways to guarantee the squential integrity of data 
- Describe the DB/data structure/end points ..

# Pipeline Architecture 

```


                                                                        ----> Scenario Generator 
ios 	---->                                                           ----> S3 ----> EMR ----> BI Tool 
               Event log/ prod MySQL ----> Fronting Kafka  -> Router	----> Elasticsearch/ Datadog 	 
android ---->                                                           ----> Consumer Kafka/ Stream Consumer(Spark/APP ...)



  Client side   |       Broker side    (Airflow ETL pipeline )          |  Consumer side 

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





# Event Data  

```javascript
// client side event
{
  "dt": "2017-01-01T07:23:12.236543Z",                // Consistent ISO8601 dates with nanosecond precision
  "level": "info",                                    // The level of the log
  "message": "POST /checkout for 192.321.22.21",      // Human readable message
  "context": { ... },                                 // Context data shared across log line
  "event": "Click_Timeline"                           // Structured representation of this log event
  "uid"  : "u000000001"                               // user id 
  "uuid"  : "43fefe-3452-4322-sc46-vrv54y456452f"     // event id 
}

```

# Prod Data  

```sql 
# transaction table 

timestamp          uid         uuid         amount platform country 
20170101 07:00:00 u000000001  7d529dd4-548b-4258-aa8e-23e34dc8d43d  100 ios UK 
20170101 07:10:00 u000000002  9829c54-4dcw-3587-ei98-435e34d345d54  50 android US 
20170101 07:30:00 u000000001  d32cdsx-zwe3-4990-ced4-76254cwsdv534  230 android UK 
20170101 08:10:00 u000000002  0cew34x-jn42-0312-sc46-vrv54y4564523  170 ios US 
20170101 09:00:00 u000000003  093cwev-0p3d-0445-435y-njy334dce232d  80 ios UK 
...


```








