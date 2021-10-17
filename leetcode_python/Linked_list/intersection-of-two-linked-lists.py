"""

160. Intersection of Two Linked Lists
Easy

Given the heads of two singly linked-lists headA and headB, return the node at which the two lists intersect. If the two linked lists have no intersection at all, return null.

For example, the following two linked lists begin to intersect at node c1:


The test cases are generated such that there are no cycles anywhere in the entire linked structure.

Note that the linked lists must retain their original structure after the function returns.

Custom Judge:

The inputs to the judge are given as follows (your program is not given these inputs):

intersectVal - The value of the node where the intersection occurs. This is 0 if there is no intersected node.
listA - The first linked list.
listB - The second linked list.
skipA - The number of nodes to skip ahead in listA (starting from the head) to get to the intersected node.
skipB - The number of nodes to skip ahead in listB (starting from the head) to get to the intersected node.
The judge will then create the linked structure based on these inputs and pass the two heads, headA and headB to your program. If you correctly return the intersected node, then your solution will be accepted.

 

Example 1:


Input: intersectVal = 8, listA = [4,1,8,4,5], listB = [5,6,1,8,4,5], skipA = 2, skipB = 3
Output: Intersected at '8'
Explanation: The intersected node's value is 8 (note that this must not be 0 if the two lists intersect).
From the head of A, it reads as [4,1,8,4,5]. From the head of B, it reads as [5,6,1,8,4,5]. There are 2 nodes before the intersected node in A; There are 3 nodes before the intersected node in B.
Example 2:


Input: intersectVal = 2, listA = [1,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 1
Output: Intersected at '2'
Explanation: The intersected node's value is 2 (note that this must not be 0 if the two lists intersect).
From the head of A, it reads as [1,9,1,2,4]. From the head of B, it reads as [3,2,4]. There are 3 nodes before the intersected node in A; There are 1 node before the intersected node in B.
Example 3:


Input: intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
Output: No intersection
Explanation: From the head of A, it reads as [2,6,4]. From the head of B, it reads as [1,5]. Since the two lists do not intersect, intersectVal must be 0, while skipA and skipB can be arbitrary values.
Explanation: The two lists do not intersect, so return null.
 

Constraints:

The number of nodes of listA is in the m.
The number of nodes of listB is in the n.
0 <= m, n <= 3 * 104
1 <= Node.val <= 105
0 <= skipA <= m
0 <= skipB <= n
intersectVal is 0 if listA and listB do not intersect.
intersectVal == listA[skipA] == listB[skipB] if listA and listB intersect.
 

Follow up: Could you write a solution that runs in O(n) time and use only O(1) memory?

"""

# V0
# IDEA : if the given 2 linked list have intersection, then 
#        they must overlap in SOMEWHERE if we go through
#        each of them in the same length
#        -> e.g.
#             process1 : headA -> headB -> headA ...
#             process2 : headB -> headA -> headB ...
class Solution(object):
    def getIntersectionNode(self, headA, headB):
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

# V0'
class Solution(object):
    def getIntersectionNode(self, headA, headB):
            a_pointer, b_pointer = headA, headB
            while a_pointer != b_pointer:
                a_pointer = a_pointer.next if a_pointer else headB
                b_pointer = b_pointer.next if b_pointer else headA
            return a_pointer

# V0'
# class Solution(object):
#     def getIntersectionNode(self, headA, headB):
#         """
#         :type head1, head1: ListNode
#         :rtype: ListNode
#         """
#         if not headA or not headB:
#             return None 
#         headA_array = []
#         while headA:
#             headA_array.append(headA.val)
#             headA = headA.next 
#         while headB:
#             if headB.val in headA_array:
#                 return headB.val
#             headB = headB.next 
#         return None 

# V1
# https://leetcode.com/problems/intersection-of-two-linked-lists/discuss/403600/Python-clean-solution
# IDEA : 
#  -> So if define pathA=ownA + commonC and pathB=ownB+commonC then we can travers distance of len(OwnA+commonC+ownB)=len(ownB+commonC+ownA).
#     In case of intersection we stop in the intersection node.
#     In case there is no intersection we stop at the end of the list (None).
class Solution(object):
    def getIntersectionNode(self, headA, headB):
            a_pointer, b_pointer = headA, headB
            while a_pointer != b_pointer:
                a_pointer = a_pointer.next if a_pointer else headB
                b_pointer = b_pointer.next if b_pointer else headA
            return a_pointer

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

# V1'
# https://www.jiuzhang.com/solution/intersection-of-two-linked-lists/#tag-highlight-lang-python
class Solution:
    # @param headA: the first list
    # @param headB: the second list
    # @return: a ListNode
    def getIntersectionNode(self, headA, headB):
        # Write your code here
        lenA, lenB = 0, 0
        node1, node2 = headA, headB
        while node1 is not None:
            lenA += 1
            node1 = node1.next
        while node2 is not None:
            lenB += 1
            node2 = node2.next
        
        node1, node2 = headA, headB
        while lenA > lenB:
            node1 = node1.next
            lenA -= 1
        while lenB > lenA:
            node2 = node2.next
            lenB -=1
        while node1 is not node2:
            node1 = node1.next
            node2 = node2.next
        return node1

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