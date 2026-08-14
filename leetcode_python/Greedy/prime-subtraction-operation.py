"""

2601. Prime Subtraction Operation
Medium

You are given a 0-indexed integer array nums of length n.

You can perform the following operation as many times as you want:

- Pick an index i that you haven't picked before, and pick a prime p strictly less than nums[i], then subtract p from nums[i].

Return true if you can make nums a strictly increasing array using the above operation and false otherwise.

A strictly increasing array is an array whose each element is strictly greater than its preceding element.


Example 1:

Input: nums = [4,9,6,10]
Output: true
Explanation: In the first operation: Pick i = 0 and p = 3, and then subtract 3 from nums[0], so that nums becomes [1,9,6,10].
In the second operation: i = 1, p = 7, subtract 7 from nums[1], so nums becomes equal to [1,2,6,10].
After the second operation, nums is sorted in strictly increasing order, so the answer is true.

Example 2:

Input: nums = [6,8,11,12]
Output: true
Explanation: Initially nums is sorted in strictly increasing order, so we don't need to make any operations.

Example 3:

Input: nums = [5,8,3]
Output: false
Explanation: It can be proven that there is no way to perform operations to make nums sorted in strictly increasing order, so the answer is false.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 1000
nums.length == n

"""

# V0
# IDEA : SIEVE + GREEDY (make every element as small as legally possible)
#
#   scan left -> right keeping `prev` = the value we settled on for i-1.
#   for nums[i] we want the SMALLEST achievable value that is still > prev,
#   because a smaller value can only help the elements after it.
#
#   achievable values of nums[i] are nums[i] itself (do nothing) and
#   nums[i] - p for every prime p < nums[i]. so we take the LARGEST prime
#   p < nums[i] with nums[i] - p > prev.
#
#   NOTE : p must be STRICTLY less than nums[i] (so the result stays >= 1),
#          and the operation is optional — "do nothing" is always a candidate.
#   NOTE : if even the best candidate is <= prev, no later choice can rescue
#          it (values only shrink), so we can bail out with False right away.
#
# time = O(V log log V + n * pi(V)), space = O(V)   ; V = 1000 = max value
class Solution(object):
    def primeSubOperation(self, nums):
        LIMIT = 1000
        is_prime = [True] * (LIMIT + 1)
        is_prime[0] = is_prime[1] = False
        i = 2
        while i * i <= LIMIT:
            if is_prime[i]:
                for j in range(i * i, LIMIT + 1, i):
                    is_prime[j] = False
            i += 1
        primes = [x for x in range(2, LIMIT + 1) if is_prime[x]]

        prev = 0
        for x in nums:
            best = x if x > prev else None
            # primes ascending -> keep the last (largest) prime that works
            for p in primes:
                if p >= x:
                    break
                if x - p > prev:
                    best = x - p
            if best is None:
                return False
            prev = best
        return True
