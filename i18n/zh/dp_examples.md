<!-- ed5fa517f4f0 -->
# DP — 實戰題解

> **範圍** — 不是模板錨定題的經典 DP 題（LC 343、91、62、279、276、1137），每一題都是一張 problem card，歸檔在它所歸約到的 [dp.md](./dp.md) 模板底下，再加上一份「其他常見 DP 題在哪裡有完整解」的索引；本身不放模板也不談理論。
> **另見**：[dp.md](./dp.md) — 這些 card 歸約到的模板；[knapsack.md](./knapsack.md) — 完整的背包家族；[stock_trading.md](./stock_trading.md) — 所有股票狀態機的變形；[dp_advanced.md](./dp_advanced.md) — 冷門技巧與深入探討；[dp_pattern.md](./dp_pattern.md) — 更精簡的「一個模式一節」模板索引。

<!-- fd7fec51d5fa -->
## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Memoization](https://leetcode.com/problem-list/memoization/)

<!-- 0951d02dea02 -->
## 總覽

這是 [dp.md](./dp.md) 的題目集：模板在那邊，題目在這邊。每張 card 寫出這題在模板之外多加的那一件事（每一步多一個選項、一道合法性檢查、一個不同的狀態），並給一份標準解。各節依 dp.md 的模板順序排列；題目都已在自己 card 上、或在專門的表裡解完的模板，這裡就沒有那一節。

<!-- de8a776ca8fe -->
## 索引

