# Collection 

## 0) Concept  

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) collection.Counter
```python
import collections
s = ['a','b','c','c']
c= collections.Counter(s)
print (c)
print (c.keys())
print (c.values())
```

### 1-2) Important method - most_common()
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

### 1-3) collection.defaultdict (int, list...)
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

### 1-3) collection.update()
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