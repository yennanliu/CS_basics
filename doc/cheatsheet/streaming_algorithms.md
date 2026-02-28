# Streaming Algorithms

- Process data streams with limited memory
- When to use: Data too large to store, online processing, random sampling, frequency estimation
- Key LeetCode problems: LC 382, 398 (Reservoir Sampling), LC 169, 229 (Majority Element), LC 528 (Random Pick with Weight)
- Core techniques: Reservoir sampling, probabilistic data structures, approximate counting
- Space-time tradeoff: Trade accuracy for memory efficiency

**Time Complexity:** O(1) per element processed (streaming)
**Space Complexity:** O(k) or O(log n) typically, independent of stream size

## 0) Concept

### 0-0) Why Streaming Algorithms?

**Problem:** Traditional algorithms assume entire dataset fits in memory
- **Streaming Context**: Data arrives continuously, too large to store
- **Memory Constraint**: Limited space, must process elements once
- **Requirements**: Make decisions with incomplete information

**Key Characteristics:**
- **Single Pass**: Process each element at most once
- **Sublinear Space**: Memory << data size
- **Approximate Results**: Trade exactness for efficiency (except Reservoir Sampling)
- **Online Processing**: Results available anytime

### 0-1) Types

#### 1. **Reservoir Sampling** - Uniform Random Sampling
- **Purpose**: Select k random elements from stream of unknown size
- **Guarantee**: Each element has exactly k/n probability of selection
- **Space**: O(k) for k samples
- **Applications**: Random sampling, shuffling, fairness in selection

#### 2. **Morris Counter** - Approximate Counting
- **Purpose**: Count large numbers with small memory
- **Space**: O(log log n) instead of O(log n)
- **Trade-off**: Approximate count with expected value n
- **Applications**: Large-scale counting, memory-constrained systems

#### 3. **Boyer-Moore Majority Vote** - Heavy Hitters
- **Purpose**: Find element(s) appearing > n/k times
- **Space**: O(k) for top-k elements
- **Applications**: Majority element, top-k frequent elements
- **Variants**: Standard (k=2), Generalized (arbitrary k)

#### 4. **Count-Min Sketch** - Frequency Estimation
- **Purpose**: Estimate frequency of elements in stream
- **Space**: O(w × d) hash table, w×d << n
- **Guarantee**: Overestimate by at most εn with probability 1-δ
- **Applications**: Network traffic analysis, query frequency

#### 5. **Bloom Filter** - Membership Testing
- **Purpose**: Test if element is in set (probabilistic)
- **Space**: O(m) bits, m << n
- **Guarantee**: No false negatives, false positives with probability p
- **Applications**: Cache filtering, spell checking, malware detection

### 0-2) When to Use Each Algorithm

| Algorithm | Use Case | Space | Accuracy | Output |
|-----------|----------|-------|----------|--------|
| Reservoir Sampling | Random k samples | O(k) | Exact | k elements |
| Morris Counter | Count stream size | O(log log n) | Approximate | ~count |
| Boyer-Moore | Majority element | O(k) | Exact if exists | Candidates |
| Count-Min Sketch | Frequency estimation | O(w×d) | Approximate | ~frequency |
| Bloom Filter | Set membership | O(m) | False positives | yes/no |

**Recognition Keywords:**
- "Random sample", "shuffle", "fairness" → **Reservoir Sampling**
- "Majority element", "appears more than n/k times" → **Boyer-Moore**
- "Estimate frequency", "approximate count" → **Count-Min Sketch**
- "Check membership", "seen before?" → **Bloom Filter**
- "Limited memory", "one pass", "streaming data" → **Any streaming algorithm**

---

## 1) Algorithm Templates

### 1-1) Reservoir Sampling (LC 382, 398)

**Algorithm Overview:**
- Maintain reservoir of k elements
- For element at index i (i ≥ k):
  - Generate random j ∈ [0, i]
  - If j < k, replace reservoir[j] with current element
- **Proof**: Element at index i has k/i chance to enter, k/(i+1) to stay → k/n final probability

**Template 1: Single Random Element (k=1)**

