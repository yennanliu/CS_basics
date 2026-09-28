"""

23. Merge k Sorted Lists
Hard

You are given an array of k linked-lists lists, 
each linked-list is sorted in ascending order.
Merge all the linked-lists into one sorted linked-list and return it.

 
Example 1:

Input: lists = [[1,4,5],[1,3,4],[2,6]]
Output: [1,1,2,3,4,4,5,6]
Explanation: The linked-lists are:
[
  1->4->5,
  1->3->4,
  2->6
]
merging them into one sorted list:
1->1->2->3->4->4->5->6

Example 2:

Input: lists = []
Output: []
Example 3:

Input: lists = [[]]
Output: []
 

Constraints:

k == lists.length
0 <= k <= 10^4
0 <= lists[i].length <= 500
-10^4 <= lists[i][j] <= 10^4
lists[i] is sorted in ascending order.
The sum of lists[i].length won't exceed 10^4.

"""



# V0
# IDEA: LINKED LIST OP + merge on pair (gpt)
class Solution(object):
    def mergeKLists(self, lists):
        """
        :type lists: List[Optional[ListNode]]
        :rtype: Optional[ListNode]
        """
        # edge cases
        if not lists:
            return None

        if len(lists) == 1:
            return lists[0]

        n = len(lists)

        # Merge lists pair by pair
        for i in range(0, n, 2):

            first = lists[i]

            # If there is no second list,
            # keep the first list as-is.
            if i + 1 >= n:
                second = None
            else:
                second = lists[i + 1]

            lists[i] = self.mergeTwoLists(first, second)

        # Now merge all merged lists together
        result = lists[0]

        for i in range(2, n, 2):
            result = self.mergeTwoLists(result, lists[i])

        return result

    def mergeTwoLists(self, first, second):
        dummy = ListNode(0)
        curr = dummy

        while first and second:

            if first.val <= second.val:
                curr.next = first
                first = first.next
            else:
                curr.next = second
                second = second.next

            curr = curr.next

        # Attach remaining nodes
        if first:
            curr.next = first
        else:
            curr.next = second

        return dummy.next



# V0-1
# IDEA: LINKED LIST OP +  Divide & Conquer (gpt)
class Solution(object):
    def mergeKLists(self, lists):
        """
        :type lists: List[Optional[ListNode]]
        :rtype: Optional[ListNode]
        """
        # edge case
        if not lists:
            return None

        # Merge lists pair by pair
        interval = 1

        while interval < len(lists):

            for i in range(0, len(lists) - interval, interval * 2):

                lists[i] = self.mergeTwoLists(
                    lists[i],
                    lists[i + interval]
                )

            interval *= 2

        return lists[0]

    def mergeTwoLists(self, first, second):

        dummy = ListNode(0)
        curr = dummy

        while first and second:

            if first.val <= second.val:
                curr.next = first
                first = first.next
            else:
                curr.next = second
                second = second.next

            curr = curr.next

        # Attach remaining nodes
        if first:
            curr.next = first
        else:
            curr.next = second

        return dummy.next



# V0-3
# IDEA: PQ (gemini)
import heapq


class Solution(object):

  def mergeKLists(self, lists):
    """:type lists: List[Optional[ListNode]] :rtype: Optional[ListNode]"""
    if not lists:
      return None

    dummy = ListNode(0)
    curr = dummy
    min_heap = []

    # 1. 將所有非空鏈結串列的頭節點放入 min-heap
    # 放入 (node.val, i, node) 以避免 node 本身無法比較的 Type Error
    for i, l in enumerate(lists):
      if l:
        heapq.heappush(min_heap, (l.val, i, l))

    # 2. 持續彈出最小值並推進下一個節點
    while min_heap:
      val, i, node = heapq.heappop(min_heap)

      curr.next = node
      curr = curr.next

      # 如果該節點後面還有節點，將其推入 heap 中
      if node.next:
        heapq.heappush(min_heap, (node.next.val, i, node.next))

    return dummy.next


