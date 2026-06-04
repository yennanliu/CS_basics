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
- Type of copy : deep copy, shallow copy, reference copy
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

# NOTE : x, z NOT affect on each other

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

### 1-4) transform N based integer to 10 based
```python
# https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/math.md

# How does int(x[,base]) work?
# -> https://stackoverflow.com/questions/33664451/how-does-intx-base-work
# -> int(string, base) accepts an arbitrary base. You are probably familiar with binary and hexadecimal, and perhaps octal; these are just ways of noting an integer number in different bases:
# exmaple :
# In [76]: int('10',2)      # transform '10' from 2 based to 10 based                                                  
# Out[76]: 2
#
# In [77]: int('11',2)      # # transform '11' from 2 based to 10 based                                                 
# Out[77]: 3
#
# In [78]: int('100',2)     # # transform '100' from 2 based to 10 based                                                 
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
        #for x in sorted(d_dict.items(), key=lambda items: -items[1]):
        for _ in sorted(d_dict.items(), key=lambda x: -x[1]):
            res.append(_)
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

### 1-6') add new element to begin of array (in place)
```python
In [1]: x = [1,2,3]

In [2]: x
Out[2]: [1, 2, 3]

In [3]: x.insert(0,0)

In [4]: x
Out[4]: [0, 1, 2, 3]

In [5]: x.insert(0,-1)

In [6]: x
Out[6]: [-1, 0, 1, 2, 3]
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

### 1-8) get Quotient, Remainder of a integer with dividend
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

```python
# LC 937
# https://leetcode.com/problems/reorder-data-in-log-files/solution/
def my_func(input):
    # do sth
    if condition:
        return key1, key2, key3....
    else:
        return key4, key5, key6....

my_array=["a1 9 2 3 1","g1 act car","zo4 4 7","ab1 off key dog","a8 act zoo"]
my_array.sort(key=lambda x : my_func)
```

### 1-11') Descending sort: `key=lambda x: -x[0]` vs `reverse=True` vs `[::-1]`

Three ways to sort descending — each has a distinct use case.

```python
# ── Context: LC 853 Car Fleet ──
# We have pos_speed = [[pos, speed, time], ...] and want to sort by position DESC.

# ── Form 1: negate the key  (in-place, fine-grained control) ──
pos_speed.sort(key=lambda x: -x[0])
# Use when:
#   • You need MIXED direction: primary DESC, secondary ASC
#     e.g. sort(key=lambda x: (-x[0], x[1]))  ← impossible with reverse=True alone
#   • Works for int and float keys

# ── Form 2: reverse=True  (cleaner for single-direction reversal) ──
pos_speed.sort(key=lambda x: x[0], reverse=True)
sorted_cars = sorted(cars, reverse=True)   # creates a NEW list
# Use when:
#   • ALL keys go the same direction (all DESC)
#   • More readable for simple cases
#   • sorted() is preferred over sort() when you need to keep the original

# ── Form 3: sort ASC then reverse/slice  (separate steps) ──
times = [(target - pos) / spe for pos, spe in sorted(cars)]   # ASC sort
for time in times[::-1]:        # iterate in reverse  — does NOT mutate list
    ...
# -- or --
for time in reversed(times):    # same effect, no extra list copy
    ...
# Use when:
#   • You want to keep the sorted-ASC list around for other uses
#   • reversed() is O(1) memory; [::-1] creates a new list copy

# ── Quick comparison ──
# Method              | In-place? | New list? | Mixed direction? | Readability
# -x[0]               |   yes     |    no     |      YES         |  moderate
# reverse=True        |   yes     |    no     |      no          |  high
# sorted(reverse=True)|   no      |    YES    |      no          |  high
# sort ASC + reversed |   yes     |    no     |      no          |  moderate

# ── Multi-key mixed direction example (only negation works here) ──
# Sort by position DESC, then by speed ASC as tiebreaker:
data.sort(key=lambda x: (-x[0], x[1]))
```

### 1-12) get remainder (residual) when divided by a number
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

### 1-16) `zip()`
```python
# python
In [1]: for x, y in zip([-1, 1, 0, 0], [0, 0, -1, 1]):
   ...:     print (x, y)
   ...:
-1 0
1 0
0 -1
0 1

In [2]: for x, y, z in zip([-1, 1, 0, 0], [0, 0, -1, 1], [0,0,0,0]):
   ...:     print (x,y,z)
   ...:
-1 0 0
1 0 0
0 -1 0
0 1 0

In [3]: for x, y, z, u in zip([-1, 1, 0, 0], [0, 0, -1, 1], [0,0,0,0], [9,9,9,9]):
   ...:     print (x,y,z,u)
   ...:
-1 0 0 9
1 0 0 9
0 -1 0 9
0 1 0 9
```

### 1-17) `eval()`
```python
# https://www.runoob.com/python/python-func-eval.html
# https://www.programiz.com/python-programming/methods/built-in/eval
# The eval() method parses the expression passed to this method and runs python expression (code) within the program.

# LC 640
# LC 150

# syntax : eval(expression[, globals[, locals]])

# eample
In [51]: x = 7
    ...: eval('3 * x')
    ...:
Out[51]: 21

In [52]: eval ('2 + 2')
    ...:
Out[52]: 4

In [53]: n = 81
    ...: eval('n + 4')
Out[53]: 85
```

