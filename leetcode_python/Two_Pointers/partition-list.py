"""

86. Partition List
Medium

Given the head of a linked list and a value x, partition it such that all nodes less than x come before nodes greater than or equal to x.

You should preserve the original relative order of the nodes in each of the two partitions.

Example 1:

https://assets.leetcode.com/uploads/2021/01/04/partition.jpg

Input: head = [1,4,3,2,5,2], x = 3
Output: [1,2,2,4,3,5]

Example 2:

Input: head = [2,1], x = 2
Output: [1,2]

Constraints:

The number of nodes in the list is in the range [0, 200].
-100 <= Node.val <= 100
-200 <= x <= 200

"""

# V0
# IDEA: 2 LINKED LIST + DUMMY NODE + RE-CONNECT (gpt)
class Solution(object):
    def partition(self, head, x):
        """
        :type head: Optional[ListNode]
        :type x: int
        :rtype: Optional[ListNode]
        """
        # Edge case
        if not head or not head.next:
            return head

        """
        NOTE !!!


        1. 
            need to setup 2 indep nodes for node 1, node 2;
            otherwise it will cause some  cyclic reference  error

    
        2. 
           dummy_1, dummy_2 for the `head` cache
        """
        # Two independent linked lists
        dummy_1 = ListNode(0)  # Nodes with val < x
        dummy_2 = ListNode(0)  # Nodes with val >= x

        """
        NOTE !!!


        list_1, list_2 are the `actual` nodes
        for the op
        """
        list_1 = dummy_1
        list_2 = dummy_2

        while head:
            if head.val < x:
                list_1.next = head
                list_1 = list_1.next
            else:
                list_2.next = head
                list_2 = list_2.next

            head = head.next

        """
        NOTE !!!

        1. make list_2 next to None
            -> 第二條 linked list 的尾端必須設定 next = None，避免保留原本的連結，造成錯誤或循環。

        2. re-connect

            list_1 end to list_2 head (dummy_2.next)
        """
        # Connect the two lists
        list_2.next = None
        list_1.next = dummy_2.next

        return dummy_1.next


# V0-1
# IDEA: LINKED LIST OP (gpt)
class Solution(object):
    def partition(self, head, x):
        """
        :type head: Optional[ListNode]
        :type x: int
        :rtype: Optional[ListNode]
        """
        # Edge case
        if not head:
            return None

        # Two linked lists:
        # list 1: nodes with values < x
        # list 2: nodes with values >= x
        dummy_1 = ListNode()
        dummy_2 = ListNode()

        p1 = dummy_1
        p2 = dummy_2

        curr = head

        while curr:
            if curr.val < x:
                p1.next = curr
                p1 = p1.next
            else:
                p2.next = curr
                p2 = p2.next

            curr = curr.next

        # Terminate the second list to avoid cycles
        p2.next = None

        """
        NOTE !!!

        below trick !! 
            -> how we re-connect nodes

            -> so we DON'T need to trace the `last nodes in p2`,
               but can just reuse the dummy_2.

        """
        # Connect the two lists
        p1.next = dummy_2.next

        return dummy_1.next


# V0-2
# IDEA: LINKED LIST OP (GEMINI)
class Solution(object):

  def partition(self, head, x):
    """:type head: Optional[ListNode]

    :type x: int
    :rtype: Optional[ListNode]
    """
    if not head:
      return None

    # 1. 建立兩個 Dummy 節點與其追蹤指標
    less_head = ListNode(0)
    greater_head = ListNode(0)

    less_ptr = less_head
    greater_ptr = greater_head

    curr = head

    # 2. 走訪原串列，將節點分流
    while curr:
      if curr.val < x:
        less_ptr.next = curr
        less_ptr = less_ptr.next
      else:
        greater_ptr.next = curr
        greater_ptr = greater_ptr.next

      curr = curr.next

    # 3. 關鍵步驟：切斷大於等於串列的尾巴，避免形成環狀鏈結 (Cycle)
    greater_ptr.next = None

    # 4. 將小於串列的尾巴接上大於串列的頭
    less_ptr.next = greater_head.next

    # 5. 回傳合併後的頭節點
    return less_head.next


# V1 
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.__next__))

# time = O(n)
# space = O(n)
class Solution(object):
    # @param head, a ListNode
    # @param x, an integer
    # @return a ListNode
    def partition(self, head, x):
        left = []
        right = []
        cur = head 
        while cur:
            if cur.val < x:
                left.append(cur.val)
            else:
                right.append(cur.val)

            cur = cur.__next__ 
        return left + right 

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
    # @param head, a ListNode
    # @param x, an integer
    # @return a ListNode
    def partition(self, head, x):
        dummySmaller, dummyGreater = ListNode(-1), ListNode(-1)
        smaller, greater = dummySmaller, dummyGreater

        while head:
            if head.val < x:
                smaller.next = head
                smaller = smaller.__next__
            else:
                greater.next = head
                greater = greater.__next__
            head = head.__next__

        smaller.next = dummyGreater.__next__
        greater.next = None

        return dummySmaller.__next__
