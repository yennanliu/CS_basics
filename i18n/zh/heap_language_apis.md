<!-- 478f5b13f7fe -->
# 堆積的語言 API — `heapq` 與 `PriorityQueue`

> **範圍** — 堆積在語言層面的機械細節：`heapq` 和 `PriorityQueue` 你會用到的每個呼叫、怎麼假造出最大堆積、怎麼在不 pop 的情況下偷看堆頂，以及「只有部分有序」的容器會帶來哪些陷阱；用到這些 API 的演算法本身，放在其他堆積的表單裡。
> **另見** — *母表單*：[heap.md](./heap.md) — 標準的堆積模板與模式選擇。*從同一份檔案拆出來的兄弟表單*：[heap_advanced.md](./heap_advanced.md) — 延遲刪除、反悔貪婪與其他比較少見的模板；[heap_examples.md](./heap_examples.md) — LC 詳解題庫。*相鄰表單*：[Collection.md](./Collection.md) — 怎麼在 Java 各種 collection 之間做選擇；[sort.md](./sort.md) — 放在排序脈絡下看的堆積排序。

<!-- b869f594d6e2 -->
## LeetCode 題目清單

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

<!-- 823305ea640e -->
## 概觀

Python 的 `heapq` 和 Java 的 `PriorityQueue` 都是**二元最小堆積**。其他東西 — 最大堆積、
自訂排序、peek、延遲刪除 — 全都是從這個原始操作堆出來的。面試中大部分的堆積 bug，其實是
API 用錯，不是演算法想錯。

<!-- 21c2391c5d49 -->
### 關鍵性質
- **複雜度**：`push` / `pop` 是 O(log N)；`peek` 是 O(1)；把既有 list 做 `heapify` 是 O(N)
- **核心想法**：只有最小堆積 — 最大堆積就是把 key 取負號後的最小堆積（Python），或是把
  comparator 反過來（Java）
- **什麼時候用**：先讀一遍，之後再回來查 peek／最大堆積／自訂 comparator 的寫法

