"""

3551. Minimum Swaps to Sort by Digit Sum
Medium

You are given an array nums of distinct positive integers. You need to sort the
array in increasing order based on the sum of the digits of each number. If two
numbers have the same digit sum, the smaller number appears first in the sorted
order.

Return the minimum number of swaps required to rearrange nums into this sorted
order.

A swap is defined as exchanging the values at two distinct positions in the
array.

Example 1:

Input: nums = [37,100]

Output: 1

Explanation:

Compute the digit sum for each integer: [3 + 7 = 10, 1 + 0 + 0 = 1] → [10, 1]

Sort the integers based on digit sum: [100, 37]. Swap 37 with 100 to obtain the
sorted order.

Thus, the minimum number of swaps required to rearrange nums is 1.

Example 2:

Input: nums = [22,14,33,7]

Output: 0

Explanation:

Compute the digit sum for each integer: [2 + 2 = 4, 1 + 4 = 5, 3 + 3 = 6, 7 = 7]
→ [4, 5, 6, 7]

Sort the integers based on digit sum: [22, 14, 33, 7]. The array is already
sorted.

Thus, the minimum number of swaps required to rearrange nums is 0.

Example 3:

Input: nums = [18,43,34,16]

Output: 2

Explanation:

Compute the digit sum for each integer: [1 + 8 = 9, 4 + 3 = 7, 3 + 4 = 7, 1 + 6
= 7] → [9, 7, 7, 7]

Sort the integers based on digit sum: [16, 34, 43, 18]. Swap 18 with 16, and
swap 43 with 34 to obtain the sorted order.

Thus, the minimum number of swaps required to rearrange nums is 2.

Constraints:

1 <= nums.length <= 10^5

1 <= nums[i] <= 10^9

nums consists of distinct positive integers.

"""

# V0
# IDEA : PERMUTATION CYCLE DECOMPOSITION
#
#   the target arrangement is a fixed permutation of the current one (values are
#   distinct, so the sort key (digit_sum, value) is a total order and the target
#   position of every element is unique).
#
#   a permutation that decomposes into c cycles needs exactly n - c swaps: each
#   cycle of length L costs L - 1 swaps, and no scheme can do better because a
#   single swap can merge at most two cycles into one.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minSwaps(self, nums):
        n = len(nums)
        order = sorted(range(n), key=lambda i: (sum(int(c) for c in str(nums[i])), nums[i]))
        # order[p] = index in nums of the element that must end up at position p
        seen = [False] * n
        cycles = 0
        for start in range(n):
            if seen[start]:
                continue
            cycles += 1
            j = start
            while not seen[j]:
                seen[j] = True
                j = order[j]
        return n - cycles
