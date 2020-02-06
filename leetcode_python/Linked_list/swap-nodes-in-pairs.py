# V0 
# IDEA : LINKED LIST
# NOTE : 
#   1) define 2 node via : n1, n2 = head.next, head.next.next
#   2) START THE PROCESS FROM "RIGHT HAND SIDE",
#      i.e. : n1.next = n2.next ( connect n1 to next node) -> connect n2 to n1 (n2.next = n1) -> connect dummy to n2 (head.next = n2)
#   3) THEN MOVE HEAD FORWARD (head = n1)
class Solution:
  def swapPairs(self, head):
    if not head or not head.next: return head
    dummy = ListNode(0)
    dummy.next = head
    head = dummy
    while head.next and head.next.next:
      n1, n2 = head.next, head.next.next
      n1.next = n2.next
      n2.next = n1
      head.next = n2      
      head = n1
    return dummy.next
    
# V1
# https://zxi.mytechroad.com/blog/list/leetcode-24-swap-nodes-in-pairs/
# VIDEO DEMO 
# https://www.youtube.com/watch?v=f45_eF1gmn8
# IDEA 
# *** EACH "SWAP" OPERATION WILL IMPLMENT ON 3 NODES
# e.g.
#  1 -> 2 -> 3 -> 4 -> 5 ----SWAP----> 2 -> 1 -> 3 -> 4 -> 5 (swap 1,2 ; but 1,2,3 are affected)
#
# SO FOR THIS PROBLEM, WE NEED TO DEFINE 3 NODES : 
# dummy (pre), cur, next 
#
# STEP 1) 
# 1 -> 2 -> 3 -> 4 -> 5  =>  dummy -> 1 -> 2 -> 3 -> 4 -> 5   
#
# STEP 2) 
# if dummy.next and dummy.next.next exist
#     dummy -> 1 -> 2 -> 3 -> 4 -> 5  =>  dummy -> 2 -> 1 -> 3 -> 4 -> 5
#     # 1 as pre node, doing next SWAP 
# STEP 3) 
# if dummy.next and dummy.next.next exist
#     dummy -> 2 -> 1 -> 3 -> 4 -> 5  =>  dummy -> 2 -> 1 -> 4 -> 3 -> 5
#     # 3 as pre node, but no pre.next or pre.next.next, SO STOP THE PROCESS
class Solution:
  def swapPairs(self, head):
    if not head or not head.next: return head
    dummy = ListNode(0)
    dummy.next = head
    head = dummy
    while head.next and head.next.next:
      n1, n2 = head.next, head.next.next
      n1.next = n2.next
      n2.next = n1
      head.next = n2      
      head = n1
    return dummy.next

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51532184
# IDEA : LINKED LIST 
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def swapPairs(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head or not head.next:
            return head
        pre = new_head = ListNode(0)
        while head and head.next:
            tmp = head.next
            head.next = tmp.next
            tmp.next = head
            pre.next = tmp
            pre = head
            head = head.next
        return new_head.next

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51532184
# IDEA : ITERATION  
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def swapPairs(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head or not head.next:
            return head
        new_head = head.next
        head.next = self.swapPairs(head.next.next)
        new_head.next = head
        return new_head

# V1''' 
# https://www.jiuzhang.com/solution/swap-nodes-in-pairs/#tag-highlight-lang-python
class Solution:
    # @param head, a ListNode
    # @return a ListNode
    def swapPairs(self, head):
        # Write your code here
        if head == None or head.next == None:
            return head
        dummy = ListNode(0)
        dummy.next = head
        p = dummy
        while p.next and p.next.next:
            tmp = p.next.next
            p.next.next = tmp.next
            tmp.next = p.next
            p.next = tmp
            p = p.next.next
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
            return "{} -> {}".format(self.val, self.next)

class Solution(object):
    # @param a ListNode
    # @return a ListNode
    def swapPairs(self, head):
        dummy = ListNode(0)
        dummy.next = head
        current = dummy
        while current.next and current.next.next:
            next_one, next_two, next_three = current.next, current.next.next, current.next.next.next
            current.next = next_two
            next_two.next = next_one
            next_one.next = next_three
            current = next_one
        return dummy.next