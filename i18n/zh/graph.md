<!-- 24d3d1e745e8 -->
# 圖論演算法

> **範圍** — 圖的表示法、走訪、連通性、環偵測，以及一般圖題的完整目錄。
> **另見** — *從本檔拆出去的深入主題*：[graph_advanced.md](./graph_advanced.md) — Tarjan（SCC、橋、關節點）、尤拉迴路、最大流／最小割、二分圖匹配與 k-著色；[graph_examples.md](./graph_examples.md) — 解題存檔（LC 133 / 200 / 207 / 323 / 329 / 399 / 695 / 742 / 802 / 815 / 886 / 947 / 1319）。
> *鄰近文件*：[bfs.md](./bfs.md) — 廣度優先走訪；[dfs.md](./dfs.md) — 深度優先走訪；[topology_sorting.md](./topology_sorting.md) — DAG 排序；[union_find.md](./union_find.md) — 無向圖連通性；[shortest_path_comparison.md](./shortest_path_comparison.md) — **怎麼挑**帶權最短路徑演算法；[Dijkstra.md](./Dijkstra.md) — 非負權重；[Bellman-Ford.md](./Bellman-Ford.md) — 負權重／限制跳數；[Floyd-Warshall.md](./Floyd-Warshall.md) — 全點對。

<!-- 0afd62786910 -->
## LeetCode 題目清單

