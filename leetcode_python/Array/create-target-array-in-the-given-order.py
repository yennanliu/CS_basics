"""

1389. Create Target Array in the Given Order
Easy

Given two arrays of integers nums and index. Your task is to create target array under the
following rules:

Initially target array is empty.
From left to right read nums[i] and index[i], insert at index index[i] the value nums[i]
in target array.
Repeat the previous step until there are no elements to read in nums and index.

Return the target array.

It is guaranteed that the insertion operations will be valid.


Example 1:

Input: nums = [0,1,2,3,4], index = [0,1,2,2,1]
Output: [0,4,1,3,2]
Explanation:
nums       index     target
0            0        [0]
1            1        [0,1]
2            2        [0,1,2]
3            2        [0,1,3,2]
4            1        [0,4,1,3,2]

Example 2:

Input: nums = [1,2,3,4,0], index = [0,1,2,3,0]
Output: [0,1,2,3,4]
Explanation:
nums       index     target
1            0        [1]
2            1        [1,2]
3            2        [1,2,3]
4            3        [1,2,3,4]
0            0        [0,1,2,3,4]

Example 3:

Input: nums = [1], index = [0]
Output: [1]


Constraints:

1 <= nums.length, index.length <= 100
nums.length == index.length
0 <= nums[i] <= 100
0 <= index[i] <= i

"""

# V0
# IDEA : DIRECT SIMULATION (list.insert already does the shifting)
#
#   read the pairs left to right and insert nums[i] at position index[i].
#   list.insert shifts everything from that position one slot right, which is
#   exactly the rule described.
#
#   NOTE : the constraint index[i] <= i guarantees the position always exists,
#          so no bounds handling is needed.
#   NOTE : n <= 100, so the O(n) cost of each insert is irrelevant here.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def createTargetArray(self, nums, index):
        target = []
        for i in range(len(nums)):
            target.insert(index[i], nums[i])
        return target


# V0-1
# IDEA : REPLAY THE INSERTIONS BACKWARDS (free-slot picking)
#
#   the pair read LAST is never shifted again, so index[n-1] is already the
#   FINAL position of nums[n-1]. drop that slot and the still-free slots,
#   in increasing order, are exactly the array as it looked one step earlier
#   -> index[i] then means "the index[i]-th still-free slot".
#
#   so : walk i from n-1 down to 0 and pop the index[i]-th free position.
#        no element is ever moved, each one is written straight to its
#        final home.
#
# time = O(n^2)     (list.pop from the middle is O(n))
# space = O(n)
class Solution(object):
    def createTargetArray(self, nums, index):
        n = len(nums)
        free = list(range(n))
        res = [0] * n
        for i in range(n - 1, -1, -1):
            pos = free.pop(index[i])
            res[pos] = nums[i]
        return res


# V0-2
# IDEA : BACKWARDS REPLAY + BINARY INDEXED TREE  (O(n log n))
#
#   same "walk the pairs backwards" observation as V0-1, but the query
#   "give me the k-th still-free slot" is served by a Fenwick tree that
#   stores 1 for every free position. binary lifting down the tree finds
#   the k-th one in O(log n), and marking it used is another O(log n),
#   which removes the O(n) list.pop of V0-1.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def createTargetArray(self, nums, index):
        n = len(nums)
        tree = [0] * (n + 1)

        def add(i, delta):
            while i <= n:
                tree[i] += delta
                i += i & (-i)

        def kth_free(k):
            # smallest 1-based pos whose prefix sum reaches k
            pos, step = 0, 1
            while step * 2 <= n:
                step *= 2
            while step:
                if pos + step <= n and tree[pos + step] < k:
                    pos += step
                    k -= tree[pos]
                step //= 2
            return pos + 1

        for i in range(1, n + 1):
            add(i, 1)

        res = [0] * n
        for i in range(n - 1, -1, -1):
            pos = kth_free(index[i] + 1)
            res[pos - 1] = nums[i]
            add(pos, -1)
        return res
