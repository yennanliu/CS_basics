"""

108. Convert Sorted Array to Binary Search Tree
Solved
Easy
Topics
premium lock icon
Companies
Given an integer array nums where the elements are sorted in ascending order, convert it to a height-balanced binary search tree.

 

Example 1:


Input: nums = [-10,-3,0,5,9]
Output: [0,-3,9,-10,null,5]
Explanation: [0,-10,5,null,-3,null,9] is also accepted:

Example 2:


Input: nums = [1,3]
Output: [3,1]
Explanation: [1,null,3] and [3,1] are both height-balanced BSTs.
 

Constraints:

1 <= nums.length <= 104
-104 <= nums[i] <= 104
nums is sorted in a strictly increasing order.
 


"""


# V0
# IDEA: DFS + BST property + mid idx -> get root (gemini)
class Solution(object):
    def sortedArrayToBST(self, nums):
        """
        :type nums: List[int]
        :rtype: Optional[TreeNode]
        """
        if not nums:
            return None

        return self.helper(nums, 0, len(nums) - 1)

    def helper(self, nums, l_idx, r_idx):
        # No elements
        if l_idx > r_idx:
            return None

        # Middle element becomes the root
        idx = (l_idx + r_idx) // 2

        root = TreeNode(nums[idx])

        # Build left subtree
        root.left = self.helper(nums, l_idx, idx - 1)

        # Build right subtree
        root.right = self.helper(nums, idx + 1, r_idx)

        return root


# V0-1
# IDEA: DFS + BST property + mid idx -> get root (GPT)
class Solution(object):
    def sortedArrayToBST(self, nums):
        """
        :type nums: List[int]
        :rtype: Optional[TreeNode]
        """
        if not nums:
            return None

        return self.helper(nums, 0, len(nums) - 1)

    def helper(self, nums, l_idx, r_idx):
        # No elements
        if l_idx > r_idx:
            return None

        # Middle element becomes the root
        idx = (l_idx + r_idx) // 2

        root = TreeNode(nums[idx])

        # Build left subtree
        root.left = self.helper(nums, l_idx, idx - 1)

        # Build right subtree
        root.right = self.helper(nums, idx + 1, r_idx)

        return root


# V0
# time = O(n log n), slicing costs O(n) total per level, O(log n) levels
# space = O(n), slices along recursion path sum to O(n)
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
# time = O(n log n), slicing costs O(n) total per level, O(log n) levels
# space = O(n), slices along recursion path sum to O(n)
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
# time = O(n log n), finding middle via slow/fast pointer costs O(n) per level, O(log n) levels
# space = O(log n), recursion stack depth
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
# time = O(n)
# space = O(logn)
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

# time = O(n)
# space = O(logn)
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
