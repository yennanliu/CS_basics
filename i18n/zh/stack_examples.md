<!-- 5353ded177a5 -->
# Stack — 實戰題解

> **範圍** — [stack.md](./stack.md) 背後的題庫：不是任何模板錨點題的堆疊題，各自以題目卡的形式放在它所歸約到的模板底下；單調堆疊題在 [monotonic_stack.md](./monotonic_stack.md) 裡完整解出，這裡只放指向它們的連結。
> **另見**：[stack.md](./stack.md) — 母頁：這些題目所歸約到的六份模板；[monotonic_stack.md](./monotonic_stack.md) — next greater／previous smaller、span、直方圖與貪婪移除，完整解在那裡；[stack_expression_parsing.md](./stack_expression_parsing.md) — 計算機、decode string 與後綴運算求值，那是自成一家的題型；[iterator.md](./iterator.md) — 以堆疊為底的迭代器（LC 173、LC 341）；[queue.md](./queue.md) — FIFO 那一側，包括 LC 232。

<!-- 34c8da6a34d8 -->
## LeetCode 題目清單

- [Stack](https://leetcode.com/problem-list/stack/)
- [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)
- [String](https://leetcode.com/problem-list/string/)

<!-- c097e1a25367 -->
## 總覽

[stack.md](./stack.md) 放六份模板；這份表是套用它們的題目。底下每個 `##` 小節對應一份模板，順序和核心表相同，每張卡片都寫出該題在模板之上多加的那一步。已經在別份表裡解出的題目，在[索引](#index)（以及所屬小節的指標表）裡只佔一列，不另抄一份。

<!-- 4295ce62b678 -->
## 索引

| LC | 題目 | 歸約到 | 難度 | 在哪裡 |
|---|---|---|---|---|
| 1047 | Remove All Adjacent Duplicates In String | [stack § Template 1](./stack.md#template-1-cancel-against-the-top--lc-1047) | Easy | 錨點題，完整解在 stack.md |
| 844 | Backspace String Compare | [advanced_simulation § Template 6](./advanced_simulation.md#variations-of-template-6) | Easy | 完整解在 advanced_simulation.md |
| 946 | Validate Stack Sequences | [advanced_simulation § Template 6](./advanced_simulation.md#variations-of-template-6) | Medium | 完整解在 advanced_simulation.md |
| 1910 | Remove All Occurrences of a Substring | [advanced_simulation § Template 6](./advanced_simulation.md#variations-of-template-6) | Medium | 完整解在 advanced_simulation.md |
| 1209 | Remove All Adjacent Duplicates in String II | [stack § Template 1](./stack.md#template-1-cancel-against-the-top--lc-1047) | Medium | [卡片](#remove-all-adjacent-duplicates-in-string-ii--lc-1209-) |
| 735 | Asteroid Collision | [stack § Template 1](./stack.md#template-1-cancel-against-the-top--lc-1047) | Medium | [卡片](#asteroid-collision--lc-735) |
| 20 | Valid Parentheses | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) | Easy | 錨點題，完整解在 stack.md |
| 921 | Minimum Add to Make Parentheses Valid | [greedy_examples § 7](./greedy_examples.md#7-minimum-add-to-make-parentheses-valid--lc-921--a-balance-counter) | Medium | 完整解在 greedy_examples.md |
| 1249 | Minimum Remove to Make Valid Parentheses | [string § Template 7](./string.md#template-7-in-place-char-array--mark-then-rebuild--lc-1249-) | Medium | 索引堆疊在 string.md；[兩趟掃描卡片](#minimum-remove-to-make-valid-parentheses--lc-1249) 在這裡 |
| 856 | Score of Parentheses | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) | Medium | [卡片](#score-of-parentheses--lc-856) |
| 1963 | Minimum Number of Swaps to Make the String Balanced | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) | Medium | [卡片](#minimum-number-of-swaps-to-make-the-string-balanced--lc-1963) |
| 32 | Longest Valid Parentheses | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) | Hard | [卡片](#longest-valid-parentheses--lc-32-) |
| 496 | Next Greater Element I | [monotonic_stack § 2-3](./monotonic_stack.md#2-3-next-greater-element-i-lc-496--monotonic-stack--hashmap) | Easy | 完整解在 monotonic_stack.md |
| 739 | Daily Temperatures | [stack § Template 3](./stack.md#template-3-monotonic-stack--next-greater--smaller--lc-739-) | Medium | 錨點題，完整解在 stack.md |
| 503 | Next Greater Element II | [monotonic_stack § 2-5](./monotonic_stack.md#2-5-next-greater-element-ii-lc-503--circular-monotonic-stack) | Medium | 完整解在 monotonic_stack.md |
| 901 | Online Stock Span | [monotonic_stack § 2-6](./monotonic_stack.md#2-6-online-stock-span-lc-901--monotonic-decreasing-stack) | Medium | 完整解在 monotonic_stack.md |
| 907 | Sum of Subarray Minimums | [monotonic_stack § 2-7](./monotonic_stack.md#2-7-sum-of-subarray-minimums-lc-907--monotonic-stack) | Medium | 完整解在 monotonic_stack.md |
| 2104 | Sum of Subarray Ranges | [monotonic_stack § 2-12](./monotonic_stack.md#2-12-sum-of-subarray-ranges-lc-2104--dual-monotonic-stack-contribution-method) | Medium | 完整解在 monotonic_stack.md |
| 402 | Remove K Digits | [monotonic_stack § 2-8](./monotonic_stack.md#2-8-remove-k-digits-lc-402--monotonic-increasing-stack) | Medium | 完整解在 monotonic_stack.md |
| 316 | Remove Duplicate Letters | [monotonic_stack § 2-8-1](./monotonic_stack.md#2-8-1-remove-duplicate-letters--lc-316) | Medium | 完整解在 monotonic_stack.md |
| 84 | Largest Rectangle in Histogram | [monotonic_stack § 2-2](./monotonic_stack.md#2-2-largest-rectangle-in-histogram-lc-84--monotonic-increasing-stack) | Hard | 完整解在 monotonic_stack.md |
| 155 | Min Stack | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) | Medium | 錨點題，完整解在 stack.md |
| 144 | Binary Tree Preorder Traversal | [stack § Template 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) | Easy | 錨點題，完整解在 stack.md |
| 145 | Binary Tree Postorder Traversal | [stack § Template 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) | Easy | 錨點題，完整解在 stack.md |
| 232 | Implement Queue using Stacks | [queue § Template 5](./queue.md#template-5-queue-using-stacks-pattern--lc-232-) | Easy | 完整解在 queue.md |
| 173 | Binary Search Tree Iterator | [bst_advanced § Template 5b](./bst_advanced.md#template-1-lazy-in-order-iterator--lc-173-) | Medium | 完整解在 bst_advanced.md |
| 341 | Flatten Nested List Iterator | [iterator § 2-2](./iterator.md#2-2-flatten-nested-list-iterator--lc-341) | Medium | 完整解在 iterator.md |
| 445 | Add Two Numbers II | [stack § Template 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) | Medium | [卡片](#add-two-numbers-ii--lc-445) |
| 71 | Simplify Path | [string § Variation 6.1](./string.md#variation-61-token-stack--lc-71-simplify-path) | Medium | 完整解在 string.md |
| 388 | Longest Absolute File Path | [stack § Template 6](./stack.md#template-6-scope--context-ledger--lc-388-lc-636-) | Medium | 錨點題，完整解在 stack.md |
| 636 | Exclusive Time of Functions | [stack § Template 6](./stack.md#exclusive-time-of-functions--lc-636) | Medium | 錨點題，完整解在 stack.md |

<!-- e1ace77793a1 -->
## 與頂端抵銷的題目

| LC | 題目 | 完整解在哪裡 |
|---|---|---|
| 1047 | Remove All Adjacent Duplicates In String | [stack § 模板 1](./stack.md#template-1-cancel-against-the-top--lc-1047) — 錨點題 |
| 844 · 946 · 1910 | Backspace String Compare · Validate Stack Sequences · Remove All Occurrences of a Substring | [advanced_simulation § 模板 6](./advanced_simulation.md#variations-of-template-6) |

<!-- db1574a5ebe4 -->
### Remove All Adjacent Duplicates in String II — LC 1209 ⭐⭐⭐⭐

> **關鍵** — 堆疊裡每一筆是一整段**連續段** `[char, length]`，所以堆疊就是化簡後前綴的遊程編碼。相同字元把頂端那段加長；一旦長度達到 `k`，整段就被 pop 掉，下一段露出來接受下一個字元 —— 這就是不用第二趟掃描也能連鎖消除的原因。

**歸約到**：[stack § 模板 1](./stack.md#template-1-cancel-against-the-top--lc-1047) — 抵銷需要 `k` 個相鄰相同字元而不是兩個，所以頂端要帶一個計數。LC 1047 就是 `k = 2`。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 只 push 單一字元、每一步都重數頂端 `k` 個，是 O(n·k)；`k = 10⁴`、n = 10⁵ 時會超時。把計數存在記錄上。
- 計數歸零而不是 pop（`"deeed"`，k = 3）會在頂端留下一段空的連續段，第二個 `d` 就另起一段，連鎖消成 `"dd"` 永遠不會發生。
- Java：用 for-each 走訪 `ArrayDeque` 是頂端 → 底部，答案會反過來；用 `pollLast()` 取出。

**相似題** — LC 1047（`k = 2`，不需要計數）· LC 1544（同一個字母、大小寫相反時抵銷）

<!-- 03791b197399 -->
### Asteroid Collision — LC 735

> **關鍵** — 堆疊裡放的是到目前為止的倖存者，而碰撞只會發生在往**左**飛的新來者（`a < 0`）和往**右**飛的頂端（`top > 0`）之間。新來者在 `while` 迴圈裡不斷擊毀比較小的頂端，所以一顆小行星可以 pop 掉很多顆；迴圈停下時它還活著才 push。

**歸約到**：[stack § 模板 1](./stack.md#template-1-cancel-against-the-top--lc-1047) — 抵銷的判斷是比大小，而且雙方都可能被毀，不只頂端。

<!--CODE-->

<!--CODE-->

**陷阱**
- 只要正負號不同就相撞：`[-2, 1]` 是互相遠離（左邊往左、右邊往右），必須維持 `[-2, 1]`。只有*頂端往右、新來者往左*才會相遇。
- 大小相等時**兩顆都**毀掉：`[8, -8]` 是 `[]`，不是 `[8]` 也不是 `[-8]`。
- 只打一場就把新來者 push：`[10, 2, -5]` 必須讓 `-5` 先擊毀 `2`，再撞上 `10` 而毀掉，得到 `[10]`。碰撞是 `while`，不是 `if`。

<!-- 2d517ad91a84 -->
## 括號配對的題目

| LC | 題目 | 完整解在哪裡 |
|---|---|---|
| 20 | Valid Parentheses | [stack § 模板 2](./stack.md#template-2-bracket-matching--lc-20-) — 錨點題 |
| 921 | Minimum Add to Make Parentheses Valid | [greedy_examples § 7](./greedy_examples.md#7-minimum-add-to-make-parentheses-valid--lc-921--a-balance-counter) — 只有一種括號，所以堆疊退化成計數器 |
| 1249 | Minimum Remove to Make Valid Parentheses（索引堆疊） | [string § 模板 7](./string.md#template-7-in-place-char-array--mark-then-rebuild--lc-1249-) — 不用堆疊的寫法是下面這張卡片 |

<!-- 9750b6738acf -->
### Minimum Remove to Make Valid Parentheses — LC 1249

> **關鍵** — 索引堆疊的解法在 [string § 模板 7](./string.md#template-7-in-place-char-array--mark-then-rebuild--lc-1249-)；
> 這張是不用堆疊的版本。只有一種括號時，堆疊可以縮成一個計數器，但計數器記不住多出來的 `(` *在哪裡*，所以按方向拆開：左 → 右刪掉每個前面沒有開著的 `(` 的 `)`，再右 → 左刪掉每個後面沒有開著的 `)` 的 `(`。每一趟只刪不可能配對的括號，所以刪得最少。

**歸約到**：[stack § 模板 2](./stack.md#template-2-bracket-matching--lc-20-) — 堆疊退化成它的大小，每個方向各跑一次。它回答的是 follow-up *「不用索引堆疊做得到嗎？」*，而且和下面 LC 32 的計數掃描是同一個雙向技巧。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 只跑一趟左 → 右：`"(()"` 會留下多出來的 `(`，因為 `open_cnt` 最後只是停在 1。反方向的第二趟才會把它刪掉。
- 第二趟之後忘了反轉：`")((a)"` 會回傳 `")a("`。

<!-- bf6dd914b495 -->
### Score of Parentheses — LC 856

> **關鍵** — 每個堆疊槽位存的是**這一層目前收集到的分數**。`(` 開一個新框（push `0`）；`)` 關掉它並併進父層：空框代表 `"()"`，得 `1` 分，非空的框加倍，所以父層加上 `max(2 * inner, 1)`。

**歸約到**：[stack § 模板 2](./stack.md#template-2-bracket-matching--lc-20-) — 堆疊為每個開著的深度存一份部分**結果**，而不是左括號本身。

<!--CODE-->

<!--CODE-->

**陷阱**
- 沒有最外層的 `0` 框：`"()()"` 在第一個 `)` 就把堆疊 pop 空，分數沒地方加。堆疊要先放 `[0]`。
- 只寫 `2 * inner` 會把 `"()"` 算成 `0`；`max(..., 1)` 就是 base case。

<!-- 2e2cab66f6d3 -->
### Minimum Number of Swaps to Make the String Balanced — LC 1963

> **關鍵** — 用堆疊把每一對配好的 `[]` 抵銷掉，剩下的永遠是 `"]" * m + "[" * m`：每個沒配對的 `]` 都排在每個沒配對的 `[` 前面。把最外側的 `]` 和最外側的 `[` 交換一次，能修好那 m 對中的**兩**對，所以答案是 `ceil(m / 2)`。

**歸約到**：[stack § 模板 2](./stack.md#template-2-bracket-matching--lc-20-) — 當作化簡器用：掃描後只剩不平衡的核心，答案是它大小的一個公式。

<!--CODE-->

**陷阱**
- 每遇到 `]` 就 pop、不檢查頂端是不是 `[`：在 `"]]][[["` 上兩個 `]` 會互相抵銷，剩下 `"][[["` 而不是 `"]]][[["`。pop 之前先確認頂端配得上。
- 用 `m // 2` 而不是 `(m + 1) // 2`：`"][]["` 的 m = 1，需要交換一次，不是零次。
- 堆疊裡永遠是 `]` 在下、`[` 在上，所以只數未配對的 `]` 也能得到同一個 `m`，空間 O(1)。

<!-- 7e5d69d5bf3c -->
### Longest Valid Parentheses — LC 32 ⭐⭐⭐⭐

> **關鍵** — push **索引**，並讓堆疊最底下那個元素是目前合法區段前一格的索引（*基準*，初始放 `-1`）。遇到 `)` 先 pop：如果堆疊空了，這個 `)` 沒有配對，成為新的基準；否則 `i - stack[-1]` 就是以 `i` 結尾的合法區段長度。

**歸約到**：[stack § 模板 2](./stack.md#template-2-bracket-matching--lc-20-) — 存索引而不是字元，再加一個把「配對成功」變成「長度」的基準哨兵。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**另外兩個值得知道的解法。** 堆疊是要先寫的那個；這兩個各自教了堆疊沒教的東西。

| 做法 | 時間 | 空間 | 它教你什麼 |
|---|---|---|---|
| 索引堆疊 + `-1` 基準（上面） | O(n) | O(n) | 基準哨兵把一次配對變成一個長度 |
| 1D DP — `dp[i]` = **以 `i` 結尾**的最長合法區段 | O(n) | O(n) | 跳過以 `i-1` 結尾的區段，找到 `s[i]` 的另一半 |
| 兩趟計數掃描（左→右，再右→左） | O(n) | **O(1)** | 一個計數器看不見多出來的 `(`，所以要倒著再掃一次 |

**1D DP。** 合法區段只能以 `)` 結尾，所以 `s[i] == '('` 時 `dp[i] = 0`。遇到 `)`：如果 `s[i-1] == '('`，就是一對新括號黏在前一段後面，`dp[i] = dp[i-2] + 2`；否則以 `i-1` 結尾的區段長 `dp[i-1]`，所以 `s[i]` 的另一半只可能在 `open_idx = i - dp[i-1] - 1`，如果那裡是 `(`，則 `dp[i] = dp[i-1] + 2 + dp[open_idx-1]` —— 最後一項把另一半之前剛結束的那段也黏上來，這是大家最常忘的部分。

<!--CODE-->

<!--CODE-->

**兩趟計數掃描。** 左 → 右數 `opens` 和 `closes`：`opens == closes` 時結束一段長度 `2 * closes` 的合法區段，`closes > opens` 代表這個 `)` 永遠配不到，兩個都歸零。左 → 右在 `"(()"` 上永遠看不到 `opens == closes`，所以再跑一趟右 → 左的鏡像，在 `opens > closes` 時歸零。每一段合法區段至少會被其中一趟抓到；這是唯一 O(1) 空間的解。

<!--CODE-->

**陷阱**
- 沒有放 `-1`：每段從索引 `0` 開始的區段（`"()"`）都會把堆疊 pop 空，永遠量不到。
- push 字元而不是索引：整個技巧就是 `i - stack[-1]`，所以堆疊必須存位置。
- DP：漏了 `dp[open_idx - 1]` 那一項，會把 `"()(())"` 算成 4 而不是 6。
- 計數掃描：只掃一個方向在 `"(()"` 上會回傳 0；鏡像的那一趟是必要的。

<!-- 61b0777865ac -->
## 單調堆疊的題目

單調堆疊題在 [monotonic_stack.md](./monotonic_stack.md) 裡完整解出；核心形式是 [stack § 模板 3](./stack.md#template-3-monotonic-stack--next-greater--smaller--lc-739-)。

| LC | 題目 | 完整解在哪裡 |
|---|---|---|
| 496 | Next Greater Element I | [monotonic_stack § 2-3](./monotonic_stack.md#2-3-next-greater-element-i-lc-496--monotonic-stack--hashmap) |
| 739 | Daily Temperatures | [stack § Template 3](./stack.md#template-3-monotonic-stack--next-greater--smaller--lc-739-) — 錨點題 |
| 503 | Next Greater Element II | [monotonic_stack § 2-5](./monotonic_stack.md#2-5-next-greater-element-ii-lc-503--circular-monotonic-stack) |
| 901 | Online Stock Span | [monotonic_stack § 2-6](./monotonic_stack.md#2-6-online-stock-span-lc-901--monotonic-decreasing-stack) |
| 907 | Sum of Subarray Minimums | [monotonic_stack § 2-7](./monotonic_stack.md#2-7-sum-of-subarray-minimums-lc-907--monotonic-stack) |
| 2104 | Sum of Subarray Ranges | [monotonic_stack § 2-12](./monotonic_stack.md#2-12-sum-of-subarray-ranges-lc-2104--dual-monotonic-stack-contribution-method) |
| 402 | Remove K Digits | [monotonic_stack § 2-8](./monotonic_stack.md#2-8-remove-k-digits-lc-402--monotonic-increasing-stack) |
| 316 | Remove Duplicate Letters | [monotonic_stack § 2-8-1](./monotonic_stack.md#2-8-1-remove-duplicate-letters--lc-316) |
| 84 | Largest Rectangle in Histogram | [monotonic_stack § 2-2](./monotonic_stack.md#2-2-largest-rectangle-in-histogram-lc-84--monotonic-increasing-stack) |

<!-- a06349d7a5ac -->
## Min Stack 的題目

| LC | 題目 | 完整解在哪裡 |
|---|---|---|
| 155 | Min Stack | [stack § 模板 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) — 錨點題，附只存最小值的省空間第二個堆疊 |

<!-- 85355253cd85 -->
## 顯式堆疊的題目

| LC | 題目 | 完整解在哪裡 |
|---|---|---|
| 144 · 145 | Binary Tree Preorder / Postorder Traversal | [stack § 模板 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) — 錨點題 |
| 232 | Implement Queue using Stacks | [queue § 模板 5](./queue.md#template-5-queue-using-stacks-pattern--lc-232-) |
| 173 | Binary Search Tree Iterator | [bst_advanced § 模板 5b](./bst_advanced.md#template-1-lazy-in-order-iterator--lc-173-) — 暫停式的中序走訪 |
| 341 | Flatten Nested List Iterator | [iterator § 2-2](./iterator.md#2-2-flatten-nested-list-iterator--lc-341) — 子元素倒序 push，在 `hasNext()` 裡展開 |

<!-- c4909b7141ad -->
### Add Two Numbers II — LC 445

> **關鍵** — 單向鏈結串列只能往前走，但加法要從最低位開始。把每個節點的值 push 進堆疊，就能**不修改輸入**地拿到倒序；再用「往前插入」建出答案，等於反轉第二次，所以結果是最高位在前。

**歸約到**：[stack § 模板 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) — 堆疊存還沒做的工作，並以相反順序交回來，這裡是兩條串列的數字。

<!--CODE-->

<!--CODE-->

**陷阱**
- 兩個堆疊都空了就停：`[9, 9] + [1]` 會掉最後的進位，回傳 `[0, 0]` 而不是 `[1, 0, 0]`。還有進位時也要繼續迴圈。
- 把每條串列轉成整數再相加，是 [add_x_sum § 2-7](./add_x_sum.md#2-7-add-two-numbers-ii--lc-445) 的做法；它只在 Python 可行，因為 Python 的整數沒有上限 —— 到 100 位數時 Java 的 `long` 就溢位了。
- 先把兩條串列反轉是 O(1) 額外空間，但會修改輸入，而 follow-up（「不要修改輸入串列」）正好排除它。

**相似題** — LC 2（最低位在前：不需要堆疊）· LC 234（全部 push，再拿前端和 pop 出來的比）

<!-- ad354678cc78 -->
## 作用域帳本的題目

| LC | 題目 | 完整解在哪裡 |
|---|---|---|
| 388 | Longest Absolute File Path | [stack § Template 6](./stack.md#template-6-scope--context-ledger--lc-388-lc-636-) — 錨點題，外加「覆寫深度槽位」的變形 |
| 636 | Exclusive Time of Functions | [stack § Template 6](./stack.md#exclusive-time-of-functions--lc-636) — 第二個錨點題 |
| 71 | Simplify Path | [string § Variation 6.1](./string.md#variation-61-token-stack--lc-71-simplify-path) — `..` 會 pop 掉上一層目錄 |
