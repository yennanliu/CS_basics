"""

2349. Design a Number Container System
Medium

Design a number container system that can do the following:

Insert or Replace a number at the given index in the system.
Return the smallest index for the given number in the system.

Implement the NumberContainers class:

NumberContainers() Initializes the number container system.
void change(int index, int number) Fills the container at index with the number. If there is already a number at that index, replace it.
int find(int number) Returns the smallest index for the given number, or -1 if there is no index that is filled by number in the system.


Example 1:

Input
["NumberContainers", "find", "change", "change", "change", "change", "find", "change", "find"]
[[], [10], [2, 10], [1, 10], [3, 10], [5, 10], [10], [1, 20], [10]]
Output
[null, -1, null, null, null, null, 1, null, 2]

Explanation
NumberContainers nc = new NumberContainers();
nc.find(10); // There is no index that is filled with number 10. Therefore, we return -1.
nc.change(2, 10); // Your container at index 2 will be filled with number 10.
nc.change(1, 10); // Your container at index 1 will be filled with number 10.
nc.change(3, 10); // Your container at index 3 will be filled with number 10.
nc.change(5, 10); // Your container at index 5 will be filled with number 10.
nc.find(10); // Number 10 is at the indices 1, 2, 3, and 5. Since the smallest index that is filled with 10 is 1, we return 1.
nc.change(1, 20); // Your container at index 1 will be filled with number 20. Note that index 1 was filled with 10 and then replaced with 20.
nc.find(10); // Number 10 is at the indices 2, 3, and 5. The smallest index that is filled with 10 is 2. Therefore, we return 2.


Constraints:

1 <= index, number <= 10^9
At most 10^5 calls will be made in total to change and find.

"""

# V0
# IDEA : HASH MAP + MIN HEAP WITH LAZY DELETION (avoid an ordered set)
#
#   d[index]  = the number currently stored at index
#   g[number] = a min-heap of every index that was EVER assigned this number
#
#   change() just overwrites d[index] and pushes index onto g[number]; we never
#   remove from the heap eagerly (that would cost O(n)).
#   find(number) pops from the top of g[number] while the top index is stale,
#   i.e. d[top] != number. The first surviving top is the answer.
#
#   NOTE : each index is pushed once per change call, so total pushes <= #calls
#          and each push is popped at most once -> amortized O(log n) per op.
#
# time = O(log n) amortized per change / find, space = O(n)
import heapq
from collections import defaultdict
class NumberContainers(object):

    def __init__(self):
        self.d = {}                      # index -> number
        self.g = defaultdict(list)       # number -> min-heap of candidate indices

    def change(self, index, number):
        self.d[index] = number
        heapq.heappush(self.g[number], index)

    def find(self, number):
        h = self.g[number]
        while h and self.d.get(h[0]) != number:
            heapq.heappop(h)
        return h[0] if h else -1


# Your NumberContainers object will be instantiated and called as such:
# obj = NumberContainers()
# obj.change(index,number)
# param_2 = obj.find(number)
