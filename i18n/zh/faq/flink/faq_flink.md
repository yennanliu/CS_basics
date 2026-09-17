<!-- 0c65b9239306 -->
# FLINK FAQ

<!-- 4b64fa78f682 -->
### 1. 什麼是 Apache Flink？
-  Flink 的核心是一個用 Java 與 Scala 寫的分散式串流資料流引擎。它以資料平行、管線化的方式執行每一個資料流程式。

<!-- 55345da18a37 -->
### 2. 說明 Apache Flink 的架構？
- Flink 是 `Kappa 架構`會建立在上面的那種引擎，但 Kappa 是*應用層*的架構，不是 Flink 的內部構造。Kappa 只保留一條處理路徑 —— 所有東西都以串流進來、由串流引擎處理，批次則視為有界的串流。
- Flink 本身`有界與無界串流都能處理`（見第 4、5 題），所以它能服務 Kappa 風格的設計，但並不被它定義。它自己的架構是下面第 3 題的 JobManager / TaskManager / slot 模型。

- 參考
	- 見下面的說明！！！
	- https://www.shuzhiduo.com/A/amd0NwnXJg/

<!-- 555804150fce -->
### 2' 說明 Apache Flink 的執行圖與它的階段？
- StreamGraph -> JobGraph -> ExecutionGraph -> 實體執行圖

<!-- dcad5af128fb -->
### 3. 說明 Apache Flink 的作業執行架構？

<p align="center"><img src="../../pic/flink-job-exe-architecture.png"></p>

- Program
	- 在 Flink 叢集上執行的那段程式碼。
- Client
	- 客戶端伺服器，接收要執行的程式碼，產生作業的資料流圖，再交給 JM（job manager）。
- Job manager（JM）
	- 從 Client 拿到作業資料流圖之後，負責產生執行圖。它把作業指派給叢集裡的 TaskManager，並監看執行狀況。
	- 週期性地觸發 `Checkpoint`
- Task manager（TM）
	- 負責執行 JobManager 指派給它的所有 task。兩個 TaskManager 會依指定的平行度，在各自的 slot 裡執行 task。它也負責把 task 的狀態回報給 JobManager。

<!-- 352aa782b6e0 -->
###  4. Apache Flink 裡的無界串流（Unbounded streams）是什麼？
- 任何型態的資料都是以事件串流的形式產生的。資料可以當成無界或有界串流來處理。
- 無界串流有起點但沒有終點。它不會結束，資料產生就持續供應。
-  無界串流應該被持續處理，也就是事件一被消費就要處理掉。因為輸入是無界的、任何時間點都不會「完整」，所以不可能等所有資料到齊。

<!-- fa6b389b82f6 -->
### 5. Apache Flink 裡的有界串流（Bounded streams）是什麼？
- 有界串流有起點也有終點。
-  有界串流可以先把所有資料都消費完，再開始計算。
-  處理有界串流不需要有序的攝入，因為有界的資料集永遠可以先排序。處理有界串流也就是所謂的批次處理。

<!-- bd0ab7e0efbd -->
### 6. Apache Flink 的 Dataset API 是什麼？
- Apache Flink 的 Dataset API 用來對一段時間內的資料做`批次`運算。
- 這個 API 有 Java、Scala 與 Python 版本。
- 它可以對資料集做各種轉換，例如過濾、映射、彙總、join 與分組。

<!-- 1f5d51f8f8c1 -->
### 7. Apache Flink 的 DataStream API 是什麼？
- Apache Flink 的 DataStream API 用來處理持續不斷的`串流`資料。
- 在串流資料上可以做過濾、路由、開窗與彙總等操作。
- 這條資料流有多種來源，例如訊息佇列、檔案與 socket 串流；產出的資料也可以寫到不同的 sink，例如命令列終端機。

<!-- b753afc59373 -->
### 8. Apache Flink 的 Table API 是什麼？
- Table API 是一套關聯式 API，語法類似 SQL。
- 它同時支援`批次`與`串流`處理。
- 它和 Java 與 Scala 的 Dataset、Datastream API 相容。
- Table 可以由內部的 Dataset 與 Datastream 產生，也可以來自外部資料源。你可以用這套關聯式 API 做 join、select、彙總與過濾等操作。

<!-- 25bcaf23d113 -->
### 9. Apache Flink 的 FlinkML 是什麼？
- FlinkML 是 Flink 的機器學習（ML）函式庫。

<!-- 3dc90d77cfa1 -->
### 10. Apache Hadoop、Apache Spark 與 Apache Flink 差在哪？

