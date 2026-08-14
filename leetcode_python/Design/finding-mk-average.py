"""

1825. Finding MK Average
Hard

You are given two integers, m and k, and a stream of integers. You are tasked to implement a data structure that calculates the MKAverage for the stream.

The MKAverage can be calculated using these steps:

If the number of the elements in the stream is less than m you should consider the MKAverage to be -1. Otherwise, copy the last m elements of the stream to a separate container.
Remove the smallest k elements and the largest k elements from the container.
Calculate the average value for the rest of the elements rounded down to the nearest integer.

Implement the MKAverage class:

MKAverage(int m, int k) Initializes the MKAverage object with an empty stream and the two integers m and k.
void addElement(int num) Inserts a new element num into the stream.
int calculateMKAverage() Calculates and returns the MKAverage for the current stream rounded down to the nearest integer.


Example 1:

Input
["MKAverage", "addElement", "addElement", "calculateMKAverage", "addElement", "calculateMKAverage", "addElement", "addElement", "addElement", "calculateMKAverage"]
[[3, 1], [3], [1], [], [10], [], [5], [5], [5], []]
Output
[null, null, null, -1, null, 3, null, null, null, 5]

Explanation
MKAverage obj = new MKAverage(3, 1);
obj.addElement(3);        // current elements are [3]
obj.addElement(1);        // current elements are [3,1]
obj.calculateMKAverage(); // return -1, because m = 3 and only 2 elements exist.
obj.addElement(10);       // current elements are [3,1,10]
obj.calculateMKAverage(); // The last 3 elements are [3,1,10].
                          // After removing smallest and largest 1 element the container will be [3].
                          // The average of [3] equals 3/1 = 3, return 3
obj.addElement(5);        // current elements are [3,1,10,5]
obj.addElement(5);        // current elements are [3,1,10,5,5]
obj.addElement(5);        // current elements are [3,1,10,5,5,5]
obj.calculateMKAverage(); // The last 3 elements are [5,5,5].
                          // After removing smallest and largest 1 element the container will be [5].
                          // The average of [5] equals 5/1 = 5, return 5


Constraints:

3 <= m <= 10^5
1 < k*2 < m
1 <= num <= 10^5
At most 10^5 calls will be made to addElement and calculateMKAverage.

"""

# V0
# IDEA : SLIDING WINDOW (deque) + FENWICK TREE INDEXED BY VALUE
#
#   two independent jobs:
#     - keep only the last m elements  -> a deque, pop the oldest on overflow
#     - answer "sum of the k smallest inside the window" fast
#
#   because 1 <= num <= 10^5, index a Fenwick tree BY VALUE and store two
#   parallel arrays : how many copies of that value are live, and their sum.
#   then `prefix_sum_of_k_smallest(t)` is a binary-lifting descent on the tree:
#   walk the bits high -> low, greedily taking a block while the running count
#   stays BELOW t; whatever is missing is supplied by the next value.
#
#   the answer is then
#     ( sum_of(m - k smallest) - sum_of(k smallest) ) // (m - 2k)
#   i.e. drop the k smallest and the k largest without ever sorting.
#
#   NOTE : duplicates are handled naturally -- the descent may take only PART
#          of a value's copies, which is exactly what the last term does.
#
# time = O(log M) per addElement / calculateMKAverage, M = 10^5
# space = O(M + m)
from collections import deque
class MKAverage(object):

    def __init__(self, m, k):
        self.m = m
        self.k = k
        self.q = deque()
        self.M = 100000
        self.cnt = [0] * (self.M + 1)   # fenwick : how many values
        self.tot = [0] * (self.M + 1)   # fenwick : sum of those values
        self.LOG = 1
        while self.LOG * 2 <= self.M:
            self.LOG *= 2

    def _update(self, v, d):
        i = v
        while i <= self.M:
            self.cnt[i] += d
            self.tot[i] += d * v
            i += i & (-i)

    def _sum_of_smallest(self, t):
        # sum of the t smallest live values
        if t <= 0:
            return 0
        idx, c, s = 0, 0, 0
        pw = self.LOG
        while pw:
            nxt = idx + pw
            if nxt <= self.M and c + self.cnt[nxt] < t:
                idx = nxt
                c += self.cnt[nxt]
                s += self.tot[nxt]
            pw //= 2
        # value idx + 1 supplies the remaining (t - c) copies
        return s + (t - c) * (idx + 1)

    def addElement(self, num):
        self.q.append(num)
        self._update(num, 1)
        if len(self.q) > self.m:
            old = self.q.popleft()
            self._update(old, -1)

    def calculateMKAverage(self):
        if len(self.q) < self.m:
            return -1
        m, k = self.m, self.k
        middle = self._sum_of_smallest(m - k) - self._sum_of_smallest(k)
        return middle // (m - 2 * k)


# Your MKAverage object will be instantiated and called as such:
# obj = MKAverage(m, k)
# obj.addElement(num)
# param_2 = obj.calculateMKAverage()
