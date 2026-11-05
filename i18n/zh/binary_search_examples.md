<!-- 2a22dae14e51 -->
# 二分搜尋 — 實戰題解

> **範圍** — [binary_search.md](./binary_search.md) 的題組：每題歸檔在它所歸約的索引空間模板底下，寫成一張題目卡、一份標準解，另外收錄少數沒在 [binary_search_on_answer.md](./binary_search_on_answer.md) 解過的答案空間題。
> **另見** — *主文件*：[binary_search.md](./binary_search.md) — 迴圈不變式、邊界模板，以及「該用哪個模板」的決策表；[binary_search_on_answer.md](./binary_search_on_answer.md) — 所有對*答案*而非索引做二分搜尋的題目。
> *鄰近文件*：[n_sum.md](./n_sum.md) — LC 167 的收斂指標解法；[sort.md](./sort.md) — LC 354 的先排序再 LIS 解法；[patience_sorting.md](./patience_sorting.md) — 歸約到 LIS 的題目；[matrix.md](./matrix.md) — 二維網格題型家族。

<!-- d25f0f2274da -->
## LeetCode 題目清單

- [Binary Search](https://leetcode.com/problem-list/binary-search/)

<!-- a3d7661069c6 -->
## 概覽

[binary_search.md](./binary_search.md) 收著八個索引空間模板；這一頁是題目。下面每一節對應一個模板，
順序與主文件相同，每張卡片只說明它比模板多出的那一步。錨定題本身就是全部重點的模板
（模板 4、6、8）在這裡沒有獨立一節。最後一節收錄歸約到
[binary_search_on_answer.md](./binary_search_on_answer.md) 卡片的答案空間題，指標表則列出在其他文件解過的題目。

<!-- c33de905c402 -->
## 索引

| 模式卡 | 題目（易 → 難） |
|---|---|
| [Template 1: Exact Match on a Closed Interval](./binary_search.md#template-1-exact-match-on-a-closed-interval--lc-704-) | [LC 4](#median-of-two-sorted-arrays--lc-4-) |
| [Template 2: Lower and Upper Bound](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) | [LC 278](#first-bad-version--lc-278) · [LC 744](#find-smallest-letter-greater-than-target--lc-744) · [LC 1150](#check-if-a-number-is-majority-element-in-a-sorted-array--lc-1150) · [LC 540](#single-element-in-a-sorted-array--lc-540) · [LC 658](#find-k-closest-elements--lc-658) · [LC 436](#find-right-interval--lc-436-) |
| [Template 3: Rotated Array Minimum](./binary_search.md#template-3-rotated-array-minimum--lc-153-) | [LC 154](#find-minimum-in-rotated-sorted-array-ii--lc-154) |
| [Template 7: Peak Finding by Slope](./binary_search.md#template-7-peak-finding-by-slope--lc-162-) | [LC 852](#peak-index-in-a-mountain-array--lc-852) · [LC 1095](#find-in-mountain-array--lc-1095-) |
| [binary_search_on_answer § Template 1: Smallest Feasible Value](./binary_search_on_answer.md#template-1-smallest-feasible-value--lc-875-) | [LC 367](#valid-perfect-square--lc-367) · [LC 1060](#missing-element-in-sorted-array--lc-1060) |
| [binary_search_on_answer § Template 2: Largest Feasible Value](./binary_search_on_answer.md#template-2-largest-feasible-value--lc-1552-) | [LC 441](#arranging-coins--lc-441) |

**在別處解** — 只解一次，放在擁有它的那份文件：

| LC | 題目 | 所在位置 | 為什麼在那裡 |
|---|---|---|---|
| 162 | Find Peak Element | [binary_search.md § Template 7](./binary_search.md#template-7-peak-finding-by-slope--lc-162-) | 該模板的錨定題 |
| 300 | Longest Increasing Subsequence | [binary_search.md § Template 5](./binary_search.md#template-5-lower-bound-on-a-maintained-tails-array--lc-300-) | 該模板的錨定題 |
| 354 | Russian Doll Envelopes | [sort.md § Template 9](./sort.md#template-9-sort-by-a-derived-key-to-unlock-greedy--dp--lc-354-) | 重點是帶平手規則的排序；之後的 LIS 就是模板 5 |
| 167 | Two Sum II — Input Array Is Sorted | [n_sum.md](./n_sum.md#2-1-2-sum-ii--lc-167) | 雙指標是 O(n)；每個元素各做一次二分搜尋是 O(n log n) |
| 209 | Minimum Size Subarray Sum | [sliding_window.md § Template 4](./sliding_window.md#template-4-shortest-window-satisfying-p--lc-209-) | 滑動視窗是 O(n)；前綴和 + 每個起點一次 lower bound 是 O(n log n) 的追問 |
| 69 | Sqrt(x) | [math.md § Integer Square Root](./math.md#1-1-10-integer-square-root--binary-search--newton-) | 對答案二分搜尋與牛頓法並排 |
| 528 | Random Pick with Weight | [math.md § Randomized Sampling](./math.md#1-1-11-randomized-sampling-templates-) | 前綴和 + 對均勻抽樣做 lower bound |
| 981 | Time Based Key-Value Store | [design_examples.md](./design_examples.md#time-based-key-value-store--lc-981) | 每個 key 一份排序清單，用 upper bound − 1 查詢 |
| 287 | Find the Duplicate Number | [binary_search_on_answer.md § Find the Duplicate Number](./binary_search_on_answer.md#find-the-duplicate-number--lc-287-) | 在值域上二分搜尋，配合鴿籠計數 |

<!-- afb2ed64be4a -->
## 精確比對範例

<!-- c3585770a476 -->
### Median of Two Sorted Arrays — LC 4 ⭐⭐⭐⭐⭐

> **關鍵** — 搜尋的是較短陣列裡的**切點**，不是某個值。`nums1` 的切點 `i` 決定了 `nums2` 的切點
> `j = half − i`；當兩個左側最大值都 `<=` 兩個右側最小值時切點就對了，切錯時，看是哪個不等式
> 不成立就知道該往哪邊移。

**歸約到**：[binary_search § Template 1](./binary_search.md#template-1-exact-match-on-a-closed-interval--lc-704-) — 同一個閉區間迴圈、三種結果（找到、往左、往右），只是搜尋的是切點位置 `[0, m]` 而不是索引，判斷用跨陣列的比較而不是 `nums[mid] == target`。

<!--CODE-->

<!--CODE-->

**陷阱**
- 搜尋較長的陣列會讓 `j = half − i` 變成負數：`nums1 = [1, 2, 3, 4]`、`nums2 = [5]` 可能探測到
  `i = 4`，於是 `j = −1`。先交換，讓 `nums1` 是較短的那個。
- 空的一邊需要 `±inf` 哨兵：`nums1 = []`、`nums2 = [1]` 時 `i = 0`，沒有 `nums1[i − 1]`。
- Java 中兩個大 int 的 `max + min` 會在除法前溢位：以 `double` 相加。

**相似題** — LC 2040 *（第 k 小的乘積：[binary_search_on_answer § Template 4](./binary_search_on_answer.md#template-4-count-at-most-over-a-value-range--lc-378-)）*

<!-- 18b390808623 -->
## 下界與上界範例

<!-- 3bb10d980c70 -->
### First Bad Version — LC 278

> **關鍵** — `isBadVersion` 讀起來是 `False … False True … True`，所以第一個壞版本就是模板 2 的
> `findLeft`，比較換成呼叫 API。沒有陣列，也沒有 target。

**歸約到**：[binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — 把 `nums[mid] >= target` 換成 `isBadVersion(mid)` 的 `findLeft`；答案一定存在，所以不用驗證。

<!--CODE-->

<!--CODE-->

**陷阱**
- Java 中 `(left + right) / 2` 會溢位：`n = 2126753390` 就是 LeetCode 自己的測資。

**相似題** — LC 374 *（猜數字 API，精確比對）* · LC 35 *（插入位置）*

<!-- dcc33b5df826 -->
### Find Smallest Letter Greater Than Target — LC 744

> **關鍵** — 「**嚴格大於** target 的最小字母」是 upper bound（第一個 `> target` 的字母）。
> 沒有更大的字母時答案繞回 `letters[0]`，`% n` 直接處理掉。

**歸約到**：[binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — 在字元上做 `findRight + 1`（upper bound），再把索引繞回。

<!--CODE-->

**陷阱**
- 用 `letters[mid] < target` 找到的是第一個 `>= target` 的字母：`letters = ["c","f","j"]`、
  `target = "c"` 會回傳 `"c"` 而不是 `"f"`。
- 允許重複：`["e","e","e","n"]`、`target = "e"` 必須跳過每個 `"e"`，`<=` 就能做到。

<!-- ee13907134d3 -->
### Check If a Number Is Majority Element in a Sorted Array — LC 1150

> **關鍵** — 在排序陣列裡，同一個值佔據一段連續區間。找出 `target` 那段的起點；
> 它是多數元素，恰好當往後 `n // 2` 個位置的元素仍是 `target`。

**歸約到**：[binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — 一次 `findLeft`，接著一次 O(1) 檢查，取代第二次搜尋。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 漏掉 `end < n` 的檢查：`nums = [1, 2]`、`target = 2` 起點是 1，`1 + 1 = 2` 已經越界。
- target 不存在時：lower bound 落在別的值上，檢查仍回傳 `False`，不需要另外判斷「有沒有找到」。

**相似題** — LC 34 *（區間的兩端）* · LC 169 *（未排序的多數元素：Boyer–Moore）*

<!-- b51996d32304 -->
### Single Element in a Sorted Array — LC 540

> **關鍵** — 在單一元素之前，每一對都從**偶數**索引開始；在它之後則從奇數索引開始。所以
> 「偶數 `mid` 那一對完整」是 `True … True False … False`，單一元素就是第一個不成立的偶數索引。

**歸約到**：[binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — 對奇偶判定式做 lower bound，只探測偶數索引。

<!--CODE-->

<!--CODE-->

**陷阱**
- 不把 `mid` 對齊到偶數，`nums[mid] == nums[mid + 1]` 可能比到一對的後半：
  `[1,1,2,3,3,4,4]` 探測 `mid = 3`，看到 `3 == 3`，跳到 `left = 5`，回傳 4 而不是 2。
- `mid + 1` 一定在範圍內，因為 `left < right` 且 `right` 永遠是偶數（長度是奇數）。

**相似題** — LC 136 *（未排序：O(n) 的 XOR）*

<!-- 2e0ff2250c7a -->
### Find K Closest Elements — LC 658

> **關鍵** — 答案是一個視窗 `arr[i .. i + k − 1]`，所以搜尋視窗的**起點**。比較平移時會互換的
> 兩個元素：若 `x − arr[i] > arr[i + k] − x`，從 `i + 1` 開始的視窗至少一樣近，而這個判斷只會從
> `True` 轉成 `False` 一次。

**歸約到**：[binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — 在視窗起點 `[0, n − k]` 上做 lower bound，判定式是「往右移不會更好」。

<!--CODE-->

**陷阱**
- 用 `abs(x - arr[mid]) > abs(arr[mid + k] - x)`，在兩者都在 `x` 同一側時會出錯：
  `arr = [1,1,2,2,2,2,2,3,3]`、`k = 3`、`x = 3` 回傳 `[2,2,2]` 而不是 `[2,3,3]`。保留帶正負號的差值。
- 用 `>=` 取代 `>` 會在平手時偏向較大的值；題目要較小的：
  `arr = [1,2,3,4,5]`、`k = 4`、`x = 3` 必須回傳 `[1,2,3,4]`。

**第二種做法** — 從兩端往內縮（丟掉比較遠的那端）直到剩 `k` 個：O(n − k)，想不出二分判定式時
就給這個版本。依距離排序（[sort.md](./sort.md)）是 O(n log n)。

<!-- 34a9bda0ac45 -->
### Find Right Interval — LC 436 ⭐⭐⭐⭐

> **關鍵** — 對每個區間，答案是所有區間中**最小的 `start >= end_i`**：在排序後的 start 上做 lower
> bound。答案必須是*原始*索引，所以排序 `(start, index)` 配對，再從搜尋落點的配對讀出索引。

**歸約到**：[binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — 每個區間一次 `findLeft`，在連同原始索引一起排序的 start 上搜尋。

<!--CODE-->

<!--CODE-->

**陷阱**
- 直接排序區間本身會弄丟答案需要的索引：在排序**之前**把每個 start 和它的索引配成一對。
- 區間可以是自己的右區間：`[[1,1]]` 回傳 `[0]`（start `1 >= end 1`）。
- 雙層迴圈是 O(n²)，在 n = 2·10⁴ 時會超時；重點就是每個區間只做一次 O(log n) 搜尋。

**相似題** — LC 35 *（最單純的 lower bound）* · LC 2300 *（每個 spell 一次 lower bound，數後綴）* · LC 1268 *（對逐步變長的前綴做 lower bound）*

<!-- a7bf2e713b7b -->
## 旋轉陣列最小值範例

<!-- 480e363950f3 -->
### Find Minimum in Rotated Sorted Array II — LC 154

> **關鍵** — 重複值帶來第三種情況：`nums[mid] == nums[right]` 無法判斷 `mid` 在哪一段，但此時
> `nums[right]` 不是唯一的最小值（`nums[mid]` 等於它），所以 `right -= 1` 丟掉它是安全的。
> 最壞情況變成 O(n)。

**歸約到**：[binary_search § Template 3](./binary_search.md#template-3-rotated-array-minimum--lc-153-) — 同樣是對 `nums[right]` 的半開擠壓，相等時多一步縮一格。

<!--CODE-->

<!--CODE-->

**陷阱**
- 改成和 `nums[left]` 比較：`[1, 3, 3]` 沒有旋轉，`nums[mid] >= nums[left]` 把搜尋送往右邊，
  錯過索引 0 的 `1`。
- O(n) 最壞情況是真的：`[1,1,1,1,0,1,1]` 任何探測都無法判斷。被問到時要說出來。

**相似題** — LC 81 *（含重複值的搜尋：在模板 4 上用同樣的 `right -= 1` 退路）*

<!-- f82bec3bfc56 -->
## 找峰值範例

<!-- 71d4cb22c143 -->
### Peak Index in a Mountain Array — LC 852

> **關鍵** — 山形陣列恰好只有一個峰，所以模板 7 不用改：往 `mid` 較高的鄰居走，直到視窗只剩一個索引。

**歸約到**：[binary_search § Template 7](./binary_search.md#template-7-peak-finding-by-slope--lc-162-) — 原封不動；山形保證讓峰值唯一。

<!--CODE-->

**相似題** — LC 162 *（任意峰值，沒有山形保證）* · LC 1095 *（見下）* · LC 1901 *（二維峰值）*

<!-- 6e4620cc7843 -->
### Find in Mountain Array — LC 1095 ⭐⭐⭐⭐⭐

> **關鍵** — 山形陣列是兩段排序區間在峰頂接起來。用模板 7 找出峰值，接著先搜尋遞增的左段
> （索引最小的勝出），之後才搜尋遞減的右段，比較方向要翻轉。三次 O(log n) 搜尋能維持在
> `get()` 的 100 次呼叫上限之內。

**歸約到**：[binary_search § Template 7](./binary_search.md#template-7-peak-finding-by-slope--lc-162-) — 先找峰值，再做兩次模板 1：左段照原樣，右段把方向翻轉。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 先搜右段會回傳較大的索引：`[1,2,3,4,5,3,1]`、`target = 3` 必須是 2，不是 5。
- 在右段沿用遞增的比較會走錯方向：`[0,5,3,1]`、`target = 1` 永遠找不到。
- 線性掃描找峰值在 n = 10⁴ 時會超出呼叫額度。

**相似題** — LC 852 *（只找峰值）* · LC 33 *（被旋轉切開的兩段排序區間：[binary_search § Template 4](./binary_search.md#template-4-search-a-rotated-array--lc-33-)）*

<!-- d46e211c8b58 -->
## 對答案搜尋範例

<!-- ee1141587cfd -->
### Valid Perfect Square — LC 367

> **關鍵** — 搜尋的是根，不是陣列：`[1, num]` 上第一個讓 `r * r >= num` 的 `r`，就是單調判定式的
> 第一個 `True`。`num` 是完全平方數，恰好當那個 `r` 的平方等於它。

**歸約到**：[binary_search_on_answer § Template 1](./binary_search_on_answer.md#template-1-smallest-feasible-value--lc-875-) — 用 `feasible(r) = r * r >= num` 找最小可行值，再做一次相等檢查。

<!--CODE-->

<!--CODE-->

**陷阱**
- Java 中 `int` 的 `mid * mid` 在 `mid > 46340` 時溢位：`num = 2147483647` 會探測到 `mid ≈ 10⁹`。用 `long`。
- 題目禁止用函式庫的 `sqrt`；牛頓迭代是另一個被接受的答案
  （[math.md § Integer Square Root](./math.md#1-1-10-integer-square-root--binary-search--newton-)）。

**相似題** — LC 69 *（下取整的根：最後一個 `r * r <= x` 的 `r`）* · LC 633 *（兩個平方數之和為 c）*

<!-- 22741efdcf6e -->
### Missing Element in Sorted Array — LC 1060

> **關鍵** — `missing(i) = nums[i] − nums[0] − i` 計算索引 `i` 之前缺了幾個數，而且絕不會變小。
> 找出最後一個缺數少於 `k` 的索引；答案就是從 `nums[i]` 再往後數那麼多步。

**歸約到**：[binary_search_on_answer § Template 1](./binary_search_on_answer.md#template-1-smallest-feasible-value--lc-875-) — 在索引上找第一個 `missing(i) >= k`；和那份文件裡的 LC 1539 是同一個推導計數技巧。

<!--CODE-->

<!--CODE-->

**陷阱**
- 第 k 個缺數可能落在陣列之後：`[4,7,9,10]`、`k = 5` → 12。`right = len(nums)` 涵蓋得到；
  `right = len(nums) − 1` 則不行。
- `left` 永遠不會是 0，因為對每個 `k >= 1` 都有 `missing(0) = 0 < k`，所以 `left − 1` 是安全的。

**相似題** — LC 1539 *（從 1 起算的缺失正整數）* · LC 275 *（H-Index II：推導出的計數對上索引）*

<!-- 3358b01e5929 -->
### Arranging Coins — LC 441

> **關鍵** — `k` 個完整的列需要 `k (k + 1) / 2` 枚硬幣，隨 `k` 遞增。答案是仍放得下的**最大** `k`，
> 也就是 `True … True False … False` 的最後一個 `True`。

**歸約到**：[binary_search_on_answer § Template 2](./binary_search_on_answer.md#template-2-largest-feasible-value--lc-1552-) — 用 `feasible(k) = k (k + 1) / 2 <= n` 找最大可行值，偏上的 mid。

<!--CODE-->

<!--CODE-->

**陷阱**
- 偏下的 mid 搭配 `left = mid`，一旦 `right = left + 1` 就永遠迴圈：`n = 1` 會卡在 `left = 0, right = 1`。
- `mid * (mid + 1)` 在 `n` 接近 2³¹ 時溢位 `int`：用 `long`。

**相似題** — LC 69 *（同樣是「成本放得下的最大 k」）* · LC 2226 *（最大可行的每堆數量）*
