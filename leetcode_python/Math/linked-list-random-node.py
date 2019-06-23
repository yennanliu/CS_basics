# V1 : dev 


# V2
# http://bookshadow.com/weblog/2016/08/10/leetcode-linked-list-random-node/
import random
class Solution(object):

    def __init__(self, head):
        """
        @param head The linked list's head. Note that the head is guanranteed to be not null, so it contains at least one node.
        :type head: ListNode
        """
        self.head = head

    def getRandom(self):
        """
        Returns a random node's value.
        :rtype: int
        """
        cnt = 0
        head = self.head
        while head:
            if random.randint(0, cnt) == 0:
                ans = head.val
            head = head.__next__
            cnt += 1
        return ans


# V3 
# Time:  O(n)
# Space: O(1)
from random import randint
class Solution(object):

    def __init__(self, head):
        """
        @param head The linked list's head. Note that the head is guanranteed to be not null, so it contains at least one node.
        :type head: ListNode
        """
        self.__head = head


    # Proof of Reservoir Sampling:
    # https://discuss.leetcode.com/topic/53753/brief-explanation-for-reservoir-sampling
    def getRandom(self):
        """
        Returns a random node's value.
        :rtype: int
        """
        reservoir = -1
        curr, n = self.__head, 0
        while curr:
            reservoir = curr.val if randint(1, n+1) == 1 else reservoir
            curr, n = curr.__next__, n+1
        return reservoir
