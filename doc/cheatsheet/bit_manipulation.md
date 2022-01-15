# Bit Manipulation 

## 0) Concept
- http://web.ntnu.edu.tw/~algo/Bit.html : bit VS byte
- https://github.com/yennanliu/CS_basics/blob/master/doc/bit_manipulation.md :  bit_manipulation.md
- https://www.runoob.com/python/python-operators.html

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Gray Code
```python
# LC 89 Gray Code
# V1'
# https://ithelp.ithome.com.tw/articles/10213273
# DEMO
# In [23]: add=1

# In [24]: add<<=1

# In [25]: add
# Out[25]: 2

# In [26]: add<<=1

# In [27]: add
# Out[27]: 4

# In [28]: add<<=1

# In [29]: add
# Out[29]: 8

# In [30]: add<<=1

# In [31]: add
# Out[31]: 16

# In [32]: add<<=1

# In [33]: add
# Out[33]: 32
class Solution:
    def grayCode(self, n):
        res = [0]
        add = 1
        for _ in range(n):
            for i in range(add):
                res.append(res[add - 1 - i] + add);
            add <<= 1
        return res

# V1''
# https://blog.csdn.net/qqxx6661/article/details/78371259
class Solution(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        res = []
        size = 1 << n  # if n=4, left move 4 digits, from 1 to 10000, which is 16 
        for i in range(size):
            res.append((i >> 1) ^ i)
        return res

# V2 
# Time:  O(2^n)
# Space: O(1)
class Solution(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        result = [0]
        for i in range(n):
            for n in reversed(result):
                result.append(1 << i | n)
        return result

# Proof of closed form formula could be found here:
# http://math.stackexchange.com/questions/425894/proof-of-closed-form-formula-to-convert-a-binary-number-to-its-gray-code
class Solution2(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        return [i >> 1 ^ i for i in range(1 << n)]
```