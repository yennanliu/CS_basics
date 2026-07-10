"""

155. Min Stack
Easy

Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.

Implement the MinStack class:

MinStack() initializes the stack object.
void push(int val) pushes the element val onto the stack.
void pop() removes the element on the top of the stack.
int top() gets the top element of the stack.
int getMin() retrieves the minimum element in the stack.
 

Example 1:

Input
["MinStack","push","push","push","getMin","pop","top","getMin"]
[[],[-2],[0],[-3],[],[],[],[]]

Output
[null,null,null,null,-3,null,0,-2]

Explanation
MinStack minStack = new MinStack();
minStack.push(-2);
minStack.push(0);
minStack.push(-3);
minStack.getMin(); // return -3
minStack.pop();
minStack.top();    // return 0
minStack.getMin(); // return -2
 

Constraints:

-231 <= val <= 231 - 1
Methods pop, top and getMin operations will always be called on non-empty stacks.
At most 3 * 104 calls will be made to push, pop, top, and getMin.

"""


# V0
# IDEA: 2 STACKS
"""
NOTE !!!


so `minStack` is NOT storing the `actual element in from min to max`,
but it saved the `min element`  (as a status) in each iteration,
-> so we can easily fetch the `min element state` via get
   the top element from  `minStack` via every API call



e.g.

minStack does not store all elements in sorted order.


-> 
Instead, minStack[i] stores the minimum value seen
in the main stack up to position i.

"""
# time = O(1)  # per operation (push/pop/top/getMin)
# space = O(n)
class MinStack(object):

    def __init__(self):
        self.stack = []
        """
        NOTE !!!

        # min_stack[i] stores the minimum value (state)
        # among st[0...i]

        """
        self.minStack = []

    def push(self, val):
        self.stack.append(val)

        """
        NOTE !!!

        below
        """
        if not self.minStack:
            self.minStack.append(val)
        else:
            self.minStack.append(min(val, self.minStack[-1]))

    def pop(self):
        """
        NOTE !!!

        since min_stack[i] stores the cur minimum value (state),
        -> so we DON'T need to check if minStack pop element
           is `same` as the one pop from stack

           -> since  `self.minStack` is saving STATE,
              not the actual element

        """
        self.minStack.pop()
        return self.stack.pop()

    def top(self):
        return self.stack[-1]

    def getMin(self):
        return self.minStack[-1]



# V0-1
# IDEA: ARRAY + MAP
# NOTE !!! the getMin time complexity is O(N), NOT O(1)
# time = O(1)  # push/pop/top O(1); getMin O(n)
# space = O(n)
class MinStack(object):

    def __init__(self):
        self.arr = []
        self.counts = {}

    def push(self, val):
        self.arr.append(val)
        self.counts[val] = self.counts.get(val, 0) + 1

    def pop(self):
        val = self.arr.pop()

        self.counts[val] -= 1
        if self.counts[val] == 0:
            del self.counts[val]

        return val

    def top(self):
        return self.arr[-1]

    def getMin(self):
        return min(self.counts.keys())



# V0
# IDEA : STACK
# IDEA :
# -> USE A STACK TO STORAGE MIN VALUE IN THE STACK WHEN EVERY PUSH
# -> SO WE CAN RETURN getMin IN CONSTANT TIEM VIA STACK ABOVE
# time = O(1)  # per operation (push/pop/top/getMin)
# space = O(n)
class MinStack(object):

    def __init__(self):
        """
        initialize your data structure here.
        """
        self.stack = []

    def push(self, x):
        if not self.stack:
            """
            NOTE : we use stack = [(x, y)]
                    x is the current element
                    y in current MIN value in current stack
            """
            ### note here
            self.stack.append((x, x))
        ### NOTICE HERE 
        # stack[i][1] save to min value when every push
        # so the latest min in stack is at stack[-1][1]
        else:
            ### note here
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
# time = O(1)  # per operation (push/pop/top/getMin)
# space = O(n)
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

# V0
# IDEA : heap
# TODO : validate if below is correct
# time = O(log n) for push/pop, O(1) for top, O(log n) for getMin (as implemented, pops from heap)
# space = O(n)
from heapq import *
class MinStack:

    def __init__(self):
        self.stack = []
        self.size = 0

    def push(self, val):
        heappush(self.stack, val)
        self.size += 1

    def pop(self):
        if self.size > 0:
            tmp = self.stack.pop(-1)
            self.size -= 1
            return tmp
        
    def top(self):
        if self.size > 0:
            return self.stack[-1]
    
    def getMin(self):
        if self.size > 0:
            self.size -= 1
            return heappop(self.stack)
            #return nsmallest(1, self.stack)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79253237
# IDEA : STACK
# time = O(1)  # per operation (push/pop/top/getMin)
# space = O(n)
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
# time = O(1)  # per operation (push/pop/top/getMin)
# space = O(n)
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
# time = O(1)  # per operation (push/pop/top/getMin)
# space = O(n)
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
# time = O(1)  # per operation (push/pop/top/getMin)
# space = O(n)
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
# time = O(1)  # per operation (push/pop/top/getMin)
# space = O(n)
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
