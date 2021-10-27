# Python Tricks

## Examples

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