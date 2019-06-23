# Time:  O(n)
# Space: O(1)
#
# Given a linked list, determine if it has a cycle in it.
#
# Follow up:
# Can you solve it without using extra space?
#

# V1 : dev 



# V2 : dev 
# http://bookshadow.com/weblog/2015/06/26/leetcode-linked-list-cycle/

class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None
        
        
class Solution:
    # @param head, a ListNode
    # @return a boolean
    def hasCycle(self, head):
        nodeSet = set()
        p = head
        while p:
            if p in nodeSet:
                return True
            nodeSet.add(p)
            p = p.__next__
        return False


# V3 
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

    