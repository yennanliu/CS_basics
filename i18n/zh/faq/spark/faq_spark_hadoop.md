<!-- aaf75032c211 -->
# SPARK / HADOOP 生態系 FAQ

0. - Spark 教學
	- https://data-flair.training/blogs/spark-tutorial/

   - Hadoop vs hive vs hbase vs pig vs RDBMS（在 EMR 系統裡）
   	- https://aws.amazon.com/tw/emr/faqs/

1. Spark 和 Hadoop 差在哪？

- ***Hadoop*** 是一套大數據框架，透過 `Map-Reduce` 對大規模資料集做運算，並把資料集切成 `data block`（`HDFS`）存到各個節點（data node）上
	- Data node：存放切開後資料區塊的元件
	- Name node：管理資料存放位置的元件
	- `階段之間不會把資料集留在記憶體裡` —— 每一輪 map/reduce 都經由 HDFS 或本機磁碟來回（它仍然會用記憶體做排序與 shuffle 的緩衝）

- ***Spark*** 同樣是存取大規模資料集的大數據框架，並負責 ETL、串流、機器學習等處理……

	- `可以把資料集跨階段留在記憶體裡`。RDD 是*延遲求值的邏輯*資料集，不是已經被快取的東西 —— 只有在你 `cache()`/`persist()` 之後它才留在記憶體，而放不下的持久化分區會溢寫到磁碟。Spark 通常比較快，是因為它避開了每個階段的磁碟來回、又能把窄轉換融合起來，不是因為所有東西永遠都在記憶體裡

- 簡單說，`Spark` 靠 RDD 與 `DAG`（
  Map-Reduce-Map-Reduce ……）能做更有彈性的資料任務，速度也更快（資料在記憶體裡），但 `Spark` 的工作也`很吃記憶體`。所以如果資料真的是`超大規模`，Spark 未必是好選擇，改用 `Hadoop` 反而合適 —— 它只做 Map-Reduce，理論上記憶體成本只花在存放 key-value pair。

  - http://web.stanford.edu/class/cs246/slides/01-intro.pdf

<p align="center"><img src="../../pic/spark_vs_hadoop.png" width="500" height="300"></p>

1'. 什麼是 `Map-Reduce` 程式模型？

- 它是一個 `Map -> Group by -> Reduce` 的流程模型，用來做可擴展的資料轉換
- 開發者只需要想 `Map` 與 `Reduce` 這兩段怎麼寫，
 `Group by` 那步由執行的機器負責
- Map：把輸入的值對應成 `key-value` pair
- Group by：把 key-value 分派給叢集裡的 worker
- Reduce：依 reduce 函式的邏輯算出該 key-value 的結果

- 優點：
	- 模型簡單
	- 記憶體成本不高
	- 可擴展

- 缺點：
	- Hadoop 的程式碼很長
	- 寫起來費時，沒有彈性的高階 API
	- 做`連續`的 map-reduce 任務時，得一直抓資料又放資料
	- 沒有 RDD 的特性：延遲執行、callback……

<p align="center"><img src="../../pic/map_reduce_overview.png" width="500" height="300"></p>  

<p align="center"><img src="../../pic/map_reduce_diagram.png" width="500" height="300"></p>  

<p align="center"><img src="../../pic/map_reduce_parallel.png" width="500" height="300"></p>  

<p align="center"><img src="../../pic/map_reduce_pattern.png" width="500" height="300"></p>  

