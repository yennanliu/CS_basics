"""

2571. Minimum Operations to Reduce an Integer to 0
Medium

You are given a positive integer n, you can do the following operation any number of times:

Add or subtract a power of 2 from n.

Return the minimum number of operations to make n equal to 0.

A number x is power of 2 if x == 2^i where i >= 0.


Example 1:

Input: n = 39
Output: 3
Explanation: We can do the following operations:
- Add 2^0 = 1 to n, so now n = 40.
- Subtract 2^3 = 8 from n, so now n = 32.
- Subtract 2^5 = 32 from n, so now n = 0.
It can be shown that 3 is the minimum number of operations we need to make n equal to 0.

Example 2:

Input: n = 54
Output: 3
Explanation: We can do the following operations:
- Add 2^1 = 2 to n, so now n = 56.
- Add 2^3 = 8 to n, so now n = 64.
- Subtract 2^6 = 64 from n, so now n = 0.
So the minimum number of operations is 3.


Constraints:

1 <= n <= 10^5

"""

# V0
# IDEA : GREEDY ON THE LOWEST SET BIT (non-adjacent form / NAF)
#
#   The answer is the minimum number of signed powers of 2 summing to n. The
#   optimal representation is the "non-adjacent form", and it can be built
#   greedily by only ever looking at the two lowest bits of n:
#
#     n & 1 == 0  -> the lowest bit is already 0, shift it away for FREE.
#                    (no operation is spent; a trailing zero costs nothing)
#     n & 3 == 3  -> at least two consecutive 1s at the bottom. ADD 1: that
#                    carries the whole run of 1s away and leaves a single 1
#                    higher up. Cheaper than clearing each 1 separately.
#     n & 3 == 1  -> an isolated 1 at the bottom (bit1 is 0). SUBTRACT 1 to
#                    clear it; adding would just create a longer run.
#
#   Each add/subtract costs 1 op; the shift costs nothing. Loop until n == 0.
#
#   NOTE : the n & 3 == 3 branch does n += 1, so n temporarily GROWS. That is
#          correct and terminating - the carry always clears >= 2 low bits and
#          sets at most 1 higher bit, so the popcount strictly decreases.
#
#   NOTE : do not "optimise" the n & 3 == 3 case into subtracting - e.g. for
#          n = 7 (111) adding 1 costs 2 ops total (7 -> 8 -> 0) while clearing
#          bit by bit costs 3.
#
# time = O(log(n)), space = O(1)
class Solution(object):
    def minOperations(self, n):
        ops = 0
        while n:
            if n & 1 == 0:
                # trailing zero -> free shift
                n >>= 1
            elif n & 3 == 3:
                # run of >= 2 ones -> carry them out with a single +1
                n += 1
                ops += 1
            else:
                # isolated one -> clear it with a single -1
                n -= 1
                ops += 1
        return ops
