"""

1349. Maximum Students Taking Exam
Hard

Given a m * n matrix seats that represent seats distributions in a classroom.
If a seat is broken, it is denoted by '#' character otherwise it is denoted by a '.' character.

Students can see the answers of those sitting next to the left, right, upper left and upper right,
but he cannot see the answers of the student sitting directly in front or behind him.
Return the maximum number of students that can take the exam together without any cheating
being possible.

Students must be placed in seats in good condition.


Example 1:

Input: seats = [["#",".","#","#",".","#"],
                [".","#","#","#","#","."],
                ["#",".","#","#",".","#"]]
Output: 4
Explanation: Teacher can place 4 students in available seats so they don't cheat on the exam.

Example 2:

Input: seats = [[".","#"],
                ["#","#"],
                ["#","."],
                ["#","#"],
                [".","#"]]
Output: 3
Explanation: Place all students in available seats.

Example 3:

Input: seats = [["#",".",".",".","#"],
                [".","#",".","#","."],
                [".",".","#",".","."],
                [".","#",".","#","."],
                ["#",".",".",".","#"]]
Output: 10
Explanation: Place students in available seats in column 1, 3 and 5.


Constraints:

seats contains only characters '.' and '#'.
m == seats.length
n == seats[i].length
1 <= m <= 8
1 <= n <= 8

"""

# V0
# IDEA : BITMASK DP row by row (n <= 8 -> a row is one byte)
#
#   encode a row's seating as an n-bit mask (bit j set = a student sits at
#   column j). Two validity rules :
#     - within a row : no two adjacent students   -> mask & (mask << 1) == 0
#     - vs the seats  : only usable seats         -> mask & ~avail[i] == 0
#     - vs the row above (diagonal peeking)       -> mask & (prev << 1) == 0
#                                                 and mask & (prev >> 1) == 0
#   (directly in front / behind is allowed, so a plain vertical overlap is fine)
#
#   dp[prev] = best total for the rows processed so far, where `prev` is the
#   seating of the LAST processed row. Sweep rows, trying every (prev, mask).
#
#   NOTE : the initial state dp[0] = 0 stands for "an empty row before row 0",
#          which imposes no diagonal constraint -- exactly what we want.
#
# time = O(m * 4^n), space = O(2^n)
class Solution(object):
    def maxStudents(self, seats):
        m, n = len(seats), len(seats[0])
        full = 1 << n

        avail = []
        for row in seats:
            mask = 0
            for j, ch in enumerate(row):
                if ch == '.':
                    mask |= 1 << j
            avail.append(mask)

        NEG = -1
        dp = [NEG] * full
        dp[0] = 0

        for i in range(m):
            ndp = [NEG] * full
            for mask in range(full):
                if mask & ~avail[i]:
                    continue
                if mask & (mask << 1):
                    continue
                cnt = bin(mask).count('1')
                for prev in range(full):
                    if dp[prev] == NEG:
                        continue
                    if mask & (prev << 1) or mask & (prev >> 1):
                        continue
                    if dp[prev] + cnt > ndp[mask]:
                        ndp[mask] = dp[prev] + cnt
            dp = ndp

        return max(dp)
