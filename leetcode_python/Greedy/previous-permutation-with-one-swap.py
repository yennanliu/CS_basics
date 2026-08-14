"""

1053. Previous Permutation With One Swap
Medium

Given an array of positive integers arr (not necessarily distinct), return the
lexicographically largest permutation that is smaller than arr, that can be made
with exactly one swap. If it cannot be done, then return the same array.

Note that a swap exchanges the positions of two numbers arr[i] and arr[j]


Example 1:

Input: arr = [3,2,1]
Output: [3,1,2]
Explanation: Swapping 2 and 1.

Example 2:

Input: arr = [1,1,5]
Output: [1,1,5]
Explanation: This is already the smallest permutation.

Example 3:

Input: arr = [1,9,4,6,7]
Output: [1,7,4,6,9]
Explanation: Swapping 9 and 7.


Constraints:

1 <= arr.length <= 10^4
1 <= arr[i] <= 10^4

"""

# V0
# IDEA : GREEDY (mirror of "next permutation")
#
#  step 1 : scan from the right, find the RIGHTMOST i with arr[i] > arr[i+1].
#           (everything to the right of i is non-decreasing -> already the
#            smallest arrangement of that suffix, so i is the only position
#            worth shrinking, and shrinking the rightmost one loses the least)
#           no such i  ->  arr is already the smallest permutation, return arr
#
#  step 2 : in the suffix, pick the LARGEST value strictly smaller than arr[i]
#           -> scan from the right until arr[j] < arr[i]
#
#  step 3 : if that value is duplicated, take its LEFTMOST occurrence.
#           e.g. [3,1,1,3] -> swapping with index 1 gives [1,3,1,3],
#                swapping with index 2 gives [1,1,3,3] -> the leftmost wins
#
# time = O(n)
# space = O(1)
class Solution(object):
    def prevPermOpt1(self, arr):
        n = len(arr)

        # step 1
        i = n - 2
        while i >= 0 and arr[i] <= arr[i + 1]:
            i -= 1
        if i < 0:
            return arr

        # step 2 : rightmost j with arr[j] < arr[i]
        j = n - 1
        while arr[j] >= arr[i]:
            j -= 1

        # step 3 : slide left over duplicates of arr[j]
        while j - 1 > i and arr[j - 1] == arr[j]:
            j -= 1

        arr[i], arr[j] = arr[j], arr[i]
        return arr
