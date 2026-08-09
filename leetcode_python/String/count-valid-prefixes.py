# https://leetcode.com/problems/count-valid-prefixes/description/

"""

4006. Count Valid Prefixes
Solved
Easy
premium lock icon
Companies
Hint
You are given a binary string s.

A prefix of s is considered valid if its characters can be rearranged to form an alternating string.

Return the number of valid prefixes of s.

A string is considered alternating if no two adjacent characters are equal.

 

Example 1:

Input: s = "00101"

Output: 3

Explanation:

The valid prefixes are:

"0": It is already an alternating string.
"001": It can be rearranged into "010", which is an alternating string.
"00101": It can be rearranged into "01010", which is an alternating string.
Thus, the answer is 3.

Example 2:

Input: s = "101"

Output: 3

Explanation:

All prefixes of s = "101" are already alternating strings. Thus, the answer is 3.

 

Constraints:

1 <= s.length <= 100
s consists only of '0' and '1'.
 
"""


# V0
class Solution(object):
    def countValidPrefixes(self, s):
        """
        :type s: str
        :rtype: int
        """
        pass


# V0-1
# IDEA: PREFIX (gemini)
class Solution(object):
    def countValidPrefixes(self, s):
        """
        :type s: str
        :rtype: int
        """
        cnt = 0
        z_cnt = 0
        o_cnt = 0

        # We can just iterate directly through the characters in the string
        for val in s:
            
            # 1. FIX: Compare against the string character '0'
            if val == '0':
                z_cnt += 1
            else:
                o_cnt += 1

            # 2. FIX: Use Python's built-in abs() function
            # This perfectly handles i == 0 and i == 1 automatically, 
            # so we don't need manual checks!
            if abs(o_cnt - z_cnt) <= 1:
                cnt += 1

        return cnt


# V0-2
# IDEA: PREFIX (gpt)
class Solution(object):
    def countValidPrefixes(self, s):
        """
        :type s: str
        :rtype: int
        """
        cnt = 0
        z_cnt = 0
        o_cnt = 0

        for ch in s:
            if ch == '0':
                z_cnt += 1
            else:
                o_cnt += 1

            if abs(o_cnt - z_cnt) <= 1:
                cnt += 1

        return cnt


# V1

# V2

