"""

3318. Find X-Sum of All K-Long Subarrays I
Easy

You are given an array nums of n integers and two integers k and x.

The x-sum of an array is calculated by the following procedure:

Count the occurrences of all elements in the array.
Keep only the occurrences of the top x most frequent elements. If two elements have the same number of occurrences, the element with the bigger value is considered more frequent.
Calculate the sum of the resulting array.

Note that if an array has less than x distinct elements, its x-sum is the sum of the array.

Return an integer array answer of length n - k + 1 where answer[i] is the x-sum of the subarray nums[i..i + k - 1].


Example 1:

Input: nums = [1,1,2,2,3,4,2,3], k = 6, x = 2
Output: [6,10,12]
Explanation:
For subarray [1, 1, 2, 2, 3, 4], only elements 1 and 2 will be kept in the resulting array. Hence, answer[0] = 1 + 1 + 2 + 2.
For subarray [1, 2, 2, 3, 4, 2], only elements 2 and 4 will be kept in the resulting array. Hence, answer[1] = 2 + 2 + 2 + 4. Note that 4 is kept in the array since it is bigger than 3 and 1 which occur the same number of times.
For subarray [2, 2, 3, 4, 2, 3], only elements 2 and 3 are kept in the resulting array. Hence, answer[2] = 2 + 2 + 2 + 3 + 3.

Example 2:

Input: nums = [3,8,7,8,7,5], k = 2, x = 2
Output: [11,15,15,15,12]
Explanation:
Since k == x, answer[i] is equal to the sum of the subarray nums[i..i + k - 1].


Constraints:

1 <= n == nums.length <= 50
1 <= nums[i] <= 50
1 <= x <= k <= nums.length

"""

# V0
# IDEA : n <= 50 — RECOUNT EACH WINDOW AND RANK BY (COUNT, VALUE)
#
#   the tie-break "bigger value wins" is exactly the tuple order
#   (count, value) descending, so sorting the window's counter by that key
#   and taking the first x entries gives the survivors.
#
#   each kept element contributes count * value to the sum. fewer than x
#   distinct values means everything is kept, which the slice handles on its
#   own.
#
# time = O(n * k log k), space = O(k)
from collections import Counter


class Solution(object):
    def findXSum(self, nums, k, x):
        res = []
        for i in range(len(nums) - k + 1):
            cnt = Counter(nums[i:i + k])
            ranked = sorted(cnt.items(), key=lambda p: (-p[1], -p[0]))
            res.append(sum(v * c for v, c in ranked[:x]))
        return res
