"""

716 Max Stack

# https://cheonhyangzhang.gitbooks.io/leetcode-solutions/content/716-max-stack.html

Problem
Design a max stack that supports push, pop, top, peekMax and popMax.

push(x) -- Push element x onto stack.
pop() -- Remove the element on top of the stack and return it.
top() -- Get the element on the top.
peekMax() -- Retrieve the maximum element in the stack.
popMax() -- Retrieve the maximum element in the stack, and remove it. If you find more than one maximum elements, only remove the top-most one.
Example 1:

MaxStack stack = new MaxStack();
stack.push(5); 
stack.push(1);
stack.push(5);
stack.top(); -> 5
stack.popMax(); -> 5
stack.top(); -> 1
stack.peekMax(); -> 5
stack.pop(); -> 1
stack.top(); -> 5
Note: -1e7 <= x <= 1e7 Number of operations won't exceed 10000. The last four operations won't be called when stack is empty.

"""

# V0

# V1
# IDEA : array
# https://github.com/qiyuangong/leetcode/blob/master/python/716_Max_Stack.py
# https://poopcode.com/max-stack-leetcode-challenge-python-solution/
class MaxStack(object):

    def __init__(self):
        """
        initialize your data structure here.
        """
        self.stack = []
        self.max_stack = []

    def push(self, x):
        """
        :type x: int
        :rtype: void
        """
        self.stack.append(x)
        if len(self.max_stack) == 0:
            self.max_stack.append(x)
            return
        if self.max_stack[-1] > x:
            self.max_stack.append(self.max_stack[-1])
        else:
            self.max_stack.append(x)

    def pop(self):
        """
        :rtype: int
        """
        if len(self.stack) != 0:
            self.max_stack.pop(-1)
            return self.stack.pop(-1)

    def top(self):
        """
        :rtype: int
        """
        return self.stack[-1]

    def peekMax(self):
        """
        :rtype: int
        """
        if len(self.max_stack) != 0:
            return self.max_stack[-1]

    def popMax(self):
        """
        :rtype: int
        """
        val = self.peekMax()
        buff = []
        while self.top() != val:
            buff.append(self.pop())
        self.pop()
        while len(buff) != 0:
            self.push(buff.pop(-1))
        return val

# V1'
# IDEA : linked list
# https://codereview.stackexchange.com/questions/210914/leetcode-maxstack-in-python
class Node:
    def __init__(self, x):
        self.val = x
        self.next = None


class MaxStack:
    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.head = None
        self.max_val = None

    def push(self, x):
        """
        Push element x onto stack.
        :type x: int
        :rtype: void
        """
        if self.head:
            n = Node(x)
            n.next = self.head
            self.head = n
        else:
            self.head = Node(x)
        self.max_val = max(x, self.max_val) if self.max_val or self.max_val == 0 else x

    def pop(self):
        """
        Removes the element on top of the stack and returns that element.
        :rtype: int
        """
        rtn = None
        if self.head:
            rtn = self.head.val
            self.head = self.head.next

        head = self.head
        v = head.val if head else None
        while head:
            v = max(v, head.val)
            head = head.next
        self.max_val = v
        return rtn

    def top(self):
        """
        Get the top element.
        :rtype: int
        """
        if self.head:
            return self.head.val

    def peekMax(self):
        """
        Retrieve the maximum element in the stack.
        :rtype: int
        """
        return self.max_val

    def popMax(self):
        """
        Retrieve the maximum element in the stack, and remove it. If you find more than one maximum elements, only remove the top-most one.
        :rtype: void
        """
        prev, cur = None, self.head
        while cur:
            if cur.val == self.max_val and cur == self.head:
                self.head = cur.next
                break
            elif cur.val == self.max_val:
                prev.next = cur.next
                break

            prev, cur = cur, cur.next

        cur = self.head
        tmp = self.max_val
        v = cur.val if cur else None
        while cur:
            if cur:
                v = max(v, cur.val)
            cur = cur.next
        self.max_val = v
        return tmp

# V1''
# IDEA : array
# https://blog.csdn.net/danspace1/article/details/88734584
class MaxStack(object):

	def __init__(self):
		self.data = []

	def push(self, x):
		self.data.insert(0, x)

	def pop(self):
		self.data.pop(0)

	def top(self):
		self.data[0]

	def peakMax(self):
		res = max(self.data)
		self.data.remove(res)
		return res

# V2