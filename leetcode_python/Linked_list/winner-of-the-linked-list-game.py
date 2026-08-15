"""

3062. Winner of the Linked List Game
Easy
🔒 (premium)

You are given the head of a linked list of even length containing integers.

Each odd-indexed node contains an odd integer and each even-indexed node contains an even integer.

We call each even-indexed node and its next node a pair, e.g., the nodes with indices 0 and 1 are a pair, the nodes with indices 2 and 3 are a pair, and so on.

For every pair, we compare the values of the nodes in the pair:

If the odd-indexed node is higher, the "Odd" team gets a point.
If the even-indexed node is higher, the "Even" team gets a point.

Return the name of the team with the higher points, if the points are equal, return "Tie".


Example 1:

Input: head = [2,1]
Output: "Even"
Explanation: There is only one pair in this linked list and that is (2,1). Since 2 > 1, the Even team gets the point.
Hence, the answer would be "Even".

Example 2:

Input: head = [2,5,4,7,20,5]
Output: "Odd"
Explanation: There are 3 pairs in this linked list. Let's investigate each pair individually:
(2,5) -> Since 2 < 5, The Odd team gets the point.
(4,7) -> Since 4 < 7, The Odd team gets the point.
(20,5) -> Since 20 > 5, The Even team gets the point.
The Odd team earned 2 points while the Even team got 1 point and the Odd team has the higher points.
Hence, the answer would be "Odd".

Example 3:

Input: head = [4,5,2,1]
Output: "Tie"
Explanation: There are 2 pairs in this linked list. Let's investigate each pair individually:
(4,5) -> Since 4 < 5, the Odd team gets the point.
(2,1) -> Since 2 > 1, the Even team gets the point.
Both teams have equal points.
Hence, the answer would be "Tie".


Constraints:

The number of nodes in the list is in the range [2, 100].
The number of nodes in the list is even.
1 <= Node.val <= 100
The value of each odd-indexed node is odd.
The value of each even-indexed node is even.

"""

# V0
# IDEA : WALK TWO NODES AT A TIME AND KEEP A RUNNING SCORE DIFFERENCE
#
#   the list length is guaranteed even, so every step consumes a complete
#   pair (even node, odd node) — no leftover to guard against.
#
#   one signed counter is enough : +1 when the even member wins, -1 when the
#   odd one does. the sign at the end names the winner, 0 means a tie.
#
#   NOTE : the values can never be equal — one is even and the other odd —
#          so a pair always awards a point.
#
# time = O(n), space = O(1)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def gameResult(self, head):
        diff = 0
        node = head
        while node and node.next:
            if node.val > node.next.val:
                diff += 1
            else:
                diff -= 1
            node = node.next.next

        if diff > 0:
            return "Even"
        if diff < 0:
            return "Odd"
        return "Tie"
