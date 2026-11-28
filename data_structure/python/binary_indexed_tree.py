#---------------------------------------------------------------
# BINARY INDEXED TREE (Fenwick tree)
#---------------------------------------------------------------
#
# Scope: prefix sums over an array that keeps CHANGING. See
#        segment_tree.py for the more general structure (min / max /
#        any range query), and doc/cheatsheet/binary_indexed_tree.md
#        for the LeetCode patterns.
#
# THE PROBLEM
#   Two operations on an array, mixed in any order:
#       update(i, delta)   add delta to nums[i]
#       query(l, r)        sum of nums[l..r]
#
#                         update      range sum
#     plain array         O(1)        O(N)        scan the range
#     prefix-sum array    O(N)        O(1)        rebuild after a change
#     Fenwick tree        O(log N)    O(log N)    <- balanced
#
# THE IDEA
#   tree[i] does not hold one element, and not a whole prefix either.
#   It holds the sum of a BLOCK of elements ending at i. The block's
#   length is the lowest set bit of i (1-indexed):
#
#       i   binary   lowbit   tree[i] covers (1-indexed elements)
#       1   0001     1        [1..1]
#       2   0010     2        [1..2]
#       3   0011     1        [3..3]
#       4   0100     4        [1..4]
#       5   0101     1        [5..5]
#       6   0110     2        [5..6]
#       7   0111     1        [7..7]
#       8   1000     8        [1..8]
#
#   lowbit(i) = i & -i   (two's complement: -i flips every bit above
#                         the lowest set bit, so the AND keeps only it)
#
# THE TWO WALKS -- both take at most log N steps, one per bit
#   prefix sum of the first i elements: add tree[i], then jump to the
#   block just before it:           i -= lowbit(i)
#       prefix(7) = tree[7] + tree[6] + tree[4]     7 -> 6 -> 4 -> 0
#                   [7..7]    [5..6]    [1..4]
#
#   update element i: fix every block that CONTAINS i:
#                                   i += lowbit(i)
#       update(5): tree[5], tree[6], tree[8]        5 -> 6 -> 8 -> stop
#
# The tree is 1-INDEXED inside (index 0 has no lowest set bit, so the
# walks would never move). The public methods take normal 0-based
# indexes and shift by one.
#
# Time  : build O(N), update O(log N), prefix_sum / range_sum O(log N)
# Space : O(N)
#
# Used by: LC 307 Range Sum Query - Mutable, LC 315 Count of Smaller
#          Numbers After Self, LC 493 Reverse Pairs.
#
# References:
#   - https://cp-algorithms.com/data_structures/fenwick.html


def lowbit(i):
    """The lowest set bit of i: lowbit(6) = lowbit(0b110) = 0b010 = 2."""
    return i & -i


class BinaryIndexedTree:
    """Point update + range sum, both O(log N)."""

    def __init__(self, nums):
        self.n = len(nums)
        self.nums = list(nums)               # current values, so `set` can work out a delta
        self.tree = [0] * (self.n + 1)       # 1-indexed; tree[0] is unused

        # O(N) build: put each value in its own block, then push every
        # finished block into the next larger block that contains it.
        for i in range(1, self.n + 1):
            self.tree[i] += nums[i - 1]
            parent = i + lowbit(i)
            if parent <= self.n:
                self.tree[parent] += self.tree[i]

    def __len__(self):
        return self.n

    def update(self, index, delta):
        """Add `delta` to nums[index] (0-based)."""
        if not 0 <= index < self.n:
            raise IndexError("index out of range: {}".format(index))
        self.nums[index] += delta
        i = index + 1                        # switch to 1-based
        while i <= self.n:
            self.tree[i] += delta            # this block contains the element
            i += lowbit(i)                   # jump to the next larger block that does

    def set(self, index, value):
        """nums[index] = value. A Fenwick tree only knows deltas, so compute one."""
        if not 0 <= index < self.n:
            raise IndexError("index out of range: {}".format(index))
        self.update(index, value - self.nums[index])

    def prefix_sum(self, count):
        """Sum of the FIRST `count` elements, nums[0 .. count-1]."""
        if not 0 <= count <= self.n:
            raise IndexError("count out of range: {}".format(count))
        total = 0
        i = count                            # already 1-based: the count-th element
        while i > 0:
            total += self.tree[i]            # take this whole block
            i -= lowbit(i)                   # jump to the block that ends just before it
        return total

    def range_sum(self, left, right):
        """Sum of nums[left .. right], both ends INCLUDED (0-based).

        The usual prefix trick:  sum(left..right) = prefix(right+1) - prefix(left)
        """
        if not 0 <= left <= right < self.n:
            raise IndexError("bad range: [{}, {}]".format(left, right))
        return self.prefix_sum(right + 1) - self.prefix_sum(left)


if __name__ == "__main__":
    assert [lowbit(i) for i in range(1, 9)] == [1, 2, 1, 4, 1, 2, 1, 8]

    nums = [3, 2, -1, 6, 5, 4, -3, 3]
    bit = BinaryIndexedTree(nums)

    # tree[i] holds the block from the table in the header
    assert bit.tree[4] == 3 + 2 - 1 + 6      # [1..4]
    assert bit.tree[6] == 5 + 4              # [5..6]
    assert bit.tree[7] == -3                 # [7..7]

    assert bit.prefix_sum(0) == 0            # the empty prefix
    assert bit.prefix_sum(7) == sum(nums[:7])
    assert bit.range_sum(0, 7) == sum(nums)
    assert bit.range_sum(2, 5) == -1 + 6 + 5 + 4
    assert bit.range_sum(3, 3) == 6          # a single element

    bit.update(4, 10)                        # nums[4]: 5 -> 15
    assert bit.range_sum(2, 5) == -1 + 6 + 15 + 4

    bit.set(4, 5)                            # and back, by value this time
    assert bit.range_sum(2, 5) == -1 + 6 + 5 + 4

    # LC 307's example
    lc307 = BinaryIndexedTree([1, 3, 5])
    assert lc307.range_sum(0, 2) == 9
    lc307.set(1, 2)
    assert lc307.range_sum(0, 2) == 8

    # agree with brute force after every one of 300 random changes
    import random

    random.seed(7)
    values = [random.randint(-9, 9) for _ in range(37)]
    tree = BinaryIndexedTree(values)
    for _ in range(300):
        index = random.randrange(len(values))
        delta = random.randint(-9, 9)
        values[index] += delta
        tree.update(index, delta)
        left = random.randrange(len(values))
        right = random.randrange(left, len(values))
        assert tree.range_sum(left, right) == sum(values[left:right + 1])

    assert BinaryIndexedTree([]).prefix_sum(0) == 0
    try:
        bit.range_sum(5, 2)                  # left > right
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    print("Success.")
