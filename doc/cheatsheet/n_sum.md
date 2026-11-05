# N Sum

> **Scope** — The k-sum family specifically — 2Sum through kSum, the sort-then-converge recursion, and duplicate handling.
> **See also**: [2_pointers.md](./2_pointers.md) — the general convergence template; [hash_map.md](./hash_map.md) — the unsorted O(n) 2Sum; [add_x_sum.md](./add_x_sum.md) — a different "sum" problem entirely (digit addition).

## LeetCode Problem Lists

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

## 0) Concept  

### 0-1) Types

- Types
    - 2 Sum
        - 2 Sum
        - 2 Sum II : array is sorted
        - 2 Sum III - Data structure design
        - 2 Sum IV - Input is a BST
        - Two Sum Less Than K
    - 3 Sum
    - 4 Sum
    - N Sum

- Algorithm
    - sorting
    - 2 pointers
        - sorting + 2 pointers
    - hashmap op
    - array op

- Data structure
    - dict
    - set
    - array
    - Treenode

## 1) General form

Sort first, then one function covers every k: `k = 2` is the two-pointer base case, and each larger k fixes one element and recurses. `n_sum_target(nums, 3, 0, 0)` is LC 15; `k = 4` is LC 18.

**Recognise** — "all unique pairs / triplets / quadruplets that sum to a target"; n ≤ 3,000 for 3 Sum, n ≤ 200 for 4 Sum, and duplicates must not repeat in the answer.

**Key point** — on a sorted array, fixing the smallest element leaves a (k − 1) Sum on the suffix with target − that element, and two pointers solve the k = 2 case in one pass; skipping equal neighbours at every level is what makes each combination appear once.

```python
# python
# IDEA: n Sum on a SORTED array = fix nums[i], solve (n-1) Sum on the suffix; n == 2 is the 2-pointer base case
# time = O(n^(k-1)) for k Sum, space = O(k) recursion depth + the intermediate result lists each level builds before copying
#        them up, which can hold as many tuples as the answer (excluding the final output)
def n_sum_target(nums, k, start, target):
    res = []
    if k < 2 or len(nums) - start < k:       # need at least 2 Sum and k numbers left
        return res
    if k == 2:
        lo, hi = start, len(nums) - 1
        while lo < hi:
            left, right = nums[lo], nums[hi]
            total = left + right
            if total < target:
                lo += 1
            elif total > target:
                hi -= 1
            else:
                res.append([left, right])
                # skip every copy of both values, so the same pair is never added twice
                while lo < hi and nums[lo] == left:
                    lo += 1
                while lo < hi and nums[hi] == right:
                    hi -= 1
        return res
    i = start
    while i < len(nums):
        for sub in n_sum_target(nums, k - 1, i + 1, target - nums[i]):
            res.append([nums[i]] + sub)
        # skip copies of nums[i]: the same first element would rebuild the same tuples
        while i < len(nums) - 1 and nums[i] == nums[i + 1]:
            i += 1
        i += 1
    return res

nums = sorted([1, 0, -1, 0, -2, 2])
n_sum_target(nums, 4, 0, 0)   # -> [[-2, -1, 1, 2], [-2, 0, 0, 2], [-1, 0, 0, 1]]
```

```java
// java
// IDEA: n Sum on a SORTED array = fix nums[i], solve (n-1) Sum on the suffix; n == 2 is the 2-pointer base case
// time = O(n^(k-1)) for k Sum, space = O(k) recursion depth + the intermediate result lists each level builds before copying
//        them up, which can hold as many tuples as the answer (excluding the final output)
public List<List<Integer>> nSumTarget(int[] nums, int k, int start, long target) {
    List<List<Integer>> res = new ArrayList<>();
    if (k < 2 || nums.length - start < k) return res;   // need at least 2 Sum and k numbers left
    if (k == 2) {
        int lo = start, hi = nums.length - 1;
        while (lo < hi) {
            int left = nums[lo], right = nums[hi];
            long sum = (long) left + right;              // long: 4Sum targets overflow int
            if (sum < target) {
                lo++;
            } else if (sum > target) {
                hi--;
            } else {
                res.add(new ArrayList<>(Arrays.asList(left, right)));
                // skip every copy of both values, so the same pair is never added twice
                while (lo < hi && nums[lo] == left) lo++;
                while (lo < hi && nums[hi] == right) hi--;
            }
        }
        return res;
    }
    for (int i = start; i < nums.length; i++) {
        for (List<Integer> sub : nSumTarget(nums, k - 1, i + 1, target - nums[i])) {
            sub.add(0, nums[i]);
            res.add(sub);
        }
        // skip copies of nums[i]: the same first element would rebuild the same tuples
        while (i < nums.length - 1 && nums[i] == nums[i + 1]) i++;
    }
    return res;
}
```

