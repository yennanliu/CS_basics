"""

696. Count Binary Substrings
Easy

Give a binary string s, return the number of non-empty substrings that have the same number of 0's and 1's, and all the 0's and all the 1's in these substrings are grouped consecutively.

Substrings that occur multiple times are counted the number of times they occur.

 

Example 1:

Input: s = "00110011"
Output: 6
Explanation: There are 6 substrings that have equal number of consecutive 1's and 0's: "0011", "01", "1100", "10", "0011", and "01".
Notice that some of these substrings repeat and are counted the number of times they occur.
Also, "00110011" is not a valid substring because all the 0's (and 1's) are not grouped together.
Example 2:

Input: s = "10101"
Output: 4
Explanation: There are 4 substrings: "10", "01", "10", "01" that have equal number of consecutive 1's and 0's.
 

Constraints:

1 <= s.length <= 105
s[i] is either '0' or '1'.
Accepted
132.8K
Submissions
206.2K

"""


# V0
# IDEA :  Group By Character + continous sub-string (gpt)
"""
Core idea:

The key observation is to count the lengths of consecutive groups.

Example:

001110011

groups:
00   -> 2
111  -> 3
00   -> 2
11   -> 2

group lengths:
[2, 3, 2, 2]

Each adjacent pair contributes:

min(2,3) = 2
min(3,2) = 2
min(2,2) = 2

Total = 6

"""
class Solution(object):
    def countBinarySubstrings(self, s):
        """
        :type s: str
        :rtype: int
        """
        if not s or len(s) <= 1:
            return 0

        g_cnt = []
        n = len(s)

        l = 0

        for r in range(1, n):
            if s[r] != s[l]:
                # NOTE !!!
                # `len == 1` still a valid group
                #  so DON'T add below logic:
                #   if r - 1 - l + 1 > 1: ..
                g_cnt.append(r - l)
                l = r


        # NOTE !!!! below
        # append the last group
        g_cnt.append(n - l)

        res = 0
        for i in range(1, len(g_cnt)):
            res += min(g_cnt[i - 1], g_cnt[i])

        return res


# V0-0-1
# IDEA :  Group By Character + continous sub-string (gpt)
"""
Core idea:

The key observation is to count the lengths of consecutive groups.

Example:

001110011

groups:
00   -> 2
111  -> 3
00   -> 2
11   -> 2

group lengths:
[2, 3, 2, 2]

Each adjacent pair contributes:

min(2,3) = 2
min(3,2) = 2
min(2,2) = 2

Total = 6

"""
class Solution(object):
    def countBinarySubstrings(self, s):
        """
        :type s: str
        :rtype: int
        """
        if len(s) <= 1:
            return 0

        prev = 0
        curr = 1
        ans = 0

        for i in range(1, len(s)):
            if s[i] == s[i - 1]:
                curr += 1
            else:
                ans += min(prev, curr)
                prev = curr
                curr = 1

        ans += min(prev, curr)

        return ans



# V0-1
# IDEA :  Group By Character + continous sub-string (GEMINI)
class Solution(object):
    def countBinarySubstrings(self, s):
        """
        :type s: str
        :rtype: int
        """
        if not s or len(s) <= 1:
            return 0
            
        ans = 0
        prev_run_length = 0
        curr_run_length = 1
        
        # Iterate starting from the second character
        for i in range(1, len(s)):
            # If the character is the same as the previous one, extend the current run
            if s[i] == s[i-1]:
                curr_run_length += 1
            else:
                # Character changed! We have a complete 'prev' block and a 'curr' block.
                # Add the number of valid substrings formed by the previous two blocks
                ans += min(prev_run_length, curr_run_length)
                
                # The current block now becomes the previous block, and we start a new run
                prev_run_length = curr_run_length
                curr_run_length = 1
                
        # Don't forget to add the substrings formed by the very last two blocks!
        ans += min(prev_run_length, curr_run_length)
        
        return ans



# V0 
# IDEA :  Group By Character + continous sub-string
# https://leetcode.com/problems/count-binary-substrings/solution/
# https://blog.csdn.net/fuxuemingzhu/article/details/79183556
# IDEA :
#   -> for x = “0110001111”, how many continuous "0" or "1"
#   -> [1,2,3,4]
#   -> So, if we want to find # of "equal 0 and 1 sub string"
#   -> all we need to do : min(3,4) = 3. e.g. ("01", "0011", "000111")
#   -> since for every "cross" sub string (e.g. 0 then 1 or 1 then 0),
#   -> we can the "number of same continuous 0 and 1"  by min(groups[i-1], groups[i])
# time = O(n)
# space = O(n)
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1

        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V0'
# IDEA : LINEAR SCAN
# # https://leetcode.com/problems/count-binary-substrings/solution/
# time = O(n)
# space = O(1)
class Solution(object):
    def countBinarySubstrings(self, s):
        ans, prev, cur = 0, 0, 1
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                ans += min(prev, cur)
                prev, cur = cur, 1
            else:
                cur += 1

        return ans + min(prev, cur)

# V0''
# IDEA : BRUTE FORCE (TLE)
# class Solution(object):
#     def countBinarySubstrings(self, s):
#         def check(x):
#             #print ("x = " + str(x))
#             _mid = len(x) // 2
#             if ("0" * _mid + "1" * _mid) == x or ("1" * _mid + "0" * _mid) == x:
#                 return True
#             else:
#                 return False  
#         # edge case
#         if not s:
#             return 0
#         #res = 0
#         res = []
#         for i in range(len(s)):
#             for j in range(i+1, len(s)):
#                 print ("s[i:j+1] = " + str(s[i:j+1]))
#                 if (j - i + 1) % 2 == 0:
#                     if check(s[i:j+1]):
#                         res.append(s[i:j+1])
#                         break
#         return res

# V1
# IDEA :  Group By Character
# https://leetcode.com/problems/count-binary-substrings/solution/
# time = O(n)
# space = O(n)
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1

        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V1'
# IDEA : Group By Character (Alternate Implentation as above (same idea))
# https://leetcode.com/problems/count-binary-substrings/solution/
# time = O(n)
# space = O(n)
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [len(list(v)) for _, v in itertools.groupby(s)]
        return sum(min(a, b) for a, b in zip(groups, groups[1:]))

# V1''
# IDEA :  Linear Scan
# https://leetcode.com/problems/count-binary-substrings/solution/
# time = O(n)
# space = O(1)
class Solution(object):
    def countBinarySubstrings(self, s):
        ans, prev, cur = 0, 0, 1
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                ans += min(prev, cur)
                prev, cur = cur, 1
            else:
                cur += 1

        return ans + min(prev, cur)

# V1'''
# https://leetcode.com/problems/count-binary-substrings/discuss/176153/Python-solution
# time = O(n)
# space = O(1)
class Solution(object):
    def countBinarySubstrings(self, s):
        res = 0
        prev = 0
        tmp = 1
        for i in range(1, len(s)):
            if s[i] != s[i-1]:
                res += min(prev, tmp)
                prev = tmp
                tmp = 1
            else:
                tmp += 1
        res += min(prev, tmp)
        return res

# V1''''
# https://blog.csdn.net/wenqiwenqi123/article/details/78462141
# https://blog.csdn.net/fuxuemingzhu/article/details/79183556
# time = O(n)
# space = O(n)
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1
 
        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def countBinarySubstrings(self, s):
        result, prev, curr = 0, 0, 1
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                result += min(prev, curr)
                prev, curr = curr, 1
            else:
                curr += 1
        result += min(prev, curr)
        return result