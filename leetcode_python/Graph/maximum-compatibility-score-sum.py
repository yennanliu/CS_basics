"""

1947. Maximum Compatibility Score Sum
Medium

There is a survey that consists of n questions where each question's answer is either 0 (no) or 1 (yes).

The survey was given to m students numbered from 0 to m - 1 and m mentors numbered from 0 to m - 1. The answers of the students are represented by a 2D integer array students where students[i] is an integer array that contains the answers of the ith student (0-indexed). The answers of the mentors are represented by a 2D integer array mentors where mentors[j] is an integer array that contains the answers of the jth mentor (0-indexed).

Each student will be assigned to one mentor, and each mentor will have one student assigned to them. The compatibility score of a student-mentor pair is the number of answers that are the same for both the student and the mentor.

For example, if the student's answers were [1, 0, 1] and the mentor's answers were [0, 0, 1], then their compatibility score is 2 because only the second and the third answers are the same.

You are tasked with finding the optimal student-mentor pairings to maximize the sum of the compatibility scores.

Given students and mentors, return the maximum compatibility score sum that can be achieved.


Example 1:

Input: students = [[1,1,0],[1,0,1],[0,0,1]], mentors = [[1,0,0],[0,0,1],[1,1,0]]
Output: 8
Explanation: We assign students to mentors in the following way:
- student 0 to mentor 2 with a compatibility score of 3.
- student 1 to mentor 0 with a compatibility score of 2.
- student 2 to mentor 1 with a compatibility score of 3.
The compatibility score sum is 3 + 2 + 3 = 8.

Example 2:

Input: students = [[0,0],[0,0],[0,0]], mentors = [[1,1],[1,1],[1,1]]
Output: 0
Explanation: The compatibility score of any student-mentor pair is 0.


Constraints:

m == students.length == mentors.length
n == students[i].length == mentors[j].length
1 <= m, n <= 8
students[i][k] is either 0 or 1.
mentors[j][k] is either 0 or 1.

"""

# V0
# IDEA : BITMASK DP OVER THE SET OF USED MENTORS (assignment problem, m <= 8)
#
#   g[i][j] = number of matching answers between student i and mentor j.
#
#   f[mask] = best total score when the mentors in `mask` have been handed out.
#   the number of mentors used is also the number of students already served,
#   so the NEXT student index is exactly popcount(mask) - no second dimension.
#
#     f[mask | (1 << j)] = max(..., f[mask] + g[popcount(mask)][j])   for j not in mask
#
#   this is 2^m * m instead of the m! of plain backtracking.
#
# time = O(m^2 * n + 2^m * m), space = O(2^m)
class Solution(object):
    def maxCompatibilitySum(self, students, mentors):
        m = len(students)
        n = len(students[0])

        g = [[0] * m for _ in range(m)]
        for i in range(m):
            for j in range(m):
                g[i][j] = sum(1 for k in range(n) if students[i][k] == mentors[j][k])

        NEG = -1
        f = [NEG] * (1 << m)
        f[0] = 0
        for mask in range(1 << m):
            if f[mask] < 0:
                continue
            i = bin(mask).count('1')        # next student to place
            if i == m:
                continue
            for j in range(m):
                if mask & (1 << j):
                    continue
                nxt = mask | (1 << j)
                cand = f[mask] + g[i][j]
                if cand > f[nxt]:
                    f[nxt] = cand
        return f[(1 << m) - 1]
