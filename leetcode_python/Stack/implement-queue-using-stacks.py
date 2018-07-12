"""

Implement the following operations of a queue using stacks.

push(x) -- Push element x to the back of queue.
pop() -- Removes the element from in front of queue.
peek() -- Get the front element.
empty() -- Return whether the queue is empty.
Example:

MyQueue queue = new MyQueue();

queue.push(1);
queue.push(2);  
queue.peek();  // returns 1
queue.pop();   // returns 1
queue.empty(); // returns false
Notes:

You must use only standard operations of a stack -- which means only push to top, peek/pop from top, size, and is empty operations are valid.
Depending on your language, stack may not be supported natively. You may simulate a stack by using a list or deque (double-ended queue), as long as you use only standard operations of a stack.
You may assume that all operations are valid (for example, no pop or peek operations will be called on an empty queue).


"""

# Time:  O(1), amortized
# Space: O(n)
#
# Implement the following operations of a queue using stacks.
#
# push(x) -- Push element x to the back of queue.
# pop() -- Removes the element from in front of queue.
# peek() -- Get the front element.
# empty() -- Return whether the queue is empty.
#
# Notes:
# You must use only standard operations of a stack
# -- which means only push to top, peek/pop from top, size, and is empty operations are valid.
# Depending on your language, stack may not be supported natively.
# You may simulate a stack by using a list or deque (double-ended queue),
# as long as you use only standard operations of a stack.
# You may assume that all operations are valid
# (for example, no pop or peek operations will be called on an empty queue).
#


# V1 : dev 

"""
class MyQueue(object):

    def __init__(self):
        self.A = []
        self.B = []

    def push(self, x):
        self.A.append(x)
        

    def pop(self):
        return self.A.pop()
        

    def peek(self):
        return self.A[-1]
        

    def empty(self):
        if len(self.A) ==0:
            return True
        else:
            return False 
"""


# V2 
# https://blog.csdn.net/coder_orz/article/details/51586814
# inStack  : for push operation 
# outStack : for pop/peek  operation

class Queue(object):
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
        self.outStack.pop()

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


# V3 
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



