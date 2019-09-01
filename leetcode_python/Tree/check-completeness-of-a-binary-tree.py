# V0 
from collections import deque
class Solution:
    def isCompleteTree(self, root):
        if not root:
            return True
        q= deque([root])
        no_sub_node = False
        while q:
            node = q.popleft()
            if not node:
                no_sub_node = True
            else:
                if no_sub_node:
                    return False
                q.append(node.left)
                q.append(node.right)
        return True

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/85032299
# IDEA : BFS 
# IDEA : USE BFS GO THROUGH TREE,
# IF THERE IS ANY "NONE" AT A NODE THAT HAS SUB TREE -> RETURN FALSE (NOT a completeness binary tree)
# ELSE : STILL  a completeness binary tree 
# DEFINITION : completeness binary tree
# Definition of a complete binary tree from Wikipedia:
# In a complete binary tree every level, except possibly the last, is completely filled, and all nodes in the last level are as far left as possible. It can have between 1 and 2h nodes inclusive at the last level h.
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
import  collections
class Solution(object):
    def isCompleteTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if not root: return True
        res = []
        que = collections.deque()
        que.append(root)
        hasNone = False
        while que:
            size = len(que)
            for i in range(size):
                node = que.popleft()
                if not node:          # if there is NO sub tree, then hasNone = True is acceptable (as a completeness binary tree)
                    hasNone = True
                    continue
                if hasNone:           # if there is NO sub tree, then hasNone = True is NON acceptable (NOT a completeness binary tree)
                    return False
                que.append(node.left)
                que.append(node.right)
        return True

# V1'
# https://www.jiuzhang.com/solution/check-completeness-of-a-binary-tree/#tag-other-lang-python
# IDEA : BFS 
from collections import deque
class Solution:
    def isCompleteTree(self, root: TreeNode) -> bool:
        if not root:
            return True
        queue = deque([root])
        is_miss = False
        while queue:
            node = queue.popleft()
            if not node:
                is_miss = True
            else:
                if is_miss:
                    return False
                queue.append(node.left)
                queue.append(node.right)
        return True

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/85032299
# IDEA : DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def isCompleteTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if not root: return True
        res = []
        self.getlevel(res, 0, root)
        depth = len(res) - 1
        for d in range(depth):
            if d != depth - 1:
                if None in res[d] or len(res[d]) != (2 ** d):
                    return False
            else:
                ni = -1
                for i, n in enumerate(res[d]):
                    if n == None:
                        if ni == -1:
                            ni = i
                    else:
                        if ni != -1:
                            return False
        return True
                

    def getlevel(self, res, level, root):
        if level >= len(res):
            res.append([])
        if not root:
            res[level].append(None)
        else:
            res[level].append(root.val)
            self.getlevel(res, level + 1, root.left)
            self.getlevel(res, level + 1, root.right)

# V2 
# Time:  O(n)
# Space: O(w)
# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Solution(object):
    def isCompleteTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        end = False
        current = [root]
        while current:
            next_level = []
            for node in current:
                if not node:
                    end = True
                    continue
                if end:
                    return False
                next_level.append(node.left)
                next_level.append(node.right)
            current = next_level
        return  True

# Time:  O(n)
# Space: O(w)
class Solution2(object):
    def isCompleteTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        prev_level, current = [], [(root, 1)]
        count = 0
        while current:
            count += len(current)
            next_level = []
            for node, v in current:
                if not node:
                    continue
                next_level.append((node.left, 2*v))
                next_level.append((node.right, 2*v+1))
            prev_level, current = current, next_level
        return prev_level[-1][1] == count