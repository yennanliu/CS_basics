"""

1227. Airplane Seat Assignment Probability
Medium

n passengers board an airplane with exactly n seats. The first passenger has lost the
ticket and picks a seat randomly. But after that, the rest of the passengers will:

Take their own seat if it is still available, and
Pick other seats randomly when they find their seat occupied

Return the probability that the nth person gets his own seat.

Example 1:

Input: n = 1
Output: 1.00000
Explanation: The first person can only get the first seat.

Example 2:

Input: n = 2
Output: 0.50000
Explanation: The second person has a probability of 0.5 to get the second seat (when
first person gets the first seat).


Constraints:

1 <= n <= 10^5

"""

# V0
# IDEA : MATH / BRAINTEASER
"""
 Let f(n) = probability the n-th passenger gets their own seat.

 f(1) = 1
 f(2) = 0.5

 For n > 2, look at what passenger 1 does:
   - picks seat 1        (prob 1/n)      -> everyone is seated correctly -> 1.0
   - picks seat n        (prob 1/n)      -> passenger n is displaced     -> 0.0
   - picks seat i, 1<i<n (prob 1/n each) -> passengers 2..i-1 sit normally,
     and passenger i now plays the role of the "lost ticket" passenger over
     the remaining n-i+1 seats -> f(n-i+1)

 =>  f(n) = 1/n * (1 + sum_{i=2}^{n-1} f(n-i+1))
          = 1/n * (1 + f(n-1) + f(n-2) + ... + f(2))

 Plugging f(2) = 0.5 and inducting gives f(n) = 0.5 for every n >= 2.
"""
# time = O(1)
# space = O(1)
class Solution(object):
    def nthPersonGetsNthSeat(self, n):
        return 1.0 if n == 1 else 0.5


# V1
# IDEA : DP (the literal recurrence, kept for reference / to sanity check V0)
#        f[k] = 1/k * (1 + sum(f[2..k-1]))
# time = O(n)
# space = O(n)
class Solution_1(object):
    def nthPersonGetsNthSeat(self, n):
        if n == 1:
            return 1.0
        f = [0.0] * (n + 1)
        f[1] = 1.0
        f[2] = 0.5
        acc = 0.0  # sum of f[2..k-1]
        for k in range(3, n + 1):
            acc += f[k - 1]
            f[k] = (1.0 + acc) / k
        return f[n]
