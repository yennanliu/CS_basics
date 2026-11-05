<!-- bd2f36166f98 -->
# BFS — 實戰題解

> **範圍** — BFS 的題目集：每一題經典的逐層、多源、狀態空間與「把樹當成圖」的題目各一張 problem card，各自歸在它所歸約到的 [bfs.md](./bfs.md) 模板之下 —— 這裡不教任何自己的模板。
> **另見**：[bfs.md](./bfs.md) — 這裡每張 card 所歸約到的模板；[bfs_advanced.md](./bfs_advanced.md) — 比較少見的 BFS 變形（雙向、0-1 雙端佇列、以路線為節點、列舉所有最短路徑、擴充狀態）。

<!-- c78b67f28ffa -->
## LeetCode 題目清單

- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

<!-- 0664940a07b6 -->
## 總覽

經典的 BFS 題目，依每題所歸約到的 [bfs.md](./bfs.md) 模板分組，順序和那份文件的模板順序相同。
每張 card 只講它在模板之上多加的那一步；模板本身一點就到。在別份文件裡解過的題目，這裡只在
Index 留一列指標。Template 3（帶方向陣列的格子 BFS）在這裡沒有自己的 card：它的格子題不是從
很多格同時出發（Template 4），就是錨定題 LC 1091 本身。

<!-- 23e39ab76a11 -->
## 索引

