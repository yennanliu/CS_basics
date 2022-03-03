"""

891. Sum of Subsequence Widths
Hard

The width of a sequence is the difference between the maximum and minimum elements in the sequence.

Given an array of integers nums, return the sum of the widths of all the non-empty subsequences of nums. Since the answer may be very large, return it modulo 109 + 7.

A subsequence is a sequence that can be derived from an array by deleting some or no elements without changing the order of the remaining elements. For example, [3,6,2,7] is a subsequence of the array [0,3,1,6,2,2,7].

 

Example 1:

Input: nums = [2,1,3]
Output: 6
Explanation: The subsequences are [1], [2], [3], [2,1], [2,3], [1,3], [2,1,3].
The corresponding widths are 0, 0, 0, 1, 1, 2, 2.
The sum of these widths is 6.
Example 2:

Input: nums = [2]
Output: 0
 

Constraints:

1 <= nums.length <= 105
1 <= nums[i] <= 105

"""

# V0

# V1
# https://leetcode.com/problems/sum-of-subsequence-widths/discuss/1627929/Python-with-explanation
class Solution(object):
    def sumSubseqWidths(self, nums):
        nums = sorted(nums)
        l = len(nums)
        res = 0
        n = 0
        s = 0
        m = 10**9+7
        for i in range(1,l):
            n = (n*2+nums[i-1])%m
            s = (s*2+1)%m
            res = (res + s*nums[i] - n)%m
        return res%m

# V1'
# IDEA : bit op
# https://leetcode.com/problems/sum-of-subsequence-widths/discuss/213939/Python-easy-solution
class Solution(object):
    def sumSubseqWidths(self, A):
        s = diff = 0 
        k = 1 << max(len(A)-2,0)
        A.sort() 
        for i in range(len(A)):
            diff += A[~i] - A[i] 
            s += k * diff 
            k >>= 1 
        return s % (10 ** 9 + 7)

# V1''
# IDEA : MATH
# https://leetcode.com/problems/sum-of-subsequence-widths/solution/
class Solution(object):
    def sumSubseqWidths(self, A):
        MOD = 10**9 + 7
        N = len(A)
        A.sort()

        pow2 = [1]
        for i in range(1, N):
            pow2.append(pow2[-1] * 2 % MOD)

        ans = 0
        for i, x in enumerate(A):
            ans = (ans + (pow2[i] - pow2[N-1-i]) * x) % MOD
        return ans

# V1'''
# IDEA : bit op
# https://leetcode.com/problems/sum-of-subsequence-widths/discuss/451179/Easy-Python-Solution
class Solution:
    def sumSubseqWidths(self, A):
        A.sort()
        l=len(A)
        res=0
        for i in range(l):
            res+=(A[i]<<i)-(A[i]<<(l-i-1))
        return res%(10**9+7)

# V2