"""

2834. Find the Minimum Possible Sum of a Beautiful Array
Medium

You are given positive integers n and target.

An array nums is beautiful if it meets the following conditions:

nums.length == n.
nums consists of pairwise distinct positive integers.
There doesn't exist two distinct indices, i and j, in the range [0, n - 1], such that nums[i] + nums[j] == target.

Return the minimum possible sum that a beautiful array could have modulo 10^9 + 7.


Example 1:

Input: n = 2, target = 3
Output: 4
Explanation: We can see that nums = [1,3] is beautiful.
- The array nums has length n = 2.
- The array nums consists of pairwise distinct positive integers.
- There doesn't exist two distinct indices, i and j, with nums[i] + nums[j] == 3.
It can be proven that 4 is the minimum possible sum that a beautiful array could have.

Example 2:

Input: n = 3, target = 3
Output: 8
Explanation: We can see that nums = [1,3,4] is beautiful.
- The array nums has length n = 3.
- The array nums consists of pairwise distinct positive integers.
- There doesn't exist two distinct indices, i and j, with nums[i] + nums[j] == 3.
It can be proven that 8 is the minimum possible sum that a beautiful array could have.

Example 3:

Input: n = 1, target = 1
Output: 1
Explanation: We can see, that nums = [1] is beautiful.


Constraints:

1 <= n <= 10^9
1 <= target <= 10^9

"""

# V0
# IDEA : GREEDY + ARITHMETIC SERIES (closed form, no loop)
#
#   Same pairing idea as LC 2829, but n reaches 10^9 so the greedy has to be
#   summed in closed form rather than simulated.
#
#   Take the smallest integers 1, 2, ..., m where m = target // 2. Picking x
#   in that range bans target - x, which is > m, i.e. strictly larger than
#   anything else in the cheap block — so the whole block 1..m is safe and
#   obviously optimal to take first.
#
#   NOTE : m = target // 2 (floor), not (target - 1) // 2. When target is
#          even, target/2 pairs only with ITSELF, and the two indices must be
#          distinct while the values are distinct — so target/2 can never
#          form the forbidden sum and is legal to include.
#
#   Everything in (m, target) is banned by a partner we already took, so the
#   remaining n - m elements are target, target + 1, ..., target + n - m - 1.
#
#       sum = m(m + 1) / 2  +  (n - m) * target + (n - m)(n - m - 1) / 2
#
#   NOTE : if n <= m we only need 1..n and stop there.
#
#   NOTE : take the modulo only at the very end — Python ints are arbitrary
#          precision, so the intermediate products (up to ~10^18) are exact.
#          A fixed-width language must reduce mid-way or use 64-bit + care.
#
# time = O(1), space = O(1)
class Solution(object):
    def minimumPossibleSum(self, n, target):
        MOD = 10 ** 9 + 7
        m = target // 2
        if n <= m:
            return (n * (n + 1) // 2) % MOD
        rest = n - m
        total = m * (m + 1) // 2 + rest * target + rest * (rest - 1) // 2
        return total % MOD
