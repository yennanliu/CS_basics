"""

1545. Find Kth Bit in Nth Binary String
Medium

Given two positive integers n and k, the binary string S_n is formed as follows:

S_1 = "0"
S_i = S_(i-1) + "1" + reverse(invert(S_(i-1))) for i > 1

Where + denotes the concatenation operation, reverse(x) returns the reversed string x, and invert(x) inverts all the bits in x (0 changes to 1 and 1 changes to 0).

For example, the first four strings in the above sequence are:

S_1 = "0"
S_2 = "011"
S_3 = "0111001"
S_4 = "011100110110001"

Return the kth bit in S_n. It is guaranteed that k is valid for the given n.


Example 1:

Input: n = 3, k = 1
Output: "0"
Explanation: S_3 is "0111001".
The 1st bit is "0".

Example 2:

Input: n = 4, k = 11
Output: "1"
Explanation: S_4 is "0111001101 1 0001".
The 11th bit is "1".


Constraints:

1 <= n <= 20
1 <= k <= 2^n - 1

"""

# V0
# IDEA : FOLD k BACK INTO THE PREVIOUS STRING (no string is ever built)
#
#   |S_n| = 2^n - 1, and the middle position is mid = 2^(n-1) holding '1'.
#
#     k <  mid : the bit is at the same index of S_(n-1)      -> recurse
#     k == mid : it is the injected '1'
#     k >  mid : the tail is reverse(invert(S_(n-1))), so position k maps to
#                position (2^n - k) of S_(n-1), with the bit FLIPPED
#
#   carry the flips in a single boolean instead of nesting inversions.
#   NOTE : S_n itself is never materialised — it would be up to 2^20 chars.
#
# time = O(n), space = O(1)
class Solution(object):
    def findKthBit(self, n, k):
        flip = False
        while n > 1:
            mid = 1 << (n - 1)
            if k == mid:
                return '0' if flip else '1'
            if k > mid:
                k = (1 << n) - k
                flip = not flip
            n -= 1
        # n == 1 -> S_1 = "0"
        return '1' if flip else '0'
