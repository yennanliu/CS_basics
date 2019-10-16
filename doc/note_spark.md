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

## 3) RDD Operations

```scala
//scala
val lines = sc.textFile("data.txt")
val lineLengths = lines.map(s => s.length)
val totalLength = lineLengths.reduce((a, b) => a + b)

//persist
lineLengths.persist()
```

RDDs support two types of operations: 
- transformations : create a new dataset from an existing one
- actions : return a value to the driver program after running a computation on the dataset. 
- e.g. 
    - `map` is a transformation that passes each dataset element through a function and returns a new RDD representing the results.

    - `reduce` is an action that aggregates all the elements of the RDD using some function and returns the final result to the driver program (although there is also a parallel `reduceByKey` that returns a `distributed dataset`).

- All transformations in Spark are lazy, in that they do not compute their results right away. Instead, they just remember the transformations applied to some base dataset (e.g. a file). The transformations are only computed when an action requires a result to be returned to the driver program. This design enables Spark to run more efficiently.

- persist 
    - you may also persist an RDD in memory using the persist (or cache) method, in which case Spark will keep the elements around on the cluster for much faster access the next time you query it. There is also support for persisting RDDs on disk, or replicated across multiple nodes.






