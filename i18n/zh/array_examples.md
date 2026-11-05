<!-- 680b74b08db1 -->
# 陣列 — 實戰題解

> **範圍** — [array.md](./array.md) 自己那些模板（索引當雜湊、一次掃描的即時狀態）背後的題目，每題都放在它歸約到的卡片底下；歸屬在別份文件的陣列標籤題，在這裡只留一列指標。
> **另見**：[array.md](./array.md) — 母表：基本操作、這些題目歸約到的模板，以及告訴你一題到底該歸哪張表的選擇表；[2_pointers.md](./2_pointers.md)、[sliding_window.md](./sliding_window.md)、[prefix_sum.md](./prefix_sum.md)、[difference_array.md](./difference_array.md) — 吃掉大部分 array 標籤題目的四個模式家族；[sort.md](./sort.md)、[matrix.md](./matrix.md)、[kadane_algorithm.md](./kadane_algorithm.md)、[stock_trading.md](./stock_trading.md) — 選擇表會把題目導向的其他主表。

<!-- 252a821e92ee -->
## LeetCode 題目清單

- [Array](https://leetcode.com/problem-list/array/)

<!-- 2a78b733c14f -->
## 總覽

[array.md](./array.md) 放模板；這份文件放應用它們的題目。每張卡片都寫出它歸約到哪個模板，以及多出來的那一步。
多數標成 *array* 的題目其實是視窗、指標、前綴和、排序或 DP 題：它們列在
[其他歸屬](#lives-elsewhere)，連到詳解它們的文件，所以每題都只在一個地方解。

<!-- b6abf90f32dd -->
## 索引

| 模式 | 題目（易 → 難） | 核心 ⭐ |
|---|---|---|
| [Template 9: Cyclic Sort, Index as Hash](./array.md#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) | LC 448 · LC 442 | LC 41（錨定題，在 array.md） |
| [Template 11: Running State Scan](./array.md#1-2-10-one-pass-running-state-) | LC 1375 · LC 1041 · LC 670 | LC 849（錨定題，在 array.md） |

<!-- d1543723b59a -->
### 其他歸屬

| LC | 題目 | 歸屬 | 為什麼放在那裡 |
|---|---|---|---|
| 41 | First Missing Positive | [array § Template 9](./array.md#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) | 循環排序卡片的錨定題 |
| 88 | Merge Sorted Array | [2_pointers § Template 5](./2_pointers.md#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-) | 反向寫入是兩個讀取指標加一個寫入指標 |
| 121 | Best Time to Buy and Sell Stock | [stock_trading.md](./stock_trading.md#lc-121-best-time-to-buy-and-sell-stock-single-transaction) | 目前為止的最低價；股票家族共用一個狀態機 |
| 122 | Best Time to Buy and Sell Stock II | [greedy § Template 3](./greedy.md#template-3-greedy-accumulation--lc-122-) | 貪心累加的錨定題：把每一段上漲加起來 |
| 189 | Rotate Array | [array § Template 10](./array.md#1-2-9-rotate-in-place-by-three-reversals-) | 三次反轉卡片的錨定題 |
| 238 | Product of Array Except Self | [prefix_sum.md](./prefix_sum.md#product-variant--no-inverse-so-build-both-sides-lc-238-) | 前綴乘積乘上後綴乘積；輸出陣列兼當前綴陣列 |
| 251 | Flatten 2D Vector | [array § Template 1](./array.md#1-1-6-flatten-array) | 惰性雙游標迭代器的錨定題 |
| 287 | Find the Duplicate Number | [binary_search_on_answer.md](./binary_search_on_answer.md#find-the-duplicate-number--lc-287-) | 它禁止修改 `nums`，所以不能用正負號標記：在值域上二分搜尋，或用 Floyd 的環偵測 |
| 334 | Increasing Triplet Subsequence | [patience_sorting.md](./patience_sorting.md#2-2-increasing-triplet-subsequence--lc-334-) | `first` / `second` 就是上限兩堆的 patience sorting 堆頂 |
| 406 | Queue Reconstruction by Height | [sort.md](./sort.md#variation--sort-by-derived-key-then-insert-greedily--lc-406) | 先按身高由高到低排，再插到索引 `k` |
| 849 | Maximize Distance to Closest Person | [array § Template 11](./array.md#1-2-10-one-pass-running-state-) | 即時狀態卡片的錨定題 |
| 1109 | Corporate Flight Bookings | [difference_array.md](./difference_array.md#2-2-corporate-flight-bookings--lc-1109) | 在端點上做區間更新，再做一次前綴和 |
| 1567 | Maximum Length of Subarray With Positive Product | [kadane_algorithm.md](./kadane_algorithm.md#6-8-maximum-length-of-subarray-with-positive-product-lc-1567--track-posneg-product-lengths) | 以這裡結尾的最長正積／負積長度，Kadane 式 |

<!-- 30e4291b7a7a -->
## 索引當雜湊題目

<!-- 6498fd1f5047 -->
### Find All Numbers Disappeared in an Array — LC 448

> **關鍵** — 把 `nums[v - 1]` 變成負數，代表「看過值 `v`」。掃完一次後，還是正數的格子就指出一個從沒出現過的值。
> 每次讀取都要經過 `abs()`，因為它讀的那格可能已經被標記過。

**歸約到**：[array § Template 9](./array.md#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) — 用正負號標記而不是搬動值，並收集每個缺口而不只是第一個。

<!--CODE-->

**陷阱**
- **沒有 `> 0` 防護就取負。** `[1, 1]` 會把第 0 格翻兩次，又變回正數，回傳 `[1, 2]` 而不是 `[2]`。
- **直接讀 `x`。** 標記之後 `x` 可能是負的；`nums[x - 1]` 在 Python 會默默從尾端索引，在 Java 會丟例外。
- **呼叫端的陣列被改了。** 如果還要再用，用 `abs` 還原。

**相似題** — LC 268（只缺一個值：總和或 XOR 更簡單）· LC 645（一個重複、一個缺少，用同一組標記找出）

<!-- cd3fd35403b8 -->
### Find All Duplicates in an Array — LC 442

> **關鍵** — 第二次讀到值 `v` 時，第 `v - 1` 格已經是負的：這次讀取就是重複。每個值最多出現兩次，
> 所以每次讀取做一次判斷，就能把每個重複值剛好回報一次。

**歸約到**：[array § Template 9](./array.md#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) — 正負號標記，發現某格已經是負的時，那就是答案，而不是缺口。

<!--CODE-->

**陷阱**
- **出現三次的值**會被這個判斷回報兩次，但 LC 442 保證最多兩次；如果沒有這個保證，就另外記一個「已回報」標記。
- **LC 287 看起來一樣，其實不是。** 它禁止修改 `nums`，所以這個解法在那裡是錯的；見[其他歸屬](#lives-elsewhere)。

**相似題** — LC 287（一個重複值，輸入唯讀）· LC 645（集合不符）

<!-- 1db495adb7d0 -->
## 一次掃描的即時狀態題目

<!-- 924015ac0b37 -->
### Number of Times Binary String Is Prefix-Aligned — LC 1375

> **關鍵** — 第 `i` 步之後，位元 `1..i+1` 全部打開，剛好等價於目前翻過的最大索引是 `i + 1`。
> 已經翻過 `i + 1` 個不同的索引（輸入是排列），而且沒有一個大於 `i + 1`，所以它們正好就是 `1..i+1`。

**歸約到**：[array § Template 11](./array.md#1-2-10-one-pass-running-state-) — 帶著的值是目前最大值。因為輸入是排列，它取代了 [Template 4](./array.md#1-2-2-frequency-array--running-count) 的頻率陣列。

<!--CODE-->

**陷阱**
- **拿從 0 開始的位置比。** 用 `enumerate(flips)` 的 `i` 寫 `largest == i`，在 `[3, 2, 4, 1, 5]` 上永遠不成立，
  回傳 0 而不是 2；索引是從 1 開始的。
- **每一步都檢查翻過的位元**是 O(n²)，在 n = 5·10⁴ 時會超時。

<!-- 4eb26aa0b80f -->
### Robot Bounded In Circle — LC 1041

> **關鍵** — 把指令跑一次。回到原點的機器人是有界的。面向北方以外任何方向的機器人也有界：
> 跑四次（面向南方的話兩次）之後位移會互相抵消。只有結束時不在原點*而且*仍面向北方的機器人會越走越遠。

**歸約到**：[array § Template 11](./array.md#1-2-10-one-pass-running-state-) — 帶著的狀態是 `(x, y, heading)`，答案從跑完一次後的狀態讀出。

<!--CODE-->

**陷阱**
- **只檢查原點。** `"GL"` 結束在 `(0, 1)`、面向西方，不在原點，但它走的是一個正方形，所以有界；方向的判斷會抓到它。
- **Java 裡的 `(heading - 1) % 4`** 在 `heading` 為 0 時是 `-1`，索引越界。像上面那樣改成加 3，而不是減 1。

<!-- b348eaa734eb -->
### Maximum Swap — LC 670

> **關鍵** — 從右往左掃數字，帶著目前看過的最大數字的索引（相同時取最右邊那個）。任何比這個最大值小的數字
> 都可以和它交換；最好的交換是最左邊的那個數字，而掃描是往左走的，所以最後記下的就是它。

**歸約到**：[array § Template 11](./array.md#1-2-10-one-pass-running-state-) — 從右邊掃，帶著每個數字右邊最大值的索引。

<!--CODE-->

**陷阱**
- **更新最大值時用 `>=`。** `1993` 必須和*最右邊*的 9 交換（9913）；`>=` 會把 `max_idx` 移到最左邊的 9，回傳 9193。
- **一找到候選就停。** 在 `98368` 上，從右邊第一個找到的是 6，得到 98386；最左邊比較小的數字是 3，得到 98863。
- **暴力試每一對**是 O(d²) — 9 位數時沒問題，但那不是面試官想看的答案。

**第二種做法** — 一張 `{digit: last index}` 表加上由左往右掃，在
[hash_map_examples.md](./hash_map_examples.md#2-17-maximum-swap-lc-670)。同樣 O(d)；它問的是「後面有沒有更大的數字？」，而不是帶著最大值走。
