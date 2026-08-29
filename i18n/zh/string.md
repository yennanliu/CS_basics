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

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- bc2f17f69de7 -->
### **模式 1：雙指標**
- **說明**：從字串兩端處理，或用快慢指標
- **例題**：LC 125、344、345、680、917
- **模式**：頭尾指標往中間靠攏

<!-- 678848a12286 -->
### **模式 2：滑動視窗**
- **說明**：找出具備特定性質的子字串
- **例題**：LC 3、76、159、340、424、567
- **模式**：擴張視窗，條件滿足時收縮

<!-- d29cb579fad4 -->
### **模式 3：字串比對**
- **說明**：在文字中找模式（KMP、Rabin-Karp）
- **例題**：LC 28、214、459、686、796
- **模式**：預處理模式，或用滾動雜湊

<!-- 64c67d55fd77 -->
### **模式 4：回文**
- **說明**：檢查或尋找回文子字串
- **例題**：LC 5、125、131、409、516、647
- **模式**：從中心擴張，或用 DP

<!-- a35122a228b6 -->
### **模式 5：字串轉換**
- **說明**：在不同字串格式之間轉換
- **例題**：LC 6、8、12、13、38、443
- **模式**：依規則解析再重組

<!-- 51dd389387dd -->
### **模式 6：字串 DP**
- **說明**：在字串上做動態規劃
- **例題**：LC 10、44、72、115、583、1143
- **模式**：用二維 DP 表比較字串

<!-- 00f5f76ff2c2 -->
### **模式 7：漸進式前綴驗證**
- **說明**：驗證一個單字能不能從它的前綴一個字元一個字元蓋出來
- **例題**：LC 720
- **模式**：先排序 + 用 HashSet 記錄可蓋出的單字 + 檢查直接前綴
- **關鍵技巧**：只需要檢查 `word.substring(0, word.length() - 1)` 在不在集合裡

<!-- 4ad7423c886d -->
### **模式 8：Run-Length 分組（連續相同字元的組）** ⭐⭐⭐⭐
- **說明**：把字串壓成**連續相同字元的組**，再在組長度陣列上解題
- **例題**：LC 696、38、443、1446、485、1004、1759
- **模式**：`s` → `[len(g1), len(g2), ...]` → 從相鄰組長度算出答案
- **關鍵技巧**：LC 696 中，每一對**相鄰的組**貢獻 `min(g[i-1], g[i])` 個合法子字串

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

<!-- 5d81d896e62e -->
### Template 1：雙指標掃描與原地反轉 — LC 125, LC 344 ⭐⭐⭐⭐⭐
> 一般性的雙指標模式（快慢指標、陣列上的左右指標）見 [2_pointers.md](./2_pointers.md)；可刪一個字元的變形（LC 680）和中心擴張見 [palindrome.md](./palindrome.md)。留在這裡的是字元層級的邊掃邊換。

<!--CODE-->

<!--CODE-->

<!-- aa88cb69c159 -->
### Template 2：字元頻率與 anagram 簽章 — LC 242, LC 438, LC 49 ⭐⭐⭐⭐⭐

**模式**：兩個字串互為 anagram 的充要條件是它們的**字元多重集合**相等。這一個簽章想法會以三種面貌出現 —
直接比較多重集合、在固定大小的視窗上**滾動**它，或把它當成 **hash key** 來分組。

**關鍵想法**：簽章就是 `Counter(s)`（或 `int[26]`）；把字元排序後的 tuple 是同一個簽章的可雜湊形式。

> 假設已經 `from collections import Counter`。細節在別處：[hashing.md](./hashing.md) 擁有 LC 242 和 LC 49 以及它們的
> Java 實作，而 [sliding_window.md](./sliding_window.md) 擁有 LC 438 / LC 567 的固定視窗機制。

<!--CODE-->