### 1-18) starred ("`*`") expression
```python
# Extended Iterable Unpacking

# https://www.python.org/dev/peps/pep-3132/
# http://swaywang.blogspot.com/2012/01/pythonstarred-expression.html

# example 1
In [38]: a, *b, c = range(5)

In [39]: a
Out[39]: 0

In [40]: b
Out[40]: [1, 2, 3]

In [41]: c
Out[41]: 4

# example 2
In [43]: for a, *b in [(1, 2, 3), (4, 5, 6, 7)]:
    ...:     print ("a = " + str(a) + " b = " + str(b))
    ...:
a = 1 b = [2, 3]
a = 4 b = [5, 6, 7]

# example 3
In [44]: first, *rest = [1, 2, 3, 4, 5]

In [45]: first
Out[45]: 1

In [46]: rest
Out[46]: [2, 3, 4, 5]

# example 4
In [47]: *directories, executable = "/usr/local/bin/vim".split("/")
    ...: print (directories)
    ...: print (executable)
['', 'usr', 'local', 'bin']
vim

# example 5
args = [1,3]
print (range(*args))
```

### 1-19) `datetime() <-> string()`
```python
# LC 681.Next Closest Time
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/next-closest-time.py

from datetime import datetime, timedelta

x = "10:20"

#--------------------------------------
# strptime : string -> datetime 
# (Return a datetime corresponding to date_string, parsed according to format.)
#--------------------------------------
x_datetime =  datetime.strptime(x, "%H:%M")
print (x_datetime)
# 1900-01-01 10:20:00


#--------------------------------------
# strftime : datetime -> string 
# (Return a string representing the date)
#--------------------------------------
x_str = x_datetime.strftime("%H:%M")
print (x_str)
# 10:20

# eatra : timedelta
tmp = x_datetime + timedelta(minutes=10)
print (tmp)
# 1900-01-01 10:30:00
```

### 1-20) `itertools`
```python
# https://docs.python.org/zh-cn/3/library/itertools.html
# https://docs.python.org/zh-tw/3/library/itertools.html

# itertools — Functions creating iterators for efficient looping

#-----------------------------------------------------------------------------------------------------
# example 1 : itertools.accumulate : Aggregated sum
#-----------------------------------------------------------------------------------------------------
In [10]: import itertools
    ...: x = itertools.accumulate(range(10))
    ...: print (list(x))
[0, 1, 3, 6, 10, 15, 21, 28, 36, 45]

#-----------------------------------------------------------------------------------------------------
# example 2 : itertools.combinations : get NON-duplicated elements from collections (with given len)
#-----------------------------------------------------------------------------------------------------
In [15]:  x = itertools.combinations(range(4), 3)

In [16]: print (list(x))
[(0, 1, 2), (0, 1, 3), (0, 2, 3), (1, 2, 3)]

In [17]: x = itertools.combinations(range(4), 4)

In [18]: print (list(x))
[(0, 1, 2, 3)]

#-----------------------------------------------------------------------------------------------------
# example 3 : itertools.combinations_with_replacement : get duplicated or non-duplicated elements from collections (with given len)
#-----------------------------------------------------------------------------------------------------
In [19]: x = itertools.combinations_with_replacement('ABC', 2)

In [20]: print(list(x))
[('A', 'A'), ('A', 'B'), ('A', 'C'), ('B', 'B'), ('B', 'C'), ('C', 'C')]

In [21]: x = itertools.combinations_with_replacement('ABC', 1)

In [22]: print(list(x))
[('A',), ('B',), ('C',)]

In [24]: x = itertools.combinations_with_replacement([1,2,2,1], 2)

In [25]: print (list(x))
[(1, 1), (1, 2), (1, 2), (1, 1), (2, 2), (2, 2), (2, 1), (2, 2), (2, 1), (1, 1)]

#-----------------------------------------------------------------------------------------------------
# example 4 : itertools.compress : filter elements by True/False
#-----------------------------------------------------------------------------------------------------
In [26]: x = itertools.compress(range(5), (True, False, True, True, False))

In [27]: print (list(x))
[0, 2, 3]

#-----------------------------------------------------------------------------------------------------
# example 5 : itertools.count : a counter, can define start point and path len
#-----------------------------------------------------------------------------------------------------
# NOTE THIS !!!!
In [2]: x = itertools.count(start=20, step=-1)

In [3]: print(list(itertools.islice(x, 0, 10, 1)))
[20, 19, 18, 17, 16, 15, 14, 13, 12, 11]

#-----------------------------------------------------------------------------------------------------
# example 6 : itertools.groupby : group by lists by value
#-----------------------------------------------------------------------------------------------------
In [4]: x = itertools.groupby(range(10), lambda x: x < 5 or x > 8)

In [5]: for condition, numbers in x:
   ...:     print(condition, list(numbers))
   ...:
True [0, 1, 2, 3, 4]
False [5, 6, 7, 8]
True [9]

#-----------------------------------------------------------------------------------------------------
# example 7 : itertools.islice : slice on iterator
#-----------------------------------------------------------------------------------------------------
# https://docs.python.org/3/library/itertools.html#itertools.islice
# syntax : itertools.islice(seq, [start,] stop [, step])

In [6]:  x = itertools.islice(range(10), 0, 9, 2)

In [7]: print (list(x))
[0, 2, 4, 6, 8]

#-----------------------------------------------------------------------------------------------------
# example 8 : itertools.permutations : generate all combinations (ordering mattered)
#-----------------------------------------------------------------------------------------------------

In [9]: x = itertools.permutations(range(4), 3)

In [10]: print(list(x))
[(0, 1, 2), (0, 1, 3), (0, 2, 1), (0, 2, 3), (0, 3, 1), (0, 3, 2), (1, 0, 2), (1, 0, 3), (1, 2, 0), (1, 2, 3), (1, 3, 0), (1, 3, 2), (2, 0, 1), (2, 0, 3), (2, 1, 0), (2, 1, 3), (2, 3, 0), (2, 3, 1), (3, 0, 1), (3, 0, 2), (3, 1, 0), (3, 1, 2), (3, 2, 0), (3, 2, 1)]

#-----------------------------------------------------------------------------------------------------
# example 9 : itertools.product : generate multiple lists, and iterators's product
#-----------------------------------------------------------------------------------------------------

In [11]:  x = itertools.product('ABC', range(3))

In [12]: print(list(x))
[('A', 0), ('A', 1), ('A', 2), ('B', 0), ('B', 1), ('B', 2), ('C', 0), ('C', 1), ('C', 2)]
```

