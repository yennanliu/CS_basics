# Prefix sum

## 0) Concept  

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Range Addition
```python
# LC 370. Range Addition
# V0
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