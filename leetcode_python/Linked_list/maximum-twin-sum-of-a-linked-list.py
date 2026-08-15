"""

2130. Maximum Twin Sum of a Linked List
Medium

In a linked list of size n, where n is even, the ith node (0-indexed) of the linked list is known as the twin of the (n-1-i)th node, if 0 <= i <= (n / 2) - 1.

For example, if n = 4, then node 0 is the twin of node 3, and node 1 is the twin of node 2. These are the only nodes with twins for n = 4.

The twin sum is defined as the sum of a node and its twin.

Given the head of a linked list with even length, return the maximum twin sum of the linked list.


Example 1:

Input: head = [5,4,2,1]
Output: 6
Explanation:
Nodes 0 and 1 are the twins of nodes 3 and 2, respectively. All have twin sum = 6.
There are no other nodes with twins in the linked list.
Thus, the maximum twin sum of the linked list is 6.

Example 2:

Input: head = [4,2,2,3]
Output: 7
Explanation:
The nodes with twins present in this linked list are:
- Node 0 is the twin of node 3 having a twin sum of 4 + 3 = 7.
- Node 1 is the twin of node 2 having a twin sum of 2 + 2 = 4.
Thus, the maximum twin sum of the linked list is max(7, 4) = 7.

Example 3:

Input: head = [1,100000]
Output: 100001
Explanation:
There is only one node with a twin in the linked list having twin sum of 1 + 100000 = 100001.


Constraints:

The number of nodes in the list is an even integer in the range [2, 10^5].
1 <= Node.val <= 10^5

"""

# V0
# IDEA : SPLIT AT THE MIDDLE, REVERSE THE SECOND HALF, WALK BOTH TOGETHER
#
#   twins are "i-th from the front" and "i-th from the back". a singly
#   linked list cannot walk backwards, so reverse the back half in place —
#   then a single pair of pointers, one per half, meets twins at each step.
#
#   slow/fast finds the start of the second half (n is guaranteed even, so
#   slow lands exactly on the boundary), and the reversal is O(1) extra
#   space.
#
# time = O(n), space = O(1)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def pairSum(self, head):
        # 1) middle
        slow, fast = head, head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next

        # 2) reverse from the middle on
        prev = None
        while slow:
            nxt = slow.next
            slow.next = prev
            prev = slow
            slow = nxt

        # 3) walk the two halves in lockstep
        res = 0
        front, back = head, prev
        while back:
            res = max(res, front.val + back.val)
            front = front.next
            back = back.next
        return res
