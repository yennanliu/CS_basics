"""

2240. Number of Ways to Buy Pens and Pencils
Medium

You are given an integer total indicating the amount of money you have. You are also given two integers cost1 and cost2 indicating the price of a pen and pencil respectively. You can spend part or all of your money to buy multiple quantities (or none) of each kind of writing utensil.

Return the number of distinct ways you can buy some number of pens and pencils.


Example 1:

Input: total = 20, cost1 = 10, cost2 = 5
Output: 9
Explanation: The price of a pen is 10 and the price of a pencil is 5.
- If you buy 0 pens, you can buy 0, 1, 2, 3, or 4 pencils.
- If you buy 1 pen, you can buy 0, 1, or 2 pencils.
- If you buy 2 pens, you can buy 0 pencils.
Thus, there are 5 + 3 + 1 = 9 ways to buy pens and pencils.

Example 2:

Input: total = 5, cost1 = 10, cost2 = 10
Output: 1
Explanation: The price of both pens and pencils are 10, which cost more than total, so you cannot buy any writing utensils. Thus, there is only 1 way: buy 0 pens and 0 pencils.


Constraints:

1 <= total, cost1, cost2 <= 10^6

"""

# V0
# IDEA : FIX THE NUMBER OF PENS, COUNT THE PENCILS ARITHMETICALLY
#
#   with x pens bought, the money left is total - x * cost1, and the number
#   of pencil choices is
#       (total - x * cost1) // cost2 + 1
#   (the +1 is buying zero pencils).
#
#   summing over x = 0 .. total // cost1 gives the answer. that loop runs at
#   most total / cost1 times, which is fine at 10^6 in the worst case, and
#   trying both orders is unnecessary — the counts are symmetric.
#
#   NOTE : the total can exceed 32 bits; python ints handle it natively.
#
# time = O(total / cost1), space = O(1)
class Solution(object):
    def waysToBuyPensPencils(self, total, cost1, cost2):
        res = 0
        for x in range(total // cost1 + 1):
            res += (total - x * cost1) // cost2 + 1
        return res
