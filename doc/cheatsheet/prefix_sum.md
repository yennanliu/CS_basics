# Prefix Sum (累積和)

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/prefix_sum.png"></p>



## 0) Concept

```
We can use prefix sums. Say P[i+1] = A[0] + A[1] + ... + A[i], where A[i] = 1 if S[i] == '1', else A[i] = 0. We can calculate P in linear time.
```

- Query sub array sum in O(1) time

- Get sub array sum in linear time (O(N))
- Get `sub array` with `some conditions`
- Cases we don't want double loop, but want a `single loop` instead
    - prefix sum + dict (check if "-x-cum_sum" in dict)
- Use cases:
    - `subarray sum` in problems
    - if want to match count -> can use hashMap
    - if want to check Divisible -> get remainder with k for each num
- Use cases
    - 1 D prefix sum
    - 2 D prefix sum
- Ref
    - [fucking algorithm - Prefix sum](https://labuladong.github.io/algo/2/19/22/)
    - [LC trick I : prefix sum](https://www.twblogs.net/a/5edebd5274efa30adcc735cc)
    - [hash_map.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/hash_map.md)
    - [LC official problem set](https://leetcode.com/tag/prefix-sum/)
    - [LC problem set I](https://leetcode.com/discuss/general-discussion/563022/prefix-sum-problems)

### 0-1) Types

- Prefix sum
    - LC 238 Product Of Array Except Self
    - LC 560 Subarray Sum Equals K --- MUST !!!
- 2D prefix sum
    - LC 304

- Flip string
- Range addition (within `start`, `end` point)
    - LC 370
    - LC 1094
- sub array
    - sum array sum == k
        - pre sum count
        - LC 560
    - Maximum Size Subarray Sum Equals k
        - LC 325
    - Maximum Sum of Two Non-Overlapping Subarrays
        - LC 1031
    - Count Number of Nice Subarrays
        - LC 1248
    - Continuous Subarray Sum (preSum with mod)
        - https://github.com/yennanliu/CS_basics/blob/master/doc/pic/presum_mod.png
        - LC 523
    - Number of Sub-arrays of Size K and Average Greater than or Equal to Threshold
        - LC 1343
    - Subarray Sums Divisible by K
        - LC 974
- get count of overlap intervals
    LC 253
- Max Chunks To Make Sorted
    - LC 769

### 0-2) Pattern

#### 0-2-1) Get count of `continuous sub array equals to K`

```java
// java
// LC 560

// ..
Map<Integer, Integer> map = new HashMap<>();

// ..

for (int num : nums) {
    presum += num;

    // Check if there's a prefix sum such that presum - k exists
    // NOTE !!! below
    if (map.containsKey(presum - k)) {
        count += map.get(presum - k);
    }

    // Update the map with the current prefix sum
    map.put(presum, map.getOrDefault(presum, 0) + 1);
}

// ..

```

#### 0-2-2) get prefix with `range addition` 

```java
// java

// LC 1094

// ...

int[] prefixSum = new int[1001]; // the biggest array size given by problem

// `init pre prefix sum`
for (int[] t : trips) {
    
    int amount = t[0];
    int start = t[1];
    int end = t[2];

    /**
     *  NOTE !!!!
     *
     *   via trick below, we can `efficiently` setup prefix sum
     *   per start, end index
     *
     *   -> we ADD amount at start point (customer pickup up)
     *   -> we MINUS amount at `end point` (customer drop off)
     *
     *   -> via above, we get the `adjusted` `init prefix sum`
     *   -> so all we need to do next is :
     *      -> loop over the `init prefix sum`
     *      -> and keep adding `previous to current val`
     *      -> e.g. prefixSum[i] = prefixSum[i-1] + prefixSum[i]
     *
     */
    prefixSum[start] += amount;
    prefixSum[end] -= amount;
}

// update `prefix sum` array
for (int i = 1; i < prefixSum.length; i++) {
    prefixSum[i] += prefixSum[i - 1];
}


// ...
```


## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Flip String to Monotone Increasing

```python
# LC 926. Flip String to Monotone Increasing
# NOTE : there is also dp approaches
# V0 
# IDEA : PREFIX SUM
class Solution(object):
    def minFlipsMonoIncr(self, S):
        # get pre-fix sum
        P = [0]
        for x in S:
            P.append(P[-1] + int(x))
        # find min
        res = float('inf')
        for j in range(len(P)):
            res = min(res, P[j] + len(S)-j-(P[-1]-P[j]))
        return res

# V1
# IDEA : PREFIX SUM
# https://leetcode.com/problems/flip-string-to-monotone-increasing/solution/
class Solution(object):
    def minFlipsMonoIncr(self, S):
        # get pre-fix sum
        P = [0]
        for x in S:
            P.append(P[-1] + int(x))
        # return min
        return min(P[j] + len(S)-j-(P[-1]-P[j])
                   for j in range(len(P)))
```

### 2-2) Range Addition
```python
# LC 370. Range Addition
# V0
# IDEA : double loop -> 2 single loops,  prefix sum
class Solution(object):
    def getModifiedArray(self, length, updates):
        # edge case
        if not length:
            return
        if length and not updates:
            return [0 for i in range(length)]
        # init res
        res = [0 for i in range(length)]
        # get cumsum on start and end idx
        # then go through res, adjust the sum
        for _start, _end, val in updates:
            res[_start] += val
            if _end+1 < length:
                res[_end+1] -= val

        #print ("res = " + str(res))
        cur = 0
        for i in range(len(res)):
            cur += res[i]
            res[i] = cur
        #print ("--> res = " + str(res))
        return res

# V0'
# IDEA : double loop -> 2 single loops,  prefix sum
class Solution(object):
    def getModifiedArray(self, length, updates):
        # NOTE : we init res with (len+1)
        res = [0] * (length + 1)
        """
        NOTE !!!

        -> We split double loop into 2 single loops
        -> Steps)
            step 1) go through updates,  add val to start and end idx in res
            step 2) go through res, maintain an aggregated sum (sum) and add it to res[i]
                e.g. res[i], sum = res[i] + sum, res[i] + sum
        """
        for start, end, val in updates:
            res[start] += val
            res[end+1] -= val
        
        sum = 0
        for i in range(0, length):
            res[i], sum = res[i] + sum, res[i] + sum
        
        # NOTE : we return res[0:-1]
        return res[0:-1]

# V0'
# IDEA : double loop -> 2 single loops,  prefix sum
class Solution(object):
    def getModifiedArray(self, length, updates):
        ret = [0] * (length + 1)
        for start, end, val in updates:
            ret[start] += val
            ret[end+1] -= val
        
        sum = 0
        for i in range(0, length):
            ret[i], sum = ret[i] + sum, ret[i] + sum
        
        return ret[0:-1]
```

### 2-3) Count Number of Nice Subarrays
```python
# 1248. Count Number of Nice Subarrays
# NOTE : there are also array, window, deque.. approaches
class Solution:
    def numberOfSubarrays(self, nums, k):
        d = collections.defaultdict(int)
        """
        definition of hash map (in this problem)

            -> d[cum_sum] = number_of_cum_sum_till_now

            -> so at beginning, cum_sum = 0, and its count = 1
            -> so we init hash map via "d[0] = 1"
        """
        d[0] = 1
        # init cum_sum
        cum_sum = 0
        res = 0
        # go to each element
        for i in nums:
            ### NOTE : we need to check "if i % 2 == 1" first, so in the next logic, we can append val to result directly
            if i % 2 == 1:
                cum_sum += 1
            """
            NOTE !!! : trick here !!!
             -> same as 2 sum ...
             -> if cum_sum - x == k
                -> x = cum_sum - k
                -> so we need to check if "cum_sum - k" already in our hash map
            """
            if cum_sum - k in d:
                ### NOTE !!! here : we need to use d[cum_sum - k], since this is # of sub string combination that with # of odd numbers  == k
                res += d[cum_sum - k]
            ### NOTE !!! : we need to add 1 to count of "cum_sum", since in current loop, we got a new cur_sum (as above)
            d[cum_sum] += 1
        return res
```

### 2-4) Maximum Size Subarray Sum Equals k
```python
# LC 325. Maximum Size Subarray Sum Equals k
# V0 
# time complexity : O(N) | space complexity : O(N)
# IDEA : HASH TBALE
# -> have a var acc keep sum of all item in nums,
# -> and use dic collect acc and its index
# -> since we want to find nums[i:j] = k  -> so it's a 2 sum problem now
# -> i.e. if acc - k in dic => there must be a solution (i,j) of  nums[i:j] = k  
# -> return the max result 
# -> ### acc DEMO : given array a = [1,2,3,4,5] ###
# -> acc_list = [1,3,6,10,15]
# -> so sum(a[1:3]) = 9 = acc_list[3] - acc_list[1-1] = 10 - 1 = 9 
class Solution(object):
    def maxSubArrayLen(self, nums, k):

        result, acc = 0, 0
        # NOTE !!! we init dic as {0:-1} ({sum:idx})
        dic = {0: -1}

        for i in range(len(nums)):
            acc += nums[i]
            if acc not in dic:
                ### NOTE : we save idx as dict value
                dic[acc] = i
            ### acc - x = k -> so x = acc - k, that's why we check if acc - x in the dic or not
            if acc - k in dic:
                result = max(result, i - dic[acc-k])
        return result
```

### 2-5) Subarray Sum Equals K

```java
// java
// LC 560
public int subarraySum(int[] nums, int k) {
    /**
     * NOTE !!!
     *
     *   use Map to store prefix sum and its count
     *
     *   map : {prefixSum: count}
     *
     *
     *   -> since "same preSum may have multiple combination" within hashMap,
     *      so it's needed to track preSum COUNT, instead of its index
     */
    Map<Integer, Integer> map = new HashMap<>();
    int presum = 0;
    int count = 0;

    /** 
     *  NOTE !!!
     *
     *  Initialize the map with prefix sum 0 (to handle subarrays starting at index 0)
     */
    map.put(0, 1);

    for (int num : nums) {
        presum += num;

        // Check if there's a prefix sum such that presum - k exists
        if (map.containsKey(presum - k)) {
            count += map.get(presum - k);
        }

        // Update the map with the current prefix sum
        map.put(presum, map.getOrDefault(presum, 0) + 1);
    }

    return count;
}
```

### 2-6) Continuous Subarray Sum

```java
// java
// LC 523
// V1
// IDEA : HASHMAP
// https://leetcode.com/problems/continuous-subarray-sum/editorial/
// https://github.com/yennanliu/CS_basics/blob/master/doc/pic/presum_mod.png
public boolean checkSubarraySum_1(int[] nums, int k) {
    int prefixMod = 0;
    HashMap<Integer, Integer> modSeen = new HashMap<>();
    modSeen.put(0, -1);

    for (int i = 0; i < nums.length; i++) {
        /**
         * NOTE !!! we get `mod of prefixSum`, instead of get prefixSum
         */
        prefixMod = (prefixMod + nums[i]) % k;

        if (modSeen.containsKey(prefixMod)) {
            // ensures that the size of subarray is at least 2
            if (i - modSeen.get(prefixMod) > 1) {
                return true;
            }
        } else {
            // mark the value of prefixMod with the current index.
            modSeen.put(prefixMod, i);
        }
    }

    return false;
}
```

### 2-7) Max Chunks To Make Sorted

```java
// java
// LC 769
// V1-1
// https://leetcode.com/problems/max-chunks-to-make-sorted/editorial/
// IDEA: Prefix Sums
/**
 *  IDEA:
 *
 *  An important observation is that a segment of the
 *  array can form a valid chunk if, when sorted,
 *  it matches the corresponding segment
 *  in the fully sorted version of the array.
 *
 * Since the numbers in arr belong to the range [0, n - 1], we can simplify the problem by using the property of sums. Specifically, for any index, it suffices to check whether the sum of the elements in arr up to that index is equal to the sum of the elements in the corresponding prefix of the sorted array.
 *
 * If these sums are equal, it guarantees that the elements in the current segment of arr match the elements in the corresponding segment of the sorted array (possibly in a different order). When this condition is satisfied, we can form a new chunk — either starting from the beginning of the array or the end of the previous chunk.
 *
 * For example, consider arr = [1, 2, 0, 3, 4] and the sorted version sortedArr = [0, 1, 2, 3, 4]. We find the valid segments as follows:
 *
 * Segment [0, 0] is not valid, since sum = 1 and sortedSum = 0.
 * Segment [0, 1] is not valid, since sum = 1 + 2 = 3 and sortedSum = 0 + 1 = 1.
 * Segment [0, 2] is valid, since sum = 1 + 2 + 0 = 3 and sortedSum = 0 + 1 + 2 = 3.
 * Segment [3, 3] is valid, because sum = 1 + 2 + 0 + 3 = 6 and sortedSum = 0 + 1 + 2 + 3 = 6.
 * Segment [4, 4] is valid, because sum = 1 + 2 + 0 + 3 + 4 = 10 and sortedSum = 0 + 1 + 2 + 3 + 4 = 10.
 * Therefore, the answer here is 3.
 *
 * Algorithm
 * - Initialize n to the size of the arr array.
 * - Initialize chunks, prefixSum, and sortedPrefixSum to 0.
 * - Iterate over arr with i from 0 to n - 1:
 *    - Increment prefixSum by arr[i].
 *    - Increment sortedPrefixSum by i.
 *    - Check if prefixSum == sortedPrefixSum:
 *        - If so, increment chunks by 1.
 * - Return chunks.
 *
 */
public int maxChunksToSorted_1_1(int[] arr) {
    int n = arr.length;
    int chunks = 0, prefixSum = 0, sortedPrefixSum = 0;

    // Iterate over the array
    for (int i = 0; i < n; i++) {
        // Update prefix sum of `arr`
        prefixSum += arr[i];
        // Update prefix sum of the sorted array
        sortedPrefixSum += i;

        // If the two sums are equal, the two prefixes contain the same elements; a chunk can be formed
        if (prefixSum == sortedPrefixSum) {
            chunks++;
        }
    }
    return chunks;
}
```

### 2-8) Maximum Sum of Two Non-Overlapping Subarrays

```java
// java
// LC 1031

// V1
// https://leetcode.ca/2018-09-26-1031-Maximum-Sum-of-Two-Non-Overlapping-Subarrays/
// IDEA: PREFIX SUM
public int maxSumTwoNoOverlap_1(int[] nums, int firstLen, int secondLen) {
    int n = nums.length;
    int[] s = new int[n + 1];
    for (int i = 0; i < n; ++i) {
        s[i + 1] = s[i] + nums[i];
    }
    int ans = 0;
    // case 1)  check `firstLen`, then `secondLen`
    for (int i = firstLen, t = 0; i + secondLen - 1 < n; ++i) {
        t = Math.max(t, s[i] - s[i - firstLen]);
        ans = Math.max(ans, t + s[i + secondLen] - s[i]);
    }
    // case 2)  check  `secondLen`, then `firstLen`
    for (int i = secondLen, t = 0; i + firstLen - 1 < n; ++i) {
        t = Math.max(t, s[i] - s[i - secondLen]);
        ans = Math.max(ans, t + s[i + firstLen] - s[i]);
    }
    return ans;
}
```
