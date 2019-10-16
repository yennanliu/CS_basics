# Note for Apache Spark 

## 1) Initializing Spark 

```scala
val conf = new SparkConf().setAppName(appName).setMaster(master)
new SparkContext(conf)
```
- The first thing a Spark program must do is to create a `SparkContext` object, which tells Spark `how to access a cluster`. To create a SparkContext you first need to build a `SparkConf` object that contains information about your application.
***Only one SparkContext may be active per JVM***. You must stop() the active SparkContext before creating a new one.

- SparkContext : tell spark how to access a cluster 
- SparkConf    : contains information about spark application
- https://spark.apache.org/docs/latest/rdd-programming-guide.html