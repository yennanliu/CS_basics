"""

1343. Number of Sub-arrays of Size K and Average Greater than or Equal to Threshold
Medium

Given an array of integers arr and two integers k and threshold, return the
number of sub-arrays of size k and average greater than or equal to threshold.


Example 1:

Input: arr = [2,2,2,2,5,5,5,8], k = 3, threshold = 4
Output: 3
Explanation: Sub-arrays [2,5,5],[5,5,5] and [5,5,8] have averages 4, 5 and 6
respectively. All other sub-arrays of size 3 have averages less than 4
(the threshold).

Example 2:

Input: arr = [11,13,17,23,29,31,7,5,2,3], k = 3, threshold = 5
Output: 6
Explanation: The first 6 sub-arrays of size 3 have averages greater than 5.
Note that averages are not integers.


Constraints:

1 <= arr.length <= 10^5
1 <= arr[i] <= 10^4
1 <= k <= arr.length
0 <= threshold <= 10^4

"""

# V0
# IDEA: SLIDING WINDOW (fixed size k)
#
#  NOTE:
#   - avoid float division: compare window SUM against threshold * k
#
# time = O(n)
# space = O(1)
class Solution(object):
    def numOfSubarrays(self, arr, k, threshold):
        target = threshold * k

        cur = sum(arr[:k])
        res = 1 if cur >= target else 0

        for i in range(k, len(arr)):
            # slide: add the new right element, drop the old left one
            cur += arr[i] - arr[i - k]
            if cur >= target:
                res += 1

        return res
