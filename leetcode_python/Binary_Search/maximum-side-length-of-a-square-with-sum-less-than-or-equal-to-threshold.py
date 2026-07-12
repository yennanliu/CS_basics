# https://leetcode.com/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/description/

"""

1292. Maximum Side Length of a Square with Sum Less than or Equal to Threshold
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given a m x n matrix mat and an integer threshold, return the maximum side-length of a square with a sum less than or equal to threshold or return 0 if there is no such square.

 

Example 1:


Input: mat = [[1,1,3,2,4,3,2],[1,1,3,2,4,3,2],[1,1,3,2,4,3,2]], threshold = 4
Output: 2
Explanation: The maximum side length of square with sum less than or equal to 4 is 2 as shown.
Example 2:

Input: mat = [[2,2,2,2,2],[2,2,2,2,2],[2,2,2,2,2],[2,2,2,2,2],[2,2,2,2,2]], threshold = 1
Output: 0
 

Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 300
0 <= mat[i][j] <= 104
0 <= threshold <= 105
 


"""


# V0
class Solution(object):
    def maxSideLength(self, mat, threshold):
        """
        :type mat: List[List[int]]
        :type threshold: int
        :rtype: int
        """
        pass


# V0

# V1-1
# IDEA: PREFIX + BINARY SEARCH (gpt)
"""
NOTE !!!


For a square whose 
  top-left corner is (i, j)
  and side length k:

```

(i, j) -------------------- (i, j+k-1)
  |                              |
  |                              |
  |                              |
(i+k-1, j) ------------ (i+k-1, j+k-1)

```


-> sum is

```
total = (
    prefix[i + k][j + k]
    - prefix[i][j + k]
    - prefix[i + k][j]
    + prefix[i][j]
)
```


where prefix is a 1-based prefix sum array.

"""
class Solution(object):
    def maxSideLength(self, mat, threshold):
        m, n = len(mat), len(mat[0])

        # 1-based prefix sum
        prefix = [[0] * (n + 1) for _ in range(m + 1)]

        for i in range(m):
            for j in range(n):
            	"""
            	NOTE !!!

            	This is the `standard` way to 

            	build a `2D prefix sum`. 


            	-> The key idea is:

						prefix[i+1][j+1] 

						stores the sum of the 
						rectangle

						from `(0,0)` to (i,j) inclusive.
            	"""
                prefix[i + 1][j + 1] = (
                    mat[i][j]
                    + prefix[i][j + 1]
                    + prefix[i + 1][j]
                    - prefix[i][j]
                )

        # Returns True if there exists a square of side k
        # whose sum <= threshold.
        def check(k):
            if k == 0:
                return True

            for i in range(m - k + 1):
                for j in range(n - k + 1):
                    total = (
                        prefix[i + k][j + k]
                        - prefix[i][j + k]
                        - prefix[i + k][j]
                        + prefix[i][j]
                    )

                    if total <= threshold:
                        return True

            return False

        left, right = 0, min(m, n)

        while left < right:
            mid = (left + right + 1) // 2

            if check(mid):
                left = mid
            else:
                right = mid - 1

        return left


# V1-2
# IDEA: PREFIX + BINARY SEARCH (gpt)
class Solution(object):
    def maxSideLength(self, mat, threshold):
        """
        :type mat: List[List[int]]
        :type threshold: int
        :rtype: int
        """
        m = len(mat)
        n = len(mat[0])
        
        # Create a 2D prefix sum array (1-indexed to prevent out-of-bounds issues)
        prefix = [[0] * (n + 1) for _ in range(m + 1)]
        
        # Build the prefix sum matrix
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                prefix[i][j] = (mat[i-1][j-1] 
                              + prefix[i-1][j] 
                              + prefix[i][j-1] 
                              - prefix[i-1][j-1])
        
        max_len = 0
        
        # Iterate through the matrix, treating (i, j) as the bottom-right corner of a potential square
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                # We only check if a square of size max_len + 1 is valid.
                # If it is, we increment max_len. We do not need to check smaller squares.
                k = max_len + 1
                
                # Ensure the square of size 'k' doesn't go out of bounds on the top or left
                if i - k >= 0 and j - k >= 0:
                    # Calculate the sum of the k x k square in O(1) time
                    square_sum = (prefix[i][j] 
                                - prefix[i - k][j] 
                                - prefix[i][j - k] 
                                + prefix[i - k][j - k])
                                
                    # If it fits the threshold, increase our maximum length
                    if square_sum <= threshold:
                        max_len = k
                        
        return max_len


# V2-1
# IDEA: BINARY SEARCH
# https://leetcode.com/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/editorial/
class Solution:
    def maxSideLength(self, mat: List[List[int]], threshold: int) -> int:
        m, n = len(mat), len(mat[0])
        P = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                P[i][j] = (
                    P[i - 1][j]
                    + P[i][j - 1]
                    - P[i - 1][j - 1]
                    + mat[i - 1][j - 1]
                )

        def getRect(x1, y1, x2, y2):
            return P[x2][y2] - P[x1 - 1][y2] - P[x2][y1 - 1] + P[x1 - 1][y1 - 1]

        l, r, ans = 1, min(m, n), 0
        while l <= r:
            mid = (l + r) // 2
            find = any(
                getRect(i, j, i + mid - 1, j + mid - 1) <= threshold
                for i in range(1, m - mid + 2)
                for j in range(1, n - mid + 2)
            )
            if find:
                ans = mid
                l = mid + 1
            else:
                r = mid - 1
        return ans




# V2-2
# IDEA: Enumeration + Optimization
# https://leetcode.com/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/editorial/
class Solution:
    def maxSideLength(self, mat: List[List[int]], threshold: int) -> int:
        m, n = len(mat), len(mat[0])
        P = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                P[i][j] = (
                    P[i - 1][j]
                    + P[i][j - 1]
                    - P[i - 1][j - 1]
                    + mat[i - 1][j - 1]
                )

        def getRect(x1, y1, x2, y2):
            return P[x2][y2] - P[x1 - 1][y2] - P[x2][y1 - 1] + P[x1 - 1][y1 - 1]

        r, ans = min(m, n), 0
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                for c in range(ans + 1, r + 1):
                    if (
                        i + c - 1 <= m
                        and j + c - 1 <= n
                        and getRect(i, j, i + c - 1, j + c - 1) <= threshold
                    ):
                        ans += 1
                    else:
                        break
        return ans
