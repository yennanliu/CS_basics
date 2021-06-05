# Scanning Line 


## 0) Concept  
1. Find the "max" overlap in intervals


### 0-1) Framework

### 0-2) Pattern
```python
open_close = []
for interval in intervals:
    open_close.append(interval[0], True)
    open_close.append(interval[1], False)

open_close = open_close.sort(lambda x : x[0], x[1])

n = 0
max_num = 0

for i in open_close:
    if i[1]:
        n += 1
    else:
        n -= 1
    max_num = max(max_num, n)
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example
- meeting-rooms-ii
    - https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Sort/meeting-rooms-ii.py
