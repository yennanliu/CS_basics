"""

237. Delete Node in a Linked List
Easy


Write a function to delete a node in a singly-linked list. You will not be given access to the head of the list, instead you will be given access to the node to be deleted directly.

It is guaranteed that the node to be deleted is not a tail node in the list.

 

Example 1:


Input: head = [4,5,1,9], node = 5
Output: [4,1,9]
Explanation: You are given the second node with value 5, the linked list should become 4 -> 1 -> 9 after calling your function.
Example 2:


Input: head = [4,5,1,9], node = 1
Output: [4,5,9]
Explanation: You are given the third node with value 1, the linked list should become 4 -> 5 -> 9 after calling your function.
Example 3:

Input: head = [1,2,3,4], node = 3
Output: [1,2,4]
Example 4:

Input: head = [0,1], node = 0
Output: [1]
Example 5:

Input: head = [-3,5,-99], node = -3
Output: [5,-99]
 

Constraints:

The number of the nodes in the given list is in the range [2, 1000].
-1000 <= Node.val <= 1000
The value of each node in the list is unique.
The node to be deleted is in the list and is not a tail node

"""


# V0
### NODE : we don't have the access to the whole linked list
#         -> we can only find a "tricky" way solve this problem via the given conditions 
class Solution:
    def deleteNode(self, node):
        node.val = node.next.val
        node.next = node.next.next

# V0'
# IDEA : if we have access to the linked list (this idea not works! since we can only access the to-delete node !)
# class Solution(object):
#     def deleteNode(self, node):
#         cur = head
#         while cur:
#             # case 1) cur val == node
#             _next = head.next
#             if cur.val == node:
#                 cur = _next
#             # case 2) next value == node
#             elif head.next.val == node:
#                 cur.next = _next.next
#                 cur = _next.next
#         return cur

# V1
# https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/847113/Python-Solution
class Solution:
    def deleteNode(self, node):
        node.val = node.next.val
        node.next = node.next.next

# V1'
# https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/140910/Python-
class Solution:
    def deleteNode(self, node):
        node.val = node.next.val
        node.next = node.next.next

# V1'
# http://bookshadow.com/weblog/2015/07/15/leetcode-delete-node-linked-list/
class Solution:
    # @param {ListNode} node
    # @return {void} Do not return anything, modify node in-place instead.
    def deleteNode(self, node):
        ### ORDERING MATTER (can't change)
        ### step 1) get the value of next node 
        ### step 2) point to next node 
        node.val = node.next.val
        node.next = node.next.__next__

# V2
class Solution(object):
    # @param {ListNode} node
    # @return {void} Do not return anything, modify node in-place instead.
    def deleteNode(self, node):
        if node and node.__next__:
            node_to_delete = node.__next__
            node.val = node_to_delete.val
            node.next = node_to_delete.__next__
            del node_to_delete