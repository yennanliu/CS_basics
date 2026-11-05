<!-- 4c74b62bd9ff -->
# 雙指標

> **範圍** — 陣列與字串上的雙指標家族：兩端向內收斂、快慢指標、從中心擴張、讀寫分離的原地重寫，每一種各給一個標準模板；依條件伸縮的視窗題不在這裡。
> **另見** — *從這份文件拆出去的深入內容*：[2_pointers_examples.md](./2_pointers_examples.md) — LC 題目實作集，每題一個標準解；[quickselect.md](./quickselect.md) — QuickSelect／以 partition 為主的第 K 個元素選取，它算是選取演算法，而不是雙指標掃描。
> *相鄰主題*：[sliding_window.md](./sliding_window.md) — 由條件驅動的變動大小視窗；[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — 同樣的想法，但作用在節點而非索引上；[n_sum.md](./n_sum.md) — 有序陣列上的 k-sum 特化；[palindrome.md](./palindrome.md) — 從中心擴張的完整討論。

<!-- 612f0fad9473 -->
## LeetCode 題目清單

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Array](https://leetcode.com/problem-list/array/)

<!-- 55bf7babef50 -->
## 總覽

**雙指標**在同一個序列上放兩個索引（或兩個序列各放一個），依照某條規則推進它們，用一趟線性掃描取代巢狀迴圈。

<!-- bbb4d0e16cec -->
### 關鍵性質
- **複雜度**：單趟掃描是 O(N)；需要先排序的話是 O(N log N)；對每個索引都做中心擴張則是 O(N^2)。額外空間 O(1) — 指標就是全部的狀態
- **核心想法**：每次迭代至少推進一個指標，而且從不回頭走同一個索引，所以總工作量被指標移動次數限制住
- **什麼時候用**：輸入有序、原地重寫、回文、合併兩個有序序列、子序列檢查、環偵測
- **關鍵技巧**：快慢指標（同向）、左右指標（收斂）、從中心擴張、一個指標永遠前進

<!-- 1fd54d6e26bc -->
### 參考資料
- [fucking-algorithm : 2 pointers](https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9)

<!-- 47b27e5df87c -->
## 辨識

| 題目裡的訊號 | 限制條件提示 | 該用 | 不是這個，如果 |
|---|---|---|---|
| 「**原地**移除…」、「回傳 k，前 k 個元素」、「O(1) 額外空間」 | n ≤ 3·10⁴，已排序或固定的 `val` | [模板 1](#template-1-fastslow-read-write-compaction--lc-26-) 快慢指標壓縮 — LC 26, LC 27, LC 80, LC 283 | 保留元素的順序無所謂、寫入代價高 → 模板 2 的雙向 LC 27 |
| 已排序陣列、「和為…的一組配對」、「兩條線之間最多的水」、「回文」 | n ≤ 10⁵ → O(n)，先排序則 O(n log n) | [模板 2](#template-2-converging-pointers-from-both-ends--lc-11-) 收斂指標 — LC 11, LC 125, LC 167, LC 344 | 未排序且答案是索引 → 雜湊表找補數（[hash_map.md](./hash_map.md)） |
| 「最長／計算回文**子字串**」 | n ≤ 1000 → O(n²) 沒問題 | [模板 3](#template-3-expand-from-centre--lc-5-) 從中心擴張 — LC 5, LC 647 | 回文**子序列** → 區間 DP（LC 516） |
| 「s 是不是 t 的**子序列**」、「刪掉一些字元後形成」 | len(t) ≤ 10⁴ | [模板 4](#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) 一個指標永遠前進 — LC 392, LC 524, LC 1023, LC 809 | 連續的**子字串**比對 → KMP／視窗；最長共同子序列 → DP（LC 1143） |
| 「把 nums2 合併進 nums1」，nums1 尾端有空位 | m + n ≤ 200，要求 O(1) 空間 | [模板 5](#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-) 從後面往前填 — LC 88, LC 977, LC 986 | 兩條鏈結串列 → 用虛擬頭節點往前合併（LC 21） |
| 「一趟把只有 0、1、2 的陣列排好」、「依值分成三組」 | O(1) 空間、一趟 | [模板 6](#template-6-three-way-partition-dutch-national-flag--lc-75-) 荷蘭國旗 — LC 75, LC 905 | 必須保持相對順序 → 串列的**穩定**分割（LC 86）見 [linked_list.md 的兩個虛擬節點](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-) |
| 「鏈結串列有沒有環」、「環從哪裡開始」、「不修改陣列找出重複數字」 | O(1) 記憶體 | [模板 7](#template-7-fastslow-cycle-detection--lc-141-) Floyd 快慢指標 — LC 141, LC 142, LC 287 | 允許 O(n) 記憶體 → 用 visited 集合更簡單 |
| 視窗依一個條件變大、依另一個條件縮小 | n ≤ 10⁵ | [模板 8](#template-8-grow-right-then-shrink-left--lc-567) 右邊長、左邊縮 — LC 567, LC 209 | 真正的視窗題 → [sliding_window.md](./sliding_window.md)，那裡才是這一族的主場 |
| 兩份輸入逐個 token 比較 — 「比較版本號」、「只差一次編輯」、「套用退格後是否相等」 | O(n + m) 時間、O(1) 空間 | [模板 9](#template-9-two-sequences-in-lockstep--lc-165-) 兩條序列同步前進 — LC 165, LC 161, LC 844, LC 986 | token 可以被跳過或重排 → 子序列（模板 4）或 DP |
| 第 k 大／最接近的 k 個（用選取，不要排序） | n ≤ 10⁵，平均 O(n) | [quickselect.md](./quickselect.md) — LC 215, LC 973 | k 很小或輸入是串流 → 堆積 |

限制條件提示取自 [complexity_cheatsheet.md](./complexity_cheatsheet.md) 的尺規。其餘的長尾題目實作，都放在 [2_pointers_examples.md](./2_pointers_examples.md)。

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- d18e63f92a7c -->
### 指標的種類

- 指標種類

    - `快慢指標`
        - 快、慢指標從`同一個起點`出發

    - `左右指標`
        - 左、右指標分別從 `idx = 0, idx = len(n) - 1` 出發
        - 通常設成
            - 左指標 = 0
            - 右指標 = len(nums)
        - [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
        - 陣列反轉
        - [2 sum](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
        - [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)

- 從中心`擴張`（並處理`奇數、偶數`兩種情況）
    - LC 680
    - LC 647
    - LC 005

- 合併有序陣列
    - LC 88

- 把所有 1 聚在一起的最少交換次數
    - LC 1151（見 [sliding_window.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)）

- 救生艇
    - LC 881

- `排序 + 固定一個 + 雙指標`（最接近／較小的和）
    - 固定 `i`，把 `l`/`r` 往內夾；用 `|sum - target|` 追蹤最接近的答案
    - LC 16（3Sum Closest）
    - LC 259（3Sum Smaller）

- `先移動右指標`，再依條件移動左指標
    - LC 567
    - LC 209（見 `sliding window cheatsheet`）

- 帶字元型態限制的`子序列比對`
    - 一個指標永遠前進，另一個看條件才動
    - 對不匹配的字元要額外驗證
    - LC 392（Is Subsequence）
    - LC 1023（Camelcase Matching — 帶大小寫限制）

- `逐段比對兩個字串`
    - 兩個指標都是一次前進一整段（同一字元的連續區塊），不是一次一個字元
    - 每組對齊的段落都要驗證：數量不同時，長度必須足以延伸（>= 3）
    - LC 809（Expressive Words）

- `找樞紐 + 找後繼 + 反轉後綴`（下一個排列）
    - 從右往左掃，找到第一個遞增的相鄰對（樞紐），再找剛好比它大的最小後繼
    - 交換樞紐與後繼，接著把遞減的後綴反轉 → 變成遞增
    - LC 31（Next Permutation）、LC 556（Next Greater Element III）

- `用收斂的 low/high 指標建出排列`（貪婪）
    - 看到一種訊號就取用最小的可用值，另一種就取最大的
    - `low`/`high` 在 `[0, n]` 範圍上往內走；最後活下來的那個填最後一格
    - LC 942（DI String Match）

- `最後出現位置 + 右邊界擴張`（貪婪分段）
    - 先算出每個字元的最後索引；`i == end` 時就切一刀
    - LC 763（Partition Labels）

- `有序二維矩陣上的階梯指標`
    - 從`右上角`的鞍點出發：太大 → `col--`，太小 → `row++`
    - LC 240（Search a 2D Matrix II）

- `跨兩個字串的雙指標（同步／逐段）`
    - 每一輪，兩個指標各從自己的字串取用一段
    - 先耗盡的那一邊 → 隱含補 `0`（LC 165）或套前綴規則（LC 953）
    - LC 165（Compare Version Numbers）、LC 953（Verifying an Alien Dictionary）

- `在有序區間上的三階段指標掃描`
    - 在前面的 → 合併重疊的 → 在後面的
    - LC 57（Insert Interval）

- 演算法
    - 二分搜尋
    - 滑動視窗
    - for 迴圈 + 「從中心往`左`、`右`擴張」

- 資料結構
    - 陣列
    - 鏈結串列

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- cf89b4b56256 -->
### 模板 1：快慢指標的讀寫壓縮 — LC 26 ⭐⭐⭐⭐⭐

**辨識** — 「**原地**移除重複值／所有 `val`，回傳保留的個數 k」、「不要配置額外空間」、
「前 k 個元素必須是結果」。n ≤ 3·10⁴ → 一趟 O(n)。

**關鍵點** — `nums[0..slow]` 永遠依序存著要保留的元素。`fast` 每個索引讀一次；
`slow` 只在保留一個值時前進，所以寫入位置永遠追不過讀取位置，沒讀過的值不會被蓋掉。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：只有一個 `for fast` 迴圈；每個索引讀一次、最多寫一次。
- **空間 O(1)**：兩個索引就是全部的狀態 — 結果直接寫回 `nums`。

**試跑**

<!--CODE-->

**陷阱**
- `[]`：在 Python 裡只寫 `return` 會回傳 `None` 而不是 `0`。要明確處理。
- 允許 **k** 份（LC 80）：要跟*已寫入*的前綴比，`nums[fast] != nums[write - k]`，
  不能跟原本的鄰居 `nums[fast - k]` 比。在 `[1,1,1,2,2,3]`、k = 2 上，
  `fast - k` 的版本會讀到已被覆寫的格子，回傳 `[1,1,2,3]`，少了一個 `2`。
- 用交換取代覆寫（`nums[slow], nums[fast] = nums[fast], nums[slow]`）也正確，
  而且每個值都還留在陣列某處；覆寫比較單純，因為尾端永遠不會再讀。

**不是這個，如果** — 保留元素的順序無所謂、寫入代價高：
[模板 2](#template-2-converging-pointers-from-both-ends--lc-11-) 底下的雙向 LC 27
從右端搬值，`val` 很罕見時碰到的格子更少。

**練習** — LC 26 ⭐ · LC 27 *（`!= val` 就保留；`slow` 是下一個空位）* · LC 283
*（跟寫入位置交換，0 自然漂到右邊）* · LC 80 *（最多兩份：跟
`nums[write - 2]` 比）* · LC 83 / LC 82 *（同一條規則用在鏈結串列，見
[2_pointers_linkedlist.md](./2_pointers_linkedlist.md)）*

<!-- 3be0dbdebc6e -->
#### 允許最多 K 份重複

把比較條件推廣成 `nums[fast] != nums[write - k]`，`write` 是下一個空位，
前 k 個元素一定保留：`k = 1` 就是 LC 26，`k = 2` 就是 LC 80。實作在
[2_pointers_examples.md](./2_pointers_examples.md)。

<!-- 0508eba91d77 -->
#### Remove Element — LC 27

> **關鍵點** — 同一種壓縮，只是保留的判斷不同：只要 `nums[fast]` 不是 `val` 就保留。
> 這裡的 `slow` 是下一個空位而不是最後保留的位置，所以答案就是 `slow` 本身。

**化約為**：[模板 1](#template-1-fastslow-read-write-compaction--lc-26-) — 只換了保留的判斷。

<!--CODE-->

<!--CODE-->

<!-- 24ba62a9fb0b -->
### 模板 2：兩端向內收斂的雙指標 — LC 11 ⭐⭐⭐⭐⭐

**辨識** — **已排序**陣列加上「和為…的一組配對」、「哪兩條線能裝最多
水」、「是不是回文」、「原地反轉」。n ≤ 10⁵ → O(n)，必要時先花 O(n log n) 排序。

**關鍵點** — 每一步都永久丟掉一端，而且被丟掉的只會是贏不了目前這組的配對。以 LC 11 來說，
較矮的牆限制了它參與的每一組配對，剩下的夥伴又都更近，所以用到那面矮牆的配對都贏不了剛量過的這組。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`while left < right` 每一輪恰好把一個指標往內移，所以迴圈
  跑 n − 1 次。
- **空間 O(1)**：兩個索引加上目前最佳值。

**陷阱**
- 移動**較高**的牆：`[1,8,6,2,5,4,8,3,7]` 會回傳 8 而不是 49 — 矮牆一直留著，
  持續限制每一組配對。
- `while left <= right` 會讓索引跟自己配對。對 LC 11 無害（寬度 0），對 LC 167 是錯的：
  `numbers = [2,3,4]`、target 4 會回傳 `[1,1]`，把 `2` 用了兩次。
- 拿未排序的輸入做配對和：「太小 → 左邊前進」這條規則需要有序。先排序，
  或在答案是原始索引時改用雜湊表。

**不是這個，如果** — 未排序、答案是索引 → 用
[hash_map.md](./hash_map.md) 找補數（LC 1）；帶條件的連續區間 → [sliding_window.md](./sliding_window.md)。

**練習** — LC 11 ⭐ · LC 344 *（反轉：交換後兩邊都移動）* · LC 125 ⭐ *（比較前先跳過非英數
字元）* · LC 167 *（和太小 → `left++`）* · LC 15 *（固定一個，其餘收斂 — 見
[n_sum.md](./n_sum.md)）* · LC 42 *（移動最大值較小的那一側）*

**Valid Palindrome — LC 125** 就是這張卡加上跳過：兩側都先跨過不算數的字元，再比較這一對。
完整解法在 [string.md § 模板 1](./string.md#template-1-two-pointer-scan--in-place-reversal--lc-125-)；
允許刪一個字元的版本（LC 680）是同一趟掃描，在第一次不匹配時分兩支檢查，見
[palindrome.md](./palindrome.md)。

<!-- c083ab117691 -->
#### Remove Element 的雙向版本 — LC 27

> **關鍵點** — `l` 上的 `val` 被 `nums[r]` 覆寫、`r` 縮小，但 `l` 不動：
> 剛搬進來的值本身也可能是 `val`。當 `l > r` 時，`l` 就是保留元素的個數。

**化約為**：[模板 2](#template-2-converging-pointers-from-both-ends--lc-11-) — 兩端收斂，拿來做分割而不是找配對。

跟快慢指標（模板 1）的關鍵差別：
- 快慢指標依序覆寫 → **保持相對順序**
- 雙向版是把 `nums[l]` 換成 `nums[r]` → **不保持順序**，但寫入次數可能較少（`val` 很罕見時划算）

<!--CODE-->

**比較：快慢 vs 雙向**

| 面向 | 快慢（模板 1） | 雙向（本節） |
|--------|-------------------|----------------------|
| **順序** | 保持相對順序 | **不**保持順序 |
| **寫入次數** | 每個有效元素寫一次 | val 罕見時寫得更少 |
| **迴圈寫法** | `for` 迴圈（fast 一定前進） | `while (l <= r)` |
| **什麼時候用** | 順序有意義時 | 順序無所謂、想少寫幾次時 |

**相似題目：**
- LC 27 Remove Element（本模式）
- LC 905 Sort Array By Parity — 偶數往左、奇數往右（同樣是雙向往內縮的想法）
- LC 75 Sort Colors（Dutch National Flag）— 三向雙指標分割
- LC 283 Move Zeroes — 順序有意義，改用快慢指標
- LC 26 Remove Duplicates from Sorted Array — 順序有意義，改用快慢指標
- LC 80 Remove Duplicates from Sorted Array II — 順序有意義，改用快慢指標

<!-- 6da8d4e90b80 -->
#### 反轉陣列

這個模板最陽春的骨架：沒有配對要評估，只是交換、兩邊一起往內走。

<!--CODE-->

<!-- 468c0e969516 -->
### 模板 3：從中心擴張 — LC 5 ⭐⭐⭐⭐

**辨識** — 「最長回文**子字串**」、「計算回文子字串的個數」。
n ≤ 1000 → 預期就是 O(n²)。

**關鍵點** — 每個回文都是從它的中心擴張出來的，而中心只有 2n − 1 個：
n 個字元（奇數長度）和 n − 1 個縫隙（偶數長度）。對每個中心在兩端相同時持續擴張，
每個回文子字串恰好被走訪一次。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n²)**：`for center` 迴圈做 2n − 1 次擴張，`expand` 裡的每個 `while`
  最多跑 n/2 步（全部相同的輸入 `"aaaa"`）。
- **空間 O(1)**：只存索引；子字串在最後切一次。

**試跑**

<!--CODE-->

**陷阱**
- 只試奇數中心：`"cbbd"` 會回傳 `"c"` — 兩個 `b` 之間的偶數中心從沒試過。
- `while` 結束時兩側都多走了一步；之後讀 `s[left:right + 1]`
  會多包進兩個不相等的字元。用 `left + 1` 和 `right`（左閉右開）。
- 每成功擴張一步就切一個新字串，會把 O(n²) 變成 O(n³)；比較長度，最後才切一次。

**不是這個，如果** — 回文**子序列**（字元可以跳過）→ 區間 DP（LC 516）；
n ≤ 10⁵ 需要 Manacher 的 O(n)。回文題型的完整討論在 [palindrome.md](./palindrome.md)。

**練習** — LC 5 ⭐ · LC 647 ⭐ *（計數：每成功擴張一步就加 1）* · LC 680 *（收斂
指標加一次跳過 — 模板 2）* · LC 516 *（子序列 → DP；長得像但不是）*

> LC 5 與 LC 647 的完整解法在 [2_pointers_examples.md](./2_pointers_examples.md)。

<!-- 0843851e6c5b -->
### 模板 4：子序列比對，一個指標永遠前進 — LC 392 ⭐⭐⭐⭐

**辨識** — 「`s` 是不是 `t` 的**子序列**」、「在不改變順序的前提下刪掉一些字元而形成」、
「查詢字串符不符合樣式」。len(t) ≤ 10⁴ → 一趟 O(|t|) 掃描。

**關鍵點** — 把 `s[i]` 配給**最早**出現的相同 `t[j]`，絕不會比配給較晚的更差：
它替 `s[i+1:]` 留下 `t` 最長的後綴。所以 `j` 每一步都前進、`i` 只在
匹配時前進，最後 `i == len(s)` 就代表每個字元都依序放好了。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(|t|)**：`for j` 迴圈把 `t` 的每個字元走一次；`i` 只是跟著。
- **空間 O(1)**：兩個索引。

**試跑**

<!--CODE-->

**陷阱**
- 沒檢查 `i < len(s)` 就讀 `s[i]`：`s = "a"`、`t = "aa"` 在第二個 `a` 時會讀到 `s` 結尾之外。
- 回傳 `j == len(t)` 而不是 `i == len(s)`：`j` 一定會走到底，所以在
  `"ahbgdc"` 裡找 `"axc"` 也會回傳 true。
- `s = ""` 是任何字串（包括 `""`）的子序列；迴圈不能要求有匹配才回傳 true。
- 對同一個 `t` 有大量查詢（LC 392 進階題、LC 792）：每次查詢都重掃 `t` 是 O(k·|t|)。
  先替每個字元建好排序的索引清單，再用二分搜尋找下一個位置。

**不是這個，如果** — **連續**的比對（子字串）→ KMP 或視窗；兩個字串的最長共同子序列
→ DP（LC 1143），因為那裡貪婪選擇不再安全。

**練習** — LC 392 ⭐ · LC 524 *（用這個掃描測字典裡的每個字）* · LC 1023 *（沒配到的
`t` 字元必須是小寫）* · LC 809 *（以相同字元的群組為單位前進）* · LC 792 *（大量
查詢：索引清單 + 二分搜尋）*

<!-- f0f4381134bf -->
### 模板 5：合併兩個有序陣列，從後面往前填 — LC 88 ⭐⭐⭐⭐⭐

**辨識** — 「把 `nums2` **原地**合併進 `nums1`」、「`nums1` 長度為 m + n，最後 n 個是
0，請忽略」。m + n ≤ 200，但重點是 O(1) 額外空間。

**關鍵點** — 把剩下**最大**的值寫進最後一個空位。寫入索引
`write = p1 + p2 + 1` 永遠 ≥ `p1`，所以一格只會在被讀過（或本來就是空的）之後才被寫：
往後合併不需要緩衝區，往前合併則會蓋掉還沒讀的 `nums1` 值。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(m + n)**：`while p2 >= 0` 每一輪寫一格並把 `write` 往左移；格子最多
  m + n 個。
- **空間 O(1)**：合併就在 `nums1` 自己的空尾端進行。

**試跑**

<!--CODE-->

**陷阱**
- 由左往右合併會蓋掉還沒讀的值：`nums1 = [4,5,6,0,0,0]`、`nums2 = [1,2,3]` 會在讀到 `4`
  之前就把 `1` 寫在它上面。
- `while p1 >= 0 and p2 >= 0` 卻沒有收尾：`nums1 = [0]`、m = 0、`nums2 = [1]` 永遠不會複製那個 `1`。
  只看 `p2` 當迴圈條件，或事後補上 `nums1[:p2 + 1] = nums2[:p2 + 1]`。
- 沒檢查 `p1 >= 0` 就讀 `nums1[p1]`：在 Python 裡 `nums1[-1]` 會默默讀到最後一格，而不是報錯。
- `nums1 = sorted(nums1 + nums2)` 只是重新綁定區域變數；呼叫者的陣列沒變。
- `write` 從 `m - 1` 開始：`nums1` 有 m + n 格；第一次寫必須在 `m + n - 1`。

**不是這個，如果** — 兩條**鏈結串列**：沒有空尾端可以填，所以用虛擬頭節點
往前合併（LC 21）；k 個有序輸入 → 堆積（LC 23）。

**練習** — LC 88 ⭐ · LC 977 *（最大的平方在兩端之一：結果從後往前填）* ·
LC 350 *（兩個有序指標，保留共同的值）* · LC 986 *（推進先結束的那個區間）* ·
LC 4 *（只在概念上合併 — 改成二分搜尋切分點）*

<!-- bd9765f0f9ec -->
### 模板 6：三向分割（荷蘭國旗）— LC 75 ⭐⭐⭐⭐

**辨識** — 「**一趟**、常數空間排好只有 0、1、2 的陣列」、「依樞紐分成小於／等於／
大於三組」。O(1) 空間、單一趟。

**關鍵點** — 每一步都維持四個區域成立：`nums[0..low-1]` = 0、
`nums[low..mid-1]` = 1、`nums[mid..high]` = 未看過、`nums[high+1..]` = 2。每一步讓
未看過的區域縮小一格，所以迴圈在 `mid > high` 時結束。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`while mid <= high` 每一輪不是把 `mid` 往右就是把 `high` 往左，所以
  未看過的區域每次縮小一格。
- **空間 O(1)**：三個索引；交換都是原地進行。

**試跑**

<!--CODE-->

**陷阱**
- 跟 `high` 交換後也推進 `mid`：`[1,2,0]` 會變成 `[1,0,2]` — 從右邊換過來的 `0`
  從沒被檢查。
- `while mid < high`：`[1,0]` 在 `mid == high` 還沒檢查時就停下，維持 `[1,0]`。

**不是這個，如果** — 必須保持相對順序（**穩定**分割）：串列的穩定分割（LC 86）
見 [linked_list.md 的兩個虛擬節點](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-)，
陣列則用模板 1 的快慢指標保持順序（LC 283）。

**練習** — LC 75 ⭐ · LC 905 *（兩組：一個交換指標就夠）* · LC 2161 *（穩定 — 需要
第二趟或額外陣列）* · LC 215 *（quickselect 裡就是同一種三向分割，見
[quickselect.md](./quickselect.md)）*

<!-- 91b71ce50ff2 -->
### 模板 7：快慢指標偵測環 — LC 141 ⭐⭐⭐⭐

**辨識** — 「鏈結串列有沒有環」、「回傳環開始的節點」、
「不修改陣列、O(1) 額外空間找出重複數字」。O(1) 記憶體就排除了 visited 集合。

**關鍵點** — `fast` 一次走兩步，`slow` 一步。兩者都進入長度 c 的環之後，
它們的距離每步縮小一，所以在 c 步內相遇；沒有環的話 `fast` 會走到 `None`。
找入口（LC 142）：頭到入口的距離 ≡ 相遇點到入口的距離（mod c），所以一個從
`head`、一個從相遇點出發，各走一步，恰好在入口相遇。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：相遇之前，`slow` 最多走 n 步（尾巴長度 + 一圈）；找入口
  的那段最多再 n 步。
- **空間 O(1)**：兩三個節點參考。

**陷阱**
- 只檢查 `fast.next`：無環的 `[1,2]` 跳一次後 `fast` 就是 `None`，`fast.next`
  會丟例外。先檢查 `fast`，再檢查 `fast.next`。
- 無環串列上的 LC 142 必須回傳 `None`；無條件執行找入口那段（根本沒相遇）
  會對 `null` 取值。
- 從 `fast = head.next` 開始會改變距離：在 `[3,2,0,-4]`（尾巴接回索引 1）上，頭／相遇點
  那段永遠走不到入口，無限迴圈。
- 比較 `slow.val == fast.val` 會在無環的 `[1,1,1]` 上回報有環。要比較節點本身。

**不是這個，如果** — 允許 O(n) 記憶體且重視清楚 → visited 集合；以節點為單位的這一族
（中間節點、倒數第 n 個、重排、回文串列）在 [2_pointers_linkedlist.md](./2_pointers_linkedlist.md)。

**練習** — LC 141 ⭐ · LC 876 *（`fast` 走到底時停：`slow` 就是中間）* · LC 202 *（隱含的
串列：n → 各位數平方和）* · LC 142 *（找入口那段）* · LC 287 *（把陣列當成串列
`i → nums[i]`；重複的數字就是入口）*

<!-- a2124deb57bf -->
### 模板 8：右邊長、左邊縮 — LC 567

**辨識** — 「`s2` 是否包含 `s1` 的某個排列」、「視窗依一個條件變大、
依另一個條件縮小」。n ≤ 10⁴。這是滑動視窗的骨架，這裡留著它的雙指標
視角；[sliding_window.md](./sliding_window.md) 才是這一族的主場。

**關鍵點** — `right` 只增不減、`left` 也只增不減，所以每個索引進出視窗各
一次。`while` 之後視窗永遠不會比 `s1` 長，所以在那裡比較計數，就把每個
長度為 `len(s1)` 的視窗恰好測過一次。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(26·n)**：`right` 和 `left` 各走過 `s2` 一次（內層 `while` 攤銷 O(1)）；
  26 格的比較每個 `right` 跑一次。
- **空間 O(26)**：兩個計數陣列。

**陷阱**
- `len(s1) > len(s2)`（`"abc"`、`"ab"`）：迴圈永遠湊不出完整視窗，正確地回傳
  false — 不要一開始就切 `s2[:len(s1)]` 並假設它存在。
- 在縮小之前就測 `window == need`，比較的是多一個字元的視窗。

**不是這個，如果** — 視窗大小不固定、條件是和或計數 → 
[sliding_window.md](./sliding_window.md) 裡的可變視窗卡片；和裡有負值 → 前綴和。

**練習** — LC 567 ⭐ · LC 438 *（收集每個起點而不是直接回傳）* · LC 209 *（和仍
≥ target 時就縮小）* · LC 76 *（have/need 計數器 — sliding_window 的卡片）*

<!-- 533bd2807ee7 -->
### 模板 9：兩條序列同步前進 — LC 165 ⭐⭐⭐⭐

**辨識** — 「比較 version1 和 version2」、「這兩個字串是否只差一次編輯」、「套用退格後再比較」、
「求兩串已排序區間的交集」。兩份輸入，各自由**自己的指標**讀過一次，每一輪讀一個 token：
一個修訂號、一個有效字元、一個區間。要求 O(n + m) 時間、O(1) 空間。

**關鍵** — 每一輪從**兩邊**各取一個 token 來比較；第一組不相等的 token 就決定答案，所以
它之前的內容永遠不會再看第二次。只要**任一邊**還有輸入就繼續跑，並讓讀完的一邊產生中性 token
（`0`、空字串），長度不同就不再是特例。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n1 + n2)**：兩個內層 `while` 只會讓 `i`、`j` 往前走，外層迴圈在兩者都
  走過自己的字串時結束。
- **空間 O(1)**：兩個指標和兩個解析出來的整數；不用 `split`。

**試跑** — `version1 = "1.01"`、`version2 = "1.001.1"`

<!--CODE-->

**陷阱**
- `while i < n1 and j < n2`（AND）會停在較短的字串：`"1.0.1"` 對 `"1"` 回傳 0
  而不是 1。
- 把修訂號當字串比：`"1.10"` 對 `"1.2"` — 以文字來看 `"10" < "2"`。要解析成整數，
  這樣 `"01"` 也會等於 `"001"`。
- Python 裡用 `split(".")` 再補零比較短，可以提一下，但它要 O(n) 額外空間；指標寫法才是
  O(1) 的答案。

**不適用時** — 兩條序列可能不按順序配對、或可任意跳過 → 子序列檢查
（[模板 4](#template-4-subsequence-matching-one-pointer-always-moves--lc-392-)）
或 LCS DP；兩份輸入都已排序、目標是合併成一份輸出 → [模板 5](#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-)。

**練習** — LC 165 ⭐ · LC 844 *（token 是退格後留下的下一個字元 —
兩個字串都從後面往前走）* · LC 161 *（停在第一個不匹配處，比較後綴）* ·
LC 953 *（相鄰單字、排名表 — [hash_map.md](./hash_map.md#template-6-rank-map--value-to-position--lc-953-)）* ·
LC 986 *（區間：先結束的那個前進 — [intervals.md](./intervals.md)）*

<!-- 7ed430207608 -->
## 練習路徑

| 階段 | 題目（易 → 難） | 增加了什麼 |
|---|---|---|
| **核心** ⭐ | LC 125 · LC 141 · LC 11 · LC 5 · LC 647 | 帶跳過的收斂、Floyd、矮牆論證、兩種中心、以擴張計數 |
| **練習** | LC 26 · LC 88 · LC 392 · LC 167 · LC 75 · LC 142 · LC 15 | 壓縮、往後合併、貪婪子序列、有序配對、三個區域、環的入口、固定一個 + 收斂（[n_sum.md](./n_sum.md)） |
| **延伸** | LC 80 · LC 881 · LC 287 · LC 42 | 最多 k 份、排序後貪婪配對、陣列上的 Floyd、兩個累計最大值 |

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- 1056b68a8465 -->
### 模式選擇表

| 模式 | 什麼時候用 | 範例題目 |
|---------|-------------|------------------|
| **反向對走** | 有序陣列、回文檢查 | LC 167, LC 344, LC 125 |
| **同向（快慢）** | 去重、環偵測 | LC 26, LC 27, LC 142 |
| **滑動視窗** | 子陣列／子字串題 | LC 3, LC 76, LC 209 |
| **合併兩串** | 合併有序陣列／串列 | LC 88, LC 21 |
| **分割** | 重排元素 | LC 75, LC 86 |
| **可刪字元的回文** | 允許 k 次修改 | LC 680, LC 1216 |
| **固定一個 + 雙指標（精確）** | 和 == target；蒐集所有解 | LC 15, LC 18 |
| **固定一個 + 雙指標（最接近）** | 和最接近 target | LC 16, LC 259 |
| **子序列比對** | 判斷一個字串是不是另一個的子序列 | LC 392, LC 524, LC 792 |
| **帶限制的樣式比對** | 子序列 + 字元型態驗證 | LC 1023 |
| **最長回文前綴** | 找最長回文前綴，把反轉後的後綴接到前面 | LC 214, LC 336 |
| **長度前綴（編碼／解碼）** | 解析 `len#word` 區塊；`i` 依宣告的長度跳 | LC 271, LC 297 |
| **收斂的 low/high（建排列）** | 貪婪：看訊號取用最小／最大的可用值 | LC 942 |
| **最後出現位置 + 擴張右界** | 貪婪分段；`i == end` 時切一刀 | LC 763 |
| **階梯法（有序二維矩陣）** | 在列與行都有序的格子上搜尋 | LC 240 |
| **兩個字串同步走** | 逐段比較／解析兩個序列 | LC 165, LC 953, LC 14 |
| **三階段區間掃描** | 把一個區間插入／合併進有序清單 | LC 57, LC 56 |

<!-- c44ac2987170 -->
### 依難度分類的經典題

<!-- 6fe53f34d06f -->
#### Easy

- LC 26 Remove Duplicates from Sorted Array
- LC 27 Remove Element
- LC 125 Valid Palindrome
- LC 283 Move Zeroes
- LC 344 Reverse String
- LC 345 Reverse Vowels of a String
- LC 349 Intersection of Two Arrays
- LC 350 Intersection of Two Arrays II
- LC 392 Is Subsequence
- LC 680 Valid Palindrome II
- LC 844 Backspace String Compare
- LC 942 DI String Match
- LC 953 Verifying an Alien Dictionary
- LC 977 Squares of a Sorted Array
- LC 14 Longest Common Prefix（同步掃過所有字串的同一個字元位置）

<!-- 3c4f48a7044f -->
#### Medium

- LC 3 Longest Substring Without Repeating Characters（滑動視窗）
- LC 5 Longest Palindromic Substring
- LC 11 Container With Most Water
- LC 15 3Sum
- LC 16 3Sum Closest
- LC 18 4Sum
- LC 75 Sort Colors（Dutch National Flag）
- LC 80 Remove Duplicates from Sorted Array II
- LC 86 Partition List
- LC 88 Merge Sorted Array
- LC 142 Linked List Cycle II
- LC 165 Compare Version Numbers
- LC 167 Two Sum II - Input Array Is Sorted
- LC 240 Search a 2D Matrix II（階梯式雙指標）
- LC 57 Insert Interval（三階段指標掃描）
- LC 763 Partition Labels
- LC 209 Minimum Size Subarray Sum（滑動視窗）
- LC 287 Find the Duplicate Number
- LC 567 Permutation in String（滑動視窗）
- LC 647 Palindromic Substrings
- LC 713 Subarray Product Less Than K
- LC 881 Boats to Save People
- LC 986 Interval List Intersections
- LC 1023 Camelcase Matching

<!-- f37feee152fd -->
#### Hard

- LC 42 Trapping Rain Water
- LC 76 Minimum Window Substring（滑動視窗）
- LC 214 Shortest Palindrome
- LC 828 Count Unique Characters of All Substrings

<!-- 3132650fb979 -->
### 面試提示

| 訊號 | 模式 |
|--------|---------|
| 「排序 + 找一組配對」 | 排完序後用左右指標 |
| 「原地移除／去重」 | 慢快寫指標 |
| 「鏈結串列有沒有環」 | 龜兔賽跑 |
| 「分成 3 組」 | Dutch national flag |
| 「原地合併有序序列」 | 從後往前填 |
| 「回文檢查」 | 從兩端往內走的指標 |
| 「最大面積／容器」 | 縮掉比較矮的那一側 |

<!-- 0bac60c6fe1a -->
### 相關文件

- [binary_search.md](./binary_search.md) — 左右指標，但每次把範圍砍半而不是掃過去
- [sliding_window.md](./sliding_window.md) — 由條件驅動的視窗
- [n_sum.md](./n_sum.md) — 固定一個元素 + 收斂指標（LC 15、LC 16、LC 18）
- [2_pointers_examples.md](./2_pointers_examples.md) — LC 題目實作集
- [quickselect.md](./quickselect.md) — 用 partition 找第 K 個元素
