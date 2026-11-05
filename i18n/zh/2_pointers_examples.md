<!-- dd30b78e2d1d -->
# 雙指標 — 實戰題解

> **範圍** — 雙指標的 LeetCode 實作題庫，每題一份標準解，依它歸約到的雙指標主文件模板分組；概念、指標種類與模板本身留在雙指標主文件裡。
> **另見**：[2_pointers.md](./2_pointers.md) — 概念，以及每個範例所套用的標準模板；[quickselect.md](./quickselect.md) — 第 k 個元素的選擇，從同一份文件拆出來；[sliding_window.md](./sliding_window.md) — 由條件驅動的視窗；[n_sum.md](./n_sum.md) — 深入 k-sum 家族；[palindrome.md](./palindrome.md) — 深入回文家族。

<!-- 9a25c02a57ed -->
## LeetCode 題目清單

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Array](https://leetcode.com/problem-list/array/)
- [String](https://leetcode.com/problem-list/string/)

<!-- 6dddcbda3263 -->
## 總覽

在某個模板上多加一步的雙指標題目。[2_pointers.md](./2_pointers.md) 放模板；這份文件放題目，
每題歸檔在它歸約到的那張卡底下，差別寫在**歸約到**那一行。完整解法在別份文件的題目，
在最後只留一行指引，不放第二份。

<!-- 893acada2737 -->
## 索引

| 模式 | 題目（易 → 難） | 核心 ⭐ |
|---|---|---|
| [模板 1 — 快慢指標的讀寫壓縮](./2_pointers.md#template-1-fastslow-read-write-compaction--lc-26-) | [LC 283](#move-zeroes--lc-283) · [LC 80](#remove-duplicates-from-sorted-array-ii--lc-80) | — |
| [模板 2 — 兩端向內收斂](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) | [LC 942](#di-string-match--lc-942) · [LC 16](#3sum-closest--lc-16) · [LC 31](#next-permutation--lc-31) · [LC 42](#trapping-rain-water--lc-42) | — |
| [模板 3 — 從中心往外擴](./2_pointers.md#template-3-expand-from-centre--lc-5-) | [LC 647](#palindromic-substrings--lc-647-) · [LC 845](#longest-mountain-in-array--lc-845) | LC 647 |
| [模板 4 — 子序列比對](./2_pointers.md#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) | [LC 524](#longest-word-in-dictionary-through-deleting--lc-524-) · [LC 1023](#camelcase-matching--lc-1023) | — |
| [模板 9 — 兩條序列同步前進](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) | [LC 161](#one-edit-distance--lc-161) · [LC 271](#encode-and-decode-strings--lc-271) | LC 271 |
| [在別處](#pointers) | LC 5 · LC 15 · LC 57 · LC 125 · LC 128 · LC 165 · LC 214 · LC 240 · LC 344 · LC 680 · LC 763 · LC 809 · LC 953 · LC 986 · LC 2104 | LC 5 · LC 15 · LC 57 · LC 125 · LC 128 |

<!-- eb14b2aee232 -->
## 快慢指標壓縮的題目

<!-- a80062af5454 -->
### Move Zeroes — LC 283

> **關鍵** — 模板 1 的壓縮，只是把複製換成**交換**：每個非零值都被換到 `slow`，零就自然
> 漂到尾端，事後不用再補。因為 `slow` 只會往前走，非零值的相對順序保持不變。

**歸約到**：[2_pointers § 模板 1](./2_pointers.md#template-1-fastslow-read-write-compaction--lc-26-) — 保留條件是 `nums[fast] != 0`，寫入改成交換，被丟掉的值會留在尾端。

<!--CODE-->

<!--CODE-->

**陷阱**
- 用複製取代交換（`nums[slow] = nums[fast]`），`[0,1,0,3,12]` 會變成 `[1,3,12,3,12]`：
  複製的寫法需要第二趟，從 `slow` 寫零到結尾。
- `slow == fast` 時的交換（`[1,2,3]`，沒有零）只是自己換自己，無害；加上
  `if slow != fast` 只是省寫入，不是修正錯誤。

**類似** — LC 27（丟掉某個值，模板 1）· LC 905（偶數在前）· LC 2460（先套用操作，再移動零）

<!-- 2601876ddbb4 -->
### Remove Duplicates from Sorted Array II — LC 80

> **關鍵** — 「最多 k 份」就是把模板 1 的保留條件往回挪 k 格：當 `nums[fast]` 跟
> `nums[slow - k]`（**k 次寫入之前**的值）不同時才保留。陣列已排序，所以那裡的值相同就代表
> 已經留了 k 份。LC 26 就是 k = 1 的同一個迴圈。

**歸約到**：[2_pointers § 模板 1](./2_pointers.md#template-1-fastslow-read-write-compaction--lc-26-) — 跟 `nums[slow - k]` 比而不是 `nums[slow - 1]`；前 k 個值一定保留。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 拿 `nums[fast - k]`（讀取端）而不是 `nums[slow - k]`（寫入端）來比，`[1,1,1,2,2,3]` 會出錯：
  第三個 `1` 被跳過之後，讀取端那格仍然是它。
- `slow` 從 1、`fast` 從 2 開始（這張卡較早的版本）在 `[0,0,1,1,1,1,2,3,3]` 會掉一個值：
  `slow < 2` 這個條件讓索引 1 被覆寫。

**類似** — LC 26（k = 1）· LC 27（丟掉一個值）· LC 283（交換取代複製）

<!-- ec8b48ee7a74 -->
## 向內收斂指標的題目

<!-- 4dba9467d743 -->
### DI String Match — LC 942

> **關鍵** — 兩個指標收斂的是**值** `0..n`，不是索引。遇到 `"I"` 放最小的未用值（剩下的都更大）；
> 遇到 `"D"` 放最大的未用值（剩下的都更小）。未用的值永遠是 `[low, high]` 這段範圍，
> 所以最後一個就是 `low`。

**歸約到**：[2_pointers § 模板 2](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) — 兩端是最小和最大的未用值，由字元決定移動哪一端。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- n 個字元要輸出 n + 1 個值；忘了最後的 `low`，回傳的排列長度就錯了。

**類似** — LC 31（下一個排列）· LC 903（計算 DI 排列的數量 — 用 DP，不是指標）

<!-- 7df6d4d1b14c -->
### 3Sum Closest — LC 16

> **關鍵** — 固定 `nums[i]`，再在排序後的其餘部分收斂。跟 `target` 的比較仍然告訴你該移哪一端
> （太小 → `left++`，太大 → `right--`），而且每個看過的和都是候選，所以保留 `|sum − target|`
> 最小的那個。剛好相等就結束。

**歸約到**：[2_pointers § 模板 2](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) — 先排序、固定一個元素；答案是看過最接近的和，而不是剛好相等的配對。固定一個的迴圈出自 [n_sum.md](./n_sum.md)。

<!--CODE-->

<!--CODE-->

**陷阱**
- 把 `closest` 初始化成 `0` 或 `Integer.MAX_VALUE` 會出錯：`0` 可能比所有真實的和都接近，
  而 target 為負時 `MAX_VALUE - target` 會溢位。從一組真實的三元組開始。
- 不需要跳過重複：答案是一個數字，不是像 LC 15 那樣的一組三元組。

**類似** — LC 15（所有剛好相等的三元組，[n_sum.md](./n_sum.md)）· LC 259（計算小於 target 的和）· LC 18（4Sum）

<!-- 4132afe7f5bd -->
### Next Permutation — LC 31

> **關鍵** — 從右邊找第一個 `nums[i] < nums[i + 1]`：`i` 之後全部遞減，所以已經是那些值最大的
> 排法。把 `nums[i]` 跟它右邊比它大的最小值（比它大的最右邊那個）交換，後綴**仍然**遞減 —
> 所以用收斂指標把它反轉，就變成最小的排法。

**歸約到**：[2_pointers § 模板 2](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) — 收斂的交換是最後一步（反轉樞紐之後的後綴），前面先由右往左掃找出樞紐和它的後繼。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 兩次掃描要用 `>=` / `<=`，不是 `>` / `<`：有重複值時（`[1,5,1]`），嚴格比較會挑到相等的值當後繼，
  回傳同一個排列。
- 沒有樞紐（`[3,2,1]`）不是特例：`i = -1`，跳過交換，從索引 0 反轉就繞回第一個排列。

**類似** — LC 556（對整數的各位數做同樣的事，加上溢位檢查）· LC 1053（一次交換的上一個排列）· LC 60（直接求第 k 個排列）

<!-- 2c3c8eae069c -->
### Trapping Rain Water — LC 42

> **關鍵** — 一根柱子上方的水是 `min(maxLeft, maxRight) − height`。移動柱子**較矮**的那一側：
> 如果 `height[left] < height[right]`，右邊已經有一面不比左邊任何柱子矮的牆，
> 所以 `left` 的水只受 `left_max` 限制。

**歸約到**：[2_pointers § 模板 2](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) — 跟 LC 11 一樣移動較矮的那一側，每個指標各帶一個累計最大值。

<!--CODE-->

**陷阱**
- 移動**較高**的那一側，結算的柱子真正的邊界其實在另一側：
  `[4,2,0,3,2,5]` 得到 7 而不是 9。
- 先更新 `max` 再加水，不能反過來：在 `[0,1,0,2,1,0,1,3,2,1,2,1]` 上先加後更新，
  第一根上升的柱子會加上 `0 - 1`，回傳 2 而不是 6。

**類似** — LC 11（同樣的移動規則，算面積而不是水）· LC 407（二維 — 用堆積，不是指標）· 單調堆疊的寫法：[monotonic_stack.md](./monotonic_stack.md)

<!-- daa669f40395 -->
## 從中心往外擴的題目

<!-- 7e8d57956c45 -->
### Palindromic Substrings — LC 647 ⭐⭐⭐⭐

> **關鍵** — 每個回文都有一個中心，總共 `2n − 1` 個（每個字元、每個間隙）。從一個中心往外擴，
> 會由短到長看過它的所有回文，並在第一個不匹配時停下，所以**每成功一步就是一個回文**：
> 數步數，而不是記最長的。

**歸約到**：[2_pointers § 模板 3](./2_pointers.md#template-3-expand-from-centre--lc-5-) — 擴張回傳的是個數，不是範圍。

<!--CODE-->

**陷阱**
- 只算奇數中心：`"aaa"` 會得到 4 而不是 6。
- 暴力法（用 `s[i:j] == s[i:j][::-1]` 檢查每個子字串）是 O(n³)；區間 DP
  （[dp.md](./dp.md)）的時間**和**空間都是 O(n²) — 往外擴才是 O(1) 空間的答案。

**類似** — LC 5（保留最長的 — [模板 3](./2_pointers.md#template-3-expand-from-centre--lc-5-)）· LC 516（子序列 — 區間 DP）· LC 131（切成多個回文）

<!-- 285bc2076501 -->
### Longest Mountain in Array — LC 845

> **關鍵** — 從**山頂**往外擴，而不是從中心：往左走只要值一直朝山頂上升，往右走只要值一直下降。
> 然後把 `i` 直接跳到右邊的山腳 — 下坡上不可能有另一個山頂 — 所以每個索引被看的次數有上限。

**歸約到**：[2_pointers § 模板 3](./2_pointers.md#template-3-expand-from-centre--lc-5-) — 中心是嚴格的局部最大值，兩側各自依單調坡擴張（不是靠字元相等），而且 `i` 會跳過已擴張的範圍。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 每個比較都要嚴格：`[2,2,2]` 沒有山，山頂是平台（`[1,3,3,1]`）也不算山頂。
- 少了 `i = right`，迴圈會在每段下坡上重新檢查每個索引 — 結果仍然正確，但 O(n) 的論證就不成立了。

**類似** — LC 941（整個陣列是不是一座山）· LC 852 / LC 162（找山頂 — 二分搜尋）· `up[]` / `down[]` 兩個陣列的版本：[prefix_sum.md](./prefix_sum.md#run-length-variant--the-side-resets-so-build-both-arrays-lc-845-)

<!-- 5939c57f2b68 -->
## 子序列比對的題目

<!-- 22fef6761339 -->
### Longest Word in Dictionary through Deleting — LC 524 ⭐⭐⭐⭐

> **關鍵** — 「刪掉 `s` 的一些字元後形成」就是「是 `s` 的子序列」，所以這題是對每個單字各跑一次
> LC 392。唯一新的部分是勝負規則：長的贏短的，長度相同時字典序較小的贏 — 要跟目前最佳比較，
> 絕不能回傳第一個符合的。

**歸約到**：[2_pointers § 模板 4](./2_pointers.md#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) — 對字典裡每個單字各跑一次，依（長度，再字典序）保留最佳。

<!--CODE-->

<!--CODE-->

**陷阱**
- 回傳第一個找到的子序列：`s = "abpcplea"`、`["ale","apple"]` 會回傳 `"ale"`。
  要嘛跟目前最佳比較（如上），要嘛先依 `(-len, word)` 排序再回傳第一個符合的 — 排序多花 O(d log d)。
- 只比長度：`["b","a"]` 必須回傳 `"a"`。

**類似** — LC 392（內層的檢查）· LC 792（很多單字 — 依下一個需要的字元分桶）· LC 2486（計算沒配到的尾巴）

<!-- 8625040c6fb2 -->
### Camelcase Matching — LC 1023

> **關鍵** — pattern 必須是 query 的子序列，**而且** pattern 跳過的每個 query 字元都必須是小寫。
> 所以多一個分支：沒配到的大寫字母立刻失敗。

**歸約到**：[2_pointers § 模板 4](./2_pointers.md#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) — 永遠前進的指標只能跳過小寫字母。

<!--CODE-->

<!--CODE-->

**陷阱**
- 先檢查邊界再比對：pattern 用完後（`"FooBarTest"`、`"FB"`），是 `j < len(pattern)`
  這個條件把結尾的 `T` 送去做大寫檢查。
- 一定要先比對：剛好**是** pattern 下一個字元的大寫字母要被吃掉，而不是被拒絕。

**類似** — LC 392（沒有大小寫規則）· LC 524（很多單字，保留最佳）

<!-- 053b20de9788 -->
## 兩條序列同步前進的題目

<!-- c6466d3021ef -->
### One Edit Distance — LC 161

> **關鍵** — 兩個字串一起走到第一個不匹配處。那裡只可能套用一種修補 — 取代（長度相同）
> 或在較短的字串插入 — 修補之後兩邊剩下的部分必須**相等**，所以比較一次後綴就能決定。

**歸約到**：[2_pointers § 模板 9](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) — 同步掃描在第一個差異處停下，剩下的就是一次後綴比較。

<!--CODE-->

<!--CODE-->

**陷阱**
- 相同的字串是**零**次編輯：`("abc", "abc")` 和 `("", "")` 回傳 false。
- `("ab", "ba")` 需要兩次取代；第一個不匹配之後的後綴比較會抓到它。

**類似** — LC 72（最少編輯次數 — DP，[dp.md](./dp.md)）· LC 680（刪一個字元變回文）· LC 844（套用退格後比較）

<!-- 40fe287359e8 -->
### Encode and Decode Strings — LC 271

> **關鍵** — 單靠分隔符行不通，因為單字裡可能出現任何字元。把每個單字寫成 `len#word`：
> 解碼時讀數字讀到第一個 `#`，然後**直接跳過**剛好 `len` 個字元，所以單字裡的 `#`
> 永遠不會被當成分隔符。

**歸約到**：[2_pointers § 模板 9](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) — 只有一條序列，每一輪吃掉一個 token；token 的結尾由長度標頭決定，而不是分隔符。

<!--CODE-->

<!--CODE-->

**陷阱**
- 用 `#` 切開，遇到 `["a#b"]` 就壞了；長度標頭才讓格式沒有歧義。
- `[""]` 和 `[]` 編碼後必須不同：`"0#"` 對上 `""`。
- 在迴圈裡用 `+=` 組出編碼字串，最壞情況是平方時間；先收集各段再 `join`。
  迴圈的 `s.find("#", i)` 寫法在 [python_trick.md](./python_trick.md)。

**類似** — LC 297（序列化一棵樹）· LC 443（字串壓縮 — 長度寫在連續段之後）

<!-- e1cfb875f78b -->
## 指引

這些題目的完整解法在別處；這份文件只留一行。

| LC | 題目 | 在這裡歸約到 | 完整解法在 |
|---|---|---|---|
| 5 | Longest Palindromic Substring | 模板 3 的代表題 | [2_pointers.md § 模板 3](./2_pointers.md#template-3-expand-from-centre--lc-5-)；O(n) 的 Manacher 在 [advanced_string_algorithms.md](./advanced_string_algorithms.md) |
| 15 | 3Sum | 固定一個 + 模板 2 | [n_sum.md](./n_sum.md) |
| 57 | Insert Interval | 一個串列、三個階段（之前／重疊／之後） | [intervals.md](./intervals.md) |
| 125 | Valid Palindrome | 模板 2 加上跳過 | [string.md § 模板 1](./string.md#template-1-two-pointer-scan--in-place-reversal--lc-125-) |
| 128 | Longest Consecutive Sequence | 不是雙指標：雜湊集合，只從連續段的開頭開始 | [set_examples.md](./set_examples.md) |
| 165 | Compare Version Numbers | 模板 9 的代表題 | [2_pointers.md § 模板 9](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) |
| 214 | Shortest Palindrome | 最長回文前綴 — 對 `s + "#" + reverse(s)` 跑 KMP | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 240 | Search a 2D Matrix II | 從右上角走階梯 | [matrix_examples.md](./matrix_examples.md) |
| 344 | Reverse String | 模板 2，交換後兩邊都移動 | [2_pointers.md § 反轉陣列](./2_pointers.md#reverse-an-array) |
| 680 | Valid Palindrome II | 模板 2，在第一個不匹配處分兩支 | [palindrome.md](./palindrome.md) |
| 763 | Partition Labels | 最後出現位置，`i == end` 時切 | [string.md § 模板 8](./string.md#template-8-greedy-partition-by-last-occurrence--lc-763-) |
| 809 | Expressive Words | 對相同字元的連續段做模板 9 | [string_examples.md](./string_examples.md) |
| 953 | Verifying an Alien Dictionary | 對每對相鄰單字做模板 9，配上排名表 | [hash_map.md § 模板 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) |
| 986 | Interval List Intersections | 在兩串區間上做模板 9：先結束的那個前進 | [intervals.md](./intervals.md) |
| 2104 | Sum of Subarray Ranges | 不是雙指標：單調堆疊算貢獻 | [monotonic_stack.md](./monotonic_stack.md) |
