# 串流演算法

> **範圍** — 被限制在單趟掃描與次線性記憶體之下的演算法 —— 蓄水池抽樣、Boyer-Moore 多數投票、加權隨機挑選，以及近似計數。
> **另見**：[heap.md](./heap.md) — 串流上的 top-k；[design.md](./design.md) — 包裝成類別的串流題；[hashing.md](./hashing.md) — 機率型計數器背後的雜湊。

- 在記憶體有限的前提下處理資料串流
- 什麼時候用：資料大到存不下、線上處理、隨機抽樣、頻率估計
- 代表性 LeetCode 題目：LC 382、398（蓄水池抽樣）、LC 169、229（Majority Element）、LC 528（Random Pick with Weight）
- 核心技巧：蓄水池抽樣、機率型資料結構、近似計數
- 時空權衡：拿準確度換記憶體

**時間複雜度：** 每處理一個元素 O(1)（串流）
**空間複雜度：** 通常是 O(k) 或 O(log n)，與串流長度無關

## LeetCode 題目清單

- [Data Stream](https://leetcode.com/problem-list/data-stream/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)
- [Reservoir Sampling](https://leetcode.com/problem-list/reservoir-sampling/)

## 0) 概念

### 0-0) 為什麼需要串流演算法？

**問題：** 傳統演算法預設整份資料放得進記憶體
- **串流情境**：資料源源不絕地進來，大到存不下
- **記憶體限制**：空間有限，每個元素只能處理一次
- **要求**：在資訊不完整的情況下做決定

**關鍵特徵：**
- **單趟**：每個元素最多處理一次
- **次線性空間**：記憶體 << 資料量
- **近似結果**：拿精確度換效率（蓄水池抽樣除外）
- **線上處理**：任何時刻都拿得到目前的結果

### 0-1) 種類

#### 1. **蓄水池抽樣（Reservoir Sampling）** - 均勻隨機抽樣
- **用途**：從長度未知的串流中挑出 k 個隨機元素
- **保證**：每個元素被選中的機率剛好是 k/n
- **空間**：k 個樣本需要 O(k)
- **應用**：隨機抽樣、洗牌、選取的公平性

#### 2. **Morris Counter** - 近似計數
- **用途**：用很小的記憶體數很大的數字
- **空間**：O(log log n)，而不是 O(log n)
- **權衡**：計數是近似的，期望值為 n
- **應用**：大規模計數、記憶體受限的系統

#### 3. **Boyer-Moore 多數投票** - Heavy Hitters
- **用途**：找出出現次數 > n/k 的元素
- **空間**：找 top-k 需要 O(k)
- **應用**：多數元素、出現頻率前 k 名
- **變體**：標準版（k=2）、廣義版（任意 k）

#### 4. **Count-Min Sketch** - 頻率估計
- **用途**：估計串流中各元素的出現頻率
- **空間**：O(w × d) 的雜湊表，w×d << n
- **保證**：以 1-δ 的機率，高估不超過 εn
- **應用**：網路流量分析、查詢頻率

#### 5. **Bloom Filter** - 成員測試
- **用途**：測試某元素是否在集合中（機率型）
- **空間**：O(m) 個位元，m << n
- **保證**：不會有偽陰性，偽陽性機率為 p
- **應用**：快取過濾、拼字檢查、惡意程式偵測

### 0-2) 各演算法的使用時機

| 演算法 | 適用情境 | 空間 | 準確度 | 輸出 |
|-----------|----------|-------|----------|--------|
| Reservoir Sampling | 隨機 k 個樣本 | O(k) | 精確 | k 個元素 |
| Morris Counter | 數串流長度 | O(log log n) | 近似 | ~計數 |
| Boyer-Moore | 多數元素 | O(k) | 存在的話就精確 | 候選者 |
| Count-Min Sketch | 頻率估計 | O(w×d) | 近似 | ~頻率 |
| Bloom Filter | 集合成員測試 | O(m) | 會有偽陽性 | yes/no |

**辨識關鍵字：**
- 「random sample」、「shuffle」、「fairness」 → **蓄水池抽樣**
- 「majority element」、「appears more than n/k times」 → **Boyer-Moore**
- 「estimate frequency」、「approximate count」 → **Count-Min Sketch**
- 「check membership」、「seen before?」 → **Bloom Filter**
- 「limited memory」、「one pass」、「streaming data」 → **任何一種串流演算法**

---

## 1) 演算法模板

### 1-1) 蓄水池抽樣（LC 382, 398）

**演算法概述：**
- 維護一個裝 k 個元素的蓄水池
- 對索引 i 的元素（i ≥ k）：
  - 產生隨機數 j ∈ [0, i]
  - 若 j < k，就用當前元素取代 reservoir[j]
- **證明**：索引 i 的元素有 k/i 的機率進池，之後每步有 k/(i+1) 的機率被換掉 → 最終機率剛好 k/n

**模板 1：只取一個隨機元素（k=1）**

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

**模板 2：取 k 個隨機元素**

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

**模板 3：通用的 k-蓄水池抽樣**

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

**關鍵洞見：** 機率算式保證了均勻分布
- 元素 i 以 k/i 的機率進池
- 留下來的機率：(1 - k/(i+1)) × (1 - k/(i+2)) × ... × (1 - k/n)
- 最終機率：k/i × i/(i+1) × (i+1)/(i+2) × ... × (n-1)/n = k/n ✓

---

### 1-2) Boyer-Moore 多數投票（LC 169, 229）

**演算法概述：**
- 找出出現次數超過 ⌊n/k⌋ 的元素
- **核心想法**：把不同的元素兩兩配對抵銷
- 多數元素一定能撐過抵銷
- **兩階段**：(1) 找候選者，(2) 驗證次數

**模板 1：標準多數元素（> n/2）**

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

**模板 2：廣義版 —— 出現次數 > n/3 的元素（LC 229）**

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

**廣義模板：前 k-1 名元素（> n/k）**

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

### 1-3) Count-Min Sketch —— 頻率估計

**演算法概述：**
- 用來估計頻率的機率型資料結構
- 使用 d 個雜湊函數，每個雜湊配 w 個計數器
- 查詢：回傳所有雜湊函數中最小的那個計數
- **保證**：以 1-δ 的機率，高估不超過 εn
- **參數**：w = ⌈e/ε⌉，d = ⌈ln(1/δ)⌉

**模板：**

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

**使用範例：**

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

### 1-4) Bloom Filter —— 成員測試

**演算法概述：**
- 一個 m 位元的位元陣列，加上 k 個雜湊函數
- **插入**：用 k 個雜湊函數把 k 個位元設成 1
- **查詢**：檢查那 k 個位元是不是全為 1
- **保證**：不會有偽陰性，偽陽性率 ≈ (1 - e^(-kn/m))^k
- **最佳 k**：(m/n) × ln(2)

**模板：**

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

**使用範例：**

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

### 1-5) 串流上的有界 Top-K 堆積（LC 692, 703）⭐⭐⭐⭐⭐

**模式：** 串流沒有上限，但*答案*只有 k 個元素 —— 所以就維護一個**大小為 k 的堆積，滿了就踢掉**。記憶體是 O(k)，與串流長度無關。

**核心想法：** 堆積的方向要跟目標**相反**：
- 想要**最大的 k 個** → 用**最小堆積**，`size > k` 時彈掉最小的（根就是第 k 大，O(1) 就答得出來）
- 想要**最常出現的 k 個** → 用一個「最差的在頂端」的堆積，`size > k` 時彈掉最差的

**平手陷阱（LC 692）：** 排名規則是次數**遞減**、但單字**遞增**。因為堆積是從根彈出的，比較子必須把*輸家*放在頂端：次數小的優先，次數相同時把**字典序較大**的放上去。

| 目標 | 堆積型態 | 何時踢掉 | 根是什麼 |
|------|-----------|-----------|------------|
| 最大的 k 個值 | 最小堆積 | `size > k` | 第 k 大（LC 703, 215） |
| 最常出現的 k 個 | 最差在頂的堆積 | `size > k` | 目前倖存者中最差的那個（LC 692） |
| 動態中位數 | 兩個堆積 | 每次加入之後 | 見 `priority_queue.md` / `heap.md`（LC 295, 480） |

```java
// java
// LC 692 - Top K Frequent Words
// IDEA: count in a map, then stream keys through a size-k "worst-on-top" heap.
//       Comparator inverted vs the ranking so poll() always drops the loser.
class TopKFrequentWords {

    private final Map<String, Integer> freq = new HashMap<>();

    /**
     * time = O(1) amortized per element
     * space = O(D) distinct words
     */
    public void offer(String word) {
        freq.put(word, freq.getOrDefault(word, 0) + 1);
    }

    /**
     * time = O(D log K)
     * space = O(K)
     */
    public List<String> topK(int k) {
        // Worst on top: lower count first; on tie, lexicographically LARGER first
        PriorityQueue<String> pq = new PriorityQueue<>(
            (a, b) -> freq.get(a).equals(freq.get(b))
                    ? b.compareTo(a)
                    : freq.get(a) - freq.get(b));

        for (String w : freq.keySet()) {
            pq.offer(w);
            if (pq.size() > k) {
                pq.poll(); // evict the worst -> heap never exceeds k
            }
        }

        // Heap pops worst-first, so build the answer back to front
        LinkedList<String> res = new LinkedList<>();
        while (!pq.isEmpty()) {
            res.addFirst(pq.poll());
        }
        return res;
    }
}
```

```python
# python
# LC 692 - Top K Frequent Words
# IDEA: heapq.nsmallest IS a bounded size-k heap internally; the key encodes
#       the mixed sort direction (count descending, word ascending).
import heapq
from collections import Counter

class TopKFrequentWords:
    def __init__(self):
        self.freq = Counter()

    def offer(self, word: str) -> None:
        """time = O(1) amortized, space = O(D) distinct words"""
        self.freq[word] += 1

    def top_k(self, k: int) -> list:
        """time = O(D log K), space = O(K)"""
        return [w for w, c in heapq.nsmallest(
            k, self.freq.items(), key=lambda x: (-x[1], x[0]))]
```

**變形 —— 每次資料到達後都要回答第 k 大（LC 703）。** 一樣是有界堆積，但根*本身*就是答案，所以最後不用再倒出來。可以跟 LC 215（`Kth Largest Element in an Array`）與 LC 973（`K Closest Points to Origin`）對照：堆積完全一樣，只是輸入是一次給完而不是串流。

```java
// java
// LC 703 - Kth Largest Element in a Stream
// IDEA: min-heap capped at k -> root is always the k-th largest so far.
class KthLargest {

    private final PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    private final int k;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        for (int n : nums) add(n);
    }

    /**
     * time = O(log K) per arrival
     * space = O(K)  <- NOT O(N): the stream can be infinite
     */
    public int add(int val) {
        minHeap.offer(val);
        if (minHeap.size() > k) {
            minHeap.poll(); // drop the smallest; only the top k survive
        }
        return minHeap.peek();
    }
}
```

```python
# python
# LC 703 - Kth Largest Element in a Stream
import heapq

class KthLargest:
    def __init__(self, k: int, nums: list):
        self.k = k
        self.heap = []
        for n in nums:
            self.add(n)

    def add(self, val: int) -> int:
        """time = O(log K) per arrival, space = O(K)"""
        heapq.heappush(self.heap, val)
        if len(self.heap) > self.k:
            heapq.heappop(self.heap)
        return self.heap[0]
```

> **交叉參考：** 動態中位數（兩個堆積）與延遲刪除的堆積都放在 `priority_queue.md` / `heap.md`；這裡的串流視角只強調一件事 ——「記憶體有界為 k，n 無上限」。

---

### 1-6) 單調雙端佇列 —— 串流上的視窗極值（LC 239）⭐⭐⭐⭐⭐

**模式：** 答案只跟**最近 k 筆到達的資料**有關，所以即使串流無限長，記憶體也必須是 O(k)。用堆積的話每次要 O(log k)，*而且*還得靠延遲刪除來處理過期；換成**單調雙端佇列**，攤銷是 **O(1)**，過期處理也乾淨俐落。

**核心想法：** 存的是**索引**，並讓對應的值保持**單調遞減**（求最大值時）。
1. **從前端剔除過期的** —— `dq[0] <= i - k` 就表示它已經滑出視窗了。
2. **從後端剔除被壓制的** —— 新值 `>=` 後端的值，代表只要新值還在，後端那個就再也當不成最大值了（它又比較小、又比較老）。彈掉。
3. `dq[0]` 永遠是視窗最大值。

每個索引只會被推入一次、彈出一次 → **每個元素攤銷 O(1)**。

```text
nums = [1,3,-1,-3,5,3,6,7], k = 3     (deque holds values, front = max)

i=0 v=1  : [1]                        window not full
i=1 v=3  : 3 dominates 1 -> [3]       window not full
i=2 v=-1 : [3,-1]                     -> max 3
i=3 v=-3 : [3,-1,-3]                  -> max 3
i=4 v=5  : 5 dominates all -> [5]     -> max 5
i=5 v=3  : [5,3]                      -> max 5
i=6 v=6  : 6 dominates -> [6]         -> max 6
i=7 v=7  : 7 dominates -> [7]         -> max 7
```

```java
// java
// LC 239 - Sliding Window Maximum
// IDEA: deque of INDICES with decreasing values; front = window max.
class Solution {
    /**
     * time = O(N) amortized (each index enters/leaves once)
     * space = O(K)
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] res = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>(); // indices, values decreasing

        for (int i = 0; i < n; i++) {
            // 1) expire: front index slid out of the window
            if (!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.pollFirst();
            }
            // 2) dominate: back values <= nums[i] can never win again
            while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i]) {
                dq.pollLast();
            }
            dq.offerLast(i);

            // 3) front is the max of the current window
            if (i >= k - 1) {
                res[i - k + 1] = nums[dq.peekFirst()];
            }
        }
        return res;
    }
}
```

```python
# python
# LC 239 - Sliding Window Maximum
# IDEA: monotonic decreasing deque of indices; dq[0] is the window max.
from collections import deque

def maxSlidingWindow(nums: list, k: int) -> list:
    """time = O(N) amortized, space = O(K)"""
    res, dq = [], deque()  # indices, values decreasing

    for i, v in enumerate(nums):
        if dq and dq[0] <= i - k:          # expire from front
            dq.popleft()
        while dq and nums[dq[-1]] <= v:    # dominate from back
            dq.pop()
        dq.append(i)
        if i >= k - 1:
            res.append(nums[dq[0]])

    return res
```

**變形 —— 視窗大小不固定，改由*條件*界定（LC 1438）。** 開**兩個**雙端佇列（最大與最小），並不斷收縮 `left` 直到 `max - min <= limit`。過期規則一樣，只是驅動過期的是限制條件而不是 `k`。

```java
// java
// LC 1438 - Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit
// IDEA: two monotonic deques track window max & min; shrink left while max-min > limit.
class Solution {
    /**
     * time = O(N) amortized
     * space = O(N) worst case for the two deques
     */
    public int longestSubarray(int[] nums, int limit) {
        Deque<Integer> maxD = new ArrayDeque<>(); // decreasing
        Deque<Integer> minD = new ArrayDeque<>(); // increasing
        int left = 0, best = 0;

        for (int right = 0; right < nums.length; right++) {
            while (!maxD.isEmpty() && nums[maxD.peekLast()] <= nums[right]) maxD.pollLast();
            maxD.offerLast(right);
            while (!minD.isEmpty() && nums[minD.peekLast()] >= nums[right]) minD.pollLast();
            minD.offerLast(right);

            // constraint broken -> advance left, evicting whichever deque head leaves
            while (nums[maxD.peekFirst()] - nums[minD.peekFirst()] > limit) {
                if (maxD.peekFirst() == left) maxD.pollFirst();
                if (minD.peekFirst() == left) minD.pollFirst();
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }
}
```

```python
# python
# LC 1438 - Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit
from collections import deque

def longestSubarray(nums: list, limit: int) -> int:
    """time = O(N) amortized, space = O(N)"""
    max_d, min_d = deque(), deque()
    left = best = 0

    for right, v in enumerate(nums):
        while max_d and nums[max_d[-1]] <= v:
            max_d.pop()
        max_d.append(right)
        while min_d and nums[min_d[-1]] >= v:
            min_d.pop()
        min_d.append(right)

        while nums[max_d[0]] - nums[min_d[0]] > limit:
            if max_d[0] == left:
                max_d.popleft()
            if min_d[0] == left:
                min_d.popleft()
            left += 1

        best = max(best, right - left + 1)

    return best
```

---

### 1-7) 單調堆疊 —— 每筆到達即時查詢（LC 901）⭐⭐⭐⭐

**模式：** 每筆資料一進來就要立刻回答「它的優勢能往回延伸多遠」（線上版的「前一個更大元素」）。天真地回頭重掃歷史，每次呼叫是 O(n)；改用**會把被吞掉的項目合併起來的單調堆疊**，就能攤銷 O(1)。

**核心想法：** 堆疊裡放 `(price, span)` 這種配對，價格**嚴格遞減**。
- 當新價格吞掉堆疊頂端（`top.price <= price`），那一項就**永遠消失了** —— 以後任何贏得過新價格的價格，不可能贏不過它。
- 但不要直接丟掉它，而是把它的 span **吸收**進新的項目裡。被彈掉的項目是被*壓縮*了、不是不見了，這就是為什麼記憶體維持得住，而且每個元素只會被推入／彈出一次。

```text
prices  : 100  80  60  70  60  75  85
stack   :(100,1)
              (80,1)
                  (60,1)
                  ->70 swallows (60,1)   => (70,2)
                        (60,1)
                  ->75 swallows (60,1),(70,2) => (75,4)
             ->85 swallows (75,4),(80,1) => (85,6)
spans   :  1   1   1   2   1   4   6
```

```java
// java
// LC 901 - Online Stock Span
// IDEA: monotonic decreasing stack of (price, span); a new price absorbs the
//       spans of every entry it dominates -> amortized O(1) per query.
class StockSpanner {

    private final Deque<int[]> stack = new ArrayDeque<>(); // {price, span}

    public StockSpanner() {}

    /**
     * time = O(1) amortized (each price pushed once, popped once)
     * space = O(N) worst case (strictly decreasing stream), O(1) if increasing
     */
    public int next(int price) {
        int span = 1;
        while (!stack.isEmpty() && stack.peek()[0] <= price) {
            span += stack.pop()[1]; // absorb the swallowed entry's span
        }
        stack.push(new int[]{price, span});
        return span;
    }
}
```

```python
# python
# LC 901 - Online Stock Span
# IDEA: stack of (price, span), decreasing; absorb spans of dominated entries.
class StockSpanner:
    def __init__(self):
        self.stack = []  # (price, span)

    def next(self, price: int) -> int:
        """time = O(1) amortized, space = O(N) worst case"""
        span = 1
        while self.stack and self.stack[-1][0] <= price:
            span += self.stack.pop()[1]
        self.stack.append((price, span))
        return span
```

---

### 1-8) 反向 Trie —— 串流上的後綴比對（LC 1032）⭐⭐⭐⭐

**模式：** 字元一個一個進來；每進來一個，就要回答*「有沒有任何字典裡的單字剛好在這裡結束？」*。這是對串流做**後綴**查詢，而串流是沒有上限的。

**核心想法：** 兩個動作就能在記憶體有界的前提下做到每次查詢 O(L)：
1. **把每個單字反過來插進 Trie。** 這樣從最新的字元往**回**走串流，就變成一般的由根往下走 Trie。
2. **把緩衝區上限壓在 `maxLen`**（最長單字的長度）。更舊的字元不可能再湊出任何單字，直接丟掉 —— 記憶體是 O(maxLen)，*不是* O(串流長度)。

往回走還有一個好處：可以**提早收工** —— 第一次找不到子節點，就代表不可能有單字比對成功，於是直接停手，不必掃完整個緩衝區。

```java
// java
// LC 1032 - Stream of Characters
// IDEA: trie of REVERSED words + deque capped at maxLen; walk newest -> oldest.
class StreamChecker {

    static class Node {
        Node[] next = new Node[26];
        boolean isWord = false;
    }

    private final Node root = new Node();
    private final Deque<Character> buf = new ArrayDeque<>();
    private int maxLen = 0;

    /**
     * time = O(total chars in words)
     * space = O(total chars in words)
     */
    public StreamChecker(String[] words) {
        for (String w : words) {
            maxLen = Math.max(maxLen, w.length());
            Node cur = root;
            for (int i = w.length() - 1; i >= 0; i--) { // insert REVERSED
                int c = w.charAt(i) - 'a';
                if (cur.next[c] == null) cur.next[c] = new Node();
                cur = cur.next[c];
            }
            cur.isWord = true;
        }
    }

    /**
     * time = O(maxLen) per char, with early bail-out
     * space = O(maxLen) buffer  <- independent of stream length
     */
    public boolean query(char letter) {
        buf.addFirst(letter);
        if (buf.size() > maxLen) {
            buf.removeLast(); // too old to ever complete a word
        }

        Node cur = root;
        for (char c : buf) { // newest -> oldest
            cur = cur.next[c - 'a'];
            if (cur == null) return false; // no word can match: stop early
            if (cur.isWord) return true;
        }
        return false;
    }
}
```

```python
# python
# LC 1032 - Stream of Characters
# IDEA: dict-based trie of reversed words + deque capped at max word length.
from collections import deque

class StreamChecker:
    def __init__(self, words: list):
        """time = O(total chars), space = O(total chars)"""
        self.root = {}
        self.max_len = 0
        for w in words:
            self.max_len = max(self.max_len, len(w))
            node = self.root
            for ch in reversed(w):          # insert REVERSED
                node = node.setdefault(ch, {})
            node["$"] = True                # word-end marker
        self.buf = deque()

    def query(self, letter: str) -> bool:
        """time = O(maxLen) per char, space = O(maxLen)"""
        self.buf.appendleft(letter)
        if len(self.buf) > self.max_len:
            self.buf.pop()

        node = self.root
        for ch in self.buf:                 # newest -> oldest
            if ch not in node:
                return False
            node = node[ch]
            if "$" in node:
                return True
        return False
```

---

### 1-9) 其他串流設計題參考

一些著名的「在串流上做設計」題目，都是重複套用上面的模板 —— 沒有新技巧，列出來只是為了認得出它們：

| 題目 | 難度 | 保存的串流狀態 | 備註 |
|---------|------------|-------------------|------|
| LC 1352 | Medium | 累積的前綴乘積串列 | `getProduct(k)` = `prefix[-1] / prefix[-1-k]`；**遇到 `0` 就把串列清空**，確保任何存起來的乘積裡都不含 0 |
| LC 2013 | Medium | 記錄所有已加入點的 `Map<Point, count>` | 每次查詢就掃過已存的點找對角伙伴，再把另外兩個角的計數相乘 |
| LC 480 | Hard | 兩個堆積 + 延遲刪除 | LC 295 的視窗版 —— 見 `priority_queue.md` / `heap.md` |
| LC 355 | Medium | 每個使用者的推文串列 + k 路合併堆積 | 合併 k 條有序串流 —— 見 `design.md` |

---

## 2) 依模式分類的 LeetCode 題目

### 2-1) 蓄水池抽樣

| 題目 | 難度 | 模式 | 備註 |
|---------|------------|---------|-------|
| LC 382 | Medium | 單一隨機（k=1） | 鏈結串列，長度未知 |
| LC 398 | Medium | 加權抽樣 | 含重複值的陣列 |
| LC 528 | Medium | 加權隨機 | 前綴和 + 二分搜尋 |
| LC 519 | Medium | 隨機二維矩陣 | 重設與洗牌 |

**LC 382 - Linked List Random Node**
- 串列長度未知
- 每次呼叫 O(n) 時間、O(1) 空間
- 經典的 k=1 蓄水池抽樣

**LC 398 - Random Pick Index**
- 目標值出現多次
- 要在重複值之間均勻挑選
- 蓄水池抽樣的變形

### 2-2) Boyer-Moore 多數投票

| 題目 | 難度 | 門檻 | 候選者數 |
|---------|------------|-----------|------------|
| LC 169 | Easy | > n/2 | 1 個候選者 |
| LC 229 | Medium | > n/3 | 2 個候選者 |
| - | - | > n/k | k-1 個候選者 |

**LC 169 - Majority Element**
- 題目保證多數元素存在
- 一個候選者就夠了
- 不需要驗證階段

**LC 229 - Majority Element II**
- 最多有 2 個元素能出現超過 n/3 次
- 必須驗證候選者
- 兩階段演算法不可省

### 2-3) Count-Min Sketch（概念）

**應用（不是直接的 LC 題目）：**
- **Top K Frequent Elements**（LC 347）- 可以用 CMS + 最小堆積
- **Kth Largest Element in Stream**（LC 703）- 相關的串流題
- **Data Stream as Disjoint Intervals**（LC 352）- 串流處理

**什麼時候用：**
- 串流大到 HashMap 塞不下記憶體
- 可以接受近似答案
- 只需要頻率估計，不需要精確計數

### 2-4) Bloom Filter（概念）

**應用（不是直接的 LC 題目）：**
- **Contains Duplicate**（LC 217）- 可以用 BF 做空間最佳化
- **First Unique Character**（LC 387）- 變形：把 BF 當前置過濾器
- **Design HashMap**（LC 706）- 可以加一層 BF 做最佳化

**什麼時候用：**
- 在大型集合上做成員查詢
- 可以接受偽陽性
- 空間極度吃緊的場景（例如網路爬蟲）

### 2-5) 其他相關的串流題

| 題目 | 難度 | 模式 | 演算法 |
|---------|------------|---------|-----------|
| LC 295 | Hard | 資料串流中位數 | 兩個堆積 |
| LC 346 | Easy | 移動平均 | 滑動視窗 + 佇列 |
| LC 352 | Hard | 不相交區間 | TreeMap／BST |
| LC 703 | Easy | 串流中的第 k 大 | 最小堆積 |

---

## 3) 常見錯誤與邊界情況

### 🚫 常見錯誤

#### 蓄水池抽樣：
1. **機率算錯**
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

2. **忘了更新計數**
   ```java
   // ❌ WRONG: Count not incremented
   while (cur != null) {
       if (random.nextInt(count) == 0) result = cur.val;
       cur = cur.next; // Forgot: count++
   }
   ```

3. **隨機範圍差一**
   ```java
   // ❌ WRONG: random.nextInt(count + 1) for decision
   // ✅ CORRECT: random.nextInt(count) for k=1
   ```

#### Boyer-Moore：
1. **少了驗證階段**
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

2. **抵銷邏輯寫錯**
   ```java
   // ❌ WRONG: Only decrement candidate's count
   if (num != candidate) count--;

   // ✅ CORRECT: Check count == 0 to reset
   if (count == 0) {
       candidate = num;
       count = 1;
   }
   ```

3. **LC 229：沒處理候選者相同的情況**
   ```java
   // ❌ WRONG: candidate1 and candidate2 can be same initially

   // ✅ CORRECT: Check candidate1 first, then candidate2
   if (num == candidate1) count1++;
   else if (num == candidate2) count2++;
   ```

#### Count-Min Sketch：
1. **用了 max 而不是 min**
   ```java
   // ❌ WRONG: Taking maximum (gives wrong estimate)
   int max = Arrays.stream(counts).max().getAsInt();

   // ✅ CORRECT: Taking minimum (minimizes overestimation)
   int min = Arrays.stream(counts).min().getAsInt();
   ```

2. **雜湊函數數量不足**
   - 雜湊函數太少 → 碰撞率高
   - 經驗法則：d ≥ 3 才有像樣的準確度

#### Bloom Filter：
1. **刪除時把位元清掉**
   ```java
   // ❌ WRONG: Bloom filters don't support deletion
   public void delete(String item) {
       for (int i = 0; i < k; i++) {
           bitSet.clear(hash(item, i)); // Breaks other items!
       }
   }

   // ✅ CORRECT: Use Counting Bloom Filter for deletions
   ```

2. **忽略偽陽性率**
   - 位元陣列太小 → 偽陽性率高
   - 依預期元素數量算出最佳的 m 與 k

### ⚠️ 邊界情況

1. **空串流**
   - 蓄水池：回傳空值或丟例外
   - Boyer-Moore：沒有多數元素
   - Bloom Filter：所有查詢都回傳 false

2. **只有一個元素**
   - 蓄水池：一定回傳那個元素
   - Boyer-Moore：那個元素就是多數

3. **所有元素都一樣**
   - Boyer-Moore：單一候選者，計數 = n
   - 蓄水池：隨便哪個元素都有代表性

4. **串流長度未知**
   - 蓄水池抽樣天生就處理得了
   - Count-Min Sketch：可能得動態調整參數

5. **整數溢位**
   - Count-Min Sketch：計數很大時用 long
   - 雜湊函數：模運算要寫對

---

## 4) 面試提示與複雜度分析

### 💡 面試策略

#### 什麼時候該用串流演算法：

**辨識模式：**
1. **「process data stream」** → 串流演算法
2. **「limited memory」** 或 **「O(1) space」** → 串流
3. **「unknown size」** 或 **「infinite stream」** → 蓄水池抽樣
4. **「majority element」** → Boyer-Moore
5. **「approximate count/frequency」** → Count-Min Sketch
6. **「have you seen before?」** → Bloom Filter

#### 解題框架：

```text
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

### 📊 複雜度分析

| 演算法 | 時間（每個元素） | 空間 | 準確度 |
|-----------|-------------------|-------|----------|
| Reservoir Sampling | O(1) | O(k) | 精確 |
| Boyer-Moore | O(1) | O(k) | 存在的話就精確 |
| Count-Min Sketch | O(d) | O(w × d) | ε-近似 |
| Bloom Filter | O(k) | O(m) 位元 | 會有偽陽性 |

**空間比較：**
- **HashMap**：O(n) - 所有元素的精確計數
- **Count-Min Sketch**：O(w × d)，其中 w × d << n
- **Bloom Filter**：O(m) 位元，m = O(n log(1/p))

**典型參數：**
- **Count-Min Sketch**：w = 1000、d = 5 → 上百萬個元素只要 5KB
- **Bloom Filter**：每個元素 10 個位元，偽陽性率 1%

### 🎯 面試時可以講的重點

1. **蓄水池抽樣為什麼成立：**
   - 「每個元素被選中的機率都是 k/n」
   - 「用歸納法證：P(元素 i 被選中) = k/i × (i/(i+1)) × ... × (n-1)/n = k/n」

2. **Boyer-Moore 的直覺：**
   - 「把不同的元素配對抵銷掉」
   - 「多數元素一定撐得過抵銷」
   - 「最多只有 k-1 個元素能出現超過 n/k 次」

3. **Count-Min Sketch 的權衡：**
   - 「拿空間換準確度：w 與 d 控制誤差界限」
   - 「絕不低估（只會因碰撞而高估）」
   - 「取所有雜湊函數的最小值可以壓低高估的程度」

4. **Bloom Filter 的性質：**
   - 「可能偽陽性，不可能偽陰性」
   - 「不能刪除元素（除非用 Counting Bloom Filter）」
   - 「空間很省：1% 偽陽性率大約每個元素 10 個位元」

### 🔧 最佳化技巧

1. **蓄水池抽樣：**
   - 用 `random.nextInt(count++)` 這個慣用寫法，程式碼更乾淨
   - 加權抽樣：用前綴和 + 二分搜尋

2. **Boyer-Moore：**
   - k=2（多數 > n/2）：題目有保證的話可以跳過驗證
   - k=3（> n/3）：剛好追蹤 2 個候選者
   - 一般的 k：用大小為 k-1 的 HashMap

3. **Count-Min Sketch：**
   - 保守更新（Conservative Update）：只把最小的那個計數加一
   - 換更快的雜湊函數（MurmurHash、CityHash）
   - 依準確度需求調整 w 與 d

4. **Bloom Filter：**
   - 挑相關性低的雜湊函數
   - 需要刪除就考慮 Counting Bloom Filter
   - 用位元層級的操作來省空間

### 📚 相關主題

- **抽樣**：分層抽樣、重要性抽樣
- **近似演算法**：HyperLogLog（基數估計）
- **機率型結構**：跳躍串列、treap
- **串流**：Misra-Gries 演算法、Space Saving 演算法
- **線上演算法**：競爭比分析、對抗模型

---

## 5) 完整程式碼範例

### 範例 1：加權蓄水池抽樣（LC 528）

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

### 範例 2：串流中位數（相關題 LC 295）

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

## 總結

**核心串流演算法：**
1. ✅ **蓄水池抽樣** - 從串流中均勻隨機抽樣
2. ✅ **Boyer-Moore** - 找多數／heavy hitters（精確）
3. ✅ **Count-Min Sketch** - 頻率估計（近似）
4. ✅ **Bloom Filter** - 成員測試（機率型）

**重點整理：**
- 串流演算法是拿準確度換空間效率
- 蓄水池抽樣：精確，k 個樣本用 O(k) 空間
- Boyer-Moore：對 heavy hitters 精確，O(k) 空間
- Count-Min Sketch：近似，誤差界限可調
- Bloom Filter：不會偽陰性，空間很省

**面試重點：**
- 要能講清楚機率證明（尤其是蓄水池抽樣）
- 知道各演算法該在什麼時候用
- 練熟兩階段驗證（Boyer-Moore）
- 說得出空間與準確度之間的取捨

**練習題：**
- 從 LC 382、398（蓄水池抽樣）開始
- 練熟 LC 169、229（Boyer-Moore）
- 弄懂 LC 295、703（串流 + 堆積）
- 研究雜湊型的設計題（LC 706、705）
