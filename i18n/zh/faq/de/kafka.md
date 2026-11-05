<!-- 81e68b25c1a5 -->
# 給資料工程師的 Apache Kafka

> **範圍** —— 資料工程師面試會問到的 Kafka：儲存 log、副本機制、KRaft controller、producer 與 consumer 的內部運作、投遞語意與 transaction、retention 與 compaction、容量規劃、Kafka Connect 與 CDC、Schema Registry、安全性、維運與故障情境；串流處理的語意（視窗、watermark、狀態）屬於串流管線那份文件。
> **另見**：[`../faq_DE_interview.md`](../faq_DE_interview.md) —— 總覽與考前檢查清單；[`./stream_pipeline.md`](./stream_pipeline.md) —— Kafka Streams、Flink 與端到端 exactly-once；[`./schema_design.md`](./schema_design.md) —— 一般性的 schema 演進；[`../kafka/faq_kafka.md`](../kafka/faq_kafka.md) —— 較早、附圖的 Kafka 筆記。

答案以 **Apache Kafka 3.x–4.x** 為準。預設值或功能在版本之間有變動的地方，
會註明是哪個版本。

---

<!-- f35294e42879 -->
## 1) Kafka 是什麼 ⭐⭐⭐⭐⭐

<!-- dfac60d5f694 -->
### 1-1) 一句話的答案

**Kafka 是一個分散式、有副本、只能附加的 commit log：producer 把紀錄附加到 partition，
consumer 用各自的 offset 讀取，而紀錄會保留到 retention 期限為止，不論有沒有人讀過。**

這一句話解釋了 Kafka 大部分的行為：

- **只能附加的 log** → 寫入是循序的，所以很快，而且每筆紀錄都有一個穩定的位置（offset）；
- **consumer 自己掌握 offset** → 可以有很多獨立的讀取者，各自照自己的速度讀，任何一個都能
  倒回去重播；
- **依 retention 保留，而不是讀完就刪** → Kafka 既是系統之間的緩衝，*也是*一個可以用來重建
  狀態的短期事實來源。

<!-- ac7302a8f13f -->
### 1-2) 名詞

| 名詞 | 意義 |
|---|---|
| **Record（紀錄）** | key（可選）、value、headers、timestamp |
| **Topic** | 一個有名字的紀錄串流 |
| **Partition** | 有序、不可變的 log；是平行度的單位，**也是**順序的單位 |
| **Offset** | 紀錄在其 partition 內的位置，單調遞增的 64 位元整數 |
| **Broker** | 一台 Kafka 伺服器；叢集由多台組成 |
| **Replica（副本）** | partition 在某台 broker 上的副本；一個是 **leader**，其他是 **follower** |
| **ISR** | in-sync replicas —— leader 加上跟上進度的 follower |
| **Controller** | 管理中介資料與 leader 選舉的元件（KRaft） |
| **Consumer group** | 一組 consumer，彼此分攤一個 topic 的 partition |

<!--CODE-->

<!-- 13e011e40fd5 -->
### 1-3) 為什麼它很快

**Kafka 很快，是因為它對每筆紀錄做的事很少：循序附加、用 OS 的 page cache 而不是自己的
快取、零拷貝傳給 consumer，以及到處都是批次。**

- **循序 I/O** —— 附加到 segment 檔案的尾端；沒有隨機寫入。
- **Page cache** —— broker 依賴 OS 快取最近寫入的資料，所以跟得上進度的 consumer 通常是
  直接從記憶體讀。
- **零拷貝** —— 在明文連線上，broker 可以用 `sendfile` 把 bytes 從 page cache 直接搬到
  socket，不必經過使用者空間複製（TLS 會讓這招失效，因為 bytes 必須先加密）。
- **批次 + 壓縮** —— producer 送出的是 record batch；batch 原封不動地被儲存與傳送，由
  producer 壓縮一次、由 consumer 解壓縮。
- **分區** —— 負載分散到多台 broker 與多顆磁碟，consumer 也能依 partition 水平擴展。

---

<!-- 93ee0ffb094e -->
## 2) 儲存內部結構 ⭐⭐⭐⭐

<!-- a695cd93b4b0 -->
### 2-1) Partition、segment 與索引

**一個 partition 是一個裝滿 segment 檔案的目錄；只有最新的（active）segment 會被寫入，
而 retention 是以整個舊 segment 為單位刪除或壓實。**

<!--CODE-->

- Segment 在達到 `segment.bytes`（預設 1 GiB）或 `segment.ms`（預設 7 天）時切換（roll）。
- **Offset 索引**是稀疏的：要讀 offset *n*，先在索引裡二分搜尋 ≤ *n* 的最近一筆，再到
  `.log` 檔裡往後掃。
- **Time 索引**回答 `offsetsForTimes` —— 「給我在這個時間點或之後的第一個 offset」，這就是
  從某個時間點重播的方式。
- Retention 永遠不會刪除 active segment，所以低流量的 topic 可能在 segment 切換之前，
  把資料保留超過 `retention.ms`。

