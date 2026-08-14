"""

2381. Shifting Letters II
Medium

You are given a string s of lowercase English letters and a 2D integer array shifts where shifts[i] = [start_i, end_i, direction_i]. For every i, shift the characters in s from the index start_i to the index end_i (inclusive) forward if direction_i = 1, or shift the characters backward if direction_i = 0.

Shifting a character forward means replacing it with the next letter in the alphabet (wrapping around so that 'z' becomes 'a'). Similarly, shifting a character backward means replacing it with the previous letter in the alphabet (wrapping around so that 'a' becomes 'z').

Return the final string after all such shifts to s are applied.


Example 1:

Input: s = "abc", shifts = [[0,1,0],[1,2,1],[0,2,1]]
Output: "ace"
Explanation: Firstly, shift the characters from index 0 to index 1 backward. Now s = "zac".
Secondly, shift the characters from index 1 to index 2 forward. Now s = "zbd".
Finally, shift the characters from index 0 to index 2 forward. Now s = "ace".

Example 2:

Input: s = "dztz", shifts = [[0,0,0],[1,1,1]]
Output: "catz"
Explanation: Firstly, shift the characters from index 0 to index 0 backward. Now s = "cztz".
Finally, shift the characters from index 1 to index 1 forward. Now s = "catz".


Constraints:

1 <= s.length, shifts.length <= 5 * 10^4
shifts[i].length == 3
0 <= start_i <= end_i < s.length
0 <= direction_i <= 1
s consists of lowercase English letters.

"""

# V0
# IDEA : DIFFERENCE ARRAY — EACH SHIFT IS A RANGE +1 OR -1
#
#   applying every shift literally would be O(len(s)) per query. instead
#   record +1 / -1 at the interval start and the inverse just past its end,
#   then one prefix sum yields the NET shift of each position.
#
#   the shifts commute (they are just additions on the letter index), so the
#   order of the queries does not matter — only the totals do.
#
#   finally map each letter with  (index + net) % 26, which python's % keeps
#   non-negative even for large negative nets.
#
# time = O(n + len(shifts)), space = O(n)
class Solution(object):
    def shiftingLetters(self, s, shifts):
        n = len(s)
        diff = [0] * (n + 1)
        for start, end, direction in shifts:
            step = 1 if direction == 1 else -1
            diff[start] += step
            diff[end + 1] -= step

        res = []
        net = 0
        for i, c in enumerate(s):
            net += diff[i]
            res.append(chr((ord(c) - ord('a') + net) % 26 + ord('a')))
        return ''.join(res)
