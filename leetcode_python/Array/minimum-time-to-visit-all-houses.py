"""

3540. Minimum Time to Visit All Houses
Medium

You are given two integer arrays forward and backward, both of size n. You are
also given another integer array queries.

There are n houses arranged in a circle. The houses are connected via roads in a
special arrangement:

For all 0 <= i <= n - 2, house i is connected to house i + 1 via a road with
length forward[i] meters. Additionally, house n - 1 is connected back to house 0
via a road with length forward[n - 1] meters, completing the circle.

For all 1 <= i <= n - 1, house i is connected to house i - 1 via a road with
length backward[i] meters. Additionally, house 0 is connected back to house n -
1 via a road with length backward[0] meters, completing the circle.

You can walk at a pace of one meter per second. Starting from house 0, find the
minimum time taken to visit each house in the order specified by queries.

Return the minimum total time taken to visit the houses.

Example 1:

Input: forward = [1,4,4], backward = [4,1,2], queries = [1,2,0,2]

Output: 12

Explanation:

The path followed is 0^(0) → 1^(1) → 2^(5) → 1^(7) → 0^(8) → 2^(12).

Note: The notation used is node^(total time), → represents forward road, and →
represents backward road.

Example 2:

Input: forward = [1,1,1,1], backward = [2,2,2,2], queries = [1,2,3,0]

Output: 4

Explanation:

The path travelled is 0 → 1 → 2 → 3 → 0. Each step is in the forward direction
and requires 1 second.

Constraints:

2 <= n <= 10^5

n == forward.length == backward.length

1 <= forward[i], backward[i] <= 10^5

1 <= queries.length <= 10^5

0 <= queries[i] < n

queries[i] != queries[i + 1]

queries[0] is not 0.

"""

# V0
# IDEA : EACH HOP IS INDEPENDENT -- TWO PREFIX SUMS AROUND THE CIRCLE
#
#   the houses have to be visited in the exact order given, so the walk breaks
#   into independent hops "from where I am to the next requested house".  there
#   is nothing to optimise across hops.
#
#   inside one hop there are only two routes on a circle: keep going forward
#   until the target appears, or keep going backward.  both are contiguous runs
#   of road lengths, which prefix sums answer in O(1) -- the only care needed
#   is the wrap-around, where the run splits into a tail and a head of the
#   array.
#
#   note the two directions use *different* road lengths (forward[] and
#   backward[]), so each needs its own prefix array; the answer per hop is
#   simply the smaller of the two.
#
# time = O(n + q), space = O(n)
class Solution(object):
    def minTotalTime(self, forward, backward, queries):
        n = len(forward)
        F = [0] * (n + 1)
        B = [0] * (n + 1)
        for i in range(n):
            F[i + 1] = F[i] + forward[i]
            B[i + 1] = B[i] + backward[i]

        total = 0
        cur = 0
        for nxt in queries:
            if nxt == cur:
                continue
            if nxt > cur:                       # forward without wrapping
                fw = F[nxt] - F[cur]
                bw = B[cur + 1] + (B[n] - B[nxt + 1])
            else:
                fw = F[n] - F[cur] + F[nxt]
                bw = B[cur + 1] - B[nxt + 1]
            total += fw if fw < bw else bw
            cur = nxt
        return total
