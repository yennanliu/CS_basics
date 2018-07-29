# Real-time pipeline for auto QA test 

- Client side event : Click Timeline/transaction/category/ Change category / Back
- Design a system collect as many as real-time scenarios 
- Form of the returned scenarios
- Ways to guarantee the squential integrity of data 
- Describe the DB/data structure/end points ..

# pipeline infra 
```



ios 	---->                                                           ----> S3 ----> EMR ----> BI Tool 
               Event log/ prod MySQL ----> Fronting Kafka  -> Router	----> Elastic Search/ Data Dog 	 
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



```
* 3) 
        ----> S3 ----> EMR ----> BI Tool
Router	----> Elastic Search/ Data Dog 	 
        ----> Consumer Kafka/ Stream Consumer(Spark/APP ...)

```






# event data  

```

timestamp          uid         sid         event platform country 
20170101 07:00:00 u000000001  s000000001  click_timeline ios UK 
...


```








