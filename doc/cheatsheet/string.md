# String 

> algorithm/LC relative to string/text/array -> string

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP
- String plus
- String bit op
- String Multiplication
- loop over string
- inverse loop over string
```python
x = "abcd"
for i in range(len(x)-1, -1, -1):
    print (i)
# 3
# 2
# 1
# 0
for i in range(len(x)-1, -1, -2):
    print (i)
# 3
# 1
```

### 1-2) Tricks
```python
# go through elements in str AVOID index out of range error
x = '1234'

for i in range(len(x)):
    if  i == len(x)-1 or x[i] != x[i+1]:
        print (x[i])
```

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

### 2-3)
```python
# LC 038 Count and say
# V0
# IDEA : ITERATION
class Solution:
    def countAndSay(self, n: int) -> str:
        
        val = ""
        res = "1"
        
        for _ in range(n-1):
            cnt = 1
            for j in range(len(res)-1):
                if res[j]==res[j+1]:
                    cnt+=1
                else:
                    val += str(cnt) + res[j]
                    cnt = 1
            val += str(cnt)+res[-1]
            res = val
            val = ""
        return res
```