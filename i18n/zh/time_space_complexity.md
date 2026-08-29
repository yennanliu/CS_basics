<!-- 7b0801062f5d -->
# 時間與空間複雜度 — 經典 LC 程式碼逐題解析

> **範圍** — **逐題解析** — 怎麼替一份真實的 LC 解答論證複雜度，每個技巧配一道經典題。
> **另見**：[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 查表用；[complexity_drills.md](./complexity_drills.md) — 自我測驗。

> **這份文件是什麼：** 一份以程式碼為主的搭配讀物。每道經典 LeetCode 題目我們都給出
> *實際的解答程式碼*，標上 `time`／`space` 複雜度，並解釋這個複雜度**為什麼**成立 —
> 再加上它所屬的**模式**與類似題目。
>
> **相關文件（不要重複內容）：**
> - [`complexity_cheatsheet.md`](./complexity_cheatsheet.md) — 參考表格 + 數學直覺（等比／等差級數、主定理）
> - [`complexity_drills.md`](./complexity_drills.md) — 自我測驗：從一段程式碼推導複雜度
> - [`lc_pattern.md`](./lc_pattern.md) — 模式 → 題目對照

---

<!-- 0ade3de42d0a -->
## 總覽

<!-- 1b18d4ad7c61 -->
### 怎麼讀複雜度（三個問題） ⭐⭐⭐⭐

<!--CODE-->

<!-- 0c74d3cd73b5 -->
### 「n 對應到多少複雜度才合理」的量尺 ⭐⭐⭐⭐⭐

<!--CODE-->
> 規則：一台機器每秒約做 10^8 次簡單運算。如果 `n × (每次運算的工作量) > 10^8`，就準備 TLE。

<!-- 39768c1fe295 -->
### 面試官最愛的兩個複雜度陷阱 ⭐⭐⭐⭐⭐

| 陷阱 | 錯誤答案 | 正確答案 | 原因 |
|------|-------|-------|-----|
| 建堆積 | O(n log n) | **O(n)** | `heapify` = 等比級數 ∑（見 [cheatsheet 3-1](./complexity_cheatsheet.md)） |
| 遞迴的堆疊空間 | 「O(n) 個節點」 | **O(h)** = 樹高 | DFS 同時只握有一條路徑，不是所有節點 |
| `j` 從 `i` 開始 | O(n) | **O(n²)** | n+(n-1)+...+1 = n(n+1)/2 |
| 在迴圈裡切片 `s[1:]` | O(n) | **O(n²)** | 每次切片都複製 O(n) 個字元 |

---

<!-- c28e5f46e157 -->
## 0) 速查表 — 經典 LC 一覽 ⭐⭐⭐⭐⭐

| # | 題目 | 模式 | 時間 | 空間 |
|---|---------|---------|------|-------|
| 1 | Two Sum | 雜湊表 | O(n) | O(n) |
| 20 | Valid Parentheses | 堆疊 | O(n) | O(n) |
| 21 | Merge Two Sorted Lists | 雙指標（鏈結串列） | O(n+m) | O(1) |
| 3 | Longest Substring w/o Repeat | 滑動視窗 | O(n) | O(min(n,Σ)) |
| 76 | Minimum Window Substring | 滑動視窗 | O(n+m) | O(Σ) |
| 704 | Binary Search | 二分搜尋 | O(log n) | O(1) |
| 33 | Search Rotated Sorted Array | 二分搜尋 | O(log n) | O(1) |
| 875 | Koko Eating Bananas | 對答案二分搜尋 | O(n log m) | O(1) |
| 56 | Merge Intervals | 排序 + 掃描 | O(n log n) | O(n) |
| 215 | Kth Largest Element | 堆積 / QuickSelect | O(n log k) / 平均 O(n) | O(k) / O(1) |
| 347 | Top K Frequent | 堆積 / 桶 | O(n log k) / O(n) | O(n) |
| 23 | Merge K Sorted Lists | 堆積 | O(N log k) | O(k) |
| 104 | Max Depth Binary Tree | DFS | O(n) | O(h) |
| 102 | Level Order Traversal | BFS | O(n) | O(w) |
| 200 | Number of Islands | 網格上的 DFS/BFS | O(m·n) | O(m·n) |
| 207 | Course Schedule | 拓撲排序 | O(V+E) | O(V+E) |
| 70 | Climbing Stairs | 一維 DP | O(n) | O(1) |
| 322 | Coin Change | 無限背包 DP | O(n·amount) | O(amount) |
| 300 | LIS | DP / patience sort | O(n²) / O(n log n) | O(n) |
| 78 | Subsets | 回溯 | O(n·2^n) | O(n) |
| 46 | Permutations | 回溯 | O(n·n!) | O(n) |
| 42 | Trapping Rain Water | 雙指標 | O(n) | O(1) |

---

<!-- 828720cee26f -->
## 1) 雜湊表 — 用空間換 O(1) 查詢

<!-- 3dcd98ffc3da -->
### LC 1 — Two Sum

**模式：** 記下「我看過什麼 → 它在哪」。把 O(n²) 的兩兩搜尋壓成 O(n)。

<!--CODE-->

**為什麼是 O(n) 時間／O(n) 空間：**
- 一個迴圈跑 n 次；每次做的都是 O(1) 的雜湊表操作 → **O(n)**。
- `seen` 最多長到 n 個 key → **O(n)** 額外記憶體。

**為什麼不是 O(n²)？** 暴力法會檢查每一對（`n(n+1)/2` 次比較）。雜湊表「記住」了互補值，
所以每個元素只被檢視一次。

> **類似題：** LC 49 Group Anagrams（O(n·k)）、LC 128 Longest Consecutive Sequence（O(n)）、LC 560 Subarray Sum = K。

---

<!-- 7d8e26afc63d -->
## 2) 堆疊 — 用 LIFO 順序做配對／回復

<!-- 0d7a774d7552 -->
### LC 20 — Valid Parentheses

<!--CODE-->

**為什麼是 O(n)/O(n)：** 單趟掃描，push/pop 都是 O(1)。堆疊最多可以裝 n 個沒配到對的左括號。

<!-- 0afce902a2a0 -->
### LC 84 — Largest Rectangle in Histogram（單調堆疊）

<!--CODE-->

**明明有內層 `while`，為什麼還是 O(n)？** 經典的攤還分析：每個索引**剛好被推入一次**、
**最多被彈出一次**，所以整趟執行下來內層迴圈的總工作量 ≤ 2n → **O(n)**，不是 O(n²)。

> **類似題：** LC 42 Trapping Rain Water、LC 496/503 Next Greater Element、LC 739 Daily Temperatures。見 [`monotonic_stack.md`](./monotonic_stack.md)。

---

<!-- 1a58e05ec8e6 -->
## 3) 滑動視窗 — 在子字串／子陣列上做到 O(n)

<!-- dab7d8d45696 -->
### LC 3 — Longest Substring Without Repeating Characters

<!--CODE-->

**為什麼是 O(n) 而不是 O(n²)？** 那個 `while` 看起來像巢狀迴圈，但 `left` 只會**遞增**，
而且永遠不會超過 `right`。整趟跑下來，`left` 總共只前進 ≤ n 步。所以兩個指標合起來的工作量是
O(n) → **O(n)**。

**為什麼空間是 O(min(n, Σ))？** 集合裡永遠不會超過相異字元的個數，而這個數同時被字串長度 n
和字母表大小 Σ（例如 26 或 128）夾住。

> **類似題：** LC 76 Min Window Substring（O(n+m)）、LC 424 Longest Repeating Char Replacement、LC 209 Min Size Subarray Sum、LC 438 Find All Anagrams。見 [`sliding_window.md`](./sliding_window.md)。

---

<!-- d5cda289c401 -->
## 4) 二分搜尋 — 每次砍掉一半搜尋空間 → O(log n)

<!-- d3f4dbb1a4a9 -->
### LC 704 — Binary Search

<!--CODE-->

**為什麼是 O(log n)？** n 要對半砍幾次才會到 1？`log₂(n)` 次。

<!-- c46186af6f2c -->
### LC 875 — Koko Eating Bananas（對**答案**二分搜尋） ⭐⭐⭐⭐

<!--CODE-->

**為什麼是 O(n log m)？** 我們是在*數值範圍* `m = max(piles)` 上做二分搜尋（→ `log m` 次迭代），
而每次可行性檢查是一趟 O(n) 掃描。答案空間具單調性（「速度 s 可行，s+1 就一定可行」），
這正是二分搜尋能套用的前提。

> **類似題：** LC 33 Search in Rotated Array（O(log n)）、LC 153 Find Min in Rotated Array、LC 410 Split Array Largest Sum、LC 1011 Capacity to Ship Packages。見 [`binary_search.md`](./binary_search.md)。

---

<!-- f4e16d275198 -->
## 5) 排序 + 掃描 — 先付一次 O(n log n)，之後都是 O(n)

<!-- f5994be3e645 -->
### LC 56 — Merge Intervals

<!--CODE-->

**為什麼是 O(n log n)？** 比較排序是瓶頸；排完之後那趟線性掃描相對來說「不用錢」。
**心得：** 看到「區間」、「重疊」或「排程」，先排序通常就能換到一趟線性掃描。

> **類似題：** LC 57 Insert Interval、LC 252/253 Meeting Rooms（II 會用到堆積）、LC 435 Non-overlapping Intervals、LC 1288 Remove Covered Intervals。見 [`intervals.md`](./intervals.md)。

---

<!-- a4003afd75c3 -->
## 6) 堆積（優先佇列） — 用低成本維持前 k 名

<!-- d68d5ac569a3 -->
### LC 215 — Kth Largest Element

<!--CODE-->

**為什麼是 O(n log k) 而不是 O(n log n)？** 堆積的大小被限制在 **k**，所以每次 push/pop 都是
`log k`（不是 `log n`）。掃過 n 個元素 → **O(n log k)**。空間是 **O(k)**，因為我們從不存超過
k 個元素。

**替代方案 — QuickSelect：平均 O(n)、額外空間 O(1)**（但最差 O(n²)）。如果你還需要*串流式*的
前 k 名，就用堆積。

> **類似題：** LC 347 Top K Frequent、LC 23 Merge K Sorted Lists（O(N log k)）、LC 295 Find Median from Data Stream（雙堆積）、LC 973 K Closest Points。見 [`heap.md`](./heap.md) / [`priority_queue.md`](./priority_queue.md)。

---

<!-- 0852bdc22098 -->
## 7) 樹 — 時間看節點數，空間看**樹高**（最經典的坑） ⭐⭐⭐⭐

<!-- 8303b6478525 -->
### LC 104 — Maximum Depth of Binary Tree

<!--CODE-->

**為什麼空間是 O(h) 而不是 O(n)？** ⭐ DFS 遞迴**一次只往下走一條路徑**。任何一個瞬間，
堆疊裡只有目前這條 root→leaf 路徑上的框架 = 樹高 `h`。它**不會**同時裝下全部 n 個節點。
- 平衡樹 → `h = log n` → 空間 **O(log n)**
- 歪斜樹（長得像鏈結串列） → `h = n` → 空間 **O(n)**

<!-- 4000bcc6d3ea -->
### LC 102 — Binary Tree Level Order Traversal（BFS）

<!--CODE-->

**DFS 與 BFS 的空間差別：** DFS = O(樹高)，BFS = O(樹寬)。平衡樹的最後一層就有大約 n/2 個節點，
所以 BFS 是 O(n) 而 DFS 只有 O(log n)。**依樹的形狀來選走訪方式。**

> **類似題：** LC 226 Invert Tree、LC 236 LCA（O(n)/O(h)）、LC 124 Max Path Sum（O(n)/O(h)）、LC 297 Serialize/Deserialize。見 [`tree.md`](./tree.md) / [`binary_tree.md`](./binary_tree.md)。

---

<!-- f74ac05510fe -->
## 8) 圖 — O(V + E)

<!-- dfb7d9465361 -->
### LC 200 — Number of Islands（在網格上做 DFS）

<!--CODE-->

**為什麼時間是 O(m·n)？** 一個有 `m·n` 個格子的網格，就是一張 `V = m·n` 個頂點、
`E ≈ 4·m·n` 條邊的圖。DFS/BFS 是 O(V+E) = **O(m·n)**。每個格子只會被沉掉（走訪）一次。

**為什麼空間是 O(m·n)？** 最差情況（一整座蛇行的島）遞迴深度可以到格子總數。
改成用佇列的 BFS 也是類似的上界，或者改用併查集。

<!-- 07a855fa7b02 -->
### LC 207 — Course Schedule（拓撲排序／偵測環）

<!--CODE-->

**為什麼是 O(V+E)？** 建圖時每條邊碰一次（O(E)）。BFS 每個節點只出隊一次（O(V)），
每條邊只鬆弛一次（O(E)）。總共 **O(V+E)**。

> **類似題：** LC 133 Clone Graph、LC 210 Course Schedule II、LC 743 Network Delay（Dijkstra O((V+E)log V)）、LC 684 Redundant Connection（併查集約 O(α)）。見 [`graph.md`](./graph.md) / [`topology_sorting.md`](./topology_sorting.md)。

---

<!-- e7a0e8684f6b -->
## 9) 動態規劃 — 定義狀態，然後算「狀態數 × 每個狀態的工作量」

<!-- a5d3c2b5411c -->
### LC 70 — Climbing Stairs（一維 DP，空間已最佳化）

<!--CODE-->

**為什麼是 O(n)/O(1)？** 總共 n 個子問題，每個都 O(1) 就能算完 → O(n) 時間。遞迴式只依賴
**前兩個**狀態，所以我們把整條 O(n) 陣列縮成 **O(1)** 的滾動變數。
> **空間最佳化的規則：** 如果 `dp[i]` 只讀 `dp[i-1]`、`dp[i-2]`，就用變數，別用陣列。

<!-- 6ae56c809aec -->
### LC 322 — Coin Change（無限背包）

<!--CODE-->

**為什麼是 O(n·amount)？** 狀態是「還要湊出多少金額」（`amount+1` 種取值），每個狀態都要試
全部 `n` 種硬幣 → **n × amount** 次轉移。這是*偽多項式*時間 — 它取決於 `amount` 這個
數值大小，而不只是輸入規模。

<!-- f59ef5a932c2 -->
### LC 300 — Longest Increasing Subsequence（兩種複雜度等級）

<!--CODE-->

<!--CODE-->

**加速的關鍵是什麼？** V1 的內層掃描是 O(n) → 整體 O(n²)。V2 把那趟掃描換成對已排序的 `tails`
陣列做 O(log n) 二分搜尋 → **O(n log n)**。答案一樣，漸進更好 — 這是教科書等級的
「你能做得更好嗎？」升級。

> **類似題：** LC 1143 LCS（O(m·n)）、LC 72 Edit Distance（O(m·n)）、LC 53 Maximum Subarray / Kadane（O(n)）、LC 5 Longest Palindromic Substring。見 [`dp.md`](./dp.md) / [`dp_pattern.md`](./dp_pattern.md)。

---

<!-- 746cee29003d -->
## 10) 回溯 — 由輸出量決定：O(答案數 × 每個答案的成本)

<!-- 9c1d5b2d7094 -->
### LC 78 — Subsets

<!--CODE-->

**為什麼是 O(n·2^n)？** 每個元素不是選就是不選 → **2^n** 個子集。把每個子集複製進結果是 O(n)
→ **n·2^n**。遞迴堆疊深度只有 **O(n)**（路徑長度）。

<!-- a9738f0b32d1 -->
### LC 46 — Permutations

<!--CODE-->

**為什麼是 O(n·n!)？** 一共有 `n!` 種排列；產生每一種要 O(n)。回溯的複雜度幾乎總是
**（解的個數）×（每個解的工作量）** — 去數遞迴樹的葉子就對了。

> **類似題：** LC 77 Combinations、LC 39/40 Combination Sum、LC 51 N-Queens（O(n!)）、LC 22 Generate Parentheses（結果數是卡塔蘭數）。見 [`backtrack.md`](./backtrack.md)。

---

<!-- 3554d9fbb8b7 -->
## 11) 雙指標 — O(n) 時間、O(1) 空間的甜蜜點

<!-- 9aefd2792a55 -->
### LC 42 — Trapping Rain Water

<!--CODE-->

**為什麼空間是 O(1)？** 樸素的 DP 要先算好 `leftMax[]` 和 `rightMax[]` 兩個陣列 = O(n) 空間。
雙指標的洞見 —「比較矮的那一側決定了水位」— 讓我們只需要追蹤兩個滾動最大值，
空間塌縮成 **O(1)**，同時保持 O(n) 時間。

> **類似題：** LC 11 Container With Most Water、LC 15 3Sum（O(n²)）、LC 167 Two Sum II、LC 125 Valid Palindrome、LC 26/27 原地移除重複元素。見 [`2_pointers.md`](./2_pointers.md)。

---

<!-- 296cb713ec80 -->
## 12) 速查 — 「面試時怎麼論證複雜度」

<!--CODE-->

<!-- 5a92a20ccbad -->
### 空間複雜度檢查清單

<!--CODE-->

<!-- 3eb717e0e8da -->
### 最後的合理性檢查

<!--CODE-->

<!-- 89e9f607eed7 -->
## 參考資料
- [`complexity_cheatsheet.md`](./complexity_cheatsheet.md) — 參考表格 + Big-O 數學（級數、主定理）
- [`complexity_drills.md`](./complexity_drills.md) — 從程式碼片段推導複雜度（測驗形式）
- [`lc_pattern.md`](./lc_pattern.md) — 模式 → LC 對照
- [Big-O Cheat Sheet](https://www.bigocheatsheet.com/)
