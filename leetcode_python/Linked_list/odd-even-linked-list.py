
 
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
				head = head.next 
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
                odd = odd.next
            else:
                even.next = head
                even = even.next
            head = head.next
            index += 1
        even.next = None
        odd.next = evenHead.next
        return oddHead.next

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
            odd_tail, cur = head, head.next
            while cur and cur.next:
                even_head = odd_tail.next
                odd_tail.next = cur.next
                odd_tail = odd_tail.next
                cur.next = odd_tail.next
                odd_tail.next = even_head
                cur = cur.next
        return head


