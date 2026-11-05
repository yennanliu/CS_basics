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

<!-- 80cb793d3b92 -->
## 辨識

| 題目敘述裡的訊號 | 限制條件線索 | 該用什麼 | 不是這個的情況 |
|---|---|---|---|
| 「移除相鄰重複」、「退格」、「復原上一步」 | n ≤ 10⁵ → 掃一趟 O(n) | [模板 1](#template-1-cancel-against-the-top--lc-1047) 與頂端相消 | 同一字元要連成 k 個才消失 → 改放 `[char, count]` 配對（[stack_examples.md](./stack_examples.md)） |
| 「合法／平衡的括號」，且括號有好幾種 | n ≤ 10⁴ → O(n) | [模板 2](#template-2-bracket-matching--lc-20-) 括號配對 | 只有一種括號 → 用平衡計數器，O(1) 空間 |
| 括號的「最少刪除數／最長合法區段」 | n ≤ 10⁵ | [模板 2 § LC 1249](#bracket-repair-variant--push-the-index-not-the-char-lc-1249-) 索引堆疊 | |
| 「下一個更大／更暖／更高」、「還要等幾天」、「最近的較小值」 | n ≤ 10⁵ → O(n²) 的掃描會超時 | [模板 3](#template-3-monotonic-stack--next-greater--smaller--lc-739-) 單調堆疊 | 每個寬度 k 視窗裡的最大值 → [monotonic_queue.md](./monotonic_queue.md) |
| 在 push／pop 之外「以常數時間取得最小值」 | 每個操作 O(1) | [模板 4](#template-4-min-stack--o1-getmin--lc-155-) 逐層彙總 | 可以從任何位置刪除，不是 LIFO → 搭配 lazy deletion 的 [heap.md](./heap.md) |
| 對一棵樹「迭代地」、「不用遞迴」 | 歪斜的樹深度可達 10⁵，遞迴會爆 | [模板 5](#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) 顯式堆疊 | 「一層一層」→ 用佇列（[bfs.md](./bfs.md)） |
| 以 tab 縮排的路徑、`start`／`end` 的 log、「現在是誰在執行」 | n ≤ 10⁴ → O(n) | [模板 6](#template-6-scope--context-ledger--lc-388-lc-636-) 作用域帳本 | 巢狀結構需要被*求值* → [stack_expression_parsing.md](./stack_expression_parsing.md) |
| 「基本計算機」、「decode string」、「逆波蘭表示法」 | | [stack_expression_parsing.md](./stack_expression_parsing.md) | |

<!-- 9f5537b2631c -->
## 題型分類

十種形狀幾乎涵蓋所有堆疊題。**在哪裡**這一欄告訴你程式碼放在下面哪個模板，或是解法搬到了哪一份表。

| 題型 | 堆疊裡放的是什麼 | LC | 在哪裡 |
|---|---|---|---|
| **括號／巢狀驗證** | 還欠一個右括號的左括號 | 20, 921, 1541, 1614 | [模板 2](#template-2-bracket-matching--lc-20-) |
| **括號修復／計量** | 未配對括號的*索引* | 1249, 32, 856 | [模板 2 § LC 1249](#bracket-repair-variant--push-the-index-not-the-char-lc-1249-)、[stack_examples.md](./stack_examples.md) |
| **單調 —— next greater／smaller** | 還在等答案的元素 | 496, 503, 739, 84, 907, 2104 | [模板 3](#template-3-monotonic-stack--next-greater--smaller--lc-739-)、[monotonic_stack.md](./monotonic_stack.md) |
| **單調 —— 貪婪移除** | 目前建出來最好的前綴 | 402, 316, 1081, 1673 | [stack_examples.md](./stack_examples.md) |
| **單調 —— span 累積** | `[value, span]` 配對，串流式處理 | 901, 735 | [stack_examples.md](./stack_examples.md) |
| **放 `[element, count]` 配對的堆疊** | 前綴的遊程壓縮表示 | 1047, 1209, 1544 | [stack_examples.md](./stack_examples.md) |
| **堆疊上的 O(1) 彙總值** | 每一層一個 `min`／`max`／delta 的*狀態* | 155, 716, 1381, 895 | [模板 4](#template-4-min-stack--o1-getmin--lc-155-) |
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

<!-- 7c185cfce4c4 -->
### 模板對照表

| 模板 | 堆疊元素 | 迴圈形狀 | 複雜度 | 什麼時候用 |
|---|---|---|---|---|
| 1 —— 與頂端相消 | 尚未被消掉的前綴 | 掃一趟，相同就 pop | O(n)／O(n) | 相鄰相消、復原、退格 |
| 2 —— 括號配對 | 左括號字元 | 掃一趟，遇右括號就 pop | O(n)／O(n) | 驗證巢狀，且括號種類 >1 |
| 3 —— 單調堆疊 | 索引 | `for` 裡包一層 `while` | O(n)／O(n) | next greater／smaller／span |
| 4 —— Min stack | 值 + 每一層一個彙總*狀態* | — | 每次操作 O(1) | O(1) `getMin()`／`getMax()`；前綴彙總、路徑狀態 |
| 5 —— 顯式堆疊 | 待處理的節點 | `while stack` | O(n)／O(h) | 迭代式走訪、順序反轉 |
| 6 —— 作用域帳本 | 每層深度的外層上下文 | 掃一趟，截到當前深度 | O(n)／O(depth) | 有縮排的輸入、start/end 事件 |

<!-- dda0b701b854 -->
### 模板 1：與頂端相消 —— LC 1047

**辨識** —— 「移除相鄰重複」、「退格」、「復原」、「相鄰兩個互相抵消」：每個新元素不是抵消最近的倖存者，就是加入倖存者。n ≤ 10⁵ → 預期掃一趟 O(n)；每刪一次就重掃字串是 O(n²)。

**關鍵** —— 堆疊*就是*目前為止的答案：它存的是已經套用完所有抵消的前綴，所以新元素唯一能互動的對象就是 `stack[-1]`。

<!--CODE-->

<!--CODE-->

下面每個模板都由這三個操作組成：

| 操作 | Python（`list`） | Java（以 `ArrayDeque` 實作的 `Deque<T>`） | 代價 |
|---|---|---|---|
| push | `stack.append(x)` | `stack.push(x)` | 均攤 O(1) |
| pop | `stack.pop()` —— 空的會 raise | `stack.pop()` —— 空的會 throw | O(1) |
| peek | `stack[-1]` —— 先用 `if stack` 擋 | `stack.peek()` —— 空的回傳 `null` | O(1) |

**複雜度**
- **時間 O(n)**：`for ch in s` 迴圈每個字元跑一次，每個字元最多 push 一次、pop 一次。
- **空間 O(n)**：`stack.append(ch)` 會留下每個倖存者；`"abcd"` 什麼都不會被消掉。

**手動推演**

<!--CODE-->

**陷阱**
- 每刪一次就重掃（`while "aa" in s: s = s.replace(...)`）：在 `"abcddcba"` 這類輸入上是 O(n²)，因為每次刪除都會露出新的一對。
- Java 的 `Stack<Character>` 由底往頂迭代，`ArrayDeque` 卻由頂往底迭代；直接迭代 `ArrayDeque` 來組答案會把結果反過來。用 `pollLast()` 從底部取出。
- Java 的 `stack.peek() == ch` 之所以安全，只因為 `ch` 是基本型別 `char`；兩個裝箱的 `Character` 用 `==` 比較的是參考。

**不是這個的情況** —— 同一字元要連成 k 個才會消失（LC 1209）→ 改 push `[char, count]` 配對，而不是單一字元（[stack_examples.md](./stack_examples.md)）。

**練習** —— LC 1047 · LC 844 *（退格：遇到 `#` 就 pop，不用比對）* · LC 1544 *（同字母、大小寫相反才相消）* · LC 232 *（兩個堆疊組成佇列）* · LC 1209 *（`[char, count]` 配對）*

---

<!-- 8c0e2de210ae -->
### 模板 2：括號配對 —— LC 20 ⭐⭐⭐⭐⭐

**辨識** —— 「合法括號」、「格式正確」、「平衡」、「正確巢狀」，而且括號或標籤**不只一種**。n ≤ 10⁴ → 掃一趟 O(n)。

**關鍵** —— 堆疊裡剛好就是還欠一個右括號的左括號，最內層在頂端；右括號只有在跟頂端配對時才合法，因為最內層開著的作用域必須最先關閉。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：只有一個 `for c in s` 迴圈；每個字元只 push 或 pop 一次。
- **空間 O(n)**：`stack.append(c)` 可能存下每個字元，例如 `"(((((("`。

**陷阱**
- 只有 `")"`：還沒檢查 `not stack` 就 pop 會 raise／throw。先檢查是否為空。
- `"(]"`：右括號需要**兩個**檢查 —— 堆疊非空，*而且*頂端是配對的那一半。
- `"((("`：迴圈從來不會失敗，所以迴圈後直接回傳 `True` 是錯的。堆疊最後必須是空的。
- Java：`st.pop() != pairs.get(c)` 兩邊都是裝箱的 `Character` 時比較的是參考。先把其中一邊拆成 `char`。

**不是這個的情況** —— 只有**一種**括號（LC 921、LC 1541、LC 1614）→ 一個平衡計數器就夠，O(1) 空間。需要出問題括號的*位置*（LC 1249、LC 32）→ push **索引**，見下方。

**練習** —— LC 20 ⭐ · LC 1614 *（最大深度 = 最大平衡值）* · LC 921 *（只用計數器）* · LC 1249 *（索引堆疊，見下方）* · LC 856 *（存部分分數的堆疊）* · LC 32 *（以 `-1` 為基準的索引堆疊）*

**括號家族 —— 同一個模板的四種變形**（解法在 [stack_examples.md](./stack_examples.md)）：

| LC | 變形 | 堆疊裡放什麼 |
|----|-------|-------------|
| 1249 | 不只驗證，還要修復 —— 見[下方](#bracket-repair-variant--push-the-index-not-the-char-lc-1249-) | 未配對 `(` 的*索引* —— 或不用堆疊：兩趟計數掃描，每個方向一趟 |
| 921 | 只有一種括號，用計數器就夠 —— O(1) 空間 | 什麼都不放（堆疊退化成一個 size） |
| 32 | 最長合法區段的長度 | 索引，外加一個 `-1` 當**基準**哨兵 |
| 856 | 從巢狀結構摺出一個分數 | 每一層的部分**結果** |

<!-- e5b8c8eb294e -->
#### 括號修復變形 —— push 的是**索引**，不是字元（LC 1249）⭐⭐⭐⭐

> **關鍵** —— LC 20 只回答*是／否*，所以堆疊可以只放左括號字元、不必記得它們在哪。LC 1249 必須**刪掉**出問題的括號，所以需要它們的*位置* —— push 每個 `(` 的**索引**。這題只有一種括號，pop 時不用比對：任何一個還開著的 `(` 都能配任何一個 `)`。

**化約為**：[模板 2](#template-2-bracket-matching--lc-20-) —— 堆疊放的是索引而不是字元。

| | LC 20 —— 驗證 | LC 1249 —— 修復 |
|---|---|---|
| 堆疊裡放 | 左括號**字元** | 未配對 `(` 的**索引** |
| 遇到 `)` 而堆疊是空的 | `return False` | 把索引 `i` 標記為要刪，繼續掃 |
| 遇到 `)` 而堆疊非空 | pop，檢查是不是另一半 | pop —— 這一對保留 |
| 掃描結束 | 堆疊必須是空的 | **還留在堆疊上**的每個索引都是未配對的 `(` → 一樣刪掉 |
| 輸出 | 一個布林值 | `s` 去掉被標記的位置 |

**為什麼這就是最少。** 一個 `)` 如果前面沒有開著的 `(`，右邊的任何東西都不可能配到它；掃完後還留在堆疊上的 `(`，後面也沒有 `)` 可配。每一次刪除都是被迫的，其餘全部成對，所以沒有刪掉任何不必刪的括號。

<!--CODE-->

<!--CODE-->

陷阱：

- **忘了第二次清掃。** 迴圈本身只抓得到多出來的 `)`；`"(()"` 需要在迴圈結束後把剩下的索引刪掉。
- **不要寫 `len(s) <= 1: return s` 這種捷徑。** `"("` 和 `")"` 都只有一個字元，而且都不合法 —— 答案是 `""`。一般迴圈本來就處理得了短字串。
- **不要在迴圈裡刪。** `s = s[:i] + s[i+1:]` 每刪一次是 O(n)（總共 O(n²)），而且會讓已經在堆疊上的每個索引全部位移。先標記，最後一次重建。
- **用一個存要刪索引的 `set` 也行**（`to_remove.update(open_idx)`，再保留 `i not in to_remove`）。演算法一樣，只是多一個結構。把 `list` 副本的位置清成空字串是比較精簡的寫法。

Java 版本，以及不用堆疊的**兩趟計數掃描**（左→右刪掉多出來的 `)`，右→左刪掉多出來的 `(` —— 面試官接下來會問的 follow-up）在 [stack_examples.md § 13](./stack_examples.md#13-minimum-remove-to-make-valid-parentheses--lc-1249)；同一個「先標記再重建」的想法，從字串技巧的角度講，在 [string.md 模板 7](./string.md#template-7-in-place-char-array--mark-then-rebuild--lc-1249-)。

---

<!-- be2b4e503551 -->
### 模板 3：單調堆疊 —— Next Greater／Smaller —— LC 739 ⭐⭐⭐⭐

**辨識** —— 「右邊下一個更大／更暖／更高」、「還要等幾天」、「兩側最近的較小值」。n ≤ 10⁵ → 每個元素往右掃的 O(n²) 會超時；預期 O(n)。

**關鍵** —— 堆疊裡放的是**還在等答案**的索引，它們的值由底往頂遞減、允許相等；第一個勝過頂端的值，就是它 pop 掉的每個索引的答案。

這份表只保留**一個**精簡版。完整家族（previous smaller、span、直方圖、子陣列最小／最大值總和，以及貪婪移除的各種變形）是 [monotonic_stack.md](./monotonic_stack.md) 的主題；解題實作在 [stack_examples.md](./stack_examples.md)。

<!--CODE-->

<!--CODE-->

把比較方向翻過來，就換一個方向：

| 想找的東西 | pop 的條件 | 答案怎麼讀出來 | LC |
|---|---|---|---|
| next **greater** 元素／下一個更暖的日子 | `top < cur` | `cur` 把 `top` pop 掉的當下 | 496, 503, 739 |
| next **smaller** 元素，或左右邊界 | `top > cur` | `cur` 把 `top` pop 掉的當下 | 84, 907, 2104 |
| 往回到上一個更大值的 **span** | `top <= cur`，並累加被 pop 掉的 span | 累加起來的計數 | 901 |
| **環狀**陣列 | 一樣的做法，跑 `nums * 2` 並用 `idx % n` | 一樣 | 503 |

**複雜度**
- **時間 O(n)**：內層 `while` 看起來是巢狀的，但每個索引只被 `stack.append(i)` push 一次、最多 pop 一次，所以整趟下來 pop 的總數最多 n 次。
- **空間 O(n)**：嚴格遞減的輸入（`[5, 4, 3, 2, 1]`）從不 pop，堆疊會存下全部 n 個。

**手動推演**

<!--CODE-->

**陷阱**
- push 值而不是索引：`[73, 74]` 就只能說出*有*更暖的日子，卻說不出*多遠*。push 索引，再透過索引讀值。
- `<` 與 `<=`：用 `<=` 時，相同溫度 `[70, 70, 71]` 中第二個 70 會把第一個 pop 掉並回報 1 天，但 70 並沒有更暖。「嚴格大於」就表示在嚴格小於時 pop。
- 最後還留在堆疊上的索引沒有答案；它們保留預設值 `0`（LC 496／503 是 `-1`），所以 `res` 要用它初始化。

**不是這個的情況** —— 答案是固定寬度**滑動視窗**裡的最大或最小值（LC 239）→ 單調*雙端佇列*，它還會從前端過期淘汰（[monotonic_queue.md](./monotonic_queue.md)）。

**練習** —— LC 739 ⭐ · LC 496 · LC 503 *（環狀：走 `2n`）* · LC 901 *（線上 span）* · LC 907 *（貢獻計數）* · LC 84 *（兩側的 next smaller）*

---

<!-- a3e3004b7d76 -->
### 模板 4：Min Stack —— O(1) getMin —— LC 155 ⭐⭐⭐⭐

**辨識** —— 「設計一個堆疊，支援 `push`、`pop`、`top`，並能**以常數時間**取得最小元素」。每個操作 O(1)，最多 3·10⁴ 次呼叫。

**關鍵** —— `min_values[i] == min(stack[0..i])`：第二個陣列逐層對映堆疊，存的是「這一層以下所有東西」的答案，所以 pop 一層就連帶 pop 掉它的答案，而下一層早就知道自己的答案。

竅門在於再開**一個逐層對映的陣列**，裡面放的不是元素本身，而是*「這一層以下所有東西」對這個查詢的答案*。LC 155 的查詢是「最小值」，所以 `min_values` 存的是每一層的最小值。這是[前綴彙總](./prefix_sum.md)在設計題裡的長相，而同一個想法在堆疊之外也到處出現 —— 見本模板最後的[經典題表](#the-same-idea-beyond-stacks--the-classics-)。

<!--CODE-->

<!--CODE-->

**複雜度**
- **每個操作時間 O(1)**：`push` 在 `min(val, self.min_values[-1])` 裡只合併兩個數字；`getMin` 只讀一格 `self.min_values[-1]`。沒有任何操作需要掃描。
- **空間 O(n)**：每 push 一個元素，`self.min_values.append(...)` 就多一筆。

**手動推演**

<!--CODE-->

**陷阱**
- 只用一個 `self.min` 變數而不是陣列：`push 1, push 0, pop` 之後最小值必須回到 1，但單一變數早已被覆寫。要復原只能 O(n) 重掃。
- 只 push *新*最小值的省空間變形必須用 `<=` 比較：用 `<` 時，`push 0, push 0, pop` 會讓 min-stack 變空，而堆疊裡還有一個 0（見 [monotonic_stack.md § 2-16](./monotonic_stack.md#2-16-min-stack-lc-155--auxiliary-non-increasing-stack-)）。上面的逐層陣列在 `pop` 裡完全不需要比較。
- Java，省空間變形：對裝箱的 `Integer` 寫 `stack.peek() == minStack.peek()` 比較的是參考，所以 push 進去的 `128` 跟自己也配不上。用 `.equals` 比較，或先拆箱。

**不是這個的情況** —— 最小值必須在從**中間**或任意順序刪除後依然正確（不是 LIFO）→ 搭配 lazy deletion 的 heap（[heap.md](./heap.md)）；視窗從前端滑走（LC 239）→ 用兩個這種堆疊組成佇列，或單調雙端佇列（見下方經典題表）。

**練習** —— LC 155 ⭐ · LC 1381 *（每層一個延遲加值）* · LC 1026 *（遞迴堆疊就是那個堆疊）* · LC 716 *（max stack，`popMax`）* · LC 895 *（每個頻率一個堆疊）*

| 做法 | `push` | `pop` | `getMin` | 差在哪裡 |
|---|---|---|---|---|
| 單一 `self.min` 變數 | O(1) | **O(n)** | O(1) | 把最小值 pop 掉之後，得重新掃一遍找前一個最小值 |
| heap（`heapq`） | O(log n) | O(log n)，需搭配 lazy deletion | O(1) | 為一個堆疊根本用不到的排序付 log 的代價 |
| `{value: count}` map | O(1) | O(1) | **O(n)** | `min(counts)` 是一次掃描（`min-stack.py` 的 V0-1 就是這個） |
| **逐層的 `min_values`** | O(1) | O(1) | O(1) | 當前頂端的答案在它被 push 的那一刻就定下來了 |

<!-- 8d1be675dbfd -->
#### 一般形式 —— 每一層一個彙總值

<!--CODE-->

從這個不變量直接推出三件事：

- **兩個陣列永遠等長** —— 每次 `push` 兩邊都 append，每次 `pop` 兩邊都移掉。`pop()` 裡不需要
  `if`，也不用把 pop 出來的值跟最小值比：`min_values[-1]` 是屬於那一層的*狀態*，不是某個元素的
  副本，所以把那一層 pop 掉就是全部的工作。這就是為什麼上面的模板不需要省空間變形那個
  `stack.pop() == mins[-1]` 的檢查。
- **重複值不用特別處理。** `push(0); push(0); pop()` 之後 `min_values = [0]`，因為每個 `0`
  都有自己的一層。只存*新*最小值的那種變形必須寫 `<=` 才能做對 —— 這是 LC 155 的經典 bug（見
  [monotonic_stack.md § 2-16](./monotonic_stack.md#2-16-min-stack-lc-155--auxiliary-non-increasing-stack-)）。
- **任何是前綴函數的彙總值都一樣可行** —— `max`、累加和、累積 GCD、待套用的 delta（LC 1381）、
  出現次數（LC 895）。把 `push` 裡的 `min(...)` 換掉，其他一行都不用動。

同一個不變量也能裝進一個存 `(value, min_so_far)` tuple 的堆疊 —— 一個結構取代兩個，界限不變：

<!--CODE-->

<!-- aabd587e1546 -->
#### 為什麼是 O(1) —— 狀態在 push 時就算好了

`getMin()` 只讀一個陣列格子。不需要掃描，因為工作已經搬到 `push` 那邊去做了，而 `push` 只要合併
**兩個**數字：新值，以及下一層的答案。整個論證就這樣 —— 每個操作只碰兩個陣列的頂端，其他什麼
都不碰。

*保留每一層的狀態*而不是只用一個 `self.min` 變數，關鍵在 `pop()`。單一變數在更小的值進來時可以往下
調，但那個值離開時卻**調不回去** —— 更早的最小值已經被覆寫掉了。陣列記得，因為它從來沒被覆寫；
它一直就在下一層。

<!-- c99d6881498a -->
#### 同一個想法走出堆疊 —— 經典題 ⭐⭐⭐⭐

這個不變量需要一個**只在一端變動**的容器，這樣頂端消失時「下一層的狀態」才仍然成立。LC 題庫裡有
四種東西是這樣運作的。看表時從第一欄下手：找到你的題目裡是什麼在扮演堆疊，每層該帶什麼彙總值就
跟著出來了。

| 什麼在扮演堆疊 | 每層的狀態 | 回答的查詢 | 經典 LC |
|---|---|---|---|
| **一個顯式堆疊** | 至今的 `min`／`max` | O(1) 的 `getMin()`／`getMax()` | **155** Min Stack、**716** Max Stack（`peekMax`） |
| | 對下方所有元素待套用的 delta | O(1) 的整批 `increment(k, val)` | **1381** Design a Stack With Increment Operation |
| | 每個頻率等級一個堆疊 | O(1) pop 出現最多次的元素 | **895** Maximum Frequency Stack |
| **兩個堆疊組成的佇列**（LC 232） | *每個*堆疊各自至今的 `min`／`max` | 攤銷 O(1) 的視窗 min／max，不需要單調 deque | **239** Sliding Window Maximum、**1438** Longest Continuous Subarray With Absolute Diff ≤ Limit |
| **一個只會變長的陣列** —— 索引 = 層 | 前綴 `min`／`max`／`sum`／`product` | 對每個 `i` 回答「`i` 之前（含）的最佳值」 | **121** Best Time to Buy and Sell Stock、**2016** Maximum Difference Between Increasing Elements、**42** Trapping Rain Water、**238** Product of Array Except Self、**303** Range Sum Query、**769** Max Chunks To Make Sorted、**915** Partition Array into Disjoint Intervals、**1477** Find Two Non-overlapping Sub-arrays Each With Target Sum |
| **遞迴堆疊** —— 根到節點的路徑 | 沿路徑的 `(lo, hi)`／`max`／前綴值 | 每個節點關於其祖先的答案 | **1026** Maximum Difference Between Node and Ancestor、**98** Validate Binary Search Tree、**1448** Count Good Nodes in Binary Tree、**129** Sum Root to Leaf Numbers |

該保留多少狀態，由兩條規則決定：

- **之後的查詢會問到「不是頂端」的那一層時，整個陣列都要留著。** LC 42 第二趟需要*每個* `i` 的
  `left_max[i]`；LC 238 需要每個前綴乘積；LC 769／915 在每個切點把前綴 max 跟後綴 min 比。單一變數
  回答不了它已經走過的那一層。
- **只會查頂端、而且永遠不 pop 時，可以塌成一個變數。** LC 121 每個索引問一次「至今最小」，從不回頭，
  所以 `min_values` 就是單一個 `lo` —— 只留陣列的最後一格，其餘丟掉。LC 155 不能這樣做，因為
  `pop()` *確實會*回頭。

<!--CODE-->

<!--CODE-->

各列的深入內容在哪裡：LC 1381 的 delta 技巧在
[design_examples.md § 6](./design_examples.md#6-stack--auxiliary-state--o1-min-and-lazy-increment-lc-155--lc-1381-)，
LC 239 的 deque 寫法在
[monotonic_queue.md](./monotonic_queue.md#template-1-sliding-window-maximum-decreasing-deque--lc-239)，
前綴陣列在 [prefix_sum.md](./prefix_sum.md)，LC 121 在
[stock_trading.md](./stock_trading.md)，`(lo, hi)` 邊界傳遞的模式在
[bst_advanced.md](./bst_advanced.md)。

---

<!-- b5bb811b4224 -->
### 模板 5：顯式堆疊 —— 迭代式走訪 —— LC 144, LC 145 ⭐⭐⭐⭐

**辨識** —— 「迭代地」、「不用遞迴」、「follow-up：遞迴解很簡單，你能用迭代做嗎？」，或樹／串列深到（歪斜的樹 n 可達 10⁵）會讓呼叫堆疊溢位。

**關鍵** —— 堆疊存的是還沒做的工作，而堆疊吐出工作的順序與放入時相反：先 push `right` 再 push `left`，`left` 就會先被處理。後序就是以 `root → right → left` 跑的前序，再反轉。

同一個堆疊的*暫停*版本 —— 必須在元素之間停下來的迭代器 —— 是 [stack_examples.md](./stack_examples.md) 裡的 LC 173／LC 341，更完整的家族在 [iterator.md](./iterator.md)。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`while stack` 每個節點恰好 pop 一次，每次 pop 最多 push 兩個子節點。
- **空間 O(h)**（堆疊本身）：最多只存一條從根到葉路徑上待處理的兄弟節點 —— 平衡時 O(log n)，歪斜時 O(n)。後序的 `res[::-1]` 副本是輸出，O(n)，不算工作空間。

**陷阱**
- 前序時先 push `left` 再 push `right`，得到的是 `root → right → left`；在 `[1, 2, 3]`（2 在左、3 在右）上會回傳 `[1, 3, 2]`。
- 把 `root = None` push 進堆疊，會在 `node.val` 上崩潰；先回傳 `[]`。
- 後序的反轉技巧得到的是*值*的順序；如果每個節點必須在子節點之後才帶著副作用被*處理*（例如釋放它們），就不能用 —— 改用 `(node, visited)` 旗標。

**不是這個的情況** —— 題目要一層一層的節點（「層序」、「右視圖」、「最小深度」）→ 用 FIFO 佇列，不是堆疊（[bfs.md](./bfs.md)）。

**練習** —— LC 144 · LC 145 · LC 94 *（中序：一路往左、pop、再往右）* · LC 173 *（暫停式中序迭代器）* · LC 341 *（巢狀串列的暫停式走訪）*

---

<!-- 3c68fd703d12 -->
### 模板 6：作用域／上下文帳本 —— LC 388, LC 636 ⭐⭐⭐⭐⭐

**辨識** —— 以 tab 縮排或巢狀的輸入（「以字串表示的檔案系統」、「標籤」），或 `start`／`end` 事件 log（「獨佔時間」、「呼叫堆疊」）；任何在一趟由左到右的掃描中問「我現在的父層是誰？」的題目。輸入長度 ≤ 10⁴ → O(n)。

**關鍵** —— 堆疊索引 == 巢狀深度：`stack[d]` 存的是深度 `d` 累積的上下文（一段路徑前綴、一個正在執行的函式），所以正在讀的東西的父層永遠是 `stack[depth]`／`stack[-1]`。

這是 Google 出現頻率最高的那批堆疊題背後的模式，而且它*不是*括號配對：這裡的「括號」是隱含的（縮排深度、start/end 的 log 事件）。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`for line in input.split('\n')` 每個字元只碰常數次；修剪用的 `while` 每個被 push 的目錄最多 pop 一次。
- **空間 O(depth)**：`stack.append(...)` 只在輸入越來越深時成長，每個開著的目錄一筆。

**手動推演**

<!--CODE-->

**陷阱**
- 忘了修剪：`"a\n\tb\nc\n\td.txt"` 會把 `d.txt` 算在 `a/b/` 底下，而不是 `c/`。
- 把檔案 push 進去：檔案不會有子節點，後面的兄弟會讀到錯的前綴。
- 忘了 `/` 的 `+ 1`：`"dir\n\tfile.ext"` 會回傳 11 而不是 12。
- 完全沒有檔案（`"a"`）：答案是 `0`，不是最長的目錄路徑。

**不是這個的情況** —— 巢狀是用明確的括號寫的，而且要*驗證* → [模板 2](#template-2-bracket-matching--lc-20-)；要*求值*（計算機、decode string）→ [stack_expression_parsing.md](./stack_expression_parsing.md)。

**練習** —— LC 388 · LC 71 *（`..` pop 掉父目錄）* · LC 636 · LC 591 *（開標籤堆疊加上 CDATA 規則）*

<!-- 59cdae7eafdf -->
#### 變體：覆寫深度位置，而不是 pop —— LC 388

> **關鍵** —— 輸入是前序（pre-order）列出的，所以深度 `d` 的一行只會讀 `path_len[d]`，而那個位置最後一次寫入正是它自己的父目錄。更深層的過期位置在被讀到之前一定會先被覆寫，所以這本帳其實就是*每個深度最後一次的寫入*。

**化約為**：[模板 6](#template-6-scope--context-ledger--lc-388-lc-636-) —— 拿掉修剪迴圈。README 把 LC 388 歸在 Hash_table 底下，就是這個寫法。複雜度一樣是 `O(n)`。

<!--CODE-->

<!--CODE-->

<!-- 8702f2d42243 -->
#### Exclusive Time of Functions — LC 636

> **關鍵** —— 堆疊就是題目描述的那個呼叫堆疊，所以頂端是*此刻*在 CPU 上執行的函式；`prev` 是還沒有被算給任何人的第一個時間單位。每一行 log 都會把 `[prev, …]` 這一段收尾、算給原本的頂端，然後移動游標。

**化約為**：[模板 6](#template-6-scope--context-ledger--lc-388-lc-636-) —— 上下文是「哪個函式正在執行」，`end` 事件會把它 pop 掉。

<!--CODE-->

<!--CODE-->

<!--CODE-->

> **為什麼只有 `end` 要 `+ 1`。** `start:t` 指的是時間單位 `t` 的*開頭*，`end:t` 指的是時間單位 `t` 的*結尾*。所以 start 的邊界在 `t`，end 的邊界在 `t + 1`。一解析到 `end:t` 就把它換成邊界 `t + 1`，兩個分支就變成同一個半開區間 `[prev, boundary)` 的相減 —— 也就是下面的寫法。複雜度一樣是 `O(n)`；值得學它的理由是：off-by-one 直接消失，而不是靠記憶。

<!--CODE-->

LC 636 特有的陷阱：

- **先記帳，再 push 或 pop。** 剛結束的那一段屬於*原本*的頂端；先 push 的話，呼叫者的時間就被算給了被呼叫者。
- **每次呼叫一個 frame，而不是每個 id 一筆。** 函式可以呼叫自己（範例 2），所以用 `{id: start_time}` 的 map 會覆寫外層那次呼叫。同一個 id 有兩個 frame 才是對的。
- **`prev` 是一個全域游標，不是每個函式各自的開始時間。** 另一種寫法是存 `[id, resume_time]` frame，並在每次 pop 之後把呼叫者的 `resume_time` 改成 `t + 1` —— 同一個想法，只是游標存在 frame 上，而且多一行容易忘記的程式。
- **長度為零的一段沒關係。** 在 `prev = 6` 之後緊接著 `1:start:6`，算進去的是 `0`；不需要特別處理。

---

<!-- 26384e1d945c -->
## 練習路徑

| 階段 | 題目（由易到難） | 新增的能力 |
|---|---|---|
| **核心** ⭐ | LC 20 · LC 155 · LC 739 | 括號配對、逐層彙總、單調堆疊 —— 各一題 |
| **練習** | LC 1047 · LC 144 · LC 232 · LC 1249 · LC 636 · LC 388 | 與頂端相消、顯式走訪、兩個堆疊組成佇列、索引堆疊，以及兩次作用域帳本 |
| **延伸** | LC 895 · LC 32 · LC 591 | 每個頻率一個堆疊；`-1` 基準哨兵；完整的標籤文法 |

<!-- ea4fa3974f42 -->
## 摘要與速查

<!-- 0428218e9466 -->
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
| push／pop 之外還要 **O(1) min／max** | 逐層彙總值 | `min_values[i] = min(stack[0..i])`，跟著那一層一起 pop | LC 155, 716, 1381 |
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
