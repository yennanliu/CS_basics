<!-- 28bddb70309a -->
# 排序演算法與技巧

> **範圍** — 排序(sorting)演算法與周邊技巧 — 比較式排序與其穩定性、計數／桶／基數排序、quickselect、自訂比較器，以及循環排序(cyclic sort)。
> **另見**：[heap.md](./heap.md) — 堆積排序與 top-k；[binary_search.md](./binary_search.md) — 排序之後能做什麼；[advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — 用合併排序來「計數」而不是排序；[greedy.md](./greedy.md) — 先排序再掃描。

<p align="center"><img src="../pic/sort_cheatsheet.png"></p>

<!-- fe33043a3d0a -->
## LeetCode 題目清單

- [Sorting](https://leetcode.com/problem-list/sorting/)
- [Merge Sort](https://leetcode.com/problem-list/merge-sort/)
- [Counting Sort](https://leetcode.com/problem-list/counting-sort/)
- [Bucket Sort](https://leetcode.com/problem-list/bucket-sort/)
- [Radix Sort](https://leetcode.com/problem-list/radix-sort/)
- [Quickselect](https://leetcode.com/problem-list/quickselect/)

<!-- f322055d1c92 -->
## 總覽
**排序(sorting)**就是把元素依特定順序（遞增或遞減）排好的過程。它是許多演算法與資料結構的基礎，讓搜尋、資料分析與解題都能更有效率。

<!-- aa10abe6837d -->
### 關鍵性質
- **穩定性**：保持相等元素之間的相對順序
- **原地**：只用 O(1) 額外空間
- **適應性**：對部分已排序的資料表現更好
- **何時使用**：資料排序、為二分搜尋做前處理、找中位數／百分位數

<!-- 319d23cc0ea1 -->
### 演算法選擇指南
- **小型資料集（n < 50）**：插入排序
- **通用場合**：快速排序、合併排序
- **保證 O(n log n)**：堆積排序、合併排序
- **接近已排序**：插入排序、氣泡排序
- **值域有限**：計數排序、基數排序

<!-- 8f36ac39853e -->
### 參考資料
- [Neetcode Sort cheatsheet](https://neetcode.io/courses/lessons/sorting-algorithms)
- [Sorting Visualizations](https://visualgo.net/en/sorting)
- [Princeton Algorithms](https://algs4.cs.princeton.edu/20sorting/)


| **排序演算法** | **時間複雜度（最佳情況）** | **時間複雜度（平均情況）** | **時間複雜度（最壞情況）** | **空間複雜度** |
|-----------------------|-------------------------------|-----------------------------------|---------------------------------|----------------------|
| **氣泡排序 Bubble Sort**        | O(n)                          | O(n²)                             | O(n²)                           | O(1)                 |
| **插入排序 Insertion Sort**     | O(n)                          | O(n²)                             | O(n²)                           | O(1)                 |
| **選擇排序 Selection Sort**     | O(n²)                         | O(n²)                             | O(n²)                           | O(1)                 |
| **合併排序 Merge Sort**         | O(n log n)                    | O(n log n)                        | O(n log n)                      | O(n)                 |
| **快速排序 Quick Sort**         | O(n log n)                    | O(n log n)                        | O(n²)                           | O(log n)             |
| **堆積排序 Heap Sort**          | O(n log n)                    | O(n log n)                        | O(n log n)                      | O(1)                 |
| **計數排序 Counting Sort**      | O(n + k)                      | O(n + k)                          | O(n + k)                        | O(k)                 |
| **基數排序 Radix Sort**         | O(nk)                         | O(nk)                             | O(nk)                           | O(n + k)             |
| **桶排序 Bucket Sort**        | O(n + k)                      | O(n + k)                          | O(n²)                           | O(n)                 |

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- 4d4a82d1067b -->
### **模式 1：自訂比較器排序** — LC 179
- **描述**：用自訂規則或多重條件來排序
- **範例**：LC 179, 791, 937, 1029, 1366
- **模式**：為複雜的排序規則定義比較函式

<!-- b22dfaa1b935 -->
### **模式 2：拓撲排序** — LC 207
- **描述**：依相依關係決定元素順序
- **範例**：LC 207, 210, 269, 310, 1136
- **模式**：DFS/BFS 搭配入度追蹤

<!-- 230cdf3fc57a -->
### **模式 3：區間排序** — LC 56
- **描述**：把區間排序以便合併／處理
- **範例**：LC 56, 57, 252, 253, 435
- **模式**：依起點排序，然後逐一處理

<!-- 064b02f08ca5 -->
### **模式 4：第 K 個元素** — LC 215
- **描述**：有效率地找出第 k 小／第 k 大的元素
- **範例**：LC 215, 347, 378, 658, 973
- **模式**：Quick Select 或堆積(heap)

<!-- 037618c8f6e9 -->
### **模式 5：桶／計數排序** — LC 164
- **描述**：值域有限時的排序
- **範例**：LC 164, 274, 451, 1122, 1636
- **模式**：把值當成索引

<!-- feca11ffe074 -->
### **模式 6：合併排序的應用** — LC 148
- **描述**：搭配排序的分治法
- **範例**：LC 23, 148, 315, 327, 493
- **模式**：合併已排序的序列

<!-- 0a5427f3ca12 -->
### **模式 7：貪婪配對（排序 + 雙指標）** — LC 1877
- **描述**：先排序，再把最小的和最大的配成一對，讓每對的和平衡並最小化其中的最大值
- **核心想法**：把大數字配在一起會造出不必要的大和；把兩端（最小 + 最大）配起來能把重量平均分散
- **範例**：LC 1877, 561, 881, 2491
- **模式**：排序 → 從兩端出發的雙指標 → 追蹤各配對結果的最大／最小值

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- d4f7025d80b0 -->
### 演算法比較表
| 演算法 | 最佳 | 平均 | 最壞 | 空間 | 穩定 | 何時使用 |
|-----------|------|---------|-------|-------|--------|-------------|
| **快速排序 Quick Sort** | O(n log n) | O(n log n) | O(n²) | O(log n) | 否 | 通用場合 |
| **合併排序 Merge Sort** | O(n log n) | O(n log n) | O(n log n) | O(n) | 是 | 需要穩定、保證 O(n log n) |
| **堆積排序 Heap Sort** | O(n log n) | O(n log n) | O(n log n) | O(1) | 否 | 原地、保證 O(n log n) |
| **插入排序 Insertion Sort** | O(n) | O(n²) | O(n²) | O(1) | 是 | 資料量小或接近已排序 |
| **計數排序 Counting Sort** | O(n+k) | O(n+k) | O(n+k) | O(k) | 是 | 值域有限的整數 |
| **基數排序 Radix Sort** | O(nk) | O(nk) | O(nk) | O(n+k) | 是 | 固定位寬的整數 |

<!-- eea388d52652 -->
### 模板 1：快速排序
<!--CODE-->

<!--CODE-->

<!-- bf02dfc9a984 -->
#### **變形 — 只跑 `partition3Way` 一次（荷蘭國旗問題）— LC 75**

**變化點**：當字母集是固定的極小集合（`{0,1,2}`）時，你根本不用遞迴 — **單獨一次** 3-way partition 就能在 O(n) / O(1) 內把整個陣列排好。

<!--CODE-->

<!--CODE-->

> 常見的追問是：「不用計數排序（兩次掃描）做得到嗎？」→ 上面這個一次掃描的 DNF 就是預期答案。

<!-- 798beaeff643 -->
### 模板 2：合併排序
<!--CODE-->

<!--CODE-->

<!-- 66698823e829 -->
#### **變形 — 由後往前原地合併 — LC 88**

**變化點**：`nums1` 尾端本來就有空位，所以沒有空間放標準 `merge` 用的 O(n) 緩衝區。改成從**尾端**開始填（先放最大的），你寫入的每個位置不是空的、就是已經被取用過 → 額外空間 O(1)。

<!--CODE-->

<!--CODE-->

> **為什麼要倒著走？** 正向合併會在讀取 `nums1[0..m-1]` 之前就把它覆蓋掉，逼你多做一次 O(m) 的複製。這種「從最大的那端開始填」的技巧會在所有原地合併中重現（例如 LC 148 Sort List 的合併步驟，就是基於同樣理由改用指標重接）。

<!-- 036bc5f22ae0 -->
### 模板 3：自訂比較器排序 — LC 179
<!--CODE-->

<!--CODE-->

<!-- 9f721c0ad5f8 -->
### 模板 4：Quick Select（第 K 個元素）— LC 215
<!--CODE-->

<!-- d4b682be22f9 -->
### 模板 5：計數排序
<!--CODE-->

<!-- c7483681af91 -->
### 模板 6：拓撲排序 — LC 207
<!--CODE-->

<!-- 420add228bd3 -->
### 模板 7：把合併排序當成**計數器**（逆序對／右側較小元素）— LC 315 ⭐⭐⭐⭐⭐

**關鍵想法**：合併步驟是唯一一個「已知整整一批右半邊元素都**小於**某個左半邊元素」的時刻。在那個時刻順手掛上一個計數器，你就能用 O(n log n) 而不是 O(n²) 數出跨越整個陣列的配對數。

**遞迴式**：`answer(lo..hi) = answer(left) + answer(right) + cross-pairs counted during merge`

**關鍵細節**：你必須排序一個**索引**陣列，而不是值 — 答案要依原始位置回報，而值會被排序打亂。

<!--CODE-->

<!--CODE-->

**圖解追蹤** — `nums = [5,2,6,1]`，最後一次把 `[2,5]`（索引 1,0）和 `[1,6]`（索引 3,2）合併：

<!--CODE-->

**同一套骨架，不同的計數判定：**

| 題目 | LC # | 合併時你在數什麼 |
|---------|------|-----------------------------|
| Count of Smaller Numbers After Self | 315 | 右邊 `<` 左邊元素的個數 |
| Reverse Pairs | 493 | 滿足 `left > 2 * right` 的配對（合併前要多掃一次） |
| Count of Range Sum | 327 | 差值落在 `[lower, upper]` 內的前綴和配對 |

---

<!-- cf545bb6afec -->
### 模板 8：依值域做桶排序（`bucket = value / width`）— LC 220 ⭐⭐⭐⭐

**關鍵想法**：當題目問的是「有沒有兩個值相差**至多** `t`？」時，把桶寬設成 `t + 1`。於是：
- 落在**同一個**桶的兩個值必定相差 ≤ `t` → 直接命中
- 相差超過 `t` 的值只可能落在**相鄰**的桶 → 你永遠只需要檢查 `id-1`、`id`、`id+1`

這就把 O(n log k) 的平衡 BST／滑動視窗排序解法變成 O(n)。

<!--CODE-->

<!--CODE-->

**桶寬設計速查表**（這才是可重複使用的部分）：

| 目標 | 桶寬 | 為什麼 |
|------|--------------|-----|
| 「兩個值相差在 `t` 以內」（LC 220） | `t + 1` | 同桶 ⇒ 差 ≤ t；也只有鄰桶可能符合 |
| 「排序後相鄰值的最大間距」（LC 164） | `(max-min)/(n-1)` | 鴿籠原理 ⇒ 最大間距必定*跨*在桶與桶之間，所以桶內順序無關緊要 |
| 「依頻率取前 K 名」（LC 347） | 以頻率當索引，`1..n` | 頻率範圍被 n 限制住 ⇒ 直接當索引用 |

---

<!-- ae3c6b43a603 -->
### 模板 9：用**衍生鍵**排序來解鎖貪婪／DP — LC 354 ⭐⭐⭐⭐⭐

**關鍵想法**：許多「二維」問題*只要挑對排序順序*，就會塌縮成一個已經解決的一維問題。排序本身就是演算法；而面試的成敗就落在平手時的處理規則上。

**模式**：第一維**遞增**排序，平手時第二維**遞減**排序 — 遞減的平手規則會讓第一維相同的項目彼此**無法**串接，因此對第二維直接做嚴格遞增掃描自動就是正確的。

<!--CODE-->

<!--CODE-->

<!-- 3a6bd6df9b06 -->
#### **變形 — 依衍生鍵排序，然後貪婪地「插入」— LC 406**

**變化點**：先依身高由高到低排序，這樣已經放好的人都 ≥ 當前這個人；於是 `k` 就正好是要插入的索引，因為之後插入的較矮的人永遠不會擾動先前那些人的計數。

<!--CODE-->

<!--CODE-->

<!-- af4cac3b2652 -->
#### **變形 — 依衍生鍵排序，然後對前驅做 DP — LC 1048**

**變化點**：依**長度**排序，這樣一個字的所有可能前驅都保證會先被處理 — 於是 DP 完全不需要遞迴，也不需要任何記憶化的順序邏輯。

<!--CODE-->

<!--CODE-->

**衍生鍵排序順序 — 快速決策表：**

| 情境 | 排序順序 | 解鎖了什麼 |
|-----------|-----------|-------------|
| 二維嚴格巢狀（LC 354） | dim1 遞增，平手時 dim2 **遞減** | 對 dim2 做 LIS |
| 放置物品且「比它大的個數」很重要（LC 406） | 身高**遞減**、k 遞增 | 直接插入在索引 k |
| 前驅是「較小者」的 DP（LC 1048） | 依大小／長度遞增 | 正向 DP，不需記憶化 |
| 以串接結果比較（LC 179） | 自訂 `a+b` vs `b+a` | 直接串接起來 |

> **⚠️ 比較器遞移性陷阱**：自訂比較器必須構成*全序* — `compare(a,b) > 0 && compare(b,c) > 0` 必須推得 `compare(a,c) > 0`。不成立時 Java 的 TimSort 會丟出 `IllegalArgumentException: Comparison method violates its general contract!`。LC 179 的 `a+b` vs `b+a` 規則*是*可證明具遞移性的；而像「依哪個欄位不為零就用哪個排」這種臨時規則通常不是。另外請優先用 `Integer.compare(a, b)` 而不是 `a - b`（大值／負值會溢位）。

---

<!-- 51b3080873b0 -->
### 模板 10：循環排序（值是 `1..n` 的一個排列）— LC 645 ⭐⭐⭐⭐

**關鍵想法**：當值本身就是索引（`1..n` 或 `0..n-1`）時，你根本不需要比較式排序 — 反覆把每個值交換**回家**到 `index = value - 1`。每次交換都會永久定位一個值，所以總工作量是 O(n)、空間 O(1)。之後，任何值不對的索引就精準指出缺失／重複／放錯位置的元素。

<!--CODE-->

<!--CODE-->

**什麼時候該拿出循環排序**：陣列長度是 `n`，**而且**值被限制在 `1..n`（或 `0..n-1`），而追問要求 O(n) 時間 / O(1) 空間（所以不能用 HashSet、不能用計數陣列）。變動的部分是排序後的掃描 — 「第一個不對的索引」就能回答缺失數／重複數／第一個缺失正整數這類問題。

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- e72c92b45a78 -->
### 依模式整理的題目表

<!-- 57c05d9adf61 -->
#### **自訂比較器題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Largest Number | 179 | 字串比較 | Medium |
| Custom Sort String | 791 | 字元順序 | Medium |
| Reorder Data in Log Files | 937 | 多重鍵排序 | Easy |
| Two City Scheduling | 1029 | 成本差 | Medium |
| Rank Teams by Votes | 1366 | 票數統計 | Medium |
| Sort Array by Parity | 905 | 奇偶分離 | Easy |
| Relative Sort Array | 1122 | 自訂順序 | Easy |

<!-- 79dbd0c95559 -->
#### **拓撲排序題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Course Schedule | 207 | 環偵測 | Medium |
| Course Schedule II | 210 | 帶相依關係的排序 | Medium |
| Alien Dictionary | 269 | 字元順序 | Hard |
| Minimum Height Trees | 310 | 樹的重心 | Medium |
| Parallel Courses | 1136 | 依層級處理 | Medium |
| Sequence Reconstruction | 444 | 唯一排序 | Medium |

<!-- 4d5daa3457d7 -->
#### **區間排序題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Merge Intervals | 56 | 排序後合併 | Medium |
| Insert Interval | 57 | 二分搜尋插入 | Medium |
| Meeting Rooms | 252 | 重疊檢查 | Easy |
| Meeting Rooms II | 253 | 掃描線 | Medium |
| Non-overlapping Intervals | 435 | 貪婪移除 | Medium |
| Minimum Number of Arrows | 452 | 區間交集 | Medium |

<!-- 2ae33efaed35 -->
#### **第 K 個元素題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Kth Largest Element | 215 | Quick select | Medium |
| Top K Frequent Elements | 347 | 桶排序 | Medium |
| Kth Smallest in Matrix | 378 | 二分搜尋 | Medium |
| Find K Closest Elements | 658 | 雙指標 | Medium |
| K Closest Points to Origin | 973 | Quick select | Medium |
| Kth Largest in Stream | 703 | 最小堆積 | Easy |

<!-- c4891d89d22e -->
#### **計數／桶排序題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Maximum Gap | 164 | 桶排序 | Hard |
| H-Index | 274 | 計數排序 | Medium |
| Sort Characters By Frequency | 451 | 頻率桶 | Medium |
| Relative Sort Array | 1122 | 計數排序 | Easy |
| Sort Array by Frequency | 1636 | 自訂比較器 | Easy |

<!-- 45f500ec8b5e -->
#### **合併排序應用題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Merge k Sorted Lists | 23 | K 路合併 | Hard |
| Sort List | 148 | 鏈結串列合併排序 | Medium |
| Count of Smaller Numbers | 315 | 合併排序併計數 | Hard |
| Count of Range Sum | 327 | 合併排序 | Hard |
| Reverse Pairs | 493 | 改造過的合併排序 | Hard |

<!-- 6ae34c65b2d8 -->
#### **貪婪配對題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Minimize Maximum Pair Sum | 1877 | 排序 + 雙指標（最小＋最大配對） | Medium |
| Array Partition | 561 | 排序 + 相鄰兩兩配對 | Easy |
| Boats to Save People | 881 | 排序 + 貪婪雙指標 | Medium |
| Divide Players Into Teams | 2491 | 排序 + 最小配最大 | Medium |

<!-- bc13df30d175 -->
#### **把排序當成一行前處理步驟**（不需要新模板）
| 題目 | LC # | 排序／計數技巧 | 難度 |
|---------|------|------------------|------------|
| Group Anagrams | 49 | 用 sorted(word)（或長度 26 的計數 tuple）當雜湊鍵 | Medium |
| Valid Anagram | 242 | 兩個字串都排序，或比較頻率表 | Easy |
| Contains Duplicate | 217 | 排序後檢查相鄰配對（用 HashSet 更好） | Easy |
| Minimum Increment to Make Array Unique | 945 | 排序後把每個值推到 `max(v, prev+1)` | Medium |
| Least Number of Unique Integers after K Removals | 1481 | 統計頻率、頻率遞增排序、先移除最稀有的 | Medium |

<!-- 45235d06cb2a -->
## 模式選擇策略

<!--CODE-->

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- d32e69deed3d -->
### 複雜度速查
| 演算法 | 最佳情況 | 平均 | 最壞情況 | 空間 | 穩定 |
|-----------|-----------|---------|------------|-------|--------|
| Quick Sort | O(n log n) | O(n log n) | O(n²) | O(log n) | 否 |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) | 是 |
| Heap Sort | O(n log n) | O(n log n) | O(n log n) | O(1) | 否 |
| Tim Sort | O(n) | O(n log n) | O(n log n) | O(n) | 是 |
| Counting Sort | O(n+k) | O(n+k) | O(n+k) | O(k) | 是 |
| Radix Sort | O(nk) | O(nk) | O(nk) | O(n+k) | 是 |

<!-- c127ce74a251 -->
### 模板速查
| 模板 | 模式 | 關鍵程式碼 |
|----------|---------|----------|
| **Quick Sort** | 分割 | `pivot; partition; recurse` |
| **Merge Sort** | 分割再合併 | `mid; merge(left, right)` |
| **Custom Sort** | 比較器 | `key=lambda x: criteria` |
| **Quick Select** | 第 K 個元素 | `partition until k` |
| **Counting Sort** | 值當索引 | `count[val]++` |
| **Topological** | 相依關係 | `in_degree; queue` |

<!-- c3d0316b51ed -->
### 常見模式與技巧

<!-- ce2ee61348f3 -->
#### **Python 排序技巧**
<!--CODE-->

<!-- 898bf5b2cade -->
#### **Java 排序技巧**
<!--CODE-->

<!-- 3b79932dbfe3 -->
### 解題步驟

1. **判斷是否需要排序**
   - 真的需要排序嗎？
   - 可以只做部分排序嗎？
   - 需要穩定性嗎？

2. **選擇演算法**
   - 資料量大小
   - 值域範圍
   - 記憶體限制
   - 穩定性需求

3. **定義比較規則**
   - 單一鍵還是多重鍵？
   - 遞增還是遞減？
   - 特殊情況的處理

4. **必要時再最佳化**
   - 第 k 個元素用 quick select
   - 值域有限用計數排序
   - 分布均勻用桶排序

<!-- 5e7f1cf3b298 -->
### 常見錯誤與提示

**🚫 常見錯誤：**
- 在自訂比較過程中修改陣列
- 比較器中的整數溢位（a - b）
- 比較器沒有處理相等的元素
- 需要穩定性時卻用了不穩定的排序
- 對大型資料集使用 O(n²) 的演算法

**✅ 最佳實務：**
- 大多數情況直接用內建排序
- 優先用 Integer.compare() 而不是相減
- 用重複元素與邊界情況測試
- 只需要 k 個元素時考慮部分排序
- 需要保持相等元素順序時用穩定排序

<!-- e1fc739941cc -->
### 面試提示

1. **演算法選擇**
   - 先從內建排序開始
   - 有需要才最佳化
   - 說明時間／空間的取捨

2. **自訂比較器**
   - 處理所有比較情況
   - 避免整數溢位
   - 維持遞移性

3. **常見問題**
   - 「為什麼選 Quick Sort 而不是 Merge Sort？」
   - 「怎麼讓 Quick Sort 變穩定？」
   - 「什麼時候該用 Counting Sort？」

4. **後續最佳化**
   - 只排序 k 個元素
   - 大資料用外部排序
   - 平行排序

<!-- 1a7e8158c86e -->
### 進階技巧

<!-- 11dc9075fafd -->
#### **混合式排序**
- Tim Sort：合併 + 插入
- Intro Sort：快速 + 堆積 + 插入
- Python 與 Java 標準函式庫都用它們

<!-- e2017e3856d2 -->
#### **外部排序**
- 針對磁碟資料的 K 路合併
- 用於資料庫與大數據

<!-- 1ddd305d5b64 -->
#### **平行排序**
- 把資料分給多個處理器
- 平行合併或 sample sort

<!-- ed4834749393 -->
### 相關主題
- **堆積(heap)**：優先佇列、第 k 個元素
- **二分搜尋**：在已排序陣列上
- **分治法**：合併排序模式
- **貪婪**：區間排程
- **圖**：拓撲排序

<!-- f5dc009dd747 -->
## LC 範例

<!-- cd8aeb6b7bde -->
### 2-9) Minimize Maximum Pair Sum in Array — LC 1877

<!--CODE-->

**使用同一套貪婪配對模式的相似題目：**
| 題目 | LC # | 變化點 |
|---------|------|-------|
| Array Partition | 561 | 最大化各對最小值的總和 → 排序後相鄰兩兩配對 |
| Boats to Save People | 881 | 最少船數 → 帶重量上限的貪婪雙指標 |
| Divide Players Into Teams | 2491 | 每隊技能總和相等 → 第一個配最後一個 |
