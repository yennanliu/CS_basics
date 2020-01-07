# V0 

# V1
# https://blog.csdn.net/qq_32424059/article/details/86998300
class MyCircularQueue(object):
 
    def __init__(self, k):
        """
        Initialize your data structure here. Set the size of the queue to be k.
        :type k: int
        """
        self.queue = [""] * k
        self.max_length = k
        self.start = -1
        self.end = -1
 
    def enQueue(self, value):
        """
        Insert an element into the circular queue. Return true if the operation is successful.
        :type value: int
        :rtype: bool
        """
        
        if not self.isFull():
            if self.start == -1:
                self.start = 0
            self.end = (self.end + 1) % self.max_length
            self.queue[self.end] = value
            return True
        else:
            return False
        
        
 
    def deQueue(self):
        """
        Delete an element from the circular queue. Return true if the operation is successful.
        :rtype: bool
        """
        if not self.isEmpty():       
            if self.start == self.end: # the last element
                self.start, self.end = -1, -1
                # self.end = -1
            else:
                self.start = (self.start + 1) % self.max_length
            return True
        else:
            return False
        
 
    def Front(self):
        """
        Get the front item from the queue.
        :rtype: int
        """
        return -1 if self.isEmpty() else self.queue[self.start]
 
 
    def Rear(self):
        """
        Get the last item from the queue.
        :rtype: int
        """
        return -1 if self.isEmpty() else self.queue[self.end]
 
    def isEmpty(self):
        """
        Checks whether the circular queue is empty or not.
        :rtype: bool
        """
        return self.start == -1 and self.end == -1
 
    def isFull(self):
        """
        Checks whether the circular queue is full or not.
        :rtype: bool
        """
        return (self.end + 1) % self.max_length == self.start

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/81027583
class MyCircularQueue(object):

    def __init__(self, k):
        """
        Initialize your data structure here. Set the size of the queue to be k.
        :type k: int
        """
        self.queue = []
        self.size = k
        self.front = 0
        self.rear = 0

    def enQueue(self, value):
        """
        Insert an element into the circular queue. Return true if the operation is successful.
        :type value: int
        :rtype: bool
        """
        if self.rear - self.front < self.size:
            self.queue.append(value)
            self.rear += 1
            return True
        else:
            return False

    def deQueue(self):
        """
        Delete an element from the circular queue. Return true if the operation is successful.
        :rtype: bool
        """
        if self.rear - self.front > 0:
            self.front += 1
            return True
        else:
            return False

    def Front(self):
        """
        Get the front item from the queue.
        :rtype: int
        """
        if self.isEmpty():
            return -1
        else:
            return self.queue[self.front]

    def Rear(self):
        """
        Get the last item from the queue.
        :rtype: int
        """
        if self.isEmpty():
            return -1
        else:
            return self.queue[self.rear - 1]

    def isEmpty(self):
        """
        Checks whether the circular queue is empty or not.
        :rtype: bool
        """
        return self.front == self.rear

    def isFull(self):
        """
        Checks whether the circular queue is full or not.
        :rtype: bool
        """
        return self.rear - self.front == self.size
# Your MyCircularQueue object will be instantiated and called as such:
# obj = MyCircularQueue(k)
# param_1 = obj.enQueue(value)
# param_2 = obj.deQueue()
# param_3 = obj.Front()
# param_4 = obj.Rear()
# param_5 = obj.isEmpty()
# param_6 = obj.isFull()

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/81027583
class MyCircularQueue(object):

    def __init__(self, k):
        """
        Initialize your data structure here. Set the size of the queue to be k.
        :type k: int
        """
        self.queue = []
        self.size = k
        self.rear = 0

    def enQueue(self, value):
        """
        Insert an element into the circular queue. Return true if the operation is successful.
        :type value: int
        :rtype: bool
        """
        if not self.isFull():
            self.queue.append(value)
            self.rear += 1
            return True
        else:
            return False

    def deQueue(self):
        """
        Delete an element from the circular queue. Return true if the operation is successful.
        :rtype: bool
        """
        if not self.isEmpty():
            self.queue.pop(0)
            self.rear -= 1
            return True
        else:
            return False

    def Front(self):
        """
        Get the front item from the queue.
        :rtype: int
        """
        if self.isEmpty():
            return -1
        else:
            return self.queue[0]

    def Rear(self):
        """
        Get the last item from the queue.
        :rtype: int
        """
        if self.isEmpty():
            return -1
        else:
            return self.queue[self.rear - 1]

    def isEmpty(self):
        """
        Checks whether the circular queue is empty or not.
        :rtype: bool
        """
        return 0 == self.rear

    def isFull(self):
        """
        Checks whether the circular queue is full or not.
        :rtype: bool
        """
        return self.rear == self.size


# Your MyCircularQueue object will be instantiated and called as such:
# obj = MyCircularQueue(k)
# param_1 = obj.enQueue(value)
# param_2 = obj.deQueue()
# param_3 = obj.Front()
# param_4 = obj.Rear()
# param_5 = obj.isEmpty()
# param_6 = obj.isFull()

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/81027583
class MyCircularQueue(object):

    def __init__(self, k):
        """
        Initialize your data structure here. Set the size of the queue to be k.
        :type k: int
        """
        self.queue = []
        self.size = k

    def enQueue(self, value):
        """
        Insert an element into the circular queue. Return true if the operation is successful.
        :type value: int
        :rtype: bool
        """
        if not self.isFull():
            self.queue.append(value)
            return True
        else:
            return False

    def deQueue(self):
        """
        Delete an element from the circular queue. Return true if the operation is successful.
        :rtype: bool
        """
        if not self.isEmpty():
            self.queue.pop(0)
            return True
        else:
            return False

    def Front(self):
        """
        Get the front item from the queue.
        :rtype: int
        """
        if self.isEmpty():
            return -1
        else:
            return self.queue[0]

    def Rear(self):
        """
        Get the last item from the queue.
        :rtype: int
        """
        if self.isEmpty():
            return -1
        else:
            return self.queue[-1]

    def isEmpty(self):
        """
        Checks whether the circular queue is empty or not.
        :rtype: bool
        """
        return 0 == len(self.queue)

    def isFull(self):
        """
        Checks whether the circular queue is full or not.
        :rtype: bool
        """
        return len(self.queue) == self.size

# V2
