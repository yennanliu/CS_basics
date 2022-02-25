"""

19. Remove Nth Node From End of List
Medium

Given the head of a linked list, remove the nth node from the end of the list and return its head.

 

Example 1:


Input: head = [1,2,3,4,5], n = 2
Output: [1,2,3,5]
Example 2:

Input: head = [1], n = 1
Output: []
Example 3:

Input: head = [1,2], n = 1
Output: [1]
 

Constraints:

The number of nodes in the list is sz.
1 <= sz <= 30
0 <= Node.val <= 100
1 <= n <= sz
 

Follow up: Could you do this in one pass?


"""

# V0
# IDEA : FAST-SLOW POINTERS (One pass algorithm)
# check LC solution video for more info. : https://leetcode.com/problems/remove-nth-node-from-end-of-list/solution/
# IDEA :
#   step 1) we move fast pointers n+1 steps -> so slow, fast pointers has n distance (n+1-1 == n)
#   step 2) we move fast, and slow pointers till fast pointer meet the end
#   step 3) then we point slow.next to slow.next.next (same as we remove n node)
#   step 4) we return new_head.next as final result
class Solution(object):
    def removeNthFromEnd(self, head, n):
        new_head = ListNode(0)
        new_head.next = head
        fast = slow = new_head
        for i in range(n+1):
            fast = fast.next
        while fast:
            fast = fast.next
            slow = slow.next
        slow.next = slow.next.next
        return new_head.next

# V1
# IDEA : FAST-SLOW POINTERS
# https://blog.csdn.net/coder_orz/article/details/51691267
class Solution(object):
    def removeNthFromEnd(self, head, n):
        new_head = ListNode(0)
        new_head.next = head
        fast = slow = new_head
        for i in range(n+1):
            fast = fast.next
        while fast:
            fast = fast.next
            slow = slow.next
        slow.next = slow.next.next
        return new_head.next

# V1'
# IDEA : Two pass algorithm
# https://leetcode.com/problems/remove-nth-node-from-end-of-list/solution/
# JAVA
# public ListNode removeNthFromEnd(ListNode head, int n) {
#     ListNode dummy = new ListNode(0);
#     dummy.next = head;
#     int length  = 0;
#     ListNode first = head;
#     while (first != null) {
#         length++;
#         first = first.next;
#     }
#     length -= n;
#     first = dummy;
#     while (length > 0) {
#         length--;
#         first = first.next;
#     }
#     first.next = first.next.next;
#     return dummy.next;
# }

# V1''
# IDEA : One pass algorithm
# https://leetcode.com/problems/remove-nth-node-from-end-of-list/solution/
# JAVA
# public ListNode removeNthFromEnd(ListNode head, int n) {
#     ListNode dummy = new ListNode(0);
#     dummy.next = head;
#     ListNode first = dummy;
#     ListNode second = dummy;
#     // Advances first pointer so that the gap between first and second is n nodes apart
#     for (int i = 1; i <= n + 1; i++) {
#         first = first.next;
#     }
#     // Move first to the end, maintaining the gap
#     while (first != null) {
#         first = first.next;
#         second = second.next;
#     }
#     second.next = second.next.next;
#     return dummy.next;
# }

# V1'''
# https://www.jiuzhang.com/solution/remove-nth-node-from-end-of-list/#tag-highlight-lang-python
class Solution(object):
    def removeNthFromEnd(self, head, n):
        res = ListNode(0)
        res.next = head
        tmp = res
        for i in range(0, n):
            head = head.next
        while head != None:
            head = head.next
            tmp = tmp.next
        tmp.next = tmp.next.next
        return res.next

# V1''''
# https://leetcode.com/problems/remove-nth-node-from-end-of-list/discuss/287711/Solution-in-Python
class Solution:
    def removeNthFromEnd(self, head, n):
        if head.next == None:
            return None
        tmp = head
        size = 0
        
        # find the size of the linked list
        while tmp:
            size += 1
            tmp = tmp.next
        tmp = head
        
        #if we have to remove the first node:
        if n == size: 
            return head.next
        
        for i in range(size-n-1):
            tmp = tmp.next
        tmp.next = tmp.next.next
        return head