- [Graph Theory](https://leetcode.com/problem-list/graph/)

<!-- 9a30eed19af6 -->
## 總覽
**圖論演算法**是用來處理圖這種資料結構的技巧，圖由頂點（節點）和邊（節點之間的連結）組成。

<!-- fed7a60f9df6 -->
### 關鍵性質
- **複雜度**：見總結區的[複雜度速查](#complexity-quick-reference)表
- **核心想法**：把實體之間的關係與連結模型化
- **什麼時候用**：網路問題、相依關係、路徑、連通性
- **關鍵演算法**：BFS、DFS、Dijkstra、併查集、拓撲排序

<!-- 596eae3672d0 -->
### 核心特徵
- **有向 vs 無向**：邊是單向還是雙向
- **帶權 vs 不帶權**：邊有沒有成本
- **有環 vs 無環**：圖裡有沒有環
- **連通 vs 不連通**：是不是所有節點都互相可達

<p align="center"><img src="../pic/graph_processing_problem.png"></p>

<!-- 168c44d0c962 -->
## Recognition

先在題目敘述裡找第一欄的字眼，再拿限制條件對照[限制條件尺規](./complexity_cheatsheet.md#6-interview-decision-guide)。
有好幾列會把你導出這份文件：題目帶權、要排順序或屬於進階主題時，這就是正確答案。

| 敘述裡的訊號 | 限制提示 | 該用 | 不是這個的情況 |
|---|---|---|---|
| 「有幾個省份／群組／連通分量」 | V + E ≤ 10⁵ | [Template 1](#template-1-connected-components-by-traversal--lc-547-) — 每個連通分量一次走訪 | 輸入是網格 → [dfs.md § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) |
| 「能不能進到每個房間」、「a 到 b 有沒有路徑」 | V + E ≤ 10⁵ | [Template 2](#template-2-dfs-reachability--lc-841-) — DFS 可達性 | 「最少步數」 → [bfs.md](./bfs.md) |
| 「回傳從 0 到 n − 1 的每一條路徑」 | n ≤ 15 | [Template 2 的 LC 797 變形](#variation-enumerate-all-paths-backtracking-no-visited-set--lc-797) — 回溯，不共用 `visited` | 圖裡有環 → 另外維護一份「在路徑上」的集合 |
| 「多餘的連線」、「邊一條一條加進來」、「因為共用 email／同一列而相連」 | n ≤ 10⁵ | [Template 3](#template-3-union-find-with-path-compression--lc-684-) — 併查集（LC 721、947：把共用的屬性當成節點） | 要的是路徑或它的長度 → BFS / DFS |
| 「先修條件」、「任務的順序」、「外星字典」 | 有向，V + E ≤ 10⁵ | [Template 4](#template-4-topological-sort-kahns-bfs--lc-207-) — Kahn 的 BFS | 無向圖 → 根本沒有順序可找 |
| 「有向圖裡的環」、「死結」、「最終安全的節點」 | 有向，V ≤ 10⁴ | [Template 5](#template-5-three-state-dfs-cycle-detection--lc-802-) — 三態 DFS | 無向圖 → 併查集 |
| 「分成兩組」、「是不是二分圖」、「彼此討厭」 | n ≤ 2,000 | [Template 6](#template-6-bipartite-check-2-coloring--lc-785-) — 2-著色 | 三組以上 → 回溯著色 |
| 帶權的「最短路徑」、「網路延遲」、「最便宜的航班」 | E ≤ 10⁵ | [Dijkstra.md](./Dijkstra.md)、[Bellman-Ford.md](./Bellman-Ford.md)、[Floyd-Warshall.md](./Floyd-Warshall.md) | 每條邊成本都是 1 → BFS |
| 「比值」、「匯率」、「計算 a / b」 | V ≤ 40 | [dfs.md § Template 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399) — 帶權 DFS | 同一張圖上有大量查詢 → 帶權併查集 |
| 「最長路徑，每一步都嚴格遞增」 | 網格 ≤ 200 × 200 | 隱式 DAG，DFS + memo（[graph_examples.md](./graph_examples.md#2-10-longest-increasing-path-in-a-matrix--lc-329)） | 可以往下走 → 不是 DAG，不能 memo |
| 「用最小總成本把所有節點連起來」 | V ≤ 1,000 | [minimum_spanning_tree.md](./minimum_spanning_tree.md) | 要的是兩點間的路徑而不是一棵樹 → 最短路徑 |
| 「拿掉這條邊／這個點會讓圖不連通」、「關鍵連線」 | V ≤ 10⁵ | Tarjan low-link（[graph_advanced.md](./graph_advanced.md#template-1-tarjans-low-link-dfs--scc-bridges-articulation-points--lc-1192-)） | 只問拿掉前後的連通性 → 併查集 |
| 「每條邊／每張機票恰好用一次」 | E ≤ 300 | 尤拉路徑，Hierholzer（[graph_advanced.md](./graph_advanced.md#template-2-euler-path--circuit-hierholzer--lc-753-)） | 每個**節點**走一次 → 哈密頓路徑，bitmask DP |
| 「最大流」、「二分圖匹配」 | V 很小 | Ford-Fulkerson / Edmonds-Karp（[graph_advanced.md](./graph_advanced.md)） | 只問能不能分成兩組 → Template 6 |

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- f37dc8e0091b -->
### 哪個演算法歸哪份文件管 ⭐⭐⭐⭐⭐

這份文件負責**表示法、走訪、連通性與環偵測**。每一個帶權或排序類的演算法都有專屬文件 —
要看實作就去那裡，別在這裡重推一次。

| 需求 | 這裡的模板 | 負責的文件 |
|---|---|---|
| 從 LC 的輸入格式建圖 | 下方的「圖的表示法」 | 本文件 |
| 數連通分量、測可達性 | Template 1 / Template 2 | 本文件；走訪本身在 [bfs.md](./bfs.md)、[dfs.md](./dfs.md) |
| 最短路徑，**不帶權** | — | [bfs.md](./bfs.md) |
| 最短路徑，**帶權** | — | 見下方表格 |
| 替有相依關係的節點排序 | Template 4（Kahn） | [topology_sorting.md](./topology_sorting.md) |
| 動態連通性、合併集合 | Template 3（DSU） | [union_find.md](./union_find.md) |
| 偵測環 | Template 5 | 本文件 |
| 分成兩組／衝突著色 | Template 6 | 本文件 |
| SCC、橋、關節點、尤拉迴路、最大流、k-著色 | — | [graph_advanced.md](./graph_advanced.md) |
| 每題一份完整解答 | — | [graph_examples.md](./graph_examples.md) |

<!-- 8b296c288737 -->
#### 帶權最短路徑 — 看專屬文件

這些各自有完整的文件；在這裡再寫一次實作，正是害 `graph.md` 和它們脫節的原因。

| 需求 | 文件 | 複雜度 | 代表題 |
|---|---|---|---|
| 單源，**非負**權重 | [Dijkstra.md](./Dijkstra.md) | O((V+E) log V) | LC 743 |
| 單源，**負**權重／限制跳數／偵測負環 | [Bellman-Ford.md](./Bellman-Ford.md) | O(V·E) | LC 787 |
| **全點對**，稠密圖 | [Floyd-Warshall.md](./Floyd-Warshall.md) | O(V³) | LC 1334 |
| 不帶權，或 0-1 權重 | [bfs.md](./bfs.md) — 一般 BFS／用雙端佇列的 0-1 BFS | O(V+E) | LC 1091, LC 1368 |

不確定要用哪個？→ [shortest_path_comparison.md](./shortest_path_comparison.md)。

<!-- 9cb5dde9b332 -->
### 圖的表示法 — 怎麼建出來 ⭐⭐⭐⭐⭐

幾乎每一道 LC 圖題丟給你的輸入，都逃不出那幾種形狀。認出形狀並把它轉成圖，就是解題的
頭三十秒。

| LC 輸入形狀 | 怎麼建 | 典型題目 |
|---|---|---|
| `edges = [[u,v], ...]` | 鄰接串列，無向圖要建**雙向** | LC 323, 684, 1971 |
| `edges = [[u,v,w], ...]` | 存 `(neighbor, weight)` 配對的鄰接串列 | LC 743, 787 |
| `isConnected[i][j]` / `graph[i][j]` | 本來就是鄰接**矩陣** | LC 547, 1334 |
| `graph[i] = [neighbors]` | 本來就是鄰接**串列** | LC 785, 797, 802 |
| 網格 `char[][]` / `int[][]` | **隱式圖** — 格子 = 節點，4 或 8 個鄰居 = 邊 | LC 200, 695, 1091 |
| 帶 `neighbors` 的 `Node` 物件 | 指標圖 — 用 `{original: copy}` 對照表走訪 | LC 133 |
| 字串配對（`["a","b"]`） | 隱式圖 — 從輸入把節點挖出來丟進 `defaultdict` | LC 399, 721 |

**該選哪種表示法**

| | 空間 | 測試 `u→v` 這條邊 | 走訪 `u` 的鄰居 | 什麼時候用 |
|---|---|---|---|---|
| 鄰接串列 | O(V + E) | O(deg u) | O(deg u) | 稀疏圖 — LC 的預設情況 |
| 鄰接矩陣 | O(V²) | O(1) | O(V) | 稠密圖，或 `V` 很小（Floyd-Warshall） |
| 邊串列 | O(E) | O(E) | O(E) | Kruskal、Bellman-Ford — 這類會掃過所有邊的演算法 |

**邊串列**完全不用建：題目丟給你的 `edges` *本身*就是表示法。Kruskal 就排序它，
Bellman-Ford 就把它掃 `V-1` 次。

<!-- 00d72b743696 -->
#### **鄰接串列**
<!--CODE-->

<!-- ca55ae167924 -->
#### **鄰接矩陣**
<!--CODE-->

<!-- 497f37d3010b -->
#### **把網格當成圖**
<!--CODE-->

<!-- ca0b0dcff6d3 -->
#### **有向 vs 無向，以及入分支度／出分支度**
<!--CODE-->

- **無向圖**：`sum(degrees) == 2 * E`。一棵樹剛好有 `V - 1` 條邊而且沒有環。
- **有向圖**：源點的 `in_deg == 0`（Kahn 的起始集合，模板 4）；匯點的 `out_deg == 0`
  （LC 802 最終安全狀態）。
- 有些題目光靠分支度就能回答，根本不用走訪 — LC 997 Find the
  Town Judge（`in == n-1 and out == 0`）、LC 2924 Find Champion II（`in == 0`，而且唯一）、
  LC 1361 Validate Binary Tree Nodes。這個模式與它的各種坑放在
  [topology_sorting.md](./topology_sorting.md#template-10-in-degree-signature-answer-read-straight-off-the-degrees-)。

<p align="center"><img src="../pic/graph_rep1.png"></p>

<p align="center"><img src="../pic/graph_rep2.png"></p>

<!-- f7501af82044 -->
### visited 集合的紀律 ⭐⭐⭐⭐

你在*哪裡*標記節點，決定了走訪會不會停下來、以及答案對不對。

- **入隊時標記，不是出隊時標記**（BFS）。出隊才標記會讓同一個節點被塞進佇列很多次 —
  單純判斷可達性時結果還是對的，但最差情況會變成平方級；而只要走訪時會更新計數器或距離，
  答案就錯了（Template 1）。
- **要數節點或判斷可達性** → 一份共用的 `visited` 集合；每個節點只處理一次。
- **要列舉所有路徑** → 不要共用集合；往下走時推入、**往回走時彈出**
  （Template 2 的 LC 797 變形）。
- **有向圖的環偵測** → 三種狀態，不是布林值（Template 5）。
- 在網格上可以直接改輸入，不必另外配置 `visited`（`grid[r][c] = '0'`）—
  額外空間 O(1)，但會毀掉輸入；面試時記得講出來。

<!-- c0e46591083e -->
#### **三種記錄方式**
<!--CODE-->

<!-- dc2ce047cf91 -->
### Template 1：用走訪數連通分量 — LC 547 ⭐⭐⭐⭐

**辨識** — 「有幾個省份／群組／連通分量」、「是不是每個節點都連通」，輸入是不帶權的邊列表或鄰接矩陣。
V + E ≤ 10⁵ → 預期一次 O(V + E) 掃描。

**關鍵** — 在**所有**發射加起來之下，每個節點恰好被走訪一次，所以「從還沒走訪過的節點發射了幾次」
就是連通分量的數量。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n²)**：內層 `for nxt in range(n)` 對 n 個節點各掃一整列矩陣。換成鄰接列表，
  同一個迴圈每條邊只讀一次：O(V + E)。
- **空間 O(n)**：`visited` 陣列和佇列。

**陷阱**
- 只從節點 0 發射，不管有幾個分量都只數到 1：
  `[[1,0],[0,1]]` 回傳 1 而不是 2。外層的 `for start` 迴圈*就是*這個模板。
- 無向邊列表只加了一個方向：LC 323 的 `n = 2, edges = [[1,0]]`
  從 0 發射永遠到不了 1，於是把 1 算成第二個分量。兩個方向都要加。
- 孤立節點也是連通分量：`n = 3, edges = []` 答案是 3。只走 `defaultdict`
  鄰接列表的 key，會漏掉每一個沒有邊的節點。

**不適用時** — 邊一條一條加進來，而且每加一條就要回答「現在連通了嗎？」 →
併查集（[Template 3](#template-3-union-find-with-path-compression--lc-684-)）。島嶼網格 →
[dfs.md § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-)，格子本身就是節點，
不用建列表。

**練習** — LC 547 · LC 1971 *（兩個節點在不在同一個分量？）* · LC 323 ⭐ *（邊列表）* ·
LC 261 ⭐ *（樹 = 一個分量且恰好 n − 1 條邊）* · LC 1319 *（分量數 − 1 條線）*

<!-- feca1e0868de -->
### Template 2：DFS 可達性 — LC 841 ⭐⭐⭐⭐

**辨識** — 「能不能進到每個房間」、「a 到 b 有沒有路徑」，圖以 `graph[i] = [neighbours]` 的形式給你，
而且題目裡完全沒出現「最短」或「最少」：任何一條路都行。V + E ≤ 10⁵。

**關鍵** — 節點第一次被走到就加進 `visited`，之後永遠不移除，所以每個節點只展開一次、每條邊只讀一次；
堆疊清空時，`visited` 恰好就是從起點可達的集合。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(V + E)**：`visited` 這道關卡讓每個房間只進堆疊一次，而
  `for key in rooms[room]` 在整趟執行中每把鑰匙只讀一次。
- **空間 O(V)**：`visited` 加上堆疊，各自最多放 V 個房間。

**陷阱**
- 在 Python 用遞迴 DFS 跑 10⁵ 個節點的鏈，會丟出 `RecursionError`（預設上限是
  1,000）。明確的堆疊沒有深度限制；Java 的遞迴寫法撐到 ~10⁴ 沒問題。
- 在彈出時才檢查 `visited`、而不是推入時：每把能開這個房間的鑰匙都會把它推一次——
  答案還是對的，但堆疊會長到 O(E)。

**不適用時** — 「最少移動／步數」 → BFS
（[bfs.md § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-)）：DFS 第一次走到某個節點，
不見得是最短的路。「回傳每一條路徑」 → 下方的 LC 797 變形，它拿掉了共用的 `visited` 集合。

**練習** — LC 841 · LC 1971 *（邊列表，兩個方向）* · LC 797 *（所有路徑：回溯）*
· LC 133 ⭐ *（visited 就是 `{original: copy}` 對照表）* · LC 417 ⭐ *（從兩個大洋各自反向求可達性）*

<!-- 27fd4fcf40b1 -->
#### 變形：列舉**所有**路徑（回溯，不用 `visited` 集合） — LC 797

*變化點*：這張圖是 **DAG**，所以根本沒有環要防 — 整個拿掉 `visited` 集合，改成推入／彈出目前的路徑（經典回溯）。在這裡用共用的 `visited` 集合是**錯的**：它會讓一個節點無法出現在多條路徑裡。

<!--CODE-->

<!--CODE-->

**經驗法則**：*數節點／判可達* → 用 `visited` 集合（每個節點一次）。*列舉路徑* → 回溯，往下走時標記、**往回走時取消標記**。

<!-- 30ca3f8cd54e -->
### Template 3：帶路徑壓縮的併查集 — LC 684 ⭐⭐⭐⭐

**辨識** — 「多餘的連線」、「邊一條一條加進來」、「a 和 b 在不在同一組」、「合併共用同一個 email 的帳號」。
n ≤ 10⁵，而且只問連通性——從不問路徑或距離。

**關鍵** — 每個集合是一棵樹，由樹根代表它。`find(a) == find(b)` 恰好在 a 和 b 屬於同一集合時成立，
所以當一次 `union` 發現兩端已經共用同一個樹根，這條邊就是讓圖形成環的那條邊。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(E · α(n))**：每條邊一次 `union`；同時有路徑壓縮*和*按秩合併時，每次
  `find` 攤還下來是 α(n)，也就是反阿克曼函數——任何實際的 n 都小於 5。
- **空間 O(n)**：`parent` 和 `rank`。

**陷阱**
- 節點編號是 1..n：`UnionFind(n)` 在 LC 684 的 `[[1,2],[1,3],[2,3]]` 會出界，因為它碰到了
  索引 3。大小要開 `n + 1`。
- `parent[a] == parent[b]` 不等於 `find(a) == find(b)`：同一集合的兩個節點，在某次 `find`
  把它們之間的路徑壓縮之前，直接父節點可能不同。
- 併查集對**有向**環一無所知：LC 685（有向版的多餘連線）必須先處理入度為 2 的情況，
  再做合併。

**不適用時** — 你需要路徑、路徑長度，或圖是有向的 → BFS / DFS。邊會隨時間被刪掉 →
併查集沒辦法拆開集合；把刪除改成離線、倒過來處理。

**練習** — LC 684 ⭐ · LC 547 · LC 323 ⭐ · LC 261 ⭐ · LC 721 *（每個 email 是一個節點）* ·
LC 947 *（每一列、每一行各是一個節點）*。帶權併查集、回滾等其他內容：
[union_find.md](./union_find.md)。

<!-- 40f561d233f8 -->
### Template 4：拓撲排序（Kahn 的 BFS） — LC 207 ⭐⭐⭐⭐⭐

> 各種排序變形——字典序最小、所有順序、平行排程、樹的中心、DAG 上的 DP——都在 [topology_sorting.md](./topology_sorting.md)。

**辨識** — 「先修條件」、「能不能修完所有課」、「讓每個任務都排在它的相依項之後的順序」、
「外星字典」。有向邊，V + E ≤ 10⁵。

**關鍵** — 節點只有在入度降到 0 時才進佇列，也就是它的每個先修項都已經輸出之後。
環上的節點永遠到不了 0，所以 `taken < n` 恰好在圖有環時成立。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(V + E)**：每個節點出隊一次，內層 `for nxt in graph[node]`
  在整趟執行中對每條邊的終點只遞減一次。
- **空間 O(V + E)**：鄰接列表；`indegree` 和佇列是 O(V)。

**試跑**

<!--CODE-->

**陷阱**
- 邊的方向：`[a, b]` 的意思是*先 b 再 a*。建成 `a → b` 仍然能過 LC 207（反向的圖環一樣），
  但到了 LC 210 回傳的順序就是倒的。
- 只把節點 0 放進佇列：完全沒有邊的課、或第二個源點，永遠不會被修到。
  每個入度為 0 的節點都要放進去。

**不適用時** — 圖是無向的 → 根本沒有順序可找；判斷有沒有環用
併查集（[Template 3](#template-3-union-find-with-path-compression--lc-684-)）。要字典序最小的順序
→ 用 min-heap 取代佇列（[topology_sorting.md](./topology_sorting.md)）。

**練習** — LC 207 ⭐ · LC 210 *（回傳順序）* · LC 1136 *（BFS 的層 = 學期）* ·
LC 269 ⭐ *（邊來自相鄰的單字）* · LC 329 ⭐ *（在遞增格子構成的隱式 DAG 上跑 Kahn）*

<!-- 6f6cd7f980c9 -->
### Template 5：三態 DFS 環偵測 — LC 802 ⭐⭐⭐⭐

**辨識** — 「這張有向圖有沒有環」、「死結」、「最終安全的節點」、
「從這個節點出發的每條路都停在終點節點」。有向邊，V ≤ 10⁴，E ≤ 4·10⁴。

**關鍵** — 節點的 DFS 還在跑時是 `ON_PATH`，跑完且沒遇到環時是 `SAFE`。
走到一個 `ON_PATH` 節點，代表沿著一條邊回到目前路徑的上方，那就是環；`SAFE` 節點已經被證明過，
永遠不會再探索一次。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(V + E)**：節點只離開 `UNVISITED` 一次；之後對它的每次呼叫都會在前兩個 `if`
  之一返回，所以 `for nxt in graph[node]` 裡的每條邊只走一次。
- **空間 O(V)**：`state` 加上遞迴堆疊，深度最多 V。

**陷阱**
- 用布林的 `visited` 而不是三種狀態，會在菱形圖裡誤報環：
  `[[1,2],[3],[3],[]]` 經由 2 第二次走到節點 3，就把它當成回邊。
- 把跑完的節點重設回 `UNVISITED`（回溯）答案還是對的，但每條路徑都會重新探索它：
  在路徑很多的 DAG 上是指數級。要把它標成 `SAFE`。
- Python 預設遞迴上限是 1,000；10⁴ 個節點的鏈需要
  `sys.setrecursionlimit(20000)`，或改在反向圖上跑 Kahn 演算法，完全不用遞迴。

**不適用時** — 圖是**無向**的：回到父節點的那條邊看起來就像回邊。
改用併查集（[Template 3](#template-3-union-find-with-path-compression--lc-684-)——回傳 False 的 `union`
就是環），或把父節點往下傳。只想知道一組先修條件有沒有環 → Kahn（[Template 4](#template-4-topological-sort-kahns-bfs--lc-207-)）。

**練習** — LC 207 ⭐ *（有環代表課修不完）* · LC 802 · LC 1059 *（每條路都停在
目的地）* · LC 2360 *（出度 ≤ 1 時的最長環）*

<!-- 7674f542b285 -->
### Template 6：二分圖判定（2-著色） — LC 785 ⭐⭐⭐⭐

**辨識** — 「把節點分成兩組，讓每條邊都跨在兩組之間」、
「這張圖是不是二分圖」、「彼此討厭的人必須分開」。n ≤ 100（LC 785）到
2,000（LC 886）。

**關鍵** — 每個上了色的節點都和替它上色的節點顏色相反，所以一條兩端同色的邊會形成**奇數**環——
而奇數環正是讓一張圖不是二分圖的唯一原因。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(V + E)**：每個節點只上色一次，每份鄰接列表在
  `for neighbor in graph[node]` 裡只讀一次。
- **空間 O(V)**：`colors` 加上佇列。

**陷阱**
- 只從節點 0 跑一次 BFS，只會檢查節點 0 所在的分量：`[[],[2,3],[1,3],[1,2]]` 有一個孤立的
  節點 0 和一個三角形，少了外層迴圈就會回傳 True。
- 自環（`graph[0] = [0]`）永遠不是二分圖。同色檢查會抓到它，因為
  節點和它的鄰居是同一個節點。
- 在 `colors` 之外再開一個 `visited` 陣列，等於兩份可能不一致的陣列；`-1` 本身就代表
  還沒走訪。

**不適用時** — 三組以上 → k-著色一般是 NP-hard；用回溯，或在度數很小時用貪婪
（[graph_advanced.md](./graph_advanced.md)）。兩組必須一樣大 → 那是分割或匹配問題，
不是著色問題。

**練習** — LC 785 ⭐ · LC 886 *（從討厭的配對建圖）* · LC 1042 *（四種顏色，
度數 ≤ 3：貪婪）* · LC 2493 *（先做二分圖判定，再對每個分量跑 BFS）*

<!-- cd78f55ef3ac -->
#### **解法 2：DFS 著色**
<!--CODE-->

> 用併查集偵測二分圖、最大二分圖匹配和貪婪 k-著色都在 [graph_advanced.md](./graph_advanced.md)；LC 886 Possible Bipartition 的完整解法在 [graph_examples.md](./graph_examples.md)。

<!-- ca54e23f0d8e -->
## 練習路徑

| 階段 | 題目（由易到難） | 新增的能力 |
|---|---|---|
| **Core** ⭐ | LC 323 · LC 261 · LC 684 · LC 133 · LC 207 · LC 269 | 連通分量、判斷是不是樹、併查集、透過對照表複製圖、Kahn、先建圖再排序 |
| **Practice** | LC 1971 · LC 997 · LC 547 · LC 841 · LC 797 · LC 785 · LC 802 · LC 210 | 邊列表／矩陣／列表三種輸入、只看度數、所有路徑、2-著色、三態 DFS、回傳順序 |
| **Stretch** | LC 721 · LC 947 · LC 1192 · LC 332 | 把屬性當成 DSU 節點、橋、尤拉路徑 |

---

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- ff2aada9fde6 -->
### 決策表 — 該用哪個圖的模式？

<!--CODE-->

<!-- 92882877f70d -->
### 複雜度速查
| 演算法 | 時間複雜度 | 空間複雜度 | 備註 |
|-----------|-----------------|------------------|-------|
| BFS/DFS | O(V + E) | O(V) | 標準走訪 |
| Dijkstra | O((V+E)logV) | O(V) | 搭配二元堆積 |
| Bellman-Ford | O(VE) | O(V) | 可處理負權重 |
| Floyd-Warshall | O(V³) | O(V²) | 全點對 |
| 併查集 | O(α(n)) | O(V) | 近似常數 |
| 拓撲排序 | O(V + E) | O(V) | 線性時間 |

<!-- cd45b05eef8f -->
### 依模式分類的題目

<!-- b7febb271cea -->
#### **圖走訪類題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Number of Islands | 200 | 網格上的 DFS/BFS | Medium |
| Max Area of Island | 695 | DFS 搭配計數 | Medium |
| Clone Graph | 133 | BFS/DFS 搭配對照表 | Medium |
| Pacific Atlantic Water | 417 | 多源 DFS | Medium |
| Word Ladder | 127 | BFS 求最短路徑 | Hard |
| Surrounded Regions | 130 | 從邊界出發的 DFS | Medium |
| Evaluate Division | 399 | 帶權 DFS（比值圖） — [graph_examples.md](./graph_examples.md#2-9-evaluate-division--lc-399) | Medium |
| Longest Increasing Path in Matrix | 329 | 隱式 DAG 上的 DFS + 記憶化 — [graph_examples.md](./graph_examples.md#2-10-longest-increasing-path-in-a-matrix--lc-329) | Hard |
| All Paths From Source to Target | 797 | DAG 上的 DFS 回溯 | Medium |
| Keys and Rooms | 841 | 從節點 0 出發的單純 DFS/BFS 可達性 | Medium |
| Find if Path Exists in Graph | 1971 | BFS/DFS 或併查集判連通 | Easy |
| Find the Town Judge | 997 | 統計入／出分支度，不需要鄰接串列 — [topology_sorting.md](./topology_sorting.md#variation-a--degree-signature-lookup-lc-997-find-the-town-judge) | Easy |

<!-- c29a8d23bb2b -->
#### **最短路徑類題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Network Delay Time | 743 | Dijkstra | Medium |
| Cheapest Flights K Stops | 787 | 改造過的 Dijkstra | Medium |
| Path with Min Effort | 1631 | 網格上的 Dijkstra | Medium |
| Bus Routes | 815 | 在路線上做 BFS | Hard |
| Shortest Path Binary Matrix | 1091 | BFS | Medium |

<!-- b23c9af4432c -->
#### **併查集類題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Number of Connected Components | 323 | 基本併查集 | Medium |
| Redundant Connection | 684 | 偵測環 | Medium |
| Accounts Merge | 721 | 併查集搭配對照表 | Medium |
| Number of Provinces | 547 | 併查集或 DFS | Medium |
| Satisfiability of Equality | 990 | 併查集 | Medium |
| Most Stones Removed | 947 | 對共用的列／行屬性做 DSU — [graph_examples.md](./graph_examples.md#2-11-most-stones-removed-with-same-row-or-column--lc-947) | Medium |
| Make Network Connected | 1319 | DSU：連通分量 + 多餘的邊 | Medium |

<!-- 57d0ed8252b7 -->
#### **拓撲排序類題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Course Schedule | 207 | 偵測環 | Medium |
| Course Schedule II | 210 | 輸出拓撲順序 | Medium |
| Alien Dictionary | 269 | 建圖 + 排序 | Hard |
| Minimum Height Trees | 310 | 逐層剝葉子 | Medium |
| Parallel Courses | 1136 | 一層一層的 BFS | Medium |

<!-- d8865d6a1702 -->
#### **二分圖類題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Is Graph Bipartite | 785 | BFS 著色 | Medium |
| Possible Bipartition | 886 | DFS 著色 | Medium |
| Flower Planting With No Adjacent | 1042 | 貪婪 k-著色（分支度 < k） | Medium |

<!-- 1eb3b94c1c3b -->
#### **進階圖論題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Critical Connections | 1192 | Tarjan 找橋 — [graph_advanced.md](./graph_advanced.md#template-1-tarjans-low-link-dfs--scc-bridges-articulation-points--lc-1192-) | Hard |
| Find Eventual Safe States | 802 | 偵測環 | Medium |
| Reconstruct Itinerary | 332 | Hierholzer 演算法 | Hard |
| Cracking the Safe | 753 | de Bruijn 圖 + 尤拉迴路 — [graph_advanced.md](./graph_advanced.md#template-2-euler-path--circuit-hierholzer--lc-753-) | Hard |
| Minimum Spanning Tree | 1135 | Kruskal ／ Prim — [minimum_spanning_tree.md](./minimum_spanning_tree.md) | Medium |

<!-- f6b226141d1f -->
### 解題步驟
1. **判斷圖的種類**：有向／無向、帶權／不帶權
2. **選表示法**：鄰接串列還是矩陣
3. **選演算法**：依題目需求決定
4. **處理邊界情況**：空圖、不連通的分量
5. **好好追蹤狀態**：已走訪節點、路徑、距離
6. **必要時再最佳化**：改善空間或時間

<!-- 3c6c8d01fd34 -->
### 常見錯誤與技巧

**🚫 常見錯誤：**
- 沒有處理不連通的分量
- visited 狀態管理錯誤
- 遞迴 DFS 裡漏掉環偵測
- 選錯圖的表示法
- 沒有考慮邊界情況（自環、重邊）

**✅ 最佳實務：**
- 稀疏圖用鄰接串列
- 明確定好 visited 的追蹤策略
- 有向和無向兩種情況都要處理
- 動態連通性考慮用併查集
- 用不連通的分量測試

<!-- d4f3490924d3 -->
### 面試技巧
1. **先問清楚圖的性質**：有向嗎？帶權嗎？連通嗎？
2. **畫小例子**：把問題視覺化
3. **選對表示法**：串列還是矩陣
4. **先講複雜度**：時間和空間都要
5. **處理邊界情況**：空圖、單一節點、環
6. **逐步最佳化**：先寫簡單版，再改進

<!-- 157df6168bdd -->
### 相關主題
- **樹**：圖的特例（連通且無環）
- **動態規劃**：圖上的 DP（路徑、樹）
- **貪婪演算法**：MST 演算法
- **堆積／優先佇列**：Dijkstra、Prim 會用到
- **遞迴／回溯**：DFS 的實作方式
