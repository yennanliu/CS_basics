"""

2467. Most Profitable Path in a Tree
Medium

There is an undirected tree with n nodes labeled from 0 to n - 1, rooted at node 0. You are given a 2D integer array edges of length n - 1 where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree.

At every node i, there is a gate. You are also given an array of even integers amount, where amount[i] represents:

the price needed to open the gate at node i, if amount[i] is negative, or,
the cash reward obtained on opening the gate at node i, otherwise.

The game goes on as follows:

Initially, Alice is at node 0 and Bob is at node bob.
At every second, Alice and Bob each move to an adjacent node. Alice moves towards some leaf node, while Bob moves towards node 0.
For every node along their path, Alice and Bob either spend money to open the gate at that node, or accept the reward. Note that:
If the gate is already open, no price will be required, nor will there be any cash reward.
If Alice and Bob reach the node simultaneously, they share the price/reward for opening the gate there. In other words, if the price to open the gate is c, then both Alice and Bob pay c / 2 each. Similarly, if the reward at the node is c, both of them receive c / 2 each.
If Alice reaches a leaf node, she stops moving. Similarly, if Bob reaches node 0, he stops moving. Note that these events are independent of each other.

Return the maximum net income Alice can have if she travels towards the optimal leaf node.


Example 1:

Input: edges = [[0,1],[1,2],[1,3],[3,4]], bob = 3, amount = [-2,4,2,-4,6]
Output: 6
Explanation:
The above diagram represents the given tree. The game goes as follows:
- Alice is initially on node 0, Bob on node 3. They open the gates of their respective nodes.
  Alice's net income is now -2.
- Both Alice and Bob move to node 1.
  Since they reach here simultaneously, they open the gate together and share the reward.
  Alice's net income becomes -2 + (4 / 2) = 0.
- Alice moves on to node 3. Since Bob already opened its gate, Alice's income remains unchanged.
  Bob moves on to node 0, and stops moving.
- Alice moves on to node 4 and opens the gate there. Her net income becomes 0 + 6 = 6.
Now, neither Alice nor Bob can make any further moves, and the game ends.
It is not possible for Alice to get a higher net income.

Example 2:

Input: edges = [[0,1]], bob = 1, amount = [-7280,2350]
Output: -7280
Explanation:
Alice follows the path 0->1 whereas Bob follows the path 1->0.
Thus, Alice opens the gate at node 0 only. Hence, her net income is -7280.


Constraints:

2 <= n <= 10^5
edges.length == n - 1
edges[i].length == 2
0 <= ai, bi < n
ai != bi
edges represents a valid tree.
bob != 0
0 <= bob < n
amount.length == n
amount[i] is an even integer in the range [-10^4, 10^4].

"""

# V0
# IDEA : BOB'S ROUTE IS FIXED — TIME-STAMP IT, THEN LET ALICE PICK A LEAF
#
#   Bob has no choice : the tree path from `bob` up to node 0 is unique, so
#   record bob_time[v] = the second at which he opens gate v.
#
#   Alice's walk down from the root at depth t then meets three cases at v :
#       Bob never passes v, or arrives later  -> Alice takes the full amount
#       they arrive at the same second        -> Alice takes half
#       Bob got there first                   -> the gate is already open, 0
#   (amounts are guaranteed EVEN, so the halving is exact.)
#
#   the answer is the best running total over all leaves.
#
#   NOTE : both traversals are ITERATIVE, since n reaches 10^5.
#
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def mostProfitablePath(self, edges, bob, amount):
        n = len(amount)
        g = defaultdict(list)
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        # parents from the root, so Bob's upward path can be replayed
        parent = [-1] * n
        seen = [False] * n
        seen[0] = True
        stack = [0]
        while stack:
            u = stack.pop()
            for v in g[u]:
                if not seen[v]:
                    seen[v] = True
                    parent[v] = u
                    stack.append(v)

        bob_time = {}
        t = 0
        node = bob
        while node != -1:
            bob_time[node] = t
            t += 1
            node = parent[node]

        res = float('-inf')
        stack = [(0, -1, 0, 0)]          # node, parent, depth, income so far
        while stack:
            u, par, depth, income = stack.pop()

            bt = bob_time.get(u)
            if bt is None or bt > depth:
                income += amount[u]
            elif bt == depth:
                income += amount[u] // 2

            children = [v for v in g[u] if v != par]
            if not children:
                res = max(res, income)
            for v in children:
                stack.append((v, u, depth + 1, income))
        return res