<p align="center"><img src="../../pic/compare_hadoop_spark_flink.png"></p>

<!-- 0e8319ca16e4 -->
### 11. 一個 Flink 串流應用的關鍵程式結構有哪些？
- 一個 Flink 串流應用由四個關鍵結構組成。
	- 1) 串流執行環境 —— 每個 Flink 串流應用都需要一個執行環境。
	- 2) 資料來源 —— Flink 應用從這些應用程式或資料儲存讀入輸入資料。
	- 3) 資料流與轉換操作 —— 來自資料來源的輸入，會以資料流的形式被 Flink 應用讀入。
	- 4) 資料 sink —— 轉換後的資料由 sink 消費，輸出到外部系統。

<!-- 2fdbd831cfcb -->
### 12. 說明`複雜事件處理（Complex Event Processing, CEP）`？
- FlinkCEP 是 Apache Flink 裡的一套 API，用來在持續的串流資料上分析事件模式。這些事件接近即時，具有高吞吐與低延遲。這套 API 最常用在感測器資料上 —— 它們即時湧入，而且處理起來很複雜。
- CEP：用於複雜事件處理。
- https://www.tutorialspoint.com/apache_flink/apache_flink_libraries.htm

<!-- 8aaa6ce1164d -->
### 13. Apache Flink 有哪些特定領域的函式庫？
- FlinkML：機器學習。
- Table：做關聯式運算。
- Gelly：做圖運算。
- CEP：複雜事件處理。
- https://www.cloudduggu.com/flink/interview-questions/

<!-- 11ad64c5156b -->
### 14. Apache Flink 有哪些使用方式？
Apache Flink 可以用下列方式部署與設定。
- 可以在本機（單機）安裝。
- 可以部署在虛擬機上。
- 可以用 Flink 的 Docker image。
- 可以設定並部署成 standalone 叢集。
- 可以部署在 Hadoop YARN 或其他資源管理框架上。
- 可以部署在雲端系統上。

<!-- 2a63ce249591 -->
### 15. 說明 flink 怎麼實作 `exactly once`？邏輯與機制是什麼？
- `分散式快照（distributed snapshot）`
- `兩階段提交（2 phases commit）`
	- TwoPhaseCommitSinkFunction
- 步驟）
	- 步驟 1) 每個 checkpoint，flink 都會開一筆「交易」，把所有資訊加進這筆交易
		- beginTransaction
			- 交易開始前先建一個暫存檔，資料先寫進去
	- 步驟 2) 資料 sink 到外部系統時不直接 commit，而是`預提交（pre-commit）`
		- pre-commit
			- 把記憶體裡的資料「flush」到暫存檔，然後關檔。下一個 checkpoint 重複這一步
	- 步驟 3) flink 收到 checkpoint 的確認之後，才提交這筆交易，資料這時才「真的」寫進外部系統
		- commit
			- 把暫存檔搬到真正的目的路徑。可能會有一點延遲
	- 注意：外部系統本身也要有「交易」機制，才能做到端對端的 exactly once
- 參考
	- https://segmentfault.com/a/1190000022891333
	- https://flink.apache.org/features/2018/03/01/end-to-end-exactly-once-apache-flink.html
	- https://eng.uber.com/real-time-exactly-once-ad-event-processing/
	- https://zhuanlan.zhihu.com/p/266620519

<!-- ccf336f70067 -->
### 16. 說明 flink 的 `savepoint`？
- `savepoint` 是 flink 在某個時間點狀態的「全域備份」
- 用於軟體升級、改設定。可以讓 flink 從上一個 savepoint 重新啟動
- 由使用者觸發
- 會一直保留到使用者刪掉為止
- 可以把 `savepoint` 理解成某個特定時間點、`checkpoint` 的一份`特殊快照`
- 觸發方式
	- `flink savepoint` 指令
	- 取消 flink 作業時用 `flink cancel -s` 指令
	- 透過 REST API：`**/jobs/:jobid /savepoints**`
- 參考
	- https://zhuanlan.zhihu.com/p/79526638

<!-- 51bd852b3e60 -->
### 17. 說明 flink 的 `checkpoint`？
- checkpoint 保存 flink 當下的狀態，是一種「容錯」機制
	- flink 的狀態
		- Operator 狀態：例如 offset
		- KeyedState 狀態：例如 MapState、ListState、ValueState
- 確保 flink 在執行期出錯時能自動復原
- 例子：如果 flink 在 chk-5 失敗，它會試著從 chk-4 復原
	<p align="center"><img src="../../pic/checkpoint1.jpeg"></p>
