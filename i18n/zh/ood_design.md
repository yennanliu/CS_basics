<!-- 6a8a778ba4d0 -->
# 物件導向設計（OOD／Low-Level Design）

> **範圍** — 低階設計那一關 — 類別建模、SOLID，以及面試官真的會問的設計模式（strategy、observer、factory、state），附完整的設計範例。
> **另見**：[design.md](./design.md) — 那些以操作複雜度而非類別結構評分的 LC 設計題；[concurrency_patterns.md](./concurrency_patterns.md) — 這些設計裡的執行緒安全問題。

<!-- 73ee7a3b04c7 -->
## LeetCode 題目清單

- [Design](https://leetcode.com/problem-list/design/)

<!-- 20481a8c0946 -->
## 總覽

**OOD（物件導向設計）**，又叫 **LLD（低階設計）**，考的是你能不能把一個真實世界的問題翻譯成一組乾淨的**類別、介面與關係**。評分的重點不在於你寫出一個能從頭跑到尾的程式，而在於**建模、責任切分、可擴充性，以及設計模式的運用**。

<!-- b0f15e998d10 -->
### 關鍵性質
- **考什麼**：類別建模、封裝、SOLID 原則、設計模式的使用，以及你的設計吸收新需求時有多優雅。
- **核心想法**：把需求（名詞 → 物件，動詞 → 方法）變成一張可維護的類別關係圖。
- **什麼時候用**：像「設計一個停車場／電梯／販賣機／紙牌遊戲」這類面試題，或任何「幫 X 設計類別」的問題。

<!-- 5d84a0fc8e72 -->
### OOD（低階）vs 系統設計（高階）

| 面向 | 系統設計（HLD） | OOD／LLD |
|--------|---------------------|-----------|
| 視角高度 | 服務、資料庫、佇列、快取、網路 | 類別、介面、方法、欄位 |
| 關注點 | 可擴展性、可用性、延遲、分片 | 責任歸屬、耦合、內聚、模式 |
| 產出 | 架構圖、API 合約、資料流 | 類別圖 + 關鍵方法簽名 + 程式碼 |
| 規模 | 數百萬使用者、分散式節點 | 單一行程、記憶體中的物件 |
| 典型題目 | 「設計一個短網址服務」 | 「設計停車場的類別」 |

> 交叉參考：**資料結構設計**（LRU、LFU、All-O(1)、Trie、iterator）與**系統層級的程式碼模式**（一致性雜湊、限流器、負載平衡器）請看 [`design.md`](design.md)。這份文件專注在 **OOD 面試的類別建模**，不重複那邊的 LRU/LFU 內容。

<!-- f264a2096229 -->
### 參考資料
- SOLID 原則（Robert C. Martin）
- Gang of Four（GoF）設計模式
- [`design.md`](design.md) — 資料結構與系統程式碼模式
- [`../faq/java/faq_OOP.md`](../faq/java/faq_OOP.md) — OOP 基礎 FAQ

---

<!-- 442b0cdc3b57 -->
## 0) 概念

<!-- e0039080c95c -->
### 0-1) 可重複套用的五步法 ⭐⭐⭐⭐⭐

**任何 OOD 題目**都套這五個步驟。面試時每一步都要講出來。

<!--CODE-->

<!-- cc1a91114ed1 -->
### 0-2) 名詞 → 類別，動詞 → 方法（心智模型）

<!--CODE-->

---

<!-- 0de0d3d03b4b -->
## 1) 通用形式

<!-- 8dd4585f1d7f -->
### 1-1) SOLID 原則 ⭐⭐⭐⭐⭐

OOD 面試中被引用最多的框架。這些一句話的定義要背起來。

| 原則 | 意義 | 它修掉的壞味道 |
|-----------|---------|---------------------|
| **S** — 單一職責 | 一個類別只該有一個改動的理由 | 什麼都做的上帝類別：解析 + 驗證 + 資料庫 + 列印 |
| **O** — 開放封閉 | 對擴充開放，對修改封閉 | 每加一個新型別就得去改那個巨大的 `if/switch` |
| **L** — 里氏替換 | 子型別必須能用在任何用得到其基底型別的地方 | `Square extends Rectangle` 把 `setWidth` 的行為搞壞 |
| **I** — 介面隔離 | 多個小介面 > 一個肥介面 | 逼一個類別去實作那些它只能丟 `UnsupportedOperation` 的方法 |
| **D** — 依賴反轉 | 依賴抽象，不要依賴具體 | 高階模組被寫死綁在具體的 `MySQLDatabase` 上 |

<!--CODE-->

<!-- 6618f44c6057 -->
### 1-2) 關係速查表

| 關係 | UML | 意義 | Java 寫法 |
|--------------|-----|---------|-----------------|
| **is-a** | ▷（空心箭頭） | 繼承／子型別 | `class Car extends Vehicle` / `implements Drivable` |
| **has-a（組合）** | ◆（實心菱形） | 部分不能活得比整體久 | `Engine` 由 `Car` 建立並擁有 |
| **has-a（聚合）** | ◇（空心菱形） | 部分可以獨立存在 | `Team` 持有 `List<Player>`，但球員活得比球隊久 |
| **uses-a（依賴）** | ┄>（虛線） | 短暫使用（參數／區域變數） | 方法把 `Logger` 當參數收進來 |

**經驗法則：組合優先於繼承。** 繼承很僵硬（只能有一個父類、耦合很緊）；組合讓你能在執行期抽換行為，也讓類別保持小。

---

<!-- 1bdc7a3d21d0 -->
## 1-3) OOD 面試的關鍵設計模式 ⭐⭐⭐⭐

要知道**什麼時候**該拿出哪一個，而且要能把骨架畫出來。

<!-- ee71b5146e9d -->
### **Strategy** — 在執行期抽換演算法
**時機**：有多種可互換的行為（計價規則、排序、路由、付款方式）。用來取代一堆分支的 `if/switch`。

<!--CODE-->

<!--CODE-->

<!-- 68e0a7754869 -->
#### **Factory** — 把物件建立集中起來
**時機**：建立邏輯很複雜，或型別要到執行期才決定。呼叫端去問工廠，而不是自己 `new` 出具體類別（支援開放封閉）。

<!--CODE-->

<!-- ec0c26c62e0f -->
#### **Singleton** — 只有一個實例
**時機**：共用的協調者／設定／登錄表（一個停車場、一個 logger）。要準備好談執行緒安全。

<!--CODE-->

<!--CODE-->

<!-- fcb42e50b13c -->
#### **Observer** — 狀態改變時的發布／訂閱
**時機**：很多物件必須對某個物件的變化做出反應（電梯顯示更新、事件通知、UI 監聽器）。

<!--CODE-->

<!-- e997398ddf35 -->
#### **State** — 行為隨內部狀態而變
**時機**：物件有生命週期，**同一個**方法在不同狀態下行為不同（販賣機：NoCoin → HasCoin → Dispensing；電梯：Idle → Moving → DoorOpen）。用來取代到處蔓延的狀態旗標。

<!--CODE-->

<!-- 95c524afaf2a -->
#### **Decorator** — 不靠繼承來加行為
**時機**：可選、可疊加的功能（咖啡 + 牛奶 + 糖；一個附電動車充電的車位）。避免組合爆炸出一堆類別。

<!--CODE-->

<!-- a6512cf1b8fd -->
#### **Adapter** — 讓不相容的介面能一起運作
**時機**：要整合第三方／舊有的類別，但它的介面跟你的程式碼期待的對不上。

<!--CODE-->

**模式選擇速查表：**

| 題目裡的徵兆 | 該拿出 |
|-----------------------|-----------|
| 「它要支援多種計價／付款／排名規則」 | **Strategy** |
| 「依輸入建立不同種類的 X」 | **Factory** |
| 「只有一個共用的控制器／登錄表」 | **Singleton** |
| 「X 改變時，通知所有的 Y」 | **Observer** |
| 「物件在生命週期的每個階段行為都不同」 | **State** |
| 「加上可以互相組合的選配功能」 | **Decorator** |
| 「橋接既有／舊有／第三方介面」 | **Adapter** |

---

<!-- 39f8dea80c61 -->
## 2) 經典 OOD 題目（完整設計）

<!-- 907f99948965 -->
### 2-1) 停車場 ⭐⭐⭐⭐⭐

**需求**
- 多層樓；每層有不同型別的車位（COMPACT、LARGE、MOTORCYCLE、EV）。
- 車輛（Car、Bike、Truck）只能停某些型別的車位。
- 停車 → 發一張**票**；取車 → 算費用。
- 回報每層樓的可用車位。

**核心類別與關係**
<!--CODE-->

**用到的模式**：Singleton（`ParkingLot`）、Strategy（計價）、Factory（建立車輛／車位）、用 enum 表示固定集合。

<!--CODE-->

**可擴充性的討論點**：新的車種 → 加一個 `Vehicle` 子類別（不用動 `Level`／`ParkingLot`）；新的計價方式 → 新的 `PricingStrategy`（開放封閉）。

> **對應的 LC 題**：LC 1603 *Design Parking System* 就是這題被剝到最精簡而誠實的版本（只剩計數器，沒有 `Spot`／`Ticket` 物件）。LC 的解答，以及是哪一條需求逼你回到上面那個完整模型，見 [§6-3](#6-3-worked-bridge--lc-1603-design-parking-system-)。

---

<!-- be4253f351ee -->
### 2-2) 電梯系統 ⭐⭐⭐⭐

**需求**
- 多部電梯、N 層樓。
- 處理外部請求（在某層按上／下）與內部請求（去第 X 層）。
- 由排程器決定哪部電梯去處理某個請求。
- 每部電梯有方向 + 狀態（IDLE、MOVING、DOOR_OPEN）。

**核心類別與關係**
<!--CODE-->

**用到的模式**：Strategy（排程演算法）、State（電梯生命週期）、Observer（顯示器）、enum。

<!--CODE-->

**討論點**：不用動 `ElevatorSystem` 就能把 `NearestCarScheduler` 換成 `LookScheduler`／`ScanScheduler`（Strategy）。顯示器透過 Observer 做出反應。

---

<!-- 070ab10caf76 -->
### 2-3) 一副撲克牌／紙牌遊戲 ⭐⭐⭐⭐

**需求**
- 標準 52 張牌：4 種花色 × 13 個點數。
- 洗牌、發 N 張牌、追蹤剩餘張數。
- 可重複利用的基底，給各種遊戲用（Blackjack、Poker）— 遊戲規則疊在上面。

**核心類別與關係**
<!--CODE-->

**用到的模式**：用 enum 表示固定的值域、組合（`Deck` 擁有一堆 `Card`）、遊戲流程可選用 Template Method、用 Factory 組出一副標準牌。

<!--CODE-->

<!--CODE-->

**討論點**：`Card` 是不可變的（執行緒安全，可以安全地當 map 的 key）。新遊戲繼承 `Game` 並覆寫規則掛勾（Template Method）— 牌堆／手牌的模型原封不動地被重複使用。

---

<!-- 91d5b24c431f -->
### 2-4) 販賣機 ⭐⭐⭐⭐⭐

**需求**
- 商品放在一格格的貨道裡，每格有價格與庫存數量。
- 接受硬幣／紙鈔；追蹤餘額。
- 選商品 → 錢夠且有庫存就出貨 → 找零。
- 要處理：錢不夠、缺貨、取消／退款。

**核心類別與關係**
<!--CODE-->

**用到的模式**：**State**（機器的生命週期 — 這題最突出的模式）、組合（Inventory）、enum（Coin）。

<!--CODE-->

**討論點**：加一個新階段（例如 `MaintenanceState`）就是新增一個實作 `MachineState` 的類別 — 完全不用動既有的狀態。對比之下，用旗標寫成 `if (state == ...)` 的做法會長到無法維護。

---

<!-- a2b27586b856 -->
## 3) 常見陷阱

- **上帝類別**：一個類別包山包海。依責任切開（SRP）。
- **繼承過頭**：類別樹太深／只為了重用程式碼而繼承。優先用組合。
- **到處對型別做 `if/switch`**：這代表你需要多型、Strategy 或 State。
- **貧血模型**：類別只有 getter/setter，沒有行為。行為要跟它的資料放在一起。
- **可變的值物件**：領域值（`Card`、`Money`）能做成不可變就做成不可變。
- **忽略邊界情況**：滿場、庫存空、無效票券、並行存取。
- **過早套模式**：不要在單純類別更清楚的地方硬塞模式。只有在模式真的划算時才把它的名字講出來。
- **跳過需求釐清**：還沒界定範圍就開始寫類別，白白丟掉好拿的分數。
- **沒有講清楚基數**：「一個停車場有樓層」— 一層？多層？要講明白。

---

<!-- 1a9a16aa801c -->
## 4) OOD 面試檢查清單

<!--CODE-->

---

<!-- 59e472bfdf4b -->
## 5) 快速決策表

| 題目關鍵字 | 可能的核心物件 | 可能的模式 |
|----------------|---------------------|-----------------|
| 停車場 | Lot、Level、Spot、Vehicle、Ticket | Singleton、Strategy、Factory |
| 電梯 | System、Elevator、Scheduler、Request | Strategy、State、Observer |
| 撲克牌／紙牌遊戲 | Card、Deck、Hand、Player、Game | Enum、組合、Template Method |
| 販賣機 | Machine、Slot、Product、State、Coin | **State**、組合 |
| 圖書館管理 | Library、Book、Member、Loan、Catalog | Strategy（罰款）、Observer（預約） |
| 西洋棋／棋盤遊戲 | Board、Piece、Move、Player | Strategy（各棋子的走法）、Factory |
| ATM | ATM、Account、Card、Transaction、State | State、Chain of Responsibility（驗證） |

> 偏重**資料結構**的設計題（LRU/LFU 快取、iterator、Trie 搜尋、限流器、一致性雜湊）請看 [`design.md`](design.md)。**OOP 基礎**（封裝、多型、SOLID 深入、interface vs abstract class）請看 [`../faq/java/faq_OOP.md`](../faq/java/faq_OOP.md)。

---

<!-- f64c0bd2eda2 -->
## 6) 用 OOD 的眼光看 LC 設計題 ⭐⭐⭐⭐⭐

一道 LeetCode 的 `Design X` 題，其實就是**類別圖已經直接給你的 OOD 面試**：LC 把類別名稱和公開方法簽名都交到你手上，然後只評分 OOD 面試官會最後才評的那部分 — 內部實作。還握在你手上的三個決定，剛好就是 OOD 的那三個：

1. **狀態** — 哪些欄位（以及哪些輔助類別）是能回答每個方法的最小集合？
2. **結構** — 一定有*某一個*操作比其他都吃緊；挑資料結構的是那個操作，不是「主要」的那個。
3. **不變式** — 一句話寫得完、在兩次呼叫之間永遠成立的規則，每個方法回傳前都必須把它恢復。

> 這一節**只是橋梁**。完整的資料結構實作（LRU/LFU、Trie 內部、堆積、線段樹）在 [`design.md`](design.md)；iterator 型的設計（LC 173／284／341／900）在 [`iterator.md`](iterator.md)；串流型的在 [`streaming_algorithms.md`](streaming_algorithms.md)。

<!-- 42545cea5ca5 -->
### 6-1) 對照表 — 每道 LC 設計題實際上在考什麼 ⭐⭐⭐⭐⭐

| LC | 你要宣告的東西（狀態／輔助類別） | 決定結構的那個操作 | 每個方法都要恢復的不變式 |
|----|-------------------------------------------|---------------------------------------|---------------------------------|
| **380** Insert Delete GetRandom O(1) | `List<Integer> vals` + `Map<Integer,Integer> pos` | `getRandom()` 要 O(1) → 底層得是陣列；`remove()` 要 O(1) → 得有索引表 | `pos[v]` 是 v 在 `vals` 中的真實索引，而且 `vals` 沒有空洞 |
| **381** Insert Delete GetRandom - Duplicates allowed | 同上，但改成 `Map<Integer, Set<Integer>>` | 重複值破壞了一對一的索引表 | 每個值對應到它所有位置構成的那個集合 |
| **297** Serialize and Deserialize Binary Tree | 一個 `Codec` 類別；設計決策在於傳輸**格式** | `deserialize` 必須是 `serialize` 的反函數 | `deserialize(serialize(t))` ≡ `t` — null 標記保住了樹的形狀 |
| **449** Serialize and Deserialize BST | 一樣是 `Codec`，但 BST 的順序讓你可以省掉 null 標記 | BST 性質是你可以拿來用的額外資訊 | 編碼後的串流是某棵 BST 的合法前序 |
| **295** Find Median from Data Stream | 兩個堆積：最大堆 `low`、最小堆 `high` | `findMedian()` 要 O(1) → 中位數必須就坐在某個堆頂 | 兩邊大小最多差 1，**而且** `max(low) ≤ min(high)` |
| **211** Design Add and Search Words | `TrieNode { children, isWord }` | `'.'` 萬用字元 → 搜尋是 DFS／遞迴，不是迴圈 | root→node 這條路徑拼出該前綴；`isWord` 精確標出被插入過的字 |
| **208** Implement Trie (Prefix Tree) | `TrieNode` 輔助類別（貨真價實的 has-a 樹） | `startsWith` → 前綴必須能一個字元一個字元走下去 | 同上 |
| **146** LRU Cache | `Map<K,Node>` + `Node` 雙向鏈結串列類別 | `get`／`put` 都要 O(1) → 要淘汰的對象必須能 O(1) 拿到 | 串列順序 == 使用時間順序；map 的 key 精確對應到現存的節點 |
| **432** All O`one Data Structure | 由相同計數的 key 組成的 `Bucket` 雙向鏈結串列 + `Map<key,Bucket>` | `getMaxKey`／`getMinKey` 要 O(1) → 計數必須維持在排序好的桶裡 | 沿著串列，桶的計數嚴格遞增；每個 key 坐在它自己計數的那個桶裡 |
| **706** Design HashMap / **705** Design HashSet | 每個桶一條 `Node` 鏈 + `Node[] buckets` | 碰撞處理就是這題的全部 | 每個 key 至多一個節點，且在 `hash(key)` 那個桶裡 |
| **981** Time Based Key-Value Store | `Map<String, List<Pair<time,value>>>` | `get(key, t)` = 「不超過 t 的最大時間」→ 二分搜尋 | 每個 key 的串列只會往後追加，且依 timestamp 排序 |
| **355** Design Twitter | 實體類別：`User { id, followees, tweets }`、`Tweet { id, time }` + 全域時鐘 | `getNewsFeed` = 跨所有追蹤對象取最新的前 10 則 → k 路合併 | timestamp 嚴格遞增；使用者也追蹤自己，讓 feed 規則保持一致 |
| **155** Min Stack | 一個放 `(val, minSoFar)` 配對的堆疊（或第二個 min 堆疊） | `getMin()` 要 O(1) → 最小值必須被帶著走，不能現算 | 頂端配對的 `minSoFar` == 堆疊中所有現存元素的最小值 |
| **895** Maximum Frequency Stack | `Map<val,freq>` + `Map<freq, Stack<val>>` + `maxFreq` | `pop()` 在頻率相同時要看**誰比較新** → 每個頻率各一個堆疊 | `group[f]` 依 push 順序放著所有曾經達到頻率 `f` 的值 |
| **729** My Calendar I | 已預訂區間的 `TreeMap<start,end>` | `book()` 需要某個 start 的左右鄰居 → 要有序 map，不是 list | 任兩個已存區間都不重疊 |
| **1146** Snapshot Array | 每個索引一份 `List<(snapId, value)>` + 一個快照計數器 | `get(i, snapId)` → 在該索引的歷史上做二分搜尋 | 每個索引只記錄自己的*變動*，依 snapId 排序 |
| **703** Kth Largest Element in a Stream | 大小上限為 `k` 的最小堆 | `add()` 必須立刻回傳第 k 大 | 堆裡精確地放著看過的最大 k 個；堆頂就是答案 |
| **384** Shuffle an Array | 原封不動的 `original` 陣列**加上**一份工作副本 | `reset()` 必須完全還原 → 原陣列永遠不能被改 | `original` 建構後不再被寫入；`shuffle` 用 Fisher-Yates（均勻） |
| **622** Design Circular Queue / **641** Design Circular Deque | 固定的 `int[]` + `head` + `size` | 容量固定下**兩端**都要 O(1) → 索引對容量取模的算術 | `0 ≤ size ≤ capacity`；元素 `i` 住在 `(head + i) % capacity` |
| **232** Implement Queue using Stacks / **225** Implement Stack using Queues | 兩個堆疊：`in`、`out` | 攤還 O(1) 的 `pop` → 非搬不可時才搬元素 | `out` 依 pop 順序放著最舊的元素；**只在** `out` 空掉時才補滿它 |
| **1603** Design Parking System | 三個計數器（見 §6-3） | `addCar` 只問「還有沒有剩？」→ 不需要身分 | `remaining[t] == capacity[t] - parked(t)`，且永不為負 |

**面試時怎麼讀這張表**：第三欄是你在寫下任何欄位*之前*就該講出來的那句話（「`getRandom` 必須是 O(1)，所以儲存必須是陣列 — 其他都是跟著推出來的」）。第四欄是你寫在欄位上方當註解的那句話；它就是把一堆 map 變成一個設計的東西。

<!-- 5f0f45892776 -->
### 6-2) LC 設計題的四步流程 ⭐⭐⭐⭐⭐

把 §0-1 的五步 OOD 法，壓縮成適用於「API 已經固定」的題目：

<!--CODE-->

**只有當一個輔助類別帶著身分或行為時才引入它。** `Node`、`TrieNode`、`Bucket`、`Tweet` 都對得起它們的位置（它們的狀態活得比單次呼叫久）。一個只是被回傳一次的 tuple 卻硬包成「類別」，那是雜訊 — LC 設計題的評分看的是狀態模型，不是類別數量。

<!-- 4ef484928271 -->
### 6-3) 完整的橋梁範例 — LC 1603 Design Parking System ⭐⭐⭐⭐

這是「需求決定你需要多少 OOD」最乾淨的例證：它就是 §2-1 的停車場，把所有能正當化那些物件的需求全部拿掉之後的樣子。

<!--CODE-->

<!--CODE-->

**現在只加一條需求 — 「車會離場，而且小車可以停大車位」** — 計數器就撐不住那個不變式了（你沒辦法釋放一個從來沒被識別過的「車位」，而且能不能停的規則現在變成一種政策）。這正是 §2-1 那套物件模型開始划算的那一刻：

<!--CODE-->

<!--CODE-->

**能拿分的那句話**：「LC 1603 不需要物件，因為沒有東西有身分，也沒有東西有生命週期。加上 `leave()`，身分就出現了；加上停車位相容規則，Strategy 就出現了；加上計費，§2-1 的 `Ticket` + `PricingStrategy` 就出現了。」能指名是哪一條*需求*創造出每一個類別，整個功力就在這裡。

<!-- b6d72b23a5eb -->
### 6-4) 在 LC 題目內部做實體建模 — LC 355 Design Twitter ⭐⭐⭐⭐

這是唯一一道常見的 LC 設計題，標準答案要的是真正的**實體建模**（而不只是一個資料結構）：

<!--CODE-->

- **為什麼 `Tweet` 要是類別**：依新舊排序需要時間戳，所以光一個 tweet id 承載不了這個狀態 — 是查詢逼出了這個值物件。
- **為什麼全域時鐘放在 `Twitter` 而不是 `User` 上**：「時間戳可以跨使用者比較」這個不變式，任何單一使用者都維護不了。
- **追蹤自己這個技巧**：註冊時做 `follow(u, u)`，`getNewsFeed` 就變成對追蹤對象的一次統一合併，不用特例處理 — 這是為了刪掉一個分支而刻意選的不變式。
- **哪個操作決定結構**：`getNewsFeed`（跨 k 個排序串列取前 10）→ 每個使用者的 tweet 串列保持最新的在最後，再加上 k 路合併（堆積）。`postTweet`／`follow`／`unfollow` 怎麼做都是 O(1)。

> k 路合併本身的實作是資料結構練習 — 見 [`design.md`](design.md) 與 [`heap.md`](heap.md)。OOD 面試官在這題想看的，是上面那張類別圖加上那兩個不變式。

<!-- 6219d9b18f6c -->
### 6-5) 只當參考、值得看一眼的 LC 設計題

同一套功力，沒有新模板 — §6-2 練到變成反射之後，這些是很好的操練材料：

- LC 1797 Design Authentication Manager — `Map<token, expiryTime>`；不變式：一個 token 有效，若且唯若它存的到期時間 > 現在（惰性過期，不要主動掃）。
- LC 2013 Detect Squares — `Map<point, count>`；是 `count()` 查詢（挑一條對角線，推出另外兩個角）決定了「點的多重集」這個狀態。
- LC 2034 Stock Price Fluctuation — `Map<timestamp, price>` + 價格的有序多重集；不變式：多重集裡精確地放著每個時間戳的*當前*價格。
- LC 1352 Product of the Last K Numbers — 前綴乘積串列；不變式：遇到 `0` 就重置串列，讓每個存下來的前綴都不是零。
- LC 707 Design Linked List / LC 1206 Design Skiplist — 純粹的節點類別建模練習。
- LC 715 Range Module / LC 731 My Calendar II / LC 732 My Calendar III — 把 §6-1 中 LC 729 那個不變式（「任兩個已存區間都不重疊」）一步步放寬。
