# Set（集合）

> **範圍** — 成員判斷、去重與集合運算 — 只需要知道元素「在不在」、不需要對應值的題目。
> **另見**：[set_examples.md](./set_examples.md) — 十四題實作範例；[hash_map.md](./hash_map.md) — 每個 key 需要帶一個值時看這裡；[hashing.md](./hashing.md) — 雜湊的內部原理；[Collection.md](./Collection.md) — 怎麼挑容器。

## LeetCode 題目清單

- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Ordered Set](https://leetcode.com/problem-list/ordered-set/)

## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大  |
| -------------- | -------- | -------- | -------- | -------- |
| Hash Set（平均） | O(1)     | O(1)     | O(1)     | O(n)     |

> 表中是平均情況。**最壞情況（所有元素都碰撞）：O(n)。** 取最小／最大值要整個掃一遍（雜湊本身不保證順序）。兩個集合的聯集／交集是 **O(min(len(s1), len(s2)))**。空間是 **O(n)**。

<img src="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/set_operations.png">

## 總覽
**集合（Set）** 是一種只存放不重複元素的容器，提供高效率的成員判斷、插入與刪除。

### 關鍵性質
- **複雜度**：見上面的[時間複雜度](#時間複雜度)表
- **核心特性**：不允許重複、無序（HashSet）、O(1) 查詢
- **什麼時候用**：去除重複、判斷是否存在、集合運算（聯集、交集、差集）

## 0) 概念

### 0-1) 種類

#### HashSet
- **Python**：`set()` — 無序，操作最快
- **Java**：`HashSet<T>` — 底層是 HashMap
- **時間**：add/remove/contains 平均 O(1)
- **適用**：不在乎順序、只要查得快

#### LinkedHashSet
- **Python**：沒有原生支援（可用 OrderedDict 的 key 模擬）
- **Java**：`LinkedHashSet<T>` — 維持插入順序
- **時間**：操作 O(1)，並保留順序
- **適用**：既要集合操作、又要插入順序

#### TreeSet
- **Python**：沒有原生支援（可用 sorted containers）
- **Java**：`TreeSet<T>` — 有序，底層是紅黑樹
- **時間**：add/remove/contains 都是 O(log n)
- **適用**：需要有序元素、需要範圍查詢

### 各實作比較
| 類型 | 順序 | 時間 | 空間 | 適用情境 |
|------|----------|------|-------|----------|
| **HashSet** | 無 | O(1) | O(n) | 查詢要快、不在乎順序 |
| **LinkedHashSet** | 插入順序 | O(1) | O(n) | 要保留插入順序 |
| **TreeSet** | 排序 | O(log n) | O(n) | 有序資料、範圍查詢 |

### 0-2) 模式

#### 模式 1：集合運算
```python
# Union, Intersection, Difference
s1 = {1, 2, 3}
s2 = {2, 3, 4}

union = s1 | s2          # {1, 2, 3, 4}
intersection = s1 & s2   # {2, 3}
difference = s1 - s2     # {1}
symmetric_diff = s1 ^ s2 # {1, 4}
```

#### 模式 2：偵測重複
```python
# Check for duplicates in array
def has_duplicate(nums):
    return len(nums) != len(set(nums))

# Find duplicates
def find_duplicates(nums):
    seen = set()
    duplicates = set()
    for num in nums:
        if num in seen:
            duplicates.add(num)
        seen.add(num)
    return duplicates
```

#### 模式 3：雙集合追蹤
```python
# Track visited and current path (for cycle detection)
def has_cycle(graph, start):
    visited = set()
    current_path = set()

    def dfs(node):
        if node in current_path:
            return True  # Cycle detected
        if node in visited:
            return False

        visited.add(node)
        current_path.add(node)

        for neighbor in graph[node]:
            if dfs(neighbor):
                return True

        current_path.remove(node)
        return False

    return dfs(start)
```

#### 模式 4：用集合追蹤路徑／祖先
```python
# LC 1650 - Find LCA using set to track ancestors
def lowestCommonAncestor(p, q):
    # Track all ancestors of p
    ancestors = set()
    while p:
        ancestors.add(p)
        p = p.parent

    # Find first common ancestor
    while q:
        if q in ancestors:
            return q
        q = q.parent
    return None
```

## 1) 通用形式

### 1-1) 基本操作

#### 1-1-1) 建立集合與基本操作
```python
# Python
# Create empty set
s = set()
s = {}  # Wrong! This creates a dict

# Create with elements
s = {1, 2, 3}
s = set([1, 2, 3])
s = set("abc")  # {'a', 'b', 'c'}

# Add element
s.add(4)

# Remove element
s.remove(3)     # Raises KeyError if not exists
s.discard(3)    # No error if not exists
s.pop()         # Remove and return arbitrary element

# Check membership
if 2 in s:
    print("Found")

# Size
len(s)

# Clear all
s.clear()
```

```java
// Java
// Create HashSet
Set<Integer> set = new HashSet<>();

// Add element
set.add(1);
set.add(2);
set.add(3);

// Remove element
set.remove(2);

// Check membership
if (set.contains(1)) {
    System.out.println("Found");
}

// Size
int size = set.size();

// Clear
set.clear();

// Iterate
for (int num : set) {
    System.out.println(num);
}
```

#### 1-1-2) 集合運算
```python
# Python set operations
s1 = {1, 2, 3, 4}
s2 = {3, 4, 5, 6}

# Union (elements in either set)
union1 = s1 | s2
union2 = s1.union(s2)           # {1, 2, 3, 4, 5, 6}

# Intersection (elements in both sets)
inter1 = s1 & s2
inter2 = s1.intersection(s2)    # {3, 4}

# Difference (elements in s1 but not s2)
diff1 = s1 - s2
diff2 = s1.difference(s2)       # {1, 2}

# Symmetric difference (elements in either but not both)
sym1 = s1 ^ s2
sym2 = s1.symmetric_difference(s2)  # {1, 2, 5, 6}

# Subset check
is_subset = s1.issubset(s2)     # False
is_superset = s1.issuperset(s2) # False

# Disjoint check (no common elements)
is_disjoint = s1.isdisjoint(s2) # False
```

```java
// Java set operations
Set<Integer> s1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> s2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

// Union
Set<Integer> union = new HashSet<>(s1);
union.addAll(s2);  // {1, 2, 3, 4, 5, 6}

// Intersection
Set<Integer> intersection = new HashSet<>(s1);
intersection.retainAll(s2);  // {3, 4}

// Difference
Set<Integer> difference = new HashSet<>(s1);
difference.removeAll(s2);  // {1, 2}

// Subset check
boolean isSubset = s2.containsAll(s1);  // false
```

#### 1-1-3) 容器之間的轉換
```python
# Python conversions
arr = [1, 2, 2, 3, 3, 4]

# Array to set (remove duplicates)
s = set(arr)  # {1, 2, 3, 4}

# Set to array
arr_unique = list(s)

# Set to sorted array
arr_sorted = sorted(s)

# String to set
char_set = set("hello")  # {'h', 'e', 'l', 'o'}

# Set to string
s = {'a', 'b', 'c'}
string = ''.join(sorted(s))  # 'abc'
```

```java
// Java conversions
Integer[] arr = {1, 2, 2, 3, 3, 4};

// Array to set
Set<Integer> set = new HashSet<>(Arrays.asList(arr));

// Set to array
Integer[] arrUnique = set.toArray(new Integer[0]);

// Set to list
List<Integer> list = new ArrayList<>(set);

// List to set
Set<Integer> set2 = new HashSet<>(list);
```

## 題型分類

### 分類 1：偵測重複（10 題）
| 題目 | LC # | 難度 | 模式 | 關鍵洞見 |
|---------|------|------------|---------|-------------|
| Contains Duplicate | 217 | Easy | 比集合大小 | len(nums) != len(set(nums)) |
| Contains Duplicate II | 219 | Easy | 滑動視窗集合 | 維持 k 個元素的視窗 |
| Contains Duplicate III | 220 | Medium | TreeSet/SortedList | 維持一個有序視窗 |
| Find Duplicate | 287 | Medium | 找環 | Floyd 演算法或用集合 |
| Find All Duplicates | 442 | Medium | 索引標記 | 把陣列當成雜湊表用 |
| Single Number | 136 | Easy | XOR／集合 | XOR 會把成對的抵消掉 |
| Single Number II | 137 | Medium | 位元運算 | 位元計數模 3 |
| Single Number III | 260 | Medium | XOR + 分組 | 依相異的那個位元分組 |
| Missing Number | 268 | Easy | 集合／XOR | 期望值 vs 實際值 |
| First Missing Positive | 41 | Hard | 原地當集合 | 直接用陣列索引 |

### 分類 2：集合運算（8 題）
| 題目 | LC # | 難度 | 模式 | 關鍵洞見 |
|---------|------|------------|---------|-------------|
| Intersection of Two Arrays | 349 | Easy | 交集 | set1 & set2 |
| Intersection of Two Arrays II | 350 | Easy | Counter | 記錄出現次數 |
| Union of Two Arrays | - | Easy | 聯集 | set1 | set2 |
| Distribute Candies | 575 | Easy | 比集合大小 | min(len(set), n/2) |
| Uncommon Words | 884 | Easy | 差集 | 只在其中一邊出現一次 |
| Set Mismatch | 645 | Easy | 差集 | 找出重複的與缺失的 |
| Fair Candy Swap | 888 | Easy | 成員判斷 | 目標差值 |
| Buddy Strings | 859 | Easy | 配對的集合 | 檢查能不能交換 |

### 分類 3：路徑／祖先追蹤（6 題）
| 題目 | LC # | 難度 | 模式 | 關鍵洞見 |
|---------|------|------------|---------|-------------|
| Lowest Common Ancestor III | 1650 | Medium | 祖先集合 | 記錄往上的父節點路徑 |
| Linked List Cycle | 141 | Easy | 走訪過集合 | 雙指標更好 |
| Linked List Cycle II | 142 | Medium | 走訪過集合 | Floyd 演算法 |
| Course Schedule | 207 | Medium | DFS + 集合 | 偵測環 |
| Course Schedule II | 210 | Medium | 拓撲排序 | 追蹤走訪過／當前路徑 |
| Find Eventual Safe Nodes | 802 | Medium | DFS + 狀態 | 終點節點 vs 不安全節點 |

### 分類 4：序列問題（7 題）
| 題目 | LC # | 難度 | 模式 | 關鍵洞見 |
|---------|------|------------|---------|-------------|
| Longest Consecutive Sequence | 128 | Medium | 集合查詢 | 只從序列的起點開始算 |
| Longest Substring Without Repeat | 3 | Medium | 滑動視窗集合 | 記錄看過的字元 |
| Longest Palindrome | 409 | Easy | 字元頻率 | 成對的加上一個奇數 |
| Maximum Length of Repeated Subarray | 718 | Medium | tuple 的集合 | 滾動雜湊 |
| Arithmetic Slices | 413 | Medium | 差值的集合 | 追蹤合法的序列 |
| Happy Number | 202 | Easy | 找環 | 記錄看過的和 |
| Valid Sudoku | 36 | Medium | 多個集合 | 分別追蹤列／行／宮格 |

### 分類 5：圖／島嶼問題（5 題）
| 題目 | LC # | 難度 | 模式 | 關鍵洞見 |
|---------|------|------------|---------|-------------|
| Number of Islands | 200 | Medium | DFS/BFS 走訪過集合 | 記錄處理過的格子 |
| Number of Distinct Islands | 694 | Medium | 形狀雜湊 | 把座標正規化 |
| Max Area of Island | 695 | Medium | DFS + 走訪過 | 記錄看過的格子 |
| Island Perimeter | 463 | Easy | 數邊界 | 數陸地與水相鄰的邊 |
| Surrounded Regions | 130 | Medium | 從邊界 DFS | 標記與邊界相連的區域 |

### 分類 6：字串／模式比對（6 題）
| 題目 | LC # | 難度 | 模式 | 關鍵洞見 |
|---------|------|------------|---------|-------------|
| Isomorphic Strings | 205 | Easy | 一對一映射 | 兩個 map 或兩個集合 |
| Word Pattern | 290 | Easy | 一對一映射 | 字元 ↔ 單字的映射 |
| Group Anagrams | 49 | Medium | 排序後當 key | 用排序後的字串 |
| Find Anagrams | 438 | Medium | 視窗 + 計數 | 滑動視窗的字元計數 |
| Jewels and Stones | 771 | Easy | 成員判斷 | 珠寶字元的集合 |
| Unique Email Addresses | 929 | Easy | 正規化 + 集合 | 先把 email 清乾淨 |

### 分類 7：有序集合（TreeSet）與「集合當索引」（8 題）

這些題目**光靠 HashSet 不夠** — 你需要順序（floor/ceiling）、位置索引，或是複合 key。

| 題目 | LC # | 難度 | 模式 | 關鍵洞見 |
|---------|------|------------|---------|-------------|
| Insert Delete GetRandom O(1) | 380 | Medium | 集合 + 密集陣列 | 刪除時跟最後一個交換 → §2-11 |
| Word Ladder | 127 | Hard | 兩端 frontier 集合 | 雙向 BFS，`remove` 就等於標記走訪過 → §2-12 |
| Odd Even Jump | 975 | Hard | TreeMap floor/ceiling | 找最接近且 ≥ / ≤ x 的值 → §2-13 |
| Minimum Area Rectangle | 939 | Medium | 編碼過的點集合 | 一條對角線就決定另外 2 個角 → §2-14 |
| The Skyline Problem | 218 | Hard | 有序多重集合 | 需要**支援任意刪除**的取最大值 → `TreeMap<height,count>` |
| Falling Squares | 699 | Hard | 有序區間集合 | 查詢某段區間的最大高度，然後覆蓋掉它 |
| Set Matrix Zeroes | 73 | Medium | 列集合 + 行集合 | 用兩個集合標記要歸零的位置（O(1) 空間的追問：改用第 0 列／第 0 行） |
| Intersection of Two Linked Lists | 160 | Easy | 節點走訪過集合 | 用節點集合可行；但 O(1) 空間的正解是雙指標換軌 |

## 決策框架

### 什麼時候用集合，什麼時候用別的資料結構

```text
Problem Analysis:

1. Need to track unique elements?
   ├── YES → Consider Set
   │   ├── Need ordering?
   │   │   ├── YES → TreeSet (Java) / sorted list (Python)
   │   │   └── NO → HashSet
   │   ├── Need count?
   │   │   └── NO → Use Counter/HashMap instead
   │   └── Need fast lookups?
   │       └── YES → HashSet (O(1) average)
   └── NO → Consider other structures

2. Performing set operations (union, intersection)?
   ├── YES → Use Set
   │   └── Multiple operations → Build set once
   └── NO → Continue analysis

3. Detecting duplicates/cycles?
   ├── YES → Use Set for visited tracking
   │   ├── Space constrained?
   │   │   └── YES → Consider Floyd's algorithm
   │   └── NO → Set is ideal
   └── NO → Continue analysis

4. Checking membership repeatedly?
   ├── YES → Convert to Set first
   │   └── O(n) conversion + O(1) lookups
   └── NO → Linear search may be fine
```

### 集合 vs HashMap 怎麼選

| 用集合的時機 | 用 HashMap 的時機 |
|--------------|------------------|
| 只需要判斷存不存在 | 需要 key-value 映射 |
| 去除重複 | 統計出現次數 |
| 集合運算（∪、∩、-） | 需要附帶資料 |
| 省記憶體（不存值） | 需要記錄次數／索引 |

### Python set vs Java Set

| 功能 | Python `set` | Java `HashSet` |
|---------|-------------|----------------|
| **建立** | `s = {1,2,3}` 或 `set()` | `Set<T> s = new HashSet<>()` |
| **新增** | `s.add(x)` | `s.add(x)` |
| **刪除** | `s.remove(x)` / `s.discard(x)` | `s.remove(x)` |
| **是否存在** | `x in s` | `s.contains(x)` |
| **聯集** | `s1 | s2` 或 `s1.union(s2)` | `s1.addAll(s2)` |
| **交集** | `s1 & s2` 或 `s1.intersection(s2)` | `s1.retainAll(s2)` |
| **差集** | `s1 - s2` 或 `s1.difference(s2)` | `s1.removeAll(s2)` |
| **大小** | `len(s)` | `s.size()` |
| **是否為空** | `not s` 或 `len(s) == 0` | `s.isEmpty()` |

## 總結與最佳實務

### 重點整理

1. **什麼時候該用集合**：
   - 把容器裡的重複元素去掉
   - 快速判斷成員（平均 O(1)）
   - 做集合運算（聯集、交集、差集）
   - 在圖／樹上記錄走訪過的節點
   - 偵測環

2. **效能特性**：
   - HashSet：平均 O(1)，最壞 O(n)（雜湊碰撞）
   - TreeSet：所有操作都是 O(log n)
   - LinkedHashSet：O(1) 操作，外加插入順序

3. **常見模式**：
   - 把陣列轉成集合來去重
   - 用集合做 O(1) 查詢，取代 O(n) 的線性搜尋
   - 用集合記錄走訪過的節點
   - 檢查元素是否已在集合中，以此偵測環

4. **時間與空間的取捨**：
   - 集合用 O(n) 額外空間換 O(1) 操作
   - 空間受限時，考慮改用雙指標
   - 輸入很小的時候，線性搜尋可能反而更快

### 面試提示

**常見錯誤：**
- 在 Python 用 `{}` 想建立空集合（結果建出來是 dict）
- 忘記集合是無序的（別假設有順序）
- 需要有序元素時沒想到 TreeSet
- 需要統計次數卻用集合（該用 Counter/HashMap）

**優化技巧：**
- 要反覆做成員判斷前，先把 list 轉成集合
- 用集合運算取代手寫迴圈
- 需要不可變／可雜湊的集合時，考慮 frozenset
- 用 set comprehension 讓程式更乾淨

**常見追問：**
- 「能不能用 O(1) 空間解？」→ 想想 Floyd 演算法
- 「如果需要保留順序呢？」→ LinkedHashSet 或 OrderedDict
- 「如果需要有序元素呢？」→ TreeSet 或有序 list
- 「如果重複的元素還帶著不同資料呢？」→ 改用 HashMap

## 實作範例

十四題實作放在 **[set_examples.md](./set_examples.md)**，按照集合在題目裡「實際被拿來做什麼」分組 —
而那通常都不是「拿來存東西」：

| 分組 | 集合的角色 | 題目 |
|---|---|---|
| [「我看過這個嗎？」](./set_examples.md#have-i-seen-this-before) | 成員判斷就是對過去的記憶 | LC 217, 136, 202, 141 |
| [集合代數](./set_examples.md#set-algebra) | 交集、差集、一對一映射 | LC 349, 290 |
| [集合當索引用](./set_examples.md#the-set-as-an-index) | 用 O(1) 查詢取代一次掃描 | LC 128, 36, 939, 694 |
| [藏在其他演算法裡](./set_examples.md#sets-inside-other-algorithms) | 當 frontier、當有序結構，或是某個設計題對外的那一半 | LC 380, 127, 975, 1650 |
