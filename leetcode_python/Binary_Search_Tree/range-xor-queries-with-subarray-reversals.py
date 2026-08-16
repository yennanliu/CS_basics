"""

3526. Range XOR Queries with Subarray Reversals
Hard

You are given an integer array nums of length n and a 2D integer array queries
of length q, where each query is one of the following three types:

Update: queries[i] = [1, index, value]
Set nums[index] = value.

Range XOR Query: queries[i] = [2, left, right]
Compute the bitwise XOR of all elements in the subarray nums[left...right], and
record this result.

Reverse Subarray: queries[i] = [3, left, right]
Reverse the subarray nums[left...right] in place.

Return an array of the results of all range XOR queries in the order they were
encountered.

Example 1:

Input: nums = [1,2,3,4,5], queries = [[2,1,3],[1,2,10],[3,0,4],[2,0,4]]

Output: [5,8]

Explanation:

Query 1: [2, 1, 3] – Compute XOR of subarray [2, 3, 4] resulting in 5.

Query 2: [1, 2, 10] – Update nums[2] to 10, updating the array to [1, 2, 10, 4,
5].

Query 3: [3, 0, 4] – Reverse the entire array to get [5, 4, 10, 2, 1].

Query 4: [2, 0, 4] – Compute XOR of subarray [5, 4, 10, 2, 1] resulting in 8.

Example 2:

Input: nums = [7,8,9], queries = [[1,0,3],[2,0,2],[3,1,2]]

Output: [2]

Explanation:

Query 1: [1, 0, 3] – Update nums[0] to 3, updating the array to [3, 8, 9].

Query 2: [2, 0, 2] – Compute XOR of subarray [3, 8, 9] resulting in 2.

Query 3: [3, 1, 2] – Reverse the subarray [8, 9] to get [9, 8].

Constraints:

1 <= nums.length <= 10^5

0 <= nums[i] <= 10^9

1 <= queries.length <= 10^5

queries[i].length == 3

queries[i][0] ∈ {1, 2, 3}

If queries[i][0] == 1:

0 <= index < nums.length

0 <= value <= 10^9

If queries[i][0] == 2 or queries[i][0] == 3:

0 <= left <= right < nums.length

"""

# V0
# IDEA : IMPLICIT TREAP (BALANCED BST KEYED BY POSITION) WITH A LAZY REVERSE FLAG
#
#   a Fenwick or segment tree cannot survive query type 3: reversing a range
#   permutes positions, and every later query addresses the *new* positions, so
#   the array itself has to be reorderable.  the structure that supports "cut a
#   positional range out and paste it back" is a balanced BST keyed by subtree
#   size rather than by value -- an implicit treap.
#
#   split(t, i) peels off the first i elements, merge(a, b) concatenates two
#   sequences; with those two primitives all three query types are the same
#   three lines -- split out [left, right], act on the middle piece, merge
#   everything back.
#
#   reversal is never performed for real, since flipping a subtree costs
#   O(size).  the middle piece only gets a `rev` flag; push() swaps a node's
#   children and hands the flag down at the moment that node is first visited.
#   xor is order-independent, so a pushed flag never invalidates the stored
#   aggregate -- that is exactly what makes lazy reversal cheap here.
#
#   the tree is built once, bottom-up, and sifted so the random priorities obey
#   the heap order; that keeps the expected depth at O(log n) afterwards.
#
# time = O(n + q * log n) expected, space = O(n)
import random


class TreapNode(object):
    __slots__ = ("val", "pri", "size", "xor", "rev", "left", "right")

    def __init__(self, val):
        self.val = val
        self.pri = random.random()
        self.size = 1
        self.xor = val
        self.rev = False
        self.left = None
        self.right = None


class Solution(object):
    def getResults(self, nums, queries):
        def build(lo, hi):
            if lo >= hi:
                return None
            mid = (lo + hi) >> 1
            t = TreapNode(nums[mid])
            t.left = build(lo, mid)
            t.right = build(mid + 1, hi)
            cur = t                       # sift the priority down into a heap
            while True:
                top = cur
                if cur.left and cur.left.pri > top.pri:
                    top = cur.left
                if cur.right and cur.right.pri > top.pri:
                    top = cur.right
                if top is cur:
                    break
                cur.pri, top.pri = top.pri, cur.pri
                cur = top
            s = 1
            x = t.val
            if t.left:
                s += t.left.size
                x ^= t.left.xor
            if t.right:
                s += t.right.size
                x ^= t.right.xor
            t.size = s
            t.xor = x
            return t

        def merge(a, b):
            if a is None:
                return b
            if b is None:
                return a
            if a.pri > b.pri:
                if a.rev:
                    a.rev = False
                    a.left, a.right = a.right, a.left
                    if a.left:
                        a.left.rev = not a.left.rev
                    if a.right:
                        a.right.rev = not a.right.rev
                r = merge(a.right, b)
                a.right = r
                a.size = 1 + (a.left.size if a.left else 0) + r.size
                a.xor = a.val ^ (a.left.xor if a.left else 0) ^ r.xor
                return a
            if b.rev:
                b.rev = False
                b.left, b.right = b.right, b.left
                if b.left:
                    b.left.rev = not b.left.rev
                if b.right:
                    b.right.rev = not b.right.rev
            l = merge(a, b.left)
            b.left = l
            b.size = 1 + l.size + (b.right.size if b.right else 0)
            b.xor = b.val ^ l.xor ^ (b.right.xor if b.right else 0)
            return b

        def split(t, cnt):
            # the first `cnt` elements go to the left result
            if t is None:
                return None, None
            if t.rev:
                t.rev = False
                t.left, t.right = t.right, t.left
                if t.left:
                    t.left.rev = not t.left.rev
                if t.right:
                    t.right.rev = not t.right.rev
            ls = t.left.size if t.left else 0
            if cnt <= ls:
                a, b = split(t.left, cnt)
                t.left = b
                t.size = 1 + (b.size if b else 0) + (t.right.size if t.right else 0)
                t.xor = t.val ^ (b.xor if b else 0) ^ (t.right.xor if t.right else 0)
                return a, t
            a, b = split(t.right, cnt - ls - 1)
            t.right = a
            t.size = 1 + ls + (a.size if a else 0)
            t.xor = t.val ^ (t.left.xor if t.left else 0) ^ (a.xor if a else 0)
            return t, b

        root = build(0, len(nums))
        res = []
        for q in queries:
            kind = q[0]
            if kind == 1:
                a, rest = split(root, q[1])
                mid, b = split(rest, 1)
                mid.val = q[2]
                mid.xor = q[2]
                root = merge(merge(a, mid), b)
            else:
                lo, hi = q[1], q[2]
                a, rest = split(root, lo)
                mid, b = split(rest, hi - lo + 1)
                if kind == 2:
                    res.append(mid.xor)
                else:
                    mid.rev = not mid.rev
                root = merge(merge(a, mid), b)
        return res
