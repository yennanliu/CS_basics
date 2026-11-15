<!-- ac053d2a03ca -->
# 時間與空間複雜度 — 經典 LC 程式碼逐題解析

> **範圍** — **逐題解析** — 怎麼替一份真實的 LC 解答論證複雜度：每個技巧先配一道完整推導的經典題，再用一張依主題分類的總表複習高頻題，每題標出決定複雜度的那一行，以及面試官會追問的問題。
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

<!-- ebd69b71a57d -->
### 「n 對應到多少複雜度才合理」的量尺 ⭐⭐⭐⭐⭐

量尺只放在一個地方：[complexity_cheatsheet.md § 6](./complexity_cheatsheet.md#6-interview-decision-guide) — `n ≤ 20` 對應 O(2^n)，`n ≤ 10^5` 對應 O(n log n)，每秒約 10^8 次簡單運算是預算。

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

<!-- 60eea39da575 -->
### LC 215 — Kth Largest Element

<!--CODE-->

**為什麼是 O(n log k) 而不是 O(n log n)？** 堆積的大小被限制在 **k**，所以每次 push/pop 都是
`log k`（不是 `log n`）。掃過 n 個元素 → **O(n log k)**。空間是 **O(k)**，因為我們從不存超過
k 個元素。

**替代方案 — QuickSelect：平均 O(n)、額外空間 O(1)**（但最差 O(n²)）。如果你還需要*串流式*的
前 k 名，就用堆積。

> **類似題：** LC 347 Top K Frequent、LC 23 Merge K Sorted Lists（O(N log k)）、LC 295 Find Median from Data Stream（雙堆積）、LC 973 K Closest Points。見 [`heap.md`](./heap.md)。

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

<!-- c45f70337ba4 -->
### LC 207 — Course Schedule（拓撲排序／偵測環）

<!--CODE-->

**為什麼是 O(V+E)？** 建圖時每條邊碰一次（O(E)）。BFS 每個節點只出隊一次（O(V)），
每條邊只鬆弛一次（O(E)）。總共 **O(V+E)**。

> **類似題：** LC 133 Clone Graph、LC 210 Course Schedule II、LC 743 Network Delay（Dijkstra O((V+E)log V)）、LC 684 Redundant Connection（併查集約 O(α)）。見 [`graph.md`](./graph.md) / [`topology_sorting.md`](./topology_sorting.md)。

<!-- 4318d0981c9f -->
### O(V + E) vs O(V × E) —— 相加，還是相乘？ ⭐⭐⭐⭐

**原則：** 每個節點、每條邊*總共*只處理一次，就**相加**；*每個*節點或*每一輪*都把整組邊（或整張圖）再處理一次，就**相乘**。時間和空間用的是同一條原則：時間算的是步數，空間算的是同一時間留在記憶體裡的東西。

| 你看到的 | 時間 | 額外空間（不計輸入） | 例子 |
|---|---|---|---|
| 在鄰接表上跑一次 BFS / DFS | **O(V + E)** | O(V + E) | LC 207、LC 399 |
| 跑 `V − 1` 輪，每輪鬆弛全部 E 條邊 | **O(V × E)** | O(V) | Bellman-Ford、LC 743 |
| 從每個節點各跑一次完整走訪 | **O(V × (V + E))** | 看你留下什麼 | LC 1462、LC 2192 |
| 每個節點都去掃原始的邊清單找鄰居 | **O(V × E)** | O(V) | 是 bug，不是演算法 |

<!-- f005b3cfd389 -->
#### 時間 —— 為什麼一次走訪是 V + E，不是 V × E

<!--CODE-->

**陷阱：**「外層迴圈跑 V 次，內層迴圈最多跑 E 次，所以是 V × E。」內層迴圈的上界對*單一*節點成立，對全部節點加起來就錯了。每個節點的鄰居清單只被掃一次，而所有清單的長度加起來就是邊數（握手定理）：

<!--CODE-->

一個節點的鄰居多，其他節點的鄰居就少。總數是固定的：無向圖是 2E，有向圖的出邊清單是 E。

<!-- c1e531cb6986 -->
#### 時間 —— V × E 真正的來源

**1. 每一輪都掃一次整組邊 —— Bellman-Ford。**

<!--CODE-->

迴圈相乘了，記憶體沒有：每一輪都寫進**同一個** `dist` 陣列，所以空間維持 O(V)。Dijkstra 能以 O((V + E) log V) 解 LC 743；當權重可能為負，或邊數有上限（LC 787）時，才用 Bellman-Ford。見 [Bellman-Ford.md](./Bellman-Ford.md)。

**2. 從每個節點各重跑一次完整走訪。**

<!--CODE-->

這是 `V × (V + E) = O(V² + V × E)`，和 V × E 不一樣。除非已知圖是連通的（這時 E ≥ V − 1，可以化簡成 O(V × E)），否則兩項都要說出來。

**3. 靠掃邊清單來找鄰居。** 如果程式從不建鄰接表，而是每 pop 出一個節點就跑一次 `for u, v in edges`，那 V 個節點每個都會掃過全部 E 條邊：**O(V × E)**。在上面的星形圖上，就是 90 步而不是 18 步。修法是先花 O(E) 建好鄰接表，走訪就回到 O(V + E)。

<!-- 8e93e8ba480e -->
#### 空間 —— 數元素總數，不是數巢狀幾層

hashmap 的空間是**所有 key 底下存的元素總數**，不是「一個 key 算一個」，也不是「map 是巢狀的，所以相乘」。

<!--CODE-->

| 存了什麼 | 數量 | 原因 |
|---|---|---|
| keys | V = 4 | 每個節點一個 |
| 所有清單裡的元素 | 2E = 8 | 每條無向邊存兩次（A→B 和 B→A） |
| **總計** | **O(V + E)** | 常數 2 可以省略 |

巢狀 map 不會改變這個數量。LC 399 Evaluate Division 存的是 `graph[a][b] = value` 和 `graph[b][a] = 1 / value`：內層的 dict 只存該節點*自己的*鄰居，全部加起來是 2E 個元素，仍然是 **O(V + E)**。

只有當 V 個 key 每個都存一份**自己的**全部 E 條邊副本時，空間才會變成 **O(V × E)**：

<!--CODE-->

在上面的正方形上，那是 4 × 4 = 16 個元素，鄰接表只有 8 個。題目很少真的需要這樣存。如果你發現自己這樣寫，把邊只存一份，大家共用就好。

兩個容易被誤標成 V × E 的乘積：

- **鄰接矩陣**不管 E 多少都是 `V × V` 格：**O(V²)**。
- **為每個起點保留一份結果**，像上面的 LC 1462，是 V 個集合、每個最多 V 個節點：**O(V²)**。

<!-- 1e15e18842f4 -->
#### 面試時怎麼說

<!--CODE-->

> 「空間是 map 裡元素的總數，不是 key 的數量。鄰接表存了 V 個節點和 E 條邊，所以是 O(V + E)。只有在 V 個節點每個都保留一份自己的全部 E 條邊時，才會是 O(V × E)。時間也是同樣的道理：一次 BFS 每條邊只碰一次，所以是 O(V + E)；Bellman-Ford 在 V − 1 輪中每輪都鬆弛每一條邊，所以是 O(V × E)。」

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

<!-- e192a1934c39 -->
## 12) 經典 LC 依主題複習 — 面試官視角總表 ⭐⭐⭐⭐⭐

第 1–11 節每個技巧挑一道經典題，逐行推導。這一節求廣度：收錄上面沒有推導過的**高頻題**，依主題分組。題目取自 [Complexity Quiz](https://yennanliu.github.io/CS_basics/lc-complexity-quiz.html) 裡屬於 Blind 75 與 NeetCode 150 的題目，再加上它的 *Interview essentials* 題組。每一列列出複雜度、**決定這個複雜度的那一行或那個資料結構**，以及面試官最可能追問的問題或陷阱。LC 編號為**粗體**者屬於 [L3 core 題組](https://yennanliu.github.io/CS_basics/l3-core.html)。

把表格當成練習用：遮住後三欄，先把複雜度和理由說出口，再對答案。Quiz 會把同樣的題目連同程式碼一起出給你。

<!-- 3a098eac5d20 -->
### Google 面試官怎麼聽你的複雜度回答

複雜度算在 *problem solving* 與 *verification* 兩項訊號裡；答錯或講不出理由，扣的分比初版寫得慢還多。同樣是 O(n)，評分可以差很多：

| 候選人怎麼說 | 聽起來是 | 原因 |
|---|---|---|
| 「O(n)。」 | Leaning no hire（傾向不錄取） | 只有答案沒有理由，面試官還得再追問 |
| 「O(n)，一個迴圈。」 | Leaning hire（傾向錄取），前提是答對 | 有給理由，但常漏掉攤銷的內層迴圈或藏起來的切片 |
| 「時間 O(n)：`right` 和 `left` 各自最多移動 n 次，所以內層 `while` 加總是 n。空間 O(k)，是 deque。」 | Hire（錄取） | 點出決定複雜度的那一行、每個上界用的是哪個變數，並把空間分開講 |
| ……再加上「用 heap 是 O(n log k)，靠 deque 才做到線性。」 | Strong hire（強力錄取） | 還把解法放進各種替代方案裡比較，而這正是下一個追問要問的 |

**用四句話回答：** *(1) 先定義變數*（「n = len(nums)，k = 視窗大小」）；*(2) 時間，以及決定它的那一行*；*(3) 空間，以及佔用空間的結構，遞迴堆疊和輸出分開說*；*(4) 取捨*，也就是較慢的暴力解是什麼，以及更快或更省空間的解法要付出什麼代價。

<!-- 80a675dec79b -->
### 陣列與雜湊（Arrays & Hashing）

面試官在聽的是：你能不能說出雜湊表**換到了什麼**（省掉一個 log 因子或第二層迴圈）以及**付出了什麼**（O(n) 空間）。接著的追問通常是要求 O(1) 空間。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **121** | Best Time to Buy and Sell Stock | O(n) | O(1) | 一次掃描，維護目前最低價與最佳利潤 | 檢查每一組買賣日是 O(n²) |
| **238** | Product of Array Except Self | O(n) | O(1) | 先做前綴、再做後綴；兩個迴圈前後接續，是相加 | 輸出陣列不計入；後綴只是一個變數 |
| **217** | Contains Duplicate | O(n) | O(n) | 一次掃描，`in seen` 平均 O(1) | 排序後比較相鄰元素：O(1) 空間，O(n log n) 時間 |
| 169 | Majority Element | O(n) | O(1) | Boyer–Moore 只留 `candidate` 與 `count` | `Counter` 要 O(n) 空間；排序是 O(n log n) |
| 41 | First Missing Positive | O(n) | O(1) | 每次交換都把一個值放到它最終的位置，所以總共最多 n 次交換 | `for` 裡有 `while` 但仍是線性：要數交換次數，不是數迴圈 |
| 36 | Valid Sudoku | O(1) | O(1) | 盤面固定 9 × 9，81 格 × 3 個鍵 | 這裡沒有 n；換成一般 n × n 盤面才是 O(n²) |
| 525 | Contiguous Array | O(n) | O(n) | 把 0 換成 −1，問題變成「這個前綴和出現過嗎」 | 對每個子陣列數 0 和 1 是 O(n²) |
| 974 | Subarray Sums Divisible by K | O(n) | O(min(n, k)) | 一次掃描；鍵是餘數 0..k−1 | 說 O(n) 空間不算錯但不夠緊：表裡最多 k 個鍵 |
| 289 | Game of Life | O(m·n) | O(1) | 每格固定讀 9 個鄰居；下一代狀態存在同一格的第 1 個位元 | 複製盤面要 O(m·n) 空間；追問會要求原地完成 |
| 442 | Find All Duplicates in an Array | O(n) | O(1) | 用 `nums[v−1]` 的正負號標記 v 出現過 | 只因為值落在 1..n 才行得通 |

<!-- 118afc253683 -->
### 雙指標與滑動視窗（Two Pointers & Sliding Window）

面試官在聽的是**攤銷**論證。「`left` 只會往前走」這句話，就能把巢狀的 `while` 變成 O(n)。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| 88 | Merge Sorted Array | O(m + n) | O(1) | 從尾端往前寫，每個位置只寫一次 | 從前往後合併需要複製，或是 O(m·n) 的搬移 |
| **567** | Permutation in String | O(m + n) | O(1) | 滑動一次；每次比較視窗是 26 格，屬於常數 | 這個比較是 O(26)，不是 O(m) |
| 283 | Move Zeroes | O(n) | O(1) | `fast` 掃一次，每個元素最多交換一次 | 迴圈裡 `remove(0)` + `append(0)` 是 O(n²) |
| **239** | Sliding Window Maximum | O(n) | O(k) | 每個索引進出 deque 各最多一次 | 每個視窗都呼叫 `max` 是 O(n·k)；用 heap 是 O(n log k)。見下方卡片 |
| 340 | Longest Substring with At Most K Distinct | O(n) | O(k) | `left` 永遠不往回走 | 空間是 O(k) 不是 O(n)：收縮會把表的大小壓住 |
| 1438 | Longest Subarray with Abs Diff ≤ Limit | O(n) | O(n) | 兩個單調 deque（最大與最小），每個元素各進出一次 | 用 heap 或 SortedList 存視窗是 O(n log n) |
| 977 | Squares of a Sorted Array | O(n) | O(1) | 最大的平方一定在兩端之一；從尾端往前填 | 先平方再排序是 O(n log n) |

<!-- d94512d38abd -->
### 堆疊（Stack）

面試官在聽的是「每個元素只 push 一次、最多 pop 一次」。這一句話幾乎能解決所有單調堆疊的複雜度。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **150** | Evaluate Reverse Polish Notation | O(n) | O(n) | 每個 token 一次 push，或兩次 pop | pop 的順序：第二個運算元先出來 |
| **853** | Car Fleet | O(n log n) | O(n) | 依位置排序；之後的掃描是線性 | 模擬車子互相追上是 O(n²) |
| 402 | Remove K Digits | O(n) | O(n) | 單調堆疊；總共只有 n 次 push | 每刪一個就從頭重掃是 O(n·k) |
| 735 | Asteroid Collision | O(n) | O(n) | 被撞毀的小行星不會再回來，所以 pop 總數 ≤ n | 一顆大的連撞好幾顆，看起來像 O(n²) |
| 907 | Sum of Subarray Minimums | O(n) | O(n) | 兩次單調掃描，找出前一個與下一個較小元素 | 一邊用 `>`、另一邊用 `>=`，相等的值才不會被算兩次 |
| 503 | Next Greater Element II | O(n) | O(n) | 迭代 2n 次，但只有前 n 次會 push 索引 | 繞兩圈只是掃描加倍，堆疊的工作量沒有加倍 |
| 1249 | Minimum Remove to Make Valid Parentheses | O(n) | O(n) | 配對一次 + 處理剩下的一次 + 一次 `join` | 迴圈裡寫 `result += c` 可能變成 O(n²) |
| 227 | Basic Calculator II | O(n) | O(n) | `*`、`/` 直接併進堆疊頂端；最後 `sum` | O(1) 空間：只留累計總和與最後一項 |
| 85 | Maximal Rectangle | O(m·n) | O(n) | 每一列的直方圖跑一次 LC 84，每列 O(n) | 看起來像 O(m·n²)；其實是 m 次攤銷 O(n) |

<!-- e2a992340fae -->
### 二分搜尋（Binary Search）

面試官在聽的是**被減半的是什麼**：可能是索引區間、攤平成一維的二維區間，或是答案的可能範圍。要把它說出來。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **74** | Search a 2D Matrix | O(log(m·n)) | O(1) | 對 m·n 個攤平後的位置做一次搜尋，`mid // n, mid % n` | = log m + log n，所以分兩次搜尋也是同樣的複雜度 |
| 34 | Find First and Last Position | O(log n) | O(1) | 兩次 lower-bound 搜尋 | 找到一個再往外擴，在 `[7,7,7,…]` 上是 O(n) |
| **4** | Median of Two Sorted Arrays | O(log min(m, n)) | O(1) | 在**較短**的陣列上二分搜尋切點 | 合併是 O(m + n)，題目明確不允許 |
| 162 | Find Peak Element | O(log n) | O(1) | 往較大的鄰居走，區間內一定還有峰值 | 陣列沒排序，大家就以為只能 O(n) |
| 528 | Random Pick with Weight | 每次 O(log n) | O(n) | 對前綴和做 `bisect` | 依權重展開成多份複製要 O(W) 空間 |
| 540 | Single Element in a Sorted Array | O(log n) | O(1) | 在單一元素之前，每一對都從偶數索引開始 | XOR 是 O(n)，那是 LC 136 的解法，沒用到已排序這個性質 |
| 1539 | Kth Missing Positive Number | O(log n) | O(1) | `arr[i] − (i + 1)` 單調遞增，所以可以對它二分 | 從 1, 2, 3… 一個個數是 O(n + k)，而 k 可能遠大於 n |

<!-- c1759fb82889 -->
### 鏈結串列（Linked List）

面試官在聽的是 **O(1) 空間**的解法。大多數串列題都有一個簡單的 O(n) 空間版本（用 set，或把值複製進陣列），這類題目就是要看你能不能避開它。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **206** | Reverse Linked List | O(n) | O(1) | 走一次，三個指標 | 遞迴版本要 O(n) 堆疊 |
| **141** | Linked List Cycle | O(n) | O(1) | 進入環之後，`fast` 每步追近一個節點 | 用 `seen` set 要 O(n) 空間 |
| **21** | Merge Two Sorted Lists | O(n + m) | O(1) | 直接接上既有節點；剩下的一段 O(1) 接上 | 遞迴版本要 O(n + m) 堆疊 |
| **19** | Remove Nth Node From End | O(L) | O(1) | `fast` 先走 n 步，再兩個一起走 | 把節點複製進陣列再從尾端索引，要 O(L) 空間 |
| **143** | Reorder List | O(n) | O(1) | 找中點 → 反轉 → 交錯合併：三次掃描前後接續 | 用節點陣列時間相同，但要 O(n) 空間 |
| **138** | Copy List with Random Pointer | O(n) | O(n) | 兩次掃描，搭配舊 → 新的對照表 | O(1) 空間：把複製節點穿插進原串列，再拆開 |
| 148 | Sort List | O(n log n) | O(log n) | 由上而下的合併排序，log n 層 | O(1) 空間要用由下而上的合併排序 |
| 234 | Palindrome Linked List | O(n) | O(1) | 原地反轉後半段再比較 | `vals == vals[::-1]` 要 O(n) 空間 |
| **287** | Find the Duplicate Number | O(n) | O(1) | 在 `i → nums[i]` 上跑 Floyd | set 要 O(n) 空間；排序會改動輸入 |
| **25** | Reverse Nodes in k-Group | O(n) | O(1) | 每個節點走兩次：一次檢查、一次反轉 | n/k 組 × O(k) = O(n)，不是 O(n·k) |
| 160 | Intersection of Two Linked Lists | O(m + n) | O(1) | 交換串列之後，兩個指標離終點一樣遠 | 看起來可能無窮迴圈；其實 m + n 步內一定結束 |
| **2** | Add Two Numbers | O(m + n) | O(1) 額外 | 一次掃描加進位；新節點屬於輸出 | 轉成整數相加，在固定位元寬度的語言會溢位 |
| 142 | Linked List Cycle II | O(n) | O(1) | 相遇點 → 從頭重新出發 → 在入口相遇 | 第二個 `while` 在第一個之後執行，不是在它裡面 |
| 92 | Reverse Linked List II | O(n) | O(1) | 走到 `left`，再做 `right − left` 次頭插 | 把值複製進陣列要 O(n) 空間 |

<!-- b9f7fb8f9a5d -->
### 樹（Trees）

面試官在聽的是**空間 = 樹高**，要講成「O(h)：平衡時 log n，歪斜時 n」。還有確認沒有哪棵子樹被每個祖先各走一次，那是藏起來的 O(n²)。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **98** | Validate Binary Search Tree | O(n) | O(h) | 往下傳 `(lo, hi)` 範圍，每個節點 O(1) | 在每個節點呼叫 `max(left)`／`min(right)` 是 O(n²) |
| **543** | Diameter of Binary Tree | O(n) | O(h) | 一次後序走訪並回傳深度 | 從每個節點另外呼叫 `depth()` 是 O(n²) |
| **105** | Construct Tree from Preorder & Inorder | O(n) | O(n) | 值 → 索引對照表；每個節點一次 `next()` | `inorder.index()` 加上切片是 O(n²) |
| **230** | Kth Smallest Element in a BST | O(h + k) | O(h) | 迭代式中序走訪，到第 k 個就停 | 先收集完整的中序串列要 O(n) 時間與空間 |
| **199** | Binary Tree Right Side View | O(n) | O(w) | 逐層 BFS；`len(q)` 固定每一層的大小 | `while` 裡的 `for` 仍然只走訪每個節點一次 |
| **110** | Balanced Binary Tree | O(n) | O(h) | 一次走訪中回傳高度，不平衡時回傳 −1 | 由上而下在每個節點算 `height()`：平衡時 O(n log n)，歪斜時 O(n²) |
| **235** | LCA of a BST | O(h) | O(1) | BST 的大小順序讓每一層只需選一個方向 | 一般樹上的 LC 236 是 O(n) |
| **572** | Subtree of Another Tree | O(m·n) | O(m + n) | 從 m 個節點的每一個開始跑 `same()` | O(m + n)：兩棵樹都帶 null 標記序列化，再用 KMP |
| 108 | Sorted Array to BST | O(n) | O(log n) | 取中間；結果是平衡的 | 切片 `nums[:mid]` 會多出 O(n log n) 的複製 |
| 437 | Path Sum III | O(n) | O(n) | 前綴和 + 雜湊表；回溯時撤銷 | 在每個節點重新開始計數是 O(n²) |
| **100** | Same Tree | O(n) | O(h) | 兩棵樹同步遞迴 | 它是 LC 572 的內層迴圈，在那裡就不再是線性 |
| 113 | Path Sum II | O(n·h) | O(h) 額外 | 每個符合的葉節點都要複製一條最長 h 的路徑 | 說 O(n) 只算了走訪，忘了複製 |
| 662 | Maximum Width of Binary Tree | O(n) | O(n) | 逐層 BFS，帶位置索引 | 寬度可能到 2^h，但工作量是按節點算的 |
| 450 | Delete Node in a BST | O(h) | O(h) | 往下一條路徑，加上後繼節點的左側鏈 | 只有樹平衡時才是 O(log n) |
| 863 | All Nodes Distance K | O(n) | O(n) | 父節點對照表，再用 `seen` 做 BFS | `range(k)` 迴圈不會讓成本乘上 k：沒有節點被走訪兩次 |
| 116 | Populating Next Right Pointers | O(n) | O(1) | 沿著上一層已設好的 `next` 連結走完每一層 | 用佇列的 BFS 要 O(n) 空間；追問要求 O(1) |

<!-- 28fe6c55e516 -->
### 堆積（Heap）

面試官在聽的是 heap 的大小。heap 限制在 k 個時是 **O(n log k)**，不是 O(n log n)。另外 `heapify` 是 O(n)。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **621** | Task Scheduler | O(t) | O(1) | 計數，再對 ≤ 26 個計數套公式 | 逐個時段模擬，常被說成 O(t·n) |
| **1046** | Last Stone Weight | O(n log n) | O(n) | 少於 n 回合 × O(log n) 的 heap 操作 | 每回合重新排序是 O(n² log n) |
| **703** | Kth Largest Element in a Stream | 每次 add O(log k) | O(k) | 大小上限 k 的 min-heap | 用已排序串列，每次插入要搬移，是 O(k) |
| **355** | Design Twitter | 每次 feed O(f) | O(f) | 每位追蹤對象最多取 10 則，heap 上限 10 | 合併整條時間軸，是對所有推文的 O(N log N) |
| 692 | Top K Frequent Words | O(n + k log u) | O(u) | 計數、`heapify`（O(u)），再 pop k 次 | `heapify` 是線性；一個個 push 才是 u log u |
| 373 | Find K Pairs with Smallest Sums | O(k log k) | O(k) | 每一列放一個起點；某列的配對被 pop 時才往前推進 | 產生全部 m·n 組配對差得多 |

<!-- 037e908a339a -->
### 圖（Graphs）

面試官在聽的是**這個輸入的 V 和 E 是什麼**。網格的 V = m·n、E ≈ 4V；鄰接矩陣不管有幾條邊都要 V²。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **994** | Rotting Oranges | O(m·n) | O(m·n) | 多源 BFS；每格只入佇列一次 | `for _ in range(len(q))` 只是標記層次，沒有增加工作量 |
| 547 | Number of Provinces | O(n²) | O(n) | 鄰接**矩陣**：每一列都要掃 | 其實就是 O(V + E)，只是 E 讀成 n² |
| **417** | Pacific Atlantic Water Flow | O(m·n) | O(m·n) | 兩次淹水填充，從兩個海洋的邊界往內 | 從每一格往外搜尋是 O((m·n)²) |
| **695** | Max Area of Island | O(m·n) | O(m·n) | 進入一格就把它淹掉 | 島嶼是蛇形時，堆疊空間可能裝下整個網格 |
| **127** | Word Ladder | O(n·L²) | O(n·L) | 每個單字：L 個位置 × 26 個字母 × O(L) 的字串建立與雜湊 | 大多數人漏掉第二個 L。見下方卡片 |
| **323** | Number of Connected Components | O(n + e·α(n)) | O(n) | 帶路徑壓縮的 union-find | 沒有壓縮時，一次 find 可能是 O(n) |
| **261** | Graph Valid Tree | O(n + e·α(n)) | O(n) | `len(edges) == n − 1`，再加上 union-find 確認沒有環 | 邊數檢查就做掉了一半的工作 |
| 721 | Accounts Merge | O(s log s) | O(s) | 每組 email 的排序是主要成本 | 在這題 union-find 反而是便宜的部分 |
| 542 | 01 Matrix | O(m·n) | O(m·n) | 一開始就把**所有** 0 放進 BFS | 從每個 1 各跑一次 BFS 是 O((m·n)²) |
| 785 | Is Graph Bipartite? | O(V + E) | O(V) | 邊走邊上色；外層迴圈處理不連通的部分 | 看起來像 V 次 BFS；`color` 防止重複走訪 |
| 909 | Snakes and Ladders | O(n²) | O(n²) | 對 n² 個格子做 BFS，每格 6 種走法 | 圖有 n² 個頂點；答 O(n) 是錯的 |

<!-- 328b0f9d18e9 -->
### 進階圖論（Advanced Graphs）

面試官在聽的是**用哪個演算法，以及為什麼它的複雜度適合這張圖**。Prim 演算法要用陣列還是 heap，取決於圖是稠密還是稀疏。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **269** | Alien Dictionary | O(c) | 圖為 O(1) | 比較相鄰單字；圖的大小上限是 26 個字母 | O(V + E) 答錯了問題：要用總字元數 c 表示 |
| **332** | Reconstruct Itinerary | O(e log e) | O(e) | 先排序機票，再用 Hierholzer 每條邊走一次 | 回溯嘗試各種機票順序是階乘級 |
| **778** | Swim in Rising Water | O(n² log n) | O(n²) | 在 n² 個格子上跑 Dijkstra；log(n²) = 2 log n | 不是 (log n)² |
| **1584** | Min Cost to Connect All Points | O(n²) | O(n) | 在完全圖上用陣列版 Prim | heap 版是 O(n² log n)，在稠密圖上反而更差 |
| 1631 | Path With Minimum Effort | O(m·n·log(m·n)) | O(m·n) | 用 minimax 鬆弛的 Dijkstra | 一般帶 `visited` 的 BFS 會答錯 |
| 1293 | Shortest Path with Obstacles Elimination | O(m·n·k) | O(m·n·k) | 狀態 = （列、行、剩餘可移除次數） | 說 O(m·n) 忘了多出來的狀態維度 |

<!-- b4ffd7d9cdc2 -->
### 動態規劃（Dynamic Programming）

面試官在聽的是**狀態數 × 轉移成本**，再問「表格能不能滾動？」。如果 dp[i] 只讀上一列，空間就能少一個維度。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **198** | House Robber | O(n) | O(1) | 每個狀態只讀 i−1 和 i−2，所以兩個變數就夠 | 單純遞迴是 O(2^n) |
| **62** | Unique Paths | O(m·n) | O(n) | 一列滾動陣列 | 用組合數 C(m+n−2, m−1) 是 O(min(m, n)) |
| **139** | Word Break | O(n²) 次切片 | O(n + m) | `for i` × `for j`，加上 set 查詢 | 每次 `s[j:i]` 切片和雜湊都是 O(n)，老實說是 O(n³)；用最長單字長度限制 j |
| **152** | Maximum Product Subarray | O(n) | O(1) | 同時追蹤最大與最小，因為負數會讓兩者互換 | `nums[1:]` 會默默複製整個陣列 |
| **91** | Decode Ways | O(n) | O(1) | 兩個滾動變數 | 單純遞迴每個索引分兩支：O(2^n) |
| **213** | House Robber II | O(n) | 用索引時 O(1) | 對 [0, n−2] 和 [1, n−1] 各跑一次 LC 198 | 切片陣列會變成 O(n) 空間 |
| **416** | Partition Equal Subset Sum | O(n·s) | O(s) | 一列對總和的布林陣列，倒著更新 | 偽多項式：s 是數值，不是輸入大小 |
| **647** | Palindromic Substrings | O(n²) | O(1) | 2n−1 個中心 × 往外擴展 | 檢查每個子字串是 O(n³)；DP 表要 O(n²) 空間 |
| 64 | Minimum Path Sum | O(m·n) | O(1) | 直接覆寫在網格上 | 列舉所有路徑是指數級 |
| 10 | Regular Expression Matching | O(m·n) | O(m·n) | 每個 (i, j) 一格，每格 O(1) | 單純遞迴是指數級 |
| **97** | Interleaving String | O(m·n) | O(n) | 滾動陣列 | 貪婪的雙指標遇到兩邊相同時會失敗 |
| **309** | Stock with Cooldown | O(n) | O(1) | 三狀態機：hold、sold、rest | 按天建的表可以縮成三個變數 |
| 312 | Burst Balloons | O(n³) | O(n²) | O(n²) 個區間 × n 種**最後**戳破的選擇 | 選第一個戳破的，會留下不連續的空隙 |
| **329** | Longest Increasing Path in a Matrix | O(m·n) | O(m·n) | 帶記憶化的 DFS，每格只解一次 | 沒有記憶化是指數級 |
| 494 | Target Sum | O(n·s) | O(s) | 一層可達總和，範圍 −s..s | 偽多項式；暴力解是 O(2^n) |
| 518 | Coin Change II | O(n·a) | O(a) | 外層硬幣、內層金額 | 兩層迴圈對調會改成計算排列順序（LC 377） |
| 516 | Longest Palindromic Subsequence | O(n²) | O(n²) → O(n) | 區間 DP；只會讀第 i 和 i+1 列 | 列舉子序列是 O(2^n) |
| 221 | Maximal Square | O(m·n) | O(n) | min(上, 左, 左上) + 1，滾動陣列 | 從每個角落往外長正方形差得多 |
| 1235 | Maximum Profit in Job Scheduling | O(n log n) | O(n) | 依結束時間排序 + 二分搜尋前一個相容的工作 | 線性往回掃是 O(n²) |
| 96 | Unique Binary Search Trees | O(n²) | O(n) | 對每個根做 Catalan 加總 | 封閉式 C(2n, n)/(n+1) 可做到 O(n) |
| 44 | Wildcard Matching | O(m·n) | O(m·n) | `*` 處理成 dp[i−1][j] 或 dp[i][j−1] | 對 `*` 可能吃掉的每種長度跑迴圈會變成 O(m²·n) |

<!-- 3018a1fd74d7 -->
### 回溯（Backtracking）

面試官在聽的是**葉節點數 × 每個葉節點的成本**，而且要算進複製到結果裡的成本。少了「建出每個答案」這個因子，是最常見的漏算。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **79** | Word Search | O(m·n·3^L) | O(L) | m·n 個起點 × 分支 3（第一步是 4），共 L 層 | 4^L 也算對；3^L 較緊，因為不會走回頭 |
| **17** | Letter Combinations of a Phone Number | O(n·4^n) | O(n) 額外 | 最多 4^n 個葉節點 × O(n) 的 `join` | O(4^n) 只算了字串數，沒算長度 |
| **131** | Palindrome Partitioning | O(n·2^n) | O(n) 額外 | 2^(n−1) 種切法 × O(n) 的回文檢查 | 先算好 O(n²) 的回文表 |
| **90** | Subsets II | O(n·2^n) | O(n) 額外 | 2^n 個子集 × 複製 | 去重不影響最壞情況（所有值都不同時） |
| **40** | Combination Sum II | O(n·2^n) | O(n) | 每個值選或不選，每找到一組就複製 | `break` 剪枝取決於數值，不取決於 n |
| 47 | Permutations II | O(n·n!) | O(n) | 全部相異的輸入是最壞情況 | 用 set 收集結果，時間相同、空間更多 |
| 93 | Restore IP Addresses | O(1) | O(1) | 4 段 × 每段 ≤ 3 位數，最多 3^4 個葉節點 | 看起來是指數級，但深度和分支數都是固定的 |
| 797 | All Paths From Source to Target | O(n·2^n) | O(n) 額外 | DAG 最多可能有 2^(n−2) 條路徑，每條都要複製 | 不是 O(V + E)：刻意不用 `visited` |

<!-- 9cbbcbca9645 -->
### 字典樹（Tries）

面試官在聽的是用**單字長度 L** 表示的複雜度，而不是單字的數量。正因為與單字數量無關，才要用 trie。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **208** | Implement Trie | 每次操作 O(L) | 每次插入 O(L) | 每個字元一次 dict 查找 | 兩個複雜度裡都沒有 n |
| **211** | Add and Search Words | 搜尋最壞 O(26^L) | O(L) 堆疊 | 每個 `.` 都分岔到所有子節點 | 一般單字是 O(L)；只有萬用字元才是指數級 |
| **212** | Word Search II | O(m·n·3^L) | O(k) | 透過 trie 共用同一次 DFS | 每個單字各跑一次 LC 79 會乘上 w |

<!-- 1acc5ccc89bc -->
### 區間（Intervals）

面試官在聽的是**排序一次、掃描一次**。排序是主要成本，所以除非輸入已排序，複雜度是 O(n log n)。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **253** | Meeting Rooms II | O(n log n) | O(n) | 排序 + 結束時間的 min-heap | `rooms[0]` 是 O(1) 的查看，不是掃描 |
| **1851** | Minimum Interval to Include Each Query | O(n log n + q log q) | O(n + q) | 兩者都排序；每個區間只進出 heap 一次 | 只能離線處理，所有查詢都要事先知道 |
| 986 | Interval List Intersections | O(m + n) | O(1) 額外 | 推進先結束的那個區間 | 兩個輸入都已排序，所以沒有 log 因子 |

<!-- 7904315658c0 -->
### 貪婪（Greedy）

面試官在聽的是**一次掃描 + 說明為什麼貪婪是安全的**。複雜度很簡單，真正在考的是證明。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **55** | Jump Game | O(n) | O(1) | 追蹤最遠可達的索引 | DP 是 O(n²) |
| **45** | Jump Game II | O(n) | O(1) | 隱式的逐層 BFS：`end` 與 `farthest` | 要在 n−1 停下，不然會多算一跳 |
| **134** | Gas Station | O(n) | O(1) | 只要總油量夠付總消耗，唯一留下的起點就可行 | 從每一站各模擬一次是 O(n²) |
| **763** | Partition Labels | O(n) | O(1) | 每個字母的最後索引（≤ 26 個鍵） | 表的大小受字母表限制，不是 n |
| **846** | Hand of Straights | O(n log n) | O(n) | 排序不同的牌；內層迴圈加總是 O(n) | 看起來像 O(n·g) |
| 31 | Next Permutation | O(n) | O(1) | 找樞紐 → 交換 → 反轉尾段 | 排序尾段是 O(n log n)；它本來就是遞減的 |
| **678** | Valid Parenthesis String | O(n) | O(1) | 追蹤左括號數的 [low, high] 範圍 | 嘗試 `*` 的每一種意義是 O(3^n) |
| 406 | Queue Reconstruction by Height | O(n²) | O(n) | 排序後，在位置 k `list.insert` 會搬移後段 | 要 O(n log n) 得用 Fenwick tree 找第 k 個空位 |
| 334 | Increasing Triplet Subsequence | O(n) | O(1) | 兩個目前最小值 | 它就是長度 3 的 patience sorting |

<!-- 1bb869c8f51d -->
### 排序（Sorting）

面試官在聽的是**一次比較的成本**。比較時要組字串的話，排序成本是 O(n log n) × 那次比較的成本。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **242** | Valid Anagram | O(n log n) | O(n) | 兩次 `sorted` | `Counter` 或 26 格的計數是 O(n) |
| 75 | Sort Colors | O(n) | O(1) | 荷蘭國旗：每一步讓 `i` 前進或讓 `hi` 後退 | 跟 `hi` 交換後 `i` 不前進 |
| 315 | Count of Smaller Numbers After Self | O(n log n) | O(n) | 名次壓縮 + Fenwick tree，每個值兩次 O(log n) 走訪 | 兩層迴圈是 O(n²) |
| 179 | Largest Number | O(k·n log n) | O(n·k) | n log n 次比較，每次串接長度 ≤ 2k 的字串 | 在這裡把一次比較當成 O(1) 是錯的 |

<!-- cc874bfbbfe3 -->
### 位元運算（Bit Manipulation）

面試官在聽的是**固定寬度就是 O(1)**。對 32 位元整數的位元跑迴圈是常數，說成 O(n) 等於把數值當成輸入大小。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| 136 | Single Number | O(n) | O(1) | XOR 讓成對的值互相抵消 | set 要 O(n) 空間 |
| **191** | Number of 1 Bits | O(1) | O(1) | `n &= n − 1` 執行 popcount ≤ 32 次 | 一般寬度 b 時是 O(b) |
| **338** | Counting Bits | O(n) | O(n) 輸出 | `dp[i] = dp[i >> 1] + (i & 1)` | 每個數做 `bin(i).count("1")` 是 O(n log n) |
| **268** | Missing Number | O(n) | O(1) | 索引與值全部 XOR 起來 | 高斯求和在固定位元寬度的語言會溢位 |
| **190** | Reverse Bits | O(1) | O(1) | 剛好 32 次迭代 | 不是 log n，因為迴圈不會提早結束 |
| **7** | Reverse Integer | O(log x) | O(1) | 每位數一步，≤ 10 位 | 溢位檢查才是這題真正的重點 |

<!-- 24897e9ad73b -->
### 數學與幾何（Math & Geometry）

面試官在聽的是矩陣的**原地**技巧，以及對數量級的正確推理：位數是 log x，快速冪是 O(log n)。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **48** | Rotate Image | O(n²) | O(1) | 轉置，再反轉每一列 | `zip(*matrix[::-1])` 要 O(n²) 空間 |
| **54** | Spiral Matrix | O(m·n) | O(1) 額外 | 四個逐漸內縮的邊界；每格只輸出一次 | 內層迴圈是在分攤工作，不是重複工作 |
| **73** | Set Matrix Zeroes | O(m·n) | O(1) | 用第 0 列和第 0 行當標記，再加一個旗標 | 兩個索引 set 要 O(m + n) 空間 |
| **50** | Pow(x, n) | O(log n) | O(log n) | 每次呼叫把指數減半 | 迭代版本是 O(1) 空間 |
| **202** | Happy Number | O(log n) | O(log n) | 第一次的位數平方和就把 n 縮成很小的值 | Floyd 環偵測可以省掉 set |
| 240 | Search a 2D Matrix II | O(m + n) | O(1) | 從右上角出發，每步刪掉一列或一行 | 攤平做一次二分搜尋是錯的：各列沒有首尾相接 |
| **43** | Multiply Strings | O(m·n) | O(m + n) | 每一位都和每一位相乘 | Karatsuba 約為 O(n^1.585) |

<!-- 87339fde6719 -->
### 設計（Design）

面試官在聽的是**每次操作的成本，以及它是不是攤銷的**。只有在你能說出昂貴的那次呼叫由誰買單時，「攤銷 O(1)」才算完整的答案。

| LC | 題目 | 時間 | 空間 | 決定複雜度的地方 | 追問／陷阱 |
|---|---|---|---|---|---|
| **155** | Min Stack | 每次操作 O(1) | O(n) | 存 `(val, min_so_far)` 配對 | `min(a, b)` 只比兩個值，沒有掃描堆疊 |
| **146** | LRU Cache | 每次操作 O(1) | O(c) | 雜湊表 + 雙向鏈結串列。見下方卡片 | `move_to_end` 只重新連結一個節點，沒有搬移任何東西 |
| **981** | Time Based Key-Value Store | get O(log n)，set O(1) | O(t) | 對每個鍵的時間戳串列做二分搜尋 | set 是 O(1) 的前提是時間戳遞增送進來 |
| **271** | Encode and Decode Strings | O(c) | O(c) | 長度前綴的封裝格式 | 單純用分隔符號不只是慢，而是錯的 |
| **2013** | Detect Squares | 每次 count O(n) | O(n) | 固定對角點；另外兩個角是 O(1) 查詢 | 嘗試每一對點是 O(n²) |
| 173 | BST Iterator | 攤銷 O(1) | O(h) | 所有呼叫加起來，每個節點只 push、pop 各一次 | 單次 `next` 可能要 O(h) |
| 380 | Insert Delete GetRandom O(1) | O(1) | O(n) | 陣列 + 值 → 索引對照表；和最後一個元素交換後 pop | `list.remove` 是 O(n) |

<!-- 7fc26cb40c27 -->
### 面試官追問最緊的三個推導

表格裡大多數的複雜度一句話就能說明。這三題是例外：最直覺的第一個答案是錯的，而追問正是為了測這一點。

<!-- c9b4935cb6c6 -->
#### Sliding Window Maximum — LC 239

> **Key point** — `for` 裡的 `while` 並不是每步 O(k)。每個索引只 append 一次、最多 pop 一次，所以整個執行過程的 pop 總數最多 n 次。

**Reduces to**：[§ 3 滑動視窗](#3-sliding-window--on-over-substringssubarrays)，把 § 3 的計數器換成單調 deque。攤銷論證和 § 2 的 LC 84 一樣。

<!--CODE-->

**怎麼說：**「時間 O(n)。內層 `while` 是攤銷的：每個索引只 push 一次、最多 pop 一次，所以整個執行過程最多 n 次 pop。deque 額外佔 O(k) 空間。替代方案是每個視窗呼叫 `max` 的 O(n·k)，以及用 heap 的 O(n log k)。」

<!-- 92d994b98b1b -->
#### LRU Cache — LC 146

> **Key point** — `get` 和 `put` 都要 O(1)，需要兩個結構。雜湊表 O(1) 找到節點，雙向鏈結串列 O(1) 移動或淘汰那個節點。只用其中一個都做不到兩件事。

**Reduces to**：[§ 1 HashMap](#1-hashmap--o1-lookup-trades-space-for-time)，讓表裡的值按最近使用的順序排列。

<!--CODE-->

**怎麼說：**「每次操作 O(1)。雜湊表提供 O(1) 查找，鏈結串列提供 O(1) 的移動與淘汰。空間 O(capacity)。如果只用一般串列，『移到最前面』這一步會是 O(n)。」面試官常會要求你自己寫出雙向鏈結串列，複雜度不變。

<!-- e397a548737b -->
#### Word Ladder — LC 127

> **Key point** — 每個出佇列的單字要做的工作是 L 個位置 × 26 個字母 × **O(L) 的字串建立與雜湊**。常見的答案 O(n·L) 忘了每個候選字都是一個新字串。

**Reduces to**：[§ 8 圖](#8-graphs--ov--e)。這是在隱式圖上的 BFS，V = n 個單字，邊是即時產生而不是事先存好的。

<!--CODE-->

**怎麼說：**「把它當成有 n 個頂點的隱式圖上的 BFS。每個單字嘗試 26·L 個鄰居，每個鄰居的建立與雜湊是 O(L)，所以時間 O(n·L²)、空間 O(n·L)。雙向 BFS 的最壞情況相同，但實際上探索的節點少得多。」

<!-- c1e22c571465 -->
## 13) 速查 — 「面試時怎麼論證複雜度」

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
