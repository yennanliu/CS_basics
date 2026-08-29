<!-- e6d4525d4497 -->
# 單調佇列（雙端佇列）

> **範圍** — 用一個維持單調性的雙端佇列，以攤還 O(1) 的成本求滑動視窗的最大／最小值。
> **另見**：[queue.md](./queue.md) — 單純的 FIFO；[monotonic_stack.md](./monotonic_stack.md) — 沒有視窗限制的對應版本；[sliding_window.md](./sliding_window.md) — 視窗本身的運作機制；[heap.md](./heap.md) — O(log n) 的替代方案。

<!-- 2f7a7871c94f -->
## LeetCode 題目清單

- [Monotonic Queue](https://leetcode.com/problem-list/monotonic-queue/)
- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Queue](https://leetcode.com/problem-list/queue/)

<!-- b5165b1b51d8 -->
## 概觀
**單調佇列**是一種讓元素保持單調順序（遞增或遞減）的雙端佇列。單調堆疊只能從一端移除元素，單調佇列則是**兩端都能移除**——這正是它能高效回答滑動視窗最小／最大值的原因。

<!-- 048a409a08fe -->
### 關鍵性質
- **時間複雜度**：滑動視窗類問題攤還 O(N)（每個元素最多進出各一次）
- **空間複雜度**：O(K)，K 是視窗大小
- **核心想法**：把候選者按單調順序放好；從前端丟掉過期的，從後端丟掉被壓過的
- **什麼時候用**：滑動視窗最小／最大值、有範圍限制的子陣列最佳化、DP 最佳化

<!-- e17b189964de -->
### 單調佇列 vs 單調堆疊

| 特性 | 單調堆疊 | 單調佇列 |
|---------|----------------|-----------------|
| 結構 | 堆疊（單端） | 雙端佇列（兩端） |
| 能從前端移除？ | 不行 | 可以——過期／視窗 |
| 主要用途 | 下一個更大／更小的元素 | 滑動視窗最小／最大值 |
| 有視窗限制？ | 沒有 | 有——範圍受限 |
| 代表題 | LC 84（直方圖） | LC 239（滑動視窗最大值） |

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- 45d61032112c -->
### **模式 1：滑動視窗最大／最小值** — LC 239
- **說明**：求每個大小為 K 的視窗裡的最大或最小值
- **例題**：LC 239（Sliding Window Maximum）、LC 1438（Longest Subarray with Abs Diff ≤ Limit）
- **模式**：求最大值用遞減佇列，求最小值用遞增佇列；元素滑出視窗就從前端彈掉

<!-- 91d44871afde -->
### **模式 2：範圍受限的 DP 最佳化** — LC 1696
- **說明**：DP 轉移時要從一個滑動範圍裡挑最好的值
- **例題**：LC 1425（Constrained Subsequence Sum）、LC 1696（Jump Game VI）
- **模式**：dp[i] = max(dp[j]) + val[i]，j 落在 [i-K, i-1] → 用單調雙端佇列維護這個最大值

<!-- 3f632dddd4db -->
### **模式 3：帶總和限制的最短子陣列** — LC 862
- **說明**：找出滿足某個總和條件的最短／最長子陣列
- **例題**：LC 862（Shortest Subarray with Sum ≥ K）、LC 1499（Max Value of Equation）
- **模式**：在前綴和上跑單調雙端佇列

<!-- 335eb8c01e91 -->
### **模式 4：多佇列（同時維護最小與最大）** — LC 1438
- **說明**：在滑動視窗中同時掌握最小值與最大值
- **例題**：LC 1438（Longest Subarray with Abs Diff ≤ Limit）
- **模式**：兩條雙端佇列——一條遞增（最小值）、一條遞減（最大值）

<!-- 2c6db1f58e27 -->
### **模式 5：順序統計量視窗（雙端佇列在這裡行不通）** — LC 480
- **說明**：視窗查詢要的是**中位數／第 k 小**，而不只是最小或最大值
- **例題**：LC 480（Sliding Window Median）
- **模式**：單調雙端佇列只能回答視窗的*極值*。任何「落在中間」的東西，都需要有序多重集合（`TreeSet` / `SortedList`）或**兩個堆積＋延遲刪除** → O(N log K)

<!-- 7c85c76438bd -->
### **模式 6：過期佇列（會自動失效的區間影響）** — LC 995
- **說明**：在索引 `i` 施加的操作會影響 `[i, i+k-1]`，之後就失效
- **例題**：LC 995（Minimum Number of K Consecutive Bit Flips）
- **模式**：佇列裝的是仍在生效的操作索引；當 `front + k <= i` 就從前端彈掉。`queue.size()` 的奇偶性就是目前的翻轉狀態（等於用佇列表達的差分陣列）。它**不是**單調的——這裡的佇列純粹當成一個過期視窗在用

<!-- 38b03b863ae4 -->
### **模式 7：環狀陣列 → 加倍前綴和＋受限雙端佇列** — LC 918
- **說明**：求環狀陣列的最佳子陣列（允許繞回頭）
- **例題**：LC 918（Maximum Sum Circular Subarray）
- **模式**：把陣列接一份在後面、取前綴和，然後在 `i - j <= n` 的額外限制下最大化 `prefix[i] - prefix[j]` → 用前綴索引的遞增雙端佇列，搭配前端過期彈出

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- c121e018923d -->
### 模板 1：滑動視窗最大值（遞減雙端佇列） — LC 239

<!--CODE-->

<!--CODE-->

<!-- 63c8c7f46149 -->
### 模板 2：DP 最佳化 — Jump Game VI（LC 1696）

<!--CODE-->

<!--CODE-->

<!-- ba6a29fb63e9 -->
### 模板 3：Shortest Subarray with Sum ≥ K（LC 862）

<!--CODE-->

<!-- 6c36212ba64b -->
### 模板 4：滑動視窗中位數 — 有序多重集合／兩個堆積（LC 480） ⭐⭐⭐⭐

> **為什麼不能用雙端佇列？** LC 239 能用，是因為答案永遠是視窗的*極值*，所以被壓過的元素可以永遠丟掉。但**中位數**可能是任何一個元素，什麼都不能丟 → 雙端佇列在這裡毫無用處。改用有序多重集合，或兩個堆積搭配**延遲刪除**。

<!--CODE-->

<!--CODE-->

<!-- 371281149b29 -->
### 模板 5：過期佇列 — K-Consecutive Bit Flips（LC 995） ⭐⭐⭐⭐

> **和模板 1 的差別**：這裡的雙端佇列**不是單調的**。它存的是每個仍在生效的翻轉的起始索引；`queue.size() % 2` 就是位置 `i` 上累積的翻轉狀態（用佇列表達的差分陣列）。

<!--CODE-->

<!--CODE-->

<!-- ccfb09a709ff -->
### 模板 6：環狀子陣列 — 加倍前綴和＋受限雙端佇列（LC 918） ⭐⭐⭐

> **和模板 3 的差別**：一樣是「前綴和＋遞增雙端佇列」，但這裡是在*長度*上限之下**最大化** `prefix[i] - prefix[j]`，而不是找滿足總和下限的最短子陣列。前端過期彈出所執行的，正好就是 `i - j <= n` 這個上限。

<!--CODE-->

<!--CODE-->

<!-- 4961c268b4b1 -->
## 關鍵決策指南

<!--CODE-->

<!-- d0ae9f689599 -->
## LC 範例

| # | 題目 | Difficulty | 模式 | Time | Space |
|---|---------|------------|---------|------|-------|
| 239 | Sliding Window Maximum | Hard | 遞減雙端佇列 | O(N) | O(K) |
| 862 | Shortest Subarray with Sum ≥ K | Hard | 前綴和＋遞增雙端佇列 | O(N) | O(N) |
| 1425 | Constrained Subsequence Sum | Hard | DP＋遞減雙端佇列 | O(N) | O(N) |
| 1438 | Longest Subarray Abs Diff ≤ Limit | Medium | 兩條雙端佇列（最小＋最大） | O(N) | O(N) |
| 1696 | Jump Game VI | Medium | DP＋遞減雙端佇列 | O(N) | O(K) |
| 1499 | Max Value of Equation | Hard | 遞減雙端佇列 | O(N) | O(N) |
| 2398 | Max Number of Robots Within Budget | Hard | 滑動視窗＋雙端佇列 | O(N) | O(N) |
| 480 | Sliding Window Median | Hard | 有序多重集合／兩個堆積＋延遲刪除（雙端佇列失效） | O(N log K) | O(K) |
| 995 | Min Number of K Consecutive Bit Flips | Hard | 過期佇列（非單調） | O(N) | O(K) |
| 918 | Maximum Sum Circular Subarray | Medium | 加倍前綴和＋遞增雙端佇列 | O(N) | O(N) |
| 2071 | Maximum Number of Tasks You Can Assign | Hard | 對答案二分搜尋＋雙端佇列當候選池 | O(N log N) | O(N) |

<!-- 80d6c82e3a7f -->
### 範圍說明

這份文件只談用**雙端佇列**解決的視窗題。用計數器／雜湊表撐起來的可變長度雙指標視窗（LC 3、76、209、424、438、567、992、1004）在 [`sliding_window.md`](sliding_window.md)；佇列／雙端佇列的**設計**題（LC 622、641、232、225）在 [`queue.md`](queue.md)。
