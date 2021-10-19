"""
142. Linked List Cycle II
Medium

Given the head of a linked list, return the node where the cycle begins. If there is no cycle, return null.

There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the next pointer. Internally, pos is used to denote the index of the node that tail's next pointer is connected to (0-indexed). It is -1 if there is no cycle. Note that pos is not passed as a parameter.

Do not modify the linked list.

 

Example 1:


Input: head = [3,2,0,-4], pos = 1
Output: tail connects to node index 1
Explanation: There is a cycle in the linked list, where tail connects to the second node.
Example 2:


Input: head = [1,2], pos = 0
Output: tail connects to node index 0
Explanation: There is a cycle in the linked list, where tail connects to the first node.
Example 3:


Input: head = [1], pos = -1
Output: no cycle
Explanation: There is no cycle in the linked list.
 

Constraints:

The number of the nodes in the list is in the range [0, 104].
-105 <= Node.val <= 105
pos is -1 or a valid index in the linked-list.
 

Follow up: Can you solve it using O(1) (i.e. constant) memory?

"""

# V0
# IDEA : 2 pointers + linked list basics
# https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/2_pointers.md
class Solution:
    def detectCycle(self, head):
        if not head or not head.next:
            return
        slow = fast = head
        while fast and fast.next:
            fast = fast.next.next
            slow = slow.next
            if fast == slow:
                break
        #print ("slow = " + str(slow) + " fast = " + str(fast))
        ### NOTE : via below condition check if is a cycle linked list
        if not fast or not fast.next:
            return
        ### NOTE : re-init slow as head (from starting point)
        slow = head
        ### NOTE : check while slow != fast
        while slow != fast:
            fast = fast.next
            slow = slow.next
        return slow

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79530638
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def detectCycle(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        slow, fast = head, head
        while fast and fast.next:
            fast = fast.next.next
            slow = slow.next
            if fast == slow:
                break
        if not fast or not fast.next:
            return None
        slow = head
        while slow != fast:
            slow = slow.next
            fast = fast.next
        return fast

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79530638
# IDEA : SET 
class Solution(object):
    def detectCycle(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head: return None
        visited = set()
        while head:
            if head in visited:
                return head
            visited.add(head)
            head = head.next
        return None

# V1''
# http://bookshadow.com/weblog/2015/07/10/leetcode-linked-list-cycle-ii/
class Solution:
    # @param head, a ListNode
    # @return a list node
    def detectCycle(self, head):
        if head is None or head.next is None:
            return None
        slow, fast = head.next, head.next.next
        while fast and fast.next and slow != fast:
            fast = fast.next.next
            slow = slow.next
        if fast is None or fast.next is None:
            return None
        slow = head
        while slow != fast:
            slow, fast = slow.next, fast.next
        return slow

# V2 
# https://www.cnblogs.com/zuoyuan/p/3701877.html
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution:
    # @param head, a ListNode
    # @return a list node
    def detectCycle(self, head):
        if head == None or head.__next__ == None:
            return None
        slow = fast = head
        while fast and fast.__next__:
            slow = slow.__next__
            fast = fast.next.__next__
            if fast == slow:
                break
        if slow == fast:
            slow = head
            while slow != fast:
                slow = slow.__next__
                fast = fast.__next__
            return slow
        return None

# V3 
# Time:  O(n)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __str__(self):
        if self:
            return "{}".format(self.val)
        else:
            return None

class Solution(object):
    # @param head, a ListNode
    # @return a list node
    def detectCycle(self, head):
        fast, slow = head, head
        while fast and fast.__next__:
            fast, slow = fast.next.__next__, slow.__next__
            if fast is slow:
                fast = head
                while fast is not slow:
                    fast, slow = fast.__next__, slow.__next__
                return fast
        return None