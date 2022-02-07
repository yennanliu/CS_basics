# Intervals
- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md
- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E4%BA%A4%E9%9B%86%E9%97%AE%E9%A2%98.md 

## 0) Concept  

### 0-1) Types

- Types
    - Merge intervals
        - LC 56
    - Insert intervala
        - LC 057
    - Overlap intervals
    - Non-overlap intervals
        - LC 435
    - Max length of pair chain
    - Courses problems
        - LC 207, 210
    - Meeting room problems
        - LC 252, 253

- Algorithm
    - array op
    - dict op

- Data structure
    - array
    - dict

### 0-2) Pattern
```python
# python

#-----------
# V1
#-----------
# LC 435, 646
intervals = [[1,2],[2,3],[3,4],[1,3]]
last = intervals[0]
intervals.sort(key = lambda x : x[1])
cnt = 0
for i in range(len(intervals)):
    if some_condition:
        do_sth
    else:
        do_sth

#-----------
# V2
#-----------
def intervalsPattern(intervals):
    if not points: return 0

    # do some update on intervals, depends, e.g. : intervals.append(newInterval)

    #### NOTE we need to SORT first
    intervals.sort(key = lambda x : x[1]) # or points.sort(key = lambda x : x[0])...depends
    
    curr_pos = points[0][1]
    ans = 1
    for i in range(len(intervals)):

        if some_condition:
            # do sth
        else:
            # do sth
    return ans
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Merge Intervals
- TODO : move it to "interval merge cheatsheet"
- [fucking-algorithm - intervals](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md)

```python
# 056 Merge Intervals
# V0
# IDEA : interval op, LC 57
class Solution(object):
    def merge(self, intervals):
        # edge case
        if not intervals:
            return
        intervals.sort(key=lambda x : x[0])
        res = []
        for i in range(len(intervals)):
            if not res or res[-1][1] < intervals[i][0]:
                res.append(intervals[i])
            else:
                res[-1][1] = max(intervals[i][1], res[-1][1])
        return res

# V0'
# IDEA : interval op
# https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md
class Solution:
    def merge(self, intervals):
        intervals = sorted(intervals, key=lambda x: x[0])
        result = []
        for interval in intervals:
            if len(result) == 0 or result[-1][1] < interval[0]:
                result.append(interval)
            else:
                result[-1][1] = max(result[-1][1], interval[1])
        return result
```

### 2-2) Non-overlapping-intervals
```python
# LC 435 Non-overlapping Intervals
# V0
# IDEA : 2 POINTERS + sorting + intervals
# TODO : make it general : (sort by x[0] or x[1] and the op)
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        ### NOTE THIS !!!
        intervals.sort(key = lambda x : x[1])
        #print ("intervals = " + str(intervals))
        res = []
        cnt = 0
        last = intervals[0]
        # edge case
        if not intervals:
            return 0
        ### NOTE : start from idx = 1
        for i in range(1, len(intervals)):
            # case 1
            if intervals[i][0] < last[1]:
                cnt += 1
            # case 2
            else:
                last = intervals[i]
                last[1] = max(intervals[i][1], last[1])
        return cnt

# V0'
# IDEA : 2 POINTERS + sorting + intervals
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        if not intervals: return 0
        ### NOTE THIS !!!
        intervals.sort(key = lambda x : x[0])
        #intervals.sort(key = lambda x : [x[0],x[1]])  # this one is OK as well
        """
        ### NOTE : last is the "idx" of last element
                 -> we'll leverage it for overlap intervals removal
        """
        last = 0
        res = 0
        for i in range(1, len(intervals)):
            if intervals[last][1] > intervals[i][0]:
                """
                ### NOTE : if "last" element's "second" element > intervals[i] 's  "second" element
                         -> we need to use intervals[i] 's index as last index
                """
                if intervals[i][1] < intervals[last][1]:
                    last = i
                res += 1
            else:
                last = i
        return res
```


### 2-3) Insert Interval
```python
# LC 57 Insert Interval
# V0
# IDEA : compare merged[-1][1]. interval[0]
# https://leetcode.com/problems/insert-interval/discuss/1236101/Python3-Easy-to-Understand-Solution
### NOTE : there are only 2 cases
# case 1) no overlap -> append interval directly
# case 2) overlap -> MODIFY 2nd element in last merged interval with the bigger index
class Solution:
    def insert(self, intervals, newInterval):
        ### NOTE THIS TRICK!!! : APPEND newInterval to intervals
        intervals.append(newInterval)
        # need to sort first (by 1st element)
        intervals.sort(key=lambda x:x[0])
        merged = []
        for interval in intervals:
            ### NOTE this condition
            # if not merged : append directly
            # if merged[-1][1] < interval[0] : means no overlap : append directly as well
            if not merged or merged[-1][1] < interval[0]:
                merged.append(interval)
            else:
                ### NOTE this op, if there is overlap, we ONLY modify the 2nd element in last interval with BIGGER digit
                merged[-1][1]= max(merged[-1][1],interval[1])
        return merged
```

### 2-4) Maximum Length of Pair Chain
```python
# LC 646 Maximum Length of Pair Chain
# V0
# IDEA : GREEDY + sorting + 2 pointers
# IDEA :
# -> SORT ON "1st element" (0 index)
# -> define i pointer, cnt
# -> loop over pairts
# -> if j == 0 or pairs[j][0] > pairs[i][1]
#    -> make i = j, and cnt += 1
class Solution(object):
    def findLongestChain(self, pairs):
        pairs.sort(key=lambda x: x[1])
        cnt = 0
        i = 0
        for j in range(len(pairs)):
            if j == 0 or pairs[j][0] > pairs[i][1]:
                i = j
                cnt += 1
        return cnt

# V0'
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

### 2-5) Minimum Number of Arrows to Burst Balloons
```python
# 452 Minimum Number of Arrows to Burst Balloons
# https://blog.csdn.net/MebiuW/article/details/53096708
class Solution(object):
    def findMinArrowShots(self, points):
        if not points: return 0
        points.sort(key = lambda x : x[1])
        curr_pos = points[0][1]
        ans = 1
        for i in range(len(points)):
            if curr_pos >= points[i][0]:
                continue
            curr_pos = points[i][1]
            ans += 1
        return ans
```