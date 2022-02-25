#---------------------------------------------------------------
# MAX HEAP
#
#
# https://docs.python.org/3/library/heapq.html
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/sell-diminishing-valued-colored-balls.py#L184
#---------------------------------------------------------------

# V1
# NOTE !!! : in py, (heapq) heap implementation is "MINIMUM heap"
import heapq
class MaxHeap:
    def __init__(self):
        self.queue = []
        
    def __len__(self):
        return len(self.queue)
        
    def top(self):
        if len(self.queue)==0:
            return 0
        return self.queue[0] * -1
    
    def push(self, val):
        heapq.heappush(self.queue, -val)
    
    def pop(self):
        return heapq.heappop(self.queue) * -1