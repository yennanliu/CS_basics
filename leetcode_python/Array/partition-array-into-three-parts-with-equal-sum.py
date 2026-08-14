"""

1013. Partition Array Into Three Parts With Equal Sum
Easy

Given an array of integers arr, return true if we can partition the array into three non-empty parts with equal sums.

Formally, we can partition the array if we can find indexes i + 1 < j with (arr[0] + arr[1] + ... + arr[i] == arr[i + 1] + arr[i + 2] + ... + arr[j - 1] == arr[j] + arr[j + 1] + ... + arr[arr.length - 1])


Example 1:

Input: arr = [0,2,1,-6,6,-7,9,1,2,0,1]
Output: true
Explanation: 0 + 2 + 1 = -6 + 6 - 7 + 9 + 1 = 2 + 0 + 1

Example 2:

Input: arr = [0,2,1,-6,6,7,9,-1,2,0,1]
Output: false

Example 3:

Input: arr = [3,3,6,5,-2,2,5,1,-9,4]
Output: true
Explanation: 3 + 3 = 6 = 5 - 2 + 2 + 5 + 1 - 9 + 4


Constraints:

3 <= arr.length <= 5 * 10^4
-10^4 <= arr[i] <= 10^4

"""

# V0
# IDEA : PREFIX SUM + GREEDY
#
#   total must be divisible by 3 -> target = total / 3
#   scan once, accumulate; every time the running sum hits target,
#   close that part and reset the running sum.
#
#   if we close >= 3 parts, the answer is True :
#     - each closed part is non-empty (we only close right after adding an element)
#     - any elements left over after the 3rd part sum to 0,
#       so they can just be glued onto the last part
#
#   NOTE : cnt >= 3 (not == 3) matters for the all-zero case, e.g. [0,0,0,0]
#
# time = O(n)
# space = O(1)
class Solution(object):
    def canThreePartsEqualSum(self, arr):
        total = sum(arr)
        if total % 3 != 0:
            return False

        target = total // 3
        cur = 0
        cnt = 0
        for v in arr:
            cur += v
            if cur == target:
                cnt += 1
                cur = 0
        return cnt >= 3
