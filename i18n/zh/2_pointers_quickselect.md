<!-- 27dc8334993f -->
# QuickSelect（以分割求第 K 個元素）

> **範圍** — 以分割（partition）為基礎的選擇演算法：只往 QuickSort 分割後的其中一側遞迴，在平均 O(n) 時間內找出第 K 大、第 K 小或最接近的 K 個元素，包含 pivot 的挑選策略，以及最壞情況 O(n) 的 Median of Medians 大綱。
> **另見**：[2_pointers.md](./2_pointers.md) — 這份文件原本就是從雙指標那頁拆出來的，因為分割掃描長得像雙指標，本質卻是選擇演算法；[sort.md](./sort.md) — QuickSort 本身，以及什麼時候整個排序反而比選擇划算；[heap.md](./heap.md) — 大小為 K 的堆積替代方案，O(n log k) 但適合串流；[advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — 把 quickselect 當成分治法遞迴式來看。

<!-- 37cf84424f85 -->
## LeetCode 題目清單

- [Quickselect](https://leetcode.com/problem-list/quickselect/)
- [Divide and Conquer](https://leetcode.com/problem-list/divide-and-conquer/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

<!-- e278009af819 -->
## 總覽

**模式總覽：**
QuickSelect 是用來在無序資料中找出第 K 小／第 K 大元素的選擇演算法。它跟 QuickSort 同源，差別在於只往分割後的其中一側遞迴，因此是 **平均 O(n) 時間**，而不是 O(n log n)。

**核心概念：**
<!--CODE-->

**關鍵洞見：**
- 以 pivot 分割完之後，pivot 就已經站在它排序後的最終位置
- pivot 索引 = k，答案就是它
- pivot 索引 < k，往右半邊找
- pivot 索引 > k，往左半邊找

**演算法步驟：**
1. 選一個 pivot（通常取最後一個元素，或隨機挑以求較好的表現）
2. 分割陣列：比 pivot 小的放左邊，比 pivot 大的放右邊
3. pivot 位置 == k，回傳 pivot 的值
4. pivot 位置 < k，往右半邊遞迴
5. pivot 位置 > k，往左半邊遞迴

<!-- cf2c7c5e734d -->
### 關鍵性質
- **複雜度**：平均 O(n)；pivot 選得差時最壞 O(n^2)（用 Median of Medians 可保證最壞 O(n)）；迭代寫法額外空間 O(1)，遞迴寫法堆疊 O(log n)
- **核心想法**：分割後 pivot 就落在它最終的排序索引上，把那個索引跟 `k` 一比，就知道答案只可能在哪一側
- **什麼時候用**：一次性的「第 k 大／第 k 小／最接近的 k 個」，而且不需要完整順序
- **什麼時候別用**：串流輸入、輸入唯讀，或需要把 k 個元素照順序輸出 — 這些改用大小為 K 的堆積

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 43eaf567777c -->
### 模板 1：第 K 大元素 — LC 215

<!--CODE-->

<!--CODE-->

---

<!-- 2e7750610fd5 -->
### 視覺化範例：在 [3, 2, 1, 5, 6, 4] 中找第 2 大

<!--CODE-->

---

<!-- f94e1e9321b1 -->
### 模板 2：距離原點最近的 K 個點 — LC 973

<!--CODE-->

<!--CODE-->

<!-- 6b20d2508667 -->
#### **Java 版**

<!--CODE-->

---

<!-- ec8419f28194 -->
### 優化：隨機 pivot

<!--CODE-->

---

<!-- 778011efd242 -->
### 分割演算法的各種版本

**1. Hoare 分割（從兩端往中間的雙指標）：**

> ⚠️ 它回傳的是**分界點**，不是 pivot 的最終索引 — `nums[j]` 不一定是
> pivot。所以 Lomuto 那套 `p == target` 的判斷在這裡是錯的；要改成縮小區間
> （`target <= j` → `hi = j`，否則 `lo = j + 1`），一路縮到 `lo == hi`。見版本 3。

<!--CODE-->

**2. Lomuto 分割（單趟掃描）：**
<!--CODE-->

**3. Java 版 Hoare — 以及它設下的陷阱：**
> 陷阱在於：`hoare()` 只保證 `a[lo..j] <= pivot <= a[j+1..hi]`，所以你**不能**測 `p == target`；要用 `target <= j` 縮小區間，縮到 `lo == hi` 為止。

<!--CODE-->

**4. 三向分割（荷蘭國旗）— 大量重複值的解法：**
> 陷阱在於：面對 `[2,2,2,…,2]`，隨機 pivot 救不了 Lomuto — 每次分割還是只剝掉一個元素，最後是 O(n²)。改成分成 `< / == / >` 三塊，一趟就把所有相等的鍵折疊掉。

<!--CODE-->

<!-- 85b44f9297e7 -->
### 其他 quickselect 變形（同一副骨架，換個比較鍵）

| 題目 | LC # | 變化點 |
|---------|------|-----------|
| Top K Frequent Elements | 347 | 對次數表的*項目*做 quickselect，比較鍵是出現次數。依頻率做桶排序才是真正的 O(N) 解 — 兩種都要講得出來。 |
| Find the Kth Largest Integer in the Array | 1985 | 值是數字**字串**；只有比較器要改：短的字串較小，長度相同再比字典序。 |
| Wiggle Sort II | 324 | 先 quickselect 找**中位數**，再做三向分割，最後寫進*虛擬索引* `(1 + 2*i) % (n | 1)`，讓相等的中位數被拆得遠遠的。 |
| Kth Largest XOR Coordinate Value | 1738 | 先建二維前綴 XOR 網格（`O(mn)`），再對這 `m*n` 個值 quickselect 找第 k 大。 |


---

<!-- 862948710f26 -->
### 進階：Median of Medians（最壞 O(n)）— 僅列大綱

> ⚠️ **這是草稿，不是能跑的解法。** 只把挑 pivot 的那一半寫清楚；
> `partition`、驅動迴圈和 `median_of_medians_list` 的遞迴都留成 `pass`。
> 不要以為複製貼上就會動 — 它永遠回傳 `None`。放在這裡是要說明 O(n) 保證*從哪來*，
> 而面試官會問的也只有這一部分。

<!--CODE-->

**注意：** Median of Medians 太複雜，面試中幾乎沒人真的寫出來。實務上偏好隨機化的 QuickSelect。

---

<!-- ea4fa3974f42 -->
## 總結與快速查表

<!-- ed0a6dbb39aa -->
### 經典 LeetCode 題目

| 題目 | LC# | 難度 | 變形 | 關鍵洞見 |
|---------|-----|------------|---------|-------------|
| Kth Largest Element in Array | 215 | Medium | 基本 QuickSelect | 轉成找第 (n-k) 小 |
| K Closest Points to Origin | 973 | Medium | 自訂比較器 | 依距離分割 |
| Top K Frequent Elements | 347 | Medium | 搭配次數表 | 對頻率做 QuickSelect |
| Top K Frequent Words | 692 | Medium | 次數表 + trie | QuickSelect + 字典序 |
| Kth Largest Element in Stream | 703 | Easy | 改用 min heap | QuickSelect 用來做初始化 |
| Find Kth Smallest Pair Distance | 719 | Hard | 對答案二分搜尋 | 不是直接的 QuickSelect |
| Wiggle Sort II | 324 | Medium | 三向分割 | 荷蘭國旗的變形 |
| Sort Colors | 75 | Medium | 三向分割 | 荷蘭國旗 |
| Kth Smallest Element in BST | 230 | Medium | 中序走訪 | 不是 QuickSelect（樹結構） |
| Find Median from Data Stream | 295 | Hard | 兩個堆積 | QuickSelect 的替代方案 |

---

<!-- 6914fa4cf294 -->
### 效能比較

| 演算法 | 平均時間 | 最壞時間 | 空間 | 適用情境 |
|-----------|--------------|------------|-------|----------|
| **QuickSelect** | **O(n)** | O(n²) | O(1) | 在未排序資料中找第 K 個元素 |
| QuickSelect（隨機化） | O(n) | O(n²)，機率極低 | O(1) | 平均表現更穩 |
| Heap（Min/Max） | O(n log k) | O(n log k) | O(k) | 線上／串流資料 |
| Full Sort | O(n log n) | O(n log n) | O(1) 或 O(n) | 反正本來就需要排序好的陣列 |
| Counting Sort | O(n + k) | O(n + k) | O(k) | 整數範圍很小 |

**什麼時候用 QuickSelect：**
- ✅ 只要第 K 個元素，不需要完整排序
- ✅ 可以就地改動輸入陣列（原地）
- ✅ 離線演算法（資料一次到齊）
- ✅ 資料量大到 O(n) 跟 O(n log n) 有感差別

**什麼時候別用 QuickSelect：**
- ❌ 需要 K 個元素照順序輸出 → 用堆積或完整排序
- ❌ 線上／串流資料 → 用堆積
- ❌ 不能改動輸入陣列 → 用堆積
- ❌ 需要最壞情況保證 → 用 Median of Medians（最壞 O(n)）

---

<!-- decfeeae2758 -->
### 面試提點

**1. 常見錯誤：**
- 忘了把「第 K 大」換算成「第 (n - k) 小」
- k 從 0 算還是從 1 算，差一錯誤
- 沒處理 left == right 的終止條件
- 分割沒有真的移動 pivot，導致無限遞迴

**2. 優化技巧：**
- **隨機 pivot**：壓低撞上最壞情況的機率
- **三數取中**：取頭、中、尾三個元素的中位數當 pivot
- **迭代版本**：陣列很大時避免堆疊爆掉
- **尾遞迴**：只對較小的那一半遞迴

**3. 複雜度分析：**
<!--CODE-->

**4. 面試時可以講的重點：**
- 「QuickSelect 就是 QuickSort，只是只往一側遞迴」
- 「只要找單一個第 K 個元素，平均 O(n) 比堆積的 O(n log k) 好」
- 「取捨在於：這個做法會改動陣列，堆積則保留原陣列」
- 「隨機 pivot 讓 O(n) 以高機率成立」

**5. 追問：**
- Q：「如果需要 K 個元素排好序呢？」
  - A：用堆積（O(n log k)）或部分 QuickSort
- Q：「如果陣列是唯讀的呢？」
  - A：複製一份，或改用堆積
- Q：「能保證最壞 O(n) 嗎？」
  - A：可以，用 Median of Medians（複雜，很少被問）

---
