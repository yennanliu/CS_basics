"""

1018. Binary Prefix Divisible By 5
Easy

You are given a binary array nums (0-indexed).

We define xi as the number whose binary representation is the subarray nums[0..i] (from most-significant-bit to least-significant-bit).

For example, if nums = [1,0,1], then x0 = 1, x1 = 2, and x2 = 5.

Return an array of booleans answer where answer[i] is true if xi is divisible by 5.


Example 1:

Input: nums = [0,1,1]
Output: [true,false,false]
Explanation: The input numbers in binary are 0, 01, 011; which are 0, 1, and 3 in base-10.
Only the first number is divisible by 5, so answer[0] is true.

Example 2:

Input: nums = [1,1,1]
Output: [false,false,false]


Constraints:

1 <= nums.length <= 10^5
nums[i] is either 0 or 1.

"""

# V0
# IDEA : RUNNING REMAINDER (mod 5)
#
#   x_i = x_(i-1) * 2 + nums[i]
#   we only ever need x_i mod 5, so keep the remainder instead of the
#   (possibly 10^5 bit long) number itself.
#
#     cur = (cur * 2 + nums[i]) % 5
#
# time = O(n)
# space = O(1), excluding the output
class Solution(object):
    def prefixesDivBy5(self, nums):
        res = []
        cur = 0
        for v in nums:
            cur = (cur * 2 + v) % 5
            res.append(cur == 0)
        return res


# V0-1
# IDEA : BRUTE FORCE (keep the exact value, never reduce it)
#
#   x_i = (x_(i-1) << 1) | nums[i], built as a real python integer, and test
#   x_i % 5 == 0.  correct but the number grows to n bits, so every step pays
#   a big-int shift + modulo instead of a single machine-word operation.
#
# time  = O(n^2 / w), w = machine word size (each step's big-int op is O(i / w))
# space = O(n) for the n-bit integer
class Solution(object):
    def prefixesDivBy5(self, nums):
        res = []
        cur = 0
        for v in nums:
            cur = (cur << 1) | v
            res.append(cur % 5 == 0)
        return res


# V0-2
# IDEA : PERIODICITY OF 2^k MOD 5 (bit-position counters, no running value)
#
#   2^k mod 5 cycles with period 4 :  1, 2, 4, 3, 1, 2, 4, 3, ...
#   and x_i = sum_{j<=i} nums[j] * 2^(i-j), so
#
#       x_i mod 5 = sum_{j<=i} nums[j] * POW2[(i - j) % 4]
#
#   bucket the set bits by j % 4 -- cnt[k] = how many set bits so far sit at a
#   position j with j % 4 == k -- and each prefix is then scored by 4 fixed
#   multiplications.  nothing is carried between steps, unlike V0's running
#   remainder; the counters are kept mod 5 so they never grow.
#
# time  = O(n) (4 constant-time terms per index)
# space = O(1), excluding the output
class Solution(object):
    def prefixesDivBy5(self, nums):
        POW2 = (1, 2, 4, 3)              # 2^0, 2^1, 2^2, 2^3 (mod 5)
        cnt = [0] * 4
        res = []
        for i, v in enumerate(nums):
            if v:
                cnt[i % 4] = (cnt[i % 4] + 1) % 5
            r = 0
            for k in range(4):
                r += cnt[k] * POW2[(i - k) % 4]
            res.append(r % 5 == 0)
        return res
