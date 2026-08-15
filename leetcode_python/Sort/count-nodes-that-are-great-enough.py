"""

2792. Count Nodes That Are Great Enough
Hard

You are given a root to a binary tree and an integer k. A node of this tree is called great enough if the followings hold:

Its subtree has at least k nodes.
Its value is greater than the value of at least k nodes in its subtree.

Return the number of nodes in this tree that are great enough.

The node u is in the subtree of the node v, if u == v or v is an ancestor of u.


Example 1:

Input: root = [7,6,5,4,3,2,1], k = 2
Output: 3
Explanation: Number the nodes from 1 to 7.
The values in the subtree of node 1: {1,2,3,4,5,6,7}. Since node.val == 7, there are 6 nodes having a smaller value than its value. So it's great enough.
The values in the subtree of node 2: {3,4,6}. Since node.val == 6, there are 2 nodes having a smaller value than its value. So it's great enough.
The values in the subtree of node 3: {1,2,5}. Since node.val == 5, there are 2 nodes having a smaller value than its value. So it's great enough.
It can be shown that other nodes are not great enough.

Example 2:

Input: root = [1,2,3], k = 1
Output: 0
Explanation: Number the nodes from 1 to 3.
The values in the subtree of node 1: {1,2,3}. Since node.val == 1, there are no nodes having a smaller value than its value. So it's not great enough.
The values in the subtree of node 2: {2}. Since node.val == 2, there are no nodes having a smaller value than its value. So it's not great enough.
The values in the subtree of node 3: {3}. Since node.val == 3, there are no nodes having a smaller value than its value. So it's not great enough.

Example 3:

Input: root = [3,2,2], k = 2
Output: 1
Explanation: Number the nodes from 1 to 3.
The values in the subtree of node 1: {2,2,3}. Since node.val == 3, there are 2 nodes having a smaller value than its value. So it's great enough.
The values in the subtree of node 2: {2}. Since node.val == 2, there are no nodes having a smaller value than its value. So it's not great enough.
The values in the subtree of node 3: {2}. Since node.val == 2, there are no nodes having a smaller value than its value. So it's not great enough.


Constraints:

The number of nodes in the tree is in the range [1, 10^4].
1 <= Node.val <= 10^4
1 <= k <= 10

"""

# V0
# IDEA : POST-ORDER + BOUNDED MAX-HEAP OF THE k SMALLEST SUBTREE VALUES
#
#   a node is "great enough" iff at least k values in its subtree are
#   STRICTLY smaller than its own value. its own value never counts, so we
#   only ever need to look at its descendants.
#
#   to decide that, we do NOT need the whole subtree - only its k smallest
#   descendant values. if the k-th smallest is < node.val, then all k of
#   them are < node.val and the node qualifies.
#
#   so each subtree returns a bounded container of size <= k holding the k
#   smallest values seen so far. we merge child containers, test the node,
#   then insert the node's own value for the parent to use.
#
#   NOTE : the container must behave like a MAX-heap (we evict the biggest
#          to keep only the k smallest). python's heapq is a min-heap, so
#          we store NEGATED values : -h[0] is then the current k-th
#          smallest, i.e. the largest value we are keeping.
#
#   NOTE : k <= 10, so every heap operation is O(log k) ~ O(1).
#
#   NOTE : the tree can hold 10^4 nodes and may degenerate into a chain, so
#          a plain recursive dfs would blow python's recursion limit. we run
#          an explicit-stack post-order instead.
#
# time = O(n * k * log k), space = O(n * k)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
import heapq


class Solution(object):
    def countGreatEnoughNodes(self, root, k):
        if not root:
            return 0

        def push(heap, neg_val):
            # keep only the k smallest values (stored negated -> max-heap)
            heapq.heappush(heap, neg_val)
            if len(heap) > k:
                heapq.heappop(heap)

        res = 0
        # iterative post-order : visit both children before the node itself
        heaps = {}
        stack = [(root, False)]
        while stack:
            node, processed = stack.pop()
            if not processed:
                stack.append((node, True))
                if node.left:
                    stack.append((node.left, False))
                if node.right:
                    stack.append((node.right, False))
                continue

            cur = heaps.pop(id(node.left), []) if node.left else []
            right = heaps.pop(id(node.right), []) if node.right else []
            # merge the smaller heap into the bigger one
            if len(right) > len(cur):
                cur, right = right, cur
            for x in right:
                push(cur, x)

            # -cur[0] is the k-th smallest descendant value
            if len(cur) == k and -cur[0] < node.val:
                res += 1

            push(cur, -node.val)
            heaps[id(node)] = cur

        return res
