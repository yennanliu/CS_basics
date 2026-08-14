"""

1206. Design Skiplist
Hard

Design a Skiplist without using any built-in libraries.

A skiplist is a data structure that takes O(log(n)) time to add, erase and search.
Comparing with treap and red-black tree which has the same function and performance,
the code length of Skiplist can be comparatively short and the idea behind Skiplists
is just simple linked lists.

For example, we have a Skiplist containing [30,40,50,60,70,90] and we want to add 80 and 45 into it.

You can see there are many layers in the Skiplist. Each layer is a sorted linked list.
With the help of the top layers, add, erase and search can be faster than O(n).
It can be proven that the average time complexity for each operation is O(log(n))
and space complexity is O(n).

See more about Skiplist: https://en.wikipedia.org/wiki/Skip_list

Implement the Skiplist class:

Skiplist() Initializes the object of the skiplist.
bool search(int target) Returns true if the integer target exists in the Skiplist or false otherwise.
void add(int num) Inserts the value num into the SkipList.
bool erase(int num) Removes the value num from the Skiplist and returns true. If num does not exist
in the Skiplist, do nothing and return false. If there exist multiple num values, removing any one
of them is fine.

Note that duplicates may exist in the Skiplist, your code needs to handle this situation.


Example 1:

Input
["Skiplist", "add", "add", "add", "search", "add", "search", "erase", "erase", "search"]
[[], [1], [2], [3], [0], [4], [1], [0], [1], [1]]
Output
[null, null, null, null, false, null, true, false, true, false]

Explanation
Skiplist skiplist = new Skiplist();
skiplist.add(1);
skiplist.add(2);
skiplist.add(3);
skiplist.search(0); // return False
skiplist.add(4);
skiplist.search(1); // return True
skiplist.erase(0);  // return False, 0 is not in skiplist.
skiplist.erase(1);  // return True
skiplist.search(1); // return False, 1 has already been erased.


Constraints:

0 <= num, target <= 2 * 10^4
At most 5 * 10^4 calls will be made to search, add, and erase.

"""

# V0
# IDEA : SKIP LIST (stack of sorted linked lists, level chosen by coin flips)
#
#   every node carries a `next` array : next[i] is its successor on level i.
#   a node is promoted to level i+1 with probability p = 0.25, so the expected
#   number of nodes on level i is n * p^i -> O(log n) levels and O(log n) hops.
#
#   all 3 operations share one primitive : starting from the sentinel head at
#   the TOP level, walk right while the next value is < target, then drop one
#   level. That walk collects, per level, the last node before the target.
#
#   NOTE : duplicates are allowed, so `erase` unlinks only the FIRST matching
#          node per level and the level walk must keep the `prev` pointers.
#   NOTE : the head sentinel holds a value smaller than any legal input (-1).
#
# time = O(log n) expected per op, space = O(n)
import random
class Skiplist(object):

    MAX_LEVEL = 16
    P = 0.25

    def __init__(self):
        self.head = self._make_node(-1, self.MAX_LEVEL)
        self.level = 1

    def _make_node(self, val, level):
        return {'val': val, 'next': [None] * level}

    def _random_level(self):
        lvl = 1
        while lvl < self.MAX_LEVEL and random.random() < self.P:
            lvl += 1
        return lvl

    def _find_prevs(self, target):
        # prevs[i] = rightmost node on level i whose value is < target
        prevs = [self.head] * self.MAX_LEVEL
        cur = self.head
        for i in range(self.level - 1, -1, -1):
            while cur['next'][i] and cur['next'][i]['val'] < target:
                cur = cur['next'][i]
            prevs[i] = cur
        return prevs

    def search(self, target):
        prevs = self._find_prevs(target)
        nxt = prevs[0]['next'][0]
        return nxt is not None and nxt['val'] == target

    def add(self, num):
        prevs = self._find_prevs(num)
        lvl = self._random_level()
        if lvl > self.level:
            self.level = lvl
        node = self._make_node(num, lvl)
        for i in range(lvl):
            node['next'][i] = prevs[i]['next'][i]
            prevs[i]['next'][i] = node

    def erase(self, num):
        prevs = self._find_prevs(num)
        target = prevs[0]['next'][0]
        if target is None or target['val'] != num:
            return False
        for i in range(len(target['next'])):
            if prevs[i]['next'][i] is target:
                prevs[i]['next'][i] = target['next'][i]
        while self.level > 1 and self.head['next'][self.level - 1] is None:
            self.level -= 1
        return True


# Your Skiplist object will be instantiated and called as such:
# obj = Skiplist()
# param_1 = obj.search(target)
# obj.add(num)
# param_3 = obj.erase(num)