- 由 flink 管理與操作。使用者只需要設定參數
- flink 自動執行
- 預設 `concurrent = 1` -> 每個 flink 應用同時只會有一個在跑
- 誰參與其中：`JobManager` 的 CheckpointCoordinator、執行這些 task 的 `TaskManager`，以及設定好的 `checkpoint 儲存`（HDFS / S3 / 檔案系統）。ZooKeeper 只在 HA 架構下出現，而且是負責 leader 選舉與 checkpoint 的*中繼資料指標* —— 從來不是存放狀態本身的地方
- 機制
	- JM 週期性地觸發 checkpoint
	- 一個 task 在所有輸入的 barrier 都對齊之後就對自己的狀態做快照、寫進 checkpoint 儲存，然後向 coordinator 回報確認。checkpoint 要等到 `CheckpointCoordinator 收到每一個參與 task 的確認`才算完成 —— 不是 sink 看到 barrier 就算
		- 狀態寫進設定好的 checkpoint 儲存；在 HA 架構下，ZooKeeper 保存的是指向最近一次完成的 checkpoint 的*指標*，讓新的 JobManager 找得到它
		- CheckpointBarrier 是一種特殊事件，會跟著紀錄往下游流動；barrier 抵達 sink 代表那個 operator 可以做快照了，不代表 checkpoint 已完成 —— coordinator 還得把每一份確認都收齊
		- 注意：CheckpointBarrier 的對齊時間也要一併考慮
	<p align="center"><img src="../../pic/checkpoint2.png" alt="Flink checkpoint barrier flowing through the operator chain"></p>
	<p align="center"><img src="../../pic/Checkpoint3.png" alt="Flink checkpoint barrier alignment at a two-input operator"></p>
- `CheckpointCoordinator` 是 checkpoint 運作裡很重要的一個類別
	- 它有以下幾個重要方法
		- triggerCheckpoint
		- triggerSavepoint
		- restoreSavepoint
		- restoreLatestCheckpointedState
		- receiveAcknowledgeMessage
	<p align="center"><img src="../../pic/checkpoint4.png"></p>
	<p align="center"><img src="../../pic/checkpoint5.png"></p>
- 用 `CheckpointConfig` 設定關閉 checkpoint
	- DELETE_ON_CANCELLATION：程式被取消時刪掉 checkpoint
	- RETAIN_ON_CANCELLATION：程式被取消時保留 checkpoint
- checkpoint 失敗的常見原因：
	- 客戶端或外部系統的程式碼沒有處理例外
		- 例如 json 解析錯誤、逾時、sink 系統丟例外
	- 頻繁 GC、記憶體不足 -> 造成 OOM
	- 網路問題、機器問題
- 參考
	- https://tech.youzan.com/flink_checkpoint_mechanism/
	- https://zhuanlan.zhihu.com/p/79526638

<!-- 8c2ee511d4a0 -->
### 17' 說明 flink 的 `Barrier`？
- 一種特殊事件
- 會跟著事件從上游 operator 流到下游 operator
- 每個 operator 在 Barrier 抵達時做快照，並向 CheckpointCoordinator 回報確認；checkpoint 要等到 `coordinator 收齊每一個參與 task 的確認`才算完成，sink 也包含在內

<!-- 981fe053665c -->
### 18. 說明 flink 的`背壓（backpressure）`？
- 這是串流框架裡常見的概念
- 它發生在`「下游」跟不上「上游」的處理速度`時
	- -> 於是有一套機制`往回推`給上游，要它們`慢一點`
- 成因可能是網路、磁碟 I/O、頻繁 GC、資料熱點……或資料傾斜、程式效率、TM 記憶體與它的 GC
- 它也會影響 checkpoint
	- 資料延遲 -> checkpoint 也跟著延遲（變更久）
	- 如果要 exactly once -> 得等延遲的 barrier -> 更多資料被快取 -> checkpoint 變更大
	- 上述都可能讓 checkpoint 失敗，或造成 OOM
- 可以透過 Flink UI 監看（1.13 以上版本）
- 背壓不見得都是問題，有時候它代表系統把資源用滿了。但嚴重的背壓會造成系統延遲

- 參考
	- https://zhuanlan.zhihu.com/p/264637970
	- https://www.modb.pro/db/128767
	- https://zhuanlan.zhihu.com/p/266638799

<!-- e6cc1c6b8c0f -->
### 18'. 說明 flink 的資料交換機制？
- 同一個 Task 內的交換
- 不同 Task、同一個 TM 的交換
- 不同 Task、不同 TM 的交換
- 參考
	- https://zhuanlan.zhihu.com/p/264637970

