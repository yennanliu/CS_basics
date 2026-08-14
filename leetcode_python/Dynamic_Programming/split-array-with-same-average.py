"""

805. Split Array With Same Average
Hard

You are given an integer array nums.

You should move each element of nums into one of the two arrays A and B such that
A and B are non-empty, and average(A) == average(B).

Return true if it is possible to achieve that and false otherwise.

Note that for an array arr, average(arr) is the sum of all the elements of arr
over the length of arr.


Example 1:

Input: nums = [1,2,3,4,5,6,7,8]
Output: true
Explanation: We can split the array into [1,4,5,8] and [2,3,6,7],
and both of them have an average of 4.5.

Example 2:

Input: nums = [3,1]
Output: false


Constraints:

1 <= nums.length <= 30
0 <= nums[i] <= 10^4

"""

# V0
# IDEA : MATH REWRITE + MEET IN THE MIDDLE
#
#   Let s = sum(nums), n = len(nums). If A has sum s1 and size k, then
#
#       s1 / k == (s - s1) / (n - k)   <=>   s1 / k == s / n
#
#   So we just need SOME non-empty proper subset whose average equals the
#   global average. To avoid floats, scale every element:
#
#       a[i] = nums[i] * n - s
#
#   Now the task becomes: is there a non-empty proper subset of `a`
#   summing to 0 ?  (note sum(a) == 0, so the complement also sums to 0)
#
#   n <= 30, so 2^30 brute force is too slow -> split the array in half and
#   enumerate 2^15 subset sums on each side (meet in the middle).
#
#   The "proper subset" requirement: we must not take EVERY element.
#   If a valid A is (all of left + part of right), its complement B lives
#   entirely inside the right half and also sums to 0, so it is found by
#   the right-half scan anyway -> skipping the all-elements combination
#   never loses an answer.
#
# time  = O(2^(n/2))
# space = O(2^(n/2))
class Solution(object):
    def splitArraySameAverage(self, nums):
        n = len(nums)
        if n < 2:
            return False

        s = sum(nums)
        # scale: "same average" -> "subset sums to 0"
        a = [v * n - s for v in nums]

        mid = n // 2
        left, right = a[:mid], a[mid:]

        def subset_sums(items):
            """sums[mask] = sum of the elements selected by bitmask `mask`"""
            sums = [0]
            for v in items:
                sums += [x + v for x in sums]
            return sums

        ls = subset_sums(left)
        rs = subset_sums(right)

        seen = set()
        for mask in range(1, 1 << len(left)):
            if ls[mask] == 0:
                return True
            seen.add(ls[mask])

        full_right = (1 << len(right)) - 1
        for mask in range(1, full_right + 1):
            t = rs[mask]
            if t == 0:
                return True
            # pairing a full right half with a full left half would take
            # every element, leaving B empty -> guarded above
            if mask != full_right and -t in seen:
                return True

        return False
