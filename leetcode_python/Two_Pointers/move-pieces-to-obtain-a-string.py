"""

2337. Move Pieces to Obtain a String
Medium

You are given two strings start and target, both of length n. Each string consists only of the characters 'L', 'R', and '_' where:

The characters 'L' and 'R' represent pieces, where a piece 'L' can move to the left only if there is a blank space directly to its left, and a piece 'R' can move to the right only if there is a blank space directly to its right.
The character '_' represents a blank space that can be occupied by any of the 'L' or 'R' pieces.

Return true if it is possible to obtain the string target by moving the pieces of the string start any number of times. Otherwise, return false.


Example 1:

Input: start = "_L__R__R_", target = "L______RR"
Output: true
Explanation: We can obtain the string target from start by doing the following moves:
- Move the first piece one step to the left, start becomes equal to "L___R__R_".
- Move the last piece one step to the right, start becomes equal to "L___R___R".
- Move the second piece three steps to the right, start becomes equal to "L______RR".
Since it is possible to get the string target from start, we return true.

Example 2:

Input: start = "R_L_", target = "__LR"
Output: false
Explanation: The 'R' piece in the string start can move one step to the right to obtain "_RL_".
After that, no pieces can move anymore, so it is impossible to obtain the string target from start.

Example 3:

Input: start = "_R", target = "R_"
Output: false
Explanation: The piece in the string start can move only to the right, so it is impossible to obtain the string target from start.


Constraints:

n == start.length == target.length
1 <= n <= 10^5
start and target consist of the characters 'L', 'R', and '_'.

"""

# V0
# IDEA : PIECES CANNOT PASS EACH OTHER — COMPARE THEM IN ORDER, POSITION BY POSITION
#
#   two invariants fully characterise reachability :
#     1. deleting the blanks from both strings must give the SAME sequence of
#        'L'/'R' — pieces never change type and never cross one another
#     2. an 'L' only ever moves LEFT, so its start index must be >= its target
#        index; an 'R' only moves RIGHT, so start index <= target index
#
#   walking two pointers over the non-blank positions of each string checks
#   both at once, and bails the moment either is violated.
#
# time = O(n), space = O(n)
class Solution(object):
    def canChange(self, start, target):
        a = [(c, i) for i, c in enumerate(start) if c != '_']
        b = [(c, i) for i, c in enumerate(target) if c != '_']
        if len(a) != len(b):
            return False

        for (ca, ia), (cb, ib) in zip(a, b):
            if ca != cb:
                return False
            if ca == 'L' and ia < ib:
                return False       # 'L' cannot move right
            if ca == 'R' and ia > ib:
                return False       # 'R' cannot move left
        return True
