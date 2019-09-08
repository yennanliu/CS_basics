# V0

# V1 
# http://bookshadow.com/weblog/2017/08/06/leetcode-maximum-binary-tree/
# IDEA :
# -> STEP 1) FIND THE INDEX OF MAX ELEMENT IN THE ARRAY 
# -> STEP 2) SET THE MAX ELEMENT AS ROOT 
# -> STEP 3) SPLIT THE ARRAY INTO LEFT AND RIGHT ON THE INDEX (OF MAX ELEMENT )
# -> STEP 4) SET MAX ELEMENT IN LEFT ARRAY AS LEFT SUB TEREE 
# -> STEP 5) SET MAX ELEMENT IN RIGHT ARRAY AS RIGHT SUB TEREE 
# -> REPEAT STEP  3) - 5)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def constructMaximumBinaryTree(self, nums):
        """
        :type nums: List[int]
        :rtype: TreeNode
        """
        if not nums: return None
        maxn = max(nums)
        idx = nums.index(maxn)
        node = TreeNode(maxn)
        node.left = self.constructMaximumBinaryTree(nums[:idx])
        node.right = self.constructMaximumBinaryTree(nums[idx+1:])
        return node

# V2 
# Time:  O(n)
# Space: O(n)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def constructMaximumBinaryTree(self, nums):
        """
        :type nums: List[int]
        :rtype: TreeNode
        """
        # https://github.com/kamyu104/LintCode/blob/master/C++/max-tree.cpp
        nodeStack = []
        for num in nums:
            node = TreeNode(num)
            while nodeStack and num > nodeStack[-1].val:
                node.left = nodeStack.pop()
            if nodeStack:
                nodeStack[-1].right = node
            nodeStack.append(node)
        return nodeStack[0]