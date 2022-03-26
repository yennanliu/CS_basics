"""

1022. Sum of Root To Leaf Binary Numbers
Easy


You are given the root of a binary tree where each node has a value 0 or 1. Each root-to-leaf path represents a binary number starting with the most significant bit.

For example, if the path is 0 -> 1 -> 1 -> 0 -> 1, then this could represent 01101 in binary, which is 13.
For all leaves in the tree, consider the numbers represented by the path from the root to that leaf. Return the sum of these numbers.

The test cases are generated so that the answer fits in a 32-bits integer.

 

Example 1:


Input: root = [1,0,1,0,1,0,1]
Output: 22
Explanation: (100) + (101) + (110) + (111) = 4 + 5 + 6 + 7 = 22
Example 2:

Input: root = [0]
Output: 0
 

Constraints:

The number of nodes in the tree is in the range [1, 1000].
Node.val is 0 or 1.

"""

# V0
# IDEA : 257. Binary Tree Paths
class Solution(object):
    def sumRootToLeaf(self, root):
        def help(_str):
            res = 0
            _str = _str[::-1]
            for i in range(len(_str)):
                res += (2**i) * int(_str[i])
            return res
        # bfs
        # edge case
        if not root:
            return
        """
        NOTE !!! we init cache as string, so NO need to deal with array idx, len
        """
        cache = ""
        res = []
        q = [[root, cache]]
        cnt = 0
        while q:
            for i in range(len(q)):
                _tmp, _cache = q.pop(0)
                """
                NOTE !!! if _tmp and not _tmp.left and not _tmp.right
                  -> consider there is tmp, but no sub trees
                """
                if _tmp and not _tmp.left and not _tmp.right:
                    res.append(_cache + str(_tmp.val))
                """
                NOTE !!!
                  -> we add val to cache when _tmp.left, _tmp.right
                """
                if _tmp.left:
                    q.append([_tmp.left, _cache + str(_tmp.val)])
                if _tmp.right:
                    q.append([_tmp.right, _cache + str(_tmp.val)])
        #print("res = " + str(res))
        """
        NOTE !! : both work
        """
        #return sum([help(x) for x in res])
        return sum([int(x, 2) for x in res])

# V1
# IDEA : DFS
# https://leetcode.com/problems/sum-of-root-to-leaf-binary-numbers/discuss/270025/JavaC%2B%2BPython-Recursive-Solution
class Solution(object):
    def sumRootToLeaf(self, root, val=0):
        if not root: return 0
        val = val * 2 + root.val
        if root.left == root.right: return val
        return self.sumRootToLeaf(root.left, val) + self.sumRootToLeaf(root.right, val)

# V1
# IDEA : DFS
# https://leetcode.com/problems/sum-of-root-to-leaf-binary-numbers/discuss/366183/Python-DFS
class Solution(object):
    def sumRootToLeaf(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        res = self.dfs(root, str(root.val), 0)
        return res
        
    def dfs(self, node, path, res):
        
        if not node.left and not node.right:
            # leaf node
            res += self.binaryStrToInt(path)
            return res
        
        for child in [node.left, node.right]:
            if child:
                path += str(child.val)
                res = self.dfs(child, path, res)
                path = path[:-1]
        
        return res
    
    def binaryStrToInt(self, string):
        return int(string,2)

# V1
# IDEA :  Iterative Preorder Traversal.
# https://leetcode.com/problems/sum-of-root-to-leaf-binary-numbers/solution/
class Solution:
    def sumRootToLeaf(self, root: TreeNode) -> int:
        root_to_leaf = 0
        stack = [(root, 0) ]
        
        while stack:
            root, curr_number = stack.pop()
            if root is not None:
                curr_number = (curr_number << 1) | root.val
                # if it's a leaf, update root-to-leaf sum
                if root.left is None and root.right is None:
                    root_to_leaf += curr_number
                else:
                    stack.append((root.right, curr_number))
                    stack.append((root.left, curr_number))
                        
        return root_to_leaf

# V1
# IDEA : Recursive Preorder Traversal.
# https://leetcode.com/problems/sum-of-root-to-leaf-binary-numbers/solution/
class Solution:
    def sumRootToLeaf(self, root: TreeNode) -> int:
        def preorder(r, curr_number):
            nonlocal root_to_leaf
            if r:
                curr_number = (curr_number << 1) | r.val
                # if it's a leaf, update root-to-leaf sum
                if not (r.left or r.right):
                    root_to_leaf += curr_number
                    
                preorder(r.left, curr_number)
                preorder(r.right, curr_number) 
        
        root_to_leaf = 0
        preorder(root, 0)
        return root_to_leaf

# V1
# IDEA : Morris Preorder Traversal.
# https://leetcode.com/problems/sum-of-root-to-leaf-binary-numbers/solution/
class Solution:
    def sumRootToLeaf(self, root: TreeNode) -> int:
        root_to_leaf = curr_number = 0
        
        while root:  
            # If there is a left child,
            # then compute the predecessor.
            # If there is no link predecessor.right = root --> set it.
            # If there is a link predecessor.right = root --> break it.
            if root.left: 
                # Predecessor node is one step to the left 
                # and then to the right till you can.
                predecessor = root.left 
                steps = 1
                while predecessor.right and predecessor.right is not root: 
                    predecessor = predecessor.right 
                    steps += 1

                # Set link predecessor.right = root
                # and go to explore the left subtree
                if predecessor.right is None:
                    curr_number = (curr_number << 1) | root.val                    
                    predecessor.right = root  
                    root = root.left  
                # Break the link predecessor.right = root
                # Once the link is broken, 
                # it's time to change subtree and go to the right
                else:
                    # If you're on the leaf, update the sum
                    if predecessor.left is None:
                        root_to_leaf += curr_number
                    # This part of tree is explored, backtrack
                    for _ in range(steps):
                        curr_number >>= 1
                    predecessor.right = None
                    root = root.right 
                    
            # If there is no left child
            # then just go right.        
            else: 
                curr_number = (curr_number << 1) | root.val
                # if you're on the leaf, update the sum
                if root.right is None:
                    root_to_leaf += curr_number
                root = root.right
                        
        return root_to_leaf

# V2