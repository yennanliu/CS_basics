"""

82. Remove Duplicates from Sorted List II
Medium

You are given the head of a sorted linked list.

Delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list.

Return the linked list sorted as well.

Example 1:

https://assets.leetcode.com/uploads/2021/01/04/linkedlist1.jpg

Input: head = [1,2,3,3,4,4,5]
Output: [1,2,5]

Example 2:

https://assets.leetcode.com/uploads/2021/01/04/linkedlist2.jpg

Input: head = [1,1,1,2,3]
Output: [2,3]

Constraints:

The number of nodes in the list is in the range [0, 300].
-100 <= Node.val <= 100
The list is guaranteed to be sorted in ascending order.

"""

# Given a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list.

# Example 1:

# Input: 1->2->3->3->4->4->5
# Output: 1->2->5

# Example 2:

# Input: 1->1->1->2->3
# Output: 2->3

# V0
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: Optional[ListNode]
        :rtype: Optional[ListNode]
        """
        pass


# V0-1
# IDEA: LINKED LIST OP (gpt)

class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: Optional[ListNode]
        :rtype: Optional[ListNode]
        """
        # Dummy node handles duplicates at the head
        dummy = ListNode(0)
        dummy.next = head

        prev = dummy
        curr = head

        while curr:
            # Check whether current value is duplicated
            if curr.next and curr.val == curr.next.val:
                duplicate_val = curr.val

                # Skip every node with this duplicate value
                while curr and curr.val == duplicate_val:
                    curr = curr.next

                # Connect previous unique node to the next value
                prev.next = curr

            else:
                # Current node is unique; keep it
                prev = curr
                curr = curr.next

        return dummy.next


# V0-2
# IDEA: LINKED LIST OP (gemini)
class Solution(object):

  def deleteDuplicates(self, head):
    """:type head: Optional[ListNode]

    :rtype: Optional[ListNode]
    """
    # 邊界條件：空串列或只有一個節點
    if not head or not head.next:
      return head

    # 建立 Dummy 節點，因為 head 本身也有可能是重複而被刪除的
    dummy = ListNode(0)
    dummy.next = head
    curr = dummy

    while curr.next and curr.next.next:
      # 如果接下來的兩個節點值相同，代表有重複
      if curr.next.val == curr.next.next.val:
        duplicate_val = curr.next.val
        # 持續往後找，將所有數值等於 duplicate_val 的節點全部拔除
        while curr.next and curr.next.val == duplicate_val:
          curr.next = curr.next.next
      else:
        # 沒有重複，指標正常往後前進
        curr = curr.next

    return dummy.next


# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80786545
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
# time = O(n)
# space = O(n)
class Solution:
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        root = ListNode(0)
        root.next = head
        val_list = []
        while head:
            val_list.append(head.val)
            head = head.next
        counter = collections.Counter(val_list)
        head = root
        while head and head.next:
            if counter[head.next.val] != 1:
                head.next = head.next.next
            else:
                head = head.next
        return root.next

# V2
# time = O(n)
# space = O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self is None:
            return "Nil"
        else:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        dummy = ListNode(0)
        pre, cur = dummy, head
        while cur:
            if cur.__next__ and cur.next.val == cur.val:
                val = cur.val
                while cur and cur.val == val:
                    cur = cur.__next__
                pre.next = cur
            else:
                pre.next = cur
                pre = cur
                cur = cur.__next__
        return dummy.__next__
