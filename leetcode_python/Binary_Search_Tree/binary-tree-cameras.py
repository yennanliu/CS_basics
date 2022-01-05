"""

968. Binary Tree Cameras
Hard

You are given the root of a binary tree. We install cameras on the tree nodes where each camera at a node can monitor its parent, itself, and its immediate children.

Return the minimum number of cameras needed to monitor all nodes of the tree.

 

Example 1:


Input: root = [0,0,null,0,0]
Output: 1
Explanation: One camera is enough to monitor all nodes if placed as shown.
Example 2:


Input: root = [0,0,null,0,null,0,null,null,0]
Output: 2
Explanation: At least two cameras are needed to monitor all nodes of the tree. The above image shows one of the valid configurations of camera placement.
 

Constraints:

The number of nodes in the tree is in the range [1, 1000].
Node.val == 0

"""

# V0

# V1
# IDEA : GREEDY + DFS
# https://leetcode.com/problems/binary-tree-cameras/discuss/211180/JavaC%2B%2BPython-Greedy-DFS
class Solution(object):
     def minCameraCover(self, root):
            self.res = 0
            def dfs(root):
                if not root: return 2
                l, r = dfs(root.left), dfs(root.right)
                if l == 0 or r == 0:
                    self.res += 1
                    return 1
                return 2 if l == 1 or r == 1 else 0
            return (dfs(root) == 0) + self.res

# V1'
# https://leetcode.com/problems/binary-tree-cameras/discuss/213666/Python-easy-to-understand
class Solution:
    def minCameraCover(self, root):
        # 0 means not covered。1 means covered but not has a camera on it. 2 means a camera on it.
        # reference: https://www.itread01.com/content/1546174153.html
        def dfs(node):
            if not node:
                return 1
            l=dfs(node.left)
            r=dfs(node.right)
            
            if l==0 or r==0:
                self.sum+=1
                return 2
            elif l==2 or r==2:
                return 1
            else:
                return 0
        
        self.sum=0
        if dfs(root)==0:
            self.sum+=1
        
        return self.sum

# V1''
# https://leetcode.com/problems/binary-tree-cameras/discuss/891691/Python
class Solution(object):
    def minCameraCover(self, root):
            self.res = 0
            def f(root):
                if not root: return "covered"
                l, r = f(root.left), f(root.right)
                if l=="cover_me" or r=="cover_me":
                    self.res+=1
                    return "covered_you"
                return "covered" if l=="covered_you" or r=="covered_you" else "cover_me"
            return (f(root) == "cover_me") + self.res # handles for the root node

# V1'''
# IDEA : RECURSION
# https://leetcode.com/problems/binary-tree-cameras/discuss/316239/Simple-recursion-in-python
class Solution(object):
    def min_camera(self, root):
        if root is None:
            return 0, 0, float("Inf")
        
        al, bl, cl = self.min_camera(root.left)
        ar, br, cr = self.min_camera(root.right)
        
        a = bl + br
        b = min(cl+br, cl+cr, cr+bl)
        c = 1 + min(al, bl, cl) + min(ar, br, cr)
        
        return a, b, c
        
    def minCameraCover(self, root):
        a, b, c = self.min_camera(root)
        return min(b, c)

# V1''''
# IDEA : GREEDY
# https://leetcode.com/problems/binary-tree-cameras/solution/
class Solution(object):
    def minCameraCover(self, root):
        self.ans = 0
        covered = {None}

        def dfs(node, par = None):
            if node:
                dfs(node.left, node)
                dfs(node.right, node)

                if (par is None and node not in covered or
                        node.left not in covered or node.right not in covered):
                    self.ans += 1
                    covered.update({node, par, node.left, node.right})

        dfs(root)
        return self.ans

# V1'''''
# IDEA : DP
# https://leetcode.com/problems/binary-tree-cameras/solution/
class Solution(object):
    def minCameraCover(self, root):
        def solve(node):
            if not node: return 0, 0, float('inf')
            L = solve(node.left)
            R = solve(node.right)

            dp0 = L[1] + R[1]
            dp1 = min(L[2] + min(R[1:]), R[2] + min(L[1:]))
            dp2 = 1 + min(L) + min(R)

            return dp0, dp1, dp2

        return min(solve(root)[1:])

# V2