


# V1  : dev 



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
