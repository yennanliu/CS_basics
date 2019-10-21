# V0
class Solution(object):
    def sortedArrayToBST(self, nums):
        """
        :type nums: List[int]
        :rtype: TreeNode
        """
        if not nums: return None
        _len = len(nums)
        mid = _len // 2
        root = TreeNode(nums[mid])
        root.left = self.sortedArrayToBST(nums[:mid])
        root.right = self.sortedArrayToBST(nums[mid+1:])
        return root

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/70665213
# IDEA : BST 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def sortedArrayToBST(self, nums):
        """
        :type nums: List[int]
        :rtype: TreeNode
        """
        if not nums: return None
        _len = len(nums)
        mid = _len // 2
        root = TreeNode(nums[mid])
        root.left = self.sortedArrayToBST(nums[:mid])
        root.right = self.sortedArrayToBST(nums[mid+1:])
        return root

# V1'
# https://www.jiuzhang.com/solution/convert-sorted-list-to-binary-search-tree/#tag-highlight-lang-python
class Solution:
    """
    @param head: The first node of linked list.
    @return: a tree node
    """
    def sortedListToBST(self, head):
        # write your code here
        res = self.dfs(head)
        return res       
    def dfs(self, head):
        
        if head == None:
            return None
        
        if head.next == None:
            return TreeNode(head.val)      
        dummy = ListNode(0)
        dummy.next = head
        fast = head
        slow = dummy       
        while fast != None and fast.next != None:
            fast = fast.next.next
            slow = slow.next
        
        temp = slow.next
        slow.next = None
        parent = TreeNode(temp.val)       
        parent.left = self.dfs(head)
        parent.right = self.dfs(temp.next)      
        return parent

# V2 
# Time:  O(n)
# Space: O(logn)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def sortedArrayToBST(self, nums):
        """
        :type nums: List[int]
        :rtype: TreeNode
        """
        return self.sortedArrayToBSTRecu(nums, 0, len(nums))

    def sortedArrayToBSTRecu(self, nums, start, end):
        if start == end:
            return None
        mid = start + self.perfect_tree_pivot(end - start)
        node = TreeNode(nums[mid])
        node.left = self.sortedArrayToBSTRecu(nums, start, mid)
        node.right = self.sortedArrayToBSTRecu(nums, mid + 1, end)
        return node

    def perfect_tree_pivot(self, n):
        """
        Find the point to partition n keys for a perfect binary search tree
        """
        x = 1
        # find a power of 2 <= n//2
        # while x <= n//2:  # this loop could probably be written more elegantly :)
        #     x *= 2
        x = 1 << (n.bit_length() - 1)  # use the left bit shift, same as multiplying x by 2**n-1

        if x // 2 - 1 <= (n - x):
            return x - 1  # case 1: the left subtree of the root is perfect and the right subtree has less nodes
        else:
            return n - x // 2  # case 2 == n - (x//2 - 1) - 1 : the left subtree of the root
                               # has more nodes and the right subtree is perfect.

# Time:  O(n)
# Space: O(logn)
class Solution2(object):
    def sortedArrayToBST(self, nums):
        """
        :type nums: List[int]
        :rtype: TreeNode
        """
        self.iterator = iter(nums)
        return self.helper(0, len(nums))
    
    def helper(self, start, end):
        if start == end:
            return None
        
        mid = (start + end) // 2
        left = self.helper(start, mid)
        current = TreeNode(next(self.iterator))
        current.left = left
        current.right = self.helper(mid+1, end)
        return current
