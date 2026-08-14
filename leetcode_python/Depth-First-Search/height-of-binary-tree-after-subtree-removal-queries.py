"""

2458. Height of Binary Tree After Subtree Removal Queries
Hard

You are given the root of a binary tree with n nodes. Each node is assigned a unique value from 1 to n. You are also given an array queries of size m.

You have to perform m independent queries on the tree where in the ith query you do the following:

Remove the subtree rooted at the node with the value queries[i] from the tree. It is guaranteed that queries[i] will not be equal to the value of the root.

Return an array answer of size m where answer[i] is the height of the tree after performing the ith query.

Note:

The queries are independent, so the tree returns to its initial state after each query.
The height of a tree is the number of edges in the longest simple path from the root to some node in the tree.


Example 1:

Input: root = [1,3,4,2,null,6,5,null,null,null,null,null,7], queries = [4]
Output: [2]
Explanation: The diagram above shows the tree after removing the subtree rooted at node with value 4.
The height of the tree is 2 (The path 1 -> 3 -> 2).

Example 2:

Input: root = [5,8,9,2,1,3,7,4,6], queries = [3,2,4,8]
Output: [3,2,3,2]
Explanation: We have the following queries:
- Removing the subtree rooted at node with value 3. The height of the tree becomes 3 (The path 5 -> 8 -> 2 -> 4).
- Removing the subtree rooted at node with value 2. The height of the tree becomes 2 (The path 5 -> 8 -> 1).
- Removing the subtree rooted at node with value 4. The height of the tree becomes 3 (The path 5 -> 8 -> 2 -> 6).
- Removing the subtree rooted at node with value 8. The height of the tree becomes 2 (The path 5 -> 9 -> 3).


Constraints:

The number of nodes in the tree is n.
2 <= n <= 10^5
1 <= Node.val <= n
All the values in the tree are unique.
m == queries.length
1 <= m <= min(n, 10^4)
1 <= queries[i] <= n
queries[i] != root.val

"""

# V0
# IDEA : PRECOMPUTE, FOR EVERY NODE, THE TREE HEIGHT WITHOUT ITS SUBTREE
#
#   the queries are independent and there can be 10^4 of them, so recomputing
#   the height per query is too slow. instead answer ALL nodes in two passes.
#
#   pass 1 : height[node] = edges down to its deepest descendant.
#   pass 2 : walk down carrying `rest` = the deepest depth reachable WITHOUT
#            entering the current node's subtree. descending into a child,
#            `rest` picks up the sibling's branch :
#                rest_child = max(rest, depth + 1 + height[sibling])
#            and the answer for a node is exactly the `rest` it was handed.
#
#   both passes are ITERATIVE — n reaches 10^5 and the tree may be a path.
#
# time = O(n + m), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def treeQueries(self, root, queries):
        # pass 1 : subtree heights, computed in reverse preorder
        order = []
        stack = [root]
        while stack:
            node = stack.pop()
            order.append(node)
            if node.left:
                stack.append(node.left)
            if node.right:
                stack.append(node.right)

        height = {}
        for node in reversed(order):
            h = 0
            if node.left:
                h = max(h, height[node.left] + 1)
            if node.right:
                h = max(h, height[node.right] + 1)
            height[node] = h

        # pass 2 : the best depth reachable while avoiding each subtree
        answer = {}
        stack = [(root, 0, 0)]
        while stack:
            node, depth, rest = stack.pop()
            answer[node.val] = rest
            # deleting a child's subtree still leaves this node (at `depth`)
            # and whatever the sibling branch reaches
            base = max(rest, depth)
            if node.left:
                r = base
                if node.right:
                    r = max(r, depth + 1 + height[node.right])
                stack.append((node.left, depth + 1, r))
            if node.right:
                r = base
                if node.left:
                    r = max(r, depth + 1 + height[node.left])
                stack.append((node.right, depth + 1, r))

        return [answer[q] for q in queries]
