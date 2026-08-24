"""

600. Non-negative Integers without Consecutive Ones
Hard

Given a positive integer n, return the number of the integers in the range [0, n]
whose binary representations do NOT contain consecutive ones.

Example 1:

Input: n = 5
Output: 5
Explanation:
Here are the non-negative integers <= 5 with their corresponding binary representations:
0 : 0
1 : 1
2 : 10
3 : 11
4 : 100
5 : 101
Among them, only integer 3 disobeys the rule (two consecutive ones) and the other 5 satisfy the rule.

Example 2:

Input: n = 1
Output: 2

Example 3:

Input: n = 2
Output: 3

Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA : FIBONACCI + BIT SCAN (greedy digit counting)
#
#   f[i] = # of binary strings of length i with no two adjacent 1s
#        -> f[0] = 1 (empty), f[1] = 2 ("0", "1"), f[i] = f[i-1] + f[i-2]
#
#   Scan the bits of n from high to low.
#   Whenever bit i of n is 1, every number that puts a 0 at bit i (and keeps
#   the higher bits identical to n) is strictly smaller than n, and the lower
#   i bits are then completely free -> that adds f[i] valid numbers.
#   Then we "commit" to putting a 1 at bit i and move on. If the previous
#   committed bit was also 1, the prefix itself is already invalid, so no
#   number >= that prefix can be counted -> stop early.
#   If we never break, n itself is valid, so add 1 at the end.
#
"""

DP def
    f[i]: number of binary strings of length i with NO two adjacent 1s

          -> f[0] = 1 (empty), f[1] = 2 ("0", "1")

DP eq

     f[i] = f[i-1] + f[i-2]          # Fibonacci

            (a leading 0 -> f[i-1]; a leading "10" -> f[i-2])


    -> e.g. then SCAN the bits of n from high to low:

         whenever bit i of n is 1, every number that puts a 0 there (higher
         bits identical to n) is strictly smaller, and the lower i bits are
         FREE -> res += f[i]

         then COMMIT to a 1 at bit i; if the previously committed bit was
         also 1 the prefix is already invalid, so STOP EARLY

     if the scan never breaks, n itself is valid -> res += 1

     ans = res

"""
# time = O(32) = O(1)
# space = O(32) = O(1)
class Solution(object):
    def findIntegers(self, n):
        # f[i] = count of valid binary strings of length i
        f = [0] * 32
        f[0] = 1
        f[1] = 2
        for i in range(2, 32):
            f[i] = f[i - 1] + f[i - 2]

        res = 0
        prev_bit = 0
        for i in range(30, -1, -1):
            if (n >> i) & 1:
                # put 0 here -> lower i bits are free
                res += f[i]
                if prev_bit == 1:
                    # prefix "11..." already invalid -> n itself not counted
                    return res
                prev_bit = 1
            else:
                prev_bit = 0

        # n itself is valid
        return res + 1


# V1
# IDEA : DIGIT DP (memoized) -- more mechanical, easier to adapt
"""

DP def
    f[i]: number of binary strings of length i with NO two adjacent 1s

          -> f[0] = 1 (empty), f[1] = 2 ("0", "1")

DP eq

     f[i] = f[i-1] + f[i-2]          # Fibonacci

            (a leading 0 -> f[i-1]; a leading "10" -> f[i-2])


    -> e.g. then SCAN the bits of n from high to low:

         whenever bit i of n is 1, every number that puts a 0 there (higher
         bits identical to n) is strictly smaller, and the lower i bits are
         FREE -> res += f[i]

         then COMMIT to a 1 at bit i; if the previously committed bit was
         also 1 the prefix is already invalid, so STOP EARLY

     if the scan never breaks, n itself is valid -> res += 1

     ans = res

"""
# time = O(L * 2 * 2 * 2) = O(1), L = 31 bits
# space = O(L)
class Solution(object):
    def findIntegers(self, n):
        bits = bin(n)[2:]
        size = len(bits)
        memo = {}

        def dp(i, prev, tight):
            """count valid completions from bit i, given prev bit and tight flag"""
            if i == size:
                return 1
            key = (i, prev, tight)
            if key in memo:
                return memo[key]
            top = int(bits[i]) if tight else 1
            total = 0
            for d in range(top + 1):
                if d == 1 and prev == 1:
                    continue
                total += dp(i + 1, d, tight and d == top)
            memo[key] = total
            return total

        return dp(0, 0, True)
