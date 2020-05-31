# String cheatsheet 

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern

## 1) General form

## 2) LC Example

### 2-1) go through 2 string, keep comparing digits in eash string
```python
# 165 Compare Version Number
class Solution(object):
    def compareVersion(self, version1, version2):
        v1_split = version1.split('.')
        v2_split = version2.split('.')
        v1_len, v2_len = len(v1_split), len(v2_split)
        maxLen = max(v1_len, v2_len)
        for i in range(maxLen):
            temp1, temp2 = 0, 0
            if i < v1_len:
                temp1 = int(v1_split[i])
            if i < v2_len:
                temp2 = int(v2_split[i])
            if temp1 < temp2:
                return -1
            elif temp1 > temp2:
                return 1
        return 0
```

### 2-2) String -> Int
```python
# 445 Add Two Numbers II
# 394 Decode String
def str_2_int(x):
    r=0
    for i in x:
        r = int(r)*10 + int(i)
        print (i, r)
    return r

x="131"
r=str_2_int(x)
print (r)
# 1 1
# 3 13
# 1 131
# 131
```