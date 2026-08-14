"""

2412. Minimum Money Required Before Transactions
Hard

You are given a 0-indexed 2D integer array transactions, where transactions[i] = [cost_i, cashback_i].

The array describes transactions, where each transaction must be completed exactly once in some order. At any given moment, you have a certain amount of money. In order to complete transaction i, money >= cost_i must hold true. After performing a transaction, money becomes money - cost_i + cashback_i.

Return the minimum amount of money required before any transaction so that all of the transactions can be completed regardless of the order of the transactions.


Example 1:

Input: transactions = [[2,1],[5,0],[4,2]]
Output: 10
Explanation:
Starting with money = 10, the transactions can be performed in any order.
It can be shown that starting with money < 10 will fail to complete all transactions in some order.

Example 2:

Input: transactions = [[3,0],[0,3]]
Output: 3
Explanation:
- If transactions are in the order [[3,0],[0,3]], the minimum money required to complete the transactions is 3.
- If transactions are in the order [[0,3],[3,0]], the minimum money required to complete the transactions is 0.
Thus, starting with the minimum money of 3, all transactions can be performed in any order.


Constraints:

1 <= transactions.length <= 10^5
transactions[i].length == 2
0 <= cost_i, cashback_i <= 10^9

"""

# V0
# IDEA : WORST ORDER = ALL THE LOSSES FIRST, THEN THE HARDEST REMAINING ONE
#
#   "regardless of the order" means we must survive the WORST ordering. an
#   adversary would run every loss-making transaction (cost > cashback)
#   first, draining
#       total_loss = sum(cost - cashback) over those
#   and then hit us with whichever single transaction demands the most cash
#   up front.
#
#   for a candidate "last" transaction i the requirement works out to
#       total_loss + min(cost_i, cashback_i)
#   — if it is loss-making its own loss is already inside total_loss, leaving
#   cashback_i; otherwise the full cost_i is still needed on top.
#
#   so the answer is total_loss + max over i of min(cost_i, cashback_i).
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumMoney(self, transactions):
        total_loss = sum(cost - cashback
                         for cost, cashback in transactions if cost > cashback)
        worst_last = max(min(cost, cashback) for cost, cashback in transactions)
        return total_loss + worst_last
