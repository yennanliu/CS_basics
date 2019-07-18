# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82084204
# https://zxi.mytechroad.com/blog/tree/leetcode-894-all-possible-full-binary-trees/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def allPossibleFBT(self, N):
        """
        :type N: int
        :rtype: List[TreeNode]
        """
        N -= 1
        if N == 0: return [TreeNode(0)]
        res = []
        for l in range(1, N, 2):
            for left in self.allPossibleFBT(l):
                for right in self.allPossibleFBT(N - l):
                    node = TreeNode(0)
                    node.left = left
                    node.right = right
                    res.append(node)
        return res

# V2 
# Time:  O(n * 4^n / n^(3/2)) ~= sum of Catalan numbers from 1 .. N
# Space: O(n * 4^n / n^(3/2)) ~= sum of Catalan numbers from 1 .. N
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Solution(object):
    def __init__(self):
        self.__memo = {1: [TreeNode(0)]}
    
    def allPossibleFBT(self, N):
        """
        :type N: int
        :rtype: List[TreeNode]
        """
        if N % 2 == 0:
            return []

        if N not in self.__memo:
            result = []
            for i in range(N):
                for left in self.allPossibleFBT(i):
                    for right in self.allPossibleFBT(N-1-i):
                        node = TreeNode(0)
                        node.left = left
                        node.right = right
                        result.append(node)
            self.__memo[N] = result

        return self.__memo[N]
