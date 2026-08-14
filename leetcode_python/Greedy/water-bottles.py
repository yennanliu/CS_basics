"""

1518. Water Bottles
Easy

There are numBottles water bottles that are initially full of water. You can exchange numExchange empty water bottles from the market with one full water bottle.

The operation of drinking a full water bottle turns it into an empty bottle.

Given the two integers numBottles and numExchange, return the maximum number of water bottles you can drink.


Example 1:

Input: numBottles = 9, numExchange = 3
Output: 13
Explanation: You can exchange 3 empty bottles to get 1 full water bottle.
Number of water bottles you can drink: 9 + 3 + 1 = 13.

Example 2:

Input: numBottles = 15, numExchange = 4
Output: 19
Explanation: You can exchange 4 empty bottles to get 1 full water bottle.
Number of water bottles you can drink: 15 + 3 + 1 = 19.


Constraints:

1 <= numBottles <= 100
2 <= numExchange <= 100

"""

# V0
# IDEA : SIMULATION (each trade consumes numExchange - 1 bottles net)
#
#   drink everything we hold, then trade empties for refills.
#   one trade : give away numExchange empties, get back 1 bottle which
#   after drinking becomes 1 empty -> net loss of (numExchange - 1) empties
#   and +1 to the drink count.
#
#   loop while we still hold enough empties to trade.
#
# time = O(numBottles / numExchange), space = O(1)
class Solution(object):
    def numWaterBottles(self, numBottles, numExchange):
        res = numBottles
        empty = numBottles
        while empty >= numExchange:
            empty -= numExchange - 1
            res += 1
        return res
