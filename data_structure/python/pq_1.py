#---------------------------------------------------------------
# PRIORITY QUEUE (V1)
#---------------------------------------------------------------

# https://pieriantraining.com/python-tutorial-creating-a-priority-queue-in-python-2/

# V1 : via heapq
import heapq

queue = []

def enqueue(item, priority):
    heapq.heappush(queue, (priority, item))

def dequeue():
    if not queue:
        return None
    return heapq.heappop(queue)[1]

# V2 : via PriorityQueue
import queue

queue = queue.PriorityQueue()

def enqueue(item, priority):
    queue.put((priority, item))

def dequeue():
    if not queue:
        return None
    return queue.get()[1]
 