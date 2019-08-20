##########################################
# LeetCode – Plus One Linked List (Java)
##########################################

# Given a non-negative number represented as a singly linked list of digits, plus one to the number.

# The digits are stored such that the most significant digit is at the head of the list.
# Example:

# Input:
# 1->2->3

# Output:
# 1->2->4


# V0

# V1 
# https://www.jiuzhang.com/solution/plus-one-linked-list/#tag-highlight-lang-python

"""
Definition of ListNode
class ListNode(object):
    def __init__(self, val, next=None):
        self.val = val
        self.next = next
"""

class Solution:
    """
    @param head: the first Node
    @return: the answer after plus one
    """
    def plusOne(self, head):
        # Write your code here
        dummy = ListNode(0)
        dummy.next = head
        l = dummy
        r = dummy
        while r.next != None:
            r = r.next
            if r.val != 9:
                l = r
        
        if r.val != 9:
            r.val += 1;
        else:
            l.val += 1
            l = l.next
            while l != None:
                l.val = 0
                l = l.next
        
        if  dummy.val == 0:
            return dummy.next
        
        return dummy;

# V1'
class Solution(object):
    def plusOne(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        my_list = []
        while head:
            my_list.append(head.val)
            head = head.__next__ 
        extra = 0 
        count = 0 
        output = []
        for i in my_list[::-1]:
            if count==0:
                i = i + 1 + extra         
            i = i + extra
            if i >= 10:
                i = i%10 
                extra = 1  
            else:
                extra = 0 
            output.append(i)
            count = count + 1
        if extra==1:
            output.append(1)
        return  output[::-1]

# V2 
# https://github.com/apachecn/awesome-algorithm/blob/master/docs/Leetcode_Solutions/Python/369.Plus%20One%20Linked%20List.md
class Solution(object):
    def plusOne(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        lst = []
        cur = head 

        while cur:
            lst.append(cur)
            cur = cur.__next__

        carry = 1
        for i in range(len(lst)-1,-1,-1):
            lst[i].val += carry
            if lst[i].val < 10:
                carry = 0
                break
            else:
                lst[i].val -= 10

        if carry == 1:
            node = ListNode(1)
            node.next = head
            return node
        else:
            return head 

# V3 
# Time:  O(n)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None


# Two pointers solution.
class Solution(object):
    def plusOne(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head:
            return None

        dummy = ListNode(0)
        dummy.next = head

        left, right = dummy, head
        while right.__next__:
            if right.val != 9:
                left = right
            right = right.__next__

        if right.val != 9:
            right.val += 1
        else:
            left.val += 1
            right = left.__next__
            while right:
                right.val = 0
                right = right.__next__

        return dummy if dummy.val else dummy.__next__


# V4 
# Time:  O(n)
# Space: O(1)
class Solution2(object):
    def plusOne(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        def reverseList(head):
            dummy = ListNode(0)
            curr = head
            while curr:
                dummy.next, curr.next, curr = curr, dummy.next, curr.next
            return dummy.__next__

        rev_head = reverseList(head)
        curr, carry = rev_head, 1
        while curr and carry:
            curr.val += carry
            carry = curr.val / 10
            curr.val %= 10
            if carry and curr.__next__ is None:
                curr.next = ListNode(0)
            curr = curr.__next__

        return reverseList(rev_head)
