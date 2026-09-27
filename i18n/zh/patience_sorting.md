<!-- e36532ecbc92 -->
# Patience Sorting — 以 O(N log N) 求 LIS

> **範圍** — `O(N log N)` 最長遞增子序列背後的紙牌遊戲演算法：牌堆、牌堆收斂成的 `tails` 陣列、如何還原子序列本身，以及可以歸約到它的題目家族。
> **另見**：[binary_search.md](./binary_search.md) §1.5 — 這個掃描所依賴的 lower-bound 模板，以及「每個元素寫入一次就是*完整*的 DP 更新」的證明；[binary_search_examples.md](./binary_search_examples.md) §18 — 完整解過的 LC 300 / LC 354；[dp_pattern.md](./dp_pattern.md) — 本法所取代的 `O(n²)` LIS DP，以及那些無法被取代的 LIS 形 DP；[sort.md](./sort.md) — 相鄰的排序演算法。

- **核心概念**：把陣列當成一局接龍（patience）發牌 — 每張牌放到最左邊、頂牌 `>= card` 的牌堆上，沒有這種牌堆就開一個新牌堆 — 而**牌堆的數量就是 LIS 的長度**
- **何時使用**：求最長的遞增／可串接序列，只需要它的**長度**，而 `O(n²)` DP 太慢
- **關鍵 LeetCode 題目**：LC 300、LC 334、LC 354、LC 1964、LC 2111、LC 1713、LC 1671
- **資料結構**：一個已排序的牌堆頂陣列（`tails`）；若需要子序列本身，再加上父指標
- **典型狀態**：`tails[k]` = 長度為 `k + 1` 的遞增序列所能擁有的最小結尾值

**時間複雜度：** O(N log N) — N 張牌 × 每張在至多 N 個牌堆頂上做一次二分搜尋
**空間複雜度：** O(N)

**實作**：[`algorithm/python/patience_sorting.py`](../../algorithm/python/patience_sorting.py) — 涵蓋下方全部五種形式，並以隨機測試與 `O(n²)` DP 交叉驗證。

<!-- f52c260c850e -->
## LeetCode 題目清單

