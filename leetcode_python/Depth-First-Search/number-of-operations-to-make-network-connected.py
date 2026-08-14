"""

1319. Number of Operations to Make Network Connected
Medium

There are n computers numbered from 0 to n - 1 connected by ethernet cables connections forming
a network where connections[i] = [ai, bi] represents a connection between computers ai and bi.
Any computer can reach any other computer directly or indirectly through the network.

You are given an initial computer network connections. You can extract certain cables between two
directly connected computers, and place them between any pair of disconnected computers to make
them directly connected.

Return the minimum number of times you need to do this in order to make all the computers connected.
If it is not possible, return -1.


Example 1:

Input: n = 4, connections = [[0,1],[0,2],[1,2]]
Output: 1
Explanation: Remove cable between computer 1 and 2 and place between computers 1 and 3.

Example 2:

Input: n = 6, connections = [[0,1],[0,2],[0,3],[1,2],[1,3]]
Output: 2

Example 3:

Input: n = 6, connections = [[0,1],[0,2],[0,3],[1,2]]
Output: -1
Explanation: There are not enough cables.


Constraints:

1 <= n <= 10^5
1 <= connections.length <= min(n * (n - 1) / 2, 10^5)
connections[i].length == 2
0 <= ai, bi < n
ai != bi
There are no repeated connections.
No two computers are connected by more than one cable.

"""

# V0
# IDEA : UNION FIND (count components; every extra cable can join two of them)
#
#   connecting k components into one always needs exactly k - 1 cables.
#   a cable is "spare" when its two endpoints are ALREADY in the same
#   component -- unplugging it does not break anything.
#
#   so : run union-find over the connections,
#        components = number of distinct roots,
#        spare      = edges that closed a cycle.
#   answer = components - 1 if spare >= components - 1 else -1.
#
#   NOTE : with fewer than n - 1 cables in total the network can never be
#          connected -- that case is exactly the `spare < components - 1` test.
#
# time = O((n + m) * a(n)), space = O(n)   a = inverse Ackermann
class Solution(object):
    def makeConnected(self, n, connections):
        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        components = n
        spare = 0
        for a, b in connections:
            ra, rb = find(a), find(b)
            if ra == rb:
                spare += 1
            else:
                parent[ra] = rb
                components -= 1

        need = components - 1
        return need if spare >= need else -1