### 1-21) move array element to rightmost/leftmost : `remove`, `append`
```python
# LC 146 LRU Cache
In [18]: x
Out[18]: [1, 3, 2]

In [19]: x = [1,2,3]

# NOTE this !!!!
# LC 146
In [20]: x.remove(2)
#x
#[1,2]

In [21]: x.append(2)

In [22]: x
Out[22]: [1, 3, 2]

In [23]:

In [23]: x.remove(1)

In [24]: x.append(1)

In [25]: x
Out[25]: [3, 2, 1]
```

### 1-22) `OrderedDict ` ( hashmap + linked list)
- check [Collection.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/Collection.md)

### 1-23) `filter()`
```python
# https://www.runoob.com/python/python-func-filter.html

#-----------------------------------------------
# syntax : filter(<filter_func>, <iterable>)
#-----------------------------------------------

# note !!! : in py 3, it will return iterable instance; while in py 2, it will return a list directly

#----------------------------
# example 1
#----------------------------
In [13]: def is_odd(n):
    ...:     return n % 2 == 1
    ...:
    ...: newlist = filter(is_odd, [1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
    ...: print(newlist)
<filter object at 0x7fc71c3dced0>

In [14]:

In [14]: list(newlist)
Out[14]: [1, 3, 5, 7, 9]


#----------------------------
# example 2
#----------------------------
In [15]: import math
    ...: def is_sqr(x):
    ...:     return math.sqrt(x) % 1 == 0
    ...:
    ...: newlist = filter(is_sqr, range(1, 101))
    ...: print(newlist)
<filter object at 0x7fc71bb10450>

In [16]:

In [16]: list(newlist)
Out[16]: [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
```

### 1-24) list comprehension
```python
#----------------------------
# example 1
#----------------------------
# https://stackoverflow.com/questions/4260280/if-else-in-a-list-comprehension

In [8]: [ x for x in range(5) ]
Out[8]: [0, 1, 2, 3, 4]

# NOTE this !!!!
In [9]: [ x if x % 2 == 0 else -1 for x in range(5) ]
   ...:
   ...:
Out[9]: [0, -1, 2, -1, 4]

In [10]: def my_func(x):
    ...:     if x % 2 ==0:
    ...:         return True
    ...:     return False
    ...:
    ...: [ x if my_func(x) else 999  for x in range(5)]
Out[10]: [0, 999, 2, 999, 4]
```

### 1-26) Add Two Numbers II,  Decode String
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

### 1-27) bisect : bisect_right, bisect_left (Array bisection algorithm)
- algorithm for `NOT sorting an array eveytime` whenever there is a new inserted element 
```python
# https://docs.python.org/zh-tw/3/library/bisect.html
# src code : https://github.com/python/cpython/blob/3.10/Lib/bisect.py
# https://myapollo.com.tw/zh-tw/python-bisect/
# https://www.liujiangblog.com/course/python/57


"""
NOTE !!! before using bisect, we need SORT the array
"""

#-------------------------------
# bisect_left
#-------------------------------
# will return an idx for inserting new element a, and keep the new array sorted, if element a already existed in array, will insert to "original" a's left idx

# example 1
In [3]: import bisect
   ...: a = [2,4,6]
   ...: idx = bisect.bisect_left(a, 3)
   ...: print (idx)
   ...:
   ...: a.insert(idx, 3)
   ...: print (a)
   ...:
1
[2, 3, 4, 6]


# example 2
In [4]: import bisect
   ...: a = [2, 4, 6]
   ...: idx = bisect.bisect_left(a, 4)
   ...: print (idx)
   ...:
   ...: a.insert(idx, 4)
   ...: print (a)
1
[2, 4, 4, 6]

#-------------------------------
# bisect_right
#-------------------------------
# will return an idx for inserting new element a, and keep the new array sorted, if element a already existed in array, will insert to "original" a's right idx

In [5]:
   ...: import bisect
   ...: a = a = [2, 2, 4, 4, 6, 6, 8, 8]
   ...: idx = bisect.bisect_right(a, 4)
   ...: print (idx)
   ...:
   ...: a.insert(idx, 4)
   ...: print (a)
4
[2, 2, 4, 4, 4, 6, 6, 8, 8]

#-------------------------------
# bisect
#-------------------------------
# bisect.bisect : 
#   -> similar as bisect.bisect_right
#   -> similar as bisect.bisect_left, but will insert to element RIGHT instead
# https://docs.python.org/zh-tw/3/library/bisect.html
# https://blog.csdn.net/qq_34914551/article/details/100062973

# example 1
# In [3]: import bisect
#    ...: a = [2, 4, 6, 8]
#    ...: idx = bisect.bisect(a, 7)
#    ...: print (idx)
#    ...: a.insert(idx, 7)
#    ...: print (a)
# 3
# [2, 4, 6, 7, 8]

#-------------------------------
# insort, insort_right, insort_left
#-------------------------------
# insort, insort_right, insort_left : will get idx and insert to array  (with idx) directly

# example 1
In [8]: import bisect
   ...: a = [2, 4, 6, 8]
   ...: bisect.insort_left(a, 4)
   ...: print (a)
   ...:
[2, 4, 4, 6, 8]

# exmaple 2
In [7]: import bisect
   ...: a = [2, 4, 6, 8]
   ...: bisect.insort_right(a, 4)
   ...: print (a)
[2, 4, 4, 6, 8]
```

### 1-27-1) heapq : Priority Queue (Min Heap by default)

**heapq** - Heap queue algorithm (priority queue)
- Min Heap by default (smallest element at index 0)
- For Max Heap, negate values or use custom comparison
- Common interview use cases: Top K elements, Kth largest/smallest, merge K sorted lists

