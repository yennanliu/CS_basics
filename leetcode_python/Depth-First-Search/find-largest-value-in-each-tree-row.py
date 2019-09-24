# V0 
# IDEA : DFS 
class Solution(object):
    def largestValues(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        levels = []
        self.dfs(root, levels, 0)
        return [max(l) for l in levels]
    
    def dfs(self, root, levels, level):
        if not root: return
        if level == len(levels): levels.append([])
        levels[level].append(root.val)
        self.dfs(root.left, levels, level + 1)
        self.dfs(root.right, levels, level + 1)

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/70183314
# IDEA :  BFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def largestValues(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        queue = collections.deque()
        res = []
        queue.append(root)
        while queue:
            size = len(queue)
            max_level = float("inf")
            for i in range(size):
                node = queue.popleft()
                if not node: continue
                max_level = max(max_level, node.val)
                queue.append(node.left)
                queue.append(node.right)
            res.append(max_level)
        return res

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/70183314
# IDEA : DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def largestValues(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        levels = []
        self.dfs(root, levels, 0)
        return [max(l) for l in levels]
    
    def dfs(self, root, levels, level):
        if not root: return
        if level == len(levels): levels.append([])
        levels[level].append(root.val)
        self.dfs(root.left, levels, level + 1)
        self.dfs(root.right, levels, level + 1)

# V1''
# https://www.jiuzhang.com/solution/find-largest-value-in-each-tree-row/#tag-highlight-lang-python
# IDEA : BFS 
class Solution:
    """
    @param root: a root of integer
    @return: return a list of integer
    """
    def largestValues(self, root):
        # write your code here
        maxes = []
        row = [root]
        while any(row):
            maxes.append(max(node.val for node in row))
            row = [kid for node in row for kid in (node.left, node.right) if kid]
        return maxes

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def largestValues(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        def largestValuesHelper(root, depth, result):
            if not root:
                return
            if depth == len(result):
                result.append(root.val)
            else:
                result[depth] = max(result[depth], root.val)
            largestValuesHelper(root.left, depth+1, result)
            largestValuesHelper(root.right, depth+1, result)

        result = []
        largestValuesHelper(root, 0, result)
        return result


# Time:  O(n)
# Space: O(n)
class Solution2(object):
    def largestValues(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        result = []
        curr = [root]
        while any(curr):
            result.append(max(node.val for node in curr))
            curr = [child for node in curr for child in (node.left, node.right) if child]
        return result
