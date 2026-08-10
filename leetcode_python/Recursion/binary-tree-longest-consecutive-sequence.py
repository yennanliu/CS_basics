# https://baihuqian.github.io/2018-08-01-binary-tree-longest-consecutive-sequence/
# https://leetcode.ca/all/298.html

"""

Leetcode 298: Binary Tree Longest Consecutive Sequence

Question
Given a binary tree, find the length of the longest consecutive sequence path.

The path refers to any sequence of nodes from some starting node to any node in the tree along the parent-child connections. The longest consecutive path need to be from parent to child (cannot be the reverse).

Example 1:

Input:

   1
    \
     3
    / \
   2   4
        \
         5

Output: 3

Explanation: Longest consecutive sequence path is 3-4-5, so return 3.
Example 2:

Input:

   2
    \
     3
    /
   2    
  /
 1

Output: 2

Explanation: Longest consecutive sequence path is 2-3, not 3-2-1, so return 2.

"""


"""
NOTE !!! below is WRONG



Reason:

1. Tree vs. Linear Path: 
   A consecutive sequence path only flows 
   from parent to child. Your serialization string
   "{}-{}-{}" combines a node with both its left and right subtrees. 
   This destroys the linear, top-to-bottom path 
   you actually need to check.


2. Syntax Errors: 
   self.p_map().keys() 
   will throw an error because a dictionary is not callable. 
   Also, len = 0 overwrites Python's built-in len() function,
    causing len(_list) to fail.

3. Performance: 
   String manipulation and splitting take 
   significantly more time and space than 
   simply traversing the tree.


(WRONG !!!)

```
class Solution(object):
    def longestConsecutive(self, root):
        # edge
        if not root:
            return 0
        if not root.left and not root.right:
            return 1

        self.max_len = 1
        # {path: cnt}
        self.p_map = {}

        self.helper(root)

        for p in self.p_map().keys():
            self.max_len = max(self.max_len, self.get_len(p))


        return self.max_len


     def get_len(self, path):
        if not path or path == "#":
            return 0

        _list = path.split("-")
        len = 0

        if len(_list) == 1:
            return 1

        start = 0
        for i in range(1, len(_list) - 1):
            if int(_list[i]) != int(_list[i-1]) + 1:
                return 0

        return len(_list) - 1


     def helper(self, root):
         # edge
         if not root:
            return "#"

         _left = self.helper(root.left)
         _right = self.helper(root.right)

         path = "{}-{}-{}".format(root.val, _left, _right)
         #path.append()
         
         if path not in self.p_map:
            p_map[path] = 1


         # ???
         return path
```
"""



# V0
class Solution(object):
    def longestConsecutive(self, root):
        pass


# V0-1
# IDEA: DFS (gemini)
class Solution(object):
    def longestConsecutive(self, root):
        # Edge case: empty tree
        if not root:
            return 0
            
        self.max_len = 0
        
        # Helper function for DFS traversal
        def dfs(node, parent_val, current_len):
            if not node:
                return
            
            # If the current node is consecutive to its parent, extend the streak
            if node.val == parent_val + 1:
                current_len += 1
            else:
                # Otherwise, the streak breaks. Reset to 1.
                current_len = 1
                
            # Update the global maximum length
            self.max_len = max(self.max_len, current_len)
            
            # Traverse left and right, passing down the current node's value and the streak
            dfs(node.left, node.val, current_len)
            dfs(node.right, node.val, current_len)
            
        # Start the DFS. We pass (root.val - 1) so the root always counts as length 1
        dfs(root, root.val - 1, 0)
        
        return self.max_len


# V0-2
# IDEA: DFS (gpt)
class Solution(object):
    def longestConsecutive(self, root):
        if not root:
            return 0

        self.max_len = 1

        def helper(node, prev_val, curr_len):
            if not node:
                return

            # Continue the consecutive sequence
            if node.val == prev_val + 1:
                curr_len += 1
            else:
                curr_len = 1

            self.max_len = max(self.max_len, curr_len)

            helper(node.left, node.val, curr_len)
            helper(node.right, node.val, curr_len)

        helper(root, root.val - 1, 0)

        return self.max_len


# V0
# IDEA : DFS
# time = O(n), each node visited once
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def longestConsecutive(self, root):
        if not root:
            return 0

        self.result = 0
        self.helper(root, 1)

        return self.result

    def helper(self, root, curLen):
        self.result = curLen if curLen > self.result else self.result
        if root.left:
            if root.left.val == root.val + 1:
                self.helper(root.left, curLen + 1)
            else:
                self.helper(root.left, 1)
        if root.right:
            if root.right.val == root.val + 1:
                self.helper(root.right, curLen + 1)
            else:
                self.helper(root.right, 1)

