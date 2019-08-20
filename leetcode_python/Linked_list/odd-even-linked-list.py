"""
Given a singly linked list, group all odd nodes together followed by the even nodes. Please note here we are talking about the node number and not the value in the nodes.

You should try to do it in place. The program should run in O(1) space complexity and O(nodes) time complexity.

Example:
Given 1->2->3->4->5->NULL,
return 1->3->5->2->4->NULL.
1
2
3
Note:

The relative order inside both the even and odd groups should remain as it was in the input.
The first node is considered odd, the second node even and so on …
"""
# V0 
class Solution(object):
    def oddEvenList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        odd = ListNode(0)
        even = ListNode(0)
        oddHead, evenHead = odd, even
        index = 0
        while head:
            if index & 1 == 0:
                odd.next = head
                odd = odd.next
            else:
                even.next = head
                even = even.next
            head = head.next
            index += 1
        even.next = None
        odd.next = evenHead.next
        return oddHead.next

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79569396
class Solution(object):
    def oddEvenList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        odd = ListNode(0)
        even = ListNode(0)
        oddHead, evenHead = odd, even
        index = 0
        while head:
            if index & 1 == 0:
                odd.next = head
                odd = odd.next
            else:
                even.next = head
                even = even.next
            head = head.next
            index += 1
        even.next = None
        odd.next = evenHead.next
        return oddHead.next

# V2 : DEV 
# https://blog.csdn.net/fuxuemingzhu/article/details/79569396
class ListNode:  
    def __init__(self, data):
        "constructor to initiate this object"

        # store data
        self.data = data

        # store reference (next item)
        self.next = None
        return

    def has_value(self, value):
        "method to compare the value with the node data"
        if self.data == value:
            return True
        else:
            return False
            
class Solution(object):
    def oddEvenList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        odd = ListNode(0)
        even = ListNode(0)
        oddHead, evenHead = odd, even
        index = 0
        while head:
            if index & 1 == 0:
                odd.next = head
                odd = odd.__next__
            else:
                even.next = head
                even = even.__next__
            head = head.__next__
            index += 1
        even.next = None
        odd.next = evenHead.__next__
        return oddHead.__next__

# V3  
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def oddEvenList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if head:
            odd_tail, cur = head, head.__next__
            while cur and cur.__next__:
                even_head = odd_tail.__next__
                odd_tail.next = cur.__next__
                odd_tail = odd_tail.__next__
                cur.next = odd_tail.__next__
                odd_tail.next = even_head
                cur = cur.__next__
        return head
