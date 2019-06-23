


# V1 : DEV 




# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79380457
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
            l1 = l1.__next__
        while l2:
            num2 += str(l2.val)
            l2 = l2.__next__
        add = str(int(num1) + int(num2))
        head = ListNode(add[0])
        answer = head
        for i in range(1, len(add)):
            node = ListNode(add[i])
            head.next = node
            head = head.__next__
        return answer


# V3 
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
            l1 = l1.__next__
        while l2:
            stk2.append(l2.val)
            l2 = l2.__next__

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


