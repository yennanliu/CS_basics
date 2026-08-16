"""

3513. Number of Unique XOR Triplets I
Medium

You are given an integer array nums of length n, where nums is a permutation of
the numbers in the range [1, n].

A XOR triplet is defined as the XOR of three elements nums[i] XOR nums[j] XOR
nums[k] where i <= j <= k.

Return the number of unique XOR triplet values from all possible triplets
(i, j, k).


Example 1:

Input: nums = [1,2]
Output: 2
Explanation:
The possible XOR triplet values are:
(0, 0, 0) -> 1 XOR 1 XOR 1 = 1
(0, 0, 1) -> 1 XOR 1 XOR 2 = 2
(0, 1, 1) -> 1 XOR 2 XOR 2 = 1
(1, 1, 1) -> 2 XOR 2 XOR 2 = 2
The unique XOR values are {1, 2}, so the output is 2.

Example 2:

Input: nums = [3,1,2]
Output: 4
Explanation:
The possible XOR triplet values include:
(0, 0, 0) -> 3 XOR 3 XOR 3 = 3
(0, 0, 1) -> 3 XOR 3 XOR 1 = 1
(0, 0, 2) -> 3 XOR 3 XOR 2 = 2
(0, 1, 2) -> 3 XOR 1 XOR 2 = 0
The unique XOR values are {0, 1, 2, 3}, so the output is 4.


Constraints:

1 <= n == nums.length <= 10^5
1 <= nums[i] <= n
nums is a permutation of integers from 1 to n.

"""

# V0
# IDEA : CLOSURE OF {1..n} UNDER XOR IS THE FULL POWER-OF-TWO BLOCK
#
#   because i <= j <= k allows repeats, taking i == j cancels a pair and leaves
#   just nums[k] — so every element 1..n is reachable on its own.
#
#   for n >= 3 the three-distinct case fills in everything else: {1..n} spans
#   all bit positions below 2^b where b = n.bit_length(), and xoring triples of
#   such numbers generates that whole block, including 0 (e.g. 1 ^ 2 ^ 3).  so
#   the answer is 2^b, the smallest power of two strictly greater than n.
#
#   n = 1 and n = 2 are special: there are not enough distinct elements to form
#   a genuine triple, so only the n singletons are reachable.
#
# time = O(1) after reading n, space = O(1)
class Solution(object):
    def uniqueXorTriplets(self, nums):
        n = len(nums)
        if n < 3:
            return n
        return 1 << n.bit_length()
