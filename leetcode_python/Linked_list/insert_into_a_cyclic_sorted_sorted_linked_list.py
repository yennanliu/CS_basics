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
# IDEA : LINKED LIST
# IDEA : CONSIDER THE 4 CASES BELOW :
# CASE 1) No head:
# CASE 2) prev.val <= val <= cur.val
# CASE 3) prev.val > cur.val and (val < cur.val or prev.val < cur): cur is either the min or the max with not all nodes with the same value
# CASE 4) val != every nodes's value in a cyclic linked list where every node has the same value
class Solution(object):
    def insert(self, head, val):
        node = Node(val, head)
        # case 1): no head
        if not head:
            return node
        prev, cur = head, head.next
        while True:
            # case 2): prev.val <= val <= cur.val
            # e.g.  1 -> 3 -> 5 -> "4" (insert 4)
            if prev.val <= val <= cur.val:
                break

            # case 3): prev.val > cur.val and val < cur.val or prev.val < cur
            # e.g. 6 -> 4 -> "5"  (insert 5), or  5 -> 4 -> "3" (insert 3)
            # "SORTED" means the linked-list can be in an ascending or descending order 
            elif prev.val > cur.val and (val <= cur.val or prev.val <= val):
                break

            prev, cur = prev.next, cur.next
            # case 4): prev == head
            # e.g. 1 -> 1 -> 1 ->...-> 1
            if prev == head: # in case of all nodes have same value that are > val 
                break

        # insert node between prev and cur
        prev.next = node
        node.next = cur
        return head
                    
# V1
# https://blog.csdn.net/weixin_41677877/article/details/81200818
class Solution:
    def insert(self, node, x):
        # write your code 
        originNode = node
        tmp = ListNode(x)
        if node == None:
            node = tmp
            node.next = node
            return node
        else:
            while True:
                if node.next.next == node:
                    tmp.next = node.next
                    node.next = tmp
                    return node
                if (node.val<=x and node.next.val>x) or (node.val<x and node.next.val>=x) or (node.val>node.next.val and node.val<x and node.next.val<x) or (node.val>node.next.val and node.val>x and node.next.val>x):
                    tmp.next = node.next
                    node.next = tmp
                    return node
                node = node.next
                if node == originNode:
                    tmp.next = node.next
                    node.next = tmp
                    return node

# V1'
# https://ttzztt.gitbooks.io/lc/content/linked-list/insert-into-a-cyclic-sorted-list.html
# IDEA : LINKED LIST
# IDEA : CONSIDER THE 4 CASES BELOW :
# CASE 1) No head:
# CASE 2) prev.val <= val <= cur.val
# CASE 3) prev.val > cur.val and (val < cur.val or prev.val < cur): cur is either the min or the max with not all nodes with the same value
# CASE 4) val != every nodes's value in a cyclic linked list where every node has the same value
class Solution(object):
    def insert(self, head, val):
        """
        :type head: Node
        :type insertVal: int
        :rtype: Node
        """
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

# V1''
# https://github.com/dennyzhang/code.dennyzhang.com/tree/master/problems/insert-into-a-cyclic-sorted-list
class Solution:
    def insert(self, head, insertVal):
        """
        :type head: Node
        :type insertVal: int
        :rtype: Node
        """
        node = Node(insertVal, None)
        # empty
        if head is None:
            node.next = node
            return node

        # one node
        if head.next is None:
            head.next = node
            node.next = head
            return head

        # find the smallest value, which is no less than the target
        p = head
        while True:
            # end of the loop
            if p.val > p.next.val:
                # biggest or smallest
                if insertVal >= p.val or insertVal <= p.next.val:
                    break

                # should keep going
                if insertVal > p.next.val and insertVal < p.val:
                    p = p.next
                    continue
                break

            if insertVal >= p.val and insertVal <= p.next.val:
                break
            p = p.next
            if p == head:
                # run into the loop again
                break

        node.next = p.next
        p.next = node
        return head

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