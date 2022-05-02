"""

232. Implement Queue using Stacks
Easy

Implement a first in first out (FIFO) queue using only two stacks. The implemented queue should support all the functions of a normal queue (push, peek, pop, and empty).

Implement the MyQueue class:

void push(int x) Pushes element x to the back of the queue.
int pop() Removes the element from the front of the queue and returns it.
int peek() Returns the element at the front of the queue.
boolean empty() Returns true if the queue is empty, false otherwise.
Notes:

You must use only standard operations of a stack, which means only push to top, peek/pop from top, size, and is empty operations are valid.
Depending on your language, the stack may not be supported natively. You may simulate a stack using a list or deque (double-ended queue) as long as you use only a stack's standard operations.
 

Example 1:

Input
["MyQueue", "push", "push", "peek", "pop", "empty"]
[[], [1], [2], [], [], []]
Output
[null, null, null, 1, 1, false]

Explanation
MyQueue myQueue = new MyQueue();
myQueue.push(1); // queue is: [1]
myQueue.push(2); // queue is: [1, 2] (leftmost is front of the queue)
myQueue.peek(); // return 1
myQueue.pop(); // return 1, queue is [2]
myQueue.empty(); // return false
 

Constraints:

1 <= x <= 9
At most 100 calls will be made to push, pop, peek, and empty.
All the calls to pop and peek are valid.
 

Follow-up: Can you implement the queue such that each operation is amortized O(1) time complexity? In other words, performing n operations will take overall O(n) time even if one of those operations may take longer.

"""

# V0 
# IDEA : STACK 
class MyQueue:
    def __init__(self):
        self.stack = []

    def push(self, x):  
        tmp = []
        while self.stack:
            tmp.append(self.stack.pop())
        tmp.append(x)
        while tmp:
            self.stack.append(tmp.pop())

    def pop(self):
        return self.stack.pop()

    def peek(self):
        return self.stack[-1]

    def empty(self):
        return self.stack == []

# V0'
# IDEA : TWO STACK
class MyQueue(object):
    def __init__(self):
        self.input = []
        self.output = []

    def push(self, x):
        self.input.append(x)

    def pop(self):
        self.peek()
        return self.output.pop()

    def peek(self):
        ### BE AWARE OF IT 
        while not self.output:
            while self.input:
                self.output.append(self.input.pop())

        return self.output[-1]

    def empty(self):
        return not self.input and not self.output

# V1
# https://leetcode.com/problems/implement-queue-using-stacks/discuss/192598/python-easy-solution
class MyQueue(object):
    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.input = []
        self.output = []

    def push(self, x):
        """
        Push element x to the back of queue.
        :type x: int
        :rtype: None
        """
        self.input.append(x)

    def pop(self):
        """
        Removes the element from in front of queue and returns that element.
        :rtype: int
        """
        self.peek()
        return self.output.pop()

    def peek(self):
        """
        Get the front element.
        :rtype: int
        """
        while not self.output:
            while self.input:
                self.output.append(self.input.pop())

        return self.output[-1]

    def empty(self):
        """
        Returns whether the queue is empty.
        :rtype: bool
        """
        return not self.input and not self.output

### Test case
q=MyQueue()
assert q.push(1) == None
assert q.push(2) == None
assert q.peek() == 1
assert q.push(3) == None
assert q.peek() == 1
assert q.pop() == 1
assert q.pop() == 2
assert q.pop() == 3
assert q.empty() == True

# V1'
# https://blog.csdn.net/coder_orz/article/details/51586814
# inStack  : for push operation 
# outStack : for pop/peek  operation
class MyQueue(object):
    def __init__(self):
        """
        initialize your data structure here.
        """
        self.inStack, self.outStack = [], []

    def push(self, x):
        """
        :type x: int
        :rtype: nothing
        """
        self.inStack.append(x)

    def pop(self):
        """
        :rtype: nothing
        """
        self.peek()
        return self.outStack.pop()

    def peek(self):
        """
        :rtype: int
        """
        if not self.outStack:
            while self.inStack:
                self.outStack.append(self.inStack.pop())
        return self.outStack[-1]

    def empty(self):
        """
        :rtype: bool
        """
        return not self.inStack and not self.outStack

# V1''
# https://leetcode.com/problems/implement-queue-using-stacks/discuss/192598/python-easy-solution
class MyQueue:

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.stack = []

    def push(self, x):
        """
        Push element x to the back of queue.
        :type x: int
        :rtype: void
        """    
        tmp = []
        while self.stack:
            tmp.append(self.stack.pop())
        tmp.append(x)
        while tmp:
            self.stack.append(tmp.pop())

    def pop(self):
        """
        Removes the element from in front of queue and returns that element.
        :rtype: int
        """
        return self.stack.pop()

    def peek(self):
        """
        Get the front element.
        :rtype: int
        """
        return self.stack[-1]

    def empty(self):
        """
        Returns whether the queue is empty.
        :rtype: bool
        """
        return self.stack == []

# V1'''''
# https://leetcode.com/problems/implement-queue-using-stacks/solution/
# JAVA

# V2
class Queue:
    # initialize your data structure here.
    def __init__(self):
        self.A, self.B = [], []

    # @param x, an integer
    # @return nothing
    def push(self, x):
        self.A.append(x)

    # @return an integer
    def pop(self):
        self.peek()
        return self.B.pop()

    # @return an integer
    def peek(self):
        if not self.B:
            while self.A:
                self.B.append(self.A.pop())
        return self.B[-1]

    # @return an boolean
    def empty(self):
        return not self.A and not self.B
