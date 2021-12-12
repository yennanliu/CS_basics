"""

You are given the root node of a binary search tree (BST) and a value to insert into the tree. Return the root node of the BST after the insertion. It is guaranteed that the new value does not exist in the original BST.

Notice that there may exist multiple valid ways for the insertion, as long as the tree remains a BST after insertion. You can return any of them.

 

Example 1:


Input: root = [4,2,7,1,3], val = 5
Output: [4,2,7,1,3,5]
Explanation: Another accepted tree is:

Example 2:

Input: root = [40,20,60,10,30,50,70], val = 25
Output: [40,20,60,10,30,50,70,null,null,25]
Example 3:

Input: root = [4,2,7,1,3,null,null,null,null,null,null], val = 5
Output: [4,2,7,1,3,5]
 

Constraints:

The number of nodes in the tree will be in the range [0, 104].
-108 <= Node.val <= 108
All the values Node.val are unique.
-108 <= val <= 108
It's guaranteed that val does not exist in the original BST.

"""

# VO : recursion + dfs
class Solution(object):
    def insertIntoBST(self, root, val):
        if not root: 
            return TreeNode(val)
        if root.val < val: 
            root.right = self.insertIntoBST(root.right, val);
        elif root.val > val: 
            root.left = self.insertIntoBST(root.left, val);
        return(root)
        
# V0'
class Solution(object):
    def insertIntoBST(self, root, val):
        if(root == None): return TreeNode(val);
        if(root.val < val): root.right = self.insertIntoBST(root.right, val);
        else: root.left = self.insertIntoBST(root.left, val);
        return(root)

# V0''
class Solution(object):
    def insertIntoBST(self, root, val):
        if(root == None): 
            return TreeNode(val)
        if (root.val > val):
            root.left = self.insertIntoBST(root.left, val)
        if(root.val < val): 
            root.right = self.insertIntoBST(root.right, val)
        return(root)

# V1
# https://leetcode.com/problems/insert-into-a-binary-search-tree/discuss/180244/Python-4-line-clean-recursive-solution
class Solution(object):
    def insertIntoBST(self, root, val):
        if(root == None): return TreeNode(val);
        if(root.val < val): root.right = self.insertIntoBST(root.right, val);
        else: root.left = self.insertIntoBST(root.left, val);
        return(root)

### Test case : dev

# V1'
# https://leetcode.com/problems/insert-into-a-binary-search-tree/discuss/390367/Python3-(Iterative-and-Recursive)
class Solution(object):
    def insertIntoBST(self, root, val):
        if not root: return TreeNode(val)
        cur = root
        while True:
            if cur.val < val:
                if cur.right: cur = cur.right
                else: 
                    cur.right = TreeNode(val)
                    break
            else:
                if cur.left: cur = cur.left
                else:
                    cur.left = TreeNode(val)
                    break   
        return root

# V1''
# https://leetcode.com/problems/insert-into-a-binary-search-tree/discuss/373442/Python-Recursive-solution-(Bottom-up-approach)-96ms
class Solution(object):
    def insertIntoBST(self, root, val):
        if not root:
            return TreeNode(val)
        if(val<root.val):
            root.left=self.insertIntoBST(root.left,val)          #Telling the parent node that this node will be the parent of the inserted node
        elif(val>root.val):
            root.right=self.insertIntoBST(root.right,val)    
        return root
        