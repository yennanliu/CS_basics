

# V1 
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution(object):
    # @param head, a ListNode
    # @param x, an integer
    # @return a ListNode
    def partition(self, head, x):
        left = []
        right = []
        cur = head 
        while cur:
            if cur.val < x:
                left.append(cur.val)
            else:
                right.append(cur.val)

            cur = cur.__next__ 
        return left + right 

# V2 
# Time:  O(n)
# Space: O(1)

class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution(object):
    # @param head, a ListNode
    # @param x, an integer
    # @return a ListNode
    def partition(self, head, x):
        dummySmaller, dummyGreater = ListNode(-1), ListNode(-1)
        smaller, greater = dummySmaller, dummyGreater

        while head:
            if head.val < x:
                smaller.next = head
                smaller = smaller.__next__
            else:
                greater.next = head
                greater = greater.__next__
            head = head.__next__

        smaller.next = dummyGreater.__next__
        greater.next = None

        return dummySmaller.__next__



