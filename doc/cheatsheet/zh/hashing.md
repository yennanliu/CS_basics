# 雜湊與計數

> **範圍** — 雜湊的**內部原理**與計數慣用手法 — 雜湊函式設計、碰撞、頻率表、滾動雜湊、自訂鍵。
> **另見**：[hash_map.md](./hash_map.md) — 以 map 為形狀的 LC 題型；[set.md](./set.md) — 成員判斷與去重；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 把滾動雜湊用在子字串搜尋上。

## LeetCode 題目清單

- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Counting](https://leetcode.com/problem-list/counting/)
- [Hash Function](https://leetcode.com/problem-list/hash-function/)

## 總覽
**雜湊與計數**這類技巧用雜湊表與頻率表來解決計數、分組與快速查找的問題。

### 關鍵性質
- **時間複雜度**：雜湊操作平均 O(1)，整體走訪 O(n)
- **空間複雜度**：雜湊表本身佔 O(n)
- **核心想法**：用雜湊表這種資料結構，拿空間換時間
- **什麼時候用**：快速查找、頻率計數、偵測重複、分組
- **關鍵資料結構**：HashMap、HashSet、Counter、defaultdict

### 核心特性
- **查找很快**：搜尋／插入／刪除平均都是 O(1)
- **追蹤頻率**：數某個元素出現幾次
- **偵測重複**：認出看過的元素
- **分組**：把性質相同的東西收在一起
- **滾動雜湊**：有效率地處理字串比對與子字串問題

## 題型分類

### **分類 1：頻率表**
- **說明**：統計出現次數並依頻率分組
- **例子**：LC 242 (Valid Anagram)、LC 49 (Group Anagrams)、LC 169 (Majority Element)
- **模式**：用 HashMap 統計頻率，再拿這些次數做分析

### **分類 2：前綴雜湊／滾動雜湊**
- **說明**：用雜湊函式做有效率的字串比對
- **例子**：LC 28 (Find Index)、LC 187 (Repeated DNA)、LC 1044 (Longest Duplicate Substring)
- **模式**：為滑動視窗計算滾動雜湊

### **分類 3：用 HashSet 記錄看過的狀態**
- **說明**：追蹤走訪過的元素，藉此偵測樣式或環
- **例子**：LC 202 (Happy Number)、LC 141 (Linked List Cycle)、LC 128 (Longest Consecutive)
- **模式**：用 HashSet 記住看過的狀態

## 模板與演算法

### 模板對照表
| 模板種類 | 使用情境 | 時間複雜度 | 什麼時候用 |
|---------------|----------|-----------------|-------------|
| **頻率計數器** | 統計元素次數 | O(n) | 變位詞、重複值 |
| **滾動雜湊** | 字串比對 | O(n+m) | 子字串搜尋 |
| **看過的狀態** | 偵測環 | O(n) | 偵測重複樣式 |
| **依雜湊鍵分組** | 分類 | O(n) | 把相似的東西歸在一起 |

### Template 1: Frequency Counter
```python
def frequency_counter_template(arr):
    """Basic frequency counting template"""
    from collections import Counter, defaultdict

    # Method 1: Using Counter
    count = Counter(arr)

    # Method 2: Using defaultdict
    freq = defaultdict(int)
    for item in arr:
        freq[item] += 1

    # Method 3: Manual counting
    manual_count = {}
    for item in arr:
        manual_count[item] = manual_count.get(item, 0) + 1

    return count, freq, manual_count
```

### Template 2: Rolling Hash (Rabin-Karp)
```python
def rolling_hash_template(text, pattern):
    """Rolling hash for pattern matching"""
    if len(pattern) > len(text):
        return -1

    # Hash function parameters
    base = 256
    mod = 10**9 + 7

    def compute_hash(s, length):
        """Compute hash for first 'length' characters"""
        hash_val = 0
        for i in range(length):
            hash_val = (hash_val * base + ord(s[i])) % mod
        return hash_val

    def rolling_hash(s, old_hash, old_char, new_char, base_power, mod):
        """Update hash by removing old_char and adding new_char"""
        new_hash = (old_hash - ord(old_char) * base_power) % mod
        new_hash = (new_hash * base + ord(new_char)) % mod
        return new_hash

    pattern_len = len(pattern)
    pattern_hash = compute_hash(pattern, pattern_len)
    text_hash = compute_hash(text, pattern_len)

    # Precompute base^(pattern_len-1) % mod
    base_power = pow(base, pattern_len - 1, mod)

    # Check first window
    if pattern_hash == text_hash and text[:pattern_len] == pattern:
        return 0

    # Rolling hash for remaining windows
    for i in range(len(text) - pattern_len):
        text_hash = rolling_hash(
            text, text_hash, text[i], text[i + pattern_len], base_power, mod
        )

        if pattern_hash == text_hash and text[i+1:i+1+pattern_len] == pattern:
            return i + 1

    return -1
```

### Template 3: HashSet for Cycle Detection
```python
def cycle_detection_template(start_value, next_function):
    """Detect cycles using HashSet"""
    seen = set()
    current = start_value

    while current not in seen:
        seen.add(current)
        current = next_function(current)

        # Optional: check for termination condition
        if is_terminal(current):
            return False

    return True  # Cycle detected

def floyd_cycle_detection(start_value, next_function):
    """Floyd's cycle detection (tortoise and hare)"""
    slow = fast = start_value

    # Phase 1: Detect if cycle exists
    while True:
        slow = next_function(slow)
        fast = next_function(next_function(fast))
        if slow == fast:
            break
        if is_terminal(fast):
            return None  # No cycle

    # Phase 2: Find cycle start
    slow = start_value
    while slow != fast:
        slow = next_function(slow)
        fast = next_function(fast)

    return slow  # Start of cycle
```

### Template 4: Group by Hash Key
```python
def group_by_hash_template(items, key_function):
    """Group items by hash key"""
    from collections import defaultdict

    groups = defaultdict(list)
    for item in items:
        key = key_function(item)
        groups[key].append(item)

    return dict(groups)

def group_anagrams_template(strs):
    """Group anagrams using sorted string as key"""
    from collections import defaultdict

    groups = defaultdict(list)
    for s in strs:
        # Use sorted string as key
        key = ''.join(sorted(s))
        groups[key].append(s)

    return list(groups.values())
```

## 依模式分類的題目

### **頻率表類題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Valid Anagram | 242 | 字元頻率 | Easy |
| Group Anagrams | 49 | 用排序後的字串當鍵 | Medium |
| Majority Element | 169 | 統計頻率 | Easy |
| Top K Frequent Elements | 347 | 頻率 + 堆積 | Medium |
| Find All Anagrams | 438 | 滑動視窗 + 頻率 | Medium |
| Longest Substring Without Repeating | 3 | 滑動視窗 + 看過的字元 | Medium |

### **滾動雜湊類題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Implement strStr() | 28 | Rabin-Karp | Easy |
| Repeated DNA Sequences | 187 | 10 個字元的滾動雜湊 | Medium |
| Longest Duplicate Substring | 1044 | 二分搜尋 + 滾動雜湊 | Hard |
| Find All Duplicates in Array | 442 | 索引當雜湊 | Medium |

### **用 HashSet 記錄狀態的題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Happy Number | 202 | 偵測數列中的環 | Easy |
| Linked List Cycle | 141 | 快慢指標或 HashSet | Easy |
| Longest Consecutive Sequence | 128 | 用 HashSet 查找 | Medium |
| Contains Duplicate | 217 | 單純的 HashSet | Easy |
| Contains Duplicate II | 219 | 帶視窗的 HashSet | Easy |

## LC 範例

### 2-1) Valid Anagram (LC 242) — Frequency Count
> 統計兩個字串的字元頻率；兩張表必須完全相同。

```java
// LC 242 - Valid Anagram
// IDEA: Count char frequencies; both strings must have same counts
// time = O(N), space = O(1) (fixed 26-char alphabet)
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    int[] count = new int[26];
    for (char c : s.toCharArray()) count[c - 'a']++;
    for (char c : t.toCharArray()) count[c - 'a']--;
    for (int v : count) if (v != 0) return false;
    return true;
}
```

```python
def isAnagram(s, t):
    """Check if two strings are anagrams"""
    if len(s) != len(t):
        return False

    # Method 1: Frequency counter
    from collections import Counter
    return Counter(s) == Counter(t)

    # Method 2: Manual counting
    count = {}
    for char in s:
        count[char] = count.get(char, 0) + 1

    for char in t:
        if char not in count:
            return False
        count[char] -= 1
        if count[char] == 0:
            del count[char]

    return len(count) == 0

    # Method 3: Sorting (not using hash)
    return sorted(s) == sorted(t)
```

### 2-2) Group Anagrams (LC 49) — Sort-Key HashMap
> 用排序後的字串當鍵；所有互為變位詞的字串會共用同一個鍵。

```java
// LC 49 - Group Anagrams
// IDEA: Sort each string to get canonical key; group by key in HashMap
// time = O(N * K log K), space = O(NK)  K = max string length
public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> map = new HashMap<>();
    for (String s : strs) {
        char[] arr = s.toCharArray();
        Arrays.sort(arr);
        String key = new String(arr);
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
    }
    return new ArrayList<>(map.values());
}
```

```python
def groupAnagrams(strs):
    """Group strings that are anagrams"""
    from collections import defaultdict

    # Method 1: Use sorted string as key
    groups = defaultdict(list)
    for s in strs:
        key = ''.join(sorted(s))
        groups[key].append(s)

    return list(groups.values())

    # Method 2: Use frequency tuple as key
    def get_frequency_key(s):
        freq = [0] * 26
        for char in s:
            freq[ord(char) - ord('a')] += 1
        return tuple(freq)

    groups = defaultdict(list)
    for s in strs:
        key = get_frequency_key(s)
        groups[key].append(s)

    return list(groups.values())
```

### 2-3) Happy Number (LC 202) — HashSet Cycle Detection
> 反覆把各位數平方相加；用一個 set 判斷在抵達 1 之前是否又回到看過的數字。

```java
// LC 202 - Happy Number
// IDEA: HashSet to detect cycle in digit-square sum sequence
// time = O(log N), space = O(log N)
public boolean isHappy(int n) {
    Set<Integer> seen = new HashSet<>();
    while (n != 1 && seen.add(n)) {
        int sum = 0;
        while (n > 0) { int d = n % 10; sum += d * d; n /= 10; }
        n = sum;
    }
    return n == 1;
}
```

```python
def isHappy(n):
    """Detect if number leads to 1 or cycles"""

    def get_sum_of_squares(num):
        total = 0
        while num > 0:
            digit = num % 10
            total += digit * digit
            num //= 10
        return total

    # Method 1: HashSet to detect cycle
    seen = set()
    while n != 1 and n not in seen:
        seen.add(n)
        n = get_sum_of_squares(n)

    return n == 1

    # Method 2: Floyd's cycle detection
    def next_number(num):
        return get_sum_of_squares(num)

    slow = fast = n
    while True:
        slow = next_number(slow)
        fast = next_number(next_number(fast))
        if slow == fast:
            break

    return slow == 1
```

### 2-4) Longest Consecutive Sequence (LC 128) — HashSet Start Detection
> 只從序列的起點（num−1 不在 set 裡）開始往外擴，避免重複計算。

```java
// LC 128 - Longest Consecutive Sequence
// IDEA: HashSet; for each sequence start (num-1 absent), count forward
// time = O(N), space = O(N)
public int longestConsecutive(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int n : nums) set.add(n);
    int best = 0;
    for (int n : set) {
        if (!set.contains(n - 1)) {
            int len = 1;
            while (set.contains(n + len)) len++;
            best = Math.max(best, len);
        }
    }
    return best;
}
```

```python
def longestConsecutive(nums):
    """Find longest consecutive sequence"""
    if not nums:
        return 0

    num_set = set(nums)
    longest = 0

    for num in num_set:
        # Only start counting if num-1 is not in set
        # This ensures we start from the beginning of a sequence
        if num - 1 not in num_set:
            current_num = num
            current_length = 1

            # Count consecutive numbers
            while current_num + 1 in num_set:
                current_num += 1
                current_length += 1

            longest = max(longest, current_length)

    return longest
```

### 2-5) Repeated DNA Sequences (LC 187) — Sliding Window HashSet
> 滑動一個 10 字元的視窗；放進 seen set；把重複的收進結果 set。

```java
// LC 187 - Repeated DNA Sequences
// IDEA: Slide 10-char window with HashSet; add to result if already seen
// time = O(N), space = O(N)
public List<String> findRepeatedDnaSequences(String s) {
    Set<String> seen = new HashSet<>(), result = new HashSet<>();
    for (int i = 0; i + 10 <= s.length(); i++) {
        String sub = s.substring(i, i + 10);
        if (!seen.add(sub)) result.add(sub);
    }
    return new ArrayList<>(result);
}
```

```python
def findRepeatedDnaSequences(s):
    """Find repeated 10-character DNA sequences using rolling hash"""
    if len(s) < 10:
        return []

    # Method 1: Simple approach with substring
    seen = set()
    repeated = set()

    for i in range(len(s) - 9):
        substring = s[i:i+10]
        if substring in seen:
            repeated.add(substring)
        else:
            seen.add(substring)

    return list(repeated)

    # Method 2: Rolling hash approach
    def char_to_num(c):
        return {'A': 0, 'C': 1, 'G': 2, 'T': 3}[c]

    def rolling_hash_dna(s):
        if len(s) < 10:
            return []

        seen = set()
        repeated = set()

        # Compute hash for first window
        hash_val = 0
        base = 4
        mod = 10**9 + 7

        for i in range(10):
            hash_val = hash_val * base + char_to_num(s[i])

        seen.add(hash_val)
        base_power = base ** 9

        # Rolling hash for remaining windows
        for i in range(10, len(s)):
            # Remove first character and add new character
            hash_val = hash_val - char_to_num(s[i-10]) * base_power
            hash_val = hash_val * base + char_to_num(s[i])

            if hash_val in seen:
                repeated.add(s[i-9:i+1])
            else:
                seen.add(hash_val)

        return list(repeated)

    return rolling_hash_dna(s)
```

### 2-6) Top K Frequent Elements (LC 347) — Bucket Sort by Frequency
> 把元素放進以頻率為索引的桶子；從頻率最高的桶往下收滿 k 個。

```java
// LC 347 - Top K Frequent Elements
// IDEA: Count frequencies, then bucket sort by frequency; collect from high buckets
// time = O(N), space = O(N)
public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> count = new HashMap<>();
    for (int n : nums) count.merge(n, 1, Integer::sum);
    List<Integer>[] buckets = new List[nums.length + 1];
    count.forEach((val, freq) -> {
        if (buckets[freq] == null) buckets[freq] = new ArrayList<>();
        buckets[freq].add(val);
    });
    int[] res = new int[k];
    int idx = 0;
    for (int i = buckets.length - 1; i >= 0 && idx < k; i--)
        if (buckets[i] != null) for (int v : buckets[i]) if (idx < k) res[idx++] = v;
    return res;
}
```

```python
def topKFrequent(nums, k):
    """Find k most frequent elements"""
    from collections import Counter
    import heapq

    # Method 1: Counter + heap
    count = Counter(nums)
    return heapq.nlargest(k, count.keys(), key=count.get)

    # Method 2: Counter + sorting
    count = Counter(nums)
    return [item for item, freq in count.most_common(k)]

    # Method 3: Bucket sort approach
    count = Counter(nums)
    buckets = [[] for _ in range(len(nums) + 1)]

    # Place elements in buckets by frequency
    for num, freq in count.items():
        buckets[freq].append(num)

    # Collect top k elements
    result = []
    for i in range(len(buckets) - 1, 0, -1):
        if buckets[i]:
            result.extend(buckets[i])
            if len(result) >= k:
                return result[:k]

    return result
```

## 鍵的設計與雜湊內部原理

> 上面那些模板雜湊的是*本來就*可雜湊的值。這一節講的是雜湊面試裡比較難的那一半：**發明一個鍵**，讓「相等」剛好等於題目要的那種相等，以及**自己把表做出來**。

### 快速決策表

| 目標 | 模板 | 你要造的鍵 | 例子 |
|------|----------|---------------|----------|
| 自己實作一個 map | [Template 5](#template-5-build-a-hash-map-from-scratch-lc-706) ⭐⭐⭐⭐⭐ | `hash(k) % capacity` → bucket | LC 706 |
| 用一個 set 同時管好多個限制 | [Template 6](#template-6-canonical-composite-key-lc-36) ⭐⭐⭐⭐⭐ | 帶標籤的 tuple `("row", r, d)` | LC 36, LC 939 |
| 比的是*形狀*而不是值 | [Template 7](#template-7-structural-hashing--canonical-serialization-lc-572) ⭐⭐⭐⭐ | 標準化的序列化字串 | LC 572, LC 508 |
| 依比值／方向分組 | [Template 8](#template-8-normalized-fraction-key-lc-149) ⭐⭐⭐⭐ | 約分過的 `(dx, dy)` | LC 149 |

**鍵設計的黃金法則**：兩個東西產生**位元組完全相同的鍵，若且唯若它們在這題的意義下是等價的**。這一節裡的每個 bug 不是*誤合*（兩個不同的東西被壓成同一個鍵）就是*誤分*（兩個等價的東西拿到不同的鍵）。

---

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

```java
// java
// LC 706 - Design HashMap  (separate chaining)
// time = O(1) average / O(N) worst per op, space = O(N)
// IDEA: fixed prime bucket array; each bucket is a singly linked list of entries
class MyHashMap {
    private static final int SIZE = 769;   // prime bucket count -> fewer clustered collisions
    private static class Node {
        int key, val; Node next;
        Node(int k, int v, Node n) { key = k; val = v; next = n; }
    }
    private final Node[] buckets = new Node[SIZE];

    private int idx(int key) { return Integer.hashCode(key) % SIZE; }

    public void put(int key, int val) {
        int i = idx(key);
        for (Node cur = buckets[i]; cur != null; cur = cur.next)
            if (cur.key == key) { cur.val = val; return; }   // update in place
        buckets[i] = new Node(key, val, buckets[i]);         // prepend = O(1) insert
    }

    public int get(int key) {
        for (Node cur = buckets[idx(key)]; cur != null; cur = cur.next)
            if (cur.key == key) return cur.val;
        return -1;
    }

    public void remove(int key) {
        int i = idx(key);
        Node prev = null, cur = buckets[i];
        while (cur != null) {
            if (cur.key == key) {
                if (prev == null) buckets[i] = cur.next; else prev.next = cur.next;
                return;
            }
            prev = cur; cur = cur.next;
        }
    }
}
```

```python
# python
# LC 706 - Design HashMap  (open addressing: linear probing + tombstones + resize)
# time = O(1) amortized per op, space = O(N)
# IDEA: one flat array; on collision walk forward; deletes leave a tombstone so
#       probe chains stay intact; resize when half the slots are used
class MyHashMap:
    _EMPTY = object()
    _DEL = object()                       # tombstone

    def __init__(self):
        self.cap = 16
        self.keys = [self._EMPTY] * self.cap
        self.vals = [0] * self.cap
        self.used = 0                     # live + tombstone slots -> drives resize

    def _probe(self, key):
        """return (slot, found); reuses the first tombstone when inserting"""
        i = hash(key) % self.cap
        first_del = -1
        while self.keys[i] is not self._EMPTY:
            if self.keys[i] is self._DEL:
                if first_del < 0:
                    first_del = i
            elif self.keys[i] == key:
                return i, True
            i = (i + 1) % self.cap        # linear probe
        return (first_del if first_del >= 0 else i), False

    def put(self, key, value):
        i, found = self._probe(key)
        if not found and self.keys[i] is self._EMPTY:
            self.used += 1
        self.keys[i], self.vals[i] = key, value
        if self.used * 2 >= self.cap:     # load factor 0.5
            self._rehash()

    def get(self, key):
        i, found = self._probe(key)
        return self.vals[i] if found else -1

    def remove(self, key):
        i, found = self._probe(key)
        if found:
            self.keys[i] = self._DEL      # NOT _EMPTY - that would cut the probe chain

    def _rehash(self):
        items = [(k, v) for k, v in zip(self.keys, self.vals)
                 if k is not self._EMPTY and k is not self._DEL]
        self.cap *= 2
        self.keys = [self._EMPTY] * self.cap
        self.vals = [0] * self.cap
        self.used = 0                     # rebuilding also sweeps away all tombstones
        for k, v in items:
            self.put(k, v)
```

> **值得先演練的追問**：*「為什麼 `HashMap` 最壞情況是 O(N)？」* → 因為所有鍵都碰撞到同一個 bucket。Java 8+ 的緩解做法是當某個 bucket 超過 8 個項目時把它轉成紅黑樹，最壞情況變成 O(log N)。
>
> **拿自訂物件當鍵**：如果你覆寫了 `equals()`，就**必須**同時覆寫 `hashCode()` — 相等的物件被規定要有相等的雜湊值，否則 map 會弄丟項目。Java 用 `Objects.hash(a, b)`；Python 用 `tuple`／`frozenset`，或是在定義 `__eq__` 的同時定義 `__hash__`。

---

### Template 6: Canonical Composite Key (LC 36)

**核心想法**：當好幾個彼此獨立的限制都必須同時成立時，不要開好幾張表。把每個限制**標籤化**寫進鍵裡，全部丟進**同一個** set。那個標籤就是用來擋掉「第 3 列有個 5」跟「第 3 行有個 5」之間誤合的東西。

```java
// java
// LC 36 - Valid Sudoku
// time = O(81) = O(1), space = O(81) = O(1)
// IDEA: encode each (constraint-type, index, digit) as one string key in a single HashSet;
//       Set.add returns false the moment a duplicate constraint appears
public boolean isValidSudoku(char[][] board) {
    Set<String> seen = new HashSet<>();
    for (int r = 0; r < 9; r++) {
        for (int c = 0; c < 9; c++) {
            char d = board[r][c];
            if (d == '.') continue;
            if (!seen.add(d + "@row" + r)
             || !seen.add(d + "@col" + c)
             || !seen.add(d + "@box" + (r / 3) + "-" + (c / 3))) return false;
        }
    }
    return true;
}
```

```python
# python
# LC 36 - Valid Sudoku
# time = O(81) = O(1), space = O(81) = O(1)
# IDEA: tuples make the tag explicit and need no delimiter juggling
def isValidSudoku(board):
    seen = set()
    for r in range(9):
        for c in range(9):
            d = board[r][c]
            if d == '.':
                continue
            for k in (('row', r, d), ('col', c, d), ('box', r // 3, c // 3, d)):
                if k in seen:
                    return False
                seen.add(k)
    return True
```

> **分隔符陷阱**：用字串當鍵時，`"1" + "2" + "3"` 跟 `"12" + "3"` 會撞在一起。一定要把各欄位隔開（`"@row"`、`"-"`），或乾脆用 tuple。Python 的 tuple／Java 的 record 是安全的預設值；只有在你真的需要一個扁平命名空間時才用字串。

#### 變體：把一組座標壓成一個整數鍵 — LC 939

*轉折：想法一樣，但鍵是用算的而不是用拼字串的 — 只有在你知道座標上界時才安全。*

```java
// java
// LC 939 - Minimum Area Rectangle
// time = O(N^2), space = O(N)
// IDEA: hash all points; for each pair treated as a DIAGONAL, look up the two
//       missing corners. x*40001+y is collision-free because 0 <= y <= 40000
public int minAreaRect(int[][] points) {
    Set<Integer> set = new HashSet<>();
    for (int[] p : points) set.add(p[0] * 40001 + p[1]);
    int best = Integer.MAX_VALUE;
    for (int i = 0; i < points.length; i++) {
        for (int j = i + 1; j < points.length; j++) {
            int[] a = points[i], b = points[j];
            if (a[0] == b[0] || a[1] == b[1]) continue;   // not a real diagonal
            if (set.contains(a[0] * 40001 + b[1]) && set.contains(b[0] * 40001 + a[1]))
                best = Math.min(best, Math.abs(a[0] - b[0]) * Math.abs(a[1] - b[1]));
        }
    }
    return best == Integer.MAX_VALUE ? 0 : best;
}
```

```python
# python
# LC 939 - Minimum Area Rectangle
# time = O(N^2), space = O(N)
def minAreaRect(points):
    pts = {(x, y) for x, y in points}          # tuple key: no packing math needed
    best = float('inf')
    for i in range(len(points)):
        x1, y1 = points[i]
        for j in range(i + 1, len(points)):
            x2, y2 = points[j]
            if x1 == x2 or y1 == y2:
                continue
            if (x1, y2) in pts and (x2, y1) in pts:
                best = min(best, abs(x1 - x2) * abs(y1 - y2))
    return 0 if best == float('inf') else best
```

> 乘數**必須大於低位欄位的最大值**（`y <= 40000` → 用 `40001`），而且乘積不能溢位：`40000 * 40001 + 40000 ≈ 1.6e9` 還塞得進 `int`。沒把握的話就用 `long` 或 tuple。

---

### Template 7: Structural Hashing — Canonical Serialization (LC 572)

**核心想法**：想用雜湊表比較*形狀*（子樹、格子、島嶼）時，先把每個形狀壓成一個**標準字串** — 要滿足「兩個形狀序列化結果相同**若且唯若**它們結構相同」。這樣形狀比對就退化成單純的字串比對。

兩個一定要有的成分：
1. **明確的 null 標記**（`#`）— 沒有它的話 `[1,2,null]` 跟 `[1,null,2]` 會序列化成一樣（誤合）。
2. **值的分隔符** — 沒有的話節點 `12` 跟節點 `1`+`2` 會糊在一起；這裡用值前面的 `^` 跟後面的 `(` 把它圍起來。

```java
// java
// LC 572 - Subtree of Another Tree
// time = O(M * N) worst / ~O(M + N) typical, space = O(M + N)
// IDEA: serialize both trees canonically, then "is a subtree" == "is a substring"
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    return serialize(root).contains(serialize(subRoot));
}

private String serialize(TreeNode node) {
    if (node == null) return "#";                       // explicit null marker
    return "^" + node.val + "(" + serialize(node.left) + "," + serialize(node.right) + ")";
}
```

```python
# python
# LC 572 - Subtree of Another Tree
# time = O(M * N) worst / ~O(M + N) typical, space = O(M + N)
def isSubtree(root, subRoot):
    def serialize(node):
        if not node:
            return "#"
        return f"^{node.val}({serialize(node.left)},{serialize(node.right)})"
    return serialize(subRoot) in serialize(root)
```

> 把序列化結果餵進**滾動雜湊**，就能把子字串測試壓到 O(M + N) — 見 [`string_matching_kmp_rolling_hash.md`](string_matching_kmp_rolling_hash.md)。同樣這招「把子樹雜湊成一個鍵」，就是重複子樹偵測（Merkle 式的樹雜湊）的原理。

#### 變體：不是拿標準鍵去配對，而是拿來計數 — LC 508

*轉折：鍵是一個算出來的聚合值（子樹總和）而不是序列化字串，而且我們對它做頻率統計。*

```java
// java
// LC 508 - Most Frequent Subtree Sum
// time = O(N), space = O(N)
// IDEA: post-order returns each subtree's sum; count sums in a HashMap, return the argmax set
public int[] findFrequentTreeSum(TreeNode root) {
    Map<Integer, Integer> count = new HashMap<>();
    subSum(root, count);
    int max = 0;
    for (int c : count.values()) max = Math.max(max, c);
    List<Integer> res = new ArrayList<>();
    for (Map.Entry<Integer, Integer> e : count.entrySet())
        if (e.getValue() == max) res.add(e.getKey());
    int[] out = new int[res.size()];
    for (int i = 0; i < out.length; i++) out[i] = res.get(i);
    return out;
}

private int subSum(TreeNode node, Map<Integer, Integer> count) {
    if (node == null) return 0;
    int s = node.val + subSum(node.left, count) + subSum(node.right, count);
    count.merge(s, 1, Integer::sum);
    return s;
}
```

```python
# python
# LC 508 - Most Frequent Subtree Sum
# time = O(N), space = O(N)
def findFrequentTreeSum(root):
    from collections import defaultdict
    count = defaultdict(int)

    def dfs(node):
        if not node:
            return 0
        s = node.val + dfs(node.left) + dfs(node.right)
        count[s] += 1
        return s

    dfs(root)
    if not count:
        return []
    best = max(count.values())
    return [s for s, c in count.items() if c == best]
```

---

### Template 8: Normalized Fraction Key (LC 149)

**核心想法**：絕對不要拿 `double` 當雜湊鍵。浮點數會讓 `1/3` 跟 `2/6` 落在*幾乎*相同但不完全相同的值上 — 這是不可靠的誤分，垂直線還會多一個除以零的狀況。改成把這組數字除以它們的 **gcd**，再固定一個**標準符號**，就得到一個精確的整數鍵。

方向 `(dx, dy)` 的標準形式：
1. 兩個都除以 `gcd(|dx|, |dy|)`；
2. 強制 `dx > 0`，或 `dx == 0 && dy > 0` — 這樣 `(1, 2)` 跟 `(-1, -2)`（同一條線）才不會誤分。

```java
// java
// LC 149 - Max Points on a Line
// time = O(N^2 * log C), space = O(N)   C = coordinate range (gcd cost)
// IDEA: anchor each point i, bucket every other point by its gcd-reduced slope key;
//       the biggest bucket + the anchor itself is the answer
public int maxPoints(int[][] points) {
    int n = points.length;
    if (n <= 2) return n;
    int best = 1;
    for (int i = 0; i < n; i++) {
        Map<String, Integer> slopes = new HashMap<>();
        for (int j = i + 1; j < n; j++) {
            int dx = points[j][0] - points[i][0];
            int dy = points[j][1] - points[i][1];
            int g = gcd(Math.abs(dx), Math.abs(dy));
            dx /= g; dy /= g;
            if (dx < 0 || (dx == 0 && dy < 0)) { dx = -dx; dy = -dy; }  // canonical sign
            int cnt = slopes.merge(dx + "/" + dy, 1, Integer::sum);
            best = Math.max(best, cnt + 1);                             // +1 for point i
        }
    }
    return best;
}

private int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
```

```python
# python
# LC 149 - Max Points on a Line
# time = O(N^2 * log C), space = O(N)
def maxPoints(points):
    from collections import defaultdict
    from math import gcd

    n = len(points)
    if n <= 2:
        return n
    best = 1
    for i in range(n):
        slopes = defaultdict(int)
        x1, y1 = points[i]
        for j in range(i + 1, n):
            dx, dy = points[j][0] - x1, points[j][1] - y1
            g = gcd(abs(dx), abs(dy))
            dx, dy = dx // g, dy // g
            if dx < 0 or (dx == 0 and dy < 0):      # canonical sign
                dx, dy = -dx, -dy
            slopes[(dx, dy)] += 1
            best = max(best, slopes[(dx, dy)] + 1)
    return best
```

> 只要鍵是**比值或方向**，同樣這套正規化就適用：除以 gcd、固定符號、保持整數。另外，斜率表要每換一個錨點就重設 — 所有錨點共用一張表是經典的誤合（穿過不同錨點的平行線）。

---

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

## 進階技巧

### 自訂雜湊函式
```python
def custom_hash_techniques():
    """Various custom hashing approaches"""

    # 1. Polynomial rolling hash
    def polynomial_hash(s, base=31, mod=10**9+7):
        hash_val = 0
        base_power = 1
        for char in s:
            hash_val = (hash_val + ord(char) * base_power) % mod
            base_power = (base_power * base) % mod
        return hash_val

    # 2. XOR hash for pairs
    def xor_hash(a, b):
        return hash(a) ^ hash(b)

    # 3. Tuple hash for coordinates
    def coordinate_hash(x, y):
        return hash((x, y))

    # 4. String hash ignoring order
    def unordered_hash(s):
        return sum(hash(c) for c in s)
```

### 以雜湊為基礎的資料結構
```python
class HashBasedStructures:
    """Examples of hash-based data structures"""

    def __init__(self):
        # Frequency counter
        from collections import defaultdict, Counter
        self.freq_counter = Counter()
        self.default_dict = defaultdict(int)

        # Seen states
        self.visited = set()

        # Grouped data
        self.groups = defaultdict(list)

    def add_element(self, element):
        """Add element and track frequency"""
        self.freq_counter[element] += 1
        self.visited.add(element)

    def group_by_property(self, items, key_func):
        """Group items by a property"""
        for item in items:
            key = key_func(item)
            self.groups[key].append(item)
        return dict(self.groups)
```

## 效能最佳化提示

### 雜湊表最佳實務
```python
def optimization_tips():
    """Performance optimization techniques"""

    # 1. Pre-size hash tables when possible
    large_dict = dict()  # Will resize multiple times
    presized_dict = {}

    # 2. Use appropriate hash functions
    def good_hash_function(obj):
        # Combine multiple attributes
        return hash((obj.attr1, obj.attr2, obj.attr3))

    # 3. Minimize hash collisions
    from collections import defaultdict

    # Use frozenset for set hashing
    set_as_key = frozenset([1, 2, 3])
    hash(set_as_key)  # Works because frozenset is hashable

    # 4. Consider memory vs speed tradeoffs
    memory_efficient = set()  # Only stores keys
    feature_rich = defaultdict(list)  # Stores key-value pairs
```

## 總結與速查

### 常見雜湊模式

| 模式 | 模板 | 使用情境 | 例子 |
|---------|----------|----------|---------|
| **頻率計數** | `Counter(arr)` | 統計出現次數 | 變位詞、重複值 |
| **看過的狀態** | `visited = set()` | 偵測環 | 快樂數、鏈結串列有環 |
| **依鍵分組** | `groups[key].append(item)` | 分類 | 分組變位詞 |
| **滾動雜湊** | 增量更新雜湊值 | 子字串搜尋 | 樣式比對 |

### 時間複雜度指南
| 操作 | 平均情況 | 最壞情況 | 備註 |
|-----------|--------------|------------|-------|
| 插入 | O(1) | O(n) | 前提是雜湊函式夠好 |
| 搜尋 | O(1) | O(n) | 取決於碰撞狀況 |
| 刪除 | O(1) | O(n) | 跟搜尋一樣 |
| 走訪 | O(n) | O(n) | 要看過所有元素 |

### 空間複雜度考量
- **雜湊表**：O(n)，n 是元素個數
- **滾動雜湊**：額外空間 O(1)
- **頻率計數器**：O(k)，k 是相異元素的個數

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

### 面試提示
1. **看出可以用雜湊的地方**：找出需要計數、分組或快速查找的地方
2. **選對資料結構**：set vs dict vs Counter vs defaultdict
3. **想清楚時間與空間的取捨**：雜湊表 vs 其他做法
4. **處理邊界情況**：空輸入、只有一個元素
5. **針對題目最佳化**：字串題用滾動雜湊，計數題用頻率表
6. **拿例子測一遍**：確認雜湊碰撞不會把邏輯弄壞

這份完整的雜湊 cheatsheet 收錄了解雜湊類題目最重要的模式與技巧。
