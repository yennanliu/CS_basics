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
class Solution(object):
    def reverseBetween(self, head, left, right):
        """
        :type head: Optional[ListNode]
        :type left: int
        :type right: int
        :rtype: Optional[ListNode]
        """
        pass


# V1-1
# IDEA: LINKED LIST (gemini)
class Solution(object):
    def reverseBetween(self, head, left, right):
        """
        :type head: ListNode
        :type left: int
        :type right: int
        :rtype: ListNode
        """
        # Edge case: Empty list or no reversal needed
        if not head or left == right:
            return head
            
        dummy = ListNode(0)
        dummy.next = head
        
        # 'prev' will be the node immediately BEFORE the reversed section
        prev = dummy
        
        # 1. Walk 'prev' forward (left - 1) steps
        for _ in range(left - 1):
            prev = prev.next
            
        # 'curr' is the first node that will be reversed.
        # It will eventually become the tail of the reversed section!
        curr = prev.next
        
        # 2. Reverse the sublist using the "insertion" method
        for _ in range(right - left):
            # The node we are about to move to the front
            next_node = curr.next 
            
            # Step A: 'curr' skips over 'next_node' to point to the rest of the list
            curr.next = next_node.next
            
            # Step B: 'next_node' points backwards to the current front of the sublist
            next_node.next = prev.next
            
            # Step C: 'prev' locks 'next_node' in as the new front of the sublist
            prev.next = next_node
            
        return dummy.next



# V1-2
# IDEA: LINKED LIST (gpt)
class Solution(object):
    def reverseBetween(self, head, left, right):
        # edge
        if not head or left == right:
            return head

        dummy = ListNode(0)
        dummy.next = head

        # Move `prev` to the node before position `left`
        prev = dummy
        for _ in range(left - 1):
            prev = prev.next

        # Start of the sublist to reverse
        start = prev.next

        # Reverse k nodes
        new_head, new_tail, next_node = self.reverse_helper(
            start, right - left + 1
        )

        # Reconnect
        prev.next = new_head
        new_tail.next = next_node

        return dummy.next

    def reverse_helper(self, head, k):
        prev = None
        curr = head

        while curr and k > 0:
            nxt = curr.next
            curr.next = prev
            prev = curr
            curr = nxt
            k -= 1

        # prev = new head of reversed list
        # head = new tail (original head)
        # curr = first node after reversed segment
        return prev, head, curr



# V1
# IDEA : Iterative Link Reversal
# https://leetcode.com/problems/reverse-linked-list-ii/solution/
# time = O(n)
# space = O(1)
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
# time = O(n)
# space = O(n) (recursion stack)
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
# time = O(n)
# space = O(1)
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
# time = O(n)
# space = O(1)
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
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.__next__))
# time = O(n)
# space = O(1)
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
