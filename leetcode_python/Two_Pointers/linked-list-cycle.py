"""

Given head, the head of a linked list, 
determine if the linked list has a cycle in it.

There is a cycle in a linked list if there is some node in the list 
that can be reached again by continuously following the next pointer. 
Internally, pos is used to denote the index of the node that tail's next pointer is connected to. 
Note that pos is not passed as a parameter.

Return true if there is a cycle in the linked list. Otherwise, return false.

Example 1:


Input: head = [3,2,0,-4], pos = 1
Output: true
Explanation: There is a cycle in the linked list, where the tail connects to the 1st node (0-indexed).

Example 2:


Input: head = [1,2], pos = 0
Output: true
Explanation: There is a cycle in the linked list, where the tail connects to the 0th node.

Example 3:


Input: head = [1], pos = -1
Output: false
Explanation: There is no cycle in the linked list.
 

Constraints:

The number of the nodes in the list is in the range [0, 104].
-105 <= Node.val <= 105
pos is -1 or a valid index in the linked-list.
 

Follow up: Can you solve it using O(1) (i.e. constant) memory?

"""
# V0
# IDEA : MAP
class Solution(object):
    def hasCycle(self, head):
        """
        :type head: ListNode
        :rtype: bool
        """
        map = {}
        while head:
            if id(head) in map:
                return True
            else:
                map[id(head)] = True
            head = head.next
        return False
    
# V1 
# https://blog.csdn.net/coder_orz/article/details/51516558
# IDEA : MAP 
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def hasCycle(self, head):
        """
        :type head: ListNode
        :rtype: bool
        """
        map = {}
        while head:
            if id(head) in map:
                return True
            else:
                map[id(head)] = True
            head = head.next
        return False

# V1'  
# https://blog.csdn.net/coder_orz/article/details/51516558
# IDEA  : TWO POINTER
class Solution(object):
    def hasCycle(self, head):
        """
        :type head: ListNode
        :rtype: bool
        """
        slow = fast = head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next
            if slow == fast:
                return True
        return False

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51516558
class Solution(object):
    def hasCycle(self, head):
        """
        :type head: ListNode
        :rtype: bool
        """
        if head and head.next and head == self.reverseList(head):
            return True
        return False

    def reverseList(self, head):
        before = after = None
        while head:
            after = head.next
            head.next = before
            before = head
            head = after
        return before

# V2 
# Time:  O(n)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution(object):
    # @param head, a ListNode
    # @return a boolean
    def hasCycle(self, head):
        fast, slow = head, head
        while fast and fast.__next__:
            fast, slow = fast.next.__next__, slow.__next__
            if fast == slow:
                return True
        return False


# V4 
# Definition for singly-linked list.
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution:
    # @param head, a ListNode
    # @return a boolean
    def hasCycle(self, head):
        fast, slow = head, head
        while fast and fast.__next__:
            fast, slow = fast.next.__next__, slow.__next__
            if fast is slow:
                return True
        return False

# if __name__ == "__main__":
#     head = ListNode(1)
#     head.next = ListNode(2)
#     head.next.next = ListNode(3)
#     head.next.next.next = head.next
#     print Solution().hasCycle(head)  