**References:**
- https://docs.python.org/3/library/heapq.html
- https://github.com/python/cpython/blob/3.10/Lib/heapq.py

```python
import heapq

#-------------------------------
# Basic Heap Operations
#-------------------------------

# Create empty heap
heap = []

# heappush: Add element to heap
# Time: O(log n)
heapq.heappush(heap, 5)
heapq.heappush(heap, 3)
heapq.heappush(heap, 7)
heapq.heappush(heap, 1)
print(heap)  # [1, 3, 7, 5] - min heap property maintained

# heappop: Remove and return smallest element
# Time: O(log n)
smallest = heapq.heappop(heap)
print(smallest)  # 1
print(heap)      # [3, 5, 7]

# heappushpop: Push then pop (more efficient than separate operations)
# Time: O(log n)
result = heapq.heappushpop(heap, 2)  # Push 2, then pop smallest
print(result)  # 2
print(heap)    # [3, 5, 7]

# heapreplace: Pop then push (more efficient than separate operations)
# Time: O(log n)
result = heapq.heapreplace(heap, 4)  # Pop smallest, then push 4
print(result)  # 3
print(heap)    # [4, 5, 7]

#-------------------------------
# Convert List to Heap
#-------------------------------

# heapify: Transform list into heap in-place
# Time: O(n) - more efficient than n × heappush
nums = [5, 7, 9, 1, 3]
heapq.heapify(nums)
print(nums)  # [1, 3, 9, 7, 5] - min heap

#-------------------------------
# Top K Elements (Most Common Interview Pattern)
#-------------------------------

# nsmallest: Find k smallest elements
# Time: O(n log k)
nums = [5, 7, 9, 1, 3, 4, 6, 8, 2]
k_smallest = heapq.nsmallest(3, nums)
print(k_smallest)  # [1, 2, 3]

# nlargest: Find k largest elements
# Time: O(n log k)
k_largest = heapq.nlargest(3, nums)
print(k_largest)  # [9, 8, 7]

# With key function (common in LC problems)
people = [(1, 'Alice'), (3, 'Bob'), (2, 'Charlie')]
top_2_by_id = heapq.nsmallest(2, people, key=lambda x: x[0])
print(top_2_by_id)  # [(1, 'Alice'), (2, 'Charlie')]

#-------------------------------
# Max Heap Pattern (Negate Values)
#-------------------------------

# Python heapq is min heap, for max heap: negate values
max_heap = []
for val in [5, 7, 9, 1, 3]:
    heapq.heappush(max_heap, -val)  # Negate for max heap

# Get largest element
largest = -heapq.heappop(max_heap)
print(largest)  # 9

# Example: Top K Frequent Elements
from collections import Counter
def topKFrequent(nums, k):
    count = Counter(nums)
    # Use negative frequency for max heap
    return heapq.nlargest(k, count.keys(), key=count.get)

#-------------------------------
# Merge K Sorted Lists/Arrays
#-------------------------------

# merge: Merge multiple sorted iterables
# Time: O(n log k) where k = number of iterables
list1 = [1, 3, 5]
list2 = [2, 4, 6]
list3 = [0, 7, 8]
merged = list(heapq.merge(list1, list2, list3))
print(merged)  # [0, 1, 2, 3, 4, 5, 6, 7, 8]

# Custom comparison for merge
# Example: Merge by second element of tuple
data1 = [(1, 'a'), (3, 'c')]
data2 = [(2, 'b'), (4, 'd')]
merged = list(heapq.merge(data1, data2, key=lambda x: x[0]))
print(merged)  # [(1, 'a'), (2, 'b'), (3, 'c'), (4, 'd')]

#-------------------------------
# Common LeetCode Patterns
#-------------------------------

# Pattern 1: Kth Largest Element (LC 215)
def findKthLargest(nums, k):
    # Use min heap of size k
    heap = nums[:k]
    heapq.heapify(heap)

    for num in nums[k:]:
        if num > heap[0]:
            heapq.heapreplace(heap, num)

    return heap[0]

# Pattern 2: Top K Frequent Elements (LC 347)
def topKFrequent(nums, k):
    count = Counter(nums)
    return heapq.nlargest(k, count.keys(), key=count.get)

# Pattern 3: Kth Smallest in Sorted Matrix (LC 378)
def kthSmallest(matrix, k):
    """
    Use heap to track smallest elements across rows
    """
    n = len(matrix)
    heap = []

    # Add first element from each row
    for r in range(min(k, n)):
        heapq.heappush(heap, (matrix[r][0], r, 0))

    result = 0
    for _ in range(k):
        result, r, c = heapq.heappop(heap)
        if c + 1 < len(matrix[0]):
            heapq.heappush(heap, (matrix[r][c+1], r, c+1))

    return result

# Pattern 4: Merge K Sorted Lists (LC 23)
def mergeKLists(lists):
    """
    Merge k sorted linked lists
    """
    heap = []
    # Initialize heap with first node from each list
    for i, lst in enumerate(lists):
        if lst:
            heapq.heappush(heap, (lst.val, i, lst))

    dummy = ListNode(0)
    current = dummy

    while heap:
        val, i, node = heapq.heappop(heap)
        current.next = node
        current = current.next

        if node.next:
            heapq.heappush(heap, (node.next.val, i, node.next))

    return dummy.next

# Pattern 5: Sliding Window Maximum (LC 239)
# Note: Usually solved with deque, but can use heap
def maxSlidingWindow(nums, k):
    """
    Use max heap (negate values) with index tracking
    """
    heap = []
    result = []

    for i, num in enumerate(nums):
        # Add to max heap (negate for max heap)
        heapq.heappush(heap, (-num, i))

        # Remove elements outside window
        while heap[0][1] <= i - k:
            heapq.heappop(heap)

        # Window is full
        if i >= k - 1:
            result.append(-heap[0][0])

    return result

#-------------------------------
# Advanced: Custom Comparison with Classes
#-------------------------------

# For complex objects, use tuples or dataclass with functools.total_ordering
from dataclasses import dataclass
from functools import total_ordering

@total_ordering
@dataclass
class Task:
    priority: int
    name: str

    def __lt__(self, other):
        return self.priority < other.priority

# Use with heapq
task_heap = []
heapq.heappush(task_heap, Task(3, "Low priority"))
heapq.heappush(task_heap, Task(1, "High priority"))
heapq.heappush(task_heap, Task(2, "Medium priority"))

top_task = heapq.heappop(task_heap)
print(top_task)  # Task(priority=1, name='High priority')

#-------------------------------
# Performance Tips
#-------------------------------

# 1. Use heapify() instead of repeated heappush() - O(n) vs O(n log n)
# SLOW:
heap = []
for num in nums:
    heapq.heappush(heap, num)  # O(n log n)

# FAST:
heap = nums[:]
heapq.heapify(heap)  # O(n)

# 2. Use nsmallest/nlargest for small k
# When k << n: nsmallest/nlargest are optimized
# When k ≈ n: just sort the array

# 3. For Top K problems with streaming data: maintain heap of size k
```

