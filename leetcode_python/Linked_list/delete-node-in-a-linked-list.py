

# V1  :  dev 

# class ListNode(object):
#      def __init__(self, x):
#          self.val = x
#          self.next = None

# class Solution(object):
#     # @param {ListNode} node
#     # @return {void} Do not return anything, modify node in-place instead.
#     def deleteNode(self, node):
#     	output = []
#     	cur = node
#     	while cur:
#     		output.append(cur.val)
#     		if cur.next:
#     			cur = cur.next
#     		else:
#     			cur = cur 
#     	return [ x for x in output if x != node.val]


# V2 
# http://bookshadow.com/weblog/2015/07/15/leetcode-delete-node-linked-list/
class Solution:
    # @param {ListNode} node
    # @return {void} Do not return anything, modify node in-place instead.
    def deleteNode(self, node):
    	### ORDERING MATTER (can't change)
    	### step 1) get the value of next node 
    	### step 2) point to next node 
        node.val = node.next.val
        node.next = node.next.next


# V3 
class Solution(object):
    # @param {ListNode} node
    # @return {void} Do not return anything, modify node in-place instead.
    def deleteNode(self, node):
        if node and node.next:
            node_to_delete = node.next
            node.val = node_to_delete.val
            node.next = node_to_delete.next
            del node_to_delete
