"""

1214. Two Sum BSTs
Medium

Given the roots of two binary search trees, root1 and root2, return true if and only if
there is a node in the first tree and a node in the second tree whose values sum up to a
given integer target.

Example 1:

Input: root1 = [2,1,4], root2 = [1,0,3], target = 5
Output: true
Explanation: 2 and 3 sum up to 5.

Example 2:

Input: root1 = [0,-10,10], root2 = [5,1,7,0,2], target = 18
Output: false


Constraints:

The number of nodes in each tree is in the range [1, 5000].
-10^9 <= Node.val, target <= 10^9

"""

# V0
# IDEA : IN-ORDER TRAVERSAL (-> sorted array) + TWO POINTERS
#        in-order walk of a BST gives a sorted list, so the classic
#        "two sum on 2 sorted arrays" two pointer scan applies:
#        left pointer on the ASC list of tree1, right pointer on the DESC end of tree2
# time = O(m + n)
# space = O(m + n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def twoSumBSTs(self, root1, root2, target):
        def inorder(node, out):
            if not node:
                return
            inorder(node.left, out)
            out.append(node.val)
            inorder(node.right, out)

        a, b = [], []
        inorder(root1, a)
        inorder(root2, b)

        i, j = 0, len(b) - 1
        while i < len(a) and j >= 0:
            s = a[i] + b[j]
            if s == target:
                return True
            elif s < target:
                i += 1
            else:
                j -= 1
        return False


# V1
# IDEA : HASH SET + BST SEARCH
#        collect every value of tree1 into a set, then for each value v of tree2
#        check whether (target - v) is in the set
# time = O(m + n)
# space = O(m)
class Solution_1(object):
    def twoSumBSTs(self, root1, root2, target):
        seen = set()

        def collect(node):
            if not node:
                return
            seen.add(node.val)
            collect(node.left)
            collect(node.right)

        def search(node):
            if not node:
                return False
            if target - node.val in seen:
                return True
            return search(node.left) or search(node.right)

        collect(root1)
        return search(root2)
