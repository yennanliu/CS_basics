"""

2483. Minimum Penalty for a Shop
Medium

You are given the customer visit log of a shop represented by a 0-indexed string customers consisting only of characters 'N' and 'Y':

if the ith character is 'Y', it means that customers come at the ith hour
whereas 'N' indicates that no customers come at the ith hour.

If the shop closes at the jth hour (0 <= j <= n), the penalty is calculated as follows:

For every hour when the shop is open and no customers come, the penalty increases by 1.
For every hour when the shop is closed and customers come, the penalty increases by 1.

Return the earliest hour at which the shop must be closed to incur a minimum penalty.

Note that if a shop closes at the jth hour, it means the shop is closed at the hour j.


Example 1:

Input: customers = "YYNY"
Output: 2
Explanation:
- Closing the shop at the 0th hour incurs in 1+1+0+1 = 3 penalty.
- Closing the shop at the 1st hour incurs in 0+1+0+1 = 2 penalty.
- Closing the shop at the 2nd hour incurs in 0+0+0+1 = 1 penalty.
- Closing the shop at the 3rd hour incurs in 0+0+1+1 = 2 penalty.
- Closing the shop at the 4th hour incurs in 0+0+1+0 = 1 penalty.
Closing the shop at 2nd or 4th hour gives a minimum penalty. Since 2 is earlier, the answer is 2.

Example 2:

Input: customers = "NNNNN"
Output: 0
Explanation: It is best to close the shop at the 0th hour as no customers arrive.

Example 3:

Input: customers = "YYYY"
Output: 4
Explanation: It is best to close the shop at the 4th hour as customers arrive at each hour.


Constraints:

1 <= customers.length <= 10^5
customers consists only of characters 'Y' and 'N'.

"""

# V0
# IDEA : SWEEP THE CLOSING HOUR, UPDATING THE PENALTY INCREMENTALLY
#
#   closing at hour j penalises the 'N's BEFORE j (open with nobody there)
#   plus the 'Y's FROM j onward (closed while customers arrive).
#
#   start at j = 0, where the penalty is simply the number of 'Y's in the
#   whole string. moving the closing hour from j to j + 1 changes it by :
#       customers[j] == 'Y' -> that hour is now served    : penalty -= 1
#       customers[j] == 'N' -> that hour is now wasted    : penalty += 1
#
#   updating only on a STRICT improvement keeps the EARLIEST best hour.
#
# time = O(n), space = O(1)
class Solution(object):
    def bestClosingTime(self, customers):
        penalty = customers.count('Y')     # closing at hour 0
        best_penalty = penalty
        best_hour = 0

        for j, c in enumerate(customers):
            penalty += -1 if c == 'Y' else 1
            if penalty < best_penalty:
                best_penalty = penalty
                best_hour = j + 1
        return best_hour
