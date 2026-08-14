"""

1758. Minimum Changes To Make Alternating Binary String
Easy

You are given a string s consisting only of the characters '0' and '1'. In one operation, you can change any '0' to '1' or vice versa.

The string is called alternating if no two adjacent characters are equal. For example, the string "010" is alternating, while the string "0100" is not.

Return the minimum number of operations needed to make s alternating.

Example 1:

Input: s = "0100"
Output: 1
Explanation: If you change the last character to '1', s will be "0101", which is alternating.

Example 2:

Input: s = "10"
Output: 0
Explanation: s is already alternating.

Example 3:

Input: s = "1111"
Output: 2
Explanation: You need two operations to reach "0101" or "1010".

Constraints:

1 <= s.length <= 10^4
s[i] is either '0' or '1'.

"""

# V0
# IDEA : COUNTING (there are only two possible alternating targets)
#
#   an alternating binary string of length n is either "0101..." or "1010...".
#   count how many positions of s differ from "0101..." -> cnt. the positions
#   that differ from "1010..." are exactly the remaining n - cnt ones, because
#   the two targets disagree everywhere.
#   NOTE : so no second pass is needed, the answer is min(cnt, n - cnt).
#
# time = O(n), space = O(1)
class Solution(object):
    def minOperations(self, s):
        cnt = 0
        for i in range(len(s)):
            if s[i] != "01"[i & 1]:
                cnt += 1
        return min(cnt, len(s) - cnt)
