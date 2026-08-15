"""

2816. Double a Number Represented as a Linked List
Medium

You are given the head of a non-empty linked list representing a non-negative integer without leading zeroes.

Return the head of the linked list after doubling it.


Example 1:

Input: head = [1,8,9]
Output: [3,7,8]
Explanation: The figure above corresponds to the given linked list which represents the number 189. Hence, the returned linked list represents the number 189 * 2 = 378.

Example 2:

Input: head = [9,9,9]
Output: [1,9,9,8]
Explanation: The figure above corresponds to the given linked list which represents the number 999. Hence, the returned linked list represents the number 999 * 2 = 1998.


Constraints:

The number of nodes in the list is in the range [1, 10^4]
0 <= Node.val <= 9
The input is generated such that the list represents a number that does not have leading zeros, except the number 0 itself.

"""

# V0
# IDEA : SINGLE FORWARD PASS - LOOK-AHEAD CARRY (NO REVERSING NEEDED)
#
#   Doubling is special: the carry out of any digit is at most 1, and it is
#   fully decided by that digit alone (digit * 2 >= 10  <=>  digit >= 5).
#   So we never have to walk the list backwards - while sitting on node `cur`
#   we can PEEK at cur.next and know whether it will hand us a carry.
#
#     cur.val = (cur.val * 2) % 10  +  (1 if cur.next and cur.next.val >= 5 else 0)
#
#   NOTE : the only overflow into a NEW leading node happens when the first
#          digit is >= 5; prepend a 0 node up front in that case and let the
#          same rule fill it with the carry.
#
#   NOTE : this rewrites the nodes in place, so it is O(1) extra space - no
#          reverse / reverse-back and no big-integer conversion (which would
#          be O(n) digits of temporary storage).
#
# time = O(n), space = O(1)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def doubleIt(self, head):
        if head.val >= 5:
            head = ListNode(0, head)
        cur = head
        while cur:
            cur.val = (cur.val * 2) % 10
            if cur.next and cur.next.val >= 5:
                cur.val += 1
            cur = cur.next
        return head
