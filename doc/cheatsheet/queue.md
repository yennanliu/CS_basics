# Queue cheatsheet 

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

### 1-2) Queue

### 1-3) Double-ended Queue
```python
# https://pymotw.com/2/collections/deque.html
import collections
d = collections.deque('abcdefg')
print (d)
print (len(d))
print ('left end :', d[0])
print ('right end :', d[-1])
print ('pop :' , d.pop())
print (d)
print ('pop_left:' , d.popleft())
print (d)

```
### 1-4) Stack simulate Queue
