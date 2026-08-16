"""

3672. Sum of Weighted Modes in Subarrays
Medium

You are given an integer array nums and an integer k.

For every subarray of length k:

The mode is defined as the element with the highest frequency. If there are
multiple choices for a mode, the smallest such element is taken.
The weight is defined as mode * frequency(mode).

Return the sum of the weights of all subarrays of length k.

Note:

A subarray is a contiguous non-empty sequence of elements within an array.
The frequency of an element x is the number of times it occurs in the
array.


Example 1:

Input: nums = [1,2,2,3], k = 3
Output: 8
Explanation:
Subarrays of length k = 3 are:

Subarray    Frequencies   Mode   Mode Frequency   Weight
[1, 2, 2]   1: 1, 2: 2    2      2                2 * 2 = 4
[2, 2, 3]   2: 2, 3: 1    2      2                2 * 2 = 4

Thus, the sum of weights is 4 + 4 = 8.

Example 2:

Input: nums = [1,2,1,2], k = 2
Output: 3
Explanation:
Subarrays of length k = 2 are:

Subarray   Frequencies  Mode   Mode Frequency   Weight
[1, 2]     1: 1, 2: 1   1      1                1 * 1 = 1
[2, 1]     2: 1, 1: 1   1      1                1 * 1 = 1
[1, 2]     1: 1, 2: 1   1      1                1 * 1 = 1

Thus, the sum of weights is 1 + 1 + 1 = 3.

Example 3:

Input: nums = [4,3,4,3], k = 3
Output: 14
Explanation:
Subarrays of length k = 3 are:

Subarray    Frequencies   Mode   Mode Frequency   Weight
[4, 3, 4]   4: 2, 3: 1    4      2                2 * 4 = 8
[3, 4, 3]   3: 2, 4: 1    3      2                2 * 3 = 6

Thus, the sum of weights is 8 + 6 = 14.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5
1 <= k <= nums.length

"""

# V0
# IDEA : SLIDING WINDOW + BUCKETS-BY-FREQUENCY WITH LAZY HEAPS
#
#   the frequency table itself slides in O(1) per step, so the whole
#   difficulty is the query: the SMALLEST value attaining the current
#   maximum frequency. plain max-tracking dies on removal, because when the
#   leader loses a count we have no idea who the new smallest leader is.
#
#   so invert the table: keep one bucket per frequency, holding the values
#   currently at that frequency, as a min-heap. maintaining exact bucket
#   membership would need deletions, which heaps do not offer -- but LAZY
#   deletion works if we push a value into bucket[c] every single time its
#   count becomes c (on the way up and on the way down alike). then a value
#   whose count is c right now provably still has a live entry in
#   bucket[c], so popping entries whose count no longer matches can never
#   discard something we still need, and any value that later returns to c
#   simply gets pushed again.
#
#   the maximum frequency itself is easy in both directions: adding can only
#   raise it by one, and removing can only lower it by one, so after a
#   removal it is enough to clean the top bucket and step down if it emptied.
#
#   each array element causes at most two pushes overall, so the heaps stay
#   O(n) in total size.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def modeWeight(self, nums, k):
        from heapq import heappush, heappop

        n = len(nums)
        cnt = {}
        buckets = {}

        def bump(v, c):
            b = buckets.get(c)
            if b is None:
                b = buckets[c] = []
            heappush(b, v)

        top = 0
        total = 0
        for i in range(n):
            x = nums[i]
            c = cnt.get(x, 0) + 1
            cnt[x] = c
            bump(x, c)
            if c > top:
                top = c

            if i >= k:
                y = nums[i - k]
                c = cnt[y] - 1
                cnt[y] = c
                if c:
                    bump(y, c)
                b = buckets.get(top)
                while b and cnt.get(b[0], 0) != top:
                    heappop(b)
                if not b:
                    top -= 1

            if i >= k - 1:
                b = buckets[top]
                while cnt.get(b[0], 0) != top:
                    heappop(b)
                total += b[0] * top
        return total
