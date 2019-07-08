# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/85032299
# IDEA : BFS 
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
        que = collections.deque()
        que.append(root)
        hasNone = False
        while que:
            size = len(que)
            for i in range(size):
                node = que.popleft()
                if not node:
                    hasNone = True
                    continue
                if hasNone:
                    return False
                que.append(node.left)
                que.append(node.right)
        return True

# V1' 
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