```java
// LC 382 - Linked List Random Node
class Solution {
    private ListNode head;
    private Random random;

    public Solution(ListNode head) {
        this.head = head;
        this.random = new Random();
    }

    /**
     * time = O(N)
     * space = O(1)
     */
    public int getRandom() {
        ListNode cur = head;
        int result = cur.val;
        int count = 1;

        // Process stream: for each element i, include with prob 1/i
        while (cur != null) {
            // Generate random in [0, count-1], replace if 0
            if (random.nextInt(count) == 0) {
                result = cur.val;
            }
            count++;
            cur = cur.next;
        }
        return result;
    }
}
```

```python
# Python implementation
import random

class Solution:
    def __init__(self, head: ListNode):
        self.head = head

    def getRandom(self) -> int:
        cur = self.head
        result = cur.val
        count = 1

        cur = cur.next
        while cur:
            count += 1
            # Include current element with probability 1/count
            if random.randint(1, count) == count:
                result = cur.val
            cur = cur.next

        return result
```

**Template 2: k Random Elements**

```java
// LC 398 - Random Pick Index (variant: pick from array with duplicates)
class Solution {
    private int[] nums;
    private Random random;

    public Solution(int[] nums) {
        this.nums = nums;
        this.random = new Random();
    }

    /**
     * time = O(N)
     * space = O(1)
     */
    public int pick(int target) {
        int result = -1;
        int count = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                count++;
                // Replace with probability 1/count
                if (random.nextInt(count) == 0) {
                    result = i;
                }
            }
        }
        return result;
    }
}
```

**Template 3: General k-Reservoir Sampling**

```java
class ReservoirSampling {
    private int k;
    private int[] reservoir;
    private Random random;
    private int count; // Elements seen so far

    public ReservoirSampling(int k) {
        this.k = k;
        this.reservoir = new int[k];
        this.random = new Random();
        this.count = 0;
    }

    /**
     * time = O(1) per element
     * space = O(k)
     */
    public void add(int val) {
        if (count < k) {
            // Fill reservoir first
            reservoir[count] = val;
        } else {
            // Generate random index in [0, count]
            int j = random.nextInt(count + 1);
            if (j < k) {
                // Replace element in reservoir
                reservoir[j] = val;
            }
        }
        count++;
    }

    public int[] getSample() {
        return Arrays.copyOf(reservoir, Math.min(k, count));
    }
}
```

```python
# Python k-reservoir sampling
import random

class ReservoirSampling:
    def __init__(self, k: int):
        self.k = k
        self.reservoir = []
        self.count = 0

    def add(self, val: int) -> None:
        """Time: O(1), Space: O(k)"""
        if self.count < self.k:
            self.reservoir.append(val)
        else:
            # Random index in [0, count]
            j = random.randint(0, self.count)
            if j < self.k:
                self.reservoir[j] = val
        self.count += 1

    def get_sample(self) -> list:
        return self.reservoir[:]
```

**Key Insight:** Probability math ensures uniform distribution
- Element i enters with probability k/i
- Stays with probability: (1 - k/(i+1)) × (1 - k/(i+2)) × ... × (1 - k/n)
- Final probability: k/i × i/(i+1) × (i+1)/(i+2) × ... × (n-1)/n = k/n ✓

---

### 1-2) Boyer-Moore Majority Vote (LC 169, 229)

**Algorithm Overview:**
- Find element(s) appearing more than ⌊n/k⌋ times
- **Key Idea**: Pair different elements and cancel them out
- Majority element will survive cancellation
- **Two-phase**: (1) Find candidates, (2) Verify counts

**Template 1: Standard Majority Element (> n/2)**

```java
// LC 169 - Majority Element
class Solution {
    /**
     * time = O(N)
     * space = O(1)
     */
    public int majorityElement(int[] nums) {
        int candidate = nums[0];
        int count = 1;

        // Phase 1: Find candidate
        for (int i = 1; i < nums.length; i++) {
            if (count == 0) {
                candidate = nums[i];
                count = 1;
            } else if (nums[i] == candidate) {
                count++;
            } else {
                count--; // Cancel out different element
            }
        }

        // Phase 2: Verify (not needed if majority guaranteed)
        // If not guaranteed, count candidate appearances
        return candidate;
    }
}
```

