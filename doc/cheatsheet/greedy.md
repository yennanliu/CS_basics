# Greedy 
> Greedy is sub-class of DP (dynamic programming)
- Brute force --optimize-->  DP -> --optimize--> Greedy
- Most of the problems don't have greedy property

## 0) Concept  

### 0-1) Types
- Interval scheduling
    - LC 621
- Non-overlapping Intervals
    - intervals.sort(key = lambda x : x[0])
    - [intervals.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/intervals.md)
- Max length
    - Max length of pair chain
        - sorted(pairs, key=lambda x : x[1])
        - LC 646
        - [intervals.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/intervals.md)
- Sell-buy stock
    - LC 122

### 0-2) Pattern
```python
# python
def GreedyPattern(self, cases):
    ans = 0
    for i in range(0,len(cases)-1):
        if condition:
            # do sth
    return ans

```
```java
// java
public int intervalSchedule(int[][] intvs) {
    if (intvs.length == 0) return 0;
    // order by end
    Arrays.sort(intvs, new Comparator<int[]>() {
        public int compare(int[] a, int[] b) {
            return a[1] - b[1];
        }
    });
    // at least one interval not overlap
    int count = 1;
    // after ordering, the first interval is x
    int x_end = intvs[0][1];
    for (int[] interval : intvs) {
        int start = interval[0];
        if (start >= x_end) {
            // find the next interval
            count++;
            x_end = interval[1];
        }
    }
    return count;
}
```

## 1) General form

### 1-1) Basic OP

#### 1-1) re-organzie string
```python
# LC 767
# ...
from collections import Counter
cnt = Counter(s)
res = "#"
while cnt:
    stop = True
    for k, _count in cnt.most_common():
        if res[-1] != k:
            stop = False
            res += k
            cnt[k] -= 1
            if cnt[k] == 0:
                del cnt[k]
            """
            NOTE this !!!
            """
            break
    if stop:
        break
# ...
```

## 2) LC Example

### 2-2) Jump Game
```python
# 055 Jump Game
# V0
class Solution(object):
    def canJump(self, nums):
        # edge case
        if not nums:
            return True
        cur = 0
        for i in range(len(nums)):
            if cur < i:
                return False
            cur = max(cur, i + nums[i])
        return True
```


### 2-2') Jump Game II
```python
# 045 Jump Game II
# V1
# IDEA : GREEDY
# https://leetcode.com/problems/jump-game-ii/discuss/1672485/Python-Greedy
class Solution:
    def jump(self, nums: List[int]) -> int:
        l = r = res = farthest = 0
        while r < len(nums) - 1:
            for idx in range(l, r+1):
                farthest = max(farthest, idx + nums[idx])
            l = r+1
            r = farthest 
            res += 1
        return res
```

### 2-3) Best Time to Buy and Sell Stock II
```python
# 122 Best Time to Buy and Sell Stock II
class Solution:
    def maxProfit(self, prices):
        profit = 0
        for i in range(0,len(prices)-1):
            if prices[i+1] > prices[i]:
                # have to sale last stock, then buy a new one
                # and sum up the price difference into profit
                profit += prices[i+1] - prices[i]
        return profit

```

### 2-4) Gas Station
```python
# 134 Gas Station
# V0
# IDEA : GREEDY
# IDEA : if sum(gas) - sum(cost) > 0, => THERE MUST BE A SOLUTION
# IDEA : since it's circular (symmetry), we can maintain "total" (e.g. total += gas[i] - cost[i]) of (gas[i], cost[i]) for each index as their "current sum"
class Solution(object):
    def canCompleteCircuit(self, gas, cost):
        start = remain = total = 0
        for i in range(len(gas)):
            remain += gas[i] - cost[i]
            total += gas[i] - cost[i]
            if remain < 0:
                remain = 0
                start = i + 1
        return -1 if total < 0 else start
```

### 2-6) Reorganize String
```python
# LC 767. Reorganize String

# V0 
# IDEA : GREEDY + COUNTER
# IDEA : 
#  step 1) order exists count (big -> small)
#  step 2) select the element which is "most remaining" and DIFFERENT from last ans element and append such element to the end of ans
#  step 3) if can't find such element, return ""
class Solution(object):
    def reorganizeString(self, S):
        cnt = collections.Counter(S)
        # Be aware of it : ans = "#" -> not to have error in ans[-1] when first loop
        ans = '#'
        while cnt:
            stop = True
            for v, c in cnt.most_common():
                """
                NOTE !!! trick here

                1) we only compare last element in ans and current key (v), if they are different, then append
                2) we break at the end of each for loop -> MAKE SURE two adjacent characters are not the same.
                3) we use a flag "stop", to decide whether should stop while loop or not
                """
                if v != ans[-1]:
                    stop = False
                    ans += v
                    cnt[v] -= 1
                    if not cnt[v]:
                        del cnt[v]
                    """
                    NOTE !!!
                     -> we BREAK right after each op, since we want to get next NEW most common element from "updated" cnt.most_common()
                    """
                    break
            # Be aware of it : if there is no valid "v", then the while loop will break automatically at this condition (stop = True)
            if stop:
                break
        return ans[1:] if len(ans[1:]) == len(S) else ''
```

