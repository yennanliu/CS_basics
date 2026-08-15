"""

3371. Identify the Largest Outlier in an Array
Medium

You are given an integer array nums. This array contains n elements, where exactly n - 2 elements are special numbers. One of the remaining two elements is the sum of these special numbers, and the other is an outlier.

An outlier is defined as a number that is neither one of the original special numbers nor the element representing the sum of those numbers.

Note that special numbers, the sum element, and the outlier must have distinct indices, but may share the same value.

Return the largest potential outlier in nums.


Example 1:

Input: nums = [2,3,5,10]
Output: 10
Explanation:
The special numbers could be 2 and 3, thus making their sum 5 and the outlier 10.

Example 2:

Input: nums = [-2,-1,-3,-6,4]
Output: 4
Explanation:
The special numbers could be -2, -1, and -3, thus making their sum -6 and the outlier 4.

Example 3:

Input: nums = [1,1,1,1,1,5,5]
Output: 5
Explanation:
The special numbers could be 1, 1, 1, 1, and 1, thus making their sum 5 and the outlier 5.


Constraints:

3 <= nums.length <= 10^5
-1000 <= nums[i] <= 1000
The input is generated such that at least one potential outlier exists.

"""

# V0
# IDEA : PICK THE SUM ELEMENT, AND THE OUTLIER FALLS OUT OF THE TOTAL
#
#   if s is the sum element and o the outlier, the specials add up to s, so
#
#       total = (specials) + s + o = 2*s + o    =>   o = total - 2*s
#
#   so try every value that could play the role of s and read off o. the two
#   must occupy DIFFERENT indices, which is why the count of o has to survive
#   removing one copy of s when they happen to be equal.
#
#   values repeat, so a Counter over the distinct values keeps it linear.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def getLargestOutlier(self, nums):
        cnt = Counter(nums)
        total = sum(nums)
        best = None
        for s in cnt:
            o = total - 2 * s
            need = 1 + (1 if o == s else 0)     # distinct indices required
            if cnt.get(o, 0) >= need:
                if best is None or o > best:
                    best = o
        return best
