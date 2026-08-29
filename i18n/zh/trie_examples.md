<!-- 8613e77b202c -->
# Trie — 實戰題解

> **範圍** — [trie.md](./trie.md) 背後的題解倉庫：五道題，從把結構蓋出來，一路到在格子上搜尋——字典樹把「每個單字各走一次」壓成「只走一趟」。
> **另見**：[trie.md](./trie.md) — 母表：九個模板、進階變形與面試筆記；[string.md](./string.md) — 不需要字典樹的字串演算法；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 單一模式串的替代解法；[dfs.md](./dfs.md) — LC 79 與 LC 212 裡的格子走訪；[backtrack.md](./backtrack.md) — 讓 LC 212 正確的那個「撤銷選擇」步驟。

<!-- 6af82a6e0a34 -->
## LeetCode 題目清單

- [Trie](https://leetcode.com/problem-list/trie/)
- [String](https://leetcode.com/problem-list/string/)

<!-- 0e6fe13861fa -->
## 概覽

這裡是 [trie.md](./trie.md) 的長尾——原本那份有 67% 都是範例。母表留下九個模板與各種變形，這份檔案則收「實際套用它們」的題目。

<!-- 1aada2183333 -->
### 關鍵性質
- **複雜度**：每次插入或查詢是 O(L)，L 是單字長度，跟裡面存了多少單字無關——這正是蓋字典樹的全部理由
- **核心想法**：字典樹把「拿這個前綴去比對每一個單字」變成「走一條路徑」，所以當同一個前綴會被反覆詢問時，它才划算
- **什麼時候用**：等母表的模板對照表告訴你這題需要哪一種走法之後

<!-- e42270637613 -->
## 把結構蓋出來

<!-- 414e7c95d8b8 -->
### 2) Add and Search Word — LC 211 — 帶萬用字元的走訪 ⭐⭐⭐⭐


**關鍵想法**：`addWord` 用一般的字典樹；`search` 則寫成 `helper(word, idx, node)` 遞迴，依 `ch == "."`（每個子節點都試）或 `ch != "."`（只走那一個對得上的子節點）分兩條路。

<!--CODE-->

**第二種寫法——用 `defaultdict(Node)` 取代明寫的 dict。** 一樣的字典樹、一樣的遞迴；差別在於 `children` 會在存取時自動生出子節點，所以 `addWord` 不必再寫 `if ch not in ...`，萬用字元的走訪也可以把 `node.children[ch]` 直接餵給遞迴。值得看一眼，因為網站上多數 Python 解法都長這樣，而且這個自動生成同時也是它的陷阱：對不存在的 key 做一次「查詢」就會默默插進去一個，所以 `search` 絕不能在沒有守衛的分支外去索引 `children`。

<!--CODE-->

<!--CODE-->

<!-- 39a2593a9496 -->
## 拿它來搜尋

<!-- 3c43bd96b118 -->
### 4) Word Search — LC 79 — 格子 DFS，還用不到字典樹

<!--CODE-->
<!--CODE-->

<!-- 418f5644c911 -->
### 5) Word Search II — LC 212 — 有字典樹這題才做得動 ⭐⭐⭐⭐

<!--CODE-->

<!--CODE-->

<!--CODE-->

---
