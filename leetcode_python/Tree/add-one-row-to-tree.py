"""

623. Add One Row to Tree
Solved
Medium
Topics
premium lock icon
Companies
Given the root of a binary tree and two integers val and depth, add a row of nodes with value val at the given depth depth.

Note that the root node is at depth 1.

The adding rule is:

Given the integer depth, for each not null tree node cur at the depth depth - 1, create two tree nodes with value val as cur's left subtree root and right subtree root.
cur's original left subtree should be the left subtree of the new left subtree root.
cur's original right subtree should be the right subtree of the new right subtree root.
If depth == 1 that means there is no depth depth - 1 at all, then create a tree node with value val as the new root of the whole original tree, and the original tree is the new root's left subtree.
 

Example 1:


Input: root = [4,2,6,3,1,5], val = 1, depth = 2
Output: [4,1,1,2,null,null,6,3,1,5]
Example 2:


Input: root = [4,2,null,3,1], val = 1, depth = 3
Output: [4,2,null,1,1,3,null,null,1]
 

Constraints:

The number of nodes in the tree is in the range [1, 104].
The depth of the tree is in the range [1, 104].
-100 <= Node.val <= 100
-105 <= val <= 105
1 <= depth <= the depth of tree + 1
 

"""


# V0
class Solution(object):
    def addOneRow(self, root, val, depth):
        """
        :type root: Optional[TreeNode]
        :type val: int
        :type depth: int
        :rtype: Optional[TreeNode]
        """
        pass


# V0-1

# V0-2

# V0-3
# IDEA: BFS (gpt)
from collections import deque

class Solution(object):
    def addOneRow(self, root, val, depth):
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root

        q = deque([root])
        cur_depth = 1

        while q:
            size = len(q)

            if cur_depth == depth - 1:
                for _ in range(size):
                    node = q.popleft()

                    # Save original children
                    old_left = node.left
                    old_right = node.right

                    # Insert new nodes
                    node.left = TreeNode(val)
                    node.right = TreeNode(val)

                    # Reattach original subtrees
                    node.left.left = old_left
                    node.right.right = old_right

                break

            for _ in range(size):
                node = q.popleft()

                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)

            cur_depth += 1

        return root



# V0-4
# IDEA: BFS (gemini)
from collections import deque

class Solution(object):
    def addOneRow(self, root, val, depth):
        """
        :type root: TreeNode
        :type val: int
        :type depth: int
        :rtype: TreeNode
        """
        # Edge case: If depth is 1, the problem states the new node becomes the new root
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root
            
        # Use deque for O(1) pops from the front
        q = deque([root])
        curr_depth = 1  # The root is at depth 1
        
        while q:
            # If we reached the row RIGHT ABOVE where we need to insert
            if curr_depth == depth - 1:
                for _ in range(len(q)):
                    node = q.popleft()
                    
                    # 1. Cache the original children
                    _ori_left = node.left
                    _ori_right = node.right
                    
                    # 2. Create the new nodes and attach them to the current node
                    node.left = TreeNode(val)
                    node.right = TreeNode(val)
                    
                    # 3. IMMEDIATELY attach the original children to the new nodes
                    node.left.left = _ori_left
                    node.right.right = _ori_right
                    
                # We added the row, we are completely done!
                return root
                
            # Otherwise, just do standard BFS to move down a level
            size = len(q)
            for _ in range(size):
                node = q.popleft()
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)
                    
            curr_depth += 1
            
        return root



# V1
# https://www.jiuzhang.com/solution/add-one-row-to-tree/#tag-highlight-lang-python
# time = O(n)  # n = number of tree nodes
# space = O(h)  # h = tree height (recursion stack)
class Solution:
    """
    @param root: the root of binary tree
    @param v: a integer
    @param d: a integer
    @return: return a TreeNode
    """
    def addOneRow(self, root, v, d):
        # write your code here
        if not root:
            return None
        if d==1:
            new_root = TreeNode(v)
            new_root.left = root
            return new_root
        if d==2:
            root.left, root.left.left = TreeNode(v), root.left
            root.right, root.right.right = TreeNode(v), root.right
            return root
        elif d>2:
            root.left = self.addOneRow(root.left, v, d-1)
            root.right = self.addOneRow(root.right, v, d-1)
        return root
        
# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79645198
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)  # n = number of tree nodes
# space = O(h)  # h = tree height (recursion stack)
class Solution(object):
    def addOneRow(self, root, v, d):
        """
        :type root: TreeNode
        :type v: int
        :type d: int
        :rtype: TreeNode
        """
        if not root: return root
        if d == 1:
            left = TreeNode(v)
            left.left = root
            root = left
        elif d == 2:
            left = TreeNode(v)
            right = TreeNode(v)
            left.left = root.left
            right.right = root.right
            root.left = left
            root.right = right
        else:
            self.addOneRow(root.left, v, d - 1)
            self.addOneRow(root.right, v, d - 1)
        return root

# V2 
# time = O(n)
# space = O(h)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def addOneRow(self, root, v, d):
        """
        :type root: TreeNode
        :type v: int
        :type d: int
        :rtype: TreeNode
        """
        if d in (0, 1):
            node = TreeNode(v)
            if d == 1:
                node.left = root
            else:
                node.right = root
            return node
        if root and d >= 2:
            root.left = self.addOneRow(root.left,  v, d-1 if d > 2 else 1)
            root.right = self.addOneRow(root.right, v, d-1 if d > 2 else 0)
        return root
