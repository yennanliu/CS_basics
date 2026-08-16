"""

3589. Count Prime-Gap Balanced Subarrays
Medium

You are given an integer array nums and an integer k.

Call a subarray prime-gap balanced if:

It contains at least two prime numbers, and
The difference between the maximum and minimum prime numbers in that subarray is less than or equal to k.

Return the count of prime-gap balanced subarrays in nums.

Note:

A subarray is a contiguous non-empty sequence of elements within an array.
A prime number is a natural number greater than 1 with only two factors, 1 and itself.


Example 1:

Input: nums = [1,2,3], k = 1
Output: 2
Explanation:
Prime-gap balanced subarrays are:
[2,3]: contains two primes (2 and 3), max - min = 3 - 2 = 1 <= k
[1,2,3]: contains two primes (2 and 3), max - min = 3 - 2 = 1 <= k
Thus, the answer is 2.

Example 2:

Input: nums = [2,3,5,7], k = 3
Output: 4
Explanation:
Prime-gap balanced subarrays are:
[2,3]: contains two primes (2 and 3), max - min = 3 - 2 = 1 <= k
[2,3,5]: contains three primes (2, 3 and 5), max - min = 5 - 2 = 3 <= k
[3,5]: contains two primes (3 and 5), max - min = 5 - 3 = 2 <= k
[5,7]: contains two primes (5 and 7), max - min = 7 - 5 = 2 <= k
Thus, the answer is 4.


Constraints:

1 <= nums.length <= 5 * 10^4
1 <= nums[i] <= 5 * 10^4
0 <= k <= 5 * 10^4

"""

# V0
# IDEA : TWO OPPOSING BOUNDS ON THE LEFT ENDPOINT, FOR EACH RIGHT ENDPOINT
#
#   fix the right end r. shrinking the window from the left can only remove
#   primes, so the "max prime - min prime <= k" condition holds for every
#   left >= some threshold L (monotone, maintained by two monotonic deques).
#   the "at least two primes" condition holds for every left <= the index of
#   the second-to-last prime seen so far.
#
#   so the valid lefts are exactly the interval [L, second_last_prime_index]
#   and the count is its size — no inner loop needed.
#
# time = O(n log log max + n), space = O(max)
from collections import deque


class Solution(object):
    def primeSubarray(self, nums, k):
        mx = max(nums)
        sieve = bytearray([1]) * (mx + 1)
        if mx >= 0:
            sieve[0] = 0
        if mx >= 1:
            sieve[1] = 0
        i = 2
        while i * i <= mx:
            if sieve[i]:
                sieve[i * i::i] = bytearray(len(sieve[i * i::i]))
            i += 1

        maxq, minq = deque(), deque()
        prime_idx = []
        left = 0
        ans = 0
        for r, v in enumerate(nums):
            if sieve[v]:
                while maxq and nums[maxq[-1]] <= v:
                    maxq.pop()
                maxq.append(r)
                while minq and nums[minq[-1]] >= v:
                    minq.pop()
                minq.append(r)
                prime_idx.append(r)
            while maxq and nums[maxq[0]] - nums[minq[0]] > k:
                if maxq[0] == left:
                    maxq.popleft()
                if minq[0] == left:
                    minq.popleft()
                left += 1
            if len(prime_idx) >= 2:
                hi = prime_idx[-2]
                if hi >= left:
                    ans += hi - left + 1
        return ans
