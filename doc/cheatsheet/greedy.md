# Greedy 
> Greedy is the sub-class of DP (dynamic programming)
- Brute force --optimize-->  DP -> --optimize--> Greedy
- Most of the problems don't have greedy property

## 0) Concept  

### 0-1) Types
- Interval scheduling
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
# 646 Maximum Length of Pair Chain
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
class Solution(object):
    def canJump(self, nums):
        reach = 0
        for i, num in enumerate(nums):
            if i > reach:
                return False
            reach = max(reach, i + num)
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