<!-- 8a137450e67d -->
### 2-2) Record batch 與 timestamp

**紀錄是以 batch 儲存的，而攜帶 producer id、epoch、序號與壓縮編碼的是 batch，
不是單筆紀錄。**

- `CreateTime`（預設）—— producer 設定的時間；`LogAppendTime` —— broker 用附加的時間
  覆寫它（`message.timestamp.type`）。
- 下游的事件時間處理會使用這個 timestamp，除非 payload 裡自帶時間。
- `max.message.bytes`（topic）/ `message.max.bytes`（broker）限制 batch 的大小 ——
  預設約 1 MB。大型 payload 應該放在物件儲存，Kafka 裡只放指標。

<!-- 988f7d634135 -->
### 2-3) 分層儲存（Tiered storage）

**分層儲存（KIP-405，3.9 起可用於正式環境）把較舊的 segment 移到物件儲存，
所以 retention 不再受本機磁碟大小限制。**

- 本機磁碟保留最近的 segment（`local.retention.ms`）；遠端層保留其餘的（`retention.ms`）。
- Broker 變得更容易替換與重新平衡，因為上面存的資料變少了。
- 讀取舊資料要從遠端層讀，延遲比 page cache 高。

---

<!-- 6adab367558f -->
## 3) 副本機制 ⭐⭐⭐⭐⭐

<!-- d73c7e49a8cc -->
### 3-1) Leader、follower 與 ISR

**每個 partition 有一個 leader 負責所有寫入（預設也負責所有讀取）；follower 像 consumer
一樣從它拉資料，而跟得上的 follower 組成 ISR。**

- Follower 在 `replica.lag.time.max.ms`（3.x 預設 30 秒）內都沒跟上，就會被移出 ISR。
- `min.insync.replicas`（搭配 replication factor 3 時通常設為 2）是 `acks=all` 寫入成功所需
  的最小 ISR 大小；低於它時，producer 會收到 `NotEnoughReplicas`，而不是一筆默默副本不足的寫入。
- **Follower fetching**（KIP-392，`replica.selector.class`）讓 consumer 從最近的副本讀，
  例如同一個可用區，以減少跨區流量。

<!-- 135223cf632b -->
### 3-2) LEO、high watermark 與 leader epoch

**Consumer 只能讀到 high watermark 為止 —— 也就是每個 ISR 成員都有的 offset ——
所以一筆紀錄要複製完才看得到，而 leader 更換也不會曝露出可能遺失的紀錄。**

| 名詞 | 意義 |
|---|---|
| **LEO**（log end offset） | 副本下一個要寫入的 offset |
| **High watermark（HW）** | ISR 中最小的 LEO；低於它的紀錄就是已提交 |
| **Leader epoch** | 每次 leader 更換就加一的計數器；和 offset 一起儲存 |

<!--CODE-->

- Leader 更換後，follower 用 **leader epoch** 找出自己的 log 從哪裡開始和新 leader 分歧，
  並截斷到那個位置（KIP-101）。它取代了原本截斷到 high watermark 的做法，後者在某些
  雙重故障情況下可能遺失或分歧資料。

<!-- bd424eb21837 -->
### 3-3) Unclean leader election

**如果所有 ISR 成員都掛了，Kafka 可以等其中一個回來（一致，預設），或把一個不同步的副本
升為 leader（可用，但已提交的紀錄會遺失）。**

- `unclean.leader.election.enable=false` 是預設 —— 選擇一致性。
- 打開它是一個 CAP 決策：partition 會比較快恢復，但一些已確認的紀錄可能就不見了。
  指標資料可以接受，金流不行。

<!-- 8bfe393fa0f4 -->
### 3-4) `acks=all` 實際保證了什麼

**使用 `acks=all` 時，leader 要等目前每個 ISR 成員都有這筆紀錄才確認；搭配
`min.insync.replicas=2`，代表至少兩台 broker 有它。**

面試時要說出口的前提：

- replication factor ≥ 3、`min.insync.replicas=2`、關閉 unclean leader election；
- 副本分散在不同的機架 / 可用區（`broker.rack`），讓單一故障域無法一次拿走全部；
- 只有在所有同步副本同時遺失時，寫入才會遺失。Kafka 確認寫入時，資料在副本的 page cache
  裡，並沒有 `fsync`；持久性來自跨機器的副本，而不是每次寫入都刷盤。

---

<!-- aa70fc7e14a4 -->
## 4) Controller 與 KRaft ⭐⭐⭐

<!-- 55bc7d272889 -->
### 4-1) Controller 做什麼

**Controller 擁有叢集的中介資料 —— topic、partition、副本分配、ISR、每個 partition 目前的
leader —— 並在 broker 故障時選出新的 leader。**

<!-- 50352136f47e -->
### 4-2) ZooKeeper vs KRaft

