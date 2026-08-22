"""

3038. Maximum Number of Operations With the Same Score I
Easy

Given an array of integers called nums, you can perform the following operation while nums contains at least 2 elements:

Choose the first two elements of nums and delete them.

The score of the operation is the sum of the deleted elements.

Your task is to find the maximum number of operations that can be performed, such that all operations have the same score.

Return the maximum number of operations possible that satisfy the condition mentioned above.


Example 1:

Input: nums = [3,2,1,4,5]
Output: 2
Explanation: We perform the following operations:
- Delete the first two elements, with score 3 + 2 = 5, nums = [1,4,5].
- Delete the first two elements, with score 1 + 4 = 5, nums = [5].
We are unable to perform any more operations as nums contain only 1 element.

Example 2:

Input: nums = [3,2,6,1,4]
Output: 1
Explanation: We perform the following operations:
- Delete the first two elements, with score 3 + 2 = 5, nums = [6,1,4].
We are unable to perform any more operations as the score of the next operation isn't the same as the previous one.


Constraints:

2 <= nums.length <= 100
1 <= nums[i] <= 1000

"""

# V0
# IDEA : THE SCORE IS FIXED BY THE FIRST PAIR — THEN JUST WALK FORWARD
#
#   there is no choice to make : the operation always eats the two leading
#   elements, so the first pair's sum IS the score every later pair must
#   match. count pairs from the left until one disagrees or fewer than two
#   elements remain.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxOperations(self, nums):
        score = nums[0] + nums[1]
        res = 0
        for i in range(0, len(nums) - 1, 2):
            if nums[i] + nums[i + 1] != score:
                break
            res += 1
        return res


# V0-1
# IDEA : RECURSION ON THE REMAINING SUFFIX
#
#   the score is decided once, up front, by the first pair; after that the
#   only state is "where does the array start now".
#     count(i) = 0                      if fewer than 2 elements are left
#              = 0                      if nums[i] + nums[i+1] != score
#              = 1 + count(i + 2)       otherwise
#
# time = O(n)
# space = O(n) recursion depth (n/2 frames)
class Solution(object):
    def maxOperations(self, nums):
        score = nums[0] + nums[1]

        def count(i):
            if i + 1 >= len(nums) or nums[i] + nums[i + 1] != score:
                return 0
            return 1 + count(i + 2)

        return count(0)


# V0-2
# IDEA : MATERIALISE THE PAIR SUMS, THEN MEASURE THE LEADING RUN (itertools)
#
#   zip(nums[::2], nums[1::2]) IS the sequence of operations, so the answer is
#   the length of the first constant run of that sum sequence.
#   itertools.groupby yields that run directly -- no index arithmetic and no
#   early-exit branch, paid for by building the whole pair-sum list first.
#
# time = O(n)
# space = O(n)
from itertools import groupby


class Solution(object):
    def maxOperations(self, nums):
        sums = [a + b for a, b in zip(nums[::2], nums[1::2])]
        _, run = next(groupby(sums))
        return len(list(run))
