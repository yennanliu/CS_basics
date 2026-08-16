"""

3630. Partition Array for Maximum XOR and AND
Hard

You are given an integer array nums.

Partition the array into three (possibly empty) subsequences A, B, and C
such that every element of nums belongs to exactly one subsequence.

Your goal is to maximize the value of: XOR(A) + AND(B) + XOR(C)

where:

XOR(arr) denotes the bitwise XOR of all elements in arr. If arr is empty,
its value is defined as 0.
AND(arr) denotes the bitwise AND of all elements in arr. If arr is empty,
its value is defined as 0.

Return the maximum value achievable.

Note: If multiple partitions result in the same maximum sum, you can
consider any one of them.


Example 1:

Input: nums = [2,3]
Output: 5
Explanation:
One optimal partition is:
A = [3], XOR(A) = 3
B = [2], AND(B) = 2
C = [], XOR(C) = 0
The maximum value of: XOR(A) + AND(B) + XOR(C) = 3 + 2 + 0 = 5. Thus, the
answer is 5.

Example 2:

Input: nums = [1,3,2]
Output: 6
Explanation:
One optimal partition is:
A = [1], XOR(A) = 1
B = [2], AND(B) = 2
C = [3], XOR(C) = 3
The maximum value of: XOR(A) + AND(B) + XOR(C) = 1 + 2 + 3 = 6. Thus, the
answer is 6.

Example 3:

Input: nums = [2,3,6,7]
Output: 15
Explanation:
One optimal partition is:
A = [7], XOR(A) = 7
B = [2,3], AND(B) = 2
C = [6], XOR(C) = 6
The maximum value of: XOR(A) + AND(B) + XOR(C) = 7 + 2 + 6 = 15. Thus, the
answer is 15.


Constraints:

1 <= nums.length <= 19
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : ENUMERATE B, THEN SOLVE THE A/C SPLIT IN CLOSED FORM WITH A XOR BASIS
#
#   n <= 19 screams "enumerate one of the three parts", and B is the right
#   one to enumerate: AND is not linear, while the A/C split turns out to
#   have an exact formula.
#
#   fix B; let S be the rest and T = XOR(S). whatever we do, XOR(A) and
#   XOR(C) xor to T, and for any two numbers u + w = (u ^ w) + 2*(u & w), so
#     XOR(A) + XOR(C) = T + 2 * (XOR(A) & XOR(C)) = T + 2 * (x & ~T)
#   writing x = XOR(A) -- the second equality because XOR(C) = x ^ T, so the
#   bits shared by the two are exactly x's bits OUTSIDE T. so the whole A/C
#   decision reduces to one number: make x & ~T as large as possible.
#
#   which x are reachable? x = XOR(A) for A ranging over subsets of S, and
#   that set is precisely the GF(2) linear span of S. masking is linear, so
#   {x & ~T : x in span(S)} = span({v & ~T : v in S}) -- meaning we can mask
#   the ELEMENTS first and then run the ordinary "maximum xor of a subset"
#   routine: gaussian elimination to a basis, then a greedy that xors in a
#   basis vector whenever it raises the running value.
#
#   so each of the 2^n choices of B costs one basis build over |S| numbers,
#   and the answer is max over B of AND(B) + T + 2 * (that maximum).
#
#   (B genuinely needs full enumeration -- capping |B| at one or two is
#   wrong; instances exist whose only optimal B has four elements.)
#
# time = O(2^n * n^2), space = O(2^n)
class Solution(object):
    def maximizeXorAndXor(self, nums):
        n = len(nums)
        full = (1 << n) - 1

        # AND / XOR of every subset, built by peeling off the lowest set bit
        and_of = [0] * (1 << n)
        xor_of = [0] * (1 << n)
        for mask in range(1, 1 << n):
            low = mask & -mask
            i = low.bit_length() - 1
            rest = mask ^ low
            and_of[mask] = (and_of[rest] & nums[i]) if rest else nums[i]
            xor_of[mask] = xor_of[rest] ^ nums[i]

        best = 0
        idxs = range(n)
        for mask in range(1 << n):          # mask = the subsequence B
            t = xor_of[full ^ mask]
            keep = ~t
            basis = []
            for i in idxs:
                if mask >> i & 1:
                    continue
                x = nums[i] & keep
                for b in basis:             # reduce against the current basis
                    if x ^ b < x:
                        x ^= b
                if x:
                    basis.append(x)
            top = 0
            for b in basis:                 # greedy maximum over the span
                if top ^ b > top:
                    top ^= b
            val = and_of[mask] + t + 2 * top
            if val > best:
                best = val
        return best
