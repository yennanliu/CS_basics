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


# V0-1
# IDEA : GREEDY SIMULATION WITH AN EXPLICIT BANNED SET
#
#   walk the candidates 1, 2, 3, ... in increasing order and take each one
#   unless it has already been banned by a partner we picked ; picking x bans
#   target - x. stop once n values are collected.
#
#   this is the version to reason with (and the oracle to test the closed
#   forms against) : it makes no claim about WHERE the boundary is, it just
#   applies the rule. it is O(n) time and memory though, so at n = 10^9 it
#   only serves as a reference implementation for small inputs.
#
# time = O(n), space = O(n)
class Solution(object):
    def minimumPossibleSum(self, n, target):
        MOD = 10 ** 9 + 7
        banned = set()
        total = 0
        taken = 0
        x = 1
        while taken < n:
            if x not in banned:
                total += x
                taken += 1
                if target - x != x:
                    banned.add(target - x)
            x += 1
        return total % MOD


# V0-2
# IDEA : COMPLEMENT COUNTING — SUM ONE CONTIGUOUS RUN, SUBTRACT THE BANNED BLOCK
#
#   the chosen set is  {1..m}  U  {target..hi}  with m = target // 2 and
#   hi = target + (n - m) - 1. instead of adding the two pieces (V0), note
#   that together they are the single run 1..hi with the middle block
#   [m + 1, target - 1] punched out :
#
#       sum = tri(hi) - ( tri(target - 1) - tri(m) )
#
#   so the whole answer is three triangular numbers, and the only formula
#   needed is tri(k) = k(k+1)/2.
#
#   every step is taken modulo 10^9 + 7 using the modular inverse of 2
#   (Fermat : 2^(MOD-2)), so nothing ever exceeds the modulus — the shape a
#   fixed-width language (Java/C++ long) has to use to stay overflow-free.
#
# time = O(log MOD) for the one modular inverse, space = O(1)
class Solution(object):
    def minimumPossibleSum(self, n, target):
        MOD = 10 ** 9 + 7
        inv2 = pow(2, MOD - 2, MOD)

        def tri(k):
            if k <= 0:
                return 0
            return (k % MOD) * ((k + 1) % MOD) % MOD * inv2 % MOD

        m = target // 2
        if n <= m:
            return tri(n)
        hi = target + (n - m) - 1
        return (tri(hi) - tri(target - 1) + tri(m)) % MOD
