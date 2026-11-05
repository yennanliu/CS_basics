<!-- 53a723680611 -->
# 字串演算法與操作

> **範圍** — 日常字串目錄 — 字元層級的雙指標掃描、頻率與 anagram 簽章、run-length 分組、切詞、解析與原地改寫 — 至於題解倉庫、語言層級的字串 API、回文、子字串搜尋與雙序列 DP，則各自有自己的一張表。
> **另見** — *從本檔拆出去的*：[string_examples.md](./string_examples.md) — LC 題解倉庫；[string_operations.md](./string_operations.md) — Python/Java 字串 API、`StringBuilder`、字元運算，以及大小寫／Unicode 的坑。
> *鄰近的表*：[palindrome.md](./palindrome.md) — 回文家族，從中心擴張一路到 Manacher；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 子字串搜尋（KMP、Rabin-Karp）；[advanced_string_algorithms.md](./advanced_string_algorithms.md) — Z-algorithm、後綴陣列、DFA 驗證；[dp_string.md](./dp_string.md) — 雙序列的網格家族；[sliding_window.md](./sliding_window.md) — 由條件驅動的字元視窗；[hashing.md](./hashing.md) — 頻率表與正規化 key；[trie.md](./trie.md) — 前綴結構。

<!-- 4a0eabd6b81f -->
## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)

<!-- a9d2024f6f77 -->
## 總覽
**字串演算法**涵蓋處理、搜尋與操作文字資料的各種技巧。它們是文字處理、模式比對、解析，以及大量面試題的基礎。

<!-- 8a5452e7d68d -->
### 關鍵性質
- **不可變性**：在很多語言裡字串是不可變的（Python、Java）
- **時間複雜度**：走訪通常是 O(n)，土法煉鋼的比對是 O(n²)
- **空間複雜度**：大部分轉換是 O(n)
- **核心技巧**：雙指標、滑動視窗、雜湊、模式比對
- **什麼時候用**：文字處理、模式比對、解析、驗證

<!-- 593b617352d9 -->
### 常見操作
- **搜尋**：找子字串、模式比對
- **操作**：反轉、旋轉、轉換
- **驗證**：回文、anagram、格式合法性
- **解析**：切割、切詞、擷取
- **比較**：字典序、編輯距離  

<!-- 0e18801c45dc -->
## 辨識

從題目敘述找訊號；限制條件的提示來自 [complexity_cheatsheet.md](./complexity_cheatsheet.md) 的那把尺。導向其他表的那幾列是正確答案，不是缺口。

