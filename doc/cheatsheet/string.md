# String 

> algorithm/LC relative to string/text/array -> string

## 0) Concept  

### 0-1) Types
- Group sub-string
    - LC 696
- Rotate String
    - LC 796

### 0-2) Pattern

### 1-1) Basic OP

#### 1-1) String plus

#### 1-2) String bit op
- [bit_manipulation.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/bit_manipulation.md)

#### 1-3) String Multiplication

#### 1-4) loop over string

#### 1-5) inverse loop over string
```python
# exmaple 1
x = "abcd"
for i in range(len(x)-1, -1, -1):
    print (i)
# 3
# 2
# 1
# 0

# exmaple 2
for i in range(len(x)-1, -1, -2):
    print (i)
# 3
# 1
```

#### 1-6) String -> List, List -> String
```python
# exmaple 1
x = "abcd"
x_list = list(x)
x_list

# exmaple 2
y = ["d","e","f"]
y_string = "".join(y)
y_string

# exmaple 3 : join with a splitor
y = ["d","e","f"]
y_string = ",".join(y)
y_string
```

#### 1-7) Tricks
```python
# go through elements in str AVOID index out of range error
x = '1234'

for i in range(len(x)):
    if  i == len(x)-1 or x[i] != x[i+1]:
        print (x[i])
```

```python
# string -> array

a = 1234
a_array = list(str(a))

In [12]: a_array
Out[12]: ['1', '2', '3', '4']
```

```java
// java
// split string (java)
/** NOTE !!! split string via .split("") */

 for (String x : s.split("")){
    System.out.println(x);
 }
```


#### 1-8) Group sub-string
```python
# LC 696. Count Binary Substrings
# ...
groups = [1]
for i in range(1, len(s)):
    """
    NOTE here !!!
    """
    if s[i-1] != s[i]:
        groups.append(1)
    else:
        groups[-1] += 1
ans = 0
for i in range(1, len(groups)):
    """
    NOTE here !!!
    """
    ans += mins(groups[i-1], groups[i])
# ...
```

#### 1-9) Rotate string
```python
# LC 796. Rotate String
class Solution(object):
    def rotateString(self, A, B):
        for i in range(len(A)):
            if A[i:] + A[:i] == B:
                return True
        return False
```

## 2) LC Example

### 2-1) Compare Version Number
- go through 2 string, keep comparing digits in eash string
```python
# 165 Compare Version Number
# V0
# IDEA : STRING + while op
class Solution(object):
    def compareVersion(self, version1, version2):
        # edge case
        if not version1 and not version2:
            return
        # split by "." as list
        v_1 = version1.split(".")
        v_2 = version2.split(".")
        # compare
        while v_1 and v_2:
            tmp1 = int(v_1.pop(0))
            tmp2 = int(v_2.pop(0))

            if tmp1 > tmp2:
                return 1
            elif tmp1 < tmp2:
                return -1
        # if v_1 remains
        if v_1:
            while v_1:
                tmp1 = int(v_1.pop(0))
                if tmp1 != 0:
                    return 1
        # if v_2 remains
        if v_2:
            while v_2:
                tmp2 = int(v_2.pop(0))
                if tmp2 != 0:
                    return -1
        return 0

# V0'
# IDEA : STRING
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

### 2-2) Add Two Numbers II,  Decode String
- String -> Int
```python
# 445 Add Two Numbers II
# 394 Decode String
def str_2_int(x):
    r=0
    for i in x:
        r = int(r)*10 + int(i)
        print (i, r)
    return r

def str_2_int_v2(x):
    res = 0
    for i in x:
        res = (res + int(i) % 10) * 10
    return int(res / 10)

# example 1
x="131"
r=str_2_int(x)
print (r)
# 1 1
# 3 13
# 1 131
# 131

# examle 2
In [62]: z
Out[62]: '5634'

In [63]: ans = 0

In [64]: for i in z:
    ...:     ans = 10 * ans + int(i)
    ...:

