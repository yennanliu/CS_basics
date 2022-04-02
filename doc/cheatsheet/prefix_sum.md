# Prefix Sum

## 0) Concept
```
We can use prefix sums. Say P[i+1] = A[0] + A[1] + ... + A[i], where A[i] = 1 if S[i] == '1', else A[i] = 0. We can calculate P in linear time.
```
- Get `sub array` with `some conditions`
- Cases we don't want double loop, but want a `single loop` instead
    - prefix sum + dict (check if "-x-cum_sum" in dict)
- Use cases:
    - `subarray sum` in problems
    - if want to match count -> can use hashMap
    - if want to check Divisible -> get remainder with k for each num
- Ref
    - [LC trick I : prefix sum](https://www.twblogs.net/a/5edebd5274efa30adcc735cc)
    - [hash_map.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/hash_map.md)
    - [LC official problem set](https://leetcode.com/tag/prefix-sum/)
    - [LC problem set I](https://leetcode.com/discuss/general-discussion/563022/prefix-sum-problems)

### 0-1) Types
- Flip string
- Range addition
    - LC 370
- sub array
    - sum array sum == k
        - LC 560
    - Count Number of Nice Subarrays
        - LC 1248
    - Continuous Subarray Sum
        - LC 523
    - Number of Sub-arrays of Size K and Average Greater than or Equal to Threshold
        - LC 1343
    - Subarray Sums Divisible by K
        - LC 974

### 0-2) Pattern

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