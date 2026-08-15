"""

3100. Water Bottles II
Medium

You are given two integers numBottles and numExchange.

numBottles represents the number of full water bottles that you initially have. In one operation, you can perform one of the following operations:

Drink any number of full water bottles turning them into empty bottles.
Exchange numExchange empty bottles with one full water bottle. Then, increase numExchange by one.

Note that you cannot exchange multiple batches of empty bottles for the same value of numExchange. For example, if numBottles == 3 and numExchange == 1, you cannot exchange 3 empty water bottles for 3 full bottles.

Return the maximum number of water bottles you can drink.


Example 1:

Input: numBottles = 13, numExchange = 6
Output: 15
Explanation: The table above shows the number of full water bottles, empty water bottles, the value of numExchange, and the number of bottles drunk.

Example 2:

Input: numBottles = 10, numExchange = 3
Output: 13
Explanation: The table above shows the number of full water bottles, empty water bottles, the value of numExchange, and the number of bottles drunk.


Constraints:

1 <= numBottles <= 100
1 <= numExchange <= 100

"""

# V0
# IDEA : DRINK EVERYTHING, THEN EXCHANGE WHILE THE EMPTIES STILL COVER THE PRICE
#
#   drinking is free and never hurts, so drink every full bottle immediately;
#   each one becomes an empty.
#
#   then repeat : if the empties cover the current price, pay it, gain one
#   full bottle, drink it (so the empty count goes down by numExchange and
#   back up by 1), and the price rises by one for the next round.
#
#   the price only grows, so the loop ends quickly — at most O(sqrt(total))
#   exchanges.
#
# time = O(sqrt(numBottles)), space = O(1)
class Solution(object):
    def maxBottlesDrunk(self, numBottles, numExchange):
        drunk = numBottles
        empty = numBottles
        while empty >= numExchange:
            empty -= numExchange
            numExchange += 1
            drunk += 1
            empty += 1                 # the new bottle is drunk right away
        return drunk
