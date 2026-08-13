"""

1265. Print Immutable Linked List in Reverse
Medium

You are given an immutable linked list, print out all values of each node in
reverse with the help of the following interface:

ImmutableListNode: An interface of immutable linked list, you are given the head of the list.

You need to use the following functions to access the linked list
(you can't access the ImmutableListNode directly):

ImmutableListNode.printValue(): Print value of the current node.
ImmutableListNode.getNext(): Return the next node.

The input is only given to initialize the linked list internally.
You must solve this problem without modifying the linked list. In other words,
you must operate the linked list using only the mentioned APIs.


Example 1:

Input: head = [1,2,3,4]
Output: [4,3,2,1]

Example 2:

Input: head = [0,-4,-1,3,-5]
Output: [-5,3,-1,-4,0]

Example 3:

Input: head = [-2,0,6,4,4,-6]
Output: [-6,4,4,6,0,-2]


Constraints:

The length of the linked list is between [1, 1000].
The value of each node in the linked list is between [-1000, 1000].


Follow up:

Could you solve this problem in:

Constant space complexity?
Linear time complexity and less than linear space complexity?

"""

# V0
# IDEA : RECURSION
#        go all the way to the tail first, print on the way back
# time = O(n)
# space = O(n), recursion stack
class Solution(object):
    def printLinkedListInReverse(self, head):
        """
        :type head: ImmutableListNode
        :rtype: None
        """
        if not head:
            return
        self.printLinkedListInReverse(head.getNext())
        head.printValue()


# V1
# IDEA : SQRT DECOMPOSITION (follow up : less than linear space)
#        store every sqrt(n)-th node as a "checkpoint", then replay each
#        block backward, walking from its checkpoint each time
# time = O(n)
# space = O(sqrt(n))
class Solution2(object):
    def printLinkedListInReverse(self, head):
        # 1) count the nodes
        n = 0
        node = head
        while node:
            n += 1
            node = node.getNext()
        if n == 0:
            return

        # 2) block size ~ sqrt(n), remember the head of each block
        block = int(n ** 0.5) + 1
        checkpoints = []
        node, i = head, 0
        while node:
            if i % block == 0:
                checkpoints.append(node)
            i += 1
            node = node.getNext()

        # 3) walk blocks backward, and inside a block print backward
        #    (a block holds at most `block` nodes -> O(sqrt(n)) buffer)
        for b in range(len(checkpoints) - 1, -1, -1):
            buf = []
            node = checkpoints[b]
            cnt = min(block, n - b * block)
            for _ in range(cnt):
                buf.append(node)
                node = node.getNext()
            for x in reversed(buf):
                x.printValue()