### 2-7) Task Scheduler
```python
# LC 621. Task Scheduler
# V0
# pattern :
#    =============================================================================
#    -> task_time = (max_mission_count - 1) * (n + 1) + (number_of_max_mission)
#    =============================================================================
#   
#    -> Example 1) :
#    ->  AAAABBBBCCD, n=3
#    => THE EXPECTED tuned missions is like : ABXXABXXABXXAB
#    -> (4 - 1) * (3 + 1) + 2 = 14
#    -> 4 is the "how many missions the max mission has" (AAAA or BBBB)
#    -> 3 is n
#    -> 2 is "how many mission have max mission count" -> A and B. so it's 2
#    -> in sum,
#    -> (4 - 1) * (3 + 1) is for ABXXABXXABXX
#    -> and 2 is for AB
#
#   -> Example 2) :
#   -> AAABBB, n = 2
#   -> THE EXPECTED tuned missions is like : ABXABXAB
#   -> (3 - 1) * (2 + 1) + (2) = 8
class Solution(object):
    def leastInterval(self, tasks, n):
        count = collections.Counter(tasks)
        most = count.most_common()[0][1]
        num_most = len([i for i, v in count.items() if v == most])
        """
        example 1 : tasks = ["A","A","A","B","B","B"], n = 2
            -> we can split tasks as : A -> B -> idle -> A -> B -> idle -> A -> B
               -> 1) so there are 3-1 group. e.g. (A -> B -> idle), (A -> B -> idle)
                     and each group has (n+1) elements. e.g. (A -> B -> idle)
               -> 2) and the remain element is num_most. e.g. (A, B)
               -> 3) so total cnt = (3-1) * (2+1) + 2 = 8
    
        example 2 : tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
            -> we can split tasks as A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> idle -> idle -> A -> idle -> idle -> A
                -> 1) so there are 6-1 group. e.g. (A -> B -> C), (A -> D -> E), (A -> F -> G), (A -> idle -> idle), (A -> idle -> idle)
                      and each group has (n+1) elements. e.g. (A,B,C) .... (as above)
                -> 2) and the remain element is num_most. e.g. (A) 
                -> 3) so total cnt = (6-1)*(2+1) + 1 =  16
        """
        time = (most - 1) * (n + 1) + num_most
        return max(time, len(tasks)) # be aware of it 
```

### 2-8) Maximum Units on a Truck
```python
# LC 1710. Maximum Units on a Truck
# V0
# IDEA : GREEDY + sorting
class Solution(object):
    def maximumUnits(self, boxTypes, truckSize):
        # boxTypes[i] = [numberOfBoxesi, numberOfUnitsPerBoxi]:
        # edge case
        if not boxTypes or not truckSize:
            return 0
        """
        NOTE : we sort on sort(key=lambda x : -x[1])

            -> if unit is bigger, we can get bigger aggregated result (n * unit)
        """
        boxTypes.sort(key=lambda x : -x[1])
        res = 0
        for n, unit in boxTypes:
            # case 1 : truckSize == 0, break for loop and return ans
            if truckSize == 0:
                break
            # case 2 : truckSize < n, we CAN'T add all n to truck, but CAN only add (truckSize * unit) amount to truck
            elif truckSize < n:
                res += (truckSize * unit)
                truckSize = 0
                break
            # case 3 : normal case, it's OK to put all (n * unit) to truck once
            else:      
                res += (n * unit)
                truckSize -= n
        return res

# V1
# IDEA : GREEDY
# https://leetcode.com/problems/maximum-units-on-a-truck/discuss/1045318/Python-solution
class Solution(object):
    def maximumUnits(self, boxTypes, truckSize):
        boxTypes.sort(key = lambda x: -x[1])
        n = len(boxTypes)
        result = 0
        i = 0
        while truckSize >= boxTypes[i][0]:
            result += boxTypes[i][1] * boxTypes[i][0]
            truckSize -= boxTypes[i][0]
            i += 1
            if i == n:
                return result
        result += truckSize * boxTypes[i][1]
        return result
```