2. `spark-submit` 之後發生了什麼事？
	- 步驟 1) 這個程式會呼叫 spark-submit 指令裡指定的 main() 方法，啟動 driver 程式。
	- 步驟 2) Driver 程式把`程式碼轉成`一張`有向無環圖（DAG）`，裡面`包含所有 RDD 與要對它們做的 transformation`。（這個階段 driver 也會做一些最佳化，然後把 DAG 轉成帶有一組 stage 的實體執行計畫。）
	- 步驟 3) 有了實體計畫之後，driver 建立出叫做 task 的小執行單位，接著把這些 task 送到 Spark 叢集。
	- 步驟 4) Driver 程式接著和 cluster manager 溝通，為執行申請資源
	- 步驟 5) Cluster manager 在 worker 節點上啟動 executor
	- 步驟 6) Executor 會向 driver 程式註冊，於是 driver 完全掌握有哪些 executor
	- 步驟 7) Driver 把 task 送給 executor 並開始執行。Driver 會一路監看這些跑在 executor 上的 task，直到工作完成
	- 步驟 8) 工作完成、或因失敗而呼叫 stop() 時，driver 程式終止並釋放配置到的資源

- http://www.bigdatainterview.com/explain-spark-architecture-or-what-happens-when-submit-a-spark-job/


2'. 說明 `spark-submit` 指令？
<!--CODE-->

- https://sparkbyexamples.com/spark/spark-submit-command/

2'' `SparkSession` 與 `SparkContext`？
- SparkContext
	- Spark 的進入點，1.x 起定義在 org.apache.spark 套件裡，用來以程式方式在叢集上建立 Spark RDD、accumulator 與 broadcast 變數。Spark 2.0 之後，SparkContext 裡大部分的功能（方法）在 SparkSession 上也有。它的物件 sc 在 spark-shell 裡預設就有，也可以用 SparkContext 類別以程式方式建立。
- SparkSession
	- 2.0 版引入，是通往底層 Spark 功能的進入點，可以用程式方式建立 Spark RDD、DataFrame 與 DataSet。它的物件 spark 在 spark-shell 裡預設就有，也可以用 SparkSession 的 builder 模式建立。
	- SparkSession 可以取代 SQLContext 與 HiveContext。

- https://sparkbyexamples.com/spark/sparksession-vs-sparkcontext/


3. 什麼是 RDD、HDFS？

- RDD
	- Resilient（彈性）—— 資料掉了可以重新算出來
	- Distributed（分散式）—— 存在叢集的各個節點上
	- Dataset（資料集）—— 初始資料來自某個分散式儲存

- 彈性分散式資料集（RDD）是一個簡單且不可變的分散式物件集合。每個 RDD 被切成多個分區（partition），可以在叢集的不同節點上計算。在 Spark 裡，所有函式都只作用在 RDD 上。

- Spark 整套東西都圍繞著 RDD 這個概念 —— 一個可以平行操作、具備容錯能力的元素集合。

- Hadoop HDFS 與 RDD

	- 在 Hadoop 裡，我們把資料存成區塊放到不同的 data node。在 Spark 裡則不是這樣，我們把 RDD 切成分區存到 worker 節點（datanode）上，並在所有節點上平行計算。

	- 在 Hadoop 裡，我們需要複製資料才能做故障復原；Spark 則不需要複製，因為這件事由 RDD 自己完成。

	- RDD 幫我們載入資料，而且它是有彈性的 —— 意思是它可以被重新計算出來。

	- RDD 有兩種操作：transformation 會從既有的 RDD 產生一個新的資料集；action 則在對資料集做完運算後回傳一個值給 driver 程式。

	- RDD 會記錄 transformation 並定期檢查它們。一旦某個節點掛掉，它可以在其他節點上平行地重建遺失的那個 RDD 分區。


3'. 說明 Spark 的資料分區？

- 分區其實就是把東西切成好幾份。在分散式系統裡談分區，可以定義成：把大的資料集切開，分成多份存在叢集各處。

- Spark 遵循資料局部性原則。Worker 節點會拿離自己比較近的資料來處理。分區做得好，網路 I/O 就會減少，資料也就處理得快得多。

- 在 Spark 裡，co-group、groupBy、groupByKey 之類的操作會需要大量的 I/O。這種情況下，如果做好分區，就能大幅減少 I/O 次數，加快資料處理。

