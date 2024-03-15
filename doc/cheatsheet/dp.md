# DP (Dynamic programming)  

## 0) Concept
- Dynamic programming, like the divide-and-conquer method, solves
problems by combining the solutions to subproblems
    - Dynamic: time-varying
    - Programming: a tabular method
- space -> time 用空間換取時間
- trace progress 讓走過的留下痕跡
- Ref
    - https://predoc.dlc.ntu.edu.tw/viewer?embedded=true&url=https%3A%2F%2Fcool.ntu.edu.tw%2Fcourses%2F8583%2Ffiles%2F1165602%2Fdownload%3Fverifier%3DnlJ3s1a9TTmgYQzbJgj9vnrGlKKZB4w0wUZyEKgm 

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Unique Paths
```python
# 62. Unique Paths
# V0
# IDEA : BFS + dp (memory)
class Solution:
    def uniquePaths(self, m, n):

        # NOTE !!! we init paths as below
        paths = [[1]*n for _ in range(m)]
        
        q = deque()
        q.append((0,0))
        
        while q:
            row, col = q.popleft()
            
            if row == m or col == n or paths[row][col] > 1:
                continue 
            
            if row-1 >= 0 and col-1 >= 0:
                paths[row][col] = paths[row-1][col] + paths[row][col-1]
            
            q.append((row+1, col))
            q.append((row, col+1))
        
        return paths[-1][-1]

# V0'
# IDEA : DP
class Solution:
    def uniquePaths(self, m: int, n: int) -> int:
        d = [[1] * n for _ in range(m)]

        for col in range(1, m):
            for row in range(1, n):
                d[col][row] = d[col - 1][row] + d[col][row - 1]

        return d[m - 1][n - 1]
```


### 2-2) Maximum Product Subarray
```python
# NOTE : there is also brute force approach
# V0
# IDEA : brute force + product
class Solution(object):
    def maxProduct(self, A):
        global_max, local_max, local_min = float("-inf"), 1, 1
        for x in A:
            local_max = max(1, local_max)
            if x > 0:
                local_max, local_min = local_max * x, local_min * x
            else:
                local_max, local_min = local_min * x, local_max * x
            global_max = max(global_max, local_max)
        return global_max

# V1
# IDEA : BRUTE FORCE (TLE)
# https://leetcode.com/problems/maximum-product-subarray/solution/
class Solution:
    def maxProduct(self, nums: List[int]) -> int:
        if len(nums) == 0:
            return 0

        result = nums[0]

        for i in range(len(nums)):
            accu = 1
            for j in range(i, len(nums)):
                accu *= nums[j]
                result = max(result, accu)

        return result

# V1
# IDEA : DP
# https://leetcode.com/problems/maximum-product-subarray/solution/
# LC 152
class Solution:
    def maxProduct(self, nums: List[int]) -> int:
        if len(nums) == 0:
            return 0

        max_so_far = nums[0]
        min_so_far = nums[0]
        result = max_so_far

        for i in range(1, len(nums)):
            curr = nums[i]
            temp_max = max(curr, max_so_far * curr, min_so_far * curr)
            min_so_far = min(curr, max_so_far * curr, min_so_far * curr)

            max_so_far = temp_max

            result = max(max_so_far, result)

        return result
```

```java
// java
// LC 152

// V0
// IDEA : DP
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/maximum-product-subarray.py#L69
// IDEA : cur max = max (cur, cur * dp[k-1])
//        But, also needs to consider "minus number"
//        -> e.g.  (-1) * (-3) = 3
//        -> so we NEED to track maxSoFar, and minSoFar
public int maxProduct(int[] nums) {

    // null check
    if (nums.length == 0){
        return 0;
    }
    // init
    int maxSoFar = nums[0];
    int minSoFar = nums[0];
    int res = maxSoFar;

    for (int i = 1; i < nums.length; i++){

        int cur = nums[i];
        /**
         *  or, can use below trick to get max in 3 numbers
         *
         *   max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
         *   min = Math.min(Math.min(temp * nums[i], min * nums[i]), nums[i]);
         *
         */
        int tmpMax = findMax(cur, maxSoFar * cur, minSoFar * cur);
        minSoFar = findMin(cur, maxSoFar * cur, minSoFar * cur);
        maxSoFar = tmpMax;

        res = Math.max(maxSoFar, res);
    }

    return res;
}

private int findMax(int a, int b, int c){
    if (a >= b && a >= c){
        return a;
    }
    else if (b >= a && b >= c){
        return b;
    }else{
        return c;
    }
}

private int findMin(int a, int b, int c){
    if (a <= b && a <= c){
        return a;
    }
    else if (b <= a && b <= c){
        return b;
    }else{
        return c;
    }
}
```