```python
# Python implementation
def majorityElement(nums: list[int]) -> int:
    """Time: O(n), Space: O(1)"""
    candidate = None
    count = 0

    # Phase 1: Find candidate
    for num in nums:
        if count == 0:
            candidate = num
            count = 1
        elif num == candidate:
            count += 1
        else:
            count -= 1

    return candidate
```

**Template 2: Generalized - Elements Appearing > n/3 Times (LC 229)**

```java
// LC 229 - Majority Element II (> n/3)
class Solution {
    /**
     * time = O(N)
     * space = O(1)
     */
    public List<Integer> majorityElement(int[] nums) {
        // At most 2 elements can appear > n/3 times
        int candidate1 = 0, candidate2 = 0;
        int count1 = 0, count2 = 0;

        // Phase 1: Find candidates (at most k-1 candidates for n/k)
        for (int num : nums) {
            if (num == candidate1) {
                count1++;
            } else if (num == candidate2) {
                count2++;
            } else if (count1 == 0) {
                candidate1 = num;
                count1 = 1;
            } else if (count2 == 0) {
                candidate2 = num;
                count2 = 1;
            } else {
                // Different from both: cancel out
                count1--;
                count2--;
            }
        }

        // Phase 2: Verify candidates
        count1 = 0;
        count2 = 0;
        for (int num : nums) {
            if (num == candidate1) count1++;
            else if (num == candidate2) count2++;
        }

        List<Integer> result = new ArrayList<>();
        if (count1 > nums.length / 3) result.add(candidate1);
        if (count2 > nums.length / 3) result.add(candidate2);

        return result;
    }
}
```

```python
# Python - Majority Element II
def majorityElement(nums: list[int]) -> list[int]:
    """Time: O(n), Space: O(1)"""
    # Phase 1: Find up to 2 candidates
    candidate1, candidate2 = None, None
    count1, count2 = 0, 0

    for num in nums:
        if num == candidate1:
            count1 += 1
        elif num == candidate2:
            count2 += 1
        elif count1 == 0:
            candidate1, count1 = num, 1
        elif count2 == 0:
            candidate2, count2 = num, 1
        else:
            count1 -= 1
            count2 -= 1

    # Phase 2: Verify
    result = []
    for candidate in [candidate1, candidate2]:
        if nums.count(candidate) > len(nums) // 3:
            result.append(candidate)

    return result
```

**Generalized Template: Top k-1 Elements (> n/k)**

```java
class BoyerMooreGeneralized {
    /**
     * Find elements appearing > n/k times
     * time = O(N * k)
     * space = O(k)
     */
    public List<Integer> majorityElement(int[] nums, int k) {
        // Can have at most k-1 elements appearing > n/k times
        Map<Integer, Integer> candidates = new HashMap<>();

        // Phase 1: Find candidates
        for (int num : nums) {
            if (candidates.containsKey(num)) {
                candidates.put(num, candidates.get(num) + 1);
            } else if (candidates.size() < k - 1) {
                candidates.put(num, 1);
            } else {
                // Decrement all counts (cancellation)
                List<Integer> toRemove = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : candidates.entrySet()) {
                    int count = entry.getValue() - 1;
                    if (count == 0) {
                        toRemove.add(entry.getKey());
                    } else {
                        candidates.put(entry.getKey(), count);
                    }
                }
                for (int key : toRemove) {
                    candidates.remove(key);
                }
            }
        }

        // Phase 2: Verify candidates
        Map<Integer, Integer> counts = new HashMap<>();
        for (int num : nums) {
            if (candidates.containsKey(num)) {
                counts.put(num, counts.getOrDefault(num, 0) + 1);
            }
        }

        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > nums.length / k) {
                result.add(entry.getKey());
            }
        }

        return result;
    }
}
```

---

### 1-3) Count-Min Sketch - Frequency Estimation

