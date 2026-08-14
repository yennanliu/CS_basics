"""

1052. Grumpy Bookstore Owner
Medium

There is a bookstore owner that has a store open for n minutes.
You are given an integer array customers of length n where customers[i] is the
number of the customers that enter the store at the start of the ith minute and
all those customers leave after the end of that minute.

During certain minutes, the bookstore owner is grumpy. You are given a binary array
grumpy where grumpy[i] is 1 if the bookstore owner is grumpy during the ith minute,
and is 0 otherwise.

When the bookstore owner is grumpy, the customers entering during that minute are
not satisfied. Otherwise, they are satisfied.

The bookstore owner knows a secret technique to remain not grumpy for `minutes`
consecutive minutes, but this technique can only be used once.

Return the maximum number of customers that can be satisfied throughout the day.


Example 1:

Input: customers = [1,0,1,2,1,1,7,5], grumpy = [0,1,0,1,0,1,0,1], minutes = 3
Output: 16
Explanation:
The bookstore owner keeps themselves not grumpy for the last 3 minutes.
The maximum number of customers that can be satisfied = 1 + 1 + 1 + 1 + 7 + 5 = 16.

Example 2:

Input: customers = [1], grumpy = [0], minutes = 1
Output: 1


Constraints:

n == customers.length == grumpy.length
1 <= minutes <= n <= 2 * 10^4
0 <= customers[i] <= 1000
grumpy[i] is either 0 or 1.

"""

# V0
# IDEA : FIXED-SIZE SLIDING WINDOW
#
#  base    = customers already satisfied (minutes where grumpy[i] == 0)
#            -> these are locked in whatever we do
#  gain(w) = customers RESCUED by using the technique on window w
#            -> sum of customers[i] where grumpy[i] == 1 inside w
#  answer  = base + max gain over all windows of size `minutes`
#
# time = O(n)
# space = O(1)
class Solution(object):
    def maxSatisfied(self, customers, grumpy, minutes):
        n = len(customers)

        base = 0
        for i in range(n):
            if grumpy[i] == 0:
                base += customers[i]

        cur = 0
        best = 0
        for i in range(n):
            if grumpy[i] == 1:
                cur += customers[i]
            # drop the element leaving the window
            if i >= minutes and grumpy[i - minutes] == 1:
                cur -= customers[i - minutes]
            best = max(best, cur)

        return base + best
