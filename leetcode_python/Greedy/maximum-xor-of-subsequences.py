"""

3681. Maximum XOR of Subsequences
Hard

You are given an integer array nums of length n where each element is a
non-negative integer.

Select two subsequences of nums (they may be empty and are allowed to
overlap), each preserving the original order of elements, and let:

X be the bitwise XOR of all elements in the first subsequence.
Y be the bitwise XOR of all elements in the second subsequence.

Return the maximum possible value of X XOR Y.

Note: The XOR of an empty subsequence is 0.


Example 1:

Input: nums = [1,2,3]
Output: 3
Explanation:
Choose subsequences:
First subsequence [2], whose XOR is 2.
Second subsequence [2,3], whose XOR is 1.
Then, XOR of both subsequences = 2 XOR 1 = 3.
This is the maximum XOR value achievable from any two subsequences.

Example 2:

Input: nums = [5,2]
Output: 7
Explanation:
Choose subsequences:
First subsequence [5], whose XOR is 5.
Second subsequence [2], whose XOR is 2.
Then, XOR of both subsequences = 5 XOR 2 = 7.
This is the maximum XOR value achievable from any two subsequences.


Constraints:

2 <= nums.length <= 10^5
0 <= nums[i] <= 10^9

"""

# V0
# IDEA : TWO OVERLAPPING SUBSEQUENCES COLLAPSE TO ONE SUBSET -- LINEAR BASIS
#
#   X ^ Y cancels every element chosen by BOTH subsequences, so X ^ Y is the
#   xor over the symmetric difference. and since the two picks may overlap
#   freely, any subset S can be realised (put S in the first pick, nothing in
#   the second), while no subset outside the reachable set appears. so the
#   "two subsequences" wrapper is decoration: the answer is just the maximum
#   xor of any subset of nums.
#
#   order does not matter for xor, so the reachable values are precisely the
#   GF(2) linear span of nums. gaussian elimination reduces nums to a basis
#   in row-echelon form -- at most 30 vectors, one per leading bit -- and
#   then a greedy pass from the top bit down maximises: xor in a basis
#   vector whenever it raises the running value. that is optimal because the
#   vector owning bit b is the ONLY one that can flip bit b once higher bits
#   are frozen, so the decision at each bit is forced and independent.
#
# time = O(n log C), space = O(log C)
class Solution(object):
    def maxXorSubsequences(self, nums):
        bits = max(nums).bit_length()
        basis = [0] * max(bits, 1)
        for x in nums:
            for b in range(bits - 1, -1, -1):
                if not (x >> b) & 1:
                    continue
                if basis[b] == 0:
                    basis[b] = x
                    break
                x ^= basis[b]

        best = 0
        for b in range(len(basis) - 1, -1, -1):
            if best ^ basis[b] > best:
                best ^= basis[b]
        return best
