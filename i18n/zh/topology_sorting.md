<!-- 374739fee693 -->
# 拓撲排序 - 完整指南

> **範圍** — 為 DAG 排序 — Kahn 的 BFS 解法、DFS 後序、環的偵測，以及建構在它們之上的排程類題目。
> **另見** — [topology_sorting_examples.md](./topology_sorting_examples.md) — 這些模板背後的八道完整解題；[diff_toposort_quickunion.md](./diff_toposort_quickunion.md) — 拓撲排序 vs 併查集 — 什麼題該用哪個工具；[union_find.md](./union_find.md) — 無向圖的連通性；[graph.md](./graph.md) — 一般的圖論素材。

<!-- 8c715f0c3021 -->
## LeetCode 題目清單

- [Topological Sort](https://leetcode.com/problem-list/topological-sort/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

<!-- 92f04c2ace83 -->
## 總覽

拓撲排序是把有向無環圖（DAG）中的頂點排成一個線性順序，使得對每一條有向邊 (u, v)，頂點 u 都排在 v 之前。

<!-- 938fa57cdaee -->
### 關鍵特性
- **只適用 DAG**：僅在有向無環圖上成立
- **答案不唯一**：可能存在很多組合法的拓撲順序
- **解決相依關係**：處理有先修／相依關係的問題
- **應用場景**：任務排程、建置系統、課程規劃、相依性解析

<!-- ec1b803ab30f -->
### 複雜度分析
| 做法 | 時間複雜度 | 空間複雜度 | 適用情境 |
|----------|----------------|------------------|----------|
| DFS（三色後序） | O(V + E) | O(V) | 通用、偵測環 |
| BFS（入度） | O(V + E) | O(V) | 找出所有順序、逐層處理 |
| 找樹的重心 | O(V + E) | O(V) | 無向樹，找中心／最小化高度 |
| 列舉所有拓撲排序 | O(V! × (V + E)) | O(V) | 小圖、所有排列 |

<!-- 84220654b8ac -->
### 參考資料
- [techbridge : topological-sort](https://blog.techbridge.cc/2020/05/10/leetcode-topological-sort/)
- [DFS-based topological sort](https://alrightchiu.github.io/SecondRound/graph-li-yong-dfsxun-zhao-dagde-topological-sorttuo-pu-pai-xu.html)
- [topological_sort.py](https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/topological_sort.py)
- [TopologicalSort.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/TopologicalSort.java)
- [NumberOfProvinces.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/DFS/NumberOfProvinces.java)（連通分量／併查集）
- [MinimumHeightTrees.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/BFS/MinimumHeightTrees.java)（找樹的重心）
- [minimum-height-trees.py](https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/minimum-height-trees.py)（LC 310 — 葉節點剝除的各種解法，外加可以拿來對照的 O(n^2) 暴力解）
- [find-the-town-judge.py](https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Graph/find-the-town-judge.py)（LC 997 — 度數特徵的計數解法，以及集合差集的替代寫法）

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- d0c859fb1f15 -->
### 1. 課程排程
牽涉先修關係與課程順序的題目。
- **模式**：建出相依圖、檢查是否有環、找出合法順序
- **代表題目**：LC 207、210、630、1462

<!-- 162d0e918192 -->
### 2. 任務排程
牽涉任務相依與平行執行的題目。
- **模式**：求最短時間、平行處理的層數
- **代表題目**：LC 1136、2050、1857

<!-- 085563e22259 -->
### 3. 字典序排序
要求字典序最小／最大之拓撲順序的題目。
- **模式**：用優先佇列決定順序、外星文字典
- **代表題目**：LC 269、953、1203

<!-- d94207c695c1 -->
### 4. 建置順序與相依性
牽涉建置系統與套件相依的題目。
- **模式**：偵測環、找出建置順序、處理群組
- **代表題目**：LC 444、802、851

<!-- ac125a080cd3 -->
### 5. 圖的分層
牽涉在 DAG 上逐層處理的題目。
- **模式**：帶層數的 BFS、DAG 上的最長路徑
- **代表題目**：LC 2192、2115、1857

<!-- c2a523f58db2 -->
### 6. 環的偵測與安全狀態
聚焦在偵測環與找出安全節點的題目。
- **模式**：三色 DFS、辨識安全狀態
- **代表題目**：LC 802、207、1059

<!-- bb2b742bd39c -->
### 7. 連通分量（併查集／DFS）
牽涉在無向圖中找連通分量的題目。
- **模式**：帶路徑壓縮的併查集、用 DFS/BFS 遍歷計數分量
- **代表題目**：LC 547、200、323、684

<!-- 4b73163f5bdc -->
### 8. 找樹的重心
牽涉尋找無向樹之中心／重心的題目。
- **模式**：逐層剝除葉節點 — 就是把 Kahn 剝除法的種子條件從 `in_degree == 0` 換成 `degree == 1`
- **代表題目**：LC 310、1245、2603

<!-- 1f7289e337bb -->
## 核心模板

<!-- aa6481634a22 -->
### 模板 1：BFS（Kahn 演算法）⭐⭐⭐⭐⭐
<!--CODE-->

<!--CODE-->

<!-- 1f27d6b9dc01 -->
### 模板 2：DFS（三色標記）⭐⭐⭐⭐

<!--CODE-->

<!--CODE-->

<!-- 1904c71e1455 -->
### 模板 3：DFS（用堆疊）
<!--CODE-->

<!-- df39f9b28c8f -->
### 模板 4：字典序
<!--CODE-->

<!--CODE-->

<!-- ac54dbbba02f -->
### 模板 5：所有拓撲順序
<!--CODE-->

<!-- 65ce979ed604 -->
### 模板 6：平行任務排程
<!--CODE-->

<!--CODE-->

<!-- e632453a34b4 -->
### 模板 7：找樹的重心（無向樹的葉節點剝除法）⭐⭐⭐⭐

**LC 模式** — 題目敘述裡有三個訊號，LC 310 三個都有：

| 訊號 | 它告訴你的事 |
|---|---|
| 無向、`n` 個節點加 **`n - 1` 條邊**、連通 | 它是一棵**樹** — 沒有環，所以不用處理環，也不需要 `visited` |
| 「你可以**選任何節點當根**」、「最小高度」 | 要你回答的是一組**節點**，不是一個順序 |
| 答案是一個 list，而且暗示「最多 2 個」 | 剝除之後**存活下來的節點就是答案** |

> **LC 310 = 找 tree center = 不斷 remove leaves。**

**核心想法** — 讓高度最小的根，就是**直徑的中點**。與其把每個節點都試一遍當根（從每個節點各做一次
BFS，也就是 O(n^2)，解題檔裡的 `V1''`），不如由外往內剝：葉節點永遠是*最差*的根，所以一次就把整層
葉節點丟掉，然後重複。活下來的節點與直徑兩端等距 — 那就是中心。

<!--CODE-->

**為什麼只會剩 1 或 2 個，不會是 3 個** — 存活的節點就是直徑路徑的中點，而一條路徑的中點只會有
一個或兩個：

<!--CODE-->

這也是為什麼迴圈條件是 `while remaining > 2` 而**不是** `while queue` — 佇列不會自己變空，是你去
停它的。

**和 Kahn 是同一套機制，只差四點**（這四點就是整個模板）：

| | 模板 1（Kahn，DAG） | 模板 7（葉節點剝除，樹） |
|---|---|---|
| 加邊 | 單向，`in_degree[v] += 1` | **雙向**，`degree[u] += 1` *且* `degree[v] += 1` |
| 種子 | `in_degree == 0`（源點） | `degree == 1`（葉節點） |
| 停止 | 佇列變空 → 完整的順序 | `remaining <= 2` → 佇列**本身**就是答案 |
| 輸出 | pop 出來的順序 | 從沒被 pop 到的節點 |

<!--CODE-->

<!--CODE-->

**這個模板專屬的坑**

1. **`n == 1` 會讓佇列是空的**，不是滿的：沒有邊，就沒有任何節點的度數會等於 1，於是 `while` 一次
   都不會跑，你回傳 `[]`。要特判。`if n <= 2: return list(range(n))` 一次蓋掉兩種瑣碎情況，也比只
   寫 `n == 1` 好讀（解題檔裡的 `V0-2`）。
2. **不需要 `visited`。** `degree` 只會遞減，所以一個節點最多只會經過 `1` 這個值一次，也就最多只會
   被推進佇列一次 — 去重是樹的結構幫你做掉的。
3. **要另外用 `remaining` 計數**，在把整層抽乾之前先扣掉這一層的大小。事後再讀 `len(queue)` 得到的
   不是還活著的節點數。
4. **葉節點被剝掉時 `degree[leaf]` 不會被歸零。** 這無害：被剝掉的葉節點之後被同層的兄弟節點遞減
   時是從 1 變 0，而 0 永遠不會再觸發推入佇列的判斷。

**相似題目**：見下方的[找樹的重心](#tree-centroid-finding)。

<!-- 8b366819baf6 -->
### 模板 8：併查集（連通分量）
<!--CODE-->

<!--CODE-->

<!-- 1d028b546715 -->
### 模板 9：在**隱式** DAG 上的拓撲排序 + DP ⭐⭐⭐⭐⭐

**核心想法**：圖從來不是以邊列表給你的——它是由一條比較規則*隱含*出來的
（`grid[a] < grid[b]` ⇒ 存在邊 `a → b`）。邊走邊算入度／出度，再用 Kahn 的做法逐層剝除。
**BFS 的層數**就是 DAG 中的最長路徑（因為邊的規則是嚴格遞增的，所以不可能有環）。

**何時使用**：格子或矩陣上的「最長遞增路徑／鏈」，或任何需要依相依順序處理節點、又不想承擔遞迴深度風險的 DAG 上 DP。

**與模板 1 的差異**：這裡從**匯點**（出度 0 = 區域極大值）而非源點開始剝，這樣每一層剛好就是 DP 的一步。從源點剝也可以——把比較方向反過來即可。

<!--CODE-->

<!--CODE-->

**注意**：經典的替代做法是 DFS + 記憶化（`dp[i][j] = 1 + max(dp[neighbour])`），同樣是
O(m·n)。當格子大到遞迴可能爆堆疊，或面試官明確要求用拓撲排序時，優先選 Kahn 版本。

**同樣的形狀，但圖是顯式的**：LC 1857 (Largest Color Value in a Directed Graph) — 在 Kahn 的流程中帶著一個
`count[node][26]` 的 DP 陣列，而不是單一的層數計數器；若佇列在所有節點都被取出之前就空了，代表有環 → 回傳 `-1`。

---

<!-- 702c7a3c7877 -->
### 模板 10：入度特徵（答案直接從度數讀出來）⭐⭐⭐⭐

**核心想法**：有些「圖」題根本不需要遍歷——答案完全由**入度／出度的計數**決定。認出這一點能把一題 Medium 變成三行程式。

**關鍵洞見（LC 1557）**：在 **DAG** 上，入度為 0 的節點只能從它自己開始才會被走到，而其餘每個節點都能從某個入度 0 的節點到達。所以入度 0 的節點集合既必要又充分——因此就是唯一的最小答案。

<!--CODE-->

<!--CODE-->

<!-- ffe96381c271 -->
#### 變化 A — 度數*特徵*比對：LC 997 Find the Town Judge

**LC 模式** — 關鍵線索是：要找的那個節點完全是*用「有幾條邊碰到它」來描述*的，從來不是用它能走到誰：

| 題目敘述裡的訊號 | 它的意思 |
|---|---|
| 「法官**不信任任何人**」 | `out_degree == 0` |
| 「**除了**法官以外，每個人都信任法官」 | `in_degree == n - 1` |
| 「**恰好只有一個**這樣的人」 | 掃描找這個指紋；沒有節點符合就回 `-1` |

這裡沒有任何一句在問*誰能走到誰*，所以**不用鄰接表、不用佇列、不用 DFS** — 兩個計數陣列、兩趟迴圈
就結束。它和上面的 LC 1557（`in_degree == 0`）是同一家族，只是換了指紋。

> **LC 997 = 找一個 node：`in_degree == n - 1` 且 `out_degree == 0`。**

**核心想法，第 1 步 — 老實地把兩種度數都數出來。** 這是應該先寫下來的版本：它和題目敘述的兩句話
一對一對應，緊張的時候也不會推錯。

<!--CODE-->

**核心想法，第 2 步 — 把兩個陣列摺成一個。** 值得寫第二個版本，因為它把空間砍半，而且多數面試官
預期看到的就是這一版：追蹤 `score = in_degree - out_degree`，然後掃描找 `n - 1`。

**為什麼可以這樣摺**（把這段講出來 — 這是唯一不顯然的一步）：`in_degree <= n - 1` 恆成立，因為
`trust` 裡的 pair 不重複而且 `a != b`，最多只有 `n - 1` 個不同的人能信任你。有了這個上限，
`in - out == n - 1` 就**強迫** `in == n - 1` 且 `out == 0` — 不可能靠很大的入度再被負的出度抵掉來
湊出 `n - 1`。如果哪天題目允許**重複**的 trust pair，`in_degree` 就可能超過 `n - 1`，這個摺疊會報出
一個假法官；而第 1 步的版本仍然正確。

<!--CODE-->

<!--CODE-->

**坑**

1. **編號是 1-indexed。** 陣列開 `n + 1`、迴圈跑 `1..n`；開 `n` 的陣列在第 `n` 個人身上就會越界。
2. **`n == 1` 且 `trust == []`** 必須回傳 `1`。上面兩個版本都免費拿到這個結果 — `n - 1 == 0`，而每個
   score 都是 `0` — 這也是它們比集合解法好的一個理由，後者需要額外寫 `if n == 1: return 1`。
3. **不要只驗到「有人信任他、而他不信任任何人」就收手。** 那只是*候選人*。計數必須恰好是 `n - 1`；
   以 `n = 3, trust = [[1,3]]` 為例，節點 3 通得過這個弱化的檢查，但它不是法官。集合差集的版本
   （`trusted - trusting`，解題檔裡的 `V2`）正是因為這樣才需要再補一趟 `n - 1` 的驗證 — 程式更長，
   複雜度一樣。
4. **`-1` 是一個真正的答案**，不是錯誤分支：有環時（`[[1,3],[2,3],[3,1]]`）沒有人的 `out_degree == 0`。

**相似題目 — 答案就是一個度數指紋**

| 題目 | 難度 | 指紋是什麼、又換了什麼 |
|---------|------------|-----------------------------------|
| 997 Find the Town Judge | Easy | `in == n - 1` 且 `out == 0` — 模板本身 |
| 277 Find the Celebrity | Medium | *同一個*指紋，但邊只能透過 `knows(a, b)` API 取得，所以你數不了度數 — 先用一趟 O(n) 淘汰候選人，再驗證存活者的那一列與那一行 |
| 2924 Find Champion II | Medium | `in == 0` 而且必須**唯一**：恰好一個入度 0 的節點才回它，否則 `-1` |
| 2923 Find Champion I | Easy | 和 2924 相同，但給的是鄰接**矩陣** — 找沒有任何入邊 `1` 的那一列 |
| 1557 Minimum Number of Vertices to Reach All Nodes | Medium | `in == 0`，但要回傳**全部**（上面的模板 10） |
| 1361 Validate Binary Tree Nodes | Medium | 所有節點 `in <= 1` + 恰好一個 `in == 0` + 一趟可達性檢查（下面的變化 B） |
| 2374 Node With Highest Edge Score | Medium | 累加的是指進來的**編號總和**，不是次數 — 同樣的一趟掃描，只是換了累加器 |
| 1615 Maximal Network Rank | Medium | 無向圖，只要一個 `degree` 陣列；一對節點相鄰時，rank 是 `deg(a) + deg(b) - 1` |

<!-- 935c83c03ae0 -->
#### 變化 B — 度數 + 一次可達性檢查：LC 1361 Validate Binary Tree Nodes

**變化點**：光靠度數還不夠。一棵合法的二元樹需要**三個**條件——
(1) 每個節點入度 ≤ 1、(2) 恰好一個節點入度為 0（根）、
(3) 全部 `n` 個節點都能從該根到達。只有條件 1+2 而沒有 3 的話，仍然可能出現一棵看似合法的樹外加一個**分離的環**，那正是這題的陷阱情境。

<!--CODE-->

<!--CODE-->

<!-- 4fb16e93a633 -->
## 題目分類

| 題目 | 難度 | 分類 | 關鍵技巧 |
|---------|------------|----------|---------------|
| [207. Course Schedule](https://leetcode.com/problems/course-schedule/) | Medium | 課程排程 | 偵測環 |
| [210. Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) | Medium | 課程排程 | BFS/DFS |
| [269. Alien Dictionary](https://leetcode.com/problems/alien-dictionary/) | Hard | 字典序 | 字元順序 |
| [310. Minimum Height Trees](https://leetcode.com/problems/minimum-height-trees/) | Medium | 樹的重心 | 葉節點剝除 |
| [444. Sequence Reconstruction](https://leetcode.com/problems/sequence-reconstruction/) | Medium | 建置順序 | 唯一順序 |
| [630. Course Schedule III](https://leetcode.com/problems/course-schedule-iii/) | Hard | 課程排程 | 貪婪 + 堆積(heap) |
| [802. Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/) | Medium | 偵測環 | 反向圖 |
| [851. Loud and Rich](https://leetcode.com/problems/loud-and-rich/) | Medium | 圖的分層 | DFS + 記憶化 |
| [953. Verifying an Alien Dictionary](https://leetcode.com/problems/verifying-an-alien-dictionary/) | Easy | 字典序 | 驗證順序 |
| [1059. All Paths from Source Lead to Destination](https://leetcode.com/problems/all-paths-from-source-lead-to-destination/) | Medium | 偵測環 | DFS |
| [1136. Parallel Courses](https://leetcode.com/problems/parallel-courses/) | Medium | 任務排程 | 分層 BFS |
| [1203. Sort Items by Groups Respecting Dependencies](https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/) | Hard | 建置順序 | 雙層拓撲排序 |
| [1462. Course Schedule IV](https://leetcode.com/problems/course-schedule-iv/) | Medium | 課程排程 | 遞移閉包 |
| [1857. Largest Color Value in a Directed Graph](https://leetcode.com/problems/largest-color-value-in-a-directed-graph/) | Hard | 圖的分層 | DAG 上的 DP |
| [2050. Parallel Courses III](https://leetcode.com/problems/parallel-courses-iii/) | Hard | 任務排程 | 時間計算 |
| [2115. Find All Possible Recipes from Given Supplies](https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/) | Medium | 建置順序 | 改造版 BFS |
| [547. Number of Provinces](https://leetcode.com/problems/number-of-provinces/) | Medium | 連通分量 | 併查集／DFS |
| [2192. All Ancestors of a Node in a Directed Acyclic Graph](https://leetcode.com/problems/all-ancestors-of-a-node-in-a-directed-acyclic-graph/) | Medium | 圖的分層 | DFS/BFS |
| [329. Longest Increasing Path in a Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/) | Hard | 圖的分層 | 隱式 DAG + Kahn（模板 9） |
| [1557. Minimum Number of Vertices to Reach All Nodes](https://leetcode.com/problems/minimum-number-of-vertices-to-reach-all-nodes/) | Medium | 圖的分層 | 入度 0 的集合（模板 10） |
| [997. Find the Town Judge](https://leetcode.com/problems/find-the-town-judge/) | Easy | 度數計算 | 入／出度特徵（模板 10-A） |
| [1361. Validate Binary Tree Nodes](https://leetcode.com/problems/validate-binary-tree-nodes/) | Medium | 偵測環 | 入度 + 可達性（模板 10-B） |
| [1245. Tree Diameter](https://leetcode.com/problems/tree-diameter/) | Medium | 樹的重心 | 葉節點剝除的層數（模板 7） |
| [2603. Collect Coins in a Tree](https://leetcode.com/problems/collect-coins-in-a-tree/) | Hard | 樹的重心 | 兩階段葉節點剝除（模板 7） |
| [277. Find the Celebrity](https://leetcode.com/problems/find-the-celebrity/) | Medium | 度數計算 | 和 997 同一個指紋，但邊只能透過 API 問（模板 10-A） |
| [2924. Find Champion II](https://leetcode.com/problems/find-champion-ii/) | Medium | 度數計算 | 唯一那個入度 0 的節點（模板 10-A） |

<!-- 55af032267a6 -->
### 依分類整理的題型

<!-- c0566898f306 -->
#### 課程排程類
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 基本的環偵測 | 207 | 檢查是否為 DAG |
| 找出合法順序 | 210 | 回傳拓撲順序 |
| 帶時間限制 | 630 | 貪婪 + 優先佇列 |
| 查詢先修關係 | 1462 | Floyd-Warshall／DFS |

<!-- 3f07717c636a -->
#### 任務排程類
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 最短時間 | 1136、2050 | 逐層 BFS |
| 平行執行 | 1136 | 計算層數 |
| 帶執行時長 | 2050 | 在完成時間上做 DP |

<!-- 77fa5cce742e -->
#### 字典序排序
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 字元順序 | 269 | 由相鄰比較建圖 |
| 驗證順序 | 953 | 檢查一致性 |
| 自訂比較器 | 269 | 從範例中萃取規則 |

<!-- 508c333768a7 -->
#### 建置順序與相依性
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 唯一重建 | 444 | 佇列大小恆為 1 |
| 食譜相依 | 2115 | 處理初始原料 |
| 群組相依 | 1203 | 兩層拓撲排序 |

<!-- 62c6521f9a5a -->
#### 圖的分層
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 找出所有祖先 | 2192 | 遍歷反向圖 |
| 可達的最富有者 | 851 | DFS + 記憶化 |
| 路徑值最大化 | 1857 | DAG 上的 DP |
| 隱式 DAG 上的最長路徑 | 329 | 邊由 `a < b` 隱含；層數 = 路徑長度 |

<!-- ea0da5eaf3d9 -->
#### 度數計算（不需遍歷）
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 最小起始集合 | 1557 | 在 DAG 上答案就是入度 0 的節點 |
| 節點指紋 | 997、277 | 法官／名人 = 入度 `n-1` 且出度 `0`；可摺成 `score = in - out == n - 1` |
| 唯一源點 | 2924、2923 | 冠軍 = **唯一**那個入度 0 的節點，否則 `-1` |
| 帶權度數 | 2374、1615 | 累加編號或單純的度數，而不是數邊 |
| 驗證樹的形狀 | 1361 | 入度 ≤ 1 + 唯一的根 + 根能到達全部 n 個節點 |

<!-- c3c19e60c737 -->
#### 環的偵測
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 安全狀態 | 802 | 反向圖 + 出度 |
| 所有路徑皆安全 | 1059 | DFS 並追蹤路徑 |
| 偵測任意環 | 207 | 三色 DFS |

<!-- 5121912a681b -->
#### 連通分量（併查集／DFS）
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 計算分量數 | 547、323 | 併查集或 DFS 遍歷 |
| 找出多餘的邊 | 684 | 併查集偵測環 |
| 島嶼數量 | 200 | 在格子上做 DFS/BFS |

<!-- 234515dabd8e -->
#### 找樹的重心

**同一套機制** — 建 degree 陣列、用葉節點當佇列種子、由外往內剝（模板 7）：

| 題目 | 難度 | 和 LC 310 的差別 |
|---------|------------|------------------------|
| 310 Minimum Height Trees | Medium | 模板本身 — 剝到 `remaining <= 2`，回傳存活的節點 |
| 1245 Tree Diameter | Medium | 讀的是**剝了幾層**而不是存活者：剩 1 個是 `2 * layers`，剩 2 個是 `2 * layers + 1`。（課本解法是兩次 BFS；這裡是重用同一套剝除。） |
| 2603 Collect Coins in a Tree | Hard | 剝**兩輪**：先反覆丟掉身上沒有硬幣的葉節點，再丟掉剛好 2 層葉節點；答案 = `2 * remaining_edges` |
| 802 Find Eventual Safe States | Medium | 有向版的表親 — 以 `out_degree == 0` 當種子剝反向圖，存活下來的是*不安全*的節點 |

**同一棵樹，換工具** — 當答案不是樹的正中間時，改用這些：

| 題目 | 難度 | 為什麼葉節點剝除不適用 |
|---------|------------|----------------------------------|
| 543 Diameter of Binary Tree | Easy | 樹已經有根了 — 一趟回傳高度的 DFS 就好，不必算度數 |
| 1522 Diameter of N-Ary Tree | Medium | 同樣的 DFS，取最大的兩個子節點高度相加 |
| 834 Sum of Distances in Tree | Hard | 要的是**每個**節點的答案 → 換根 DP，不是剝一次就好 |
| 863 All Nodes Distance K in Binary Tree | Medium | 先補上父節點連結，再從某個節點**往外** BFS，方向剛好相反 |
| 1443 Minimum Time to Collect All Apples in a Tree | Medium | 剪的依據是*內容*（子樹裡沒有蘋果），不是度數 |

<!-- 8f6ae47f1610 -->
## 決策框架

<!--CODE-->

> 上面的 `UnionFind` 類別就是模板；LC 547 用三種方式套用它——
> 併查集、DFS 與 BFS——見
> [topology_sorting_examples.md](./topology_sorting_examples.md#8-number-of-provinces--lc-547)。

<!-- d6dcff876e4a -->
## 完整解題範例

八道題目，依它們所編碼的相依關係形狀分組，收錄在
**[topology_sorting_examples.md](./topology_sorting_examples.md)**：

| 分組 | 題目 | 練到的模板 |
|---|---|---|
| [課程排程與排序](./topology_sorting_examples.md#course-scheduling--ordering) | LC 210、207、269、444 | Kahn（T1）、三色 DFS（T2）、字典序（T4） |
| [分層與平行排程](./topology_sorting_examples.md#layering--parallel-scheduling) | LC 1136 | 平行任務排程（T6） |
| [環的偵測與安全狀態](./topology_sorting_examples.md#cycle-detection--safe-states) | LC 802 | 三色 DFS（T2） |
| [無向圖 — 連通分量與重心](./topology_sorting_examples.md#undirected-graphs--components--centroids) | LC 310、547 | 葉節點剝除（T7）、併查集（T8） |

<!-- 5d1b8e4b8f13 -->
## 總結與面試提示

<!-- 97027005a880 -->
### 常見陷阱
1. **忘了偵測環**：永遠要檢查結果的大小是否等於節點數
2. **邊的方向搞反**：記住邊是從先修指向相依者
3. **沒處理不連通的部分**：所有尚未拜訪的節點都要處理
4. **入度初始化錯誤**：確保所有節點都被納入
5. **漏掉邊界情況**：空圖、單一節點、自環
6. **混淆度數與入度**：無向樹用總度數，DAG 用入度
7. **停止條件錯誤**：找樹的重心時要在剩下 ≤2 個節點時停（不是等佇列空）

<!-- 5abf90bc7aef -->
### 關鍵洞見
1. **BFS vs DFS**：要找出一個順序時 BFS 比較簡單，要列出所有順序則用 DFS
2. **追蹤入度**：入度為 0 的節點才可以被處理
3. **三色 DFS**：白（未拜訪）、灰（拜訪中）、黑（已完成）
4. **反向圖**：對安全狀態這類題目很有用
5. **逐層處理**：用於平行執行與求最短時間
6. **找樹的重心**：無向樹要用度數（不是入度），逐層剝除葉節點直到剩下 1-2 個節點
7. **無向 vs 有向**：無向樹需要雙向邊並追蹤度數，DAG 則使用入度

<!-- de332cae3b5d -->
### 面試作法
1. **釐清需求**：
   - 圖是否保證為 DAG？
   - 需要所有順序還是只要一個？
   - 對順序有沒有特別偏好？

2. **選擇演算法**：
   - 預設用 BFS（Kahn）因為最單純
   - 遞迴類問題用 DFS
   - 需要字典序時用優先佇列

3. **處理邊界情況**：
   - 空圖
   - 單一節點
   - 不連通的部分
   - 圖中有環

4. **必要時再最佳化**：
   - 偵測到環就提早終止
   - 用原地修改節省空間
   - 用更好的資料結構縮短時間

<!-- c58e8afadb40 -->
### 時間／空間複雜度總表
| 操作 | 時間 | 空間 | 備註 |
|-----------|------|-------|-------|
| 建圖 | O(E) | O(V + E) | 鄰接表 |
| 計算入度 | O(E) | O(V) | 陣列或雜湊表 |
| BFS/DFS 遍歷 | O(V + E) | O(V) | 每個節點／邊只走一次 |
| 偵測環 | O(V + E) | O(V) | 三色標記 |
| 列舉所有順序 | O(V! × E) | O(V) | 所有排列，指數級 |

<!-- 13acfef99cb6 -->
### 相關概念
- **強連通分量**：Tarjan／Kosaraju 演算法
- **DAG 上的最短路徑**：拓撲排序 + 鬆弛
- **要徑法（Critical Path Method）**：專案排程
- **相依性解析**：套件管理器、建置系統
- **資料流分析**：編譯器最佳化
