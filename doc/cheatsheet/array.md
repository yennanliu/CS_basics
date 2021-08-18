# Array 

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP
#### 1-1-1) Insert into Array
```python
# Out[27]: [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
# In [28]: p.insert(1, [6,1])
# In [29]: p
# Out[29]: [[7, 0], [6, 1], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
```
#### 1-1-2) Delete from Array
#### 1-1-3) check if element in Array
#### 1-1-4) append to array (head, tail)
#### 1-1-5) Sort Array
#### 1-1-6) Flatten Array
```python
# V1
r = []
def flatten_array(_array):
    for i in _array:
        if type(i) == int:
            print (i)
            r.append(i)
        else:
            flatten_array(i)

_input = [1,0, [1,2,[4,[5,[6,[7]]]]]]#[1,[4,[6]]] #[[1,1],2,[1,1]]

flatten_array(_input)
print ("r = " + str(r))

# V2
# https://stackoverflow.com/questions/2158395/flatten-an-irregular-list-of-lists
def flatten(L):
    for item in L:
        try:
            yield from flatten(item)
        except TypeError:
            yield item

r2 = flatten(_input)
for i in r2:
    print (i)

# V3
def flatten2(L):
    for item in L:
        try:
            yield from flatten2(item)
        except:
            yield item

r3 = flatten2(_input)
for i in r3:
    print (i)
```


```python
y = [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
print (y)
y.sort(key = lambda x : (-x[0], x[1]))
print (y)
# [[7, 0], [4, 4], [7, 1], [5, 0], [6, 1], [5, 2]]
# [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
```

## 2) LC Example

## 2-1) Merge Intervals
```python
# 056 Merge Intervals
# V0
class Solution:
    # @param intervals, a list of Interval
    # @return a list of Interval
    def merge(self, intervals):
        intervals = sorted(intervals, key=lambda x: x.start)
        result = []
        for interval in intervals:
            if len(result) == 0 or result[-1].end < interval.start:
                result.append(interval)
            else:
                result[-1].end = max(result[-1].end, interval.end)
        return result

# V1
# https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md
# intervals : [[1,3],[2,6]...]
def merge(intervals):
    if not intervals: return []
    intervals.sort(key=lambda intv: intv[0])
    res = []
    res.append(intervals[0])
    
    for i in range(1, len(intervals)):
        curr = intervals[i]
        last = res[-1]
        if curr[0] <= last[1]:
            last[1] = max(last[1], curr[1])
        else:
            res.append(curr)
    return res
```

## 2-2) Non-overlapping-intervals
```python
# V0 
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

## 2-3) Queue Reconstruction by Height
```python
# 406 Queue Reconstruction by Height
class Solution(object):
    def reconstructQueue(self, people):
        people.sort(key = lambda x : (-x[0], x[1]))
        res = []
        for p in people:
            res.insert(p[1], p)
        return res
```