**Common Interview Problems Using heapq:**
- LC 215: Kth Largest Element in an Array
- LC 347: Top K Frequent Elements
- LC 373: Find K Pairs with Smallest Sums
- LC 378: Kth Smallest Element in a Sorted Matrix
- LC 23: Merge k Sorted Lists
- LC 295: Find Median from Data Stream (use 2 heaps)
- LC 253: Meeting Rooms II (interval scheduling)
- LC 767: Reorganize String (greedy + heap)

**Summary:**
- ✅ heapq provides efficient min heap (priority queue)
- ✅ O(log n) for push/pop, O(1) for peek (heap[0])
- ✅ O(n) for heapify, O(n log k) for nsmallest/nlargest
- ✅ For max heap: negate values or use `-val`
- ✅ For custom comparison: use tuple ordering or implement `__lt__`


### 1-28) useful `functools` modules
- functools.lru_cache
    - implement cache via LRU (Least Recently Used (LRU) cache) in py
- ref
    - https://walkonnet.com/archives/451257
    - https://docs.python.org/3/library/functools.html
```python
# example 1
@lru_cache
def count_vowels(sentence):
    return sum(sentence.count(vowel) for vowel in 'AEIOUaeiou')

# example 2
@lru_cache(maxsize=32)
def get_pep(num):
    'Retrieve text of a Python Enhancement Proposal'
    resource = 'https://www.python.org/dev/peps/pep-%04d/' % num
    try:
        with urllib.request.urlopen(resource) as s:
            return s.read()
    except urllib.error.HTTPError:
        return 'Not Found'

# example 3
@lru_cache(maxsize=None)
def fib(n):
    if n < 2:
        return n
    return fib(n-1) + fib(n-2)

# example 4
@api.route("/user/info", methods=["GET"])
@functools.lru_cache()
@login_require
def get_userinfo_list():
    userinfos = UserInfo.query.all()
    userinfo_list = [user.to_dict() for user in userinfos]
    return jsonify(userinfo_list)
```

### 1-29) fill "0" to String
```python
# LC 67. Add Binary
#NOTE : zfill syntax
#    -> fill n-1 "0" to a string at beginning

#example :
In [10]: x = '1'

In [11]: x.zfill(2)
Out[11]: '01'

In [12]: x.zfill(3)
Out[12]: '001'

In [13]: x.zfill(4)
Out[13]: '0001'

In [14]: x.zfill(10)
Out[14]: '0000000001'
```

### 1-30) `collections.defaultdict`
```python
# defaultdict never raises KeyError — returns a default value for missing keys
from collections import defaultdict

#----------------------------
# example 1 : int (default 0)
#----------------------------
d = defaultdict(int)
for ch in "aabbbc":
    d[ch] += 1
print(dict(d))  # {'a': 2, 'b': 3, 'c': 1}

#----------------------------
# example 2 : list (default [])
#----------------------------
graph = defaultdict(list)
edges = [(0,1),(0,2),(1,3)]
for u, v in edges:
    graph[u].append(v)
    graph[v].append(u)
# graph[0] -> [1, 2],  graph[99] -> []  (no KeyError)

#----------------------------
# example 3 : set (default set())
#----------------------------
d = defaultdict(set)
d['key'].add(1)
d['key'].add(2)
print(d['key'])  # {1, 2}

#----------------------------
# example 4 : nested defaultdict (adjacency matrix with weights)
#----------------------------
dist = defaultdict(lambda: defaultdict(lambda: float('inf')))
dist[0][1] = 5
print(dist[0][1])   # 5
print(dist[0][99])  # inf
```

### 1-31) `collections.Counter`
```python
from collections import Counter

#----------------------------
# basic usage
#----------------------------
c = Counter("aabbbc")
print(c)            # Counter({'b': 3, 'a': 2, 'c': 1})
print(c['b'])       # 3
print(c['z'])       # 0  (no KeyError, returns 0)

# most_common(k) : top k frequent elements
print(c.most_common(2))   # [('b', 3), ('a', 2)]

# Counter arithmetic
c1 = Counter("aab")
c2 = Counter("abc")
print(c1 + c2)  # Counter({'a': 3, 'b': 2, 'c': 1})
print(c1 - c2)  # Counter({'a': 1})  (only positive counts)
print(c1 & c2)  # Counter({'a': 1, 'b': 1})  intersection (min)
print(c1 | c2)  # Counter({'a': 2, 'b': 1, 'c': 1})  union (max)

# update (add) vs subtract
c = Counter({'a': 3})
c.update({'a': 1, 'b': 2})
print(c)    # Counter({'a': 4, 'b': 2})
c.subtract({'a': 2})
print(c)    # Counter({'a': 2, 'b': 2})

# LC 347 Top K Frequent Elements
from collections import Counter
def topKFrequent(nums, k):
    return [x for x, _ in Counter(nums).most_common(k)]
```

