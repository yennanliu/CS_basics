"""

2829. Determine the Minimum Sum of a k-avoiding Array
Medium

You are given two integers, n and k.

An array of distinct positive integers is called a k-avoiding array if there does not exist any pair of distinct elements that sum to k.

Return the minimum possible sum of a k-avoiding array of length n.


Example 1:

Input: n = 5, k = 4
Output: 18
Explanation: Consider the k-avoiding array [1,2,4,5,6], which has a sum of 18.
It can be proven that there is no k-avoiding array with a sum less than 18.

Example 2:

Input: n = 2, k = 6
Output: 3
Explanation: We can construct the array [1,2], which has a sum of 3.
It can be proven that there is no k-avoiding array with a sum less than 3.


Constraints:

1 <= n, k <= 50

"""

# V0
# IDEA : GREEDY (take the smallest still-legal integer each time)
#
#   Scan i = 1, 2, 3, ... and take i whenever it is not banned. Taking i bans
#   its partner k - i (that pair is the only way i can ever create the sum k).
#
#   NOTE : greedy is optimal because the integers pair up as
#          {1, k-1}, {2, k-2}, ... — at most one member of each pair may be
#          used, and the banned partner k - i is always LARGER than the i we
#          took (we meet i first), so we never trade a small number away for
#          a bigger one.
#
#   NOTE : i == k - i (i.e. k even, i = k/2) is not a conflict: the pair must
#          use two DISTINCT elements and all elements are distinct, so k/2
#          alone is fine. Marking k - i == i as banned after we already took
#          i is harmless since i is never revisited.
#
#   NOTE : once i passes k every later integer is free, so the loop always
#          terminates after at most n + k steps.
#
# time = O(n + k), space = O(n + k)
class Solution(object):
    def minimumSum(self, n, k):
        banned = set()
        total = 0
        i = 1
        for _ in range(n):
            while i in banned:
                i += 1
            total += i
            banned.add(k - i)
            i += 1
        return total


# V0-1
# IDEA : CLOSED FORM (two arithmetic series, no loop at all)
#
#   the greedy answer always has the same shape, so it can be summed directly:
#     - 1, 2, ..., k//2 are ALL safe together (the largest pair among them sums
#       to at most k - 1), so take as many of them as we still need
#     - every number from k//2 + 1 to k - 1 is the banned partner of one of
#       those, so nothing in that band is usable
#     - from k upward everything is free (a pair summing to k needs both
#       members below k)
#
#   head = min(n, k // 2) numbers -> 1 + 2 + ... + head
#   tail = n - head numbers       -> k + (k + 1) + ... + (k + tail - 1)
#
# time = O(1), space = O(1)
class Solution(object):
    def minimumSum(self, n, k):
        head = min(n, k // 2)
        total = head * (head + 1) // 2
        tail = n - head
        if tail > 0:
            total += tail * k + tail * (tail - 1) // 2
        return total


# V0-2
# IDEA : DP / EXACT-COUNT KNAPSACK OVER THE CONFLICT GROUPS
#
#   this one proves nothing about greedy -- it just lets a DP choose, which is
#   the approach to fall back on if the greedy exchange argument is not obvious
#   under interview pressure.
#
#   only the numbers 1 .. n + k can ever be needed (that window already holds
#   n usable numbers in the worst case). partition that window into groups:
#     - {i, k - i} when both sides exist and differ -> at most ONE may be taken
#     - anything else (i >= k, or i == k - i)       -> a free singleton
#   the task is then "take exactly n items, at most one per group, minimum
#   sum", a tiny knapsack over dp[c] = cheapest way to have taken c numbers.
#
# time = O((n + k) * n), space = O(n + k)
class Solution(object):
    def minimumSum(self, n, k):
        limit = n + k
        taken = [False] * (limit + 2)
        groups = []
        for i in range(1, limit + 1):
            if taken[i]:
                continue
            j = k - i
            taken[i] = True
            if 1 <= j <= limit and j != i:
                taken[j] = True
                groups.append((i, j))
            else:
                groups.append((i,))

        INF = float("inf")
        dp = [INF] * (n + 1)
        dp[0] = 0
        for g in groups:
            nxt = dp[:]
            for c in range(n):
                if dp[c] == INF:
                    continue
                for v in g:
                    if dp[c] + v < nxt[c + 1]:
                        nxt[c + 1] = dp[c] + v
            dp = nxt
        return dp[n]
