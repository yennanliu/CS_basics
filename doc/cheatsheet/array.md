# Array 

## 0) Concept  

### 0-1) Types

- Types
    - [greedy.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/greedy.md)

- Algorithm
    - index op
    - array op
    - sorting
    - dinary search
    - 2 pointers
    - sliding window

- Data structure
    - dict
    - set
    - array

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

#### 1-1-1) Insert into Array
```python
# Out[27]: [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
# In [28]: p.insert(1, [6,1])
# In [29]: p
# Out[29]: [[7, 0], [6, 1], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
```

#### 1-1-2) Delete from Array
#### 1-1-3) check if element in Array
#### 1-1-4) append to array (head, tail)

#### 1-1-5) Sort Array*****
```python
# Pattern :
# V1
_array.sort(key = lambda x : <your_sorting_func>)
# V2
sorted(_array, key = lambda x : <your_sorting_func>)

# 049  Group Anagrams
strs = ["eat","tea","tan","ate","nat","bat"]
strs.sort(key = lambda x : ''.join(sorted(x)) )
print (strs)
# ['bat', 'eat', 'tea', 'ate', 'tan', 'nat']

### NOTE can use this as well
sorted(strs, key = lambda x : ''.join(sorted(x)))
```

#### 1-1-6) Flatten Array
```python
# V1
def flatten_array(_array):
    r = []
    def helper(_array):
        for i in _array:
            if type(i) == int:
                print (i)
                r.append(i)
            else:
                helper(i)

    helper(_array)
    return r
    
_input = [1,0, [1,2,[4,[5,[6,[7]]]]]]#[1,[4,[6]]] #[[1,1],2,[1,1]]

res = flatten_array(_input)
print ("res = " + str(res))

# V2
# https://stackoverflow.com/questions/2158395/flatten-an-irregular-list-of-lists
def flatten(L):
    for item in L:
        try:
            yield from flatten(item)
        except TypeError:
            yield item

r2 = flatten(_input)
r2_ = [x for x in r2]
print (r2_)

# V3
def flatten2(L):
    for item in L:
        try:
            yield from flatten2(item)
        except:
            yield item

r3 = flatten2(_input)
r3_ = [x for x in r3]
print (r3_)
```

#### 1-1-7) Sort array with 2 keys

```python
y = [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
print (y)
y.sort(key = lambda x : (-x[0], x[1]))
print (y)
# [[7, 0], [4, 4], [7, 1], [5, 0], [6, 1], [5, 2]]
# [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
```

#### 1-1-8) go through 2 arrays (length could be different)
```python
# 2 array : s,t
# len(s) = 10, len(t) = 7
# or
# len(s) = 10, len(t) = 11
if len(s) > len(t):
    s,t  = t,s

for i in range(len(s)):
    print (s[i], t[i])
```

#### 1-1-9) shallow, deep copy
```python
# LC 670
# V0'
# IDEA : BRUTE FORCE
# NOTE : ans = A[:]
#        A[:] is a `shallow copy` syntax in python,
#        it will copy "parent obj" (not child obj) to the other instance
#        so the changes ("parent obj" only) in original instance will NOT affect the copied instance
# https://stackoverflow.com/questions/4081561/what-is-the-difference-between-list-and-list-in-python
# https://github.com/yennanliu/til#20210923
class Solution(object):
    def maximumSwap(self, num):
        A = list(str(num))
        ans = A[:]
        for i in range(len(A)):
            for j in range(i+1, len(A)):
                A[i], A[j] = A[j], A[i]
                if A > ans: 
                    ans = A[:]
                A[i], A[j] = A[j], A[i]

        return int("".join(ans))
```

#### 1-1-10) Matrix
```python
# 1) init matrix 
# LC 73
### NOTE : 
# -> for cases matrix[i][j]:
#    -> y is FIRST element  (i)
#    -> x is SECOND element (j)

# 2) avg value of matrix
# LC 661
# some code
# M : matrix
row, col = len(M), len(M[0])
res = [[0]*col for i in range(row)]
temp = [M[i+m][j+n] for m,n in dirs if 0<=i+m<row and 0<=j+n<col]
res[i][j] = sum(temp)//len(temp)
# some code
```

