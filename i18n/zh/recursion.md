<!-- 3df8213e1c22 -->
# 遞迴

> **範圍** — 把遞迴當成一種機制來談：base case、往下傳的狀態 vs 往上回傳的結果、呼叫堆疊的成本，以及怎麼改寫成迭代。*使用*遞迴的那些題型家族各自有專屬檔案。
> **另見**：[recursion_to_dp.md](./recursion_to_dp.md) — 把遞迴加上記憶化變成 DP；[backtrack.md](./backtrack.md) — 帶復原動作的遞迴；[dfs.md](./dfs.md) — 在圖與樹上的遞迴；[advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — 帶合併步驟的遞迴。

<!-- 944506f9aaae -->
## LeetCode 題目清單

- [Recursion](https://leetcode.com/problem-list/recursion/)

<!-- afbde9851ba1 -->
## 0) 速查

**什麼時候該用遞迴？**
- 問題有**重疊子問題**，而且每次都能縮小規模
- 你能清楚定義出 **base case** 和**遞迴情況**
- 問題天生就能拆成自己的小規模版本
- **樹／圖走訪**或**回溯**類的問題

**快速決策指南**

| 使用情境 | 模式 | 核心想法 |
|----------|---------|----------|
| 需要來自父節點的資訊 | **由上而下** | 一邊走訪一邊把上下文往下傳 |
| 需要來自子節點的結果 | **由下而上** | 先解子節點，再合併結果 |
| 需要切開再合併結果 | **分治法** | 切分問題、各自求解、合併 |
| 需要窮舉所有可能 | **回溯** | 帶決策的 DFS |
| 多次遞迴呼叫、子問題重複 | **記憶化** | 把結果快取起來，避免重複計算 |

<!-- fdf52f182bcd -->
### 核心原理

對於問題 F(X)，X 是輸入：

<!--CODE-->

<!-- 78ff90345d01 -->
### 小技巧

- **拿不定主意時**：先把**遞迴關係式**寫下來（F(n) 跟 F(n-1)、F(n-2) 等等是什麼關係）
- **有重複呼叫時**：加上**記憶化**（把中間結果快取起來）
- **怕堆疊溢位時**：用**尾遞迴**，或改寫成**迭代**

---

<!-- cd1ce7300cdb -->
## 1) 概念

<!-- dc25aefa6906 -->
### 1-1) 複雜度分析

**時間複雜度**：
把遞迴想成一個**樹狀結構**：
<!--CODE-->

給定一個遞迴演算法：**O(T) = R × O(S)**
- **R** = 遞迴呼叫的次數
- **O(S)** = 每次呼叫本身做的事的時間複雜度
- 沒有記憶化的 Fibonacci：**O(2^n)**（指數級）

**空間複雜度**：

**與遞迴相關的空間**（呼叫堆疊）：
- 遞迴函式呼叫裡的區域變數
- 輸入參數
- 輸出變數
- **堆疊溢位風險**：配置的堆疊空間碰到系統上限時

**與遞迴無關的空間**（heap）：
- 全域變數
- 記憶化的快取（存中間結果）
- **重點**：分析整體複雜度時，記憶化用掉的空間也要算進去

<!-- 5a824a09d19b -->
### 1-2) 相關概念

遞迴會用在：
- **DFS**（深度優先搜尋）—— 樹／圖走訪
- **回溯** —— 帶剪枝地窮舉所有可能
- **樹的問題** —— 遞迴演算法天生就合用
- **動態規劃** —— 搭配記憶化最佳化

---

<!-- ac012f276cbe -->
## 2) 模式

<!-- 532b10ce39a7 -->
### 2-1) 基本操作

無限地走過 list 裡的元素（在回溯／生成類題目很常見）：
<!--CODE-->

---

<!-- 13a20b92b6af -->
### 2-2) 由上而下的遞迴 —— LC 112

**定義**：從根開始，在每個節點依據父節點傳下來的資訊做決定。也就是所謂的「前序」做法。

**時間複雜度**：
- 通常是 O(n)，n 是節點數
- 如果重複解同樣的子問題又沒有記憶化，可能變成 O(n²)

**空間複雜度**：
- O(h)，h 是遞迴樹的高度（呼叫堆疊）
- 用了記憶化的話還要 O(n) 的額外空間

**使用情境**：
- 需要把資訊從父節點傳給子節點
- 帶累積狀態的樹走訪
- 路徑類問題
- 驗證類問題

**優點**：
- 直覺、好懂
- 對「資訊由父流向子」的問題很自然
- 適合做提早結束的判斷

**缺點**：
- 沒有記憶化的話可能重複計算
- 呼叫堆疊會讓空間複雜度偏高

**模式**：
<!--CODE-->

**常見的 LeetCode 題目**：
- LC 104: Maximum Depth of Binary Tree
- LC 110: Balanced Binary Tree
- LC 112: Path Sum
- LC 113: Path Sum II
- LC 124: Binary Tree Maximum Path Sum
- LC 236: Lowest Common Ancestor
- LC 257: Binary Tree Paths
- LC 404: Sum of Left Leaves
- LC 437: Path Sum III

**範例 —— Path Sum（LC 112）**：
<!--CODE-->

<!-- 5e1c1ba5ba3b -->
### 2-3) 由下而上的遞迴 —— LC 104

**定義**：從葉節點開始，把子節點的結果合併起來，一層一層堆出答案。也就是所謂的「後序」做法。

**時間複雜度**：
- 通常是 O(n)，n 是節點數
- 一般來說效率更好，因為每個節點剛好走一次

**空間複雜度**：
- O(h)，h 是遞迴樹的高度（呼叫堆疊）
- 通常不需要記憶化的額外空間

**使用情境**：
- 答案取決於子樹的結果
- 計算樹的性質（高度、直徑等）
- 聚合類問題
- 樹上的動態規劃

**優點**：
- 效率較好 —— 每個子問題剛好解一次
- 對「資訊由子流向父」的問題很自然
- 程式碼通常比較乾淨
- 多數情況下效能更好

**缺點**：
- 某些問題想起來沒那麼直覺
- 遞迴呼叫可能需要回傳多個值

**模式**：
<!--CODE-->

**常見的 LeetCode 題目**：
- LC 104: Maximum Depth of Binary Tree
- LC 110: Balanced Binary Tree
- LC 543: Diameter of Binary Tree
- LC 124: Binary Tree Maximum Path Sum
- LC 968: Binary Tree Cameras
- LC 979: Distribute Coins in Binary Tree
- LC 1120: Maximum Average Subtree
- LC 1130: Minimum Cost Tree From Leaf Values
- LC 1372: Longest ZigZag Path in a Binary Tree

**範例 —— Maximum Depth（LC 104）**：
<!--CODE-->

**範例 —— Balanced Binary Tree（LC 110）**：
<!--CODE-->

**比較表**：

| 面向 | 由上而下 | 由下而上 |
|--------|----------|-----------|
| **方向** | 根 → 葉 | 葉 → 根 |
| **資訊流向** | 父 → 子 | 子 → 父 |
| **什麼時候用** | 需要父節點的上下文 | 需要子樹的結果 |
| **效率** | 可能有重複計算 | 通常比較好 |
| **直覺度** | 路徑類問題比較直覺 | 聚合類問題比較直覺 |
| **需要記憶化嗎** | 常常需要 | 很少需要 |

---

<!-- 66559b317269 -->
### 2-4) 把狀態傳給下一層遞迴 —— LC 404

把累積的狀態／上下文當成參數傳給子層的遞迴呼叫。需要記住父節點的資訊時很好用。

**範例：LC 404（Sum of Left Leaves）**
<!--CODE-->

**關鍵洞見**：把 `isLeft` 當參數傳下去，就能追蹤父節點的上下文，不需要全域狀態。

---

<!-- a13613dd2e0d -->
### 2-5) 遞迴中的「任一為真」 —— LC 572

當你要在多個遞迴呼叫中找出「有沒有任何一個為真」，就用 OR 邏輯。只要有一個遞迴呼叫回傳 true 就提早收工。

**範例：LC 572（Subtree of Another Tree）**

<!--CODE-->

**關鍵洞見**：用 OR（`||`）可以在找到 true 的當下就離開，省掉不必要的遞迴呼叫。

---

<!-- 093a136bdeb9 -->
### 2-6) 笛卡兒積式建構 —— LC 95

**定義**：切分一個區間，對每種切法遞迴生成所有子結果，再用笛卡兒積把它們組起來，藉此生成所有可能的結構。這是**分治法**的一種形式，只是「合併」那一步變成列舉所有左 × 右的組合。

**時間複雜度**：O(4^n / n^(3/2)) —— Catalan 數的成長速度

**空間複雜度**：O(4^n / n^(3/2)) —— 存下所有生成的結構

**使用情境**：
- 生成所有結構相異的樹（BST、完滿二元樹）
- 列舉一個運算式所有加括號／切分的方式
- 任何「切分區間，再合併所有子結果」的問題

**模式**：
<!--CODE-->

<!--CODE-->

**關鍵洞見**：base case 必須回傳 `[null]`（裝著 null 的 list），**不是**空 list。否則笛卡兒積會把所有左／右子樹為空的樹整批弄丟。

**最佳化**：用 `Map<Pair<Integer,Integer>, List<TreeNode>>` 加上記憶化，避免重算重疊的子問題。

**常見的 LeetCode 題目**：
- LC 95: Unique Binary Search Trees II
- LC 96: Unique Binary Search Trees（Catalan 計數）
- LC 241: Different Ways to Add Parentheses
- LC 894: All Possible Full Binary Trees
- LC 1382: Balance a Binary Search Tree

**範例 —— LC 95: Unique Binary Search Trees II**：
<!--CODE-->

---

<!-- 17e1b6937f6c -->
## 3) 進階技巧

<!-- c0720c0c8ffb -->
### 3-1) 記憶化 —— LC 70

**想法**：把遞迴呼叫的結果快取起來，同一個子問題再出現時就不用重算。

**什麼時候用**：
- 遞迴呼叫會重複（重疊子問題）
- 不做記憶化的話時間複雜度是指數級
- 拿空間換時間（用雜湊表當快取）

**範例 1：Fibonacci**
<!--CODE-->

**範例 2：Climbing Stairs（LC 70）**
<!--CODE-->

**參考**：https://leetcode.com/explore/learn/card/recursion-i/255/recursion-memoization/1495/

---

<!-- a9e8416bee8f -->
### 3-2) 分治法 —— LC 23

**模板**：
<!--CODE-->

**虛擬碼**：
<!--CODE-->

**常見例子**：
- 合併排序 —— O(n log n)
- 快速排序 —— 平均 O(n log n)
- 二分搜尋 —— O(log n)

**常見的 LeetCode 題目**：
- LC 22: Generate Parentheses
- LC 84: Largest Rectangle in Histogram
- LC 315: Count of Smaller Numbers After Self
- LC 493: Reverse Pairs
- LC 1649: Create Sorted Array Through Instructions

**參考**：https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2869/

---

<!-- 5d6b65db50e1 -->
### 3-3) 把遞迴改寫成迭代（展開遞迴）

**為什麼要改**：
- 避免堆疊溢位的風險
- 改善空間／時間效率
- 減少函式呼叫的開銷

**怎麼改**：
<!--CODE-->

**範例**：https://leetcode.com/explore/learn/card/recursion-ii/503/recursion-to-iteration/2693/

---

<!-- c9a0cffbcc54 -->
## 4) 完整的 LeetCode 範例

<!-- a483eadad8ce -->
### 4-1) Symmetric Tree（LC 101）

**模式**：由下而上的遞迴，同時比較兩棵子樹。

<!--CODE-->

---

<!-- f6a46ebe49cc -->
### 4-2) One Edit Distance（LC 161）

**模式**：提早剪枝（長度差的絕對值 > 1），再逐一檢查每個位置。

<!--CODE-->

---

<!-- e5b15b5ca7b6 -->
### 4-3) Merge Two Sorted Lists（LC 21）

**模式**：單純的遞迴，順手更新區域狀態。

<!--CODE-->

---

<!-- adcdf53b1f67 -->
### 4-4) Subtree of Another Tree（LC 572）

**模式**：搭配遞迴輔助函式的「任一為真」。

<!--CODE-->

**Java 版本**：
<!--CODE-->

---

<!-- ce00a9e46a77 -->
## 5) 更多遞迴模板

上面那些章節都以樹為中心。下面四個模板涵蓋面試會出現的其他遞迴形狀：**鏈結串列重接**、**遞迴下降剖析**、**折半遞迴**，以及**純遞迴關係化簡**。

**快速決策表**

| 題目裡的訊號 | 模板 | 例題 |
|-----------------------|----------|----------|
| 重建／重排鏈結串列 | **5-1) 重接指標並回傳新的 head** | LC 206, 24, 25, 203, 234 |
| 字串裡有巢狀括號／文法 | **5-2) 遞迴下降（共用游標）** | LC 394, 224, 1106, 736 |
| `n` 每一步是按*倍率*縮小 | **5-3) 折半遞迴** | LC 50, 1922, 231/326/342 |
| 從 `f(n-1)` 推出封閉形式的 `f(n)` | **5-4) 遞迴關係化簡** | LC 779, 1823, 273 |

