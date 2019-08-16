# V0
class Solution(object):
    def getIntersectionNode(self, headA, headB):
        """
        :type head1, head1: ListNode
        :rtype: ListNode
        """
        if not headA or not headB:
            return None 
        headA_array = []
        while headA:
            headA_array.append(headA.val)
            headA = headA.next 
        while headB:
            if headB.val in headA_array:
                return headB.val
            headB = headB.next 
        return None 

# V1 
# https://blog.csdn.net/coder_orz/article/details/51615444
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def getIntersectionNode(self, headA, headB):
        """
        :type head1, head1: ListNode
        :rtype: ListNode
        """
        if not headA or not headB:
            return None
        p, q = headA, headB
        while p and q and p != q:
            p = p.next
            q = q.next
            if p == q:
                return p
            if not p:
                p = headB
            if not q:
                q = headA
        return p

# V2 
# Time:  O(m + n)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution(object):
    # @param two ListNodes
    # @return the intersected ListNode
    def getIntersectionNode(self, headA, headB):
        curA, curB = headA, headB
        begin, tailA, tailB = None, None, None

        # a->c->b->c
        # b->c->a->c
        while curA and curB:
            if curA == curB:
                begin = curA
                break

            if curA.__next__:
                curA = curA.__next__
            elif tailA is None:
                tailA = curA
                curA = headB
            else:
                break

            if curB.__next__:
                curB = curB.__next__
            elif tailB is None:
                tailB = curB
                curB = headA
            else:
                break
        return begin