**KRaft 用一組 controller 節點組成的 Raft quorum 取代 ZooKeeper，把中介資料存在一個 Kafka
內部 log 裡；它從 3.3 起可用於正式環境，而 Kafka 4.0 已移除 ZooKeeper。**

| | ZooKeeper 模式 | KRaft 模式 |
|---|---|---|
| 中介資料存放處 | 外部的 ZooKeeper 叢集 | `__cluster_metadata` log，由 Raft 複製 |
| Controller | 透過 ZooKeeper 選出的一台 broker | 一組 controller quorum（3 或 5 台） |
| 容錯切換 | 新 controller 要從 ZooKeeper 重新載入所有中介資料 | 待命的 controller 早就有那份 log |
| 維運 | 要跑、要保護兩套系統 | 一套 |
| 狀態 | 4.0 已移除 | 4.0 唯一的模式 |

- Controller 可以跑在專用節點上（大叢集建議如此），也可以和 broker 合在一起
  （`process.roles=broker,controller`，適合小叢集）。
- Broker 從 controller log 拉取中介資料變更，而不是被推送更新，這讓非常大量的 partition
  變得可行。

---

<!-- 33aab895f667 -->
## 5) Producer ⭐⭐⭐⭐⭐

<!-- 855cbd826452 -->
### 5-1) 送出路徑

**`send()` 是非同步的：紀錄被序列化、分配到某個 partition，並附加到記憶體中的 batch；
背景的 sender 執行緒再把準備好的 batch 送到各 partition 的 leader。**

<!--CODE-->

- `batch.size`（每個 partition batch 的 bytes，預設 16 KB）與 `linger.ms`（等多久來填滿一個
  batch；4.0 起預設 5 ms，之前是 0）是在延遲與吞吐量之間取捨。
- `buffer.memory` 滿了 → `send()` 最多阻塞 `max.block.ms`，然後丟出例外。
- `compression.type` —— 常見選擇是 `lz4` 或 `zstd`；壓縮以 batch 為單位，所以 batch 越大
  壓縮得越好。

<!-- d84bf522bb15 -->
### 5-2) 分區

**Key 決定 partition：相同的 key 永遠進同一個 partition，這就是每個 key 有序的由來。**

- 有 key：`murmur2(keyBytes) % numPartitions`。
- 沒有 key：**sticky partitioner**（KIP-480，2.4 起為預設）先填滿某個 partition 的一個
  batch 再換下一個，batch 會比 round-robin 大。
- **增加 partition 會重新對應 key** —— key *k* 可能被移到新的 partition，而它在變更前後的
  順序就斷了。應該在建立時就多預留 partition。
- 自訂 `Partitioner` 是給真正傾斜的 key 用的（把一個熱門租戶分散到多個 partition），
  代價是那個租戶失去順序。

<!-- bda6619867b3 -->
### 5-3) `acks`、重試與順序

| `acks` | Leader 何時回覆 | 已確認的寫入在什麼情況下會遺失 |
|---|---|---|
| `0` | 不等 —— 送了就不管 | 任何故障 |
| `1` | leader 寫入之後 | leader 在 follower 複製之前掛掉 |
| `all` / `-1` | 目前每個 ISR 成員都寫入之後 | 所有同步副本同時遺失（前提見 3-4） |

- `retries` 預設實際上是無限大；`delivery.timeout.ms`（預設 2 分鐘）才是一次送出最多能
  嘗試多久的真正上限。
- 有多個請求同時在途中時，重試可能讓紀錄亂序。開啟冪等性後，只要
  `max.in.flight.requests.per.connection ≤ 5`，順序就能維持。

<!-- ffbced5f50b2 -->
### 5-4) 冪等 producer

**冪等性給每個 producer 一個 producer id、給每個 batch 一個 per-partition 序號，所以 broker
會丟掉已經寫過的重試 batch —— 重試不會產生重複，順序也保住了。**

- `enable.idempotence=true` 從 3.0 起是預設（它需要 `acks=all`）。
- 它只涵蓋**一個 producer session**：重啟的 producer 會拿到新的 producer id，所以跨重啟
  不會去重。那是 `transactional.id` 補上的東西（第 7 節）。
- 它不會替一個對同一個業務事件呼叫兩次 `send()` 的應用程式去重 —— 那需要業務鍵加上冪等的
  consumer。

<!--CODE-->

---

<!-- 0d413ec5c027 -->
## 6) Consumer ⭐⭐⭐⭐⭐

<!-- 5e02d031ed55 -->
### 6-1) Poll 迴圈

**Consumer 是一個 pull 迴圈：`poll()` 從分配給它的 partition 抓紀錄，應用程式處理它們，
然後 consumer 提交已處理完的 offset。**

<!--CODE-->

- `max.poll.records` 限制一批的數量；`max.poll.interval.ms`（預設 5 分鐘）是應用程式在兩次
  poll 之間最久能停多久，超過就被視為死亡，它的 partition 會被重新分配。處理太慢 →
  調高它，或每次 poll 少處理一些紀錄。
