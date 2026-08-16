"""

3618. Split Array by Prime Indices
Medium

You are given an integer array nums.

Split nums into two arrays A and B using the following rule:

Elements at prime indices in nums must go into array A.
All other elements must go into array B.

Return the absolute difference between the sums of the two arrays:
|sum(A) - sum(B)|.

Note: An empty array has a sum of 0.


Example 1:

Input: nums = [2,3,4]
Output: 1
Explanation:
The only prime index in the array is 2, so nums[2] = 4 is placed in array A.
The remaining elements, nums[0] = 2 and nums[1] = 3 are placed in array B.
sum(A) = 4, sum(B) = 2 + 3 = 5.
The absolute difference is |4 - 5| = 1.

Example 2:

Input: nums = [-1,5,7,0]
Output: 3
Explanation:
The prime indices in the array are 2 and 3, so nums[2] = 7 and nums[3] = 0 are
placed in array A.
The remaining elements, nums[0] = -1 and nums[1] = 5 are placed in array B.
sum(A) = 7 + 0 = 7, sum(B) = -1 + 5 = 4.
The absolute difference is |7 - 4| = 3.


Constraints:

1 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : SIEVE OF ERATOSTHENES + SIGNED SUM IN ONE PASS
#
#   the split is decided purely by the index, never by the value, so the two
#   arrays never actually need to be materialised. give every element a sign
#   (+1 at a prime index, -1 elsewhere) and sum: that single number is
#   sum(A) - sum(B), and the answer is its absolute value.
#
#   the only real work is deciding primality of every index below n, which a
#   linear-memory sieve settles in O(n log log n) — far cheaper than testing
#   each index on its own.
#
# time = O(n log log n), space = O(n)
class Solution(object):
    def splitArray(self, nums):
        n = len(nums)

        is_prime = [True] * max(n, 2)
        is_prime[0] = is_prime[1] = False
        i = 2
        while i * i < n:
            if is_prime[i]:
                for j in range(i * i, n, i):
                    is_prime[j] = False
            i += 1

        total = 0
        for i in range(n):
            total += nums[i] if is_prime[i] else -nums[i]
        return abs(total)