### 1-32) `collections.deque` (Double-Ended Queue)
```python
from collections import deque

# O(1) append/pop from BOTH ends (list.pop(0) is O(n))
d = deque([1, 2, 3])

d.append(4)       # [1, 2, 3, 4]
d.appendleft(0)   # [0, 1, 2, 3, 4]
d.pop()           # removes 4  -> [0, 1, 2, 3]
d.popleft()       # removes 0  -> [1, 2, 3]

# maxlen: auto-evicts oldest element (sliding window)
d = deque(maxlen=3)
for i in range(5):
    d.append(i)
print(d)  # deque([2, 3, 4], maxlen=3)

#----------------------------
# BFS template with deque
#----------------------------
def bfs(graph, start):
    visited = set([start])
    queue = deque([start])
    while queue:
        node = queue.popleft()
        for neighbor in graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)

#----------------------------
# Monotonic deque (LC 239 Sliding Window Maximum)
#----------------------------
def maxSlidingWindow(nums, k):
    d = deque()   # stores indices, decreasing order of values
    result = []
    for i, num in enumerate(nums):
        while d and nums[d[-1]] <= num:
            d.pop()
        d.append(i)
        if d[0] == i - k:   # front out of window
            d.popleft()
        if i >= k - 1:
            result.append(nums[d[0]])
    return result
```

### 1-33) `any()` operator
```python
# Returns True if ANY element in iterable is True (short-circuits)
In [1]: any([False, False, True])
Out[1]: True

In [2]: any([False, False, False])
Out[2]: False

In [3]: any(x > 3 for x in [1, 2, 5])
Out[3]: True

# Complement to all():
# all() -> every element must be True
# any() -> at least one element must be True
```

### 1-34) `enumerate()`
```python
# Returns (index, value) pairs — avoids manual index tracking
fruits = ['apple', 'banana', 'cherry']

for i, v in enumerate(fruits):
    print(i, v)
# 0 apple
# 1 banana
# 2 cherry

# start parameter
for i, v in enumerate(fruits, start=1):
    print(i, v)
# 1 apple  2 banana  3 cherry

# Build index map (very common in LC)
s = "abcba"
idx_map = {v: i for i, v in enumerate(s)}
print(idx_map)  # {'a': 4, 'b': 3, 'c': 2}  (last occurrence)
```

### 1-35) String character operations: `ord()`, `chr()`, `isalpha()`, `isdigit()`
```python
#-------------------------------
# ord() : char -> ASCII int
# chr() : ASCII int -> char
#-------------------------------
print(ord('a'))   # 97
print(ord('A'))   # 65
print(ord('0'))   # 48

print(chr(97))    # 'a'
print(chr(65))    # 'A'

# Common pattern: normalize letter to 0-25 index
ch = 'c'
idx = ord(ch) - ord('a')   # 2

# Shift a character by n positions
def shift(ch, n):
    return chr((ord(ch) - ord('a') + n) % 26 + ord('a'))

#-------------------------------
# String check methods
#-------------------------------
"abc".isalpha()     # True  — all letters
"abc123".isalpha()  # False
"123".isdigit()     # True  — all digits
"abc123".isalnum()  # True  — letters + digits
"  ".isspace()      # True
"ABC".isupper()     # True
"abc".islower()     # True

# LC 125 Valid Palindrome
def isPalindrome(s):
    filtered = [c.lower() for c in s if c.isalnum()]
    return filtered == filtered[::-1]

# NOTE: lower() has NO effect on digits — safe to call on any alphanumeric char
# >>> "0".lower()
# '0'
# >>> "A".lower()
# 'a'
# So the one-liner below works for both letters and numbers:
fixed_s = ''.join(c.lower() for c in s if c.isalnum())
```

### 1-36) Set operations
```python
a = {1, 2, 3, 4}
b = {3, 4, 5, 6}

# Basic ops
a | b    # union        {1, 2, 3, 4, 5, 6}
a & b    # intersection {3, 4}
a - b    # difference   {1, 2}
a ^ b    # symmetric diff {1, 2, 5, 6}

# Membership: O(1) average
3 in a   # True

# Mutation
a.add(5)
a.discard(99)   # no error if missing (vs remove() which raises KeyError)
a.remove(1)     # raises KeyError if missing

# NOTE: can directly remove a specific element from a set by value (not index)
# Common in sliding window problems (LC 3)
seen = set()
seen.add('a')
seen.remove('a')   # removes 'a' directly — no index needed

# Set comprehension
squares = {x**2 for x in range(5)}   # {0, 1, 4, 9, 16}

# Freeze (hashable, usable as dict key)
fs = frozenset([1, 2, 3])
```

### 1-37) Dict `get()`, `setdefault()`, comprehension
```python
d = {'a': 1, 'b': 2}

# get(key, default) — safe access
d.get('c', 0)     # 0  (no KeyError)
d.get('a', 0)     # 1

# setdefault(key, default) — insert if missing, return value
d.setdefault('c', []).append(3)   # d['c'] = [3]
d.setdefault('c', []).append(4)   # d['c'] = [3, 4]

# dict comprehension
squares = {x: x**2 for x in range(5)}
# {0: 0, 1: 1, 2: 4, 3: 9, 4: 16}

# invert a dict (assuming unique values)
inv = {v: k for k, v in squares.items()}

# filter dict
evens = {k: v for k, v in squares.items() if v % 2 == 0}
```

