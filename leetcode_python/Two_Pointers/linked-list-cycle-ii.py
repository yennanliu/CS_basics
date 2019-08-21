# V0

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