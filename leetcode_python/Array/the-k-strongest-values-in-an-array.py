"""

1471. The k Strongest Values in an Array
Medium

Given an array of integers arr and an integer k.

A value arr[i] is said to be stronger than a value arr[j] if |arr[i] - m| > |arr[j] - m| where m is the centre of the array.
If |arr[i] - m| == |arr[j] - m|, then arr[i] is said to be stronger than arr[j] if arr[i] > arr[j].

Return a list of the strongest k values in the array. Return the answer in any arbitrary order.

The centre is the middle value in an ordered integer list. More formally, if the length of the list is n, the centre is the element in position ((n - 1) / 2) in the sorted list (0-indexed).

For arr = [6, -3, 7, 2, 11], n = 5 and the centre is obtained by sorting the array arr = [-3, 2, 6, 7, 11] and the centre is arr[m] where m = ((5 - 1) / 2) = 2. The centre is 6.
For arr = [-7, 22, 17, 3], n = 4 and the centre is obtained by sorting the array arr = [-7, 3, 17, 22] and the centre is arr[m] where m = ((4 - 1) / 2) = 1. The centre is 3.


Example 1:

Input: arr = [1,2,3,4,5], k = 2
Output: [5,1]
Explanation: Centre is 3, the elements of the array sorted by the strongest are [5,1,4,2,3]. The strongest 2 elements are [5, 1]. [1, 5] is also accepted answer.
Please note that although |5 - 3| == |1 - 3| but 5 is stronger than 1 because 5 > 1.

Example 2:

Input: arr = [1,1,3,5,5], k = 2
Output: [5,5]
Explanation: Centre is 3, the elements of the array sorted by the strongest are [5,5,1,1,3]. The strongest 2 elements are [5, 5].

Example 3:

Input: arr = [6,7,11,7,6,8], k = 5
Output: [11,8,6,6,7]
Explanation: Centre is 7, the elements of the array sorted by the strongest are [11,8,6,6,7,7].
Any permutation of [11,8,6,6,7] is accepted.


Constraints:

1 <= arr.length <= 10^5
-10^5 <= arr[i] <= 10^5
1 <= k <= arr.length

"""

# V0
# IDEA : SORT TWICE (find the centre, then rank by the custom "strength")
#
#   sort once to read the centre m = arr[(n-1)//2].
#   sort again with key (-|x - m|, -x): larger distance first, and ties
#   broken by the larger value first, which is exactly the strength order.
#   then take the first k.
#   NOTE : the centre uses (n-1)//2 on the SORTED array - it is not the
#          average and not the upper median.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def getStrongest(self, arr, k):
        arr.sort()
        m = arr[(len(arr) - 1) // 2]
        arr.sort(key=lambda x: (-abs(x - m), -x))
        return arr[:k]
