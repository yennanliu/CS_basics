"""

Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.

According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined between two nodes p and q as the lowest node in T that has both p and q as descendants (where we allow a node to be a descendant of itself).”

 

Example 1:


Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
Output: 3
Explanation: The LCA of nodes 5 and 1 is 3.
Example 2:


Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
Output: 5
Explanation: The LCA of nodes 5 and 4 is 5, since a node can be a descendant of itself according to the LCA definition.
Example 3:

Input: root = [1,2], p = 1, q = 2
Output: 1
 

Constraints:

The number of nodes in the tree is in the range [2, 105].
-109 <= Node.val <= 109
All Node.val are unique.
p != q
p and q will exist in the tree.

"""

# V0
# IDEA : RECURSION + POST ORDER TRANSVERSAL
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):

        ### NOTE here
        # if not root or find p in tree or find q in tree
        # -> then we quit the recursion and return root

        ### NOTE : we compare `p == root` and  `q == root`
        if not root or p == root or q == root:
            return root
        ### NOTE here
        #  -> not root.left, root.right, BUT left, right
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)

        ### NOTE here
        # find q and p on the same time -> LCA is the current node (root)
        # if left and right -> p, q MUST in left, right sub tree respectively

        ### NOTE : if left and right, means this root is OK for next recursive
        if left and right:
            return root
        ### NOTE here
        # if p, q both in left sub tree or both in right sub tree
        return left if left else right

# V0'
# IDEA : RECURSION + POST ORDER TRANSVERSAL
### NOTE : we need POST ORDER TRANSVERSAL for this problem
#          -> left -> right -> root
#          -> we can make sure that if p == q, then the root must be p and q's "common ancestor"
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        # if not root or find p in the tree or find q in the tree
        if not root or p == root or q == root:
            return root
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        # find q and p on the same time -> LCA is the current node (root)
        if left != None and right != None:
            return root
        # if only find p or only find q -> LCA is the one we found at the moment
        return left if left else right

# V0'
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        # if not root or find p in the tree or find q in the tree
        if not root or p == root or q == root:
            return root
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        # find q and p on the same time -> LCA is the current node (root)
        if left != None and right != None:
            return root
        # if only find p or only find q -> LCA is the one we found at the moment
        #return left if left else right
        if left == None and right == None:
            return None
        if left or right:
            return left or right

# V0''
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        if not root or p == root or q == root:
            return root
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        if left and right:
            return root
        return left if left else right

# V0'''
class Solution:
    def lowestCommonAncestor(self, root, A, B):
        # A & : if there is B below => A 
        # B & : if there is A below => B 
        # A & : if there is nothing below => A 
        # B & : if there is everything below => B 
        if root is None:
            return None
        
        if root == A or root == B:
            return root
        
        left_result = self.lowestCommonAncestor(root.left, A, B)
        right_result = self.lowestCommonAncestor(root.right, A, B)
        
        # split to A and B 
        if left_result and right_result: 
            return root
        
        # if there is one point or LCA at sub left tree 
        if left_result:
            return left_result
        
        # if there is one point or LCA at sub right tree 
        if right_result:
            return right_result
        
        # if there is nothing at left, right sub tree 
        return None

# V0''
# DFS : TO FIX 
# class Solution(object):
#     def lowestCommonAncestor(self, root, p, q):
#         if not root or p == root or q == root:
#             return root
#         r = []
#         r_p = self.dfs(root, p, r)
#         r_q = self.dfs(root, q, r)
#         if len(r_p) > len(r_q):
#             r_long, r_short = r_p, r_q 
#         else:
#             r_long, r_short = r_q, r_p
#         return r_short[-1]
#     def dfs(self, root, t, r):
#         if not root or t == root: 
#             return root
#         r.append(root.val)
#         if t in r:
#             return r 
#         self.dfs(root.left, t, r)
#         self.dfs(root.right, t, r)
        
# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80778001
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """
        if not root or p == root or q == root:
            return root
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        if left and right:
            return root
        return left if left else right

# V1'
# https://www.jiuzhang.com/solution/lowest-common-ancestor-of-a-binary-tree/#tag-highlight-lang-python
class Solution:
    """
    @param: root: The root of the binary search tree.
    @param: A: A TreeNode in a Binary.
    @param: B: A TreeNode in a Binary.
    @return: Return the lowest common ancestor(LCA) of the two nodes.
    """
    def lowestCommonAncestor(self, root, A, B):
        # A & : if there is B below => A 
        # B & : if there is A below => B 
        # A & : if there is nothing below => A 
        # B & : if there is everything below => B 
        if root is None:
            return None
        
        if root == A or root == B:
            return root
        
        left_result = self.lowestCommonAncestor(root.left, A, B)
        right_result = self.lowestCommonAncestor(root.right, A, B)
        
        # split to A and B 
        if left_result and right_result: 
            return root
        
        # if there is one point or LCA at sub left tree 
        if left_result:
            return left_result
        
        # if there is one point or LCA at sub right tree 
        if right_result:
            return right_result
        
        # if there is nothing at left, right sub tree 
        return None

# V1'
# https://leetcode.com/articles/lowest-common-ancestor-of-a-binary-tree/
# IDEA : BFS 
class Solution:

    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """

        # Stack for tree traversal
        stack = [root]

        # Dictionary for parent pointers
        parent = {root: None}

        # Iterate until we find both the nodes p and q
        while p not in parent or q not in parent:

            node = stack.pop()

            # While traversing the tree, keep saving the parent pointers.
            if node.left:
                parent[node.left] = node
                stack.append(node.left)
            if node.right:
                parent[node.right] = node
                stack.append(node.right)

        # Ancestors set() for node p.
        ancestors = set()

        # Process all ancestors for node p using parent pointers.
        while p:
            ancestors.add(p)
            p = parent[p]

        # The first ancestor of q which appears in
        # p's ancestor set() is their lowest common ancestor.
        while q not in ancestors:
            q = parent[q]
        return q

