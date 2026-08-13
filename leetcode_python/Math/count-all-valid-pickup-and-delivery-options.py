"""

1359. Count All Valid Pickup and Delivery Options
Hard

Given n orders, each order consists of a pickup and a delivery service.

Count all valid pickup/delivery possible sequences such that delivery(i) is always after of pickup(i).

Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: n = 1
Output: 1
Explanation: Unique order (P1, D1), Delivery 1 always is after of Pickup 1.

Example 2:

Input: n = 2
Output: 6
Explanation: All possible orders:
(P1,P2,D1,D2), (P1,P2,D2,D1), (P1,D1,P2,D2), (P2,P1,D1,D2), (P2,P1,D2,D1) and (P2,D2,P1,D1).
This is an invalid order (P1,D2,P2,D1) because Pickup 2 is after of Delivery 2.

Example 3:

Input: n = 3
Output: 90


Constraints:

1 <= n <= 500

"""

# V0
# IDEA : COMBINATORICS / 1D DP
#
#  f[i] = number of valid sequences using i orders.
#
#  Add the i-th order to a valid sequence of (i-1) orders (length 2*(i-1)):
#    - inserting the PAIR (Pi, Di) into the existing sequence, we must place
#      2 items into 2*i slots keeping Pi before Di
#      -> C(2*i, 2) = i * (2*i - 1) ways
#
#  DP eq:
#     f[i] = i * (2*i - 1) * f[i-1],  f[1] = 1
#
#  Only f[i-1] is needed -> keep a single variable.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def countOrders(self, n):
        mod = 10 ** 9 + 7
        f = 1
        for i in range(2, n + 1):
            f = (f * i * (2 * i - 1)) % mod
        return f