**Algorithm Overview:**
- Probabilistic data structure for frequency estimation
- Uses d hash functions and w counters per hash
- Query: return minimum count across all hash functions
- **Guarantee**: Overestimates by at most εn with probability 1-δ
- **Parameters**: w = ⌈e/ε⌉, d = ⌈ln(1/δ)⌉

**Template:**

```java
class CountMinSketch {
    private int[][] count; // d × w matrix
    private int d; // Number of hash functions
    private int w; // Width of each row
    private long[] hashA, hashB; // Hash parameters
    private static final long PRIME = 2147483647L; // Large prime

    /**
     * Constructor
     * epsilon: error tolerance (e.g., 0.01 for 1% error)
     * delta: failure probability (e.g., 0.01 for 99% confidence)
     */
    public CountMinSketch(double epsilon, double delta) {
        this.w = (int) Math.ceil(Math.E / epsilon);
        this.d = (int) Math.ceil(Math.log(1.0 / delta));
        this.count = new int[d][w];

        // Initialize hash functions: h(x) = ((a*x + b) % p) % w
        Random random = new Random();
        hashA = new long[d];
        hashB = new long[d];
        for (int i = 0; i < d; i++) {
            hashA[i] = random.nextInt(Integer.MAX_VALUE);
            hashB[i] = random.nextInt(Integer.MAX_VALUE);
        }
    }

    private int hash(int item, int i) {
        // Hash function i
        long hash = ((hashA[i] * item + hashB[i]) % PRIME) % w;
        return (int) hash;
    }

    /**
     * Add item to sketch
     * time = O(d)
     * space = O(1)
     */
    public void add(int item) {
        for (int i = 0; i < d; i++) {
            int index = hash(item, i);
            count[i][index]++;
        }
    }

    /**
     * Estimate frequency of item
     * time = O(d)
     * space = O(1)
     * Returns: estimated count (may overestimate, never underestimate)
     */
    public int estimateCount(int item) {
        int minCount = Integer.MAX_VALUE;
        for (int i = 0; i < d; i++) {
            int index = hash(item, i);
            minCount = Math.min(minCount, count[i][index]);
        }
        return minCount;
    }

    /**
     * Get space usage
     */
    public int getSpace() {
        return d * w; // Number of counters
    }
}
```

```python
# Python Count-Min Sketch
import math
import random

class CountMinSketch:
    def __init__(self, epsilon: float, delta: float):
        """
        epsilon: error tolerance
        delta: failure probability
        """
        self.w = math.ceil(math.e / epsilon)
        self.d = math.ceil(math.log(1.0 / delta))
        self.count = [[0] * self.w for _ in range(self.d)]

        # Initialize hash parameters
        PRIME = 2147483647
        self.hash_a = [random.randint(1, PRIME-1) for _ in range(self.d)]
        self.hash_b = [random.randint(0, PRIME-1) for _ in range(self.d)]
        self.PRIME = PRIME

    def _hash(self, item: int, i: int) -> int:
        """Hash function i"""
        return ((self.hash_a[i] * item + self.hash_b[i]) % self.PRIME) % self.w

    def add(self, item: int) -> None:
        """Time: O(d), Space: O(1)"""
        for i in range(self.d):
            index = self._hash(item, i)
            self.count[i][index] += 1

    def estimate_count(self, item: int) -> int:
        """Time: O(d), Returns estimated frequency"""
        return min(self.count[i][self._hash(item, i)] for i in range(self.d))
```

**Usage Example:**

```java
// Track top-k frequent elements in stream
CountMinSketch cms = new CountMinSketch(0.01, 0.01); // 1% error, 99% confidence

// Process stream
for (int item : stream) {
    cms.add(item);
}

// Query frequencies
int freq = cms.estimateCount(42); // Estimated frequency of item 42
```

---

### 1-4) Bloom Filter - Membership Testing

**Algorithm Overview:**
- Bit array of m bits, k hash functions
- **Insert**: Set k bits to 1 using k hash functions
- **Query**: Check if all k bits are 1
- **Guarantee**: No false negatives, false positive rate ≈ (1 - e^(-kn/m))^k
- **Optimal k**: (m/n) × ln(2)

**Template:**

