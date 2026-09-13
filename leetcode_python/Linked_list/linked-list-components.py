"""

817. Linked List Components
Medium

You are given the head of a linked list containing unique integer values and an integer array nums that is a subset of the linked list values.

Return the number of connected components in nums. A connected component is a non-empty, maximal sequence of consecutive nodes in the linked list such that every node's value belongs to nums.

Example 1:

https://assets.leetcode.com/uploads/2021/07/22/lc-linkedlistcom1.jpg

Input: head = [0,1,2,3], nums = [0,1,3]
Output: 2
Explanation: 0 and 1 are connected, so [0, 1] and [3] are the two connected components.

Example 2:

https://assets.leetcode.com/uploads/2021/07/22/lc-linkedlistcom2.jpg

Input: head = [0,1,2,3,4], nums = [0,3,1,4]
Output: 2
Explanation: 0 and 1 are connected, 3 and 4 are connected, so [0, 1] and [3, 4] are the two connected components.

Constraints:

The number of nodes in the linked list is n.
1 <= n <= 10^4
0 <= Node.val < n
All the values Node.val are unique.
1 <= nums.length <= n
0 <= nums[i] < n
All the values of nums are unique.

"""

  # V1'
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

# time = O(m + n), m is the number of G, n is the number of nodes
# space = O(m)
class Solution(object):
    def numComponents(self, head, G):
        count = 0 
        G = set(G)
        dummy  = ListNode(-1)
        dummy.next = head 
        cur = dummy 
        while cur and cur.__next__:
            if cur.val not in G and cur.next.val in G:
                count = count + 1 
            cur = cur.__next__ 
        return count
                
  
# V0

# V2
# time = O(m + n), m is the number of G, n is the number of nodes
# space = O(m)
class Solution:
    def numComponents(self, head, G):
        """
        :type head: ListNode
        :type G: List[int]
        :rtype: int
        """
        groups = 0
        subset = set(G)
        while head:
            if head.val in subset and (not head.__next__ or head.next.val not in subset):
                groups += 1
            head = head.__next__
        return groups


# V3
# time = O(m + n), m is the number of G, n is the number of nodes
# space = O(m)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None


class Solution(object):
    def numComponents(self, head, G):
        """
        :type head: ListNode
        :type G: List[int]
        :rtype: int
        """
        lookup = set(G)
        dummy = ListNode(-1)
        dummy.next = head
        curr = dummy
        result = 0
        while curr and curr.__next__:
            if curr.val not in lookup and curr.next.val in lookup:
                result += 1
            curr = curr.__next__
        return result
