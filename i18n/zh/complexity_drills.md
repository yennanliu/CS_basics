<!-- 4f563768d808 -->
# 複雜度分析練習題

> **範圍** — **20 道自我測驗題** — 讀程式片段、說出複雜度、再對答案檢查自己。
> **另見** — [complexity_cheatsheet.md](./complexity_cheatsheet.md) — 參考用的對照表；[time_space_complexity.md](./time_space_complexity.md) — 完整的推導過程。

> 練習從程式片段判斷時間複雜度與空間複雜度。
> Google 面試官非常愛追問這一塊——預期會有後續問題，例如
> 「還能更快嗎？」以及「如果最佳化的話空間是多少？」

---

<!-- 8dd709d716e9 -->
## 使用方式

1. 讀完程式片段
2. 在看答案**之前**先自己判斷時間與空間複雜度
3. 對照解說檢查你的推理
4. 把答錯的題目標上星號（⭐）——之後回來重做

---

<!-- de6b247bc6f2 -->
## 練習 1：巢狀迴圈

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N²)**
內層迴圈執行次數：N + (N-1) + (N-2) + ... + 1 = N(N+1)/2 → O(N²)

**空間：O(1)**

常見錯誤：因為「j 從 i 開始」就說是 O(N)。這個級數的總和仍然是平方級。
</details>

---

<!-- 2705232cc726 -->
## 練習 2：以乘法遞增的迴圈

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(log N)**
i 依序取 1、2、4、8、…，直到 i ≥ N。也就是 log₂(N) 次迭代。

**空間：O(1)**
</details>

---

<!-- ee3da18ddc27 -->
## 練習 3：以除法遞減的迴圈

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N)**
內層迴圈執行次數：N + N/2 + N/4 + ... + 1 = 2N → O(N)（等比級數！）

**空間：O(1)**

常見錯誤：說成 O(N log N)。每次迭代的工作量都**減半**——等比級數收斂到 2N。
</details>

---

<!-- 0983827cb693 -->
## 練習 4：遞迴費氏數列（樸素版）

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(2^N)** — 更精確地說是 O(φ^N)，其中 φ ≈ 1.618（黃金比例）
每次呼叫都分岔成 2 個子呼叫。這棵樹約有 2^N 個節點。

**空間：O(N)** — 遞迴堆疊深度為 N（會一路沿著最左的分支下去，之後才回傳）

常見錯誤：說空間是 O(2^N)。堆疊在任一時刻只裝著一條路徑。
</details>

---

<!-- 85cc3eb0bf4c -->
## 練習 5：迴圈中的 HashMap

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N)** 平均 — HashMap 的 put/get 是攤還 O(1)
**空間：O(N)** — 最多存 N 筆資料

注意：如果所有 key 都雜湊到同一個 bucket，最壞情況是 O(N²)。面試時可以提一下，但主要陳述平均情況。
</details>

---

<!-- c2f80f7b7893 -->
## 練習 6：排序 + 二分搜尋

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N log N)** — 排序是 O(N log N)，迴圈是 N × O(log N) = O(N log N)。總計：O(N log N)。

**空間：Timsort（Python）為 O(N)**，原地排序（quicksort）則為 O(log N)
</details>

---

<!-- eca140778812 -->
## 練習 7：格子上的 BFS

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(M·N)** — 每一格最多被拜訪一次
**空間：O(M·N)** — visited 集合 + 佇列最多可裝 M·N 格

常見錯誤：因為有 4 個方向就說成 O(M·N·4)。那個 4 是常數——要省略。
</details>

---

<!-- 6c606a892c8c -->
## 練習 8：合併排序

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N log N)** — 共 log N 層，每一層合併的總工作量為 O(N)

**空間：O(N)** — 合併時會建立新陣列；遞迴堆疊是 O(log N)，但被合併陣列的 O(N) 蓋過去

注意：`arr[:mid]` 會複製一份——這就是那個 O(N) 空間。原地合併排序只要 O(log N) 空間，但實作困難得多。
</details>

---

<!-- 67330a203b1a -->
## 練習 9：產生所有子集合

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N × 2^N)** — 共 2^N 個子集合，每個最長 N，複製都要成本

**空間：O(N × 2^N)** — 要存下所有子集合

逐步分析：處理完 k 個元素後，result 中有 2^k 個子集合。我們複製全部並加入 num → 2^k 份平均長度 k/2 的複本。
</details>

---

<!-- 8f20703b8ad6 -->
## 練習 10：滑動視窗

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N)** — `left` 指標只會往前走，總移動次數 ≤ N。每個字元最多進出視窗各一次。

**空間：O(K)** 或 O(min(N, Σ))，其中 Σ = 字母集大小

常見錯誤：因為 for 迴圈裡有 while 迴圈就說是 O(N²)。但 left 永遠不會往回走——攤還後是 O(N)。
</details>

---

<!-- cc62db3edfe8 -->
## 練習 11：帶路徑壓縮 + 按秩合併的併查集

<!--CODE-->

<details>
<summary>答案</summary>

**時間：每次操作 O(α(N))** — α 是反 Ackermann 函數，對所有實務上的輸入而言等同 O(1)（當 N ≤ 2^65536 時 α(N) ≤ 5）

