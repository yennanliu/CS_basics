"""

1539. Kth Missing Positive Number
Easy

Given an array arr of positive integers sorted in a strictly increasing order, and an integer k.

Return the kth positive integer that is missing from this array.


Example 1:

Input: arr = [2,3,4,7,11], k = 5
Output: 9
Explanation: The missing positive integers are [1,5,6,8,9,10,12,13,...]. The 5th missing positive integer is 9.

Example 2:

Input: arr = [1,2,3,4], k = 2
Output: 6
Explanation: The missing positive integers are [5,6,7,...]. The 2nd missing positive integer is 6.


Constraints:

1 <= arr.length <= 1000
1 <= arr[i] <= 1000
1 <= k <= 1000
arr[i] < arr[j] for 1 <= i < j <= arr.length


Follow up:

Could you solve this problem in less than O(n) complexity?

"""

# V0
# IDEA : BINARY SEARCH ON "HOW MANY ARE MISSING BEFORE INDEX i"
#
#   missing(i) = arr[i] - (i + 1)
#     -> how many positive integers are absent strictly before arr[i],
#        because a gap-free prefix would read 1, 2, ..., i+1.
#
#   missing() is non-decreasing, so binary search for the FIRST index whose
#   missing count reaches k. call that index lo (possibly len(arr)).
#
#   then exactly lo array elements sit below the answer, so the answer is
#   simply lo + k.
#
#   NOTE : lo == len(arr) handles "k-th missing lies past the last element"
#          without any special casing.
#
# time = O(log n), space = O(1)
class Solution(object):
    def findKthPositive(self, arr, k):
        lo, hi = 0, len(arr)
        while lo < hi:
            mid = (lo + hi) // 2
            if arr[mid] - mid - 1 >= k:
                hi = mid
            else:
                lo = mid + 1
        return lo + k
