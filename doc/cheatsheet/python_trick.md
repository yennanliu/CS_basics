# Python Tricks

## 1) Examples

### 0-1) inverse looping elements in string
```python
In [5]: def check(x):
            #### note here : range(len(x)-1,-1, -1
            # (end at idx = -1)
   ...:     for i in range(len(x)-1,-1, -1):
   ...:         #print (x[i])
   ...:         print (i)
   ...:
   ...:
   ...: x = "332"
   ...: check(x)
2
1
0
```

### 0-2) assignment VS shallow copy VS deep copy
- https://www.runoob.com/w3cnote/python-understanding-dict-copy-shallow-or-deep.html
- https://iter01.com/578999.html
```python
# LC 138

#-------------------------------------------------
# CASE 1) assignment : point to the same instance
#-------------------------------------------------

In [112]: z = [1,2,3]

In [113]: x = y = z

In [114]: x
Out[114]: [1, 2, 3]

In [115]: y
Out[115]: [1, 2, 3]

In [116]: z
Out[116]: [1, 2, 3]

In [117]: z.append(4)

In [118]: z
Out[118]: [1, 2, 3, 4]

In [119]: x
Out[119]: [1, 2, 3, 4]

In [120]: y
Out[120]: [1, 2, 3, 4]

In [121]: z
Out[121]: [1, 2, 3, 4]


#-------------------------------------------------
# CASE 2) shallow copy : copy "parent" instance, but NOT sub instance
#-------------------------------------------------

# https://docs.python.org/zh-tw/3/tutorial/datastructures.html
# form 1
a.copy()

# form 2
a[:]

# demo
In [90]: x = [1,2,3]

In [91]: y = x[:]

In [92]: y
Out[92]: [1, 2, 3]

In [93]: x.append(4)

In [94]: x
Out[94]: [1, 2, 3, 4]

In [96]: y
Out[96]: [1, 2, 3]


# LC 77 Combinations
class Solution(object):
    def combine(self, n, k):
        result = []
        
        def dfs(current, start):
            if(len(current) == k):
                result.append(current[:])
                return
            
            for i in range(start, n + 1):
                current.append(i)
                dfs(current, i + 1)
                current.pop()
            
        dfs([], 1)
        return result

#-------------------------------------------------
# CASE 3) deep copy : copy "parent" instance, AND sub instance
#-------------------------------------------------

import copy

In [25]: import copy
    ...:
    ...: x = [1,2,3]
    ...: z =  copy.deepcopy(x)
    ...:
    ...: x
Out[25]: [1, 2, 3]

In [26]:

In [26]: z
Out[26]: [1, 2, 3]

In [27]: x.append(4)

In [28]: x
Out[28]: [1, 2, 3, 4]

In [29]: z
Out[29]: [1, 2, 3]

In [31]: z.append(5)

In [32]: z
Out[32]: [1, 2, 3, 5]

In [33]: x
Out[33]: [1, 2, 3, 4]
```

### 1-1) Or logic for either existed element
```python
In [8]: def test(l1, l2):
   ...:     if l1 or l2:
   ...:         return l1 or l2
   ...:
   ...: res = test("l1", None)
   ...: print (res)
   ...:
   ...: res2 = test(None, "l2")
   ...: print (res2)
l1
l2
```

### 1-2) swap for longer array
```python
if len(l1) < len(l2):
   l1, l2 = l2, l1
```

### 1-3) fina numeric items from a string
```python
# LC 008
s = '4193 with words'
res = re.search('(^[\+\-]?\d+)', s).group()
print (res)
```

### 1-4) transform String to Int with N based
```python
# How does int(x[,base]) work?
# -> https://stackoverflow.com/questions/33664451/how-does-intx-base-work
# -> int(string, base) accepts an arbitrary base. You are probably familiar with binary and hexadecimal, and perhaps octal; these are just ways of noting an integer number in different bases:
# exmaple :
# In [76]: int('10',2)      # transform '10' to a 2 bases int                                                 
# Out[76]: 2
#
# In [77]: int('11',2)      # transform '11' to a 2 bases int                                                      
# Out[77]: 3
#
# In [78]: int('100',2)     # transform '100' to a 2 bases int                                                       
# Out[78]: 4

# LC 089
```

