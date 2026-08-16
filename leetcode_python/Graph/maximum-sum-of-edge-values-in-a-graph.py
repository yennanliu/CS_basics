"""

3547. Maximum Sum of Edge Values in a Graph
Hard

You are given an undirected connected graph of n nodes, numbered from 0 to n -
1. Each node is connected to at most 2 other nodes.

The graph consists of m edges, represented by a 2D array edges, where edges[i] =
[a_i, b_i] indicates that there is an edge between nodes a_i and b_i.

You have to assign a unique value from 1 to n to each node. The value of an edge
will be the product of the values assigned to the two nodes it connects.

Your score is the sum of the values of all edges in the graph.

Return the maximum score you can achieve.

Example 1:

Input: n = 4, edges = [[0,1],[1,2],[2,3]]

Output: 23

Explanation:

The diagram above illustrates an optimal assignment of values to nodes. The sum
of the values of the edges is: (1 * 3) + (3 * 4) + (4 * 2) = 23.

Example 2:

Input: n = 6, edges = [[0,3],[4,5],[2,0],[1,3],[2,4],[1,5]]

Output: 82

Explanation:

The diagram above illustrates an optimal assignment of values to nodes. The sum
of the values of the edges is: (1 * 2) + (2 * 4) + (4 * 6) + (6 * 5) + (5 * 3) +
(3 * 1) = 82.

Constraints:

1 <= n <= 5 * 10^4

m == edges.length

1 <= m <= n

edges[i].length == 2

0 <= a_i, b_i < n

a_i != b_i

There are no repeated edges.

The graph is connected.

Each node is connected to at most 2 other nodes.

"""

# V0
# IDEA : THE GRAPH IS ONE PATH OR ONE CYCLE -- BUILD THE ZIGZAG AROUND n
#
#   every node has degree at most 2 and the graph is connected, so there is
#   nothing to explore: with n - 1 edges it is a single path, with n edges a
#   single cycle.  the labels do not care about node identity either, only
#   about the shape, so the whole problem is "arrange 1..n along a path (or a
#   cycle) to maximise the sum of adjacent products".
#
#   the big values must sit next to each other, because a value contributes
#   once per incident edge and is multiplied by its neighbours.  that leads to
#   the zigzag: put n in the middle, n-1 beside it, then keep hanging the next
#   largest value on whichever side currently ends in the smaller number --
#   i.e. alternate sides going outward, ... 5, 3, 1 | 2, 4, 6 ... with n and
#   n-1 in the centre.
#
#   the loop below realises exactly that order without ever materialising it:
#   `a` and `b` are the two current ends, and c is glued to the older of them,
#   which is the side that has waited one step longer.  a cycle only differs by
#   one extra edge joining the two outermost values, 1 and 2.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxScore(self, n, edges):
        is_cycle = len(edges) == n
        a = b = n
        total = 0
        for c in range(n - 1, 0, -1):
            total += a * c
            a, b = b, c
        if is_cycle:
            total += a * b
        return total
