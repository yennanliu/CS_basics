<!-- ac792b10b137 -->
# Dijkstra — 實作範例

> **範圍** — [Dijkstra.md](./Dijkstra.md) 背後的解題實作庫：十一題、兩種語言，依「搜尋狀態長什麼樣」分組，因為那正是決定你需要 `dist[]`、需要第二個狀態維度、還是一個單純 `visited[]` 的關鍵。
> **另見**：[Dijkstra.md](./Dijkstra.md) — 母文件：五個模板、兩個判斷問題與演算法比較；[Bellman-Ford.md](./Bellman-Ford.md) — 邊權可以是負的時候；[Floyd-Warshall.md](./Floyd-Warshall.md) — 全點對最短路徑；[shortest_path_comparison.md](./shortest_path_comparison.md) — 三者之間怎麼挑；[bfs.md](./bfs.md) — 無權重的情況與 0-1 BFS；[heap.md](./heap.md) — 撐起這一切的優先佇列。

<!-- 9d2d3d383e1b -->
## LeetCode 題目清單

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph](https://leetcode.com/problem-list/graph/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

<!-- eada32f56033 -->
## 總覽

這裡是 [Dijkstra.md](./Dijkstra.md) 的長尾。母文件收模板，以及決定用哪個模板的那兩個問題；這份文件收*實際套用*這些模板的題目。

<!-- 099258334490 -->
### 關鍵性質
- **複雜度**：除非該解法另外註明，一律是用二元堆積的 O(E log V)；0-1 BFS 那幾題是 O(V + E)
- **核心想法**：演算法本身從來沒變 — 變的是「一個節點」代表什麼，底下的分組就是按照狀態離「單純一個節點」有多遠來排的
- **什麼時候用**：當母文件那兩個問題已經告訴你，這題根本上就是 Dijkstra 的形狀之後

<!-- 14320eb0e8e9 -->
## 經典單源最短路徑

<!-- 3ffc03194e08 -->
### 1) Network Delay Time — LC 743

> 從來源 k 跑 Dijkstra（戴克斯特拉）；答案是所有最短距離的最大值，若有任何節點到不了就回傳 -1。

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- 950c64017270 -->
### 2) Path with Maximum Probability — LC 1514 — 以機率為 key 的最大堆積


> 用最大堆積版的 Dijkstra，把邊上的機率相乘；起點是 1.0，目標是讓抵達機率最大。

**核心想法 — 為什麼需要 `best[]`（也就是 `max_prob[]`）：**
<!--CODE-->
這其實就是標準 Dijkstra 那套 `dist[node]` 剪枝，只是反過來：不是檢查 `dist[u] + w < dist[v]`（讓總和最小），而是檢查 `prob[u] * edge_prob > prob[v]`（讓乘積最大）。要用**最大堆積**（把機率取負，因為 `heapq` 預設是最小堆積），至於堆積裡那些過期／已被更好的值取代的項目，用 `if prob < best[node]: continue` 跳過。

<!--CODE-->

<!--CODE-->

**其他解法（LC 1514 不用優先佇列也解得掉）：**

因為邊權（機率）都非負，而且我們是求乘積最大而非總和最小，這題也能用 **Bellman-Ford** 和 **SPFA** 解 — 面試官若追問 Dijkstra 以外的做法，這兩個就派上用場。

<!--CODE-->

<!--CODE-->

| 解法 | 時間 | 空間 | 備註 |
|----------|------|-------|-------|
| **Dijkstra（最大堆積）** | O((V+E) log V) | O(V+E) | 一般情況最佳選擇；`end` 一被 pop 出來就能提早結束 |
| **Bellman-Ford** | O(V·E) | O(V) | 單純的巢狀迴圈，不用堆積；不准用 PQ 時的好備案 |
| **SPFA** | 最壞 O(V·E)，一般情況更快 | O(V+E) | 用佇列取代堆積；跟 0-1 BFS 同一個想法，只是套在帶權鬆弛上 |

<!-- c3b7f8350e89 -->
### 3) Number of Ways to Arrive at Destination — LC 1976 — Dijkstra + 路徑計數


> 標準 Dijkstra；在記錄最短距離的同時，順便記錄每個節點的最短路徑條數。

<!--CODE-->

<!--CODE-->

<!-- 98cbe9b1a52d -->
## 帶狀態限制的 Dijkstra

<!-- 7727c1c44f80 -->
### 4) Cheapest Flights Within K Stops — LC 787 — 二維狀態 ⭐⭐⭐⭐


> ⚠️ 這**不是**標準 Dijkstra。K 站中轉這個限制，替狀態多加了一個維度。
> 在這裡用標準的 `dist[node]` 剪枝是**錯的** — 用不同中轉次數抵達同一個節點，是不同的合法狀態。

**核心想法：**
- 狀態：`(cost, node, stops_used)` — stops_used 也是狀態身分的一部分
- 剪枝：用 `best[(node, stops)] <= cost` 取代 `dist[node] <= cost`
- 為什麼：用 1 次中轉花 900 抵達節點 A，跟用 2 次中轉花 100 抵達節點 A，兩者**都**合法；丟掉任何一個都會算出錯的答案

<!--CODE-->

<!--CODE-->

**為什麼 `dist[node]` 剪枝會失效（具體追蹤）：**
<!--CODE-->

<!-- d4919f9d7698 -->
## 網格

<!-- f74e3ad20ec2 -->
### 5) Path With Minimum Effort — LC 1631 — 網格上的極小化極大 ⭐⭐⭐⭐


> 讓路徑上最大的絕對差值最小；用最小堆積，以 effort 當優先權 key。

<!--CODE-->

<!-- d76bffc064af -->
#### LC 1631 的幾種 Dijkstra 寫法

<!-- 8c392537da60 -->
##### **寫法 1：用 dist[][] 陣列（推薦）**
<!--CODE-->
**為什麼可行**：`dist[][]` 的那句 `if (effort > dist[r][c]) continue;` 會自動略過任何比目前最佳解更差的路徑。

<!-- c84200b8bd50 -->
##### **寫法 2：用 visited[] 陣列**
<!--CODE-->
**為什麼 visited 可行**：最小堆積保證某個格子第一次被 pop 出來時，帶的就是最佳 effort，所以標記成 visited 就不會重複處理。

<!-- bd5cd3533ea3 -->
##### **兩種寫法比較**
| 解法 | 空間 | 邏輯 | 適用時機 |
|----------|-------|-------|----------|
| **dist[][]** | 額外 O(m×n) | 跟目前已知最佳值比較 | 會多次更新的時候 |
| **visited[]** | 額外 O(m×n) | 標記為已定案 | 邏輯較單純、提早結束較快 |

<!-- d8a741f4ed49 -->
#### LC 1631 的其他解法

<!-- aa30e396511e -->
##### **解法 3：二分搜尋 + DFS**
<!--CODE-->
**時間**：O((V+E) × log(maxH)) | **空間**：O(V)

<!-- 4c85d56ff822 -->
##### **解法 4：併查集（Kruskal 演算法）**
<!--CODE-->
**時間**：O((V+E) log(V+E)) = O(m×n × log(m×n)) | **空間**：O(m×n)

<!-- 31e9d8553f77 -->
#### LC 1631 該挑哪個解法
| 解法 | 優點 | 缺點 | 什麼時候用 |
|----------|------|------|-----------|
| **Dijkstra + dist[][]** | 最直覺、最標準 | 多花空間 | 想要經典的 Dijkstra 寫法 |
| **Dijkstra + visited[]** | 提早結束更單純 | 彈性較低 | 只需要求出最小 effort |
| **二分搜尋 + DFS** | 某些情況記憶體用得較少 | 較慢（要反覆 DFS） | 記憶體是關鍵限制 |
| **併查集** | 從圖的角度看很優雅 | 實作較複雜 | 練併查集 |

<!-- 72e2fe3b7406 -->
### 6) Swim in Rising Water — LC 778 — 網格上的極小化極大


> 最小堆積，優先權 = 到目前為止看過的最大高度；答案 = 抵達右下角的時間。

<!--CODE-->

<!-- c6e3ec33d6db -->
### 7) Minimum Path Sum — LC 64 — DAG 網格，DP 才是最佳解


> 只能往右／往下走，讓路徑上的總和最小。Dijkstra **可以動**，但這個網格是個 **DAG**，所以單純的 DP 嚴格來說更好。這題很適合拿來看清楚 Dijkstra 到底幫你買到了什麼 — 以及沒買到什麼。
> 參考：`leetcode_python/Dynamic_Programming/minimum-path-sum.py`（V0-1 / V0-2 = Dijkstra，V1 / V2 = DP）

<!-- 340602892d18 -->
#### **1) 核心想法**

<!--CODE-->

- **成本模型是可加的**（`new_cost = curr_cost + grid[nr][nc]`）而且**所有權重 ≥ 0** → Dijkstra 成立。
- **貪婪保證**：最小堆積永遠會 pop 出全域最便宜的邊界格子，所以**終點第一次被 pop 出來時就是答案** — 直接回傳，不用把堆積清空。
- **`cost_grid[r][c]`**（也就是「dist」陣列）= *目前為止*抵達 `(r,c)` 的最佳成本。它身兼**兩個**角色：
  1. **鬆弛過濾器** — 只有 `new_cost < cost_grid[nr][nc]` 才把鄰居推進堆積。
  2. **隱式的 `visited`** — `if curr_cost > cost_grid[r][c]: continue` 會丟掉堆積裡的過期項目，所以不需要另外開 `visited[][]`。
- **但是**：移動方向只有右／下 → 這個網格是**有天然拓撲順序的 DAG**（按列由左到右）。每個格子只可能從 `(r-1,c)` / `(r,c-1)` 走過來，而這兩格都在它*之前*就算好了。所以**一個格子算完之後再也不會被改進** — Dijkstra 那個堆積的存在意義，在這裡完全被浪費掉。
- **結論**：DP 的 `O(m*n)` 打敗 Dijkstra 的 `O(m*n*log(m*n))`。只有當題目改成四方向移動、或成本不可加時，才需要 Dijkstra。

<!--CODE-->

<!-- 77aab42cda7c -->
#### **2) 模式**

**模式名稱**：*可加且非負權重的網格最短路徑* → `heap of (cost, r, c)` + `dist[][]`

<!--CODE-->

**模式檢查清單**（任何可加成本的網格題都能重用）：

| 步驟 | 程式碼 | 為什麼 |
|------|------|-----|
| 1. 堆積 key 放第一位 | `pq = [[cost, r, c]]` | `heapq` 比較的是第 0 個元素 → 必須是 cost |
| 2. 初始化 `dist[][]` | `[[inf] * n for _ in range(m)]` | 記錄目前最佳值；同時扮演 `visited` |
| 3. 放入起點 | `cost_grid[0][0] = grid[0][0]` | LC 64 裡起點格子本身的值也要算 |
| 4. pop 到終點就回傳 | `if (r,c) == dest: return cost` | 貪婪保證 → 第一次 pop 就是最佳 |
| 5. 跳過過期項目 | `if cost > cost_grid[r][c]: continue` | 取代顯式的 `visited[][]` |
| 6. 鬆弛 | `if new_cost < cost_grid[nr][nc]: push` | 避免堆積爆掉 |

**更推薦的 DP 解**（同一題，不用堆積 — `O(m*n)` 時間、`O(1)` 額外空間）：

<!--CODE-->

<!--CODE-->

**⚠️ 那份 python 檔裡看得到的坑**

- **PQ 的排序**：cost 必須是 tuple 的*第一個*元素，否則堆積會照 row/col 排序。
- **`(x, y)` 跟 `(r, c)` 搞混**：V0-2 推的是 `(new_cost, nx, ny)`，其中 `x` 是行、`y` 是列，所以索引寫成 `grid[ny][nx]`，終點判斷是 `x == n-1 and y == m-1`。挑一種慣例（`r, c` 搭 `grid[r][c]` 比較安全）然後從頭用到尾。
- **二維初始化**：要寫 `[[inf] * n for _ in range(m)]`，絕對不要寫 `[[inf] * n] * m`（所有列都指向同一個 list）。
- **別把 `grid[0][0]` 加兩次**：把 `grid[0][0]` 當成起點的 cost 塞進堆積後，鬆弛時就不要再加一次。

<!-- 2b2ee3ce0c8a -->
#### **3) 相似的 LC 題目**

| LC # | 題目 | 移動方向 | 成本模型 | 最佳解法 | 為什麼 |
|------|-------|----------|-----------|----------------|-----|
| **64** | Minimum Path Sum | 只有 ↓→ | 可加總和 | **DP**（Dijkstra 也可以） | DAG → 存在拓撲順序 |
| **62** | Unique Paths | 只有 ↓→ | 計數 | **DP** | 是計數不是最小化 — 根本沒有堆積的概念 |
| **63** | Unique Paths II | 只有 ↓→ | 計數 + 障礙 | **DP** | 跟 62 一樣，障礙格算 0 |
| **120** | Triangle | ↓ / ↓ 右 | 可加總和 | **DP** | 三角形同樣是 DAG |
| **931** | Minimum Falling Path Sum | ↓ 三個方向 | 可加總和 | **DP** | 仍是 DAG（一列一列來） |
| **1289** | Min Falling Path Sum II | ↓ 任一行 | 可加總和 | **DP + 最小／次小值** | DAG + 每列的最佳化 |
| **174** | Dungeon Game | 只有 ↓→ | 可加，**但**血量要 ≥1 | **反向 DP** | 正向貪婪會失效 → 從終點往回推 |
| **1631** | Path With Minimum Effort | 四方向 | `max(diff)` | **Dijkstra** | 有環 + 不可加 → DP 做不到 |
| **778** | Swim in Rising Water | 四方向 | `max(height)` | **Dijkstra** | 有環 + 極小化極大成本 |
| **1091** | Shortest Path in Binary Matrix | 八方向 | 單位成本 | **BFS** | 權重全相等 → 單純 BFS 就夠 |
| **1293** | Shortest Path with Obstacle Elim. | 四方向 | 單位成本 + k 額度 | **BFS + 狀態** | `(r, c, k)` 三維狀態 |
| **2290** | Minimum Obstacle Removal | 四方向 | 成本 0 或 1 | **0-1 BFS / Dijkstra** | 0/1 權重下雙端佇列勝過堆積 |
| **1368** | Min Cost to Make Valid Path | 四方向 | 成本 0 或 1 | **0-1 BFS / Dijkstra** | 同樣的 0/1 權重技巧 |

**從這個題型家族歸納出來的判斷規則：**

<!--CODE-->

<!-- a69b4fa55533 -->
### 8) Minimum Obstacle Removal to Reach Corner — LC 2290 — 0-1 BFS


> 障礙格成本 1、空格成本 0；用 0-1 BFS（雙端佇列）或 Dijkstra 讓總成本最小。

<!--CODE-->

<!-- ab2a44da8413 -->
### 9) Minimum Cost to Make at Least One Valid Path in a Grid — LC 1368 — 0-1 BFS


> 上面幾張表都提到 LC 1368；這裡是 0-1 BFS **雙端佇列**版的完整實作
> （[8) LC 2290](#8-minimum-obstacle-removal-to-reach-corner--lc-2290--0-1-bfs) 示範的是優先佇列版）。

**核心想法**：每個格子都有**一條免費的出邊**（權重 `0`）— 就是箭頭指的那個方向 — 以及**三條要付錢的出邊**（權重 `1`，也就是把箭頭轉向的成本）。只有兩種相異權重時你不需要堆積：把權重 `0` 的鬆弛結果 `pushFront`、權重 `1` 的 `pushBack`，**雙端佇列**就會自己維持排序。

**為什麼可行**：雙端佇列在任何時刻最多只裝兩種距離值（`d` 和 `d+1`）。把權重 `0` 的鄰居推到前面，它就留在 `d` 這一段；把權重 `1` 的鄰居推到後面，它就落在 `d+1` 那一段 — 這正是優先佇列會產生的順序，但每次操作只要 `O(1)` 而不是 `O(log V)`。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**0-1 BFS vs Dijkstra — 什麼時候該把堆積換成雙端佇列**

| | Dijkstra（堆積） | 0-1 BFS（雙端佇列） |
|---|---|---|
| 邊權 | 任何非負值 | **只能是 0 和 1** |
| 邊界結構 | 最小堆積 | 雙端佇列 |
| 複雜度 | `O(E log V)` | `O(V + E)` |
| 推入規則 | `heappush(pq, (d, node))` | `w == 0 -> appendleft`，`w == 1 -> append` |
| LC 例題 | 743, 1631, 778 | **1368**, 2290 |

> ⚠️ **陷阱**：0-1 BFS *只有*在權重剛好是 `{0, 1}` 時才成立。權重變成 `{0, 1, 2}` 時，雙端佇列裡就會出現三段距離，
> 順序不變量直接破功 — 這時退回去用 Dijkstra。

---

<!-- fb75ce9c1687 -->
## 多源與隱式圖

<!-- 6dd2b7210051 -->
### 10) Trapping Rain Water II — LC 407 — 從邊界出發的多源搜尋


> 用最小堆積從邊界格子開始處理；某格接住的水 = max(邊界高度) - 該格高度。

<!--CODE-->

<!-- 0721e4ad8067 -->
### 11) 隱式圖上的最佳優先搜尋 — LC 373


> **模式**：圖從頭到尾都沒有被建出來的 Dijkstra。「節點」是索引 tuple、「邊」是*後繼規則*、
> 「距離」就是值本身。因為每個後繼狀態都 `>=` 它的父狀態，key 是**單調非遞減**的 — 而這正是
> Dijkstra「第一次 pop 就定案」成立的條件。

**核心想法**：把 `for neighbor in graph[u]` 換成 `for successor in nextStates(u)`。其他部分 — 最小堆積、
用 `visited`/`seen` 去重、每次 pop 最小值的迴圈 — 都是原封不動的 Dijkstra。

<!--CODE-->

**實作範例 — LC 373 Find K Pairs with Smallest Sums。**
狀態 = `(i, j)` 索引對；`(i, j)` 的後繼是 `(i+1, j)` 和 `(i, j+1)`；key = `nums1[i] + nums2[j]`。
兩個陣列都已排序，所以任何後繼的總和都 `>=` 父狀態的總和 — 剛好滿足 Dijkstra 要的單調 key。

<!--CODE-->

<!--CODE-->

<!-- 208b3c756560 -->
#### **變形 A — LC 378 Kth Smallest Element in a Sorted Matrix**

> 變化點：一樣是在 `(r, c)` 網格上走，但 key 直接就是 `matrix[r][c]`，而且我們只要第 `k` 次 pop 的結果，不用整個清單。

<!--CODE-->

<!--CODE-->

> 註：LC 378 還有一個 `O(n log(max-min))` 的**對答案二分搜尋**解法，當 `k ~ n^2` 時比這裡快。
> 但堆積版才是這裡值得記住的，因為它*根本就是跟 LC 373 一模一樣的程式形狀*。

<!-- 57292999960e -->
#### **變形 B — LC 264 Ugly Number II**

> 變化點：狀態就是**值本身**（不是索引 tuple），後繼是 `v*2, v*3, v*5`。
> 這說明「隱式圖」不一定是網格 — 任何單調的後繼規則都行。

<!--CODE-->

<!--CODE-->

<!-- e16a7ab819cd -->
#### **家族總結**

| LC # | 狀態 | 後繼規則 | key（也就是「距離」） |
|------|-------|----------------|----------------------|
| **373** | `(i, j)` 索引對 | `(i+1, j)`, `(i, j+1)` | `nums1[i] + nums2[j]` |
| **378** | `(r, c)` 格子 | `(r+1, c)`, `(r, c+1)` | `matrix[r][c]` |
| **264** | 值 `v` | `2v`, `3v`, `5v` | `v` |

**面試中怎麼認出這個模式**

1. 題目要一個大到無法全部列舉的集合裡的**第 k 小／前 k 小**。
2. 每個後繼的 key 都 `>=` 目前的 key（**單調** — 沒有「負權邊」）。
3. 多個父狀態可能生出同一個狀態 → 你**必須**用 `seen` 集合對狀態去重，否則堆積會被重複項目撐爆
   （這正是 `dist[]`／`visited` 在 Dijkstra 裡扮演的角色）。

---
