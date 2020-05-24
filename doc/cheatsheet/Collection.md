# Collection cheatsheet 

### 1) collection.Counter
```python
import collections
s = ['a','b','c','c']
c= collections.Counter(s)
print (c)
print (c.keys())
print (c.values())
```

### 1-1) Important method - most_common()
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

### 2) collection.defaultdict (int, list...)
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