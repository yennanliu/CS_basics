"""

1290. Convert Binary Number in a Linked List to Integer
Easy

Given head which is a reference node to a singly-linked list.
The value of each node in the linked list is either 0 or 1.
The linked list holds the binary representation of a number.

Return the decimal value of the number in the linked list.

The most significant bit is at the head of the linked list.


Example 1:

Input: head = [1,0,1]
Output: 5
Explanation: (101) in base 2 = (5) in base 10

Example 2:

Input: head = [0]
Output: 0


Constraints:

The Linked List is not empty.
Number of nodes will not exceed 30.
Each node's value is either 0 or 1.

"""

# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next

# V0
# IDEA: LINKED LIST traversal + BIT SHIFT
#
#  -> head is the MOST significant bit, so we can build the answer
#     left to right : res = res * 2 + node.val
#     (equivalently res = (res << 1) | node.val)
#
# time = O(n)
# space = O(1)
class Solution(object):
    def getDecimalValue(self, head):
        res = 0
        while head:
            res = (res << 1) | head.val
            head = head.next
        return res
