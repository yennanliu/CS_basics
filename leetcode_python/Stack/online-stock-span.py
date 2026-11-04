"""

901. Online Stock Span
Medium

Design an algorithm that collects daily price quotes for some stock and returns
the span of that stock's price for the current day.

The span of the stock's price in one day is the maximum number of consecutive
days (starting from that day and going backward) for which the stock price was
less than or equal to the price of that day.

For example, if the prices of the stock in the last four days are [7,2,1,2] and
the price of the stock today is 2, then the span of today is 3 because starting
from today, the price of the stock was less than or equal to 2 for 3 consecutive
days.
Also, if the prices of the stock in the last four days is [7,34,1,2] and the
price of the stock today is 8, then the span of today is 3 because starting from
today, the price of the stock was less than or equal 8 for 3 consecutive days.

Implement the StockSpanner class:

StockSpanner() Initializes the object of the class.
int next(int price) Returns the span of the stock's price given that today's
price is price.

Example 1:

Input
["StockSpanner", "next", "next", "next", "next", "next", "next", "next"]
[[], [100], [80], [60], [70], [60], [75], [85]]
Output
[null, 1, 1, 1, 2, 1, 4, 6]

Explanation
StockSpanner stockSpanner = new StockSpanner();
stockSpanner.next(100); // return 1
stockSpanner.next(80);  // return 1
stockSpanner.next(60);  // return 1
stockSpanner.next(70);  // return 2
stockSpanner.next(60);  // return 1
stockSpanner.next(75);
// return 4, because the last 4 prices (including today's price of 75) were less
than or equal to today's price.
stockSpanner.next(85);  // return 6

Constraints:

1 <= price <= 10^5
At most 10^4 calls will be made to next.

"""

# V0
# IDEA : MONOTONIC DECREASING STACK OF (price, span)
#
#   Today's span covers every earlier day whose price is <= today's, walking
#   back until a strictly higher price. Once a day is covered by a higher (or
#   equal) later day, it can never be the "blocker" for any future day - the
#   later day is at least as high and closer. So pop it, and fold its span
#   into today's: those days are covered too, without visiting them again.
#
#   The stack therefore holds prices in strictly decreasing order, each with
#   the number of days it absorbed.
#
#   e.g. prices 100, 80, 60, 70, 60, 75
#        75 arrives, stack = [(100,1), (80,1), (70,2), (60,1)]
#        pop (60,1), pop (70,2) -> span = 1 + 1 + 2 = 4, stop at 80
#        stack = [(100,1), (80,1), (75,4)]
#
#   (Scanning back over the raw price list is O(n) per call -> O(n^2) on a
#    rising series of 10^4 prices; the stack visits each day at most twice.)
#
# time = O(1) amortized per call (each price pushed and popped once),
#        space = O(n)
class StockSpanner(object):

    def __init__(self):
        # (price, span) pairs, prices strictly decreasing from bottom to top
        self.stack = []

    def next(self, price):
        """
        :type price: int
        :rtype: int
        """
        # today always counts itself
        span = 1

        # NOTE !!! <= (not <): an equal earlier price is part of today's span
        while self.stack and self.stack[-1][0] <= price:
            covered_price, covered_span = self.stack.pop()
            span += covered_span

        self.stack.append((price, span))
        return span


# V0-1
# IDEA : MONOTONIC STACK OF (day index, price) -> PREVIOUS GREATER ELEMENT
#
#   Same stack, different bookkeeping: store the day index instead of the
#   span. After popping every price <= today's, the top of the stack is the
#   nearest earlier day with a strictly HIGHER price (the "previous greater
#   element"), so span = today - that day. With no higher day left, the span
#   is every day so far: today + 1 (days are 0-indexed).
#
#   e.g. prices 100, 80, 60, 70, 60, 75 -> 75 is day 5
#        pop days 4 (60) and 3 (70); top is day 1 (80) -> span = 5 - 1 = 4
#
#   Worth knowing because it is the general template: the same loop answers
#   LC 739 / 496 / 503 (next / previous greater element).
#
# time = O(1) amortized per call, space = O(n)
class StockSpanner2(object):

    def __init__(self):
        # (day, price) pairs, prices strictly decreasing from bottom to top
        self.stack = []
        self.day = -1

    def next(self, price):
        """
        :type price: int
        :rtype: int
        """
        self.day += 1

        # drop every earlier day this price covers
        while self.stack and self.stack[-1][1] <= price:
            self.stack.pop()

        # the top (if any) is the previous strictly higher day
        if self.stack:
            previous_higher_day = self.stack[-1][0]
        else:
            previous_higher_day = -1

        self.stack.append((self.day, price))
        return self.day - previous_higher_day
