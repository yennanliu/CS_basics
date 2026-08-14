"""

1528. Shuffle String
Easy

You are given a string s and an integer array indices of the same length. The string s will be shuffled such that the character at the ith position moves to indices[i] in the shuffled string.

Return the shuffled string.


Example 1:

Input: s = "codeleet", indices = [4,5,6,7,0,2,1,3]
Output: "leetcode"
Explanation: As shown, "codeleet" becomes "leetcode" after shuffling.

Example 2:

Input: s = "abc", indices = [0,1,2]
Output: "abc"
Explanation: After shuffling, each character remains in its position.


Constraints:

s.length == indices.length == n
1 <= n <= 100
s consists of only lowercase English letters.
0 <= indices[i] < n
All values of indices are unique.

"""

# V0
# IDEA : SCATTER INTO A RESULT BUFFER
#
#   indices is a PERMUTATION (all values unique, 0 <= indices[i] < n), so
#   every destination slot is written exactly once — a single pass placing
#   s[i] at res[indices[i]] fills the whole buffer.
#
#   NOTE : this is a scatter (res[indices[i]] = s[i]), not a gather
#          (res[i] = s[indices[i]]) — those give different strings unless
#          the permutation happens to be its own inverse.
#
# time = O(n), space = O(n)
class Solution(object):
    def restoreString(self, s, indices):
        res = [None] * len(s)
        for i, c in enumerate(s):
            res[indices[i]] = c
        return "".join(res)