**Complexity**
- **Time O(n^(k-1))**: k − 2 nested fixed elements, each an O(n) loop, around the O(n) two-pointer scan; the O(n log n) sort is dominated for k ≥ 2.
- **Space**: O(k) recursion depth, plus the result lists each level builds before handing them up.

**Traps**
- Skipping duplicates only in the base case: `[-1, -1, 0, 1]` with k = 3, target 0 returns `[-1, 0, 1]` twice. Skip equal `nums[i]` at every level (`i > start and nums[i] == nums[i - 1]`).
- Summing in `int` in Java: LC 18 allows values up to 10⁹, so four of them overflow. The Java takes a `long` target.
- Forgetting to sort first: the two pointers then move the wrong way and miss pairs.

**Drill** — LC 15 · LC 1 *(k = 2, unsorted → hash map instead)* · LC 167 · LC 18 · LC 16 *(closest, not equal)*

### 1-1) Quick Decision Table ⭐⭐⭐⭐

| Goal | Template | Time | LC |
|------|----------|------|-----|
| Find **one** pair hitting target, array unsorted, need indices | Hash map complement (`target - x` lookup) | O(n) | LC 1 |
| Find **all unique** tuples summing to target | Sort + fix (k-2) indices + 2 pointers, skip duplicates | O(n^(k-1)) | LC 15, LC 18 |
| Generalize to any `k` | Recursive kSum reduction → base case = sorted 2 Sum | O(n^(k-1)) | LC 18 |
| **Closest** sum instead of exact | Same 2-pointer scan, track min abs(sum - target) | O(n^2) | LC 16 |
| Sum **under** a bound (not exact) | 2 pointers, move `l` on success (record max) | O(n log n) | LC 1099 |
| Count **subarrays** (contiguous) with sum = k | Prefix sum + hash map complement | O(n) | LC 560 |

**Key Idea**: every N Sum reduces to **2 Sum**. The only two 2 Sum engines are
(a) **hash map complement** — unsorted input, needs indices, O(n) but can't dedupe tuples easily, and
(b) **sorted 2 pointers** — needs sort O(n log n), but dedupes naturally and supports "closest / less than" variants.

### 1-2) Sorted + 2 pointers (duplicate skipping) ⭐⭐⭐⭐⭐

**Pattern**: sort → fix the outer element(s) → shrink `lo/hi` inward. Dedup happens in **two places**:
outer loop (`nums[i] == nums[i-1] → continue`) and after a hit (advance past equal values).

```python
# python
# LC 15 - 3Sum
# IDEA: sort, fix nums[i], then 2 pointers on the remaining sorted suffix
class Solution(object):
    def threeSum(self, nums):
        # time = O(n^2), space = O(n) (python sort = Timsort, O(n) aux) excluding output
        nums.sort()
        res = []
        n = len(nums)
        for i in range(n - 2):
            if nums[i] > 0:            # sorted -> rest can't sum to 0
                break
            if i > 0 and nums[i] == nums[i-1]:   # NOTE: skip duplicated 1st element
                continue
            lo, hi = i + 1, n - 1
            while lo < hi:
                s = nums[i] + nums[lo] + nums[hi]
                if s < 0:
                    lo += 1
                elif s > 0:
                    hi -= 1
                else:
                    res.append([nums[i], nums[lo], nums[hi]])
                    # NOTE: skip duplicated 2nd / 3rd element
                    while lo < hi and nums[lo] == nums[lo+1]:
                        lo += 1
                    while lo < hi and nums[hi] == nums[hi-1]:
                        hi -= 1
                    lo += 1
                    hi -= 1
        return res
```

```java
// java
// LC 15 - 3Sum
// IDEA: sort, fix nums[i], then 2 pointers on the remaining sorted suffix
public List<List<Integer>> threeSum(int[] nums) {
    // time = O(n^2), space = O(log n) (sorting) excluding output
    Arrays.sort(nums);
    List<List<Integer>> res = new ArrayList<>();
    int n = nums.length;
    for (int i = 0; i < n - 2; i++) {
        if (nums[i] > 0) break;                        // sorted -> rest can't sum to 0
        if (i > 0 && nums[i] == nums[i - 1]) continue; // NOTE: skip duplicated 1st element
        int lo = i + 1, hi = n - 1;
        while (lo < hi) {
            int sum = nums[i] + nums[lo] + nums[hi];
            if (sum < 0) {
                lo++;
            } else if (sum > 0) {
                hi--;
            } else {
                res.add(Arrays.asList(nums[i], nums[lo], nums[hi]));
                // NOTE: skip duplicated 2nd / 3rd element
                while (lo < hi && nums[lo] == nums[lo + 1]) lo++;
                while (lo < hi && nums[hi] == nums[hi - 1]) hi--;
                lo++;
                hi--;
            }
        }
    }
    return res;
}
```

