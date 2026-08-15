"""

3073. Maximum Increasing Triplet Value
Medium
🔒 (premium)

Given an array nums, return the maximum value of a triplet (i, j, k) such that i < j < k and nums[i] < nums[j] < nums[k].

The value of a triplet (i, j, k) is nums[i] - nums[j] + nums[k].


Example 1:

Input: nums = [5,6,9]
Output: 8
Explanation: We only have one choice for an increasing triplet and that is choosing all three elements. The value of this triplet would be 5 - 6 + 9 = 8.

Example 2:

Input: nums = [1,5,3,6]
Output: 4
Explanation: There are only two increasing triplets:
(0, 1, 3): The value of this triplet is nums[0] - nums[1] + nums[3] = 1 - 5 + 6 = 2.
(0, 2, 3): The value of this triplet is nums[0] - nums[2] + nums[3] = 1 - 3 + 6 = 4.
Thus the answer would be 4.


Constraints:

3 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
The input is generated such that at least one triplet meets the given condition.

"""

# V0
# IDEA : FIX THE MIDDLE, THEN WANT THE BIGGEST VALID LEFT AND RIGHT
#
#   for a fixed j the value splits cleanly :
#       nums[i] - nums[j] + nums[k]
#   so take the LARGEST nums[i] < nums[j] with i < j, and the LARGEST
#   nums[k] > nums[j] with k > j — the two choices are independent.
#
#   right side : a suffix maximum array. it is a valid k exactly when that
#   maximum exceeds nums[j] (nothing bigger can hide behind a bigger value).
#
#   left side : "max value strictly below nums[j] among what came before" is
#   a prefix-max query over the VALUE axis, so compress the values and keep a
#   Fenwick tree of maxima indexed by rank.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maximumTripletValue(self, nums):
        n = len(nums)
        order = sorted(set(nums))
        rank = {v: i + 1 for i, v in enumerate(order)}
        size = len(order)

        tree = [0] * (size + 1)          # Fenwick tree of prefix maxima

        def update(i, val):
            while i <= size:
                if tree[i] < val:
                    tree[i] = val
                i += i & -i

        def query(i):                    # max over ranks 1..i, 0 when empty
            best = 0
            while i > 0:
                if tree[i] > best:
                    best = tree[i]
                i -= i & -i
            return best

        suffix_max = [0] * (n + 1)
        for t in range(n - 1, -1, -1):
            suffix_max[t] = max(suffix_max[t + 1], nums[t])

        res = float('-inf')
        for j in range(n):
            left = query(rank[nums[j]] - 1)      # biggest value < nums[j], so far
            right = suffix_max[j + 1]
            if left > 0 and right > nums[j]:
                res = max(res, left - nums[j] + right)
            update(rank[nums[j]], nums[j])
        return res