- 心跳在背景執行緒上跑（`session.timeout.ms`，3.0 起預設 45 秒）。
- 每筆訊息都提交很慢；正式環境的程式碼會每批或每 N 秒提交一次。

<!-- 5a4c6a3c2ca5 -->
### 6-2) Consumer group 與分配

**在一個 group 裡，每個 partition 只會被一個 consumer 消費，所以 partition 數就是 group
平行度的上限；不同的 group 則各自拿到每一筆紀錄。**

<!--CODE-->

| Assignor | 行為 |
|---|---|
| `RangeAssignor` | 每個 topic 分連續的區段；跨 topic 時可能不平均 |
| `RoundRobinAssignor` | 平均分散 partition；變動時搬動很多 |
| `StickyAssignor` | 平均，且盡量保留既有的分配 |
| `CooperativeStickyAssignor` | sticky **而且**是增量的 —— 只撤回要搬動的 partition |

<!-- 0b5c79f8a54e -->
### 6-3) Rebalance

**成員變動時，rebalance 會重新分配 partition；在傳統的 eager 協定下，每個 consumer 都要
停下來交出所有 partition，這就是面試官會問的「stop-the-world」停頓。**

觸發條件：consumer 加入、離開、當機（錯過 session timeout），或超過 `max.poll.interval.ms`；
訂閱的 topic 的 partition 數變了。

| 協定 | 搬動什麼 | 備註 |
|---|---|---|
| Eager（傳統） | 所有 partition 先撤回，再重新分配 | 整個 group 暫停 |
| Cooperative incremental（2.4+） | 只有換主人的 partition | 數輪短暫的 rebalance，沒有全面停止 |
| Static membership（`group.instance.id`，2.3+） | 快速重啟時什麼都不搬 | 成員在 session timeout 內重啟會保留自己的 id |
| KIP-848 consumer 協定（4.0 GA） | 由 broker 端的 group coordinator 計算 | 沒有整個 group 的同步屏障；consumer 各自調整 |

- 在 Kubernetes 上做滾動部署時用 static membership，這樣 pod 重啟不會讓整個 group 重新洗牌。
- 處理 `on_revoke` / `onPartitionsRevoked`：在 partition 被搬走前，提交已處理的部分並刷出
  狀態，否則下一個主人會重新處理它。

<!-- edcbec3ef720 -->
### 6-4) Offset 管理

**已提交的 offset 存在經過 compaction 的內部 topic `__consumer_offsets`，以 group、topic 與
partition 為 key；提交的值是下一個要讀的 offset。**

| 策略 | 順序 | 當機的結果 |
|---|---|---|
| 處理前提交 | 提交 → 處理 | at-most-once（紀錄遺失） |
| 處理後提交 | 處理 → 提交 | at-least-once（紀錄重播） |
| 自動提交（`enable.auto.commit=true`） | 在 `poll()` 內依計時器提交 | 若處理在下一次 poll 前完成就是 at-least-once，否則可能跳過 |
| Offset 和輸出一起存 | 一個 DB transaction 同時寫入結果與 offset | 對該 sink 而言等同只處理一次 |

- `auto.offset.reset`（`earliest` / `latest` / `none`）只在沒有已提交的 offset 或它已過期時
  才生效（`offsets.retention.minutes`，預設 7 天）。
- 用 CLI 重設或重播一個 group（該 group 必須是非活躍狀態）：

<!--CODE-->

<!-- e8ced37a8240 -->
### 6-5) Consumer lag

**Lag = log end offset − 已提交 offset，以 partition 計；它是第一個該設告警的指標，因為
慢掉或死掉的 consumer 就是從這裡現形。**

- 對**持續成長**的 lag 告警，或對以時間計的 lag（最舊的未處理紀錄有多舊）告警，而不是
  固定筆數 —— 忙碌的 topic 永遠會有一些 lag。
- 修法，依序：找出慢的步驟（某個 DB 呼叫、某則大訊息）；把 consumer 加到 partition 數為止；
  對 sink 批次寫入；增加 partition（會重新對應 key —— 見 5-2）。

---

<!-- 8125965034f4 -->
## 7) 投遞語意與 transaction ⭐⭐⭐⭐⭐

<!-- 6a0570f8d425 -->
### 7-1) 三種語意

**At-least-once 是你設計時的預設；Kafka 內部的 exactly-once 需要冪等 producer 加上
transaction；要 exactly-once 寫進外部系統，則需要冪等或支援交易的 sink。**

| 語意 | 怎麼做 | 故障時的結果 |
|---|---|---|
| At-most-once | 先提交 offset，再處理 | 紀錄遺失 |
| At-least-once | 先處理，再提交 | 紀錄重複 |
| Exactly-once（Kafka → Kafka） | transaction + `isolation.level=read_committed` | 兩者皆無 |
| Effectively-once（Kafka → DB） | at-least-once + 冪等 upsert，或把 offset 放在同一個 DB transaction | 對該 sink 而言兩者皆無 |

<!-- f52203c01477 -->
### 7-2) Transaction 如何運作

