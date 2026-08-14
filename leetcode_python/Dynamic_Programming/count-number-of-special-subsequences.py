"""

1955. Count Number of Special Subsequences
Hard

A sequence is special if it consists of a positive number of 0s, followed by a positive number of 1s, then a positive number of 2s.

For example, [0,1,2] and [0,0,1,1,1,2] are special.
In contrast, [2,1,0], [1], and [0,1,2,0] are not special.

Given an array nums (consisting of only integers 0, 1, and 2), return the number of different subsequences that are special. Since the answer may be very large, return it modulo 10^9 + 7.

A subsequence of an array is a sequence that can be derived from the array by deleting some or no elements without changing the order of the remaining elements. Two subsequences are different if the set of indices chosen are different.


Example 1:

Input: nums = [0,1,2,2]
Output: 3
Explanation: The special subsequences are bolded [0,1,2,2], [0,1,2,2], and [0,1,2,2].

Example 2:

Input: nums = [2,2,0,0]
Output: 0
Explanation: There are no special subsequences in [2,2,0,0].

Example 3:

Input: nums = [0,1,2,0,1,2]
Output: 7
Explanation: The special subsequences are bolded:
- [0,1,2,0,1,2]
- [0,1,2,0,1,2]
- [0,1,2,0,1,2]
- [0,1,2,0,1,2]
- [0,1,2,0,1,2]
- [0,1,2,0,1,2]
- [0,1,2,0,1,2]


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 2

"""

# V0
# IDEA : DP ON "WHICH BLOCK ARE WE IN" (3 rolling counters, one pass)
#
#   f0 = number of valid prefixes made of 0s only
#   f1 = number of valid prefixes 0...0 1...1
#   f2 = number of complete special subsequences 0...0 1...1 2...2
#
#   seeing a 0 : it can start a brand new subsequence (+1) or extend / be
#                skipped by each existing f0  ->  f0 = 2 * f0 + 1
#   seeing a 1 : it can promote any f0 prefix, or extend / be skipped by each
#                existing f1                  ->  f1 = f0 + 2 * f1
#   seeing a 2 : symmetric                    ->  f2 = f1 + 2 * f2
#
#   the "2 *" is the take-or-skip factor that makes subsequences with different
#   INDEX SETS count separately, exactly as the problem requires.
#
# time = O(n), space = O(1)
class Solution(object):
    def countSpecialSubsequences(self, nums):
        MOD = 10 ** 9 + 7
        f0 = f1 = f2 = 0
        for x in nums:
            if x == 0:
                f0 = (2 * f0 + 1) % MOD
            elif x == 1:
                f1 = (f0 + 2 * f1) % MOD
            else:
                f2 = (f1 + 2 * f2) % MOD
        return f2
