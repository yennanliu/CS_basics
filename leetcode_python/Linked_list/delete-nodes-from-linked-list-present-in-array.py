"""

3217. Delete Nodes From Linked List Present in Array
Medium

You are given an array of integers nums and the head of a linked list. Return the head of the modified linked list after removing all nodes from the linked list that have a value that exists in nums.


Example 1:

Input: nums = [1,2,3], head = [1,2,3,4,5]
Output: [4,5]
Explanation:
Remove the nodes with values 1, 2, and 3.

Example 2:

Input: nums = [1], head = [1,2,1,2,1,2]
Output: [2,2,2]
Explanation:
Remove the nodes with value 1.

Example 3:

Input: nums = [5], head = [1,2,3,4]
Output: [1,2,3,4]
Explanation:
No node has value 5.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5
All elements in nums are unique.
The number of nodes in the given list is in the range [1, 3 * 10^5].
1 <= Node.val <= 10^5
The input is generated such that there is at least one node in the linked list that has a value not present in nums.

"""

# V0
# IDEA : HASH THE VALUES, THEN ONE UNLINKING PASS BEHIND A DUMMY HEAD
#
#   turning nums into a set makes each membership test O(1) — scanning the
#   array per node would be 3*10^5 * 10^5 comparisons.
#
#   a dummy node in front removes the "what if the head itself is deleted"
#   special case : the tail pointer simply skips every doomed node and links
#   past it.
#
# time = O(n + m), space = O(m)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def modifiedList(self, nums, head):
        drop = set(nums)
        dummy = ListNode(0)
        tail = dummy
        node = head
        while node:
            if node.val not in drop:
                tail.next = node
                tail = node
            node = node.next
        tail.next = None
        return dummy.next
