<!-- 4b5735ebc4a4 -->
# 滑動視窗 — 進階視窗形態

> **範圍** — 第一輪可以先跳過的滑動視窗技巧：至多 K 個相異的家族、帶禁用元素重置或一次掃描的恰好 K 計數、同時處理多個不重疊的視窗，以及以單字而非字元為鍵的視窗；七個必會模板留在主文件，而由別份文件負責的進階型態（雙端佇列求極值、補集視窗、數值分桶）在這裡只留指引。
> **另見**：[sliding_window.md](./sliding_window.md) — 這裡每個技巧所依據的七個標準模板；[sliding_window_examples.md](./sliding_window_examples.md) — 不需要進階技巧的實作題；[monotonic_queue.md](./monotonic_queue.md) — 用雙端佇列求視窗極值；[prefix_sum_advanced.md](./prefix_sum_advanced.md) — 補集視窗（「從兩端拿」）；[prefix_sum.md](./prefix_sum.md) — 視窗總和可能為負時，改用前綴和的做法。

<!-- 08fe05f53c92 -->
## LeetCode 題目清單

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)
- [Monotonic Queue](https://leetcode.com/problem-list/monotonic-queue/)
- [Rolling Hash](https://leetcode.com/problem-list/rolling-hash/)

<!-- b6a87abaf3e9 -->
## 總覽

下面每個技巧都是 [sliding_window.md](./sliding_window.md) 的某個模板換掉一個欄位：收縮條件數的是相異鍵的個數、視窗被禁用元素重置、合法起點的個數跨步驟帶著走、同時替多個視窗打分，或視窗一次走一整個單字。至多 K 個相異（模板 1）和恰好 K 的重置（模板 2）在 L3 面試會出現；其餘較少見，每張卡都會說明通常哪個較簡單的模板就夠了。

<!-- f69d29eb1767 -->
### 先備知識

- [sliding_window § 模板 2](./sliding_window.md#template-2-grow-then-shrink-the-while-invariant--lc-1004-)：先擴後縮的迴圈。這裡每個模板都保留它。
- [sliding_window § 模板 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-)：最長的合法視窗。模板 1 就是它加上相異個數的判斷。
- [sliding_window § 模板 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-)：用 `at_most(K) − at_most(K − 1)` 求恰好 K。模板 2 與 3 延伸它。
- [sliding_window § 模板 1](./sliding_window.md#template-1-fixed-size-window--lc-643-)：固定大小的視窗。模板 4 與 5 建立在它上面。

<!-- c64517cdfa91 -->
## 辨識

| 題目敘述中的訊號 | 限制提示 | 該用 | 不適用時 |
|---|---|---|---|
| 「**至多 K 個相異**值的最長子陣列」 | n ≤ 10⁵ | [模板 1](#template-1-at-most-k-distinct--lc-904-) | 相異值的個數必須恰好是 K，而且是計數不是量長度 → 模板 2 |
| 「只用這些字元、恰好 K 個相異的子字串有幾個」 | n ≤ 10⁵ | [模板 2](#template-2-exactly-k-with-a-forbidden-element-reset--lc-2062-) | 每個元素都允許 → 照原樣用 [sliding_window § 模板 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) |
| 「恰好 k 個奇數／1 的子陣列有幾個」，一次掃描 | n ≤ 5·10⁴，值 ≥ 0 | [模板 3](#template-3-one-pass-exactly-k--count-the-left-gap--lc-1248-) | 值可能為負，或目標是總和 → 前綴和 + hash map |
| 「**三個**（或兩個）長度為 k、**不重疊**、總和最大的子陣列」 | n ≤ 2·10⁴ | [模板 4](#template-4-non-overlapping-fixed-windows--prefixsuffix-argmax--lc-689-) | 視窗個數是參數 → 在（已用視窗數、前綴）上做 DP |
| 「**所有單字**串接而成」，每個單字等長 | 單字長度 ≤ 30 | [模板 5](#template-5-word-level-window-one-pass-per-offset--lc-30-) | 視窗是單一字元 → [hash_map § 模板 5](./hash_map.md#template-5-fixed-window-character-counts--lc-438) |
| 「大小為 k 的**每個視窗**的最大／最小值」 | n ≤ 10⁵ | 單調雙端佇列 — [monotonic_queue § 模板 1](./monotonic_queue.md#template-1-sliding-window-maximum-decreasing-deque--lc-239) | 答案是中位數 → 兩個 heap，[monotonic_queue § 模板 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-) |
| 「從**兩端**拿 k 張牌」、「從兩端移除直到總和為 x」 | n ≤ 10⁵，值 ≥ 1 | 補集視窗 — [prefix_sum_advanced § 模板 9](./prefix_sum_advanced.md#template-9-complement-trick--total--middle-window---lc-1423) | 可以從中間拿 → 剩下的不是一個視窗 |
| 「`abs(i − j) ≤ k` **而且** `abs(nums[i] − nums[j]) ≤ t`」 | n ≤ 10⁵ | 寬度 `t + 1` 的數值分桶 — [sort § 模板 8](./sort.md#template-8-bucket-sort-by-value-range-bucket--value--width--lc-220-) | 值必須相等 → 在索引視窗上用一般的 set（LC 219） |

<!-- ed014bb62bb1 -->
## 至多 K 與恰好 K 計數

<!-- a7936bd2f465 -->
### 模板 1：至多 K 個相異 — LC 904 ⭐⭐⭐⭐

**辨識** — 「**至多 K 個相異**值的最長子陣列／子字串」、「兩個籃子，每籃只放一種水果」。n ≤ 10⁵ → O(n)。

**關鍵** — `len(basket)` 就是視窗中相異值的個數，因為某個鍵的計數一到 0 就被刪掉。至多 K 個相異的視窗收縮後仍然合法，所以模板 3 的迴圈加上 `while len(basket) > K` 就能用。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`left` 只往前走，內層 `while` 總共最多跑 n 次。
- **空間 O(K)**：`basket` 最多有 K + 1 個鍵，因為 `while` 會立刻移掉多出來的那一個。

**陷阱**
- 只減計數不 `del`：在 `[1, 2, 3, 2, 2]` 上，1 的零計數項讓 `len(basket)` 停在 3，`while` 永遠停不下來，把 `left` 推出陣列尾端（`IndexError`，而不是 4）。
- 在 `while` 裡面記錄 `best`，量到的是有 K + 1 種的視窗：`[1, 2, 3]` 會回報 3。

**不適用時** — 題目問的是有幾個子陣列**恰好**有 K 個相異值 → 帶計數欄位把這個迴圈跑兩次（[sliding_window § 模板 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-)）。

**練習** — LC 904 · LC 159 *（同一題換成字元）* · LC 340 *（`> K` 取代 `> 2`）* · LC 992 *（恰好 K：這個迴圈跑兩次並計數）*

<!-- eb6f8d54f1ab -->
### 模板 2：帶禁用元素重置的恰好 K — LC 2062 ⭐⭐⭐⭐

**辨識** — 「只用母音、而且**五個都要有**的子字串有幾個」、「恰好 K 個相異，而且子字串不能含 …」。n ≤ 10⁵。

**關鍵** — 至多 K 個相異值的視窗，減掉至多 K − 1 個的，剛好就是恰好 K 個的視窗。被計入的視窗都不能含禁用字元，所以 `at_most` 遇到它就重置：清空 map、把 `left` 移到它後面。相減的式子不變，因為兩次呼叫在同樣的位置重置。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：兩次 `at_most`，每次的 `left` 都只往前走。
- **空間 O(1)**：`count` 最多放六個母音。

**試跑** — 為什麼相減剛好算出恰好 K 個，以 `[1, 2, 1, 3]`、K = 2 為例：

<!--CODE-->

**陷阱**
- 忘了重置：在 `"aeiouxa"` 上視窗會跨過 `x`，答案變成 3 而不是 1。
- 推廣成 `exactly(k)` 時，`k = 0` 會呼叫 `at_most(−1)`，它的 `while len(count) > −1` 永遠在收縮；相減前先對 `k = 0` 回傳 0。
- 把兩個**最長視窗**的長度相減沒有任何意義：這個恆等式對計數（`total += right − left + 1`）成立，對 `max(len)` 不成立。

**不適用時** — 條件是可能為負的總和，或是任何「至多」都表達不了的等式（負數下的「總和等於 k」，LC 560）→ [hash_map § 模板 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-)。

**練習** — LC 992 *（沒有禁用元素）* · LC 2062 · LC 930 *（恰好 `goal` 個 1）* · LC 1248 *（恰好 k 個奇數；也可用模板 3）*

<!-- 5d76cb0b15bf -->
### 模板 3：一次掃描的恰好 K — 數左側間隙 — LC 1248 ⭐⭐⭐

**辨識** — 「恰好 k 個奇數／1 的子陣列有幾個」，值是指示性的（奇或偶、1 或 0），而且要求一次掃描、O(1) 空間。

**關鍵** — 視窗一旦含有 k 個奇數，從 `left` 到第一個奇數之間的每個起點都給出一個合法子陣列。`while odd == k` 迴圈把 `left` 走過這些起點，並把它們數進 `gap`。在新的奇數出現之前，`gap` 對之後每個 `right` 都仍然成立，所以每一步都加上它，只在遇到奇數時重置。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`left` 總共只走過陣列一次。
- **空間 O(1)**：四個整數。前綴和 + hash map 的版本是 O(n) 空間。

**試跑** — `nums = [2, 2, 1, 2, 1], k = 2`：

<!--CODE-->

**陷阱**
- 遇到新奇數時沒有重置 `gap`：`[1, 1, 1], k = 1` 在第二個奇數之後還在加舊的間隙，回傳 6 而不是 3。
- 只在 `while` 裡面計數：第 k 個奇數之後的偶數（`[1, 2, 2], k = 1`）每個都會延長所有已算過的子陣列，這些會被漏掉 — 得到 1 而不是 3。

**不適用時** — 值可能為負，或目標是任意值的總和 → 前綴和 + hash map，[prefix_sum § 模板 6](./prefix_sum.md#template-6-transform-and-count--lc-1248)（同一題，用 `{prefix: count}` 計數）；重視清楚勝過一次掃描時，模板 2 的相減比較好解釋。

**練習** — LC 1248 · LC 930 *（用 1 取代奇數）* · LC 1358 *（數包含三種字母的視窗：同樣是「第一個必要字元之前的每個起點」的計數）*

<!-- 5b64e1b4d4aa -->
## 同時處理多個視窗

<!-- acc1b8deb977 -->
### 模板 4：不重疊的固定視窗 — 前綴／後綴 argmax — LC 689 ⭐⭐⭐

**辨識** — 「**三個**長度為 k、不重疊、總和最大的子陣列」、「兩個長度分別為 L 與 M 的不重疊子陣列」。n ≤ 2·10⁴。

**關鍵** — 固定中間的視窗。最好的左視窗就落在 `[0, mid − k]` 的任何位置，最好的右視窗落在 `[mid + k, end]` 的任何位置，兩者互相獨立，所以在視窗總和上做一次前綴 argmax 和一次後綴 argmax，每個 `mid` 都能 O(1) 回答。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：一次滾動算出 `w`，`left` 與 `right` 各掃一次，再掃一次 `mid`。
- **空間 O(n)**：三個存視窗總和與 argmax 的陣列。

**試跑** — `nums = [1, 2, 1, 2, 6, 7, 5, 1], k = 2`：

<!--CODE-->

**陷阱**
- 平手：LC 689 要字典序最小的答案。前綴掃描用 `>=` 會把左視窗移到最後一個平手的位置：`[1, 1, 2, 2], k = 1` 回傳 `[1, 2, 3]` 而不是 `[0, 2, 3]`。
- 中間的迴圈兩側都必須留一整個視窗：`mid` 的範圍是 `[k, m − k)`，不是 `[0, m)`。

**不適用時** — 視窗個數是輸入 j → DP，`dp[j][i] = max(dp[j][i − 1], dp[j − 1][i − k] + w[i − k + 1])`，O(n·j)。

**練習** — LC 1031 *（兩個長度不同的視窗：兩種先後順序都試，維護目前最好的左視窗；在 [prefix_sum_examples.md](./prefix_sum_examples.md) 中解）* · LC 689

<!-- 008d80276a19 -->
## 鍵不是字元的視窗

<!-- 0e592e6fadbb -->
### 模板 5：單字層級的視窗，每個偏移跑一次 — LC 30 ⭐⭐⭐⭐

**辨識** — 「找出所有**單字全部串接**而成的子字串的起點」，每個單字等長。視窗一次移動一整個單字。

**關鍵** — 合法起點 `s` 滿足 `s % wordLen == r`，其中 `r < wordLen`，餘數相同的起點共用同樣的單字邊界。所以跑 `wordLen` 個獨立的視窗，每個偏移一個，每次走 `wordLen`；在單一視窗裡，它就是以單字為鍵的字元計數視窗。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(wordLen · n)**：`wordLen` 次掃描，每次約 `n / wordLen` 步，每一步都要切出並雜湊一個長度 `wordLen` 的單字。
- **空間 O(m · wordLen)**：`need` 與 `window` 最多存 m 個不同的單字。

**試跑** — `s = "barfoothefoobarman", words = ["foo", "bar"]`：

<!--CODE-->

**陷阱**
- 只跑一次、每次前進 1、在每個起點重數 m 個單字，是 O(n · m · wordLen)；正是偏移讓每個單字在一個視窗裡只進出一次。
- 重複的單字（`["word", "good", "best", "word"]`）：要和 `need` 比較計數，不能用 set，否則多餘副本的收縮永遠不會觸發。

**不適用時** — 視窗是固定長度、只問「之前看過嗎？」（LC 187，重複的 DNA）→ rolling hash，在 [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md)。

**練習** — LC 438 *（同一個視窗，換成字元）* · LC 30 · LC 187 *（固定長度視窗加 rolling hash）*

<!-- ecbb506554e9 -->
## 看起來像但不是單一視窗的題目

<!-- 2211536525b3 -->
### Meeting Scheduler — LC 1229

> **關鍵** — 兩個排好序的串列，各一個指標。先結束的區間和另一個串列裡後面的任何區間，重疊都不會比現在更多，所以把它的指標往前移；第一個長度至少 `duration` 的重疊就是最早的答案。

**歸約到**：[intervals § 模板 3](./intervals.md#template-3-two-pointer-intersection-lc-986) — 兩個排序區間串列的交集，只是回傳第一個夠長的重疊，而不是全部收集起來。這裡沒有視窗也沒有合法性判斷，所以它不是滑動視窗的模板。

<!--CODE-->

<!--CODE-->

**陷阱**
- 移動**開始**較早的那個指標：`slots1 = [[0, 50]], slots2 = [[10, 15], [20, 40]], duration = 10` 會丟掉 `[0, 50]`，回傳 `[]` 而不是 `[20, 30]`。
- 輸入沒排序：題目沒有保證時段已排序，所以兩邊都要先排序。

**相似題** — LC 986 *（收集所有交集）* · LC 759 *（多個串列之間的空閒時間）*

<!-- b8c677af94b0 -->
## 不適用時

- **模板 1（至多 K 個相異）** — K 是 1 或「沒有重複」時，它就是用 set 的 [sliding_window § 模板 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-)；不需要計數。
- **模板 2（帶重置的恰好 K）** — 沒有禁用元素時，它就是照原樣的 [sliding_window § 模板 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-)。也可以先在禁用字元處把字串切開，再對每一段跑模板 6。
- **模板 3（一次掃描數間隙）** — 模板 2 的兩次掃描一樣快、也更好解釋；只有被要求一次掃描時才用數間隙。有負數或真正的總和時兩者都不行：改用前綴和計數表。
- **模板 4（前綴／後綴 argmax）** — 只有**兩個**視窗時，維護目前最好的左視窗、每種順序掃一次就夠了（LC 1031）；j 個視窗時用 DP。
- **模板 5（每個偏移跑一次）** — 單字長度不同時完全不適用（回溯或 trie）；視窗是單一字元時，就是 [hash_map § 模板 5](./hash_map.md#template-5-fixed-window-character-counts--lc-438) 的一般固定視窗。

<!-- cf146c401303 -->
## 總結

<!-- cf90375ed0a1 -->
### 該用哪種進階型態？— 決策表

| 題目說 | 該用 | 在哪 |
|---|---|---|
| 「至多 K 個相異」（最長） | 模板 3 的迴圈加 `len(map) > K` | [模板 1](#template-1-at-most-k-distinct--lc-904-) |
| 「恰好 K …」（計數），字元不受限 | `at_most(K) − at_most(K − 1)` | [sliding_window § 模板 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) |
| 「恰好 K …」但只允許某些字元 | 至多計數，遇到禁用字元硬重置 | [模板 2](#template-2-exactly-k-with-a-forbidden-element-reset--lc-2062-) |
| 「恰好 k 個奇數／1」，一次掃描 | 數左側間隙 | [模板 3](#template-3-one-pass-exactly-k--count-the-left-gap--lc-1248-) |
| 「恰好 K …」而且值可能為**負** | 前綴和 + hash map — 沒有視窗能用 | [hash_map § 模板 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) |
| 「兩個／三個不重疊子陣列，總和最大」 | 視窗總和 + 前綴／後綴 argmax | [模板 4](#template-4-non-overlapping-fixed-windows--prefixsuffix-argmax--lc-689-) |
| 「所有單字的串接」 | 每個對齊偏移一個視窗 | [模板 5](#template-5-word-level-window-one-pass-per-offset--lc-30-) |
| 「兩份行程中最早的共同空檔」 | 兩個指標，移動先結束的那個 | [Meeting Scheduler — LC 1229](#meeting-scheduler--lc-1229) |

<!-- 10793a3c3f48 -->
### 決定工具的那一個問題

> **合法性是單調的嗎？** 如果一個不合法的視窗在 `right` 前進時仍然不合法，一個 `while (invalid) shrink` 就夠了，一切都是普通的視窗。如果不是 — 「恰好 k」、負值，或無法改寫成「至多」的等式 — 就需要至多相減、前綴 map，或數間隙。這一個問題就能把本頁每個技巧分開。

<!-- 235280d4f171 -->
### 放在別處的題目

完整解法在別份文件、用同一個做法的進階視窗題。

| LC | 題目 | 技巧 | 解在 |
|---|---|---|---|
| 239 | Sliding Window Maximum | 存索引的遞減雙端佇列 | [monotonic_queue § 模板 1](./monotonic_queue.md#template-1-sliding-window-maximum-decreasing-deque--lc-239) |
| 480 | Sliding Window Median | 兩個 heap 加延遲刪除 | [monotonic_queue § 模板 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-) |
| 1423 | Maximum Points You Can Obtain from Cards | `total − 大小為 n − k 的最小視窗` | [prefix_sum_advanced § 模板 9](./prefix_sum_advanced.md#template-9-complement-trick--total--middle-window---lc-1423) |
| 1658 | Minimum Operations to Reduce X to Zero | `n − 總和為 total − x 的最長視窗` | [prefix_sum_advanced § 模板 9](./prefix_sum_advanced.md#template-9-complement-trick--total--middle-window---lc-1423)（它的變化題） |
| 220 | Contains Duplicate III | 索引視窗 + 寬度 `t + 1` 的數值分桶 | [sort § 模板 8](./sort.md#template-8-bucket-sort-by-value-range-bucket--value--width--lc-220-)；有序集合版本在 [scanning_line § 模板 11](./scanning_line.md#template-11-index-sweep--active-ordered-set--lc-220) |
| 187 | Repeated DNA Sequences | 長度 10 的固定視窗 + 2-bit rolling hash | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 718 | Maximum Length of Repeated Subarray | DP，或二分搜尋 + rolling hash | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 1031 | Maximum Sum of Two Non-Overlapping Subarrays | 前綴和 + 目前最好的左視窗 | [prefix_sum_examples.md](./prefix_sum_examples.md) |
| 1248 | Count Number of Nice Subarrays（前綴 map 版本） | `{奇數個數: 次數}` | [prefix_sum § 模板 6](./prefix_sum.md#template-6-transform-and-count--lc-1248) |

只供辨識、沒有新技巧：

| 題目 | LC# | 一句話重點 |
|---|---|---|
| Maximum Number of Occurrences of a Substring | 1297 | 只用大小為 `minSize` 的固定視窗：較長的合法子字串一定包含一個合法的 `minSize` 子字串，所以 `maxSize` 是幌子 |
| New 21 Game | 837 | 在 DP 陣列上的滑動視窗：`dp[i]` 是前 `maxPts` 個機率的視窗總和除以 `maxPts` |
| Max Value of Equation | 1499 | 以 `xj − xi ≤ k` 為界的視窗，在 `yi − xi` 上用單調雙端佇列（[monotonic_queue.md](./monotonic_queue.md)） |

<!-- 08768315d871 -->
### 這裡不涵蓋的內容

- **無界串流上的視窗** — 右邊界是即時資料流而不是陣列索引的視窗。本頁沒有任何東西假設可以隨機存取，所以模板可以直接套用；串流用的 reservoir／衰減計數器技巧在 [streaming_algorithms.md](./streaming_algorithms.md)。
- **深入的雙端佇列家族** — [monotonic_queue.md](./monotonic_queue.md)。
