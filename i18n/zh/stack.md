<!-- 14de823b3fc2 -->
# 堆疊（Stack）

> **範圍** — LIFO 的基本功，加上堆疊的經典模板：括號配對、min-stack、單調堆疊的精簡版、用顯式堆疊做走訪，以及作用域／上下文帳本。
> **另見**：[stack_expression_parsing.md](./stack_expression_parsing.md) — 計算機、decode string 與後綴式求值，整個運算式剖析家族；[stack_examples.md](./stack_examples.md) — 這些模板背後的解題實作庫；[monotonic_stack.md](./monotonic_stack.md) — next greater／previous smaller／span 類問題的深入版；[queue.md](./queue.md) — FIFO 的對照組；[iterator.md](./iterator.md) — 以堆疊為底的迭代器。

<!-- fc9337e5f128 -->
## LeetCode 題目清單

- [Stack](https://leetcode.com/problem-list/stack/)
- [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)

<!-- f5c7645bf9dc -->
## 時間複雜度

| 資料結構 | 搜尋 | 插入 | 刪除 | 最小／最大 |
| -------------- | -------- | -------- | -------- | -------- |
| 堆疊          | O(n)     | O(1)     | O(1)     | O(n)     |

> 插入 = push，刪除 = pop，peek —— 全都發生在頂端，全都是 **O(1)**。最小／最大值可以靠一個輔助的 min／max 堆疊做到 **O(1)**（見 [monotonic_stack.md](./monotonic_stack.md)）。空間是 **O(n)**。

<!-- 24a46ef5fcd3 -->
## 總覽

<p align="center"><img src="../pic/stack.jpeg"></p>

**堆疊**是具有後進先出（LIFO）性質的資料結構。每個操作都在堆疊頂端加入或移除元素。

<!-- 509d99389500 -->
### 關鍵性質
- **複雜度**：見上方的[時間複雜度](#time-complexity)表
- **核心原理**：最後放進去的元素最先被拿出來
- **適用場景**：牽涉到順序反轉、樣式配對，或是要維護上下文的問題

<p align="center"><img src="../pic/stack_101.png"></p>

<!-- c1da75d31c24 -->
### 參考資料

- 文章
    - [fuck-Algorithm - single stack](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E5%8D%95%E8%B0%83%E6%A0%88.md)
    - [fuck-Algorithm - implement array via stack / stack via array ](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%98%9F%E5%88%97%E5%AE%9E%E7%8E%B0%E6%A0%88%E6%A0%88%E5%AE%9E%E7%8E%B0%E9%98%9F%E5%88%97.md)
    - [Java Stack](https://blog.csdn.net/oChangWen/article/details/72859556) — 底層實作：陣列
- 影片
    - [Stack Fundamentals](https://www.bilibili.com/list/525438321?sort_field=pubtime&spm_id_from=333.999.0.0&oid=779764003&bvid=BV1my4y1Z7jj)

<!-- 116ce5bd9d0a -->
## 題型分類

九種形狀幾乎涵蓋所有堆疊題。**在哪裡**這一欄告訴你程式碼放在下面哪個模板，或是解法搬到了哪一份表。

| 題型 | 堆疊裡放的是什麼 | LC | 在哪裡 |
|---|---|---|---|
| **括號／巢狀驗證** | 還欠一個右括號的左括號 | 20, 921, 1541, 1614 | [模板 2](#template-2-bracket-matching--lc-20-) |
| **括號修復／計量** | 未配對括號的*索引* | 1249, 32, 856 | [stack_examples.md](./stack_examples.md) |
| **單調 —— next greater／smaller** | 還在等答案的元素 | 496, 503, 739, 84, 907, 2104 | [模板 3](#template-3-monotonic-stack--next-greater--smaller--lc-739-)、[monotonic_stack.md](./monotonic_stack.md) |
| **單調 —— 貪婪移除** | 目前建出來最好的前綴 | 402, 316, 1081, 1673 | [stack_examples.md](./stack_examples.md) |
| **單調 —— span 累積** | `[value, span]` 配對，串流式處理 | 901, 735 | [stack_examples.md](./stack_examples.md) |
| **放 `[element, count]` 配對的堆疊** | 前綴的遊程壓縮表示 | 1047, 1209, 1544 | [stack_examples.md](./stack_examples.md) |
| **運算式剖析** | 運算元／延後的項／未關閉的作用域 | 224, 227, 772, 394, 150, 682 | [stack_expression_parsing.md](./stack_expression_parsing.md) |
| **作用域／上下文帳本** | *外層*的上下文，以深度為鍵 | 388, 636, 591, 71 | [模板 6](#template-6-scope--context-ledger--lc-388-lc-636-) |
| **順序反轉／暫停的走訪** | 還沒做完的工作 | 144, 145, 173, 341, 445 | [模板 5](#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) |

<!-- d5fb7cca997c -->
### 值得知道的堆疊變形

- 單一堆疊
- 用堆疊做出佇列
     - LC 232（用 `2 stack`）
- 用佇列做出堆疊
- **放 (char, count) 配對的堆疊**
     - 存 `[element, count]` 配對，而不是原始元素
     - LC 1047（k=2 的特例，單純 pop）
     - LC 1209（移除 k 個連續重複字元）
     - LC 1544（Make The String Great）
     - LC 394（Decode String，用堆疊記重複次數）
     - LC 726（Number of Atoms）

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 413bb2ee673a -->
### 模板對照表

| 模板 | 堆疊元素 | 迴圈形狀 | 複雜度 | 什麼時候用 |
|---|---|---|---|---|
| 1 —— 基本操作 | 任何東西 | — | 每次操作 O(1) | push／pop／peek 的慣用寫法 |
| 2 —— 括號配對 | 左括號字元 | 掃一趟，遇右括號就 pop | O(n)／O(n) | 驗證巢狀，且括號種類 >1 |
| 3 —— 單調堆疊 | `(value, index)` | `for` 裡包一層 `while` | O(n)／O(n) | next greater／smaller／span |
| 4 —— Min stack | 值 + 當下最小值 | — | 每次操作 O(1) | 堆疊上的 O(1) `getMin()` |
| 5 —— 顯式堆疊 | 待處理的節點 | `while stack` | O(n)／O(h) | 迭代式走訪、順序反轉 |
| 6 —— 作用域帳本 | 每層深度的外層上下文 | 掃一趟，截到當前深度 | O(n)／O(depth) | 有縮排的輸入、start/end 事件 |

<!-- 486a928c0c09 -->
### 模板 1：堆疊基本操作

**push（放入）：**
<!--CODE-->

<!--CODE-->

**pop（移除頂端）：**
<!--CODE-->

<!--CODE-->

**peek（看頂端）：**
<!--CODE-->

<!--CODE-->

---

<!-- ba1ed1ac1d58 -->
### 模板 2：括號配對 —— LC 20 ⭐⭐⭐⭐⭐

> 面試出現頻率最高的堆疊模式。**把左括號 push 進去，遇到右括號就檢查頂端是不是它的另一半。** 只要括號**不只一種**，就非用堆疊不可（計數器不夠），因為順序有意義：`([)]` 是不合法的。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**括號家族 —— 同一個模板的四種變形**（解法在 [stack_examples.md](./stack_examples.md)）：

| LC | 變形 | 堆疊裡放什麼 |
|----|-------|-------------|
| 1249 | 不只驗證，還要修復 | 未配對 `(` 的*索引* |
| 921 | 只有一種括號，用計數器就夠 —— O(1) 空間 | 什麼都不放（堆疊退化成一個 size） |
| 32 | 最長合法區段的長度 | 索引，外加一個 `-1` 當**基準**哨兵 |
| 856 | 從巢狀結構摺出一個分數 | 每一層的部分**結果** |

---

<!-- b01679ef3ef1 -->
### 模板 3：單調堆疊 —— Next Greater／Smaller —— LC 739 ⭐⭐⭐⭐

> **核心想法**：堆疊裡放的是**還在等答案**的元素，並保持單調順序。把比較方向翻過來就換一個方向 —— `top < cur` 時 pop 是找 *next greater*，`top > cur` 時 pop 是找 *next smaller*。每個元素只 push 一次、最多 pop 一次，所以 `for` 裡包 `while` 仍然是 **O(n)**。
>
> 這份表只保留**一個**精簡版。完整家族（previous smaller、span、直方圖、子陣列最小／最大值總和，以及貪婪移除的各種變形）是 [monotonic_stack.md](./monotonic_stack.md) 的主題；解題實作在 [stack_examples.md](./stack_examples.md)。

- 存 `(value, index)` —— 索引才能把「找到了」變成一段*距離*或一個*寬度*。

<!--CODE-->

<!--CODE-->

| 想找的東西 | pop 的條件 | 答案怎麼讀出來 | LC |
|---|---|---|---|
| next **greater** 元素／下一個更暖的日子 | `top < cur` | `cur` 把 `top` pop 掉的當下 | 496, 503, 739 |
| next **smaller** 元素，或左右邊界 | `top > cur` | `cur` 把 `top` pop 掉的當下 | 84, 907, 2104 |
| 往回到上一個更大值的 **span** | `top <= cur`，並累加被 pop 掉的 span | 累加起來的計數 | 901 |
| **環狀**陣列 | 一樣的做法，跑 `nums * 2` 並用 `idx % n` | 一樣 | 503 |

---

<!-- 1bab10284f25 -->
### 模板 4：Min Stack —— O(1) getMin —— LC 155 ⭐⭐⭐⭐

**模式：2 個堆疊（主堆疊 + 追蹤最小值的堆疊）**

<!--CODE-->

<!--CODE-->

<!--CODE-->

---

<!-- e31bfbd05fbc -->
### 模板 5：顯式堆疊 —— 迭代式走訪 —— LC 144, LC 145 ⭐⭐⭐⭐

> **核心想法**：把遞迴的呼叫堆疊**攤開來自己管**。把還沒做的工作 push 進去，pop 出來就做。**前序**要先 push `right` 再 push `left`，因為堆疊會把你餵進去的東西反過來吐。**後序**是樹裡最划算的小把戲：用 `root → right → left` 跑一次前序，然後把**輸出反轉**。
>
> 同一個堆疊的*暫停*版本 —— 必須在元素之間停下來的迭代器 —— 是 [stack_examples.md](./stack_examples.md) 裡的 LC 173／LC 341，更完整的家族在 [iterator.md](./iterator.md)。

<!--CODE-->

<!--CODE-->

---

<!-- fd3b09e47584 -->
### 模板 6：作用域／上下文帳本 —— LC 388, LC 636 ⭐⭐⭐⭐⭐

> **核心想法**：堆疊裡放的不是*字元*，而是**外層的上下文**（一段路徑前綴、一個正在執行的函式、一個未關閉的標籤）。進入一個作用域就 **push** 上下文，離開就 **pop**，答案是拿 `stack[-1]`／`stack[depth]` —— 也就是你當下所在的那層上下文 —— 算出來的。
>
> 這是 Google 出現頻率最高的那批堆疊題背後的模式，而且它*不是*括號配對：這裡的「括號」是隱含的（縮排深度、start/end 的 log 事件）。

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!--CODE-->

---

<!-- ea4fa3974f42 -->
## 摘要與速查

<!-- e50f14588e93 -->
### 決策表 —— 該用哪一種堆疊模式？

| 問題類型 | 模式 | 核心想法 | 例題 |
|--------------|---------|----------|----------|
| 找 **next greater／smaller** 元素 | 單調堆疊 | 維持遞增／遞減的順序 | LC 496, 503, 739 |
| **移除相鄰重複字元** | 放 [element, count] 配對的堆疊 | 記次數，湊到 k 就 pop | LC 1047, 1209, 1544 |
| 帶括號的**字串解碼** | 帶計數的堆疊 | 用配對處理巢狀重複 | LC 394, 726 |
| **算術運算式** | 帶運算子的堆疊 | 處理優先序與求值 | LC 224, 227 |
| **移除 k 位數**使數字最小 | 貪婪 + 單調 | 划算就把較大的數字 pop 掉 | LC 402 |
| 有重複字元時求**字典序最小** | 單調 + 最後出現位置 | 貪婪移除，搭配「後面還會出現」的檢查 | LC 316, 1081 |
| **串流／線上**頻率統計 | 帶 span 配對的堆疊 | 用配對累加計數 | LC 901 |
| **用 LIFO 做出 FIFO** | 兩個堆疊 | 用 input／output 兩個堆疊模擬佇列 | LC 232 |
| **括號平衡**驗證 | 括號配對 | push 左括號，遇右括號 pop 並驗證 | LC 20, 1249, 32 |
| **巢狀上下文**（縮排、start/end 事件） | 作用域／上下文帳本 | `stack[depth]` = 外層上下文 | LC 388, 636, 591 |
| **反轉**一個只能往前走的序列 | 全部 push，再全部 pop | pop 出來就是逆序 | LC 445, 234, 143 |
| **迭代式**走訪／延遲式迭代器 | 顯式堆疊 | 堆疊裡放的是還沒做的工作 | LC 144, 145, 173, 341 |
| **後綴式／RPN** 求值 | 運算元堆疊 | 遇運算子就 pop 兩個、push 結果 | LC 150, 682 |

**怎麼用**：在最左欄找到你的問題目標，再拿對應的模式和例題當起點。

<!-- bc120d3d9b7f -->
### 各模式的複雜度

這裡列的是每個*模式*的成本。結構本身每個*操作*的成本在最上面的[時間複雜度](#time-complexity)表。

| 模式 | 時間 | 空間 | 為什麼 |
|---|---|---|---|
| 括號配對 | O(n) | O(n)，只有一種括號時 O(1) | 掃一趟，每個字元最多 push 一次 |
| 單調堆疊 | O(n) | O(n) | 每個元素 push 一次、最多 pop 一次 |
| 貪婪移除（丟掉 `k` 個） | O(n) | O(n) | 同樣的攤還分析；`k` 限制了 pop 的次數 |
| `[element, count]` 配對 | O(n) | O(n) | 堆疊就是前綴的遊程編碼 |
| Min stack | 每次操作 O(1) | O(n) | 每次 push 多存一筆輔助資料 |
| 顯式堆疊走訪 | O(n) | O(h) | 待處理的只有當前那條 root 到節點的路徑 |
| 作用域帳本 | O(n) | O(depth) | 每個未關閉的作用域一筆 |
| 運算式剖析 | O(n) | O(n) | 堆疊深度 = 巢狀深度 |

<!-- 91bffd502e64 -->
### 常見陷阱

- **單調堆疊**：處理「next greater／smaller」問題的關鍵模式 —— 先確認題目要的是遞增還是遞減
- **配對堆疊**：移除相鄰重複或巢狀計數的題目，堆疊裡存 `[element, count]` 配對
- **貪婪移除**：有些題目適合在維持某個不變量的前提下，貪婪地把元素丟掉
- **先檢查堆疊是否為空**：每個由右括號觸發的 `pop()`／`peek()` 前面都要有 `!stack.isEmpty()`。
- **Java 的 `Character` vs `char`**：對兩個裝箱的 `Character` 用 `!=` 比的是參考 —— 比較前先拆箱。
- 這類題目的**整數除法是往零截斷**；Python 的 `//` 是*往下取整*，所以要寫 `int(a / b)`。
- 當堆疊必須吐出由左到右的順序時，**子節點要反著 push**。

<!-- 0193ddf226d3 -->
### 其他內容在哪裡

| 你在找 | 檔案 |
|---|---|
| 計算機（LC 224／227／772）、decode string（LC 394）、後綴式（LC 150） | [stack_expression_parsing.md](./stack_expression_parsing.md) |
| 上面提到那些題目的解題實作 | [stack_examples.md](./stack_examples.md) |
| next greater／previous smaller／直方圖的理論 | [monotonic_stack.md](./monotonic_stack.md) |
| 迭代器設計（LC 173, 341, 284） | [iterator.md](./iterator.md) |
| FIFO、雙端佇列、單調佇列 | [queue.md](./queue.md)、[monotonic_queue.md](./monotonic_queue.md) |
