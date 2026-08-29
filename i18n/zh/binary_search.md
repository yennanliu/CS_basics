<!-- 51e9781a0030 -->
# 二分搜尋

> **範圍** — 對**單調**搜尋空間做折半 — `l <= r` 與 `l < r` 背後的迴圈不變式推理、邊界（lower / upper bound）模板、旋轉陣列，以及浮點數與二維搜尋。
> **另見** — *從本文件拆出去的深入主題*：[binary_search_on_answer.md](./binary_search_on_answer.md) — 對*答案空間*做搜尋：`canFinish` / `isValid` 判定式、最小化最大值 vs 最大化最小值，以及值域計數；[binary_search_examples.md](./binary_search_examples.md) — 題解存放處，每題一份標準解。
> *鄰近文件*：[sort.md](./sort.md) — 先把陣列排好序；[advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — 折半*外加*合併步驟；[bst.md](./bst.md) — 同一個不變式做成資料結構；[heap.md](./heap.md) — 不用整體排序就取第 k 大元素。

<!-- d25f0f2274da -->
## LeetCode 題目清單

- [Binary Search](https://leetcode.com/problem-list/binary-search/)

<!-- 9bcc867ee71e -->
## 總覽

**二分搜尋（Binary Search）**是一個用雙指標在**已排序的搜尋空間**中尋找目標值的高效演算法。

<!-- ff89b5f30c9f -->
### 關鍵性質
- **時間複雜度**：O(log n)
- **空間複雜度**：迭代 O(1)，遞迴 O(log n)
- **前提條件**：已排序的陣列，或具備單調性
- **搜尋空間**：不限於完全排序的陣列，以下情況都適用：
  - 完全排序的陣列
  - 部分排序的陣列
  - 旋轉排序陣列
  - 任何具備單調性質的空間

<!-- c788377dbe27 -->
### 核心演算法步驟
1. **定義邊界**：把 `left` 與 `right` 指標初始化成涵蓋所有可能情況
2. **定義回傳值**：想清楚要回傳什麼（索引、值、-1 等）
3. **定義結束條件**：選對迴圈條件（`<=`、`<` 或 `< -1`）
4. **更新指標**：依與 target 的比較結果移動邊界

<!-- f06e08396e02 -->
### 何時使用二分搜尋
- **已排序陣列**：找確切值的經典場景
- **單調函數**：只要 `condition(k)` 能推得 `condition(k+1)`，就能用二分搜尋
- **搜尋邊界**：找某個值第一次 / 最後一次出現的位置
- **最佳化問題**：找滿足限制條件的最小 / 最大值

<!-- af4c38522b78 -->
### 參考資料
- **框架**：
  - [labuladong Binary Search Framework](https://labuladong.online/algo/essential-technique/binary-search-framework/)
  - [Binary Search 101 Handbook](https://leetcode.com/problems/binary-search/discuss/423162/Binary-Search-101-The-Ultimate-Binary-Search-Handbook)
- **題目集合**：
  - [Binary Search in Action](https://labuladong.online/algo/frequency-interview/binary-search-in-action/)
  - [Binary Search Problem Set](https://labuladong.online/algo/problem-set/binary-search/)
- **Python 工具**：
  - [Python bisect module](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md) — 插入時維持排序順序
  - [Python Universal Binary Search Template](https://leetcode.com/discuss/general-discussion/786126/python-powerful-ultimate-binary-search-template-solved-many-problems) — 一個模板通吃多題


<p align="center"><img src ="../pic/binary_search_pattern.png" ></p>

<!-- 7f3af396dd96 -->
## 理解二分搜尋的指標行為

<!-- 7ff54d3eb55b -->
### 核心洞見：`l` 和 `r` 到底代表什麼？

這是讓二分搜尋成立的**根本概念**，也解釋了為什麼像 LC 35 這種求插入位置的題目回傳 `l` 是對的。

<!-- 8d4261aef2e6 -->
#### 迴圈進行中：搜尋空間不變式

在整個 `while (l <= r)` 迴圈期間：

- **`l` 左邊的所有元素**都嚴格 `< target`
- **`r` 右邊的所有元素**都嚴格 `> target`
- `target` 可能出現的位置永遠落在 `[l, r]` 之內

每次迭代砍掉一半搜尋空間，同時保持這個不變式。

<!--CODE-->

<!-- 18e50cb7d113 -->
#### 迴圈結束時：指標的位置

迴圈在 `l > r` 時結束，也就是 `l == r + 1`。

在這一瞬間，陣列被切成兩部分：

<!--CODE-->

**迴圈結束時的關鍵性質：**

1. `r` 是**最後一個小於 target 的元素**
2. `l` 是**第一個大於等於 target 的元素**
3. `r` 和 `l` 之間**不存在任何索引**（因為 `r = l - 1`）

這就是為什麼 `l` 正是正確的插入位置！

<!-- 9f9bf0b67afd -->
#### 圖解範例

我們來追蹤 `nums = [1, 3, 5, 6], target = 4`：

<!--CODE-->

<!-- 03bb8c2f90f1 -->
### 總結表

| 狀態 | `l` 的位置 | `r` 的位置 | 意義 |
|-------|-------------|--------------|---------|
| **迴圈進行中** | 第一個未檢查且 >= target 的索引 | 最後一個未檢查且 <= target 的索引 | 搜尋空間是 `[l, r]` |
| **迴圈結束** | 第一個 >= target 的元素 | 最後一個 < target 的元素 | `l` 就是插入點 |
| **圖示** | `... r \| l ...` | 兩者之間沒有空隙 | `l = r + 1` |

<!-- 5af68368e1a5 -->
### 應用：Search Insert Position (LC 35)

<!--CODE-->

**為什麼不需要特判就能成立：**

1. 若 target 存在：迴圈中就直接回傳 mid
2. 若 target 不存在：
   - 迴圈以 `l > r` 結束
   - 依不變式：`nums[0..l-1] < target` 且 `nums[l..end] >= target`
   - 因此 `l` 正是 target 該被插入的位置

<!-- 7d9d57823383 -->
### 該避開的常見錯誤

<!--CODE-->

**為什麼這樣寫是錯的**：二分搜尋本來就會自然收斂到正確位置。相信指標不變式，迴圈結束後直接回傳 `l` 就好。

---

<!-- 44335a6bb19f -->
## 1) 二分搜尋的類型與模式

<!-- 592fb5b03d86 -->
### 1.1) 類型速覽

**基本二分搜尋 — LC 704**（標準模板見 §2.1）
- **目的**：在已排序陣列中找出確切的目標值
- **回傳**：target 的索引，找不到則回傳 -1
- **複雜度**：O(log n)

**遞迴版二分搜尋**
- **使用時機**：遞迴寫法比較直觀時
- **空間**：因呼叫堆疊而為 O(log n)

**旋轉陣列搜尋**（§1.2）
- **關鍵概念**：判斷哪一半是有序的，再決定往哪邊搜尋
- **應用**：找 target、找最小元素

**找邊界 — 左 / 右，即 lower / upper bound**（§1.3）
- **目的**：找出第一個 / 最後一個滿足判定式的索引，而不是某個確切值
- **回傳**：分界點 — 絕不 `return mid`

**二維矩陣搜尋**（§2.3）
- **做法 1**：用 `row = idx / cols`、`col = idx % cols` 把矩陣攤平
- **做法 2**：逐列做二分搜尋
- **時間**：O(log(m×n))

**對答案空間做二分搜尋**（§1.4）
- **目的**：搜尋的是*候選答案的範圍*，而不是一個陣列
- **回傳**：單調可行性判定式的分界點

<!-- 88c893e86307 -->
### 1.2) 旋轉排序陣列 — 找出樞紐點

- **關鍵概念**：判斷哪一半是有序的，再決定往哪邊搜尋
- **應用**：找 target、找最小元素

<!-- 66fa00c4469f -->
#### Find Minimum in Rotated Sorted Array (LC 153) ⭐⭐⭐⭐⭐

<!-- b538e90f0180 -->
##### 模式：找出旋轉點

旋轉排序陣列一定長這個樣子：

<!--CODE-->

**最小值永遠在旋轉點上** — 也就是陣列中唯一那個「下墜」的位置。

<!-- 8fae5d09ac1a -->
##### 核心想法

判斷 `mid` 落在哪一段平台上，然後往**沒有排序的那一側**移動（最小值就在那裡）：

<!--CODE-->

**判斷規則：**
- `nums[mid] >= nums[l]` → mid 在**左平台**上 → 最小值在右邊 → `l = mid + 1`
- `nums[mid] < nums[l]`  → mid 在**右平台**上 → 最小值就是 mid 或在其左邊 → `r = mid - 1`

<!-- 9ff9298baa77 -->
##### 推薦模板（閉區間邊界，追蹤 `ans`）

<!--CODE-->

<!-- 33db6972b020 -->
##### 替代模板（開區間邊界 `r > l`，不用 `ans` 變數）

<!--CODE-->

<!-- b26340d8d6ba -->
##### 視覺追蹤：`nums = [3,4,5,1,2]`

<!--CODE-->

<!-- 69e162b938fa -->
##### 模板比較

| 模板 | 迴圈條件 | 更新方式 | 回傳 | 適用時機 |
|----------|---------------|--------|--------|-----------|
| 閉區間 `l <= r` + `ans` | `l <= r` | `l=mid+1` / `r=mid-1` | `ans` | 需要記錄候選答案時 |
| 開區間 `r > l` | `r > l` | `r=mid` / `l=mid+1` | `nums[l]` | 最乾淨，會收斂到索引 |

<!-- b39a2e6f766e -->
##### 相似題目

| LC # | 題目 | 關鍵差異 |
|------|---------|---------------|
| **153** | Find Minimum in Rotated Sorted Array | 元素不重複，找最小值 |
| **154** | Find Minimum in Rotated Sorted Array II | 有重複值 — `nums[mid]==nums[r]` 時用 `r--` |
| **33** | Search in Rotated Sorted Array | 找 target（不是最小值）— 要檢查 target 是否落在有序的那一半 |
| **81** | Search in Rotated Sorted Array II | 在有重複值的情況下找 target |
| **189** | Rotate Array | 概念相關，但任務不同 |

<!-- 826be5f2e70b -->
#### Search in Rotated Sorted Array (LC 33, LC 81)
<!--CODE-->

<!--CODE-->

**關鍵差異**：
- **LC 153**（找最小值）：只需要判斷該往哪一側搜尋
- **LC 33/81**（找 target）：還必須檢查 target 是否落在有序的那一半裡

<!-- e9579f387d51 -->
### 1.3) 找邊界 — Lower 與 Upper Bound (LC 34) ⭐⭐⭐⭐⭐

**目的**：在有重複值的**非遞減**陣列中，找出 target 第一次與最後一次出現的位置

<!-- bd2065940a8e -->
#### 模式：兩次獨立的邊界搜尋

有重複值的排序陣列可以看成**三個區塊**：

<!--CODE-->

因為相等的那個區塊是**連續的**，一次「找確切值」的二分搜尋毫無用處（它會停在區塊內的任意位置）。正確做法是跑**兩次獨立的搜尋**，各自去找一個*區塊邊緣*：

- `findLeft`  → 第一個滿足 `nums[i] >= target` 的索引（相等區塊的起點）
- `findRight` → 最後一個滿足 `nums[i] <= target` 的索引（相等區塊的終點）

<!-- f96d07041628 -->
##### 核心想法 ⭐⭐⭐⭐⭐

**關鍵想法**：不要去找那個值 — 去找「太小」與「夠大」之間的**分界點**。絕不提早 `return mid`；持續縮小範圍，把指標擠到邊緣上。

這兩個輔助函式是**只差一個字元的同一段程式碼**（`<` vs `<=`），而且它們**完全不做相等判斷**：

| 輔助函式 | 移動 `l` 的條件 | 相等時走哪條路 | 回傳 |
|--------|----------------------|---------------------|--------|
| `findLeft`  | `nums[mid] < target`  | 走進 `else` → `r = mid - 1`（往左推） | `l` |
| `findRight` | `nums[mid] <= target` | 走進 `if`   → `l = mid + 1`（往右推） | `r` |

**為什麼這樣成立** — 結束時 `l` 與 `r` 已經交錯成 `r == l - 1`，正好夾住分界點：

<!--CODE-->

**為什麼 `[l, r]` 同時也是有效性檢查**（不必再回頭讀 `nums`）：

<!--CODE-->

**與 Python `bisect` 的對應關係** — 把這個對照記起來，兩個輔助函式就再也忘不掉：

<!--CODE-->

<!-- dffa53a9ad41 -->
##### 推薦模板（兩個輔助函式，閉區間 `l <= r`）

<!--CODE-->

<!--CODE-->

<!-- f179a4cb3956 -->
##### 替代解法 1：一個輔助函式，呼叫兩次（`target` 與 `target + 1`）⭐⭐⭐⭐⭐

最乾淨的技巧 — 只寫 `bisect_left`，然後注意到 `target` 最後一次出現的位置
就在 `target + 1` 第一次出現的位置的前一格：

<!--CODE-->

<!-- 3b9e4011a85f -->
##### 替代解法 2：相等時記錄 `bound`（最明確）

保留 `nums[mid] == target` 這個分支，並記下目前為止看到的最佳候選：

<!--CODE-->

<!-- 1aec55ec2787 -->
##### 替代解法 3：`bisect` 一行解（面試的保底寫法 / 驗算用）

<!--CODE-->

<!-- acee3aed8398 -->
##### 視覺追蹤：`nums = [5,7,7,8,8,10]`、`target = 8`

<!--CODE-->

target 不存在的情況，`nums = [5,7,7,8,8,10]`、`target = 6`：

<!--CODE-->

<!-- ef7c6937c226 -->
##### 模板比較

| 模板 | 邊界 | 迴圈 | 「太小」時的更新 | 回傳 | 適用時機 |
|----------|----------|------|----------------------|--------|-----------|
| 兩個輔助函式 `<` / `<=` | 閉區間 `[l, r]` | `l <= r` | `l = mid + 1` / `r = mid - 1` | `l` / `r` | 推薦 — 對稱、沒有相等分支、`l <= r` 順便驗證 |
| 一個輔助函式，`target` 與 `target+1` | 半開區間 `[lo, hi)` | `lo < hi` | `lo = mid + 1` / `hi = mid` | `lo` | 要背的程式碼最少；只適用於整數 target |
| 相等時追蹤 `bound` | 閉區間 `[l, r]` | `l <= r` | 保留三向 `if/elif/else` | `bound` | 口頭講解時最好懂 |
| `bisect_left` / `bisect_right` | — | — | — | — | 允許用函式庫的 Python 面試 |

<!-- b7f0d8b95d20 -->
##### 常見陷阱

- ❌ `nums[mid] == target` 時就 `return mid` → 停在區塊中間，而不是邊緣
- ❌ 從 `findLeft` 回傳 `r`（或從 `findRight` 回傳 `l`）→ 差一錯誤；**左搜尋回傳 `l`，右搜尋回傳 `r`**
- ❌ 混用邊界風格：`hi = len(nums)` 必須搭配 `while lo < hi` 與 `hi = mid`（不減 1）；`r = len(nums)-1` 必須搭配 `while l <= r` 與 `r = mid - 1`
- ❌ 忘了空陣列的情況 — 兩個閉區間輔助函式都會自然處理（`l=0, r=-1` → 跳過迴圈 → `l=0 > r=-1` → `[-1,-1]`）
- ❌ 事先檢查 `if target not in nums` — 那是 O(N)，直接毀掉 O(log N) 的要求

<!-- 98f074ee7c0b -->
##### 相似題目

| LC # | 題目 | 關鍵差異 |
|------|---------|---------------|
| **34** | Find First and Last Position of Element in Sorted Array | 基準題 — 兩個邊界都要 |
| **35** | Search Insert Position | 只用 `findLeft`，回傳 `l` 且不做驗證 |
| **704** | Binary Search | 只找確切值，不需處理重複 |
| **278** | First Bad Version | 對布林判定式（而非 `<`）做 `findLeft` |
| **852 / 162** | Peak Index / Find Peak Element | 以 `nums[mid] < nums[mid+1]` 為邊界條件 |
| **744** | Find Smallest Letter Greater Than Target | `bisect_right` + 取模繞回 |
| **1146** | Snapshot Array | 對每個索引的版本清單做 `bisect` |
| **658** | Find K Closest Elements | 用 `findLeft` 定位視窗起點，再往外擴 |
| **300** | Longest Increasing Subsequence (O(N log N)) | 用 `bisect_left` 替換 tails |
| **981** | Time Based Key-Value Store | `findRight`（最大且 `<=` 查詢值的時間戳） |
| **436** | Find Right Interval | 對排序後的起點做 `findLeft` |
| **1898** | Maximum Number of Removable Characters | 對答案做邊界搜尋 + 可行性檢查 |

<!-- ca04c67efae4 -->
### 1.4) 對答案空間做二分搜尋 ⭐⭐⭐⭐⭐

不是在陣列**裡面**找某個值，而是對**候選答案的範圍**做二分搜尋，並用一個可行性判定式決定要留下哪一半。它是 tier-5 的二分搜尋技巧中最少被練到的一項，因此獨立成一份文件：

> **完整內容** — `canFinish` / `isValid` 的思考框架、最小化 vs 最大化的
> 決策矩陣、`left = max(nums)` / `right = sum(nums)` 的邊界配方、
> 單調判定式的證明、值域計數，以及所有題解
> （LC 875、410、1011、1283、1482、1231、2616、1631、378、287、1539）：
> **[binary_search_on_answer.md](./binary_search_on_answer.md)**。

**辨識關鍵字**：「minimize the maximum」、「maximize the minimum」、「找出最小的
capacity / speed / divisor」、「能不能切分 / 分配 / 派送」。

<!-- b7959cdc6094 -->
### 1.5) 相關演算法與資料結構

**互補演算法**：
- **雙指標**：用於沒有隨機存取能力的已排序序列
- **滑動視窗**：用於具備特定性質的子陣列問題
- **遞迴**：另一種實作方式

**資料結構**：
- **陣列**：二分搜尋的主要應用場景
- **二元搜尋樹**：樹走訪中隱含的二分搜尋
- **雜湊表**：不需要排序時的 O(1) 查詢替代方案

<!-- b37869ec1eb6 -->
## 2) 二分搜尋模板與模式

<!-- 3db4140d8c34 -->
### 補充資源
- [Binary-Search-101-The-Ultimate-Binary-Search-Handbook](https://leetcode.com/problems/binary-search/discuss/423162/Binary-Search-101-The-Ultimate-Binary-Search-Handbook)
- [Python Universal Binary Search Template](https://leetcode.com/discuss/general-discussion/786126/python-powerful-ultimate-binary-search-template-solved-many-problems)

<!-- 490b35e3d4cb -->
### 2.0) 迴圈結束條件比較

**關鍵差異**：結束條件決定迴圈何時停止，也連帶影響邊界的處理方式。

| 條件 | 邊界型態 | 使用時機 | 主要特徵 |
|-----------|---------------|-------------|-------------------|
| `while (l <= r)` | **閉區間 [l, r]** | 標準二分搜尋 | • 最常見的做法<br>• 搜尋空間同時包含 l 與 r<br>• 需要 `l = mid + 1`、`r = mid - 1` |
| `while (l < r)` | **半開區間 [l, r)** | 找邊界 / 插入位置 | • 搜尋空間不含 r<br>• `l == r` 時迴圈結束<br>• 使用 `l = mid + 1`、`r = mid` |
| `while (l < r - 1)` | **保留間隙** | 特殊情況下避免無窮迴圈 | • 確保 l 與 r 永不相鄰<br>• 迴圈結束後需要再做一次檢查<br>• 較少見，用於複雜條件 |

**詳細分析：**

<!--CODE-->

<!--CODE-->

<!--CODE-->

**各自的使用時機：**

- **`while (l <= r)`**：經典二分搜尋，找確切值
- **`while (l < r)`**：找第一次 / 最後一次出現的位置、插入位置、找峰值
- **`while (l < r - 1)`**：mid 可能等於 l 或 r 的複雜條件

<!-- 6bd09c874c55 -->
#### 依模式分類的經典 LeetCode 題目

**模式 1：`while (l <= r)` — 精確搜尋**
- LC 704: Binary Search（基本實作）
- LC 33: Search in Rotated Sorted Array
- LC 81: Search in Rotated Sorted Array II
- LC 74: Search a 2D Matrix
- LC 240: Search a 2D Matrix II
- LC 69: Sqrt(x)
- LC 367: Valid Perfect Square
- LC 441: Arranging Coins

**模式 2：`while (l < r)` — 找邊界 / 找峰值**
- LC 34: Find First and Last Position of Element
- LC 35: Search Insert Position
- LC 162: Find Peak Element
- LC 852: Peak Index in a Mountain Array
- LC 153: Find Minimum in Rotated Sorted Array
- LC 154: Find Minimum in Rotated Sorted Array II
- LC 278: First Bad Version
- LC 658: Find K Closest Elements
- LC 744: Find Smallest Letter Greater Than Target

**模式 3：驗證函式類題目**（LC 410、875、1011、1060、1482）— 這些題二分搜尋的是
*答案*，而不是索引；見 [binary_search_on_answer.md](./binary_search_on_answer.md)。

<!-- 039e50264bd0 -->
### 2.1) 標準二分搜尋模板 — LC 704

**關鍵原則**：
- **初始化**：`left = 0, right = nums.length - 1`（閉區間）
- **迴圈條件**：`while (left <= right)`  
- **指標更新**：`left = mid + 1`、`right = mid - 1`
- **清晰度小訣竅**：所有條件都用 `else if`，把邏輯攤開講明白

> **寫程式小訣竅**：避免使用 `else` — 把所有條件都寫成 `else if`，清楚列出每種情況，減少 bug。

<!--CODE-->

<!--CODE-->

<!-- 23ba3dc1560d -->
### 2.2) 浮點數二分搜尋
用於答案是實數的題目（開根號、最佳分配、幾何問題）：

<!--CODE-->

<!-- d6d990bc8646 -->
### 2.3) 二維矩陣搜尋 — 兩種不同的題目 (LC 74 / LC 240)

**LC 74**（矩陣的列與行都排序，值由左到右、由上到下遞增）：
<!--CODE-->

**LC 240**（每列排序、每行也排序，但**整體並未排序** — 用階梯搜尋）：
<!--CODE-->
**關鍵洞見**：LC 240 的階梯搜尋每一步消掉一整列或一整行 → O(m+n)。**不要**把 LC 240 當成攤平後的二分搜尋 — 這個矩陣並非整體有序。

**Java** — 同樣的攤平做法，完整寫出來：
<!--CODE-->

<!-- c6645b5b9a41 -->
### 2.4) 雙調 / 山脈陣列 — 遞減順序的翻轉

山脈（雙調）陣列本身沒有排序，但它**確實**是兩段有序序列的接合，
所以需要三次二分搜尋：先用爬坡搜尋找到峰值，接著在左半段做一般的
**遞增**搜尋，最後在右半段做**遞減**搜尋。

**遞減順序的模板**就是把標準模板的比較方向翻過來：

<!--CODE-->

有個漂亮的寫法能把兩者合成一個函式：`if ((val < target) == ascending) l = mid + 1; else r = mid - 1;`

題解範例 — LC 1095 Find in Mountain Array — 見 [binary_search_examples.md](./binary_search_examples.md)。

<!-- 68c1ad485db6 -->
### 2.5) 速查 — 其他帶有二分搜尋味道的題目

這些知名題目都是重用本文件已有的模板；列在這裡是為了讓你一眼認出來，不需要新技巧。

| LC | 題目 | 用哪個模板 |
|----|---------|----------------|
| 275 | H-Index II | 對索引做邊界搜尋：第一個滿足 `citations[i] >= n - i` 的 `i`（§1.3） |
| 1268 | Search Suggestions System | 排序 products，對逐步變長的前綴做 `lower_bound`（§1.3；binary_search_examples.md 的 LC 436）；Trie 是另一種做法 |
| 349 / 350 | Intersection of Two Arrays I / II | 排序較大的陣列，對每個元素做二分搜尋（也可用雜湊集合 / 雙指標） |
| 792 | Number of Matching Subsequences | 每個字元一份排序索引清單 + `upper_bound` 跳到下一次出現處（前綴和 + lower bound 家族 — binary_search_examples.md §19） |
| 222 | Count Complete Tree Nodes | 對**最後一層的節點索引**做二分搜尋，沿著候選值的位元路徑往下走來驗證 — `O(log²n)` |
| 1044 | Longest Duplicate Substring | 對**答案長度**做二分搜尋 + Rabin-Karp 滾動雜湊當判定式（binary_search_on_answer.md） |
| 1385 | Find the Distance Value Between Two Arrays | 排序 `arr2`，對每個 `arr1[i]` 二分搜尋最接近的鄰居 |
| 1346 | Check If N and Its Double Exist | 排序 + 二分搜尋 `2*x`（也可用雜湊集合） |

<!-- 6b58a0929f57 -->
## 3) 總結與速查

<!-- e96de109ff89 -->
### 3.1) 何時該用二分搜尋
✅ **以下情況請用二分搜尋：**
- 陣列有序（完全有序、部分有序或旋轉有序）
- 搜尋空間具備單調性
- 需要 O(log n) 的搜尋效能
- 要找邊界或插入位置
- 具備二元性質的最佳化問題

<!-- 616a3f557623 -->
### 3.2) 模板選擇指南

整份文件用這一張表就夠了：看輸入的形狀，決定該拿哪個模板。

| 題型 / 輸入形狀 | 模板 | 題解範例 |
|---|---|---|
| 在排序陣列中做**精確搜尋** | 標準閉區間 `while l <= r` — §2.1 | LC 704 |
| **左邊界**（第一個 `>= target` 的索引） | Lower bound — §1.3 | LC 34、LC 35、LC 278 |
| **右邊界**（最後一個 `<= target` 的索引） | Upper bound − 1 — §1.3 | LC 34、LC 981 |
| **插入位置** | Lower bound，回傳 `l` 不做驗證 — §1.3 | LC 35 |
| **峰 / 谷**，沒有目標值 | 半開區間 `while l < r`、`r = mid` | LC 162、LC 852 |
| **旋轉**排序陣列 | 找出有序的那一半 — §1.2 | LC 33、LC 81、LC 153、LC 154 |
| 陣列**先升後降**（山脈 / 雙調） | 找峰值 + 兩次有序搜尋，其中一次**遞減** — §2.4 | LC 1095 |
| **二維矩陣** | 整體有序就攤平，只有列 + 行有序就走階梯 — §2.3 | LC 74 vs LC 240 |
| 答案是**實數**、要求精度 | 浮點數 / 固定迭代次數 — §2.2 | LC 69（浮點數版） |
| 「**最小化最大值**」/「**最大化最小值**」 | 對答案做二分搜尋 — [binary_search_on_answer.md](./binary_search_on_answer.md) | LC 410、875、1011、1231、2616 |
| 值域已知，但**陣列沒有排序** | 對值域做二分搜尋 + 計數 — [binary_search_on_answer.md](./binary_search_on_answer.md) | LC 287、LC 378 |
| 可行性判斷需要**走訪圖** | 對答案做二分搜尋 + BFS/DFS 判定式 — [binary_search_on_answer.md](./binary_search_on_answer.md) | LC 1631、LC 778 |
| `O(n log n)` LIS、加權抽樣、有序歷史紀錄 | 對維護中的有序陣列做 `lower_bound` — [binary_search_examples.md](./binary_search_examples.md) | LC 300、LC 354、LC 528、LC 981 |

<!-- 2f2b58b17200 -->
### 3.3) 常見陷阱與訣竅

**🚫 常見錯誤：**
- `mid = (left + right) / 2` 的整數溢位 → 改用 `mid = left + (right - left) / 2`
- 邊界更新寫錯（`mid` 與 `mid ± 1` 搞混）
- 忘了做後續的有效性驗證
- `while l < r` 搭配錯誤的更新方式造成無窮迴圈

**✅ 最佳實務：**
- 一律用 `else if` 讓邏輯清楚
- 邊界搜尋結束後要驗證結果  
- 邊界型態要前後一致（閉區間 vs 半開區間）
- 用邊界情況測試：空陣列、單一元素、重複值

<!-- 422dbae9bba7 -->
### 3.4) 面試訊號 — 該用哪個模式？

| 訊號 | 模式 |
|--------|---------|
| 「找出最小 / 最大的 X 使得……」 | 對答案做二分搜尋 |
| 「已排序陣列，找第一次 / 最後一次出現」 | 左 / 右邊界二分搜尋 |
| 「矩陣的列與行都排序」 | 階梯搜尋（**不是**攤平的二分搜尋） |
| 「答案是實數，要求精度」 | 浮點數二分搜尋 |
| 「我們做得到 X 嗎？」具備單調性 | 對單調判定式做二分搜尋 |
| 已有 O(n) 解法但題目要 O(log n) | 想一想：那個有序的搜尋空間是什麼？ |
