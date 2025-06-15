# Hash Map

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/hash_op_101.png"></p>

- [NC - HashMap under the hood](https://www.linkedin.com/posts/neetcodeio_how-do-hashmaps-work-under-the-hood-activity-7298370869301526530-DsIi?utm_source=social_share_send&utm_medium=member_desktop_web&rcm=ACoAAA6fzw4BpOSBO1YeSrJwPZ-dNBhjC3jXTDE)

## 0) Concept

- [Java HashMap](https://bbs.huaweicloud.com/blogs/276884?utm_source=juejin&utm_medium=bbs-ex&utm_campaign=other&utm_content=content)
    - Low level : Array + Linked list / red-black tree
        - if Linked list length > 8 -> transform Linked list to red-black tree
        - if Linked list length < 6 -> transform red-black tree back to Linked list

- FAQ
    - why hashmap search time complexity ~= O(1) ? explain ?
        - TL;DR : O(1) is avg and best case. worst case could be O(N) (hash collision)
        - hash func matters -> how to storage data & possible hash collision happens
        - OP
            - insert
                - get key, get hash val via hash func
                - find bucket in memory based on hash val
                - save key and value in the bucket
            - query
                - get index based on key
                - find bucket location based on index
                    - NOTE !!! use bit op (`int pos = (n - 1) & hash`), so this op can be O(1) time complexity. (find bucket address directly, NO need to loop over all items)
                - loop over all elements under that key (if there is one element, then do once)
                - return value
        <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/hash_map1.png"></p>
        <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/hash_map2.jpeg"></p>
        - [ref 1](https://blog.csdn.net/junqing_wu/article/details/104606619)
        - [ref 2](https://blog.csdn.net/john1337/article/details/104727895)
- LC Ref
    - [prefix_sum.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/prefix_sum.md)

### 0-1) Types

- N sum:
    - [n_sum.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
    
- Prefix problems
    - [prefix_sum.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/prefix_sum.md)
    - Continous sum
        - LC 525 : Contiguous Array
        - LC 523 : Continuous Subarray Sum
    - Pair of sums
        - LC 1010 : Pairs of Songs With Total Durations Divisible by 60
    - Sub array sum
        - LC 560 : Subarray Sum Equals K
            - `TODO : note this as pattern!!!`
        - LC 325: Maximum Size Subarray Sum Equals k
            - `prefix sum` + hashmap
        ```
       subarray[i,j] = prefixSum[j] - prefixSum[i-1]

       so, to find a subarray equals k

       -> prefixSum[j] - prefixSum[i-1] = k

       -> prefixSum[j]  - k = prefixSum[i-1]

       -> so all need to do is : check if "prefixSum[j]  - k" is in map
        ```
    - check `permutaion sub string`
        - LC 567
        ```java
        // LC 567
        // ...
             /** NOTE !!!
             *
             *  we use below trick to
             *
             *  -> 1) check if `new reached s2 val` is in s1 map
             *  -> 2) check if 2 map are equal
             *
             *  -> so we have more simple code, and clean logic
             */
            if (map2.equals(map1)) {
                return true;
            }
        // ...
        ```

- Check with `letest existed idx`
    - LC 763 Partition Labels 

- Any problems with below
    - need to cache
    - avoid double loop

### 0-2) Pattern

## 1) General form

- Definition 

- When to use 
	- Use case that need data IO with ~ O(1) time complexity
    - optimization via cache (space - time tradeoff)
    - `sum, pair, continuous`
    - avoid double loop (O(N^2))

- When Not to use
	- When data is time sequence 
	- When data is in ordering 
	- https://www.reddit.com/r/learnprogramming/comments/29t4s4/when_is_it_bad_to_use_a_hash_table/

- Hash Collisions
	- Chaining 
	- Open addressing
    - [hash_map_collisions.md](https://github.com/yennanliu/CS_basics/blob/master/doc/hash_map_collision.md)

- Ref 
	- https://blog.techbridge.cc/2017/01/21/simple-hash-table-intro/
	- https://www.freecodecamp.org/news/hash-tables/

### 1-1) Basic OP

- `get` : get value from dict with default value if key not existed
```python
In [10]: d = {'a': 1, 'b': 2}
    ...: d['a']
Out[10]: 1

In [11]: d.get('a')
Out[11]: 1

In [12]: d.get('c', 0)
Out[12]: 0

In [13]: d.get('z')

In [14]:
```

- `setdefault()`
	- https://www.w3schools.com/python/ref_dictionary_setdefault.asp
```python
#-------------------------------------------------------------------------------
# setdefault : will creatte key if key NOT existed (with value as well if defined)
#-------------------------------------------------------------------------------

# syntax
d.setdefault(new_key)
d.setdefault(new_key, new_value)

# 662 Maximum Width of Binary Tree
car = {
  "brand": "Ford",
  "model": "Mustang",
  "year": 1964
}

# example 1) insert key "my_key", since my_key not existed, -> make it as new key and value as None (since not defined)
car.setdefault("my_key")
print (car)
# In [18]: car
# Out[18]: {'brand': 'Ford', 'model': 'Mustang', 'year': 1964, 'my_key': None}

# example 2) insert key "color", since my_key not existed, -> make it as new key and value as white
car.setdefault("color", "white")
print (car)
# Out[22]:
# {'brand': 'Ford',
#  'model': 'Mustang',
#  'year': 1964,
#  'my_key': None,
#  'color': 'white'}
```

- `Sort` on ***hashmap (dict)***
```python
# https://stackoverflow.com/questions/613183/how-do-i-sort-a-dictionary-by-value

x = {1: 2, 3: 4, 4: 3, 2: 1, 0: 0}
In [11]: x.items()
Out[11]: dict_items([(1, 2), (3, 4), (4, 3), (2, 1), (0, 0)])

#----------------------------------
# Sort hashMap by key/value !!!
#----------------------------------
x = {1: 2, 3: 4, 4: 3, 2: 1, 0: 0}
# note : have to use sorted(xxx, key=yyy), instead of xxx.sorted(....)
### NOTE this !!! : x.items()
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
        """
        NOTE this !!!
            1. use sorted()
            2. count_dict.items()
        """
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

- Get `max index` for each element in a string
```python
s = 'ababcbacadefegdehijhklij'
{k:v for k,v in enumerate(s)}

# LC 763
# V0
# IDEA : GREEDY
class Solution(object):
    def partitionLabels(self, s):
        d = {val:idx for idx, val in enumerate(list(s))}
        #print (d)
        res = []
        tmp = set()
        for idx, val in enumerate(s):
            #print ("idx = " + str(idx) + " tmp = " + str(tmp) + "idx == d[val] = " + str(idx == d[val]))
            """
            ### have to fit 2 CONDITIONS so we can split the string
            # -> 1) the element has "last time exist index" with current index
            # -> 2) ALL of the elements in cache with "last time exist index" should <= current index
            """
            if idx == d[val] and all(idx >= d[t] for t in tmp):
                res.append(idx+1)
            else:
                tmp.add(val)
        _res = [res[0]] + [ res[i] - res[i-1] for i in range(1, len(res)) ]
        return _res

# V0'
# IDEA : GREEDY
class Solution(object):
    def partitionLabels(self, S):
        # note : this trick for get max index for each element in S
        lindex = { c: i for i, c in enumerate(S) }
        j = anchor = 0
        ans = []
        for i, c in enumerate(S):
            ### NOTE : trick here
            #          -> via below line of code, we can get the max idx of current substring which "has element only exist in itself"
            #          -> e.g. the index we need to do partition 
            j = max(j, lindex[c])
            print ("i = " + str(i) + "," + " c = " + str(c) + "," +   " j = " + str(j) + "," +  " ans = " + str(ans))
            if i == j:
                ans.append(j - anchor + 1)
                anchor = j + 1
        return ans
```

- Get pairs with specific sum
```python
# LC 1010 Pairs of Songs With Total Durations Divisible by 60
d = {}
res = 0
DURATION = some_duration
for num in nums:
    tmp = num % DURATION # let's say sum is multiply by 60
    ### NOTICE THIS :  (60 - tmp) % 60
    if (DURATION - tmp) % DURATION in d:
        res += d[(DURATION - tmp) % DURATION]
    if tmp not in d:
        d[tmp] = 1
    else:
        d[tmp] += 1
```

- Get `sub array sum`
```python
# (algorithm book (labu) p.350)
my_array = [1,2,3,4,5]
my_array_pre = [0] * (len(my_array)+1)
cur = 0
for i in range(len(my_array)):
    cur += my_array[i]
    my_array_pre[i+1] += cur

# In [17]: print ("my_array = " + str(my_array))
#     ...: print ("my_array_pre = " + str(my_array_pre))
# my_array = [1, 2, 3, 4, 5]
# my_array_pre = [0, 1, 3, 6, 10, 15]

#-----------------------------------------------
# Get sub array sum !!!!!!!
#    -> nums[i..j] sum = preSum[j+1] - preSum[i]
#-----------------------------------------------

# example 1 : sum of [1,2]
my_array_pre[1+1] - my_array_pre[0]  # 1's index is 0, and 2's index is 1. (my_array = [1, 2, 3, 4, 5])

# example 2 : sum of [2,3,4]
my_array_pre[3+1] - my_array_pre[1] # 2's index is 1, and 4's index is 3. (my_array = [1, 2, 3, 4, 5])
```

- Longest Substring
```python
# LC 003
# 2 pointers + dict
# ....
l = 0
d = {}
res = 0
for r in range(len(s)):
    if s[r] in d:
        l = max(l, d[s[r]]+1)
    d[s[r]] = r
    res = max(res, r-l+1)
# ...
```

## 2) LC Example

### 2-1) Contiguous Array
- Core concept : finding if there are `at least 2 indexes` with `SAME sum`
- Above concept is AS SAME AS finding `any 2 x-axis with same y-axis` in below charts
- Explanation : Said we have a sequence `[0, 0, 0, 0, 1, 1]`, the count starting from 0, will equal -1, -2, -3, -4, -3, -2 -> we can find : the longest subarray with equal number of 0 and 1 started and ended when count equals -2. Moreover, 1st chart below shows the `changes VS index` of the sequence, We can easily find out `longest subarray length is 4` (index 2 - 6), since `index 2 and index 6 have the same y-axis` -> `sum in index 2, and index 6 are the same`
<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_525_1.png" ></p>
<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_525_1.png" ></p>

```python
# 525 Contiguous Array

# V0
# IDEA : HashMap
#     -> SET UP A DICT,
#     -> FIND MAX SUB ARRAY LENGH WHEN COUNT(0) == COUNT(1)
#     -> (WHEN cur in _dict, THERE IS THE COUNT(0) == COUNT(1) CASE)
# explaination : https://leetcode.com/problems/contiguous-array/discuss/99655/python-on-solution-with-visual-explanation
class Solution(object):
    def findMaxLength(self, nums):
        # edge case
        if len(nums) <= 1:
            return 0
        # note this edge case
        if len(nums) == 2:
            if nums.count(0) == nums.count(1):
                return 2
            else:
                return 0

        # NOTE !!! : init hash map like below (idx=0, no solution, for [0,1,1] case)
        d = {0:-1} # {tmp_sum : index}
        tmp = 0
        res = 0
        for k, v in enumerate(nums):
            if v == 1:
                tmp += 1
            else:
                tmp -= 1
            """
            Case 1 : if tmp sum in dict
            # NOTE THIS : if tmp in d, return the max of (res,cur-index - index) from d with same cur-value
            """
            if tmp in d:
                res = max(res, k - d[tmp])
            """
            Case 2 : if tmp sum NOT in dict
            # NOTE THIS : if tmp not in d, then use its cur value as key, index as value
            """
            else:
                d[tmp] = k ### NOTE : we just need to add index to dict at once, since what we need is MAX len of continous subarray with condition, so we only add 1st index to dist will make this work (max len subarray)
        return res

# V0'
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Tree/contiguous-array.py
# explanation : https://leetcode.com/problems/contiguous-array/discuss/99655/python-on-solution-with-visual-explanation
# HASH MAP FIND EQUAL 0, 1
class Solution(object):
    def findMaxLength(self, nums):
        r = 0
        cur = 0
        ### NOTE : WE HAVE TO INIT DICT LIKE BELOW
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
- Similar concept as Contiguous Array (LC 525)

```python
# 523 Continuous Subarray Sum
# V0
# IDEA : HASH TABLE
# -> if sum(nums[i:j]) % k == 0 for some i < j, 
#   ->  then sum(nums[:j]) % k == sum(nums[:i]) % k  !!!!
#   -> So we just need to use a dict to keep track of sum(nums[:i]) % k 
#   -> and the corresponding index i. Once some later sum(nums[:i']) % k == sum(nums[:i]) % k and i' - i > 1, so we return True.
class Solution(object):
    def checkSubarraySum(self, nums, k):
        """
        # _dict = {0:-1} : for edge case (need to find a continuous subarray of size AT LEAST two )
        # https://leetcode.com/problems/continuous-subarray-sum/discuss/236976/Python-solution
        # 0: -1 is for edge case that current sum mod k == 0
        # demo :
                In [93]: nums = [0]
                    ...: k = 1
                    ...:
                    ...:
                    ...: s = Solution()
                    ...: r = s.checkSubarraySum(nums, k)
                    ...: print (r)
                0
                i - _dict[tmp] = 1
                False
        """
        ### NOTE : we need to init _dict as {0:-1}
        _dict = {0:-1}
        tmp = 0
        for i in range(len(nums)):
            tmp += nums[i]
            if k != 0:
                ### NOTE : we get remainder of tmp by k
                tmp = tmp % k
            # if tmp in _dict, means there is the other sub part make sub array sum % k == 0
            if tmp in _dict:
                ### only if continuous sub array with length >= 2
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
# V0'
# IDEA : TWO POINTER + SLIDING WINDOW + DICT (NOTE this method !!!!)
#       -> use a hash table (d) record visited "element" (e.g. : a,b,c,...)
#          (but NOT sub-string)
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        d = {}
        # left pointer
        l = 0
        res = 0
        # NOTE !!! right pointer
        for r in range(len(s)):
            """
            ### NOTE : deal with "s[r] in d" case ONLY !!! 
            ### NOTE : if already visited, means "repeating"
            #      -> then we need to update left pointer (l)
            """
            if s[r] in d:
                """
                NOTE !!! this
                -> via max(l, d[s[r]] + 1) trick,
                   we can get the "latest" idx of duplicated s[r], and start from that one
                """
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
# prime(x) : check if x is a prime
# prime(0) = 0
# prime(1) = 0
# prime(2) = 0
# prime(3) = 1
# prime(4) = 2
# prime(5) = 3
# python 3
class Solution:
    def countPrimes(self, n):
        # using sieve of eretosthenes algorithm
        if n < 2: return 0
        nonprimes = set()
        for i in range(2, round(n**(1/2))+1):
            if i not in nonprimes:
                for j in range(i*i, n, i):
                    nonprimes.add(j)
        return n - len(nonprimes) - 2  # remove prime(1), prime(2)
```

### 2-5) Valid Sudoku
```python
# python
# LC 036 Valid Sudoku
# V0
class Solution(object):
    def isValidSudoku(self, board):
        """
        :type board: List[List[str]]
        :rtype: bool
        """
        n = len(board)
        return self.isValidRow(board) and self.isValidCol(board) and self.isValidNineCell(board)
        
    def isValidRow(self, board):
        n = len(board)
        for r in range(n):
            row = [x for x in board[r] if x != '.']
            if len(set(row)) != len(row): # if not repetition 
                return False
        return True

    def isValidCol(self, board):
        n = len(board)
        for c in range(n):
            col = [board[r][c] for r in range(n) if board[r][c] != '.']
            if len(set(col)) != len(col): # if not repetition 
                return False
        return True

    def isValidNineCell(self, board):
        n = len(board)
        for r in range(0, n, 3):
            for c in range(0, n, 3):
                cell = []
                for i in range(3):
                    for j in range(3):
                        num = board[r + i][c + j]
                        if num != '.':
                            cell.append(num)
                if len(set(cell)) != len(cell): # if not repetition 
                    return False
        return True
```
```java
// java
// LC 036 Valid Sudoku
// backtrack
// (algorithm book (labu) p.311)
boolean backtrack(char[][] boolean,int i, int j){

    int m = 9, n = 9;
    
    if (j == n){
        // if visit last col, start from next row
        return backtrack(board, i + 1, 0);
    }

    if (i == m){
        // found one solution, trigger base case
        return true;
    }

    if (board[i][j] != '.'){
        // if there id default number, then no need to looping
        return backtrack(board, i, j + 1);
    }

    for (char ch = '1'; ch <= '9'; ch++){
        // if there is no valid number, negelect it
        if (!isValid(board, i, j, ch)){
            continue;
        }

        board[i][j] = ch;

        // if found one solution, return it and terminate the program
        if (backtrack(board, i, j+1)){
            return true;
        }

        board[i][j] = '.';
    }

    // if looping 1 ~ 9, still can't find a solution
    // -> change a number to loop
    return false;
}

bollean isValid(char[][] board, int r, int c, char n){
    for (int i = 0; i < 9; i++){
        // check if row has duplicate
        if (board[r][i] == n) return false;
        // check if col has duplicate
        if (board[i][c] == n) return false;
        // check if "3 x 3 matrix" has duplicate
        if (board[ (r/3) * 3 + i / 3 ][ (c/3) * 3 + i % 3] == n) return false;
    }
    return true;
}  
```

### 2-6) Pairs of Songs With Total Durations Divisible by 60
```python
# LC 1010. Pairs of Songs With Total Durations Divisible by 60
# V0
# IDEA : dict
# IDEA : NOTE : we only count "NUMBER OF PAIRS", instead get all pairs indexes
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        rem = {}
        pairs = 0
        for t in time:
            #print ("rem = " + str(rem))
            t %= 60
            if (60 - t) % 60 in rem:
                """
                NOTE : this trick
                -> we append "all 60 duration combinations count" via the existing times of element "(60 - t) % 60" 
                """
                pairs += rem[(60 - t) % 60]
            if t not in rem:
                rem[t] = 1
            else:
                ### NOTE : here : we plus 1 when an element already exist
                rem[t] += 1
        return pairs
```

### 2-7) Subarray Sum Equals K
```python
# LC 560 : Subarray Sum Equals K

# V0
# IDEA : HASH TABLE + sub array sum
# IDEA : https://blog.csdn.net/fuxuemingzhu/article/details/82767119
class Solution(object):
    def subarraySum(self, nums, k):
        n = len(nums)
        d = collections.defaultdict(int)
        d[0] = 1
        sum = 0
        res = 0
        for i in range(n):
            sum += nums[i]
            # if sum - k in d
            #  -> if sum - (every _ in d) == k
            if sum - k in d:
                res += d[sum - k]
            d[sum] += 1
        return res

# V0'
# IDEA : HASH TABLE + sub array sum
class Solution:
    def subarraySum(self, nums, k):
        # write your code here
        for i in range(1, len(nums)):
            nums[i] += nums[i - 1]
        print ("nums = " + str(nums))
        d = {0:1}
        ans = 0
        for i in range(len(nums)):
            # check sub array equals k
            if(d.get(nums[i] - k) != None):
                ans += d[nums[i] - k]
            # update dict
            if nums[i] not in d:
                d[nums[i]] = 1
            else:
                d[nums[i]] += 1
        return ans
```
```java
// LC 560 : Subarray Sum Equals K
// java
// (algorithm book (labu) p.350)
// V1 : brute force + cum sum
int subarraySum(int[] nums, int k){
    int n = nums.length;
    // init pre sum
    int[] sum = new int[n+1];
    sum[0] = 0;
    for (int i = 0; i < n; i++){
        sum[i+1] = sum[i] + nums[i];
    }

    int ans = 0;
    // loop over all sub array
    for (int i=1; i <= n; i++){
        for (int j=0; j < i; j++){
            // sum of nums[j...i-1]
            if (sum[i] - sum[j] == k){
                ans += 1;
            }
        }
    }
    return ans;
}

// (algorithm book (labu) p.350)
// V2 : hash map + cum sum
int subarraySum(int[] nums, int k){
    int n = nums.length;
    // map :  key : prefix, value : prefix exists count
    // init hash map
    HashMap<Integer, Integer> preSum = new HashMap<Integer, Integer>();

    // base case
    preSum.put(0,1);

    int ans = 0;
    sum0_i = 0;

    for (int i = 0; i < n; i++){
        sum0_i += nums[i];
        // for presum : nums[0..j]
        int sum0_j = sum0_i - k;
        // if there is already presum, update the ans directly
        if (preSum.containsKey(sum0_j)){
            ans += preSum.get(sum0_j);
        }
        // add prefix and nums[0..i] and record exists count
        preSum.put(sum0_i, preSum.getOrDefault(sum0_i,0) + 1);
    }
    return ans;
}
```

### 2-8) K-diff Pairs in an Array
```python
# LC 532 K-diff Pairs in an Array
# V0
# IDEA : HASH TABLE
import collections
class Solution(object):
    def findPairs(self, nums, k):
        answer = 0
        cnt = collections.Counter(nums)
        # NOTE THIS : !!! we use set(nums) for reduced time complexity, and deal with k == 0 case separately
        for num in set(nums):
            """
            # [b - a] = k
            #  -> b - a = +k or -k
            #  -> b = k + a or b = -k + a
            #  -> however, 0 <= k <= 10^7, so ONLY b = k + a is possible

            2 cases
                -> case 1) k > 0 and num + k in cnt
                -> case 2) k == 0 and cnt[num] > 1
            """
            # case 1) k > 0 and num + k in cnt
            if k > 0 and num + k in cnt: # | a - b | = k -> a - b = +k or -k, but here don't have to deal with "a - b = -k" case, since this sutuation will be covered when go through whole nums  
                answer += 1
            # case 2) k == 0 and cnt[num] > 1
            if k == 0 and cnt[num] > 1:  # for cases k = 0 ->  pair like (1,1) will work. (i.e. 1 + (-1))
                answer += 1
        return answer

# V0'
# IDEA : SORT + BRUTE FORCE + BREAK
class Solution(object):
    def findPairs(self, nums, k):
        # edge case
        if not nums and k:
            return 0
        nums.sort()
        res = 0
        tmp = []
        for i in range(len(nums)):
            for j in range(i+1, len(nums)):
                if abs(nums[j] - nums[i]) == k:
                    cur = [nums[i], nums[j]]
                    cur.sort()
                    if cur not in tmp:
                        res += 1
                        tmp.append(cur)
                elif abs(nums[j] - nums[i]) > k:
                    break
        return res
```

### 2-9) Sentence Similarity
```python
# LC 734. Sentence Similarity
# V0'
# https://zxi.mytechroad.com/blog/hashtable/leetcode-734-sentence-similarity/
import collections
class Solution(object):
    def areSentencesSimilar(self, words1, words2, pairs):
        if len(words1) != len(words2): return False
        similars = collections.defaultdict(set)
        for w1, w2 in pairs:
            similars[w1].add(w2)
            similars[w2].add(w1)
        for w1, w2 in zip(words1, words2):
            if w1 != w2 and w2 not in similars[w1]:
                return False
        return True

# V0
# IDEA : array op
#   -> Apart from edge cases
#   -> there are cases we need to consider
#     -> 1) if sentence1[i] == sentence2[i]
#     -> 2) if sentence1[i] != sentence2[i] and
#           -> [sentence1[i], sentence2[i]] in similarPairs
#           -> [sentence2[i], sentence1[i]] in similarPairs
class Solution(object):
    def areSentencesSimilar(self, sentence1, sentence2, similarPairs):
        # edge case
        if sentence1 == sentence2:
            return True
        if len(sentence1) != len(sentence2):
            return False
        for i in range(len(sentence1)):
            tmp = [sentence1[i], sentence2[i]]
            """
            NOTE : below condition
                1) sentence1[i] != sentence2[i]
                  AND
                2) (tmp not in similarPairs and tmp[::-1] not in similarPairs)

                -> return false
            """
            if sentence1[i] != sentence2[i] and (tmp not in similarPairs and tmp[::-1] not in similarPairs):
                return False
        return True
```

### 2-10) LRU Cache
```python
# LC 146 LRU Cache
# note : there is also array/queue approach
# V1
# IDEA : Ordered dictionary
# https://leetcode.com/problems/lru-cache/solution/
# IDEA : 
#       -> There is a structure called ordered dictionary, it combines behind both hashmap and linked list. 
#       -> In Python this structure is called OrderedDict 
#       -> and in Java LinkedHashMap.
from collections import OrderedDict
class LRUCache(OrderedDict):

    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.capacity = capacity

    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        if key not in self:
            return - 1
        
        self.move_to_end(key)
        return self[key]

    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: void
        """
        if key in self:
            self.move_to_end(key)
        self[key] = value
        if len(self) > self.capacity:
            self.popitem(last = False)
```

### 2-11) Find All Anagrams in a String
```python
# LC 438. Find All Anagrams in a String
# V0
# IDEA : SLIDING WINDOW + collections.Counter()
class Solution(object):
    def findAnagrams(self, s, p):
        ls, lp = len(s), len(p)
        cp = collections.Counter(p)
        cs = collections.Counter()
        ans = []
        for i in range(ls):
            cs[s[i]] += 1
            if i >= lp:
                cs[s[i - lp]] -= 1
                ### BE AWARE OF IT
                if cs[s[i - lp]] == 0:
                    del cs[s[i - lp]]
            if cs == cp:
                ans.append(i - lp + 1)
        return ans
```

### 2-12) Brick Wall
```python
# LC 554. Brick Wall
# V0
# IDEA : HASH TABLE + COUNTER UPDATE (looping every element in the list and cumsum and 
import collections
class Solution(object):
    def leastBricks(self, wall):
        _counter = collections.Counter()
        count = 0
        # go through every sub-wall in wall
        for w in wall:
            cum_sum = 0
            # go through every element in sub-wall
            for i in range(len(w) - 1):
                cum_sum += w[i]
                ### NOTE we can update collections.Counter() via below
                _counter.update([cum_sum])
                count = max(count, _counter[cum_sum])
        return len(wall) - count
```

### 2-13) Maximum Size Subarray Sum Equals k

```java
// java
// LC 325
    public int maxSubArrayLen_0_1(int[] nums, int k) {
        // Map to store (prefixSum, index)
        Map<Integer, Integer> preSumMap = new HashMap<>();
        preSumMap.put(0, -1); // Initialize for subarrays starting from index 0

        int curSum = 0;
        int maxSize = 0;

        for (int i = 0; i < nums.length; i++) {
            curSum += nums[i];

            /**
             *
             *   TODO: check if `preSum == k` already existed before (within loop over nunms)
             *
             *    -> if preSum == k existed
             *    -> (let's say current idx = j, and a previous idx = i, can make sum(i, j) == k)
             *    -> `preSum(j) - preSum(i) = k`  !!!!
             *      -> preSum(j) if what we have  (preSum)
             *      ----> so need to check if `preSum(j) - k` exists in map  !!!
             */
            // Check if there's a prefix sum such that curSum - prefixSum = k
            /**
             *  Prefix sum
             *
             *
             * The prefix sum approach works because any subarray sum can be expressed in terms of two prefix sums:
             *
             *
             * sum of subarray[i,j] = prefixSum[j] - prefixSum[i-1]
             *
             *
             * Where:
             *  •   prefixSum[j] is the cumulative sum of the array up to index j.
             *  •   prefixSum[i-1] is the cumulative sum of the array up to index i-1.
             *
             * Rewriting this:
             *
             * -> prefixSum[j] - prefixSum[i-1] = k
             *
             * -> prefixSum[i-1] = prefixSum[j] - k
             *
             *
             * Thus, the task is to find a previous prefix
             * sum (prefixSum[i-1]) such that the
             * difference between the current
             * prefix sum (prefixSum[j]) and that value equals k.
             *
             *
             *
             *  How the Code Works
             *
             *  1.  Tracking Prefix Sums:
             *      •   curSum is the cumulative prefix sum up to the current index i.
             *      •   The map preSumMap stores previously seen prefix sums as keys, with their earliest index as the value.
             *  2.  Checking for Subarrays:
             *      •   At any index i, the condition curSum - k checks if there exists a previously seen prefix sum that, when subtracted from the current cumulative sum, gives the desired subarray sum k.
             *
             *  3.  Why It Covers All Possible Subarrays******:
             *      •   The map contains all prefix sums seen so far, so it inherently includes all potential starting points of subarrays.
             *      •   If a subarray [start, i] has a sum equal to k, the difference curSum - k corresponds to the prefix sum at start - 1. Since the map stores all previously seen prefix sums, this difference is guaranteed to be checked.
             *
             */
            if (preSumMap.containsKey(curSum - k)) {
                maxSize = Math.max(maxSize, i - preSumMap.get(curSum - k));
            }

            // Add current prefix sum to the map if not already present
            preSumMap.putIfAbsent(curSum, i);
        }

        return maxSize;
    }
```

### 2-14)  Smallest Common Region

```java
// java
// LC 1257

// V0-1
// IDEA: HASHMAP (fixed by gpt)
// TODO: validate
public String findSmallestRegion_0_1(List<List<String>> regions, String region1, String region2) {

    // Map each region to its parent
    /**
     *  NOTE !!!
     *
     *   map : {child : parent}
     *
     *   -> so the key is child, and the value is its parent
     *
     */
    Map<String, String> parentMap = new HashMap<>();

    for (List<String> regionList : regions) {
        String parent = regionList.get(0);
        for (int i = 1; i < regionList.size(); i++) {
            parentMap.put(regionList.get(i), parent);
        }
    }

    // Track ancestors of region1
    /**  NOTE !!!
     *
     *  we use `set` to track `parents` (ancestors)
     *  if exists, add it to set,
     *  and set `current region` as its `parent`
     *
     */
    Set<String> ancestors = new HashSet<>();
    while (region1 != null) {
        ancestors.add(region1);
        region1 = parentMap.get(region1);
    }

    // Traverse region2’s ancestors until we find one in region1’s ancestor set
    while (!ancestors.contains(region2)) {
        region2 = parentMap.get(region2);
    }

    return region2;
}
```