**容易踩到的坑**
- ⚠️ `Counter(a) == Counter(b)` 是 O(n)，但會配置記憶體；用 `int[26]` 的差值計數才是 O(1) 空間的寫法。
- ⚠️ 滾動視窗時要**把值為零的項刪掉** — `Counter` 會留著 `0`，然後就再也比不相等了。
- ⚠️ `sorted(s)` 是 `list`，不可雜湊；key 必須是 `tuple(sorted(s))` 或 `"".join(sorted(s))`。

<!-- 7922b2adcebc -->
### Template 3：Run-Length 分組（連續相同字元的組） — LC 696 ⭐⭐⭐⭐

**核心想法**

不要去列舉所有子字串（O(n²)），而是**把字串壓成連續的組**，然後在（小得多的）組長度陣列上解題。

<!--CODE-->

對 **LC 696（Count Binary Substrings）** 來說，每個合法子字串一定長成 `0…01…1` 或 `1…10…0`，
也就是說它必須**恰好跨過兩個相鄰組之間的一道邊界**。
長度分別是 `a` 和 `b` 的兩組之間的邊界，剛好產生 `min(a, b)` 個合法子字串：

<!--CODE-->

> **為什麼是 `min(a, b)`？** 你可以挑一個配對數量 `k = 1, 2, ..., min(a, b)`，
> 然後取邊界左邊 `k` 個字元 + 右邊 `k` 個字元。任何 `k > min(a, b)` 都會溢進第三組，
> 破壞「連續分組」這個規則。

**模板 — 建出組陣列（O(n) 時間、O(n) 空間）**

<!--CODE-->

**模板 — 串流／O(1) 空間（只需要 `prev` + `cur` 兩組）**

<!--CODE-->

<!--CODE-->

**容易踩到的坑**
- ⚠️ **記得沖掉最後一組。** 迴圈只有在看到邊界時才會結算一組，所以最後一組永遠沒被配對到 —
  迴圈結束後一定要再加一次 `min(prev, cur)`。
- ⚠️ **長度為 1 的組也是合法的組** — 不要把 `len == 1` 濾掉。
- ⚠️ 把 `prev` 初始化成 `0`（不是 1），這樣第一道邊界貢獻的是 `min(0, cur) = 0`。
- ⚠️ 迴圈從 `i = 1` 開始，比較 `s[i]` 和 `s[i-1]`，避免索引越界。

**相似題目（Run-Length 分組）**

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

<!-- 89d49b30ade3 -->
### Template 4：字串轉換 — 解析後重建 — LC 8, LC 12 ⭐⭐⭐⭐
<!--CODE-->

<!-- 2b4efeab8ba2 -->
### Template 5：貪婪打包整行 + 分配空白（文字換行） — LC 68 ⭐⭐⭐⭐⭐

**模式**：先貪婪地在一行塞進最多能放的單字，再把剩下的空白*攤*到各個間隙上。
所有換行斷句／欄位排版的題目都是這兩個階段。

**關鍵想法**：打包時，`words[i..j]` 需要的寬度是 `sum(len) + (間隙數量)` — 間隙數量剛好是
`j - i`，所以塞不塞得下的檢查是 `lineLen + len(words[j]) + (j - i) <= maxWidth`。分配時，
`base = spaces / slots`，而且**前 `spaces % slots` 個間隙各多拿一個空白**（左重規則）。

<!--CODE-->

<!--CODE-->

**容易踩到的坑**
- ⚠️ **最後一行是靠左對齊**，不是左右對齊 — 只有一個單字的那一行也一樣。
- ⚠️ 剩下的空白要**往左堆**：當 `k < spaces % slots` 時，第 `k` 個間隙拿 `base + 1`。
- ⚠️ 每一行輸出都必須**剛好** `maxWidth` 個字元 — 靠左對齊的情況記得補空白。
- ⚠️ `slots == 0` 時，如果忘了單一單字那個分支就會除以零。

> 同一套「先把片段收進 list、最後 join 一次」的紀律，套到三位數一組上就是
> LC 273 Integer to English Words — 見 [string_examples.md](./string_examples.md)。

<!-- a26b4c0b25fe -->
### Template 6：解析結構化文字（分隔符切割 + 深度／堆疊） — LC 388 ⭐⭐⭐⭐