In [65]: ans
Out[65]: 5634
```

### 2-3) Count and say
```python
# LC 038 Count and say
# V0
# IDEA : ITERATION
class Solution:
    def countAndSay(self, n):
        
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

### 2-4) Monotone Increasing Digits
```python
# LC 738 Monotone Increasing Digits
class Solution:
    def monotoneIncreasingDigits(self, N):
        s = list(str(N));
        ### NOTICE HERE 
        for i in range(len(s) - 2,-1,-1):
            # if int(s[i]) > int(s[i+1]) -> the string is not `monotone increase`
            # -> we need to find the next biggest int, 
            # -> so we need to make all right hand side digit as '9'
            # -> and minus current digit with 1  (s[i] = str(int(s[i]) - 1))
            if int(s[i]) > int(s[i+1]):
                ### NOTICE HERE 
                for j in range(i+1,len(s)):
                    s[j] = '9'
                s[i] = str(int(s[i]) - 1)
        s = "".join(s)        
        return int(s) 
```

### 2-5) Validate IP Address
```python
# LC 468. Validate IP Address
# V0
# IDEA : Divide and Conquer
class Solution:
    def validate_IPv4(self, IP):
        nums = IP.split('.')
        for x in nums:
            # Validate integer in range (0, 255):
            # 1. length of chunk is between 1 and 3
            if len(x) == 0 or len(x) > 3:
                return "Neither"
            # 2. no extra leading zeros
            # 3. only digits are allowed
            # 4. less than 255
            if x[0] == '0' and len(x) != 1 or not x.isdigit() or int(x) > 255:
                return "Neither"
        return "IPv4"
    
    def validate_IPv6(self, IP):
        nums = IP.split(':')
        hexdigits = '0123456789abcdefABCDEF'
        for x in nums:
            # Validate hexadecimal in range (0, 2**16):
            # 1. at least one and not more than 4 hexdigits in one chunk
            # 2. only hexdigits are allowed: 0-9, a-f, A-F
            if len(x) == 0 or len(x) > 4 or not all(c in hexdigits for c in x):
                return "Neither"
        return "IPv6"
        
    def validIPAddress(self, IP):
        if IP.count('.') == 3:
            return self.validate_IPv4(IP)
        elif IP.count(':') == 7:
            return self.validate_IPv6(IP)
        else:
            return "Neither"
```

### 2-6) String to Integer (atoi)
```python
# LC 008
# V0'
# IDEA : string op
class Solution(object):
    def myAtoi(self, _str):
        _str = _str.strip()
        number = 0
        flag = 1
        print ("_str = " + str(_str))
        if not _str:
            return 0
        if _str[0] == '-':
            _str = _str[1:]
            flag = -1
        elif _str[0] == '+':
            _str = _str[1:]
        for c in _str:
            #if c >= '0' and c <= '9':  # '3' > '2' -> True
            if c in [str(x) for x in range(10)]:
                """
                str(int) -> ord demo

                Example 1 :
                In [55]: for i in range(10):
                        ...: print (str(i) + " ord = " + str(ord(str(i))))
                        ...:
                                0 ord = 48
                                1 ord = 49
                                2 ord = 50
                                3 ord = 51
                                4 ord = 52
                                5 ord = 53
                                6 ord = 54
                                7 ord = 55
                                8 ord = 56
                                9 ord = 57

                Example 2 :

                            In [62]: z
                            Out[62]: '5634'

                            In [63]: ans = 0

                            In [64]: for i in z:
                                ...:     ans = 10 * ans + int(i)
                                ...:

                            In [65]: ans
                            Out[65]: 5634
                """
                #number = 10*number + ord(c) - ord('0')  # _string to integer 
                number = 10*number + int(c)  # _string to integer , above is OK as well
            else:
                break
        res = flag * number
        res = res if res <= 2**31 - 1 else 2**31 - 1    # 2**31 == 2147483648
        res = res if res >= -1 * 2**31  else -1 * 2**31   # -(1)*(2**31) == - 2147483648
        return res
```