### 1-3) Generic kSum recursion (k Sum → 2 Sum) ⭐⭐⭐⭐

**Key Idea**: `kSum(target, start, k)` = for each candidate `nums[i]`, prepend it to `kSum(target - nums[i], i+1, k-1)`.
Recursion bottoms out at `k == 2` (sorted 2 pointers). Same skeleton solves LC 15 (`k=3`) and LC 18 (`k=4`).

```python
# python
# LC 18 - 4Sum  (generic kSum)
# IDEA: recursively peel one element off -> base case is sorted 2 Sum
class Solution(object):
    def fourSum(self, nums, target):
        # time = O(n^(k-1)) = O(n^3), space = O(n) sorting (Timsort) + O(k) recursion depth (excluding output)
        nums.sort()
        return self.kSum(nums, target, 0, 4)

    def kSum(self, nums, target, start, k):
        res = []
        if start >= len(nums):
            return res
        if k == 2:                       # base case
            return self.twoSumSorted(nums, target, start)
        for i in range(start, len(nums) - k + 1):
            if i > start and nums[i] == nums[i-1]:   # skip duplicated element
                continue
            for sub in self.kSum(nums, target - nums[i], i + 1, k - 1):
                res.append([nums[i]] + sub)
        return res

    def twoSumSorted(self, nums, target, start):
        res = []
        lo, hi = start, len(nums) - 1
        while lo < hi:
            s = nums[lo] + nums[hi]
            if s < target:
                lo += 1
            elif s > target:
                hi -= 1
            else:
                res.append([nums[lo], nums[hi]])
                while lo < hi and nums[lo] == nums[lo+1]:
                    lo += 1
                while lo < hi and nums[hi] == nums[hi-1]:
                    hi -= 1
                lo += 1
                hi -= 1
        return res
```

```java
// java
// LC 18 - 4Sum  (generic kSum)
// IDEA: recursively peel one element off -> base case is sorted 2 Sum
public List<List<Integer>> fourSum(int[] nums, int target) {
    // time = O(n^(k-1)) = O(n^3), space = O(k) recursion depth (excluding output)
    Arrays.sort(nums);
    return kSum(nums, (long) target, 0, 4);   // NOTE: long target -> avoid int overflow
}

private List<List<Integer>> kSum(int[] nums, long target, int start, int k) {
    List<List<Integer>> res = new ArrayList<>();
    if (start >= nums.length) return res;
    if (k == 2) return twoSumSorted(nums, target, start);   // base case
    for (int i = start; i < nums.length - k + 1; i++) {
        if (i > start && nums[i] == nums[i - 1]) continue;   // skip duplicated element
        for (List<Integer> sub : kSum(nums, target - nums[i], i + 1, k - 1)) {
            List<Integer> cur = new ArrayList<>();
            cur.add(nums[i]);
            cur.addAll(sub);
            res.add(cur);
        }
    }
    return res;
}

private List<List<Integer>> twoSumSorted(int[] nums, long target, int start) {
    List<List<Integer>> res = new ArrayList<>();
    int lo = start, hi = nums.length - 1;
    while (lo < hi) {
        long sum = (long) nums[lo] + nums[hi];
        if (sum < target) {
            lo++;
        } else if (sum > target) {
            hi--;
        } else {
            res.add(Arrays.asList(nums[lo], nums[hi]));
            while (lo < hi && nums[lo] == nums[lo + 1]) lo++;
            while (lo < hi && nums[hi] == nums[hi - 1]) hi--;
            lo++;
            hi--;
        }
    }
    return res;
}
```

### 1-4) Closest sum variant ⭐⭐⭐

