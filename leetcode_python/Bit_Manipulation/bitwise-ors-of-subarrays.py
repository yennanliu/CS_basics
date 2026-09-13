"""

898. Bitwise ORs of Subarrays
Medium

Given an integer array arr, return the number of distinct bitwise ORs of all the non-empty subarrays of arr.

The bitwise OR of a subarray is the bitwise OR of each integer in the subarray. The bitwise OR of a subarray of one integer is that integer.

A subarray is a contiguous non-empty sequence of elements within an array.

Example 1:

Input: arr = [0]
Output: 1
Explanation: There is only one possible result: 0.

Example 2:

Input: arr = [1,1,2]
Output: 3
Explanation: The possible subarrays are [1], [1], [2], [1, 1], [1, 2], [1, 1, 2].
These yield the results 1, 1, 2, 1, 3, 3.
There are 3 unique values, so the answer is 3.

Example 3:

Input: arr = [1,2,4]
Output: 6
Explanation: The possible results are 1, 2, 3, 4, 6, and 7.

Constraints:

1 <= arr.length <= 5 * 10^4
0 <= arr[i] <= 10^9

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83511833
# IDEA : DP
# STATUS EQ : dp[i] = [b | A[i] for b in dp[i - 1]] + A[i]
# time = O(n * 30)  # n = len(A); each cur has at most O(log(max(A))) distinct values
# space = O(n * 30)  # res can hold up to n * 30 distinct OR values
class Solution(object):
    def subarrayBitwiseORs(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        res = set()
        cur = set()
        for a in A:
            cur = {n | a for n in cur} | {a}
            res |= cur
        return len(res)
        
# V2
# time = O(32 * n)  # n = len(A)
# space = O(32 * n)  # result/curr can hold up to O(32 * n) distinct OR values
class Solution(object):
    def subarrayBitwiseORs(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        result, curr = set(), {0}
        for i in A:
            curr = {i} | {i | j for j in curr}
            result |= curr
        return len(result)