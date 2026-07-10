"""

1670. Design Front Middle Back Queue
Medium

Design a queue that supports push and pop operations in the front, middle, and back.

Implement the FrontMiddleBack class:

FrontMiddleBack() Initializes the queue.
void pushFront(int val) Adds val to the front of the queue.
void pushMiddle(int val) Adds val to the middle of the queue.
void pushBack(int val) Adds val to the back of the queue.
int popFront() Removes the front element of the queue and returns it. If the queue is empty, return -1.
int popMiddle() Removes the middle element of the queue and returns it. If the queue is empty, return -1.
int popBack() Removes the back element of the queue and returns it. If the queue is empty, return -1.
Notice that when there are two middle position choices, the operation is performed on the frontmost middle position choice. For example:

Pushing 6 into the middle of [1, 2, 3, 4, 5] results in [1, 2, 6, 3, 4, 5].
Popping the middle from [1, 2, 3, 4, 5, 6] returns 3 and results in [1, 2, 4, 5, 6].
 

Example 1:

Input:
["FrontMiddleBackQueue", "pushFront", "pushBack", "pushMiddle", "pushMiddle", "popFront", "popMiddle", "popMiddle", "popBack", "popFront"]
[[], [1], [2], [3], [4], [], [], [], [], []]
Output:
[null, null, null, null, null, 1, 3, 4, 2, -1]

Explanation:
FrontMiddleBackQueue q = new FrontMiddleBackQueue();
q.pushFront(1);   // [1]
q.pushBack(2);    // [1, 2]
q.pushMiddle(3);  // [1, 3, 2]
q.pushMiddle(4);  // [1, 4, 3, 2]
q.popFront();     // return 1 -> [4, 3, 2]
q.popMiddle();    // return 3 -> [4, 2]
q.popMiddle();    // return 4 -> [2]
q.popBack();      // return 2 -> []
q.popFront();     // return -1 -> [] (The queue is empty)
 

Constraints:

1 <= val <= 109
At most 1000 calls will be made to pushFront, pushMiddle, pushBack, popFront, popMiddle, and popBack.

"""

# V0

# V1
# IDEA : ARRAY
# https://leetcode.com/problems/design-front-middle-back-queue/discuss/954022/Python3-Use-Python...
class FrontMiddleBackQueue:

    # time = O(1)
    # space = O(1)
    def __init__(self):
        self.dq = []

    # time = O(n), n = len(dq) (list insert at front)
    # space = O(1)
    def pushFront(self, val):
        self.dq.insert(0, val)

    # time = O(n), n = len(dq) (list insert at middle)
    # space = O(1)
    def pushMiddle(self, val):
        self.dq.insert(len(self.dq) // 2, val)

    # time = O(1) amortized
    # space = O(1)
    def pushBack(self, val):
        self.dq.append(val)

    # time = O(n), n = len(dq) (list pop from front)
    # space = O(1)
    def popFront(self):
        if len(self.dq) == 0:
            return -1
        return self.dq.pop(0)

    # time = O(n), n = len(dq) (list pop from middle)
    # space = O(1)
    def popMiddle(self):
        if len(self.dq) == 0:
            return -1
        if len(self.dq) % 2:
            return self.dq.pop(len(self.dq) // 2)
        return self.dq.pop((len(self.dq) - 1) // 2)

    # time = O(1) amortized
    # space = O(1)
    def popBack(self):
        if len(self.dq) == 0:
            return -1
        return self.dq.pop()

# V1'
# IDEA : ARRAY
# https://leetcode.com/problems/design-front-middle-back-queue/discuss/952168/Python-3-simple-list-implementation-faster-than-100.00
class FrontMiddleBackQueue:

    # time = O(1)
    # space = O(1)
    def __init__(self):
        self.q=[]

    # time = O(n), n = len(q) (list rebuild)
    # space = O(n)
    def pushFront(self, val):
        self.q=[val] + self.q

    # time = O(n), n = len(q) (list slicing/rebuild)
    # space = O(n)
    def pushMiddle(self, val):
        n=len(self.q)//2
        self.q=self.q[:n] + [val] + self.q[n:]

    # time = O(1) amortized
    # space = O(1)
    def pushBack(self, val):
        self.q+=[val]

    # time = O(n), n = len(q) (list pop from front)
    # space = O(1)
    def popFront(self):
        if self.q:
            return self.q.pop(0)
        else: return -1

    # time = O(n), n = len(q) (list pop from middle)
    # space = O(1)
    def popMiddle(self):
        if self.q:
            return self.q.pop(len(self.q)//2-1+len(self.q)%2)
        else: return -1

    # time = O(1) amortized
    # space = O(1)
    def popBack(self):
        if self.q:
            return self.q.pop(-1)
        else: return -1

# V1''
# IDEA : DEQUE
# https://leetcode.com/problems/design-front-middle-back-queue/discuss/952022/Python-balance-two-deques
class FrontMiddleBackQueue:

    # time = O(1)
    # space = O(1)
    def __init__(self):
        self.front = deque()
        self.back = deque()

    # time = O(1) amortized (rebalances by at most one element)
    # space = O(1)
    def _correct_size(self):
        while len(self.back) > len(self.front):
            self.front.append(self.back.popleft())

        while len(self.front) > len(self.back) + 1:
            self.back.appendleft(self.front.pop())

    # time = O(1)
    # space = O(1)
    def pushFront(self, val):
        self.front.appendleft(val)
        self._correct_size()

    # time = O(1)
    # space = O(1)
    def pushMiddle(self, val):
        if len(self.front) > len(self.back):
            self.back.appendleft(self.front.pop())
        self.front.append(val)
        self._correct_size()

    # time = O(1)
    # space = O(1)
    def pushBack(self, val):
        self.back.append(val)
        self._correct_size()

    # time = O(1)
    # space = O(1)
    def popFront(self):
        front = self.front if self.front else self.back
        ret = front.popleft() if front else -1
        self._correct_size()
        return ret

    # time = O(1)
    # space = O(1)
    def popMiddle(self):
        ret = self.front.pop() if self.front else -1
        self._correct_size()
        return ret

    # time = O(1)
    # space = O(1)
    def popBack(self):
        back = self.back if self.back else self.front
        ret = back.pop() if back else -1
        self._correct_size()
        return ret

# V2
