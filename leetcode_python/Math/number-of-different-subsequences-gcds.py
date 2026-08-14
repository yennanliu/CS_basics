"""

1819. Number of Different Subsequences GCDs
Hard

You are given an array nums that consists of positive integers.

The GCD of a sequence of numbers is defined as the greatest integer that divides all the numbers in the sequence evenly.

For example, the GCD of the sequence [4,6,16] is 2.

A subsequence of an array is a sequence that can be formed by removing some elements (possibly none) of the array.

For example, [2,5,10] is a subsequence of [1,2,1,2,4,1,5,10].

Return the number of different GCDs among all non-empty subsequences of nums.


Example 1:

Input: nums = [6,10,3]
Output: 5
Explanation: The figure shows all the non-empty subsequences and their GCDs.
The different GCDs are 6, 10, 3, 2, and 1.

Example 2:

Input: nums = [5,15,40,5,6]
Output: 7


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 2 * 10^5

"""

# V0
# IDEA : ENUMERATE THE CANDIDATE GCD, NOT THE SUBSEQUENCE (harmonic sieve)
#
#   there are 2^n subsequences but only <= 2*10^5 possible gcd values, so flip
#   the question: for each candidate x, is x achievable?
#
#   key fact : if x is achievable at all, it is achievable by the subsequence
#   of ALL multiples of x present in nums -- adding more multiples of x can
#   only shrink the gcd towards x, never below it. so:
#     g = gcd of every y in nums with x | y ;  x is achievable  <=>  g == x
#
#   walking x = 1..mx and its multiples is the harmonic sum -> O(M log M),
#   and we can stop early the moment the running gcd already equals x.
#
#   NOTE : `vis` is a set of the values present, so duplicates cost nothing.
#
# time = O(M log M + n), M = max(nums)
# space = O(M)
from math import gcd
class Solution(object):
    def countDifferentSubsequenceGCDs(self, nums):
        present = set(nums)
        mx = max(nums)

        res = 0
        for x in range(1, mx + 1):
            g = 0
            for y in range(x, mx + 1, x):
                if y in present:
                    g = gcd(g, y)
                    if g == x:
                        res += 1
                        break
        return res
