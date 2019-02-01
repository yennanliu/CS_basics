

# V1  : dev 



# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None

#     def __repr__(self):
#         if self:
#             return "{} -> {}".format(self.val, self.next)

# class Solution(object):
# 	# @param a ListNode
# 	# @return a ListNode
# 	def swapPairs(self, list_):
# 		dummy = ListNode(0)
# 		#dummy.next = head
# 		current = dummy
# 		output = []
# 		if list_.next is None:
# 			return list_
# 		while list_:
# 			if list_.next and  list_.next.next:
# 				output.append(list_.next.val)
# 				output.append(list_.val)
# 				list_=list_.next.next
# 			elif list_.next:
# 				output.append(list_.next.val)
# 				output.append(list_.val)
# 				list_=list_.next
# 			elif list_ and list_.next is None:
# 				output.append(list_.val)
# 				#current = current.next 
# 				return output 
# 		return output



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

