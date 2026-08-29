<!-- 21c7a6a47825 -->
# DP 模式總表

> **範圍** — **模板索引**：每個經典 DP 模式各佔一小節（Kadane、LIS、MCM、LCS、背包、狀態機、格子、位元遮罩、數位、樹上 DP、正規表達式、加權區間排程、切分、記憶化 DAG）。
> **另見**：[dp.md](./dp.md) — 這些模板背後的解釋與實作範例；[recursion_to_dp.md](./recursion_to_dp.md) — 怎麼從一段遞迴*推導*出其中一個模板。

- https://leetcode.com/discuss/study-guide/1308617/Dynamic-Programming-Patterns

<!-- 6417951bd971 -->
## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

<!-- 517606459b80 -->
## 1. Kadane 演算法（最大子陣列） ⭐⭐⭐⭐⭐

**模式**：求一段連續子陣列的最大／最小和。

**核心想法**：在每個位置決定要延續當前子陣列，還是重新開一段。

**遞迴式**：`dp[i] = max(nums[i], dp[i-1] + nums[i])`

**時間複雜度**：O(n) | **空間複雜度**：O(1)

<!-- 5a456f7e3b71 -->
### 模板程式碼：

**Python：**
<!--CODE-->

**Java：**
<!--CODE-->

<!-- a00230615325 -->
### 常見題目：
- LC 53: Maximum Subarray
- LC 152: Maximum Product Subarray
- LC 918: Maximum Sum Circular Subarray
- LC 1749: Maximum Alternating Sum
- 二進位字串中 0 與 1 個數差的最大值
- 最小和的連續子陣列
- 最大和的遞增連續子陣列
- 二維矩陣中的最大和矩形

<!-- b42ddfcb6b59 -->
## 2. 最長遞增子序列（LIS） ⭐⭐⭐⭐

**模式**：找出元素遞增的最長子序列。

**核心想法**：對每個元素，求出以該位置結尾的最長遞增子序列。

**遞迴式**：對所有滿足 `nums[j] < nums[i]` 的 `j < i`，`dp[i] = max(dp[j] + 1)`

**時間複雜度**：O(n²)，或搭配二分搜尋的 O(n log n) | **空間複雜度**：O(n)

<!-- edb8343cbbf8 -->
### 模板程式碼（O(n²) 版）：

**Python：**
<!--CODE-->

**Java：**
<!--CODE-->

<!-- f68d90833a61 -->
### 模板程式碼（搭配二分搜尋的 O(n log n) 版）：

**Python：**
<!--CODE-->

**Java：**
<!--CODE-->

<!-- fd9ff4335a2d -->
### 常見題目：
- LC 300: Longest Increasing Subsequence
- LC 673: Number of Longest Increasing Subsequence
- LC 334: Increasing Triplet Subsequence
- LC 1626: Best Team with No Conflicts
- LC 1964: Find the Longest Valid Obstacle Course at Each Position
- LC 2111: Minimum Number of Removals to Make Mountain Array
- LC 354: Russian Doll Envelopes（二維 LIS，見下方變形）
- LC 1048: Longest String Chain（「比較小」＝*是前驅字串*；先依字長排序，再套 LIS 邏輯）
- 最大和遞增子序列
- 印出 LIS（`Longest Increasing Subsequence`）
- 和幾乎為 K 的 LIS

<!-- a9609efd7ff1 -->
### 變形：二維 LIS — Russian Doll Envelopes（LC 354）

> **轉折**：寬度**升冪**排序，但寬度相同時改用高度**降冪**當次序。這個降冪的平手規則讓兩個等寬信封不可能互相嵌套，於是問題塌成單純在高度上跑 O(n log n) 的 LIS。

**Java：**
<!--CODE-->

**Python：**
<!--CODE-->

<!-- 0b196ed39f88 -->
## 3. 矩陣連乘（MCM）／區間 DP ⭐⭐⭐⭐

