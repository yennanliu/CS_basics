"""

1717. Maximum Score From Removing Substrings
Medium

You are given a string s and two integers x and y. You can perform two types of operations any number of times.

Remove substring "ab" and gain x points.
  For example, when removing "ab" from "cabxbae" it becomes "cxbae".
Remove substring "ba" and gain y points.
  For example, when removing "ba" from "cabxbae" it becomes "cabxe".

Return the maximum points you can gain after applying the above operations on s.


Example 1:

Input: s = "cdbcbbaaabab", x = 4, y = 5
Output: 19
Explanation:
- Remove the "ba" underlined in "cdbcbbaaabab". Now, s = "cdbcbbaaab" and 5 points are added to the score.
- Remove the "ab" underlined in "cdbcbbaaab". Now, s = "cdbcbbaa" and 4 points are added to the score.
- Remove the "ba" underlined in "cdbcbbaa". Now, s = "cdbcba" and 5 points are added to the score.
- Remove the "ba" underlined in "cdbcba". Now, s = "cdbc" and 5 points are added to the score.
Total score = 5 + 4 + 5 + 5 = 19.

Example 2:

Input: s = "aabbaaxybbaabb", x = 5, y = 4
Output: 20


Constraints:

1 <= s.length <= 10^5
1 <= x, y <= 10^4
s consists of lowercase English letters.

"""

# V0
# IDEA : GREEDY + STACK COUNTERS (strip the higher-paying pair first)
#
#   key fact : both operations delete exactly one 'a' and one 'b', so on a
#   maximal a/b block the TOTAL number of removals is fixed - only the mix
#   between the x-pair and the y-pair is ours to choose. hence take as many
#   of the better-paying pair as possible, then mop up with the other one.
#
#   WLOG make the pair worth x be (a, b) with x >= y : if x < y, swap both
#   the prices and the letter roles, so the code below always removes the
#   expensive pair first.
#
#   one pass, two counters (a stack of only a's and b's degenerates to counts):
#     'a'   -> cnt_a += 1                     (hold it, it may start a pair)
#     'b'   -> if cnt_a: pay x, cnt_a -= 1    (greedily close the good pair)
#              else    : cnt_b += 1
#     other -> block ends. what is left is cnt_b b's followed by cnt_a a's,
#              i.e. min(cnt_a, cnt_b) of the cheap pair -> pay y each. reset.
#
#   NOTE : do the same flush once more after the loop for the trailing block.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumGain(self, s, x, y):
        a, b = 'a', 'b'
        if x < y:
            x, y = y, x
            a, b = b, a

        res = 0
        cnt_a = cnt_b = 0
        for c in s:
            if c == a:
                cnt_a += 1
            elif c == b:
                if cnt_a > 0:
                    cnt_a -= 1
                    res += x
                else:
                    cnt_b += 1
            else:
                res += min(cnt_a, cnt_b) * y
                cnt_a = cnt_b = 0

        res += min(cnt_a, cnt_b) * y
        return res
