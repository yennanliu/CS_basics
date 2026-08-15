"""

3155. Maximum Number of Upgradable Servers
Medium
🔒 (premium)

You have n data centers and need to upgrade their servers.

You are given four arrays count, upgrade, sell, and money of length n, which show:

The number of servers
The cost of upgrading a single server
The money you get by selling a server
The money you initially have

for each data center respectively.

Return an array answer, where for each data center, the corresponding element in answer represents the maximum number of servers that can be upgraded.

Note that the money from one data center cannot be used for another data center.


Example 1:

Input: count = [4,3], upgrade = [3,5], sell = [4,2], money = [8,9]
Output: [3,2]
Explanation:
For the first data center, if we sell one server, we'll have 8 + 4 = 12 units of money and we can upgrade the remaining 3 servers for 3 * 3 = 9 units of money.
For the second data center, if we sell one server, we'll have 9 + 2 = 11 units of money and we can upgrade the remaining 2 servers for 2 * 5 = 10 units of money.


Constraints:

1 <= count.length == upgrade.length == sell.length == money.length <= 10^5
1 <= count[i], upgrade[i], sell[i], money[i] <= 10^5

"""

# V0
# IDEA : SOLVE THE INEQUALITY FOR k INSTEAD OF SEARCHING FOR IT
#
#   upgrading k servers means selling the other (count - k), so the budget
#   condition is
#
#       k * upgrade  <=  money + (count - k) * sell
#
#   collecting the k terms turns that into a single division :
#
#       k * (upgrade + sell)  <=  money + count * sell
#       k  <=  (money + count * sell) // (upgrade + sell)
#
#   and k is of course also capped by count. no binary search needed — each
#   data center is answered in O(1), and they never share money.
#
# time = O(n), space = O(n)
class Solution(object):
    def maxUpgrades(self, count, upgrade, sell, money):
        return [min(c, (m + c * s) // (u + s))
                for c, u, s, m in zip(count, upgrade, sell, money)]
