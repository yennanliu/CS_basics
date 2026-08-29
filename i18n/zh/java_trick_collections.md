<!-- b2906d79803d -->
# Java 容器與陣列速查表

> **範圍** — 存放資料用的 Java 函式庫 API：陣列與二維陣列、list、map、佇列、堆積、堆疊與 pair — 每一種怎麼初始化、複製、走訪、索引，以及過程中的陷阱。
> **另見**：[java_trick.md](./java_trick.md) — 這些 API 背後的語言語意，包含為什麼複製一個物件陣列之後物件本身還是共用的；[java_trick_strings_sorting.md](./java_trick_strings_sorting.md) — String 相關操作與各種 comparator；[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 每個操作的代價；[heap.md](./heap.md)、[hash_map.md](./hash_map.md)、[queue.md](./queue.md)、[stack.md](./stack.md) — 結構本身，而不是它們的 Java API。

<!-- 0a44bac2543d -->
## LeetCode 題目清單

- [Array](https://leetcode.com/problem-list/array/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

<!-- d3df764217a6 -->
## 總覽

從 [java_trick.md](./java_trick.md) 拆出來 — 那份文件已經長到 3,418 行，這些 API 散落在五套不同的編號體系裡。這裡收的全是*函式庫表面*：解釋那些反直覺行為的語言規則留在母文件。

<!-- af8c70413e1b -->
### Key Properties
- **複雜度**：見 [complexity_cheatsheet.md](./complexity_cheatsheet.md)；底下只在「最直覺的那個呼叫剛好最慢」時才特別標註
- **核心想法**：陣列與容器的分界貫穿全篇 — 固定大小且共變 vs 可成長且泛型 — 大部分轉換上的痛苦都來自跨過這條界線
- **什麼時候用**：當你已經知道*要存什麼*，只需要那個確切的呼叫寫法

<!-- 396040910246 -->
## 陣列

<!-- d1775936f72b -->
### 陣列 vs 容器 — 關鍵差異

**關鍵區別**：

| 方法 | 可變性 | 影響原陣列 | 最佳使用時機 |
|--------|------------|----------------------|---------------|
| `Arrays.asList()` | **固定大小**（不能 add/remove） | ✅ **會** | 唯讀操作時的快速轉換 |
| `new ArrayList()` | **完全可變** | ❌ **不會** | 需要修改容器內容時 |

<!--CODE-->

**建議**：需要完全可變時，用 `new ArrayList<>(Arrays.asList(arr))`。

<!-- 9512218b1440 -->
### 陣列初始化的幾種寫法


<!--CODE-->

<!-- 5864e37e4e48 -->
### 陣列／容器的複製 ⭐


> **核心規則**：`arr2 = arr` 複製的是**參考**，不是資料。兩個變數指向同一個陣列 — 改一個等於改另一個。

<!-- 86f539ecc1be -->
#### 一維陣列

<!--CODE-->

<!-- 105985106642 -->
#### 二維陣列（淺複製 vs 深複製）

<!--CODE-->

<!-- 85171c6ff69e -->
#### 快速參考

| 型別 | 參考（錯的） | 淺複製（正確） | 深複製 |
|------|-------------------|------------------------|-----------|
| `int[]` | `arr2 = arr` | `arr.clone()` / `Arrays.copyOf(arr, n)` | 不適用（基本型別） |
| `int[][]` | `m2 = matrix` | `matrix.clone()` ⚠（內層仍共用） | 迴圈 + `row.clone()` |
| `List<T>` | `list2 = list` | `new ArrayList<>(list)` | 逐個元素深複製 |
| `String[]` | `s2 = s` | `s.clone()` | 不適用（String 不可變） |

**最常踩到的場合**：先複製再排序，然後跟原陣列比對（例如 LC 769、LC 75、LC 242），或是 BFS/DFS 中需要當前狀態的快照時。

<!-- 31ef2a06d420 -->
### 陣列 ↔ List 互轉


<!--CODE-->

**效能備註**：`toArray(new T[size])` 通常比 `toArray()` 快，因為它省掉內部重新配置。

<!-- c8c2329a4b67 -->
### 把值寫進 `int[]`

<!--CODE-->

<!-- aa598d9c0331 -->
### 一維陣列上的 `Arrays.fill`


<!--CODE-->

<!-- 5e850e515fd6 -->
### `Arrays.copyOfRange` — 取子陣列


<!--CODE-->

<!-- 0633f87b4df6 -->
### `Arrays.toString` — 印出陣列


<!--CODE-->

<!-- c41204548141 -->
### 陣列中的最大值

<!--CODE-->

<!-- 45aac74d9cd7 -->
## 二維陣列與矩陣

<!-- d0786fe2c5b2 -->
### 初始化二維陣列


<!--CODE-->

<!-- 6eff30afe99e -->
### 初始化 `M x N` 的 boolean 矩陣

<!--CODE-->

<!-- 8e7c21898bf6 -->
### 存取 `M x N` 的 boolean 矩陣


<!--CODE-->

<!-- ec75b86a3437 -->
## List

<!-- 115a89c76107 -->
### 初始化 list

<!--CODE-->

<!-- 90aa7f7bcb29 -->
### 替換 list 中某個索引的值


<!--CODE-->

<!-- 5c4c29160774 -->
#### 1-0-0-2) 反向走訪 list

<!--CODE-->

<!-- c5381b8fcb49 -->
### 在指定索引插入

<!--CODE-->

<!--CODE-->

<!-- 9fafacb8104d -->
### 往二維 list 追加元素

<!--CODE-->

<!-- 37f09d66a471 -->
### 反轉 list

<!--CODE-->

<!-- 133013c1c034 -->
## Map

<!-- dfaf8ee4a2cd -->
### HashMap 進階操作

<!-- 0be38a19ab70 -->
#### 巢狀 HashMap 模式
<!--CODE-->

<!-- 9d5c5d2adbcb -->
#### 必備的 HashMap 方法
<!--CODE-->

<!-- 20985095505b -->
#### 用 `putIfAbsent` 優雅地更新 map 的值
<!--CODE-->

<!-- 4bfe1025c749 -->
### 回傳預設值 — `getOrDefault`

<!--CODE-->

<!-- db6988f9af44 -->
### 走訪 map

<!--CODE-->

<!-- 44092d75c616 -->
### 同時取出 key 和 value


<!--CODE-->

<!-- 81ad79fbd01d -->
### 依插入順序追蹤元素計數


<!--CODE-->

<!-- aac6cc5183b6 -->
### `TreeMap` 基礎

- java.util.TreeMap.floorKey()
- 會回傳 key 集合中的最大值；若為空則回傳 null
- 還有一種 Map 會在內部對 key 進行排序，也就是 SortedMap。
- SortedMap 保證走訪時依 key 的順序進行。預設按字母排序。
- 使用 TreeMap 時，輸入的 key 必須實作 Comparable 介面。
- https://www.yxjc123.com/post/v0i7dl
- https://liaoxuefeng.com/books/java/collection/tree-map/index.html

<!-- c5f5b05ceabf -->
### 用 `TreeMap` 把 HashMap 依 key 排序

<!--CODE-->

<!--CODE-->

<!--CODE-->

- `TreeMap` 的 `floorEntry` 方法
- https://blog.csdn.net/a1510841693/article/details/124323418
- floorEntry()：回傳「小於或等於指定 key 的最大 key」所對應的 key-value entry；若沒有這種 key 就回傳 null。

<!--CODE-->

<!-- f7907834f278 -->
### `TreeMap` 的 key 順序 — 遞增 vs 遞減 ⭐


> **核心規則**：`TreeMap` 永遠讓 key 保持**排序**。預設是遞增（小 → 大）。傳入 `Comparator.reverseOrder()` 就翻成遞減（大 → 小）。

<!--CODE-->

**另一種做法 — 在預設 TreeMap 上用 `descendingKeySet()`：**
<!--CODE-->

**應用模式 — LC 362 Design Hit Counter（5 分鐘滑動視窗）：**
<!--CODE-->

**為什麼這裡遞減順序有幫助**：由大到小走訪，一碰到落在 5 分鐘視窗外的 key 就能直接 `break`，不必掃完整個 map。

**小結：**

| 目標 | 做法 |
|------|-----|
| 遞增走訪（預設） | `new TreeMap<>()` |
| 遞減走訪（用建構子） | `new TreeMap<>(Comparator.reverseOrder())` |
| 遞減走訪（在既有 map 上） | `map.descendingKeySet()` |
| 最接近且 ≤ target 的 key | `map.floorKey(target)` |
| 最接近且 ≥ target 的 key | `map.ceilingKey(target)` |

**用到 TreeMap 排序的類似 LC 題目：**
| 題目 | LC # | 關鍵用法 |
|---------|------|-----------|
| Design Hit Counter | 362 | 反向走訪 + 提早 break |
| Snapshot Array | 1146 | 用 `floorEntry(snapId)` 找快照前的最後一個值 |
| Time Based Key-Value Store | 981 | `floorKey(timestamp)` |
| My Calendar I | 729 | 用 `floorEntry` / `ceilingEntry` 檢查重疊 |

---

<!-- 8253742835b6 -->
### 陣列不能當 HashMap 的 key ⭐


> **核心規則**：絕對不要拿 `int[]` 或 `Integer[]` 當 `HashMap` 的 key — 它們的 `.equals()` 和 `.hashCode()` 是用記憶體位址，不是元素的值。

<!--CODE-->

<!-- a4643b6b8bab -->
#### 正確的替代方案

**選項 1：用 String 當 key（最簡單）**
<!--CODE-->

**選項 2：巢狀 Map**
<!--CODE-->

**選項 3：自訂 Point 類別，覆寫 `equals()` + `hashCode()`**
<!--CODE-->

<!-- dd6c205170f3 -->
#### 總結表

| key 型別 | 可行嗎？ | 原因 |
|----------|--------|-----|
| `int[]` / `Integer[]` | ❌ | `equals()`/`hashCode()` 用的是記憶體位址 |
| `String`（例如 `"x,y"`） | ✅ | 本來就是依值比較 |
| `Map<Integer, Map<Integer, Integer>>` | ✅ | 巢狀 map 直接繞開這個問題 |
| 覆寫 `equals()` + `hashCode()` 的自訂類別 | ✅ | 明確定義依值判斷的身分 |
| `List<Integer>` | ✅ | `ArrayList.equals()` 是依內容比較 |

**備註**：`List<Integer>`（例如 `Arrays.asList(x, y)`）也能當 map 的 key，因為 `ArrayList` 覆寫了 `equals()` 和 `hashCode()` 來比較元素 — 但它比 `String` key 慢。

<!-- e5cfe2b02656 -->
## 佇列、堆積與堆疊

<!-- 91dc54e4c54a -->
### PriorityQueue（堆積）基礎


**關鍵概念**：Java 的 `PriorityQueue` **預設是最小堆積**。

<!-- 2fbb0129c053 -->
#### 最小堆積的寫法
<!--CODE-->

<!-- 157a0efa18dd -->
#### 最大堆積的寫法

<!--CODE-->

**常見用途**：Top-K 問題、求中位數、任務排程

<!-- 2cb037b62f01 -->
### PriorityQueue 範例


<!--CODE-->

<!-- ee738b0ef76f -->
### 自訂排序的 PriorityQueue

<!--CODE-->

<!-- bd8352e8e9d3 -->
### 初始化佇列


- https://stackoverflow.com/questions/4626812/how-do-i-instantiate-a-queue-object-in-java

- Queue 是一個 `interface`，也就是說你`不能`直接建構一個 Queue。
- 請改用下列其中一種實作：
<!--CODE-->

<!-- 065c4c97eb93 -->
### `add()` vs `offer()`


| 方法 | 失敗時的行為 | 回傳型別 | 最佳使用時機 |
|--------|------------------|-------------|---------------|
| `add(e)` | **丟出例外** | `boolean` | 失敗就該中止執行時 |
| `offer(e)` | **回傳 false** | `boolean` | 想優雅地處理失敗時 |

<!--CODE-->

**建議**：有容量上限的佇列用 `offer()`，像 `LinkedList` 這種無上限的用 `add()`。

<!-- 57a7afe0d1ed -->
### 佇列的移除方法


<!--CODE-->

<!-- 4f81846a0cf5 -->
### 把陣列推進堆疊

<!--CODE-->

<!-- 9d8e875acaf4 -->
### 走訪堆疊

<!--CODE-->

<!-- 0a3eab1d84f4 -->
## Pair

<!-- b35e0b37bf3d -->
### `Pair` 資料結構


- Pair 提供 (key, value) 結構
- 提供 getKey、getValue 方法
- 可以放進其他資料結構裡（例如佇列、雜湊表……）
- 預設的 Java 函式庫、apache.common 或其他函式庫都有

<!--CODE-->

- 或者，你也可以自己定義 pair 結構：

<!--CODE-->
