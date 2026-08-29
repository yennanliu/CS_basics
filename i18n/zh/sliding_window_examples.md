<!-- c4e8a33857a1 -->
# 滑動視窗 — 實戰範例

> **範圍** — 滑動視窗的 LeetCode 實作目錄，每題每語言只留一份標準解，並歸到它所對應的模板底下；模板本身、概念與選擇表都留在主檔滑動視窗那份。
> **另見**：[sliding_window.md](./sliding_window.md) — 每個範例所對應的六個標準模板，以及在它們之間做選擇的決策表；[sliding_window_advanced.md](./sliding_window_advanced.md) — 比較少見的視窗形狀（雙端佇列取極值、補集、以字為單位、分桶）；[hash_map.md](./hash_map.md) — 這些視窗大多要帶著的頻率表；[2_pointers_examples.md](./2_pointers_examples.md) — 對撞指標的實作目錄。

<!-- c0d9acb2df0d -->
## LeetCode 題目清單

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [String](https://leetcode.com/problem-list/string/)

<!-- 16122133931b -->
## 總覽

這裡每一題的解法，都是 [sliding_window.md](./sliding_window.md) 六個模板的其中一個，把三個空格填好而已。標題已經標明用的是哪一個模板，所以先讀模板，這份文件就會變成一組模板的實例，而不是九道各自為政的題目。

<!-- bf98e13a6f4e -->
### 題目索引

| # | 題目 | LC# | 模板 | 語言 |
|---|---------|-----|----------|-------------|
| 1 | Permutation in String | 567 | 1 — 固定長度 | Java, Python |
| 2 | Find All Anagrams in a String | 438 | 1 — 固定長度 | Java, Python |
| 3 | Minimum Swaps to Group All 1's Together | 1151 | 1 — 固定長度（`k = #ones`） | Python |
| 4 | Max Consecutive Ones III | 1004 | 3 — 最長視窗 | Java |
| 5 | Longest Repeating Character Replacement | 424 | 3 — 最長視窗 | Java, Python |
| 6 | Frequency of the Most Frequent Element | 1838 | 3 — 最長視窗（先排序） | Java |
| 7 | Subarray Product Less Than K | 713 | 6 — 計數槽 | Java, Python |
| 8 | Arithmetic Slices | 413 | 自訂 — 回看連續段計數 | Python |
| 9 | Partition Labels | 763 | 自訂 — 貪婪的最後出現位置視窗 | Java |

> LC 3、LC 76、LC 209 **不在**這裡：它們是 [sliding_window.md](./sliding_window.md) 中模板 3、5、4 的實作本體，不重複陳述。

<!-- 30eacfa613f8 -->
## 固定長度視窗

<!-- 039f68ccb2ed -->
### 1) Permutation in String — LC 567

*模板 1。視窗固定為 `len(s1)`；因為長度固定，直接比對兩張頻率表就夠划算了 — 不需要 `have`／`need` 計數器。*

> 維護長度為 len(s1) 的視窗字元頻率；檢查是否與 s1 的頻率相符。

<!--CODE-->

<!--CODE-->

<!-- 4d005d8fcf69 -->
### 2) Find All Anagrams in a String — LC 438

*模板 1。視窗跟 LC 567 一模一樣，只是改成蒐集每個起始索引，而不是在第一次配對成功時就回傳。*

> 與 LC 567 相同，但要蒐集所有 anagram 視窗配對成功的起始索引。

<!--CODE-->

<!--CODE-->

<!-- 94be2af0606d -->
### 3) Minimum Swaps to Group All 1's Together — LC 1151

*模板 1，但視窗長度是推導出來的：視窗寬 `k = sum(data)`，答案是 `ones - 任一這種視窗內的最大 1 數量` — 留在視窗裡的 0 剛好就是需要交換的次數。*

<!--CODE-->

<!-- dd4091ddee36 -->
## 最長視窗（可變、求最大）

<!-- 7727a726d65b -->
### 4) Max Consecutive Ones III — LC 1004

*模板 3 最純粹的樣子：視窗狀態就是一個計數器（`zeroCnt`），合法性判斷是 `zeroCnt <= k`。*

> 右邊擴張，當 0 的數量超過 k 時從左邊收縮。

<!--CODE-->

<!-- b92c47b5932e -->
### 5) Longest Repeating Character Replacement — LC 424

*模板 3，合法性判斷改成 `windowLen - maxFreq <= k`：不管視窗裡出現最多次的是哪個字元，其他全部都得被替換掉。*

> 視窗合法的條件是（視窗長度 - 最大頻率）<= k；一邊擴張一邊追蹤最大頻率。

<!--CODE-->

<!--CODE-->

**第二份 Java 版本，留下來是有理由的** — 這是*另一個*演算法，不是同一份程式碼換個寫法：對每個相異字母各跑一個獨立視窗，然後問「這個視窗能不能全部變成 `letter`？」。複雜度是 O(26·n) 而不是 O(n)，但完全不用維護 `maxFreq`，所以在面試壓力下要論證它是對的容易得多。

<!--CODE-->

<!-- 9e48f6f503e6 -->
### 6) Frequency of the Most Frequent Element — LC 1838

*模板 3，前面先做一次 O(n log n) 排序。排序才是讓視窗有意義的關鍵：一個視窗最便宜的目標值一定是它最右邊那個數，所以把整個視窗拉平的成本就是 `nums[r] * windowSize - windowSum`。*

> 先排序；右邊擴張，當拉平視窗的成本超過 k 時從左邊收縮。

<!--CODE-->

<!-- 85c4962e20c8 -->
## 計數型視窗

<!-- eae0ed10f3d5 -->
### 7) Subarray Product Less Than K — LC 713

*模板 6 的計數槽，但少了扣減那一步：條件（`product < k`）本身就已經是「至多」了，所以每一步做 `count += r - l + 1` 就是全部的答案。*

> 當乘積 >= k 時從左邊收縮；每個合法的右端位置貢獻 (r-l+1) 個子陣列。

<!--CODE-->

<!--CODE-->

<!-- 2f38e59a0f46 -->
## 自訂視窗形狀

<!-- 42515952ce2e -->
### 8) Arithmetic Slices — LC 413

*這不是雙指標視窗，而是一個回看的連續段計數器。每個延續等差連續段的索引，會為它後面每一段仍然成立的延伸各貢獻一個切片。之所以放在這裡，是因為 LeetCode 把它歸類在滑動視窗，而且「一路往回看到條件斷掉為止」的迴圈用的是同一種直覺。*

<!--CODE-->

<!-- 077d78a52c2e -->
### 9) Partition Labels — LC 763

*貪婪視窗，沒有收縮階段：先預先算出每個字元最後出現的索引，掃描時一路把 `end` 往外拉，`i == end` 時就切一刀。這個視窗只會變大，然後重新開始。*

<!--CODE-->

<!-- bdeeb6e982ca -->
## 總結與速查

| 填進去的槽 | LC 567 / 438 | LC 1004 | LC 424 | LC 1838 | LC 713 |
|---|---|---|---|---|---|
| 視窗狀態 | 字元頻率表 | `zeroCnt` | 字元頻率 + `maxFreq` | `windowSum` | 累計 `product` |
| 合法性判斷 | 長度 `== len(p)` | `zeroCnt <= k` | `len - maxFreq <= k` | `nums[r]*len - sum <= k` | `product < k` |
| 結果更新 | 表相符時 `res.add(l)` | `max(len)` | `max(len)` | `max(len)` | `count += r - l + 1` |
| 模板 | 1 | 3 | 3 | 3（排序後） | 6（計數槽） |

- **記得刪掉次數歸零的 key。** 這裡每個以表為基礎的視窗，都倚賴 `map.size()`／`len(dict)` 是真正的相異元素個數。只遞減不刪除會讓它虛胖，收縮迴圈就會出錯。
- **字母集有界就改用陣列。** LC 567／438 用 `int[26]` 搭配 `Arrays.equals`，比拿 `HashMap` 來比對又快又短。
- **在正確的位置記錄答案。** 最長視窗類的題目在收縮迴圈*之後*更新；最短視窗類的題目在迴圈*裡面*更新。

這些範例所實例化的模板見 [sliding_window.md](./sliding_window.md)；不在六個模板裡的視窗形狀見 [sliding_window_advanced.md](./sliding_window_advanced.md)。
