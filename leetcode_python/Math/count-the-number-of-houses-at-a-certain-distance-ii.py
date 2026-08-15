"""

3017. Count the Number of Houses at a Certain Distance II
Hard

You are given three positive integers n, x, and y.

In a city, there exist houses numbered 1 to n connected by n streets. There is a street connecting the house numbered i with the house numbered i + 1 for all 1 <= i <= n - 1 . An additional street connects the house numbered x with the house numbered y.

For each k, such that 1 <= k <= n, you need to find the number of pairs of houses (house1, house2) such that the minimum number of streets that need to be traveled to reach house2 from house1 is k.

Return a 1-indexed array result of length n where result[k] represents the total number of pairs of houses such that the minimum streets required to reach one house from the other is k.

Note that x and y can be equal.


Example 1:

Input: n = 3, x = 1, y = 3
Output: [6,0,0]
Explanation: Let's look at each pair of houses:
- For the pair (1, 2), we can go from house 1 to house 2 directly.
- For the pair (2, 1), we can go from house 2 to house 1 directly.
- For the pair (1, 3), we can go from house 1 to house 3 directly.
- For the pair (3, 1), we can go from house 3 to house 1 directly.
- For the pair (2, 3), we can go from house 2 to house 3 directly.
- For the pair (3, 2), we can go from house 3 to house 2 directly.

Example 2:

Input: n = 5, x = 2, y = 4
Output: [10,8,2,0,0]
Explanation: For each distance k the pairs are:
- For k == 1, the pairs are (1, 2), (2, 1), (2, 3), (3, 2), (2, 4), (4, 2), (3, 4), (4, 3), (4, 5), and (5, 4).
- For k == 2, the pairs are (1, 3), (3, 1), (1, 4), (4, 1), (2, 5), (5, 2), (3, 5), and (5, 3).
- For k == 3, the pairs are (1, 5) and (5, 1).
- For k == 4 and k == 5, there are no pairs.

Example 3:

Input: n = 4, x = 1, y = 1
Output: [6,4,2,0]
Explanation: For each distance k the pairs are:
- For k == 1, the pairs are (1, 2), (2, 1), (2, 3), (3, 2), (3, 4), and (4, 3).
- For k == 2, the pairs are (1, 3), (3, 1), (2, 4), and (4, 2).
- For k == 3, the pairs are (1, 4), and (4, 1).
- For k == 4, there are no pairs.


Constraints:

2 <= n <= 10^5
1 <= x, y <= n

"""

# V0
# IDEA : THE GRAPH IS A CYCLE WITH TWO TAILS — EACH SOURCE ADDS O(1) RANGES
#
#   n reaches 10^5 so the per-source BFS of LC 3015 is out. but the shape is
#   very rigid : with x < y, the houses x..y form a CYCLE of L = y - x + 1
#   nodes (the extra street closes it), houses 1..x-1 are a tail hanging off
#   x, and houses y+1..n a tail hanging off y.
#
#   that makes every source's distance profile a handful of ARITHMETIC RUNS,
#   which a difference array absorbs in O(1) each :
#
#   * cycle -> cycle : from any cycle node the others sit at distance
#       1..(L-1)//2 twice each, plus one node at L/2 when L is even.
#   * cycle <-> tail : the tail is only reachable through its anchor, so the
#       distances are anchor_distance + 1 .. anchor_distance + tail_size,
#       one node each.
#   * tail -> tail (same tail)  : plain path distances, two runs.
#   * tail -> far tail          : anchor-to-anchor is 1 step (the extra
#                                 street), giving one shifted run.
#
#   when y - x <= 1 the extra street is a duplicate and the city is a plain
#   path, where the count at distance k is simply 2 * (n - k).
#
# time = O(n), space = O(n)
class Solution(object):
    def countOfPairs(self, n, x, y):
        if x > y:
            x, y = y, x

        # degenerate : the extra street adds nothing, so it is a straight path
        if y - x <= 1:
            return [2 * (n - k) for k in range(1, n + 1)]

        diff = [0] * (n + 3)

        def add(lo, hi, c):
            if lo > hi or lo > n:
                return
            lo = max(lo, 1)
            hi = min(hi, n)
            if lo > hi:
                return
            diff[lo] += c
            diff[hi + 1] -= c

        L = y - x + 1                 # nodes on the cycle
        half = (L - 1) // 2           # distances hit twice
        even = (L % 2 == 0)           # one extra node exactly opposite
        A = x - 1                     # left tail size
        B = n - y                     # right tail size

        # ---- sources in the left tail : houses 1 .. x-1 ----
        for u in range(1, x):
            a = x - u                             # steps up to the anchor x
            add(1, u - 1, 1)                      # neighbours further left
            add(1, x - 1 - u, 1)                  # neighbours further right
            add(a, a, 1)                          # the anchor itself
            add(a + 1, a + half, 2)               # the rest of the cycle
            if even:
                add(a + L // 2, a + L // 2, 1)
            add(a + 2, a + 1 + B, 1)              # across to the right tail

        # ---- sources in the right tail : houses y+1 .. n ----
        for u in range(y + 1, n + 1):
            b = u - y                             # steps down to the anchor y
            add(1, n - u, 1)
            add(1, u - y - 1, 1)
            add(b, b, 1)
            add(b + 1, b + half, 2)
            if even:
                add(b + L // 2, b + L // 2, 1)
            add(b + 2, b + 1 + A, 1)              # across to the left tail

        # ---- sources on the cycle : houses x .. y ----
        for u in range(x, y + 1):
            add(1, half, 2)
            if even:
                add(L // 2, L // 2, 1)
            dx = min(u - x, L - (u - x))          # distance to the left anchor
            dy = min(y - u, L - (y - u))          # distance to the right anchor
            add(dx + 1, dx + A, 1)
            add(dy + 1, dy + B, 1)

        res = []
        run = 0
        for k in range(1, n + 1):
            run += diff[k]
            res.append(run)
        return res
