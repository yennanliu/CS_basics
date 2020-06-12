# V0
class Solution:
    def tree2str(self, t):
        if not t:
            return ''
        if not t.left and not t.right:
            return str(t.val)
        ### NOTICE HERE
        if not t.left:
            return str(t.val) + '()' + '(' + self.tree2str(t.right) + ')'
        ### NOTICE HERE
        if not t.right:
            return str(t.val) + '(' + self.tree2str(t.left) + ')'
        ### NOTICE HERE
        return str(t.val) + '(' + self.tree2str(t.left) + ')' + '(' + self.tree2str(t.right) + ')'

# V0'
class Solution(object):
    def tree2str(self, t):
        if not t: return ''
        ans = str(t.val)
        if t.left or t.right: ans += '(' + self.tree2str(t.left) + ')'
        if t.right: ans += '(' + self.tree2str(t.right) + ')'
        return ans

# V0''
class Solution(object):
    def tree2str(self, t):
        if not t:
            return ""
        res = ""
        left = self.tree2str(t.left)
        right = self.tree2str(t.right)
        if left or right:
            res += "(%s)" % left
        if right:
            res += "(%s)" % right
        return str(t.val) + res
        
# V1 
# http://bookshadow.com/weblog/2017/06/04/leetcode-construct-string-from-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def tree2str(self, t):
        """
        :type t: TreeNode
        :rtype: str
        """
        if not t: return ''
        ans = str(t.val)
        if t.left or t.right: ans += '(' + self.tree2str(t.left) + ')'
        if t.right: ans += '(' + self.tree2str(t.right) + ')'
        return ans

### Test case : dev 

# V1'
# https://www.polarxiong.com/archives/LeetCode-606-construct-string-from-binary-tree.html
class Solution:
    def tree2str(self, t):
        """
        :type t: TreeNode
        :rtype: str
        """
        if not t:
            return ''
        if not t.left and not t.right:
            return str(t.val)
        if not t.left:
            return str(t.val) + '()' + '(' + self.tree2str(t.right) + ')'
        if not t.right:
            return str(t.val) + '(' + self.tree2str(t.left) + ')'
        return str(t.val) + '(' + self.tree2str(t.left) + ')' + '(' + self.tree2str(t.right) + ')'

# V1''
# https://leetcode.com/problems/construct-string-from-binary-tree/discuss/104090/Python-recursion
# IDEA : RECURSION
class Solution:
    def tree2str(self, t):
        """
        :type t: TreeNode
        :rtype: str
        """
        def preorder(root):
            if root is None:
                return ""
            s=str(root.val)
            l=preorder(root.left)
            r=preorder(root.right)
            if r=="" and l=="":
                return s 
            elif l=="":
                s+="()"+"("+r+")"
            elif r=="":                
                s+="("+l+")"
            else :   
                s+="("+l+")"+"("+r+")"
            return s
        return preorder(t)

# V1'''
# https://leetcode.com/problems/construct-string-from-binary-tree/discuss/151806/Python3%3A-Iterative-Method-Using-stack
# IDEA : STACK 
class Solution(object):
    def tree2str(self,t):
        if not t: return ""
        stack = []
        stack.append(t)
        res = ""
        while stack:
            node = stack.pop()
            if node == ")":
                res += ")"
                continue
            res += "("+str(node.val)
            if not node.left and  node.right:
                res += "()"
            if  node.right:
                stack.append(")")
                stack.append(node.right)
            if  node.left:
                stack.append(")")
                stack.append(node.left)
        return res[1:]

# V1''''
# https://leetcode.com/problems/construct-string-from-binary-tree/discuss/103984/Python-Simple-Solution
class Solution(object):
    def tree2str(self, t):
        """
        :type t: TreeNode
        :rtype: str
        """
        if not t:
            return ""
        res = ""
        left = self.tree2str(t.left)
        right = self.tree2str(t.right)
        if left or right:
            res += "(%s)" % left
        if right:
            res += "(%s)" % right
        return str(t.val) + res

# V1'''''
# https://leetcode.com/problems/construct-string-from-binary-tree/discuss/104000/Python-Straightforward-with-Explanation
class Solution(object):
    def tree2str(self, t):
        if not t: return ''
        left = '({})'.format(self.tree2str(t.left)) if (t.left or t.right) else ''
        right = '({})'.format(self.tree2str(t.right)) if t.right else ''
        return '{}{}{}'.format(t.val, left, right)

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def tree2str(self, t):
        """
        :type t: TreeNode
        :rtype: str
        """
        if not t: return ""
        s = str(t.val)
        if t.left or t.right:
            s += "(" + self.tree2str(t.left) + ")"
        if t.right:
            s += "(" + self.tree2str(t.right) + ")"
        return s