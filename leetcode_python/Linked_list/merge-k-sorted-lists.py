# Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
#
# Example:
#
# Input:
# [
#   1->4->5,
#   1->3->4,
#   2->6
# ]
# Output: 1->1->2->3->4->4->5->6

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83068632
# IDEA : HEAP SAVE KEY & VALUE
import heapq
class Solution:
    def mergeKLists(self, lists):
        """
        :type lists: List[ListNode]
        :rtype: ListNode
        """
        head = ListNode(-1)
        move = head
        heap = []
        heapq.heapify(heap)
        [heapq.heappush(heap, (l.val, i)) for i, l in enumerate(lists) if l]
        while heap:
            curVal, curIndex = heapq.heappop(heap)
            curHead = lists[curIndex]
            curNext = curHead.next
            move.next = curHead
            curHead.next = None
            move = curHead
            curHead = curNext
            if curHead:
                lists[curIndex] = curHead
                heapq.heappush(heap, (curHead.val, curIndex))
        return head.next

### Test case : dev 

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83068632
# IDEA : HEAP SAVE VALUE AND NODE 
class Solution(object):
    def mergeKLists(self, lists):
        """
        :type lists: List[ListNode]
        :rtype: ListNode
        """
        head = ListNode(-1)
        move = head
        heap = []
        heapq.heapify(heap)
        [heapq.heappush(heap, (l.val, l)) for i, l in enumerate(lists) if l]
        while heap:
            curVal, curHead = heapq.heappop(heap)
            curNext = curHead.next
            move.next = curHead
            curHead.next = None
            move = curHead
            curHead = curNext
            if curHead:
                heapq.heappush(heap, (curHead.val, curHead))
        return head.next

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/83068632
# IDEA : BRUTE FORCE -> TIME OUT ERROR
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution:
    def mergeKLists(self, lists):
        """
        :type lists: List[ListNode]
        :rtype: ListNode
        """
        head = ListNode(-1)
        move = head
        while True:
            curHead = ListNode(float('inf'))
            curIndex = -1
            for i, llist in enumerate(lists):
                if llist and llist.val < curHead.val:
                    curHead = llist
                    curIndex = i
            if curHead.val == float('inf'):
                break
            curNext = curHead.next
            move.next = curHead
            curHead.next = None
            move = curHead
            curHead = curNext
            lists[curIndex] = curHead
        return head.next

# V1'''
# https://blog.csdn.net/qian2729/article/details/50528385
class Solution(object):
    def mergeKLists(self, lists):
        """
        :type lists: List[ListNode]
        :rtype: ListNode
        """
        heap = []
        for l in lists:
            if l != None:
                heap.append((l.val,l))
        heapq.heapify(heap)
        dummy = ListNode(0)
        cur = dummy
        while heap:
            _,h = heapq.heappop(heap)
            cur.next = h
            cur = cur.next
            if h.next:
                heapq.heappush(heap,(h.next.val,h.next))
        return dummy.next

# V2