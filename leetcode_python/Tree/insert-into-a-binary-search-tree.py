# V0
class Solution(object):
    def insertIntoBST(self, root, val):
        if(root == None): return TreeNode(val);
        if(root.val < val): root.right = self.insertIntoBST(root.right, val);
        else: root.left = self.insertIntoBST(root.left, val);
        return(root)

# V0'
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
        