**空間：O(N)** — parent 與 rank 陣列

關鍵洞見：只做路徑壓縮是攤還 O(log N)。只做按秩合併也是 O(log N)。兩者合用：O(α(N)) ≈ O(1)。
</details>

---

<!-- 58757a127b60 -->
## 練習 12：字典樹的插入與搜尋

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(M)**，其中 M = 單字長度

**空間：最壞情況 O(M)**（沒有共用前綴時的新節點數）

N 個平均長度 M 的單字，整棵字典樹最壞情況是 O(26 × M × N)，但因為前綴共用，實際上通常小很多。
</details>

---

<!-- b2d6014d7776 -->
## 練習 13：堆積(heap) — 最近的 K 個點

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N log K)** — `nsmallest` 維護一個大小為 K 的 max-heap，處理全部 N 個點

**空間：O(K)** — 堆積(heap) 本身

替代做法：QuickSelect 平均 O(N) 時間、O(1) 額外空間（但會改動輸入）。
</details>

---

<!-- 0ac51b896143 -->
## 練習 14：使用二維表格的 DP

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(M·N)** — 填滿 M×N 的表格，每格 O(1)

**空間：O(M·N)** — 但可用滾動陣列最佳化到 O(min(M,N))（只需要前一列）

後續追問：「空間可以再省嗎？」→ 用大小為 min(M,N)+1 的一維陣列，並用一個暫存變數保存對角線值來原地更新。
</details>

---

<!-- 5770fb087557 -->
## 練習 15：回溯 — N 皇后

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N!)** — 第 0 列有 N 種選擇、第 1 列約 N-1、第 2 列約 N-2，依此類推（有剪枝的話實務上少得多）

**空間：O(N)** — 遞迴深度為 N，每個集合最多裝 N 個元素

注意：解的數量成長速度遠低於 N!。當 N=8 時，8! = 40320 種擺法中只有 92 組解。
</details>

---

<!-- 513fdd2055dc -->
## 練習 16：攤還分析 — 動態陣列

<!--CODE-->

<details>
<summary>答案</summary>

**時間：總共 O(N)，每次 add 攤還 O(1)**

擴容發生在大小為 1、2、4、8、…、N 的時候。總複製次數：1 + 2 + 4 + ... + N = 2N → O(N)。

**空間：O(N)**

關鍵洞見：即使單次 add 可能是 O(N)（擴容當下），攤還成本仍是 O(1)，因為擴容以指數方式變得稀少。
</details>

---

<!-- ea2ffbf4781d -->
## 練習 17：在迴圈中串接字串

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N²)** — 每次串接都會複製整個既有字串。複製量：1 + 2 + 3 + ... + N = O(N²)

**空間：O(N)** — 結果字串（中間產生的字串會被 GC 回收）

修正方式：改用 `StringBuilder`，總時間變 O(N)。

這是經典的面試陷阱。在 Java 中一定要提到 StringBuilder/StringBuffer。
</details>

---

<!-- 2f83e9306c34 -->
## 練習 18：雙指標 — Container with Most Water

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N)** — 每次迭代移動一個指標，總共最多 N 次迭代

**空間：O(1)**

為什麼可行：移動較矮的那一側是唯一可能增加面積的做法（移動較高的一側只會縮小寬度，而高度不會增加）。
</details>

---

<!-- a7139e5ed96b -->
## 練習 19：樹的 DFS 加上序列化

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O(N²)** — 在字串串接會產生複本的語言中（Python、Java String）。每次串接都會複製愈來愈長的字串。

**空間：O(N)** — 遞迴堆疊（若樹平衡：O(log N) 堆疊 + O(N) 結果字串）

修正方式：用 list 收集、最後再 join → O(N) 時間。

<!--CODE-->
</details>

---

<!-- 5fc3f27ba8dc -->
## 練習 20：搭配優先佇列的 Dijkstra

<!--CODE-->

<details>
<summary>答案</summary>

**時間：O((V + E) log V)** — 每個頂點只會被真正取出一次（靠跳過檢查），每條邊只鬆弛一次，堆積(heap) 操作為 O(log V)。堆積(heap) 中最多可能有 E 筆資料 → 嚴格說是 O((V + E) log E) = O((V + E) log V)，因為 log E ≤ log V² = 2 log V。

**空間：O(V + E)** — dist 陣列 O(V)，堆積(heap) 最多裝 O(E) 筆

注意：如果少了 `if (d > dist[u]) continue` 這個檢查，就會處理到過期的資料，可能變成 O(E log E) 且常數更差。
</details>

---

<!-- a29577926333 -->
## 為自己評分

| 分數 | 程度 | 下一步 |
|-------|-------|-----------|
| 答對 18-20 題 | 已達 Google 水準 | 專注在速度——30 秒內講清楚 |
| 答對 14-17 題 | 就差一點 | 複習 complexity_cheatsheet.md 中的數學直覺 |
| 答對 10-13 題 | 還需加強 | 研讀等比級數、攤還分析、遞迴樹 |
| 答對 < 10 題 | 基礎不足 | 從 Big-O 基礎開始，用簡單例子練習 |
