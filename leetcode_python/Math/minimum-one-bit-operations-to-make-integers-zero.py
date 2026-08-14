"""

1611. Minimum One Bit Operations to Make Integers Zero
Hard

Given an integer n, you must transform it into 0 using the following operations any number of times:

- Change the rightmost (0th) bit in the binary representation of n.
- Change the ith bit in the binary representation of n if the (i-1)th bit is set to 1 and the (i-2)th through 0th bits are set to 0.

Return the minimum number of operations to transform n into 0.


Example 1:

Input: n = 3
Output: 2
Explanation: The binary representation of 3 is "11".
"11" -> "01" with the 2nd operation since the 0th bit is 1.
"01" -> "00" with the 1st operation.

Example 2:

Input: n = 6
Output: 4
Explanation: The binary representation of 6 is "110".
"110" -> "010" with the 2nd operation since the 1st bit is 1 and 0th through 0th bits are 0.
"010" -> "011" with the 1st operation.
"011" -> "001" with the 2nd operation since the 0th bit is 1.
"001" -> "000" with the 1st operation.


Constraints:

0 <= n <= 10^9

"""

# V0
# IDEA : GRAY CODE (n is a Gray code; the answer is its rank)
#
#   the two allowed moves are exactly the moves of the standard reflected
#   Gray code sequence, so the reachable states form a path
#     0 = g(0), g(1), g(2), ... where g(k) = k ^ (k >> 1)
#   and the minimum #ops to walk n down to 0 is the index k with g(k) = n.
#
#   inverting Gray code : k = n ^ (n>>1) ^ (n>>2) ^ ... ^ (n>>31)
#   which is what the loop below accumulates.
#
# time = O(log n), space = O(1)
class Solution(object):
    def minimumOneBitOperations(self, n):
        res = 0
        while n:
            res ^= n
            n >>= 1
        return res