- 要把資料分區，得先把它存起來。Spark 是以 RDD 的形式存放資料的。

- https://acadgild.com/blog/partitioning-in-spark

4. 說明 Spark 的 `master node`、`worker node`、`executor`、`receiver`、`driver`……？

	<p align="center"><img src="../../pic/spark_driver_workder_executor.png" width="500" height="300"></p>

	- Driver
		- 執行你的 `main()` 的那個行程，負責宣告要對 RDD 做哪些 transformation 與 action。它建立 `SparkContext`、向 cluster manager 要 executor、把 RDD 圖轉成 stage 與 task，然後排程它們。在 `client` 模式下它跑在你送出作業的地方；在 `cluster` 模式下由 cluster manager 在叢集的某個節點上啟動它 —— 無論哪一種，它*都不是* master。
		- Driver 是一個行程，不是一個節點 —— 多個 driver 可以共用同一個叢集。
		- Driver 不做運算（filter、map、reduce 等等）。
		- 它是*它自己那個應用*的協調者；叢集的 master／資源管理器是另一個元件，負責把資源配給每一個應用。
		- 當你對一個 RDD 或 Dataset 呼叫 collect() 時，`整份資料`都會被送到 `Driver`。所以呼叫 collect() 要很小心。

	- Master
		- Master 節點負責任務排程與資源分配。

	- Worker
		- `Worker 節點指的是叢集裡任何能執行應用程式碼的節點`。Driver 程式必須監聽並接受來自它的 executor 的連線，而且必須是 worker 節點在網路上找得到的位址。
		- Worker 節點基本上就是`從節點（slave node）`。Master 分派工作，worker 實際執行被分派的任務。`Worker 節點處理存在該節點上的資料，並把資源狀況回報給 master`。Master 再依資源是否可用來排程任務。

	- Executor
		- 當 SparkContext 連上 cluster manager 時，它會在叢集節點上取得 executor。Executor 是執行運算並把資料存在 worker 節點上的 Spark 行程。SparkContext 最後產出的 task 會被送到 executor 去執行。
		- Executor 是跑在 worker 節點上的 JVM。
		- 真正在資料分區上跑 task 的就是這些 JVM。

	- Cluster
		- Cluster 是一群透過網路連起來的 JVM（節點），每一個都跑著 Spark，扮演 Driver 或 Worker 的角色。

5. 說明怎麼處理 `Spark 資料傾斜（data skew）`問題？***

6. 如果某個 spark 節點因為超載而掛掉，該怎麼辦？

7. 一般來說 Spark 為什麼比 MapReduce 快？

8. 說明 Spark 的執行模式：`stand alone`、`Mesos`、`YARN`？

	- Spark stand alone
		- 用 standalone 叢集部署時，cluster manager 就是一個 Spark master 實例。

	- Spark Mesos
		- 用 Mesos 時，Mesos master 取代 Spark master 成為 cluster manager。由 Mesos 決定哪些機器處理哪些任務。因為它在排這些大量短命任務時會把其他框架也考慮進去，多個框架可以共存在同一個叢集上，而不必把資源靜態切開。

	- Spark YARN

9. 說明 Spark 的運算子（operator）？

10. 說明 `cache` 與 `persist`？

11. RDD 裡 `reducebykey` 與 `groupby` 哪個比較好？為什麼？

12. `spark streaming` 可能出哪些錯？怎麼修？

13. 怎麼避免 Spark 的 `記憶體不足` `(OOM)` 問題？
- https://medium.com/swlh/spark-oom-error-closeup-462c7a01709d
- https://dzone.com/articles/common-reasons-your-spark-applications-are-slow-or

13' Spark 裡的 executor 記憶體（heap 記憶體）是怎麼管理的？

14. Spark 怎麼切 `stage`？

15. Spark 的 work、stage、task 是什麼？它們的關係是什麼？

