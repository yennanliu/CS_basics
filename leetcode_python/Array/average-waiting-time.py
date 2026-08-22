"""

1701. Average Waiting Time
Medium

There is a restaurant with a single chef. You are given an array customers, where
customers[i] = [arrivali, timei]:

- arrivali is the arrival time of the ith customer. The arrival times are sorted in non-decreasing
  order.
- timei is the time needed to prepare the order of the ith customer.

When a customer arrives, he gives the chef his order, and the chef starts preparing it once he is
idle. The customer waits till the chef finishes preparing his order. The chef does not prepare food
for more than one customer at a time. The chef prepares food for customers in the order they were
given in the input.

Return the average waiting time of all customers. Solutions within 10^-5 from the actual answer are
considered accepted.


Example 1:

Input: customers = [[1,2],[2,5],[4,3]]
Output: 5.00000
Explanation:
1) The first customer arrives at time 1, the chef takes his order and starts preparing it immediately
at time 1, and finishes at time 3, so the waiting time of the first customer is 3 - 1 = 2.
2) The second customer arrives at time 2, the chef takes his order and starts preparing it at time 3,
and finishes at time 8, so the waiting time of the second customer is 8 - 2 = 6.
3) The third customer arrives at time 4, the chef takes his order and starts preparing it at time 8,
and finishes at time 11, so the waiting time of the third customer is 11 - 4 = 7.
So the average waiting time = (2 + 6 + 7) / 3 = 5.

Example 2:

Input: customers = [[5,2],[5,4],[10,3],[20,1]]
Output: 3.25000
Explanation:
1) The first customer arrives at time 5, the chef takes his order and starts preparing it immediately
at time 5, and finishes at time 7, so the waiting time of the first customer is 7 - 5 = 2.
2) The second customer arrives at time 5, the chef takes his order and starts preparing it at time 7,
and finishes at time 11, so the waiting time of the second customer is 11 - 5 = 6.
3) The third customer arrives at time 10, the chef takes his order and starts preparing it at time 11,
and finishes at time 14, so the waiting time of the third customer is 14 - 10 = 4.
4) The fourth customer arrives at time 20, the chef takes his order and starts preparing it
immediately at time 20, and finishes at time 21, so the waiting time of the fourth customer is
21 - 20 = 1.
So the average waiting time = (2 + 6 + 4 + 1) / 4 = 3.25.


Constraints:

1 <= customers.length <= 10^5
1 <= arrivali, timei <= 10^4
arrivali <= arrivali+1

"""

# V0
# IDEA : SINGLE-SERVER FIFO SIMULATION (track when the chef becomes free)
#
#   orders are cooked strictly in input order, so one running clock `free` is
#   enough: the chef starts customer i at max(free, arrival_i) and finishes
#   `time_i` later.
#     free = max(free, arrival) + cook
#     wait = free - arrival        (arrival -> the moment the dish is done)
#
#   accumulate the waits as an integer sum and divide once at the end to avoid
#   float drift over 10^5 additions.
#
# time = O(n), space = O(1)
class Solution(object):
    def averageWaitingTime(self, customers):
        free = 0
        total = 0
        for arrival, cook in customers:
            if free < arrival:
                free = arrival
            free += cook
            total += free - arrival
        return float(total) / len(customers)


# V0-1
# IDEA : PREFIX SUM OF COOK TIMES + RUNNING MAX ("max-plus" closed form)
#
#   unrolling finish_i = max(finish_(i-1), arrival_i) + cook_i gives, with P the
#   prefix sum of the cook times :
#
#       finish_i = P[i+1] + max over j <= i of ( arrival_j - P[j] )
#
#   so the whole "is the chef still busy" state collapses into ONE running
#   maximum of (arrival_j - P[j]) - no simulation clock at all.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def averageWaitingTime(self, customers):
        pre = 0        # P[i] : cook times of customers 0 .. i-1
        best = None    # max over j <= i of (arrival_j - P[j])
        total = 0
        for arrival, cook in customers:
            cand = arrival - pre
            if best is None or cand > best:
                best = cand
            pre += cook            # pre is now P[i+1]
            total += pre + best - arrival
        return float(total) / len(customers)


# V0-2
# IDEA : TOP-DOWN MEMOISED RECURSION ON THE FINISH TIMES
#
#   finish(i) = max(finish(i-1), arrival_i) + cook_i, with finish(-1) = 0.
#   asking for finish(n-1) pulls the whole chain in and the memo table then
#   holds every finish time, so the waits are read straight off it.
#   NOTE : depth is O(n), so the recursion limit has to be lifted for the 10^5
#          upper bound - that is the price of the recursive phrasing.
#
# time = O(n)
# space = O(n)
import sys


class Solution(object):
    def averageWaitingTime(self, customers):
        n = len(customers)
        sys.setrecursionlimit(max(2000, n + 100))
        memo = {}

        def finish(i):
            if i < 0:
                return 0
            if i in memo:
                return memo[i]
            arrival, cook = customers[i]
            prev = finish(i - 1)
            memo[i] = (prev if prev > arrival else arrival) + cook
            return memo[i]

        finish(n - 1)
        total = sum(memo[i] - customers[i][0] for i in range(n))
        return float(total) / n
