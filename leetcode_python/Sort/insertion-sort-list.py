# V0 


# V1 
# https://www.jiuzhang.com/solution/insertion-sort-list/#tag-highlight-lang-python
"""
Definition of ListNode
class ListNode(object):

    def __init__(self, val, next=None):
        self.val = val
        self.next = next
"""
class Solution:
    """
    @param head: The first node of linked list.
    @return: The head of linked list.
    """ 
    def insertionSortList(self, head):
        # write your code here
        dummy = ListNode(0)

        while head:
            temp = dummy
            next = head.next
            while temp.next and temp.next.val < head.val:
                temp = temp.next

            head.next = temp.next
            temp.next = head
            head = next
        
        return dummy.next

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80785630
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class TreeNode:
    def __init__(self, data):
        self.data = data
        self.lLink = None
        self.rLink = None

class Solution(object):
    def insertionSortList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head or not head.__next__: return head
        root = TreeNode(0)
        root.next = head
        while head.__next__:
            if head.val <= head.next.val:
                head = head.__next__
            else:
                temp = head.__next__
                q = root
                head.next = head.next.__next__
                while q.__next__ and q.next.val < temp.val:
                    q = q.__next__
                temp.next = q.__next__
                q.next = temp
        return root.__next__

# V1''
# https://blog.csdn.net/aliceyangxi1987/article/details/50752090
class Solution(object):
    def insertionSortList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        
        if head is None or head.next is None:
            return head
        
        dummy=ListNode(0)
        dummy.next=head
        cur=head
        
        while cur.next:
            
            if cur.val>cur.next.val:
            
                pre=dummy
                while pre.next.val<cur.next.val:
                    pre=pre.next
                m=cur.next
                cur.next=m.next
                m.next=pre.next
                pre.next=m
            else:
                cur=cur.next
        
        return dummy.next
            
# V2 
# Time:  O(n ^ 2)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.__next__))
        else:
            return "Nil"

class Solution(object):
    # @param head, a ListNode
    # @return a ListNode
    def insertionSortList(self, head):
        if head is None or self.isSorted(head):
            return head

        dummy = ListNode(-2147483648)
        dummy.next = head
        cur, sorted_tail = head.__next__, head
        while cur:
            prev = dummy
            while prev.next.val < cur.val:
                prev = prev.__next__
            if prev == sorted_tail:
                cur, sorted_tail = cur.__next__, cur
            else:
                cur.next, prev.next, sorted_tail.next = prev.next, cur, cur.next
                cur = sorted_tail.__next__

        return dummy.__next__

    def isSorted(self, head):
        while head and head.__next__:
            if head.val > head.next.val:
                return False
            head = head.__next__
        return True