---

<!-- ad148daf215d -->
### 5-1) 遞迴式鏈結串列重接 —— LC 206 / 24 / 25 ⭐⭐⭐⭐⭐

**定義**：一個遞迴的鏈結串列函式收下某段子串列的 head，然後**回傳已經處理完的那段子串列的新 head**。呼叫者再把回傳的 head 接到自己的節點上。所有指標手術都發生在遞迴呼叫*回來之後*（也就是說，在串列上這是由下而上的做法）。

**三步驟契約** —— 這三步做對，所有串列遞迴都會自己掉出來：

<!--CODE-->

**時間**：O(n) —— 每個節點碰一次。**空間**：O(n) 的呼叫堆疊（LC 25 是 O(n/k)）。

**關鍵洞見**：絕對不要想「原地改、回傳 void」。回傳值*就是*新的 head；LC 206 忘了設 `head.next = null` 就是那個經典的環狀 bug。

**範例 —— LC 206: Reverse Linked List**

<!--CODE-->

<!--CODE-->

**範例 —— LC 24: Swap Nodes in Pairs**

<!--CODE-->

<!--CODE-->

**範例 —— LC 25: Reverse Nodes in k-Group**（把 LC 24 從 k=2 推廣到任意 k）

<!--CODE-->

<!--CODE-->

