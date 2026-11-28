#---------------------------------------------------------------
# SEGMENT TREE
#---------------------------------------------------------------
#
# Scope: range queries over an array that keeps CHANGING, for any
#        operation that can be combined from two halves (sum, min,
#        max, gcd ...). See binary_indexed_tree.py for the shorter
#        structure that handles sums only, and
#        doc/cheatsheet/segment_tree.md for the LeetCode patterns.
#
# THE IDEA
#   A binary tree in which every node stores the answer for one RANGE
#   of the array. A leaf is a single element; a parent is its two
#   children combined. For sums over nums = [2, 1, 5, 3]:
#
#                    [0..3] = 11
#                   /           \
#            [0..1] = 3       [2..3] = 8
#            /      \         /      \
#        [0] = 2  [1] = 1  [2] = 5  [3] = 3
#
#   query(1, 3) never visits every element. It takes whole nodes that
#   fit inside the range and only splits the ones that straddle it:
#
#       [0..3]  straddles         -> ask both children
#       [0..1]  straddles         -> ask both children
#       [0]     outside [1..3]    -> contributes nothing
#       [1]     inside            -> take 1
#       [2..3]  inside            -> take 8, do NOT go deeper
#                                    answer: 1 + 8 = 9
#
#   update(i, value) changes one leaf and recomputes its ancestors --
#   one root-to-leaf path.
#
# STORAGE
#   Like a heap, the tree lives in a flat array (see binary_tree.py):
#   the root is node 1 and the children of node k are 2k and 2k+1.
#   4 * N slots is always enough room.
#
# Time  : build O(N), update O(log N), query O(log N)
# Space : O(N)
#
# FENWICK OR SEGMENT TREE?
#   sums only            -> binary indexed tree: a third of the code
#   min / max / anything -> segment tree: min has no "undo", so the
#                           prefix(r) - prefix(l) trick does not exist
#
# Used by: LC 307 Range Sum Query - Mutable, LC 315 Count of Smaller
#          Numbers After Self, LC 699 Falling Squares.
#
# References:
#   - https://cp-algorithms.com/data_structures/segment_tree.html


class SegmentTree:
    """Point update + range query for a combinable operation.

    `combine(a, b)` merges the answers of two adjacent ranges, and
    `identity` is the value that changes nothing when combined:

        sum : combine = a + b,     identity = 0
        min : combine = min(a, b), identity = float("inf")
        max : combine = max(a, b), identity = float("-inf")
    """

    def __init__(self, nums, combine=None, identity=0):
        if combine is None:
            combine = self._add
        self.n = len(nums)
        self.combine = combine
        self.identity = identity
        self.tree = [identity] * (4 * self.n)
        if self.n > 0:
            self._build(nums, node=1, low=0, high=self.n - 1)

    @staticmethod
    def _add(a, b):
        return a + b

    def __len__(self):
        return self.n

    # In every helper below, `node` is a position in self.tree and
    # [low..high] is the range of the ORIGINAL array that node answers for.

    def _build(self, nums, node, low, high):
        if low == high:                          # a leaf: one element
            self.tree[node] = nums[low]
            return
        mid = (low + high) // 2
        self._build(nums, 2 * node, low, mid)            # left half
        self._build(nums, 2 * node + 1, mid + 1, high)   # right half
        self.tree[node] = self.combine(self.tree[2 * node], self.tree[2 * node + 1])

    def update(self, index, value):
        """nums[index] = value, then repair every range that contains it."""
        if not 0 <= index < self.n:
            raise IndexError("index out of range: {}".format(index))
        self._update(index, value, node=1, low=0, high=self.n - 1)

    def _update(self, index, value, node, low, high):
        if low == high:                          # reached the leaf for `index`
            self.tree[node] = value
            return
        mid = (low + high) // 2
        if index <= mid:                         # the element is in the left half
            self._update(index, value, 2 * node, low, mid)
        else:
            self._update(index, value, 2 * node + 1, mid + 1, high)
        # one child changed, so this node's answer has to be recomputed
        self.tree[node] = self.combine(self.tree[2 * node], self.tree[2 * node + 1])

    def query(self, left, right):
        """Combined value of nums[left .. right], both ends INCLUDED."""
        if not 0 <= left <= right < self.n:
            raise IndexError("bad range: [{}, {}]".format(left, right))
        return self._query(left, right, node=1, low=0, high=self.n - 1)

    def _query(self, left, right, node, low, high):
        if right < low or high < left:           # no overlap: contributes nothing
            return self.identity
        if left <= low and high <= right:        # fully inside: take the whole node
            return self.tree[node]
        mid = (low + high) // 2                  # straddles: ask both halves
        left_part = self._query(left, right, 2 * node, low, mid)
        right_part = self._query(left, right, 2 * node + 1, mid + 1, high)
        return self.combine(left_part, right_part)


if __name__ == "__main__":
    #--- range sum: the tree from the header -----------------------
    sums = SegmentTree([2, 1, 5, 3])
    assert sums.tree[1] == 11                    # root: [0..3]
    assert sums.tree[2] == 3 and sums.tree[3] == 8

    assert sums.query(1, 3) == 9
    assert sums.query(0, 3) == 11
    assert sums.query(2, 2) == 5                 # a single element

    sums.update(2, 10)                           # nums[2]: 5 -> 10
    assert sums.query(1, 3) == 14
    assert sums.tree[1] == 16                    # the root was repaired too

    # LC 307's example
    lc307 = SegmentTree([1, 3, 5])
    assert lc307.query(0, 2) == 9
    lc307.update(1, 2)
    assert lc307.query(0, 2) == 8

    #--- range minimum / maximum: only combine + identity change ---
    nums = [5, 2, 8, 1, 9, 3]
    lowest = SegmentTree(nums, combine=min, identity=float("inf"))
    highest = SegmentTree(nums, combine=max, identity=float("-inf"))
    assert lowest.query(0, 5) == 1 and highest.query(0, 5) == 9
    assert lowest.query(0, 2) == 2 and highest.query(0, 2) == 8
    assert lowest.query(4, 5) == 3

    lowest.update(3, 7)                          # the old minimum (1) is gone
    assert lowest.query(0, 5) == 2

    #--- agree with brute force after 300 random changes -----------
    import random

    random.seed(7)
    values = [random.randint(-9, 9) for _ in range(37)]   # not a power of two
    total = SegmentTree(values)
    smallest = SegmentTree(values, combine=min, identity=float("inf"))
    for _ in range(300):
        index = random.randrange(len(values))
        values[index] = random.randint(-9, 9)
        total.update(index, values[index])
        smallest.update(index, values[index])
        left = random.randrange(len(values))
        right = random.randrange(left, len(values))
        assert total.query(left, right) == sum(values[left:right + 1])
        assert smallest.query(left, right) == min(values[left:right + 1])

    assert len(SegmentTree([])) == 0             # empty input builds nothing
    assert SegmentTree([42]).query(0, 0) == 42   # the root is also the leaf
    try:
        sums.query(3, 1)                         # left > right
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    print("Success.")