16. 怎麼設定 Spark master 的高可用（HA）？
- 搭配 ZooKeeper 的 Standby Master
- 用本機檔案系統做單節點復原
- https://spark.apache.org/docs/latest/spark-standalone.html#high-availability
- https://support.datafabric.hpe.com/s/article/How-to-enable-High-Availability-on-Spark-with-Zookeeper?language=en_US


17. `spark-submit` 怎麼匯入外部的 `jar`？
- https://sparkbyexamples.com/spark/add-multiple-jars-to-spark-submit-classpath/ 

18. 說明 Spark 的 Polyglot 與 Lazy Evaluation？

- Polyglot（多語言支援）
- Lazy Evaluation（延遲求值）
	- 每個 RDD 都能取得它的父 RDD
	- 第一個 RDD 的父節點值是 NULL
	- 在算出自己的值之前，它一定先算父節點
	- 在出現 `Action` 操作之前，Spark 不會真的執行任務
	- Spark 的 RDD 操作有兩種：
		- Transformation
			- map、groupby、union、filter、flatmap……
		- Action
			- reduce、count、collect、show、saveAsTextFile……

18'. Transformation 有哪幾種？
- 窄轉換（Narrow transformation）
	- 計算`單一分區`裡的紀錄所需要的所有元素，都住在父 RDD 的同一個分區裡
	- `map`、`filter`
- 寬轉換（Wide transformation）
	- 計算單一分區裡的紀錄所需要的元素，可能散在父 RDD 的`很多個分區`裡
	- `groupbyKey`、`reducebykey`

19. 既然 Spark 比 MapReduce 好，學 MapReduce 還有意義嗎？

	- 有。MapReduce 是很多大數據工具（包含 Spark）都在用的典範。資料越來越大時它特別有用。像 Pig 和 Hive 這類工具，大多會把查詢轉成 MapReduce 階段以便做更好的最佳化。

20. Spark 應用裡的 Executor Memory 是什麼？

21. RDD 支援哪些操作？

	- RDD 支援兩種操作：transformation 與 action。

		- Transformation：從既有的 RDD 產生新的 RDD，例如剛才看到的 map、reduceByKey 與 filter。Transformation 是按需執行的，也就是延遲計算。
		例如：`map`、`flatmap`、`reducebykey`、`filter`、`union`、`sample`

		- Action：回傳 RDD 運算的最終結果。Action 會依 lineage 圖觸發執行，把資料載入最初的 RDD、跑完所有中間的 transformation，然後把最終結果回傳給 Driver 程式，或寫到檔案系統。

22. Flatmap 與 map？Reduce 與 ReduceByKey？

23. 舉出幾個使用 Spark 的缺點。

24. 什麼是稀疏向量（Sparse Vector）？

25. 用 Spark 時要怎麼減少資料傳輸？

	- 減少資料傳輸、避免 shuffle，才寫得出跑得快又可靠的 spark 程式。用 Apache Spark 時減少資料傳輸的方式有：

		- 用廣播變數（Broadcast Variable）—— 廣播變數能提升小 RDD 與大 RDD 之間 join 的效率。

		- 用累加器（Accumulator）—— 累加器讓變數的值可以在執行時平行更新。

	最常見的做法是`避開` `ByKey、repartition` 或`任何其他`會觸發 `shuffle` 的操作。

26. 什麼是廣播變數與累加器？

27. 怎麼觸發 Spark 的自動清理，處理累積的中繼資料？

	- 可以設定 `spark.cleaner.ttl` 參數來觸發清理，或是把長時間執行的工作切成不同批次，把中間結果寫到磁碟。

28. 說明 Spark Streaming 裡的快取？

	- DStream 讓開發者可以把串流的資料快取／持久化在記憶體裡。當 DStream 的資料會被計算很多次時特別有用。做法是對 DStream 呼叫 persist() 方法。對於從網路接收資料的輸入串流（例如 Kafka、Flume、Socket 等），預設的持久化層級會把資料複製到兩個節點以達成容錯。