**各種變形**

- **LC 203（Remove Linked List Elements）** —— 變化點：做的是*刪除*而不是重排，所以「重接」那一步變成有條件的 return。不需要 dummy 節點：

<!--CODE-->

- **LC 234（Palindrome Linked List）** —— 變化點：完全不重接；把**呼叫堆疊當成反向迭代器**。遞迴回溯的過程往回走，同時用一個成員變數往前走。

<!--CODE-->

<!--CODE-->

- **LC 143（Reorder List）** —— 變化點：是組合而不是新的遞迴。用快慢指標從中間切開，用 **LC 206** 反轉後半段，再把兩半交錯合併（**LC 21** 的合併步驟）。

**常見的 LeetCode 題目**
- LC 206: Reverse Linked List（基本模板）
- LC 24: Swap Nodes in Pairs
- LC 25: Reverse Nodes in k-Group（LC 24 的困難版）
- LC 203: Remove Linked List Elements
- LC 234: Palindrome Linked List
- LC 143: Reorder List
- LC 21: Merge Two Sorted Lists（見 4-3）

---

<!-- 032423b1cf71 -->
### 5-2) 遞迴下降剖析 —— LC 394 / 224 ⭐⭐⭐⭐⭐

**定義**：剖析巢狀字串時，**每條文法規則寫一個函式**，所有函式共用同一個**游標**（索引）。每個函式剛好吃掉自己那條規則的字元，然後把游標停在它們的後面。巢狀括號 = 遞迴；多層優先序 = **相互遞迴**（`expr` 呼叫 `term`，`term` 呼叫 `expr`）。

