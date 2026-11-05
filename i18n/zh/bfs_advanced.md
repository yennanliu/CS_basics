<!-- e232285a0db9 -->
# BFS — 進階變形

> **範圍** — 第一輪可以先跳過的 BFS 技巧：雙向 BFS、搭配 deque 的 0-1 BFS、超出標準模板的多源 BFS、在隱式或擴充狀態空間上的 BFS，以及列舉所有最短路徑的 DAG —— 必會的佇列模板留在主文件。
> **另見**：[bfs.md](./bfs.md) — 這些變形所依據的模板；[bfs_examples.md](./bfs_examples.md) — 經典 BFS 題目，每題一張 card；[Dijkstra.md](./Dijkstra.md) — 邊帶有任意權重時。

<!-- 8d2e813a1836 -->
## LeetCode 題目清單

- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)

<!-- 25a80cc9ef9b -->
## 總覽

這裡每一項都是 [bfs.md](./bfs.md) 裡某個模板的變形：佇列裝的東西變了、佇列變成 deque、圖變成隱式的，
或一次 BFS 變成很多次。大部分 L3 不會考。會考的 —— 擴充狀態（Pattern 16）和 0-1 BFS（Pattern 15）
—— 通常是格子題上的 Medium/Hard 轉折。`Pattern N` 的名稱就是 [bfs.md](./bfs.md) 引用的那些。

| 變形 | 推入／內容規則 | 成本 |
|---|---|---|
| 多源（標準版：[bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-)） | 所有來源都放在第 0 層 | O(V + E) |
| 各自獨立的 BFS（Pattern 4.6） | 每個來源用全新的 `visited` | O(k(V + E)) |
| 擴充狀態（Pattern 16） | `visited` 以 `(node, resource)` 為鍵 | O(V · \|resource\|) |
| 0-1 BFS（Pattern 15） | deque：成本 0 放前端、成本 1 放後端 | O(V + E) |
| 雙向 | 擴展比較小的那一邊前緣 | ~O(b^(d/2)) |

<!-- a5275cd2566e -->
### 先備知識

- [bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-)：入列時標記的圖 BFS。下面每個 pattern 都維持這個不變量。
- [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-)：多源 BFS。Pattern 4.5 和 4.6 是它的延伸。
- [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-)：即時產生鄰居的 BFS。Pattern 8、8.5、12 和雙向搜尋是它的延伸。

<!-- 8847461e80ed -->
## 辨識

| 題目敘述中的訊號 | 限制條件提示 | 該用 | 不適用時 |
|---|---|---|---|
| 「最短的橋」、用最少翻轉把兩個連通塊接起來 | 格子 ≤ 100 × 100 | Pattern 4.5 — 標出一個連通塊，從它全部格子做多源 BFS | 只有一個起點格 → 一般的 [bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) |
| 「到所有建築物的距離**總和**」、「每個來源都走得到」 | k 個來源 × 格子 ≤ 10⁶ | Pattern 4.6 — 每個來源跑一次 BFS，各用全新的 `visited` | 「到**最近**來源的距離」→ 一次多源 BFS |
| 「最少搭幾班公車／轉幾次車／幾條線」 | 路線 ≤ 500 | Pattern 8 — 以路線為節點的 BFS | 數的是站數而不是轉乘 → 以站為節點的 BFS |
| 「回傳**所有**最短轉換序列」 | 單字表 ≤ 500 | Pattern 8.5 — BFS 建前驅 DAG，DFS 列舉 | 只要最短長度 → [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) |
| 「所有組合」，從 k 個互相獨立的群組各選一個 | 輸出大小 ≤ 10⁴ | Pattern 9 — 一個群組一層 | 選擇之間互相限制 → 回溯 |
| 「移除**最少**幾個字元讓它合法」，並列出所有答案 | n ≤ 25 | Pattern 12 — 一層刪一個，停在第一個有結果的層 | 只問個數 → 貪心計數器 |
| 「收集所有鑰匙」、「最多移除 k 個障礙物」、「最多 k 站」 | m·n·\|resource\| ≤ 10⁶ | Pattern 16 — `visited` 以 `(cell, resource)` 為鍵 | 資源不影響之後的移動 → 一般格子 BFS |
| 移動成本只有 0 或 1（「順著箭頭免費」、「改一個標誌花 1」） | 格子 ≤ 10⁴ 格 | Pattern 15 — 搭配 deque 的 0-1 BFS | 權重大於 1 → [Dijkstra.md](./Dijkstra.md) |
| 兩個端點都已知、分支因子很大 | 單字表 ≤ 5,000 | 雙向 BFS | 只有一個端點，或目標集合未知 → 單向 BFS |
| 必須照固定順序拜訪目標（最矮的樹先砍） | 目標數 × 格子 ≤ 10⁶ | Pattern 6 — 排序，再對每對相鄰目標各跑一次 BFS | 順序不限 → 在距離矩陣上做 TSP |

