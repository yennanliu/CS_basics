

#V1 : DEV 
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None     
          
# class Solution:
#     def numComponents(self, head, G):
#         set(G)
#         print ('G :', G)
#         head_list = []
#         if G == None:
#             return 0 
#         while head and (not head.next) :
#             head_list.append(head.val)
#             head = head.next
#         print ('G :', G)
#         print ('head_list :', head_list)
#         over_lap = list(set(G) ^ set(head_list))
#         return len(over_lap) + 1 


# V2 
class Solution:
    def numComponents(self, head, G):
        """
        :type head: ListNode
        :type G: List[int]
        :rtype: int
        """
        groups = 0
        subset = set(G)
        while head:
            if head.val in subset and (not head.next or head.next.val not in subset):
                groups += 1
            head = head.next
        return groups


# V3 
# Time:  O(m + n), m is the number of G, n is the number of nodes
# Space: O(m)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None


class Solution(object):
    def numComponents(self, head, G):
        """
        :type head: ListNode
        :type G: List[int]
        :rtype: int
        """
        lookup = set(G)
        dummy = ListNode(-1)
        dummy.next = head
        curr = dummy
        result = 0
        while curr and curr.next:
            if curr.val not in lookup and curr.next.val in lookup:
                result += 1
            curr = curr.next
        return result