**時間**：O(n) 個 token（文法會展開時是 O(output)，例如 LC 394）。
**空間**：O(巢狀深度)。

**模式**：
<!--CODE-->

**關鍵洞見**：最常見的 bug 就是把游標寫成*參數*而不是共用狀態 —— 這樣父層會把子層已經吃掉的字元再剖析一次。先把文法用 BNF 寫下來；程式碼只是它的機械式翻譯。

**範例 —— LC 394: Decode String**（文法：`str := (char | int '[' str ']')*`）

<!--CODE-->

<!--CODE-->

**範例 —— LC 224: Basic Calculator**（兩層文法上的相互遞迴）

<!--CODE-->

<!--CODE-->

<!--CODE-->

**各種變形**
- **LC 1106（Parsing A Boolean Expression）** —— 同一個游標模板；文法是 `expr := 't' | 'f' | '!(' expr ')' | ('&'|'|') '(' expr (',' expr)* ')'`，所以遞迴要收集一*串*子結果，再用 `and`／`or` 摺起來。
- **LC 736（Parse Lisp Expression）** —— 同一個模板再加一個**作用域堆疊**：`let` 會綁定變數，所以每次遞迴呼叫都要帶著（或 push/pop）一份環境表。
- **LC 770（Basic Calculator IV）** —— 同一個模板，但每個子結果是一個*多項式*（排序後的變數 tuple → 係數的對照表）而不是整數。

**常見的 LeetCode 題目**
- LC 394: Decode String
- LC 224: Basic Calculator
- LC 1106: Parsing A Boolean Expression
- LC 736: Parse Lisp Expression
- LC 770: Basic Calculator IV

---

<!-- 82d56d980698 -->
### 5-3) 折半遞迴（快速冪） —— LC 50 ⭐⭐⭐⭐