```java
class BloomFilter {
    private BitSet bitSet;
    private int m; // Bit array size
    private int k; // Number of hash functions
    private long[] hashSeeds;

    /**
     * Constructor
     * expectedElements: expected number of elements
     * falsePositiveRate: desired false positive probability (e.g., 0.01)
     */
    public BloomFilter(int expectedElements, double falsePositiveRate) {
        // Optimal m: -(n * ln(p)) / (ln(2)^2)
        this.m = (int) Math.ceil(
            -(expectedElements * Math.log(falsePositiveRate)) / Math.pow(Math.log(2), 2)
        );

        // Optimal k: (m/n) * ln(2)
        this.k = (int) Math.ceil((m / (double) expectedElements) * Math.log(2));

        this.bitSet = new BitSet(m);

        // Initialize hash seeds
        Random random = new Random();
        hashSeeds = new long[k];
        for (int i = 0; i < k; i++) {
            hashSeeds[i] = random.nextLong();
        }
    }

    private int hash(String item, int i) {
        long hash = hashSeeds[i];
        for (char c : item.toCharArray()) {
            hash = hash * 31 + c;
        }
        return Math.abs((int) (hash % m));
    }

    /**
     * Add item to filter
     * time = O(k)
     * space = O(1)
     */
    public void add(String item) {
        for (int i = 0; i < k; i++) {
            int index = hash(item, i);
            bitSet.set(index);
        }
    }

    /**
     * Check if item might be in set
     * time = O(k)
     * space = O(1)
     * Returns: true if might exist (possible false positive)
     *          false if definitely not in set (no false negative)
     */
    public boolean mightContain(String item) {
        for (int i = 0; i < k; i++) {
            int index = hash(item, i);
            if (!bitSet.get(index)) {
                return false; // Definitely not in set
            }
        }
        return true; // Might be in set
    }

    /**
     * Get expected false positive rate
     */
    public double getFalsePositiveRate(int insertedElements) {
        return Math.pow(1 - Math.exp(-k * insertedElements / (double) m), k);
    }
}
```

```python
# Python Bloom Filter
import math
import mmh3  # MurmurHash3 (install: pip install mmh3)
from bitarray import bitarray  # (install: pip install bitarray)

class BloomFilter:
    def __init__(self, expected_elements: int, false_positive_rate: float):
        """
        expected_elements: expected number of items
        false_positive_rate: desired false positive probability
        """
        # Optimal m
        self.m = math.ceil(
            -(expected_elements * math.log(false_positive_rate)) / (math.log(2) ** 2)
        )

        # Optimal k
        self.k = math.ceil((self.m / expected_elements) * math.log(2))

        self.bit_array = bitarray(self.m)
        self.bit_array.setall(0)

    def add(self, item: str) -> None:
        """Time: O(k)"""
        for i in range(self.k):
            index = mmh3.hash(item, i) % self.m
            self.bit_array[index] = 1

    def might_contain(self, item: str) -> bool:
        """Time: O(k), False positives possible, no false negatives"""
        for i in range(self.k):
            index = mmh3.hash(item, i) % self.m
            if not self.bit_array[index]:
                return False  # Definitely not in set
        return True  # Might be in set
```

**Usage Example:**

```java
// Check if URL has been visited
BloomFilter bf = new BloomFilter(10000, 0.01); // 10k URLs, 1% false positive

// Add URLs
bf.add("https://example.com");
bf.add("https://google.com");

// Query
if (bf.mightContain("https://example.com")) {
    // Might be visited (99% confident)
}

if (!bf.mightContain("https://new-site.com")) {
    // Definitely NOT visited (100% sure)
}
```

---

## 2) LeetCode Problems by Pattern

### 2-1) Reservoir Sampling

| Problem | Difficulty | Pattern | Notes |
|---------|------------|---------|-------|
| LC 382 | Medium | Single random (k=1) | Linked list, unknown length |
| LC 398 | Medium | Weighted sampling | Array with duplicates |
| LC 528 | Medium | Weighted random | Prefix sum + binary search |
| LC 519 | Medium | Random 2D matrix | Reset and shuffle |

**LC 382 - Linked List Random Node**
- Unknown list length
- O(n) time per call, O(1) space
- Classic reservoir sampling k=1

