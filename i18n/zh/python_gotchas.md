<!-- 493b4fab2ed1 -->
# Python 陷阱與並行處理

> **範圍** — 那些會悄悄害你 submission 掛掉的 Python 行為：可變預設參數、closure 的延遲綁定、整數快取、複製語意，再加上 GIL 與 Python 的並行處理全貌。
> **另見**：[python_trick.md](./python_trick.md) — 幫得上忙而不是扯後腿的慣用寫法；[concurrency_patterns.md](./concurrency_patterns.md) — Java 這邊的並行對照；[java_trick.md](./java_trick.md) — 給正在換語言的人。

<!-- fa160d1433d1 -->
## LeetCode 題目清單

- [Concurrency](https://leetcode.com/problem-list/concurrency/)

<!-- 8ce208c940f8 -->
## 總覽

面試官很少直接問「請列舉 Python 的陷阱」。這些坑通常是**藏在**一題正常的 coding 題裡冒出來的 — 你用 `[[0]]*n` 初始化 grid、你用了可變預設參數、你在 hot loop 裡 `pop(0)` — 強的候選人會當場看出來並解釋清楚。

知道這些坑代表你有**深度**：

- 你懂 Python 的**物件模型**（參考 vs 複製、可變性、interning）。
- 你懂**求值時機**（延遲綁定、generator 的惰性）。
- 你懂**執行成本**（為什麼 `list.pop(0)` 是 O(n)、為什麼會有 GIL）。

這份文件只講 Python。一般的 Python 慣用寫法（切片、複製示範、進位轉換、字典排序）請看 **[`python_trick.md`](python_trick.md)** — 這裡**不重複**那些內容，只做交叉引用。Java 的並行原語請看 **[`concurrency_patterns.md`](concurrency_patterns.md)**。

<!-- 7cad65e00065 -->
### 關鍵性質

- **核心想法**：名字是「綁定」到物件上的；賦值永遠不會複製；很多「bug」其實只是共用參考或延後求值。
- **什麼時候用**：每一場面試 — 大多數陷阱都是藏在較大解法裡的一行小錯。

<!-- 0ea21eb3d551 -->
### 參考資料

- [The Python Language Reference — Data model](https://docs.python.org/3/reference/datamodel.html)
- [Common Gotchas — The Hitchhiker's Guide to Python](https://docs.python-guide.org/writing/gotchas/)
- [`python_trick.md`](python_trick.md) · [`concurrency_patterns.md`](concurrency_patterns.md)

---

<!-- 442b0cdc3b57 -->
## 0) 概念

<!-- c466c7de5479 -->
### 0-1) （幾乎）所有陷阱背後的心智模型

三件事就能解釋大部分 Python 的意外：

| 事實 | 後果 |
|------|-------------|
| 變數是**綁到某個物件上的名字**，不是裝著值的盒子 | 賦值（`b = a`）複製的是*參考*，不是資料 |
| 物件分成**可變**（`list`、`dict`、`set`）與**不可變**（`int`、`str`、`tuple`、`frozenset`） | 改動一個共用的可變物件，所有綁到它的名字都會看到 |
| 有些東西是**立即求值**（預設參數、list 字面值），有些是**惰性求值**（generator、closure） | 程式碼*什麼時候*跑，決定了它看到*什麼*值 |

<!-- 8533d0327231 -->
### 0-2) 「陷阱 vs 修法」的固定格式

底下每一節都是同一個形狀：先一段 `# gotcha:` 展示坑，再用 `# fix:` / `# why:` 解釋。要背的是*原因*，不是變通寫法 — 面試官挖的就是這個。

---

<!-- 8a678ec6eac5 -->
## 1) 語言層面的陷阱 ⭐⭐⭐⭐⭐

<!-- 77fb7b685b7b -->
### 1-1) 可變的預設參數

<!--CODE-->

<!-- 75d67170b7ca -->
### 1-2) `is` vs `==` 與 interning

<!--CODE-->

<!-- be580a173aa4 -->
### 1-3) 迴圈裡 closure 的延遲綁定

<!--CODE-->

<!-- 195169420bb2 -->
### 1-4) 整數快取與任意精度

<!--CODE-->

<!-- 15ee72ab5bd1 -->
### 1-5) 淺複製 vs 深複製

<!--CODE-->

<!-- 08bb6571af25 -->
### 1-6) 變數作用域：LEGB、`global`、`nonlocal`

<!--CODE-->

<!-- 63374f714076 -->
### 1-7) 真假值與短路運算子的回傳值

<!--CODE-->

<!-- 12e8d99e993f -->
### 1-8) 浮點數相等與 Decimal

<!--CODE-->

<!-- 2f20f0fc91d4 -->
### 1-9) 字典順序與安全存取

<!--CODE-->

<!-- e6f84f8d06f6 -->
### 1-10) Generator vs list：惰性且只能走一次

<!--CODE-->

<!-- 77d224904805 -->
### 1-11) `*args`、`**kwargs`、拆包、海象運算子

<!--CODE-->

<!-- 630215f4c79d -->
### 1-12) `list * n` 的別名問題 — grid 初始化的經典坑 ⭐⭐⭐⭐⭐

<!--CODE-->

<!-- 0de39b7a3fb8 -->
### 1-13) 字串不可變 — 為什麼 `join` 贏過 `+=`

<!--CODE-->

<!-- e12316697871 -->
### 1-14) 例外與走訪的陷阱

<!--CODE-->

<!-- 63b6d4a90bec -->
### 1-15) 整數除法 `//` 與 `%` — Python 是**向下取整**，Java 是**向零截斷** ⭐⭐⭐⭐⭐

<!--CODE-->

<!-- 2c7beaad2681 -->
### 1-16) 預設遞迴上限 — 深度 DFS 會噴 `RecursionError`

<!--CODE-->

<!-- a8e252a1bbae -->
### 1-17) 排序：穩定性、`key=` vs `cmp_to_key`

<!--CODE-->

<!-- 377839228345 -->
### 1-18) `set` **沒有**順序保證（`dict` 有）

<!--CODE-->

---

<!-- e9ccb7e13ad9 -->
## 2) 面試用的資料結構與效能筆記

<!-- 27eb5b6faba2 -->
### 2-1) 成本速查表

| 結構 | 快（一般情況） | 慢／陷阱 |
|-----------|----------------|---------------|
| `list` | 索引 `O(1)`、在**尾端** append/pop `O(1)`* | `pop(0)` / `insert(0, x)` 是 **O(n)**（整串要位移） |
| `collections.deque` | **兩端**都能 `O(1)` append/pop | 隨機索引是 `O(n)` — 不適合存取中間 |
| `set` / `dict` | 查詢、插入、刪除平均 `O(1)` | 最壞 `O(n)`；不可雜湊的 key 會丟 `TypeError` |
| `str` | 索引 `O(1)` | 在迴圈裡串接是 `O(n^2)`（改用 `join`） |
| `heapq`（架在 list 上） | push/pop `O(log n)`、看最小值 `O(1)` | **只有 min-heap** |
| `bisect`（排序好的 list） | 搜尋 `O(log n)` | 插入仍然是 `O(n)`（list 位移） |

*append 是攤還 O(1)。

<!-- ca1ff04086e2 -->
### 2-2) 佇列：絕對不要用 `list.pop(0)`

<!--CODE-->

<!-- 1e966afbeee4 -->
### 2-3) `heapq` — 只有 min-heap；要 max-heap 就取負號

<!--CODE-->

<!-- e504ae1ca3d8 -->
### 2-4) `collections` 的好用工具

<!--CODE-->

<!-- 45037a3b3cfc -->
### 2-5) `bisect` 與 `functools.lru_cache`

<!--CODE-->

<!-- e6e3d07280c1 -->
### 2-6) `defaultdict` **在你「讀」的時候就會插入 key** ⭐⭐⭐⭐

<!--CODE-->

<!-- ec1e68f9fe04 -->
### 2-7) 切片是**複製** — 每次切片都要 `O(k)`

<!--CODE-->

---

<!-- da1a9730ac0b -->
## 3) Python 的並行處理 ⭐⭐⭐⭐⭐

這是常見的**知識盲點**，也是面試官很愛問的題目，因為答案沒那麼單純：「Python 有 thread，但它不會讓 CPU 工作變快 — 原因是這樣。」

<!-- e7853658530a -->
### 3-1) GIL（Global Interpreter Lock）

**它是什麼**：CPython 用一把全域的 mutex — 也就是 GIL — 讓**同一時間只有一條 thread 在執行 Python bytecode**，即使機器有多核也一樣。它保護直譯器內部狀態（例如 reference count）不被資料競爭破壞。

**後果**：

| 工作型態 | thread 有幫助嗎？ | 為什麼 |
|----------|---------------|-----|
| **CPU-bound**（密集迴圈、數學運算、parsing） | **沒有** | thread 會在 GIL 上排隊 — 你只拿到約一顆核心的吞吐量，還多付了 context switch 成本 |
| **I/O-bound**（網路、磁碟、資料庫） | **有** | 阻塞式 I/O 期間 GIL 會**被釋放**，所以一條 thread 在等的時候其他 thread 可以跑 |

**GIL 什麼時候釋放**：阻塞式 I/O（socket／檔案讀取）、`time.sleep`，以及許多會主動放掉 GIL 的 C 擴充套件（NumPy 的重運算）。所以 NumPy 那種向量化運算可以平行化，純 Python 迴圈則不行。

**可以拿來講的點**：要在 CPython 拿到真正的 CPU 平行，就用**多個 process**（每個都有自己的直譯器和 GIL），透過 `multiprocessing` 或 `ProcessPoolExecutor`。*（註：較新的 CPython 3.12+ 加入了 per-interpreter GIL，3.13+ 也開始提供實驗性的 free-threaded／無 GIL 版本 — 提一下可以顯示你有在跟進，但預設版本仍然有 GIL。）*

<!-- dd4ccb2e7572 -->
### 3-2) 決策表：threading vs multiprocessing vs asyncio

| 模型 | 最適合 | 平行性 | 成本／備註 |
|-------|----------|-------------|--------------|
| `threading` | I/O-bound、併發量中等、要搭配阻塞式函式庫 | 併發，但**不是**平行（GIL） | 搶佔式；需要 lock；thread 還算便宜 |
| `multiprocessing` / `ProcessPoolExecutor` | **CPU-bound** 的工作 | **真正**平行（N 顆核心） | 記憶體各自獨立；參數／回傳值必須可 **pickle**；有 IPC 與啟動成本 |
| `asyncio` | **高併發的 I/O**（上千條連線） | 併發，單一 thread | 協作式 — 一個阻塞呼叫就會卡住全部；需要 `async` 版的函式庫 |

大原則：

- **CPU-bound → `multiprocessing`**（或丟給 C／NumPy）。
- **I/O-bound、任務數不多 → `threading`**（可以直接用一般的阻塞式程式碼）。
- **I/O-bound、任務數很多 → `asyncio`**（能便宜地擴到上千條連線）。

<!-- c388c812361b -->
### 3-3) `threading.Thread` + `Lock`

<!--CODE-->

<!-- 82ae0d13aad3 -->
### 3-4) `concurrent.futures` 的 pool

<!--CODE-->

<!-- 1122d6a47514 -->
### 3-5) `asyncio` — async/await 與 gather

<!--CODE-->

<!-- 73ba859c6795 -->
### 3-6) 並行處理的面試講稿

| 名詞 | 一句話解釋 |
|------|----------------------|
| **競爭條件（race condition）** | 結果取決於 thread 的時序；兩條 thread 各做 `x += 1` 可能少算一次，因為 read-modify-write 不是原子操作。用 `Lock` 修。 |
| **死鎖（deadlock）** | 兩條 thread 各自握著對方要的 lock，就這樣互等到天荒地老。解法是用**一致的全域順序**取得 lock（或加 timeout）。 |
| **活鎖／飢餓** | thread 一直在對彼此做反應卻沒進展（活鎖），或某條 thread 永遠排不到（飢餓）。 |
| **為什麼 thread 加速不了 CPU 工作** | **GIL** 讓 Python bytecode 序列化 — 同時只有一條 thread 在跑。要 CPU 平行就用 process。 |
| **原子性** | 有些單一 bytecode 看起來是原子的，但複合語句（`+=`、`if x: x=...`）不是。永遠不要賭「這應該是原子的吧」。 |
| **`concurrent` vs `parallel`** | 併發 = 任務*交錯*進行（結構問題）；平行 = 任務*真的同時*在多核上跑。thread／asyncio 給你併發，process 才給你平行。 |
| **執行緒安全的結構** | `queue.Queue` 對 producer/consumer 是 thread-safe；一般的 `list`／`dict` 在多個 writer 同時寫時不保證安全。 |

---

<!-- 49487231cbd1 -->
## 4) Java ↔ Python 快速對照（給正在換語言的人）

| 概念 | Java | Python |
|---------|------|--------|
| 型別 | 靜態，編譯期檢查 | 動態、duck typing（type *hint* 是選配，不強制） |
| 整數範圍 | `int` 32 位元、`long` 64 位元 — 會**溢位** | `int` 是**任意精度** — 永遠不溢位 |
| 預設參數陷阱 | 無（沒有預設參數物件） | 可變預設值只求值**一次**（見 1-1 節） |
| 物件的 `==` | 比 identity（參考）；比值要用 `.equals()` | `==` 比**值**（`__eq__`）；`is` 才是 identity |
| 組字串 | 迴圈裡用 `StringBuilder` | list + `"".join()`（迴圈裡別用 `+=`） |
| 可增長陣列 | `ArrayList` | `list` |
| 雙端佇列 | `ArrayDeque` / `LinkedList` | `collections.deque`（別用 `list.pop(0)`） |
| 雜湊表／雜湊集合 | `HashMap` / `HashSet` | `dict` / `set`（3.7 起 dict 保留插入順序） |
| 優先佇列 | `PriorityQueue`（min-heap） | `heapq`（只有 min-heap；要 max 就取負號） |
| 有序 map | `TreeMap`（有序，`O(log n)`） | 標準庫沒有 sorted map — 用 `bisect` 搭 list，或第三方的 `sortedcontainers` |
| 記憶化 | 自己用 `HashMap` 當快取 | `@functools.lru_cache` 裝飾器 |
| 真正的平行 | 原生 thread 可用滿所有核心 | **process**（GIL 讓 thread 在 CPU 工作上只用得到一顆核心） |
| Lambda | `x -> x + 1`，closure 捕捉的是 *effectively final* 變數 | `lambda x: x + 1`，closure 以**參考**捕捉（延遲綁定，見 1-3 節） |
| Null | `null` | `None`（用 `is None` 比較） |
| 三元運算 | `cond ? a : b` | `a if cond else b` |
| 整數除法 | `/` 向 0 **截斷**（`-7/2 == -3`） | `//` 向 -∞ **取整**（`-7//2 == -4`）；見 1-15 節 |
| 取餘數的正負號 | 跟著**被除數**（`-7 % 2 == -1`） | 跟著**除數**（`-7 % 2 == 1`） |
| 遞迴深度 | JVM stack，約一萬層以上 | 預設 **1000** → `RecursionError`；見 1-16 節 |
| 自訂排序 | `Comparator`（三向 `compare`） | `key=`（優先）或 `functools.cmp_to_key`；兩者都穩定 |
| 子串列／子陣列 | `List.subList` 是一個**view** | 切片是**複製**，每次 `O(k)`；見 2-7 節 |

---

<!-- 547a33f6b98a -->
## 另見

- [`python_trick.md`](python_trick.md) — 切片、複製示範、進位轉換、字典排序、字串技巧。
- [`concurrency_patterns.md`](concurrency_patterns.md) — Java 並行原語與 LC 的 threading 題目。
