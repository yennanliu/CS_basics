"""

617. Merge Two Binary Trees
Easy

You are given two binary trees root1 and root2.

Imagine that when you put one of them to cover the other, some nodes of the two trees are overlapped while the others are not. You need to merge the two trees into a new binary tree. The merge rule is that if two nodes overlap, then sum node values up as the new value of the merged node. Otherwise, the NOT null node will be used as the node of the new tree.

Return the merged tree.

Note: The merging process must start from the root nodes of both trees.

 

Example 1:


Input: root1 = [1,3,2,5], root2 = [2,1,3,null,4,null,7]
Output: [3,4,5,5,4,null,7]
Example 2:

Input: root1 = [1], root2 = [1,2]
Output: [2,2]
 

Constraints:

The number of nodes in both trees is in the range [0, 2000].
-104 <= Node.val <= 104

"""

# V0
# IDEA : recursive
class Solution(object):
    def mergeTrees(self, root1, root2):
        def help(root1, root2):
            if not root1 and not root2:
                return
            if root1 and root2:
                newT = TreeNode(root1.val + root2.val)
                newT.left = help(root1.left, root2.left)
                newT.right = help(root1.right, root2.right)
                return newT
            if not root1 or not root2:
                return root1 if root1 else root2
        res = help(root1, root2)
        return res

# V0'
# IDEA : ITERATION, BFS
class Solution(object):
     def mergeTrees(self, t1, t2):
            dummy = TreeNode(0)
            stack = [(t1, t2, dummy, 'l')]
            while stack:
                n1, n2, parent, lr = stack.pop()
                n = TreeNode((n1.val if n1 else 0 ) + (n2.val if n2 else 0 )) if n1 or n2 else None
                if lr == 'l': 
                    parent.left = n 
                else:
                    parent.right = n

                if n1 or n2:
                    stack.append((n1 and n1.left, n2 and n2.left, n, 'l'))
                    stack.append((n1 and n1.right, n2 and n2.right, n, 'r'))

            return dummy.left

# V0
# IDEA : recursive
class Solution:
    def mergeTrees(self, t1, t2):
        return self.dfs(t1,t2)

    def dfs(self, t1, t2):
        if not t1 and not t2:
            return
        
        if t1 and t2:
            ### NOTE here
            newT = TreeNode(t1.val +  t2.val)
            newT.right = self.mergeTrees(t1.right, t2.right)
            newT.left = self.mergeTrees(t1.left, t2.left)   
            return newT
        
        ### NOTE here
        else:
            return t1 or t2

# V0'
# IDEA : DFS + BACKTRACK
class Solution:
    def mergeTrees(self, t1, t2):
        return self.dfs(t1,t2)

    def dfs(self, t1, t2):
        if t1 and t2:
            newT = TreeNode(t1.val +  t2.val)
            newT.left = self.mergeTrees(t1.left, t2.left)
            newT.right = self.mergeTrees(t1.right, t2.right)
            return newT
        else:
            return t1 or t2

# V0'
class Solution:
    def mergeTrees(self, t1, t2):
        if t1 and t2:
            newT = TreeNode(t1.val +  t2.val)
            newT.left = self.mergeTrees(t1.left, t2.left)
            newT.right = self.mergeTrees(t1.right, t2.right)
            return newT
        else:
            return t1 or t2

# V1
# IDEA : DFS 
# https://blog.csdn.net/fuxuemingzhu/article/details/79052953
class Solution:
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        if t1 and t2:
            newT = TreeNode(t1.val +  t2.val)
            newT.left = self.mergeTrees(t1.left, t2.left)
            newT.right = self.mergeTrees(t1.right, t2.right)
            return newT
        else:
            return t1 or t2

# V1
# https://leetcode.com/problems/merge-two-binary-trees/discuss/159138/Python-solution
# IDEA : resursive
class Solution(object):
    def mergeTrees(self, t1, t2):
        if t1 == None and t2 == None:
            return None
        elif t1 == None:
            return t2
        elif t2 == None:
            return t1
        new_root = TreeNode(t1.val + t2.val)
        left = self.mergeTrees(t1.left, t2.left)
        right = self.mergeTrees(t1.right, t2.right)
        new_root.left = left
        new_root.right = right
        return new_root