# V1'''
# IDEA : Recursive Approach
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/solution/
class Solution:

    def __init__(self):
        # Variable to store LCA node.
        self.ans = None

    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """
        def recurse_tree(current_node):

            # If reached the end of a branch, return False.
            if not current_node:
                return False

            # Left Recursion
            left = recurse_tree(current_node.left)

            # Right Recursion
            right = recurse_tree(current_node.right)

            # If the current node is one of p or q
            mid = current_node == p or current_node == q

            # If any two of the three flags left, right or mid become True.
            if mid + left + right >= 2:
                self.ans = current_node

            # Return True if either of the three bool values is True.
            return mid or left or right

        # Traverse the tree
        recurse_tree(root)
        return self.ans

# V1''''
# IDEA :  Iterative using parent pointers
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/solution/
class Solution:

    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """

        # Stack for tree traversal
        stack = [root]

        # Dictionary for parent pointers
        parent = {root: None}

        # Iterate until we find both the nodes p and q
        while p not in parent or q not in parent:

            node = stack.pop()

            # While traversing the tree, keep saving the parent pointers.
            if node.left:
                parent[node.left] = node
                stack.append(node.left)
            if node.right:
                parent[node.right] = node
                stack.append(node.right)

        # Ancestors set() for node p.
        ancestors = set()

        # Process all ancestors for node p using parent pointers.
        while p:
            ancestors.add(p)
            p = parent[p]

        # The first ancestor of q which appears in
        # p's ancestor set() is their lowest common ancestor.
        while q not in ancestors:
            q = parent[q]
        return q

# V1''''
# IDEA :  Iterative without parent pointers
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/solution/
class Solution:

    # Three static flags to keep track of post-order traversal.

    # Both left and right traversal pending for a node.
    # Indicates the nodes children are yet to be traversed.
    BOTH_PENDING = 2
    # Left traversal done.
    LEFT_DONE = 1
    # Both left and right traversal done for a node.
    # Indicates the node can be popped off the stack.
    BOTH_DONE = 0

    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """

        # Initialize the stack with the root node.
        stack = [(root, Solution.BOTH_PENDING)]

        # This flag is set when either one of p or q is found.
        one_node_found = False

        # This is used to keep track of LCA index.
        LCA_index = -1

        # We do a post order traversal of the binary tree using stack
        while stack:

            parent_node, parent_state = stack[-1]

            # If the parent_state is not equal to BOTH_DONE,
            # this means the parent_node can't be popped of yet.
            if parent_state != Solution.BOTH_DONE:

                # If both child traversals are pending
                if parent_state == Solution.BOTH_PENDING:

                    # Check if the current parent_node is either p or q.
                    if parent_node == p or parent_node == q:

                        # If one_node_found is set already, this means we have found both the nodes.
                        if one_node_found:
                            return stack[LCA_index][0]
                        else:
                            # Otherwise, set one_node_found to True,
                            # to mark one of p and q is found.
                            one_node_found = True

                            # Save the current top index of stack as the LCA_index.
                            LCA_index = len(stack) - 1

                    # If both pending, traverse the left child first
                    child_node = parent_node.left
                else:
                    # traverse right child
                    child_node = parent_node.right

                # Update the node state at the top of the stack
                # Since we have visited one more child.
                stack.pop()
                stack.append((parent_node, parent_state - 1))

                # Add the child node to the stack for traversal.
                if child_node:
                    stack.append((child_node, Solution.BOTH_PENDING))
            else:

                # If the parent_state of the node is both done,
                # the top node could be popped off the stack.

                # i.e. If LCA_index is equal to length of stack. Then we decrease LCA_index by 1.
                if one_node_found and LCA_index == len(stack) - 1:
                    LCA_index -= 1
                stack.pop()

        return None

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    # @param {TreeNode} root
    # @param {TreeNode} p
    # @param {TreeNode} q
    # @return {TreeNode}
    def lowestCommonAncestor(self, root, p, q):
        if root in (None, p, q):
            return root

        left, right = [self.lowestCommonAncestor(child, p, q) \
                         for child in (root.left, root.right)]
        # 1. If the current subtree contains both p and q,
        #    return their LCA.
        # 2. If only one of them is in that subtree,
        #    return that one of them.
        # 3. If neither of them is in that subtree,
        #    return the node of that subtree.
        return root if left and right else left or right