**LC 398 - Random Pick Index**
- Multiple occurrences of target
- Uniform selection among duplicates
- Reservoir sampling variant

### 2-2) Boyer-Moore Majority Vote

| Problem | Difficulty | Threshold | Candidates |
|---------|------------|-----------|------------|
| LC 169 | Easy | > n/2 | 1 candidate |
| LC 229 | Medium | > n/3 | 2 candidates |
| - | - | > n/k | k-1 candidates |

**LC 169 - Majority Element**
- Guaranteed majority exists
- Single candidate suffices
- No verification phase needed

**LC 229 - Majority Element II**
- Up to 2 elements can appear > n/3 times
- Must verify candidates
- Two-phase algorithm essential

### 2-3) Count-Min Sketch (Conceptual)

**Applications (Not direct LC problems):**
- **Top K Frequent Elements** (LC 347) - Can use CMS + min-heap
- **Kth Largest Element in Stream** (LC 703) - Related streaming problem
- **Data Stream as Disjoint Intervals** (LC 352) - Stream processing

**When to Apply:**
- Large streams where HashMap exceeds memory
- Approximate answers acceptable
- Need frequency estimates, not exact counts

### 2-4) Bloom Filter (Conceptual)

**Applications (Not direct LC problems):**
- **Contains Duplicate** (LC 217) - Can use BF for space optimization
- **First Unique Character** (LC 387) - Variant: use BF as pre-filter
- **Design HashMap** (LC 706) - Can add BF layer for optimization

**When to Apply:**
- Membership queries on large sets
- False positives acceptable
- Space-critical applications (e.g., web crawlers)

### 2-5) Related Streaming Problems

| Problem | Difficulty | Pattern | Algorithm |
|---------|------------|---------|-----------|
| LC 295 | Hard | Data stream median | Two heaps |
| LC 346 | Easy | Moving average | Sliding window + queue |
| LC 352 | Hard | Disjoint intervals | TreeMap/BST |
| LC 703 | Easy | Kth largest in stream | Min-heap |

---

## 3) Common Mistakes & Edge Cases

### 🚫 Common Mistakes

#### Reservoir Sampling:
1. **Wrong Probability Calculation**
   ```java
   // ❌ WRONG: This is NOT uniform
   if (random.nextInt(i + 1) < k) { // Incorrect for k=1
       reservoir[...] = element;
   }

   // ✅ CORRECT: For k=1
   if (random.nextInt(count) == 0) {
       result = element;
   }
   ```

2. **Forgetting to Update Count**
   ```java
   // ❌ WRONG: Count not incremented
   while (cur != null) {
       if (random.nextInt(count) == 0) result = cur.val;
       cur = cur.next; // Forgot: count++
   }
   ```

3. **Off-by-One in Random Range**
   ```java
   // ❌ WRONG: random.nextInt(count + 1) for decision
   // ✅ CORRECT: random.nextInt(count) for k=1
   ```

#### Boyer-Moore:
1. **Missing Verification Phase**
   ```java
   // ❌ WRONG: Assuming candidate is always majority
   return candidate; // What if no majority exists?

   // ✅ CORRECT: Verify count
   int actualCount = 0;
   for (int num : nums) {
       if (num == candidate) actualCount++;
   }
   return actualCount > nums.length / 2 ? candidate : -1;
   ```

2. **Wrong Cancellation Logic**
   ```java
   // ❌ WRONG: Only decrement candidate's count
   if (num != candidate) count--;

   // ✅ CORRECT: Check count == 0 to reset
   if (count == 0) {
       candidate = num;
       count = 1;
   }
   ```

3. **For LC 229: Not Handling Same Candidates**
   ```java
   // ❌ WRONG: candidate1 and candidate2 can be same initially

   // ✅ CORRECT: Check candidate1 first, then candidate2
   if (num == candidate1) count1++;
   else if (num == candidate2) count2++;
   ```

#### Count-Min Sketch:
1. **Using Max Instead of Min**
   ```java
   // ❌ WRONG: Taking maximum (gives wrong estimate)
   int max = Arrays.stream(counts).max().getAsInt();

   // ✅ CORRECT: Taking minimum (minimizes overestimation)
   int min = Arrays.stream(counts).min().getAsInt();
   ```