### 1-38) Infinity and boundary values
```python
# Use float('inf') / float('-inf') instead of sys.maxsize for clarity
INF = float('inf')
NEG_INF = float('-inf')

# Works with min/max comparisons
min_val = float('inf')
for x in [3, 1, 4, 1, 5]:
    min_val = min(min_val, x)
print(min_val)  # 1

# Common in DP initialization
dp = [[float('inf')] * n for _ in range(m)]

# Python int has no overflow — safe to use large numbers
# But float('inf') is cleaner for "unbounded" semantics
```

### 1-39) 2D array (matrix) initialization
```python
#-------------------------------------------------
# CORRECT way — use list comprehension (independent rows)
#-------------------------------------------------
m, n = 3, 4
grid = [[0] * n for _ in range(m)]
grid[0][0] = 1
# Only grid[0][0] is changed

#-------------------------------------------------
# WRONG way — all rows share the same list!
#-------------------------------------------------
bad = [[0] * n] * m
bad[0][0] = 1
# ALL rows become [1, 0, 0, 0]  — common bug!

#-------------------------------------------------
# Common DP patterns
#-------------------------------------------------
# 1D DP
dp = [0] * (n + 1)

# 2D DP (m rows, n cols, filled with False)
dp = [[False] * (n + 1) for _ in range(m + 1)]

# Fill with infinity
dp = [[float('inf')] * n for _ in range(m)]
```

### 1-40) `nonlocal` and `global` in nested functions
```python
# nonlocal: modify a variable in the ENCLOSING (not global) scope
def outer():
    count = 0
    def inner():
        nonlocal count
        count += 1
    inner()
    inner()
    print(count)  # 2

# Without nonlocal, count += 1 raises UnboundLocalError

# global: modify a module-level variable inside a function
total = 0
def add(x):
    global total
    total += x

# Common LC pattern: DFS with mutable result
def maxDepth(root):
    res = [0]
    def dfs(node, depth):
        if not node:
            return
        res[0] = max(res[0], depth)  # list trick avoids nonlocal
        dfs(node.left, depth + 1)
        dfs(node.right, depth + 1)
    dfs(root, 1)
    return res[0]
```

### 1-41) `min()` / `max()` with `key`
```python
# key= works exactly like sort's key= parameter
nums = [-3, -1, 2, 4]
print(max(nums, key=abs))   # -3  (largest absolute value)
print(min(nums, key=abs))   # -1  (smallest absolute value)

# With iterable of tuples
points = [(1, 5), (3, 2), (2, 8)]
print(max(points, key=lambda p: p[1]))  # (2, 8)

# min/max with default (avoids error on empty iterable)
print(min([], default=0))   # 0

# clamp a value between lo and hi
val = max(lo, min(val, hi))
```

### 1-42) Ternary (conditional) expression
```python
# syntax: <value_if_true> if <condition> else <value_if_false>
x = 5
result = "even" if x % 2 == 0 else "odd"   # "odd"

# Nested ternary (keep shallow — hard to read beyond two levels)
sign = "positive" if x > 0 else ("zero" if x == 0 else "negative")

# Common LC use
ans = left if left else right          # return whichever is not None
val = node.val if node else 0
```

### 1-43) `pow(x, n, mod)` — fast modular exponentiation
```python
# Built-in 3-arg pow is O(log n), much faster than (x**n) % mod
MOD = 10**9 + 7

print(pow(2, 10, MOD))    # 1024
print(pow(2, 100, MOD))   # 976371285  (computed efficiently)

# Modular inverse (when mod is prime): pow(a, mod-2, mod)
inv = pow(3, MOD - 2, MOD)   # modular inverse of 3

# LC 50 Pow(x, n) — manual fast power
def myPow(x, n):
    if n < 0:
        x, n = 1 / x, -n
    res = 1
    while n:
        if n % 2 == 1:
            res *= x
        x *= x
        n //= 2
    return res
```

### 1-44) `reduce()` from functools
```python
from functools import reduce

# reduce(func, iterable[, initializer])
# Applies func cumulatively: func(func(a, b), c) ...

product = reduce(lambda a, b: a * b, [1, 2, 3, 4])   # 24
total   = reduce(lambda a, b: a + b, [1, 2, 3, 4], 0) # 10

# XOR all elements (LC 136 Single Number)
from functools import reduce
import operator
result = reduce(operator.xor, [4, 1, 2, 1, 2])  # 4
# equivalent to:
result = reduce(lambda a, b: a ^ b, [4, 1, 2, 1, 2])
```

### 1-45) String methods cheatsheet
```python
s = "  Hello, World!  "

# Strip whitespace (or specific chars)
s.strip()          # "Hello, World!"
s.lstrip()         # "Hello, World!  "
s.rstrip()         # "  Hello, World!"
s.strip("!")       # "  Hello, World!  " (only strips specified chars from ends)

# Case
s.lower()          # "  hello, world!  "
s.upper()          # "  HELLO, WORLD!  "
s.title()          # "  Hello, World!  "
s.swapcase()       # "  hELLO, wORLD!  "

# Search
s.find("World")    # 9  (-1 if not found)
s.index("World")   # 9  (raises ValueError if not found)
s.count("l")       # 3
s.startswith("  H")  # True
s.endswith("!  ")    # True

# Replace and join
s.replace("World", "Python")   # "  Hello, Python!  "
", ".join(["a", "b", "c"])     # "a, b, c"
"a,b,c".split(",")             # ['a', 'b', 'c']

# Reverse a string
rev = s[::-1]

# String multiplication
"ab" * 3   # "ababab"
"-" * 10   # "----------"

# Check if string is a palindrome
def is_palindrome(s):
    return s == s[::-1]
```

