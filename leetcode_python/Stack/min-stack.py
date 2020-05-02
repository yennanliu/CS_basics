# Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
#
# push(x) -- Push element x onto stack.
# pop() -- Removes the element on top of the stack.
# top() -- Get the top element.
# getMin() -- Retrieve the minimum element in the stack.
# Example:
# MinStack minStack = new MinStack();
# minStack.push(-2);
# minStack.push(0);
# minStack.push(-3);
# minStack.getMin();   --> Returns -3.
# minStack.pop();
# minStack.top();      --> Returns 0.
# minStack.getMin();   --> Returns -2.
#
#
# Time:  O(n)
# Space: O(1)
#
# Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
#
# push(x) -- Push element x onto stack.
# pop() -- Removes the element on top of the stack.
# top() -- Get the top element.
# getMin() -- Retrieve the minimum element in the stack.
#
#
### Stack : 「Last-In-First-Out」
### http://alrightchiu.github.io/SecondRound/stack-introjian-jie.html

# V0
# IDEA : STACK
# IDEA : 
# -> USE A STACK TO STORAGE MIN VALUE IN THE STACK WHEN EVERY PUSH
# -> SO WE CAN RETURN getMin IN CONSTANT TIEM VIA STACK ABOVE
class MinStack(object):

    def __init__(self):
        """
        initialize your data structure here.
        """
        self.stack = []
        
    def push(self, x):
        if not self.stack:
            self.stack.append((x, x))
        ### NOTICE HERE 
        # stack[i][1] save to min value when every push
        # so the latest min in stack is at stack[-1][1]
        else:
            self.stack.append((x, min(x, self.stack[-1][1])))
        
    def pop(self):
        self.stack.pop()
        
    def top(self):
        return self.stack[-1][0]
        
    def getMin(self):
        # the latest min in stack is at stack[-1][1]
        return self.stack[-1][1]

# V0'
# IDEA : STACK
class MinStack(object):

    def __init__(self):
        self.stack = []
        self.min = []
        

    def push(self, x):
        self.stack.append(x)
        if not self.min:
            self.min.append(x)
        else:
            self.min.append(min(self.min[-1], x))

    def pop(self):
        self.stack.pop()
        self.min.pop()

    def top(self):
        return self.stack[-1]

    def getMin(self):
        return self.min[-1]

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79253237
# IDEA : STACK
class MinStack(object):

    def __init__(self):
        """
        initialize your data structure here.
        """
        self.stack = []
        

    def push(self, x):
        """
        :type x: int
        :rtype: void
        """
        if not self.stack:
            self.stack.append((x, x))
        else:
            self.stack.append((x, min(x, self.stack[-1][1])))
        

    def pop(self):
        """
        :rtype: void
        """
        return self.stack.pop()[0]
        

    def top(self):
        """
        :rtype: int
        """
        return self.stack[-1][0]
        

    def getMin(self):
        """
        :rtype: int
        """
        return self.stack[-1][1]

    def print_stack(self):
        print (self.stack)

### Test case
s=MinStack()
assert s.__init__() == None
assert s.push(-2) == None
assert s.push(0) == None
assert s.push(-3) == None
assert s.getMin() == -3
assert s.pop() == -3
assert s.top() ==0

s=MinStack()
assert s.__init__() == None
assert s.push(100) == None
assert s.push(99) == None
assert s.pop() == 99
assert s.top() ==100

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79253237
class MinStack(object):

    def __init__(self):
        """
        initialize your data structure here.
        """
        self.stack = []
        self.min = []
        

    def push(self, x):
        """
        :type x: int
        :rtype: void
        """
        self.stack.append(x)
        if not self.min:
            self.min.append(x)
        else:
            self.min.append(min(self.min[-1], x))

    def pop(self):
        """
        :rtype: void
        """
        self.stack.pop()
        self.min.pop()

    def top(self):
        """
        :rtype: int
        """
        return self.stack[-1]

    def getMin(self):
        """
        :rtype: int
        """
        return self.min[-1]

