# A linked list is given such that each node contains an additional random pointer which could point to any node in the list or null.
# Return a deep copy of the list.
# The Linked List is represented in the input/output as a list of n nodes. Each node is represented as a pair of [val, random_index] where:
# val: an integer representing Node.val
# random_index: the index of the node (range from 0 to n-1) where random pointer points to, or null if it does not point to any node.
#
# Example 1:
#
# Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
# Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
# Example 2:
#
#
# Input: head = [[1,1],[2,1]]
# Output: [[1,1],[2,1]]
# Example 3:
#
# Input: head = [[3,null],[3,0],[3,null]]
# Output: [[3,null],[3,0],[3,null]]
# Example 4:
#
# Input: head = []
# Output: []
# Explanation: Given linked list is empty (null pointer), so return null.
#
# Constraints:
# -10000 <= Node.val <= 10000
# Node.random is null or pointing to a node in the linked list.
# Number of Nodes will not exceed 1000.


# V0
class Node(object):
    def __init__(self, val, next, random):
        self.val = val
        self.next = next
        self.random = random

class Solution:
    def copyRandomList(self, head):
        dic = dict()
        m = n = head
        while m:
            dic[m] = Node(m.val)
            m = m.next
        while n:
            dic[n].next = dic.get(n.next)
            dic[n].random = dic.get(n.random)
            n = n.next
        return dic.get(head)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/80787528
"""
# Definition for a Node.
class Node(object):
    def __init__(self, val, next, random):
        self.val = val
        self.next = next
        self.random = random
"""
class Solution(object):
    def copyRandomList(self, head):
        """
        :type head: Node
        :rtype: Node
        """
        nodeDict = dict()
        dummy = Node(0, None, None)
        nodeDict[head] = dummy
        newHead, pointer = dummy, head
        while pointer:
            node = Node(pointer.val, pointer.next, None)
            nodeDict[pointer] = node
            newHead.next = node
            newHead, pointer = newHead.next, pointer.next
        pointer = head
        while pointer:
            if pointer.random:
                nodeDict[pointer].random = nodeDict[pointer.random]
            pointer = pointer.next
        return dummy.next

### Test case : dev 

# V1'
# http://bookshadow.com/weblog/2015/07/31/leetcode-copy-list-random-pointer/
class Solution:
    # @param head, a RandomListNode
    # @return a RandomListNode
    def copyRandomList(self, head):
        nodeDict = dict()
        dummy = RandomListNode(0)
        pointer, newHead = head, dummy
        while pointer:
            newNode = RandomListNode(pointer.label)
            nodeDict[pointer] = newHead.next = newNode
            newHead, pointer = newHead.next, pointer.next
        pointer, newHead = head, dummy.next
        while pointer:
            if pointer.random:
                newHead.random = nodeDict[pointer.random]
            pointer, newHead = pointer.next, newHead.next
        return dummy.next

# V1''
# https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43485/Clear-and-short-python-O(2n)-and-O(n)-solution
# Definition for a Node.
class Node(object):
    def __init__(self, val, next, random):
        self.val = val
        self.next = next
        self.random = random

class Solution:
    def copyRandomList(self, head):
        dic = dict()
        m = n = head
        while m:
            dic[m] = Node(m.val)
            m = m.next
        while n:
            dic[n].next = dic.get(n.next)
            dic[n].random = dic.get(n.random)
            n = n.next
        return dic.get(head)

# V1'''
# https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43485/Clear-and-short-python-O(2n)-and-O(n)-solution
class Node(object):
    def __init__(self, val, next, random):
        self.val = val
        self.next = next
        self.random = random
        
class Solution:
    def copyRandomList(self, head):
        dic = collections.defaultdict(lambda: Node(0))
        dic[None] = None
        n = head
        while n:
            dic[n].val = n.val
            dic[n].next = dic[n.next]
            dic[n].random = dic[n.random]
            n = n.next
        return dic[head]

# V1'''''
# https://www.jiuzhang.com/solution/copy-list-with-random-pointer/#tag-highlight-lang-python
class Solution:
    # @param head: A RandomListNode
    # @return: A RandomListNode
    def copyRandomList(self, head):
        # write your code here
        if head == None:
            return None
            
        myMap = {}
        nHead = RandomListNode(head.label)
        myMap[head] = nHead
        p = head
        q = nHead
        while p != None:
            q.random = p.random
            if p.next != None:
                q.next = RandomListNode(p.next.label)
                myMap[p.next] = q.next
            else:
                q.next = None
            p = p.next
            q = q.next
        
        p = nHead
        while p!= None:
            if p.random != None:
                p.random = myMap[p.random]
            p = p.next
        return nHead

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