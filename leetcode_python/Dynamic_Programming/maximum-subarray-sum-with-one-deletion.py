"""

1186. Maximum Subarray Sum with One Deletion
Medium

Given an array of integers, return the maximum sum for a non-empty subarray
(contiguous elements) with at most one element deletion. In other words, you want
to choose a subarray and optionally delete one element from it so that there is
still at least one element left and the sum of the remaining elements is maximum
possible.

Note that the subarray needs to be non-empty after deleting one element.


Example 1:

Input: arr = [1,-2,0,3]
Output: 4
Explanation: Because we can choose [1, -2, 0, 3] and drop -2, thus the subarray
[1, 0, 3] becomes the maximum value.

Example 2:

Input: arr = [1,-2,-2,3]
Output: 3
Explanation: We just choose [3] and it's the maximum sum.

Example 3:

Input: arr = [-1,-1,-1,-1]
Output: -1
Explanation: The final subarray needs to be non-empty. You can't choose [-1] and
delete -1 from it, then get an empty subarray to make the sum equals to 0.


Constraints:

1 <= arr.length <= 10^5
-10^4 <= arr[i] <= 10^4

"""

# V0
# IDEA: KADANE with 2 STATES (DP)
"""

 DP def:
    - keep = max subarray sum ending at i, with NO deletion used
    - drop = max subarray sum ending at i, with EXACTLY one deletion used
      (result is still non-empty, since `drop` can only be built
       from a previous non-empty `keep`)


 DP eq:
    - drop = max(drop_prev + x,   # keep x, deletion already spent earlier
                 keep_prev)       # delete x itself
    - keep = max(keep_prev + x, x)
"""
# time = O(n)
# space = O(1)
class Solution(object):
    def maximumSum(self, arr):
        keep = arr[0]
        drop = float('-inf')
        res = arr[0]

        for x in arr[1:]:
            """
            NOTE !!!

            -> update `drop` BEFORE `keep`,
               since `drop` needs the PREVIOUS `keep`
            """
            drop = max(drop + x, keep)
            keep = max(keep + x, x)
            res = max(res, keep, drop)

        return res


# V1
# IDEA: PREFIX (left) + SUFFIX (right) MAX SUBARRAY SUM
# left[i]  = max subarray sum ending at i
# right[i] = max subarray sum starting at i
# deleting arr[i] -> left[i-1] + right[i+1]
# time = O(n)
# space = O(n)
class Solution(object):
    def maximumSum(self, arr):
        n = len(arr)

        left = [0] * n
        left[0] = arr[0]
        for i in range(1, n):
            left[i] = max(left[i - 1], 0) + arr[i]

        right = [0] * n
        right[n - 1] = arr[n - 1]
        for i in range(n - 2, -1, -1):
            right[i] = max(right[i + 1], 0) + arr[i]

        res = max(left)
        for i in range(1, n - 1):
            res = max(res, left[i - 1] + right[i + 1])

        return res