**模式**：輸入是一個*序列化後的結構*（路徑、log、縮排樹）。用分隔符切開，然後維護一個
**堆疊（或是深度 → 前綴長度的對應表）**來描述目前的上下文，而不是回頭重掃字串。

**關鍵想法**：永遠不要搬子字串 — 搬的是**長度／token**。`depthLen[d]` = 深度 `d` 的路徑前綴長度，
所以深度 `d` 的檔案在 O(1) 內就能算出 `depthLen[d] + len(name)`。

<!--CODE-->

<!--CODE-->

**容易踩到的坑**
- ⚠️ `'\t'` 是**一個**字元 — 不要當成 4 個空白的縮排。
- ⚠️ 沒有檔案時要回傳 `0`（`"a"` → `0`），而不是最長的目錄路徑。
- ⚠️ 每次都覆寫 `depthLen[depth+1]` 是對的：只有*當前*這條分支有意義。
- ⚠️ 每個目錄要 `+1` 是因為那個 `'/'` 分隔符；檔案本身後面不接斜線。

<!-- 3eafc568aef6 -->
#### 變形 6.1：Token 堆疊 — LC 71 Simplify Path

*轉折*：一樣是切割後用堆疊的形狀，但堆疊裡放的是 **token**，而且 `..` 是彈出而不是推入。

<!--CODE-->

<!--CODE-->

- ⚠️ `"..."` / `"....."` 是**合法的目錄名稱** — 只有剛好等於 `".."` 才彈出。
- ⚠️ 結果一定以 `/` 開頭，而且絕不以 `/` 結尾（純根目錄 `"/"` 除外）。

<!-- 344d6d3d3089 -->
### Template 7：原地 char 陣列 — 先標記再重建 — LC 1249 ⭐⭐⭐⭐⭐

**模式**：當一題「刪掉某些字元」需要的是那些違規者的*索引*時，就轉成 `char[]`，
第一趟用一個哨兵值**標記**要刪的位置，第二趟再**重建**。
這樣可以避免反覆 `substring`／字串串接造成的 O(n²)。

**關鍵想法**：堆疊裡放的是**索引，不是字元**，所以掃完之後還留在堆疊上的東西，
剛好就是還要刪掉的位置。

<!--CODE-->

<!--CODE-->

**容易踩到的坑**
- ⚠️ 哨兵值要挑一個**輸入裡不可能出現**的（這裡是 `'*'`；Python 用空字串也行，
  因為 `"".join` 會直接跳過它）。
- ⚠️ 別忘了**第二次沖洗** — 堆疊上還坐著那些沒配對到的 `'('`。
- ⚠️ 在迴圈裡用 `substring` 刪字元，會讓它變成 O(n²)，而且會把後面每個索引都往前推。

**相關**：LC 20 Valid Parentheses 是同一套掃描，但只需要一個*布林值*（掃完堆疊是不是空的）；
LC 32 Longest Valid Parentheses 則沿用這個索引堆疊來量 `i - stack.peek()`。

<!-- 3fdda2bf9fae -->
### Template 8：依最後出現位置貪婪切分 — LC 763 ⭐⭐⭐⭐

**模式**：把字串切成**最多段**，同時讓某個性質保持在局部（例如每個字母只出現在一段裡）。
先算出每個字元的最後索引，再一邊掃一邊把當前切點往右撐。

**關鍵想法**：當前這段不可能在 `max(last[c])`（對目前看過的所有 `c`）之前結束。
當 `i == end`，裡面沒有任何東西還能碰到更右邊 → **就在這裡切**。

<!--CODE-->

<!--CODE-->

**容易踩到的坑**
- ⚠️ `last` 要在**獨立的第一趟**建好；你不可能一邊切一邊知道未來。
- ⚠️ 切的條件是 `i == end`，**不是** `i == last[s[i]]`（後面的字元可能已經把 `end` 往右推了）。
- ⚠️ 這其實是換皮的貪婪*區間合併*：把 `[first[c], last[c]]` 這些區間合併起來。

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
