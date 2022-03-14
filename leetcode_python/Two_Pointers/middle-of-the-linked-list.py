"""

876. Middle of the Linked List
Easy

Given the head of a singly linked list, return the middle node of the linked list.

If there are two middle nodes, return the second middle node.

 

Example 1:


Input: head = [1,2,3,4,5]
Output: [3,4,5]
Explanation: The middle node of the list is node 3.
Example 2:


Input: head = [1,2,3,4,5,6]
Output: [4,5,6]
Explanation: Since the list has two middle nodes with values 3 and 4, we return the second one.
 

Constraints:

The number of nodes in the list is in the range [1, 100].
1 <= Node.val <= 100

"""

# V0
# IDEA : fast, slow pointers + linkedlist
class Solution(object):
    def middleNode(self, head):
        # edge case
        if not head:
            return
        s = f = head
        while f and f.next:
            # if not f:
            #     break
            f = f.next.next
            s = s.next
        return s

# V1
# IDEA : OUTPUT TO ARRAY
# https://leetcode.com/problems/middle-of-the-linked-list/solution/
class Solution:
    def middleNode(self, head: ListNode) -> ListNode:
        arr = [head]
        while arr[-1].next:
            arr.append(arr[-1].next)
        return arr[len(arr) // 2]

# V2 
# Time:  O(n)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution(object):
    def middleNode(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        slow, fast = head, head
        while fast and fast.__next__:
            slow, fast = slow.__next__, fast.next.__next__
        return slow