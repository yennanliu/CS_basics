
 
# V1 
class Solution(object):
	def oddEvenList(self, head):
		"""
		:type head: ListNode
		:rtype: ListNode
		"""
		output_list = []
		if head:
			while head:
				output_list.append(head.val)
				head = head.__next__ 
		return [ v  for k,v in enumerate(output_list)  if k%2 ==0 ] +  [ v  for k,v in enumerate(output_list)  if k%2 ==1 ] 


# V2 : DEV 
# https://blog.csdn.net/fuxuemingzhu/article/details/79569396
class Solution(object):
    def oddEvenList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        odd = ListNode(0)
        even = ListNode(0)
        oddHead, evenHead = odd, even
        index = 0
        while head:
            if index & 1 == 0:
                odd.next = head
                odd = odd.__next__
            else:
                even.next = head
                even = even.__next__
            head = head.__next__
            index += 1
        even.next = None
        odd.next = evenHead.__next__
        return oddHead.__next__

# V3  
# Time:  O(n)
# Space: O(1)

class Solution(object):
    def oddEvenList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if head:
            odd_tail, cur = head, head.__next__
            while cur and cur.__next__:
                even_head = odd_tail.__next__
                odd_tail.next = cur.__next__
                odd_tail = odd_tail.__next__
                cur.next = odd_tail.__next__
                odd_tail.next = even_head
                cur = cur.__next__
        return head


