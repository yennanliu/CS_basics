# Python FAQ

## Concept 

#### 1) Range VS Xrange ?

- The only difference is that range returns a Python list object and xrange returns an xrange object. It means that xrange doesn't actually generate a static list at run-time like range does. It creates the values as you need them with a special technique called yielding. This technique is used with a type of object known as generators.  

- That means that if you have a really gigantic range you'd like to generate a list for, say one billion, xrange is the function to use. This is especially true if you have a really memory sensitive system such as a cell phone that you are working with, as range will use as much memory as it can to create your array of integers, which can result in a MemoryError and crash your program. It's a memory hungry beast.

- That being said, if you'd like to iterate over the list multiple times, it's probably better to use range. This is because xrange has to generate an integer object every time you access an index, whereas range is a static list and the integers are already "there" to use.

- https://www.pythoncentral.io/how-to-use-pythons-xrange-and-range/

#### 2) Decorator in python ?

#### 3) Generators (yield method) in python ?

- The idea of generators is to calculate a series of results one-by-one on demand (on the fly). In the simplest case, a generator can be used as a list, where each element is calculated lazily. Lets compare a list and a generator that do the same thing - return powers of two:

```python 

>>> # First, we define a list
>>> the_list = [2**x for x in range(5)]
>>>
>>> # Type check: yes, it's a list
>>> type(the_list)
<class 'list'>
>>>
>>> # Iterate over items and print them
>>> for element in the_list:
...     print(element)
...
1
2
4
8
16
>>>
>>> # How about the length?
>>> len(the_list)
5
>>>
>>> # Ok, now a generator.
>>> # As easy as list comprehensions, but with '()' instead of '[]':
>>> the_generator = (x+x for x in range(3))
>>>
>>> # Type check: yes, it's a generator
>>> type(the_generator)
<class 'generator'>
>>>
>>> # Iterate over items and print them
>>> for element in the_generator:
...     print(element)
...
0
2
4
>>>
>>> # Everything looks the same, but the length...
>>> len(the_generator)
Traceback (most recent call last):
  File "", line 1, in
TypeError: object of type 'generator' has no len()

```

- Iterating over the list and the generator looks completely the same. However, although the `generator` is iterable, `it is not a collection`, and thus has no length. Collections (lists, tuples, sets, etc) keep all values in memory and we can access them whenever needed.`A generator calculates the values on the fly and forgets them`, so it does not have any overview about the own result set.

- Generators are especially useful for memory-intensive tasks, where there is no need to keep all of the elements of a memory-heavy list accessible at the same time. Calculating a series of values one-by-one can also be useful in situations where the complete result is never needed, yielding intermediate results to the caller until some requirement is satisfied and further processing stops.

- https://www.pythoncentral.io/python-generators-and-yield-keyword/

#### 4) list comprehensions ?

- List comprehensions provide a concise way to create lists.  It consists of brackets containing an expression followed by a for clause, then
zero or more for or if clauses. The expressions can be anything, meaning you can
put in all kinds of objects in lists.  The result will be a new list resulting from evaluating the expression in the context of the for and if clauses which follow it. 

- The list comprehension always returns a result list. 

```python

# for loop append to a list 
new_list = []
for i in old_list:
    if filter(i):
        new_list.append(expressions(i))

# via list comprehensions  (for loop append to a list )
new_list = [expression(i) for i in old_list if filter(i)]

```

- https://www.pythonforbeginners.com/basics/list-comprehensions-in-python

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

## Subarray 
#### 1) Find the sub-array as list by multiple?

```python

array = [ i for i in range(31) ]
multiple_2 = array[2::2]
multiple_3 = array[3::3]
multiple_5 = array[5::5]

# In [42]: multiple_2
# Out[42]: [2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30]

# In [43]: multiple_3
# Out[43]: [3, 6, 9, 12, 15, 18, 21, 24, 27, 30]

# In [44]: multiple_5
# Out[44]: [5, 10, 15, 20, 25, 30]

```