**Twist**: there may be **no exact hit**, so instead of returning on match we keep scanning and track the best
`|sum - target|`. Pointer movement rule is unchanged (`sum < target → lo++`, else `hi--`) — that is what makes
the greedy scan valid. Early return when `sum == target` (distance 0 can't be beaten).

```python
# python
# LC 16 - 3Sum Closest
# IDEA: same sort + 2 pointers scan, but track min |sum - target| instead of exact match
class Solution(object):
    def threeSumClosest(self, nums, target):
        # time = O(n^2), space = O(n) (python sort = Timsort, O(n) aux)
        nums.sort()
        n = len(nums)
        best = nums[0] + nums[1] + nums[2]
        for i in range(n - 2):
            if i > 0 and nums[i] == nums[i-1]:
                continue
            lo, hi = i + 1, n - 1
            while lo < hi:
                s = nums[i] + nums[lo] + nums[hi]
                if abs(s - target) < abs(best - target):
                    best = s
                if s == target:
                    return s          # can't get closer
                elif s < target:
                    lo += 1
                else:
                    hi -= 1
        return best
```

```java
// java
// LC 16 - 3Sum Closest
// IDEA: same sort + 2 pointers scan, but track min |sum - target| instead of exact match
public int threeSumClosest(int[] nums, int target) {
    // time = O(n^2), space = O(log n) (sorting)
    Arrays.sort(nums);
    int n = nums.length;
    int best = nums[0] + nums[1] + nums[2];
    for (int i = 0; i < n - 2; i++) {
        if (i > 0 && nums[i] == nums[i - 1]) continue;
        int lo = i + 1, hi = n - 1;
        while (lo < hi) {
            int sum = nums[i] + nums[lo] + nums[hi];
            if (Math.abs(sum - target) < Math.abs(best - target)) best = sum;
            if (sum == target) return sum;   // can't get closer
            else if (sum < target) lo++;
            else hi--;
        }
    }
    return best;
}
```

- **Variation** — LC 1099 (Two Sum Less Than K, see 2-4 below): same skeleton with a **strict bound** instead of a
  distance: when `nums[lo] + nums[hi] < k` record the sum and move `lo++` (try bigger), else `hi--`.

### 1-5) Hash map complement on prefix sums (contiguous version)

**Twist**: LC 560 asks for **contiguous subarrays**, not arbitrary pairs — so we can't sort. Build prefix sums
`P[j]` and note `sum(i..j) = P[j] - P[i-1] = k` ⇔ `P[i-1] = P[j] - k`. That is exactly the **2 Sum complement
lookup**, run over prefix sums, with a **count** map (not an index map) since we want all pairs.

```python
# python
# LC 560 - Subarray Sum Equals K
# IDEA: 2 Sum complement lookup over prefix sums; cnt[0] = 1 seeds the empty prefix
from collections import defaultdict
class Solution(object):
    def subarraySum(self, nums, k):
        # time = O(n), space = O(n)
        cnt = defaultdict(int)
        cnt[0] = 1          # NOTE: empty prefix
        prefix, res = 0, 0
        for x in nums:
            prefix += x
            res += cnt[prefix - k]   # complement lookup
            cnt[prefix] += 1         # NOTE: insert AFTER querying
        return res
```

```java
// java
// LC 560 - Subarray Sum Equals K
// IDEA: 2 Sum complement lookup over prefix sums; cnt[0] = 1 seeds the empty prefix
public int subarraySum(int[] nums, int k) {
    // time = O(n), space = O(n)
    Map<Integer, Integer> cnt = new HashMap<>();
    cnt.put(0, 1);                 // NOTE: empty prefix, so subarrays starting at idx 0 count
    int prefix = 0, res = 0;
    for (int x : nums) {
        prefix += x;
        res += cnt.getOrDefault(prefix - k, 0);            // complement lookup
        cnt.put(prefix, cnt.getOrDefault(prefix, 0) + 1);  // NOTE: insert AFTER querying
    }
    return res;
}
```

> **Gotcha**: `nums` can contain negatives / zeros here, so sliding window does **not** work — the prefix + hash map
> approach is required.

## 2) LC Example

### 2-1) 2 Sum — LC 1 ⭐⭐⭐⭐⭐
```python
# LC 001
class Solution(object):
    def twoSum(self, nums, target):
        lookup = {}
        for i, num in enumerate(nums):
            if target - num in lookup:
                return [lookup[target - num], i]
            lookup[num] = i
        return [-1, -1]
```

### 2-1') 2 Sum II — LC 167
```python
# LC 167 Two Sum II - Input Array Is Sorted
# NOTE : we can also use "2 sum" dict approach
# V0
# IDEA : TWO POINTERS
#       -> l = 0, r = len(numbers) - 1
class Solution(object):
    def twoSum(self, numbers, target):
        l = 0
        ### NOTE this
        r = len(numbers) - 1
        res = []
        while r > l:
            #print ("l, r = {}, {}".format(l, r))
            tmp = numbers[l] + numbers[r]
            if tmp == target:
                return [l+1, r+1]
            ### NOTE this
            elif tmp > target:
                r -= 1
            ### NOTE this
            else:
                l += 1
        return [-1, -1]
```

### 2-2) 3 Sum — LC 15 ⭐⭐⭐⭐⭐
```python
# LC 015
# V0
# IDEA : for loop + 2 sum
class Solution(object):
    def threeSum(self, nums):
        # edge case
        if not nums or len(nums) < 3:
            return []
        if len(nums) == 3:
            return [nums] if sum(nums) == 0 else []
        if nums.count(0) == len(nums):
            return [[0,0,0]]
        res = []
        nums.sort()
        # for loop
        for i in range(len(nums)):
            cur = nums[i]
            # 2 sum
            d = {}
            """
            Note:
             -> we don't need below implementation (will cause TLE)

                #_nums = nums[:i] + nums[i+1:]
                #for j, x in enumerate(_nums):

             -> this one is enough : for j, x in enumerate(nums[i+1:])
            """
            for j, x in enumerate(nums[i+1:]):
                # cur + x + y = 0
                # -> y = -x - cur
                if -x-cur in d:
                    tmp = [cur, x, -x-cur]
                    tmp.sort()
                    if tmp not in res:
                        res.append(tmp)
                    #res.append([cur, x, -x-cur])
                else:
                    d[x] = j
        return res

# V0'
# IDEA : 2 SUM -> 3 SUM
class Solution(object):
    def threeSum(self, nums):
        if not nums or len(nums) <= 2:
            return []
        res = []
        # optimize, not necessary
        nums.sort()
        # loop over i
        for i in range(len(nums)):
            """
            2 sum
            """
            d = {}
            """
            here we loop over range(i+1, len(nums))
            #   -> since we need non duplicated results
            """
            for j in range(i+1, len(nums)):
                """
                NOTE : nums[i] + nums[j] + nums[k] = 0
                #    -> so - (nums[i] + nums[j]) = nums[k]
                #    -> and we are trying to find if such k already in the dict
                """
                if -(nums[i] + nums[j]) in d:
                    tmp = [nums[i]] + [nums[j], -(nums[i]+nums[j])]
                    tmp.sort()
                    if tmp not in res:
                        res.append(tmp)
                else:
                    d[nums[j]] = j
        return res

# V0''
# IDEA : 2 SUM -> 3 SUM
class Solution(object):
    def threeSum(self, nums):
        res = []
        if not nums or len(nums) <= 2:
            return res
        # this sort may not be necessary
        nums.sort()
        for i in range(len(nums)):
            # NOTE : set target = -nums[i]
            t = -nums[i]
            d = {}
            ### NOTE : we NEED tp loop from idx = i+1 to len(nums)
            for j in range(i+1, len(nums)):
                if  (-nums[j] + t) in d:
                        tmp = [ nums[i], nums[j], -nums[j] + t ]
                        tmp.sort()
                        # note :  this trick to not append duplicated ans
                        if tmp not in res:
                            res.append(tmp)
                d[nums[j]] = j
        return res
```

### 2-3) 4 Sum — LC 18
```python
# LC 018
# V0 
class Solution(object):
    def fourSum(self, nums, target):
        resultList = []
        nums.sort()
        for num1 in range(0, len(nums)-3):
            for num2 in range(num1 + 1, len(nums)-2):
                num3 = num2 + 1
                num4 = len(nums) -1
                while num3 != num4:
                    summer = nums[num1] + nums[num2] + nums[num3] + nums[num4]
                    if summer == target:
                        list_temp = [nums[num1],nums[num2],nums[num3],nums[num4]]
                        if list_temp not in resultList:
                            resultList.append(list_temp)
                        num3 += 1
                    elif summer > target:
                        num4 -= 1
                    else:
                        num3 += 1
        return resultList
```
### 2-4) Two Sum Less Than K — LC 1099
```python
# LC 1099 Two Sum Less Than K
# V1
# https://goodtecher.com/leetcode-1099-two-sum-less-than-k/
class Solution:
    def twoSumLessThanK(self, nums, k):    
        nums = sorted(nums)
        i = 0
        j = len(nums) - 1
      
        max_sum = -1
        
        while i < j:
            if nums[i] + nums[j] >= k:
                j -= 1
            else:
                max_sum = max(max_sum, nums[i] + nums[j])
                i += 1      
        return max_sum

# V1'
# https://blog.51cto.com/u_15344287/3647641
class Solution:
    def twoSumLessThanK(self, nums: List[int], k: int) -> int:
        nums.sort()
        ans = -1
        for i1 in range(len(nums)):
            n1 = nums[i1]
            i2 = bisect.bisect_left(nums, k - nums[i1], lo=i1 + 1) - 1
            n2 = nums[i2]
            if i2 > i1:
                ans = max(ans, n1 + n2)
        return ans
```