# V0-5
# IDEA : LC 021 + implement mergeTwoLists on each of the 2 linked list
# time = O(k * n), k = number of lists, n = total number of nodes
# space = O(1)
class Solution(object):
    def mergeKLists(self, lists):
        if len(lists) == 0:
            return
        if len(lists) == 1:
            return lists[0]
        
        _init_list = lists[0]
        for _list in lists[1:]:
            tmp = self.mergeTwoLists(_init_list, _list)
            _init_list = tmp
        return tmp

    # LC 021 : https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Linked_list/merge-two-sorted-lists.py
    def mergeTwoLists(self, l1, l2):

        if not l1 or not l2:
            return l1 or l2
            
        res = head = ListNode()
        while l1 and l2:
            if l1.val < l2.val:
                res.next = l1
                l1 = l1.next
            else:
                res.next = l2
                l2 = l2.next
            res = res.next

        if l1 or l2:
            res.next = l1 or l2

        return head.next

# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/merge-k-sorted-lists/solution/
# time = O(n log n), n = total number of nodes
# space = O(n)
class Solution(object):
    def mergeKLists(self, lists):
        """
        :type lists: List[ListNode]
        :rtype: ListNode
        """
        self.nodes = []
        head = point = ListNode(0)
        for l in lists:
            while l:
                self.nodes.append(l.val)
                l = l.next
        for x in sorted(self.nodes):
            point.next = ListNode(x)
            point = point.next
        return head.next

# V2
# IDEA : Optimize Approach 2 by Priority Queue
# https://leetcode.com/problems/merge-k-sorted-lists/solution/
# Priority Queue
# https://www.gushiciku.cn/pl/pJaa/zh-tw
from Queue import PriorityQueue

# time = O(n log k), n = total number of nodes, k = number of lists
# space = O(k)
class Solution(object):
    def mergeKLists(self, lists):
        """
        :type lists: List[ListNode]
        :rtype: ListNode
        """
        head = point = ListNode(0)
        q = PriorityQueue()
        for l in lists:
            if l:
                q.put((l.val, l))
        while not q.empty():
            val, node = q.get()
            point.next = ListNode(val)
            point = point.next
            node = node.next
            if node:
                q.put((node.val, node))
        return head.next

# V3
# IDEA : Merge with Divide And Conquer
# https://leetcode.com/problems/merge-k-sorted-lists/solution/
# time = O(n log k), n = total number of nodes, k = number of lists
# space = O(1)
class Solution(object):
    def mergeKLists(self, lists):
        """
        :type lists: List[ListNode]
        :rtype: ListNode
        """
        amount = len(lists)
        interval = 1
        while interval < amount:
            for i in range(0, amount - interval, interval * 2):
                lists[i] = self.merge2Lists(lists[i], lists[i + interval])
            interval *= 2
        return lists[0] if amount > 0 else None

    def merge2Lists(self, l1, l2):
        head = point = ListNode(0)
        while l1 and l2:
            if l1.val <= l2.val:
                point.next = l1
                l1 = l1.next
            else:
                point.next = l2
                l2 = l1
                l1 = point.next.next
            point = point.next
        if not l1:
            point.next=l2
        else:
            point.next=l1
        return head.next

# V4
# https://blog.csdn.net/fuxuemingzhu/article/details/83068632
# IDEA : HEAP SAVE KEY & VALUE
# time = O(n log k), n = total number of nodes, k = number of lists
# space = O(k)
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

# V5
# https://blog.csdn.net/fuxuemingzhu/article/details/83068632
# IDEA : HEAP SAVE VALUE AND NODE
# time = O(n log k), n = total number of nodes, k = number of lists
# space = O(k)
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

# V6
# https://blog.csdn.net/fuxuemingzhu/article/details/83068632
# IDEA : BRUTE FORCE -> TIME OUT ERROR
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
# time = O(k * n), k = number of lists, n = total number of nodes
# space = O(1)
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

# V7
# https://blog.csdn.net/qian2729/article/details/50528385
# time = O(n log k), n = total number of nodes, k = number of lists
# space = O(k)
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
