# FAQ for python 

## String 

### 1) Write a func return all indexes for a substring exist in a string. i.e. `x = '99023430990999', y = '99'. The func should return [0, 8, 11, 12]`

```python 

def findall(p, s):
    '''Yields all the positions of
    the pattern p in the string s.'''
    # python find module 
    # https://www.w3schools.com/python/ref_string_find.asp
    # string.find(value, start, end)
    # start	: Optional. Where to start the search. Default is 0
    # end: Optional. Where to end the search. Default is to the end of the string
    # so s.find(p, i+1) means to find the next elment 
    i = s.find(p)
    while i != -1:
        yield i
        i = s.find(p, i+1)

# demo 1 
# x = '99023430990999'
# y = '99'
# [i for i in findall(y, x)]
# demo 2 
# x = '195378678'
# y = '78'
# [i for i in findall(y, x)]

```
