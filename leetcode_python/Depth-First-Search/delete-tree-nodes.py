"""

1273. Delete Tree Nodes
Medium

A tree rooted at node 0 is given as follows:

The number of nodes is nodes;
The value of the ith node is value[i];
The parent of the ith node is parent[i].

Remove every subtree whose sum of values of nodes is zero.

Return the number of the remaining nodes in the tree.


Example 1:

Input: nodes = 7, parent = [-1,0,0,1,2,2,2], value = [1,-2,4,0,-2,-1,-1]
Output: 2

Example 2:

Input: nodes = 7, parent = [-1,0,0,1,2,2,2], value = [1,-2,4,0,-2,-1,-2]
Output: 6


Constraints:

1 <= nodes <= 10^4
parent.length == nodes
0 <= parent[i] <= nodes - 1
parent[0] == -1 which indicates that 0 is the root.
value.length == nodes
-10^5 <= value[i] <= 10^5
The given input is guaranteed to represent a valid tree.

"""

# V0
# IDEA : POST ORDER DFS (iterative, so a 10^4 deep chain is safe)
#        for every node compute (subtree sum, subtree node count);
#        if the subtree sum is 0, the whole subtree is deleted -> count = 0
# time = O(n)
# space = O(n)
class Solution(object):
    def deleteTreeNodes(self, nodes, parent, value):
        children = [[] for _ in range(nodes)]
        for i in range(1, nodes):
            children[parent[i]].append(i)

        subtree_sum = [0] * nodes
        subtree_cnt = [0] * nodes

        # iterative post order : push (node, visited_flag)
        stack = [(0, False)]
        while stack:
            node, visited = stack.pop()
            if not visited:
                stack.append((node, True))
                for c in children[node]:
                    stack.append((c, False))
            else:
                s = value[node]
                cnt = 1
                for c in children[node]:
                    s += subtree_sum[c]
                    cnt += subtree_cnt[c]
                subtree_sum[node] = s
                # NOTE : a deleted child contributes 0 nodes (subtree_cnt[c] == 0)
                subtree_cnt[node] = 0 if s == 0 else cnt

        return subtree_cnt[0]