# V1
# IDEA : iterative
# https://leetcode.com/problems/merge-two-binary-trees/discuss/121170/Python-iterative
class Solution(object):
     def mergeTrees(self, t1, t2):
            dummy = TreeNode(0)
            stack = [(t1, t2, dummy, 'l')]
            while stack:
                n1, n2, parent, lr = stack.pop()
                n = TreeNode((n1.val if n1 else 0 ) + (n2.val if n2 else 0 )) if n1 or n2 else None
                if lr == 'l': 
                    parent.left = n 
                else:
                    parent.right = n

                if n1 or n2:
                    stack.append((n1 and n1.left, n2 and n2.left, n, 'l'))
                    stack.append((n1 and n1.right, n2 and n2.right, n, 'r'))

            return dummy.left

### Test case : dev 

# V1
# https://leetcode.com/problems/merge-two-binary-trees/solution/
# IDEA : resursive
# java
# /**
#  * Definition for a binary tree node.
#  * public class TreeNode {
#  *     int val;
#  *     TreeNode left;
#  *     TreeNode right;
#  *     TreeNode(int x) { val = x; }
#  * }
#  */
# public class Solution {
#     public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
#         if (t1 == null)
#             return t2;
#         if (t2 == null)
#             return t1;
#         t1.val += t2.val;
#         t1.left = mergeTrees(t1.left, t2.left);
#         t1.right = mergeTrees(t1.right, t2.right);
#         return t1;
#     }
# }


# V1
# https://leetcode.com/problems/merge-two-binary-trees/solution/
# IDEA : Iterative
# java
# /**
#  * Definition for a binary tree node.
#  * public class TreeNode {
#  *     int val;
#  *     TreeNode left;
#  *     TreeNode right;
#  *     TreeNode(int x) { val = x; }
#  * }
#  */
# public class Solution {
#     public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
#         if (t1 == null)
#             return t2;
#         Stack < TreeNode[] > stack = new Stack < > ();
#         stack.push(new TreeNode[] {t1, t2});
#         while (!stack.isEmpty()) {
#             TreeNode[] t = stack.pop();
#             if (t[0] == null || t[1] == null) {
#                 continue;
#             }
#             t[0].val += t[1].val;
#             if (t[0].left == null) {
#                 t[0].left = t[1].left;
#             } else {
#                 stack.push(new TreeNode[] {t[0].left, t[1].left});
#             }
#             if (t[0].right == null) {
#                 t[0].right = t[1].right;
#             } else {
#                 stack.push(new TreeNode[] {t[0].right, t[1].right});
#             }
#         }
#         return t1;
#     }
# }


# V1'
# https://www.polarxiong.com/archives/LeetCode-617-merge-two-binary-trees.html
class Solution:
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        if t1 is not None and t2 is not None:
            t1.left = self.mergeTrees(t1.left, t2.left)
            t1.right = self.mergeTrees(t1.right, t2.right)
            t1.val += t2.val
            return t1
        return t1 if t2 is None else t2

# V1''
# https://www.jiuzhang.com/solution/merge-two-binary-trees/#tag-highlight-lang-python
class Solution:
    """
    @param t1: the root of the first tree
    @param t2: the root of the second tree
    @return: the new binary tree after merge
    """
    def mergeTrees(self, t1, t2):
        # Write your code here
        if t1 is None:
            return t2
        if t2 is None:
            return t1
        t3 = TreeNode(t1.val + t2.val)
        t3.left = self.mergeTrees(t1.left, t2.left)
        t3.right = self.mergeTrees(t1.right, t2.right)
        return t3

# V1'''
# https://leetcode.com/problems/merge-two-binary-trees/discuss/124537/Python-recursive-iterative-DFS-BFS-solutions
# IDEA : BFS 
from collections import deque
class Solution:
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        if t1 is None and t2 is None:
            return
        if t1 is None:
            return t2
        if t2 is None:
            return t1
        
        q1, q2, q = deque(), deque(), deque()
        tRoot = TreeNode(t1.val + t2.val)
        t = tRoot
        q1.append(t1)
        q2.append(t2)
        q.append(t)
        
        while len(q1) > 0:
            t1, t2, t = q1.popleft(), q2.popleft(), q.popleft()
            if t1.left is None and t2.left is None:
                pass
            elif t1.left is None:
                t.left = t2.left
            elif t2.left is None:
                t.left = t1.left
            else:
                t.left = TreeNode(t1.left.val + t2.left.val)
                q1.append(t1.left)
                q2.append(t2.left)
                q.append(t.left)
            if t1.right is None and t2.right is None:
                pass
            elif t1.right is None:
                t.right = t2.right
            elif t2.right is None:
                t.right = t1.right
            else:
                t.right = TreeNode(t1.right.val + t2.right.val)
                q1.append(t1.right)
                q2.append(t2.right)
                q.append(t.right)   
        return tRoot