### 2-7) License Key Formatting
```python
# LC 482. License Key Formatting
# ref : LC 725. Split Linked List in Parts

# V0
class Solution(object):
    def licenseKeyFormatting(self, S, K):
        result = []
        for i in reversed(range(len(S))):
            if S[i] == '-':
                continue
            if len(result) % (K + 1) == K:
                result += '-'
            result += S[i].upper()
        return "".join(reversed(result))

# V0'
# IDEA : string op + brute force
class Solution(object):
    def licenseKeyFormatting(self, s, k):
        # edge case
        if not s or not k:
            return s
        s = s.replace("-", "")
        s_ = ""
        for _ in s:
            if _.isalpha():
                s_ += _.upper()
            else:
                s_ += _

        s_ = list(s_)
        #print ("s_ = " + str(s_))
        s_len = len(s)
        remain = s_len % k
        #res = []
        res = ""
        tmp = ""
        # if s_len % k != 0
        while remain != 0:
            tmp += s_.pop(0)
            remain -= 1
        #res.append(tmp)
        res += (tmp + "-")
        tmp = ""
        # if s_len % k == 0
        for i in range(0, len(s_), k):
            #print (s_[i:i+k])
            #res.append(s_[i:i+k])
            res += ("".join(s_[i:i+k]) + "-")
        return res.strip("-")
```

### 2-8) Repeated String Match
```python
# LC 686. Repeated String Match
# V0
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/repeated-string-match/discuss/108090/Intuitive-Python-2-liner
# -> if there is a sufficient solution, B must "inside" A
# -> Let n be the answer, 
# -> Let x be the theoretical lower bound, which is ceil(len(B)/len(A)).
# -> the value of n can br ONLY "x" or "x + 1"
# -> e.g. : in the case where len(B) is a multiple of len(A) like in A = "abcd" and B = "cdabcdab") and not more. Because if B is already in A * n, B is definitely in A * (n + 1).
# --> So all we need to check whether are:
#       -> 1) B in A * x
#         or
#       -> 2) B in A * (x+1)
# -> return -1 if above contitions are not met
class Solution(object):
    def repeatedStringMatch(self, A, B):
        sa, sb = len(A), len(B)
        x = 1
        while (x - 1) * sa <= 2 * max(sa, sb):
            if B in A * x: 
                return x
            x += 1
        return -1

# V0'
class Solution(object):
    def repeatedStringMatch(self, a, b):
        # edge case
        if not a and b:
            return -1
        if (not a and not b) or (a == b) or (b in a):
            return 1
        res = 1
        sa = len(a)
        sb = len(b)
        #while res * sa <= 3 * max(sa, sb):  # this condition is OK as well
        while (res-1) * sa <= 2 * max(sa, sb):
            a_ = res * a
            if b in a_:
                return res
            res += 1
        return -1
```

### 2-9) Count Binary Substrings
```python
# LC 696. Count Binary Substrings
# V0 
# IDEA :  Group By Character + continous sub-string
# https://leetcode.com/problems/count-binary-substrings/solution/
# https://blog.csdn.net/fuxuemingzhu/article/details/79183556
# IDEA :
#   -> for x = “0110001111”, how many continuous "0" or "1"
#   -> [1,2,3,4]
#   -> So, if we want to find # of "equal 0 and 1 sub string"
#   -> all we need to do : min(3,4) = 3. e.g. ("01", "0011", "000111")
#   -> since for every "cross" sub string (e.g. 0 then 1 or 1 then 0),
#   -> we can the "number of same continuous 0 and 1"  by min(groups[i-1], groups[i])
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1

        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V1
# IDEA :  Group By Character
# https://leetcode.com/problems/count-binary-substrings/solution/
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1

        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V1''
# IDEA :  Linear Scan
# https://leetcode.com/problems/count-binary-substrings/solution/
class Solution(object):
    def countBinarySubstrings(self, s):
        ans, prev, cur = 0, 0, 1
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                ans += min(prev, cur)
                prev, cur = cur, 1
            else:
                cur += 1

        return ans + min(prev, cur)
```

