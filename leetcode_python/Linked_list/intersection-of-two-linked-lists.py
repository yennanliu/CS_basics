

# V1 : dev 

# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None

# class Solution(object):
#     # @param two ListNodes
#     # @return the intersected ListNode
#     def getIntersectionNode(self, headA, headB):
#         curA, curB = headA, headB
#         begin, tailA, tailB = None, None, None
#         if ( not headA  or  not headB):
#             return begin
#         while headA.next and headB.next:
#             if headA.val == headB.val:
#                 begin = curA
#                 break 
#             else:
#                 headA = headA.next
#                 headB = headB.next

#         return begin 

            

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

            if curA.next:
                curA = curA.next
            elif tailA is None:
                tailA = curA
                curA = headB
            else:
                break

            if curB.next:
                curB = curB.next
            elif tailB is None:
                tailB = curB
                curB = headA
            else:
                break

        return begin