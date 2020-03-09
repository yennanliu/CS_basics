# V0
# IDEA : LINKED LIST -> LIST
class Solution(object):
    def isPalindrome(self, head):
        """
        :type head: ListNode
        :rtype: bool
        """
        if not head or not head.next:
            return True

        tmp_list = []
        while head:
            tmp_list.append(head.val)
            head = head.next

        length = len(tmp_list)
        mid_ = length//2
        return tmp_list[:mid_] == tmp_list[-mid_:][::-1]

# V1
# IDEA : LINKED LIST -> LIST
# https://blog.csdn.net/coder_orz/article/details/51306985
class Solution(object):
    def isPalindrome(self, head):
        """
        :type head: ListNode
        :rtype: bool
        """
        if not head or not head.next:
            return True

        tmp_list = []
        while head:
            tmp_list.append(head.val)
            head = head.next

        length = len(tmp_list)
        for i in range(0, length/2):
            if tmp_list[i] != tmp_list[length-i-1]:
                return False
        return True

# V1'
# https://blog.csdn.net/coder_orz/article/details/51306985
class Solution(object):
    def isPalindrome(self, head):
        """
        :type head: ListNode
        :rtype: bool
        """
        if not head or not head.next:
            return True

        new_list = []

        # slow & fast pointers find the middle of the linked list 
        slow = fast = head
        while fast and fast.next:
            new_list.insert(0, slow.val)
            slow = slow.next
            fast = fast.next.next
        # to find how many nodes in the linked list    
        if fast: 
            slow = slow.next

        for val in new_list:
            if val != slow.val:
                return False
            slow = slow.next
        return True

# V1''
# https://blog.csdn.net/coder_orz/article/details/51306985
class Solution(object):
    def isPalindrome(self, head):
        """
        :type head: ListNode
        :rtype: bool
        """
        if not head or not head.next:
            return True

        # slow & fast pointers find the middle of the linked list 
        slow = fast = head
        while fast.next and fast.next.next:
            slow = slow.next
            fast = fast.next.next
        # slow points to the second half part of the linked list
        slow = slow.next 
        slow = self.reverseList(slow)

        while slow:
            if head.val != slow.val:
                return False
            slow = slow.next
            head = head.next
        return True

    def reverseList(self, head):
        new_head = None
        while head:
            p = head
            head = head.next
            p.next = new_head
            new_head = p
        return new_head

# V1'
# http://bookshadow.com/weblog/2015/07/10/leetcode-palindrome-linked-list/
class Solution:
    # @param {ListNode} head
    # @return {boolean}
    def isPalindrome(self, head):
        if head is None:
            return True
        #find mid node
        fast = slow = head
        while fast.next and fast.next.next:
            slow = slow.next
            fast = fast.next.next
        #reverse second half
        p, last = slow.next, None
        while p:
            next = p.next
            p.next = last
            last, p = p, next
        #check palindrome
        p1, p2 = last, head
        while p1 and p1.val == p2.val:
            p1, p2 = p1.next, p2.next
        #resume linked list(optional)
        p, last = last, None
        while p:
            next = p.next
            p.next = last
            last, p = p, next
        slow.next = last
        return p1 is None

# V1''
# https://blog.csdn.net/coder_orz/article/details/51306985
# Definition for singly-linked list.
class ListNode(object):
     def __init__(self, x):
         self.val = x
         self.next = None

class Solution(object):
    def isPalindrome(self, head):
        """
        :type head: ListNode
        :rtype: bool
        """
        if not head or not head.__next__:
            return True

        tmp_list = []
        while head:
            tmp_list.append(head.val)
            head = head.__next__

        length = len(tmp_list)
        for i in range(0, length/2):
            if tmp_list[i] != tmp_list[length-i-1]:
                return False
        return True

# V2
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param {ListNode} head
    # @return {boolean}
    def isPalindrome(self, head):
        reverse, fast = None, head
        # Reverse the first half part of the list.
        while fast and fast.__next__:
            fast = fast.next.__next__
            head.next, reverse, head = reverse, head, head.next

        # If the number of the nodes is odd,
        # set the head of the tail list to the next of the median node.
        tail = head.__next__ if fast else head

        # Compare the reversed first half list with the second half list.
        # And restore the reversed first half list.
        is_palindrome = True
        while reverse:
            is_palindrome = is_palindrome and reverse.val == tail.val
            reverse.next, head, reverse = head, reverse, reverse.next
            tail = tail.__next__
        return is_palindrome