<!-- 4778ef136314 -->
### 19. 說明 flink 怎麼把作業提交到 yarn？
- 元件
	- ResourceManager（RM）
	- NodeManager（NM）
	- AppMaster（`job manager 跑在同一個 container 裡`）
	- Container（task manager 跑在上面）
- 步驟
	- 步驟 1) flink client 把 `flink jar、應用 jar 與設定檔`上傳到 HDFS
	- 步驟 2) client 把作業提交給 Yarn ResourceManager，並登記資源
	- 步驟 3) ResourceManager 配發 container 並啟動 `AppMaster`，AppMaster 接著載入 jar、設定環境，然後啟動 `job manager`
	- 步驟 4) 上述步驟成功後，AppMaster 就知道 job manager 的 ip
	- 步驟 5) Job Manager 為 task manager 產生一份新的 flink 設定，這份設定也上傳到 HDFS。AppMaster 提供 flink web 服務的端點
		- 注意：Yarn 提供的端點都是暫時性的，使用者可以在 Yarn 上跑多個 flink
	- 步驟 6) AppMaster 向 ResourceManager 要資源。NodeManager 載入 flink jar 並啟動 TaskManager
	- 步驟 7) TaskManager 啟動成功後，對 job manager 送 heartbeat，準備執行任務（job manager 的指令）
- 優點：
	- 「要用才拿」-> 可以提高系統的記憶體使用率
	- 可以依 Yarn 作業的優先序設定，按「優先序」執行工作
	- 可以自動處理`各種角色的 failover`
		- JobManager、TaskManager 出錯……Yarn 都能自動重試／重跑
- 圖
	<p align="center"><img src="../../pic/flink_yarn_1.png"></p>
- 參考
	- https://blog.csdn.net/penriver/article/details/120221565
	- https://blog.csdn.net/lb812913059/article/details/86601150
	- https://blog.51cto.com/u_15478540/4911346
	- https://cloud.tencent.com/developer/article/1708704

<!-- f1d1cf5d9d90 -->
### 20. 說明 flink 的 watermark？
- `watermark` 是一個`時間戳`（事件發生的時間，不是被處理的時間）
- `watermark` 告訴 Flink `從什麼時候起就不必再等遲到的事件了`
- 串流框架用它來「判斷是否還有事件沒到」
- 種類
	- Punctuated Watermark
		- 有「特殊事件」時才產生 watermark
		- 和視窗時間無關，取決於何時收到「特殊事件」
		- 通常用在「真・即時」的場景
	- Periodic Watermark
		- 週期性地產生 watermark
		- 時間間隔可以由使用者設定
- 例子：
	- 「亂序（out-of-order）」
		- `watermark` 的時間戳 > 視窗的 endTime
		- 表示 (window_start_time, window_end_time] 之間還有資料
	- 「遲到元素（late element）」的情況
		- `watermark` 抵達之後 -> 觸發 `window` -> 做運算
- 參考
	- https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/event-time/generating_watermarks/#:~:text=%E6%97%B6%E9%97%B4%E6%A6%82%E8%A7%88%E5%B0%8F%E8%8A%82%E3%80%82-,Watermark%20%E7%AD%96%E7%95%A5%E7%AE%80%E4%BB%8B,%E5%8E%BB%E8%AE%BF%E9%97%AE%2F%E6%8F%90%E5%8F%96%E6%97%B6%E9%97%B4%E6%88%B3%E3%80%82
	- https://www.cnblogs.com/rossiXYZ/p/12286407.html
	- http://chris-liu.cn/Flink-%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0-%E2%80%94%E2%80%94%E2%80%94-WaterMark-%E6%B0%B4%E5%8D%B0.html
	- https://www.gushiciku.cn/pl/pRWT/zh-tw

<!-- cfaa3ab51c75 -->
### 21. 說明 flink 的 EventTime、IngestionTime、ProcessingTime？
- `EvenTime`
	- 「事件發生的時間」。事情真正發生當下最準確的時間
- `IngestionTime`
	- 事件被攝入 Flink 的時間，也就是 source operator 建立它的時間。多數情況下是 Flink job manager 的系統時間
- `ProcessingTime`
	- 事件被處理的時間。是轉換發生當下的時間戳，由 Flink task manager 產生

<!-- 20b66b0b4a54 -->
## 參考資料
- https://www.techgeeknext.com/apache/apache-flink-interview-questions
- https://www.interviewgrid.com/interview_questions/flink
- https://www.tutorialspoint.com/apache_flink/index.htm