**交易式 producer 寫入多個 partition —— 包括 consumer offset topic —— 並以原子方式提交或
中止它們；`read_committed` 的 consumer 永遠看不到被中止的紀錄。**

- `transactional.id` —— 每個 producer 實例一個穩定的 id；重啟時它會**隔離（fence）**舊實例
  （遞增 epoch），讓殭屍實例無法寫入。
- **Transaction coordinator** 把狀態記在內部 topic `__transaction_state`，並在 transaction
  碰過的每個 partition 寫入 commit / abort **標記（marker）**。
- `read_committed` 的 consumer 只讀到 **last stable offset** 為止 —— 也就是任何仍未結束的
  transaction 的第一個 offset —— 所以一個開著不結束的 transaction 會延遲下游的讀取。

<!--CODE-->

<!-- 6f5cfc0c1410 -->
### 7-3) Exactly-once 止於何處

- 它涵蓋的是 **Kafka 進、Kafka 出**。迴圈裡的副作用 —— 一個 HTTP 呼叫、一封 email ——
  仍然可能發生兩次。
- 寫入資料庫：以確定性的 key（`order_id`）做 upsert，或把消費到的 offset 和結果存在同一個
  DB transaction 裡，啟動時 seek 到那個位置。
- Kafka Streams（`processing.guarantee=exactly_once_v2`）與 Flink 的 Kafka sink 都建立在這些
  transaction 之上 —— 見 [`./stream_pipeline.md`](./stream_pipeline.md)。

---

<!-- 2cfa63a45409 -->
## 8) Retention 與 log compaction ⭐⭐⭐⭐

<!-- 3840a8c68e2b -->
### 8-1) Delete retention

**`cleanup.policy=delete` 會在整個已關閉的 segment 超過 `retention.ms`（預設 7 天），或
partition 超過 `retention.bytes`（預設無上限）時把它丟掉。**

- Retention 以 partition 與 segment 為單位 —— 資料是一個 segment 一個 segment 被刪的。
- 磁碟容量估算為 `寫入速率 × retention × replication factor`，再加上餘裕。

<!-- 2aa0e0206f6c -->
### 8-2) Log compaction

**`cleanup.policy=compact` 至少保留每個 key 的最新一筆，所以這個 topic 是一份可以用來重建
表格的 changelog；值為 `null` 的紀錄（tombstone）會刪除該 key。**

<!--CODE-->

- Log cleaner 只壓實非 active 的 segment；active 的前端還不會被壓實。
- Offset 會保留 —— compaction 會留下空隙，consumer 不能假設 offset 是連續的。
- Tombstone 會保留 `delete.retention.ms`（預設 1 天），讓慢的 consumer 也看得到這次刪除。
- `cleanup.policy=compact,delete` 兩者合併：每個 key 留最新的，但不留任何超過 retention 的資料。
- 用於 `__consumer_offsets`、Kafka Streams 的 changelog、CDC topic，以及「每個實體的目前
  狀態」這類 topic。

<!-- 63282c522c63 -->
### 8-3) 順序保證

**Kafka 只在單一 partition 內保證順序；要讓某個實體的事件保持順序，就用實體 id 當 key。**

- 全域順序 → 只用一個 partition，沒有平行度。
- 開啟冪等性且 `max.in.flight.requests.per.connection ≤ 5` 時，順序在重試下仍能保持。
- 增加 partition（key 重新對應），或 consumer 用多個執行緒同時處理同一個 partition 的紀錄時，
  順序就會被打破。

---

<!-- be31a932f093 -->
## 9) Topic 與 partition 的容量規劃 ⭐⭐⭐⭐

<!-- 3fe302e66262 -->
### 9-1) 要幾個 partition

**選擇足以應付尖峰所需 consumer 平行度與吞吐量的 partition 數，並留下餘裕，因為事後增加
partition 會重新對應 key。**

<!--CODE-->

- 每個 partition 的吞吐量要在你自己的硬體與 payload 上量測；它取決於訊息大小、壓縮、
  `acks` 以及 consumer 的處理成本。
- Partition 太多的代價：更多開啟的檔案與副本流量、更久的 leader 選舉、producer 更多記憶體
  （每個 partition 一個 batch 緩衝）。
- 太少：consumer 平行度有一個硬上限。

<!-- 83fe0451ecf2 -->
### 9-2) Replication factor 與擺放

- `replication.factor=3` 搭配 `min.insync.replicas=2` 是常見的正式環境預設：寫入可容忍一台
  broker 掛掉，讀取可容忍兩台。
- 設定 `broker.rack`，讓副本分散在不同機架 / 可用區。
- 新增 broker 時用 `kafka-reassign-partitions.sh`（或 Cruise Control）搬移 partition ——
  新的 broker 自己不會接手任何既有的 partition。

<!-- 64f4654b79f3 -->
### 9-3) Key 的設計

