# Hashing & Counting

## LeetCode Problem Lists

- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Counting](https://leetcode.com/problem-list/counting/)
- [Hash Function](https://leetcode.com/problem-list/hash-function/)

## Overview
**Hashing & Counting** techniques use hash tables and frequency maps to solve problems involving counting, grouping, and fast lookups.

### Key Properties
- **Time Complexity**: O(1) average for hash operations, O(n) for full traversal
- **Space Complexity**: O(n) for hash table storage
- **Core Idea**: Trade space for time using hash table data structures
- **When to Use**: Fast lookups, frequency counting, duplicate detection, grouping
- **Key Data Structures**: HashMap, HashSet, Counter, defaultdict

### Core Characteristics
- **Fast Lookups**: O(1) average case for search/insert/delete
- **Frequency Tracking**: Count occurrences of elements
- **Duplicate Detection**: Identify seen elements
- **Grouping**: Collect items with same properties
- **Rolling Hash**: Efficient string matching and substring problems

## Problem Categories

### **Category 1: Frequency Maps**
- **Description**: Count occurrences and group by frequency
- **Examples**: LC 242 (Valid Anagram), LC 49 (Group Anagrams), LC 169 (Majority Element)
- **Pattern**: Use HashMap to count frequencies, then analyze counts

### **Category 2: Prefix Hash / Rolling Hash**
- **Description**: Efficient string matching using hash functions
- **Examples**: LC 28 (Find Index), LC 187 (Repeated DNA), LC 1044 (Longest Duplicate Substring)
- **Pattern**: Compute rolling hash for sliding windows

### **Category 3: HashSet for Seen States**
- **Description**: Track visited elements to detect patterns or cycles
- **Examples**: LC 202 (Happy Number), LC 141 (Linked List Cycle), LC 128 (Longest Consecutive)
- **Pattern**: Use HashSet to remember seen states

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Time Complexity | When to Use |
|---------------|----------|-----------------|-------------|
| **Frequency Counter** | Count elements | O(n) | Anagrams, duplicates |
| **Rolling Hash** | String matching | O(n+m) | Substring search |
| **Seen States** | Cycle detection | O(n) | Detect patterns |
| **Group by Hash** | Categorization | O(n) | Grouping similar items |

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

## Problems by Pattern

### **Frequency Maps Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Valid Anagram | 242 | Character frequency | Easy |
| Group Anagrams | 49 | Sorted string as key | Medium |
| Majority Element | 169 | Count frequency | Easy |
| Top K Frequent Elements | 347 | Frequency + heap | Medium |
| Find All Anagrams | 438 | Sliding window + freq | Medium |
| Longest Substring Without Repeating | 3 | Sliding window + seen | Medium |

### **Rolling Hash Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Implement strStr() | 28 | Rabin-Karp | Easy |
| Repeated DNA Sequences | 187 | 10-char rolling hash | Medium |
| Longest Duplicate Substring | 1044 | Binary search + rolling hash | Hard |
| Find All Duplicates in Array | 442 | Index hashing | Medium |

### **HashSet for Seen States Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Happy Number | 202 | Detect cycle in sequence | Easy |
| Linked List Cycle | 141 | Fast/slow or HashSet | Easy |
| Longest Consecutive Sequence | 128 | HashSet lookup | Medium |
| Contains Duplicate | 217 | Simple HashSet | Easy |
| Contains Duplicate II | 219 | HashSet with window | Easy |

## LC Examples

### 2-1) Valid Anagram (LC 242) — Frequency Count
> Count character frequencies in both strings; maps must be equal.

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
> Use sorted string as key; all anagrams share the same key.

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
> Sum digit squares repeatedly; use a set to detect if we revisit a number before reaching 1.

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
> Only expand from sequence starts (num−1 not in set) to avoid redundant counting.

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
> Slide a 10-char window; add to seen set; collect duplicates in result set.

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
> Place elements in buckets indexed by frequency; collect top k from highest buckets.

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

## Key Design & Hash Internals

> The templates above hash values that are *already* hashable. This section covers the harder half of hashing interviews: **inventing a key** so that "equal" means what the problem needs, and **building the table itself**.

### Quick Decision Table

| Goal | Template | Key you build | Examples |
|------|----------|---------------|----------|
| Implement the map itself | [Template 5](#template-5-build-a-hash-map-from-scratch-lc-706) ⭐⭐⭐⭐⭐ | `hash(k) % capacity` → bucket | LC 706 |
| One set enforces many constraints | [Template 6](#template-6-canonical-composite-key-lc-36) ⭐⭐⭐⭐⭐ | tagged tuple `("row", r, d)` | LC 36, LC 939 |
| Compare *shapes*, not values | [Template 7](#template-7-structural-hashing--canonical-serialization-lc-572) ⭐⭐⭐⭐ | canonical serialization string | LC 572, LC 508 |
| Group by a ratio / direction | [Template 8](#template-8-normalized-fraction-key-lc-149) ⭐⭐⭐⭐ | gcd-reduced `(dx, dy)` | LC 149 |

**Golden rule of key design**: two items must produce **byte-identical keys iff they are equivalent for the problem**. Every bug in this section is either a *false merge* (two different things collapse to one key) or a *false split* (two equivalent things get different keys).

---

### Template 5: Build a Hash Map From Scratch (LC 706)

**Key Idea**: a hash map is `bucket = hash(key) % capacity` plus a **collision policy**. The two classic policies are worth knowing cold, because interviewers ask "what happens on a collision?" as a follow-up to almost any hash question.

| | **Separate chaining** | **Open addressing (linear probing)** |
|---|---|---|
| Collision handling | Bucket holds a linked list | Walk to the next free slot |
| Delete | Unlink the node | Needs a **tombstone** marker |
| Load factor | Can exceed 1.0 | Must stay < 1 (resize at ~0.5–0.75) |
| Cache behavior | Poor (pointer chasing) | Good (contiguous array) |
| Used by | `java.util.HashMap` | Python `dict`, `Set` |

**Trap**: with open addressing you may **not** just blank a deleted slot — that breaks the probe chain and later lookups stop early. Write a `DELETED` tombstone instead, and reuse it on insert.

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

> **Follow-up to rehearse**: *"Why is `HashMap` O(N) worst case?"* → all keys collide into one bucket. Java 8+ mitigates this by converting a bucket to a red-black tree past 8 entries, giving O(log N) worst case.
>
> **Custom objects as keys**: if you override `equals()` you **must** override `hashCode()` — equal objects are required to have equal hashes, or the map will lose entries. In Java use `Objects.hash(a, b)`; in Python use a `tuple`/`frozenset`, or define `__hash__` alongside `__eq__`.

---

### Template 6: Canonical Composite Key (LC 36)

**Key Idea**: when several independent constraints must all hold, don't build several maps. **Tag** each constraint into the key and drop everything into **one** set. The tag is what prevents a false merge between `row 3 has a 5` and `col 3 has a 5`.

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

> **Delimiter trap**: with string keys, `"1" + "2" + "3"` and `"12" + "3"` collide. Always separate the fields (`"@row"`, `"-"`) or use a tuple. Python tuples / Java records are the safe default; strings are only for when you need one flat namespace.

#### Variation: pack a coordinate pair into one integer key — LC 939

*Twist: same idea, but the key is arithmetic instead of textual — safe only when you know the coordinate bound.*

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

> The multiplier **must exceed the max value of the low field** (`y <= 40000` → use `40001`), and the product must not overflow: `40000 * 40001 + 40000 ≈ 1.6e9` still fits in `int`. When in doubt use `long` or a tuple.

---

### Template 7: Structural Hashing — Canonical Serialization (LC 572)

**Key Idea**: to compare *shapes* (subtrees, grids, islands) with a hash map, first flatten each shape into a **canonical string** — one where two shapes serialize identically **iff** they are structurally identical. Then shape comparison becomes plain string comparison.

Two mandatory ingredients:
1. **Explicit null markers** (`#`) — without them `[1,2,null]` and `[1,null,2]` serialize the same (false merge).
2. **Value delimiters** — without them node `12` and node `1`+`2` blur together; here `^` before the value and `(` after fence it in.

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

> Feeding the serialization into a **rolling hash** turns the substring test into O(M + N) — see [`string_matching_kmp_rolling_hash.md`](string_matching_kmp_rolling_hash.md). The same "hash a subtree into a key" trick is how duplicate-subtree detection (Merkle-style tree hashing) works.

#### Variation: count canonical keys instead of matching them — LC 508

*Twist: the key is a computed aggregate (subtree sum) rather than a serialization, and we frequency-count it.*

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

**Key Idea**: never use a `double` as a hash key. Floating point makes `1/3` and `2/6` land on *almost* the same value — an unreliable false split, plus a division-by-zero case for vertical lines. Instead reduce the pair by its **gcd** and pin a **canonical sign**, giving an exact integer key.

Canonical form for a direction `(dx, dy)`:
1. divide both by `gcd(|dx|, |dy|)`;
2. force `dx > 0`, or `dx == 0 && dy > 0` — so `(1, 2)` and `(-1, -2)` (the same line) do not false-split.

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

> Same normalization trick applies whenever the key is a **ratio or direction**: reduce by gcd, fix the sign, keep it integral. Only reset the slope map per anchor point — sharing one map across anchors is a classic false merge (parallel lines through different anchors).

---

### Also Worth Knowing (no new template)

| Problem | LC # | Why it's here |
|---------|------|---------------|
| Insert Delete GetRandom O(1) | 380 | HashMap `value → index` + array; delete by swapping with the last element |
| Top K Frequent Words | 692 | LC 347 plus a lexicographic tie-break in the comparator |
| Ransom Note | 383 | Frequency-map subtraction (LC 242's asymmetric cousin) |
| Isomorphic Strings | 205 | Needs **two** maps — a one-way map allows a false merge |
| Task Scheduler | 621 | Counting map, then answer comes from `maxFreq` alone |
| Subarray Sum Equals K | 560 | Prefix-sum-as-key — see [`hash_map.md`](hash_map.md) |
| Continuous Subarray Sum | 523 | Prefix **remainder** as key — see [`hash_map.md`](hash_map.md) |

**Related cheatsheets**: [`hash_map.md`](hash_map.md) (map-centric patterns, prefix sums), [`set.md`](set.md) (dedup / membership), [`string_matching_kmp_rolling_hash.md`](string_matching_kmp_rolling_hash.md) (rolling hash in depth).

## Advanced Techniques

### Custom Hash Functions
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

### Hash-based Data Structures
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

## Performance Optimization Tips

### Hash Table Best Practices
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

## Summary & Quick Reference

### Common Hash Patterns

| Pattern | Template | Use Case | Example |
|---------|----------|----------|---------|
| **Frequency Count** | `Counter(arr)` | Count occurrences | Anagrams, duplicates |
| **Seen States** | `visited = set()` | Cycle detection | Happy number, linked list cycle |
| **Group by Key** | `groups[key].append(item)` | Categorization | Group anagrams |
| **Rolling Hash** | Update hash incrementally | Substring search | Pattern matching |

### Time Complexity Guide
| Operation | Average Case | Worst Case | Notes |
|-----------|--------------|------------|-------|
| Insert | O(1) | O(n) | With good hash function |
| Search | O(1) | O(n) | Depends on collisions |
| Delete | O(1) | O(n) | Same as search |
| Iteration | O(n) | O(n) | Visit all elements |

### Space Complexity Considerations
- **Hash Table**: O(n) where n is number of elements
- **Rolling Hash**: O(1) additional space
- **Frequency Counter**: O(k) where k is number of unique elements

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Using mutable objects as hash keys
- Not handling hash collisions properly
- Excessive hash function computation
- Memory leaks with large hash tables

**✅ Best Practices:**
- Use immutable types as keys (strings, tuples, frozensets)
- Choose good hash functions to minimize collisions
- Consider using `defaultdict` for automatic initialization
- Use `Counter` for frequency counting
- Implement rolling hash for string matching problems

### Interview Tips
1. **Identify hash opportunities**: Look for counting, grouping, or fast lookup needs
2. **Choose right data structure**: set vs dict vs Counter vs defaultdict
3. **Consider time-space tradeoffs**: Hash table vs other approaches
4. **Handle edge cases**: Empty inputs, single elements
5. **Optimize for the problem**: Rolling hash for strings, frequency maps for counting
6. **Test with examples**: Verify hash collisions don't break logic

This comprehensive hashing cheatsheet covers the most important patterns and techniques for solving hash-based problems efficiently.