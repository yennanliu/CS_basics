"""

2147. Number of Ways to Divide a Long Corridor
Hard

Along a long library corridor, there is a line of seats and decorative plants. You are given a 0-indexed string corridor of length n consisting of letters 'S' and 'P' where each character describes a seat or plant in the ith position.

One room divider has already been installed to the left of index 0, and another to the right of index n - 1. Additional room dividers can be installed. For each position between indices i - 1 and i (1 <= i <= n - 1), at most one divider can be installed.

Divide the corridor into non-overlapping sections, where each section has exactly two seats with any number of plants. There may be multiple ways to perform the division. Two ways are different if there is a position with a room divider installed in the first way but not in the second way.

Return the number of ways to divide the corridor. Since the answer may be very large, return it modulo 10^9 + 7. If there is no way, return 0.


Example 1:

Input: corridor = "SSPPSPS"
Output: 3
Explanation: There are 3 different ways to divide the corridor.
The black bars in the above image indicate the two room dividers already installed.
Note that in each of the ways, each section has exactly two seats.

Example 2:

Input: corridor = "PPSPSP"
Output: 1
Explanation: There is only 1 way to divide the corridor, by not installing any additional dividers.
Installing any would create some section that does not have exactly two seats.

Example 3:

Input: corridor = "S"
Output: 0
Explanation: There is no way to divide the corridor because there will always be a section that does not have exactly two seats.


Constraints:

n == corridor.length
1 <= n <= 10^5
corridor[i] is either 'S' or 'P'.

"""

# V0
# IDEA : PAIR UP THE SEATS, MULTIPLY THE GAPS BETWEEN CONSECUTIVE PAIRS
#
#   the seats must split as (1st,2nd), (3rd,4th), (5th,6th), ... — no other
#   pairing keeps two seats per section. so an odd seat count, or none at
#   all, means 0 ways.
#
#   between the 2nd seat of one pair and the 1st seat of the next there is a
#   stretch of plants; the divider may go in ANY of the slots along that
#   stretch. if those two seats sit at indices a < b, there are (b - a)
#   choices, and the pairs are independent — so multiply.
#
#   NOTE : the plants before the first seat and after the last one never
#          create a choice; they belong to the outer sections.
#
# time = O(n), space = O(n)
class Solution(object):
    def numberOfWays(self, corridor):
        MOD = 10 ** 9 + 7
        seats = [i for i, ch in enumerate(corridor) if ch == 'S']
        if not seats or len(seats) % 2:
            return 0
        res = 1
        for i in range(2, len(seats), 2):
            res = res * (seats[i] - seats[i - 1]) % MOD
        return res