| LC | 題目 | 歸約到 | 難度 |
|---|---|---|---|
| 116 / 117 | [Populating Next Right Pointers in Each Node](#populating-next-right-pointers-in-each-node--lc-116--117) | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | Medium |
| 958 | [Check Completeness of a Binary Tree](#check-completeness-of-a-binary-tree--lc-958) | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | Medium |
| 623 | [Add One Row to Tree](#add-one-row-to-tree--lc-623) | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | Medium |
| 662 | [Maximum Width of Binary Tree](#maximum-width-of-binary-tree--lc-662) | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | Medium |
| 909 | [Snakes and Ladders](#snakes-and-ladders--lc-909) | [bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-) | Medium |
| 207 | Course Schedule | [graph § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-) — 在那裡解 | Medium |
| 286 | [Walls and Gates](#walls-and-gates--lc-286) | [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) | Medium |
| 130 | [Surrounded Regions](#surrounded-regions--lc-130) | [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) | Medium |
| 994 | Rotting Oranges | [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) — 錨定題，在那裡解 | Medium |
| 417 | Pacific Atlantic Water Flow | 在 [dfs_examples.md](./dfs_examples.md) 解 | Medium |
| 310 | Minimum Height Trees | 在 [topology_sorting_examples.md](./topology_sorting_examples.md) 解（剝葉子） | Medium |
| 752 | [Open the Lock](#open-the-lock--lc-752) | [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) | Medium |
| 279 | [Perfect Squares](#perfect-squares--lc-279) | [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) | Medium |
| 773 | [Sliding Puzzle](#sliding-puzzle--lc-773) | [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) | Hard |
| 742 | [Closest Leaf in a Binary Tree](#closest-leaf-in-a-binary-tree--lc-742) | [bfs § Template 6](./bfs.md#template-6-parent-map--bfs-radiating-from-a-target--lc-863-) | Medium |
| 863 | All Nodes Distance K in Binary Tree | [bfs § Template 6](./bfs.md#template-6-parent-map--bfs-radiating-from-a-target--lc-863-) — 錨定題，在那裡解 | Medium |

<!-- 5af2ef82078d -->
## 逐層 BFS 的題目

<!-- 3513cef84f17 -->
### Populating Next Right Pointers in Each Node — LC 116 / 117

> **關鍵** — `next` 是「同一層裡在我右邊的節點」，正好就是逐層 BFS 彈出一層的順序：把每個彈出的
> 節點接到下一個彈出的節點，一層的最後一個節點維持 `next = None`。完全沒用到樹的形狀，所以同一份
> 程式碼同時解 LC 116（完美二元樹）與 LC 117（任意樹）。

**歸約到**：[bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — 在一層之內多帶一個 `prev` 指標。

<!--CODE-->

追問要 O(1) 額外空間。在**完美**二元樹（LC 116）上，剛接好的那一層本身就是一條鏈結串列，所以
順著 `next` 走它，就能接好下一層 —— 複雜度不同，所以保留：

<!--CODE-->

**陷阱**
- O(1) 版本假設每個節點都有兩個子節點；在 LC 117 上會去存取不存在的子節點。LC 117 的 O(1) 版本
  要改成每層一個 dummy head。
- `prev` 每一層開頭都要重設，否則上一層的最後一個節點會接到下一層的第一個節點。

**相似題** — LC 199（每層最後一個節點） · LC 102（基本迴圈）

<!-- 8255d4da9400 -->
### Check Completeness of a Binary Tree — LC 958

> **關鍵** — 連 `None` 子節點也入列。佇列這時讀出來就是樹的陣列表示法，而完全二元樹的所有 `None`
> 都在尾端：一旦彈出過 `None`，之後再出現任何真的節點就代表中間有洞。

**歸約到**：[bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — 不需要逐層迴圈，而且 `None` 子節點留在佇列裡。

<!--CODE-->

**陷阱**
- 只檢查「除了最後一層都是滿的」會漏掉 `[1,2,3,4,5,null,7]`：最後一層中間有洞。
- 逐層序列化（LC 297）寫出 `null` 標記也是同樣的理由；這裡就是把那個佇列讀回來。

**相似題** — LC 297（用 `null` 標記序列化） · LC 222（計算完全二元樹的節點數）

<!-- ecfe13a8587d -->
### Add One Row to Tree — LC 623

> **關鍵** — 要改的是第 `depth - 1` 層，也就是子指標會變的那個**父層**。逐層 BFS 走到佇列剛好裝著
> 那一層，就幫其中每個節點掛兩個新子節點，並把舊的子樹掛在**外側**。`depth == 1` 沒有父層，所以
> 新節點直接當根。

**歸約到**：[bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — 停在某一層把它重接，而不是繼續往下走。

<!--CODE-->

帶著深度往下走的 DFS 比較短，空間是 O(h) 而不是 O(w) —— 因為這點而保留：

<!--CODE-->

**陷阱**
- 停在 `cur_depth == depth` 已經晚了一層：要改的指標在父層上。
- 把舊子樹掛在內側（`new_left.right = old_left`）會讓子樹左右鏡像。
- 重接之後還繼續 BFS，會走進新建的 `val` 節點。
- 歪斜樹上 `depth` 可達 10⁴，超過 Python 預設的遞迴上限；BFS 寫法沒有這個限制。

**相似題** — LC 199（同樣的逐層迴圈） · LC 226（先存再換子指標）

<!-- 3f65cc2022f4 -->
### Maximum Width of Binary Tree — LC 662

> **關鍵** — 給每個節點它的 heap 索引（`i` 的子節點是 `2i` 與 `2i + 1`）；一層的寬度是
> `last - first + 1`，這樣就把 `None` 的空隙算進去卻不用存它們。每一層的索引都要減掉該層第一個
> 索引，否則深度約 60 的歪斜樹會讓 `int` 溢位。

**歸約到**：[bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — 佇列裡放 `(node, index)`。

<!--CODE-->

<!--CODE-->

前序 DFS 不需要佇列：它最先碰到每一層最左邊的節點，所以記住那個索引，之後同一層的節點都從它
量起 —— 空間是 O(h) 而不是 O(w)：

<!--CODE-->

**陷阱**
- 沒有重新歸零，Java 在 3,000 個節點的鋸齒鏈上會溢位；Python 不會溢位，但整數會長到幾千個位元。
  DFS 寫法沒辦法每層歸零，所以在 Java 裡要用 `long`，或減掉該深度的第一個索引。
- 只數每層非空節點，得到的是該層的節點數，不是寬度。

**相似題** — LC 958（同樣用 heap 索引看樹） · LC 102

<!-- 695b4c939044 -->
## 圖 BFS 的題目

<!-- 5ca41e38698e -->
### Snakes and Ladders — LC 909

> **關鍵** — 棋盤就是格子 `1 … n²` 上的一張圖：從格子 `s` 各有一條邊連到 `s + 1 … s + 6`，若落點
> 有蛇或梯子就改道。每擲一次都是一步，所以普通 BFS 給出最少擲骰次數。唯一的功夫是把格子編號轉成
> 蛇行排列的 `(row, col)`。

**歸約到**：[bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-) — 邊由骰子算出來，而不是來自鄰接串列。

<!--CODE-->

<!--CODE-->

**陷阱**
- 要標記的是走完蛇或梯子之後**停下來**的格子，不是骰子顯示的格子；梯子的起點不是你會停留的地方。
- 每擲一次只走一段蛇或梯子：落在一條梯子的終點、而那裡剛好是另一條梯子的起點，並不會連著走。

**相似題** — LC 1971（模板的錨定題） · LC 1306（把 jump game 當成圖）

<!-- 8b1ada0b3e68 -->
## 多源 BFS 的題目

<!-- 1b2593ecdb84 -->
### Walls and Gates — LC 286

> **關鍵** — 「每個房間到**最近**的門的距離」就是一次 BFS，把每扇門都放在距離 0 當起點。最先抵達
> 某個房間的門走的是最短路徑，所以房間在第一次入列時就寫好；`INF` 同時代表「還沒到過」。

**歸約到**：[bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) — 距離直接存在格子裡。

<!--CODE-->

<!--CODE-->

**陷阱**
- 每扇門各跑一次 BFS 再取最小值是對的，但慢了 k 倍。
- 牆是 `-1`，永遠不是 `INF`，所以 `== INF` 的判斷已經跳過牆和每一扇門。

**相似題** — LC 542（最近的 0，在 [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) 解） · LC 1162 · LC 994

<!-- 3f3d1f3383eb -->
### Surrounded Regions — LC 130

> **關鍵** — 把問題反過來。一個 `'O'` 會留下來，若且唯若它和邊界上的某個 `'O'` 相連，所以用每個
> 邊界 `'O'` 當 BFS 起點，把走得到的都標成安全，再把沒走到的 `'O'` 全部翻掉。

**歸約到**：[bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) — 起點是邊界上的格子，而且只保留可達性。

<!--CODE-->

<!--CODE-->

**陷阱**
- 對每個內部區域做 flood fill 再檢查有沒有碰到邊界也行，但猜錯的區域要復原；從邊界出發就不用猜。
- 需要第三種標記（`'S'`）：把連到邊界的格子直接翻回 `'O'`，會和還沒走訪的格子分不出來。

**相似題** — LC 1020 Number of Enclaves · LC 1254（DFS 寫法，[dfs § Template 7](./dfs.md#template-7-2-pass-dfs-boundary-elimination--lc-1254)） · LC 417

<!-- 0caff9c8e9fd -->
## 即時產生鄰居的 BFS 題目

<!-- fd9230cd42b5 -->
### Open the Lock — LC 752

> **關鍵** — 每個 4 位數組合是一個節點，鄰居是轉動一個轉輪一格得到的 8 個組合，需要時才產生。
> deadend 是從圖上拿掉的節點。BFS 的層數就是轉動次數，所以目標第一次被彈出時就是最小值。

**歸約到**：[bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — 字母表是 10 個數字，每個位置可以往兩個方向轉。

<!--CODE-->

<!--CODE-->

**陷阱**
- `"0000"` 本身可能就是 deadend：在 BFS 開始前就回傳 -1。
- `(digit - 1) % 10` 在 Python 是 `9`，在 Java 是 `-1`；Java 需要 `+ 10`。

**相似題** — LC 127（錨定題） · LC 433 Minimum Genetic Mutation · LC 773

<!-- 475aaa14096d -->
### Perfect Squares — LC 279

> **關鍵** — 把每個剩餘的數當節點，「減掉一個完全平方數」當邊。加總為 `n` 的最少平方數個數，就是
> 從 `n` 到 0 的最少邊數，所以從 `n` 出發的 BFS 回傳某次相減第一次碰到 0 的那一層。

**歸約到**：[bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — 鄰居是 `remaining - k²`。

<!--CODE-->

**陷阱**
- 沒有 `seen`，同一個數會被很多種順序走到（`12 - 4 - 1` 與 `12 - 1 - 4`），佇列會爆掉。
- DP `dp[i] = 1 + min(dp[i - k²])` 有同樣的上界，也是面試官期待的另一種答案；BFS 可以提早結束，DP 不行。

**相似題** — LC 322 Coin Change（同一張圖，硬幣有權重） · LC 127

<!-- ab9383393999 -->
### Sliding Puzzle — LC 773

> **關鍵** — 和 LC 752 同一個骨架；只有狀態編碼和鄰居規則不同。把 2×3 的盤面攤平成像 `"123450"`
> 的字串，並預先算好空格可以和哪些索引交換，這樣產生鄰居就是查表，而不是做邊界檢查。720 種排列
> 只有一半走得到，所以 `-1` 是真的會出現的答案。

**歸約到**：[bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — 狀態是整個盤面。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 換成別的盤面形狀時，手寫交換表最容易出錯；盤面不是 2×3 時，從 `(r ± 1, c)` / `(r, c ± 1)` 推出來。
- 在各個鄰居之間共用同一個 list 做交換（而不是每次複製一份），會弄壞同一個狀態之後的每個鄰居。

**相似題** — LC 752 · LC 854 K-Similar Strings

<!-- 74f18d22b540 -->
## 父節點表 + 向外輻射的題目

<!-- 0abb37cc3610 -->
### Closest Leaf in a Binary Tree — LC 742

> **關鍵** — 最近的葉節點可能在目標的**上方**，所以光有子指標不夠。補上父節點的邊（這裡是建一張
> 無向鄰接表），再從值為 `k` 的節點向外 BFS：每條邊成本都是 1，所以第一個彈出的葉節點就是最近的。

**歸約到**：[bfs § Template 6](./bfs.md#template-6-parent-map--bfs-radiating-from-a-target--lc-863-) — 停在第一個葉節點，而不是停在距離 `k`。

<!--CODE-->

<!--CODE-->

**陷阱**
- 「葉節點」要用樹的子節點判斷，不能用圖上的度數：圖上每個非根節點都有父節點這個鄰居，所以度數
  為 1 也會配到只有一個子節點的根。
- 只有根（`[1]`，k = 1）時根就是葉節點，答案是它自己。

**相似題** — LC 863（錨定題） · LC 2385 Amount of Time for Binary Tree to Be Infected
