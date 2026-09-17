<!-- fc48a64bad2f -->
# Java Collection FAQ

> **範圍** — 集合框架：整個階層、`ArrayList` 與 `LinkedList` 的差別、`HashMap` 與 `ConcurrentHashMap` 的內部結構，以及每個操作的 Big-O。
> **另見**：[`java_generics.md`](./java_generics.md) — 這些 API 用到的型別參數；[`java_functional.md`](./java_functional.md) — 用 stream 處理它們；[`java_multi_thread.md`](./java_multi_thread.md) — 並發版本。

<!-- 6bce93361fb8 -->
## 1) Collection 階層

<!--CODE-->

---

<!-- 72e8316161af -->
## 2) ArrayList 與 LinkedList

| 操作 | ArrayList | LinkedList |
|-----------|-----------|------------|
| 依索引取值 | `O(1)` | `O(n)` |
| 在尾端新增／移除 | `O(1)`（攤銷） | `O(1)` |
| 在頭部／中間新增或移除 | `O(n)`（要搬動） | 已知節點是 `O(1)`，否則要 `O(n)` 找 |
| 記憶體 | 緊密（陣列） | 每個節點多了指標 |
| 快取區域性 | 好 | 差 |

**經驗法則**：預設就用 `ArrayList`。`LinkedList` 很少是正確答案；要 stack／queue
就用 `ArrayDeque`。

- `ArrayList` 滿了會成長約 1.5 倍 —— 已知大小就用 `new ArrayList<>(n)` 先開好，
  避免反覆複製。

---

<!-- 6f6e6c2f683e -->
## 3) HashMap 的內部結構

底層是一個 **bucket 陣列**（`Node[] table`）。每個 bucket 放的是 key 雜湊到該索引的
那些項目。

<!--CODE-->

- **雜湊擴散**：`hash = h ^ (h >>> 16)` 把高位混進低位，讓雜湊值有更多位元能影響
  bucket 索引。
- **碰撞**用鏈結處理：
  - Java 7：鏈結串列（新節點插在最前面）。
  - Java 8 以後：**先是鏈結串列，當一個 bucket 有 ≥ 8 個項目**而且** table 大小
    ≥ 64 時轉成紅黑樹**。該 bucket 的查找就從 `O(n)` 變成 `O(log n)`。
    縮回 ≤ 6 個時再退化成串列。
- **負載因子**（預設 `0.75`）：當 `size > capacity * loadFactor` 時，table 會
  **擴容**（容量加倍）並把項目**重新雜湊**。0.75 是空間與碰撞率之間的平衡點。
- **預設容量**：16。
- **`null` key**：允許（放在 bucket 0）；`null` value 也允許。
- **不是執行緒安全的** —— 並發寫入可能把結構弄壞。

**equals / hashCode 的契約**（非常重要）：
- 若 `a.equals(b)`，則 `a.hashCode() == b.hashCode()`。
- 不相等的物件*可以*有相同雜湊（碰撞），但最好不要。
- 兩個都要一起覆寫，否則以雜湊為基礎的集合就壞了。key 請用不可變的欄位。

---

<!-- f51f3c9ceed1 -->
## 4) HashSet、LinkedHashMap、TreeMap

- **HashSet**：只是 `HashMap` 的一層薄包裝 —— 元素當 key，value 放一個固定的虛值。
  add/contains/remove 平均 `O(1)`；沒有順序。
- **LinkedHashMap**：`HashMap` 再加一條雙向鏈結串列，把項目依插入（或存取）順序串起來。
  存取順序模式讓它直接就是一個現成的 **LRU 快取**（覆寫 `removeEldestEntry`）。
- **TreeMap**：紅黑樹，key 保持**排序**（自然順序或 `Comparator`）。
  操作 `O(log n)`；支援範圍查詢（`floorKey`、`ceilingKey`、`subMap`）。

---

<!-- 6f419b3dd2fe -->
## 5) ConcurrentHashMap

高併發下的執行緒安全 map，比 `Hashtable` / `Collections.synchronizedMap`
（它們鎖整個 map）好得多。

