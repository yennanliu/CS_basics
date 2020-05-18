# Queue cheatsheet 

### 1) General form

### 2-1) Queue

### 2-2) Double-ended Queue
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

### 2-3) Stack simulate Queue
