# Bit Manipulation 

## 0) Concept
- Base
    - [Ref](https://leetcode.com/explore/learn/card/bit-manipulation/669/bit-manipulation-concepts/4494/)
    - The actual value of a base-X number is determined by each digit and its location.
    - example :
        - 123.45 (base 10) = 1 * 10^2 + 2 * 10^1 + 3 * 10^0 + 4 * 10^(-1) + 5 * 10^(-2)
        - 720.5 (base 8) = 7 * 8^2 + 2 * 8^1 + 0 * 8^0 + 5 * 8^(-1)
    - In computer science, the binary system is most commonly used. It has two digits: 0, and 1. Octal (base-8) and hexadecimal (base 16) are also commonly used. Octal has eight digits: 0, 1, 2, 3, 4, 5, 6, and 7.

- [bit VS byte VS char](http://web.ntnu.edu.tw/~algo/Bit.html)
    - basic
        - bit : binary number (use 2 as base : 0, 1)
        - Hexadecimal Number : use 16 as base : 0123456789abcdef (lower, upper case are same)
    - byte : 8 bytes (字節)
    - char : 16 bytes (字符)
    - ref:
        - [java example](https://github.com/yennanliu/JavaHelloWorld/blob/main/src/main/java/Advances/IOFlow/demo1.java#L25)
- ref
    - [bit_manipulation.md](https://github.com/yennanliu/CS_basics/blob/master/doc/bit_manipulation.md)
    - [python-operators.html](https://www.runoob.com/python/python-operators.html)
    - [leetcode-easy-bitwise-xor-summary](https://steveyang.blog/2022/07/02/leetcode-easy-bitwise-xor-summary/)

<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/bit_basic1.png" ></p>
<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/bit_basic2.png" ></p>

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Gray Code
```python
# LC 89 Gray Code
# V0
# IDEA : bit op
# https://blog.csdn.net/qqxx6661/article/details/78371259
# DEMO
# i = 0 bin(i) = 0b0 bin(i >> 1) = 0b0 bin(i >> 1) ^ i  = 0b0
# i = 1 bin(i) = 0b1 bin(i >> 1) = 0b0 bin(i >> 1) ^ i  = 0b1
# i = 2 bin(i) = 0b10 bin(i >> 1) = 0b1 bin(i >> 1) ^ i  = 0b11
# i = 3 bin(i) = 0b11 bin(i >> 1) = 0b1 bin(i >> 1) ^ i  = 0b10
# i = 4 bin(i) = 0b100 bin(i >> 1) = 0b10 bin(i >> 1) ^ i  = 0b110
# i = 5 bin(i) = 0b101 bin(i >> 1) = 0b10 bin(i >> 1) ^ i  = 0b111
# i = 6 bin(i) = 0b110 bin(i >> 1) = 0b11 bin(i >> 1) ^ i  = 0b101
# i = 7 bin(i) = 0b111 bin(i >> 1) = 0b11 bin(i >> 1) ^ i  = 0b100
# i = 8 bin(i) = 0b1000 bin(i >> 1) = 0b100 bin(i >> 1) ^ i  = 0b1100
# i = 9 bin(i) = 0b1001 bin(i >> 1) = 0b100 bin(i >> 1) ^ i  = 0b1101
# i = 10 bin(i) = 0b1010 bin(i >> 1) = 0b101 bin(i >> 1) ^ i  = 0b1111
# i = 11 bin(i) = 0b1011 bin(i >> 1) = 0b101 bin(i >> 1) ^ i  = 0b1110
# i = 12 bin(i) = 0b1100 bin(i >> 1) = 0b110 bin(i >> 1) ^ i  = 0b1010
# i = 13 bin(i) = 0b1101 bin(i >> 1) = 0b110 bin(i >> 1) ^ i  = 0b1011
# i = 14 bin(i) = 0b1110 bin(i >> 1) = 0b111 bin(i >> 1) ^ i  = 0b1001
# i = 15 bin(i) = 0b1111 bin(i >> 1) = 0b111 bin(i >> 1) ^ i  = 0b1000
class Solution(object):
    def grayCode(self, n):
        res = []
        size = 2**n
        for i in range(size):
            print ("i = " + str(i) + " bin(i) = " + str(bin(i)) + " bin(i >> 1) = " + str(bin(i >> 1))  + " bin(i >> 1) ^ i  = " + str( bin((i >> 1) ^ i) )  )
            """
            NOTE : 
              step 1) we move 1 digit right in every iteration (i >> 1), for keep adding space
              step 2) we do (i >> 1) ^ i. for getting "inverse" binary code with i
              step 3) append and return the result 
            """
            res.append((i >> 1) ^ i)
        return res

# V1'
# https://ithelp.ithome.com.tw/articles/10213273
# DEMO
# In [23]: add=1

# In [24]: add = add << 1

# In [25]: add
# Out[25]: 2

# In [26]: add = add << 1

# In [27]: add
# Out[27]: 4

# In [28]: add = add << 1

# In [29]: add
# Out[29]: 8

# In [30]: add = add << 1

# In [31]: add
# Out[31]: 16

# In [32]: add = add << 1

# In [33]: add
# Out[33]: 32
#
class Solution:
    def grayCode(self, n):
        res = [0]
        add = 1
        for _ in range(n):
            for i in range(add):
                res.append(res[add - 1 - i] + add);
            add <<= 1
        return res
```

### 2-2) Reverse Bits
```python
# 190. Reverse Bits
# V0
class Solution:
    def reverseBits(self, n):
        s = bin(n)[2:]
        s = "0"*(32 - len(s)) + s
        t = s[::-1]
        return int(t,2)

# V0'
# DEMO
# n = 10100101000001111010011100
# n =       10100101000001111010011100
class Solution:
    def reverseBits(self, n):
        n = bin(n)[2:]         # convert to binary, and remove the usual 0b prefix
        print ("n = " + str(n))
        n = '%32s' % n         # print number into a pre-formatted string with space-padding
        print ("n = " + str(n))
        n = n.replace(' ','0') # Convert the useful space-padding into zeros
        # Now we have a  proper binary representation, so we can make the final transformation
        return int(n[::-1],2)

# V0'' 
class Solution(object):
    def reverseBits(self, n):
        #b = bin(n)[:1:-1]
        b = bin(n)[2:][::-1]
        return int(b + '0'*(32-len(b)), 2)
```

### 2-3) Power of Two
```python
# LC 231. Power of Two
# NOTE : there is also brute force approach
# V0'
# IDEA : BIT OP
# IDEA : Bitwise operators : Turn off the Rightmost 1-bit
# https://leetcode.com/problems/power-of-two/solution/
class Solution(object):
    def isPowerOfTwo(self, n):
        if n == 0:
            return False
        return n & (n - 1) == 0
```

### 2-4) Add Binary
```python
# LC 67. Add Binary
# V0
# IDEA : Bit-by-Bit Computation
class Solution:
    def addBinary(self, a, b):
        n = max(len(a), len(b))
        """
        NOTE : zfill syntax
            -> fill n-1 "0" to a string at beginning

            example :
                In [10]: x = '1'

                In [11]: x.zfill(2)
                Out[11]: '01'

                In [12]: x.zfill(3)
                Out[12]: '001'

                In [13]: x.zfill(4)
                Out[13]: '0001'

                In [14]: x.zfill(10)
                Out[14]: '0000000001'
        """
        a, b = a.zfill(n), b.zfill(n)
        
        carry = 0
        answer = []
        for i in range(n - 1, -1, -1):
            if a[i] == '1':
                carry += 1
            if b[i] == '1':
                carry += 1
                
            if carry % 2 == 1:
                answer.append('1')
            else:
                answer.append('0')
            
            carry //= 2
        
        if carry == 1:
            answer.append('1')
        answer.reverse()
        
        return ''.join(answer)

# V0''''
# IDEA : py default
class Solution:
    def addBinary(self, a, b) -> str:
        return '{0:b}'.format(int(a, 2) + int(b, 2))

# V0''''
# IDEA : Bit Manipulation
class Solution:
    def addBinary(self, a, b) -> str:
        x, y = int(a, 2), int(b, 2)
        while y:
            answer = x ^ y
            carry = (x & y) << 1
            x, y = answer, carry
        return bin(x)[2:]
```

### 2-5) Sum of Two Integers
```python
# 371. Sum of Two Integers
# V0'
# https://blog.csdn.net/fuxuemingzhu/article/details/79379939
#########
# XOR op:
#########
# https://stackoverflow.com/questions/14526584/what-does-the-xor-operator-do
# XOR is a binary operation, it stands for "exclusive or", that is to say the resulting bit evaluates to one if only exactly one of the bits is set.
# -> XOR : RETURN 1 if only one "1", return 0 else 
# -> XOR extra : Exclusive or or exclusive disjunction is a logical operation that is true if and only if its arguments differ. It is symbolized by the prefix operator J and by the infix operators XOR, EOR, EXOR, ⊻, ⩒, ⩛, ⊕, ↮, and ≢. Wikipedia
# a | b | a ^ b
# --|---|------
# 0 | 0 | 0
# 0 | 1 | 1
# 1 | 0 | 1
# 1 | 1 | 0
# This operation is performed between every two corresponding bits of a number.
# Example: 7 ^ 10
# In binary: 0111 ^ 1010
#   0111
# ^ 1010
# ======
#   1101 = 13
class Solution(object):
    def getSum(self, a, b):
        """
        :type a: int
        :type b: int
        :rtype: int
        """
        # 32 bits integer max
        MAX = 2**31-1  #0x7FFFFFFF
        # 32 bits interger min
        MIN = 2**31    #0x80000000
        # mask to get last 32 bits
        mask = 2**32-1 #0xFFFFFFFF
        while b != 0:
            # ^ get different bits and & gets double 1s, << moves carry
            a, b = (a ^ b) & mask, ((a & b) << 1) & mask
        # if a is negative, get a's 32 bits complement positive first
        # then get 32-bit positive's Python complement negative
        return a if a <= MAX else ~(a ^ mask)

# V0'
# https://blog.csdn.net/fuxuemingzhu/article/details/79379939
class Solution():
    def getSum(self, a, b):
        MAX = 2**31-1  #0x7fffffff
        MIN = 2**31    #0x80000000
        mask = 2**32-1 #0xFFFFFFFF
        while b != 0:
            a, b = (a ^ b) & mask, ((a & b) << 1)
        return a if a <= MAX else ~(a ^ mask)
```