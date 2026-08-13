"""

1144. Decrease Elements To Make Array Zigzag
Medium

Given an array nums of integers, a move consists of choosing any element
and decreasing it by 1.

An array A is a zigzag array if either:

Every even-indexed element is greater than adjacent elements,
ie. A[0] > A[1] < A[2] > A[3] < A[4] > ...
OR, every odd-indexed element is greater than adjacent elements,
ie. A[0] < A[1] > A[2] < A[3] > A[4] < ...

Return the minimum number of moves to transform the given array nums into a zigzag array.


Example 1:

Input: nums = [1,2,3]
Output: 2
Explanation: We can decrease 2 to 0 or 3 to 1.

Example 2:

Input: nums = [9,6,1,6,2]
Output: 4


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 1000

"""

# V0
# IDEA : GREEDY + ENUMERATE THE 2 PARITIES
#        we may only DECREASE, so it never helps to touch the "peak" positions.
#        -> just pick which parity becomes the "valley" (smaller than neighbours),
#           and for every valley index j push nums[j] down to
#           min(neighbours) - 1 (cost = max(0, nums[j] - min(nb) + 1)).
#        -> valleys are non adjacent, so their costs are independent.
#        answer = min(cost when even indices are valleys,
#                     cost when odd  indices are valleys)
# time = O(n)
# space = O(1)
class Solution(object):
    def movesToMakeZigzag(self, nums):
        n = len(nums)
        res = [0, 0]
        for i in range(2):
            for j in range(i, n, 2):
                nb = float('inf')
                if j - 1 >= 0:
                    nb = min(nb, nums[j - 1])
                if j + 1 < n:
                    nb = min(nb, nums[j + 1])
                if nb != float('inf') and nums[j] >= nb:
                    res[i] += nums[j] - nb + 1
        return min(res)
