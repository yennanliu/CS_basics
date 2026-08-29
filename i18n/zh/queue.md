<!-- 52c971d2d84b -->
# Queue 佇列

> **範圍** — FIFO 的基本功：BFS 佇列、雙端佇列、環形緩衝區，以及以佇列為底的設計題。
> **另見**：[monotonic_queue.md](./monotonic_queue.md) — 滑動視窗最大／最小值；[heap.md](./heap.md) — 排序依據是優先級而非到達順序時；[bfs.md](./bfs.md) — 消耗佇列的那個演算法；[stack.md](./stack.md) — LIFO 的對照組。

<!-- e51c32be5f69 -->
## LeetCode 題目清單

- [Queue](https://leetcode.com/problem-list/queue/)

<!-- 0dcc9eb6c676 -->
## 時間複雜度

| 資料結構 | 搜尋 | 插入 | 刪除 | Min/Max |
| -------------- | -------- | -------- | -------- | -------- |
| 佇列 | O(n) | O(1) | O(1) | O(n) |

> 插入 = enqueue（尾端），刪除 = dequeue（前端），兩者都是 **O(1)**——但前提是底層是鏈結串列／雙端佇列；用陣列從前端 dequeue 的天真作法是 **O(n)**。滑動視窗上的 Min/Max 可以用單調雙端佇列做到攤還 **O(1)**（[monotonic_queue.md](./monotonic_queue.md)）。空間是 **O(n)**。

<p align="center"><img src="../pic/queue2.png"></p>

<p align="center"><img src="../pic/stack_vs_queue.png"></p>

<!-- 06b3448a12eb -->
## 總覽
**佇列**是遵守先進先出（FIFO）原則的線性資料結構。元素從尾端加入（enqueue）、從前端移除（dequeue），就像現實生活中排隊一樣。

<!-- 328d78cf555b -->
### 關鍵性質
- **複雜度**：看上面的[時間複雜度](#time-complexity)表
- **核心想法**：最先放進去的元素最先被拿出來（FIFO）
- **什麼時候用**：BFS 走訪、逐層處理、任務排程、緩衝

<!-- 4f69964b97ce -->
### 實作選項
- **陣列版**：固定大小，用環形緩衝區提升效率
- **鏈結串列版**：大小可動態成長，操作有效率
- **雙端佇列**：兩端都能存取
- **優先佇列**：依優先級處理元素（另篇討論）

<!-- 1aee00b6f566 -->
### 參考資料
- [Java Queue Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html)
- [Python collections.deque](https://docs.python.org/3/library/collections.html#collections.deque)
- [Queue vs Stack Comparison](https://www.geeksforgeeks.org/difference-between-stack-and-queue-data-structures/)

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- 005718587a50 -->
### **模式 1：BFS 與層序走訪** — LC 102
- **描述**：在樹與圖上一層一層處理
- **範例題**：LC 102、103、107、199、513、515、637
- **模式**：先把當前這層的節點全部處理完，再進到下一層

<!-- 55f3fdef272e -->
### **模式 2：搭配佇列的滑動視窗** — LC 239
- **描述**：用 FIFO 順序維護視窗狀態
- **範例題**：LC 239、346、362、933、1438
- **模式**：用雙端佇列讓兩端操作都是 O(1)

<!-- 7f894f92f2d5 -->
### **模式 3：設計佇列的各種變形** — LC 232
- **描述**：在特定限制或特殊功能下實作佇列
- **範例題**：LC 225、232、622、641、1670
- **模式**：用堆疊、陣列或鏈結串列，配上特定邏輯

<!-- d6357bfe8395 -->
### **模式 4：單調佇列** — LC 239
- **描述**：在佇列中維持遞增／遞減的順序
- **範例題**：LC 239、862、907、1425、1696
- **模式**：把破壞單調性的元素移掉

<!-- 98193f578570 -->
### **模式 5：串流處理** — LC 346
- **描述**：處理連續進來的資料串流
- **範例題**：LC 346、352、362、703、933
- **模式**：固定大小視窗，或依時間淘汰

<!-- 3cf212040279 -->
### **模式 6：任務排程與模擬** — LC 621
- **描述**：模擬現實世界的排隊系統
- **範例題**：LC 621、1429、1834、2073
- **模式**：在限制條件下依序處理任務

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 7d9b2dcd0d86 -->
### 模板對照表
| 模板類型 | 使用情境 | 實作方式 | 複雜度 | 什麼時候用 |
|---------------|----------|----------------|------------|-------------|
| **基本佇列** | 單純 FIFO | 陣列／LinkedList | 操作 O(1) | 一般佇列操作 |
| **環形佇列** | 固定大小緩衝區 | 陣列加指標 | 操作 O(1) | 有界緩衝區、ring buffer |
| **雙端佇列** | 兩端存取 | 雙向鏈結串列 | 操作 O(1) | 滑動視窗、迴文 |
| **單調佇列** | 維持順序 | 雙端佇列加邏輯 | 攤還 O(1) | 視窗內的最大／最小值 |
| **用堆疊做佇列** | 以堆疊實作佇列 | 兩個堆疊 | 攤還 O(1) | 面試題 |
| **層序 BFS** | 樹／圖走訪 | 佇列 + 記錄層大小 | O(n) | 逐層處理 |

<!-- 3259363f85b8 -->
### 模板 1：基本佇列操作
<!--CODE-->

<!--CODE-->

<!-- e3bcb77aad8f -->
### 模板 2：層序 BFS 模式 — LC 102 ⭐⭐⭐⭐⭐
<!--CODE-->

<!--CODE-->

<!-- aecb7e01fd00 -->
### 模板 3：環形佇列模式 — LC 622 ⭐⭐⭐
<!--CODE-->

<!--CODE-->

<!-- 2a5340079916 -->
### 模板 4：單調佇列模式 — LC 239 ⭐⭐⭐⭐⭐
<!--CODE-->

<!--CODE-->

<!-- b52da435c323 -->
### 模板 5：用堆疊實作佇列的模式 — LC 232 ⭐⭐⭐⭐
<!--CODE-->

<!--CODE-->

<!-- cc74fa062085 -->
### 模板 6：串流處理模式 — LC 346
<!--CODE-->

<!--CODE-->

<!-- a495c039fc3d -->
### 模板 7：攤平成佇列的迭代器模式 — LC 341
> **核心想法**：只要在建構子裡把*巢狀*結構攤平成一個 **FIFO 佇列**，走訪它的迭代器就變得無聊了。`next()` = `popleft()`，`hasNext()` = `佇列非空`。

<!--CODE-->

<!--CODE-->

> **面試追問**：「如果清單超大／無限長呢？」→ 改用**延遲堆疊**版：把 `nestedList` 反轉後推入堆疊，在 `hasNext()` 裡只要頂端是清單就一直彈出並展開。攤還一樣是 O(1)，但額外空間只要 O(深度 + 頂層大小)，而不是 O(N)。

<!-- 64624a6a98c5 -->
### 模板 8：首個唯一元素佇列（佇列 + 計數表）模式 — LC 387
> **核心想法**：維護一個*候選*元素佇列加一張計數表。回答之前，**從前端淘汰**掉所有計數已經超過 1 的候選。此時前端就是第一個仍然唯一的元素。它能**線上**運作（串流），這是「先計數再重掃」的解法做不到的。

<!--CODE-->

<!--CODE-->

> **變形——LC 1429（First Unique Number）**：結構相同，但淘汰迴圈搬到 `showFirstUnique()` 裡，`add()` 只負責更新計數並入列。這就把它變成一題每次呼叫攤還 O(1) 的*設計*題。

<!-- 180039d4b051 -->
### 模板 9：佇列輪轉／模擬模式 — LC 1823
> **核心想法**：題目描述一群人／一疊牌**在隊伍裡循環**時，就照字面模擬：`q.addLast(q.pollFirst())` 轉一步；`q.pollFirst()` 淘汰一個。那個雙端佇列*就是*那個圓圈。

<!--CODE-->

<!--CODE-->

**同一個模擬想法的各種變形：**

- **LC 950（Reveal Cards In Increasing Order）** — *轉折：把流程**倒過來**模擬*。先升冪排序，再從最大走到最小；每一步把一輪反做回去：把後端的牌移到前端，再把新牌推到前端。
- **LC 649（Dota2 Senate）** — *轉折：用兩個佇列而不是一個*。把兩個陣營的索引各排一列；每一輪各彈出一個，索引小的獲勝並以 `index + n` 重新入列（也就是進到下一輪）。

<!--CODE-->

<!--CODE-->

<!-- 1cad2229876a -->
### 模板 10：視窗內生效效果佇列模式 — LC 995
> **核心想法**：當索引 `i` 上的一次操作會影響接下來 `k` 個索引時，不要重複套用 `k` 次。把它的起始索引推進佇列，一旦 `front + k <= i` 就**從前端讓它過期**，然後用 `queue.size()` 告訴你在 `i` 這裡還有幾個效果生效中。（和差分陣列是同一招，只是用佇列來表達。）

<!--CODE-->

<!--CODE-->

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- e72c92b45a78 -->
### 各模式題目表

<!-- 2709c222b2ff -->
#### **BFS 與層序題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Binary Tree Level Order Traversal | 102 | 佇列 + 記錄層 | Medium |
| Binary Tree Zigzag Level Order | 103 | 佇列 + 方向旗標 | Medium |
| Binary Tree Level Order II | 107 | 佇列 + 反轉結果 | Medium |
| Binary Tree Right Side View | 199 | 佇列 + 取每層最後一個 | Medium |
| Find Bottom Left Tree Value | 513 | 佇列 + 追蹤層數 | Medium |
| Find Largest Value in Each Tree Row | 515 | 佇列 + 每層取最大 | Medium |
| Average of Levels in Binary Tree | 637 | 佇列 + 每層求和 | Easy |
| Maximum Width of Binary Tree | 662 | 佇列 + 位置編碼 | Medium |
| Populating Next Right Pointers | 116 | 佇列 + 同層串接 | Medium |
| N-ary Tree Level Order Traversal | 429 | 佇列 + 多個子節點 | Medium |

<!-- e79636242324 -->
#### **滑動視窗佇列題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Sliding Window Maximum | 239 | 單調雙端佇列 | Hard |
| Moving Average from Data Stream | 346 | 固定大小佇列 | Easy |
| Design Hit Counter | 362 | 依時間淘汰 | Medium |
| Number of Recent Calls | 933 | 時間視窗佇列 | Easy |
| Longest Subarray Absolute Diff | 1438 | 兩個雙端佇列（min/max） | Medium |
| Jump Game VI | 1696 | DP + 單調佇列 | Medium |
| Constrained Subsequence Sum | 1425 | DP + 單調佇列 | Hard |

<!-- 4bbe3b2ccf55 -->
#### **佇列設計題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Implement Stack using Queues | 225 | 兩個佇列或輪轉 | Easy |
| Implement Queue using Stacks | 232 | 兩個堆疊 | Easy |
| Design Circular Queue | 622 | 陣列加指標 | Medium |
| Design Circular Deque | 641 | 雙端環形 | Medium |
| Design Front Middle Back Queue | 1670 | 兩個雙端佇列平衡 | Medium |
| Design Most Recently Used Queue | 1756 | 雙端佇列 + set | Medium |

<!-- 448bfffd78ba -->
#### **單調佇列題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Sliding Window Maximum | 239 | 遞減單調 | Hard |
| Shortest Subarray with Sum K | 862 | 前綴和 + 單調 | Hard |
| Sum of Subarray Minimums | 907 | 單調堆疊／佇列 | Medium |
| Maximum Score of Good Subarray | 1793 | 單調邊界 | Hard |
| Jump Game VI | 1696 | DP + 單調佇列 | Medium |
| Longest Continuous Subarray | 1438 | 兩個單調佇列 | Medium |
| Maximum Sum Circular Subarray | 918 | 在加倍陣列上做前綴和 + 雙端佇列（視窗 ≤ n）——見 [monotonic_queue.md](./monotonic_queue.md) | Medium |

<!-- 00abdc041160 -->
#### **迭代器與佇列模擬題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Flatten Nested List Iterator | 341 | 建構子裡攤平成佇列（模板 7） | Medium |
| First Unique Character in a String | 387 | 候選佇列 + 計數表（模板 8） | Easy |
| First Unique Number | 1429 | 和 387 相同，但包成設計／串流題 | Medium |
| Find the Winner of the Circular Game | 1823 | 輪轉淘汰／約瑟夫問題（模板 9） | Medium |
| Reveal Cards In Increasing Order | 950 | 用雙端佇列反向模擬（模板 9） | Medium |
| Dota2 Senate | 649 | 兩個索引佇列，以 `i + n` 重新入列（模板 9） | Medium |
| Minimum Number of K Consecutive Bit Flips | 995 | 視窗內生效效果佇列（模板 10） | Hard |

<!-- 0b3309a988fa -->
#### **串流處理題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Moving Average from Data Stream | 346 | 固定視窗 | Easy |
| Data Stream as Disjoint Intervals | 352 | 區間合併 | Hard |
| Design Hit Counter | 362 | 時間佇列 | Medium |
| Logger Rate Limiter | 359 | 時間視窗 | Easy |
| Number of Recent Calls | 933 | 時間視窗 | Easy |
| Finding MK Average | 1825 | 多個佇列 | Hard |

<!-- 99e9bbe22369 -->
#### **任務排程題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Task Scheduler | 621 | 佇列 + 冷卻時間 | Medium |
| Design a Number Container | 2349 | 每個數字一個佇列 | Medium |
| Time Needed to Buy Tickets | 2073 | 佇列模擬 | Easy |
| Single-Threaded CPU | 1834 | 佇列 + 優先佇列 | Medium |
| Number of Visible People in Queue | 1944 | 單調堆疊 | Hard |

<!-- 45235d06cb2a -->
## 模式選擇策略

<!--CODE-->

<!-- 1b556feb4eba -->
## 基本操作速查

<!-- 72074ec423b5 -->
### Java Queue 與 Deque 操作 ⭐⭐⭐⭐
<!--CODE-->

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- a102096e7869 -->
### 複雜度速查
| 操作 | 陣列佇列 | 鏈結佇列 | 雙端佇列 | 環形佇列 |
|-----------|-------------|--------------|-------|----------------|
| Enqueue | O(1) | O(1) | O(1) | O(1) |
| Dequeue | O(n) | O(1) | O(1) | O(1) |
| 看前端 | O(1) | O(1) | O(1) | O(1) |
| 看尾端 | O(1) | O(1) | O(1) | O(1) |
| 空間 | O(n) | O(n) | O(n) | 固定 O(k) |

<!-- b2ff6e21fbf1 -->
### 模板速查
| 模板 | 模式 | 關鍵程式碼 |
|----------|---------|----------|
| **BFS** | 逐層處理 | `for _ in range(level_size)` |
| **環形** | 固定緩衝區 | `(head + count) % capacity` |
| **單調** | 維持順序 | `while q and q[-1] < val: q.pop()` |
| **兩個堆疊** | 模擬佇列 | `if not out: transfer from in` |
| **滑動** | 追蹤視窗 | `if i >= k-1: result.append()` |
| **串流** | 時間／數量視窗 | `while old: queue.popleft()` |

<!-- c3d0316b51ed -->
### 常見模式與技巧

<!-- 4c8071a20379 -->
#### **逐層處理**
<!--CODE-->

<!-- 5dc1a8f97c25 -->
#### **環形索引計算**
<!--CODE-->

<!-- 297ae3b73d29 -->
#### **維持單調性質**
<!--CODE-->

<!-- d17e4303373c -->
#### **雙堆疊佇列的最佳化**
<!--CODE-->

<!-- 0cbe61e0a52a -->
### 解題步驟

1. **判斷該不該用佇列**
   - 需要 FIFO 處理嗎？
   - 要一層一層走訪嗎？
   - 是有順序的滑動視窗嗎？
   - 是串流處理嗎？

2. **選實作方式**
   - 單純佇列 → deque 或 LinkedList
   - 固定大小 → 環形佇列
   - 兩端都要 → 雙端佇列
   - 有優先級 → 優先佇列（另一種資料結構）

3. **處理邊界情況**
   - 對空佇列做操作
   - 佇列滿了（有界佇列）
   - 只有一個元素
   - 環形佇列的繞回

4. **最佳化操作**
   - 用雙端佇列讓操作維持 O(1)
   - 固定大小就用環形緩衝區
   - 面試題用兩個堆疊
   - 最大／最小查詢用單調結構

<!-- 63911bbed265 -->
### 常見錯誤與提醒

**🚫 常見錯誤：**
- 在 Python 裡用 list.pop(0)（這是 O(n)）
- BFS 時忘了記錄每層的大小
- 環形繞回沒處理好
- 在 Java 裡把 queue.poll() 和 queue.remove() 搞混
- 單調性質沒有正確維持

**✅ 最佳實務：**
- Python 一律用 collections.deque
- Java 用 ArrayDeque 而不是 LinkedList，效能較好
- 層序走訪時明確記下佇列大小
- 滑動視窗要清掉過期元素
- 環形索引用取模運算

<!-- e62a8886d0a7 -->
### 面試提醒

1. **先釐清需求**
   - 固定大小還是可變大小？
   - 需要存取兩端嗎？
   - 需要執行緒安全嗎？
   - 有空間限制嗎？

2. **BFS vs DFS 的取捨**
   - BFS → 最短路徑、層序
   - DFS → 找路徑、回溯
   - BFS 用佇列，DFS 用堆疊

3. **實作選擇**
   - Python：一律優先用 deque
   - Java：用 ArrayDeque 效能較好
   - 有界問題考慮環形佇列

4. **常見追問**
   - 把它做成執行緒安全
   - 處理多生產者／多消費者
   - 換一組限制條件重做
   - 最佳化空間／時間複雜度

<!-- 1a7e8158c86e -->
### 進階技巧

<!-- c90aa55ffd15 -->
#### **無鎖佇列**
- 用在並行程式設計
- 靠 compare-and-swap 操作
- Michael and Scott 演算法

<!-- 0d0d0471a508 -->
#### **優先雙端佇列**
- 結合優先佇列與雙端佇列
- 雙端優先佇列
- 用 interval heap 實作

<!-- d52e0fb0ca9a -->
#### **持久化佇列**
- 帶版本的不可變佇列
- 函數式程式設計風格
- 內部用兩個堆疊

<!-- 566acc50fe43 -->
### 相關主題
- **堆疊**：LIFO vs FIFO 的對照
- **優先佇列**：依順序處理
- **BFS**：佇列的主要應用
- **環形緩衝區**：固定大小的佇列實作
- **生產者－消費者**：佇列的經典應用

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 3b3130337185 -->
### 2-1) Sliding Window Maximum — LC 239
> 維護一個遞減的索引雙端佇列；前端永遠是當前視窗的最大值。

<!--CODE-->

<!-- 455b5d1ba823 -->
### 2-2) Design Circular Queue — LC 622
> 固定大小陣列；記住 head 索引與元素個數；用模運算處理繞回。

<!--CODE-->
