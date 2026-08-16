"""

3538. Merge Operations for Minimum Travel Time
Hard

You are given a straight road of length l km, an integer n, an integer k, and
two integer arrays, position and time, each of length n.

The array position lists the positions (in km) of signs in strictly increasing
order (with position[0] = 0 and position[n - 1] = l).

Each time[i] represents the time (in minutes) required to travel 1 km between
position[i] and position[i + 1].

You must perform exactly k merge operations. In one merge, you can choose any
two adjacent signs at indices i and i + 1 (with i > 0 and i + 1 < n) and:

Update the sign at index i + 1 so that its time becomes time[i] + time[i + 1].

Remove the sign at index i.

Return the minimum total travel time (in minutes) to travel from 0 to l after
exactly k merges.

Example 1:

Input: l = 10, n = 4, k = 1, position = [0,3,8,10], time = [5,8,3,6]

Output: 62

Explanation:

Merge the signs at indices 1 and 2. Remove the sign at index 1, and change the
time at index 2 to 8 + 3 = 11.

After the merge:

position array: [0, 8, 10]

time array: [5, 11, 6]

Segment | Distance (km) | Time per km (min) | Segment Travel Time (min)
--------+---------------+-------------------+--------------------------
0 -> 8  | 8             | 5                 | 8 x 5 = 40
8 -> 10 | 2             | 11                | 2 x 11 = 22

Total Travel Time: 40 + 22 = 62, which is the minimum possible time after
exactly 1 merge.

Example 2:

Input: l = 5, n = 5, k = 1, position = [0,1,2,3,5], time = [8,3,9,3,3]

Output: 34

Explanation:

Merge the signs at indices 1 and 2. Remove the sign at index 1, and change the
time at index 2 to 3 + 9 = 12.

After the merge:

position array: [0, 2, 3, 5]

time array: [8, 12, 3, 3]

Segment | Distance (km) | Time per km (min) | Segment Travel Time (min)
--------+---------------+-------------------+--------------------------
0 -> 2  | 2             | 8                 | 2 x 8 = 16
2 -> 3  | 1             | 12                | 1 x 12 = 12
3 -> 5  | 2             | 3                 | 2 x 3 = 6

Total Travel Time: 16 + 12 + 6 = 34, which is the minimum possible time after
exactly 1 merge.

Constraints:

1 <= l <= 10^5

2 <= n <= min(l + 1, 50)

0 <= k <= min(n - 2, 10)

position.length == n

position[0] = 0 and position[n - 1] = l

position is sorted in strictly increasing order.

time.length == n

1 <= time[i] <= 100

1 <= sum(time) <= 100

"""

# V0
# IDEA : DP OVER (PREVIOUS KEPT SIGN, CURRENT KEPT SIGN, MERGES USED)
#
#   a merge deletes sign i and pours its time into sign i + 1, and i is never 0
#   nor the last index -- so after k merges exactly k of the interior signs are
#   gone, and any k of them can be chosen.  the surviving signs
#   0 = i_0 < i_1 < ... < i_{m-1} = n - 1 fully describe the outcome.
#
#   when the block (a, b) is deleted between two survivors a and b, all of the
#   deleted times cascade rightwards into b, so b's time becomes
#   time[a+1] + ... + time[b].  that is the crucial detail: the rate charged on
#   the stretch position[b] -> position[c] depends not only on b but also on
#   the survivor a *before* it.
#
#   hence the state carries two indices.  dp[a][b][r] is the cheapest way to
#   have reached survivor b, with a as its predecessor and r signs already
#   merged away, and the step to the next survivor c costs
#       (position[c] - position[b]) * (prefix[b+1] - prefix[a+1])
#   while consuming c - b - 1 further merges.
#
# time = O(n^3 * k), space = O(n^2 * k)
class Solution(object):
    def minTravelTime(self, l, n, k, position, time):
        pre = [0] * (n + 1)
        for i in range(n):
            pre[i + 1] = pre[i] + time[i]

        INF = float("inf")
        # dp[a + 1][b][r] -- a = -1 means "b is the very first sign"
        dp = [[[INF] * (k + 1) for _ in range(n)] for _ in range(n + 1)]
        dp[0][0][0] = 0
        for b in range(n - 1):
            for a1 in range(b + 1):          # a + 1, so a1 = 0 means a = -1
                row = dp[a1][b]
                rate = pre[b + 1] - pre[a1]
                for r in range(k + 1):
                    cur = row[r]
                    if cur == INF:
                        continue
                    for c in range(b + 1, n):
                        nr = r + (c - b - 1)
                        if nr > k:
                            break
                        cand = cur + (position[c] - position[b]) * rate
                        if cand < dp[b + 1][c][nr]:
                            dp[b + 1][c][nr] = cand

        best = INF
        for a1 in range(n):
            v = dp[a1][n - 1][k]
            if v < best:
                best = v
        return best
