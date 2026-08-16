"""

3656. Determine if a Simple Graph Exists
Medium

You are given an integer array degrees, where degrees[i] represents the
desired degree of the ith vertex.

Your task is to determine if there exists an undirected simple graph with
exactly these vertex degrees.

A simple graph has no self-loops or parallel edges between the same pair of
vertices.

Return true if such a graph exists, otherwise return false.


Example 1:

Input: degrees = [3,1,2,2]
Output: true
Explanation:
One possible undirected simple graph is:
Edges: (0, 1), (0, 2), (0, 3), (2, 3)
Degrees: deg(0) = 3, deg(1) = 1, deg(2) = 2, deg(3) = 2.

Example 2:

Input: degrees = [1,3,3,1]
Output: false
Explanation:
degrees[1] = 3 and degrees[2] = 3 means they must be connected to all other
vertices.
This requires degrees[0] and degrees[3] to be at least 2, but both are equal
to 1, which contradicts the requirement.
Thus, the answer is false.


Constraints:

1 <= n == degrees.length <= 10^5
0 <= degrees[i] <= n - 1

"""

# V0
# IDEA : ERDOS-GALLAI THEOREM
#
#   a non-increasing sequence d1 >= d2 >= ... >= dn is graphical exactly
#   when the sum is even and, for every k,
#
#       d1 + ... + dk  <=  k*(k-1) + sum_{i>k} min(d_i, k).
#
#   the inequality just counts where the k hungriest vertices can put their
#   edge endpoints: at most k*(k-1) of them land inside the group (that is
#   2 * C(k,2), each of the C(k,2) internal edges consuming two), and a
#   vertex outside can absorb at most min(d_i, k) of them -- capped by its
#   own degree, and capped by k because it may attach to each of the k
#   vertices only once. the theorem says these obvious necessary counts are
#   also sufficient.
#
#   evaluating the right-hand side naively is O(n) per k. sorting
#   descending makes the tail split cleanly: among i > k the elements with
#   d_i >= k form a prefix of the tail (say up to index j), each
#   contributing k, and the rest contribute their own value, which a suffix
#   sum gives in O(1). j is found from a "how many degrees are >= k"
#   counting table rather than a binary search.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def simpleGraphExists(self, degrees):
        n = len(degrees)
        if sum(degrees) % 2:
            return False

        d = sorted(degrees, reverse=True)
        pre = [0] * (n + 1)
        for i in range(n):
            pre[i + 1] = pre[i] + d[i]

        # ge[k] = how many degrees are >= k  (degrees live in [0, n-1])
        cnt = [0] * (n + 2)
        for x in d:
            cnt[x] += 1
        ge = [0] * (n + 2)
        for v in range(n, -1, -1):
            ge[v] = ge[v + 1] + cnt[v]

        for k in range(1, n + 1):
            j = ge[k] if ge[k] > k else k     # tail elements >= k end at j
            rhs = k * (k - 1) + (j - k) * k + (pre[n] - pre[j])
            if pre[k] > rhs:
                return False
        return True
