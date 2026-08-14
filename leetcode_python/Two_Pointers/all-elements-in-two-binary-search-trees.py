"""

1305. All Elements in Two Binary Search Trees
Medium

Given two binary search trees root1 and root2, return a list containing all the integers
from both trees sorted in ascending order.


Example 1:

Input: root1 = [2,1,4], root2 = [1,0,3]
Output: [0,1,1,2,3,4]

Example 2:

Input: root1 = [1,null,8], root2 = [8,1]
Output: [1,1,8,8]


Constraints:

The number of nodes in each tree is in the range [0, 5000].
-10^5 <= Node.val <= 10^5

"""

# V0
# IDEA : INORDER + TWO POINTERS MERGE (never re-sort)
#
#   an inorder walk of a BST already yields a sorted list, so :
#     1) inorder(root1) -> sorted list a
#     2) inorder(root2) -> sorted list b
#     3) merge a and b with two pointers, exactly like merge sort's merge step
#
#   NOTE : sorting the concatenation would cost O((m+n) log(m+n));
#          the merge exploits the BST order and stays linear.
#
# time = O(m + n), space = O(m + n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def getAllElements(self, root1, root2):
        def inorder(node, out):
            if not node:
                return
            inorder(node.left, out)
            out.append(node.val)
            inorder(node.right, out)

        a, b = [], []
        inorder(root1, a)
        inorder(root2, b)

        res = []
        i, j = 0, 0
        while i < len(a) and j < len(b):
            if a[i] <= b[j]:
                res.append(a[i])
                i += 1
            else:
                res.append(b[j])
                j += 1
        res.extend(a[i:])
        res.extend(b[j:])
        return res