#### 1-1-11) Matrix relative problems
```python
# Transpose matrix
# LC  048 Rotate Image
def rotate(matrix):
    n = len(matrix)
    for i in range(n):
        for j in range(i+1, n):
            matrix[i][j], matrix[j][i] = \
            matrix[j][i], matrix[i][j]

    return matrix

# Spiral matrix
# LC 054 Spiral Matrix
class Solution:
    def spiralOrder(self, matrix):
        res = []
        if len(matrix) == 0:
            return res
        num_row = max(0, len(matrix) - 1)
        num_col = len(matrix[0])
        row = 0
        col = -1
        while True:
            # right
            if num_col == 0:
                break
            for i in range(num_col):
                col += 1
                res.append(matrix[row][col])                
            num_col -= 1
            # down
            if num_row == 0:
                break
            for i in range(num_row):
                row += 1
                res.append(matrix[row][col])
            num_row -= 1
            # left
            if num_col == 0:
                break
            for i in range(num_col):
                col -= 1
                res.append(matrix[row][col])
            num_col -= 1
            # up
            if num_row == 0:
                break
            for i in range(num_row):
                row -= 1
                res.append(matrix[row][col])
            num_row -= 1
        return res
```
 
## 2) LC Example

### 2-1) Merge Intervals
- TODO : move it to "interval merge cheatsheet"
- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md
```python
# 056 Merge Intervals
# V0
class Solution:
    # @param intervals, a list of Interval
    # @return a list of Interval
    def merge(self, intervals):
        intervals = sorted(intervals, key=lambda x: x.start)
        result = []
        for interval in intervals:
            if len(result) == 0 or result[-1].end < interval.start:
                result.append(interval)
            else:
                result[-1].end = max(result[-1].end, interval.end)
        return result

# V1
# https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md
# intervals : [[1,3],[2,6]...]
def merge(intervals):
    if not intervals: return []
    intervals.sort(key=lambda intv: intv[0])
    res = []
    res.append(intervals[0])
    
    for i in range(1, len(intervals)):
        curr = intervals[i]
        last = res[-1]
        if curr[0] <= last[1]:
            last[1] = max(last[1], curr[1])
        else:
            res.append(curr)
    return res
```

### 2-2) Non-overlapping-intervals
```python
# V0 
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        if not intervals: return 0
        intervals.sort(key = lambda x : x[0])
        last = 0
        res = 0
        for i in range(1, len(intervals)):
            if intervals[last][1] > intervals[i][0]:
                if intervals[i][1] < intervals[last][1]:
                    last = i
                res += 1
            else:
                last = i
        return res 
```

### 2-3) Queue Reconstruction by Height
```python
# 406 Queue Reconstruction by Height
class Solution(object):
    def reconstructQueue(self, people):
        people.sort(key = lambda x : (-x[0], x[1]))
        res = []
        for p in people:
            res.insert(p[1], p)
        return res
```

### 2-4) Product of Array Except Self
```python
# 238 Product of Array Except Self
# IDEA : 
# SINCE output[i] = (x0 * x1 * ... * xi-1) * (xi+1 * .... * xn-1)
# -> SO DO A 2 LOOP
# -> 1ST LOOP : GO THROGH THE ARRAY (->) : (x0 * x1 * ... * xi-1)
# -> 2ND LOOP : GO THROGH THE ARRAY (<-) : (xi+1 * .... * xn-1)
# e.g.
# given [1,2,3,4], return [24,12,8,6].
# -> output = [2*3*4, 1,1,1]  <-- 2*3*4    (right of 1: 2,3,4)
# -> output = [2*3*4, 1*3*4,1,1] <-- 1*3*4 (left of 2 :1, right of 2: 3,4)
# -> output = [2*3*4, 1*3*4,1*2*4,1] <-- 1*2*4 (left of 3: 1,2 right of 3 : 4)
# -> output = [2*3*4, 1*3*4,1*2*4,1*2*3] <-- 1*2*3 (left of 4 : 1,2,3)
# -> final output  = [2*3*4, 1*3*4,1*2*4,1*2*3] = [24,12,8,6]
class Solution:
    def productExceptSelf(self, nums):
        size = len(nums)
        output = [1] * size
        left = 1
        for x in range(size - 1):
            left *= nums[x]
            output[x + 1] *= left
        right = 1
        for x in range(size - 1, 0, -1):
            right *= nums[x]
            output[x - 1] *= right
        return output
```

