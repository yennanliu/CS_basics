# Hashing & Counting

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

### 1. Valid Anagram (LC 242)
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

### 2. Group Anagrams (LC 49)
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

### 3. Happy Number (LC 202)
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

### 4. Longest Consecutive Sequence (LC 128)
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

### 5. Repeated DNA Sequences (LC 187)
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

### 6. Top K Frequent Elements (LC 347)
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