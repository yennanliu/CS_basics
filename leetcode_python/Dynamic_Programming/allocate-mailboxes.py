"""

1478. Allocate Mailboxes
Hard

Given the array houses where houses[i] is the location of the ith house along a street and an integer k, allocate k mailboxes in the street.

Return the minimum total distance between each house and its nearest mailbox.

The test cases are generated so that the answer fits in a 32-bit integer.


Example 1:

Input: houses = [1,4,8,10,20], k = 3
Output: 5
Explanation: Allocate mailboxes in position 3, 9 and 20.
Minimum total distance from each houses to nearest mailboxes is |3-1| + |4-3| + |9-8| + |10-9| + |20-20| = 5

Example 2:

Input: houses = [2,3,5,12,18], k = 2
Output: 9
Explanation: Allocate mailboxes in position 3 and 14.
Minimum total distance from each houses to nearest mailboxes is |2-3| + |3-3| + |5-3| + |12-14| + |18-14| = 9.


Constraints:

1 <= k <= houses.length <= 100
1 <= houses[i] <= 10^4
All the integers of houses are unique.

"""

# V0
# IDEA : INTERVAL COST + PARTITION DP
#
#   after sorting, each mailbox serves a CONTIGUOUS block of houses, so the
#   task is "cut the sorted array into k blocks, minimise total cost".
#   cost of one block = put the mailbox at its median; pairing the outermost
#   houses gives the closed form
#     g[i][j] = g[i+1][j-1] + houses[j] - houses[i]
#   (the median cancels out, only the pairwise spans remain).
#   f[i][j] = min cost for the first i+1 houses using j mailboxes
#           = min over split p of f[p][j-1] + g[p+1][i].
#   NOTE : sorting first is required - the input is not ordered.
#
# time = O(n^2 * k), space = O(n^2)
class Solution(object):
    def minDistance(self, houses, k):
        houses.sort()
        n = len(houses)
        INF = float('inf')

        # g[i][j] = cost of serving houses i..j with ONE mailbox
        g = [[0] * n for _ in range(n)]
        for i in range(n - 2, -1, -1):
            for j in range(i + 1, n):
                g[i][j] = g[i + 1][j - 1] + houses[j] - houses[i]

        f = [[INF] * (k + 1) for _ in range(n)]
        for i in range(n):
            f[i][1] = g[0][i]
            for j in range(2, min(k, i + 1) + 1):
                for p in range(i):
                    if f[p][j - 1] + g[p + 1][i] < f[i][j]:
                        f[i][j] = f[p][j - 1] + g[p + 1][i]

        return f[n - 1][k]
