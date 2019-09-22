# Python FAQ

## String 

#### 1) Write a func return all indexes for a substring exist in a string. 
- i.e. `x = '99023430990999', y = '99'. The func should return [0, 8, 11, 12]`

```python 

def findall(x, y):
    '''Yields all the positions of
    the pattern p in the string s.'''
    # python find module 
    # https://www.w3schools.com/python/ref_string_find.asp
    # string.find(value, start, end)
    # start : Optional. Where to start the search. Default is 0
    # end: Optional. Where to end the search. Default is to the end of the string
    # so s.find(p, i+1) means to find the next elment 
    i = x.find(y)
    while i != -1:
        yield i
        i = x.find(y, i+1)


def findall_2(x, y):
    res = []
    i = x.find(y)
    while i != -1:
         res.append(i)
         i = x.find(y, i+1)
    return res 

# demo 1 
# x = '99023430990999'
# y = '99'
# [i for i in findall(x, y)]
# demo 2 
# x = '195378678'
# y = '78'
# [i for i in findall(x, y)]

```
## Defaultdict 

#### 1) Given an array : richer = [[1,0],[2,1],[3,1],[3,7],[4,3],[5,3],[6,3]] with the form :[ppl, richer ppl]
`(i.e.:`0>1`,`1>2`,`1>3`...) `Find the list 
of ppl with ppl that richer than them?

```python 
richer = [[1,0],[2,1],[3,1],[3,7],[4,3],[5,3],[6,3]]
import collections 
ppl_richer = collections.defaultdict(list)
for i, j in richer:
    #ppl_richer[j].append(i)
    ppl_richer[i].append(j)

print (ppl_richer)
#>>> defaultdict(<class 'list'>, {1: [0], 2: [1], 3: [1, 7], 4: [3], 5: [3], 6: [3]})


```

