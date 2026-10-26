"""

3319. K-th Largest Perfect Subtree Size in Binary Tree
Medium

You are given the root of a binary tree and an integer k.

Return an integer denoting the size of the kth largest perfect binary subtree,
or -1 if it doesn't exist.

A perfect binary tree is a tree where all leaves are on the same level, and
every parent has two children.

Example 1:

Input: root = [5,3,6,5,2,5,7,1,8,null,null,6,8], k = 2

Output: 3

Explanation:

The roots of the perfect binary subtrees are highlighted in black. Their sizes,
in non-increasing order are [3, 3, 1, 1, 1, 1, 1, 1].
The 2nd largest size is 3.

Example 2:

Input: root = [1,2,3,4,5,6,7], k = 1

Output: 7

Explanation:

The sizes of the perfect binary subtrees in non-increasing order are
[7, 3, 3, 1, 1, 1, 1]. The size of the largest perfect binary subtree is 7.

Example 3:

Input: root = [1,2,3,null,4], k = 3

Output: -1

Explanation:

The sizes of the perfect binary subtrees in non-increasing order are [1, 1].
There are fewer than 3 perfect binary subtrees.

Constraints:

The number of nodes in the tree is in the range [1, 2000].
1 <= Node.val <= 2000
1 <= k <= 1024

"""

# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right

# V0
# IDEA : DFS (POST ORDER) -> COLLECT EVERY PERFECT SUBTREE SIZE -> SORT DESC
#
#   A subtree rooted at `node` is perfect exactly when
#     1) its left subtree is perfect,
#     2) its right subtree is perfect,
#     3) both have the SAME height (so all leaves sit on one level).
#   An empty subtree counts as perfect with height 0, which makes a leaf
#   perfect (height 1, size 1) with no special case.
#
#   Post order (left -> right -> root) is required: a node can only be judged
#   after both children have reported back.
#
#   e.g. [1,2,3,null,4]: node 4 -> perfect(1); node 3 -> perfect(1);
#        node 2 -> left h=0, right h=1 -> NOT perfect; node 1 -> NOT perfect
#        -> sizes [1, 1] -> k = 3 -> -1
#
# time = O(n log n), space = O(n)
class Solution(object):
    def kthLargestPerfectSubtree(self, root, k):
        """
        :type root: Optional[TreeNode]
        :type k: int
        :rtype: int
        """
        perfect_sizes = []
        self._dfs(root, perfect_sizes)

        # fewer than k perfect subtrees -> no k-th largest
        if len(perfect_sizes) < k:
            return -1

        perfect_sizes.sort(reverse=True)
        return perfect_sizes[k - 1]

    def _dfs(self, node, perfect_sizes):
        """Return (height, size, is_perfect) for the subtree at node."""
        # empty subtree: perfect, height 0, size 0
        if node is None:
            return 0, 0, True

        left_height, left_size, left_perfect = self._dfs(node.left, perfect_sizes)
        right_height, right_size, right_perfect = self._dfs(node.right, perfect_sizes)

        # NOTE !!! equal heights is what forces every leaf onto the same level
        is_perfect = left_perfect and right_perfect and left_height == right_height

        size = left_size + right_size + 1
        height = max(left_height, right_height) + 1

        if is_perfect:
            perfect_sizes.append(size)

        return height, size, is_perfect


# V0-1
# IDEA : DFS (POST ORDER) + MIN HEAP OF SIZE k
#
#   Same DFS as V0, but keep only the k largest sizes seen so far in a min heap.
#   The heap's top is then the k-th largest; anything smaller is popped at once.
#   Worth it when k is much smaller than n (here k <= 1024, n <= 2000).
#
# time = O(n log k), space = O(n) (recursion stack O(h) + heap O(k))
import heapq


class Solution2(object):
    def kthLargestPerfectSubtree(self, root, k):
        """
        :type root: Optional[TreeNode]
        :type k: int
        :rtype: int
        """
        min_heap = []
        self._dfs(root, k, min_heap)

        # heap never filled -> fewer than k perfect subtrees
        if len(min_heap) < k:
            return -1
        return min_heap[0]

    def _dfs(self, node, k, min_heap):
        """Return (height, size, is_perfect) for the subtree at node."""
        if node is None:
            return 0, 0, True

        left_height, left_size, left_perfect = self._dfs(node.left, k, min_heap)
        right_height, right_size, right_perfect = self._dfs(node.right, k, min_heap)

        is_perfect = left_perfect and right_perfect and left_height == right_height
        size = left_size + right_size + 1
        height = max(left_height, right_height) + 1

        if is_perfect:
            heapq.heappush(min_heap, size)
            # keep only the k largest sizes: drop the smallest
            if len(min_heap) > k:
                heapq.heappop(min_heap)

        return height, size, is_perfect