- **Java 7**：分段鎖（lock striping）—— map 切成多個 segment，各自獨立上鎖。
- **Java 8 以後**：拿掉 segment。改用 **CAS + 對 bucket 頭節點 synchronized**，
  所以鎖是以 bucket 為單位 → 寫入併發度高。讀取則完全無鎖。
- **不允許** `null` key 或 value（在並發讀取下，它和「不存在」無法區分）。
- 原子輔助方法：`putIfAbsent`、`computeIfAbsent`、`merge`。

---

<!-- 8cab85d6a7b0 -->
## 6) Big-O 速查

| 結構 | 存取 | 搜尋 | 插入 | 刪除 | 備註 |
|-----------|--------|--------|--------|--------|-------|
| ArrayList | `O(1)` | `O(n)` | `O(1)`* | `O(n)` | *尾端攤銷 |
| LinkedList | `O(n)` | `O(n)` | `O(1)` | `O(1)` | 位置已知時 |
| HashMap / HashSet | — | `O(1)` | `O(1)` | `O(1)` | 平均；Java 8 起最差 `O(log n)` |
| TreeMap / TreeSet | — | `O(log n)` | `O(log n)` | `O(log n)` | 有排序 |
| ArrayDeque | 兩端 `O(1)` | `O(n)` | 兩端 `O(1)` | 兩端 `O(1)` | 最佳的 stack/queue |
| PriorityQueue | peek `O(1)` | `O(n)` | `O(log n)` | `O(log n)` | 二元堆積 |

---

<!-- 266936724c5e -->
## 7) 常見的坑

- **Fail-fast 迭代器**：迭代期間修改集合（除了透過 `Iterator.remove()`）*可能*丟出
  `ConcurrentModificationException` —— 這個檢查是盡力而為，所以永遠不要寫依賴它的
  邏輯。要條件式移除就用 `removeIf`。
- **`Arrays.asList()`** 回傳的是以該陣列為底的固定長度 list —— `add`/`remove` 會丟例外。
  `List.of(...)` 則完全不可變、也不接受 `null`；需要可變的副本時用
  `new ArrayList<>(List.of(...))`。
- 宣告時用介面：`List<T> x = new ArrayList<>()`。
- 要 stack 就用 `ArrayDeque`，不要用 `Stack`（舊類別，而且有同步開銷）。
- **可變的 key 會毀掉 map**：插入後才改動 `hashCode()` 用到的欄位，那個項目就永遠
  再也找不到了。
- 集合存的是物件，所以 `List<Integer>` 每個元素都會裝箱 —— 熱路徑上請用 `int[]` 或
  primitive stream。
- `HashMap` 的迭代順序沒有規範，而且*擴容時會變*；在意順序就用 `LinkedHashMap`，
  在意排序就用 `TreeMap`。

---

<!-- bbba828c669b -->
## 8) 一張表選完

| 你要的是 | 就用 |
|----------|-----|
| 依索引存取、迭代 | `ArrayList` |
| stack、queue、deque | `ArrayDeque` |
| 去重、`O(1)` 判斷是否存在 | `HashSet` |
| 去重 + 排序／範圍查詢 | `TreeSet` |
| key → value 查找 | `HashMap` |
| 查找 + 插入順序（或 LRU） | `LinkedHashMap` |
| 查找 + key 排序、`floor`/`ceiling` | `TreeMap` |
| Top-K、優先序排程 | `PriorityQueue` |
| 跨執行緒共用的 map | `ConcurrentHashMap` |
| 一直被讀、很少被寫的 list | `CopyOnWriteArrayList`（每次寫入都複製整個陣列） |
| 生產者與消費者執行緒之間交棒 | 有界的 `BlockingQueue` |

---

<!-- d67136fb9eb6 -->
## 參考資料

- [JavaGuide — collection questions](https://javaguide.cn/java/collection/java-collection-questions-01.html) · [part 2](https://javaguide.cn/java/collection/java-collection-questions-02.html)
- [JavaGuide — HashMap 原始碼導讀](https://javaguide.cn/java/collection/hashmap-source-code.html)
- [`java_generics.md`](./java_generics.md) · [`java_functional.md`](./java_functional.md) · [`java_multi_thread.md`](./java_multi_thread.md)
