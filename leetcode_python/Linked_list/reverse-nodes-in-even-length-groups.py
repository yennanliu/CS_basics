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
# IDEA : WALK GROUP BY GROUP, REVERSE IN PLACE WHEN THE ACTUAL LENGTH IS EVEN
#
#   `prev` always points at the node just before the current group. for group
#   size k, first count how many nodes are ACTUALLY there (the tail group may
#   be short) — that real count, not k, decides odd/even.
#
#   if even, reverse those `cnt` nodes with the standard head-insertion loop
#   and re-link :  prev.next -> new head,  old head -> the rest.
#
#   NOTE : after reversing, the OLD group head becomes the group tail, which
#          is the `prev` for the next round.
#
# time = O(n), space = O(1)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def reverseEvenLengthGroups(self, head):
        dummy = ListNode(0)
        dummy.next = head
        prev = dummy
        k = 1
        while prev.next:
            # how many nodes does this group really have, and where does it end?
            cnt = 0
            node = prev.next
            last = None
            while node and cnt < k:
                last = node
                cnt += 1
                node = node.next

            group_head = prev.next
            if cnt % 2 == 0:
                # reverse `cnt` nodes starting at group_head
                cur = group_head
                new_head = None
                for _ in range(cnt):
                    nxt = cur.next
                    cur.next = new_head
                    new_head = cur
                    cur = nxt
                prev.next = new_head
                group_head.next = cur   # group_head is now the group tail
                prev = group_head
            else:
                prev = last             # untouched group -> its own last node
            k += 1
        return dummy.next
