"""

2074. Reverse Nodes in Even Length Groups
Medium

You are given the head of a linked list.

The nodes in the linked list are sequentially assigned to non-empty groups whose lengths form the sequence of the natural numbers (1, 2, 3, 4, ...). The length of a group is the number of nodes assigned to it. In other words,

The 1st node is assigned to the first group.
The 2nd and the 3rd nodes are assigned to the second group.
The 4th, 5th, and 6th nodes are assigned to the third group, and so on.

Note that the length of the last group may be less than or equal to 1 + the length of the second to last group.

Reverse the nodes in each group with an even length, and return the head of the modified linked list.


Example 1:

Input: head = [5,2,6,3,9,1,7,3,8,4]
Output: [5,6,2,3,9,1,4,8,3,7]
Explanation:
- The length of the first group is 1, which is odd, hence no reversal occurs.
- The length of the second group is 2, which is even, hence the nodes are reversed.
- The length of the third group is 3, which is odd, hence no reversal occurs.
- The length of the last group is 4, which is even, hence the nodes are reversed.

Example 2:

Input: head = [1,1,0,6]
Output: [1,0,1,6]
Explanation:
- The length of the first group is 1. No reversal occurs.
- The length of the second group is 2. The nodes are reversed.
- The length of the last group is 1. No reversal occurs.

Example 3:

Input: head = [1,1,0,6,5]
Output: [1,0,1,5,6]
Explanation:
- The length of the first group is 1. No reversal occurs.
- The length of the second group is 2. The nodes are reversed.
- The length of the last group is 2. The nodes are reversed.


Constraints:

The number of nodes in the list is in the range [1, 10^5].
0 <= Node.val <= 10^5

"""


# V0
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def reverseEvenLengthGroups(self, head):
        """
        :type head: Optional[ListNode]
        :rtype: Optional[ListNode]
        """
        pass
   

# V0-1


# V0-2     



# V0-5
# IDEA : WALK GROUP BY GROUP, REVERSE IN PLACE WHEN THE ACTUAL LENGTH IS EVEN (CLAUDE)
#
#   `prev` always points at the node just BEFORE the current group. for the
#   group of target size k, first count how many nodes are ACTUALLY there —
#   the tail group may be short, and that real count, not k, decides odd/even.
#
#   e.g. [1,1,0,6,5] -> groups [1] [1,0] [6,5] : the last group's target is 3
#        but only 2 nodes remain -> even -> reversed -> [1,0,1,5,6]
#
#   if even, reverse those `count` nodes with the standard pointer-flip loop,
#   then re-link both ends :  prev.next -> new group head,
#                             old group head (now the tail) -> rest of list.
#
# time = O(n), space = O(1)


# Definition for singly-linked list.
class ListNode(object):
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


class Solution(object):
    def reverseEvenLengthGroups(self, head):
        """
        :type head: Optional[ListNode]
        :rtype: Optional[ListNode]
        """
        # dummy node so the group before the first group always exists
        dummy = ListNode(0)
        dummy.next = head
        prev = dummy
        group_size = 1

        while prev.next:
            # count the nodes this group really has, and remember its last node
            count = 0
            node = prev.next
            group_tail = None
            while node and count < group_size:
                group_tail = node
                count += 1
                node = node.next

            group_head = prev.next
            # NOTE !!! parity of the ACTUAL count, not of group_size
            if count % 2 == 0:
                # reverse `count` nodes starting at group_head
                cur = group_head
                reversed_head = None
                for _ in range(count):
                    nxt = cur.next
                    cur.next = reversed_head
                    reversed_head = cur
                    cur = nxt
                # cur is now the first node AFTER the group
                prev.next = reversed_head
                group_head.next = cur
                # the old head is the new tail -> it precedes the next group
                prev = group_head
            else:
                # untouched group -> its own last node precedes the next group
                prev = group_tail

            group_size += 1

        return dummy.next


# V0-6
# IDEA : COPY VALUES INTO AN ARRAY, REVERSE EVEN GROUPS AS SLICES, WRITE BACK (CLAUDE)
#
#   the list's SHAPE never changes — only which value sits in which node.
#   so read every value into an array, walk the same group boundaries over
#   indices, reverse the values of each even-length group, then write the
#   array back into the nodes in order.
#
#   NOTE : simpler to get right live (no pointer re-linking), but O(n) extra
#          space — say so, and offer V0 when asked for O(1).
#
# time = O(n), space = O(n)
class Solution2(object):
    def reverseEvenLengthGroups(self, head):
        """
        :type head: Optional[ListNode]
        :rtype: Optional[ListNode]
        """
        # collect values in list order
        values = []
        node = head
        while node:
            values.append(node.val)
            node = node.next

        n = len(values)
        start = 0
        group_size = 1
        while start < n:
            # the last group may be cut short by the end of the list
            end = min(start + group_size, n)    # exclusive
            if (end - start) % 2 == 0:
                # reverse values[start:end] with two pointers
                left = start
                right = end - 1
                while left < right:
                    values[left], values[right] = values[right], values[left]
                    left += 1
                    right -= 1
            start = end
            group_size += 1

        # write the (partly reversed) values back into the same nodes
        node = head
        for val in values:
            node.val = val
            node = node.next

        return head