**定義**：當參數是按**倍率**縮小（通常是 /2）而不是減 1 時，遞迴深度就從 O(n) 掉到 O(log n)。最經典的例子就是二進位快速冪：

<!--CODE-->

**時間**：O(log n)。**空間**：O(log n) 的呼叫堆疊。

**關鍵洞見**：`half` 只算**一次**，然後把它平方。寫成 `fastPow(x, n/2) * fastPow(x, n/2)` 看起來一樣，卻讓遞迴樹重新展開成 O(n)。

**範例 —— LC 50: Pow(x, n)**

<!--CODE-->

<!--CODE-->

**各種變形**

- **LC 1922（Count Good Numbers）** —— 變化點：同樣的遞迴，但要**取模**，而且 `n` 可以到 10^15，所以非 O(log n) 不可。偶數索引有 5 種選擇（0,2,4,6,8），奇數索引有 4 種（質數 2,3,5,7）→ `5^ceil(n/2) * 4^floor(n/2) mod 1e9+7`。

<!--CODE-->

<!--CODE-->

- **LC 231 / 326 / 342（Power of Two / Three / Four）** —— 變化點：方向*反過來* —— 把 `n` 一路除到 1，而不是往上乘出來。一個模板涵蓋三題（換底數就好）；記得擋掉 `n < 1`，否則遞迴永遠不會結束。

<!--CODE-->

<!--CODE-->

**常見的 LeetCode 題目**
- LC 50: Pow(x, n)
- LC 1922: Count Good Numbers（模數快速冪）
- LC 231: Power of Two
- LC 326: Power of Three
- LC 342: Power of Four

---

<!-- c0d2911d00b0 -->
### 5-4) 遞迴關係化簡（沒有樹，也沒有搜尋） —— LC 779 / 1823 ⭐⭐⭐⭐

**定義**：有些問題**根本沒有資料結構可以走訪**。整個解就是一行把 `f(n)` 和 `f(n-1)`（或 `f(n/2)`、`f(n/1000)`……）連起來的遞迴關係式。面試考的是你能不能*推導*出那個關係；推出來之後程式碼只有 3 行。

**怎麼推導**：
<!--CODE-->

**時間**：O(遞迴的深度)。**空間**：O(深度) 的呼叫堆疊（尾遞迴形狀可以輕鬆改成 O(1) 的迴圈）。

**範例 —— LC 779: K-th Symbol in Grammar**

第 `n` 列是把第 `n-1` 列的每個 `0` 換成 `01`、每個 `1` 換成 `10`。所以第 `n` 列的位置 `k` 來自第 `n-1` 列的位置 `(k+1)/2`：**`k` 為奇數就複製父元素，`k` 為偶數就翻轉它。**

<!--CODE-->

<!--CODE-->

**範例 —— LC 1823: Find the Winner of the Circular Game**（Josephus 遞迴式）

第一次淘汰之後剩下 `n-1` 個人，而且計數要從往前 `k` 個位置重新開始 —— 所以小問題的答案只要平移 `k`（再對 `n` 取模）就好。

<!--CODE-->

<!--CODE-->

**變形 —— 按數量級拆解，而不是一次減 1：LC 273（Integer to English Words）**

變化點：這個遞迴式每次剝掉**最大的量級單位**（Billion／Million／Thousand／Hundred），再對餘下的部分遞迴，所以深度是 O(log10 n) 而不是 O(n)。

<!--CODE-->

<!--CODE-->

**常見的 LeetCode 題目**
- LC 779: K-th Symbol in Grammar（索引對映）
- LC 1823: Find the Winner of the Circular Game（Josephus）
- LC 390: Elimination Game（對反轉後的半數問題遞迴）
- LC 273: Integer to English Words（按數量級拆解）
- LC 233: Number of Digit One（逐位遞迴）
- LC 509: Fibonacci Number（教科書等級的遞迴式 —— 記得加記憶化）

---

<!-- 2b37e43d2eac -->
### 5-5) 其他掛著 Recursion 標籤的經典題

這些題目都落在上面已經談過的模式裡，列出來只是求完整：

| LC | 題目 | 屬於 |
|----|---------|------|
| 10 | Regular Expression Matching | 對 `(i, j)` 做由上而下的遞迴 + 記憶化 —— 見 `recursion_to_dp.md` |
| 44 | Wildcard Matching | 跟 LC 10 一樣，只是 `*` 配對的是一整段，而不是「前一個字元重複 0 次以上」 |
| 486 | Predict the Winner | 對 `(l, r)` 做 minimax 遞迴 + 記憶化 —— 見 `recursion_to_dp.md` |