- 選一個**值很多且流量平均**的 key —— `user_id`、`order_id`。
- 熱門 key（一個超大租戶）會壓垮一個 partition；用子鍵把它拆開並接受該租戶失去順序，
  或給它自己的 topic。
- `null` key 會把紀錄分散，但沒有順序。

---

<!-- 1d305cded04e -->
## 10) Kafka Connect 與 CDC ⭐⭐⭐⭐

<!-- a666bc0bf58c -->
### 10-1) Connect 架構

**Kafka Connect 是一個在 Kafka 與其他系統之間搬資料的框架，用設定而不是程式碼：source
connector 寫進 Kafka，sink connector 從 Kafka 讀出。**

| 概念 | 意義 |
|---|---|
| **Worker** | 執行 connector 的 JVM 行程；distributed 模式下組成一個 group |
| **Connector** | 工作定義（例如 JDBC source、S3 sink） |
| **Task** | connector 拆分出來的平行單位 |
| **Converter** | 序列化紀錄 —— JSON、Avro、Protobuf（搭配 Schema Registry） |
| **SMT** | single message transform —— 改名、遮罩、路由某個欄位 |
| **DLQ** | `errors.tolerance=all` + `errors.deadletterqueue.topic.name`，處理 sink 裡的壞紀錄 |

- Distributed 模式把設定、offset 與狀態存在 Kafka topic 裡，worker 死掉時會重新平衡 task。

<!-- d58f3ffe7c76 -->
### 10-2) 用 Debezium 做 CDC

**基於 log 的 CDC 讀取資料庫的複寫 log（MySQL binlog、PostgreSQL WAL），把每一次 insert、
update 與 delete 變成一個事件，完全不用查詢資料表。**

<!--CODE-->

- **先快照再串流**：先對資料表做一次初始快照，再從快照結束時的 log 位置開始讀變更。
- 以主鍵當 key → 同一列的所有變更都在同一個 partition、依序排列；對 topic 做 compaction
  就能保留最新狀態。
- 抓得到**刪除**（以時間戳查詢做不到），而且對來源的負擔比輪詢小。
- 代價：複寫槽（replication slot，PostgreSQL）會一直保留 WAL 直到 Debezium 讀走，所以停掉的
  connector 可能塞滿資料庫的磁碟 —— 要監控它。

<!-- 7a8725d9a21a -->
### 10-3) Outbox 模式

**要原子地發布一個事件並更新資料庫，就在同一個 transaction 裡把事件寫進 `outbox` 表，再讓
CDC 發布它 —— 永遠不要把寫 DB 和寫 Kafka 當成兩個分開的步驟。**

- 兩次分開的寫入會各自失敗：資料列提交了而事件遺失，或反過來。
- Debezium 的 outbox event router 會把 outbox 的資料列轉成正確 topic 上的事件。

---

<!-- b754ead3fa8a -->
## 11) Schema Registry 與相容性 ⭐⭐⭐⭐

<!-- c39402fbed46 -->
### 11-1) 為什麼需要 registry

**Schema registry 為每個 topic 儲存有版本的 schema，讓 producer 只送一個小小的 schema id
而不是整份 schema，同時拒絕會打壞 consumer 的變更。**

<!--CODE-->

- Subject 名稱預設是 `<topic>-key` / `<topic>-value`（`TopicNameStrategy`）。
- Consumer 依 id 抓取寫入者的 schema，再和自己的讀取者 schema 做解析對應。

<!-- 0a357d940e9c -->
### 11-2) 相容性模式

| 模式 | 保證 | 允許的變更（Avro） |
|---|---|---|
| `BACKWARD`（預設） | 新的 consumer 能讀用前一版 schema 寫的資料 | 刪除欄位；新增**有預設值**的欄位 |
| `FORWARD` | 舊的 consumer 能讀用新 schema 寫的資料 | 新增欄位；刪除有預設值的欄位 |
| `FULL` | 兩者皆是 | 只能新增或刪除有預設值的欄位 |
| `*_TRANSITIVE` | 同上，但對照**所有**先前版本，而不只上一版 | |
| `NONE` | 不檢查 | 任何變更 |

- 升級順序跟著模式走：`BACKWARD` → 先升級 consumer；`FORWARD` → 先升級 producer。
- 欄位改名等於刪除加新增；用 alias（Avro），或新增一個欄位並淘汰舊的。
- 一般性的 schema 演進：[`./schema_design.md`](./schema_design.md)。

---

<!-- de2915aeac8a -->
## 12) 一節講完 Kafka Streams ⭐⭐⭐

**Kafka Streams 是一個 Java 函式庫 —— 不是一個叢集 —— 它讀取 topic、用本地狀態處理，
再寫回 topic；要擴展就多跑幾個你的應用程式實例。**

- `KStream`（每一個事件）vs `KTable`（每個 key 的最新值，背後是一個經過 compaction 的 changelog）。
- 狀態存在本地 RocksDB，並備份到 changelog topic；新的實例會從那裡還原（standby replica 讓
  還原更快）。
