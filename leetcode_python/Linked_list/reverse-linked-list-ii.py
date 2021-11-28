"""

92. Reverse Linked List II
Medium

Given the head of a singly linked list and two integers left and right where left <= right, reverse the nodes of the list from position left to position right, and return the reversed list.

 

Example 1:


Input: head = [1,2,3,4,5], left = 2, right = 4
Output: [1,4,3,2,5]
Example 2:

Input: head = [5], left = 1, right = 1
Output: [5]
 

Constraints:

The number of nodes in the list is n.
1 <= n <= 500
-500 <= Node.val <= 500
1 <= left <= right <= n
 

Follow up: Could you do it in one pass?

"""

# V0

# V1
# IDEA : Iterative Link Reversal
# https://leetcode.com/problems/reverse-linked-list-ii/solution/
class Solution:
    def reverseBetween(self, head, m, n):
        """
        :type head: ListNode
        :type m: int
        :type n: int
        :rtype: ListNode
        """

        # Empty list
        if not head:
            return None

        # Move the two pointers until they reach the proper starting point
        # in the list.
        cur, prev = head, None
        while m > 1:
            prev = cur
            cur = cur.next
            m, n = m - 1, n - 1

        # The two pointers that will fix the final connections.
        tail, con = cur, prev

        # Iteratively reverse the nodes until n becomes 0.
        while n:
            third = cur.next
            cur.next = prev
            prev = cur
            cur = third
            n -= 1

        # Adjust the final connections as explained in the algorithm
        if con:
            con.next = prev
        else:
            head = prev
        tail.next = cur
        return head

# V1'
# IDEA : Recursion
# https://leetcode.com/problems/reverse-linked-list-ii/solution/
class Solution:
    def reverseBetween(self, head, m, n):
        """
        :type head: ListNode
        :type m: int
        :type n: int
        :rtype: ListNode
        """

        if not head:
            return None

        left, right = head, head
        stop = False
        def recurseAndReverse(right, m, n):
            nonlocal left, stop

            # base case. Don't proceed any further
            if n == 1:
                return

            # Keep moving the right pointer one step forward until (n == 1)
            right = right.next

            # Keep moving left pointer to the right until we reach the proper node
            # from where the reversal is to start.
            if m > 1:
                left = left.next

            # Recurse with m and n reduced.
            recurseAndReverse(right, m - 1, n - 1)

            # In case both the pointers cross each other or become equal, we
            # stop i.e. don't swap data any further. We are done reversing at this
            # point.
            if left == right or right.next == left:
                stop = True

            # Until the boolean stop is false, swap data between the two pointers     
            if not stop:
                left.val, right.val = right.val, left.val

                # Move left one step to the right.
                # The right pointer moves one step back via backtracking.
                left = left.next           

        recurseAndReverse(right, m, n)
        return head

# V1''
# http://bookshadow.com/weblog/2015/01/29/leetcode-reverse-linked-list-ii/
# IDEA : dummyNode
# Definition for singly-linked list.
# PATTERN OF REVERSE LINKED LIST
# def reverse(head):
#     p = head
#     start = None
#     while p
#         next = p.next
#         p.next = start
#         start = p
#         p = next
#     return start  
# class ListNode:
#    def __init__(self, x):
#        self.val = x
#        self.next = None
class Solution:
    def reverseBetween(self, head, m, n):
        dummyNode = ListNode(0)
        p = dummyNode
        q = head

        for x in range(m - 1):
            p.next = q
            q = q.next
            p = p.next

        start = None
        end = q
        next = None
        for x in range(m, n + 1):
            next = q.next
            q.next = start
            start = q
            q = next

        p.next = start
        end.next = next
        return dummyNode.next

# V1'
# https://www.jiuzhang.com/solution/reverse-linked-list-ii/#tag-highlight-lang-python
from lintcode import ListNode
class Solution:

    def reverse(self, head):
        prev = None
        while head != None:
            next = head.next
            head.next = prev
            prev = head
            head = next
        return prev

    def findkth(self, head, k):
        for i in xrange(k):
            if head is None:
                return None
            head = head.next
        return head

    def reverseBetween(self, head, m, n):
        dummy = ListNode(-1, head)
        mth_prev = self.findkth(dummy, m - 1)
        mth = mth_prev.next
        nth = self.findkth(dummy, n)
        nth_next = nth.next
        nth.next = None

        self.reverse(mth)
        mth_prev.next = nth
        mth.next = nth_next
        return dummy.next

# V2 
# Time:  O(n)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.__next__))
class Solution(object):
    def reverseBetween(self, head, m, n):
        diff, dummy, cur = n - m + 1, ListNode(-1), head
        dummy.next = head

        last_unswapped = dummy
        while cur and m > 1:
            cur, last_unswapped, m = cur.__next__, cur, m - 1

        prev, first_swapped = last_unswapped,  cur
        while cur and diff > 0:
            cur.next, prev, cur, diff = prev, cur, cur.next, diff - 1

        last_unswapped.next, first_swapped.next = prev, cur

        return dummy.__next__