<!-- f82fbb540584 -->
## 多源 BFS — 深入探討

標準的多源 BFS 與它的距離變形 LC 542 01 Matrix，在
[bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) 與它的
[01 Matrix card](./bfs.md#01-matrix--lc-542) 裡解過。下面兩個 pattern 是那個模板自己做不到的：
從一個得先找出來的連通塊出發，以及讓各個來源彼此分開。

<!-- 8bed4f9dd52f -->
### Pattern 4.5：DFS + 多源 BFS（島嶼擴張）— LC 934

> **關鍵** — 兩座島之間最短的橋，一定從第一座島的*某一格*出發，所以把它的**所有**格子都當 BFS
> 起點。先 flood fill 第一座島（標成 `2` 並把每格入列），再往水面向外 BFS；BFS 第一次碰到 `1` 的
> 那一層，就是要翻的格子數。

**歸約到**：[bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) — 起點是先用 flood fill 找出來的整個連通塊。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 只用 A 島的一格當 BFS 起點，找到的是從那一格出發的*某座*橋，不是最短的。
- 在 100 × 100 的島上做遞迴 fill 會有 10⁴ 層深：超過 Python 預設的遞迴上限，所以 Python 版用 stack 來 fill。

**相似題** — LC 1162 As Far from Land as Possible · LC 542 · LC 1020 Number of Enclaves

<!-- a56a3153ab2e -->
### Pattern 4.6：多源 BFS vs 各自獨立的 BFS（關鍵區別）

**關鍵** — 一次共用、以所有來源為起點的 BFS，回答的是「到**最近**來源的距離」，因為最先抵達某格
的來源獲勝，那格就關閉了。問的是**每一個**來源 —— 距離**總和**，或「每個來源都走得到」—— 就需要
每個來源到每格各自的距離，所以是每個來源跑一次 BFS，每次都用**全新**的 `visited`。

| 面向 | 同時進行的多源 | 各自獨立的 BFS |
|---|---|---|
| 佇列初始化 | 一次放入所有來源 | 每個來源自己開一次 BFS |
| `visited` | 一份，共用 | 每次都是全新的 |
| 時間 | O(m·n) | O(k · m·n)，k = 來源數 |
| 第一次到達代表 | 到最近來源的距離 | 到目前這個來源的距離 |
| 例題 | LC 542、994、1162 | LC 317 |

獨立版本，用在 LC 317 Shortest Distance from All Buildings（讓走到每棟建築物的**總**距離最小）：

<!--CODE-->

<!--CODE-->

**陷阱**
- 在各建築物之間共用一份 `visited`：第二棟建築物看不到第一棟已關閉的格子，它的距離永遠加不進去，
  所以答案是錯的，而不只是慢。
- 有某棟建築物走不到的空地不是候選：要檢查 `reach == buildings`。
- 省空間的技巧是拿格子本身取代 `visited`：第 i 次 BFS 只走值為 `-i` 的格子並寫成 `-(i + 1)`，
  所以之後的 BFS 都會跳過先前任一棟走不到的格子。答案相同，不用 `visited` 陣列。

**相似題** — LC 2045 · LC 296 Best Meeting Point（曼哈頓距離，不需要 BFS）

<!-- fb77b43e121e -->
## 狀態空間與隱式圖上的 BFS

標準的狀態空間 BFS —— 產生每個鄰居、拿去查字典、再把字元還原 —— 是
[bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-)，
在它的錨定題 LC 127 上解過，還原那一步寫在它的陷阱裡。它的經典題（LC 752、LC 773、LC 279）在
[bfs_examples.md](./bfs_examples.md) 各有一張 card。下面的 pattern 改變的是*節點是什麼*。

<!-- 465ed0f5d5f7 -->
### Pattern 8：抽象圖上的 BFS（以路線為節點）— LC 815

> **關鍵** — 答案數的是**公車**，所以讓每條路線當一個節點：兩條路線共用某個站就相鄰。把經過
> `source` 的每條路線都當起點；BFS 層數 = 搭過的公車數，第一條含有 `target` 的路線就結束搜尋。
> 對站做的 `visited` 讓繁忙的轉乘站不會把經過它的每條路線重新展開一次。

**歸約到**：[bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-) — 在以路線為節點的圖上，這張圖從「站 → 路線」對應表建出來。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 以站為節點、同一路線上每兩站之間都連邊的 BFS，每條路線是 O(L²)，而且數的是站不是公車。
- `source == target` 是 0 班公車，即使沒有任何路線經過它。
- 沒有 `seen_stops`，位在很多路線上的轉運站，每被一條路線走到就要重掃一次它的路線清單。

**相似題** — LC 127（以單字為節點） · LC 841 Keys and Rooms · LC 1197 Minimum Knight Moves

---

<!-- 0f9c815cad25 -->
### Pattern 8.5：BFS + DFS（找出所有最短路徑 — DAG 列舉）— LC 126

> **關鍵** — 逐層 BFS，並為每個單字記下它在上一層的**所有**前驅。一整層做完之後才把那一層從字典
> 移除，這樣同一層被兩個父節點走到的單字兩個都會保留。在走到終點單字的那一層之後就停；再從
> 前驅清單往回 DFS，清單裡只有最短路徑。

**歸約到**：[bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — 記錄的是前驅 DAG，而不只是一個距離。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 單字一被第一次走到就從字典移除，會丟掉它在同一層的第二個父節點（上面的 `cog`），連帶丟掉經過
  `log` 的所有路徑。
- 從 `beginWord` 在整張單字圖上往前 DFS，會探索比最短還長的路徑而超時；要沿前驅清單往回走。
- `found` 之後還建下一層，會讓較長的路徑混進 `parents`。

**相似題** — LC 127（只要一個最短長度） · LC 2115 Find All Possible Recipes

---

<!-- 004d33dd34c9 -->
### Pattern 9：BFS 式的笛卡兒積生成（逐層組合建構）— LC 1087

> **關鍵** — 當輸出是從 k 個互相獨立的群組各選一個時，每個群組就是一層 BFS：把第 i 層清空、並把
> 群組 i 的每個選項接到每個前綴後面，留下的正好是群組 `0 … i` 的笛卡兒積。沒有圖、沒有 `visited`；
> 從 BFS 借來的只有分層結構。

**歸約到**：[bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — 一個群組一層，節點的子節點是把它的前綴接上每個選項。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 展開前先把每個群組排序；事後才排序輸出要多付 O(|result| log |result|)。
- 最後一層一次裝著整個輸出；當選擇之間互相限制（不能重複字母、總和上限）時改用回溯，它能剪枝，
  而且只需要 O(depth) 記憶體。

**相似題** — LC 17 Letter Combinations of a Phone Number · LC 784 Letter Case Permutation · LC 320

---

<!-- bf6a1385cc11 -->
### Pattern 12：在字串狀態上做 BFS — 停在第一個有結果的層 — LC 301 ⭐⭐⭐⭐⭐

> **關鍵** — 「移除**最少**幾個字元」讓一次刪除 = 一層 BFS：第 k 層裝的是恰好刪 k 個字元能到達的
> 所有字串。第一個含有合法字串的層就是答案層 —— 回傳該層所有合法字串，絕不建下一層。

**歸約到**：[bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — 鄰居是刪掉一個括號後的字串。

**為什麼這裡 BFS 勝過 DFS**：DFS 能找到「某個」合法字串，但你還得證明它是最少的；BFS 直接從層數免費得到最小性，而且一次就回傳該長度下的**所有**答案。

<!--CODE-->

<!--CODE-->

**陷阱**
- 不去重的話，`"(())"` 可以由很多種刪除順序到達，層會爆掉。
- 找到合法字串後就停止**擴展**，但該層要掃完：同樣長度可能有好幾個答案。
- `")("` 沒有非空的合法答案；迴圈會走到空字串，它是合法的，所以回傳 `[""]`。

---

<!-- c04871af3b88 -->
### Pattern 16：在擴增狀態上做 BFS — `(cell, resource)` — LC 864 & LC 1293 ⭐⭐⭐⭐⭐

> **關鍵** — 一般的格子 BFS 以**格子**當 `visited` 的鍵。只有在「抵達某格之後，未來的一切都相同」
> 時這才正確。一旦你在行走途中攜帶了某樣東西 —— 收集到的鑰匙、剩餘的消除次數、燃料、顏色、
> 奇偶性 —— 兩次抵達同一格就是搜尋中**不同的位置**，而修正方式永遠是同一行。

**歸約到**：[bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — 把資源加進 `visited` 的鍵。

<!--CODE-->

其餘部分仍是教科書式的佇列 BFS，所以第一次彈出終點時，你拿到的依然是最短路徑。代價在於狀態數量：`m * n * |resource|`。

| 題目敘述中的訊號 | 額外的維度 |
|---|---|
| 「收集鑰匙，門需要對應的鑰匙」（LC 864） | 鑰匙位元遮罩，`2^k` 種值 |
| 「最多可以移除 `k` 個障礙物」（LC 1293） | **剩餘**消除次數，`k+1` 種值 |
| 「最多 `k` 站／`k` 條邊」（LC 787） | 目前已使用的邊數 |
| 「最多可以反轉一條邊」 | 一個 0/1 旗標 |
| 「移動在兩位玩家／兩種顏色之間交替」（LC 1129） | 上一次使用的顏色 |

> **為什麼 `k` 必須是鍵的一部分，而不是 `best[r][c]` 純量** — 以*更多*剩餘預算抵達某格絕不會更差，所以當新的抵達擁有嚴格更多預算時，這一格就值得再訪。只以格子為鍵會把這次抵達丟掉，並在可解的網格上回報 `-1`。以 `(cell, k)` 為鍵永遠正確；`best[r][c] = 見過的最大預算` 是同一件事的壓縮版，也是常見的記憶體最佳化。

<!-- a9ff680f64d5 -->
#### LC 864 — 資源是鑰匙的位元遮罩

> **關鍵** — 小寫 `a..f` 是鑰匙、大寫 `A..F` 是鎖；`k ≤ 6`，所以鑰匙串用 6 個位元就放得下，狀態
> 空間是 `m * n * 64`。只有遮罩裡已經有那個位元時鎖才能通過；答案是第一次彈出時手上已經有全部鑰匙。

**歸約到**：[bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — `visited` 以 `(r, c, mask)` 為鍵。

<!--CODE-->

<!--CODE-->

<!-- 60a53f492c2d -->
#### LC 1293 — 資源是倒數計數

> **關鍵** — 額外的維度是「我還能移除幾個障礙物」。只有在比先前任何一次抵達該格的預算**嚴格更多**
> 時，這次抵達才值得保留，所以用 `best[r][c]` 記下見過的最大預算，取代三維 `visited`。

**歸約到**：[bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — 預算放在佇列元素裡一起帶著走。

有兩件事值得帶進面試：

- **捷徑。** 若 `k >= m + n - 2`，你可以直接剷平一路穿過去，答案就是曼哈頓距離 `m + n - 2`，這個分支以 `O(1)` 回傳。但它的價值不只是一個提早結束：過了這關，所有剩下的輸入都滿足 `k < m + n - 2`，所以上界是 `O(m*n*(m+n))`，而不會隨著無上限的 `k` 成長。
- **用 `best[r][c]` 取代三維 `visited`。** 在每格儲存曾見過的最大剩餘預算，並跳過任何沒有嚴格更好的抵達。答案相同，記憶體 `O(m*n)`。

<!--CODE-->

<!--CODE-->

**陷阱**

- 在**格子**上標記 `seen` — LC 864 就會在「撿到鑰匙後必須穿越自己先前路線」時回報 `-1`，而測資大多是這種情況。
- 把已經持有的鑰匙算成新狀態 — 無害但會讓佇列加倍；`nMask == mask` 反正會被 `seen` 檢查擋下。
- 在 LC 1293 中，在**進入**的格子檢查預算，卻在**離開**的格子扣預算。單獨採用任一種慣例都正確 —— 兩個端點都是 `0`，所以一條路徑付費的障礙物相同 —— 但混用兩者，會讓一整串相鄰的障礙物只用 1 的預算就通過。

**相似題目**：LC 787 Cheapest Flights Within K Stops（`(node, stops)`；有權重，所以用 Dijkstra 或 Bellman-Ford）、LC 1928 Minimum Cost to Reach Destination in Time（`(node, time)`）、LC 1129 Shortest Path with Alternating Colors（`(node, lastColour)`）、LC 847 Shortest Path Visiting All Nodes（`(node, visitedMask)` — 見 [dp_bitmask.md](./dp_bitmask.md)）。當額外的維度讓邊變成*有權重*時，同樣的狀態改放進堆積而不是佇列 — [Dijkstra.md](./Dijkstra.md)。

---

<!-- de3f9635383b -->
## 0-1 BFS 與雙向 BFS

兩種改變的是佇列、而不是節點的 BFS 形狀。邊帶有任意非負權重就根本不是 BFS 了：用堆積
（[Dijkstra.md](./Dijkstra.md)）。沿路徑帶著一個值 —— 例如 LC 399 Evaluate Division 的比值乘積 ——
不需要新技巧：那是 [dfs § Template 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399)，
帶著 `(node, product)` 的 BFS 給出一樣的答案。

<!-- ef736ec509cf -->
### Pattern 15：搭配 deque 的 0-1 BFS — LC 1368 ⭐⭐⭐⭐

> **關鍵** — 當每條邊的成本只有 **0 或 1**，用 deque 取代 Dijkstra 的堆積：成本 0 的邊放前端
> （同一層），成本 1 的邊放後端（下一層）。deque 裡最多只有兩種距離值，而且有序，所以節點第一次
> 被取出時的距離就是最終的 —— 用 **O(V + E)** 拿到 Dijkstra 的保證，而不是 `O(E log V)`。

**歸約到**：[bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — 改用 deque，並用 `dist` 鬆弛取代入列時標記。

**LC 1368**：格子告訴你每一格的「免費」方向。順著箭頭走成本為 `0`；其餘 3 個方向各成本 `1`（改一個標誌）。

<!--CODE-->

<!--CODE-->

**BFS vs 0-1 BFS vs Dijkstra**

| 邊權重 | 資料結構 | 推入規則 | 時間 |
|---|---|---|---|
| 全為 1 | 佇列 | 一律放後端 | O(V + E) |
| 0 或 1 | **Deque** | 0 → 前端、1 → 後端 | O(V + E) |
| 任意 ≥ 0 | PriorityQueue | 依距離排序 | O(E log V) |

**陷阱**
- 要用 `dist[r][c] + cost < dist[nr][nc]` 鬆弛，不能用 `visited` 集合：第一次經由成本 1 的邊抵達的
  節點，之後可能經由成本 0 的邊以更低成本抵達。
- 每條邊都推到後端就退化成普通 BFS，數的是步數，不是改了幾個標誌。

**相似的 0-1 BFS 題目**：LC 2290 Minimum Obstacle Removal to Reach Corner · LC 1263 Minimum
Moves to Move a Box to Their Target Location（推箱子成本 1、玩家自己走動成本 0 — 狀態是
`(box, player)`）。

<!-- e15cafa75ab1 -->
### 雙向 BFS

> **關鍵** — 兩個端點都已知時，從兩端同時搜尋，前緣相遇就停。分支因子 b、距離 d 時，兩個深度
> d/2 的搜尋大約拜訪 `2·b^(d/2)` 個節點，而不是 `b^d`。永遠擴展**比較小**的那一邊，而且一次擴展
> 一整層，這樣第一次相遇就是最短長度。

**歸約到**：[bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — 在 LC 127 Word Ladder 上從兩端跑。

<!--CODE-->

**陷阱**
- 只在單字被*彈出*時檢查相遇（而不是產生時）可能多走一層。
- 一個節點一個節點地擴展（而不是一層一層），可能先在較長的路徑上相遇。
- 終點未知時（任一葉節點、任一扇門），沒有東西可以往回搜：用單向 BFS。

<!-- bbbdb68ca9e5 -->
## 重複與連續執行的 BFS

<!-- 27feb46b0264 -->
### Pattern 6：排序 + 重複 BFS（連續最短路徑）— LC 675

> **關鍵** — 拜訪順序是固定的（最矮的樹先砍），所以總步數是相鄰目標之間最短路徑的和。把目標排序，
> 再對每對相鄰目標各跑一次普通的格子 BFS；任何一對走不到，整個答案就是 -1。

**歸約到**：[bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — 對每對相鄰目標呼叫一次。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- `(0, 0)` 上可能就有樹；第一次 BFS 回傳 0，而不是失敗。
- 砍掉的樹變成 `1`（可通行），永遠不是 `0`；每次 BFS 都要全新的 `visited`。
- 用曼哈頓距離當啟發的 A* 能加快每次搜尋，但不改變上界。

**相似題** — LC 317（每個來源一次 BFS） · LC 1091

<!-- 4b48354c48ef -->
## 不適用時

| 技巧 | 通常就夠用的、更簡單的核心模板 |
|---|---|
| Pattern 4.5（先 fill，再多源 BFS） | 只有一個起點格 → [bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) |
| Pattern 4.6（各自獨立的 BFS） | 問的是**最近**的來源 → 一次[多源 BFS](./bfs.md#template-4-multi-source-bfs--lc-994-)；每個來源各跑一次答案一樣但慢 k 倍 |
| Pattern 8（以路線為節點） | 答案數的是站或邊，而不是路線 → [bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-) |
| Pattern 8.5（前驅 DAG） | 只問長度或一條路徑 → [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-)，路徑再加一個父節點表 |
| Pattern 9（笛卡兒積 BFS） | 選擇之間互相限制，或在意記憶體 → 回溯（[dfs § Template 4](./dfs.md#template-4-backtracking--lc-46)） |
| Pattern 12（一層刪一個） | 只問要刪**幾個** → 一次從左到右的平衡計數器，O(n) |
| Pattern 16（擴充狀態） | 帶著的值從不改變哪些移動合法 → 一般格子 BFS；它只是答案的一部分 |
| Pattern 15（0-1 BFS） | 每步成本都一樣 → 普通佇列；成本不只 0 和 1 → [Dijkstra.md](./Dijkstra.md) |
| 雙向 BFS | 圖很小，或目標是一個集合或條件 → 單向 BFS；L3 很少需要它才能過 |
| Pattern 6（排序後重複 BFS） | 目標可以任意順序拜訪 → 那是 TSP 式的搜尋，不是這個 |
| 葉節點之間的樹上距離（LC 1530） | 回傳各深度葉節點數的後序 DFS 是 O(n)，不需要建圖（[dfs_advanced.md](./dfs_advanced.md)） |

<!-- 536fee6ceeae -->
## 總結

| 題目中的訊號 | 變形 | 章節 |
|---|---|---|
| 「到**最近**來源的距離」vs「到所有來源的距離**總和**」 | 一份共用的 `visited` vs 每個來源一份全新的 `visited` | Pattern 4.6 |
| 兩個連通塊，用最少翻轉把它們接起來 | flood fill 一個連通塊，再從它全部格子向外做多源 BFS | Pattern 4.5 |
| 「最少**轉乘**／公車／路線」 | 節點是**路線**而不是站的 BFS | Pattern 8 |
| 「回傳**所有**最短序列」 | BFS 建前驅 DAG，DFS 列舉它 | Pattern 8.5 |
| 「最少移除幾個讓它合法」 | 一層 BFS = 一次移除；停在第一個有結果的層 | Pattern 12 |
| 從互相獨立的群組列舉所有組合 | 一個群組一層 BFS（笛卡兒積） | Pattern 9 |
| 鑰匙、預算或顏色會改變哪些移動合法 | `visited` 以 `(cell, resource)` 為鍵 | Pattern 16 |
| 每條邊成本只有 **0 或 1** | deque：成本 0 放前端、成本 1 放後端 | Pattern 15 |
| 兩個端點都已知且路徑很長 | 從兩端各擴展比較小的那一邊 | 雙向 BFS |
| 必須照**固定順序**拜訪目標 | 排序，再對每對相鄰目標各跑一次 BFS | Pattern 6 |

這些所依據的必會佇列模板在 [bfs.md](./bfs.md)；經典題目在 [bfs_examples.md](./bfs_examples.md)。