- 平行度 = 輸入的 partition 數；每個 partition 是一個 task。
- 用 `processing.guarantee=exactly_once_v2` 達成 exactly-once。
- 視窗、join，以及它和 Flink、Spark 的比較：[`./stream_pipeline.md`](./stream_pipeline.md)。

---

<!-- 267e435302a2 -->
## 13) 安全性 ⭐⭐

**用 TLS 加密，用 SASL 或 mTLS 驗證身分，用 ACL 授權 —— 依 principal、依 topic、依操作。**

| 層次 | 選項 |
|---|---|
| 傳輸加密 | TLS listener（會停用零拷貝 `sendfile`） |
| 身分驗證 | mTLS、SASL/SCRAM、SASL/OAUTHBEARER、SASL/GSSAPI（Kerberos） |
| 授權 | ACL：對 topic、group、transactional id 的 `Read` / `Write` / `Describe` / `Create` |
| 靜態資料 | OS / 雲端層級的磁碟加密；Kafka 本身不加密 segment |

<!--CODE-->

---

<!-- 15516b226d1a -->
## 14) 維運與監控 ⭐⭐⭐⭐

<!-- 4458eb263a1e -->
### 14-1) 重要的指標

| 指標 | 健康時 | 不正常時代表 |
|---|---|---|
| `UnderReplicatedPartitions` | 0 | 有 follower 落後或有 broker 掛了 |
| `UnderMinIsrPartitionCount` | 0 | 對那些 partition 的 `acks=all` 寫入正在失敗 |
| `OfflinePartitionsCount` | 0 | 沒有 leader —— partition 無法使用 |
| `ActiveControllerCount` | 全叢集剛好 1 | controller 有問題 |
| Consumer lag（每個 group） | 穩定 | consumer 太慢或停了 |
| 請求延遲（produce / fetch p99） | 穩定 | 磁碟、網路或 GC 壓力 |
| 磁碟用量 | 低於 retention 規劃 | retention 或分層設定錯誤 |

<!-- 5844c04f0bab -->
### 14-2) 例行維運

- **滾動重啟** —— 一次一台 broker；等 `UnderReplicatedPartitions` 回到 0 再換下一台。
- **新增 broker** —— 把 partition 重新分配給它們；限制複製速度（`--throttle`），讓搬移不會
  餓死客戶端流量。
- **Preferred leader election** —— 重啟之後把 leadership 移回去，讓負載平均
  （`auto.leader.rebalance.enable` 會定期做這件事）。
- **Topic 設定變更** —— `kafka-configs.sh --alter`；不需要重啟。

---

<!-- 7cf4e93dbcc2 -->
## 15) 故障情境 ⭐⭐⭐⭐⭐

**面試官會問「當……時會怎樣」—— 回答叢集做了什麼、客戶端看到什麼，以及資料會不會遺失。**

| 情境 | 發生什麼事 | 資料遺失？ |
|---|---|---|
| 一台 follower broker 掛了 | 它離開 ISR；leader 繼續服務 | 不會 |
| 一台 leader broker 掛了 | controller 從 ISR 選出新 leader；客戶端刷新中介資料並重試 | 不會，前提是 `acks=all` 且 ISR 有 2 個以上 |
| ISR 縮到低於 `min.insync.replicas` | `acks=all` 寫入以 `NotEnoughReplicas` 失敗；讀取照常 | 不會 —— 改成拒絕寫入 |
| 所有 ISR 成員都掛了 | partition 離線直到其中一個回來 | 不會，除非開啟 unclean election |
| Producer 送出逾時 | 它會重試；冪等性在同一個 session 內避免重複 | 不會；應用程式要到 `delivery.timeout.ms` 之後才看到錯誤 |
| Consumer 在一批處理到一半時當機 | 它的 partition 被搬走；新主人從上次提交處繼續 | 不會，但未提交的紀錄會被重新處理 |
| Consumer 太慢（`max.poll.interval.ms`） | 被踢出；rebalance；它的提交可能失敗 | 不會，但會有重複與 rebalance 風暴 |
| 毒訊息 | consumer 一再卡在它上面 | 不會，但 partition 卡住了 —— 攔下來送到 dead-letter |
| Broker 磁碟滿了 | broker 關閉受影響的 log 目錄 | 取決於其他地方的副本 |
| 網路分區把 leader 隔離 | controller 選出新 leader；舊 leader 被 leader epoch 隔離 | 已確認的 `acks=all` 寫入不會 |

---

<!-- b9918e66549d -->
## 16) Kafka vs 其他選擇 ⭐⭐⭐⭐

<!-- 355452b5fef3 -->
### 16-1) Kafka vs 訊息佇列

**佇列在訊息被確認後就刪掉；Kafka 保留整個 log，所以多個獨立的 consumer 都能讀，
而且任何一個都能重播。**

