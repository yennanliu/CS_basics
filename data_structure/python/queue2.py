#################################################################
# DATA STRUCTURE DEMO : Queue (Mehtod 2)
#################################################################

# V0
class Queue(object):
    def __init__(self, limit = 10):
        self.queue = []
        self.front = None
        self.rear = None
        self.limit = limit
        self.size = 0

    def __str__(self):
        return ' '.join([str(i) for i in self.queue])

    # to check if queue is empty
    def isEmpty(self):
        return self.size <= 0

    # to add an element from the rear end of the queue
    def enqueue(self, data):
        if self.size >= self.limit:
            return -1          # queue overflow
        else:
            """
            BEWARE OF IT
            -> the queue is in "inverse" order to the array which is the way we implement here in python 
            i.e. 
                q = [1,2,3]
                but the q is start from 1, end at 3 actually 
            e.g.
                dequeue <---- 1, 2 ,3  <---- enqueue 
            """
            self.queue.append(data)

        # assign the rear as size of the queue and front as 0
        if self.front is None:
            self.front = self.rear = 0
        else:
            self.rear = self.size
            
        self.size += 1

    # to pop an element from the front end of the queue
    def dequeue(self):
        if self.isEmpty():
            return -1          # queue underflow
        else:
            """
            BEWARE OF IT 
            x = [1,2,3]
            x.pop(0)
            -> x = [2,3]
            """
            self.queue.pop(0)
            self.size -= 1
            if self.size == 0:
                self.front = self.rear = 0
            else:
                self.rear = self.size - 1

    def getSize(self):
        return self.size

# V1
# https://github.com/yennanliu/Data-Structures-using-Python/blob/master/Queue/Queue.py
class Queue(object):
    def __init__(self, limit = 10):
        self.queue = []
        self.front = None
        self.rear = None
        self.limit = limit
        self.size = 0

    def __str__(self):
        return ' '.join([str(i) for i in self.queue])

    # to check if queue is empty
    def isEmpty(self):
        return self.size <= 0

    # to add an element from the rear end of the queue
    def enqueue(self, data):
        if self.size >= self.limit:
            return -1          # queue overflow
        else:
            self.queue.append(data)

        # assign the rear as size of the queue and front as 0
        if self.front is None:
            self.front = self.rear = 0
        else:
            self.rear = self.size

        self.size += 1

    # to pop an element from the front end of the queue
    def dequeue(self):
        if self.isEmpty():
            return -1          # queue underflow
        else:
            self.queue.pop(0)
            self.size -= 1
            if self.size == 0:
                self.front = self.rear = 0
            else:
                self.rear = self.size - 1

    def getSize(self):
        return self.size

# if __name__ == '__main__':
#     myQueue = Queue()
#     for i in range(10):
#         myQueue.enqueue(i)
#     print(myQueue)
#     print(('Queue Size:',myQueue.getSize()))
#     myQueue.dequeue()
#     print(myQueue)
#     print(('Queue Size:',myQueue.getSize()))