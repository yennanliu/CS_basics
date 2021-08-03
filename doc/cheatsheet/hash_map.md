# Hash Map

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern

## 1) General form
<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/hash_table_2.png" ></p>

- Definition 

- When to use 
	- Use case that need data IO with ~ O(1) time complexity

- When Not to use
	- When data is time sequence 
	- When data in in ordering 
	- https://www.reddit.com/r/learnprogramming/comments/29t4s4/when_is_it_bad_to_use_a_hash_table/

- Hash Collisions
	- Chaining 
	- Open addressing

- Ref 
	- https://blog.techbridge.cc/2017/01/21/simple-hash-table-intro/
	- https://www.freecodecamp.org/news/hash-tables/

### 1-1) Basic OP

- `setdefault()`
	- https://www.w3schools.com/python/ref_dictionary_setdefault.asp
```python
# 662 Maximum Width of Binary Tree
car = {
  "brand": "Ford",
  "model": "Mustang",
  "year": 1964
}

# get key "brand" value
print (car.setdefault("brand"))
# insert key "my_key"
print (car.setdefault("my_key"))
print (car)
# add new key "color" and its value white
print (car.setdefault("color", "white"))
print (car)

```

- `sort` on ***hashmap (dict)***
```python
# https://stackoverflow.com/questions/613183/how-do-i-sort-a-dictionary-by-value

x = {1: 2, 3: 4, 4: 3, 2: 1, 0: 0}
sorted_x = sorted(x.items(), key=lambda kv: kv[1])
print (sorted_x)
# [(0, 0), (2, 1), (1, 2), (4, 3), (3, 4)]


x = {1: 2, 3: 4, 4: 3, 2: 1, 0: 0}
sorted_x = sorted(x.items(), key=lambda kv: kv[0])
print (sorted_x)
# [(0, 0), (1, 2), (2, 1), (3, 4), (4, 3)]

# 451  Sort Characters By Frequency
import collections
class Solution(object):
    def frequencySort(self, s):
        count = collections.Counter(s)
        count_dict = dict(count)
        count_tuple_sorted = sorted(count_dict.items(), key=lambda kv : -kv[1])
        res = ''
        for item in count_tuple_sorted:
            res += item[0] * item[1]
        return res
```

## 2) LC Example
```python
# 525 Contiguous Array
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Tree/contiguous-array.py
# HASH MAP FIND EQUAL 0, 1
class Solution(object):
    def findMaxLength(self, nums):
        r = 0
        cur = 0
        ### NOTE : WE NEED INIT DICT LIKE BELOW
        # https://blog.csdn.net/fuxuemingzhu/article/details/82667054
        _dict = {0:-1}
        for k, v in enumerate(nums):
            if v == 1:
                cur += 1
            else:
                cur -= 1
            if cur in _dict:
                r = max(r, k - _dict[cur])
            else:
                _dict[cur] = k
        return r
```
