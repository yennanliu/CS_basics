"""

2315. Count Asterisks
Easy

You are given a string s, where every two consecutive vertical bars '|' are grouped into a pair. In other words, the 1st and 2nd '|' make a pair, the 3rd and 4th '|' make a pair, and so forth.

Return the number of '*' in s, excluding the '*' between each pair of '|'.

Note that each '|' will belong to exactly one pair.


Example 1:

Input: s = "l|*e*et|co|*de|"
Output: 2
Explanation: The considered characters are underlined: "l|*e*et|co|*de|".
The characters between the first and second '|' are excluded from the answer.
Also, the characters between the third and fourth '|' are excluded from the answer.
There are 2 asterisks considered. Therefore, we return 2.

Example 2:

Input: s = "iamprogrammer"
Output: 0
Explanation: In this example, there are no asterisks in s. Therefore, we return 0.

Example 3:

Input: s = "yo|uar|e|b|e*au|tifu|l"
Output: 5
Explanation: The considered characters are underlined: "yo|uar|e|b|e*au|tifu|l". There are 5 asterisks considered. Therefore, we return 5.


Constraints:

1 <= s.length <= 1000
s consists of lowercase English letters, vertical bars '|', and asterisks '*'.
s contains an even number of vertical bars '|'.

"""

# V0
# IDEA : PARITY TOGGLE (one scan, a single "am I inside a pair?" flag)
#
#   walk the string keeping a boolean `outside`, initially True.
#   every '|' toggles it: after an odd number of bars we are between a
#   pair, after an even number we are outside again.
#   count a '*' only while `outside` is True.
#
#   NOTE : the bars are guaranteed to come in an even count, so the flag
#          always ends back at True - no post-fix needed.
#
# time = O(n), space = O(1)
class Solution(object):
    def countAsterisks(self, s):
        res = 0
        outside = True
        for c in s:
            if c == '|':
                outside = not outside
            elif c == '*' and outside:
                res += 1
        return res
