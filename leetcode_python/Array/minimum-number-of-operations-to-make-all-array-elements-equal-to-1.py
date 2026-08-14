"""

2654. Minimum Number of Operations to Make All Array Elements Equal to 1
Medium

You are given a 0-indexed array nums consisting of positive integers. You can do the following operation on the array any number of times:

Select an index i such that 0 <= i < n - 1 and replace either of nums[i] or nums[i+1] with their gcd value.

Return the minimum number of operations to make all elements of nums equal to 1. If it is impossible, return -1.

The gcd of two integers is the greatest common divisor of the two integers.


Example 1:

Input: nums = [2,6,3,4]
Output: 4
Explanation: We can do the following operations:
- Choose index i = 2 and replace nums[2] with gcd(3,4) = 1. Now we have nums = [2,6,1,4].
- Choose index i = 1 and replace nums[1] with gcd(6,1) = 1. Now we have nums = [2,1,1,4].
- Choose index i = 0 and replace nums[0] with gcd(2,1) = 1. Now we have nums = [1,1,1,4].
- Choose index i = 2 and replace nums[3] with gcd(1,4) = 1. Now we have nums = [1,1,1,1].

Example 2:

Input: nums = [2,10,6,14]
Output: -1
Explanation: It can be shown that it is impossible to make all the elements equal to 1.


Constraints:

2 <= nums.length <= 50
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : GCD + SHORTEST SUBARRAY WHOSE GCD IS 1
#
#   ONCE a single 1 exists, every other cell can be turned into 1 with ONE
#   operation each (gcd(1, anything) == 1) — so the cost is just the number
#   of non-1 cells.
#
#   so :
#     1) if nums already contains c ones -> answer = n - c        (no gcd work)
#     2) if gcd(all nums) != 1 -> every reachable value stays a multiple of
#        that gcd -> impossible -> -1
#     3) otherwise, find the SHORTEST subarray with gcd == 1, say of length L.
#        collapsing it into a 1 costs L - 1 operations, then spreading that 1
#        to the remaining n - 1 cells costs n - 1 more :
#            answer = (L - 1) + (n - 1)
#        n is only 50, so the O(n^2) double loop over subarray gcds is fine.
#
#   NOTE : step 1 MUST come first — with an existing 1 the shortest gcd-1
#          subarray has length 1 and the formula would over-count.
#
#   NOTE : the running gcd is monotonically non-increasing, so the inner loop
#          can break as soon as it hits 1.
#
# time = O(n^2 * log(max(nums))), space = O(1)
class Solution(object):
    def minOperations(self, nums):
        n = len(nums)

        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        ones = nums.count(1)
        if ones > 0:
            return n - ones

        g = 0
        for v in nums:
            g = gcd(g, v)
        if g != 1:
            return -1

        best = n                              # length - 1 of the best subarray
        for i in range(n):
            cur = nums[i]
            for j in range(i + 1, n):
                cur = gcd(cur, nums[j])
                if cur == 1:
                    if j - i < best:
                        best = j - i
                    break
        return best + n - 1