### 1-5) Sort freq on dict
```python
# LC 451 Sort Characters By Frequency
# V1
import collections
class Solution(object):
    def frequencySort(self, s):
        d = collections.Counter(s)
        d_dict = dict(d)
        res = []
        for x in sorted(d_dict, key=lambda k : -d_dict[k]):
            res.append(x)
        return res

x= [1, 2, 3, 1, 2, 1, 2, 1]
s = Solution()
r = s.frequencySort(x)

# V2
# https://stackoverflow.com/questions/613183/how-do-i-sort-a-dictionary-by-value
import collections
class Solution(object):
    def frequencySort(self, s):
        d = collections.Counter(s)
        d_dict = dict(d)
        res = []
        for x in sorted(d_dict.items(), key=lambda items: -items[1]):
            res.append(x)
        return res

x= [1, 2, 3, 1, 2, 1, 2, 1]
s = Solution()
r = s.frequencySort(x)
```

### 1-6) Insert into array (in place)
```python

# syntax : 
# arr.insert(<index>,<value>)
In [12]: x = [1,2,3]
    ...: x.insert(2,77)

In [13]: x
Out[13]: [1, 2, 77, 3]
```

### 1-7) sort string
```python
def _sort(x):
    _x = list(x)
    _x.sort()
    return "".join(_x)

x = "bca"
print (x)
x_ = _sort(x)
print (x_)
```

### 1-8) get Quotient, Remainder on a integer
```python
In [1]: x,y = divmod(100, 3)

In [2]: x
Out[2]: 33

In [3]: y
Out[3]: 1
```

### 1-9) *all* operator
- Will return Boolean (true or false) per condition for ALL elements in a list
```python
# example 1
In [36]: a = "000"

In [37]: all( i == "0" for i in a )
Out[37]: True

# example 2
In [38]: b = "abc123"

In [39]: all ( i == "a" for i in b )
Out[39]: False

# LC 763. Partition Labels
class Solution(object):
    def partitionLabels(self, s):
        d = {val:idx for idx, val in enumerate(list(s))}
        #print (d)
        res = []
        tmp = set()
        for idx, val in enumerate(s):
            """
            NOTE : below condition
            """
            if idx == d[val] and all(idx >= d[t] for t in tmp):
                res.append(idx+1)
            else:
                tmp.add(val)
        _res = [res[0]] + [ res[i] - res[i-1] for i in range(1, len(res)) ]
        return _res
```

### 1-10) `not` logic
```python
#----------------------------
# can be either None, [], ""
#----------------------------
In [32]: x = None

In [34]: not x
Out[34]: True

In [35]: y = []

In [36]: not y
Out[36]: True

In [37]: z = ""

In [38]: not z
Out[38]: True
```

### 1-11) `sort` on a `lambda func`
```python
# example 1
# LC 973. K Closest Points to Origin
# IDEA : sort + lambda
class Solution(object):
    def kClosest(self, points, K):
        points.sort(key = lambda x : x[0]**2 +  x[1]**2)
        return points[:K]


# example 2
In [28]: def my_func(x):
    ...:     return x**2
    ...:
    ...: x = [-4,-5,0,1,2,5]
    ...: x.sort(key=lambda x: my_func(x))
    ...: print (x)
[0, 1, 2, -4, -5, 5]
```

### 1-12) get remainder (residue) when divided by a number
```python
#-----------------
# V1 : %=
#-----------------

In [7]: x = 100

In [8]: x %= 60

In [9]: x
Out[9]: 40

In [10]: y = 120

In [11]: y %= 60

In [12]: y
Out[12]: 0

#-----------------
# V2 : divmod
#-----------------
In [13]: a = 100

In [14]: q, r = divmod(a, 60)

In [15]: q
Out[15]: 1

In [16]: r
Out[16]: 40

In [17]: b = 120

In [18]: q2, r2 = divmod(b, 60)

In [19]: q2
Out[19]: 2

In [20]: r2
Out[20]: 0
```