**模式**：在不同位置切開，把問題拆成子問題，再把結果合起來。

**核心想法**：試遍所有切分區間的方式，取最佳的那個。

**遞迴式**：對 `[i, j)` 中所有 `k`，`dp[i][j] = min/max(dp[i][k] + dp[k+1][j] + cost)`

**時間複雜度**：O(n³) | **空間複雜度**：O(n²)

<!-- 0654bbceba70 -->
### 常見題目：
- LC 312: Burst Balloons
- LC 1039: Minimum Score Triangulation of Polygon
- LC 87: Scramble String
- LC 131: Palindrome Partitioning
- LC 132: Palindrome Partitioning II
- LC 1547: Minimum Cost to Cut a Stick
- LC 1000: Minimum Cost to Merge Stones
- LC 96 / LC 95: Unique Binary Search Trees (I / II) — 以**根節點**切分區間：`dp[n] = Σ dp[i-1] * dp[n-i]`（卡特蘭數）；LC 95 回傳的是樹本身而不是數量
- 布林運算式加括號使其為 True
- 運算式的最小／最大值
- 丟雞蛋問題

<!-- 5641e7ce674a -->
## 4. 最長共同子序列（LCS） ⭐⭐⭐⭐⭐

**模式**：找出兩個序列共同的最長子序列。

**核心想法**：字元相同就延長 LCS；不同就取「跳過其中一邊」的較大值。

