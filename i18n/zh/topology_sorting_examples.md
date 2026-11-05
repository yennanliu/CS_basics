<!-- 3475a3262ab9 -->
# 拓撲排序 — 實戰題解

> **範圍** — [topology_sorting.md](./topology_sorting.md) 背後的題目集：每題一張 problem card，依照它歸約到的模板分組，本身不放任何模板或理論。
> **另見**：[topology_sorting.md](./topology_sorting.md) — 這些卡片套用的十個模板、題型分類與決策流程；[graph.md](./graph.md) — LC 207 和 LC 802 在那裡是 Kahn 與三態 DFS 兩張卡的錨定題；[bfs.md](./bfs.md) 和 [dfs.md](./dfs.md) — Kahn 演算法與三色模板所依靠的兩種走訪。

<!-- ce782da61195 -->
## LeetCode 題目清單

- [Topological Sort](https://leetcode.com/problem-list/topological-sort/)
- [Graph](https://leetcode.com/problem-list/graph/)

<!-- 1614991528b3 -->
## 總覽

經典的依賴順序題，每題一張 problem card，放在它歸約到的
[topology_sorting.md](./topology_sorting.md) 模板底下。排序本身幾乎從來不是難處，
難處是決定節點和邊**是什麼**：課程、字母、序列裡的數字、樹的葉子。[topology_sorting.md](./topology_sorting.md)
放模板；這份放題目。

<!-- f1269ad9c3fc -->
## 索引

| LC | 題目 | 歸約到 | 難度 |
|---|---|---|---|
| 210 | [Course Schedule II](#course-schedule-ii--lc-210-) | [Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — Kahn 演算法，回傳順序 | Medium |
| 444 | [Sequence Reconstruction](#sequence-reconstruction--lc-444) | [Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — Kahn 演算法，佇列永遠不會同時有兩個 | Medium |
| 269 | [Alien Dictionary](#alien-dictionary--lc-269-) | [Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — 對相鄰單字讀出的邊跑 Kahn | Hard |
| 1136 | [Parallel Courses](#parallel-courses--lc-1136) | [Template 6](./topology_sorting.md#template-6-parallel-task-scheduling) — Kahn 的一層 = 一個學期 | Medium |
| 310 | [Minimum Height Trees](#minimum-height-trees--lc-310-) | [Template 7](./topology_sorting.md#template-7-tree-centroid-finding-leaf-trimming-for-undirected-trees-) — 剝葉子 | Medium |

**在別份解過** — 一題一個家，所以這些只是指標：

| LC | 題目 | 解在 |
|---|---|---|
| 207 | Course Schedule | [graph.md § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-) |
| 802 | Find Eventual Safe States | [graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-) |
| 547 | Number of Provinces | [graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-)（走訪）；[union_find_examples.md](./union_find_examples.md)（union-find） |
| 329 | Longest Increasing Path in a Matrix | [topology_sorting.md § Template 9](./topology_sorting.md#template-9-topological-sort--dp-on-an-implicit-dag-) |

<!-- c1ebaf3f7e3f -->
## Kahn 演算法範例

<!-- 4312441843a7 -->
### Course Schedule II — LC 210 ⭐⭐⭐⭐⭐

> **關鍵** — 一門課只有在它所有先修課都已經彈出之後才會彈出，所以彈出的順序**就是**合法的課表。
> 環會讓其中的課入度永遠大於 0，所以順序會變短：`len(order) < numCourses` 代表排不出課表。

**歸約到**：[Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — 回傳順序而不是是／否的 Kahn 演算法（只問是／否的是 LC 207，見 [graph.md § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-)）。

<!--CODE-->

<!--CODE-->

**陷阱**
- `[a, b]` 的意思是 *b 在 a 之前*：邊是 `b → a`。方向弄反會回傳反過來的順序，它照樣通過環檢查，
  卻會讓每個順序測試失敗。
- 沒有先修、也沒人依賴它的課仍然是節點：佇列要從 `range(numCourses)` 播種，不要從 `graph` 的 key。
- DFS 的替代做法（沿 `course → prerequisite` 邊做後序）也行。它的三態環檢查見
  [graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-)。

<!-- c367078d0ee0 -->
### Sequence Reconstruction — LC 444

> **關鍵** — `nums` 是**唯一**最短超序列，恰好等於拓撲順序唯一；而順序唯一，恰好等於 Kahn 的佇列
> **從來不會同時有超過一個節點**。兩個節點同時就緒，就代表有兩種合法順序。

**歸約到**：[Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — Kahn 演算法，外加檢查佇列是否唯一，並在每次彈出時和 `nums` 比對。

<!--CODE-->

**陷阱**
- 同一條邊只算一次。否則 `[[1,2],[1,2]]` 會把 `in_degree[2]` 加到 2，2 永遠不會被釋放。
- 沒有出現在任何序列裡的值，一開始入度就是 0，所以它會和真正的第一個元素同時在佇列裡，檢查就失敗。
  這正是對的答案：沒有任何資訊固定它的位置。

<!-- bcfdbebfbf4a -->
### Alien Dictionary — LC 269 ⭐⭐⭐⭐

> **關鍵** — 一對相鄰單字唯一提供的順序資訊，在它們**第一個不同的字母**。後面的字母什麼都沒說。
> 每對相鄰單字讀出一條邊，再對字母做拓撲排序。唯一沒有任何順序能解釋的輸入，是某個單字後面接著它自己的
> 真前綴（`"abc"` 在 `"ab"` 之前）。

**歸約到**：[Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — 先從單字列表建圖，再跑 Kahn 演算法。

<!--CODE-->

**陷阱**
- 在第一個不同的字母就停（`break`）。比較每個位置會加進假的邊。
- 前綴的情況只有在迴圈**沒有**找到差異時才偵測得到；這就是 `for … else` 的用途。`["abc", "ab"]` 必須回傳 `""`。
- 沒出現在任何邊裡的字母仍然屬於答案：讀邊之前，先為每個單字的每個字母建立節點。

<!-- eeb7fece3b7a -->
## 平行任務排程範例

<!-- 4455dcc16bcc -->
### Parallel Courses — LC 1136

> **關鍵** — 每學期把所有能修的課都修掉是最佳的，所以 Kahn BFS 的一**層**就是一個學期，答案就是層數，
> 也就是 DAG 裡最長的鏈。環會讓某些課修不到：回傳 −1。

**歸約到**：[Template 6](./topology_sorting.md#template-6-parallel-task-scheduling) — 逐層的 Kahn 演算法，每個任務都花一單位時間。

<!--CODE-->

**陷阱**
- 進入每層迴圈前先凍結 `len(queue)`。學期中解鎖的課會加到同一個 deque，必須等下一學期。
- 若每學期最多只能修 `k` 門（LC 1494），逐層 greedy 就不再最佳；那題是 bitmask DP。

<!-- 71c6c477786f -->
## 剝葉子範例

<!-- f487980b1937 -->
### Minimum Height Trees — LC 310 ⭐⭐⭐⭐

> **關鍵** — 高度最小的根，是樹上最長路徑的**中心**，所以一層一層剝掉葉子，直到剩下最多兩個節點。
> 一條路徑的中心有一個或兩個，所以答案永遠是一個或兩個節點。每個節點只被剝一次，所以是 O(n)；
> 從每個節點各跑一次 BFS 則是 O(n²)。

**歸約到**：[Template 7](./topology_sorting.md#template-7-tree-centroid-finding-leaf-trimming-for-undirected-trees-) — 在無向樹上做 Kahn 式的剝除，以「度數為 1」作為就緒條件。

<!--CODE-->

<!--CODE-->

**陷阱**
- `n == 1` 沒有邊，也沒有度數為 1 的節點；少了這個防護，答案會是 `[]`。
- 停在 `remaining > 2`，不是「佇列空了」。剝過兩個節點就把中心刪掉了。
- 每輪剝掉一整層（凍結大小）。一次剝一片葉子，可能讓樹的一側比另一側剝得快，最後停在偏離中心的地方。

**相似題** — LC 2603（先剝掉沒有硬幣的葉子）· LC 1245（樹的直徑：它的中點就是中心）
