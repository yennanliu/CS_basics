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