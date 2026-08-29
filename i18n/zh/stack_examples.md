<!-- ea2192a3cf6b -->
# Stack — 實戰題解

> **範圍** — [stack.md](./stack.md) 背後的題解庫：單調堆疊、貪婪移除、相鄰重複、括號家族、走訪與設計類題目，每題每語言各一份標準解，依各自演練的模板分組。
> **另見**：[stack.md](./stack.md) — 母頁：本庫所支撐的標準模板、決策表與陷阱；[stack_expression_parsing.md](./stack_expression_parsing.md) — 計算機、decode string 與後綴運算求值，那是自成一家的題型；[monotonic_stack.md](./monotonic_stack.md) — next greater／previous smaller 的理論，底下不少題目其實歸它管；[iterator.md](./iterator.md) — LC 173／LC 341 之外的迭代器設計；[queue.md](./queue.md) — FIFO 那一側，包括從另一個角度看 LC 232。

<!-- 34c8da6a34d8 -->
## LeetCode 題目清單

- [Stack](https://leetcode.com/problem-list/stack/)
- [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)
- [String](https://leetcode.com/problem-list/string/)

<!-- 16d8329430b2 -->
## 總覽

這裡是 [stack.md](./stack.md) 的長尾。母頁放六份模板；這個檔案放實際*套用*它們的題目，才不會讓模板被兩千行的題解埋掉。各節依演練的模板分組，並用一組連續編號。

<!-- 1344ab9b8681 -->
### 關鍵性質
- **複雜度**：見母頁的 [Time Complexity](./stack.md#time-complexity) 表；底下每份解法都是 O(n) 時間，除非該解法自己的註解另有說明
- **核心想法**：每一節都是母頁某一份模板的演練 —— 要背的是模板，這些是練習量
- **什麼時候用**：當你已經知道一道題該用哪份模板，想看它完整寫出來長什麼樣

<!-- 6220b8183e35 -->
### 關於重複收錄

其中十二題在 [monotonic_stack.md](./monotonic_stack.md) 裡也有題解（LC 32、84、155、388、402、496、503、735、739、901、907、2104），LC 173／LC 341 則是 [iterator.md](./iterator.md) 的主題。這些重複目前是刻意保留的 —— 要整併它們是跨檔案的工程，不是單一份 cheatsheet 能處理的。

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- c3abc72a2441 -->
### 單調堆疊 —— Next Greater / Smaller

<!-- c296df38087c -->
#### 1) Next Greater Element I — LC 496

> `nums1` 是 `nums2` 的子集，所以**只掃 `nums2`**，用一趟單調掃描建出 `{element: next-greater}`
> 的對照表，再照 `nums1` 把答案讀出來。
> 兩段 Python 是暴力法的基準線（沒用堆疊，O(n·m)）；Java 那段才是標準的單調堆疊解。

<!--CODE-->

<!--CODE-->

<!-- 5137737465fb -->
#### 2) Next Greater Element II — LC 503

> **環狀**陣列：跑 `nums * 2`（或索引取 mod `n`），讓元素可以繞回頭去找答案。這裡給了兩個方向 ——
> 由左往右、在 pop 的當下就決定答案；以及由右往左、直接從剩下的堆疊頂端讀答案。

<!--CODE-->

<!-- 0d9dddf30d32 -->
#### 3) Daily Temperatures — LC 739 ⭐⭐⭐⭐

<!--CODE-->

<!--CODE-->

<!-- 1c17ceb1853e -->
#### 4) Sum of Subarray Minimums — LC 907

> **貢獻度計數**：對每個元素問「它*主宰*了幾個子陣列？」兩趟單調掃描分別給出往左、往右可以延伸的數量；
> 答案就是 `sum(a * left * right)`。

<!--CODE-->

<!-- 556890dbce60 -->
#### 5) Sum of Subarray Ranges — LC 2104

> 把 LC 907 做兩次：`sum(max) - sum(min)`，兩半都用同一套貢獻度計數，並在頭尾放哨兵，
> 逼每個元素都會被彈出堆疊。

<!--CODE-->

<!-- 66dfc06dfb73 -->
#### 6) Largest Rectangle in Histogram — LC 84 ⭐⭐⭐⭐

> 被彈出的那根柱子是矩形的**高**；新索引與新的堆疊頂端之間的距離是它的**寬**。
> 底部那個 `-1` 哨兵讓寬度的算式可以統一寫。

<!--CODE-->

<!-- 5bcf5b1bfa4e -->
#### 7) Online Stock Span — LC 901

> **串流版**單調堆疊：它跨呼叫存活，而且每一筆都帶著自己已經吸收的 span，
> 所以一次 pop 就能一整塊天數一起加進來。

<!--CODE-->

<!-- 580487c69024 -->
### 單調堆疊 —— 貪婪移除與字典序

<!-- 6d0ba4d1ce52 -->
#### 8) Remove K Digits — LC 402 ⭐⭐⭐⭐

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- fa3144780d45 -->
#### 9) Remove Duplicate Letters — LC 316

> LC 402 的貪婪移除，再加兩個額外的不變式：**每個字母只能出現一次**，而且一個字母只有在
> **後面還會再出現**時才能被彈出 —— 否則丟掉就永遠找不回來了。LC 1081 是同一題。

<!--CODE-->

> 上面的 `freq[top] > 0` 與下面的 `lastOccurrence[top] > i` 是同一個條件的兩種寫法 ——
> *「這個字元後面還會再出現嗎？」*。底下的走查會說明，為什麼這個檢查正是讓貪婪彈出安全的關鍵。

**「後面還會出現」邏輯的說明：**

<!--CODE-->

<!-- 609f6c475166 -->
#### 10) Asteroid Collision — LC 735

> 一個裝倖存者的堆疊：往左飛的小行星（`new < 0`）只會跟往右飛的堆疊頂端（`ans[-1] > 0`）對撞。
> 注意那個 `for ... else` —— `else` 只有在 `while` 沒被 break 時才會執行，也就是新來的那顆活下來了。

<!--CODE-->

<!-- d234f5af7735 -->
### 相鄰重複移除 —— `[element, count]` 配對

<!-- 6452612418f7 -->
#### 11) Remove All Adjacent Duplicates in String — LC 1047

> `k = 2` 的特例：不必記次數，單純「頂端等於當前字元就 pop」就夠了。
> 第二段是 O(1) 額外空間的雙指標寫法 —— 想法一樣，只是陣列自己當堆疊用。

<!--CODE-->

<!-- 91f811705c9c -->
#### 12) Remove All Adjacent Duplicates in String II — LC 1209 ⭐⭐⭐⭐

**模式：帶字元計數配對的堆疊**

這個模式用 `Stack<int[]>` 或 `Stack<[char, count]>` 來有效率地追蹤連續重複元素與它們的次數。當你需要移除 k 個連續相同元素時特別好用。

**什麼時候用這個模式：**

1. **題目提到「k 個連續／相鄰的相同元素」**
   - 移除 k 個重複：LC 1209
   - 數 k 個連續：各種計數題

2. **需要同時追蹤字元「和」它的出現次數**
   - 只記字元不行（k 次移除需要次數）
   - 只記次數也不行（要知道是哪個字元）

3. **次數達到門檻 k 時就移除**
   - 不像 LC 1047（k=2，一個 `stack.pop()` 就解決），這裡 k 是變數
   - 需要保留還沒湊滿的進度（例如 k=3 時，"aaab" 裡的 "aa"）

4. **要求 O(n) 空間的一趟解法**
   - 堆疊存的是壓縮形式：{char, count}
   - 比把所有字元都存下來有效率

**辨識訊號：**
- ✓ 關鍵字：「k adjacent」、「k consecutive」、「k duplicates」
- ✓ 剛好累積到 k 次時就移除／計數
- ✓ 需要處理不完整的序列（count < k）
- ✓ 輸入限制：k >= 2（若 k=1，要換另一套做法）

**結構：**
<!--CODE-->

**相似題：**
- LC 1047: Remove All Adjacent Duplicates in String（k=2 的特例）
- LC 1544: Make The String Great（移除相鄰的大小寫相反配對）
- LC 316: Remove Duplicate Letters（用堆疊求字典序）
- LC 394: Decode String（堆疊帶計數，但用途是重複展開）

<!--CODE-->

<!--CODE-->

<!-- 9ae1315c8f33 -->
### 括號家族 —— LC 20 模板的各種變化

基礎模板是[母頁的模板 2](./stack.md)；底下這四題就是它點名的四種變化。

<!-- b287ed4a58f4 -->
#### 13) Minimum Remove to Make Valid Parentheses — LC 1249

> **變化點**：推入 `(` 的**索引**而不是字元本身，這樣最後才能把沒配對到的位置刪掉。沒配對的 `)` 當下就會被抓到（堆疊為空）；沒配對的 `(` 就是掃完後*還留在堆疊裡*的那些。

<!--CODE-->

<!--CODE-->

<!-- 6c3a5f044318 -->
#### 14) Minimum Add to Make Parentheses Valid — LC 921

> **變化點**：只有 `(` 和 `)` 時，堆疊退化成它自己的**大小**，所以用一個累加的 balance 就能做到 O(1) 空間。`balance < 0` 代表 `)` 來得太早 → 必須補一個 `(` 並重設。

<!--CODE-->

<!--CODE-->

<!-- c824eef85597 -->
#### 15) Longest Valid Parentheses — LC 32 ⭐⭐⭐⭐

> **變化點**：我們要的是最長合法區段的**長度**，所以堆疊存索引，而且它的**底部元素是當前合法區段前一格的索引**（也就是「基準點」）。初始化放 `-1`。遇到 `)` 先 pop；如果堆疊變空了，當前這個 `)` 就成為新的基準點，否則 `i - stack.top()` 就是以 `i` 結尾的合法長度。

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- 4d7bf5a60f06 -->
#### 16) Score of Parentheses — LC 856

> **變化點**：堆疊的每一格存的是**那個深度裡累積的分數**。`(` 開一個新的框（推入 `0`），`)` 把它收掉：空的框算 1 分，否則就翻倍 —— `max(2 * inner, 1)` —— 然後併回上一層的框。

<!--CODE-->

<!--CODE-->

<!-- 70ac58de625f -->
### 作用域、反轉與設計

<!-- c5c91385a088 -->
#### 17) Simplify Path — LC 71

> 最迷你版的作用域帳本：名字就推入一層目錄，`..` 彈出父層，`.` 和空片段是雜訊。

<!--CODE-->

<!-- 4f73520273d4 -->
#### 18) Minimum Number of Swaps to Make the String Balanced — LC 1963

> 把括號模板當成**歸約器**用：掃完之後堆疊裡只剩下不平衡的 `]]][[[` 核心，答案就是它長度的一條公式。

<!--CODE-->

<!-- 4108841a6a80 -->
#### 19) 顯式堆疊迭代器 — LC 173, LC 341 ⭐⭐⭐⭐

> **關鍵想法**：遞迴有一個*隱式*的呼叫堆疊，而且會一路跑到底。但**迭代器必須在元素之間暫停**，所以你要把那個堆疊變成**顯式**的，每次 `next()` 只推進一步。堆疊裡放的是*還沒做的工作*。

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- 32bacfc728f4 -->
#### 20) Add Two Numbers II — LC 445

> **關鍵想法**：單向鏈結串列只能往前走，但有些題目需要**倒著**處理（從個位數開始相加）。把每個節點推入堆疊，就能在**不動到輸入**的前提下倒著存取 —— 這正是面試官問「不反轉串列做得到嗎？」時的答案。

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- a67d017eeffa -->
#### 21) Implement Queue using Stacks — LC 232

> **用兩個 LIFO 湊出 FIFO**：推入時放 `input`，`output` 空掉時就把 `input` 整個倒過去
> —— 那一次反轉攤還下來，每個操作是 O(1)。佇列那一側的觀點見 [queue.md](./queue.md)。

<!--CODE-->

<!-- 374f282301b0 -->
### 速查 —— 其他值得知道的堆疊題

| LC | 題目 | 一句話講堆疊怎麼用 |
|----|---------|------------------------|
| 946 | Validate Stack Sequences | **模擬**：逐一推入 `pushed[i]`，然後貪婪地在 `top == popped[j]` 時彈出；每個元素都被彈掉才合法 |
| 844 | Backspace String Compare | 用堆疊把兩個字串各建出來（`'#'` → 非空就 pop），再比較 —— O(n) 空間；O(1) 的追問版本要從後往前掃 |
| 1910 | Remove All Occurrences of a Substring | 推入字元；只要堆疊**最後 `len(part)` 個字元**等於 `part` 就彈掉 —— 一趟就能處理連鎖移除 |
| 331 | Verify Preorder Serialization of a Binary Tree | 把 `"num,#,#"` 三元組彈成單一個 `#`；等價的寫法是追蹤還剩幾個可用「空位」 |
| 385 | Mini Parser | 跟 LC 394 一樣的四種情況掃描，只是堆疊裡放的是 `NestedInteger` 框而不是字串 |
| 1111 | Maximum Nesting Depth of Two Valid Parentheses Strings | 用深度計數器就好，不用真的堆疊：偶數深度給 A，奇數深度給 B |

> **注意**：LC 42 (Trapping Rain Water)、LC 84 / 85 (Maximal Rectangle)、LC 456 (132 Pattern)、LC 853 (Car Fleet)、LC 581、LC 654、LC 769、LC 962 都是**單調堆疊**題 —— 那些模板見 [monotonic_stack.md](./monotonic_stack.md)。
