# Array
> Basic linear data structure

## 0) Concept  

### 0-1) Types

- Types
    - [greedy.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/greedy.md)

- Algorithm
    - index op
    - array op
    - sorting
    - [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
    - [2 pointers](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/2_pointers.md)
        - fast-slow pointers
        - left-right pointers
    - [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)
    - [prefix sum](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/prefix_sum.md)
    - [difference array](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/diffence_array.md)
- Data structure
    - dict
    - set
    - array

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

#### 1-1-0) Split Array
```python
# https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md

#-----------------------------------------------------------------------------------------------------
# example 7 : itertools.islice : slice on iterator
#-----------------------------------------------------------------------------------------------------
# https://docs.python.org/3/library/itertools.html#itertools.islice
# syntax : itertools.islice(seq, [start,] stop [, step])

In [6]:  x = itertools.islice(range(10), 0, 9, 2)

In [7]: print (list(x))
[0, 2, 4, 6, 8]


In [18]: y = itertools.islice(range(10), 0, 10, 3)
    ...: print (list(y))
[0, 3, 6, 9]
```

#### 1-1-1) Insert into Array
```python
p=[[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
Out[27]: [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
In [28]: p.insert(1, [6,1])
In [29]: p
Out[29]: [[7, 0], [6, 1], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
```

#### 1-1-2) Delete from Array
```python
p=[[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]

In [4]: p
Out[4]: [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]

In [5]: p.remove([7, 1])

In [6]: p
Out[6]: [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4]]
```
#### 1-1-3) check if element in Array
```python
In [7]: p
Out[7]: [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4]]

In [8]: [7,0] in p
Out[8]: True
```
#### 1-1-4) append to array (head, tail)
```python
# tail
In [9]: p
Out[9]: [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4]]

In [10]: p.append([0,0])

In [11]: p
Out[11]: [[7, 0], [6, 1], [5, 0], [5, 2], [4, 4], [0, 0]]
```

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
# LC 341
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

```java
// java
// algorithm book (labu) p.355
//------------------------------------------
// implement NestedInteger data structure
//------------------------------------------
public class NestedInteger {
    private Integer val;
    private List<NestedInteger> list;

    public NestedInteger(Integer val){
        this.val = val;
        this.list = null;
    }

    public NestedInteger(List<NestedInteger> list){
        this.list = list;
        this.val = null;
    }

    // if saved value is integer, return true, else false
    public boolean isIntger(){
        return val != null;
    }

    // if saved value is integer, return it, else return null
    public Integer getInteger(){
        return this.val;
    }

    // if saved value is array, return it, else return null
    public List<NestedInteger> getList(){
        return this.list;
    }

}
```

```java
// java
// LC 341
// algorithm book (labu) p.357
//-----------------------------------------------------------
// NestedInteger solution V1 :  via tree algorithm
//-----------------------------------------------------------
class NestedIterator implements Iterator<Integer>{

    private Iterator<Integer> it;

    public NestedInteger(List<NestedInteger> nestedList){
        // save flatten result
        List<Integer> result = new LinkedList<>();

        for (NestedInteger node: nestedList){
            // start from each node and proceed
            traverse(node, result);
        }

        // get result's iterator
        this.it = result.iterator();
    }

    public Integer next(){
        return it.next();
    }

    public boolean hasNext(){
        return it.hasNext();
    }

    // traverse tree with root as root, and add nodes to result array
    private void traverse(NestedInteger root, List<Integer> result){
        if (root.isIntger()){
            // arrive root node
            result.add(root.getInteger());
            return;
        }

        // traverse framework
        for (NestedInteger child: root.getList()){
            traverse(child, result);
        }
    }
}
```

```java
// java
// LC 341
// algorithm book (labu) p.358
//-----------------------------------------------------------
// NestedInteger solution V2 :  via lazy calling
//-----------------------------------------------------------
public class NestedIterator implements Iterator<Integer>{

    private LinkedList<NestedInteger> list;

    public NestedInteger(List<NestedInteger> nestedList){
        // use LinkedList, for good performance in below op
        list = new LinkedList<>(nestedList);
    }

    public Integer next(){
        // hasNext method make sure 1st element must be Integer type
        return list.remove(0).getInteger();
    }

    public boolean hasNext(){
        // for loop split elements in array until 1st element is Integer type
        while (!list.isEmpty() && list.get(0).isIntger()){
            // when 1st element is array type, go into the loop
            List<NestedInteger> first = list.remove(0).getList();
            // flatten 1st array, and add to "start" in ordering
            for (int i = first.size() - 1; i >= 0; i--){
                list.addFirst(first.get(i));
            }  
        }
        return !list.isEmpty();
    }
}
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
#--------------------
# example 1
#--------------------

# 2 array : s,t
# len(s) = 10, len(t) = 7
# or
# len(s) = 10, len(t) = 11
if len(s) > len(t):
    s,t  = t,s

for i in range(len(s)):
    print (s[i], t[i])


#--------------------
# example 2
#--------------------
# LC 165
class Solution:
    def compareVersion(self, version1: str, version2: str) -> int:
        nums1 = version1.split('.')
        nums2 = version2.split('.')
        n1, n2 = len(nums1), len(nums2)
        
        # NOTE here !!!
        # compare versions
        for i in range(max(n1, n2)):
            i1 = int(nums1[i]) if i < n1 else 0
            i2 = int(nums2[i]) if i < n2 else 0
            if i1 != i2:
                return 1 if i1 > i2 else -1
        
        # the versions are equal
        return 0
```

#### 1-1-9) shallow, deep copy
```python
# LC 670
# V0'
# IDEA : BRUTE FORCE
# NOTE : there is also 2 pointers solution : 
#        -> https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/maximum-swap.py#L49
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
# -> for matrix[i][j]:
#    -> y is FIRST element  (i)
#    -> x is SECOND element (j)
```

```python
#----------------------
# transpose matrix
#----------------------
# LC  048 Rotate Image
matrix = [
    [1,2,4],
    [4,5,6],
    [7,8,9]
]
for i in range(len(matrix)):
    """
    NOTE !!!
        -> j start from i+1 to len(matrix[0])
    """
    for j in range(i+1, len(matrix[0])): # NOTE THIS !!!!
        matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]

print (matrix)
# In [36]: matrix
# Out[36]: [[1, 4, 7], [2, 5, 8], [4, 6, 9]]
```

```python
#----------------------
# Rotate matrix
#----------------------
# LC  048 Rotate Image
class Solution:
    def rotate(self, matrix):
        ### NOTE : TRANSPOSE matrix
        n = len(matrix)
        # transpose
        for i in range(n):
             for j in range(i+1, n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        # reverse
        for i in range(n):
            matrix[i].reverse()
        return matrix
```

```python
#----------------------
# 2) get avg value of matrix
#----------------------
# LC 661
# some code
# M : matrix
row, col = len(M), len(M[0])
res = [[0]*col for i in range(row)]
# get tmp sum
dirs = [[0,0],[0,1],[0,-1],[1,0],[-1,0],[1,1],[-1,-1],[-1,1],[1,-1]]
temp = [M[i+m][j+n] for m,n in dirs if 0<=i+m<row and 0<=j+n<col]
# get avg value
res[i][j] = sum(temp)//len(temp)
# some code
```

#### 1-1-11) Matrix relative problems
```python
# Transpose matrix
# LC  048 Rotate Image
# V0
# IDEA : TRANSPOSE -> REVERSE 
class Solution:
    def rotate(self, matrix):
        ### NOTE : TRANSPOSE matrix
        n = len(matrix)
        # transpose
        for i in range(n):
             for j in range(i+1, n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        # reverse
        for i in range(n):
            matrix[i].reverse()
        return matrix

# Spiral matrix
# LC 054 Spiral Matrix
# V0
# IDEA : 4 cases : right, down, left, up + boundary condition
class Solution(object):
    def spiralOrder(self, matrix):
        # edge case
        if not matrix:
            return
        res=[]
        """
        NOTE this : we define 4 boundaries
        """
        left, right, top, bottom = 0, len(matrix[0])-1, 0, len(matrix)-1
        """
        NOTE : this condition
        """
        while left <= right and top <= bottom:
            # right
            for j in range(left, right+1):  # note : range(left, right+1)
                res.append(matrix[top][j])
            # down
            for i in range(top+1, bottom):  # note : range(top+1, bottom)
                res.append(matrix[i][right])
            # left
            for j in range(left, right+1)[::-1]: # note : range(left, right+1)[::-1]
                """
                NOTE : this condition
                """
                if top < bottom:
                    res.append(matrix[bottom][j])
            # up
            for i in range(top+1, bottom)[::-1]: # note : range(top+1, bottom)[::-1]
                """
                NOTE : this condition
                """
                if left < right:
                    res.append(matrix[i][left])

            left += 1
            right -= 1
            top += 1
            bottom -= 1
            
        return res

# V0'
class Solution(object):
    # @param matrix, a list of lists of integers
    # @return a list of integers
    def spiralOrder(self, matrix):
        result = []
        if matrix == []:
            return result

        left, right, top, bottom = 0, len(matrix[0]) - 1, 0, len(matrix) - 1

        while left <= right and top <= bottom:
            # right
            for j in range(left, right + 1):
                result.append(matrix[top][j])
            # down
            for i in range(top + 1, bottom):
                result.append(matrix[i][right])
            # left
            for j in (range(left, right + 1))[::-1]:
                if top < bottom: # notice
                    result.append(matrix[bottom][j])
            # up
            for i in range(top + 1, bottom)[::-1]:
                if left < right: # notice
                    result.append(matrix[i][left])
            left, right, top, bottom = left + 1, right - 1, top + 1, bottom - 1

        return result
```

```python
# -------------------------
# get diagonal sum of matrix
# -------------------------
# LC 348. Design Tic-Tac-Toe
# ...
n = len(self.grid)
sum_of_row = sum([self.grid[row][c] == mark for c in range(n)])
sum_of_col = sum([self.grid[r][col]== mark for r in range(n)])
sum_of_left_d = sum([self.grid[i][i] == mark for i in range(n)])
sum_of_right_d = sum([self.grid[i][n-1-i] == mark for i in range(n)])
# ....
```

```python
# LC 311. Sparse Matrix Multiplication
# V0 
# TODO : OPTIMIZE THE PROCESS DUE TO THE SPARSE-MATRIX CONDITION 
class Solution(object):
    def multiply(self, A, B):
        """
        :type A: List[List[int]]
        :type B: List[List[int]]
        :rtype: List[List[int]]
        """
        m, n, l = len(A), len(A[0]), len(B[0])
        res = [[0 for _ in range(l)] for _ in range(m)]
        for i in range(m):
            for k in range(n):
                if A[i][k]:
                    for j in range(l):
                        res[i][j] += A[i][k] * B[k][j]
        return res
```

```python
# LC 498. Diagonal Traverse
# V0 
# IDEA : while loop + boundary conditions
### NOTE : the "directions" trick
class Solution(object):
    def findDiagonalOrder(self, matrix):
        if not matrix or not matrix[0]: return []
        ### NOTE this trick
        directions = [(-1, 1), (1, -1)]
        count = 0
        res = []
        i, j = 0, 0
        M, N = len(matrix), len(matrix[0])
        while len(res) < M * N:
            if 0 <= i < M and 0 <= j < N:
                res.append(matrix[i][j])
                direct = directions[count % 2]
                i, j = i + direct[0], j + direct[1]
                continue
            elif i < 0 and 0 <= j < N:
                i += 1
            elif 0 <= i < M and j < 0:
                j += 1
            elif i < M and j >= N:
                i += 2
                j -= 1
            elif i >= M and j < N:
                j += 2
                i -= 1
            count += 1
        return res
```
 
## 2) LC Example

### 2-1) Queue Reconstruction by Height
```python
# LC 406 Queue Reconstruction by Height
class Solution(object):
    def reconstructQueue(self, people):
        people.sort(key = lambda x : (-x[0], x[1]))
        res = []
        for p in people:
        """
        py insert syntax
        # syntax : 
        # https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md#1-6-insert-into-array-in-place

        # arr.insert(<index>,<value>)
        """
            res.insert(p[1], p)
        return res
```

### 2-2) Product of Array Except Self
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

### 2-3) Maximum Swap
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

### 2-4) Set Matrix Zeroes
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

### 2-5) Image Smoother
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

### 2-6) Search a 2D Matrix
```python
# LC 74 Search a 2D Matrix
# LC 240. Search a 2D Matrix II
"""
NOTE !!!  boundary condition
"""
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

### 2-7) Best Time to Buy and Sell Stock
```python
# LC 121 Best Time to Buy and Sell Stock
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

### 2-8) Bulb Switcher III
```python
# LC 1375. Bulb Switcher III
# V0
class Solution:
    def numTimesAllBlue(self, light):
        max_bulb_ind = 0
        count = 0
        turnedon_bulb = 0
        
        for bulb in light:
            max_bulb_ind = max(max_bulb_ind,bulb)
            turnedon_bulb += 1
            if turnedon_bulb == max_bulb_ind:
                count += 1
        
        return count
```

### 2-9) Robot Bounded In Circle
```python
# LC 1041. Robot Bounded In Circle
# V0
# IDEA : math + array
class Solution:
    def isRobotBounded(self, instructions):
        """
        NOTE !!! we make direction as below
        """
        dirs = [[0,1], [1,0], [0,-1], [-1,0]]
        x = 0;
        y = 0;
        idx = 0;
        for c in instructions:
            print ("c = " + str(c) + " idx = " + str(idx))
            """
            NOTE !!! since we need to verify if robot back to start point
                -> we use (idx + k)  % 4 for detecting cyclic cases
            """
            if c == 'L':
                idx = (idx + 3) % 4
            elif c == 'R':
                idx = (idx + 1) % 4
            elif c == 'G':
                x = x + dirs[idx][0]
                y = y + dirs[idx][1]
        return (x == 0 and y ==0) or idx !=0
```

### 2-10) Maximum Length of Subarray With Positive Product
```python
# LC 1567 Maximum Length of Subarray With Positive Product

# V0
class Solution:
    def getMaxLen(self, nums):
        first_neg, zero = None, -1
        mx = neg = 0
        for i,v in enumerate(nums):
            if v == 0:
                first_neg, zero, neg = None, i, 0
                continue
            if v < 0:
                neg += 1
                if first_neg == None:
                    first_neg = i
            j = zero if not neg % 2 else first_neg if first_neg != None else 10**9
            mx = max(mx, i-j)
        return mx

# V0'
# IDEA : 2 POINTERS
class Solution:
    def getMaxLen(self, nums):
        res = 0
        k = -1 # most recent 0
        j = -1 # first negative after most recent 0
        cnt = 0 # count of negatives after most recent 0
        for i, n in enumerate(nums):
            if n == 0:
                k = i
                j = i
                cnt = 0
            elif n < 0:
                cnt += 1
                if cnt % 2 == 0:
                    res = max(res, i - k)
                else:
                    if cnt == 1:
                        j = i
                    else:
                        res = max(res, i - j)        
            else:
                if cnt % 2 == 0:
                    res = max(res, i - k)
                else:
                    res = max(res, i - j)
        return res
```

### 2-11) Corporate Flight Bookings
```python
# LC 1109. Corporate Flight Bookings
# V1
# IDEA : ARRAY + prefix sum
# https://leetcode.com/problems/corporate-flight-bookings/discuss/328856/JavaC%2B%2BPython-Sweep-Line
# IDEA :
# Set the change of seats for each day.
# If booking = [i, j, k],
# it needs k more seat on ith day,
# and we don't need these seats on j+1th day.
# We accumulate these changes then we have the result that we want.
# Complexity
# Time O(booking + N) for one pass on bookings
# Space O(N) for the result
class Solution:
    def corpFlightBookings(self, bookings, n):
        res = [0] * (n + 1)
        for i, j, k in bookings:
            res[i - 1] += k
            res[j] -= k
        for i in range(1, n):
            res[i] += res[i - 1]
        return res[:-1]

# V1''
# IDEA : ARRAY
# https://leetcode.com/problems/corporate-flight-bookings/discuss/328893/Short-python-solution
# IDEA : Simply use two arrays to keep track of how many bookings are added for every flight.
class Solution:
    def corpFlightBookings(self, bookings: List[List[int]], n: int) -> List[int]:        
        opens = [0]*n
        closes = [0]*n
        
        for e in bookings:
            opens[e[0]-1] += e[2]
            closes[e[1]-1] += e[2]
            
        ret, tmp = [0]*n, 0
        for i in range(n):
            tmp += opens[i]
            ret[i] = tmp
            tmp -= closes[i]
            
        return ret
```

### 2-12) First Missing Positive
```python
# LC 41. First Missing Positive
# V1'
# IDEA :  Index as a hash key.
# https://leetcode.com/problems/first-missing-positive/solution/
# /doc/pic/first-missing-positive.png
class Solution:
    def firstMissingPositive(self, nums: List[int]) -> int:
        n = len(nums)
        
        # Base case.
        if 1 not in nums:
            return 1
        
        # Replace negative numbers, zeros,
        # and numbers larger than n by 1s.
        # After this convertion nums will contain 
        # only positive numbers.
        for i in range(n):
            if nums[i] <= 0 or nums[i] > n:
                nums[i] = 1
        
        # Use index as a hash key and number sign as a presence detector.
        # For example, if nums[1] is negative that means that number `1`
        # is present in the array. 
        # If nums[2] is positive - number 2 is missing.
        for i in range(n): 
            a = abs(nums[i])
            # If you meet number a in the array - change the sign of a-th element.
            # Be careful with duplicates : do it only once.
            if a == n:
                nums[0] = - abs(nums[0])
            else:
                nums[a] = - abs(nums[a])
            
        # Now the index of the first positive number 
        # is equal to first missing positive.
        for i in range(1, n):
            if nums[i] > 0:
                return i
        
        if nums[0] > 0:
            return n
            
        return n + 1
```

### 2-13) Increasing Triplet Subsequence
```python
# LC 334 Increasing Triplet Subsequence
# V0
# IDEA : MAINTAIN var first, second
#        AND GO THROUGH nums to check if there exists x (on the right hand side of a, b )
#        such that x > second > first
class Solution(object):
    def increasingTriplet(self, nums):
        """
        NOTE !!! we init first, second as POSITIVE float('inf')
        """
        first = float('inf')
        second = float('inf')
        # loop with normal ordering
        for num in nums:
            if num <= first:     # min num
                first = num
            elif num <= second:  # 2nd min num
                second = num
            else:                # 3rd min num
                return True      
        return False
```

### 2-14) First Missing Positive
```python
# LC 41. First Missing Positive
# V0
# IDEA : for loop + while loop + problem understanding
class Solution:
    def firstMissingPositive(self, nums):
        for i, n in enumerate(nums):
            if n < 0:
                continue
            else:
                while n <= len(nums) and n > 0:
                    tmp = nums[n-1]
                    nums[n-1] = float('inf')
                    n = tmp
        for i in range(len(nums)):
            if nums[i] != float('inf'):
                return i+1
            
        return len(nums)+1
```

### 2-15) Rotate Array
```python
# LC 189. Rotate Array
# V0
# IDEA : pop + insert
class Solution(object):
    def rotate(self, nums, k):
        _len = len(nums)
        k = k % _len
        while k > 0:
            tmp = nums.pop(-1)
            nums.insert(0, tmp)
            k -= 1

# V0'
# IDEA : SLICE (in place)
class Solution(object):
    def rotate(self, nums, k):
        # edge case
        if k == 0 or not nums or len(nums) == 1:
            return nums
        ### NOTE this
        k = k % len(nums)
        if k == 0:
            return nums
        """
        NOTE this !!!!
        """
        nums[:k], nums[k:] = nums[-k:], nums[:-k]
        return nums
```

### 2-16) Flatten 2D Vector
```python
# LC 251. Flatten 2D Vector
# V0
# IDEA : ARRAY OP
class Vector2D:

    def __init__(self, v):
        # We need to iterate over the 2D vector, getting all the integers
        # out of it and putting them into the nums list.
        self.nums = []
        for inner_list in v:
            for num in inner_list:
                self.nums.append(num)
        # We'll keep position 1 behind the next number to return.
        self.position = -1

    def next(self):
        # Move up to the current element and return it.
        self.position += 1
        return self.nums[self.position]

    def hasNext(self):
        # If the next position is a valid index of nums, return True.
        return self.position + 1 < len(self.nums)
```