


#  V1 
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None

# class Solution(object):
#     import math 
#     def middleNode(self, head):
#         my_list = []
#         for i in head:
#             my_list.append(i)
#             #cur = cur.next
#         return cur[math.ceil(len(my_list)/2)]


# V2 
# Time:  O(n)
# Space: O(1)

class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution(object):
    def middleNode(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        slow, fast = head, head
        while fast and fast.__next__:
            slow, fast = slow.__next__, fast.next.__next__
        return slow