"""

3115. Maximum Prime Difference
Medium

You are given an integer array nums.

Return an integer that is the maximum distance between the indices of two (not necessarily different) prime numbers in nums.


Example 1:

Input: nums = [4,2,9,5,3]
Output: 3
Explanation: nums[1], nums[3], and nums[4] are prime. So the answer is |4 - 1| = 3.

Example 2:

Input: nums = [4,8,2,8]
Output: 0
Explanation: nums[2] is prime. Because there is just one prime number, the answer is |2 - 2| = 0.


Constraints:

1 <= nums.length <= 3 * 10^5
1 <= nums[i] <= 100
The input is generated such that the number of prime numbers in the nums is at least one.

"""

# V0
# IDEA : ONLY THE FIRST AND LAST PRIME POSITIONS MATTER
#
#   the widest gap is always between the earliest prime and the latest one,
#   so a single pass recording those two indices answers it. "not necessarily
#   different" means a lone prime gives 0, which falls out of first == last.
#
#   values are capped at 100, so primality is a set lookup.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumPrimeDifference(self, nums):
        primes = set()
        for x in range(2, 101):
            if all(x % d for d in range(2, int(x ** 0.5) + 1)):
                primes.add(x)

        first = last = -1
        for i, v in enumerate(nums):
            if v in primes:
                if first == -1:
                    first = i
                last = i
        return last - first