| 題目敘述中的訊號 | 限制條件提示 | 該用什麼 | 不是這個，如果 |
|---|---|---|---|
| 「只看英數字元，是不是回文」、「原地反轉」 | n ≤ 2·10⁵，要求 O(1) 額外空間 | [Template 1](#template-1-two-pointer-scan--in-place-reversal--lc-125-) 雙指標掃描 | 允許刪一個字元，或要找最長回文子字串 → [palindrome.md](./palindrome.md) |
| 「anagram」、「是…的排列」、「把字母相同的單字分在一組」 | 小寫英文字母 → `int[26]` | [Template 2](#template-2-character-frequency--anagram-signatures--lc-242-) 頻率簽章 | 「最多 k 種不同字元的最長子字串」→ 可變視窗，[sliding_window.md](./sliding_window.md) |
| 「連續的 0 和 1」、「相同字元的組」、「壓縮」 | n ≤ 10⁵ → 不能 O(n²) 列舉子字串 | [Template 3](#template-3-run-length-grouping-consecutive-character-groups--lc-696-) run-length 分組 | 最多翻 k 個零 → 直接在原字串上開視窗（LC 1004） |
| 「把字串轉成整數」、羅馬數字、一條條由上往下讀的規則 | 題目點名 32 位元溢位 | [Template 4](#template-4-parse--rebuild-by-phases--lc-8-) 分階段解析 | 帶指數的完整數字文法（LC 65）→ DFA，[advanced_string_algorithms.md](./advanced_string_algorithms.md) |
| 「每一行剛好 maxWidth 個字元」、「左右對齊」 | 總字元數 ≤ 10⁴ | [Template 5](#template-5-greedy-line-packing--space-distribution-text-wrapping--lc-68-) 貪婪打包 + 分配 | 要最小化各行參差的代價 → DP，不是貪婪 |
| 「絕對路徑」、輸入裡有 `\n` 和 `\t`、「簡化路徑」 | | [Template 6](#template-6-parse-structured-text-delimiter-split--depthstack--lc-388-) 切割 + 深度表／token 堆疊 | 有優先順序的算式 → [stack_expression_parsing.md](./stack_expression_parsing.md) |
| 「刪掉最少的字元讓結果合法」 | n ≤ 10⁵ → 不能反覆 `substring` | [Template 7](#template-7-in-place-char-array--mark-then-rebuild--lc-1249-) 先標記再重建 | 只問「合不合法？」→ LC 20，普通堆疊就夠 |
| 「切成盡可能多段」、「每個字母最多只出現在一段」 | | [Template 8](#template-8-greedy-partition-by-last-occurrence--lc-763-) 最後出現位置掃描 | 每段直接以區間給出 → 合併區間（LC 56） |
| 「用清單裡其他單字一次加一個字元蓋出每個單字」 | | 排序 + 集合，只檢查直接前綴 → [string_examples.md](./string_examples.md)（LC 720） | 多個單字之間的前綴查詢 → [trie.md](./trie.md) |
| 「在文字裡找模式」、「重複的子字串」 | n·m 太大 → O(n + m) | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) | 只搜一次且 n·m 放得下 → 內建的 `find` / `indexOf` |
| 「滿足…的最長／最短子字串」 | n ≤ 10⁵ | [sliding_window.md](./sliding_window.md) | |
| 比較或對齊**兩個**字串、「最少操作次數」、「最長公共」 | m·n ≤ 10⁶ | [dp_string.md](./dp_string.md) | |

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 69aaf64b33e9 -->
### 模板比較表
| 模板 | 適用情境 | 複雜度 | 程式碼在哪 |
|---|---|---|---|
| **雙指標掃描／反轉** | 從兩端比較或交換 | O(n) | Template 1 |
| **字元頻率／anagram 簽章** | 「字元一樣嗎？」、「依字元分組」 | O(n) | Template 2 |
| **Run-Length 分組** | 答案取決於相同字元的連續段 | O(n) | Template 3 |
| **解析後重建** | `atoi`、羅馬數字、格式規則 | O(n) | Template 4 |
| **貪婪打包 + 分配** | 換行斷句、欄位排版 | O(total chars) | Template 5 |
| **切割 + 深度／token 堆疊** | 路徑、縮排樹、log | O(n) | Template 6 |
| **標記後重建 `char[]`** | 依索引刪除字元 | O(n) | Template 7 |
| **依最後出現位置切分** | 最多能切成幾段獨立的區塊 | O(n) | Template 8 |
| **子字串搜尋**（KMP、Rabin-Karp、Z） | 精確模式搜尋 | O(n+m) | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| **字元上的滑動視窗** | 具備某性質的最長／最短子字串 | O(n) | [sliding_window.md](./sliding_window.md) |
| **回文**（中心擴張、Manacher） | 回文子字串 | O(n²) / O(n) | [palindrome.md](./palindrome.md) |
| **雙序列 DP** | 編輯距離、LCS | O(mn) | [dp_string.md](./dp_string.md) |
| **Trie** | 多個單字之間的前綴比對 | O(m) | [trie.md](./trie.md) |

<!-- 5964d8f723b5 -->
### Template 1：雙指標掃描與原地反轉 — LC 125 ⭐⭐⭐⭐⭐

> 一般性的雙指標模式（快慢指標、陣列上的左右指標）見 [2_pointers.md](./2_pointers.md)；可刪一個字元的變形（LC 680）和中心擴張見 [palindrome.md](./palindrome.md)。留在這裡的是字元層級的邊掃邊換。

**辨識** — 「只考慮英數字元，是不是回文」、「**原地**反轉字串／母音／只反轉字母」。
n ≤ 2·10⁵ 且要求 O(1) 額外空間 → 不要先做一份清理過的副本，直接從兩端走原字串。

**關鍵點** — `s[left..right]` 以外的每個字元都已經和它的鏡像配對過，所以視窗內第一個不相等
就證明「不是回文」，而 `left >= right` 就證明它是。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：兩個跳過迴圈看起來是巢狀的，但 `left` 只往右、`right` 只往左，
  三個迴圈加起來最多走 n 步。
- **空間 O(1)**：直接讀 `s`，沒有 `cleaned` 副本。

**陷阱**
- 用 `isalpha` 取代 `isalnum` 來跳過：`"0P"` 會把 `0` 跳掉，兩指標相遇，回傳 `True`。
  數字也要算。
- 跳過迴圈拿掉 `left < right`：`".,"` 會讓 `left` 跑出字串尾端，丟出 `IndexError`。
- `cleaned == cleaned[::-1]` 是對的，但要 O(n) 空間；面試官的追問就是「不用副本再做一次」。

**不是這個，如果** — 「最多刪一個字元」（LC 680）或「最長回文子字串」（LC 5）：
它們會分岔或從中心擴張 → [palindrome.md](./palindrome.md)。

**練習** — LC 125 ⭐ · LC 344 · LC 345 *（只交換母音）* · LC 917 *（只交換字母）* ·
LC 151 *（先反轉整個字串，再反轉每個單字）*

<!-- 7f09a502fda0 -->
### Template 2：字元頻率與 anagram 簽章 — LC 242 ⭐⭐⭐⭐⭐

> 細節在別處：[hashing.md](./hashing.md) 擁有 LC 242 和 LC 49 的題解，而
> [sliding_window.md](./sliding_window.md) 擁有 LC 438 / LC 567 的固定視窗機制。

**辨識** — 「anagram」、「是…的排列」、「字元相同」、「把用相同字母的單字分成一組」。
限制條件寫「小寫英文字母」→ 用 `int[26]` 取代 map。

**關鍵點** — 兩個字串互為 anagram 的充要條件是它們的**字元多重集合**相等。這個多重集合就是簽章：
直接比較它（LC 242）、在固定長度的視窗上**滾動**它（LC 438），或用它的可雜湊寫法 —
排序後的字元 — 當成 **key**（LC 49）。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**（LC 242）：兩個字串一起走一趟，再檢查固定的 26 格。LC 49 是
  O(n · k log k)：由每個單字的 `sorted(word)` 決定；改用 26 格計數的 tuple 當 key 就是 O(n · k)。
- **空間 O(1)**（LC 242）：不管 n 多大，`count` 都只有 26 格。LC 49 每個單字存一次，O(n · k)。

**陷阱**
- 省略長度檢查：`isAnagram("a", "ab")` — `zip` 在較短的字串就停了，回傳 `True`。
- `groups[sorted(word)]` 會丟出 `TypeError: unhashable type: 'list'`。要用 `"".join(sorted(word))`
  或 `tuple(...)`。
- LC 438 的滾動視窗：計數歸零就刪掉。Python 3.10 之前，帶著 `0` 項的 `Counter` 和沒有那一項的
  不相等，所以 `"cbaebabacd"`、`"abc"` 會漏掉位置 6 的答案（被移出的 `e` 留成 `e: 0`）；
  刪掉在每個版本都正確。
- `int[26]` 假設是小寫字母。追問「Unicode 呢？」要的是 `HashMap`。

**不是這個，如果** — 「最多 k 種不同字元的最長子字串」：視窗長度不固定，
所以是可變視窗 → [sliding_window.md](./sliding_window.md)。

**練習** — LC 242 ⭐ · LC 383 *（單向：雜誌要涵蓋信件）* · LC 49 ⭐ · LC 438 *（滾動簽章）* ·
LC 567

<!-- a0a18eee33a9 -->
### Template 3：Run-Length 分組（連續相同字元的組） — LC 696 ⭐⭐⭐⭐

**辨識** — 「連續的 0 和 1」、「相同字元的組」、「壓縮字串」、「最長的連續段」。
n ≤ 10⁵ 排除了 O(n²) 列舉子字串；先壓縮，再在組長度陣列上做。

**關鍵點** — LC 696 的每個合法子字串都長成 `0…01…1` 或 `1…10…0`，所以它**恰好跨過一道**
相鄰兩組之間的邊界。長度分別為 `a` 和 `b` 的兩組之間的邊界剛好產生 `min(a, b)` 個：
對 `k = 1 … min(a, b)` 各取邊界兩側 `k` 個字元；再大的 `k` 就會溢進第三組。

<!--CODE-->

串流寫法只保留最後兩組，這就是追問 O(1) 空間時的答案：

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：一趟 `for i in range(1, len(s))`；每個字元不是延長一組就是開一組新的。
- **空間 O(n)** 給 `groups`（每段一格，最壞情況 `"0101…"` 是 n）；串流寫法是 **O(1)**，
  只留兩個整數。

**手動跑一遍**

<!--CODE-->

**陷阱**
- 忘了迴圈後的收尾：`"01"` 會回傳 0 而不是 1，因為迴圈要看到*下一道*邊界才結算一對。
- 用 `prev = 1` 起頭：`"01"` 會回傳 2。用 `prev = 0`，第一道邊界才會如預期加上
  `min(0, cur) = 0`。
- 把長度為 1 的組濾掉：`"010"` 有兩個合法子字串，都是由長度 1 的組構成的。

**不是這個，如果** — 「最多翻 k 個零，求最長的連續 1」（LC 1004）：答案橫跨好幾組，
所以要在原字串上滑視窗 → [sliding_window.md](./sliding_window.md)。

**練習** — LC 696 ⭐ · LC 1446 *（最大組長度）* · LC 830 *（回報長度 ≥ 3 的組）* · LC 443
*（原地寫入 `char + count`）* · LC 38 *（每組輸出 `count + char`，做 n 次）*

| 題目 | LC # | 拿組長度來做什麼 | Difficulty |
|---------|------|------------------------------------|------------|
| Count Binary Substrings | 696 | 對每一對相鄰組加總 `min(g[i-1], g[i])` | Easy |
| Count and Say | 38 | 每組輸出 `count + char`，迭代 n 次 | Medium |
| String Compression | 443 | 原地寫入 `char + count` | Medium |
| Consecutive Characters | 1446 | `max(組長度)` | Easy |
| Max Consecutive Ones | 485 | `1` 那些組的最大長度 | Easy |
| Max Consecutive Ones III | 1004 | 在組上做滑動視窗（最多翻 k 個零） | Medium |
| Max Consecutive Ones II | 487 | 跨過單一個 `0` 組，把兩個 `1` 組合併 | Medium |
| Longest Repeating Char Replacement | 424 | 視窗 + 最高頻率（分組想法的推廣） | Medium |
| Positions of Large Groups | 830 | 回報長度 ≥ 3 的組 | Easy |
| Find Longest Awesome Substring | 1542 | 位元遮罩奇偶性（分組的變形） | Hard |
| Merge Strings Alternately | 1768 | 在連續段上做雙指標 | Easy |

<!-- 007a0c747b1d -->
### Template 4：分階段解析與重建 — LC 8 ⭐⭐⭐⭐

**辨識** — 「把字串轉成 32 位元整數」、「整數轉羅馬數字／羅馬數字轉整數」、「zigzag」、
「把一串字串編碼成一個字串」：題目敘述是一條條由上往下讀的**規則清單**，並且點名了溢位的行為。

**關鍵點** — 一個索引依序走過固定的階段（空白 → 最多一個正負號 → 數字）；每個階段只吃自己的字元，
所以第一個沒有階段接受的字元就結束解析。數值是在**建構的同時**就被夾住，所以永遠不會超出 32 位元範圍。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：索引 `i` 在三個階段迴圈裡只會往前走。
- **空間 O(1)**：數字是原地累加的，沒有切任何片段。（LC 12 的輸出也是 O(1)：
  num ≤ 3999 時最多 15 個符號。）

**陷阱**
- `"+-12"` 回傳 0：吃掉一個正負號之後，`-` 是非數字，結束第三階段。接受第二個正負號是經典 bug。
- Java 的 `num * 10 + digit` 會默默溢位：`"2147483648"` 會變成負數，除非溢位檢查跑在乘法**之前**。
  `"-91283472332"` 必須夾到 `-2147483648`。
- `s.strip()` 也會吃掉尾端空白和 `"\t"`；題目說的只有開頭的 `' '`。在 LeetCode 的測資上無害，
  但讀規則時是錯的。
- `str.isdigit()` 對 `"²"` 也是 `True`；要和 `'0'` … `'9'` 比較。

**不是這個，如果** — 帶正負號、小數點和指數的完整數字文法（LC 65 Valid Number）：
階段多到不適合手寫 → [advanced_string_algorithms.md](./advanced_string_algorithms.md) 裡的 DFA。

**練習** — LC 8 ⭐ · LC 13 *（羅馬轉整數：較小的符號在較大的前面就相減）* ·
LC 12 · LC 6 *（zigzag：一個來回彈跳的列索引）* · LC 271 *（長度前綴編碼：先解析長度，
再讀那麼多個字元）*

<!-- c2a74a5dc58b -->
### Template 5：貪婪打包整行 + 分配空白（文字換行） — LC 68 ⭐⭐⭐⭐⭐

**辨識** — 「每一行剛好 `maxWidth` 個字元」、「左右對齊」、「多出來的空白盡量平均分配，
左邊的間隙拿比較多」。所有換行斷句或欄位排版的題目都是兩個階段：打包一行，再分配它剩下的空白。

**關鍵點** — 打包時，`words[i..j]` 需要的寬度是 `sum(len) + (j - i)`：間隙數量剛好是 `j - i`，
所以塞不塞得下的檢查是 `lineLen + len(words[j]) + (j - i) <= maxWidth`。分配時，每個間隙拿
`base = spaces // slots`，而**前 `spaces % slots` 個間隙各多拿一個**（往左堆）。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(total chars)**：每個單字只被打包一次（內層 `while j` 不會回頭，因為 `i = j` 跳過了它），
  每個輸出字元也只寫一次。
- **空間 O(total chars)**：就是輸出本身；工作狀態只有幾個整數。

**陷阱**
- 把最後一行也左右對齊：`["What","must","be","acknowledgment","shall","be"]`、寬度 16，
  最後一行必須是靠左對齊再補空白的 `"shall be        "`，不是 `"shall          be"`。
- 只有一個單字的那一行：`"acknowledgment"` 單獨一行時 `slots == 0`，除非那個分支放在前面，
  否則 `spaces / slots` 會除以零。
- 往右堆：`"example  of text"`（寬度 16）是 3 個空白分給 2 個間隙；多的那一個給**第一個**間隙。
- 靠左對齊的那一行沒有補到 `maxWidth`，過不了長度剛好的檢查。

**不是這個，如果** — 「最小化各行的總參差度」：對平方空白的代價來說貪婪打包不是最佳 →
在斷行點上做 DP。

**練習** — LC 68 ⭐ · LC 1592 *（只有一行：平均分配空白，多的放到最後）* · LC 273
*（把片段收進 list、最後 join 一次 — 改成三位數一組；見
[string_examples.md](./string_examples.md)）*

<!-- c7b38349169b -->
### Template 6：解析結構化文字（分隔符切割 + 深度／堆疊） — LC 388 ⭐⭐⭐⭐

**辨識** — 輸入是一個**序列化後的結構**：「絕對路徑」、輸入裡有 `\n` 和 `\t`、
「簡化 Unix 路徑」、帶欄位的 log 行。用分隔符切開並維護目前的上下文，永遠不要回頭重掃字串。

**關鍵點** — 搬的是**長度或 token**，永遠不是子字串。`depth_len[d]` 是目前這條分支上深度 `d`
的路徑前綴長度，所以深度 `d` 的檔案在 O(1) 內就是 `depth_len[d] + len(name)`，
而覆寫 `depth_len[d + 1]` 就丟掉了已經結束的分支。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`split` 和數 tab 都只碰每個字元一次；每次查表都是 O(1)。
- **空間 O(max depth)** 給 `depth_len`，外加 `split` 回傳的 O(n) 串列。

**陷阱**
- 算縮排寬度：`'\t'` 是**一個**字元，所以 `"dir\n\tfile.txt"` 的深度是 1，不是 4。
- 完全沒有檔案：`"a"` 回傳 0，不是最長的目錄路徑。
- 檔案後面多接一個 `/`：`+ 1` 只屬於目錄；`"dir\n\tfile.ext"` 是 12（`dir/file.ext`），不是 13。

**練習** — LC 388 ⭐ · LC 1598 *（只需要深度計數）* · LC 71 *（token 堆疊，見下方）* · LC 937
*（每筆 log 切成 id + 內容，再排序）*

<!-- 887a7b67fd69 -->
#### 變形 6.1：Token 堆疊 — LC 71 Simplify Path

> **關鍵點** — 一樣是切割後維護上下文的形狀，但上下文是一個 **token** 堆疊：處理完每個 token 後，
> 堆疊剛好就是到目前為止的正規路徑，所以 `..` 彈出，其他名稱一律推入。

**化約為**：[Template 6](#template-6-parse-structured-text-delimiter-split--depthstack--lc-388-) — 深度表換成 token 堆疊。

<!--CODE-->

<!--CODE-->

- ⚠️ `"..."` / `"....."` 是**合法的目錄名稱** — 只有剛好等於 `".."` 才彈出。
- ⚠️ 結果一定以 `/` 開頭，而且絕不以 `/` 結尾（純根目錄 `"/"` 除外）。

<!-- e494f0def426 -->
### Template 7：原地 char 陣列 — 先標記再重建 — LC 1249 ⭐⭐⭐⭐⭐

**辨識** — 「刪掉最少數量的字元，讓結果合法」、「刪掉這些位置上的字元」。違規者是用**索引**找到的，
而 n ≤ 10⁵ 排除了在迴圈裡用 `substring` 刪除。

**關鍵點** — 堆疊裡放的是 **`'(` 的索引，不是字元**，所以掃完之後它剛好就是沒配對到的 `'('` 位置；
每個沒配對到的 `')'` 在出現當下就被標記了。兩趟：先標記，再一次重建。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：一趟標記、一次清空堆疊（最多 n 個索引）、一趟重建。
- **空間 O(n)**：`char[]` 副本和索引堆疊；`"(((("` 時堆疊會存下每一個索引。

**陷阱**
- 省略第二次清空：`"))(("` 會回傳 `"(("` 而不是 `""` — 沒配對到的 `'('` 要等掃完才知道。
- 哨兵值可能出現在輸入裡：`'*'` 對 LC 1249 是安全的（只有字母和括號）；Python 用空字串最安全，
  因為 `"".join` 會跳過它。
- 在迴圈裡用 `s = s[:i] + s[i+1:]` 刪除是 O(n²)，而且會把後面每個索引往前推，存下來的位置就過期了。

**不是這個，如果** — 只問「合不合法？」（LC 20）只需要布林值，不需要索引；
「最長合法子字串」（LC 32）沿用索引堆疊來量 `i - stack[-1]`。

**練習** — LC 1249 ⭐ · LC 20 *（只驗證合法性）* · LC 921 *（數沒配對到的，不用重建）* ·
LC 32 *（用索引堆疊量長度）*

<!-- da740d6689b1 -->
### Template 8：依最後出現位置貪婪切分 — LC 763 ⭐⭐⭐⭐

**辨識** — 「把字串切成盡可能多段，讓每個字母最多只出現在一段」：在某個性質保持在段內的前提下，
求最多段數。

**關鍵點** — 當前這段不可能在 `max(last[c])`（對這段裡看過的每個 `c`）之前結束。當 `i == end`，
段裡沒有任何東西碰得到更右邊，所以在這裡切是安全的，而在第一個安全點就切，段數最多。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：對 `s` 兩趟互相獨立的掃描。
- **空間 O(1)**：`last` 最多 26 項。

**陷阱**
- 用 `i == last[s[i]]` 取代 `i == end` 來切：`"abab"` 會在第二個 `a` 後面切，回傳 `[3, 1]`；
  正確答案是 `[4]`，因為 `b` 把 `end` 推到了 3。
- 在切分的同一趟裡建 `last`：切的時候看不到後面的出現位置，所以獨立的第一趟是必要的。

**不是這個，如果** — 每段直接以區間給出，而不是從字串推出來：同一套掃描就是合併區間（LC 56），
把 `[first[c], last[c]]` 合併起來。

**練習** — LC 763 ⭐ · LC 2405 *（遇到重複字元就切）* · LC 56 *（區間的形式）*

<!-- 11abb00b0367 -->
### 重量級演算法各自住在哪 ⭐⭐⭐⭐

有四個字串家族大到足以擁有自己的一張表。本表**不會**把它們重新推導一遍；
挑到對的那一列，就直接去那張表。

**子字串搜尋 — 該用哪個演算法？**

| 情境 | 用什麼 | 表 |
|---|---|---|
| 只搜一次，`n·m` 在限制內過得去 | 內建的 — `s.find(p)` / `s.indexOf(p)` | — |
| 單一模式、輸入是刻意刁難的、需要 O(n+m) | **KMP** failure function | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 多個模式，或「有沒有任何長度為 `k` 的視窗重複出現？」 | **滾動雜湊**（Rabin-Karp），用雙雜湊壓掉碰撞 | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 最長的「同時是前綴也是後綴」；週期性 | **KMP failure 陣列**或 **Z-array** | [advanced_string_algorithms.md](./advanced_string_algorithms.md) |
| 對答案長度二分搜尋 + 對視窗做雜湊 | 二分搜尋上面疊滾動雜湊（LC 1044、718） | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 重複子字串、後綴排名 | **後綴陣列／自動機** | [advanced_string_algorithms.md](./advanced_string_algorithms.md) |
| 驗證一個數值／格式化的 token | 手刻 **DFA**（LC 65） | [advanced_string_algorithms.md](./advanced_string_algorithms.md) |

**另外三個家族**

| 家族 | 訊號 | 表 |
|---|---|---|
| **回文** | 「回文子字串／子序列」、「把它變成回文」、可刪一個字元的檢查 | [palindrome.md](./palindrome.md) — 中心擴張、區間 DP、Manacher（LC 5）、KMP 前綴技巧（LC 214）、回文配對（LC 336） |
| **字元視窗** | 「最長／最短的子字串，滿足…」、「最多 k 種相異字元」、「包含 t 的所有字元」 | [sliding_window.md](./sliding_window.md) — LC 3、76、159、340、424、438、567、1004 |
| **雙序列 DP** | 比較／對齊**兩個**字串、「最少操作次數」、「最長共同…」 | [dp_string.md](./dp_string.md) 和 [dp.md](./dp.md) — LC 72、97、115、583、712、1143 |

<!-- 181a78da307b -->
## 字串 API 必備

完整的 API 導覽 — 切片、`split`/`join`、`StringBuilder`、字元運算、大小寫與 Unicode 陷阱 —
已經搬到 [string_operations.md](./string_operations.md)。這裡留的是不用翻開那張表也該記得的子集。

| 任務 | Python | Java |
|---|---|---|
| 字串 → 字元 | `list(s)` | `s.toCharArray()` |
| 字元 → 字串 | `"".join(chars)` | `new String(chars)` |
| 反轉 | `s[::-1]` | `new StringBuilder(s).reverse().toString()` |
| 取子字串 | `s[i:j]` | `s.substring(i, j)` |
| 依空白／分隔符切割 | `s.split()` / `s.split(",")` | `s.trim().split("\\s+")` / `s.split(",")` |
| 用分隔符接起來 | `",".join(parts)` | `String.join(",", parts)` |
| 逐步蓋出字串 | 先 `parts.append(x)` 再 `"".join(parts)` | 先 `StringBuilder.append(x)` 再 `.toString()` |
| 字元 → 碼／碼 → 字元 | `ord(c)` / `chr(n)` | `(int) c` / `(char) n` |
| 對應到 26 個字母的索引 | `ord(c) - ord('a')` | `c - 'a'` |
| 是不是字母／數字／英數 | `c.isalpha()` / `c.isdigit()` / `c.isalnum()` | `Character.isLetter(c)` / `isDigit(c)` / `isLetterOrDigit(c)` |
| 轉小寫 | `c.lower()` | `Character.toLowerCase(c)` |
| 頻率表 | `Counter(s)` | `int[26]` 或 `HashMap<Character,Integer>` |

- ⚠️ **絕對不要在迴圈裡串接字串** — 兩種語言的 `s += x` 都是 O(n²)。收集起來，最後 join。
- ⚠️ Java 的 `String.split` 吃的是**正規表示式**：`split(".")` 會在每個字元處切開；要用 `split("\\.")`。
- ⚠️ Java 的 `s.split(",")` 會丟掉結尾的空欄位；把 limit 傳 `-1` 才會留著。

<!-- fda4a0ae3ed5 -->
## 練習路徑

| 階段 | 題目（易 → 難） | 加了什麼 |
|---|---|---|
| **核心** ⭐ | LC 125 · LC 242 · LC 49 · LC 271 | 雙指標掃描、頻率簽章作為檢查與作為 key、長度前綴解析 |
| **練習** | LC 344 · LC 696 · LC 8 · LC 763 · LC 388 · LC 1249 | 剩下每個模板各一題：反轉、run-length 分組、分階段解析、最後出現位置切分、深度表、先標記再重建 |
| **延伸** | LC 443 · LC 71 · LC 68 | 原地壓縮、token 堆疊、往左堆空白的貪婪打包 |

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- 48041d632a7e -->
### 題目 → 模板決策表 ⭐⭐⭐⭐⭐

看**訊號**那一欄；那是題目敘述裡決定該用哪個做法的關鍵字。

| 題目裡的訊號 | 做法 | 在哪 | 題目 |
|---|---|---|---|
| 從**兩端**比較或交換；原地反轉 | 雙指標，`while left < right` | Template 1 | 125, 344, 345, 541, 917, 925, 151 |
| 「**anagram**」、「是不是某個排列」、「把相同字母的分成一組」 | 字元頻率簽章 | Template 2 | 242, 438, 49, 567, 451 |
| 答案取決於相同字元的**連續段** | 組長度陣列（或串流式的 `prev`/`cur`） | Template 3 | 696, 38, 443, 485, 487, 830, 1004, 1446, 1768, 809 |
| 在**不同格式之間**轉換 — 數字、羅馬數字、Z 字形 | 依明確規則解析，再重建 | Template 4 | 6, 8, 12, 13, 273, 482, 468 |
| **固定行寬**、補空白、欄位排版 | 貪婪打包，再把剩餘空白往左堆分配 | Template 5 | 68, 273 |
| 輸入是**序列化後的結構** — 路徑、log、縮排樹 | 依分隔符切割 + 深度表或 token 堆疊 | Template 6 | 388, 71, 937, 1071 |
| 「**移除**最少的字元，使得…」 | `char[]` + 索引堆疊，先標記再重建 | Template 7 | 1249, 20, 32, 921 |
| 「**最多能切成幾段**」，同時某性質保持在局部 | 掃過最後出現位置，`i == end` 就切 | Template 8 | 763, 56 |
| 從字典裡**一個字元一個字元**蓋出單字 | 排序 + 集合，只檢查直接前綴 | [string_examples.md](./string_examples.md) | 720, 648, 745 |
| 「在那段文字裡**有效率地**找到這個模式」 | KMP、滾動雜湊或 Z-array | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) | 28, 459, 686, 796, 1044, 1392 |
| 「**最長／最短子字串**，滿足…」 | 滑動視窗 | [sliding_window.md](./sliding_window.md) | 3, 76, 159, 340, 424, 1004 |
| 任何和**回文**有關的 | 中心擴張、區間 DP、Manacher、KMP 前綴 | [palindrome.md](./palindrome.md) | 5, 9, 125, 131, 132, 214, 409, 516, 647, 680, 1216, 1312 |
| 比較／對齊**兩個**字串 | 雙序列網格 DP | [dp_string.md](./dp_string.md) | 10, 44, 72, 97, 115, 583, 712, 1143 |
| **很多單字**共用前綴 | 字典樹（Trie） | [trie.md](./trie.md) | 208, 211, 212, 648, 745 |
| 「一個是不是另一個的**旋轉**」 | 先比長度，再看 `goal in (s + s)` | [string_examples.md](./string_examples.md) | 796 |
| 自訂字母表／**比較器**的排序 | 用 `int[26]` 建排名表，比較相鄰的一對 | [string_examples.md](./string_examples.md) | 953, 269, 937 |
| 列舉**切割位置**並驗證每一段 | 巢狀列舉 + 每段各自的合法性規則 | [string_examples.md](./string_examples.md) | 816, 93, 282, 468 |

<!-- d3e09ae67d81 -->
### 複雜度速查
| 操作 | 時間 | 空間 | 備註 |
|-----------|------|-------|-------|
| 雙指標 | O(n) | O(1) | 掃一趟 |
| 滑動視窗 | O(n) | O(k) | k = 視窗內元素數 |
| KMP 搜尋 | O(n+m) | O(m) | m = 模式長度 |
| Rabin-Karp | O(n) 平均 | O(1) | 有雜湊碰撞 |
| 中心擴張 | O(n²) | O(1) | 所有回文 |
| 編輯距離 | O(mn) | O(mn) | 可壓到 O(n) |
| Trie 操作 | O(m) | O(ALPHABET_SIZE * m) | m = 單字長度 |

<!-- 684de0dc942d -->
### 常見技巧

<!-- 6fc845844e09 -->
#### **ASCII 大小寫差值技巧（|char1 - char2| == 32）**

一個很好用的技巧，用來偵測**同一個字母但大小寫不同**（例如 `'a'` 對 `'A'`）：

<!--CODE-->

**為什麼是 32？** 在 ASCII 裡，小寫字母從 97（`'a'`）開始，大寫從 65（`'A'`）開始。同一個字母的差值永遠剛好是 32。

**經典應用：LC 1544 - Make The String Great（堆疊）**
> 一直移除「同字母但大小寫不同」的相鄰配對，直到沒有這種配對為止。

<!--CODE-->

> 這段 Java 掃描只用 `StringBuilder` 的那個寫法已經被拿掉了 — 堆疊語意完全一樣，沒有新的東西。

<!--CODE-->

**關鍵洞見：** 這個技巧可以推廣到任何「相鄰配對互相抵消、而配對由 ASCII 距離定義」的題目。搭配堆疊就是 O(N) 時間、O(N) 空間。

| 檢查 | 意思 | 例子 |
|-------|---------|---------|
| `Math.abs(a - b) == 32` | 同一個字母，大小寫不同 | `'a'` 和 `'A'` |
| `Character.toLowerCase(a) == Character.toLowerCase(b)` | 同一個字母（不管大小寫） | `'a'` 和 `'A'` |
| `a == b` | 完全相同的字元 | `'a'` 和 `'a'` |

<!-- d70c93e623b0 -->
### 常見錯誤與面試建議

**🚫 常見錯誤：**
- 在迴圈裡串接字串（O(n²)）
- 取子字串時差一位
- 沒處理空字串
- 想去修改不可變的字串
- 字元編碼問題

**✅ 最佳實務：**
- 用 StringBuilder／list + join
- 問清楚字元集（ASCII/Unicode）
- 想清楚有沒有分大小寫
- 用特殊字元測一下
- 轉數字時處理溢位

**🎤 面試建議：**

1. **問清楚需求**
   - 字元集是什麼？
   - 分大小寫嗎？
   - 可以原地做嗎？
   - 要處理特殊字元嗎？

2. **從簡單的開始**
   - 先寫暴力解
   - 一步一步優化
   - 說清楚取捨

3. **常見的追問**
   - 處理 Unicode
   - 優化空間
   - 串流處理
   - 平行處理

- **要講出來的邊界情況**：空字串、單一字元、所有字元都相同、非英數字元、大小寫混雜、轉數字時溢位。

<!-- ba863dd66b18 -->
### 其餘的內容在哪

| 表 | 放了什麼 |
|---|---|
| [string_examples.md](./string_examples.md) | LC 題解倉庫 — 本表模板還沒解掉的每一題，每題每種語言一份正典解。 |
| [string_operations.md](./string_operations.md) | 語言層級的 API：Python 切片與各種方法、Java 的 `String`/`StringBuilder`、字元分類，以及蓋字串的效能規則。 |