- [Binary Search](https://leetcode.com/problem-list/binary-search/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Greedy](https://leetcode.com/problem-list/greedy/)

<!-- 442b0cdc3b57 -->
## 0) 概念

<!-- 488011cfe032 -->
### 0-0) 核心原理 — 把陣列發成牌堆 ⭐⭐⭐⭐⭐

三條規則，由左到右作用在輸入上：

1. 把牌放到**最左邊、頂牌 `>= card` 的牌堆**上。
2. 如果沒有符合的牌堆，就在右邊**開一個新牌堆**。
3. 發完牌時，**牌堆的數量就是 LIS 的長度**。

<!--CODE-->

有兩個事實讓這成為演算法，而不只是紙牌把戲：

- **牌堆頂由左到右遞增。** 落在某牌堆上的牌*小於或等於*該堆的頂牌，且*嚴格大於*左邊那堆的頂牌；開新牌堆的牌則大於所有頂牌。無論哪種情況，頂牌都維持有序 — 也就是說「最左邊、頂牌 `>= x` 的牌堆」就是單純的 `lower_bound`（`bisect_left`），成本是 `O(log P)` 而不是 `O(P)`。
- **只有頂牌會被讀取。** 把牌堆收斂成各自的頂牌，就得到面試時大家寫的 `tails` 陣列：

<!--CODE-->

<!-- d2c3687e5984 -->
### 0-1) 類型

1. **只求長度** — 單純的掃描，答案是 `len(tails)`（LC 300）
2. **每個索引的長度** — 邊掃邊回報插入位置，每個元素一個答案（LC 1964）
3. **子序列本身** — 同樣的掃描加上父指標（§1-4）
4. **二維，先排序** — 對一個維度排序，讓另一個維度變成一維 LIS（LC 354）
5. **歸約** — 把另一個問題改寫成 LIS：其中一邊元素互異的 LCS、`k` 條交錯的序列（LC 1713、LC 2111）
6. **非遞減而非嚴格遞增** — 只差一個字元，改用 `bisect_right`（§1-2）

<!-- 29b4101d1950 -->
### 0-2) 為什麼牌堆數*就是* LIS ⭐⭐⭐⭐⭐

兩個方向，各一行，合起來就是完整的正確性證明：

<!--CODE-->

從兩邊夾擠，`piles == LIS`。面試時點出它的名字是加分訊號：這就是 **Dilworth 定理** — 牌堆構成陣列的一個*最小非遞增覆蓋*，而這種覆蓋的最小大小等於最長遞增子序列的長度。

> 同一件事用 `tails` 的語言來講 — 有序陣列的不變量，以及每個元素恰好只有一個格子
> 可能被改進的證明 — 在 [binary_search.md](./binary_search.md) §1.5。
> 牌堆觀點用來理解*為什麼*，`tails` 觀點用來寫*程式碼*。

<!-- e3b903ae15aa -->
### 0-3) 模式 — 什麼時候適用 ⭐⭐⭐⭐⭐

當以下**三個條件全部**成立時，就用 patience sorting：

| 條件 | 為什麼重要 |
|---|---|
| 答案是一個**長度**（或 `n − length`），不是方法數，也不是加權總和 | 每個牌堆一個值只能承載「多長」，永遠無法承載「有幾種」或「有多少」 |
| 「可串接」是單一鍵上的**全序** — 數字上的 `<`，或先排序過的某個鍵上的 `<` | 牌堆頂必須概括到目前為止進度的*一切* |
| 直覺解法是對 `j < i` 做 `dp[i] = max(dp[j]) + 1`，而你需要更快 | 內層那個 `max` 正是 lower bound 所取代的東西 |

**一句話判別**：`tails` 可行，當且僅當一條鏈上的進度能用**一個可比較的數字**概括，且你只想知道**多長**。

三個條件任一不成立，就退回 `O(n²)` DP 或 Fenwick tree — 見 §2-8 的陷阱表。

<!-- 0de0d3d03b4b -->
## 1) 通用形式

<!-- 456006490977 -->
### 1-1) 基本操作 — `tails` 掃描 ⭐⭐⭐⭐⭐

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- 5f42dd99f9a8 -->
### 1-2) 嚴格遞增 vs 非遞減 — 只差一個字元

這個家族最常見的錯誤答案，就是用錯 `bisect`：

| 目標 | 在 `tails` 上的查詢 | Python | 遇到相等值的效果 |
|---|---|---|---|
| **嚴格**遞增（LC 300） | 第一個 `>= num` 的 tail | `bisect_left` | 落在相等的 tail *上* → **覆寫**它，不增長 |
| **非遞減**（允許重複） | 第一個 `> num` 的 tail | `bisect_right` | 落在相等的 tail *之後* → **延長**序列 |
| 非遞增／遞減 | 把輸入取負，再套上面的做法 | 對 `-num` 做 `bisect_*` | — |

<!--CODE-->

<!--CODE-->

<!-- fd920020b23b -->
### 1-3) 保留牌堆

求長度時很少需要，但這才是演算法的原貌 — 也正是 patience *sorting* 之所以是一種排序的原因（之後把牌堆合併起來，就像 Timsort 合併 run 一樣）：

<!--CODE-->

<!-- 434dc856086a -->
### 1-4) 還原子序列，而不只是長度 ⭐⭐⭐⭐

`tails` **不是**一個子序列 — 只有它的長度有意義：

<!--CODE-->

要交回真正的序列，就記住*每張牌落下時接在誰後面*：也就是放置當下它左邊那堆的 tail。

<!--CODE-->

- 往回走得到的是**某一條**最長子序列，而不是標準唯一的一條：
  `[10,9,2,5,3,7,101,18]` 會得到 `[2,3,7,18]`，因為 `18` 取代了 `101` 成為長度 4 的 tail。
- 即使 `tails_idx[l-1]` 之後會被覆寫，`prev[i]` 仍然安全：它是在**放置當下**記錄的，那時該元素確實排在 `nums[i]` 之前。

<!-- 9989f7cf569e -->
### 1-5) 複雜度與真正會咬人的陷阱

| | |
|---|---|
| 時間 | `O(n log n)` — 每個元素在至多 `n` 個頂牌上做一次 `bisect` |
| 空間 | `tails` 為 `O(n)`；還原再多 `O(n)`；保留完整牌堆總共 `O(n)` |
| 最壞情況 | 遞增輸入會產生 `n` 個牌堆，遞減輸入只有 1 個 — 兩者都仍是 `O(n log n)` |

- ❌ 把 `tails` 當成答案子序列來讀（§1-4）。
- ❌ 題目允許重複卻用 `bisect_left`，或不允許重複卻用 `bisect_right`（§1-2）。
- ❌ 為了「幫忙」而先排序輸入。排序會破壞子序列所依據的順序 — 唯一合法的排序是 LC 354 裡那個*刻意*的排序，因為掃描接著跑的是第二個維度。
- ❌ 問題問的是「有幾條」（LC 673）或「最大總和」（LC 2926）時還拿它來用 — 見 §2-8。

<!-- 5fa13fe640ce -->
## 2) LC 範例

<!-- 1f97ea620b55 -->
### 2-1) Longest Increasing Subsequence — LC 300 ⭐⭐⭐⭐⭐

就是 §1-1 的單純掃描，答案是 `len(tails)`。`O(n²)` DP 是預期的第一個答案，`O(n log n)` 掃描則是追問；兩者完整的解說與 dry-run 表格在 [binary_search_examples.md](./binary_search_examples.md) §18。

<!-- 55da57735ecd -->
### 2-2) Increasing Triplet Subsequence — LC 334 ⭐⭐⭐⭐

那個有名的雙變數技巧**就是**這個演算法，只是 `tails` 被限制在兩個牌堆：`first`/`second` 就是 `tails[0]`/`tails[1]`，而「第三個牌堆將會開啟」就是答案。

<!--CODE-->

不需要二分搜尋：只有兩個格子時，線性掃描*就是* lower bound，這也是它看起來像另一個演算法、其實不是的原因。

<!-- fadf8cf36be4 -->
### 2-3) Russian Doll Envelopes — LC 354 ⭐⭐⭐⭐⭐

二維的 LIS。寬度**遞增**排序，寬度相同時高度**遞減**，然後只對高度做掃描。平手規則就是整道題的關鍵：高度遞減時，兩個寬度相同的信封永遠不可能同時被選中，因為後面那個的高度較小，無法接在前面那個之後。

程式碼 — 以及把同樣技巧用在 LC 1996 — 在 [binary_search_examples.md](./binary_search_examples.md) §18。

<!-- be95ebd3c982 -->
### 2-4) Longest Valid Obstacle Course at Each Position — LC 1964 ⭐⭐⭐⭐

掃描*每一步*其實都已經知道答案：一張牌落在哪個牌堆，就是以這張牌結尾的最長序列長度。把它回報出來，而不只回報最終的數量。

<!--CODE-->

<!-- 2c2ec2f376c4 -->
### 2-5) Minimum Operations to Make the Array K-Increasing — LC 2111 ⭐⭐⭐⭐

`arr[i-k] <= arr[i]` 只會牽連到模 `k` 同餘類中的索引，所以這個陣列其實是 `k` 條彼此獨立的序列。每條保留它的最長非遞減子序列，其餘全部替換掉。

<!--CODE-->

<!-- fcd3f432e8a7 -->
### 2-6) Minimum Operations to Make a Subsequence — LC 1713 ⭐⭐⭐⭐⭐

這題值得一眼認出：它讀起來像 LCS，而 LCS 是 `O(n·m)` — 但 `target` 的值**互異**，所以把 `arr` 的每個值改寫成它在 `target` 中的位置，「公共子序列」就變成了「遞增子序列」。

<!--CODE-->

這個歸約需要互異性：有重複時，一個值會對應到多個索引，「遞增」就不再能刻畫「公共」。

<!-- 07da7053d07c -->
### 2-7) Minimum Number of Removals to Make Mountain Array — LC 1671 ⭐⭐⭐⭐

山形就是一段遞增序列和一段遞減序列在共同的山頂相接，所以把掃描跑**兩次** — 正向求以 `i` 結尾的序列，反向求從 `i` 開始的序列。

<!--CODE-->

<!-- 21ee529d7293 -->
### 2-8) 經典題一覽

**直接用掃描解決**（有時要先刻意排序）：

| LC # | 題目 | 變化之處 |
|------|---------|--------------|
| **300** | Longest Increasing Subsequence | 基準 — `bisect_left`，答案 `len(tails)` |
| **334** | Increasing Triplet Subsequence | `tails` 限制為 2 → 兩個變數，`O(1)` 空間 |
| **354** | Russian Doll Envelopes | 以 `(w asc, h desc)` 排序，再對高度掃描 |
| **646** | Maximum Length of Pair Chain | 同樣是最長鏈問題；依結尾排序的貪婪比較簡單，但這個也行 |
| **435** | Non-overlapping Intervals | `n −` LC 646 的鏈長 |
| **1964** | Longest Valid Obstacle Course at Each Position | `bisect_right`，並在每一步回報落點索引 |
| **2111** | Minimum Operations to Make the Array K-Increasing | `k` 個同餘類，各自用非遞減變體 |
| **1671** | Minimum Number of Removals to Make Mountain Array | 掃描正向、反向各跑一次 |
| **1713** | Minimum Operations to Make a Subsequence | 一邊互異的 LCS → 對映射後索引做 LIS |

**近親** — 同樣是「對你維護的結構做二分搜尋」，但存的值是 DP 結果而不是 tail：LC 1235（Maximum Profit in Job Scheduling）、LC 1751（Maximum Number of Events That Can Be Attended II）、LC 981、LC 528 — 都在 [binary_search_examples.md](./binary_search_examples.md)。

**陷阱** — 長得像 LIS，但不是這個演算法：

| LC # | 題目 | 為什麼 `tails` 不行 | 改用 |
|------|---------|-------------------|-------------|
| **673** | Number of Longest Increasing Subsequence | 求的是**數量**而非長度 — 每個長度一個 tail 無法承載重數 | 帶平行計數陣列的 `O(n²)` DP，或在值域上用 BIT |
| **368** | Largest Divisible Subset | 整除**不是全序**，沒有單一值能概括一條鏈 | `O(n²)` DP + 父指標 |
| **1691** | Maximum Height by Stacking Cuboids | 串接需要三個維度都 `<=` — 同樣是非全序的失敗 | 每個長方體的維度各自排序、再排序整個清單，`O(n²)` DP |
| **1027** | Longest Arithmetic Subsequence | 狀態是 `(index, difference)` | 雜湊表 DP |
| **1218** | Longest Arithmetic Subsequence of Given Difference | 鏈以值為鍵，而不是以順序 | 一個雜湊表，`O(n)` |
| **2926** | Maximum Balanced Subsequence Sum | 最大化的是**總和**，所以每個長度的最佳值不是單一可比較的 tail | 在前綴最大值上用 Fenwick tree |

<!-- 8787619dd4f6 -->
## 總結

- **每次都是同樣三條規則**：頂牌 `>= x` 的最左牌堆，否則開新牌堆，答案 = 牌堆數。把牌堆收斂成頂牌，就得到 `tails` 和一個 `lower_bound`。
- **正確性只要兩行**：牌堆是非遞增的，所以 `LIS <= piles`；一張牌沿著牌堆往回的鏈是遞增的，所以 `LIS >= piles`（Dilworth）。
- **嚴格遞增用 `bisect_left`，非遞減用 `bisect_right`** — 整個家族最常見的 bug。
- **落點索引就是逐元素的資訊** — LC 1964、LC 1671 不需要第二趟。
- **一個長度、一個全序、一個可比較的鍵。** 少了任何一個，答案就是 `O(n²)` DP 或 Fenwick tree，而不是更聰明的 `bisect`。

<!-- 7d00277348f4 -->
### 參考資料

- [Patience sorting（維基百科）](https://en.wikipedia.org/wiki/Patience_sorting)
- [最長遞增子序列（維基百科）](https://en.wikipedia.org/wiki/Longest_increasing_subsequence)
- [Dilworth 定理（維基百科）](https://en.wikipedia.org/wiki/Dilworth%27s_theorem)
- [`algorithm/python/patience_sorting.py`](../../algorithm/python/patience_sorting.py) — 可執行，附測試
