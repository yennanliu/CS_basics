"""

942. DI String Match
Easy
Topics
premium lock icon
Companies
A permutation perm of n + 1 integers of all the integers in the range [0, n] can be represented as a string s of length n where:

s[i] == 'I' if perm[i] < perm[i + 1], and
s[i] == 'D' if perm[i] > perm[i + 1].
Given a string s, reconstruct the permutation perm and return it. If there are multiple valid permutations perm, return any of them.

 

Example 1:

Input: s = "IDID"
Output: [0,4,1,3,2]
Example 2:

Input: s = "III"
Output: [0,1,2,3]
Example 3:

Input: s = "DDI"
Output: [3,2,0,1]
 

Constraints:

1 <= s.length <= 105
s[i] is either 'I' or 'D'.


"""


# V0
# IDEA: 2 POINTERS (gpt)
class Solution(object):
    def diStringMatch(self, s):
        n = len(s)

        nums = [i for i in range(n + 1)]

        l = 0
        r = n

        ans = []

        for ch in s:
            if ch == "I":
                ans.append(nums[l])
                l += 1
            else:
                ans.append(nums[r])
                r -= 1

        ans.append(nums[l])

        return ans


# V0-1
# IDEA: 2 POINTERS (GEMINI)
class Solution(object):
    def diStringMatch(self, s):
        """
        :type s: str
        :rtype: List[int]
        """
        n = len(s)
        
        # Pointers representing our available range [0, n]
        l = 0
        r = n
        
        ans = []
        
        # Build the array sequentially based on directions
        for char in s:
            if char == "I":
                ans.append(l)
                l += 1
            else:  # char == "D"
                ans.append(r)
                r -= 1
                
        # Crucial step: append the remaining number left at the end
        ans.append(l)  # at this point, l == r
        
        return ans


# V0-2
# IDEA: 2 POINTERS (gpt)
class Solution(object):
    def diStringMatch(self, s):
        low = 0
        high = len(s)

        ans = []

        for c in s:
            if c == "I":
                ans.append(low)
                low += 1
            else:
                ans.append(high)
                high -= 1

        ans.append(low)
        return ans



# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/84206493
class Solution:
    def diStringMatch(self, S):
        """
        :type S: str
        :rtype: List[int]
        """
        N = len(S)
        ni, nd = 0, N
        res = []
        for s in S:
            if s == "I":
                res.append(ni)
                ni += 1
            else:
                res.append(nd)
                nd -= 1
        res.append(ni)
        return res
        
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def diStringMatch(self, S):
        """
        :type S: str
        :rtype: List[int]
        """
        result = []
        left, right = 0, len(S)
        for c in S:
            if c == 'I':
                result.append(left)
                left += 1
            else:
                result.append(right)
                right -= 1
        result.append(left)
        return result
