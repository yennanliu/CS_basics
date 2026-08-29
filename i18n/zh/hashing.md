<!-- db7cca7acc9f -->
# 雜湊與計數

> **範圍** — 雜湊的**內部原理**與計數慣用手法 — 雜湊函式設計、碰撞、頻率表、滾動雜湊、自訂鍵。
> **另見**：[hash_map.md](./hash_map.md) — 以 map 為形狀的 LC 題型；[set.md](./set.md) — 成員判斷與去重；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 把滾動雜湊用在子字串搜尋上。

<!-- cddbdc1aa790 -->
## LeetCode 題目清單

- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Counting](https://leetcode.com/problem-list/counting/)
- [Hash Function](https://leetcode.com/problem-list/hash-function/)

<!-- ccaa19e9c9e0 -->
## 總覽
**雜湊與計數**這類技巧用雜湊表與頻率表來解決計數、分組與快速查找的問題。

<!-- 59ea23ba89d3 -->
### 關鍵性質
- **時間複雜度**：雜湊操作平均 O(1)，整體走訪 O(n)
- **空間複雜度**：雜湊表本身佔 O(n)
- **核心想法**：用雜湊表這種資料結構，拿空間換時間
- **什麼時候用**：快速查找、頻率計數、偵測重複、分組
- **關鍵資料結構**：HashMap、HashSet、Counter、defaultdict

<!-- 6f58197bc376 -->
### 核心特性
- **查找很快**：搜尋／插入／刪除平均都是 O(1)
- **追蹤頻率**：數某個元素出現幾次
- **偵測重複**：認出看過的元素
- **分組**：把性質相同的東西收在一起
- **滾動雜湊**：有效率地處理字串比對與子字串問題

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- fd38faffd7cc -->
### **分類 1：頻率表**
- **說明**：統計出現次數並依頻率分組
- **例子**：LC 242 (Valid Anagram)、LC 49 (Group Anagrams)、LC 169 (Majority Element)
- **模式**：用 HashMap 統計頻率，再拿這些次數做分析

<!-- c0bc1818214a -->
### **分類 2：前綴雜湊／滾動雜湊**
- **說明**：用雜湊函式做有效率的字串比對
- **例子**：LC 28 (Find Index)、LC 187 (Repeated DNA)、LC 1044 (Longest Duplicate Substring)
- **模式**：為滑動視窗計算滾動雜湊

<!-- 53ea6929c3ad -->
### **分類 3：用 HashSet 記錄看過的狀態**
- **說明**：追蹤走訪過的元素，藉此偵測樣式或環
- **例子**：LC 202 (Happy Number)、LC 141 (Linked List Cycle)、LC 128 (Longest Consecutive)
- **模式**：用 HashSet 記住看過的狀態

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 08bf7ed803e2 -->
### 模板對照表
| 模板種類 | 使用情境 | 時間複雜度 | 什麼時候用 |
|---------------|----------|-----------------|-------------|
| **頻率計數器** | 統計元素次數 | O(n) | 變位詞、重複值 |
| **滾動雜湊** | 字串比對 | O(n+m) | 子字串搜尋 |
| **看過的狀態** | 偵測環 | O(n) | 偵測重複樣式 |
| **依雜湊鍵分組** | 分類 | O(n) | 把相似的東西歸在一起 |

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- bd72783be782 -->
### **頻率表類題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Valid Anagram | 242 | 字元頻率 | Easy |
| Group Anagrams | 49 | 用排序後的字串當鍵 | Medium |
| Majority Element | 169 | 統計頻率 | Easy |
| Top K Frequent Elements | 347 | 頻率 + 堆積 | Medium |
| Find All Anagrams | 438 | 滑動視窗 + 頻率 | Medium |
| Longest Substring Without Repeating | 3 | 滑動視窗 + 看過的字元 | Medium |

<!-- 3100f213e7f1 -->
### **滾動雜湊類題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Implement strStr() | 28 | Rabin-Karp | Easy |
| Repeated DNA Sequences | 187 | 10 個字元的滾動雜湊 | Medium |
| Longest Duplicate Substring | 1044 | 二分搜尋 + 滾動雜湊 | Hard |
| Find All Duplicates in Array | 442 | 索引當雜湊 | Medium |

<!-- 31e208c58c74 -->
### **用 HashSet 記錄狀態的題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Happy Number | 202 | 偵測數列中的環 | Easy |
| Linked List Cycle | 141 | 快慢指標或 HashSet | Easy |
| Longest Consecutive Sequence | 128 | 用 HashSet 查找 | Medium |
| Contains Duplicate | 217 | 單純的 HashSet | Easy |
| Contains Duplicate II | 219 | 帶視窗的 HashSet | Easy |

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- a46d3a90e276 -->
### 2-1) Valid Anagram (LC 242) — Frequency Count
> 統計兩個字串的字元頻率；兩張表必須完全相同。

<!--CODE-->

<!--CODE-->

<!-- a534d3fb6a80 -->
### 2-2) Group Anagrams (LC 49) — Sort-Key HashMap
> 用排序後的字串當鍵；所有互為變位詞的字串會共用同一個鍵。

<!--CODE-->

<!--CODE-->

<!-- 179ac3d4c3ff -->
### 2-3) Happy Number (LC 202) — HashSet Cycle Detection
> 反覆把各位數平方相加；用一個 set 判斷在抵達 1 之前是否又回到看過的數字。

<!--CODE-->

<!--CODE-->

<!-- de305466072d -->
### 2-4) Longest Consecutive Sequence (LC 128) — HashSet Start Detection
> 只從序列的起點（num−1 不在 set 裡）開始往外擴，避免重複計算。

<!--CODE-->

<!--CODE-->

<!-- 7b7f21d1e582 -->
### 2-5) Repeated DNA Sequences (LC 187) — Sliding Window HashSet
> 滑動一個 10 字元的視窗；放進 seen set；把重複的收進結果 set。

<!--CODE-->

<!--CODE-->

<!-- 66d2deef06cd -->
### 2-6) Top K Frequent Elements (LC 347) — Bucket Sort by Frequency
> 把元素放進以頻率為索引的桶子；從頻率最高的桶往下收滿 k 個。

<!--CODE-->

<!--CODE-->

<!-- dc04b4e72029 -->
## 鍵的設計與雜湊內部原理

> 上面那些模板雜湊的是*本來就*可雜湊的值。這一節講的是雜湊面試裡比較難的那一半：**發明一個鍵**，讓「相等」剛好等於題目要的那種相等，以及**自己把表做出來**。

<!-- 68613978ef59 -->
### 快速決策表

| 目標 | 模板 | 你要造的鍵 | 例子 |
|------|----------|---------------|----------|
| 自己實作一個 map | [Template 5](#template-5-build-a-hash-map-from-scratch-lc-706) ⭐⭐⭐⭐⭐ | `hash(k) % capacity` → bucket | LC 706 |
| 用一個 set 同時管好多個限制 | [Template 6](#template-6-canonical-composite-key-lc-36) ⭐⭐⭐⭐⭐ | 帶標籤的 tuple `("row", r, d)` | LC 36, LC 939 |
| 比的是*形狀*而不是值 | [Template 7](#template-7-structural-hashing--canonical-serialization-lc-572) ⭐⭐⭐⭐ | 標準化的序列化字串 | LC 572, LC 508 |
| 依比值／方向分組 | [Template 8](#template-8-normalized-fraction-key-lc-149) ⭐⭐⭐⭐ | 約分過的 `(dx, dy)` | LC 149 |

**鍵設計的黃金法則**：兩個東西產生**位元組完全相同的鍵，若且唯若它們在這題的意義下是等價的**。這一節裡的每個 bug 不是*誤合*（兩個不同的東西被壓成同一個鍵）就是*誤分*（兩個等價的東西拿到不同的鍵）。

---

<!-- 0a0825c5f644 -->
### Template 5: Build a Hash Map From Scratch (LC 706)

**核心想法**：一個雜湊表就是 `bucket = hash(key) % capacity` 再加上一套**碰撞處理策略**。這兩種經典策略值得背熟，因為幾乎任何雜湊題後面，面試官都會追問一句「那碰撞的時候會發生什麼事？」。

| | **分離鏈結法（separate chaining）** | **開放定址法（線性探測）** |
|---|---|---|
| 碰撞處理 | 每個 bucket 掛一條 linked list | 往後找下一個空位 |
| 刪除 | 把節點從串列拔掉 | 需要放一個**墓碑（tombstone）**標記 |
| 負載因子 | 可以超過 1.0 | 必須 < 1（大約 0.5–0.75 就擴容） |
| 快取表現 | 差（一直追指標） | 好（連續的陣列） |
| 誰在用 | `java.util.HashMap` | Python 的 `dict`、`Set` |

**陷阱**：用開放定址法時，**不能**只是把刪掉的格子清空 — 那會打斷探測鏈，之後的查找會提早停下來。要改成寫入一個 `DELETED` 墓碑，插入時再拿它來重用。

<!--CODE-->

<!--CODE-->

> **值得先演練的追問**：*「為什麼 `HashMap` 最壞情況是 O(N)？」* → 因為所有鍵都碰撞到同一個 bucket。Java 8+ 的緩解做法是當某個 bucket 超過 8 個項目時把它轉成紅黑樹，最壞情況變成 O(log N)。
>
> **拿自訂物件當鍵**：如果你覆寫了 `equals()`，就**必須**同時覆寫 `hashCode()` — 相等的物件被規定要有相等的雜湊值，否則 map 會弄丟項目。Java 用 `Objects.hash(a, b)`；Python 用 `tuple`／`frozenset`，或是在定義 `__eq__` 的同時定義 `__hash__`。

---

<!-- 355087fd65a1 -->
### Template 6: Canonical Composite Key (LC 36)

**核心想法**：當好幾個彼此獨立的限制都必須同時成立時，不要開好幾張表。把每個限制**標籤化**寫進鍵裡，全部丟進**同一個** set。那個標籤就是用來擋掉「第 3 列有個 5」跟「第 3 行有個 5」之間誤合的東西。

<!--CODE-->

<!--CODE-->

> **分隔符陷阱**：用字串當鍵時，`"1" + "2" + "3"` 跟 `"12" + "3"` 會撞在一起。一定要把各欄位隔開（`"@row"`、`"-"`），或乾脆用 tuple。Python 的 tuple／Java 的 record 是安全的預設值；只有在你真的需要一個扁平命名空間時才用字串。

<!-- 3fc290a19c40 -->
#### 變體：把一組座標壓成一個整數鍵 — LC 939

*轉折：想法一樣，但鍵是用算的而不是用拼字串的 — 只有在你知道座標上界時才安全。*

<!--CODE-->

<!--CODE-->

> 乘數**必須大於低位欄位的最大值**（`y <= 40000` → 用 `40001`），而且乘積不能溢位：`40000 * 40001 + 40000 ≈ 1.6e9` 還塞得進 `int`。沒把握的話就用 `long` 或 tuple。

---

<!-- 00e62485673d -->
### Template 7: Structural Hashing — Canonical Serialization (LC 572)

**核心想法**：想用雜湊表比較*形狀*（子樹、格子、島嶼）時，先把每個形狀壓成一個**標準字串** — 要滿足「兩個形狀序列化結果相同**若且唯若**它們結構相同」。這樣形狀比對就退化成單純的字串比對。

兩個一定要有的成分：
1. **明確的 null 標記**（`#`）— 沒有它的話 `[1,2,null]` 跟 `[1,null,2]` 會序列化成一樣（誤合）。
2. **值的分隔符** — 沒有的話節點 `12` 跟節點 `1`+`2` 會糊在一起；這裡用值前面的 `^` 跟後面的 `(` 把它圍起來。

<!--CODE-->

<!--CODE-->

> 把序列化結果餵進**滾動雜湊**，就能把子字串測試壓到 O(M + N) — 見 [`string_matching_kmp_rolling_hash.md`](string_matching_kmp_rolling_hash.md)。同樣這招「把子樹雜湊成一個鍵」，就是重複子樹偵測（Merkle 式的樹雜湊）的原理。

<!-- 9fa320099a1f -->
#### 變體：不是拿標準鍵去配對，而是拿來計數 — LC 508

*轉折：鍵是一個算出來的聚合值（子樹總和）而不是序列化字串，而且我們對它做頻率統計。*

<!--CODE-->

<!--CODE-->

---

<!-- 2c7e829559cf -->
### Template 8: Normalized Fraction Key (LC 149)

**核心想法**：絕對不要拿 `double` 當雜湊鍵。浮點數會讓 `1/3` 跟 `2/6` 落在*幾乎*相同但不完全相同的值上 — 這是不可靠的誤分，垂直線還會多一個除以零的狀況。改成把這組數字除以它們的 **gcd**，再固定一個**標準符號**，就得到一個精確的整數鍵。

方向 `(dx, dy)` 的標準形式：
1. 兩個都除以 `gcd(|dx|, |dy|)`；
2. 強制 `dx > 0`，或 `dx == 0 && dy > 0` — 這樣 `(1, 2)` 跟 `(-1, -2)`（同一條線）才不會誤分。

<!--CODE-->

<!--CODE-->

> 只要鍵是**比值或方向**，同樣這套正規化就適用：除以 gcd、固定符號、保持整數。另外，斜率表要每換一個錨點就重設 — 所有錨點共用一張表是經典的誤合（穿過不同錨點的平行線）。

---

<!-- 4c7fdb53d207 -->
### 另外值得知道的題（沒有新模板）

| 題目 | LC # | 為什麼列在這 |
|---------|------|---------------|
| Insert Delete GetRandom O(1) | 380 | HashMap `value → index` 加陣列；刪除時跟最後一個元素交換 |
| Top K Frequent Words | 692 | 就是 LC 347 再加上比較器裡的字典序 tie-break |
| Ransom Note | 383 | 頻率表相減（LC 242 的不對稱版本） |
| Isomorphic Strings | 205 | 需要**兩張**表 — 只做單向對應會誤合 |
| Task Scheduler | 621 | 先做計數表，答案光靠 `maxFreq` 就能推出來 |
| Subarray Sum Equals K | 560 | 前綴和當鍵 — 見 [`hash_map.md`](hash_map.md) |
| Continuous Subarray Sum | 523 | 前綴和的**餘數**當鍵 — 見 [`hash_map.md`](hash_map.md) |

**相關 cheatsheet**：[`hash_map.md`](hash_map.md)（以 map 為主的模式、前綴和）、[`set.md`](set.md)（去重／成員判斷）、[`string_matching_kmp_rolling_hash.md`](string_matching_kmp_rolling_hash.md)（滾動雜湊的深入介紹）。

<!-- 5ecab1c9ff42 -->
## 進階技巧

<!-- f170e4611aea -->
### 自訂雜湊函式
<!--CODE-->

<!-- 1b63409bd591 -->
### 以雜湊為基礎的資料結構
<!--CODE-->

<!-- fd4e4f0a995f -->
## 效能最佳化提示

<!-- 54591dad768d -->
### 雜湊表最佳實務
<!--CODE-->

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- d2fdc8462839 -->
### 常見雜湊模式

| 模式 | 模板 | 使用情境 | 例子 |
|---------|----------|----------|---------|
| **頻率計數** | `Counter(arr)` | 統計出現次數 | 變位詞、重複值 |
| **看過的狀態** | `visited = set()` | 偵測環 | 快樂數、鏈結串列有環 |
| **依鍵分組** | `groups[key].append(item)` | 分類 | 分組變位詞 |
| **滾動雜湊** | 增量更新雜湊值 | 子字串搜尋 | 樣式比對 |

<!-- 9a047244ed80 -->
### 時間複雜度指南
| 操作 | 平均情況 | 最壞情況 | 備註 |
|-----------|--------------|------------|-------|
| 插入 | O(1) | O(n) | 前提是雜湊函式夠好 |
| 搜尋 | O(1) | O(n) | 取決於碰撞狀況 |
| 刪除 | O(1) | O(n) | 跟搜尋一樣 |
| 走訪 | O(n) | O(n) | 要看過所有元素 |

<!-- bfc4aca5e2f4 -->
### 空間複雜度考量
- **雜湊表**：O(n)，n 是元素個數
- **滾動雜湊**：額外空間 O(1)
- **頻率計數器**：O(k)，k 是相異元素的個數

<!-- 89100bf58d29 -->
### 常見錯誤與提示

**🚫 常見錯誤：**
- 拿可變物件當雜湊鍵
- 沒有妥善處理雜湊碰撞
- 雜湊函式算太多次
- 雜湊表太大造成記憶體外洩

**✅ 最佳實務：**
- 用不可變型別當鍵（字串、tuple、frozenset）
- 選好的雜湊函式，把碰撞降到最低
- 考慮用 `defaultdict` 來自動初始化
- 計數就用 `Counter`
- 字串比對題就實作滾動雜湊

<!-- 173a9eac82d1 -->
### 面試提示
1. **看出可以用雜湊的地方**：找出需要計數、分組或快速查找的地方
2. **選對資料結構**：set vs dict vs Counter vs defaultdict
3. **想清楚時間與空間的取捨**：雜湊表 vs 其他做法
4. **處理邊界情況**：空輸入、只有一個元素
5. **針對題目最佳化**：字串題用滾動雜湊，計數題用頻率表
6. **拿例子測一遍**：確認雜湊碰撞不會把邏輯弄壞

這份完整的雜湊 cheatsheet 收錄了解雜湊類題目最重要的模式與技巧。