2. **Insufficient Hash Functions**
   - Too few hash functions → high collision rate
   - Rule of thumb: d ≥ 3 for reasonable accuracy

#### Bloom Filter:
1. **Resetting Bits on Delete**
   ```java
   // ❌ WRONG: Bloom filters don't support deletion
   public void delete(String item) {
       for (int i = 0; i < k; i++) {
           bitSet.clear(hash(item, i)); // Breaks other items!
       }
   }

   // ✅ CORRECT: Use Counting Bloom Filter for deletions
   ```

2. **Ignoring False Positive Rate**
   - Too small bit array → high false positive rate
   - Calculate optimal m and k based on expected elements

### ⚠️ Edge Cases

1. **Empty Stream**
   - Reservoir: Return empty or throw exception
   - Boyer-Moore: No majority element
   - Bloom Filter: All queries return false

2. **Single Element**
   - Reservoir: Always return that element
   - Boyer-Moore: Single element is majority

3. **All Elements Same**
   - Boyer-Moore: Single candidate with count = n
   - Reservoir: Any element is representative

4. **Stream Size Unknown**
   - Reservoir Sampling handles this naturally
   - Count-Min Sketch: May need to adjust parameters dynamically

5. **Integer Overflow**
   - Count-Min Sketch: Use long for large counts
   - Hash functions: Use proper modulo arithmetic

---

## 4) Interview Tips & Complexity Analysis

### 💡 Interview Strategy

#### When to Use Streaming Algorithms:

**Recognition Patterns:**
1. **"Process data stream"** → Streaming algorithm
2. **"Limited memory"** or **"O(1) space"** → Streaming
3. **"Unknown size"** or **"infinite stream"** → Reservoir Sampling
4. **"Majority element"** → Boyer-Moore
5. **"Approximate count/frequency"** → Count-Min Sketch
6. **"Have you seen before?"** → Bloom Filter

#### Problem-Solving Framework:

```
1. Identify constraint: Memory vs Accuracy
   ├─ Exact answer needed? → Reservoir Sampling or Boyer-Moore
   └─ Approximate OK? → Count-Min Sketch or Bloom Filter

2. Determine stream characteristics:
   ├─ Need random samples? → Reservoir Sampling
   ├─ Need heavy hitters? → Boyer-Moore
   ├─ Need frequency estimates? → Count-Min Sketch
   └─ Need membership tests? → Bloom Filter

3. Consider space-time tradeoff:
   ├─ More space → Better accuracy
   └─ Less space → More approximation error
```

### 📊 Complexity Analysis

| Algorithm | Time (per element) | Space | Accuracy |
|-----------|-------------------|-------|----------|
| Reservoir Sampling | O(1) | O(k) | Exact |
| Boyer-Moore | O(1) | O(k) | Exact (if exists) |
| Count-Min Sketch | O(d) | O(w × d) | ε-approximate |
| Bloom Filter | O(k) | O(m) bits | False positives |

**Space Comparisons:**
- **HashMap**: O(n) - exact counts for all elements
- **Count-Min Sketch**: O(w × d) where w × d << n
- **Bloom Filter**: O(m) bits, m = O(n log(1/p))

**Typical Parameters:**
- **Count-Min Sketch**: w = 1000, d = 5 → 5KB for millions of elements
- **Bloom Filter**: 10 bits per element for 1% false positive rate

### 🎯 Interview Talking Points

1. **Why Reservoir Sampling Works:**
   - "Each element has equal probability k/n of being selected"
   - "Prove by induction: P(element i selected) = k/i × (i/(i+1)) × ... × (n-1)/n = k/n"

2. **Boyer-Moore Intuition:**
   - "Pair up different elements and cancel them"
   - "Majority element survives cancellation"
   - "At most k-1 elements can appear more than n/k times"

3. **Count-Min Sketch Trade-off:**
   - "Trade space for accuracy: w and d control error bounds"
   - "Never underestimates (only overestimates due to collisions)"
   - "Minimum across hash functions reduces overestimation"

