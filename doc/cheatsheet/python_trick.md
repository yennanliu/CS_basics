# Python Tricks

## Examples

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
import collections
class Solution(object):
    def frequencySort(self, s):
        d = collections.Counter(s)
        d_dict = dict(d)
        res = ""
        for x in sorted(d_dict, key=lambda k : -d_dict[k]):
            res += x * d_dict[x]
        return res
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