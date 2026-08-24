"""

1569. Number of Ways to Reorder Array to Get Same BST
Hard

Given an array nums that represents a permutation of integers from 1 to n. We are going to construct a binary search tree (BST) by inserting the elements of nums in order into an initially empty BST. Find the number of different ways to reorder nums so that the constructed BST is identical to that formed from the original array nums.

For example, given nums = [2,1,3], we will have 2 as the root, 1 as a left child, and 3 as a right child. The array [2,3,1] also yields the same BST but [3,2,1] yields a different BST.

Return the number of ways to reorder nums such that the BST formed is identical to the original BST formed from nums.

Since the answer may be very large, return it modulo 10^9 + 7.

Example 1:

Input: nums = [2,1,3]
Output: 1
Explanation: We can reorder nums to be [2,3,1] which will yield the same BST. There are no other ways to reorder nums which will yield the same BST.

Example 2:

Input: nums = [3,4,5,1,2]
Output: 5
Explanation: The following 5 arrays will yield the same BST:
[3,1,2,4,5]
[3,1,4,2,5]
[3,1,4,5,2]
[3,4,1,2,5]
[3,4,1,5,2]

Example 3:

Input: nums = [1,2,3]
Output: 0
Explanation: There are no other orderings of nums that will yield the same BST.

Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= nums.length
All integers in nums are distinct.

"""

# V0
# IDEA : COMBINATORICS + DIVIDE & CONQUER (interleave the two subtrees)
#
#   the root must stay first. the remaining len-1 values split into the
#   "< root" list and the "> root" list, and their relative order inside
#   each list must be kept -> the number of interleavings is
#     C(len - 1, len(left))
#   and the same reasoning applies recursively, so
#     ways(nums) = PRODUCT over every node of C(size - 1, left_size)
#   answer = ways - 1 (the original ordering does not count).
#   NOTE : an explicit stack is used instead of recursion, a sorted
#          input would otherwise nest 1000 levels deep.
#
"""

DP def
    (divide & conquer + combinatorics)

    the root must stay FIRST. the remaining len-1 values split into the
    "< root" list and the "> root" list, and the relative order INSIDE each
    list must be kept - so the two lists may be interleaved freely.

    ways(list): number of orderings of `list` producing the same BST

DP eq

     ways(cur) = C( len(cur) - 1, len(left) ) * ways(left) * ways(right)

               -> so overall

     result = PRODUCT over every node of C(size - 1, left_size)


    -> e.g. C(...) is the number of interleavings of the two subtree
              sequences; Pascal's triangle supplies the binomials

     NOTE !!! an explicit STACK replaces recursion - a sorted input would
              otherwise nest 1000 levels deep

     ans = (result - 1) % (10^9 + 7)     # the original ordering does not count

"""
# time = O(n^2), space = O(n^2)
class Solution(object):
    def numOfWays(self, nums):
        MOD = 10 ** 9 + 7
        n = len(nums)
        # pascal triangle : C[i][j]
        C = [[0] * (n + 1) for _ in range(n + 1)]
        for i in range(n + 1):
            C[i][0] = 1
            for j in range(1, i + 1):
                C[i][j] = (C[i - 1][j - 1] + C[i - 1][j]) % MOD

        res = 1
        stack = [nums]
        while stack:
            cur = stack.pop()
            if len(cur) <= 2:
                continue
            root = cur[0]
            left = [x for x in cur if x < root]
            right = [x for x in cur if x > root]
            res = res * C[len(cur) - 1][len(left)] % MOD
            stack.append(left)
            stack.append(right)
        return (res - 1) % MOD
