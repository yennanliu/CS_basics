"""

129. Sum Root to Leaf Numbers
Solved
Medium
Topics
premium lock icon
Companies
You are given the root of a binary tree containing digits from 0 to 9 only.

Each root-to-leaf path in the tree represents a number.

For example, the root-to-leaf path 1 -> 2 -> 3 represents the number 123.
Return the total sum of all root-to-leaf numbers. Test cases are generated so that the answer will fit in a 32-bit integer.

A leaf node is a node with no children.

 

Example 1:


Input: root = [1,2,3]
Output: 25
Explanation:
The root-to-leaf path 1->2 represents the number 12.
The root-to-leaf path 1->3 represents the number 13.
Therefore, sum = 12 + 13 = 25.
Example 2:


Input: root = [4,9,0,5,1]
Output: 1026
Explanation:
The root-to-leaf path 4->9->5 represents the number 495.
The root-to-leaf path 4->9->1 represents the number 491.
The root-to-leaf path 4->0 represents the number 40.
Therefore, sum = 495 + 491 + 40 = 1026.
 

Constraints:

The number of nodes in the tree is in the range [1, 1000].
0 <= Node.val <= 9
The depth of the tree will not exceed 10.

"""


# V0
class Solution(object):
    def sumNumbers(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: int
        """
        pass
        
# V0-1
# IDEA: DFS + PATH (gpt)
class Solution(object):
    def sumNumbers(self, root):
        self.res = 0
        self.helper(root, [])
        return self.res

    def helper(self, root, path):
        if not root:
            return

        path.append(root.val)

        if not root.left and not root.right:
            val = "".join(map(str, path))
            self.res += int(val)
            path.pop()          # backtrack before returning
            return

        self.helper(root.left, path)
        self.helper(root.right, path)

        path.pop()              # backtrack


# V0-2
# IDEA: DFS (gpt)
class Solution(object):
    def sumNumbers(self, root):
        return self.helper(root, 0)

    def helper(self, root, curr):
        if not root:
            return 0

        curr = curr * 10 + root.val

        if not root.left and not root.right:
            return curr

        return self.helper(root.left, curr) + self.helper(root.right, curr)



# V0-3
# IDEA: DFS + PATH (GEMINI)
class Solution(object):
    def sumNumbers(self, root):
        self.res = 0
        self.helper(root, [])
        return self.res
        
    def helper(self, root, path):
        if not root:
            return 
            
        # 1. Append as string so "".join() works later
        path.append(str(root.val))
        
        if not root.left and not root.right:
            val = "".join(path)
            self.res += int(val)
            # 2. MUST backtrack before our early return!
            path.pop()
            return
            
        self.helper(root.left, path)
        self.helper(root.right, path)
        
        # backtrack (undo) for non-leaf nodes
        path.pop()


# V0
# time = O(n)
# space = O(h), h is height of binary tree (recursion stack)
class Solution(object):
    def sumNumbers(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def dfs(root, val):
            val = val * 10 + root.val
            if (root.left or root.right) is None:
                return val
            sums = 0
            if root.left:
                sums += dfs(root.left, val)
            if root.right:
                sums += dfs(root.right, val)
            return sums
        if root is None:
            return 0
        return dfs(root, 0) 

# V1
# http://bookshadow.com/weblog/2016/01/07/leetcode-sum-root-to-leaf-numbers/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)
# space = O(h), h is height of binary tree (recursion stack)
class Solution(object):
    def sumNumbers(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def dfs(root, val):
            val = val * 10 + root.val
            if (root.left or root.right) is None:
                return val
            sums = 0
            if root.left:
                sums += dfs(root.left, val)
            if root.right:
                sums += dfs(root.right, val)
            return sums
        if root is None:
            return 0
        return dfs(root, 0)

# V1'
# https://www.jiuzhang.com/solution/sum-root-to-leaf-numbers/#tag-highlight-lang-python
# time = O(n)
# space = O(h), h is height of binary tree (recursion stack)
class Solution:
    """
    @param root: the root of the tree
    @return: the total sum of all root-to-leaf numbers
    """
    def sumNumbers(self, root):
        # write your code here
        return self.dfs(root, 0)
    def dfs(self, root, prev):
        if(root == None):
            return 0
        sum = root.val + prev * 10
        if(root.left == None and root.right == None):
            return sum
        return self.dfs(root.left, sum) + self.dfs(root.right, sum)

# V2
# time = O(n)
# space = O(h), h is height of binary tree
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param root, a tree node
    # @return an integer
    def sumNumbers(self, root):
        return self.sumNumbersRecu(root, 0)

    def sumNumbersRecu(self, root, num):
        if root is None:
            return 0

        if root.left is None and root.right is None:
            return num * 10 + root.val

        return self.sumNumbersRecu(root.left, num * 10 + root.val) + self.sumNumbersRecu(root.right, num * 10 + root.val)
