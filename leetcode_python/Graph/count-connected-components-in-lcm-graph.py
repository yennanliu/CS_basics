"""

3378. Count Connected Components in LCM Graph
Hard

You are given an array of integers nums of size n and a positive integer threshold.

There is a graph consisting of n nodes with the ith node having a value of nums[i]. Two nodes i and j in the graph are connected via an undirected edge if lcm(nums[i], nums[j]) <= threshold.

Return the number of connected components in this graph.

A connected component is a subgraph of a graph in which there exists a path between any two vertices, and no vertex of the subgraph shares an edge with a vertex outside of the subgraph.

The term lcm(a, b) denotes the least common multiple of a and b.


Example 1:

Input: nums = [2,4,8,3,9], threshold = 5
Output: 4
Explanation:
The four connected components are (2, 4), (3), (8), (9).

Example 2:

Input: nums = [2,4,8,3,9,12], threshold = 10
Output: 2
Explanation:
The two connected components are (2, 3, 4, 8, 9), and (12).


Constraints:

1 <= nums.length <= 10^5
All elements of nums are unique.
1 <= nums[i] <= 10^9
1 <= threshold <= 2 * 10^5

"""

# V0
# IDEA : GROUP BY COMMON MULTIPLE — EVERY DIVISOR OF L IS CONNECTED THROUGH L
#
#   a value above the threshold can never pair with anything (even with
#   itself the lcm exceeds it), so each of those is its own component.
#
#   for the rest, note that if a and b both divide some L <= threshold then
#   lcm(a, b) divides L and is therefore <= threshold — they are connected.
#   so sweeping L from 1 to threshold and unioning all the input values that
#   divide L captures every edge, transitively.
#
#   enumerating "the values dividing L" the other way round — for each value
#   v, walk its multiples v, 2v, 3v, ... up to the threshold — costs the
#   harmonic sum, about threshold * ln(threshold) steps. keeping one
#   representative per multiple avoids storing the buckets.
#
# time = O(threshold log threshold * alpha), space = O(threshold + n)
class Solution(object):
    def countComponents(self, nums, threshold):
        small = [v for v in nums if v <= threshold]
        big = len(nums) - len(small)
        if not small:
            return big

        idx = {v: i for i, v in enumerate(small)}
        parent = list(range(len(small)))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb

        rep = [-1] * (threshold + 1)            # a value dividing this multiple
        for v in small:
            i = idx[v]
            for L in range(v, threshold + 1, v):
                if rep[L] == -1:
                    rep[L] = i
                else:
                    union(i, rep[L])

        roots = set(find(i) for i in range(len(small)))
        return len(roots) + big
