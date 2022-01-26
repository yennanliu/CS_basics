# Hash Map

## 0) Concept  

### 0-1) Types
- N sum:
    - [n_sum.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
- Continous sum
    - LC 525 : Contiguous Array
    - LC 523 : Continuous Subarray Sum
- Pair of sums
    - LC 1010 : Pairs of Songs With Total Durations Divisible by 60
- Sub array sum
    - LC 560 : Subarray Sum Equals K

### 0-2) Pattern

## 1) General form
<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/hash_table_2.png" ></p>

- Definition 

- When to use 
	- Use case that need data IO with ~ O(1) time complexity
    - optimization via cache (space - time tradeoff)
    - `sum, pair, continous`
    - avoid double loop (O(N^2))

- When Not to use
	- When data is time sequence 
	- When data in in ordering 
	- https://www.reddit.com/r/learnprogramming/comments/29t4s4/when_is_it_bad_to_use_a_hash_table/

- Hash Collisions
	- Chaining 
	- Open addressing
    - [hash_map_collisions.md](https://github.com/yennanliu/CS_basics/blob/master/doc/hash_map_collision.md)

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

#-----------------------------------------------------------------------------
# setdefault : will make key in dict if such key not existed yet (with value as well if defined)
#-----------------------------------------------------------------------------
# insert key "my_key", since my_key not existed, -> make it as new key and value as None (since not defined)
car.setdefault("my_key")
print (car)
# In [18]: car
# Out[18]: {'brand': 'Ford', 'model': 'Mustang', 'year': 1964, 'my_key': None}
# insert key "color", since my_key not existed, -> make it as new key and value as white
car.setdefault("color", "white")
print (car)
# Out[22]:
# {'brand': 'Ford',
#  'model': 'Mustang',
#  'year': 1964,
#  'my_key': None,
#  'color': 'white'}
```

- `sort` on ***hashmap (dict)***
```python
# https://stackoverflow.com/questions/613183/how-do-i-sort-a-dictionary-by-value

x = {1: 2, 3: 4, 4: 3, 2: 1, 0: 0}
In [11]: x.items()
Out[11]: dict_items([(1, 2), (3, 4), (4, 3), (2, 1), (0, 0)])

x = {1: 2, 3: 4, 4: 3, 2: 1, 0: 0}
# note : have to use sorted(xxx, key=yyy), instead of xxx.sorted(....)
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
            #          -> via below line of code, we can get the max idx of current substring which "has such element in itself"
            #          -> e.g. the index we need to do partition 
            j = max(j, lindex[c])
            print ("i = " + str(i) + "," + " c = " + str(c) + "," +   " j = " + str(j) + "," +  " ans = " + str(ans))
            if i == j:
                ans.append(j - anchor + 1)
                anchor = j + 1
        return ans
```

- get pairs with specific sum
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

## 2) LC Example

### 2-1) Contiguous Array
- Core concept : finding if there are `at least 2 indexes` with `SAME sum`
- Above concept is AS SAME AS finding `any 2 x-axis with same y-axis` in below charts
- Explanation : Said we have a sequence `[0, 0, 0, 0, 1, 1]`, the count starting from 0, will equal -1, -2, -3, -4, -3, -2 -> we can find : the longest subarray with equal number of 0 and 1 started and ended when count equals -2. Moreover, 1st chart below shows the `changes VS index` of the sequence, We can easily find out `longest subarray length is 4` (index 2 - 6), since `index 2 and index 6 have the same y-axis` -> `sum in index 2, and index 6 are the same`
<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_525_1.png" ></p>
<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_525_1.png" ></p>

```python

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
        if len(nums) == 2:
            if nums.count(0) == nums.count(1):
                return 2
            else:
                return 0

        # init hash map like below (idx=0, no solution)
        d = {0:-1}
        tmp = 0
        res = 0
        for k, v in enumerate(nums):
            if v == 1:
                tmp += 1
            else:
                tmp -= 1
            if tmp in d:
                res = max(res, k - d[tmp])
            else:
                d[tmp] = k
        return res

# V0'
# 525 Contiguous Array
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
# V0
# IDEA : SLIDING WINDOW + DICT
#       -> use a hash table (d) record visited "alphabet" (e.g. : a,b,c,...)
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