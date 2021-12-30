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


#-------------------------------------------------
# CASE 3) deep copy : copy "parent" instance, AND sub instance
#-------------------------------------------------
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

# pattern
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
# LC 973. K Closest Points to Origin
# IDEA : sort + lambda
class Solution(object):
    def kClosest(self, points, K):
        points.sort(key = lambda x : x[0]**2 +  x[1]**2)
        return points[:K]
```