| LC | 題目 | 歸約到 | 難度 |
|---|---|---|---|
| 343 | [Integer Break](#integer-break--lc-343) | [Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-) 一維線性 DP，試遍最後一刀 | Medium |
| 91 | [Decode Ways](#decode-ways--lc-91) | [Template 1a](./dp.md#template-1a-length-indexed-table-n--1-slots--lc-70) 以長度為索引的表 | Medium |
| 62 | [Unique Paths](#unique-paths--lc-62) | [Template 2](./dp.md#template-2-2-d-grid-dp--lc-64-) 二維格子 DP，計數 | Medium |
| 279 | [Perfect Squares](#perfect-squares--lc-279) | [knapsack.md § Template 4](./knapsack.md#template-4-unbounded-min-count--lc-322-) 完全背包，最少個數 | Medium |
| 276 | [Paint Fence](#paint-fence--lc-276) | [Template 5](./dp.md#template-5-state-machine-dp--lc-309) 狀態機，兩個狀態 | Medium |
| 1137 | [N-th Tribonacci Number](#n-th-tribonacci-number--lc-1137) | [Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables--lc-198-) 滾動變數 | Easy |

<!-- ba470cbb8218 -->
### 在別處有完整解

每一題都只在擁有它的那張 card 或那份表裡解一次。

| LC | 題目 | 所在 |
|---|---|---|
| 53 | Maximum Subarray | [dp.md § Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-)（錨定題）；Kadane 家族在 [kadane_algorithm.md](./kadane_algorithm.md) |
| 152 | Maximum Product Subarray | [kadane_algorithm.md](./kadane_algorithm.md)：同時追蹤目前的最大**與**最小乘積，因為負數會讓兩者對調 |
| 70 | Climbing Stairs | [dp.md § Template 1a](./dp.md#template-1a-length-indexed-table-n--1-slots--lc-70)（錨定題） |
| 139 | Word Break | [dp.md § Template 1b](./dp.md#template-1b-prefix-partition-dp--lc-139-)（錨定題） |
| 64 | Minimum Path Sum | [dp.md § Template 2](./dp.md#template-2-2-d-grid-dp--lc-64-)（錨定題） |
| 416 / 494 / 1049 | Partition Equal Subset Sum / Target Sum / Last Stone Weight II | [knapsack.md](./knapsack.md) — Template 1（錨定題）與它的 LC Examples |
| 322 / 518 / 377 | Coin Change / II / Combination Sum IV | [knapsack.md](./knapsack.md) — Template 4、2、3（錨定題） |
| 309 | Best Time to Buy and Sell Stock with Cooldown | [dp.md § Template 5](./dp.md#template-5-state-machine-dp--lc-309)（錨定題） |
| 121 / 122 / 123 / 188 / 714 | 其他股票題 | [stock_trading.md](./stock_trading.md) |
| 198 | House Robber | [dp.md § Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables--lc-198-)（錨定題） |

<!-- f1d464c819f4 -->
### 這裡沒有完整解 — 一句話的歸約

| 題目 | LC | 歸約到 | 一句話的想法 |
|---|---|---|---|
| Trapping Rain Water | 42 | 前綴／後綴陣列 | `water[i] = min(maxLeft[i], maxRight[i]) - h[i]`；雙指標可以省掉陣列 |
| Jump Game | 55 | 一維可達性 → 貪心 | 追蹤能到達的最遠索引 |
| Jump Game II | 45 | 一維 DP → 貪心分層 | `dp[i] = min(dp[j] + 1)`；像 BFS 那樣分層可以做到 O(n) |
| Minimum Cost For Tickets | 983 | 以天為單位的 [Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-) | `dp[d] = min(dp[d-1] + c1, dp[d-7] + c7, dp[d-30] + c30)` |
| 01 Matrix | 542 | [Template 2](./dp.md#template-2-2-d-grid-dp--lc-64-)，掃兩趟 | 左上 → 右下，再反過來一趟 |
| Counting Bits | 338 | [Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-) | `dp[i] = dp[i >> 1] + (i & 1)` |
| Is Subsequence | 392 | LCS 的退化情況 | 雙指標 O(n)；只有查詢很多次時才用 LCS 表 |
| Pascal's Triangle / II | 118 / 119 | [Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables--lc-198-)，一列滾動 | `row[j] += row[j-1]`，**倒著**跑 |

<!-- 94fc0c898cc0 -->
## 一維線性 DP 範例

<!-- 3226eac882a5 -->
### Integer Break — LC 343

> **關鍵** — `dp[i]` 是把 `i` 拆成**至少兩段**時的最大乘積。最後一刀 `j + (i - j)` 之後，剩下的
> 部分可以保持完整（`j * (i - j)`），也可以再拆（`j * dp[i - j]`）。兩種都要：`dp[2] = 1`，但把 `2`
> 留著不拆值 2。

**歸約到**：[dp.md § Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-) — `dp[i]` 讀之前的每一格，每一刀有兩個選項。

<!--CODE-->

<!--CODE-->

**試跑** — `n = 10`：

<!--CODE-->

**陷阱**
- 只用 `j * dp[i - j]`：剩下的 2 或 3 一定會被再拆，價值就掉了 —— `n = 4` 得到 3 而不是 4，
  `n = 6` 得到 6 而不是 9。
- O(n) 的貪心（盡量拆成 3，`3 + 1` 改成 `2 + 2`）是追問，不是開場：它需要一個 DP 不需要的證明。

<!-- 45eb93ca65d8 -->
## 以長度為索引的表 範例

<!-- ecef2a337622 -->
### Decode Ways — LC 91

> **關鍵** — 有合法性檢查的 Climbing Stairs：`dp[i]`（前 `i` 個字元的解碼方式數）在最後一位是
> `1`–`9` 時加上 `dp[i - 1]`，在最後兩位是 `10`–`26` 時加上 `dp[i - 2]`。`0` 只能當兩位數的第二位。

**歸約到**：[dp.md § Template 1a](./dp.md#template-1a-length-indexed-table-n--1-slots--lc-70) — `n + 1` 的表，`dp[0] = 1` 代表空前綴。

<!--CODE-->

<!--CODE-->

**陷阱**
- `"06"` 是 `0`，不是 `1`：`06` 不是合法的兩位數代碼，所以檢查是 `10..26`，不是 `<= 26`。
- 在 `n + 1` 的表裡，`s[i - 1]` 是**目前**的字元，不是前一個 —— 見 Template 1a 底下的
  [`s[i-1]` 陷阱](./dp.md#the-si-1-trap--it-is-the-current-character-not-the-previous-one)。

**類似** — LC 639 *（有 `*` 萬用字元）* · LC 70 · LC 1137

<!-- 7cef5394ae4f -->
## 二維格子 DP 範例

<!-- e13257f14e19 -->
### Unique Paths — LC 62

> **關鍵** — 走進一格的路徑數，是走進上面那格的路徑數加上走進左邊那格的路徑數，因為最後一步
> 只有這兩種。第一列和第一行都是 `1`：到每一格只有一條直線路徑。

**歸約到**：[dp.md § Template 2](./dp.md#template-2-2-d-grid-dp--lc-64-) — 把 `min` 換成 `+`，格子沒有成本。

<!--CODE-->

<!--CODE-->

**陷阱**
- 每一列只需要上一列，所以只開一個長度 `n` 的陣列、做 `dp[col] += dp[col - 1]`，就是 O(n) 空間的追問。
- 封閉解 `C(m + n - 2, m - 1)` 也對；用整數運算算它，見
  [combinatorics_math_patterns.md](./combinatorics_math_patterns.md)。

**類似** — LC 63 *（有障礙物：被擋住的格子是 0）* · LC 64 · LC 980 *（每格剛好走一次：回溯，不是 DP）*

<!-- f30d7c4dffa5 -->
## 背包範例

<!-- 357fe9078f1f -->
### Perfect Squares — LC 279

> **關鍵** — 平方數 `1, 4, 9, …` 就是無限供應的硬幣，`n` 是金額：湊成 `n` 的最少平方數個數，
> 就是 Coin Change。`dp[0] = 0`；其他每格都從 `n` 開始（`n` 個 1 永遠可行），所以沒有 `-1` 的情況。

**歸約到**：[knapsack.md § Template 4](./knapsack.md#template-4-unbounded-min-count--lc-322-) — 完全背包求最少個數，硬幣是所有 ≤ 金額的平方數。

<!--CODE-->

<!--CODE-->

**陷阱**
- 「先拿最大的平方數」的貪心會錯：`12 = 9 + 1 + 1 + 1`（4 個），但 `4 + 4 + 4` 只要 3 個。
- 求最少個數時迴圈順序不重要；數方法數時就重要了
  （[knapsack.md § Loop Order](./knapsack.md#loop-order-combinations-vs-permutations)）。

**類似** — LC 322 · LC 518 · LC 377 — 對餘數做 BFS、一層一層走，也能解同一題。

<!-- b1978d3d140a -->
## 狀態機 DP 範例

<!-- bdcc3e2a9f53 -->
### Paint Fence — LC 276

> **關鍵** — 最後一根柱子有兩個狀態：`same`（跟前一根同色）與 `diff`（不同色）。「不能三根連續同色」
> 代表 `same` 只能接在 `diff` 後面，所以 `same = diff_prev`，`diff = (same_prev + diff_prev) * (k - 1)`。

**歸約到**：[dp.md § Template 5](./dp.md#template-5-state-machine-dp--lc-309) — 兩個只讀前一根柱子的狀態，滾動成兩個變數。

<!--CODE-->

<!--CODE-->

**陷阱**
- 用**新的** `same` 去更新 `diff`：先把舊值存起來，就像每一個狀態機一樣。
- `k = 1`、`n ≥ 3` 是 `0`；遞迴式自己會算到（`diff` 一開始就是 0）。

**類似** — LC 198 · LC 309 · LC 740 *（對值做拿／不拿）* · LC 790 *（把鋪磚當成狀態）*

<!-- a29e3b087d90 -->
## 滾動變數範例

<!-- 7795e6c16b8b -->
### N-th Tribonacci Number — LC 1137

> **關鍵** — `T(n)` 剛好讀最後三個值，所以三個變數就能取代整張表。由最舊的開始往前移
> （`a = b`、`b = c`、`c = next`），否則某個值會在被讀到之前就被覆蓋。

**歸約到**：[dp.md § Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables--lc-198-) — 最後一階，`k = 3` 個滾動變數。

<!--CODE-->

<!--CODE-->

**陷阱**
- `n = 0, 1, 2` 在迴圈前就回傳；起始值 `0, 1, 1` 必須滿足它們開頭的那個遞迴式。
- 滾動變數的更新順序是經典 bug —— [dp_advanced.md](./dp_advanced.md) 在滾動變數的深入探討裡逐步追蹤了它。

**類似** — LC 70 · LC 509 · LC 746
