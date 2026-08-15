"""

3228. Maximum Number of Operations to Move Ones to the End
Medium

You are given a binary string s.

You can perform the following operation on the string any number of times:

Choose any index i from the string where i + 1 < s.length such that s[i] == '1' and s[i + 1] == '0'.
Move the character s[i] to the right until it reaches the end of the string or another '1'. For example, for s = "010010", if we choose i = 1, the resulting string will be s = "000110".

Return the maximum number of operations that you can perform.


Example 1:

Input: s = "1001101"
Output: 4
Explanation:
We can perform the following operations:
Choose index i = 0. The resulting string is s = "0011101".
Choose index i = 4. The resulting string is s = "0011011".
Choose index i = 3. The resulting string is s = "0010111".
Choose index i = 2. The resulting string is s = "0001111".

Example 2:

Input: s = "00111"
Output: 0


Constraints:

1 <= s.length <= 10^5
s[i] is either '0' or '1'.

"""

# V0
# IDEA : EVERY 1 CAN CROSS EACH ZERO-BLOCK TO ITS RIGHT EXACTLY ONCE
#
#   one operation slides a single '1' across the run of zeros in front of it
#   until it hits another '1' or the end. so a given '1' can be moved once
#   per ZERO-BLOCK lying to its right, and no more — after crossing, that
#   block is behind it forever.
#
#   summing over the ones is the same as summing over the blocks : each
#   zero-block will eventually be crossed by every '1' that starts to its
#   left. so scan once, and when a new zero-block begins, add the number of
#   ones seen so far.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxOperations(self, s):
        res = 0
        ones = 0
        n = len(s)
        for i, ch in enumerate(s):
            if ch == '1':
                ones += 1
            elif i == 0 or s[i - 1] == '1':      # this zero starts a new block
                res += ones
        return res
