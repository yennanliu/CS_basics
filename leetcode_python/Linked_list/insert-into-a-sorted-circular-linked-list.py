"""

[LeetCode] 708. Insert into a Cyclic Sorted List

# Insert into a Cyclic Sorted List linspiration

Problem
Given a node from a cyclic linked list which is sorted in ascending order, write a function to insert a value into the list such that it remains a cyclic sorted list. The given node can be a reference to any single node in the list, and may not be necessarily the smallest value in the cyclic list.

If there are multiple suitable places for insertion, you may choose any place to insert the new value. After the insertion, the cyclic list should remain sorted.

If the list is empty (i.e., given node is null), you should create a new single cyclic list and return the reference to that single node. Otherwise, you should return the original given node.

The following example may help you understand the problem better:

clipboard.png

In the figure above, there is a cyclic sorted list of three elements. You are given a reference to the node with value 3, and we need to insert 2 into the list.

clipboard.png

The new node should insert between node 1 and node 3. After the insertion, the list should look like this, and we should still return node 3.

"""

# V0

# V1
#  https://ttzztt.gitbooks.io/lc/content/linked-list/insert-into-a-cyclic-sorted-list.html
class Solution(object):
    def insert(self, head, val):
        node = Node(val, head)
        # case 1 no head
        if not head:
            return node
        prev, cur = head, head.next
        while 1:
            # case 2: prev.val <= val <= cur.val
            if prev.val <= val <= cur.val:
                break

            # case 3: prev.val > cur.val and val < cur.val or prev.val < cur
            elif prev.val > cur.val and (val <= cur.val or prev.val <= val):
                break

            prev, cur = prev.next, cur.next
            # case 4: prev == head
            if prev == head: # in case of all nodes have same value that are > val 
                break

        # insert node between prev and cur
        prev.next = node
        node.next = cur

        return head

# V1
# https://ithelp.ithome.com.tw/articles/10223721

# V1'
# https://blog.51cto.com/u_15127692/3670466

# V2
