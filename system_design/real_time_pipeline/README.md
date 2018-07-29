# Real-time pipeline for auto QA test 

- Client side event : Click Timeline/transaction/category/ Change category / Back
- Design a system collect as many as real-time scenarios 
- Form of the returned scenarios
- Ways to guarantee the squential integrity of data 
- Describe the DB/data structure/end points ..

# pipeline infra 
```

Ios     ---------↘
				  Fronting Kafka			 
Android ---------↗


```


# event data  

```

timestamp          uid         sid         event platform country 
20170101 07:00:00 u000000001  s000000001  click_timeline ios UK 
...


```

# Description 

