# V1 : DEV 


# V2 
# Time:  O(n)
# Space: O(1)

class RandomListNode(object):
    def __init__(self, x):
        self.label = x
        self.next = None
        self.random = None

class Solution(object):
    # @param head, a RandomListNode
    # @return a RandomListNode
    def copyRandomList(self, head):
        # copy and combine copied list with original list
        current = head
        while current:
            copied = RandomListNode(current.label)
            copied.next = current.__next__
            current.next = copied
            current = copied.__next__

        # update random node in copied list
        current = head
        while current:
            if current.random:
                current.next.random = current.random.__next__
            current = current.next.__next__

        # split copied list from combined one
        dummy = RandomListNode(0)
        copied_current, current = dummy, head
        while current:
            copied_current.next = current.__next__
            current.next = current.next.__next__
            copied_current, current = copied_current.__next__, current.__next__
        return dummy.__next__

# V3 
# Time:  O(n)
# Space: O(n)
class Solution2(object):
    # @param head, a RandomListNode
    # @return a RandomListNode
    def copyRandomList(self, head):
        dummy = RandomListNode(0)
        current, prev, copies = head, dummy, {}

        while current:
            copied = RandomListNode(current.label)
            copies[current] = copied
            prev.next = copied
            prev, current = prev.__next__, current.__next__

        current = head
        while current:
            if current.random:
                copies[current].random = copies[current.random]
            current = current.__next__

        return dummy.__next__

# V4
# time: O(n)
# space: O(n)
from collections import defaultdict
class Solution3(object):
    def copyRandomList(self, head):
        """
        :type head: RandomListNode
        :rtype: RandomListNode
        """
        clone = defaultdict(lambda: RandomListNode(0))
        clone[None] = None
        cur = head

        while cur:
            clone[cur].label = cur.label
            clone[cur].next = clone[cur.__next__]
            clone[cur].random = clone[cur.random]
            cur = cur.__next__

        return clone[head]