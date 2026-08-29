<!-- 50efd4a0ffaf -->
# 字典樹（Trie）

> **範圍** — 前綴樹 — insert/search/startsWith、節點該怎麼擺，以及那些「共用前綴」就是全部訣竅的題目（自動補完、單字搜尋、XOR trie）。
> **另見**：[trie_examples.md](./trie_examples.md) — 這些模板背後的五道實作題；[string.md](./string.md) — 不用 trie 的字串處理；[hash_map.md](./hash_map.md) — 只要存整個單字的集合就夠時；[dfs.md](./dfs.md) — trie 題目底下跑的那套走訪；[advanced_string_algorithms.md](./advanced_string_algorithms.md) — 以後綴為基礎的替代方案。

> 只要題目一次丟出很多個字串，就該先想想 trie 能不能幫上忙。
- https://leetcode.com/problems/search-suggestions-system/solution/

<!-- 6af82a6e0a34 -->
## LeetCode 題目清單

- [Trie](https://leetcode.com/problem-list/trie/)
- [String](https://leetcode.com/problem-list/string/)

<!-- 666bef171a30 -->
## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大  |
| -------------- | -------- | -------- | -------- | -------- |
| Trie           | O(L)     | O(L)     | O(L)     | O(L)     |

> **L = 鍵值（單字）的長度** — 複雜度跟存了幾個鍵值無關。最小／最大 = 字典序最小／最大的鍵值。

<!-- 1cc29736889e -->
## 0) 概念
- https://blog.csdn.net/fuxuemingzhu/article/details/79388432
- 樹 + dict
    - `put Node into dict`（例如 defaultdict(Node)）

<p align="center"><img src="../pic/trie_1.png"></p>

<p align="center"><img src="../pic/trie_2.png"></p>

<!-- 0d35b1f39c31 -->
### 0-1) 分類

- **雜湊表版 trie** — `children` 是 dict／`Map`；字母集合可以任意（見下面的 Pattern）。
- **陣列版 trie** — `children` 是固定的 `[None] * 26` / `TrieNode[26]`；只處理小寫時最快（模板 2）。
- **萬用字元 trie** — 靠對所有子節點做 DFS 來支援 `.` 比對（模板 3，LC 211）。
- **二元（XOR）trie** — 子節點就是 bit `0/1`；用在 max-XOR／位元運算題（模板 5，LC 421）。

<!-- a648655d6a7c -->
## 模板與演算法

模板 1 就是 [0-2) Pattern](#0-2-pattern) 裡那個雜湊表版的 trie。底下全部都是同一個結構，
只改動一件事：裝子節點的容器、走訪到某個節點時允許做什麼，或者「字母表」是什麼。

<!-- 17b56dac0cb9 -->
### 模板 2：陣列版 Trie（固定字母集合）
<!--CODE-->

<!--CODE-->

<!-- 706f4862ab32 -->
### 模板 3：支援萬用字元的 Trie — LC 211
<!--CODE-->

<!--CODE-->

<!-- 21a90ccf6aea -->
### 模板 4：自動補完 Trie — LC 1268
<!--CODE-->

<!--CODE-->

<!-- 99705a8432b6 -->
### 模板 5：二元 Trie（XOR 題） — LC 421 ⭐⭐⭐
<!--CODE-->

<!--CODE-->

<!-- d650d438f7f0 -->
### 模板 6：支援刪除的 Trie

**刪除演算法 — 三步驟遞迴邏輯：**
1. 一路走到單字結尾；如果這個單字不存在，回傳 `False`。
2. 把結尾節點的 `is_end` 取消掉。
3. 回溯的過程中，把已經變成「非終端葉節點」（沒有子節點、也不是別的單字的結尾）的子節點移掉 — 這一步負責清掉懸空的節點。

**關鍵不變量**：只有在一個節點「沒有剩下任何子節點」**而且**「不是另一個單字的結尾」時才能刪。共用的前綴必須留著。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**追蹤 — trie 裡同時有 `"app"` 時執行 `delete("apple")`：**
<!--CODE-->

模板 1-6 沒涵蓋到的模式。每一個都是換一種方式*走*這棵 trie —
trie 本身幾乎沒變。

| 題目裡的關鍵訊號 | 模板 | 實作範例 |
|-----------------------|----------|----------------|
| 「把字串切成字典裡的單字」 | 模板 7 — Trie + DP | LC 139、LC 472 |
| 「把兩個單字接成迴文」 | 模板 8 — 反向單字 trie | LC 336 |
| 「數字的字典序」 | 模板 9 — 隱式的十元數字 trie | LC 386 |
| 「用最短的字根／前綴取代單字」 | 模板 1 的變形 — 在第一個 `is_end` 就停 | LC 648 |

<!-- 9dea64c2f32f -->
### 模板 7：Trie + DP（單字切分） — LC 139 ⭐⭐⭐⭐⭐

**關鍵想法**：`dp[i] = 「s[0..i) 可以被切開」`。從每個到得了的索引 `i` 出發，一次一個字元往 trie 深處走；
每次落在位置 `j` 的 `is_end` 節點上，就把 `dp[j+1] = True`。
trie 取代了內層那個「把字典裡每個單字都試一遍」的迴圈 — 前綴一離開 trie 你就跳出，
所以完全不需要對子字串做雜湊。

<!--CODE-->

<!--CODE-->

**變形 — LC 472 Concatenated Words**：一樣是 trie + DP，但答案需要**至少 2 段**，
而且一個單字不能由它自己組成。訣竅：**依長度排序，邊測邊插入** — 測 `w` 的時候，
trie 裡只有*嚴格更短*的單字，所以任何切得成功的結果自動就用到了 ≥ 2 個單字。

<!--CODE-->

<!-- 6cefe5f2a3f6 -->
### 模板 8：反向單字 Trie（迴文配對） — LC 336

**關鍵想法**：`words[i] + words[j]` 是迴文，若且唯若其中一個單字「蓋住」了另一個的反轉，
而*剩下*的中段本身也是迴文。所以把每個**反轉後**的單字插進 trie，每個節點存兩樣東西：
- `word_index` — 剛好有一個反轉單字結束在這裡
- `palindrome_below` — 那些在這個節點*底下*剩餘後綴是迴文的單字索引

接著拿 `words[i]` 往 trie 下走，情況剛好分成 2 種：

<!--CODE-->

這兩種情況以長度區分、彼此不重疊，所以不會有同一組配對被輸出兩次。

<!--CODE-->

<!--CODE-->

> `""`（空字串）的情況不用特別處理就對了：它會終止在**根節點**，所以 `root.palindromeBelow`
> 剛好就是「所有本身是迴文的單字」。

<!-- 790a46f268bb -->
### 模板 9：隱式數字 Trie（字典序數字） — LC 386

**關鍵想法**：`1..n` 就是一棵你永遠不用真的建出來的**十元 trie** — 節點 `x` 的子節點是
`x*10 .. x*10+9`，根節點則是 `1..9`。對這棵 trie 做**前序 DFS**，吐出來的數字就是字典序。
把 DFS 寫成迭代版，額外空間就是 O(1)。

<!--CODE-->

**移動規則**（整個演算法就這樣）：
- 往**深處**走：`cur * 10`（前提是 `cur * 10 <= n`）
- 否則走到**下一個兄弟**：`cur + 1`
- 如果兄弟不存在（`cur % 10 == 9` 或 `cur + 1 > n`），先用 `cur //= 10` **回溯**

<!--CODE-->

<!--CODE-->

<!-- fb26f84f444f -->
### 模板 1 的變形 — 用最短字根取代 — LC 648

**訣竅**：拿一個單字在普通 trie 上往下走時，**在第一個 `is_end` 節點就停** — 那就是最短的字典字根，
剛好是 LC 648 要你替換上去的東西。

<!--CODE-->

<!--CODE-->

<!-- 0de0d3d03b4b -->
## 1) 一般形式

<!-- a97b6e2b980c -->
### 1-1) 基本操作

看 [0-2) Pattern](#0-2-pattern) 裡的 `insert` / `search` / `startsWith` 骨架，
以及上面那些變形模板（陣列版、萬用字元、自動補完、二元／XOR、刪除）。

<!-- fb3f1f5017bb -->
## 進階 Trie 變形 — XOR Trie、串流比對、刪除

<!-- 31b5e731482d -->
### XOR Trie — LC 421，精簡版參考

> 完整走一遍在上面的 [Template 5](#template-5-binary-trie-xor-problems--lc-421-)；這裡是同一個想法的速查版。
用二元 trie（子節點是 bit 0/1）找出任兩個數字之間的最大 XOR。

<!--CODE-->

<!-- 4877e0baf519 -->
### Trie + DP（串流比對） — LC 1032 Stream of Characters
把**反轉**單字組成的 trie 跟目前為止的串流結合，然後倒著走這段串流。

> **複雜度**：走訪會停在第一個沒有 trie 子節點的字元，所以一次查詢是
> O(L)，L 是字典裡最長的單字 — **不是** O(1)，只要你有節制地保留資料，也不會是 O(stream)。
> 下面這個版本一直往 `self.stream` 塞東西，所以走訪長度受串流長度限制；只保留最後 L 個字元
> （`deque(maxlen=L)`）才能讓每次查詢是 O(L)、記憶體是 O(L)。

<!--CODE-->

<!-- c3eee02f07db -->
### Trie 刪除 — 精簡版參考

> 完整實作在上面的 [Template 6](#template-6-trie-with-delete-operation)；這裡把它維持的不變量單獨拉出來講。

<!--CODE-->

<!-- fb644cef3ddd -->
### 前綴-後綴 Trie — LC 745
要同時比對前綴和後綴的題目，把每個單字包成 `suffix#word` 再插進同一棵 trie。

<!--CODE-->

<!-- 682a28ce5735 -->
### 面試提示 — trie
| 訊號 | 模式 |
|--------|---------|
| 「前綴比對」、「自動補完」 | 標準 trie |
| 「最大 XOR」、「位元最佳化」 | 二元 XOR trie |
| 「字元串流」、「即時比對」 | 反向單字 trie + 狀態 |
| 「前綴**和**後綴都要」 | suffix#word trie |
| 「萬用字元 `.` 比對」 | 在 `.` 節點做 DFS |
| 「數有幾個單字帶這個前綴」 | 在 TrieNode 上加一個 `count` 欄位 |

---

<!-- c1d66d7eb39e -->
### 也能用 trie 解（不需要新模板）

- **LC 14 Longest Common Prefix** — 把所有單字插進去，然後從根往下走，只要節點剛好只有 1 個子節點
  且不是 `is_end` 就繼續；走過的路徑就是答案。（面試時單純的垂直掃描更簡單 — 只有在對方問「重複查詢很多次呢」時才提 trie。）

<!-- b8683b705f84 -->
## 實作範例

五道題目放在 **[trie_examples.md](./trie_examples.md)**：

| 分組 | 題目 |
|---|---|
| [Building the structure](./trie_examples.md#building-the-structure) | LC 208, 211 |
| [Searching with it](./trie_examples.md#searching-with-it) | LC 1268, 79, 212 |

LC 79 放在那裡而且刻意不用 trie：它是讓 LC 212 變得好懂的基準線 —
同樣的格子 DFS，每個單字各跑一次，直到 trie 把所有單字塌縮成同一趟走訪為止。