# V1''''
# https://www.cnblogs.com/zuoyuan/p/4091870.html
# stack1 : regular stack
# stack2 : stack for return minimum in-stack element 
class MinStack:
    # @param x, an integer
    def __init__(self):
        self.stack1 = []
        self.stack2 = []
    # @return an integer
    def push(self, x):
        self.stack1.append(x)
        if len(self.stack2) == 0 or x <= self.stack2[-1]:
            self.stack2.append(x)

    # @return nothing
    def pop(self):
        top = self.stack1[-1]
        self.stack1.pop()
        if top == self.stack2[-1]:
            self.stack2.pop()
        
    # @return an integer
    def top(self):
        return self.stack1[-1]

    # @return an integer
    def getMin(self):
        return self.stack2[-1]

# V1'''''
class MinStack:
    def __init__(self):
        self.min = None
        self.stack = []

    # @param x, an integer
    # @return an integer
    def push(self, x):
        if not self.stack:
            self.stack.append(0)
            self.min = x
        else:
            self.stack.append(x - self.min)
            if x < self.min:
                self.min = x

    # @return nothing
    def pop(self):
        x = self.stack.pop()
        if x < 0:
            self.min = self.min - x

    # @return an integer
    def top(self):
        x = self.stack[-1]
        if x > 0:
            return x + self.min
        else:
            return self.min

    # @return an integer
    def getMin(self):
        return self.min

# V1'''''''
# Time:  O(n)
# Space: O(n)
class MinStack2:
    def __init__(self):
        self.stack, self.minStack = [], []
    # @param x, an integer
    # @return an integer
    def push(self, x):
        self.stack.append(x)
        if len(self.minStack):
            if x < self.minStack[-1][0]:
                self.minStack.append([x, 1])
            elif x == self.minStack[-1][0]:
                self.minStack[-1][1] += 1
        else:
            self.minStack.append([x, 1])

    # @return nothing
    def pop(self):
        x = self.stack.pop()
        if x == self.minStack[-1][0]:
            self.minStack[-1][1] -= 1
            if self.minStack[-1][1] == 0:
                self.minStack.pop()

    # @return an integer
    def top(self):
        return self.stack[-1]

    # @return an integer
    def getMin(self):
        return self.minStack[-1][0]

# if __name__ == "__main__":
#     stack = MinStack()
#     stack.push(-1)
#     print([stack.top(), stack.getMin()])

# V1'''''''''
# Time:  O(n)
# Space: O(1)
class MinStack(object):
    def __init__(self):
        self.min = None
        self.stack = []

    # @param x, an integer
    # @return an integer
    def push(self, x):
        if not self.stack:
            self.stack.append(0)
            self.min = x
        else:
            self.stack.append(x - self.min)
            if x < self.min:
                self.min = x

    # @return nothing
    def pop(self):
        x = self.stack.pop()
        if x < 0:
            self.min = self.min - x

    # @return an integer
    def top(self):
        x = self.stack[-1]
        if x > 0:
            return x + self.min
        else:
            return self.min

    # @return an integer
    def getMin(self):
        return self.min

# V2 
# Time:  O(n)
# Space: O(n)
class MinStack2(object):
    def __init__(self):
        self.stack, self.minStack = [], []
    # @param x, an integer
    # @return an integer
    def push(self, x):
        self.stack.append(x)
        if len(self.minStack):
            if x < self.minStack[-1][0]:
                self.minStack.append([x, 1])
            elif x == self.minStack[-1][0]:
                self.minStack[-1][1] += 1
        else:
            self.minStack.append([x, 1])

    # @return nothing
    def pop(self):
        x = self.stack.pop()
        if x == self.minStack[-1][0]:
            self.minStack[-1][1] -= 1
            if self.minStack[-1][1] == 0:
                self.minStack.pop()

    # @return an integer
    def top(self):
        return self.stack[-1]

    # @return an integer
    def getMin(self):
        return self.minStack[-1][0]

# V3
# time: O(1)
# space: O(n)
class MinStack3(object):

    def __init__(self):
        self.stack = []

    def push(self, x):
        if self.stack:
            current_min = min(x, self.stack[-1][0])
            self.stack.append((current_min, x))
        else:
            self.stack.append((x, x))

    def pop(self):
        return self.stack.pop()[1]

    def top(self):
        return self.stack[-1][1]

    def getMin(self):
        return self.stack[-1][0]

# V8 
# Time:  O(1), amortized
# Space: O(n)
class Queue(object):
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