### 2-5) Maximum Swap
```python
# 670 Maximum Swap
class Solution(object):
    def maximumSwap(self, num):
        """
        :type num: int
        :rtype: int
        """
        # BE AWARE OF IT 
        digits = list(str(num))
        left, right = 0, 0
        max_idx = len(digits)-1
        for i in range(len(digits))[::-1]:
            # BE AWARE OF IT 
            if digits[i] > digits[max_idx]:
                max_idx = i
            # BE AWARE OF IT  
            # if current digit > current max digit -> swap them 
            elif digits[max_idx] > digits[i]:
                left, right = i, max_idx        # if current max digit > current digit -> save current max digit to right idnex, and save current index to left
        digits[left], digits[right] = digits[right], digits[left] # swap left and right when loop finished 
        return int("".join(digits))
```

### 2-6) Set Matrix Zeroes
```python
# LC 73. Set Matrix Zeroes
# V0
class Solution(object):
    def setZeroes(self, matrix):   

        if not matrix:
            return matrix

        def help(matrix, xy):
            ### NOTE : 
            #          -> for cases matrix[i][j]:
            #            -> y is FIRST element  (i)
            #            -> x is SECOND element (j)
            x = xy[1]
            y = xy[0]
            matrix[y] = [0] * len(matrix[0])
            for j in range(len(matrix)):
                matrix[j][x] = 0
            return matrix

        _list = []
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j] == 0:
                    _list.append([i,j])

        for xy in _list:
            matrix = help(matrix, xy)
        return matrix
```

### 2-7) Image Smoother
```python
# LC 661 Image Smoother
class Solution:
    def imageSmoother(self, M):
        row, col = len(M), len(M[0])
        res = [[0]*col for i in range(row)]
        dirs = [[0,0],[0,1],[0,-1],[1,0],[-1,0],[1,1],[-1,-1],[-1,1],[1,-1]]
        # note we need to for looping row, col
        for i in range(row):
            for j in range(col):
                # and to below op for each i, j (row, col)
                temp = [M[i+m][j+n] for m,n in dirs if 0<=i+m<row and 0<=j+n<col]
                ### NOTE : this trick for getting avg
                res[i][j] = sum(temp)//len(temp)
        return res
```

### 2-8) Search a 2D Matrix
```python
# LC 74 Search a 2D Matrix
# LC 240. Search a 2D Matrix II
# V0'
# IDEA : DFS
class Solution(object):
    def searchMatrix(self, matrix, target):
        def dfs(matrix, target, x, y):
            if matrix[y][x] == target:
                res.append(True)
            matrix[y][x] = "#"
            moves = [[0,1],[0,-1],[1,0],[-1,0]]
            for move in moves:
                _x = x + move[1]
                _y = y + move[0]
                #print ("_x = " + str(_x) + " _y = " + str(_y))
                if 0 <= _x < w and 0 <= _y < l:
                    if matrix[_y][_x] != "#":
                        dfs(matrix, target, _x, _y)

        if not matrix:
            return False
        l = len(matrix)
        w = len(matrix[0])
        res = []
        dfs(matrix, target, 0, 0)
        return True in res
```

### 2-9) Best Time to Buy and Sell Stock
```python
# LC 121
# V0
# IDEA : array op + problem understanding
class Solution(object):
    def maxProfit(self, prices):
        if len(prices) == 0:
            return 0
        ### NOTE : we define 1st minPrice as prices[0]
        minPrice = prices[0]
        maxProfit = 0
        ### NOTE : we only loop prices ONCE
        for p in prices:
            # only if p < minPrice, we get minPrice
            if p < minPrice:
                minPrice = p
            ### NOTE : only if p - minPrice > maxProfit, we get maxProfit
            elif p - minPrice > maxProfit:
                maxProfit = p - minPrice
        return maxProfit
```