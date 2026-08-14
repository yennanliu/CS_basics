"""

1801. Number of Orders in the Backlog
Medium

You are given a 2D integer array orders, where each orders[i] = [pricei, amounti, orderTypei] denotes that amounti orders have been placed of type orderTypei at the price pricei. The orderTypei is:

0 if it is a batch of buy orders, or

1 if it is a batch of sell orders.

Note that orders[i] represents a batch of amounti independent orders with the same price and order type. All orders represented by orders[i] will be placed before all orders represented by orders[i+1] for all valid i.

There is a backlog that consists of orders that have not been executed. The backlog is initially empty. When an order is placed, the following happens:

If the order is a buy order, you look at the sell order with the smallest price in the backlog. If that sell order's price is smaller than or equal to the current buy order's price, they will match and be executed, and that sell order will be removed from the backlog. Else, the buy order is added to the backlog.

Vice versa, if the order is a sell order, you look at the buy order with the largest price in the backlog. If that buy order's price is larger than or equal to the current sell order's price, they will match and be executed, and that buy order will be removed from the backlog. Else, the sell order is added to the backlog.

Return the total amount of orders in the backlog after placing all the orders from the input. Since this number can be large, return it modulo 10^9 + 7.

Example 1:

Input: orders = [[10,5,0],[15,2,1],[25,1,1],[30,4,0]]

Output: 6

Explanation: Here is what happens with the orders:

- 5 orders of type buy with price 10 are placed. There are no sell orders, so the 5 orders are added to the backlog.

- 2 orders of type sell with price 15 are placed. There are no buy orders with prices larger than or equal to 15, so the 2 orders are added to the backlog.

- 1 order of type sell with price 25 is placed. There are no buy orders with prices larger than or equal to 25 in the backlog, so this order is added to the backlog.

- 4 orders of type buy with price 30 are placed. The first 2 orders are matched with the 2 sell orders of the least price, which is 15 and these 2 sell orders are removed from the backlog. The 3^rd order is matched with the sell order of the least price, which is 25 and this sell order is removed from the backlog. Then, there are no more sell orders in the backlog, so the 4^th order is added to the backlog.

Finally, the backlog has 5 buy orders with price 10, and 1 buy order with price 30. So the total number of orders in the backlog is 6.

Example 2:

Input: orders = [[7,1000000000,1],[15,3,0],[5,999999995,0],[5,1,1]]

Output: 999999984

Explanation: Here is what happens with the orders:

- 10^9 orders of type sell with price 7 are placed. There are no buy orders, so the 10^9 orders are added to the backlog.

- 3 orders of type buy with price 15 are placed. They are matched with the 3 sell orders with the least price which is 7, and those 3 sell orders are removed from the backlog.

- 999999995 orders of type buy with price 5 are placed. The least price of a sell order is 7, so the 999999995 orders are added to the backlog.

- 1 order of type sell with price 5 is placed. It is matched with the buy order of the highest price, which is 5, and that buy order is removed from the backlog.

Finally, the backlog has (1000000000-3) sell orders with price 7, and (999999995-1) buy orders with price 5. So the total number of orders = 1999999991, which is equal to 999999984 % (10^9 + 7).

Constraints:

1 <= orders.length <= 10^5

orders[i].length == 3

1 <= pricei, amounti <= 10^9

orderTypei is either 0 or 1.

"""

# V0
# IDEA : TWO HEAPS SIMULATING AN ORDER BOOK
#
#   sell backlog -> min heap on price (cheapest sell first)
#   buy  backlog -> max heap on price (dearest buy first, stored negated)
#   an incoming batch keeps eating the opposite book while the price crosses,
#   partially consuming the top entry when the amounts do not line up (push the
#   remainder back). whatever is left of the batch joins its own book.
#   NOTE : amounts go up to 10^9, so batches must be matched in BULK, never one
#          order at a time - the loop runs at most once per pushed entry, so
#          the whole simulation is O(n log n).
#
# time = O(n log n), space = O(n)
from heapq import heappush, heappop
class Solution(object):
    def getNumberOfBacklogOrders(self, orders):
        MOD = 10 ** 9 + 7
        buy, sell = [], []          # buy holds (-price, amount)
        for price, amount, typ in orders:
            if typ == 0:
                while amount and sell and sell[0][0] <= price:
                    p, cnt = heappop(sell)
                    take = min(amount, cnt)
                    amount -= take
                    if cnt > take:
                        heappush(sell, (p, cnt - take))
                if amount:
                    heappush(buy, (-price, amount))
            else:
                while amount and buy and -buy[0][0] >= price:
                    p, cnt = heappop(buy)
                    take = min(amount, cnt)
                    amount -= take
                    if cnt > take:
                        heappush(buy, (p, cnt - take))
                if amount:
                    heappush(sell, (price, amount))

        res = 0
        for _, cnt in buy:
            res += cnt
        for _, cnt in sell:
            res += cnt
        return res % MOD
