"""

430. Flatten a Multilevel Doubly Linked List
Medium

You are given a doubly linked list, which contains nodes that have a next pointer, a previous pointer, and an additional child pointer. This child pointer may or may not point to a separate doubly linked list, also containing these special nodes. These child lists may have one or more children of their own, and so on, to produce a multilevel data structure as shown in the example below.

Given the head of the first level of the list, flatten the list so that all the nodes appear in a single-level, doubly linked list. Let curr be a node with a child list. The nodes in the child list should appear after curr and before curr.next in the flattened list.

Return the head of the flattened list. The nodes in the list must have all of their child pointers set to null.

Example 1:

https://assets.leetcode.com/uploads/2021/11/09/flatten11.jpg

Input: head = [1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]
Output: [1,2,3,7,8,11,12,9,10,4,5,6]
Explanation: The multilevel linked list in the input is shown.
After flattening the multilevel linked list it becomes:
https://assets.leetcode.com/uploads/2021/11/09/flatten12.jpg

Example 2:

https://assets.leetcode.com/uploads/2021/11/09/flatten2.1jpg

Input: head = [1,2,null,3]
Output: [1,3,2]
Explanation: The multilevel linked list in the input is shown.
After flattening the multilevel linked list it becomes:
https://assets.leetcode.com/uploads/2021/11/24/list.jpg

Example 3:

Input: head = []
Output: []
Explanation: There could be empty list in the input.

Constraints:

The number of Nodes will not exceed 1000.
1 <= Node.val <= 10^5

How the multilevel linked list is represented in test cases:

We use the multilevel linked list from Example 1 above:

 1---2---3---4---5---6--NULL
         |
         7---8---9---10--NULL
             |
             11--12--NULL

The serialization of each level is as follows:

[1,2,3,4,5,6,null]
[7,8,9,10,null]
[11,12,null]

To serialize all levels together, we will add nulls in each level to signify no node connects to the upper node of the previous level. The serialization becomes:

[1,    2,    3, 4, 5, 6, null]
             |
[null, null, 7,    8, 9, 10, null]
                   |
[            null, 11, 12, null]

Merging the serialization of each level and removing trailing nulls we obtain:

[1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]

"""

# V0
# time = O(n)  # n = total number of nodes across all levels
# space = O(d)  # d = max nesting depth, due to recursion stack
class Solution:
    def flatten(self, head: 'Node') -> 'Node':
        self.dfs(head)
        return head

    def dfs(self, head):
        cur = head
        while cur:
            # if there is a "next layer" child
            if cur.child:
                next = cur.next
                cur.next = cur.child
                cur.next.prev = cur
                # keep go through the same layer linked list via dfs, and connect "childLast" to the next node 
                childLast = self.dfs(cur.child)
                childLast.next = next
                if next: 
                    next.prev = childLast
                cur.child = None
            head = cur
            cur = cur.next
        return head

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/81985172
# time = O(n)  # n = total number of nodes across all levels
# space = O(d)  # d = max nesting depth, due to recursion stack
class Solution(object):
    def flatten(self, head):
        """
        :type head: Node
        :rtype: Node
        """
        if not head: return None
        node = head
        while node:
            node_next = node.next
            if node.child:
                flattened = self.flatten(node.child)
                node.child = None
                nextNode = self.appendToList(node, flattened)
                node = nextNode
            else:
                node = node.next
        return head

    def appendToList(self, node, listToAppendHead):
        next_node = node.next
        node.next = listToAppendHead
        listToAppendHead.prev = node
        while node.next:
            node = node.next
        node.next = next_node
        if next_node:
            next_node.prev = node
        return next_node

# V1'
# https://lequ7.com/2019/07/27/java/LeetCode430-bian-ping-hua-duo-ji-shuang-xiang-lian-biao-FlattenaMultilevelDoublyLinkedList/
# time = O(n)  # n = total number of nodes across all levels
# space = O(d)  # d = max nesting depth, due to recursion stack
class Solution:
    def flatten(self, head: 'Node') -> 'Node':
        self.dfs(head)
        return head

    def dfs(self, head):
        cur = head
        while cur:
            if cur.child:
                next = cur.next
                cur.next = cur.child
                cur.next.prev = cur
                childLast = self.dfs(cur.child)
                childLast.next = next
                if next: next.prev = childLast
                cur.child = None
            head = cur
            cur = cur.next
        return head
        
# V2
