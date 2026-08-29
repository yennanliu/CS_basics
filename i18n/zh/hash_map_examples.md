<!-- 0dbeff7af27f -->
# 雜湊表 — 題目詳解

> **範圍** — 雜湊表家族的解法檔案庫：每題一個標準解、那些其實是單題深入剖析的專屬模板，以及有序 map（Java `TreeMap` / Python `SortedDict`）的參考資料。
> **另見** — *母文件*：[hash_map.md](./hash_map.md) — 標準模板、「題目→模式」決策表，還有這份檔案庫在背後撐著的面試建議。
> *鄰近文件*：[prefix_sum.md](./prefix_sum.md) — 前綴和自成一家的完整說明；[hashing.md](./hashing.md) — 雜湊怎麼運作，以及計數與 rolling hash 的慣用寫法；[set.md](./set.md) — 只管有沒有，不管值。

<!-- d19c3a8130bb -->
## LeetCode 題目清單

- [Hash Table](https://leetcode.com/problem-list/hash-table/)

<!-- aac43b398ef8 -->
## 總覽

這個檔案是 [hash_map.md](./hash_map.md) 的長尾。它放了三類東西，這些留在主文件裡只會把模板淹掉：

- **模板與演算法** — 那些實際上是在深入講一兩題的模式（bucket sort、rolling hash、拆字探查、最大頻率的算術），加上有序 map 參考（`TreeMap` / `SortedDict`）— 那是**排序過的** map，所以本來就不屬於主文件。
- **LC 範例** — 寫完整的解法，每題每語言一個標準版本。
- **依模式分類的題目** — 各分類的完整題目表。

<!-- 42b2f65343ee -->
### 關鍵性質
- **複雜度**：見主文件的 [Time Complexity](./hash_map.md#time-complexity) 表
- **核心想法**：這裡每一節都是主文件某個[模板](./hash_map.md#templates--algorithms)的應用 — 該背的是模板，這些是排練
- **什麼時候用**：當你已經知道一題要用哪個模板，想看它完整寫出來長什麼樣

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- e2a015f73c51 -->
### 有序 Map — Java TreeMap / Python SortedDict

> ⚠️ **Python 沒有內建的 `TreeMap`** — 標準函式庫根本沒有有序 map。
> 大家實際上都用第三方套件 **`sortedcontainers`** 的 `SortedDict`（LeetCode 上已預裝）。
> 兩者到底差在哪，見下面的
> [`SortedDict` vs `TreeMap`](#sorteddict-vs-treemap-implementation-differences)
> 比較。

<!--CODE-->

<!-- 9532fea10628 -->
#### Java `TreeMap` → Python `SortedDict` API 對照 ⭐⭐⭐⭐⭐

| Java `TreeMap` | Python `SortedDict` | 備註 |
|---|---|---|
| `firstKey()` / `lastKey()` | `d.keys()[0]` / `d.keys()[-1]` | |
| `firstEntry()` / `lastEntry()` | `d.peekitem(0)` / `d.peekitem(-1)` | 回傳 `(k, v)` tuple |
| `floorKey(x)`（最大的 ≤ x） | `d.keys()[d.bisect_right(x) - 1]` | 要檢查 `idx >= 0` |
| `ceilingKey(x)`（最小的 ≥ x） | `d.keys()[d.bisect_left(x)]` | 要檢查 `idx < len(d)` |
| `lowerKey(x)`（嚴格 < x） | `d.keys()[d.bisect_left(x) - 1]` | 要檢查 `idx >= 0` |
| `higherKey(x)`（嚴格 > x） | `d.keys()[d.bisect_right(x)]` | 要檢查 `idx < len(d)` |
| `subMap(lo, true, hi, true)` | `d.irange(lo, hi)` | 兩端都包含 |
| `headMap(hi, true)` | `d.irange(maximum=hi)` | |
| `tailMap(lo, true)` | `d.irange(minimum=lo)` | |
| `pollFirstEntry()` / `pollLastEntry()` | `d.popitem(0)` / `d.popitem(-1)` | |
| `descendingMap()` | `reversed(d)` / `d.keys()[::-1]` | |
| `new TreeMap<>(comparator)` | `SortedDict(key_func)` | 是 key 的**轉換函式**，不是比較器 |

⚠️ **頭號陷阱**：Java 的 `floorKey/ceilingKey` 直接給你一個**鍵**（或 `null`）；
Python 的 `bisect_*` 給你的是**索引**，而且可能是 `-1` 或 `len(d)`。
**下標前一定要先檢查索引**：

<!--CODE-->

<!--CODE-->

<!-- 1410fd059467 -->
#### **`SortedDict` vs `TreeMap`：實作上的差異**

它們能解的問題一樣，但**不是同一種資料結構**：

| | Python `SortedDict` | Java `TreeMap` |
|---|---|---|
| **來源** | `pip install sortedcontainers` — **不是標準函式庫**（LeetCode 上已預裝） | `java.util`，內建 |
| **實作** | `dict` 加上一個由鍵組成的 `SortedList`（list of lists，接近 B-tree） | 紅黑樹（自平衡二元搜尋樹） |
| **`d[k]` / `get(k)`** | **O(1)** — 就是雜湊查找 | **O(log n)** — 要往下走樹 |
| **插入／刪除** | O(log n) 攤提 | O(log n) |
| **floor / ceiling** | O(log n)，靠 `bisect_*`（回傳的是**索引**） | O(log n)，靠 `floorKey/ceilingKey`（回傳**鍵**或 `null`） |
| **第 k 小的鍵** | **O(log n)** — `d.keys()[k]` ✅ | ❌ 不支援（只能 O(n) 走訪） |
| **自訂排序** | `SortedDict(key_func)` — 只能做 key 的**轉換** | `Comparator` — 任意的兩參數邏輯 |
| **重複鍵** | ❌ | ❌ |
| **執行緒安全** | ❌ | ❌（要用 `ConcurrentSkipListMap`） |

**幾個結論：**
1. 單純查值的話，`SortedDict` 比 `TreeMap` **快**（O(1) 雜湊 vs O(log n) 走樹）。
2. `SortedDict` 支援 O(log n) 的**索引存取**（`keys()[k]`）— 對「第 k 小的鍵」這類題目很好用，`TreeMap` 沒有 order-statistic tree 就辦不到。
3. `TreeMap` 的 `Comparator` 表達力嚴格強過 `SortedDict` 的 key function。
4. 如果只能 import 標準函式庫，退而求其次用一般 list 上的 `bisect`
   （查找 O(log n)，但因為要搬動元素，**插入是 O(n)**）— `n` 小的時候夠用。

**TreeMap 與 HashMap 的比較：**

| 特性 | HashMap | TreeMap |
|---------|---------|---------|
| **順序** | 沒有順序 | 依鍵排序 |
| **底層結構** | 雜湊表 + 鏈結串列／紅黑樹（處理碰撞） | 紅黑樹 |
| **插入／刪除／搜尋** | 平均 O(1)，最差 O(n) | O(log n) |
| **走訪** | 沒有特定順序 | 依鍵的排序順序 |
| **Floor/Ceiling** | 不支援 | O(log n) |
| **範圍查詢** | 不支援 | O(k log n) |
| **適用場景** | 只要快速查找，不在乎順序 | 有序走訪、範圍查詢、floor/ceiling |
| **記憶體** | 較少（雜湊表） | 較多（樹節點 + 指標） |

**什麼時候該用 TreeMap：**
- 需要鍵維持排序
- 需要 floor/ceiling（找最接近的鍵）
- 需要範圍查詢（`[a, b]` 之間的所有鍵）
- 需要高效率拿到第一個／最後一個鍵
- 題目牽涉到區間、範圍或順序限制

**什麼時候不該用 TreeMap：**
- 只需要 O(1) 快速查找、不在乎順序
- 記憶體吃緊（TreeMap 比較耗記憶體）
- 用不到有序操作（HashMap 更快）

**常見的 TreeMap 模式：**

1. **模式 1：用有序 Map 做排序**
<!--CODE-->

2. **模式 2：區間管理**
<!--CODE-->

3. **模式 3：連續元素**
<!--CODE-->

4. **模式 4：範圍／資料流問題**
<!--CODE-->

**經典 LeetCode 題目：**

| 題目 | LC# | 難度 | 關鍵的 TreeMap 操作 |
|---------|-----|------------|----------------------|
| Car Fleet | 853 | Medium | 用位置當鍵來排序 |
| My Calendar I | 729 | Medium | 用 floorKey/ceilingKey 檢查重疊 |
| My Calendar II | 731 | Medium | 計算重疊的預約數 |
| My Calendar III | 732 | Hard | 最大重疊數 |
| Hand of Straights | 846 | Medium | 用 firstKey 拿最小元素 |
| Data Stream as Disjoint Intervals | 352 | Hard | 用 floor/ceiling 合併區間 |
| Time Based Key-Value Store | 981 | Medium | 用 floorKey 查時間戳 |
| Count of Smaller Numbers After Self | 315 | Hard | 有序走訪 |
| Contains Duplicate III | 220 | Medium | 用 floorKey/ceilingKey 做範圍檢查 |
| The Skyline Problem | 218 | Hard | 用 TreeMap 當多重集合 |

**範例：LC 853 - Car Fleet**

<!--CODE-->

<!--CODE-->

**範例：LC 729 - My Calendar I**

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!--CODE-->

**TreeMap 類題目的面試建議：**

1. **辨識訊號：**
   - 「排序順序」、「最小／最大」、「floor/ceiling」→ 想到 TreeMap
   - 「重疊區間」→ TreeMap 配 floorKey/ceilingKey
   - 「連續元素」→ 用 TreeMap.firstKey() 做貪婪處理
   - 「範圍查詢」→ TreeMap.subMap()

2. **常見錯誤：**
   - 忘了 TreeMap 是 O(log n)，HashMap 才是 O(1)
   - 沒處理 floor/ceiling 回傳 null 的情況
   - HashMap 就夠用了卻硬要用 TreeMap
   - 沒考慮樹結構的記憶體開銷
   - **（Python）** 把 `bisect_left/right` 的結果當成**鍵** — 它是**索引**；
     忘了 `idx >= 0` / `idx < len(d)` 的檢查 → `IndexError`，或更慘的是無聲繞回
     （`keys()[-1]` 回傳的是**最大**的鍵，不是「沒有」！）
   - **（Python）** 寫 `bisect.bisect_left(list(d.keys()), x)` — 那個 `list(...)` 複製是 O(n)；
     改呼叫 `d.bisect_left(x)`

3. **最佳化：**
   - 如果只需要有序走訪一次，直接把陣列排序就好（O(n log n)，不用維護 TreeMap）
   - 如果範圍查詢很少發生，考慮延遲排序
   - Python 的話，`sortedcontainers` 提供了高效率的 SortedDict

4. **邊界情況：**
   - 空的 TreeMap（firstKey/lastKey 會丟例外）
   - floor/ceiling 回傳 null
   - 重複鍵（TreeMap 不允許，改把值當計數器用）
   - 反向走訪（Java 用 descendingMap()）

<!-- 6c77794097ab -->
### 用雜湊表做 Bucket Sort（Top-K 頻率，O(n)）

**被問到 top-K 高頻元素時，先問一句：「要 O(n) 嗎？」** — bucket 這招可以完全避開堆積。

**想法**：開一排 bucket，`bucket[freq]` 裝所有出現該次數的元素。從最高頻率往下掃，收集前 K 個。

<!--CODE-->

| 做法 | 時間 | 空間 | 什麼時候用 |
|----------|------|-------|------|
| 堆積（nlargest） | O(n log k) | O(n) | 預設 |
| Bucket sort | O(n) | O(n) | 題目明講要 O(n) 時 |

---

<!-- 031ab56e6533 -->
### 雜湊表 + 記憶化／DP

**模式**：把 dict 當成由上而下 DP 的快取（記憶化）。鍵就是子問題的狀態（索引、剩餘目標值、走訪過的集合等等）。

<!--CODE-->

**關鍵原則**：計算**之前**一定先檢查 `if state in memo: return memo[state]`。回傳**之前**一定先把結果存起來。

---

<!-- 1be11eed9982 -->
### 單調堆疊 + 雜湊表

**模式**：用堆疊依單調順序處理元素；用雜湊表依索引或值記下每個元素的答案。

<!--CODE-->

**辨識訊號**：「下一個更大／更小」、「幾天後會變暖」、「股價的跨度」、「最大矩形」。

---

<!-- 5cd2e2865b82 -->
### Rolling Hash（Rabin-Karp）

**什麼時候用**：在期望 O(n) 時間內找出重複／相符的子字串。比 O(n²) 的暴力子字串比較好。

**想法**：用多項式 rolling hash 對每個視窗算雜湊值。視窗右移時，把最左邊的字元移掉、把新的最右字元加進來，都是 O(1)。

<!--CODE-->

**碰撞防護**：雜湊值相同時，一定要再用 `s[i:i+m] == pattern` 驗證一次 — 碰撞很少見，但不是不可能。

| 題目 | LC# | 難度 | 技巧 |
|---------|-----|------------|-----------|
| Repeated DNA Sequences | 187 | Medium | 子字串集合／rolling hash |
| Longest Duplicate Substring | 1044 | Hard | 二分搜尋 + rolling hash |
| Rabin-Karp string match | - | - | 上面的模板 |

---

<!-- 8be815186c74 -->
### 單字 → 索引的 Map 做配對查找（拆字探查） ⭐⭐⭐⭐

**模式**：想在 `n` 個字串裡找**配對**又不想寫 O(n²) 的雙層迴圈，就把每個字串放進 `word -> index` 的 map，然後對每個單字列出它 O(k) 個切點，拿「能補成答案的那一半」去**探查**這個 map。成本從 `O(n^2 * k)` 掉到 `O(n * k^2)`。

**關鍵想法（LC 336）**：`w = prefix + suffix`。`w + partner` 要是回文，只有兩種形狀：
- `suffix` 是回文 → `partner = reverse(prefix)` 接在**右邊**
- `prefix` 是回文 → `partner = reverse(suffix)` 接在**左邊**

<!--CODE-->

<!--CODE-->

**兩個讓它正確的檢查**（兩個都是去重邏輯，也都是面試的 follow-up）：
- `back != w` — 一個單字不能跟自己配對（題目保證單字互異）。
- 第二個分支裡的 `j != n` — 少了它，`w` / `reverse(w)` 這種配對在「空後綴切法」和「空前綴切法」各會吐出兩組有序對，於是每一組都被**重複回報兩次**。

**空字串自然就處理好了**：`words = ["a", ""]` 會同時得到 `[0,1]` 和 `[1,0]`，因為 `""` 從兩邊看都是回文。

---

<!-- 3ee4f649ae32 -->
### 頻率 Map + 最大頻率的算術（貪婪排程） ⭐⭐⭐⭐

**模式**：一個計數 map，但**個別**的計數根本不重要 — 重要的只有 **`maxFreq`** 和**有幾個鍵並列最大**（`countOfMax`，一個只看一項的「計數的計數」）。答案就是一條封閉形式的公式，不用模擬，也不用堆積。

**關鍵想法（LC 621）**：出現最多次的任務決定了整個排法。它切出 `maxFreq - 1` 個寬度為 `n + 1` 的完整框，再加上最後一個框，裝下所有並列最大的任務。

<!--CODE-->

**遞推式**：`answer = max(len(tasks), (maxFreq - 1) * (n + 1) + countOfMax)`
那個 `max(len(tasks), ...)` 在**任務種類多到根本不需要閒置**時才會派上用場 — 這時公式本身會少算。

<!--CODE-->

<!--CODE-->

**為什麼奇偶位填法有效**：擺在 `i` 和 `i+2` 的兩份永遠不相鄰，唯一的風險是繞回頭的接點 — 而那裡剛好安全，因為 `max_freq <= (n+1)//2` 保證了出現最多次的字元完全塞得進偶數位。

| 題目 | LC# | `maxFreq` 決定了什麼 |
|---------|-----|------------------------|
| Task Scheduler | 621 | 總時間 = 出現最多次的任務切出的框 |
| Reorganize String | 767 | 可行性：`maxFreq <= (n+1)/2` |

---

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 43ea09ce1bdf -->
### 2-1) Contiguous Array（LC 525）

**核心模式：轉換 + 前綴和 + HashMap**

<!-- c569288c772c -->
#### 關鍵概念
找出是否存在`至少 2 個索引`具有`相同的計數`（running sum）。

這等同於在下面的圖裡找出`任意 2 個 y 值相同的 x`。

<!-- a708c9e98cbd -->
#### 模式拆解

**1. 問題轉換：**
<!--CODE-->

**2. HashMap 的結構：**
<!--CODE-->

**3. 核心邏輯：**
<!--CODE-->

**4. 為什麼只存「第一次」出現？**
<!--CODE-->

**5. 為什麼要初始化 map.put(0, -1)？**
<!--CODE-->

<!-- 39081105d219 -->
#### 圖解範例
序列：`[0, 0, 0, 0, 1, 1]`
計數變化（0→-1、1→+1）：0 → -1 → -2 → -3 → -4 → -3 → -2

計數在索引 2 和索引 5 都回到 **-2**。長度 = 5 - 2 = **4**，也就是子陣列 `nums[3..5] = [0, 1, 1]` — 等等，講精確一點：這個子陣列是 `nums[index2+1 .. index5] = nums[3..5] = [0,1,1]`……其實 map 裡的索引代表的是這個 running count 上次出現的位置，所以長度 = `i - map[count]` = `5 - 1 = 4`，對應的子陣列是 `nums[2..5] = [0,0,1,1]`（4 個元素，2 個 0 和 2 個 1 ✓）。

<p align="center"><img src="../pic/lc_525_1.png"></p>

<!-- a96820199283 -->
#### 數學上的道理

**為什麼計數相同就代表子陣列平衡：**
<!--CODE-->

<!-- d102ac0647bb -->
#### 實作模板

<!--CODE-->

<!--CODE-->

<!-- c6df9e3b4d43 -->
#### 和 LC 560 模式的關鍵差異

| 面向 | LC 560（Subarray Sum K） | LC 525（Contiguous Array） |
|--------|-------------------------|---------------------------|
| **目標** | 數出**所有**子陣列 | 找**最長**的子陣列 |
| **Map 的值** | `count`（出現次數） | `index`（第一次出現的位置） |
| **Map 更新** | 每次都累加次數 | 只在計數第一次出現時寫入 |
| **檢查式** | `presum - k` | 相同的 `count` |
| **初始化** | `{0: 1}` | `{0: -1}` |

<!-- a25e1cfed8bf -->
#### 同模式的相關題目

- **LC 525**：Contiguous Array（就是這個模式）
- **LC 1124**：Longest Well-Performing Interval（類似的轉換）
- **LC 523**：Continuous Subarray Sum（改成取模的轉換）
- **LC 325**：Maximum Size Subarray Sum Equals k（前綴和 + 索引）

---

<!-- 073c852d5a92 -->
### 2-1-1) Subarray Sums Divisible by K（LC 974）

**核心模式：前綴和 + 模運算 + HashMap**

<!-- e7a2c187e1cb -->
#### 關鍵概念
用餘數追蹤來數出**所有**和可被 K 整除的子陣列。

如果兩個前綴和**對 K 取模的餘數相同**，它們的差就能被 K 整除。

<!-- 21557fb360e8 -->
#### 模式拆解

**1. 數學基礎：**
<!--CODE-->

**2. HashMap 的結構：**
<!--CODE-->

**3. 為什麼存餘數的「次數」而不是「索引」？**
<!--CODE-->

**4. 關鍵：處理負餘數**
<!--CODE-->

**為什麼？** 在 Java／Python 裡 `-7 % 5 = -2`，但我們要的是餘數 3（因為 -2 ≡ 3 mod 5）。

**5. 初始化：為什麼是 map.put(0, 1)？**
<!--CODE-->

<!-- b0cda7fbbfee -->
#### 圖解範例

**輸入：** `nums = [4, 5, 0, -2, -3, 1]`、`k = 5`

**前綴和：** `[4, 9, 9, 7, 4, 5]`

**餘數（mod 5）：** `[4, 4, 4, 2, 4, 0]`

| 索引 | Num | PrefixSum | 餘數 | Map 狀態 | 新增次數 | 累計 |
|-------|-----|-----------|-----------|-----------|-------------|-------------|
| - | - | 0 | 0 | {0:1} | - | 0 |
| 0 | 4 | 4 | 4 | {0:1, 4:1} | 0 | 0 |
| 1 | 5 | 9 | 4 | {0:1, 4:2} | +1 | 1 |
| 2 | 0 | 9 | 4 | {0:1, 4:3} | +2 | 3 |
| 3 | -2 | 7 | 2 | {0:1, 4:3, 2:1} | 0 | 3 |
| 4 | -3 | 4 | 4 | {0:1, 4:4, 2:1} | +3 | 6 |
| 5 | 1 | 5 | 0 | {0:2, 4:4, 2:1} | +1 | **7** |

**結果：** 7 個子陣列的和可被 5 整除

**找到的子陣列：**
1. `[4,5,0,-2,-3,1]`（整個陣列，結尾餘數 0）
2. `[5]`（索引 0 與 1 的餘數同為 4）
3. `[5,0]`（索引 0 與 2 的餘數同為 4）
4. `[5,0,-2,-3]`（索引 0 與 4 的餘數同為 4）
5. `[0]`（索引 1 與 2 的餘數同為 4）
6. `[0,-2,-3]`（索引 1 與 4 的餘數同為 4）
7. `[-2,-3]`（索引 2 與 4 的餘數同為 4）

<!-- a815c1c062dd -->
#### 實作模板

<!--CODE-->

<!--CODE-->

**注意：** Python 的 `%` 一定回傳正餘數，所以不需要調整。

<!-- b8df84b754a5 -->
#### 最佳化：用陣列取代 HashMap

既然餘數一定落在 `[0, k-1]`，改用陣列會更快：

<!--CODE-->

**時間複雜度：** O(N)
**空間複雜度：** O(K)，不是 O(N)

<!-- fdf8e3679d26 -->
#### 和相關題目的關鍵差異

| 面向 | LC 560（Sum = K） | LC 974（Divisible by K） | LC 525（Equal 0/1） |
|--------|------------------|-------------------------|---------------------|
| **目標** | 數子陣列 | 數子陣列 | 找最長 |
| **Map 的鍵** | `prefixSum` | `prefixSum % k` | `count` |
| **Map 的值** | `count` | `count` | `first_index` |
| **檢查式** | `presum - k` | 相同的 `remainder` | 相同的 `count` |
| **特別處理** | 無 | **負餘數！** | 把 0 轉成 -1 |
| **初始化** | `{0: 1}` | `{0: 1}` | `{0: -1}` |

<!-- 131451dfc969 -->
#### 關鍵：為什麼負餘數非處理不可

**範例：** `nums = [-1, -2, -3]`、`k = 5`

不調整的話：
<!--CODE-->

調整之後：
<!--CODE-->

現在餘數 4 對上了 → 子陣列 `[-1]` 和 `[-2, -3]` 餘數相同 → 子陣列 `[-2, -3]` 的和可被 5 整除 ✓

<!-- a7e64a126441 -->
#### 同模式的相關題目

- **LC 974**：Subarray Sums Divisible by K（就是這個模式）
- **LC 523**：Continuous Subarray Sum（一樣是整除，但多了長度 ≥ 2 的限制）
- **LC 560**：Subarray Sum Equals K（沒有取模，更單純）
- **LC 1248**：Count Nice Subarrays（轉換 + 計數模式）

---

<!-- 0016d2e1a93f -->
### 2-1-2) Count Number of Nice Subarrays（LC 1248）

**核心模式：把奇數轉換掉 → 前綴和計數（跟 LC 560 一樣）**

<!-- 23ba3037a6fb -->
#### 關鍵概念
把每個數字看成 0（偶數）或 1（奇數），再套上前綴和 + 雜湊表的模式，就能數出**剛好有 k 個奇數**的子陣列。

<!-- 4188ea2c09e8 -->
#### 核心想法

**轉換：** 把每個元素換成 `num % 2`（奇數是 1，偶數是 0）。

問題就變成：**數出和等於 k 的子陣列** — 這正是 LC 560！

<!--CODE-->

**為什麼要 `map.put(0, 1)`？**
<!--CODE-->

<!-- 1ae2fe022c05 -->
#### 實作模板

<!--CODE-->

<!--CODE-->

> **為什麼是 `+= cnt_map.get(prefix_cnt - k, 0)` 而不是 `+= 1`？**
> `prefix_cnt - k`（那個「互補」的奇數個數）可能在前面好幾個索引都達到過。
> 每一個那樣的起點都能跟現在這個索引配成一個剛好有 `k` 個奇數的子陣列，
> 所以要把整個次數加進來 — 跟 LC 560 的「在前綴值上做 2-sum」是同一招。

<!-- f24c02854f98 -->
#### 另一種做法：滑動視窗（atMost 技巧）

<!--CODE-->

<!-- d543c15bc0f4 -->
#### 和相關題目的關鍵差異

| 面向 | LC 560（Sum = K） | LC 930（Binary Sum = K） | LC 1248（Nice Subarrays） |
|--------|-----------------|------------------------|--------------------------|
| **轉換** | 不用（直接用值） | 值本來就是 0/1 | `num % 2` → 0 或 1 |
| **Map 的鍵** | `prefixSum` | `prefixSum` | `oddCount` |
| **Map 的值** | `count` | `count` | `count` |
| **初始化** | `{0: 1}` | `{0: 1}` | `{0: 1}` |

<!-- 2ebe7204bc17 -->
#### 同模式的相關題目

- **LC 560**：Subarray Sum Equals K（完全同一個模式，不用轉換）
- **LC 930**：Binary Subarrays with Sum（值本來就是 0/1，想法一樣）
- **LC 974**：Subarray Sums Divisible by K（取模的變形）
- **LC 1248**：Count Nice Subarrays（本題 — 先轉成 0/1 再套 LC 560）

---

<!-- 0e94772f60cd -->
### 2-2) Continuous Subarray Sum — LC 523
- 概念跟 Contiguous Array（LC 525）相近

<!--CODE-->

<!-- af5e0dd42c9f -->
### 2-3) Group Anagrams — LC 49

**想法**：把每個字串排序，當成標準化的雜湊鍵；鍵相同的就分到同一組。

> 標準解跟分組模板放在一起，見 [hash_map.md → Template 3: Grouping by a Computed Key](./hash_map.md#template-3-grouping-by-a-computed-key)。

<!-- 087f0f7c04a4 -->
### 2-3') Longest Substring Without Repeating Characters — LC 3
<!--CODE-->

<!-- d620adfff695 -->
### 2-4) Count Primes — LC 204
<!--CODE-->

<!-- 5750f591273c -->
### 2-5) Valid Sudoku — LC 36
<!--CODE-->
> **注意**：LC 36 只問**目前這個盤面**合不合法 — 它沒有要你把數獨解出來。
> 回溯法的解題器回答的是另一個問題（那是 LC 37），而且它可能把不合法的盤面判成合法，
> 因為它從來不去檢查那些已填好的格子彼此之間有沒有衝突。

<!--CODE-->

<!-- 3a122ae82656 -->
### 2-6) Pairs of Songs With Total Durations Divisible by 60 — LC 1010
<!--CODE-->

<!-- 67f52712ac68 -->
### 2-7) Subarray Sum Equals K — LC 560
<!--CODE-->
<!--CODE-->

<!-- 6306dcd4af15 -->
### 2-8) K-diff Pairs in an Array — LC 532
<!--CODE-->

<!-- 83fee33bbc5e -->
### 2-9) Sentence Similarity — LC 734
<!--CODE-->

<!-- 0fa361081a54 -->
### 2-10) LRU Cache — LC 146
<!--CODE-->

<!-- 06284fec5512 -->
### 2-11) Find All Anagrams in a String — LC 438
<!--CODE-->

<!-- 39ace14ebba9 -->
### 2-12) Brick Wall — LC 554
<!--CODE-->

<!-- 350f83619603 -->
### 2-13) Maximum Size Subarray Sum Equals k — LC 325

<!--CODE-->

<!-- deb4682d7137 -->
### 2-14) Smallest Common Region — LC 1257

<!--CODE-->

---

<!-- bd95c3aa8bad -->
### 2-15) Tuple with Same Product（LC 1726）

**核心想法：配對乘積的頻率 → 組合計數**

給一個元素互異的正整數陣列，數出滿足 `a * b = c * d` 的四元組 `(a, b, c, d)`。

<!-- 4c8ed71b8d8f -->
#### 關鍵洞見

1. 對所有 `i < j` 算出**每一組配對的乘積** `nums[i] * nums[j]`
2. 數出**有幾組配對**共用同一個乘積
3. 若某個乘積出現 `n` 次，任取兩組配對 → `C(n, 2) = n*(n-1)/2` 種組合
4. 每一種配對組合會產生 **8 個四元組**（`(a,b,c,d)` 的排列）

**為什麼是 8？** 給定兩組乘積相同的配對 `(a,b)` 與 `(c,d)`：
- 第一組內部交換：`(a,b)` 或 `(b,a)` → 2 種
- 第二組內部交換：`(c,d)` 或 `(d,c)` → 2 種
- 交換哪一組當 `(a,b)`、哪一組當 `(c,d)` → 2 種
- 合計：`2 × 2 × 2 = 8`

<!-- 085ec5b0bcff -->
#### 模式

<!--CODE-->

<!-- cc67383676ae -->
#### Java 實作

<!--CODE-->

<!-- bb71c86f68e8 -->
#### 兩種公式其實等價

<!--CODE-->

兩種寫法都對。`4 * count * (count - 1)` 這種寫法可以避開整數除法。

<!-- a165ea6fc52f -->
#### 同模式的相關題目

| 題目 | LC# | 難度 | 模式 |
|---------|-----|------------|---------|
| Tuple with Same Product | 1726 | Medium | 配對乘積 → C(n,2) × 8 |
| Number of Good Pairs | 1512 | Easy | 配對計數 → C(n,2) |
| Number of Boomerangs | 447 | Medium | 配對距離的頻率 → n*(n-1) |
| Count Number of Texts | 2266 | Medium | 頻率 → 組合計數 |

**和 LC 1512（Good Pairs）的關鍵差異：**
- LC 1512：數出 `nums[i] == nums[j]` 的配對 → 每個值 `C(n,2)`
- LC 1726：從乘積相同的配對數出**四元組** → 每個乘積 `C(n,2) * 8`

---

<!-- c119731d18a3 -->
### 2-16) Minimum Operations to Sort Binary Tree by Level（LC 2471）

**核心模式：逐層 BFS + 用 `{value: index}` 雜湊表求最少交換次數**

> LC 2471 - Minimum Number of Operations to Sort a Binary Tree by Level
> https://leetcode.com/problems/minimum-number-of-operations-to-sort-a-binary-tree-by-level/

<!-- 3cf77ad5f8ba -->
#### 關鍵概念

每次操作可以交換**同一層裡任兩個節點的值**。要讓整棵樹逐層排好，
答案就是**每一層各自排序所需的最少交換次數，加總起來**。

所以問題拆成兩塊，彼此獨立：
1. **BFS** 把每一層的值收集成一個陣列。
2. **求每個陣列排序的最少交換次數** — 雜湊表就是在這裡發光。

<!-- 42af43b42fc2 -->
#### 雜湊表的招數：把陣列排好的最少交換次數

**關鍵想法**：要用**最少**交換次數把陣列排好，就是反覆地
**用一次交換把正確的值放到每個索引上**。而要做到 O(1) 的交換，
必須知道**每個值現在待在哪裡** → 這就是 `{value: index}` 雜湊表的用途。

<!--CODE-->

**⚠️ 關鍵：交換「之前」先更新 map。** 交換之後，`arr[i]` 已經不是原本被擠掉的那個值了，
你救不回它的舊鍵。先把兩個新位置寫進 map，再去動陣列。

**為什麼這是最少的**：每一次成功的交換至少讓一個元素落到它最終的排序位置，
所以不會「浪費」任何一次交換。（這就是循環分解的結論：一個陣列需要 `n - (循環數)` 次交換；
這個貪婪的逐索引掃描剛好達到這個次數。）

<!-- 25db8bce0704 -->
#### 實作

<!--CODE-->

<!--CODE-->

<!-- 5dae5dae09c2 -->
#### 圖解追蹤 — `min_swaps([3, 1, 2])`

<!--CODE-->

<!-- f9ddd48220bb -->
#### 為什麼要用雜湊表（而不是線性掃描）？

沒有 map 的話，要找 `swap_idx`（`correct_val` 待的位置）就得 O(n) 掃一遍，
`min_swaps` 會變成 O(n²)。`{value: index}` 這個 map 把查找變成 O(1)，
於是每一層的成本是 O(n log n)（排序），而不是 O(n²)。

| 做法 | 找交換目標 | min_swaps 總成本 |
|----------|------------------|-----------------|
| 每步線性掃描 | O(n) | O(n²) |
| **`{value: index}` 雜湊表** | **O(1)** | **O(n log n)** |

<!-- e5b8184833cf -->
#### 同樣是「最少交換次數排序」的相關題目

| 題目 | LC# | 備註 |
|---------|-----|-------|
| Min Operations to Sort Tree by Level | 2471 | BFS 分層 + 每層求最少交換 |
| Minimum Swaps to Group All 1's Together | 1151 / 2134 | 滑動視窗變形 |
| Couples Holding Hands | 765 | 循環／併查集求最少交換 |
| First Missing Positive | 41 | 把元素換到對應索引的想法 |

---

<!-- 4910d45ca510 -->
### 2-17) Maximum Swap（LC 670）

**核心模式：`{digit: last index}` 雜湊表 + 由左往右的貪婪掃描**

> LC 670 - Maximum Swap
> https://leetcode.com/problems/maximum-swap/
> 給一個整數，**最多交換一次**兩個位數，讓數值最大。

<!-- e4569da23929 -->
#### 核心想法

只能換一次的話，就要讓**最大的位數盡量往左搬**。由左往右掃，
在**第一個**可以被後面更大位數壓過去的位置，把它跟那個更大位數的
**最後（最右邊）一次出現**交換 — 然後就停。

雜湊表是關鍵：預先算好 `0-9` 這十個位數的 `{digit: last index}`，
於是「我右邊有沒有更大的位數？它最右邊那份在哪？」就從 O(n) 掃描變成 O(1) 查表。

<!--CODE-->

因為位數只有 10 種，map 最多 10 個鍵 → 實際上是 O(1) 空間。

<!-- d130b483abcc -->
#### 圖解追蹤 — `num = 2736`

<!--CODE-->

`num = 9973` → 每個位數右邊都沒有更大的位數 → 不交換 → `9973`。

<!-- 30f2dc5ab2fe -->
#### 模式（Python）

<!--CODE-->

<!-- 16053810b080 -->
#### 模式（Java）

<!--CODE-->

<!-- 10f066770df2 -->
#### 另一種做法 — 三個指標（不用雜湊表）

**由右往左**掃描，同時追蹤 `max_idx`（目前看過最大位數的最右索引），
並記住最好的那組 `(left, right)` 交換。一樣是 O(n) 時間、O(1) 空間，
但雜湊表版本讀起來更直接。

<!--CODE-->

<!-- 16416eefa5c5 -->
#### 各做法比較

| 做法 | 時間 | 空間 | 備註 |
|----------|------|-------|------|
| 暴力（試遍每一組配對） | O(n²) | O(n) | 簡單，留下最大的候選 |
| `{digit: last index}` 雜湊表 | O(n) | O(1) | 貪婪：第一個可改善的位置 → 最後出現的更大位數 |
| 三個指標（`left/right/max_idx`） | O(n) | O(1) | 由右往左，不用 map |

<!-- 6013cd357ec6 -->
#### 相似題目

| 題目 | LC# | 關聯 |
|---------|-----|----------|
| Maximum Swap | 670 | `{digit: last index}` + 由左貪婪掃描 |
| Next Greater Element III | 556 | 重排位數，找下一個更大的數 |
| Next Permutation | 31 | 找樞紐 + 後繼 + 反轉後綴（相鄰的想法） |
| Remove K Digits | 402 | 在位數上做貪婪的單調堆疊 |
| Largest Number | 179 | 對數字字串自訂排序 |
| Create Maximum Number | 321 | 跨陣列的貪婪選位數 |

---

<!-- dd10beff657c -->
### 2-18) Longest Repeating Character Replacement（LC 424）

**核心模式：滑動視窗 + HashMap（頻率計數）+ 追蹤 `max_freq`**

<!-- 09529fab47b6 -->
#### 關鍵概念
給字串 `s` 和整數 `k`，你可以替換**最多 `k` 個**字元。回傳能做出的、由同一個字母重複組成的最長子字串長度。

**關鍵洞見**：對任何一個視窗來說，必須替換掉的字元數是：
<!--CODE-->
當 `replacements_needed <= k` 時這個視窗就**合法**。取最大的合法視窗。

<!-- d1609cb85d41 -->
#### 模式拆解

1. **右邊界 `r` 往外擴**，更新 `cnt_map[s[r]] += 1`。
2. **追蹤 `max_freq`** = 視窗內單一字元的最高計數。
3. **左邊界 `l` 往內縮**，只要 `(r - l + 1) - max_freq > k`（要替換的太多了）。
4. **記錄** `max_len = max(max_len, r - l + 1)`。

> **順序很重要**：**先**更新雜湊表，**再**用 `while` 迴圈檢查合法性。
> 這跟前綴和類的雜湊表題（LC 523、525）相反 — 那些是更新*之前*先檢查。

<!-- 6c7743f2a1fd -->
#### 合法性檢查的兩種寫法

| 寫法 | 檢查式 | 成本 | 備註 |
|---------|-------|------|------|
| **追蹤 `max_freq`** | `(r-l+1) - max_freq > k` | 每步 O(1) | 建議用這個 — 不用掃 map 的值 |
| `max(cnt_map.values())` | `(r-l+1) - max(cnt_map.values()) > k` | 每步 O(26) | 比較好想；因為值最多 26 個，整體還是 O(n) |

> **為什麼縮視窗時不需要把 `max_freq` *調小***：`max_freq` 只用來反映目前找到過的最佳視窗。就算它「過期了」（比當下真正的最大值還大），答案仍然正確 — `max_len` 只有在真的出現更長的合法視窗時才會變大，而那需要一個新的、更高的 `max_freq`。

<!-- 3e048a312f5c -->
#### 複雜度
<!--CODE-->

<!-- 7fea28d1eaa9 -->
#### 為什麼是 O(n) — 雙指標的論證
<!--CODE-->

<!-- e60ffbf2762f -->
#### 和其他滑動視窗雜湊表題的對照

| 題目 | LC# | 視窗合法的條件 | Map 的角色 |
|---------|-----|-------------------|----------|
| Longest Repeating Char Replacement | 424 | `size - max_freq <= k` | 視窗內字元的頻率 |
| Longest Substring w/o Repeating | 3 | 沒有重複字元 | `{char: last index}` |
| Max Consecutive Ones III | 1004 | 視窗內 0 的個數 `<= k` | 0 的計數（同樣想法，二元版） |
| Min Window Substring | 76 | 視窗涵蓋目標 | 需要多少 vs 已有多少 |

<!-- 8af30c6a5890 -->
#### 同模式的相關題目
- **LC 424**：Longest Repeating Character Replacement（就是這個模式）
- **LC 1004**：Max Consecutive Ones III（二元的特例：`size - ones <= k`）
- **LC 1493**：Longest Subarray of 1's After Deleting One Element
- **LC 340**：Longest Substring with At Most K Distinct Characters

---

<!-- cc73acacac5a -->
### 2-19) Partition Labels — LC 763

**想法**：一個 `{char: last index}` 的 map，把「這個字母最後出現在哪？」變成 O(1) 查表；接著由左往右貪婪掃描，把目前這段一路延伸到看過的最遠 last index，等掃描索引走到它的那一刻就切一刀。

<!--CODE-->

---

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- 0d6a71c65c4b -->
### 分類 1：計數與頻率（25 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| Valid Anagram | 242 | Easy | 計數 | 比較字元頻率 |
| Group Anagrams | 49 | Medium | 計數 | 把排序後的字串當鍵 |
| Sort Characters by Frequency | 451 | Medium | 計數 | 依頻率排序 |
| Top K Frequent Elements | 347 | Medium | 計數 + 堆積 | 計數 + 優先佇列 |
| Top K Frequent Words | 692 | Medium | 計數 + 堆積 | 計數 + 自訂比較器 |
| Most Common Word | 819 | Easy | 計數 | 清理輸入，再數單字 |
| Subdomain Visit Count | 811 | Easy | 計數 | 拆網域，累加造訪次數 |
| Find All Anagrams in String | 438 | Medium | 滑動視窗 | 視窗頻率比對 |
| Word Pattern | 290 | Easy | 計數 | pattern 與單字之間的雙射 |
| Isomorphic Strings | 205 | Easy | 計數 | 字元對應 |
| First Unique Character | 387 | Easy | 計數 | 找第一個 freq=1 的 |
| Unique Number of Occurrences | 1207 | Easy | 計數 | 頻率的頻率 |
| Find Anagram Mappings | 760 | Easy | 計數 | 索引對應 |
| Vowels of All Substrings | 2063 | Medium | 計數 | 每個母音的貢獻度 |
| Maximum Number of Balloons | 1189 | Easy | 計數 | 數出卡住的那個字元 |
| Number of Good Pairs | 1512 | Easy | 計數 | n*(n-1)/2 組配對 |
| Decode the Message | 2325 | Easy | 計數 | 字元替換 |
| Sort Array by Frequency | 1636 | Easy | 計數 | 先依頻率、再依值排序 |
| Check if Two Strings are Equivalent | 1662 | Easy | 計數 | 組出字串再比較 |
| Baseball Game | 682 | Easy | 計數 | 模擬遊戲規則 |
| Number of Arithmetic Triplets | 2367 | Easy | 計數 | 檢查差值 |
| Count Elements | 1426 | Easy | 計數 | 數出 x+1 存在的那些 x |
| Distribute Candies | 575 | Easy | 計數 | 取種類數與 n/2 的較小值 |
| Intersection of Two Arrays | 349 | Easy | 計數 | 集合交集 |
| Intersection of Two Arrays II | 350 | Easy | 計數 | 頻率交集 |

<!-- 11eab3a12fb4 -->
### 分類 2：Two Sum 變形（15 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| Two Sum | 1 | Easy | Two Sum | 存互補值的索引 |
| Two Sum II | 167 | Easy | 雙指標 | 利用陣列已排序 |
| 3Sum | 15 | Medium | Two Sum | 固定一個，再找配對 |
| 3Sum Closest | 16 | Medium | Two Sum | 追蹤最接近的和 |
| 4Sum | 18 | Medium | Two Sum | 固定兩個，再找配對 |
| Two Sum IV - BST | 653 | Easy | Two Sum | 中序走訪 + 雜湊集合 |
| K-diff Pairs in Array | 532 | Medium | Two Sum | 處理 k=0 的情況 |
| Pairs of Songs with Total Duration Divisible by 60 | 1010 | Medium | Two Sum | 模運算 |
| Count Number of Pairs with Absolute Difference K | 2006 | Easy | Two Sum | 檢查 num+k、num-k |
| Find All K-Distant Indices | 2200 | Easy | Two Sum | 距離限制 |
| Max Number of K-Sum Pairs | 1679 | Medium | Two Sum | 貪婪地把配對移掉 |
| Two Sum Less Than K | 1099 | Easy | Two Sum | 追蹤最大的合法和 |
| Two Sum - Data Structure | 170 | Easy | 設計題 | Add/Find 操作 |
| Count Good Meals | 1711 | Medium | Two Sum | 以 2 的次方當目標值 |
| Count Pairs With XOR in Range | 1803 | Hard | 字典樹（Trie） + Two Sum | XOR 的性質 |

<!-- 529eb68ff958 -->
### 分類 3：前綴和與子陣列（17 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| **Subarray Sum Equals K** | **560** | **Medium** | **前綴和** | **{sum: count} 模式，更新前先檢查** |
| Maximum Size Subarray Sum Equals k | 325 | Medium | 前綴和 | 存第一次出現的索引 |
| Continuous Subarray Sum | 523 | Medium | 前綴和 | 模運算，存索引 |
| **Contiguous Array** | **525** | **Medium** | **前綴和 + 轉換** | **把 0→-1、1→+1；存 {count: first_index}** |
| Binary Subarrays with Sum | 930 | Medium | 前綴和 | 跟 LC 560 一樣的計數模式 |
| **Subarray Sums Divisible by K** | **974** | **Medium** | **前綴和 + 取模** | **{remainder: count}；一定要處理負餘數！** |
| Count Number of Nice Subarrays | 1248 | Medium | 前綴和 | 奇數轉 1、偶數轉 0 |
| Subarray Sum Equals K II | 1074 | Hard | 前綴和 | 二維矩陣版本 |
| Minimum Size Subarray Sum | 209 | Medium | 滑動視窗 | 和 ≥ target 時收縮 |
| Number of Subarrays with Bounded Maximum | 795 | Medium | 前綴和 | 排容原理 |
| Shortest Subarray with Sum at Least K | 862 | Hard | 雙端佇列 | 單調雙端佇列最佳化 |
| Count of Range Sum | 327 | Hard | 合併排序 | 逆序對計數的變形 |
| Range Sum Query - Immutable | 303 | Easy | 前綴和 | 預先算好前綴和 |
| Range Sum Query 2D | 304 | Medium | 前綴和 | 二維前綴和陣列 |
| Subarray Product Less Than K | 713 | Medium | 滑動視窗 | 乘積 ≥ k 時收縮 |
| Maximum Average Subarray I | 643 | Easy | 滑動視窗 | 固定視窗大小 |
| Find Pivot Index | 724 | Easy | 前綴和 | 左邊的和 = 右邊的和 |

<!-- f6c7619b2bd4 -->
### 分類 4：滑動視窗配雜湊表（12 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| Longest Substring Without Repeating Characters | 3 | Medium | 滑動視窗 | 追蹤最後出現的位置 |
| Minimum Window Substring | 76 | Hard | 滑動視窗 | 合法時就收縮 |
| Permutation in String | 567 | Medium | 滑動視窗 | 固定視窗大小 |
| Find All Anagrams in String | 438 | Medium | 滑動視窗 | 比對頻率表 |
| Longest Substring with At Most Two Distinct Characters | 159 | Medium | 滑動視窗 | 追蹤字元計數 |
| Longest Substring with At Most K Distinct Characters | 340 | Medium | 滑動視窗 | 把相異字元上限一般化 |
| Fruit Into Baskets | 904 | Medium | 滑動視窗 | 最多 2 種 |
| Longest Repeating Character Replacement | 424 | Medium | 滑動視窗 | 追蹤最大頻率 — [詳細模式](#2-18-longest-repeating-character-replacement-lc-424) |
| Get Equal Substrings Within Budget | 1208 | Medium | 滑動視窗 | 成本限制 |
| Max Consecutive Ones III | 1004 | Medium | 滑動視窗 | 最多翻轉 K 個 0 |
| Substring with Concatenation of All Words | 30 | Hard | 滑動視窗 | 多個單字同時比對 |
| Replace the Substring for Balanced String | 1234 | Medium | 滑動視窗 | 讓所有頻率 ≤ n/4 |

<!-- e7c8ed3a0496 -->
### 分類 5：設計與快取（10 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| LRU Cache | 146 | Medium | OrderedDict | 雜湊表 + 雙向鏈結串列 |
| LFU Cache | 460 | Hard | 雜湊表 + 堆積 | 同時追蹤頻率與最近使用 |
| Design HashMap | 706 | Easy | 陣列 + 鏈結法 | 處理碰撞 |
| Design HashSet | 705 | Easy | 陣列 + 鏈結法 | 跟 HashMap 差不多 |
| All O(1) Data Structure | 432 | Hard | 雜湊表 + 雙向鏈結串列 | 多層次的複雜結構 |
| Insert Delete GetRandom O(1) | 380 | Medium | 雜湊表 + 陣列 | 維護索引對應 |
| Insert Delete GetRandom O(1) - Duplicates | 381 | Hard | 雜湊表 + 陣列 | 處理重複元素 |
| Design Twitter | 355 | Medium | 雜湊表 + 堆積 | 動態消息與追蹤關係 |
| Time Based Key-Value Store | 981 | Medium | 雜湊表 + 二分搜尋 | 依時間戳存取 |
| Design A Leaderboard | 1244 | Medium | 雜湊表 + 排序 | 追蹤分數 |

<!-- d145adaeefa0 -->
### 分類 6：圖與樹配雜湊表（8 題）

| 題目 | LC# | 難度 | 模板 | 關鍵洞見 |
|---------|-----|------------|----------|-------------|
| Clone Graph | 133 | Medium | 雜湊表 + DFS | 走訪過程中做節點對應 |
| Copy List with Random Pointer | 138 | Medium | 雜湊表 + DFS | 為 random 指標做節點對應 |
| Find Duplicate Subtrees | 652 | Medium | 雜湊表 + DFS | 把子樹序列化成鍵 |
| Sentence Similarity | 734 | Easy | 雜湊表 + 集合 | 雙向的相似性對應 |
| Accounts Merge | 721 | Medium | 雜湊表 + 併查集 | email 到帳號的對應 |
| Evaluate Division | 399 | Medium | 雜湊表 + DFS | 建出等式圖 |
| Most Stones Removed | 947 | Medium | 雜湊表 + 併查集 | 把同列／同行的石頭連起來 |
| Smallest Common Region | 1257 | Medium | 雜湊表 + 集合 | 父節點對應 + LCA |

<!-- 55e4835738ef -->
### 其他高頻雜湊表題（沒有新模板）

| 題目 | LC# | 難度 | 一句話重點 |
|---------|-----|------|-------------------|
| Find Duplicate File in System | 609 | Medium | 用標準化的鍵分組（模板 1），鍵是**檔案內容**，值是路徑清單 |
| Degree of an Array | 697 | Easy | 一趟建出 `value -> (count, first_index, last_index)`；答案 = 計數最大的那些值裡跨度最短的 |
| First Unique Character in a String | 387 | Easy | 先計數一趟，再按原順序掃第二趟 — 保住「第一個」靠的就是第二趟 |
| Ransom Note | 383 | Easy | 計數相減；Python 直接寫 `Counter(ransom) <= Counter(mag)` |
| Bulls and Cows | 299 | Medium | 第一趟數 bulls；cows = 非 bull 的位數上 `sum(min(count_secret[d], count_guess[d]))` |
| Roman to Integer / Integer to Roman | 13 / 12 | Easy / Medium | 靜態查表 + 貪婪；那些相減的組合（`IV`、`IX`……）要直接放**進**表裡 |
| Jewels and Stones | 771 | Easy | 「用查表打敗巢狀迴圈」的經典暖身題 |

---

<!-- 981fe8d99bce -->
## 總結與速查

| 想找什麼 | 去哪裡 |
|---|---|
| 一題該用哪個模板 | [hash_map.md → Problem → Pattern Decision Table](./hash_map.md#problem--pattern-decision-table) |
| 要背起來的標準模板 | [hash_map.md → Templates & Algorithms](./hash_map.md#templates--algorithms) |
| 完整寫出來的解法 | 上面的 [LC 範例](#lc-examples) |
| 某個分類的所有題目 | 上面的 [依模式分類的題目](#problems-by-pattern) |
| 有序 map 的操作（floor/ceiling/範圍） | 上面的 [有序 Map — Java TreeMap / Python SortedDict](#ordered-map--java-treemap--python-sorteddict) |
| 面試建議與常見錯誤 | [hash_map.md → Summary & Quick Reference](./hash_map.md#summary--quick-reference) |
