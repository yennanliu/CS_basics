"""

Merge two sorted linked lists and return it as a sorted list. 
The list should be made by splicing together the nodes of the first two lists.


Example 1:

Input: l1 = [1,2,4], l2 = [1,3,4]
Output: [1,1,2,3,4,4]

Example 2:

Input: l1 = [], l2 = []
Output: []

Example 3:

Input: l1 = [], l2 = [0]
Output: [0]
 

Constraints:

The number of nodes in both lists is in the range [0, 50].
-100 <= Node.val <= 100
Both l1 and l2 are sorted in non-decreasing order.

"""

# V0
# IDEA : LOOP 2 LINKED LISTS
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        if not l1 or not l2:
            return l1 or l2
        ### NOTICE THIS
        #   -> we init head, and cur
        #   -> use cur for `link` op
        #   -> and return the `head.next`
        head = cur = ListNode(0)
        while l1 and l2:
            if l1.val < l2.val:
                """
                ### NOTE
                 1) assign node to cur.next !!! (not cur)
                 2) assign node rather than node.val
                """ 
                cur.next = l1
                l1 = l1.next
            else:
                """
                ### NOTE
                 1) assign node to cur.next !!! (not cur)
                 2) assign node rather than node.val
                """ 
                cur.next = l2
                l2 = l2.next
            # note this
            cur = cur.next
        ### NOTE this (in case either l1 or l2 is remaining so we need to append one of them to cur)
        cur.next = l1 or l2
        ### NOTICE THIS : we return head.next
        return head.next

# V0'
# IDEA : LOOP 2 LINKED LISTS
class Solution(object):
    def mergeTwoLists(self, list1, list2):
        # edge case
        if not list1 and not list2:
            return
        res = head = ListNode()
        while list1 and list2:
            if list1.val < list2.val:
                head.next = list1.val
                list1 = list1.next
            else:
                head.next = list2.val
                list2 = list2.next
            head = head.next
        if list1 or list2:
            head.next = list1 if list1 else list2

        #print ("head = " + str(head))
        return res.next

# V0''
# IDEA : RECURSION
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        if not l1 or not l2:
            return l1 or l2
        if l1.val < l2.val:
            l1.next = self.mergeTwoLists(l1.next, l2)
            return l1
        else:
            l2.next = self.mergeTwoLists(l1, l2.next)
            return l2

# V1
# https://blog.csdn.net/coder_orz/article/details/51529359
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        if not l1 or not l2:
            return l1 or l2
        head = cur = ListNode(0)
        while l1 and l2:
            if l1.val < l2.val:
                cur.next = l1
                l1 = l1.next
            else:
                cur.next = l2
                l2 = l2.next
            cur = cur.next
        cur.next = l1 or l2
        return head.next

# V1'
# https://blog.csdn.net/coder_orz/article/details/51529359
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        if not l1 or not l2:
            return l1 or l2
        head = small = l1 if l1.val < l2.val else l2
        big = l1 if l1.val >= l2.val else l2
        pre = None
        while big:
            if not small:
                pre.next = big
                break
            if big.val >= small.val:
                pre = small
                small = small.next
            else:
                tmp = big.next
                pre.next = big
                pre = big
                big.next = small
                big = tmp
        return head

# V1''
# https://blog.csdn.net/coder_orz/article/details/51529359
# IDEA : RECURSION
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        if not l1 or not l2:
            return l1 or l2
        if l1.val < l2.val:
            l1.next = self.mergeTwoLists(l1.next, l2)
            return l1
        else:
            l2.next = self.mergeTwoLists(l1, l2.next)
            return l2

# V1''
# https://www.jiuzhang.com/solution/merge-two-sorted-lists/#tag-highlight-lang-python
# IDEA : TWO POINTERS
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        dummy = ListNode(0)
        tmp = dummy
        while l1 != None and l2 != None:
            if l1.val < l2.val:
                tmp.next = l1
                l1 = l1.next
            else:
                tmp.next = l2
                l2 = l2.next
            tmp = tmp.next
        if l1 != None:
            tmp.next = l1
        else:
            tmp.next = l2
        return dummy.next

# V1 
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, self.__next__)

class Solution(object):
    def mergeTwoLists(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        output  = []
        curr = dummy = ListNode(0)
        while l1 or l2:
            try:
                output.append(l1.val)
                l1 = l1.__next__
            except:
                output.append(l2.val)
                l2 = l2.__next__
        return sorted(output)

# V2 
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, self.__next__)

class Solution(object):
    def mergeTwoLists(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        curr = dummy = ListNode(0)
        while l1 and l2:
            if l1.val < l2.val:
                curr.next = l1
                l1 = l1.__next__
            else:
                curr.next = l2
                l2 = l2.__next__
            curr = curr.__next__
        curr.next = l1 or l2
        return dummy.__next__
