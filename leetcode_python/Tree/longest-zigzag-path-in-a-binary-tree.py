"""

1372. Longest ZigZag Path in a Binary Tree
Medium

You are given the root of a binary tree.

A ZigZag path for a binary tree is defined as follow:

Choose any node in the binary tree and a direction (right or left).
If the current direction is right, move to the right child of the current node; otherwise, move to the left child.
Change the direction from right to left or from left to right.
Repeat the second and third steps until you can't move in the tree.
Zigzag length is defined as the number of nodes visited - 1. (A single node has a length of 0).

Return the longest ZigZag path contained in that tree.

 

Example 1:


Input: root = [1,null,1,1,1,null,null,1,1,null,1,null,null,null,1,null,1]
Output: 3
Explanation: Longest ZigZag path in blue nodes (right -> left -> right).
Example 2:


Input: root = [1,1,1,null,1,null,null,1,1,null,1]
Output: 4
Explanation: Longest ZigZag path in blue nodes (left -> right -> left -> right).
Example 3:

Input: root = [1]
Output: 0
 

Constraints:

The number of nodes in the tree is in the range [1, 5 * 104].
1 <= Node.val <= 100

"""

# V0

# V1
# IDEA : BFS
# https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/discuss/534968/Python-BFS-solution-(100)
class Solution:
    def longestZigZag(self, root):
        q = [(root, 1, 0)] # node, direction, length; direction can be initialized to be 1 (to right child) or -1 (to left child)
        res = 0
        while q:
            node, direction, length = q.pop(0)
            while True:
                if direction == 1:
                    if node.left:
                        q.append((node.left, direction, 1))
                    if node.right:
                        node, direction, length = node.right, -direction, length + 1
                    else:
                        res = max(res, length)
                        break
                else:
                    if node.right:
                        q.append((node.right, direction, 1))
                    if node.left:
                        node, direction, length = node.left, -direction, length + 1
                    else:
                        res = max(res, length)
                        break
        return res

# V1
# IDEA : DFS
# https://blog.csdn.net/Wonz5130/article/details/104733479

# V1
# IDEA : DFS
# https://www.codeleading.com/article/69034872436/
class Solution:
    def maxZigZag(self,root,isLeft,step):
        if(root is None):
            return
        self.maxStep=max(self.maxStep,step)
        if(isLeft):
            self.maxZigZag(root.right,True,1)
            self.maxZigZag(root.left,False,step+1)
        else:
            self.maxZigZag(root.right,True,step+1)
            self.maxZigZag(root.left,False,1)
        
    def longestZigZag(self, root: TreeNode) -> int:
        self.maxStep=0
        self.maxZigZag(root,True,0)
        self.maxZigZag(root,False,0)
        return self.maxStep

# V1
# IDEA : DFS
# https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/discuss/531867/JavaPython-DFS-Solution
# IDEA :
# Recursive return [left, right, result], where:
# left is the maximum length in direction of root.left
# right is the maximum length in direction of root.right
# result is the maximum length in the whole sub tree.
class Solution:
    def longestZigZag(self, root):
        def dfs(root):
            if not root: return [-1, -1, -1]
            left, right = dfs(root.left), dfs(root.right)
            return [left[1] + 1, right[0] + 1, max(left[1] + 1, right[0] + 1, left[2], right[2])]
        return dfs(root)[-1]

# V1
# IDEA : POST ORDER
# https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/discuss/531860/python-postorder
# a = the max number of nodes on a path down (starting from u) if I take a left
# b = the max number of nodes on a path down (starting from u) if I take a right
class Solution(object):
    def longestZigZag(self, root):        
        c = [0]
        self.f(root, c)
        return c[0] - 1
    
    def f(self, node, count):
        if node is None: return (0,0)
        a,b = self.f(node.left,count), self.f(node.right,count)
        count[0] = max(count[0], 1+ a[1], 1+b[0])
        return (1 + a[1], 1 + b[0]) 

# V1
# IDEA : BFS
# https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/discuss/1771498/python-or-bfs
class Solution:
    def longestZigZag(self, root: Optional[TreeNode]) -> int:
        
        if root.left == None and root.right == None:
            return 0
        
        queue = []
        ans = 0
        if root.left:
            queue.append((root.left , True ,1))
        if root.right:
            queue.append((root.right , False , 1))
            
        while(queue):
            node , left , val = queue.pop(0)
            if not node:
                continue
            
            ans = max(ans , val)
            
            if left:
                queue.append((node.right , False , val +1))
                queue.append((node.left , True ,1))
            else:
                queue.append((node.left , True , val+1))
                queue.append((node.right , False , 1))
        
        return ans

# V1
# IDEA : DFS
# https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/discuss/532057/python-dfs
class Solution(object):
    def longestZigZag(self, root):
        self.max=0
        def dfs(node):
            if not node: return -1,-1
            l_dir_left, l_dir_right = dfs(node.left)
            r_dir_left, r_dir_right = dfs(node.right)
            self.max = max(self.max,l_dir_left, l_dir_right,r_dir_left, r_dir_right,l_dir_right +1,r_dir_left+1)
            return (l_dir_right +1,r_dir_left+1)
        dfs(root)
        return self.max


# V2