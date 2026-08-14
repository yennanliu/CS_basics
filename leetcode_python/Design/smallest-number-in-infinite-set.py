"""

2336. Smallest Number in Infinite Set
Medium

You have a set which contains all positive integers [1, 2, 3, 4, 5, ...].

Implement the SmallestInfiniteSet class:

SmallestInfiniteSet() Initializes the SmallestInfiniteSet object to contain all positive integers.
int popSmallest() Removes and returns the smallest integer contained in the infinite set.
void addBack(int num) Adds a positive integer num back into the infinite set, if it is not already in the infinite set.


Example 1:

Input
["SmallestInfiniteSet", "addBack", "popSmallest", "popSmallest", "popSmallest", "addBack", "popSmallest", "popSmallest", "popSmallest"]
[[], [2], [], [], [], [1], [], [], []]
Output
[null, null, 1, 2, 3, null, 1, 4, 5]

Explanation
SmallestInfiniteSet smallestInfiniteSet = new SmallestInfiniteSet();
smallestInfiniteSet.addBack(2);    // 2 is already in the set, so no change is made.
smallestInfiniteSet.popSmallest(); // return 1, since 1 is the smallest number, and remove it from the set.
smallestInfiniteSet.popSmallest(); // return 2, and remove it from the set.
smallestInfiniteSet.popSmallest(); // return 3, and remove it from the set.
smallestInfiniteSet.addBack(1);    // 1 is added back to the set.
smallestInfiniteSet.popSmallest(); // return 1, since 1 was added back to the set and
                                   // is the smallest number, and remove it from the set.
smallestInfiniteSet.popSmallest(); // return 4, and remove it from the set.
smallestInfiniteSet.popSmallest(); // return 5, and remove it from the set.


Constraints:

1 <= num <= 1000
At most 1000 calls will be made in total to popSmallest and addBack.

"""

# V0
# IDEA : A FRONTIER COUNTER FOR THE UNTOUCHED TAIL + A MIN-HEAP OF RETURNS
#
#   the set is always "everything from `frontier` upward, plus whatever has
#   been added back". so :
#       frontier = the smallest number never yet popped
#       heap     = the re-added numbers, all strictly below frontier
#
#   popSmallest : if the heap has anything it holds the true minimum, so pop
#                 from there; otherwise hand out `frontier` and advance it.
#   addBack     : only meaningful for a number below the frontier that is not
#                 already back — the `in_heap` set keeps it idempotent.
#
# time = O(log n) per call, space = O(n)
import heapq


class SmallestInfiniteSet(object):

    def __init__(self):
        self.frontier = 1        # smallest never-popped number
        self.heap = []           # re-added numbers, all < frontier
        self.in_heap = set()

    def popSmallest(self):
        if self.heap:
            num = heapq.heappop(self.heap)
            self.in_heap.discard(num)
            return num
        num = self.frontier
        self.frontier += 1
        return num

    def addBack(self, num):
        if num < self.frontier and num not in self.in_heap:
            heapq.heappush(self.heap, num)
            self.in_heap.add(num)


# Your SmallestInfiniteSet object will be instantiated and called as such:
# obj = SmallestInfiniteSet()
# param_1 = obj.popSmallest()
# obj.addBack(num)
