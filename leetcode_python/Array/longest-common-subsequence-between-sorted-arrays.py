"""

1940. Longest Common Subsequence Between Sorted Arrays
Medium

Given an array of integer arrays arrays where each arrays[i] is sorted in strictly increasing order, return an integer array representing the longest common subsequence among all the arrays.

A subsequence is a sequence that can be derived from another sequence by deleting some elements (possibly none) without changing the order of the remaining elements.


Example 1:

Input: arrays = [[1,3,4],
                 [1,4,7,9]]
Output: [1,4]
Explanation: The longest common subsequence in the two arrays is [1,4].

Example 2:

Input: arrays = [[2,3,6,8],
                 [1,2,3,5,6,7,10],
                 [2,3,4,6,9]]
Output: [2,3,6]
Explanation: The longest common subsequence in all three arrays is [2,3,6].

Example 3:

Input: arrays = [[1,2,3,4,5],
                 [6,7,8]]
Output: []
Explanation: There is no common subsequence between the two arrays.


Constraints:

2 <= arrays.length <= 100
1 <= arrays[i].length <= 100
1 <= arrays[i][j] <= 100
arrays[i] is sorted in strictly increasing order.

"""

# V0
# IDEA : COUNTING (every row is STRICTLY increasing -> LCS == set intersection)
#
#   because each row is sorted and has no duplicates, any set of shared values
#   already appears in the same (increasing) order in every row. so the longest
#   common subsequence is simply "the values present in ALL rows", sorted.
#
#   count how often each value shows up across the rows; a value belongs to the
#   answer exactly when its count equals len(arrays).
#
#   NOTE : values are bounded by 100, so a fixed-size counter array is enough
#          and the output comes out sorted for free.
#
# time = O(N + M), N = total elements, M = 101
# space = O(M)
class Solution(object):
    def longestCommonSubsequence(self, arrays):
        cnt = [0] * 101
        for row in arrays:
            for x in row:
                cnt[x] += 1
        need = len(arrays)
        return [x for x in range(101) if cnt[x] == need]
