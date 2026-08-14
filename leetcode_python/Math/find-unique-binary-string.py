"""

1980. Find Unique Binary String
Medium

Given an array of strings nums containing n unique binary strings each of length n, return a binary string of length n that does not appear in nums. If there are multiple answers, you may return any of them.


Example 1:

Input: nums = ["01","10"]
Output: "11"
Explanation: "11" does not appear in nums. "00" would also be correct.

Example 2:

Input: nums = ["00","01"]
Output: "11"
Explanation: "11" does not appear in nums. "10" would also be correct.

Example 3:

Input: nums = ["111","011","001"]
Output: "101"
Explanation: "101" does not appear in nums. "000", "010", "100", and "110" would also be correct.


Constraints:

n == nums.length
1 <= n <= 16
nums[i].length == n
nums[i] is either '0' or '1'.

"""

# V0
# IDEA : CANTOR DIAGONAL (flip the i-th bit of the i-th string)
#
#   build res so that res[i] != nums[i][i] for every i.
#   then res differs from nums[i] at position i, for EVERY i, so it cannot
#   equal any string in the list - no search / set membership needed.
#
#   NOTE : this works because the matrix is square (n strings of length n),
#          which the constraints guarantee.
#
# time = O(n), space = O(n) for the answer
class Solution(object):
    def findDifferentBinaryString(self, nums):
        res = []
        for i, s in enumerate(nums):
            res.append("1" if s[i] == "0" else "0")
        return "".join(res)
