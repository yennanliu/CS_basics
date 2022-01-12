# Greedy 
> Greedy is the sub-class of DP (dynamic programming)
- Brute force --optimize-->  DP -> --optimize--> Greedy
- Most of the problems don't have greedy property

## 0) Concept  

### 0-1) Types
- Interval scheduling
- Non-overlapping Intervals
    - intervals.sort(key = lambda x : x[0])
- Max length
    - Max length of pair chain
        - sorted(pairs, key=lambda x : x[1])
- Sell-buy stock

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

## 2) LC Example

### 2-1) Maximum Length of Pair Chain
```python
# LC 646 Maximum Length of Pair Chain
# V0 
# IDEA : GREEDY + sorting
# ->  we sort on pair's 1st element -> possible cases that we can get sub pairs with max length with the needed conditions
# ->  we need to find the "max length" of "continous or non-continous" sub pairs (with condition)
#      -> so start from the "sorted 1st pair" CAN ALWAYS MAKE US GET THE MAX LENGTH of sub pairs with the condition ( we define a pair (c, d) can follow another pair (a, b) if and only if b < c. Chain of pairs can be formed in this fashion.)
##########
# DEMO
#     ...: 
#     ...: x = [[-10,-8],[8,9],[-5,0],[6,10],[-6,-4],[1,7],[9,10],[-4,7]]
#     ...: s = Solution()
#     ...: r = s.findLongestChain(x)
#     ...: print (r)
# pairs = [[-10, -8], [-6, -4], [-5, 0], [1, 7], [-4, 7], [8, 9], [6, 10], [9, 10]]
# x = -10 y = -8
#  currTime = -8 ans = 1
# x = -6 y = -4
#  currTime = -4 ans = 2
# x = -5 y = 0
#  currTime = -4 ans = 2
# x = 1 y = 7
#  currTime = 7 ans = 3
# x = -4 y = 7
#  currTime = 7 ans = 3
# x = 8 y = 9
#  currTime = 9 ans = 4
# x = 6 y = 10
#  currTime = 9 ans = 4
# x = 9 y = 10
#  currTime = 9 ans = 4
# 4
class Solution(object):
    def findLongestChain(self, pairs):
        pairs = sorted(pairs, key=lambda x : x[1])
        ### NOTICE HERE
        currTime, ans = float('-inf'), 0
        for x, y in pairs:
            ### NOTICE HERE
            if currTime < x:
                currTime = y
                ans += 1
        return ans
```

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
class Solution:
    def canCompleteCircuit(self, gas, cost):
        start, sum_ = 0, 0 
        for i in range(len(gas)):
            sum_ += gas[i] - cost[i]
            if sum_ < 0:
                start, sum_ = i+1, 0 
        return start if sum(gas) - sum(cost) >= 0 else -1 
```

### 2-5) Non-overlapping Intervals
```python
# 435 Non-overlapping Intervals
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        if not intervals: return 0
        intervals.sort(key = lambda x : x[0])
        last = 0
        res = 0
        for i in range(1, len(intervals)):
            if intervals[last][1] > intervals[i][0]:
                if intervals[i][1] < intervals[last][1]:
                    last = i
                res += 1
            else:
                last = i
        return res
```

### 2-6) Reorganize String
```python
# 767 Reorganize String
class Solution(object):
    def reorganizeString(self, S):
        """
        :type S: str
        :rtype: str
        """
        cnt = collections.Counter(S)
        # Be aware of it : ans = "#" -> not to have error in ans[-1] when first loop
        ans = '#'
        while cnt:
            stop = True
            for v, c in cnt.most_common():
                if v != ans[-1]:
                    stop = False
                    ans += v
                    cnt[v] -= 1
                    if not cnt[v]: del cnt[v]
                    break
            # Be aware of it : if there is no valid "v", then the while loop will break automatically at this condition (stop = True)
            if stop: break
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