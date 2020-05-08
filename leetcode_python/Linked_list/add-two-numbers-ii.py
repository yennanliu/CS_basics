# V0
class Solution:
    def addTwoNumbers(self, l1, l2):
        if not l1 and not l2:
            return None

        l1_num = 0
        while l1:
            l1_num = l1_num * 10 + l1.val
            l1 = l1.next

        l2_num = 0
        while l2:
            l2_num = l2_num * 10 + l2.val
            l2 = l2.next

        lsum = l1_num + l2_num

        head = ListNode(None)
        cur = head
        for istr in str(lsum):
            cur.next = ListNode(int(istr))
            cur = cur.next
        return head.next

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79380457
# IDEA : STRING  + LINKED LIST
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        num1 = ''
        num2 = ''
        while l1:
            num1 += str(l1.val)
            l1 = l1.next
        while l2:
            num2 += str(l2.val)
            l2 = l2.next
        add = str(int(num1) + int(num2))
        head = ListNode(add[0])
        answer = head
        for i in range(1, len(add)):
            node = ListNode(add[i])
            head.next = node
            head = head.next
        return answer

### Test case : dev

# V1'
# https://leetcode.com/problems/add-two-numbers-ii/discuss/241079/Fast-Python3-Solution
# IDEA : LINKED LIST
class Solution:
    def addTwoNumbers(self, l1, l2):
        if not l1 and not l2:
            return None

        l1_num = 0
        while l1:
            l1_num = l1_num * 10 + l1.val
            l1 = l1.next

        l2_num = 0
        while l2:
            l2_num = l2_num * 10 + l2.val
            l2 = l2.next

        lsum = l1_num + l2_num

        head = ListNode(None)
        cur = head
        for istr in str(lsum):
            cur.next = ListNode(int(istr))
            cur = cur.next

        return head.next

# V1''
# https://leetcode.com/problems/add-two-numbers-ii/discuss/92682/Naive-Python-Solution
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        c1, c2 = '', ''
        while l1:
            c1 += str(l1.val)
            l1 = l1.next
        while l2:
            c2 += str(l2.val)
            l2 = l2.next
        num = str(int(c1) + int(c2))
        dummy = ListNode(0)
        c = dummy
        for i in range(len(num)):
            c.next = ListNode(num[i])
            c = c.next
        return dummy.next

# V1''' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79380457
# IDEA : STACK 
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        stack1 = []
        stack2 = []
        while l1:
            stack1.append(l1.val)
            l1 = l1.next
        while l2:
            stack2.append(l2.val)
            l2 = l2.next
        answer = None
        carry = 0
        while stack1 and stack2:
            add = stack1.pop() + stack2.pop() + carry
            carry = 1 if add >= 10 else 0
            temp = answer
            answer = ListNode(add % 10)
            answer.next = temp
        l = stack1 if stack1 else stack2
        while l:
            add = l.pop() + carry
            carry = 1 if add >= 10 else 0
            temp = answer
            answer = ListNode(add % 10)
            answer.next = temp
        if carry:
            temp = answer
            answer = ListNode(1)
            answer.next = temp
        return answer

# V1'''''
# http://bookshadow.com/weblog/2016/10/29/leetcode-add-two-numbers-ii/
# IDEA : TWO POINTER 
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        s1 = self.getSize(l1)
        s2 = self.getSize(l2)
        s = max(s1, s2)

        p = h = ListNode(0)
        while s:
            p.next = ListNode(0)
            p = p.next
            if s <= s1:
                p.val += l1.val
                l1 = l1.next
            if s <= s2:
                p.val += l2.val
                l2 = l2.next
            s -= 1

        p = h
        while p:
            q = p.next
            while q and q.val == 9:
                q = q.next
            if q and q.val > 9:
                while p != q:
                    p.val += 1
                    p = p.next
                    p.val -= 10
            else: p = q
        return h if h.val else h.next
    
    def getSize(self, h):
        c = 0
        while h:
            c += 1
            h = h.next
        return c

# V2 
# Time:  O(m + n)
# Space: O(m + n)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        stk1, stk2 = [], []
        while l1:
            stk1.append(l1.val)
            l1 = l1.next
        while l2:
            stk2.append(l2.val)
            l2 = l2.next

        prev, head = None, None
        sum = 0
        while stk1 or stk2:
            sum /= 10
            if stk1:
                sum += stk1.pop()
            if stk2:
                sum += stk2.pop()

            head = ListNode(sum % 10)
            head.next = prev
            prev = head

        if sum >= 10:
            head = ListNode(sum / 10)
            head.next = prev
        return head