29. Apache Spark 有哪些持久化層級？

	- MEMORY_ONLY：把 RDD 以反序列化的 Java 物件存在 JVM 裡。如果 RDD 放不進記憶體，有些分區就不會被快取，每次需要時再重算。這是預設層級。

	- MEMORY_AND_DISK：把 RDD 以反序列化的 Java 物件存在 JVM 裡。放不下的分區存到磁碟，需要時再從磁碟讀。

	- MEMORY_ONLY_SER：把 RDD 以序列化的 Java 物件存放（每個分區一個 byte array）。

	- MEMORY_AND_DISK_SER：和 MEMORY_ONLY_SER 類似，但放不進記憶體的分區會溢寫到磁碟，而不是每次需要時重算。

	- DISK_ONLY：RDD 的分區只存在磁碟上。OFF_HEAP：和 MEMORY_ONLY_SER 類似，但資料存在堆外記憶體。

30. 說明 `mapPartitions`，它和 map 差在哪？

31. 說明 `broadcast join`，它和一般 join 差在哪？

32. 怎麼用 `partitioner` 來最佳化 Spark？

33. 說明 `combineByKey` 與 `groupByKey`？

例如：
<!--CODE-->

34. 說明 `application`、`job`、`stage`、`task`？

- `Application` -> `job` -> `stage` -> `task`

- Application
	- 初始化一個 SparkContext 就產生一個 Application

- Job
	- 一個 Job 是一連串的 `Stage`，由某個 `Action` 觸發，例如 .count()、foreachRdd()、collect()、read() 或 write()。
	- 一個 `Action` 運算子會產生一個 job

- Stage
	- 一個 Stage 是一連串可以一起跑完、`中間不需要 shuffle` 的 `Task`。
	- 例如：用 .read 從磁碟讀檔案，再跑 .map 與 .filter，全程`不需要 shuffle`，所以可以放進`同一個` stage。

- Task
	- 一個 Task 是在`一個分區`上跑完該 stage 的`整條管線` —— 不是單一個 `.map` 或 `.filter`。stage 裡的窄轉換會被融合，所以先 `.map` 再 `.filter` 是每個分區一個 task，不是兩個。
	- 每個 Task 在 Executor 裡以單一執行緒執行
	- 如果你的資料集有 2 個分區，一次 filter() 之類的操作就會觸發 2 個 Task，每個分區一個。
	- 排程器以 `TaskSet` 的形式送出一個 stage —— 也就是該 stage 的那組 task，每個分區一個。TaskSet 是 stage 被*排程*的方式，不是 stage *本身*。

- 每個 `stage` 裡的 `task` 數量，等於該 `RDD` 的 `分區`數
	- 也就是 partition（RDD 的一部分）-> task（stage 的一部分）

- http://queirozf.com/entries/spark-concepts-overview-clusters-jobs-stages-tasks-etc#stage-vs-task

- https://medium.com/@thejasbabu/spark-under-the-hood-partition-d386aaaa26b7

- https://www.youtube.com/watch?v=pEWrWdt60nY&list=PLmOn9nNkQxJF-qlCCDx9WsdAe6x5hhH77&index=56

<p align="center"><img src="../../pic/spark_stage_task.png" width="500" height="300"></p> 

<p align="center"><img src="../../pic/spark_stage_task2.png" width="500" height="300"></p> 


35. 說明 `shuffle`？

- Shuffle 指的是資料在叢集之間`重新分區`的操作。
- `join` 以及`任何以 ByKey 結尾的操作`都會觸發 Shuffle。
- 它是`昂貴的`操作，因為大量資料得走網路。

36. 說明 `partition`，以及它和 RDD 的關係？
- `partition -> RDD`
- Partition 是你的 RDD/Dataset 在邏輯上的一塊
- 資料被切成分區，讓每個 Executor 各處理一塊，藉此達成平行化。
- 一塊可以由單一個 Executor core 處理。
- 例如：如果你有 4 個資料分區、4 個 executor core，就能一次平行處理完。

