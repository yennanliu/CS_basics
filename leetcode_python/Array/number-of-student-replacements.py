"""

3616. Number of Student Replacements
Medium

You are given an integer array ranks where ranks[i] represents the rank of the
ith student arriving in order. A lower number indicates a better rank.

Initially, the first student is selected by default.

A replacement occurs when a student with a strictly better rank arrives and
replaces the current selection.

Return the total number of replacements made.


Example 1:

Input: ranks = [4,1,2]
Output: 1
Explanation:
The first student with ranks[0] = 4 is initially selected.
The second student with ranks[1] = 1 is better than the current selection, so a
replacement occurs.
The third student has a worse rank, so no replacement occurs.
Thus, the number of replacements is 1.

Example 2:

Input: ranks = [2,2,3]
Output: 0
Explanation:
The first student with ranks[0] = 2 is initially selected.
Neither of ranks[1] = 2 or ranks[2] = 3 is better than the current selection.
Thus, the number of replacements is 0.


Constraints:

1 <= ranks.length <= 10^5
1 <= ranks[i] <= 10^5

"""

# V0
# IDEA : COUNT STRICT PREFIX MINIMA
#
#   the selected student is always the best rank seen so far, so the running
#   state is nothing more than a prefix minimum. a replacement happens exactly
#   when the arriving rank strictly beats that prefix minimum — i.e. when it
#   is a new strict record low.
#
#   so the answer is simply the number of strict prefix minima after the
#   first element. no need to model the "selection" at all; ties change
#   nothing because a replacement requires a strictly better rank.
#
# time = O(n), space = O(1)
class Solution(object):
    def totalReplacements(self, ranks):
        best = ranks[0]
        res = 0
        for r in ranks:
            if r < best:
                best = r
                res += 1
        return res