```python
# LC 1010
# V0
# IDEA : dict
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        rem = {}
        pairs = 0
        for t in time:
            #print ("rem = " + str(rem))
            t %= 60
            if (60 - t) % 60 in rem:
                pairs += rem[(60 - t) % 60]
            if t not in rem:
                rem[t] = 1
            else:
                rem[t] += 1
        return pairs
```

### 1-13) `split` method
```python
# python
# syntax : split(separator, number_of_split_result) 
# example 1
# In [17]: x = 'dig1 8 1 5 1'

# In [18]: x
# Out[18]: 'dig1 8 1 5 1'

# In [19]: x.split(" ")
# Out[19]: ['dig1', '8', '1', '5', '1']

# In [20]: x.split(" ", 1)
# Out[20]: ['dig1', '8 1 5 1']

# In [21]: x.split(" ", 2)
# Out[21]: ['dig1', '8', '1 5 1']

# In [22]: x.split(" ", 3)
# Out[22]: ['dig1', '8', '1', '5 1']

# In [23]: x.split(" ", 4)
# Out[23]: ['dig1', '8', '1', '5', '1']

# In [24]: x.split(" ", 100)
# Out[24]: ['dig1', '8', '1', '5', '1']

# example 2
# LC 937 Reorder Data in Log Files
class Solution:
    def reorderLogFiles(self, logs):
        def f(log):
            id_, rest = log.split(" ", 1)
            return (0, rest, id_) if rest[0].isalpha() else (1,)

        logs.sort(key = lambda x : f(x))
        return logs #sorted(logs, key = f)
```


### 1-13) array `extend`
```python
# LC 969. Pancake Sorting

In [10]: x = [1,2,3]

In [11]: x.extend([4])

In [12]: x
Out[12]: [1, 2, 3, 4]

In [13]: x = [1,2,3]

In [14]: x = x + [4]

In [15]: x
Out[15]: [1, 2, 3, 4]
```


### 1-14) math `ceil`
```python
# https://www.runoob.com/python/func-number-ceil.html
# https://www.runoob.com/python/func-number-ceil.html

"""
The method ceil(x) in Python returns a ceiling value of x 
-> i.e., the SMALLEST integer GREATER than or EQUAL to x.
"""
In [9]:
   ...: import math
   ...:
   ...: # prints the ceil using ceil() method
   ...: print ("math.ceil(-23.11) : ", math.ceil(-23.11))
   ...: print ("math.ceil(300.16) : ", math.ceil(300.16))
   ...: print ("math.ceil(300.72) : ", math.ceil(300.72))
math.ceil(-23.11) :  -23
math.ceil(300.16) :  301
math.ceil(300.72) :  301


# LC 875. Koko Eating Bananas
#...
# Iterate over the piles and calculate hour_spent.
# We increase the hour_spent by ceil(pile / middle)
for pile in piles:
    # python ceil : https://www.runoob.com/python/func-number-ceil.html
    hour_spent += math.ceil(pile / middle)
# Check if middle is a workable speed, and cut the search space by half.
if hour_spent <= h:
    right = middle
else:
    left = middle + 1
#...
```

### 1-15) math `floor`
```python
# https://www.geeksforgeeks.org/floor-ceil-function-python/

"""
floor() method in Python returns the floor of x 
-> i.e., the LARGEST integer NOT GREATER than x. 
"""

# This will import math module
import math   
  
In [8]: import math
   ...:
   ...: # prints the ceil using floor() method
   ...: print ("math.floor(-23.11) : ", math.floor(-23.11))
   ...: print ("math.floor(300.16) : ", math.floor(300.16))
   ...: print ("math.floor(300.72) : ", math.floor(300.72))
math.floor(-23.11) :  -24
math.floor(300.16) :  300
math.floor(300.72) :  300
```

### 1-15) for loop dict
```python
d = {'a':1, 'b':2, 'c': 3}
# loop over key, value
for k, v in d.items():
    print (k, v)

# loop over key
for k in d.keys():
    print (k)

# loop over value
for v in d.values():
    print (v)
```