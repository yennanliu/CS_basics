# https://leetcode.com/problems/transform-binary-string-using-subsequence-sort/description/

"""

3998. Transform Binary String Using Subsequence Sort
Solved
Medium
premium lock icon
Companies
Hint
You are given a binary string s.

You are also given an array of strings strs, where each strs[i] has the same length as s and consists of characters '0', '1', and '?'. Each '?' can be replaced by either '0' or '1'.

You may perform the following operation any number of times (including zero):

Choose any subsequence sub of s.
Sort sub in non-decreasing order.
Replace the chosen subsequence in s with the sorted sub, keeping all other characters unchanged.
Return a boolean array ans, where ans[i] is true if it's possible to replace all '?' in strs[i] with '0' or '1' and transform s into the resulting string using the allowed operation above, otherwise return false.

 

Example 1:

Input: s = "101", strs = ["1?1","0?1","0?0"]

Output: [true,true,false]

Explanation:

i	strs[i]	Replacement	Result strs[i]	Operation(s)	Result
0	"1?1"	? → 0	"101"	Matches s.	true
1	"0?1"	? → 1	"011"	Select the subsequence at indices [0..2] of s → "101".
Sort "101" to get "011" = strs[i].	true
2	"0?0"	? → 0 or 1	"000" or "010"	Not feasible.	false
Thus, ans = [true, true, false].

Example 2:

Input: s = "1100", strs = ["0011","11?1","1?1?"]

Output: [true,false,true]

Explanation:

i	strs[i]	Replacement	Result strs[i]	Operation(s)	Result
0	"0011"	-	"0011"	Select the subsequence at indices [0..3] of s → "1100".
Sort "1100" to get "0011" = strs[i].	true
1	"11?1"	? → 0	"1101"	Not feasible.	false
2	"1?1?"	First ? → 0
Second ? → 0	"1010"	Select the subsequence at indices [1, 2] of s → "10".
Sort "10" to get "01", so s = "1010".	true
Thus, ans = [true, false, true].

Example 3:

Input: s = "1010", strs = ["0011"]

Output: [true]

Explanation:

i	strs[i]	Replacement	Result strs[i]	Operation(s)	Result
0	"0011"	-	"0011"	Select the subsequence at indices [0, 2, 3] of s → "110".
Sort "110" to get "011", so s = "0011" = strs[i].	true
Thus, ans = [true].

 

Constraints:

1 <= n == s.length <= 2000
s[i] is either '0' or '1'.
1 <= strs.length <= 2000
strs[i].length == n
strs[i] is either '0', '1', or '?'​​​​​​​.
 


"""


# V0
class Solution(object):
    def transformStr(self, s, strs):
        """
        :type s: str
        :type strs: List[str]
        :rtype: List[bool]
        """
        pass


# V0-1
# IDEA: PREFIX + GREEDY (gemini)
class Solution(object):
    def transformStr(self, s, strs):
        """
        :type s: str
        :type strs: List[str]
        :rtype: List[bool]
        """
        ans = []
        ones_s = s.count('1')
        
        # Precompute prefix sums of '1's in 's' for O(1) lookups
        pref_s = [0] * len(s)
        curr = 0
        for i, char in enumerate(s):
            if char == '1':
                curr += 1
            pref_s[i] = curr
            
        for x in strs:
            ones_x = x.count('1')
            q_x = x.count('?')
            
            # 1. If we can't exactly match the total number of '1's, it's impossible
            if ones_s < ones_x or ones_s > ones_x + q_x:
                ans.append(False)
                continue
                
            ones_needed = ones_s - ones_x
            
            # 2. We will greedily fill the first '?'s with '0's, and the rest with '1's
            zeros_for_q = q_x - ones_needed
            
            curr_ones_x = 0
            q_seen = 0
            possible = True
            
            for i, char in enumerate(x):
                if char == '1':
                    curr_ones_x += 1
                elif char == '?':
                    q_seen += 1
                    # Once we've used our quota of '0's, we must place '1's
                    if q_seen > zeros_for_q:
                        curr_ones_x += 1
                        
                # 3. If at any point the target string has MORE '1's than 's', it's invalid
                if pref_s[i] < curr_ones_x:
                    possible = False
                    break
                    
            ans.append(possible)
            
        return ans


# V1