**遞迴式**：
- 若 `s1[i] == s2[j]`：`dp[i][j] = dp[i-1][j-1] + 1`
- 否則：`dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

**時間複雜度**：O(m*n) | **空間複雜度**：O(m*n) 或 O(min(m,n))

<!-- 041a3ff7df65 -->
### 常見題目：
- LC 1143: Longest Common Subsequence
- LC 72: Edit Distance
- LC 583: Delete Operation for Two Strings
- LC 712: Minimum ASCII Delete Sum for Two Strings
- LC 1092: Shortest Common Supersequence
- LC 516: Longest Palindromic Subsequence
- LC 5: Longest Palindromic Substring
- LC 647: Palindromic Substrings
- LC 115: Distinct Subsequences
- LC 392: Is Subsequence
- LC 97: Interleaving String（同一張雙字串格子，但 `dp[i][j]` = `s1[0..i)` + `s2[0..j)` *能不能*交錯成 `s3[0..i+j)`）
- LC 718: Maximum Length of Repeated Subarray（這其實是陣列上的最長共同**子字串**——不相符就歸零）
- 最長共同子字串
- 印出 LCS / SCS
- 把字串 a 變成 b 的最少插入／刪除次數
- 最長重複子序列
- 子序列模式比對
- 計算 a 以子序列形式在 b 中出現幾次

<!-- 6ca6f0320a15 -->
## 5. 完全背包 ⭐⭐⭐⭐

**模式**：物品數量無限，選出組合來最大化／最小化價值，或計算方案數。

**核心想法**：每個物品可以重複使用。要決定的是：再拿一次當前物品，還是換到下一個物品。

**遞迴式**：`dp[i][w] = max(dp[i-1][w], dp[i][w-weight[i]] + value[i])`

**時間複雜度**：O(n*W) | **空間複雜度**：O(W)

<!-- 8b82dda5afd1 -->
### 與 0/1 背包的關鍵差異

| 變形 | 迴圈順序 | 為什麼 |
|---------|-----------|-----|
| 0/1 背包 | 外層：物品，內層：容量**反向** | 每個物品最多用一次 |
| 完全背包 | 外層：物品，內層：容量**正向** | 物品可以重複使用 |

<!-- 2fc3e461559c -->
### 模板程式碼（以 Coin Change 為例）：

**Python：**
<!--CODE-->

**Java：**
<!--CODE-->

<!-- 93f02a0256a7 -->
### 常見題目：
- LC 322: Coin Change（最少硬幣數）
- LC 518: Coin Change II（方案數）
- LC 377: Combination Sum IV
- LC 139: Word Break
- LC 140: Word Break II（同樣的 `dp[i]` 切分判斷，但記憶化的是**句子清單**而不是布林值）
- LC 472: Concatenated Words（拿*其他*單字當字典，對每個單字跑 Word Break；先依長度排序，字典裡就只會有比較短的字）
- LC 279: Perfect Squares（硬幣 = 所有 ≤ n 的完全平方數；求最少個數）
- LC 1155: Number of Dice Rolls With Target Sum（有界／分組背包：剛好 `k` 顆骰子，每顆貢獻 1..f）
- LC 983: Minimum Cost For Tickets
- 切鋼條問題
- 剪緞帶最大段數
- 數字分割

<!-- 188b0c7a8fcb -->
## 6. 0/1 背包 ⭐⭐⭐⭐⭐

**模式**：每個物品最多用一次，選出組合來最大化／最小化價值，或計算方案數。

**核心想法**：對每個物品，決定拿或不拿。

**遞迴式**：`dp[i][w] = max(dp[i-1][w], dp[i-1][w-weight[i]] + value[i])`

**時間複雜度**：O(n*W) | **空間複雜度**：O(W)

<!-- 57791ce190fb -->
### 常見題目：
- LC 416: Partition Equal Subset Sum
- LC 494: Target Sum
- LC 698: Partition to K Equal Sum Subsets
- LC 1049: Last Stone Weight II
- LC 474: Ones and Zeroes（二維背包）
- 子集合和
- 和為指定值的子集合個數
- 最小子集合和差
- 差值為指定值的子集合個數

<!-- 375a002ebb36 -->
## 7. 狀態機 DP ⭐⭐⭐⭐

**模式**：狀態會依動作／決策而轉移的問題。

**核心想法**：追蹤各種狀態以及它們之間的轉移。買賣股票題最常見。

**時間複雜度**：O(n*狀態數) | **空間複雜度**：O(狀態數)

<!-- e318df938b88 -->
### 常見題目：
- LC 121: Best Time to Buy and Sell Stock
- LC 122: Best Time to Buy and Sell Stock II
- LC 123: Best Time to Buy and Sell Stock III
- LC 188: Best Time to Buy and Sell Stock IV
- LC 309: Best Time to Buy and Sell Stock with Cooldown
- LC 714: Best Time to Buy and Sell Stock with Transaction Fee
- LC 198: House Robber（搶／不搶兩種狀態）
- LC 213: House Robber II
- LC 801: Minimum Swaps To Make Sequences Increasing（每個索引 2 種狀態：**交換過** / **保持原樣**；轉移是否合法要同時看 `A`/`B` 的比較結果）
- LC 926: Flip String to Monotone Increasing（2 種狀態：前綴結尾是 `0` / 結尾是 `1`；翻轉成本依狀態各自累加）

<!-- 0fcb0fe4f314 -->
## 8. 格子路徑 DP ⭐⭐⭐⭐

**模式**：在格子上計算路徑數，或求最小／最大成本路徑。

**核心想法**：每個格子只取決於能走到它的那些格子（通常是上方、左方或斜對角）。

**遞迴式**：`dp[i][j] = dp[i-1][j] + dp[i][j-1]`（用於計算路徑數）

**時間複雜度**：O(m*n) | **空間複雜度**：O(n)

<!-- 216382f98e25 -->
### 常見題目：
- LC 62: Unique Paths
- LC 63: Unique Paths II
- LC 64: Minimum Path Sum
- LC 120: Triangle
- LC 174: Dungeon Game
- LC 221: Maximal Square
- LC 931: Minimum Falling Path Sum
- LC 1594: Maximum Non Negative Product in a Matrix
- LC 1277: Count Square Submatrices with All Ones（遞迴式和 LC 221 Maximal Square 完全一樣——把 dp 表**加總**起來，而不是取最大值）
- LC 688: Knight Probability in Chessboard（機率格子 DP：`dp[k][r][c]` = 走 `k` 步後仍在棋盤上的機率；8 種走法各帶 `1/8` 的權重）
- LC 764: Largest Plus Sign（四個方向的連續長度前綴 DP——上／下／左／右——再在每格取最小值）

<!-- dfd1f9fca195 -->
## 9. 位元遮罩 DP

**模式**：資料規模很小（n ≤ 20）時，用位元遮罩表示子集合／狀態。

**核心想法**：每個位元代表某個元素有沒有被選中／走訪過。走遍所有可能狀態。

**時間複雜度**：O(2^n * n) 或 O(2^n * n²) | **空間複雜度**：O(2^n)

<!-- 8e28d8df36b5 -->
### 常見題目：
- LC 847: Shortest Path Visiting All Nodes
- LC 943: Find the Shortest Superstring
- LC 1125: Smallest Sufficient Team
- LC 1434: Number of Ways to Wear Different Hats to Each Other
- LC 1595: Minimum Cost to Connect Two Groups of Points
- LC 2172: Maximum AND Sum of Array
- LC 464: Can I Win（用位元遮罩記*已使用的數字*，再加上賽局理論的勝負記憶化）
- LC 691: Stickers to Spell Word（用位元遮罩記目標字串裡哪些字母已經湊齊）
- 旅行推銷員問題
- 指派問題

<!-- cf6304139f90 -->
## 10. 數位 DP

**模式**：計算某個範圍內滿足特定「數位性質」的數有幾個。

**核心想法**：一位一位把數字組出來，同時追蹤各種限制（是否貼著上界、前導零等等）。

**時間複雜度**：O(位數 * 狀態數) | **空間複雜度**：O(位數 * 狀態數)

<!-- a91930f98a48 -->
### 常見題目：
- LC 233: Number of Digit One
- LC 357: Count Numbers with Unique Digits
- LC 600: Non-negative Integers without Consecutive Ones
- LC 902: Numbers At Most N Given Digit Set
- LC 1012: Numbers With Repeated Digits
- LC 2376: Count Special Integers

<!-- d5f3c08fc52d -->
### 另一種模板——計算 `[0, n]` 中滿足性質 P 的整數個數
> LC 233、LC 1012。想法和上面一樣，只是把狀態明確寫成 `(position, tight, ...)`。
計算 `[1, n]` 中滿足某個數位限制的數的個數。

<!--CODE-->

關鍵狀態變數：
- `pos`：當前的數位位置
- `tight`：是否仍然被 `n` 的位數卡住
- 任何題目專屬的計數器（1 的個數、數位和等等）

<!-- d3694f66b719 -->
## 11. 樹上 DP

**模式**：依子樹的值算出樹節點上的值。

**核心想法**：用 DFS／後序走訪先解完子節點，再在父節點把結果合起來。

**時間複雜度**：O(n) | **空間複雜度**：O(樹高)

<!-- 8f70ff22bd17 -->
### 常見題目：
- LC 124: Binary Tree Maximum Path Sum
- LC 337: House Robber III
- LC 543: Diameter of Binary Tree
- LC 687: Longest Univalue Path
- LC 968: Binary Tree Cameras
- LC 979: Distribute Coins in Binary Tree
- LC 1130: Minimum Cost Tree From Leaf Values
- LC 2246: Longest Path With Different Adjacent Characters

<!-- 4573ca5fc77c -->
## 12. 萬用字元／正規表達式比對 DP ⭐⭐⭐⭐⭐

**辨識訊號**：有兩個字串，但它們**不對稱**——一個是文字 `s`，另一個是含有萬用字元（`*`、`?`、`.`）的*樣式* `p`。答案是布林值。貪婪會失敗，因為 `*` 可以吃掉任意多個字元。

**模式**：和 LCS 一樣的二維前綴格子，但轉移是由**樣式字元**驅動，而不是由「相等」驅動。

**核心想法**：`dp[i][j]` = `s[0..i)` 能不能比對上 `p[0..j)`。`*` 給你一個二選一：*再吃掉一個文字字元*（停在 `*` 上）或*把 `*` 丟掉*。

**遞迴式**（LC 44，`*` = 任意序列）：
- `p[j-1] == '*'` → `dp[i][j] = dp[i-1][j]（星號吃掉 s[i-1]） || dp[i][j-1]（星號什麼都不吃）`
- `p[j-1] == '?'` 或字元相等 → `dp[i][j] = dp[i-1][j-1]`

**時間複雜度**：O(m*n) | **空間複雜度**：O(m*n) → 用滾動列可降到 O(n)

> ⚠️ **這題大家都死在基底列**：當樣式是一串 `*` 時，`dp[0][j]`（空文字）必須保持 `true`——否則 `"" vs "***"` 就會錯。

<!-- 8cd6dcbc6fa8 -->
### 模板程式碼：

**Java：**
<!--CODE-->

**Python：**
<!--CODE-->

<!-- 465aebed50a6 -->
### 變形：`*` 綁定前一個字元 — LC 10 Regular Expression Matching

> **轉折**：在正規表達式裡，`*` 是**作用在 `p[j-2]` 上的量詞**，不是一個獨立的萬用字元。所以「用零次」要跳過**兩個**樣式字元（`dp[i][j-2]`），而「再用一次」只有在 `p[j-2]` 真的比對得上 `s[i-1]` 時才允許。

**Java：**
<!--CODE-->

**Python：**
<!--CODE-->

<!-- 6393eb9344da -->
### LC 44 vs LC 10 — 只差這兩行

| | LC 44 的 `*`（萬用字元） | LC 10 的 `*`（量詞） |
|---|---|---|
| 意義 | 任意序列，獨立存在 | `p[j-2]` 出現 0 次以上 |
| 「比對空字串」 | `dp[i][j-1]`（丟掉 1 個字元） | `dp[i][j-2]`（丟掉 2 個字元） |
| 「再多比對一個」 | `dp[i-1][j]` — 永遠允許 | `dp[i-1][j]` — 只有 `p[j-2]` 比對得上 `s[i-1]` 才行 |
| 基底列 | 遇到 `*` 時 `dp[0][j] = dp[0][j-1]` | 遇到 `*` 時 `dp[0][j] = dp[0][j-2]` |

<!-- 83da973b9fe4 -->
### 常見題目：
- LC 44: Wildcard Matching
- LC 10: Regular Expression Matching
- LC 97: Interleaving String（格子形狀相同，轉移不同）
- LC 72: Edit Distance（格子形狀相同，求最小成本而不是布林值）

<!-- 5dc488dbf11a -->
## 13. 加權區間排程 DP（排序 + 二分搜尋）

**辨識訊號**：物件是**帶價值的區間**（`start`、`end`、`profit`），你要選一組**互不重疊**的子集合把價值最大化，而且 `n` 很大（10⁴–10⁵），所以「跟前面每一個都比一次」的 O(n²) DP 太慢。一旦區間各自帶了不同權重，純貪婪（像「最多能選幾個不重疊區間」那種）就**不管用**了。

**模式**：依**結束時間**排序，然後用二分搜尋找出每個物件的前驅。

**核心想法**：依結束時間排序後，`dp[i]` = 用前 `i` 個工作能拿到的最佳利潤。工作 `i` 要嘛不選（`dp[i-1]`），要嘛選；選的話，所有與它相容的工作剛好是排序後陣列的一段**前綴**——一次二分搜尋就找得到。

**遞迴式**：`dp[i] = max(dp[i-1], dp[p(i)] + profit[i])`，其中 `p(i)` = 滿足 `end <= start[i]` 的工作數量

**時間複雜度**：O(n log n) | **空間複雜度**：O(n)

<!-- 80449f6864be -->
### 變形：加一個「最多選 k 個」的維度 — LC 1751 Maximum Number of Events That Can Be Attended II

> **轉折**：一樣是「依結束時間排序 + 二分搜尋」的骨架，只是多一個表示預算的維度：
> `dp[i][j] = max(dp[i-1][j], dp[p(i)][j-1] + value_i)` — O(n·k·log n)。

<!-- aed7075aaad6 -->
### 常見題目：
- LC 1235: Maximum Profit in Job Scheduling
- LC 1751: Maximum Number of Events That Can Be Attended II（上限 `k` 個區間）
- LC 646: Maximum Length of Pair Chain（沒有權重 → 貪婪也行）
- LC 300: Longest Increasing Subsequence（一維版本的「用二分搜尋找前驅」）

<!-- 53e2136559ce -->
## 14. 切成 K 段連續區塊（切分 DP）

**辨識訊號**：「把陣列切成／把工作排成**剛好 `k` 段連續的部分**」，而目標函數定義在這些部分上（各段最大值的總和、各段總和的最大值、各段成本的總和）。注意這些部分必須是**連續的**——這正是它和背包的分野。

**模式**：二維 DP，第二個維度是*還剩幾段可以用*。和區間／MCM DP 不同：MCM 是遞迴地切成兩半；這裡是從左到右把序列切成 `k` 個區塊。

**核心想法**：`dp[i][k]` = 用剛好 `k` 段覆蓋後綴 `a[i..n)` 的最佳成本。列舉**第一段**在哪裡結束，並且邊走邊維護第一段的 `max`／`sum`，讓每次轉移都是 O(1)。

**遞迴式**：`dp[i][k] = min over j >= i of ( cost(a[i..j]) + dp[j+1][k-1] )`

**時間複雜度**：O(n² * k) | **空間複雜度**：O(n * k)

> 可行性守衛：若 `n < k`，元素不夠湊出 `k` 個非空的部分 → 回傳 -1。

<!-- edcb40f0d724 -->
### 變形：最小化最大的那一段，而不是總和 — LC 410 Split Array Largest Sum

> **轉折**：目標函數是用 `max` 而不是 `+` 把各段合起來：
> `dp[i][t] = min over j of max( dp[j][t-1], sum(a[j..i)) )` — O(n²k)。
> 因為答案具有單調性（「每段都 ≤ X 的切法存在嗎？」），LC 410 *也*可以用**在答案上二分搜尋**在 O(n log ΣA) 解掉——面試時兩種都提，然後實作二分搜尋那版。

<!-- b3e21b90c89c -->
### 常見題目：
- LC 1335: Minimum Difficulty of a Job Schedule
- LC 410: Split Array Largest Sum（也可以在答案上二分搜尋）
- LC 813: Largest Sum of Averages
- LC 1043: Partition Array for Maximum Sum（區塊長度上限為 `k`——那個*上限*取代了「段數」這個維度）
- LC 132: Palindrome Partitioning II（每段都必須是迴文；最小化段數）

<!-- 856abc58d7c3 -->
## 15. 隱式 DAG 上的記憶化 DFS

**辨識訊號**：移動**不**限制在「往右／往下」，也看不出明顯的處理順序——但有一個**嚴格單調的限制**（格子值嚴格遞增、跳躍嚴格往前）保證不會有環。這讓狀態圖變成一個 DAG，於是單純的 DFS + 記憶化就合法了，複雜度是 O(狀態數)。

**模式**：當你沒辦法輕鬆地手動把狀態拓撲排序時，就讓遞迴自己去發現順序，每個狀態只快取一次。（對照第 8 節的格子路徑 DP，那裡的列優先順序*就是*拓撲順序。）

**核心想法**：`memo[state]` = 從該狀態*出發*的子問題答案。每個狀態只展開一次；每條邊只鬆弛一次。

**遞迴式**：對單調限制允許的邊 `u → v`，`f(u) = 1 + max(f(v))`（沒有這種邊時 `f(u) = 1`）

**時間複雜度**：O(V + E)，格子的話是 O(m*n) | **空間複雜度**：O(m*n)

<!-- 73fd573dbbe1 -->
### 模板程式碼：

**Java：**
<!--CODE-->

**Python：**
<!--CODE-->

> ⚠️ **Python 的遞迴深度**：記憶化省的是*計算量*，不是*堆疊深度*——一條遞增路徑可能橫跨全部 `M*N` 個格子（LC 329 允許 200×200 = 40000），遠遠超過 CPython 預設的 1000 層。加上 `sys.setrecursionlimit(10**6)`（Java 沒這問題），或者改寫成迭代版：照拓撲順序一層層剝掉格子（先處理出度為 0 的，再用 BFS 一層一層來）。

<!-- e2575ac69383 -->
### 變形：狀態必須帶上「上一步」 — LC 403 Frog Jump

> **轉折**：光知道某顆石頭可達還不夠——下一步能跳多遠取決於你剛剛跳了多遠，所以狀態是 `(石頭索引, 上一次跳躍距離)` 這個配對。看出「位置本身不構成狀態」就是這題的全部。

**Java：**
<!--CODE-->

**Python：**
<!--CODE-->

> ⚠️ **Python 的遞迴深度**：同樣的警告——青蛙可以連跳到 `N` 顆石頭（LC 403 允許 2000），所以要調高 `sys.setrecursionlimit`，或改用標準的迭代版：`reach[i]` = 能落在石頭 `i` 上的跳躍距離集合，由左往右填。

<!-- cdd17a598213 -->
### 常見題目：
- LC 329: Longest Increasing Path in a Matrix
- LC 403: Frog Jump（狀態 = 位置 + 上一次跳躍距離）
- LC 1048: Longest String Chain（在單字上建 DAG；邊 = 「刪掉一個字元」）
- LC 787: Cheapest Flights Within K Stops（狀態 = `(city, stops used)`；正是那個轉機計數讓它無環）

<!-- 3ec925fe9be5 -->
## 16. 賽局理論／Minimax DP

狀態：`dp[i][j]` = 子陣列 `[i..j]` 上的最佳分數差（當前玩家 − 對手）。

<!--CODE-->

<!-- 681711ff118d -->
## 17. 搭配拓撲排序的 DAG 上 DP

當 DP 轉移只會從 DAG 中較前面的節點指向較後面的節點時，就照拓撲順序處理。

<!--CODE-->

<!-- 9ced3a2f74e9 -->
## 18. 單調佇列的 DP 最佳化

當 DP 轉移形如「`dp[i] = max/min(dp[j]) + cost`，其中 `j` 落在一個滑動視窗裡」時，用單調雙端佇列把 O(n²) 壓成 O(n)。

<!--CODE-->

<!-- d92b739ebfca -->
## DP 解題的關鍵步驟

1. **判斷這是不是 DP 題**：找最佳子結構與重疊子問題
2. **定義狀態**：哪些參數能唯一決定一個子問題？
3. **定義遞迴關係**：子問題之間怎麼互相牽連？
4. **找出基底情況**：最小的子問題長什麼樣？
5. **決定做法**：由上而下（記憶化）還是由下而上（表格化）？
6. **最佳化空間**：能不能降維，或改用滾動陣列？

<!-- 824b5e685c1a -->
## DP 最佳化技巧

- **空間最佳化**：只需要前一列／前一行時，用一維陣列取代二維
- **滾動陣列**：只保留最近 k 列／k 個狀態，而不是全部
- **狀態壓縮**：用位元遮罩把狀態壓起來
- **單調佇列／堆疊**：最佳化以視窗為基礎的 DP（滑動視窗最大值）
- **矩陣快速冪**：處理 n 很大的線性遞迴
- **凸包優化（Convex Hull Trick）**：最佳化某些特定形式的遞迴關係

<!-- 1f0741a7ee8f -->
### 記憶化 vs 表格化：什麼時候用哪個
| 面向 | 記憶化（由上而下） | 表格化（由下而上） |
|--------|----------------------|----------------------|
| 程式碼清晰度 | 貼近遞迴 → 比較好寫 | 需要明確安排順序 |
| 空間 | 堆疊框架 + 快取 | 只有 DP 表 |
| 子問題 | 只算需要用到的子問題 | 所有子問題都算 |
| 面試預設 | 從這裡開始 | 被要求 O(1) 空間時再換 |
| 無窮遞迴風險 | 有（遇到環） | 沒有 |

**原則**：面試時先用記憶化（比較好驗證正確性），如果空間是個問題，再最佳化成表格化。

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 8e105b22cbba -->
### 2-1) Climbing Stairs (LC 70) — 一維線性 DP
> dp[i] = dp[i-1] + dp[i-2]；費氏數列風格的 DP。模式看第 2 節（LIS）；同樣的滾動變數空間最佳化在這裡也適用。

<!-- 23905e7bd0d7 -->
### 2-2) Coin Change (LC 322) — 完全背包 DP
> dp[i] = 湊出金額 i 所需的最少硬幣數；把所有幣值都試一遍。

<!--CODE-->

<!-- 4c4704741963 -->
### 2-3) Longest Increasing Subsequence (LC 300) — LIS DP／二分搜尋
> dp[i] = 以索引 i 結尾的 LIS 長度；用 patience sorting 最佳化。O(n²) 與 O(n log n) 兩份模板都在第 2 節（LIS）。

<!-- 333a9ed24912 -->
### 2-4) Partition Equal Subset Sum (LC 416) — 0/1 背包 DP
> dp[j] = 是否存在總和為 j 的子集合；走訪物品並由右往左更新 dp。完整模板與「為什麼要反向走訪」的說明在第 6 節（0/1 背包）。

<!-- 948d64dd2499 -->
### 2-5) Unique Paths (LC 62) — 二維格子 DP
> dp[i][j] = 走到 (i,j) 的路徑數 = dp[i-1][j] + dp[i][j-1]；第一列／第一行都是 1。

<!--CODE-->

<!-- a13412d92841 -->
### 2-6) Decode Ways (LC 91) — 一維 DP
> dp[i] = 解碼 s[0..i-1] 的方法數；同時考慮 1 位數與 2 位數的解碼。

<!--CODE-->

<!-- 4e633cb18297 -->
### 2-7) Longest Common Subsequence (LC 1143) — 二維字串 DP
> dp[i][j] = s1[0..i-1] 與 s2[0..j-1] 的 LCS；相符時取斜對角 + 1，否則取相鄰兩格的最大值。

<!--CODE-->

<!-- 4f9e7d5e85da -->
### 2-8) Burst Balloons (LC 312) — 區間 DP
> dp[i][j] = 戳破 i 與 j 之間所有氣球能拿到的最大金幣；把每顆都試著當成最後戳的那顆。

<!--CODE-->

<!-- 820a91319cf8 -->
### 2-9) Best Time to Buy and Sell Stock with Cooldown (LC 309) — 狀態機 DP
> 三個狀態：hold、sold、rest；轉移規則強制賣出後要冷凍一天。

<!--CODE-->

<!-- 694530535bce -->
### 2-10) Minimum Path Sum (LC 64) — 格子 DP
> dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])；先把邊界初始化好。

<!--CODE-->

<!-- da712c3c3689 -->
### 2-11) Target Sum (LC 494) — DP／帶記憶化的 DFS
> 為每個數字指派 + 或 −；dp[j] = 湊出總和 j 的方法數。

<!--CODE-->
