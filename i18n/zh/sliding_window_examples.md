<!-- 8270219bf097 -->
# 滑動視窗 — 實戰題解

> **範圍** — 不屬於任何模板錨定題的滑動視窗 LeetCode 實作題，每題一張題目卡，依它歸約到的主文件模板歸檔；模板、概念與決策表都留在滑動視窗主文件。
> **另見**：[sliding_window.md](./sliding_window.md) — 每個範例歸約到的七個標準模板，以及在它們之間做選擇的決策表；[sliding_window_advanced.md](./sliding_window_advanced.md) — 較少見的視窗型態（帶重置的恰好 K、單字層級、同時處理多個視窗）；[hash_map.md](./hash_map.md) — 這些視窗大多帶著的頻率表；[2_pointers_examples.md](./2_pointers_examples.md) — 相向指標的題目集。

<!-- c0d9acb2df0d -->
## LeetCode 題目清單

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [String](https://leetcode.com/problem-list/string/)

<!-- d0fee3a94179 -->
## 總覽

這裡的每一題，都是 [sliding_window.md](./sliding_window.md) 裡某個模板換掉一個欄位：視窗狀態、合法性判斷，或記錄什麼。每張卡都寫出它是哪個模板、多出哪一步，所以請先讀模板。模板自己的錨定題（LC 643、1004、3、209、76、992、4067）在那邊的卡片裡解，這裡不重複。

<!-- 365923cc4a4c -->
## 索引

| 模式 | 本頁題目（由易到難） | 核心 ⭐ |
|---|---|---|
| [模板 1 — 固定大小視窗](./sliding_window.md#template-1-fixed-size-window--lc-643-) | LC 1151 | — |
| [模板 3 — 滿足 P 的最長視窗](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) | LC 424 · LC 1838 | LC 424 |
| [模板 6 — 用至多相減求恰好 K](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-)（它的計數欄位） | LC 413 · LC 713 | — |

下面這些滑動視窗題在別份文件裡解，用的是同一個做法，所以這裡不重複。

| LC | 題目 | 放在 |
|---|---|---|
| 438 | Find All Anagrams in a String | [hash_map § 模板 5](./hash_map.md#template-5-fixed-window-character-counts--lc-438) — 用字母計數比對的固定視窗 |
| 567 | Permutation in String | [2_pointers § 模板 8](./2_pointers.md#template-8-grow-right-then-shrink-left--lc-567) — 同一個視窗，第一次相符就回傳 |
| 763 | Partition Labels | [string § 模板 8](./string.md#template-8-greedy-partition-by-last-occurrence--lc-763-) — 在最後出現位置貪心切開，不是會收縮的視窗 |
| 1004 | Max Consecutive Ones III | [sliding_window § 模板 2](./sliding_window.md#template-2-grow-then-shrink-the-while-invariant--lc-1004-) — 這個模板的錨定題 |

<!-- 05277f5da8e2 -->
## 固定大小視窗範例

<!-- 5279e8abb90b -->
### Minimum Swaps to Group All 1's Together — LC 1151

> **關鍵** — 所有的 1 最後會落在某個寬度恰為 `ones = sum(data)` 的區塊裡，區塊中每個 0 都要花一次交換。所以答案是 `ones −（任一寬度為 ones 的視窗中最多的 1 數）`。

**歸約到**：[sliding_window § 模板 1](./sliding_window.md#template-1-fixed-size-window--lc-643-) — 視窗寬度由輸入推得（`ones`），分數是 1 的個數而不是總和。

<!--CODE-->

**陷阱**
- 全是 0（`[0, 0, 0]`）：`ones = 0` 給出寬度為 0 的視窗，而 `data[right - 0]` 會把剛加入的格子又移掉；在迴圈前就回傳 0。

**相似題** — LC 2134 *（環狀陣列上的同一題：在 `data + data` 上跑視窗）*

<!-- 9d001b2e15f7 -->
## 最長視窗範例

<!-- cbff3f8188ac -->
### Longest Repeating Character Replacement — LC 424 ⭐⭐⭐⭐⭐

> **關鍵** — 一個視窗最多換 `k` 個字元就能變成同一個字母，若且唯若 `len − maxFreq ≤ k`。收縮時從不調低 `maxFreq`：過期的值可能讓視窗停在不合法的區段上，但那時視窗只會平移（不會變長），所以 `best` 只有在真的出現更高的 `maxFreq`、可能有更長合法視窗時才會變大。

**歸約到**：[sliding_window § 模板 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) — 合法性判斷改成 `len − maxFreq ≤ k`，而不是「沒有重複」。

<!--CODE-->

<!--CODE-->

**第二種做法 — 每個字母一個視窗。** 對每個不同的字母各跑一次模板 3，問「這個視窗能不能全變成 `letter`？」。它是 O(26·n) 而不是 O(n)，但沒有過期的 `maxFreq` 要解釋，所以在壓力下比較容易說明它是對的。

<!--CODE-->

**陷阱**
- 在收縮之前就記錄 `best`，量到的是不合法的視窗：`"ABAB", k = 0` 會回傳 2 而不是 1。
- 每一步重算 `max(count.values())` 也是對的，但每步要 O(26)；收縮時調低 `max_freq` 沒有任何好處，因為過期的值永遠不會把 `best` 灌大。

**相似題** — LC 1004 *（二元字母表：數 0 的個數，取代 `len − maxFreq`）* · LC 2024

<!-- d2c5973a24cc -->
### Frequency of the Most Frequent Element — LC 1838

> **關鍵** — 排序之後，一個視窗最便宜的目標值就是它最右邊的值，把整個視窗補到這個值的成本是 `nums[right] · len − windowSum`。視窗往右變大時這個成本只會增加，所以它是模板 3 的合法性判斷。

**歸約到**：[sliding_window § 模板 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) — 先做 O(n log n) 排序，合法性是 `nums[right] · len − windowSum ≤ k`。

<!--CODE-->

<!--CODE-->

**陷阱**
- `right` 從 1 開始又讓 `windowSum = 0`，會把 `nums[0]` 漏在總和外，每個成本都被高估：`[1, 2, 4], k = 5` 回傳 2 而不是 3。（這裡原本的 Java 版就是這樣。）
- `nums[i] = 10⁵, n = 10⁵` 時 `nums[right] * len` 會讓 `int` 溢位：用 `long` 相乘。

<!-- fecc6e4cd887 -->
## 恰好 K 計數範例

<!-- b9f98bf81a45 -->
### Arithmetic Slices — LC 413

> **關鍵** — 一段結束在 `i`、有 `run + 2` 個元素的等差段，恰好包含 `run` 個結束在 `i` 的等差切片（長度 3 … run + 2）。所以維護 `run`，每個延續這段的索引都把它加進答案，公差改變時把它歸 0。

**歸約到**：[sliding_window § 模板 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-)（它的計數欄位） — 視窗是目前這一段，公差改變時是重置而不是收縮，而且每一步加的是 `len − 2` 而不是 `len`，因為一個切片至少要三個元素。

<!--CODE-->

**陷阱**
- 少於三個元素（`[1]`、`[1, 2]`）：迴圈一次都不跑，答案是 0。
- 從每個 `i` 往回看到公差斷掉為止也是對的，但在一整段長等差（`[1, 2, …, n]`）上是 O(n²)；`run` 把這個數往前帶，省掉回看。

**相似題** — LC 446 *（子序列而非子陣列：在公差的 map 上做 DP，見 [dp.md](./dp.md)）*

<!-- 8fa2d3430f59 -->
### Subarray Product Less Than K — LC 713

> **關鍵** — 值都是正數時，視窗變大乘積只會變大，所以「乘積 < k」本身就是一個至多條件：收縮之後，每個結束在 `right`、起點落在 `[left, right]` 的子陣列都合法，共 `right − left + 1` 個。

**歸約到**：[sliding_window § 模板 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) — 只有 `at_most` 那一半；條件本來就是「至多」，所以不用相減。

<!--CODE-->

<!--CODE-->

**陷阱**
- `k ≤ 1`：沒有任何視窗合法，少了這個防護 `while` 會把 `left` 推過 `right`、讀到陣列外：`[1, 2, 3], k = 0` 會丟出 `IndexError` 而不是回傳 0。
- 記錄 `max(len)` 而不是累加 `len`，回答的是「最長」而不是「有幾個」。

**相似題** — LC 992 *（恰好 K 個相異：這個計數做兩次）* · LC 2302
