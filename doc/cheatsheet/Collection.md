# Collection 

## 0) Concept  

### 0-1) Types

- Types

- Algorithm
    - dict/collections op
        - collections.Counter
        - collections.Counter().most_common()
    - sort
    - get most freq
    - get sub-string with validated alphabets
    - custom sort
        - LC 791

- Data structure
    - dict
    - set
    - array

### 0-2) Pattern

## 1) General form

#### 1-0) get element if existed in collecitons (custom sort)
```python
# LC 791. Custom Sort String
# ...
s_map = Counter(s)
res = ""
for o in order:
    if o in s_map:
        res += (o * s_map[o])
        del s_map[o]
for s in s_map:
    res += (s * s_map[s])
# ...
```

#### 1-1) collection.Counter
```python
import collections
s = ['a','b','c','c']
c= collections.Counter(s)
print (c)
print (c.keys())
print (c.values())
```

#### 1-2) Important method - most_common()
```python
# 451 Sort Characters By Frequency
import collections
s = ['a','b','c','c']
count = collections.Counter(s).most_common()
for item, freq in count:
    print (item, freq)   
#c 2
#a 1
#b 1
```

#### 1-3) collection.defaultdict (int, list...)
```python
import collections
s = ['a','b','c','c']
count = collections.defaultdict(int)
for i in s:
    count[i] += 1 

print (count)
print (dict(count))
```

```python
import collections
s=[('yellow', 1), ('blue', 2), ('yellow', 3), ('blue', 4), ('red', 1)]
count = collections.defaultdict(list)
for k, v in s:
    count[k].append(v)

print (count)
print (count.keys())
print (count.values())
print(count.items())
```  

#### 1-3) collection.update()
```python
# LC # 554 rick Wall
import collections
In [87]: _counter = collections.Counter()

In [88]: _counter
Out[88]: Counter()

In [89]: _counter.update([1])

In [90]: _counter
Out[90]: Counter({1: 1})

In [91]: _counter.update([1])

In [92]: _counter
Out[92]: Counter({1: 2})

In [93]: _counter.update([2])

In [94]: _counter
Out[94]: Counter({1: 2, 2: 1})
```

### 1-4) `OrderedDict ` ( hashmap + linked list)
```python
# LC 146 LRU Cache

# There is a structure called ordered dictionary, it combines behind both hashmap and linked list. In Python this structure is called OrderedDict and in Java LinkedHashMap.

# https://docs.python.org/3/library/collections.html#collections.OrderedDict
# https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/367557/
# https://www.w3help.cc/a/202107/420653.html

"""

# OrderedDict = hashmap + linked list
# CAN make dict ordering (default dict is NOT ordering)
# Return an instance of a dict subclass that has methods specialized for rearranging dictionary order

* popitem(last=True)
    The popitem() method for ordered dictionaries returns and removes a (key, value) pair. The pairs are returned in LIFO order if last is true or FIFO order if false.

* move_to_end(key, last=True)
    Move an existing key to either end of an ordered dictionary. The item is moved to the right end if last is true (the default) or to the beginning if last is false. Raises KeyError if the key does not exist:

"""

#----------------------------
# example 0
#----------------------------

# default dict
In [34]: d = {}
    ...: d['a'] = 'A'
    ...: d['b'] = 'B'
    ...: d['c'] = 'C'
    ...: d['d'] = 'D'
    ...: d['e'] = 'E'
    ...:
    ...: for k, v in d.items():
    ...:     print (k, v)
    ...:
# NON ordering
a A
b B
c C
d D
e E

# OrderedDict
In [35]: from collections import OrderedDict
    ...: d = OrderedDict()
    ...: d['a'] = 'A'
    ...: d['b'] = 'B'
    ...: d['c'] = 'C'
    ...: d['d'] = 'D'
    ...: d['e'] = 'E'
    ...:
    ...: for k, v in d.items():
    ...:     print (k, v)
    ...:

# ordering !!!
a A
b B
c C
d D
e E


#----------------------------
# example 1
#----------------------------
In [28]:  d = OrderedDict.fromkeys('abcde')

In [29]: d
Out[29]: OrderedDict([('a', None), ('b', None), ('c', None), ('d', None), ('e', None)])

In [30]: d.move_to_end('b')

In [31]: "".join(d)
Out[31]: 'acdeb'

In [32]:

In [32]: d.move_to_end('b', last=False)

In [33]: "".join(d)
Out[33]: 'bacde'

#----------------------------
# example 2
#----------------------------
class LastUpdatedOrderedDict(OrderedDict):
    'Store items in the order the keys were last added'

    def __setitem__(self, key, value):
        super().__setitem__(key, value)
        self.move_to_end(key)

#----------------------------
# example 3
#----------------------------
from time import time

class TimeBoundedLRU:
    "LRU Cache that invalidates and refreshes old entries."

    def __init__(self, func, maxsize=128, maxage=30):
        self.cache = OrderedDict()      # { args : (timestamp, result)}
        self.func = func
        self.maxsize = maxsize
        self.maxage = maxage

    def __call__(self, *args):
        if args in self.cache:
            self.cache.move_to_end(args)
            timestamp, result = self.cache[args]
            if time() - timestamp <= self.maxage:
                return result
        result = self.func(*args)
        self.cache[args] = time(), result
        if len(self.cache) > self.maxsize:
            self.cache.popitem(0)
        return result
```

## 2) LC Example

### 2-1) Custom Sort String
```python
# LC 791. Custom Sort String
# V0
# IDEA : COUNTER
from collections import Counter
class Solution(object):
    def customSortString(self, order, s):
        s_map = Counter(s)
        res = ""
        for o in order:
            if o in s_map:
                res += (o * s_map[o])
                del s_map[o]
        for s in s_map:
            res += s * s_map[s]
        return res
```