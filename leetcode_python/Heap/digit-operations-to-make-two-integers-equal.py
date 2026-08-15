"""

3377. Digit Operations to Make Two Integers Equal
Medium

You are given two integers n and m that consist of the same number of digits.

You can perform the following operations any number of times:

Choose any digit from n that is not 9 and increase it by 1.
Choose any digit from n that is not 0 and decrease it by 1.

The integer n must not be a prime number at any point, including its original value and after each operation.

The cost of a transformation is the sum of all values that n takes throughout the operations performed.

Return the minimum cost to transform n into m. If it is impossible, return -1.


Example 1:

Input: n = 10, m = 12
Output: 85
Explanation:
We perform the following operations:
Increase the first digit, now n = 20.
Increase the second digit, now n = 21.
Increase the second digit, now n = 22.
Decrease the first digit, now n = 12.
The sum of all values that n takes is 10 + 20 + 21 + 22 + 12 = 85.

Example 2:

Input: n = 4, m = 8
Output: -1
Explanation:
It is impossible to make n equal to m.

Example 3:

Input: n = 6, m = 2
Output: -1
Explanation:
Since 2 is already a prime, we can't make n equal to m.


Constraints:

1 <= n, m < 10^4
n and m consist of the same number of digits.

"""

# V0
# IDEA : DIJKSTRA OVER THE NUMBERS, WHERE ENTERING A STATE COSTS ITS VALUE
#
#   every operation moves to a number with the same digit count, so the
#   states are just the integers of that width — at most 9000 of them. an
#   edge exists between two numbers differing by +-1 in a single digit.
#
#   the cost is the SUM of the values visited, i.e. arriving at a node costs
#   that node's value, so it is a shortest-path problem with node weights.
#   Dijkstra handles it directly, seeded with n's own value.
#
#   primes are forbidden anywhere along the way, including the endpoints, so
#   they are simply never entered — a sieve marks them up front.
#
# time = O(states * digits * 10 log states), space = O(states)
import heapq


class Solution(object):
    def minOperations(self, n, m):
        digits = len(str(n))
        lo = 10 ** (digits - 1) if digits > 1 else 1
        hi = 10 ** digits - 1

        sieve = [True] * (hi + 1)
        sieve[0] = False
        if hi >= 1:
            sieve[1] = False
        p = 2
        while p * p <= hi:
            if sieve[p]:
                for v in range(p * p, hi + 1, p):
                    sieve[v] = False
            p += 1

        if sieve[n] or sieve[m]:
            return -1

        INF = float('inf')
        dist = {}
        pq = [(n, n)]
        dist[n] = n

        while pq:
            d, cur = heapq.heappop(pq)
            if d > dist.get(cur, INF):
                continue
            if cur == m:
                return d
            s = str(cur)
            for i, ch in enumerate(s):
                c = int(ch)
                for nd in (c - 1, c + 1):
                    if nd < 0 or nd > 9:
                        continue
                    if i == 0 and nd == 0 and digits > 1:
                        continue                # no leading zero
                    nxt = int(s[:i] + str(nd) + s[i + 1:])
                    if sieve[nxt]:
                        continue                # primes are off limits
                    nd_cost = d + nxt
                    if nd_cost < dist.get(nxt, INF):
                        dist[nxt] = nd_cost
                        heapq.heappush(pq, (nd_cost, nxt))
        return -1
