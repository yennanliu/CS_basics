# Hash Map

## 0) Concept  

### 0-1) Types

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

- get value from dict with default value if key not existed
```python
d = {'a': 1, 'b': 2}
d['a']
d.get('a')
d.get('c', 0)
```

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

```python
# dict values -> array
In [6]:
   ...: mydict = {'a':['a1','a2','a3'], 'b':['b1','b2','b3']}
   ...:
   ...: res = [mydict[x] for x in mydict]
   ...:
   ...: print (res)
[['a1', 'a2', 'a3'], ['b1', 'b2', 'b3']]


# LC 049 Group Anagrams

# V0
# IDEA : HASH TABLE
class Solution:
    def groupAnagrams(self, strs):
        res = {}
        for item in strs:
            k = ''.join(sorted(item))  # sort the string 
            if k not in res:  #  check if exists in res 
                res[k] = []
            res[k].append(item)  # if same, put all the same string into dict k 
        return [res[x] for x in res]  # output the result 
```

- get `max index` for each element in a string
```python
s = 'ababcbacadefegdehijhklij'
{k:v for k,v in enumerate(s)}

# LC 763
class Solution(object):
    def partitionLabels(self, S):
        # note : this trick for get max index for each element in S
        lindex = { c: i for i, c in enumerate(S) }
        j = anchor = 0
        ans = []
        for i, c in enumerate(S):
            ### NOTE : trick here
            #          -> via below line of code, we can get the max idx of current substring which is "has element only exist in itself"
            #          -> e.g. the index we need to do partition 
            j = max(j, lindex[c])
            print ("i = " + str(i) + "," + " c = " + str(c) + "," +   " j = " + str(j) + "," +  " ans = " + str(ans))
            if i == j:
                ans.append(j - anchor + 1)
                anchor = j + 1
        return ans
```

## 2) LC Example

### 2-1) Contiguous Array
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

### 2-2) Continuous Subarray Sum
```python
# 523 Continuous Subarray Sum
class Solution(object):
    def checkSubarraySum(self, nums, k):
        _dict = {0:-1}
        tmp = 0
        for i in range(len(nums)):
            tmp += nums[i]
            if k != 0:
                tmp = tmp % k
            if tmp in _dict:
                if i - _dict[tmp] > 1:
                    return True
            else:
                _dict[tmp] = i
        return False
```

### 2-3) Group Anagrams
```python
# 049 Group Anagrams
# V0
# IDEA : HASH TABLE
class Solution:
    def groupAnagrams(self, strs):
        res = {}
        for item in strs:
            k = ''.join(sorted(item))  # sort the string 
            if k not in res:  #  check if exists in res 
                res[k] = []
            res[k].append(item)  # if same, put all the same string into dict k 
        return [res[x] for x in res]  # output the result 

```

### 2-3) Longest Substring Without Repeating Characters
```python
# LC 003

# V0
# IDEA : SLIDING WINDOW + DICT
#       -> use a hash table (d) record visit "alphabet" (e.g. : a,b,c,...)
#          (but NOT sub-string)
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        d = {}
        l = 0
        res = 0
        for r in range(len(s)):
            ### NOTE : if already visited, means "repeating"
            #         -> then we need to update left pointer (l)
            if s[r] in d:
                # note here
                l = max(l, d[s[r]] + 1)
            # if not visited yet, record the alphabet
            # and re-calculate the max length
            d[s[r]] = r
            res = max(res, r -l + 1)
        return res
```

### 2-4) Count Primes
```python
# LC 204 Count Primes
# V0
# IDEA : dict
# https://leetcode.com/problems/count-primes/discuss/1343795/python%3A-sieve-of-eretosthenes
# prinme(x) : check if x is a prime
# prinme(0) = 0
# prinme(1) = 0
# prinme(2) = 0
# prinme(3) = 1
# prinme(4) = 2
# prinme(5) = 3
class Solution:
    def countPrimes(self, n):
        # using sieve of eretosthenes algorithm
        if n < 2: return 0
        nonprimes = {}
        for i in range(2, round(n**(1/2))+1):
            if i not in nonprimes:
                for j in range(i*i, n, i):
                    nonprimes[j] = 1
        return n - len(nonprimes) - 2
```