"""

3687. Library Late Fee Calculator
Easy

You are given an integer array daysLate where daysLate[i] indicates how
many days late the ith book was returned.

The penalty is calculated as follows:

If daysLate[i] == 1, penalty is 1.
If 2 <= daysLate[i] <= 5, penalty is 2 * daysLate[i].
If daysLate[i] > 5, penalty is 3 * daysLate[i].

Return the total penalty for all books.


Example 1:

Input: daysLate = [5,1,7]
Output: 32
Explanation:
daysLate[0] = 5: Penalty is 2 * daysLate[0] = 2 * 5 = 10.
daysLate[1] = 1: Penalty is 1.
daysLate[2] = 7: Penalty is 3 * daysLate[2] = 3 * 7 = 21.
Thus, the total penalty is 10 + 1 + 21 = 32.

Example 2:

Input: daysLate = [1,1]
Output: 2
Explanation:
daysLate[0] = 1: Penalty is 1.
daysLate[1] = 1: Penalty is 1.
Thus, the total penalty is 1 + 1 = 2.


Constraints:

1 <= daysLate.length <= 100
1 <= daysLate[i] <= 100

"""

# V0
# IDEA : PER-BOOK PIECEWISE FEE, SUMMED
#
#   the fee schedule is a piecewise function of a single book's lateness and
#   books do not interact, so the total is just the sum of f(x) over the
#   array. the only thing to get right is the tier boundaries: the tiers are
#   {1}, [2..5], [6..], and since the constraints guarantee x >= 1 there is
#   no zero/negative case to worry about -- testing x == 1 then x > 5 covers
#   the middle band by elimination.
#
# time = O(n), space = O(1)
class Solution(object):
    def lateFee(self, daysLate):
        total = 0
        for x in daysLate:
            if x == 1:
                total += 1
            elif x > 5:
                total += 3 * x
            else:
                total += 2 * x
        return total
