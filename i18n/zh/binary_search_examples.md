<!-- 196b7e2f99a8 -->
# 二分搜尋 — 範例題解

> **範圍** — [binary_search.md](./binary_search.md) 的題解存放處 — 索引空間類模板的每題一份標準解，附上主文放不下的追蹤過程與陷阱。
> **另見** — *主文件*：[binary_search.md](./binary_search.md) — 迴圈不變式、邊界模板，以及「該用哪個模板」的決策表；[binary_search_on_answer.md](./binary_search_on_answer.md) — 所有對*答案*而非索引做二分搜尋的題目。
> *鄰近文件*：[2_pointers.md](./2_pointers.md) — LC 167 / LC 658 的收斂指標替代解法；[dp.md](./dp.md) — LC 300 取代掉的那個 `O(n²)` DP；[matrix.md](./matrix.md) — 二維網格題型家族。

<!-- d25f0f2274da -->
## LeetCode 題目清單

- [Binary Search](https://leetcode.com/problem-list/binary-search/)

<!-- 295f9a72dd28 -->
## 總覽

下面每一題都只解**一次**，用最能講清楚它的語言（兩種語言都有教學價值時才都寫）。每題用到的模板放在
[binary_search.md](./binary_search.md) — 這份檔案是練習集，不是第二份模板目錄。對*答案空間*
做二分搜尋的題目請看 [binary_search_on_answer.md](./binary_search_on_answer.md)。

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 431ad60b0b89 -->
### 1) Two Sum II — Input Array Is Sorted (LC 167)
**做法**：對每個元素二分搜尋它的補數
<!--CODE-->

<!-- ee7a025fa737 -->
#### 核心想法：往上爬（保證有峰值）

**關鍵規則：**
<!--CODE-->

**為什麼一定成立（「-∞ 邊界」這個技巧）：**

題目說 `nums[-1] = nums[n] = -∞`。也就是說陣列兩端永遠被兩個無底洞夾著。

<!--CODE-->

不管你站在陣列的哪個位置，只要往*比較高*的鄰居走，一定會撞到某個峰值 — 要嘛地勢先升後降
（中間有個峰），要嘛一路升到底（最後一個元素就是峰，因為它右邊是 -∞）。

---

<!-- bd615c78b7e5 -->
#### 情況 1：`nums[mid] < nums[mid+1]` → 正在**上坡** → 往**右**走

<!--CODE-->

<!--CODE-->

---

<!-- d1778f6a992d -->
#### 情況 2：`nums[mid] > nums[mid+1]` → 正在**下坡** → 留在**左邊**（含 mid）

<!--CODE-->

<!--CODE-->

---

<!-- 34235d4c2c73 -->
#### 圖解：搜尋空間如何收斂

<!--CODE-->

<!--CODE-->

---

<!-- 933438cd195e -->
#### 為什麼是 `while (l < r)` 而不是 `while (l <= r)`？

因為用的是 `r = mid`（不是 `r = mid - 1`），當 `l == r` 時迴圈就必須停 —
否則 `mid == l == r` 會造成無窮迴圈（`r = mid` 根本不會縮小）。

<!--CODE-->

---

**做法**：把 mid 和相鄰元素比較，決定往哪邊搜
<!--CODE-->

<!--CODE-->

<!-- 616c6ac634a6 -->
### 3) Valid Perfect Square (LC 367)
**做法**：在 [1, num] 這個範圍上二分搜尋平方根
<!--CODE-->

<!--CODE-->

<!-- 9b16699590ce -->
### 4) Sqrt(x) (LC 69)
**做法**：二分搜尋，邊界要小心處理
<!--CODE-->

<!-- 1ef32402ab28 -->
### 5) Minimum Size Subarray Sum (LC 209)
**做法**：對可能的子陣列長度二分搜尋 + 用滑動視窗驗證
<!--CODE-->

<!-- d54da047a9a3 -->
### 6) First Bad Version (LC 278)
> 找出最左邊的壞版本，而且不要多呼叫 API。

<!--CODE-->

<!-- d2daa9beef8f -->
### 7) Find K Closest Elements (LC 658)
**做法**：用雙指標把陣列縮到剩 k 個元素
<!--CODE-->

<!-- cdb4e7bdbe78 -->
### 8) Find Smallest Letter Greater Than Target (LC 744)
**模式**：`while (l < r)` — 找插入位置
<!--CODE-->

<!-- 721e695bca82 -->
### 9) Arranging Coins (LC 441)
**模式**：`while (l <= r)` — 用數學性質找精確值
<!--CODE-->

<!-- b37dd5dacf79 -->
### 10) Find Minimum in Rotated Sorted Array II (LC 154)
**模式**：`while (l < r)` — 處理旋轉陣列中的重複值
<!--CODE-->

<!-- e3c502cecdb2 -->
### 11) Missing Element in Sorted Array (LC 1060)
**模式**：`while (l < r - 1)` — 用差距計算找出缺失元素
<!--CODE-->

<!-- 16caa2f86511 -->
### 12) Median of Two Sorted Arrays (LC 4)
> 對較短陣列的切分點做二分搜尋，用 O(log(min(M,N))) 找中位數。

<!--CODE-->

<!-- 535ab4bd1370 -->
### 13) Time Based Key-Value Store (LC 981)
> 對每個 key，在排序好的時間戳上二分搜尋，找出最大且 <= 給定時間的那個。

<!--CODE-->

<!-- a5f0810749b5 -->
### 14) Single Element in a Sorted Array (LC 540)
> 成對的規律在單獨元素之後就被打破了；對偶數索引做二分搜尋。

<!--CODE-->

<!-- 89360c4b7a61 -->
#### 核心想法

給一個排序好的陣列，**多數元素**指的是出現超過 `N/2` 次的元素。

**關鍵洞見**：在排序好的陣列中，若 target 出現超過 `N/2` 次，那麼索引 `firstIndex + N/2` 上的元素也一定是 target。

**為什麼成立：**
- 先找到 target 第一次出現的索引 `firstIndex`
- 若 target 出現 `> N/2` 次，它至少要佔掉 `N/2 + 1` 個連續位置
- 所以 `nums[firstIndex + N/2]` **必定**還是 target

這樣就不用數所有出現次數 — O(log N) 而不是 O(N)。

---

<!-- c8f24afbe66d -->
#### 模式：用二分搜尋找第一個索引

<!--CODE-->

**模板規則：**
- 當 `nums[mid] == target`：先把 `mid` 存成候選，再**往左縮**（`high = mid - 1`）繼續找
- 迴圈結束時，`firstIdx` 就是 target 最左邊的索引（找不到則為 -1）

---

<!-- 23a6ad2bc5bd -->
#### 解法

<!--CODE-->

**改用 lower_bound 風格的寫法（V1）：**
<!--CODE-->

---

<!-- cbf3c4020a50 -->
#### 圖解範例

<!--CODE-->

---

<!-- 9a3c00da218a -->
#### 找第一個索引 — 與相似模式的比較

| 模式 | `nums[mid] == target` 時 | 迴圈結束後 | 回傳 |
|---------|---------------------------|-----------|---------|
| **標準二分搜尋** | `return mid` | 不適用 | 精確索引或 -1 |
| **找第一個（左邊界）** | `firstIdx = mid; high = mid-1` | `firstIdx` | 最左索引或 -1 |
| **找最後一個（右邊界）** | `lastIdx = mid; low = mid+1` | `lastIdx` | 最右索引或 -1 |
| **Lower Bound** | `right = mid`（半開區間） | `left` | 第一個 >= target 的索引 |

---

<!-- 8e3830ca8ac6 -->
#### 相似的 LC 題目

| 題目 | 核心想法 | 難度 |
|---------|-----------|------------|
| **LC 1150** | 找第一個索引 + 跳 N/2 驗證多數性 | Easy（Prime） |
| LC 34 | 兩次邊界搜尋，找第一個和最後一個出現位置 | Medium |
| LC 35 | 找插入位置（迴圈後回傳 `left`） | Easy |
| LC 278 | First Bad Version — 找條件第一次為真的索引 | Easy |
| LC 153 | 在旋轉排序陣列中找最小值 | Medium |
| LC 374 | Guess Number Higher or Lower — 經典的找第一個 | Easy |
| LC 540 | Single Element in a Sorted Array — 用奇偶性做邊界搜尋 | Medium |
| LC 852 | Peak Index in a Mountain Array — 找第一個開始下降的點 | Medium |

---

<!-- 3a4885d4b473 -->
#### 「找第一個」模式的面試技巧

1. **看到排序陣列 + 計數查詢** → 想「找第一個索引 + O(1) 檢查」
2. **關鍵那一行**：`return majorityIndex < n && nums[majorityIndex] == target` — 邊界檢查很重要
3. **為什麼不用數的？** 數是 O(N)，二分搜尋是 O(log N) — 面試官要的是最佳解
4. **邊界情況**：target 不在陣列裡、只有一個元素、全部元素都等於 target

---

<!-- 20a3402eeac8 -->
### 16) Find Right Interval (LC 436) ⭐⭐⭐⭐

**做法**：把起點排序 + 做 **lower-bound** 二分搜尋，再把排序後的位置對回**原始索引**。

<!-- 4831cac57e65 -->
#### 1) 核心想法

> 對每個區間 `[start_i, end_i]`，找出 `start_j` 是**所有 `>= end_i` 的起點中最小的**那個區間 `j` —
> 這就是一個 **lower-bound**（第一個 `>=` target）搜尋。

麻煩的地方：答案要的是那個區間的**原始索引**，但二分搜尋需要起點是**排序好的**。所以我們把
`(start, original_index)` 成對一起排序 — 排序會讓每個起點跟它的原始索引黏在一起，
定位到排序陣列中的位置後，直接從這個 pair 讀出原始索引就好。

<!--CODE-->

**為什麼能二分搜尋？** 起點互不相同，排序後就是**單調的** — 剛好符合 lower-bound 的謂詞
`start >= end_i`（False…False, True…True）。總複雜度：`O(n log n)`。

<!-- 17d9aeae5dea -->
#### 2) 模式 — 帶索引排序 + Lower Bound

<!--CODE-->

**用 `bisect` 更乾淨**（把排序後的起點抽出來，`bisect_left` 就是第一個 `>=`）：

<!--CODE-->

> **常見陷阱（TLE）**：暴力做法「排序後用雙層迴圈重建索引對照」是 `O(n^2)`。
> 整件事的重點就是把內層那趟掃描換成 `O(log n)` 的 lower-bound 搜尋 —
> 認出**第一個 `>=` target** 這個形狀才是關鍵。

**帶索引排序的通用做法（可重複套用）**：題目要排序資料、但答案得是*原始*位置時，
在排序**之前**先把每個值跟索引配成對（`(val, idx)`），排序這些 pair，對值做二分搜尋，
再讀 `pair[1]` 拿回原始索引。LC 315 / LC 493 用的是同一招。

<!-- 13f4da1c7dd5 -->
#### 3) 相似的 LC 題目

| 題目 | LC# | 二分搜尋扮演的角色 | 變化點 |
|---------|-----|--------------------|-------|
| **Find Right Interval** | **436** | lower bound：第一個 `start >= end_i` | 排序後位置 → 原始索引 |
| Search Insert Position | 35 | lower bound：第一個 `>= target` | 直接回傳插入索引 |
| Time Based Key-Value Store | 981 | **upper bound − 1**：時間戳的 floor | 每個 key 一份排序好的時間戳清單 |
| Two Sum II (sorted) | 167 | 在排序好的另一半中搜補數 | 也可以用雙指標 |
| Find First and Last Position | 34 | 左邊界 + 右邊界搜尋 | 兩次 lower/upper-bound 呼叫 |
| Count of Smaller After Self | 315 | `SortedList` + `bisect`，由右往左掃 | 保留索引的計數 |
| My Calendar I | 729 | 用 `SortedDict` 做 floor/ceiling | 在有序 map 上檢查重疊 |
| Data Stream as Disjoint Intervals | 352 | 用 floor/ceiling 合併區間 | 有序的區間 map |

> **辨識訊號**：對一個可以**一次排序完**的集合，「對每個元素，找出*最小的 `>=` X*（或*最大的
> `<=` X*）」→ 排序 + lower/upper-bound 二分搜尋。
> 如果這個集合**會隨時間變動**，改用 `SortedList`／`SortedDict`（見
> [python_trick.md §1-27-3](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md)）。

---

<!-- 09a4326748a5 -->
### 17) Find in Mountain Array (LC 1095) ⭐⭐⭐⭐⭐

> **山形陣列**先嚴格遞增到峰值，再嚴格遞減。找出值等於 `target` 的**最小索引**（沒有就回 `-1`），只能用 `arr.get(i)` / `arr.length()` — 而且 `get()` 呼叫次數要越少越好。

<!-- 99d68de38d70 -->
#### 核心想法 — 三次二分搜尋，不是一次

陣列本身沒有排序，但它是**兩段排序好的區間接起來**的。所以：

<!--CODE-->

先搜左半段，因為題目要的是最小索引。

讓步驟 3 成立的**遞減方向翻轉**技巧，在
[binary_search.md](./binary_search.md) §2.4 講過一次。
<!--CODE-->

<!--CODE-->

**面試筆記**
- `arr.get()` 是**有次數限制的 API**（LC 1095 上限 100 次） — 什麼都不用快取，把呼叫次數控制在 `3 log n` 就好。絕對不要線性掃描去找峰值。
- 找峰值用的是 `while (l < r)` 搭配 `r = mid` — 為什麼 `l <= r` 會衝過 `mid + 1`，見 §2（Find Peak Element）。
- 找峰值會比較 `a[mid]` 和 `a[mid+1]`，所以 `mid + 1` 必須在範圍內 — 因為 `r = n - 1` 且 `mid < r`，這點有保證。

**相似題目**

| LC | 題目 | 關聯 |
|----|---------|----------|
| **1095** | Find in Mountain Array | 本題 — 找峰值 + 兩次有序搜尋 |
| 852 | Peak Index in a Mountain Array | 只有步驟 1（找峰值） |
| 162 | Find Peak Element | 不保證是山形時的找峰值 |
| 33 / 153 | Search in Rotated Sorted Array | 同樣是「兩段排序區間」，但切分規則不同（[binary_search.md](./binary_search.md) §1.2） |

---

<!-- 2dd722b12a5a -->
### 18) Longest Increasing Subsequence — `tails` 陣列 (LC 300) ⭐⭐⭐⭐⭐

> 用 `O(n log n)` 求最長遞增子序列。`O(n²)` 的 DP 是預期中的第一個答案；二分搜尋版才是 FAANG 面試官接著要的追問。

<!-- f415fda21e77 -->
#### 核心想法

維護 `tails[k]` = 長度為 `k + 1` 的遞增子序列，**可能的最小結尾值**。

- `tails` **永遠是遞增排序的** → 可以二分搜尋。
- 對每個 `x`，找 `lower_bound(tails, x)`（第一個 `>= x` 的 tail）：
  - 索引 `== len(tails)` → `x` 把最長的那串再延長 → **append**
  - 否則 → 用比較小的 `x` **覆蓋**那個 tail（保留未來的可能性）
- 答案 = `len(tails)`。

> `tails` **不是**真正的子序列 — 只有它的**長度**有意義。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**變形 — 嚴格遞增 vs 非遞減**（經典的差一位陷阱）：

<!--CODE-->

**變形 — LC 354 Russian Doll Envelopes** = 二維的 LIS。寬度**遞增**排序，寬度相同時高度**遞減**排序（這樣同寬的兩個信封絕不會同時被選中），然後對高度跑一模一樣的 LIS：

<!--CODE-->

<!--CODE-->

**相關 — 把二分搜尋當成 DP 轉移的查表**

| LC | 題目 | 二分搜尋怎麼用 |
|----|---------|---------------------------|
| **300** | Longest Increasing Subsequence | `tails` + lower bound |
| **354** | Russian Doll Envelopes | 按 (w 遞增, h 遞減) 排序 → 對高度做 LIS |
| 1235 | Maximum Profit in Job Scheduling | 工作按結束時間排序；二分搜尋**最後一個結束時間 `<= start_i` 的工作**，然後 `dp[i] = max(dp[i-1], profit + dp[j])` |
| 1751 | Maximum Number of Events That Can Be Attended II | 同樣是「按結束時間排序 + 二分搜尋前一個相容項」的 DP，多一個 `k` 場次的維度 |
| 1027 | Longest Arithmetic Subsequence | 對 `(index, diff)` 做 DP — 用雜湊表，**不是**二分搜尋（要分得出差別） |

---

<!-- 68565844b1ba -->
### 19) Random Pick with Weight (LC 528) ⭐⭐⭐⭐

> `pickIndex()` 回傳索引 `i` 的機率必須是 `w[i] / sum(w)`。

<!-- bb04c18c2409 -->
#### 核心想法 — 把權重變成連續區段，再二分搜尋

先算前綴和，在 `[1, total]` 之間抽一個均勻整數 `target`，回傳**第一個 `>= target` 的前綴和**（也就是 `lower_bound`）。每個索引 `i` 剛好佔掉 `total` 個位置中的 `w[i]` 個 → 機率正好是 `w[i] / total`。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**差一位的防呆** — 挑**一種**慣例，然後從一而終：
- 在 `[1, total]` 抽 → `bisect_left`（第一個前綴和 `>= target`） ✅（上面用的就是這個）
- 在 `[0, total)` 抽 → `bisect_right`（第一個前綴和 `> target`）
- 兩者混用會安靜地讓索引 `0` 的機率變成 `0`，或產生超出範圍的索引。

**相似題目 — 「對排序好的歷史／累積陣列做二分搜尋」**

| LC | 題目 | 排序陣列裡放什麼 | 查詢方式 |
|----|---------|------------------------------|-------|
| **528** | Random Pick with Weight | 權重的前綴和 | 對隨機抽樣做 lower bound |
| 497 | Random Point in Non-overlapping Rectangles | 每個矩形點數的前綴和 | 一樣的 lower bound，再在矩形內挑點 |
| 911 | Online Election | 時間陣列 + 該時刻的領先者陣列 | upper bound − 1（時間的 floor） |
| 1146 | Snapshot Array | 每個索引一份 `(snap_id, val)` 清單 | upper bound − 1（最新且 `<= snap_id` 的值） |
| 1348 | Tweet Counts Per Frequency | 每個名稱一份排序好的推文時間 | 兩個邊界 → 數出 `[start, end]` 內的數量 |
| 981 | Time Based Key-Value Store | 每個 key 一份排序好的時間戳 | upper bound − 1（上面 §13） |

> 這六題是同一個模板：**維護一個排序好的陣列，每次查詢用 `lower_bound` / `upper_bound − 1` 回答。** 變的只是陣列裡裝什麼。

---

<!-- 62c751997bd5 -->
## 依模式分類的題目

| 模板（在 [binary_search.md](./binary_search.md)） | 這裡的題目 |
|---|---|
| 標準精確搜尋，`while l <= r` — §2.1 | LC 167, LC 367, LC 69, LC 441 |
| Lower bound／第一個 `>=` — §1.3 | LC 278, LC 744, LC 436, LC 1150, LC 528, LC 300 |
| Upper bound − 1／floor 查詢 — §1.3 | LC 981 |
| 半開區間 `while l < r`、`r = mid` — §2.0 | LC 162, LC 852, LC 540, LC 154 |
| 用差距的 `while l < r - 1` — §2.0 | LC 1060 |
| 旋轉／兩段排序區間 — §1.2, §2.4 | LC 154, LC 1095 |
| 二分搜尋切分點，而不是索引 — §2.0 | LC 4 |
| 二分搜尋長度 + 視窗驗證 — §1.4 | LC 209 |

<!-- 2d234a9480d4 -->
## 總結

- **這份存放處不是模板目錄。** 這裡有題目讓你看不懂，先回主文件讀它的模板 — 下面每一份解法都是某個模板的實例。
- **`lower_bound` 是主力。** LC 278、744、436、1150、528、300 全都是「單調謂詞第一次為真的索引」，只是寫成了六種樣子。
- **`while l < r` 搭配 `r = mid` 是為了收斂，不是為了比對** — LC 162、540、154 和 1095 的找峰值都不測試相等；它們是把 `l` 和 `r` 擠到同一個索引上。
- **看資料，不是看搜尋。** LC 436、981 和 528 全都是「維護一個排序陣列，每次查詢用邊界回答」 — 變的只有陣列裡存什麼。
