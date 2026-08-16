"""

3443. Maximum Manhattan Distance After K Changes
Medium

You are given a string s consisting of the characters 'N', 'S', 'E', and 'W',
where s[i] indicates movements in an infinite grid:

'N' : Move north by 1 unit.
'S' : Move south by 1 unit.
'E' : Move east by 1 unit.
'W' : Move west by 1 unit.

Initially, you are at the origin (0, 0). You can change at most k characters to
any of the four directions.

Find the maximum Manhattan distance from the origin that can be achieved at any
time while performing the movements in order.
The Manhattan Distance between two cells (x_i, y_i) and (x_j, y_j) is |x_i -
x_j| + |y_i - y_j|.

Example 1:

Input: s = "NWSE", k = 1

Output: 3

Explanation:

Change s[2] from 'S' to 'N'. The string s becomes "NWNE".

Movement
Position (x, y)
Manhattan Distance
Maximum

s[0] == 'N'
(0, 1)
0 + 1 = 1
1

s[1] == 'W'
(-1, 1)
1 + 1 = 2
2

s[2] == 'N'
(-1, 2)
1 + 2 = 3
3

s[3] == 'E'
(0, 2)
0 + 2 = 2
3

The maximum Manhattan distance from the origin that can be achieved is 3. Hence,
3 is the output.

Example 2:

Input: s = "NSWWEW", k = 3

Output: 6

Explanation:

Change s[1] from 'S' to 'N', and s[4] from 'E' to 'W'. The string s becomes
"NNWWWW".

The maximum Manhattan distance from the origin that can be achieved is 6. Hence,
6 is the output.

Constraints:

1 <= s.length <= 10^5
0 <= k <= s.length
s consists of only 'N', 'S', 'E', and 'W'.

"""

# V0
# IDEA : AT EVERY PREFIX, EACH EDIT IS WORTH EXACTLY +2 — UNTIL THE PREFIX RUNS OUT
#
#   after i moves the honest position is (x, y) and the honest distance is
#   d = |x| + |y|.  flipping one wasted step — an 'S' when the walk is heading
#   north, say — swings that axis by 2, so each edit inside the prefix is worth
#   +2.  the number of wasted steps is exactly (i - d)/2, since the i steps
#   split into d that push outward and the rest into cancelling pairs.
#
#   so the best distance reachable at prefix i is
#     d + 2*min(k, (i - d)/2)  =  min(i, d + 2k),
#   the second form being the tidier one: either every step ends up pulling in
#   the same direction, or we are edit-limited.
#
#   the question asks for the maximum over *all* moments, so we evaluate this at
#   each prefix and keep the best.  the same k budget is reused for each prefix,
#   which is legitimate because we only need one witness moment.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxDistance(self, s, k):
        x = y = 0
        ans = 0
        for i, ch in enumerate(s, 1):
            if ch == 'N':
                y += 1
            elif ch == 'S':
                y -= 1
            elif ch == 'E':
                x += 1
            else:
                x -= 1
            d = abs(x) + abs(y) + 2 * k
            if i < d:
                d = i
            if d > ans:
                ans = d
        return ans
