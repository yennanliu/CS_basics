"""

2449. Minimum Number of Operations to Make Arrays Similar
Hard

You are given two positive integer arrays nums and target, of the same length.

In one operation, you can choose any two distinct indices i and j where 0 <= i, j < nums.length and:

set nums[i] = nums[i] + 2 and
set nums[j] = nums[j] - 2.

Two arrays are considered to be similar if the frequency of each element is the same.

Return the minimum number of operations required to make nums similar to target. The test cases are generated such that nums can always be similar to target.


Example 1:

Input: nums = [8,12,6], target = [2,14,10]
Output: 2
Explanation: It is possible to make nums similar to target in two operations:
- Choose i = 0 and j = 2, nums = [10,12,4].
- Choose i = 1 and j = 2, nums = [10,14,2].
It can be shown that 2 is the minimum number of operations needed.

Example 2:

Input: nums = [1,2,5], target = [4,1,3]
Output: 1
Explanation: We can make nums similar to target in one operation:
- Choose i = 1 and j = 2, nums = [1,4,3].

Example 3:

Input: nums = [1,1,1,1,1], target = [1,1,1,1,1]
Output: 0
Explanation: The array nums is already similiar to target.


Constraints:

n == nums.length == target.length
1 <= n <= 10^5
1 <= nums[i], target[i] <= 10^6
It is possible to make nums similar to target.

"""

# V0
# IDEA : GREEDY MATCH WITHIN EACH PARITY CLASS
#
#   +2 / -2 never changes an element's parity, so odds must map to odds and
#   evens to evens. sort each parity class and match them in order - for two
#   sorted sequences, the order-preserving pairing minimizes sum |a - b|
#   (any crossing pair can be uncrossed without increasing the total).
#
#   one operation moves 2 up on one side and 2 down on the other, so it
#   reduces the total absolute discrepancy by 4:
#     answer = sum |nums[i] - target[i]| / 4
#
#   NOTE : sorting by the key (x & 1, x) puts evens first then odds, and
#          sorts inside each class, giving both groupings in one pass.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def makeSimilar(self, nums, target):
        a = sorted(nums, key=lambda x: (x & 1, x))
        b = sorted(target, key=lambda x: (x & 1, x))
        total = 0
        for x, y in zip(a, b):
            total += abs(x - y)
        return total // 4
