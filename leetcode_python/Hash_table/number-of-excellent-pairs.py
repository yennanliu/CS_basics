"""

2354. Number of Excellent Pairs
Hard

You are given a 0-indexed positive integer array nums and a positive integer k.

A pair of numbers (num1, num2) is called excellent if the following conditions are satisfied:

Both the numbers num1 and num2 exist in the array nums.
The sum of the number of set bits in num1 OR num2 and num1 AND num2 is greater than or equal to k, where OR is the bitwise OR operation and AND is the bitwise AND operation.

Return the number of distinct excellent pairs.

Two pairs (a, b) and (c, d) are considered distinct if either a != c or b != d. For example, (1, 2) and (2, 1) are distinct.

Note that a pair (num1, num2) such that num1 == num2 can also be excellent if you have at least one occurrence of num1 in the array.


Example 1:

Input: nums = [1,2,3,1], k = 3
Output: 5
Explanation: The excellent pairs are the following:
- (3, 3). (3 AND 3) and (3 OR 3) are both equal to (11) in binary. The total number of set bits is 2 + 2 = 4, which is greater than or equal to k = 3.
- (2, 3) and (3, 2). (2 AND 3) is equal to (10) in binary, and (2 OR 3) is equal to (11) in binary. The total number of set bits is 1 + 2 = 3.
- (1, 3) and (3, 1). (1 AND 3) is equal to (01) in binary, and (1 OR 3) is equal to (11) in binary. The total number of set bits is 1 + 2 = 3.
So the number of excellent pairs is 5.

Example 2:

Input: nums = [5,1,1], k = 10
Output: 0
Explanation: There are no excellent pairs for this array.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
1 <= k <= 60

"""

# V0
# IDEA : BIT COUNTING IDENTITY + SUFFIX COUNTS
#
#   Key identity : popcount(a | b) + popcount(a & b) == popcount(a) + popcount(b)
#   (every bit position contributes to OR/AND exactly as often as it appears
#    in a and b separately).
#
#   So the condition collapses to  bits(a) + bits(b) >= k, and the values
#   themselves no longer matter -- only their popcount.
#
#   NOTE : "distinct pairs" -> dedupe nums first, but ordered pairs count twice
#          and (x, x) is allowed, so just count ordered pairs over the buckets.
#
#   Bucket the distinct values by popcount (0..30), build a suffix-sum over the
#   buckets, and for each bucket b add cnt[b] * suffix[max(0, k - b)].
#
# time = O(n + 31), space = O(n)
class Solution(object):
    def countExcellentPairs(self, nums, k):
        cnt = [0] * 32
        for v in set(nums):
            b = bin(v).count("1")
            cnt[b] += 1

        # suffix[i] = number of distinct values with popcount >= i
        suffix = [0] * 34
        for i in range(31, -1, -1):
            suffix[i] = suffix[i + 1] + cnt[i]

        res = 0
        for b in range(32):
            if cnt[b] == 0:
                continue
            need = k - b
            if need < 0:
                need = 0
            if need > 32:
                continue
            res += cnt[b] * suffix[need]
        return res
