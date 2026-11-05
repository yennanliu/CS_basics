<!-- 9d1f106a3a82 -->
# 進階圖論演算法

> **範圍** — 第一輪面試準備可以先跳過的圖論技巧：Tarjan 的 low-link 家族（強連通分量、橋、關節點）、Euler 路徑與迴路，以及二分圖的延伸主題 — 奇偶併查集、最大匹配（以及它的最大流觀點）和貪婪 k-著色。
> **另見**：[graph.md](./graph.md) — 圖的表示法、走訪、連通性、環偵測與 2-著色，以及把你導向這裡的選擇表；[graph_examples.md](./graph_examples.md) — 建立在 graph.md 模板上的經典題；[dfs_advanced.md](./dfs_advanced.md) — 不屬於圖論的冷門 DFS 技巧（雙網格驗證、字典樹 DFS、以深度為索引的堆疊）；[union_find.md](./union_find.md) — Template 3 所依賴的併查集原語。

<!-- 9b52ab093d4b -->
## LeetCode 題目清單

- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Strongly Connected Component](https://leetcode.com/problem-list/strongly-connected-component/)
- [Eulerian Circuit](https://leetcode.com/problem-list/eulerian-circuit/)
- [Biconnected Component](https://leetcode.com/problem-list/biconnected-component/)

<!-- 14b567efad5c -->
## 總覽

這裡的東西全都是 **low-link DFS**、**吃掉邊的 DFS**，或**找增廣路徑的搜尋**。它們共用同一個形狀：
一次普通的走訪，再加一份額外的狀態 — `low[]`、被消耗掉的鄰接表，或配對／殘餘容量陣列 —
就把走訪變成了一個證明。L3 面試很少考其中任何一個；真的考時，幾乎都是找橋（LC 1192）
或行程重建（LC 332），而且題目會直接點名那個結構。

<!-- e56e3e1e9951 -->
### 先備知識
- [graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-)：帶著每個節點狀態的
  DFS。Tarjan 在同一個 DFS 上多加一個發現時間戳和一個 `low[]` 值。
- [graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-)：BFS / DFS
  2-著色。下面的 Templates 3–5 是它不夠用時該做的事。
- [graph.md § Template 3](./graph.md#template-3-union-find-with-path-compression--lc-684-)：
  帶路徑壓縮的併查集，Template 3 把它加倍成奇偶併查集。
- [dfs.md § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-)：這裡每個模板的起點，
  遞迴的圖 DFS。

<!-- c112d1f8e583 -->
## 辨識

| 題目裡的訊號 | 限制條件線索 | 該用 | 不適用時 |
|---|---|---|---|
| 「關鍵連線」、「拿掉哪條邊會讓網路斷開」 | V ≤ 10⁵，E ≤ 10⁵ | [Template 1](#template-1-tarjans-low-link-dfs--scc-bridges-articulation-points--lc-1192-) — `low[child] > disc[parent]` 就是橋 | 只問拿掉一條邊後還連不連通 → 重跑 [graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-) |
| 「拿掉哪台伺服器／哪個格子會讓網路分裂」 | V ≤ 10⁵ | [Template 1 的關節點變形](#variant-articulation-points-cut-vertices) | 網格題答案永遠 ≤ 2（LC 1568）→ 逐格嘗試拿掉 |
| 「互相可達」、「強連通」 | 有向，V ≤ 10⁵ | [Template 1 的 SCC 變形](#variant-strongly-connected-components-directed) | 無向 → 那就只是連通分量 |
| 「每張機票／每條邊恰好用一次」 | E ≤ 300 | [Template 2](#template-2-euler-path--circuit-hierholzer--lc-332-) — Hierholzer | 每個**節點**走一次 → 哈密頓路徑，bitmask DP |
| 「包含所有組合的最短字串」 | k^n ≤ 4,096 | [Cracking the Safe — LC 753](#cracking-the-safe--lc-753) — de Bruijn 圖 + 尤拉迴路 | — |
| 「分兩組，這些配對必須分開」，配對逐一到來 | n ≤ 2,000 | [Template 3](#template-3-parity-union-find-bipartite-check--lc-886) — 奇偶併查集 | 所有配對一開始就給齊 → BFS 2-著色，[graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) |
| 「最多能配成幾對」、「每個 X 最多指派給一個 Y」 | 每邊 ≤ 200 | [Template 4](#template-4-bipartite-matching-by-augmenting-paths-kuhn--lc-1820) — Kuhn | 每個配對有成本 → 最小成本指派（匈牙利演算法／bitmask DP） |
| 「最大吞吐量」、「切斷哪些連線最便宜」 | V 最多幾百 | [Template 4 的最大流推廣](#generalisation-max-flow--min-cut-edmonds-karp) | — |
| 「k 種顏色，相鄰不同色」，每個度數 < k | V ≤ 10⁴ | [Template 5](#template-5-greedy-k-colouring--lc-1042) — 貪婪 | k = 2 → 二分圖檢查；沒有度數上界 → 回溯 |

<!-- 02d3781fd454 -->
## Low-Link DFS

<!-- a182d5daceb3 -->
### Template 1：Tarjan 的 Low-Link DFS — SCC、橋、關節點 — LC 1192 ⭐⭐⭐⭐

**辨識** — 「關鍵連線」、「拿掉哪條邊會讓圖不連通」、「割點」。無向圖，V 最多 10⁵：
逐條拿掉邊再檢查連通性是 O(E·(V + E))，太慢；一次 DFS 是 O(V + E)。

**關鍵** — `disc[u]` 是 DFS 第一次到達 `u` 的時間；`low[u]` 是 `u` 的子樹只用樹邊加上**一條**回邊
能到達的最小 `disc`。樹邊 `u → v` 恰好在 `low[v] > disc[u]` 時是橋：`v` 子樹裡沒有任何東西能爬回
`u` 或更上面，所以切斷 `u–v` 就把它孤立了。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(V + E)**：`dfs` 對每個頂點只跑一次（由 `disc[v] == -1` 把關），它的
  `for v in graph[u]` 迴圈每個鄰接項目只讀一次，總共 2E 個。
- **空間 O(V + E)**：鄰接表，加上 `disc` / `low` 和深度最多 V 的遞迴堆疊。

**試跑** — `n = 4`，邊 `[[0,1],[1,2],[2,0],[1,3]]`：

<!--CODE-->

**陷阱**
- 遇到回邊取 `disc[v]`，不是 `low[v]`。找橋時兩者剛好都對，但找關節點時 `low[v]`
  會把祖先自己的可達範圍再往上帶，藏住一個割點。
- `v == parent` 假設沒有平行邊（LC 1192 成立）。如果 `u–v` 有兩條，要改用**邊的編號**跳過，
  否則重複的那條會被誤報成橋。
- Python 預設遞迴上限是 1,000；10⁵ 個節點的鏈需要 `setrecursionlimit` 那一行，
  或改用明確的堆疊。

**不適用時** — 問題只是「拿掉這一條邊後圖還連通嗎」：查詢少時，每次查詢做一次走訪
（[graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-)）比較簡單。
邊隨時間加入、而且只在乎連通性 → 併查集。

**練習** — LC 1192 ⭐ · LC 1568 *（網格上的關節點；答案最多是 2）* ·
LC 2360 *（函數圖裡的環，不用 Tarjan 的 SCC 想法）*

<!-- 62cfbf40e3fc -->
#### 變形：強連通分量（有向）

在**有向**圖上跑同一個 DFS，再加一個堆疊存放目前 DFS 路徑上的頂點。
當 `v` 的子節點都跑完後 `low[v] == disc[v]`，`v` 就是一個 SCC 的根：把堆疊彈到 `v` 為止。
回邊只有在目標還在堆疊上時才算數（已完成的 SCC 是封閉的）。

<!--CODE-->

**Kosaraju** 是兩趟法的替代方案：先 DFS 一次記錄完成順序，再依完成順序的反向，在**反向**圖上 DFS；
第二趟的每棵樹就是一個 SCC。一樣 O(V + E)，在壓力下比較容易寫對，但需要把反向圖存在記憶體裡。

<!-- 6de0c108dc85 -->
#### 變形：關節點（割點）

非根節點 `u` 是割點，當它有某個子節點 `v` 滿足 `low[v] >= disc[u]`（注意是 `>=`：子節點可以到達 `u`
本身，但到不了更上面）。DFS 的根恰好在有兩個以上 DFS 子節點時是割點。

<!--CODE-->

| | 橋 `u–v` | 關節點 `u` | SCC 根 `v` |
|---|---|---|---|
| 圖 | 無向 | 無向 | 有向 |
| 判斷 | `low[v] > disc[u]` | `low[v] >= disc[u]`（根：≥ 2 個子節點） | `low[v] == disc[v]` |

<!-- 93da71aff86e -->
## 覆蓋所有邊的走法

<!-- 2f61787305f9 -->
### Template 2：尤拉路徑／迴路（Hierholzer） — LC 332 ⭐⭐⭐⭐

**辨識** — 「每張機票／每條邊恰好用一次」、「重建行程」。圖上有一條把每條**邊**用一次的路徑
（不是每個節點）。LC 332 的 E ≤ 300，但對邊的順序做普通回溯在最壞情況是指數級；Hierholzer 是線性的。

**關鍵** — 一路往前走、把邊吃掉，直到卡住；卡住的那個節點只可能是剩下路程的**終點**，
所以在那時（後序）把它加進路線。把後序反轉就是整條走法。消耗的是邊，節點從不標記：
同一個機場本來就可以重複經過。

| 圖 | 尤拉迴路存在的充要條件 | 尤拉路徑存在的充要條件 |
|---|---|---|
| 無向 | 每個度數都是偶數 | 奇數度的頂點有 0 或 2 個 |
| 有向 | 每處 `in == out` | 一個 `out − in = 1`（起點）、一個 `in − out = 1`（終點），其他相等 |

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(E log E)**：`sorted(tickets, ...)` 那一行（Java 是堆積）；之後每張機票在
  `while stack` 迴圈裡只被推入、彈出各一次。
- **空間 O(E)**：鄰接表、堆疊和路線各自最多存 E + 1 個項目。

**試跑** — 機票 `JFK→SFO, JFK→ATL, SFO→ATL, ATL→JFK, ATL→SFO`：

<!--CODE-->

**陷阱**
- 對機場用 `visited` 集合就會壞掉：`JFK → ATL → JFK` 是故意重訪 JFK。
- 只做「永遠取最小的」貪婪而沒有後序，會在 `JFK→KUL, JFK→NRT, NRT→JFK` 上失敗：
  KUL 是死路，所以它必須排在**最後**。後序會把它放到那裡。
- 路線是倒著建的；忘了最後的反轉（或 `addFirst`）是經典 bug。

**不適用時** — 每個**節點**都要走一次（哈密頓路徑，LC 847）：一般情況下是 NP-hard，
小 n 時用 bitmask BFS / DP，不是 Hierholzer。

**練習** — LC 332 ⭐ · LC 753 *（de Bruijn 圖，見下）* · LC 2097 *（合法的配對排列：
先從度數差找出起點）*

<!-- 87ee8be87837 -->
### Cracking the Safe — LC 753

> **關鍵** — 節點是最後打進去的 `n − 1` 個數字，`k` 個數字各是一條通往下一個節點的邊，所以
> `k^n` 條邊正好就是 `k^n` 組密碼。每個節點都是 `in = out = k`，所以尤拉迴路存在，沿著它走一圈
> 就把每組密碼剛好打一次：最短答案長度是 `k^n + n − 1`。

**歸約到**：[Template 2](#template-2-euler-path--circuit-hierholzer--lc-332-) — 在隱式圖上跑 Hierholzer，
消耗一條邊就是取用該節點下一個還沒用過的數字。

<!--CODE-->

**陷阱**
- 遞迴寫法（在數字迴圈裡呼叫 `dfs(edge[1:])`）每組密碼遞迴一層，最深 4,096 層，
  超過 Python 預設的 1,000 上限。上面明確堆疊的寫法沒有這個限制。
- 別忘了 `start` 前綴：走法輸出的是邊（數字），而第一組密碼之前要先打好 `n − 1` 個數字。

<!-- 5d0324ebe1aa -->
## 匹配、流量與著色

<!-- e64d6e78a268 -->
### Template 3：奇偶併查集（二分圖檢查） — LC 886

**辨識** — 「分成兩組，讓每對互相討厭的人分開」，配對以清單給出，或一次來一個。
n ≤ 2,000，配對 ≤ 10⁴。

**關鍵** — 給每個人 `x` 一個分身 `x + n`，代表「另一組」。配對 `(a, b)` 表示 `a` 和 `b` 的分身同組、
`b` 和 `a` 的分身同組。一旦某個配對的兩人已經在同一個集合裡，就不可能了：某條長度為偶數的
「相反」約束鏈把他們放在一起，所以這個配對封閉了一個奇數環。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O((n + m)·α(n))**：m 個配對各做兩次 `find` 和兩次 `union`；路徑減半讓每次都接近常數。
- **空間 O(n)**：長度 2(n + 1) 的 `parent` 陣列。

**陷阱**
- 檢查必須在同一個配對的 union **之前**；放在之後永遠會發現 `a` 和 `b` 的分身在一起，
  永遠抓不到衝突。
- LC 886 的人編號從 1 開始：陣列大小要 `2 * (n + 1)`，不是 `2 * n`。

**不適用時** — 所有配對在作答前都已知：BFS 2-著色
（[graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-)）才是面試官預期的答案，
而且還能回傳兩個組別。LC 886 完整的 BFS 解法在 [graph_examples.md](./graph_examples.md)。

**練習** — LC 886 ⭐ · LC 785 · LC 990 *（先處理等式、再處理不等式：一般併查集，
不用分身）*

<!-- 407360449495 -->
### Template 4：用增廣路徑做二分匹配（Kuhn） — LC 1820

**辨識** — 「最多能配成幾對」、「每個男生最多邀一個女生，每個女生最多接受一個」。
兩邊、一個允許配對的矩陣，每邊 ≤ 200。

**關鍵** — 依序替每個左側頂點找配對。左側 `u` 可以拿到右側 `v`，條件是 `v` 還沒配對，
或者 `v` 目前的伴侶能**換到**另一個右側頂點 — 這個遞迴嘗試就是一條增廣路徑。
匹配是最大的，恰好在沒有增廣路徑剩下時，所以每個左側頂點嘗試一次就夠了。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(m·m·n)**：外層迴圈呼叫 m 次 `try_match`；每一次裡面，`tried` 讓每個女生只被進入一次，
  而每次進入都掃過她男伴那一列的 n 格 — 每次搜尋 O(m·n)。
- **空間 O(n)**：`partner` 和 `tried`，加上深度最多 m 的遞迴堆疊。

**陷阱**
- `tried` 必須**每個男生都重新建立**。跨男生重用，會擋住後面增廣路徑需要的女生；
  完全沒有它，則會在一圈互相換位上無限循環。
- 「每個男生拿第一個空著的女生」的貪婪不是最大的：`[[1,1],[1,0]]` 貪婪讓男生 0 拿女生 0，
  男生 1 落單；Kuhn 會把男生 0 換到女生 1。

**不適用時** — 配對有**成本**，要找最便宜的完美指派（LC 1066、LC 1947）：
n ≤ 10 時那是 bitmask DP，不是無權匹配。

**練習** — LC 1820 ⭐ · LC 1349 *（二分座位圖上的最大獨立集 = 座位數 −
匹配數）* · LC 2123 *（最少操作次數，König 定理）*

<!-- 26b8edc2b3ec -->
#### 推廣：最大流／最小割（Edmonds-Karp）

匹配就是每條容量都是 1 的最大流：源點 → 每個左側頂點 → 允許的配對 → 每個右側頂點 → 匯點。
一般容量需要 Edmonds-Karp：在殘餘圖上 BFS 找最短的增廣路徑，推送它的瓶頸量，並加上反向容量，
讓之後的路徑可以撤銷它。根據最大流最小割定理，流量值同時也是最便宜的割。

<!--CODE-->

<!-- fd276a781e90 -->
### Template 5：貪婪 k-著色 — LC 1042

**辨識** — 「從 k 種顏色／類型中選一種，相連的節點不能相同」，並附上「每個節點最多 k − 1 個鄰居」
的上界。LC 1042：度數 ≤ 3，4 種花。

**關鍵** — 鄰居少於 k 個的節點，不管鄰居已經用了什麼，永遠都有一個空著的顏色。
所以按任意順序著色、每個節點取第一個鄰居沒用的顏色，永遠不會卡住：不用搜尋，也不用回溯。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(V + E)**：每個花園在 `for neighbour in graph[garden]` 裡只讀一次鄰居串列；
  顏色掃描最多 4 步。
- **空間 O(V + E)**：鄰接表。

**陷阱**
- 花園從 1 開始編號，答案串列從 0 開始。這裡差一，會變成替錯的花園著色。
- 沒有度數保證時貪婪可能失敗（用 2 種顏色、以壞順序替 4-環著色）；那就是回溯，
  或在 k = 2 時做二分圖檢查。

**不適用時** — k = 2：選一個顏色會**強制**每個鄰居的顏色，那是 BFS
2-著色（[graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-)）。

**練習** — LC 1042 ⭐ · LC 785 *（k = 2：傳播，不是貪婪）*

<!-- 9870b9ffe0e6 -->
## 何時不要用

| 技巧 | 通常改用什麼就夠 | 什麼時候簡單的卡片就夠 |
|---|---|---|
| Tarjan 橋／割點 | 拿掉那條邊或那個頂點再重跑一次走訪（[graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-)） | 只有一兩個候選要拿掉，或答案最多是 2 的網格題（LC 1568） |
| Tarjan / Kosaraju SCC | 三態 DFS（[graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-)）或 Kahn（[graph.md § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-)） | 問題只是「有沒有環」或「哪些節點不在任何環上」，而不是哪些節點共用一個環 |
| Hierholzer | 對邊做 DFS + 回溯 | E 很小，而且面試官要的是任一合法順序，不是字典序最小的 |
| 奇偶併查集 | BFS 2-著色（[graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-)） | 所有配對一開始就給齊，LeetCode 每一題二分圖題都是這樣 |
| Kuhn 匹配／最大流 | 貪婪，或 bitmask DP | 某一邊 ≤ 10 個成員（bitmask DP），或結構本身強制唯一選擇 |
| 貪婪 k-著色 | 回溯著色 | 沒有給度數上界：這時貪婪不保證能完成 |

<!-- 0a7c94678cf2 -->
## 總結

| 演算法 | 時間 | 空間 | 備註 |
|---|---|---|---|
| Tarjan（橋／割點／SCC） | O(V + E) | O(V + E) | 一次 DFS，`disc[]` + `low[]` |
| Kosaraju SCC | O(V + E) | O(V + E) | 兩趟 DFS，需要反向圖 |
| Hierholzer | O(E log E) | O(E) | 邊的順序不重要時是 O(E) |
| 奇偶併查集 | O((n + m)·α(n)) | O(n) | `2n` 個節點：`x` 和它的分身 |
| Kuhn 匹配 | O(V·E) | O(V) | 每個左側頂點一次增廣搜尋 |
| Edmonds-Karp | O(V·E²) | O(V²) | 最小割 = 最大流 |
| 貪婪 k-著色 | O(V + E) | O(V + E) | 只在每個度數 < k 時成立 |

**這份文件特有的陷阱**
- Tarjan 遇到回邊讀 `disc[v]`；橋的判斷是 `>`，割點的判斷是 `>=`。
- Hierholzer 消耗的是**邊**，從不標記節點；路線是反著出來的。
- Kuhn 對每個左側頂點都需要一個新的 `tried` 陣列。
- 遞迴很深：Templates 1 和 2 在 Python 需要 `setrecursionlimit` 或明確的堆疊。

<!-- 9fcceec63b50 -->
### 相關主題
- **[graph.md](./graph.md)**：表示法、走訪、環偵測與 2-著色 — 先讀這份
- **[union_find.md](./union_find.md)**：Template 3 加倍使用的併查集原語
- **[topology_sorting.md](./topology_sorting.md)**：把 SCC 縮點後得到一個可以排序的 DAG
- **[minimum_spanning_tree.md](./minimum_spanning_tree.md)**：Kruskal（排序邊 + 併查集）
  和 Prim（堆積或稠密陣列）。LC 1135、LC 1584。
