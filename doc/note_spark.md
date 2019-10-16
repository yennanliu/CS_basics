# Note for Apache Spark 

## 1) Initializing Spark 

```scala
//scala
val conf = new SparkConf().setAppName(appName).setMaster(master)
new SparkContext(conf)
```
- The first thing a Spark program must do is to create a `SparkContext` object, which tells Spark `how to access a cluster`. To create a SparkContext you first need to build a `SparkConf` object that contains information about your application.
***Only one SparkContext may be active per JVM***. You must stop() the active SparkContext before creating a new one.

- SparkContext : tell spark how to access a cluster 
- SparkConf    : contains information about spark application
- https://spark.apache.org/docs/latest/rdd-programming-guide.html
- https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.SparkConf
- https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.SparkContext

## 2) Resilient Distributed Datasets (RDDs)

```scala
//scala
val data = Array(1, 2, 3, 4, 5)
val distData = sc.parallelize(data)
```
- Parallelized collections are created by calling `SparkContext’s parallelize` method on an existing collection in your driver program (a Scala Seq). The elements of the collection are copied to form a `distributed dataset` that can be operated on in `parallel`.

