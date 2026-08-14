"""

2612. Minimum Reverse Operations
Hard

You are given an integer n and an integer p representing an array arr of length n where all elements are set to 0's, except position p which is set to 1. You are also given an integer array banned containing restricted positions. Perform the following operation on arr:

Reverse a subarray with size k if the single 1 is not set to a position in banned.

Return an integer array answer with n results where the ith result is the minimum number of operations needed to bring the single 1 to position i in arr, or -1 if it is impossible.


Example 1:

Input: n = 4, p = 0, banned = [1,2], k = 4

Output: [0,-1,-1,1]

Explanation:

Initially 1 is placed at position 0 so the number of operations we need for position 0 is 0.
We can never place 1 on the banned positions, so the answer for positions 1 and 2 is -1.
Perform the operation of size 4 to reverse the whole array.
After a single operation 1 is at position 3 so the answer for position 3 is 1.

Example 2:

Input: n = 5, p = 0, banned = [2,4], k = 3

Output: [0,-1,-1,-1,-1]

Explanation:

Initially 1 is placed at position 0 so the number of operations we need for position 0 is 0.
We cannot perform the operation on the subarray positions [0, 2] because position 2 is in banned.
Because 1 cannot be set at position 2, it is impossible to set 1 at other positions in more operations.

Example 3:

Input: n = 4, p = 2, banned = [0,1,3], k = 1

Output: [-1,-1,0,-1]

Explanation:

Perform operations of size 1 and 1 never changes its position.


Constraints:

1 <= n <= 10^5
0 <= p <= n - 1
0 <= banned.length <= n - 1
0 <= banned[i] <= n - 1
1 <= k <= n
banned[i] != p
all values in banned are unique

"""

# V0
# IDEA : BFS over an INTERVAL of same-parity targets + UNION-FIND "next unvisited"
#
#   ONE MOVE.  if the 1 sits at i and we reverse the window [L, L + k - 1]
#   (so L <= i <= L + k - 1), the 1 lands at
#       j = L + (L + k - 1 - i) = 2*L + k - 1 - i
#   L ranges over [max(0, i - k + 1), min(i, n - k)], hence j sweeps an
#   ARITHMETIC PROGRESSION with step 2 :
#       lo = max(i - k + 1, k - 1 - i)      (plug in the two ends of L)
#       hi = min(i + k - 1, 2*n - k - 1 - i)
#   i.e. every j in [lo, hi] whose parity equals lo's parity is reachable
#   in ONE operation. (parity is fixed because j and i differ by 2*L + k - 1 - 2*i.)
#
#   BFS.  answer[i] = shortest number of operations = BFS layer of i.
#
#   THE CATCH.  a plain BFS would rescan the whole [lo, hi] range every time
#   and blow up to O(n^2) for n = 10^5. since a position is enqueued at most
#   once, we keep the still-unvisited positions in TWO ordered sets (even /
#   odd) and only ever walk the ones that are still there.
#
#   NOTE : the "ordered set" is a union-find `nxt[]` where nxt[x] points to
#          the first unvisited position >= x within x's parity class.
#          deleting x is just `nxt[x] = x + 2`, so each position is popped
#          once overall -> near O(n) total.
#
#   NOTE : banned positions (and the start p) are deleted UP FRONT, so they
#          are simply never handed out by the union-find and keep answer -1.
#
#   NOTE : k == 1 gives lo == hi == i, so nothing new is ever reached — the
#          formula already handles example 3 without a special case.
#
# time = O((n + len(banned)) * alpha(n)), space = O(n)
class Solution(object):
    def minReverseOperations(self, n, p, banned, k):
        # nxt[x] = first not-yet-visited position >= x with the same parity as x
        nxt = list(range(n + 4))

        def find(x):
            root = x
            while nxt[root] != root:
                root = nxt[root]
            while nxt[x] != root:      # path compression (iterative, no recursion)
                nxt[x], x = root, nxt[x]
            return root

        ans = [-1] * n
        ans[p] = 0
        nxt[p] = p + 2                 # start is consumed
        for b in banned:
            nxt[b] = b + 2             # banned positions are never reachable

        layer = [p]
        step = 0
        while layer:
            step += 1
            nxt_layer = []
            for i in layer:
                lo = max(i - k + 1, k - 1 - i)
                hi = min(i + k - 1, 2 * n - k - 1 - i)
                j = find(lo)
                while j <= hi:
                    ans[j] = step
                    nxt[j] = j + 2
                    nxt_layer.append(j)
                    j = find(j + 2)
            layer = nxt_layer
        return ans