### 1-46) Useful built-ins: `sorted()`, `reversed()`, `sum()`, `abs()`
```python
# sorted() returns a NEW list; list.sort() is in-place
nums = [3, 1, 4, 1, 5]
print(sorted(nums))            # [1, 1, 3, 4, 5]  — nums unchanged
print(sorted(nums, reverse=True))  # [5, 4, 3, 1, 1]

# Sort list of tuples: primary key asc, secondary key desc
pairs = [(1, 3), (2, 1), (1, 5)]
pairs.sort(key=lambda x: (x[0], -x[1]))
# [(1, 5), (1, 3), (2, 1)]

# reversed() returns an iterator
for x in reversed([1, 2, 3]):
    print(x)  # 3 2 1

list(reversed([1,2,3]))  # [3, 2, 1]

# sum() with start
sum([1, 2, 3], 10)   # 16

# sum of 2D list
matrix = [[1,2],[3,4]]
total = sum(sum(row) for row in matrix)  # 10

# abs()
abs(-5)    # 5
abs(3+4j)  # 5.0  (complex magnitude)
```

### 1-47) `map()` and generator expressions
```python
# map(func, iterable) — lazy, returns iterator
nums = ["1", "2", "3"]
ints = list(map(int, nums))    # [1, 2, 3]

# map with lambda
doubled = list(map(lambda x: x * 2, [1, 2, 3]))  # [2, 4, 6]

# Generator expression (lazy list comprehension) — memory efficient
gen = (x**2 for x in range(1000000))   # nothing computed yet
total = sum(x**2 for x in range(1000000))  # computed on the fly

# Prefer generator expression over list comprehension inside sum/any/all/max/min
max_val = max(abs(x) for x in nums)
has_neg = any(x < 0 for x in nums)
```

### 1-48) `int` tricks: integer division `//`, bit operations
```python
#-------------------------------
# Integer division (floor division)
#-------------------------------
7 // 2    # 3
-7 // 2   # -4  (rounds toward -inf, NOT toward 0!)
int(-7/2) # -3  (truncation toward 0)

# Safe mid-point (avoids overflow in other languages)
lo, hi = 0, 100
mid = (lo + hi) // 2

#-------------------------------
# Bit operations (common in LC)
#-------------------------------
# AND, OR, XOR, NOT
5 & 3    # 1   (101 & 011 = 001)
5 | 3    # 7   (101 | 011 = 111)
5 ^ 3    # 6   (101 ^ 011 = 110)
~5       # -6  (bitwise NOT: ~x = -(x+1))

# Shift
5 << 1   # 10  (multiply by 2)
5 >> 1   # 2   (floor divide by 2)

# Check/set/clear bit i
x = 13          # 1101
x & (1 << i)    # check bit i (non-zero if set)
x | (1 << i)    # set bit i
x & ~(1 << i)   # clear bit i
x ^ (1 << i)    # flip bit i

# Count set bits
bin(13).count('1')   # 3
# or: use Brian Kernighan
def count_bits(n):
    count = 0
    while n:
        n &= n - 1   # clear lowest set bit
        count += 1
    return count

# x & (x-1) removes lowest set bit — useful for power-of-2 check
def is_power_of_two(n):
    return n > 0 and (n & (n - 1)) == 0
```

### 1-49) `isinstance()` and type checking
```python
isinstance(3, int)        # True
isinstance(3.0, float)    # True
isinstance("hi", str)     # True
isinstance([], list)      # True
isinstance({}, dict)      # True

# Check multiple types at once
isinstance(3, (int, float))   # True

# type() for exact type (no inheritance)
type(3) == int    # True
type(True) == int # False  (bool is subclass of int, but type() is exact)
isinstance(True, int)  # True  (True IS an int!)
```

### 1-50) Common LeetCode patterns cheatsheet
```python
#-------------------------------
# Sliding window template
#-------------------------------
def sliding_window(s, k):
    left = 0
    window = {}
    result = 0
    for right, ch in enumerate(s):
        window[ch] = window.get(ch, 0) + 1
        while len(window) > k:       # shrink condition
            lch = s[left]
            window[lch] -= 1
            if window[lch] == 0:
                del window[lch]
            left += 1
        result = max(result, right - left + 1)
    return result

#-------------------------------
# Binary search template
#-------------------------------
def binary_search(nums, target):
    lo, hi = 0, len(nums) - 1
    while lo <= hi:
        mid = (lo + hi) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            lo = mid + 1
        else:
            hi = mid - 1
    return -1

# Binary search on answer (find leftmost valid value)
def binary_search_left(lo, hi, feasible):
    while lo < hi:
        mid = (lo + hi) // 2
        if feasible(mid):
            hi = mid
        else:
            lo = mid + 1
    return lo

#-------------------------------
# DFS template (iterative)
#-------------------------------
def dfs_iterative(graph, start):
    visited = set()
    stack = [start]
    while stack:
        node = stack.pop()
        if node in visited:
            continue
        visited.add(node)
        for neighbor in graph[node]:
            stack.append(neighbor)

#-------------------------------
# Backtracking template
#-------------------------------
def backtrack(result, current, choices):
    if is_complete(current):
        result.append(current[:])
        return
    for choice in choices:
        current.append(choice)
        backtrack(result, current, next_choices(choice))
        current.pop()

#-------------------------------
# Union-Find (Disjoint Set Union)
#-------------------------------
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # path compression
        return self.parent[x]

    def union(self, x, y):
        px, py = self.find(x), self.find(y)
        if px == py:
            return False
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1
        return True

#-------------------------------
# Trie (Prefix Tree)
#-------------------------------
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False

class Trie:
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word):
        node = self.root
        for ch in word:
            node = node.children.setdefault(ch, TrieNode())
        node.is_end = True

    def search(self, word):
        node = self.root
        for ch in word:
            if ch not in node.children:
                return False
            node = node.children[ch]
        return node.is_end

    def startsWith(self, prefix):
        node = self.root
        for ch in prefix:
            if ch not in node.children:
                return False
            node = node.children[ch]
        return True
```