# V1'''''
# IDEA : DICT
# https://leetcode.com/problems/remove-nth-node-from-end-of-list/discuss/1524633/Runtime-24-ms-python-solution-with-dict
class Solution:
    def removeNthFromEnd(self, head, n):
        curr = head
        mapping = {}
        index = 0
        while curr:
            mapping[index] = curr
            curr = curr.next
            index += 1

        rm_index = len(mapping) - n
        if rm_index == 0:
            return head.next
        elif n == 1:
            mapping.get(rm_index-1).next = None
            return head
        else:
            mapping.get(rm_index-1).next = mapping.get(rm_index+1)
            return head

# V1''''''
# IDEA : Value-Shifting 
# https://leetcode.com/problems/remove-nth-node-from-end-of-list/discuss/8802/3-short-Python-solutions
class Solution:
    def removeNthFromEnd(self, head, n):
        def index(node):
            if not node:
                return 0
            i = index(node.next) + 1
            if i > n:
                node.next.val = node.val
            return i
        index(head)
        return head.next

# V1'''''''
# IDEA : Index and Remove
# https://leetcode.com/problems/remove-nth-node-from-end-of-list/discuss/8802/3-short-Python-solutions
class Solution:
    def removeNthFromEnd(self, head, n):
        def remove(head):
            if not head:
                return 0, head
            i, head.next = remove(head.next)
            return i+1, (head, head.next)[i+1 == n]
        return remove(head)[1]

# V1''''''''
# IDEA : n ahead
# https://leetcode.com/problems/remove-nth-node-from-end-of-list/discuss/8802/3-short-Python-solutions
class Solution:
    def removeNthFromEnd(self, head, n):
        fast = slow = head
        for _ in range(n):
            fast = fast.next
        if not fast:
            return head.next
        while fast.next:
            fast = fast.next
            slow = slow.next
        slow.next = slow.next.next
        return head

# V1'''''''''
# IDEA : MOVE last ~ n node
# https://blog.csdn.net/coder_orz/article/details/51691267
class Solution(object):
    def removeNthFromEnd(self, head, n):
        def getIndex(node):
            if not node:
                return 0
            index = getIndex(node.next) + 1
            if index > n:
                node.next.val = node.val
            return index
        getIndex(head)
        return head.next

# V1''''''''' 
# https://blog.csdn.net/coder_orz/article/details/51691267
class Solution(object):
    def removeNthFromEnd(self, head, n):
        def remove(node):
            if not node:
                return 0, node
            index, node.next = remove(node.next)
            next_node = node if n != index+1 else node.next
            return index+1, next_node
        ind, new_head = remove(head)
        return new_head

# V1'''''''''
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self is None:
            return "Nil"
        else:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution:
    # @return a ListNode
    def removeNthFromEnd(self, head, n):
        my_list = []
        cur = head 
        while cur:
            my_list.append(cur.val)
            cur = cur.__next__ 
        my_list.pop(-n)
        return my_list

# V2 
# https://www.cnblogs.com/zuoyuan/p/3701971.html
# -- idea: 
# make a dummy node, make 2 pointers : p1 p2, 
# then move p1 only for n steps 
# then move p1 and p2 on the same time 
# when p1.next == None  --->  p2.next is the to-delete node 
class Solution:
    # @return a ListNode
    def removeNthFromEnd(self, head, n):
        dummy=ListNode(0); dummy.next=head
        p1=p2=dummy
        for i in range(n): p1=p1.__next__
        while p1.__next__:
            p1=p1.__next__; p2=p2.__next__
        p2.next=p2.next.__next__
        return dummy.__next__

# V3  
# Definition for singly-linked list. 
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self is None:
            return "Nil"
        else:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution:
    # @return a ListNode
    def removeNthFromEnd(self, head, n):
        dummy = ListNode(-1)
        dummy.next = head
        slow, fast = dummy, dummy

        for i in range(n):
            fast = fast.__next__

        while fast.__next__:
            slow, fast = slow.__next__, fast.__next__

        slow.next = slow.next.__next__

        return dummy.__next__
        # head = ListNode(1)
        # head.next = ListNode(2)
        # head.next.next = ListNode(3)
        # head.next.next.next = ListNode(4)
        # head.next.next.next.next = ListNode(5)
        # print Solution().removeNthFromEnd(head, 2)