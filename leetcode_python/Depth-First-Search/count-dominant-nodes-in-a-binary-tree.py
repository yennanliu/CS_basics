# https://leetcode.com/problems/count-dominant-nodes-in-a-binary-tree/description/

"""

3997. Count Dominant Nodes in a Binary Tree
Solved
Medium
premium lock icon
Companies
Hint
You are given the root of a complete binary tree.

A node x is called dominant if its value is equal to the maximum value among all nodes in the subtree rooted at x.

Return the number of dominant nodes in the tree.

 

Example 1:



Input: root = [5,3,8,2,4,7,1]

Output: 5

Explanation:

The leaf nodes with values 2, 4, 7, and 1 are dominant.
The node with value 8 is dominant because its value is the maximum value in its subtree [8, 7, 1].
Thus, the answer is 5.
Example 2:



Input: root = [1,2,3,1,2]

Output: 4

Explanation:

The leaf nodes with values 1, 2, and 3 are dominant.
The node with value 2 whose subtree is [2, 1, 2] is dominant because its value is the maximum value in its subtree.
Thus, the answer is 4.
 

Constraints:

The number of nodes in the tree is in the range [1, 105].
1 <= Node.val <= 109
The tree is guaranteed to be a complete binary tree.
 


"""



# V0
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def countDominantNodes(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: int
        """
        
        pass


# V0-1
# IDEA: DFS (post order)(gpt)
class Solution(object):
    def countDominantNodes(self, root):
        self.cnt = 0

        self.helper(root)

        return self.cnt

    def helper(self, node):
        if not node:
        	"""
        	NOTE !!!

        	below edge case
        	"""
            return float("-inf")

        left_max = self.helper(node.left)
        right_max = self.helper(node.right)

        child_max = max(left_max, right_max)

        if node.val >= child_max:
            self.cnt += 1

     	"""
    	NOTE !!!

    	we need to return `seen max till now`
    	so, it has to be the max from
    	left_max, right_max, and root.val

    	"""       
        return max(node.val, child_max)


# V0-2
# IDEA: DFS (post order)(gpt)
class Solution(object):
    def countDominantNodes(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.cnt = 0
        self.helper(root)  # Fixed typo: pass 'root', not 'node'
        return self.cnt
        
    def helper(self, node):
        # Base case: return negative infinity so negative node values still work
        if not node:
            return float('-inf') 
            
        # 1. Get the max from the left and right subtrees
        _left = self.helper(node.left)
        _right = self.helper(node.right)
        
        # 2. Find the highest value out of all children
        _max = max(_left, _right)
        
        # 3. If the current node is greater than or equal to all children, it's dominant!
        if node.val >= _max:
            self.cnt += 1
            
        # 4. Return the overall max of this ENTIRE subtree (including the current node)
        return max(node.val, _max)


# V1


# V2
