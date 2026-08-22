"""

3285. Find Indices of Stable Mountains
Easy

There are n mountains in a row, and each mountain has a height. You are given an integer array height where height[i] represents the height of mountain i, and an integer threshold.

A mountain is called stable if the mountain just before it (index i - 1) has a height strictly greater than threshold. Note that mountain 0 is not stable.

Return an array containing the indices of all stable mountains in any order.


Example 1:

Input: height = [1,2,3,4,5], threshold = 2
Output: [3,4]
Explanation:
Mountain 3 is stable because height[2] == 3 is greater than threshold == 2.
Mountain 4 is stable because height[3] == 4 is greater than threshold == 2.

Example 2:

Input: height = [10,1,10,1,10], threshold = 3
Output: [1,3]

Example 3:

Input: height = [10,1,10,1,10], threshold = 10
Output: []


Constraints:

2 <= n == height.length <= 100
1 <= height[i] <= 100
1 <= threshold <= 100

"""

# V0
# IDEA : A DIRECT READING — LOOK ONE STEP BACK
#
#   stability depends only on the PREVIOUS mountain, so index i qualifies iff
#   height[i-1] > threshold. index 0 has no predecessor and is excluded, so
#   the scan simply starts at 1.
#
# time = O(n), space = O(n)
class Solution(object):
    def stableMountains(self, height, threshold):
        return [i for i in range(1, len(height)) if height[i - 1] > threshold]


# V0-1
# IDEA : STREAMING ONE PASS, CARRYING THE PREVIOUS HEIGHT
#
#   never index backwards: consume the heights in order, keep the last one seen
#   in a variable, and emit the current position whenever that remembered
#   height cleared the threshold. this works over any iterable (a generator, a
#   stream) and not just over a list that supports height[i - 1].
#
#   NOTE : prev is None only on the very first element, which is how mountain 0
#          gets excluded.
#
# time = O(n)
# space = O(1) besides the output
class Solution(object):
    def stableMountains(self, height, threshold):
        res = []
        prev = None
        for i, h in enumerate(height):
            if prev is not None and prev > threshold:
                res.append(i)
            prev = h
        return res


# V0-2
# IDEA : BITMASK OF THE TALL POSITIONS, SHIFTED LEFT BY ONE
#
#   set bit i when height[i] > threshold. shifting that whole mask left by one
#   slides every tall position onto its SUCCESSOR, so the set bits of
#   (mask << 1) that are still inside [0, n) are exactly the stable indices -
#   and bit 0 can never survive the shift, which drops mountain 0 for free.
#
#   the bits are then read off with the low-bit trick low = m & -m, so the
#   whole predicate is evaluated by integer arithmetic rather than by per-index
#   comparisons.
#
# time = O(n)
# space = O(n) for the n-bit mask
class Solution(object):
    def stableMountains(self, height, threshold):
        n = len(height)
        mask = 0
        for i, h in enumerate(height):
            if h > threshold:
                mask |= 1 << i
        mask = (mask << 1) & ((1 << n) - 1)
        res = []
        while mask:
            low = mask & -mask
            res.append(low.bit_length() - 1)
            mask ^= low
        return res