# V0'
# IDEA : BFS
# time = O(n), each node visited once
# space = O(n), stack can hold up to O(n) nodes
class Solution(object):
    def longestConsecutive(self, root):
        if root is None:
            return 0

        stack = list()
        stack.append((root, 1))
        maxLen = 1
        while len(stack) > 0:
            node, pathLen = stack.pop()
            if node.left is not None:
                if node.val + 1 == node.left.val:
                    stack.append((node.left, pathLen + 1))
                    maxLen = max(maxLen, pathLen + 1)
                else:
                    stack.append((node.left, 1))
            if node.right is not None:
                if node.val + 1 == node.right.val:
                    stack.append((node.right, pathLen + 1))
                    maxLen = max(maxLen, pathLen + 1)
                else:
                    stack.append((node.right, 1))

        return maxLen

# V0''
# IDEA : BFS
# TODO : validate this approach
# BFS
# class Solution(object):
#     def longestConsecutive(self, root):
#         # edge case
#         if not root:
#             return 0
#         cache = []
#         q = [[cache, root]]
#         res = []
#         while q:
#             for i in range(len(q)):
#                 cache, tmp = q.pop(0)
#                 if cache:
#                     if tmp.val = cache[-1] + 1:
#                         cache.append(tmp.val)
#                         res = max(res, len(cache))
#                     else:
#                         res = max(res, len(cache))
#                         cache = []
#                 if tmp.left:
#                     q.append([cache, tmp.left])
#
#                 if tmp.right:
#                     q.append([cache, tmp.right])
#         return res

# V1
# https://www.jianshu.com/p/ebabdeed9bca
# IDEA : DFS
# time = O(n), each node visited once
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def longestConsecutive(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root:
            return 0

        self.result = 0
        self.helper(root, 1)

        return self.result

    def helper(self, root, curLen):
        self.result = curLen if curLen > self.result else self.result
        if root.left:
            if root.left.val == root.val + 1:
                self.helper(root.left, curLen + 1)
            else:
                self.helper(root.left, 1)
        if root.right:
            if root.right.val == root.val + 1:
                self.helper(root.right, curLen + 1)
            else:
                self.helper(root.right, 1)

### Test case : dev

# V1'
# IDEA : BFS
# https://baihuqian.github.io/2018-08-01-binary-tree-longest-consecutive-sequence/
# time = O(n), each node visited once
# space = O(n), stack can hold up to O(n) nodes
class Solution(object):
    def longestConsecutive(self, root):
        if root is None:
            return 0

        stack = list()
        stack.append((root, 1))
        maxLen = 1
        while len(stack) > 0:
            node, pathLen = stack.pop()
            if node.left is not None:
                if node.val + 1 == node.left.val:
                    stack.append((node.left, pathLen + 1))
                    maxLen = max(maxLen, pathLen + 1)
                else:
                    stack.append((node.left, 1))
            if node.right is not None:
                if node.val + 1 == node.right.val:
                    stack.append((node.right, pathLen + 1))
                    maxLen = max(maxLen, pathLen + 1)
                else:
                    stack.append((node.right, 1))

        return maxLen

# V1'
# https://eugenejw.github.io/2017/08/leetcode-298
# IDEA : DFS
# time = O(n), each node visited once
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def __init__(self):
        self.ret = 0
        
    def longestConsecutive(self, root):
        """
        :type root: TreeNode
        :rtype:     int
        :algorithm: Pre-order DFS, O(n)
        :runtimr:   188ms
        """
        if not root:
            return 0
        self.dfs(root, 1)
        return self.ret
        
    def dfs(self, root, carry):
        """
        :type root: TreeNode
        :rtype: void
        """
        self.ret = max(self.ret, carry)
        if root.left:
            if root.left.val - 1 == root.val:
                self.dfs(root.left, carry+1)
            else:
                self.dfs(root.left, 1)

        if root.right:
            if root.right.val - 1 == root.val:
                self.dfs(root.right, carry+1)
            else:
                self.dfs(root.right, 1)

# V1
# https://blog.csdn.net/qq508618087/article/details/50883425
# JAVA
# /**
#  * Definition for a binary tree node.
#  * struct TreeNode {
#  *     int val;
#  *     TreeNode *left;
#  *     TreeNode *right;
#  *     TreeNode(int x) : val(x), left(NULL), right(NULL) {}
#  * };
#  */
# class Solution {
# public:
#     void DFS(TreeNode* root, int pre, int len, int& Max)
#     {
#         if(!root) return;
#         len = (root->val==pre+1)?len+1:1;
#         Max = max(Max, len);
#         DFS(root->left, root->val, len, Max);
#         DFS(root->right, root->val, len, Max);
#     }
#    
#     int longestConsecutive(TreeNode* root) {
#         if(!root) return 0;
#         int Max = 1;
#         DFS(root, root->val, 0, Max);
#         return Max;
#     }
# };

# V2
# time = O(n)
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def longestConsecutive(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.max_len = 0

        def longestConsecutiveHelper(root):
            if not root:
                return 0

            left_len = longestConsecutiveHelper(root.left)
            right_len = longestConsecutiveHelper(root.right)

            cur_len = 1
            if root.left and root.left.val == root.val + 1:
                cur_len = max(cur_len, left_len + 1)
            if root.right and root.right.val == root.val + 1:
                cur_len = max(cur_len, right_len + 1)

            self.max_len = max(self.max_len, cur_len)

            return cur_len

        longestConsecutiveHelper(root)
        return self.max_len
