
  # V1' 
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution(object):
    def numComponents(self, head, G):
        count = 0 
        G = set(G)
        dummy  = ListNode(-1)
        dummy.next = head 
        cur = dummy 
        while cur and cur.__next__:
            if cur.val not in G and cur.next.val in G:
                count = count + 1 
            cur = cur.__next__ 
        return count
                
  
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
            if head.val in subset and (not head.__next__ or head.next.val not in subset):
                groups += 1
            head = head.__next__
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
        while curr and curr.__next__:
            if curr.val not in lookup and curr.next.val in lookup:
                result += 1
            curr = curr.__next__
        return result