<p align="center"><img src="../../pic/rdd_partiiton.png" width="500" height="300"></p> 


37. 說明 Spark 的 `cache`？
- cache 內部其實是呼叫 persist API
- persist 為指定的 RDD 設定特定的儲存層級
- Spark context 會追蹤被持久化的 RDD
- 第一次求值時，分區會被 block manager 放進記憶體


38. 說明 repartition？
	- repartition()：
		- 會在 executor 之間 shuffle 資料，並把資料切成指定數量的分區。但這可能是`昂貴的`操作，因為它在 executor 之間`shuffle` 資料、牽涉`網路流量`。`最理想的分區時機`是在`資料來源`端，也就是取資料的時候。分區做對了可以大幅加速，做錯了則會嚴重拖慢，尤其是因為 Shuffle。
		- 把 RDD 裡的資料`隨機`重新洗牌，產生`更多或更少`的分區並讓資料平均分布。這永遠會把`所有資料都透過網路 shuffle 一次`。
	- repartitionAndSortWithinPartitions
		- 依`給定的 partitioner` 重新分區，並在每個結果分區裡依 key 排序紀錄。這比先 `repartition` 再各自排序`更有效率`，因為它能把排序下推進 shuffle 的機制裡。
	- https://spark.apache.org/docs/latest/rdd-programming-guide.html

39. RDD 的寬／窄依賴？
	- 窄依賴（Narrow dependencies）
		- 當父 RDD 的每個分區最多只被子 RDD 的一個分區用到時，就是窄依賴。這類 transformation 算得相當快，因為不需要在叢集網路上 shuffle 任何資料。
	- 寬依賴（Wide dependencies）
		- 當父 RDD 的每個分區可能被多個子分區依賴（寬依賴）時，計算速度可能受到明顯影響，因為建立新分區時可能得在不同節點之間搬資料。
	- 參考
		- [ref1](https://medium.com/@dvcanton/wide-and-narrow-dependencies-in-apache-spark-21acf2faf031)
		- [ref2](https://www.coursera.org/lecture/scala-spark-big-data/wide-vs-narrow-dependencies-shGAX)
		- [ref3](https://youtu.be/ha6vTXJ9BMQ?t=707)

<p align="center"><img src="../../pic/package_nego.png" width="500" height="300"></p> 

40. Cache/Persist 與 Checkpoint？
	- Cache/Persist
		- 用 StorageLevel.DISK_ONLY 做 persist 或 cache（在 `RAM` 裡）會讓該 RDD 被算出來並存在某個位置，之後再用到這個 RDD 時就不必往回重算它的 lineage。
		- 呼叫 persist 之後，Spark 仍然記得這個 RDD 的 lineage，即使它沒有用到。
		- 應用程式結束後，快取就被清掉、檔案也被銷毀
	- Checkpoint
		- Checkpoint 把 rdd `實體地存到 hdfs`（在`磁碟`上），並銷毀產生它的那條 lineage。
		- Checkpoint 檔案在 Spark 應用結束之後也不會被刪掉。
		- Checkpoint 檔案可以在後續的工作或 driver 程式裡使用
		- 對 RDD 做 checkpoint 會造成雙重計算，因為它會先呼叫一次 cache，才真的去計算並寫進 checkpoint 目錄。
	- [ref](https://stackoverflow.com/questions/35127720/what-is-the-difference-between-spark-checkpoint-and-persist-to-a-disk)

<!-- 9b2b03d82249 -->
## 參考資料
- https://www.edureka.co/blog/interview-questions/top-apache-spark-interview-questions-2016/
- https://data-flair.training/blogs/spark-rdd-operations-transformations-actions/
- https://zhuanlan.zhihu.com/p/47499258
- https://bbs.huaweicloud.com/blogs/326863
