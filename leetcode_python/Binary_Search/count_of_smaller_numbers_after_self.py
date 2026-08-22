"""
Given an integer array nums, return an integer array counts where counts[i] is the number of smaller elements to the right of nums[i].

 

Example 1:

Input: nums = [5,2,6,1]
Output: [2,1,1,0]
Explanation:
To the right of 5 there are 2 smaller elements (2 and 1).
To the right of 2 there is only 1 smaller element (1).
To the right of 6 there is 1 smaller element (1).
To the right of 1 there is 0 smaller element.
Example 2:

Input: nums = [-1]
Output: [0]
Example 3:

Input: nums = [-1,-1]
Output: [0,0]
 

Constraints:

1 <= nums.length <= 105
-104 <= nums[i] <= 104
"""

# V0
# IDEA : BINARY INDEXED TREE (FENWICK) OVER COMPRESSED VALUES, SCAN RIGHT -> LEFT
#
#   walk nums from the right and keep a frequency BIT of everything already
#   seen (i.e. everything to the RIGHT of the current index).  the answer for
#   nums[i] is then the prefix count of ranks strictly below rank(nums[i]).
#
#   values are compressed to ranks 1..u first so the tree stays O(u) instead
#   of O(range of nums).
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def countSmaller(self, nums):
        ranks = {v: i + 1 for i, v in enumerate(sorted(set(nums)))}
        u = len(ranks)
        tree = [0] * (u + 1)

        def add(i):
            while i <= u:
                tree[i] += 1
                i += i & (-i)

        def prefix(i):
            total = 0
            while i > 0:
                total += tree[i]
                i -= i & (-i)
            return total

        res = []
        for v in reversed(nums):
            r = ranks[v]
            res.append(prefix(r - 1))
            add(r)
        res.reverse()
        return res


# V0-1
# IDEA : MERGE SORT ON INDICES (COUNT "INVERSIONS TO THE RIGHT")
#
#   sort the INDICES by their value.  during a merge, whenever we take
#   idx[i] from the left half we have already emitted (j - mid) elements of
#   the right half, and every one of them is both larger in index and
#   smaller in value -> credit exactly that many to counts[idx[i]].
#
#   summing those credits over all merge levels gives the final answer, so
#   no extra data structure is needed.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def countSmaller(self, nums):
        n = len(nums)
        counts = [0] * n
        idx = list(range(n))

        def sort(lo, hi):
            if hi - lo <= 1:
                return
            mid = (lo + hi) // 2
            sort(lo, mid)
            sort(mid, hi)
            merged = []
            i, j = lo, mid
            while i < mid and j < hi:
                if nums[idx[j]] < nums[idx[i]]:
                    merged.append(idx[j])
                    j += 1
                else:
                    counts[idx[i]] += j - mid
                    merged.append(idx[i])
                    i += 1
            while i < mid:
                counts[idx[i]] += j - mid
                merged.append(idx[i])
                i += 1
            while j < hi:
                merged.append(idx[j])
                j += 1
            idx[lo:hi] = merged

        sort(0, n)
        return counts


# V0-2
# IDEA : SORTED LIST BUILT RIGHT -> LEFT WITH bisect + list.insert
#
#   the same "everything already seen is to my right" idea as V0, but the
#   multiset is a plain python list kept sorted : bisect_left gives the
#   number of smaller elements in O(log n), and list.insert places it.
#
#   the SEARCH is logarithmic but the INSERT shifts the list tail, so this is
#   O(n^2) in the worst case (fine up to a few thousand elements, and it is
#   the shortest correct solution to write in an interview).
#
# time = O(n^2) worst case (O(n log n) comparisons, O(n) memmove per insert)
# space = O(n)
import bisect
class Solution(object):
    def countSmaller(self, nums):
        seen = []
        res = []
        for v in reversed(nums):
            pos = bisect.bisect_left(seen, v)
            res.append(pos)
            seen.insert(pos, v)
        res.reverse()
        return res


# V1

# V2