### 2-10) Roman to Integer
```python
# LC 13. Roman to Integer
# V0
class Solution(object):
    def romanToInt(self, s):
        # helper ref
        roman = {"I":1, "V":5, "X":10, "L":50, "C":100, "D":500, "M":1000}
        # NOTE : we init res as below
        res = roman[s[-1]]
        N = len(s)
        """
        2 cases:
            case 1) XY, X > Y -> res = X - Y
            case 2) XY, X < Y -> res = X + Y
        """
        for i in range(N - 2, -1, -1):
            # case 1
            if roman[s[i]] < roman[s[i + 1]]:
                res -= roman[s[i]]
            # case 2
            else:
                res += roman[s[i]]
        return res
```

### 2-11) ount Unique Characters of All Substrings of a Given String
```python
# LC 828. Count Unique Characters of All Substrings of a Given String
# V0
class Solution(object):
     def uniqueLetterString(self, S):
            index = {c: [-1, -1] for c in 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'}
            res = 0
            for i, c in enumerate(S):
                k, j = index[c]
                res += (i - j) * (j - k)
                index[c] = [j, i]
            for c in index:
                k, j = index[c]
                res += (len(S) - j) * (j - k)
            return res % (10**9 + 7)

# V1
# https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/discuss/128952/C%2B%2BJavaPython-One-pass-O(N)
# IDEA :
# Let's think about how a character can be found as a unique character.
# Think about string "XAXAXXAX" and focus on making the second "A" a unique character.
# We can take "XA(XAXX)AX" and between "()" is our substring.
# We can see here, to make the second "A" counted as a uniq character, we need to:
# insert "(" somewhere between the first and second A
# insert ")" somewhere between the second and third A
# For step 1 we have "A(XA" and "AX(A", 2 possibility.
# For step 2 we have "A)XXA", "AX)XA" and "AXX)A", 3 possibilities.
# So there are in total 2 * 3 = 6 ways to make the second A a unique character in a substring.
# In other words, there are only 6 substring, in which this A contribute 1 point as unique string.
# Instead of counting all unique characters and struggling with all possible substrings,
# we can count for every char in S, how many ways to be found as a unique char.
# We count and sum, and it will be out answer.
class Solution(object):
     def uniqueLetterString(self, S):
            index = {c: [-1, -1] for c in string.ascii_uppercase}
            res = 0
            for i, c in enumerate(S):
                k, j = index[c]
                res += (i - j) * (j - k)
                index[c] = [j, i]
            for c in index:
                k, j = index[c]
                res += (len(S) - j) * (j - k)
            return res % (10**9 + 7)
```

### 2-12) Palindromic Substrings
```python
# LC 647. Palindromic Substrings
# V0
# IDEA : BRUTE FORCE
class Solution(object):
    def countSubstrings(self, s):
        count = 0
        # NOTE: since i from 0 to len(s) - 1, so for j we need to "+1" then can get go throgh all elements in str
        for i in range(len(s)):
            # Note : for j we need to "+1"
            for j in range(i+1, len(s)+1):
                if s[i:j] == s[i:j][::-1]:
                    count += 1
        return count
```

### 2-13) Repeated Substring Pattern
```python
# LC 459. Repeated Substring Pattern
# V0
# IDEA : # only have to go through till HALF of s's length, since it's not possbile to find the SubstringPattern if len(s[:x]) > size//2
class Solution(object):
    def repeatedSubstringPattern(self, s):
        _len_s = len(s)
        i = 0
        tmp = ""
        while i < _len_s:
            if i == 0:
                multiply = 0
            if i != 0:
                multiply = _len_s // i
            if multiply * tmp == s:
                return True
            if i > _len_s // 2:
                return False
            tmp += s[i]
            i += 1
        return False
```