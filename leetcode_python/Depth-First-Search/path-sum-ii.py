"""

113. Path Sum II
Medium

Given the root of a binary tree and an integer targetSum, return all root-to-leaf paths where the sum of the node values in the path equals targetSum. Each path should be returned as a list of the node values, not node references.

A root-to-leaf path is a path starting from the root and ending at any leaf node. A leaf is a node with no children.

 

Example 1:


Input: root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
Output: [[5,4,11,2],[5,8,4,5]]
Explanation: There are two paths whose sum equals targetSum:
5 + 4 + 11 + 2 = 22
5 + 8 + 4 + 5 = 22
Example 2:


Input: root = [1,2,3], targetSum = 5
Output: []
Example 3:

Input: root = [1,2], targetSum = 0
Output: []
 

Constraints:

The number of nodes in the tree is in the range [0, 5000].
-1000 <= Node.val <= 1000
-1000 <= targetSum <= 1000

"""

# V0
# IDEA: DFS (post order) + backtrack (GPT)
class Solution(object):
    def pathSum(self, root, targetSum):
        self.res = []

        if not root:
            return self.res

        self.helper(root, targetSum, 0, [])

        return self.res

    def helper(self, root, targetSum, cur_sum, cache):
        if not root:
            return

        cur_sum += root.val
        cache.append(root.val)

        if not root.left and not root.right and cur_sum == targetSum:
            self.res.append(cache[:])

        self.helper(root.left, targetSum, cur_sum, cache)
        self.helper(root.right, targetSum, cur_sum, cache)

        """
        NOTE !!!


        we need `Backtrack` here,
        (array is NOT primitive type, so it use the same copy in recursion)


        -> why we need Backtrack?

            ### Why `cache.pop()` is necessary

            Suppose the tree is

            ```
                1
               / \
              2   3
            ```

            Without `cache.pop()`:

            ```
            visit 1: cache = [1]
            visit 2: cache = [1,2]
            return
            visit 3: cache = [1,2,3]   # Wrong!
            ```

            With `cache.pop()`:

            ```
            visit 1: cache = [1]
            visit 2: cache = [1,2]
            return -> pop() => [1]
            visit 3: cache = [1,3]     # Correct
            ```

            This is the standard DFS backtracking pattern:

            ```python
            cache.append(...)
            dfs(...)
            dfs(...)
            cache.pop()
            ```

            * `cur_sum` does **NOT** need to be restored because integers are immutable and passed by value (reference to an immutable object).
            * `cache` **DOES** need to be restored because lists are mutable and shared across recursive calls.

        """
        # Backtrack
        cache.pop()

        """

         NOTE !!! 

        we DON'T need to do backtrack on `cur_sum`


        -> because `integers` are `immutable` in Python, 
           meaning their values CAN NOT be changed in place.


        -> 

        When you pass an integer like cur_sum into a function call, 
        Python passes it by value (technically, a copy of the reference to that integer object). 
        Any modifications you make to cur_sum inside a 
        specific function frame stay locked inside that 
        frame and its children.


        """



# V0-1
# IDEA: DFS (post order) + backtrack (gemini)
class Solution(object):
    def pathSum(self, root, targetSum):
        """
        :type root: TreeNode
        :type targetSum: int
        :rtype: List[List[int]]
        """
        self.res = []
        if not root:
            return self.res
        self.helper(root, targetSum, 0, [])
        return self.res
        
    def helper(self, root, targetSum, cur_sum, cache):
        if not root:
            return
            
        cur_sum += root.val
        cache.append(root.val)
        
        if not root.left and not root.right and cur_sum == targetSum:
            self.res.append(cache[:])
            
        self.helper(root.left, targetSum, cur_sum, cache)
        self.helper(root.right, targetSum, cur_sum, cache)
        
        # CRITICAL FIX: Backtrack! 
        # Remove the current node's value before returning up to the parent
        cache.pop()




# V0
# IDEA : DFS
class Solution(object):
    def pathSum(self, root, sum):
        if not root: return []
        res = []
        self.dfs(root, sum, res, [root.val])
        return res

    def dfs(self, root, target, res, path):
        if not root: return
        if sum(path) == target and not root.left and not root.right:
            res.append(path)
            return
        if root.left:
            self.dfs(root.left, target, res, path + [root.left.val])
        if root.right:
            self.dfs(root.right, target, res, path + [root.right.val])
            
# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80779574
# IDEA : DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: List[List[int]]
        """
        if not root: return []
        res = []
        self.dfs(root, sum, res, [root.val])
        return res

    def dfs(self, root, target, res, path):
        if not root: return
        if sum(path) == target and not root.left and not root.right:
            res.append(path)
            return
        if root.left:
            self.dfs(root.left, target, res, path + [root.left.val])
        if root.right:
            self.dfs(root.right, target, res, path + [root.right.val])

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80779574
# IDEA : DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: List[List[int]]
        """
        res = []
        self.dfs(root, sum, res, [])
        return res

    def dfs(self, root, target, res, path):
        if not root: return
        path += [root.val]
        if sum(path) == target and not root.left and not root.right:
            res.append(path[:])
            return
        if root.left:
            self.dfs(root.left, target, res, path[:])
        if root.right:
            self.dfs(root.right, target, res, path[:])
        path.pop(-1)

# V1''
# https://www.jiuzhang.com/solution/path-sum-ii/#tag-highlight-lang-python
class Solution:
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: List[List[int]]
        """
        # sum is overwriter, so new function my sum ....
        def mysum(nums):
            count = 0
            for n in nums:
                count += n
            return count
            
        # dfs find each path
        def findPath(root, path):
            if root.left is None and root.right is None:
                if mysum(path + [root.val]) == sum: 
                    allPath.append([t for t in path + [root.val]])      
            if root.left: findPath(root.left, path + [root.val])
            if root.right: findPath(root.right, path + [root.val])    
        allPath = []
        if root: findPath(root, [])
        return allPath

# V2 
# Time:  O(n)
# Space: O(h), h is height of binary tree
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param root, a tree node
    # @param sum, an integer
    # @return a list of lists of integers
    def pathSum(self, root, sum):
        return self.pathSumRecu([], [], root, sum)


    def pathSumRecu(self, result, cur, root, sum):
        if root is None:
            return result

        if root.left is None and root.right is None and root.val == sum:
            result.append(cur + [root.val])
            return result

        cur.append(root.val)
        self.pathSumRecu(result, cur, root.left, sum - root.val)
        self.pathSumRecu(result, cur,root.right, sum - root.val)
        cur.pop()
        return result
