<!-- f47e8b90606c -->
# 回溯 — 實戰題解

> **範圍** — 不是模板錨定題的那些回溯題（LC 17、784、996、93、140、980、22），每一題都是一張 problem card，歸檔在它所歸約到的 [backtrack.md](./backtrack.md) 模板底下；錨定題本身在那邊自己的 card 上解，這份不教任何自己的模板。
> **另見**：[backtrack.md](./backtrack.md) — 這裡每張 card 歸約到的模板，以及挑模板用的決策表；[backtrack_advanced.md](./backtrack_advanced.md) — Hard 等級的模板（LC 212、282、301）；[tree_backtrack.md](./tree_backtrack.md) — 樹上 root→leaf 的路徑題；[dfs.md](./dfs.md) — 不需要撤銷的走訪。

<!-- 2de3e520ab98 -->
## LeetCode 題目清單

- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Recursion](https://leetcode.com/problem-list/recursion/)

<!-- 70c7b8ee624a -->
## 總覽

這是 [backtrack.md](./backtrack.md) 的題目集：模板在那邊，題目在這邊。每張 card 寫出它歸約到哪個模板、以及它多加的那一步。各節依核心表的模板順序排列；題目都已在自己 card 上解完的模板，這裡就沒有那一節。

如果某張 card 看起來陌生，回到核心表它依靠的那一段：

| 如果這個看起來陌生 | 回去看 |
|---|---|
| 為什麼要 `path.pop()`，什麼時候不需要 | [Template 14](./backtrack.md#template-14-when-to-undo--mutable-vs-immutable-state--lc-113-) |
| 遞迴呼叫傳 `i` 還是 `i + 1` | [Template 2](./backtrack.md#template-2-start_idx--i-vs-i--1--lc-39-) |
| 跳過重複的值 | [跳過重複](./backtrack.md#duplicate-skipping--the-same-level-skip-rule-) |
| 剪枝時用 `break` 還是 `continue` | [排序、去重、剪枝 — 何時](./backtrack.md#sort-dedup-prune--when) |
| 從題目敘述挑出正確的形狀 | [決策表](./backtrack.md#decision-table--which-backtrack-shape-) |

<!-- 849d2b110cd7 -->
## 索引

| LC | 題目 | 歸約到 | 難度 |
|---|---|---|---|
| 17 | [Letter Combinations of a Phone Number](#letter-combinations-of-a-phone-number--lc-17) | [Template 1](./backtrack.md#template-1-choose--explore--un-choose--lc-216-) 選擇 → 探索 → 撤銷 | Medium |
| 784 | [Letter Case Permutation](#letter-case-permutation--lc-784) | [Template 3](./backtrack.md#template-3-subsets--lc-78-) 子集，選／不選的寫法 | Medium |
| 996 | [Number of Squareful Arrays](#number-of-squareful-arrays--lc-996) | [Template 5](./backtrack.md#template-5-permutations--lc-46-) 排列 + LC 47 的跳過規則 | Hard |
| 93 | [Restore IP Addresses](#restore-ip-addresses--lc-93) | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) 選在哪裡切 | Medium |
| 140 | [Word Break II](#word-break-ii--lc-140) | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) 選在哪裡切，加記憶化 | Hard |
| 980 | [Unique Paths III](#unique-paths-iii--lc-980) | [Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) 標記、遞迴、還原 | Hard |
| 22 | [Generate Parentheses](#generate-parentheses--lc-22) | [Template 12](./backtrack.md#template-12-pruning-at-the-choice--lc-526-) 遞迴前先檢查 | Medium |

<!-- 767d2c084e45 -->
### 在別處有完整解

這些題目只在擁有它們的那張 card 上解一次。列在這裡，是讓搜尋這個題號的人有地方可落腳。

| LC | 題目 | 所在 |
|---|---|---|
| 216 | Combination Sum III | [backtrack.md § Template 1](./backtrack.md#template-1-choose--explore--un-choose--lc-216-)（錨定題） |
| 39 / 40 | Combination Sum / II | [backtrack.md § Template 2](./backtrack.md#template-2-start_idx--i-vs-i--1--lc-39-) 與 [§ Template 7](./backtrack.md#template-7-combination-sum--lc-39--lc-40-)（錨定題） |
| 78 | Subsets | [backtrack.md § Template 3](./backtrack.md#template-3-subsets--lc-78-)（錨定題） |
| 90 | Subsets II | [backtrack.md § Template 4](./backtrack.md#template-4-subsets-ii-skip-same-level-duplicates--lc-90-)（錨定題） |
| 46 / 47 | Permutations / II | [backtrack.md § Template 5](./backtrack.md#template-5-permutations--lc-46-)（錨定題，LC 47 在它底下） |
| 77 | Combinations | [backtrack.md § Template 6](./backtrack.md#template-6-combinations--lc-77)（錨定題） |
| 79 | Word Search | [backtrack.md § Template 9](./backtrack.md#template-9-grid--word-search--lc-79-)（錨定題） |
| 526 | Beautiful Arrangement | [backtrack.md § Template 12](./backtrack.md#template-12-pruning-at-the-choice--lc-526-)（錨定題） |
| 1219 | Path with Maximum Gold | [dfs.md § Template 9](./dfs.md#template-9-grid-dfs-with-restore-backtracking--lc-1219)（錨定題）；標記存續時間的逐步追蹤在 [backtrack.md § Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) 底下 |
| 139 | Word Break | [dp.md § Template 1b](./dp.md#template-1b-prefix-partition-dp--lc-139-)（錨定題）：只問切法*存不存在*，所以是 DP，不是回溯 |
| 207 | Course Schedule | [topology_sorting_examples.md](./topology_sorting_examples.md)：`visiting` 集合是在偵測環，不是在搜尋答案 |

<!-- da6948e0f564 -->
## 選擇 → 探索 → 撤銷 範例

<!-- 8415b14af5dc -->
### Letter Combinations of a Phone Number — LC 17

> **關鍵** — 深度就是 `digits` 的索引，而每一層有它**自己**的選項清單（`digits[i]` 對應的字母）。
> 兩層之間不會從同一個池子取，所以不需要 `start_idx`，也不需要 `used[]`：每個數字都選了一個字母，
> 路徑就完成了。

**歸約到**：[backtrack.md § Template 1](./backtrack.md#template-1-choose--explore--un-choose--lc-216-) — 選項清單改由目前這一層決定，而不是 `start..n`。

<!--CODE-->

<!--CODE-->

第二種解法，留下來是因為它是不同的演算法，而不只是換個寫法：**一層一層**建出答案，完全不遞迴。
它同時保留所有部分組合，所以要 O(4^n) 記憶體，而回溯只保留一條路徑。

<!--CODE-->

**陷阱**
- `digits = ""` 必須回傳 `[]`。只靠 base case 會回傳 `[""]`，也就是一個空組合。
- 撤銷時移除的是**最後一個**字元。`deleteCharAt(0)` 在 `"2"` 會過，在 `"23"` 會錯。

<!-- 407998de2c19 -->
## 子集範例

<!-- 5cfdf9edbe0b -->
### Letter Case Permutation — LC 784

> **關鍵** — 雖然名字叫排列，字元的順序其實是固定的：每個**字母**是二選一（小寫或大寫），
> 每個數字只有一種選擇。這就是子集那棵樹的選／不選寫法，每個位置一次二元決定，所以 L 個字母
> 有 2^L 個葉節點。

**歸約到**：[backtrack.md § Template 3](./backtrack.md#template-3-subsets--lc-78-) — 選／不選的寫法，兩個分支換成「小寫／大寫」。

<!--CODE-->

**陷阱**
- 把它當成 Template 5（排列）去重排字元：`"a1b2"` 有 4 個答案，不是 4! × 4。
- 在數字上也分支，會讓每個答案重複一倍。

<!-- 5f7cdf8f4d38 -->
## 排列範例

<!-- d80b5905aefc -->
### Number of Squareful Arrays — LC 996

> **關鍵** — 排列迴圈裡有兩道過濾，都在遞迴**之前**做：LC 47 的同層跳過規則（輸入排序後，
> 相等的左鄰居不在路徑上時就跳過 `nums[i]`），讓相等的值不會被數兩次；以及對 `path[-1]` 的
> 相鄰檢查，讓一對數不合格時那個分支當場結束。

**歸約到**：[backtrack.md § Template 5](./backtrack.md#template-5-permutations--lc-46-) — 加上 [LC 47 的跳過規則](./backtrack.md#permutations-with-duplicates--lc-47) 與一道兩兩檢查。

<!--CODE-->

**陷阱**
- 沒有先排序，跳過規則比較的是不相干的鄰居：`[2, 2, 2]` 必須得到 `1`。
- `int(x ** 0.5)` 遇到很大的完全平方數可能會往下捨入；`math.isqrt` 不會。

<!-- 59213869d152 -->
## 回文切分範例

<!-- 75a37b38ee7e -->
### Restore IP Addresses — LC 93

> **關鍵** — 跟 LC 131 同一個「選在哪裡切」的迴圈，只是關卡不同：一段是 1–3 位數、不超過 255、
> 而且沒有前導零。有兩種情況會結束一個分支：四段剛好用完**整個**字串（記錄下來），或四段之後
> 還剩數字（丟掉）。讓它變快的剪枝：還要放 `k` 段時，剩下的數字必須介於 `k` 到 `3k` 個之間，
> 所以一個不可能完成的分支在遞迴之前就被剪掉。

**歸約到**：[backtrack.md § Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) — 關卡是「合法的一段」，深度上限是 4。

<!--CODE-->

**手動追蹤** — `s = "0000"`：

<!--CODE-->

**陷阱**
- `"0000"` → `["0.0.0.0"]`：單獨一個 `0` 是合法的一段；只有**更長**的段不能以 `0` 開頭。
- 湊滿四段就記錄、卻沒檢查 `start == len(s)`，會回傳漏掉數字的位址。
- 只在一開始檢查 `len(s) > 12`，在根節點以下什麼都剪不掉：`"1111111111"`（10 位數）切出
  `["1"]` 後剩 9 個數字給 3 段，已經是死路。每次呼叫都檢查的 `k..3k` 界線會立刻把它丟掉。

<!-- 0f4f5e505d74 -->
### Word Break II — LC 140

> **關鍵** — 在前綴是字典單字的地方切開，再解剩下的後綴。從 `start` 開始的後綴，不論前綴怎麼切，
> 拼出來的句子都一樣，所以用 `start` 當鍵把它們快取起來。沒有快取的話，`"aaaa…aaab"` 配
> `["a", "aa", "aaa"]` 會以指數次數重解同樣的後綴，最後還是什麼都找不到。

**歸約到**：[backtrack.md § Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) — 關卡是 `piece in words`，遞迴回傳後綴的句子，好讓它能被記憶化。

<!--CODE-->

**陷阱**
- 迴圈跑字典、測試接起來的前綴（而不是切 `s`），會在拒絕之前先建出比 `s` 還長的句子。
- 如果只問切法*存不存在*，那是 LC 139：布林 DP，
  [dp.md § Template 1b](./dp.md#template-1b-prefix-partition-dp--lc-139-)。

<!-- aa95ec802c2d -->
## 格子範例

<!-- 5c3c2d84db94 -->
### Unique Paths III — LC 980

> **關鍵** — LC 79 的標記 → 遞迴 → 還原骨架，但每條路徑都要走到底，所以不能提早回傳：改成計數。
> 只有走過**每一個**可走格子之後抵達終點，那條路徑才算數，所以一個 `remaining` 計數器跟著遞迴往下傳。

**歸約到**：[backtrack.md § Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) — 回傳計數，終點多一個「走遍每一格」的條件。

<!--CODE-->

**陷阱**
- 每次抵達終點都算一次：`[[1,0,0,0],[0,0,0,0],[0,0,2,-1]]` 會得到遠多於 `2` 的數。
- 像 LC 79 那樣第一次成功就回傳，計數會停在 `1`。

<!-- c690a07cf714 -->
## 剪枝範例

<!-- df75f7fc3bc9 -->
### Generate Parentheses — LC 22

> **關鍵** — 帶著兩個計數，在放括號**之前**檢查：已開的 `(` 少於 `n` 時可以放 `(`，已放的 `)`
> 少於 `(` 時可以放 `)`。這樣建出來的每個前綴都是合法前綴，每個葉節點都是答案，沒有任何東西是
> 建出來只為了被丟掉。

**歸約到**：[backtrack.md § Template 12](./backtrack.md#template-12-pruning-at-the-choice--lc-526-) — 遞迴前的檢查就是括號是否平衡。

<!--CODE-->

<!--CODE-->

**陷阱**
- 產生全部 `2^(2n)` 個字串再在葉節點逐一驗證，是最直覺的第一個想法，也是指數級的浪費：兩個計數讓驗證變得多餘。
- 是 `close < open`，不是 `close < n`：後者會允許 `")("`。