<!-- 7f6f6e6f1e13 -->
### 參考資料
- [Python heapq documentation](https://docs.python.org/3/library/heapq.html)
- [Java PriorityQueue](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html)
- [Priority Queue Implementation Notes](https://docs.python.org/3/library/heapq.html#priority-queue-implementation-notes)

<!-- 4a09619a4d5d -->
## Python `heapq`

<!-- 679dab35c4da -->
### API 速查
- 注意 :
    - 在 Python 裡，heapq 是 `MIN heap`
        - 如果要最大堆積，可以用 `-1 * val`
            - LC 1492
    - 在 Python 的實作中，`index start from 0`
    - `pop()` 會回傳 `min` 元素（不是最大的那個）
    - 建堆積的兩種方式（Python）
        - heappush(heap, num)
        - heapify(array)
    - 複雜度
        - push/pop（各自）
            - time : O(log(N))
            - space : O(N)
            - ref : [SF - whats-the-time-complexity-of-functions-in-heapq-library](https://stackoverflow.com/questions/38806202/whats-the-time-complexity-of-functions-in-heapq-library#:~:text=heapq%20is%20a%20binary%20heap,O(n%20log%20n))
        - 所以如果對所有元素都做一次 push/pop，成本是
            - time : O(N log(N))
            - space : O(N)
- 基本 API
    - heapify : 把 list 轉成堆積
    - heappush : 把元素放進堆積
    - heappop  : 取出（並移除）堆頂元素
        - Min heap : 刪掉最小堆積的頂端元素
        - Max heap : 刪掉最大堆積的頂端元素
    - heappushpop : 先 heappush 再 heappop（先放，再取）
    - heapreplace : 先 heappop 再 heappush（先取，再放）
    - nlargest : 回傳最大的 N 個元素
    - nsmallest : 回傳最小的 N 個元素
- 參考
    - https://docs.python.org/zh-tw/3/library/heapq.html
    - https://ithelp.ithome.com.tw/articles/10247299
    - https://cloud.tencent.com/developer/article/1794191#:~:text=heapq%20%E5%BA%93%E6%98%AFPython%E6%A0%87%E5%87%86,%E7%AD%89%E4%BA%8E)%E5%AE%83%E7%9A%84%E5%AD%90%E8%8A%82%E7%82%B9%E3%80%82
    - https://python.plainenglish.io/python-for-interviewing-an-overview-of-the-core-data-structures-666abdf8b698

<!--CODE-->

<!-- c3d42739f02a -->
### Peek：不 pop 就拿到堆頂元素 ⭐⭐⭐⭐⭐

**核心想法**：Python 的 `heapq` **沒有 `peek()` 這個函式** — 堆積本身就是一個普通的 `list`，
而堆積性質保證最小值一定在索引 `0`。所以 **`pq[0]` 就是 peek**，而且是 `O(1)`。

<!-- 64b26019e159 -->
#### **peek 的幾種寫法（Python）**

| 寫法 | 時間 | 評價 |
|-----|------|-----|
| `pq[0]` | O(1) | ✅ **道地寫法** |
| `heapq.nsmallest(1, pq)[0]` | O(n) | ❌ 掃過整個 list，完全沒用到堆積結構 |
| `min(pq)` | O(n) | ❌ 同樣的問題 |
| `pq.queue[0]` | O(1) | 只適用 `queue.PriorityQueue`（list 加鎖的包裝，執行緒安全但比較慢） |

<!--CODE-->

<!-- 8f0e8d56a9dc -->
#### **⚠️ 容易踩到的坑**

<!--CODE-->

<!-- 54bf18f22c9a -->
#### **經典用法：延遲刪除（peek → 丟掉過期的堆頂）**

會用到 peek 最常見的理由就是**延遲刪除** — 你永遠不從堆積中間移除過期的項目（heapq 也做不到），
而是等它浮到頂端時再 pop 掉。

<!--CODE-->

<!-- eb20afaa7426 -->
#### **不需要另外 peek 的操作**

如果你本來就打算*換掉*堆頂，下面這些操作一次下沉就搞定，不用做兩次：

<!--CODE-->

<!-- b187bf81b993 -->
#### **Java 的對應寫法**

<!--CODE-->

| | Python `heapq` | Java `PriorityQueue` |
|---|---|---|
| Peek | `pq[0]` | `pq.peek()` |
| 空的時候 | `IndexError` | `peek()` → `null`，`element()` → 丟例外 |
| 判斷是否為空 | `if pq:` | `pq.isEmpty()` |
| 最大堆積的 peek | `-pq[0]`（push 時取負號） | `pq.peek()` 搭配 `Collections.reverseOrder()` |

<!-- 5fc3273fdd8a -->
### 堆積排序
<!--CODE-->

<!-- 2d6e98c9100a -->
## Java `PriorityQueue`

<!-- 09a827cf6d12 -->
### 操作
<!--CODE-->

<!-- b2ae09af3a65 -->
### 排序示範
<!--CODE-->

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 9363f579ddbb -->
### Design Twitter — LC 355

時間軸本質上是對所有追蹤對象的推文清單做**k 路合併**（每一份都已經是最新在前），
所以用 `heapq.merge` 就能拿到最新的 10 則貼文，不必把每份清單都展開。

<!--CODE-->

<!-- c60aba944554 -->
## 總結與速查

| 操作 | Python `heapq` | Java `PriorityQueue` |
|---|---|---|
| 建最小堆積 | `h = []` | `new PriorityQueue<>()` |
| 建最大堆積 | key 取負號：`heappush(h, -v)` | `new PriorityQueue<>(Collections.reverseOrder())` |
| 從 list 建堆積 | `heapq.heapify(lst)` — O(N) | `new PriorityQueue<>(collection)` — O(N) |
| Push | `heapq.heappush(h, v)` | `pq.offer(v)` / `pq.add(v)` |
| Pop 堆頂 | `heapq.heappop(h)` | `pq.poll()` |
| Peek 堆頂 | `h[0]` — **沒有 `peek()` 這種東西** | `pq.peek()` |
| 先 pop 再 push | `heapq.heapreplace(h, v)` | `pq.poll(); pq.offer(v);` |
| 先 push 再 pop | `heapq.heappushpop(h, v)` | `pq.offer(v); pq.poll();` |
| 前 k 大 | `heapq.nlargest(k, it)` | 大小為 k 的最小堆積，最後倒出來 |
| 前 k 小 | `heapq.nsmallest(k, it)` | 大小為 k 的最大堆積，最後倒出來 |
| 合併多個已排序序列 | `heapq.merge(a, b, ...)` | 自己手刻 k 路合併 |
| 判斷是否為空 | `if h:` | `pq.isEmpty()` |
| 自訂排序 | tuple，或在 class 上定義 `__lt__` | comparator lambda／`Comparable` |

**避開大多數 API bug 的三條規則**

1. 只有索引 `0` 有意義。`h[1]`、`h[-1]`，以及走訪 Java `PriorityQueue`，拿到的都是
   **部分**有序，不是排好序的結果。
2. comparator 要用 `Integer.compare(a, b)` / `Long.compare(a, b)` 來寫，絕對不要用 `a - b` —
   數值很大或是負數時，減法會溢位。
3. 空的情況要先擋：`h[0]` 會丟 `IndexError`，Java 的 `peek()` 回傳 `null`、`element()` 會丟例外。
   在 `while` 條件裡，把「是否為空」的判斷放**最前面**，才能短路。
