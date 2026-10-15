"""

83. Remove Duplicates from Sorted List
Easy

Given the head of a sorted linked list, delete all duplicates such that each element appears only once. Return the linked list sorted as well.

 

Example 1:


Input: head = [1,1,2]
Output: [1,2]
Example 2:


Input: head = [1,1,2,3,3]
Output: [1,2,3]
 

Constraints:

The number of nodes in the list is in the range [0, 300].
-100 <= Node.val <= 100
The list is guaranteed to be sorted in ascending order.

"""

# V0
# IDEA: LINKED LIST OP (GPT)
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: Optional[ListNode]
        :rtype: Optional[ListNode]
        """
        # Edge case
        if not head:
            return head

        curr = head

        """
        NOTE !!!


        1. ONLY move `cur` node

        2. if - else case

            - if curr.val == curr.next.val

            - else
        """
        while curr and curr.next:
            if curr.val == curr.next.val:

                """
                NOTE !!!

                1. if, else op are NOT symmetric
                    - if
                        curr.next = curr.next.next
                    - else
                        curr = curr.next


                
                2. 
                
                `re-connect` next to next.next node

                -> `curr.next = curr.next.next`


                NOTE !!!

                NOT `curr = curr.next.next`
                """
                # Remove the next node
                curr.next = curr.next.next
            else:
                # Move forward only when values differ
                curr = curr.next

        return head


# V0-0-1

class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: Optional[ListNode]
        :rtype: Optional[ListNode]
        """
        # Edge case
        if not head or not head.next:
            return head

        res = ListNode(0)
        res.next = head

        while head and head.next:
            if head.val == head.next.val:
                # Remove the next node
                head.next = head.next.next
            else:
                # Move forward only when values differ
                head = head.next

        return res.next


# V0-1
# IDEA: LINKED LIST OP (GEMINI)
class Solution(object):

  def deleteDuplicates(self, head):
    """:type head: Optional[ListNode]

    :rtype: Optional[ListNode]
    """
    curr = head

    # 當前節點與下一個節點皆存在時進行檢查
    while curr and curr.next:
      if curr.val == curr.next.val:
        # 發現重複，跳過下一個重複的節點
        curr.next = curr.next.next
      else:
        # 沒有重複，指針正常往前推進
        curr = curr.next

    return head


# V1 
# https://blog.csdn.net/coder_orz/article/details/51506143
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
# time = O(n)
# space = O(1)
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        p = head
        while p:
            if p.next and p.next.val == p.val:
                p.next = p.next.next
            else:
                p = p.next
        return head

# V1'
# https://blog.csdn.net/coder_orz/article/details/51506143
# time = O(n)
# space = O(1)
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        p = q = head
        while p:
            while q and q.val == p.val:
                q = q.next
            p.next = q
            p = q
        return head

# V1''
# https://blog.csdn.net/coder_orz/article/details/51506143
# IDEA : ITERATION
# time = O(n)
# space = O(n) (recursion stack)
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head or not head.next:
            return head
        head.next = self.deleteDuplicates(head.next)
        return head if head.val != head.next.val else head.next

# V2
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None


# time = O(n)
# space = O(1)
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        cur = head
        while cur:
            runner = cur.__next__
            while runner and runner.val == cur.val:
                runner = runner.__next__
            cur.next = runner
            cur = runner
        return head

    # time = O(n)
    # space = O(n) (recursion stack)
    def deleteDuplicates2(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head: return head
        if head.__next__:
            if head.val == head.next.val:
                head = self.deleteDuplicates2(head.__next__)
            else:
                head.next = self.deleteDuplicates2(head.next)
        return head