4. **Bloom Filter Properties:**
   - "False positives possible, false negatives impossible"
   - "Can't delete items (unless using Counting Bloom Filter)"
   - "Space-efficient: ~10 bits per element for 1% FP rate"

### 🔧 Optimization Techniques

1. **Reservoir Sampling:**
   - Use `random.nextInt(count++)` idiom for cleaner code
   - For weighted sampling: Use prefix sum + binary search

2. **Boyer-Moore:**
   - For k=2 (majority > n/2): Skip verification if guaranteed
   - For k=3 (> n/3): Track exactly 2 candidates
   - For general k: Use HashMap with size k-1

3. **Count-Min Sketch:**
   - Conservative Update: Only increment minimum count
   - Use faster hash functions (MurmurHash, CityHash)
   - Adjust w and d based on accuracy requirements

4. **Bloom Filter:**
   - Choose hash functions that minimize correlation
   - Consider Counting Bloom Filter for deletions
   - Use bit-level operations for space efficiency

### 📚 Related Topics

- **Sampling**: Stratified sampling, importance sampling
- **Approximate Algorithms**: HyperLogLog (cardinality estimation)
- **Probabilistic**: Skip lists, treaps
- **Streaming**: Misra-Gries algorithm, Space Saving algorithm
- **Online Algorithms**: Competitive analysis, adversarial models

---

## 5) Complete Code Examples

### Example 1: Weighted Reservoir Sampling (LC 528)

```java
// LC 528 - Random Pick with Weight
class Solution {
    private int[] prefixSum;
    private Random random;

    /**
     * time = O(N) for constructor
     * space = O(N)
     */
    public Solution(int[] w) {
        prefixSum = new int[w.length];
        prefixSum[0] = w[0];
        for (int i = 1; i < w.length; i++) {
            prefixSum[i] = prefixSum[i - 1] + w[i];
        }
        random = new Random();
    }

    /**
     * time = O(log N) using binary search
     * space = O(1)
     */
    public int pickIndex() {
        int target = random.nextInt(prefixSum[prefixSum.length - 1]) + 1;
        // Binary search for target in prefixSum
        int left = 0, right = prefixSum.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (prefixSum[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
```

### Example 2: Stream Median (Related Problem LC 295)

```java
// LC 295 - Find Median from Data Stream
class MedianFinder {
    private PriorityQueue<Integer> maxHeap; // Lower half
    private PriorityQueue<Integer> minHeap; // Upper half

    public MedianFinder() {
        maxHeap = new PriorityQueue<>((a, b) -> b - a);
        minHeap = new PriorityQueue<>();
    }

    /**
     * time = O(log N)
     * space = O(N)
     */
    public void addNum(int num) {
        // Add to max heap first
        maxHeap.offer(num);

        // Balance: move largest from max to min
        minHeap.offer(maxHeap.poll());

        // Maintain size property: maxHeap.size >= minHeap.size
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }
}
```

---

## Summary

**Core Streaming Algorithms:**
1. ✅ **Reservoir Sampling** - Uniform random sampling from stream
2. ✅ **Boyer-Moore** - Find majority/heavy hitters (exact)
3. ✅ **Count-Min Sketch** - Frequency estimation (approximate)
4. ✅ **Bloom Filter** - Membership testing (probabilistic)

**Key Takeaways:**
- Streaming algorithms trade accuracy for space efficiency
- Reservoir Sampling: Exact, O(k) space for k samples
- Boyer-Moore: Exact for heavy hitters, O(k) space
- Count-Min Sketch: Approximate, tunable error bounds
- Bloom Filter: No false negatives, space-efficient

**Interview Focus:**
- Understand probability proofs (especially Reservoir Sampling)
- Know when to use each algorithm
- Master two-phase verification (Boyer-Moore)
- Explain space-accuracy tradeoffs

**Practice Problems:**
- Start with LC 382, 398 (Reservoir Sampling)
- Master LC 169, 229 (Boyer-Moore)
- Understand LC 295, 703 (Stream + Heap)
- Study hash-based designs (LC 706, 705)
