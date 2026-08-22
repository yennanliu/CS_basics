"""

3010. Divide an Array Into Subarrays With Minimum Cost I
Easy

You are given an array of integers nums of length n.

The cost of an array is the value of its first element. For example, the cost of [1,2,3] is 1 while the cost of [3,4,1] is 3.

You need to divide nums into 3 disjoint contiguous subarrays.

Return the minimum possible sum of the cost of these subarrays.


Example 1:

Input: nums = [1,2,3,12]
Output: 6
Explanation: The best possible way to form 3 subarrays is: [1], [2], and [3,12] at a total cost of 1 + 2 + 3 = 6.
The other possible ways to form 3 subarrays are:
- [1], [2,3], and [12] at a total cost of 1 + 2 + 12 = 15.
- [1,2], [3], and [12] at a total cost of 1 + 3 + 12 = 16.

Example 2:

Input: nums = [5,4,3]
Output: 12
Explanation: The best possible way to form 3 subarrays is: [5], [4], and [3] at a total cost of 5 + 4 + 3 = 12.
It can be shown that 12 is the minimum cost achievable.

Example 3:

Input: nums = [10,3,1,1]
Output: 12
Explanation: The best possible way to form 3 subarrays is: [10,3], [1], and [1] at a total cost of 10 + 1 + 1 = 12.
It can be shown that 12 is the minimum cost achievable.


Constraints:

3 <= n <= 50
1 <= nums[i] <= 50

"""

# V0
# IDEA : THE FIRST COST IS FORCED — PICK THE TWO CHEAPEST STARTS FOR THE REST
#
#   the first subarray always begins at index 0, so nums[0] is paid no matter
#   how the cuts fall. the other two subarrays begin at two DISTINCT indices
#   somewhere in 1..n-1, and any such pair is achievable (the cuts are free
#   to sit anywhere).
#
#   so the answer is nums[0] plus the two smallest values in nums[1:].
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minimumCost(self, nums):
        rest = sorted(nums[1:])
        return nums[0] + rest[0] + rest[1]


# V0-1
# IDEA : BRUTE FORCE OVER EVERY PAIR OF CUT POSITIONS
#
#   a division is fully described by the two indices i < j at which the 2nd and
#   3rd subarrays begin (1 <= i < j <= n-1), and its cost is then simply
#   nums[0] + nums[i] + nums[j]. n <= 50, so all O(n^2) pairs can be tried.
#
#   this is the "no insight needed" baseline that the V0 observation replaces.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def minimumCost(self, nums):
        n = len(nums)
        best = float('inf')
        for i in range(1, n - 1):
            for j in range(i + 1, n):
                cost = nums[0] + nums[i] + nums[j]
                if cost < best:
                    best = cost
        return best


# V0-2
# IDEA : ONE PASS SELECTION OF THE TWO SMALLEST STARTS (NO SORTING)
#
#   same insight as V0 (pay nums[0], then the two cheapest of nums[1:]), but the
#   two minima are tracked with a pair of running variables instead of being read
#   off a sorted copy -- selection instead of a full sort, so O(n) / O(1).
#
#   `first` holds the smallest seen so far, `second` the runner up; a new value
#   either displaces `first` (pushing it down into `second`) or only `second`.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumCost(self, nums):
        first = second = float('inf')
        for i in range(1, len(nums)):
            v = nums[i]
            if v < first:
                first, second = v, first
            elif v < second:
                second = v
        return nums[0] + first + second
