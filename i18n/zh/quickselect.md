<!-- 4456956a33c0 -->
# Quickselect（以分割求第 K 個元素）

> **範圍** — 以分割（partition）為基礎的選擇演算法：只保留 quicksort 分割後的其中一側，在平均 O(n) 時間內找出第 k 大、第 k 小或最接近的 k 個元素；也收它可用的各種分割方式，以及讓最壞情況變成線性的 median-of-medians pivot。LC 215 的 quickselect 解法以這裡為主場；堆積解法在 heap.md。
> **另見**：[heap.md](./heap.md) — 大小為 k 的堆積替代方案，O(n log k) 但適合串流；[2_pointers.md](./2_pointers.md) — 本頁模板所依據的荷蘭國旗分割（LC 75）；[sort.md](./sort.md) — quicksort 本身，以及什麼時候整個排序反而比選擇划算；[advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — 把 quickselect 看成唯一只保留一側的分治遞迴式。

<!-- 37cf84424f85 -->
## LeetCode 題目清單

- [Quickselect](https://leetcode.com/problem-list/quickselect/)
- [Divide and Conquer](https://leetcode.com/problem-list/divide-and-conquer/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

<!-- 28b7f05c1531 -->
## 0) 概念

Quicksort 以 pivot 分割後，會往**兩側**都遞迴。Quickselect 只要一個索引，所以每次分割後只保留含有那個索引的**那一側**，另一側直接丟掉。總工作量是 `n + n/2 + n/4 + … ≈ 2n`，因此**平均 O(n)**，排序要 O(n log n)，大小為 k 的堆積要 O(n log k)。

<!--CODE-->

<!-- 3d2cee45d57f -->
### 關鍵性質

- **複雜度**：平均 O(n)；每一輪都選到不好的 pivot 時最壞 O(n²)，隨機 pivot 讓這件事幾乎不可能發生，median-of-medians pivot 則徹底排除它。寫成迴圈時額外空間 O(1)。
- **核心想法**：分割後，pivot 那一段左邊的值都比較小、右邊的值都比較大，所以把那一段跟目標索引一比，就知道答案只可能在哪一側。
- **什麼時候用**：一次性的「第 k 大／第 k 小／最接近的 k 個」，資料可以重新排列，而且 k 個結果不需要照順序輸出。
- **什麼時候別用**：串流輸入、唯讀輸入，或 k 個結果要照順序：改用大小為 k 的堆積（[heap.md § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-)）。

<!-- ce806acc6fc4 -->
### Quickselect、堆積與排序的比較

| 做法 | 平均時間 | 最壞時間 | 額外空間 | 適合的情況 |
|---|---|---|---|---|
| Quickselect，隨機 pivot | O(n) | O(n²)，幾乎不可能發生 | O(1) | 只問一次，輸入可以重排 |
| Quickselect，median of medians | O(n) | O(n) | 本頁寫法為 O(n) | 題目要求最壞情況的保證 |
| 大小為 k 的堆積 | O(n log k) | O(n log k) | O(k) | 串流、唯讀輸入、k ≪ n |
| 完整排序 | O(n log n) | O(n log n) | O(1) 到 O(n) | 反正也需要排好的順序 |
| 計數／桶排序 | O(n + range) | O(n + range) | O(range) | 整數範圍小，或是按頻率（LC 347） |

<!-- 457fd2b31bf6 -->
### 辨識

| 題目敘述的訊號 | 限制條件線索 | 該用 | 不適用時 |
|---|---|---|---|
| 「第 k 大／第 k 小的元素」、「中位數」 | n ≤ 10⁵、只問一次、追問「能不能比 O(n log n) 更快？」 | [模板 1](#template-1-three-way-partition-quickselect--lc-215-) | 值一個一個到來（LC 703）→ 大小為 k 的[堆積](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) |
| 「最接近的 k 個點」、「最強的 k 個」、「順序不限」 | k 個結果可以不排序回傳 | [模板 2](#template-2-quickselect-on-a-derived-key--lc-973-) | k 個結果必須排好 → 堆積，或先選再把那 k 個排序 |
| 「第 k 大」但值是字串或 tuple | 比較方式不是單純的 `<` | [模板 2](#template-2-quickselect-on-a-derived-key--lc-973-)，用那個比較鍵 | |
| 「排序矩陣中第 k 小」、「第 k 小的數對距離」 | 值沒有實際存在，或多達 n² 個 | | 對值做二分搜尋 → [binary_search_on_answer.md](./binary_search_on_answer.md)（LC 378、LC 719） |

<!-- 0de0d3d03b4b -->
## 1) 一般形式

<!-- 0bd29aa405ad -->
### 模板 1：三向分割 Quickselect — LC 215 ⭐⭐⭐⭐⭐

**辨識** — 「找出第 k 大的元素」、「第 k 小」、「中位數」，只問一次，且允許重排輸入。n ≤ 10⁵ 又被追問要比排序快 → 平均 O(n)。

**關鍵** — 三向分割之後是 `[left, lt) < pivot`、`[lt, gt] == pivot`、`(gt, right] > pivot`，而且每一段裡的每個值，相對於其他段的每個值都已經站在正確的一側。所以目標索引若落在 `[lt, gt]` 裡，答案**就是** pivot；否則只有一段可能含有答案。`==` 那一段讓全部相等的輸入也維持 O(n)：雙向分割遇到 `[2, 2, …, 2]` 每輪只剝掉一個元素。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間平均 O(n)**：內層 `while i <= gt` 掃描把目前區間的每個值各看一次，隨機 pivot 讓區間每輪大約減半，所以掃描總量是 `n + n/2 + n/4 + … < 2n`。最壞 O(n²) 需要每一輪都選到接近極端的 pivot。
- **空間 O(1)**：分割是原地交換，外層 `while True` 取代了遞迴。

**試跑** — `nums = [3, 2, 1, 5, 6, 4]`，k = 2，所以 target = 4。

<!--CODE-->

**陷阱**
- 第 k **大**就是遞增順序中的索引 `n - k`。一開始就轉換好，比把每個比較都反過來安全：只反了兩個分支中的一個，就會回傳第 k 小。
- 在 `> pivot` 的交換之後推進 `i`，會跳過剛從 `gt` 換過來的值：`[1, 3, 2]` 以 1 為 pivot 時，2 最後落在 `==` 那段，於是 k = 2 回傳 1 而不是 2。
- 固定取第一個或最後一個元素當 pivot，遇到已排序的輸入就是 O(n²)，而這正是 LC 215 的對抗性測資。pivot 要隨機挑。
- 雙向（Lomuto）分割即使用隨機 pivot，遇到 `[2, 2, …, 2]` 仍是 O(n²)；`==` 那一段就是用來修這個的。
- 它會重排 `nums`。呼叫端若還需要原本的順序，先複製一份，或改用堆積。

**不適用時** — 值以串流方式到來，或每插入一次就再問一次第 k 個（LC 703）：大小為 k 的堆積（[heap.md § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-)）每次查詢 O(log k)。若候選值是隱含的而不是存起來的（排序矩陣中第 k 小，LC 378），改成對值做二分搜尋。

**練習** — LC 215 ⭐ · LC 75 *（同樣的分割，pivot 已知，當成排序用）* · LC 1985 *（以 `(len, s)` 比較）* · LC 324 *（選出中位數，再交錯排列）*

<!-- a95806da71f7 -->
### 模板 2：以衍生鍵做 Quickselect — LC 973 ⭐⭐⭐⭐

**辨識** — 「回傳最接近的 k 個點」、「最弱的 k 列」、「最強的 k 個值」、「順序不限」：要的是最好的 k 個的**集合**，不是它們的順序，而「最好」是由每個項目算出來的鍵。

**關鍵** — 分割一旦把索引 `k - 1` 定下來，`items[0 .. k-1]` 裡每個鍵都 ≤ 它之後的每個鍵，這段前綴雖然沒排序，卻**就是**答案。所以當 `k - 1` 落在 `==` 那段時迴圈就停，永遠不必把這 k 個結果排序。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間平均 O(n)**：跟模板 1 一樣的減半論證。`dist` 是 O(1)，只要鍵算起來便宜，衍生鍵就不額外花成本；昂貴的鍵應該先算好放進一個平行陣列。
- **額外空間 O(1)**：分割是原地交換列；只有輸出的切片是新的。

**陷阱**
- 用 `sqrt` 只會讓鍵變成浮點數，毫無好處：距離平方的順序完全相同。
- 迴圈還沒結束就取 `points[:k]`，拿到的是任意一段前綴。要等 `k - 1` 定下來，或 `left >= right` 之後才回傳。
- 題目若接著要求 k 個結果**排好序**，就把那段前綴排序：O(n + k log k)，仍比整個排序好。

**不適用時** — 點一個一個到來，或 k 很小、輸入很大又是唯讀：以距離為鍵、大小為 k 的 max-heap（heap.md）在 O(n log k) 內做完，而且什麼都不重排。

**練習** — LC 973 ⭐ · LC 1337 *（鍵 = `(soldiers, row)`）* · LC 1471 *（鍵 = 與中位數的距離，而中位數本身也是一次 quickselect）* · LC 347 *（鍵 = 頻率；桶排序才是真正的 O(n) 解）*

<!-- a416bbd1424e -->
### 分割方式 — Lomuto、Hoare 與三向 ⭐⭐⭐

上面的模板用的是三向分割。另外兩種是面試官說「quicksort 的分割」時指的東西，而每一種都會改變迴圈能測什麼。

| 方式 | 回傳 | 迴圈的判斷 | 弱點 |
|---|---|---|---|
| Lomuto | pivot 的最終索引 `p` | `p == target` → 結束 | 大量重複值時 O(n²) |
| Hoare | 一個分界 `j`，滿足 `a[lo..j] <= pivot <= a[j+1..hi]` | `target <= j` → `hi = j`，否則 `lo = j + 1`，直到 `lo == hi` | `nums[j]` 不一定是 pivot |
| 三向 | 等於 pivot 的那一段 `[lt, gt]` | target 落在那段裡 → 結束 | 用於選擇時沒有；多幾次交換 |

<!--CODE-->

<!--CODE-->

**陷阱**
- 把 Hoare 配上 Lomuto 的迴圈（`p == target`、`right = p - 1`）是經典的錯誤答案：回傳的 `j` 是分界，不是 pivot 的索引。`[2, 3, 1]`、target 0：以 3 分割後得到 `[2, 1, 3]`、`j = 1`，迴圈把 `right` 設成 0，好像索引 1 放的是已定位的 pivot，於是回傳 2，但最小值 1 正是在索引 1。
- Lomuto 掃描時把 `<` 換成 `<=` 一樣能動，但面對重複值同樣是 O(n²)；只有三向的那一段能解決。

<!-- 37f3ea68e094 -->
### Median of Medians — 線性最壞情況從哪來 ⭐⭐

面試官會問保證 O(n) *為什麼*存在，很少要你寫程式。把值分成每組五個，取每組的中位數，再以這些中位數的中位數當 pivot。這樣兩側各至少有 3/10 的值，所以遞迴式是 `T(n) ≤ T(n/5) + T(7n/10) + O(n)`，又因為 `1/5 + 7/10 < 1`，解出來是 O(n)。

<!--CODE-->

它的常數很大，所以實際上大家寫的都是隨機 pivot。

<!-- 5fa13fe640ce -->
## 2) LC 範例

<!-- e773f01acb73 -->
### Find the Kth Largest Integer in the Array — LC 1985

> **關鍵** — 值是沒有前導零的數字**字串**，所以較長的字串一定是較大的數，而等長的兩個字串照字面比較就是對的。鍵 `(len(s), s)` 不必轉成 `int`，就把字串順序變成數值順序。

**歸約到**：[模板 2](#template-2-quickselect-on-a-derived-key--lc-973-) — 鍵是 `(len(s), s)`，答案是單一索引，跟模板 1 一樣。

<!--CODE-->

**陷阱**
- 只把字串當文字比較，`"10"` 會排在 `"6"` 前面。
- `int(s)` 在 Python 行得通，在 Java 不行：字串可以長達 100 位數。

<!-- 9d4081808c57 -->
### 同一副骨架的其他題目

| 題目 | 變化點 | 主場 |
|---|---|---|
| LC 215 Kth Largest Element in an Array | 錨定題 | [模板 1](#template-1-three-way-partition-quickselect--lc-215-)；堆積解法在 [heap.md](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) |
| LC 347 Top K Frequent Elements | 以次數對 (value, count) 項目做 quickselect；依次數做桶排序才是真正的 O(n) | [heap.md](./heap.md) |
| LC 324 Wiggle Sort II | 選出中位數，以它做三向分割，再透過虛擬索引 `(1 + 2*i) % (n \| 1)` 寫入，讓相等的中位數彼此分開 | 本骨架 |
| LC 1738 Kth Largest XOR Coordinate Value | 先建二維前綴 XOR 網格，再對它的 `m*n` 個值做選擇 | 本骨架 |
| LC 75 Sort Colors | 三向分割本身，pivot 固定為 1 | [2_pointers.md](./2_pointers.md) |
| LC 378 / LC 719 矩陣中第 k 小／第 k 小數對距離 | 值是隱含的，沒有存起來：改成對值做二分搜尋 | [binary_search_on_answer.md](./binary_search_on_answer.md) |

<!-- 09cc5f29ae84 -->
## 3) 練習路徑

| 階段 | 題目（易 → 難） | 學到什麼 |
|---|---|---|
| **核心** ⭐ | LC 215 · LC 973 | 三向迴圈；衍生鍵，以及以未排序的前綴當答案 |
| **練習** | LC 75 · LC 1985 · LC 1337 · LC 347 | 單獨的分割；字串鍵；tuple 鍵；對次數表做選擇 |
| **延伸** | LC 1471 · LC 324 · LC 1738 | 先選中位數，再選一次；中位數加虛擬索引；對衍生網格做選擇 |