# V1'''''''
# https://leetcode.com/problems/merge-two-binary-trees/discuss/124537/Python-recursive-iterative-DFS-BFS-solutions
# IDEA : RECURSION PRE-ORDER
class Solution:
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        
        if t1 is None and t2 is None:
            return
        if t1 is None:
            return t2
        if t2 is None:
            return t1  
        t = TreeNode(t1.val + t2.val)
        t.left = self.mergeTrees(t1.left, t2.left)
        t.right = self.mergeTrees(t1.right, t2.right)
        return t

# V1''''''''
# https://leetcode.com/problems/merge-two-binary-trees/discuss/124537/Python-recursive-iterative-DFS-BFS-solutions
# IDEA : Recursion in-order
class Solution:
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        
        if t1 is None and t2 is None:
            return
        if t1 is None:
            return t2
        if t2 is None:
            return t1
        
        tLeft = self.mergeTrees(t1.left, t2.left)
        t = TreeNode(t1.val + t2.val)
        t.left = tLeft
        t.right = self.mergeTrees(t1.right, t2.right)
        
        return t

# V1''''''''
# https://leetcode.com/problems/merge-two-binary-trees/discuss/124537/Python-recursive-iterative-DFS-BFS-solutions
# IDEA : Recursion post-order
class Solution:
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        
        if t1 is None and t2 is None:
            return
        if t1 is None:
            return t2
        if t2 is None:
            return t1
        
        tLeft = self.mergeTrees(t1.left, t2.left)
        tRight = self.mergeTrees(t1.right, t2.right)
        t = TreeNode(t1.val + t2.val)
        t.left, t.right = tLeft, tRight
        
        return t

# V1''''''''''
# https://leetcode.com/problems/merge-two-binary-trees/discuss/124537/Python-recursive-iterative-DFS-BFS-solutions
# Iterative in-order
class Solution:
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        if t1 is None and t2 is None:
            return
        if t1 is None:
            return t2
        if t2 is None:
            return t1
        
        stack1 = []
        stack2 = []
        stack = []
        tRoot = TreeNode(t1.val + t2.val)
        t = tRoot
        while len(stack1) > 0 or t1 is not None:
            while t1 is not None and t2 is not None:
                stack1.append(t1)
                stack2.append(t2)
                stack.append(t)
                # Note: need to delay going to left if one tree is null
                if t1.left is not None and t2.left is not None:
                    t.left = TreeNode(t1.left.val + t2.left.val)
                    t = t.left
                t1, t2 = t1.left, t2.left
            if t1 is not None:
                t.left = t1
            if t2 is not None:
                t.left = t2
            t1, t2, t = stack1.pop(), stack2.pop(), stack.pop()
            if t1.right is None and t2.right is None:
                t1 = t2 = None
            elif t1.right is None:
                t.right = t2.right
                t1 = t2 = None
            elif t2.right is None:
                t.right = t1.right
                t1 = t2 = None
            else:
                t.right = TreeNode(t1.right.val + t2.right.val)
                t1, t2, t = t1.right, t2.right, t.right                
        return tRoot

# V1'''''''''''''''
# https://leetcode.com/problems/merge-two-binary-trees/discuss/124537/Python-recursive-iterative-DFS-BFS-solutions
# IDEA : Iterative pre-order
class Solution:
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        if t1 is None and t2 is None:
            return
        if t1 is None:
            return t2
        if t2 is None:
            return t1
        
        stack1 = [t1]
        stack2 = [t2]
        tRoot = TreeNode(t1.val + t2.val)
        stack = [tRoot]
        while len(stack) > 0:
            t1 = stack1.pop()
            t2 = stack2.pop()
            t = stack.pop()
            if t1.right is None and t2.right is None:
                pass
            elif t1.right is None:
                t.right = t2.right
            elif t2.right is None:
                t.right = t1.right
            else:
                t.right = TreeNode(t1.right.val + t2.right.val)
                stack1.append(t1.right)
                stack2.append(t2.right)
                stack.append(t.right)
            if t1.left is None and t2.left is None:
                pass
            elif t1.left is None:
                t.left = t2.left
            elif t2.left is None:
                t.left = t1.left
            else:
                t.left = TreeNode(t1.left.val + t2.left.val)
                stack1.append(t1.left)
                stack2.append(t2.left)
                stack.append(t.left)
        
        return tRoot

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        if t1 is None:
            return t2
        if t2 is None:
            return t1
        t1.val += t2.val
        t1.left = self.mergeTrees(t1.left, t2.left)
        t1.right = self.mergeTrees(t1.right, t2.right)
        return t1