"""

230. Kth Smallest Element in a BST
Medium

Given the root of a binary search tree, and an integer k, return the kth smallest value (1-indexed) of all the values of the nodes in the tree.

 
Example 1:


Input: root = [3,1,4,null,2], k = 1
Output: 1
Example 2:


Input: root = [5,3,6,2,4,null,null,1], k = 3
Output: 3
 

Constraints:

The number of nodes in the tree is n.
1 <= k <= n <= 104
0 <= Node.val <= 104
 

Follow up: If the BST is modified often (i.e., we can do insert and delete operations) and you need to find the kth smallest frequently, how would you optimize?

"""

# V0
# IDEA : DFS
# -> pre order traversal BST, then sort it and get the k (from 1) smallest element
class Solution(object):
    def kthSmallest(self, root, k):

        # pre order traversal
        def help(root):
            _list.append(root.val)
            if root.left:
                help(root.left)
            if root.right:
                help(root.right)

        if not root:
            return None
        _list = []
        help(root)
        _list.sort()
        return _list[k-1]

# V0' 
class Solution(object):
    def kthSmallest(self, root, k):
        self.k = k
        self.res = None
        self.dfs(root)
        return self.res

    def dfs(self, node):
        if not node:
            return
        self.dfs(node.left)
        self.k -= 1
        if self.k == 0:
            self.res = node.val
            return
        self.dfs(node.right)

# V1
# IDEA : Approach 1: Recursive Inorder Traversal
#        -> the property of BST : inorder traversal of BST is an array sorted in the ascending order.
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
class Solution:
    def kthSmallest(self, root, k):
        def inorder(r):
            return inorder(r.left) + [r.val] + inorder(r.right) if r else []
    
        return inorder(root)[k - 1]

# V1'
# IDEA : Approach 2: Iterative Inorder Traversal
#        -> the property of BST : inorder traversal of BST is an array sorted in the ascending order.
#        -> The above recursion could be converted into iteration, with the help of stack. This way one could speed up the solution because there is no need to build the entire inorder traversal, and one could stop after the kth element.
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
class Solution:
    def kthSmallest(self, root, k):
        stack = []
        
        while True:
            # Inorder Traversal
            ### get all `left sub tree` first
            while root:
                stack.append(root)
                root = root.left
            # then pop each of the collected root
            root = stack.pop()
            k -= 1
            # means we found the k-th smallest element
            if not k:
                return root.val
            # if not yet fount it, keep Inorder Traversal
            root = root.right

# V1''
# http://bookshadow.com/weblog/2015/07/02/leetcode-kth-smallest-element-bst/
# IDEA : using inorder survey through whole BST (via BFS)
class Solution:
    def kthSmallest(self, root, k):
        stack = []
        node = root
        while node:
            stack.append(node)
            node = node.left
        x = 1
        while stack and x <= k:
            node = stack.pop()
            x += 1
            right = node.right
            while right:
                stack.append(right)
                right = right.left
        return node.val

### Test case
# dev

# V1'
# IDEA : Recursive 
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63829/Python-Easy-Iterative-and-Recursive-Solution
class Solution(object):
    def kthSmallest(self, root, k):
        self.k = k
        self.res = None
        self.dfs(root)
        return self.res

    def dfs(self, node):
        if not node:
            return
        self.dfs(node.left)
        self.k -= 1
        if self.k == 0:
            self.res = node.val
            return
        self.dfs(node.right)

# V1'''
# IDEA : Iterative 
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63829/Python-Easy-Iterative-and-Recursive-Solution
class Solution(object):
    def kthSmallest(self,root, k):
        stack = []
        while root or stack:
            while root:
                stack.append(root)
                root = root.left
            root = stack.pop()
            k -= 1
            if k == 0:
                return root.val
            root = root.right

# V1'''''
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
# IDEA : RECURSION 
#Time complexity : O(N), to build a traversal.
# Space complexity : O(N) to keep an inorder traversal.
class Solution:
    def kthSmallest(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: int
        """
        def inorder(r):
            return inorder(r.left) + [r.val] + inorder(r.right) if r else []
    
        return inorder(root)[k - 1]

# V1''''''
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
# IDEA : ITERATION
# Time complexity : O(H+k), where HH is a tree height. This complexity is defined by the stack,
# Space complexity : O(H+k), the same as for time complexity, O(N+k) in the worst case, and O(logN+k) in the average case.
class Solution:
    def kthSmallest(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: int
        """
        stack = []
        
        while True:
            while root:
                stack.append(root)
                root = root.left
            root = stack.pop()
            k -= 1
            if not k:
                return root.val
            root = root.right

# V1''''''''
# https://blog.csdn.net/zhangpeterx/article/details/102879948
class Solution:
    def kthSmallest(self, root: TreeNode, k: int) -> int:
        def inorder(r):
            return inorder(r.left) + [r.val] + inorder(r.right) if r else []    
        return inorder(root)[k - 1]

# V2 
# Time:  O(max(h, k))
# Space: O(h)
class Solution(object):
    # @param {TreeNode} root
    # @param {integer} k
    # @return {integer}
    def kthSmallest(self, root, k):
        s, cur, rank = [], root, 0

        while s or cur:
            if cur:
                s.append(cur)
                cur = cur.left
            else:
                cur = s.pop()
                rank += 1
                if rank == k:
                    return cur.val
                cur = cur.right

        return float("-inf")

# time: O(max(h, k))
# space: O(h)
from itertools import islice
class Solution2(object):
    def kthSmallest(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: int
        """
        def gen_inorder(root):
            if root:
                for n in gen_inorder(root.left):
                    yield n

                yield root.val

                for n in gen_inorder(root.right):
                    yield n
        return next(islice(gen_inorder(root), k-1, k))