| | Kafka | RabbitMQ | Amazon SQS |
|---|---|---|---|
| 模型 | 分區的 log，pull | broker 的佇列與 exchange，push | 託管佇列，pull |
| 消費之後 | 保留到 retention 期限 | 確認後移除 | 刪除後移除 |
| 重播 | 可以 —— 重設 offset | 不行（streams 外掛除外） | 不行 |
| 順序 | 每個 partition 內 | 每個佇列內 | FIFO 佇列依 message group |
| 逐訊息路由 / 優先順序 | 沒有 | 有 | 有限 |
| 強項 | 事件流、fan-out、CDC、重播 | 任務佇列、路由、RPC 式的工作 | serverless、零維運的佇列 |

<!-- 5fbda40e0695 -->
### 16-2) Kafka vs Pulsar vs Kinesis

| | Kafka | Apache Pulsar | Amazon Kinesis Data Streams |
|---|---|---|---|
| 儲存 | broker 擁有 partition（加上分層儲存） | 無狀態 broker + BookKeeper 儲存 | 託管的 shard |
| 擴展單位 | partition | partition / segment | shard |
| 多租戶、跨地域複製 | 靠工具（MirrorMaker 2、Cluster Linking） | 內建 | 跨區域要自己做複製 |
| 佇列語意 | 只有 consumer group（share group，KIP-932，正在 4.x 推出） | 串流**與**佇列訂閱都有 | 沒有 |
| 維運 | 自管或託管（MSK、Confluent Cloud） | 自管或託管 | 全託管，只在 AWS |
| Retention | 可設定，有分層時無上限 | 可設定、分層 | 預設 24 小時，最長 365 天 |

常見的面試答案：選 Kafka 是因為它的生態系（Connect、Streams、Flink、Debezium）與重播能力；
團隊全押 AWS 又不想維運時選 Kinesis；多租戶與跨地域複製是一等需求時選 Pulsar。

---

<!-- 16b84b5940c5 -->
## 17) 快問快答 ⭐⭐⭐⭐

- **問：Kafka 會遺失資料嗎？** —— 會，如果 `acks` 低於 `all`、`min.insync.replicas` 是 1、
  開啟了 unclean leader election，或所有副本同時故障。在 RF 3、`min.insync.replicas=2`、
  `acks=all` 且關閉 unclean election 時，已確認的寫入撐得過任何單一 broker 故障。
- **問：怎麼保證順序？** —— 相同 key → 相同 partition；單一 partition 是有序的。保持冪等性
  開啟，讓重試不會打亂順序。
- **問：為什麼 consumer 不能比 partition 多？** —— 每個 group 中，一個 partition 只分給一個
  consumer，多出來的 consumer 只會閒著。增加 partition，或用另一個 group。
- **問：Exactly-once —— 真的嗎？** —— 對 Kafka 到 Kafka 而言是真的，靠 transaction 加上
  `read_committed`。其他情況就是 at-least-once 加上冪等的 sink。
- **問：Push 還是 pull？** —— Pull。consumer 控制自己的速率、自然地批次處理，還能重播。
- **問：怎麼重播資料？** —— 在 retention 期限內，重設 group 的 offset（`--to-datetime`、
  `--to-earliest`），或開一個新的 group。
- **問：什麼是 rebalance 風暴，怎麼阻止？** —— consumer 一再超過 `max.poll.interval.ms` 或
  一再重啟；修掉處理太慢的問題、調低 `max.poll.records`、使用 cooperative-sticky 分配與
  static membership。
- **問：訊息可以多大？** —— 預設約 1 MB；保持小巧，大型 payload 放在物件儲存，紀錄裡只放指標。
- **問：Consumer offset 存在哪？** —— 在經過 compaction 的 `__consumer_offsets` topic
  （只有非常舊的客戶端才存在 ZooKeeper）。
- **問：怎麼把資料從資料庫搬進 Kafka 而不用雙重寫入？** —— 用 Debezium 做 CDC；應用程式
  自己發出的事件則用 outbox 模式。

---

<!-- eae4c47dbeb0 -->
## 18) 考前檢查清單

- **模型** —— 分區、有副本、只能附加的 log；consumer 自己掌握 offset；依 retention 保留，不是讀完就刪。
- **儲存** —— segment、稀疏的 offset 與 time 索引、長 retention 用分層儲存。
- **副本** —— leader / follower / ISR、high watermark、leader epoch、關閉 unclean election。
- **Producer** —— key → partition、`acks=all` + `min.insync.replicas=2`、冪等性、批次與壓縮。
- **Consumer** —— poll 迴圈、`max.poll.interval.ms`、group 與 assignor、cooperative 與 static membership、KIP-848。
- **語意** —— 處理後提交 = at-least-once；transaction + `read_committed` = Kafka 內部的 exactly-once。
- **Retention** —— delete vs compact、tombstone、順序只在 partition 內。
- **容量規劃** —— 依尖峰平行度決定 partition 數、RF 3、機架感知、增加 partition 會重新對應 key。
- **生態系** —— Connect、Debezium CDC、outbox、Schema Registry 相容性模式。
- **維運** —— 副本不足的 partition、低於 min ISR、離線 partition、consumer lag。
